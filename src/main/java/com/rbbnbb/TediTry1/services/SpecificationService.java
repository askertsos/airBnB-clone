package com.rbbnbb.TediTry1.services;

import com.rbbnbb.TediTry1.dto.SearchRequestDTO;
import com.rbbnbb.TediTry1.dto.SpecificationDTO;
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
                switch(specDTO.getOperation()){
                    case EQUAL:
                        Predicate equal = criteriaBuilder.equal(root.get(specDTO.getColumn()),specDTO.getValue());
                        predicates.add(equal);
                        break;

                    case IN:
                        String[] inValues = specDTO.getValue().split(",");
                        Predicate in = root.get(specDTO.getColumn()).in(Arrays.asList(inValues));
                        predicates.add(in);
                        break;

                    case LIKE:
                        Predicate like = criteriaBuilder.like(root.get(specDTO.getColumn()),specDTO.getValue()+"%");
                        predicates.add(like);
                        break;

                    case BETWEEN:
                        String[] betweenValues = specDTO.getValue().split(",");
                        if (betweenValues.length != 2){
                            throw new IllegalArgumentException("Between call must have 2 comma-separated values");
                        }
                        Predicate between = criteriaBuilder.between(root.get(specDTO.getColumn()),betweenValues[0],betweenValues[1]);
                        predicates.add(between);
                        break;

                    case GREATER_THAN:
                        Predicate greaterThan = criteriaBuilder.greaterThan(root.get(specDTO.getColumn()),specDTO.getValue());
                        predicates.add(greaterThan);
                        break;

                    case GREATER_OR_EQUAL:
                        Predicate greaterThanOrEqualTo = criteriaBuilder.greaterThanOrEqualTo(root.get(specDTO.getColumn()),specDTO.getValue());
                        predicates.add(greaterThanOrEqualTo);
                        break;

                    case LESS_THAN:
                        Predicate lessThan = criteriaBuilder.lessThan(root.get(specDTO.getColumn()),specDTO.getValue());
                        predicates.add(lessThan);
                        break;

                    case JOIN:
                        Predicate join = criteriaBuilder.equal(root.join(specDTO.getJoinTable()).get(specDTO.getColumn()),specDTO.getValue());
                        predicates.add(join);
                        break;

                    case DATES:
                        //Get the start date and end date
                        String[] stringDates = specDTO.getValue().split(",");
                        if (stringDates.length != 2){
                            throw new IllegalArgumentException("Dates call must consist of start date and end date");
                        }
                        LocalDate startDate = LocalDate.parse(stringDates[0],formatter);
                        LocalDate endDate = LocalDate.parse(stringDates[1],formatter);
                        List<LocalDate> dateList = startDate.datesUntil(endDate.plusDays(1L)).toList();

                        for (LocalDate date: dateList) {
                            predicates.add(criteriaBuilder.isMember(date,root.get("availableDates")));
                        }
//
//                        //Convert them to LocalDate and insert them into a list
//                        List<LocalDate> datesList = new ArrayList<>();
//                        List<Predicate> predicateList = new ArrayList<>();
//                        for (String stringDate: stringDates) {
//                                LocalDate localDate = LocalDate.parse(stringDate, formatter);
//                                datesList.add(localDate);
//                                predicates.add(criteriaBuilder.isMember(localDate,root.get("availableDates")));
//
//                        }
                        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

                        //Every element of the list must be in the "availableDates" field
//                        for (LocalDate localDate: datesList) {
//                            predicates.add(root.get("availableDates").in(localDate));
//                        }
//                        System.out.println("before root.get.in");
//                        Predicate date = root.get("availableDates").in(datesList);
//                        predicates.add(date);

//                        Predicate isMember = criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
//                        predicates.add(isMember);






//                        for (LocalDate availableDate: root.get("availableDates")){
//
//                        }


//                        LocalDate[] dateArray = new LocalDate[datesList.size()];
//                        dateArray = datesList.toArray(new LocalDate[0]);
//                        CriteriaBuilder.In<Rental> inClause = criteriaBuilder.in(root.get("availableDates"));
//                        for (LocalDate date: datesList) {
//                            inClause.value(date);
//                        }
//                        query.select(root).where(inClause);
                    case AMENITIES:
                    case BOOLEAN:
                        boolean boolValue = Boolean.parseBoolean(specDTO.getValue());
                        Predicate bool = criteriaBuilder.equal(root.get(specDTO.getColumn()),boolValue);
                        predicates.add(bool);
                        break;

                    default: throw new IllegalStateException("Invalid operator");
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