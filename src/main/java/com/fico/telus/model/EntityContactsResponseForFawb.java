package com.fico.telus.model;

import telus.cdo.cnc.collmgmt.colldatamgmt.model.EntityDigitalContact;
import telus.cdo.cnc.collmgmt.colldatamgmt.model.EntityMailingContact;

import java.util.List;

public class EntityContactsResponseForFawb {


    private List<EntityMailingContact> mailingContacts = null;

    private List<EntityDigitalContact> digitalContacts = null;
    private Integer totalNoOfElement=null;


    public List<EntityMailingContact> getMailingContacts() {
        return mailingContacts;
    }

    public void setMailingContacts(List<EntityMailingContact> mailingContacts) {
        this.mailingContacts = mailingContacts;
    }

    public List<EntityDigitalContact> getDigitalContacts() {
        return digitalContacts;
    }

    public void setDigitalContacts(List<EntityDigitalContact> digitalContacts) {
        this.digitalContacts = digitalContacts;
    }

    public Integer getTotalNoOfElement() {
        return totalNoOfElement;
    }

    public void setTotalNoOfElement(Integer totalNoOfElement) {
        this.totalNoOfElement = totalNoOfElement;
    }
}
