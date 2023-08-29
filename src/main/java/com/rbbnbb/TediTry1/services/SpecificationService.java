package com.rbbnbb.TediTry1.services;

import com.rbbnbb.TediTry1.dto.SearchRequestDTO;
import com.rbbnbb.TediTry1.dto.SpecificationDTO;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SpecificationService<T> {

//    public Specification<T> getSearchSpecification(SearchRequestDTO searchRequestDTO){
//        return new Specification<T>() {
//            @Override
//            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
////                return criteriaBuilder.equal(root.get(searchRequestDTO.getColumn()),searchRequestDTO.getValue());
//            }
//        };
//    }

    public Specification<T> getSearchSpecification(SearchRequestDTO searchRequestDTO){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            List<SpecificationDTO> specificationDTOS = searchRequestDTO.getSpecificationList();
            for (SpecificationDTO specDTO: specificationDTOS) {
                switch(specDTO.getOperation()){
                    case EQUAL:
                        Predicate equal = criteriaBuilder.equal(root.get(specDTO.getColumn()),specDTO.getValue());
                        predicates.add(equal);
                        break;
                    case IN:
                        String[] values = specDTO.getValue().split(",");
                        Predicate in = root.get(specDTO.getColumn()).in(Arrays.asList(values));
                        predicates.add(in);
                        break;
                    case LIKE:
                        Predicate like = criteriaBuilder.like(root.get(specDTO.getColumn()),specDTO.getValue()+"%");
                        predicates.add(like);
                        break;
                    case BETWEEN:
                        break;
                    case LESS_THAN:
                        break;
                    case GREATER_THAN:
                        break;
                    default: throw new IllegalStateException("Invalid operator");
                }

            }
            if (searchRequestDTO.getGlobalOperator().equals(SearchRequestDTO.GlobalOperator.AND))
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            else return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }
}
