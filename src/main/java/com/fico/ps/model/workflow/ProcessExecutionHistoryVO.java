package com.fico.ps.model.workflow;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wavemaker.runtime.security.xss.XssDisable;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcessExecutionHistoryVO {

    private String transactionId;
    private ArrayList<Content> content;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public ArrayList<Content> getContent() {
        return content;
    }

    public void setContent(ArrayList<Content> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ProcessExecutionHistoryVO{" +
                "transactionId='" + transactionId + '\'' +
                ", content=" + content +
                '}';
    }
}
