/*
 *  Use following services as dependency injection:
 * 'Utils' for Utility service
 * 'DialogService' for Dialog service
 * 'i18nService' for i18n service
 * 'SpinnerService' for Spinner service
 * 'ToasterService' for Toaster service
 * 'HttpService' for Http service
 * example: var utils = App.getDependency('Utils');
 */

/* perform any action on widgets/variables within this block */
Partial.onReady = function() {
    /*
     * variables can be accessed through 'Partial.Variables' property here
     * e.g. to get dataSet in a staticVariable named 'loggedInUser' use following script
     * Partial.Variables.loggedInUser.getData()
     *
     * widgets can be accessed through 'Partial.Widgets' property here
     * e.g. to get value of text widget named 'username' use following script
     * 'Partial.Widgets.username.datavalue'
     */
    App.showRowExpansionBanViewHome = function(row, data) {
        debugger;
        Partial.Widgets.acctStatus.caption = row.acctStatus;
        Partial.Widgets.statusDT.caption = row.acctStatusDate;
        Partial.Widgets.language.caption = row.language;
        Partial.Widgets.workCategory.caption = row.workCategory;
        Partial.Widgets.subPortfolio.caption = row.portfolioSubCategory;
        Partial.Widgets.province.caption = row.province;
        Partial.Widgets.CBU.caption = row.cbu;
        Partial.Widgets.CBUCIDName.caption = row.cbucidName;
        Partial.Widgets.RCIDname.caption = row.rcidName;
        Partial.Widgets.companyCode.caption = row.companyCode;
        Partial.Widgets.custRisk.caption = row.entityRisk;
        Partial.Widgets.custValue.caption = row.entityValue;
        Partial.Widgets.collStatus.caption = row.banCollectionStatus;
        Partial.Widgets.entType.caption = row.entityType;
        Partial.Widgets.FTNP.caption = row.FTNP;


        var getEntityDetailsForWorkCategoryVar = Partial.Variables.getEntityDetailsForWorkCategory;
        getEntityDetailsForWorkCategoryVar.invoke({
                "inputFields": {
                    "entityId": row.entityId,
                    "accountStatus": "ALL"
                },
            },
            function(data) {
                Partial.Widgets.workCategory.caption = data.entityDetails.primeWorkCategory;
            },
            function(error) {
                // Error Callback
                console.log("error", error);
            }
        );
    }
};