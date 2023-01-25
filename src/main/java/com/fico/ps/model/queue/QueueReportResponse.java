package com.fico.ps.model.queue;

/**
 * @author AnubhavDas
 * 
 * @implSpec
 * Model class for storing the Queue report response
 */
public class QueueReportResponse {

	private Integer id;
	private String reportId;
	private String queueName;
	private Long period_0 = (long) 0;
	private Long period_1 = (long) 0;
	private Long period_2 = (long) 0;
	private Long period_3 = (long) 0;
	private Long period_4 = (long) 0;
	private Long period_5 = (long) 0;
	private Long period_6_10 = (long) 0;
	private Long period_10_plus = (long) 0;
	private Long totalApps = (long) 0;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public Long getPeriod_0() {
		return period_0;
	}
	public void setPeriod_0(Long period_0) {
		this.period_0 = period_0;
	}
	public Long getPeriod_1() {
		return period_1;
	}
	public void setPeriod_1(Long period_1) {
		this.period_1 = period_1;
	}
	public Long getPeriod_2() {
		return period_2;
	}
	public void setPeriod_2(Long period_2) {
		this.period_2 = period_2;
	}
	public Long getPeriod_3() {
		return period_3;
	}
	public void setPeriod_3(Long period_3) {
		this.period_3 = period_3;
	}
	public Long getPeriod_4() {
		return period_4;
	}
	public void setPeriod_4(Long period_4) {
		this.period_4 = period_4;
	}
	public Long getPeriod_5() {
		return period_5;
	}
	public void setPeriod_5(Long period_5) {
		this.period_5 = period_5;
	}
	public Long getPeriod_6_10() {
		return period_6_10;
	}
	public void setPeriod_6_10(Long period_6_10) {
		this.period_6_10 = period_6_10;
	}
	public Long getPeriod_10_plus() {
		return period_10_plus;
	}
	public void setPeriod_10_plus(Long period_10_plus) {
		this.period_10_plus = period_10_plus;
	}
	public String getQueueName() {
		return queueName;
	}
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	public Long getTotalApps() {
		return totalApps;
	}
	public void setTotalApps(Long totalApps) {
		this.totalApps = totalApps;
	}
	
}
