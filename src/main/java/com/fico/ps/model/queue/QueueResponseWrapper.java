package com.fico.ps.model.queue;

import java.util.List;

public class QueueResponseWrapper {

	private Integer pageNumber;
	private Integer pageSize;
	private Long totalRecords;
	private List<QueueResponse> queueData;
	
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
	public List<QueueResponse> getQueueData() {
		return queueData;
	}
	public void setQueueData(List<QueueResponse> queueData) {
		this.queueData = queueData;
	}
	
}
