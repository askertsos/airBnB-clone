package com.rbbnbb.TediTry1.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;

public class PageRequestDTO {

    private Integer pageNo = 0;

    private Integer pageSize = 10;

    private Sort.Direction sort = Sort.Direction.ASC;

    private String sortByColumn = "id";

    public PageRequestDTO(){}

    public Pageable getPageable(PageRequestDTO dto){
        Integer pageNum = Objects.nonNull(dto.getPageNo()) ? dto.getPageNo() : this.pageNo;
        Integer pageSz = Objects.nonNull(dto.getPageSize()) ? dto.getPageSize() : this.pageSize;
        Sort.Direction sortType = Objects.nonNull(dto.getSort()) ? dto.getSort() : this.sort;
        String sortColumn = Objects.nonNull(dto.getSortByColumn()) ? dto.getSortByColumn() : this.sortByColumn;

        PageRequest pageRequest = PageRequest.of(pageNum,pageSz,sortType,sortColumn);

        return pageRequest;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Sort.Direction getSort() {
        return sort;
    }

    public void setSort(Sort.Direction sort) {
        this.sort = sort;
    }

    public String getSortByColumn() {
        return sortByColumn;
    }

    public void setSortByColumn(String sortByColumn) {
        this.sortByColumn = sortByColumn;
    }
}