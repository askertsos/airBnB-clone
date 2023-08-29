package com.rbbnbb.TediTry1.dto;

import java.util.List;

public class SearchRequestDTO {
    public enum GlobalOperator {AND,OR};

    private GlobalOperator globalOperator;

    private List<SpecificationDTO> specificationList;

    public GlobalOperator getGlobalOperator() {
        return globalOperator;
    }

    public void setGlobalOperator(GlobalOperator globalOperator) {
        this.globalOperator = globalOperator;
    }

    public List<SpecificationDTO> getSpecificationList() {
        return specificationList;
    }

    public void setSpecificationList(List<SpecificationDTO> specificationList) {
        this.specificationList = specificationList;
    }
}
