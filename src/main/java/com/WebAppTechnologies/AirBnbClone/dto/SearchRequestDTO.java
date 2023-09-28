package com.WebAppTechnologies.AirBnbClone.dto;

import java.util.List;

public class SearchRequestDTO {
    public enum GlobalOperator {AND,OR};

    private String jwt;
    private GlobalOperator globalOperator;

    private List<SpecificationDTO> specificationList;

    private PageRequestDTO pageRequestDTO;

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

    public PageRequestDTO getPageRequestDTO() {
        return pageRequestDTO;
    }

    public void setPageRequestDTO(PageRequestDTO pageRequestDTO) {
        this.pageRequestDTO = pageRequestDTO;
    }
}
