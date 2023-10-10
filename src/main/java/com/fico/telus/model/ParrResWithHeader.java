package com.fico.telus.model;

import io.swagger.client.model.CollectionPaymentArrangement;

import java.util.List;

public class ParrResWithHeader {

    private Integer totalNumberOfElement;

    private List<CollectionPaymentArrangement> responseObjectList;

      public Integer getTotalNumberOfElement() {
        return totalNumberOfElement;
    }

    public void setTotalNumberOfElement(Integer totalNumberOfElement) {
        this.totalNumberOfElement = totalNumberOfElement;
    }

    public List<CollectionPaymentArrangement> getResponseObjectList() {
        return responseObjectList;
    }

    public void setResponseObjectList(List<CollectionPaymentArrangement> responseObjectList) {
        this.responseObjectList = responseObjectList;
    }
}
