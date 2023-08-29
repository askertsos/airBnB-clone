package com.rbbnbb.TediTry1.dto;

public class SpecificationDTO {

    public enum Operation{
        EQUAL, IN, LIKE, BETWEEN, GREATER_THAN, LESS_THAN
    }
    private String column;
    private String value;
    private Operation operation;

    public SpecificationDTO() {}

    public SpecificationDTO(String column, String value, Operation operation) {
        this.column = column;
        this.value = value;
        this.operation = operation;
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
}
