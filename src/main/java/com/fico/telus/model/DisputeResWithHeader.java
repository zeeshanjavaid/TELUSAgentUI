package com.fico.telus.model;

//import io.swagger.client.model.CollectionDispute;
//import io.swagger.client.model.CollectionPaymentArrangement;

import telus.cdo.cnc.collmgmt.collentitymgmt.model.CollectionDispute;

import java.util.List;

public class DisputeResWithHeader {

    private Integer totalNumberOfElement;

    private List<CollectionDispute> responseObjectList;


    public Integer getTotalNumberOfElement() {
        return totalNumberOfElement;
    }

    public void setTotalNumberOfElement(Integer totalNumberOfElement) {
        this.totalNumberOfElement = totalNumberOfElement;
    }

    public List<CollectionDispute> getResponseObjectList() {
        return responseObjectList;
    }

    public void setResponseObjectList(List<CollectionDispute> responseObjectList) {
        this.responseObjectList = responseObjectList;
    }
}
