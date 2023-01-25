package com.fico.ps.model.workflow;

public class ServiceData {

    private String requestIdentifier;
    private String responseIdentifier;
    private String responseContentType;

    public String getRequestIdentifier() {
        return requestIdentifier;
    }

    public void setRequestIdentifier(String requestIdentifier) {
        this.requestIdentifier = requestIdentifier;
    }

    public String getResponseIdentifier() {
        return responseIdentifier;
    }

    public void setResponseIdentifier(String responseIdentifier) {
        this.responseIdentifier = responseIdentifier;
    }

    public String getResponseContentType() {
        return responseContentType;
    }

    public void setResponseContentType(String responseContentType) {
        this.responseContentType = responseContentType;
    }

    @Override
    public String toString() {
        return "ServiceData{" +
                "requestIdentifier='" + requestIdentifier + '\'' +
                ", responseIdentifier='" + responseIdentifier + '\'' +
                ", responseContentType='" + responseContentType + '\'' +
                '}';
    }
}
