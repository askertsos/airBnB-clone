package com.WebAppTechnologies.AirBnbClone.services;

import com.WebAppTechnologies.AirBnbClone.dto.SearchRequestDTO;
import com.WebAppTechnologies.AirBnbClone.dto.SpecificationDTO;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class SpecificationService<T> {

    @Autowired
    private DateTimeFormatter formatter;

    public Specification<T> getSearchSpecification(SearchRequestDTO searchRequestDTO){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            List<SpecificationDTO> specificationDTOS = searchRequestDTO.getSpecificationList();
            for (SpecificationDTO specDTO: specificationDTOS) {
                switch (specDTO.getOperation()) {
                    case EQUAL -> {
                        Predicate equal = criteriaBuilder.equal(root.get(specDTO.getColumn()), specDTO.getValue());
                        predicates.add(equal);
                    }
                    case IN -> {
                        String[] inValues = specDTO.getValue().split(",");
                        Predicate in = root.get(specDTO.getColumn()).in(Arrays.asList(inValues));
                        predicates.add(in);
                    }
                    case LIKE -> {
                        Predicate like = criteriaBuilder.like(root.get(specDTO.getColumn()), specDTO.getValue() + "%");
                        predicates.add(like);
                    }
                    case BETWEEN -> {
                        String[] betweenValues = specDTO.getValue().split(",");
                        if (betweenValues.length != 2) {
                            throw new IllegalArgumentException("Between call must have 2 comma-separated values");
                        }
                        Predicate between = criteriaBuilder.between(root.get(specDTO.getColumn()), betweenValues[0], betweenValues[1]);
                        predicates.add(between);
                    }
                    case GREATER_THAN -> {
                        Predicate greaterThan = criteriaBuilder.greaterThan(root.get(specDTO.getColumn()), specDTO.getValue());
                        predicates.add(greaterThan);
                    }
                    case GREATER_OR_EQUAL -> {
                        Predicate greaterThanOrEqualTo = criteriaBuilder.greaterThanOrEqualTo(root.get(specDTO.getColumn()), specDTO.getValue());
                        predicates.add(greaterThanOrEqualTo);
                    }
                    case LESS_THAN -> {
                        Predicate lessThan = criteriaBuilder.lessThan(root.get(specDTO.getColumn()), specDTO.getValue());
                        predicates.add(lessThan);
                    }
                    case LESS_OR_EQUAL -> {
                        Predicate lessThanOrEqualTo = criteriaBuilder.lessThanOrEqualTo(root.get(specDTO.getColumn()), specDTO.getValue());
                        predicates.add(lessThanOrEqualTo);
                    }
                    case JOIN -> {
                        Predicate join = criteriaBuilder.equal(root.join(specDTO.getJoinTable()).get(specDTO.getColumn()), specDTO.getValue());
                        predicates.add(join);
                    }
                    case DATES -> {
                        String[] stringDates = specDTO.getValue().split(",");
                        //Convert them to LocalDate and insert them into a list
                        for (String stringDate : stringDates) {
                            LocalDate localDate = LocalDate.parse(stringDate, formatter);
                            predicates.add(criteriaBuilder.isMember(localDate, root.get("availableDates")));
                        }
                        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                    }
                    case AMENITIES, BOOLEAN -> {
                        boolean boolValue = Boolean.parseBoolean(specDTO.getValue());
                        Predicate bool = criteriaBuilder.equal(root.get(specDTO.getColumn()), boolValue);
                        predicates.add(bool);
                    }
                    default -> throw new IllegalStateException("Invalid operator");
                }

            }
            if (Objects.isNull(searchRequestDTO.getGlobalOperator())){
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
            if (searchRequestDTO.getGlobalOperator().equals(SearchRequestDTO.GlobalOperator.OR))
                return criteriaBuilder.or(predicates.toArray(new Predicate[0]));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}