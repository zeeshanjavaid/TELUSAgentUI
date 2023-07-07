package com.fico.telus.utility;

public class URIConstant {

    public static final String COLLECTION_ENTITY_SERVICE_URL = "https://apigw-public-yul-np-002.cloudapps.telus.com/customer/collectionEntityMgmt/v1";
        public static final String COLLECTION_ENTITY_DATA_SERVICE_URL = "https://apigw-public-yul-np-002.cloudapps.telus.com/customer/collectionDataMgmt/v1";
            public static final String COLLECTION_TREATMENT_STEP_SERVICE_URL = "https://apigw-public-yul-np-002.cloudapps.telus.com/customer/collectionTreatmentMgmt/v1";


    

    public enum ApiMapping {;
        public static final String GET_ENTITY = "/entity";
        public static final String GET_PARR = "/paymentArrangement";
        public static final String ENTITY_SEARCH = "/entitySearch";
        public static final String ENTITY_DETAILS = "/entityDetails";
        public static final String GET_COLLECTION_TREATMENT = "/collectionTreatmentStep";
        public static final String GET_COLL_ACTIVITY_LOG = "/collectionActivityLog";
        public static final String GET_DISPUTE = "/dispute";
        public static final String GET_CONTACT = "/contact";
        public static final String GET_BILLING_ACCOUNT_REF = "/billingAccountRef";
        public static final String ENTITY_CONTACTS = "/entityContacts";
        public static final String ENTITY_BAN_DETAILS = "/entityBanDetails";


    }
}
