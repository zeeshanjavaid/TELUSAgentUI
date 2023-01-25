package com.fico.ps.model.workflow;

import java.util.Date;
import java.util.List;

public class SubprocessStep {

    private String id;
    private String name;
    private String type;
    private Date start;
    private Date end;
    private int duration;
    private String status;
    private String user;
    private String transactionHistoryText;
    private ServiceData serviceData;
    private List<SubprocessStep> subprocessSteps;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTransactionHistoryText() {
        return transactionHistoryText;
    }

    public void setTransactionHistoryText(String transactionHistoryText) {
        this.transactionHistoryText = transactionHistoryText;
    }

    public ServiceData getServiceData() {
        return serviceData;
    }

    public void setServiceData(ServiceData serviceData) {
        this.serviceData = serviceData;
    }

    public List<SubprocessStep> getSubprocessSteps() {
		return subprocessSteps;
	}

	public void setSubprocessSteps(List<SubprocessStep> subprocessSteps) {
		this.subprocessSteps = subprocessSteps;
	}

    @Override
    public String toString() {
        return "SubprocessStep{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", duration=" + duration +
                ", status='" + status + '\'' +
                ", user='" + user + '\'' +
                ", transactionHistoryText='" + transactionHistoryText + '\'' +
                ", serviceData=" + serviceData +
                '}';
    }
}
