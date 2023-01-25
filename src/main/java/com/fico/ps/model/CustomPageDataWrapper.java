package com.fico.ps.model;

import java.util.List;

public class CustomPageDataWrapper<T> {
	Integer pageNumber;
	Integer pageSize;
	Long totalRecords;
	List<T> pageContent;
	
	public Integer getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Long getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(Long totalRecords) {
		this.totalRecords = totalRecords;
	}
	public List<T> getPageContent() {
		return pageContent;
	}
	public void setPageContent(List<T> pageContent) {
		this.pageContent = pageContent;
	}
	
	
}