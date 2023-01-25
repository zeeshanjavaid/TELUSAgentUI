package com.fico.ps.model.queue;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author AnubhavDas
 *
 * @implSpec
 * Wrapper class for wrapping various aspects of Queue report data to be presented in UI
 */
public class QueueReportResponseWrapper {

	private String reportId;
	private String reportStatusCode;
	private Timestamp dataAsOf;
	private String barChartJSON;
	private String pieChartJSON;
	private List<QueueReportResponse> reportData;
	
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getReportStatusCode() {
		return reportStatusCode;
	}
	public void setReportStatusCode(String reportStatusCode) {
		this.reportStatusCode = reportStatusCode;
	}
	public Timestamp getDataAsOf() {
		return dataAsOf;
	}
	public void setDataAsOf(Timestamp dataAsOf) {
		this.dataAsOf = dataAsOf;
	}
	public String getBarChartJSON() {
		return barChartJSON;
	}
	public void setBarChartJSON(String barChartJSON) {
		this.barChartJSON = barChartJSON;
	}
	public String getPieChartJSON() {
		return pieChartJSON;
	}
	public void setPieChartJSON(String pieChartJSON) {
		this.pieChartJSON = pieChartJSON;
	}
	public List<QueueReportResponse> getReportData() {
		return reportData;
	}
	public void setReportData(List<QueueReportResponse> reportData) {
		this.reportData = reportData;
	}
	
}
