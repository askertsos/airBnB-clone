package com.rbbnbb.TediTry1.dto;

public class SpecificationDTO {

    public enum Operation{
        EQUAL, IN, LIKE, BETWEEN, GREATER_THAN, GREATER_OR_EQUAL, LESS_THAN, LESS_OR_EQUAL, JOIN, DATES, AMENITIES, BOOLEAN
    }
    private String column;
    //private Double valueDouble, private Integer valueInteger, private List<String> valueStringList, k.o.k
    private String value;
    private Operation operation;

    private String joinTable;

    public SpecificationDTO() {}

    public SpecificationDTO(String column, String value, Operation operation, String joinTable) {
        this.column = column;
        this.value = value;
        this.operation = operation;
        this.joinTable = joinTable;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public String getJoinTable() {
        return joinTable;
    }

    public void setJoinTable(String joinTable) {
        this.joinTable = joinTable;
    }
}
