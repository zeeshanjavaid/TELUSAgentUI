package com.fico.telus.model;

import telus.cdo.cnc.collmgmt.colldatamgmt.model.EntityCustomerContact;
import telus.cdo.cnc.collmgmt.colldatamgmt.model.EntityCollectionContact;
import telus.cdo.cnc.collmgmt.colldatamgmt.model.EntityMailingContact;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class EntityContactsResponseForFawb {


    private List<EntityMailingContact> mailingContacts = null;
    
    @JsonProperty("digitalCustomerContacts")
    private List<EntityCustomerContact> customerContacts = null;
    
    @JsonProperty("digitalCollectionContacts")
    private List<EntityCollectionContact> collectionContacts = null;
    private Integer totalNoOfElement=null;


    public List<EntityMailingContact> getMailingContacts() {
        return mailingContacts;
    }

    public List<EntityCollectionContact> getCollectionContacts() {
        return collectionContacts;
    }
    public List<EntityCustomerContact> getCustomerContacts() {
        return customerContacts;
    }
    public void setMailingContacts(List<EntityMailingContact> mailingContacts) {
        this.mailingContacts = mailingContacts;
    }

    public void setCollectionContacts(List<EntityCollectionContact> collectionContacts) {
        this.collectionContacts = collectionContacts;
    }
    public void setCustomerContacts(List<EntityCustomerContact> customerContacts) {
        this.customerContacts = customerContacts;
    }
    public Integer getTotalNoOfElement() {
        return totalNoOfElement;
    }

    public void setTotalNoOfElement(Integer totalNoOfElement) {
        this.totalNoOfElement = totalNoOfElement;
    }
}
