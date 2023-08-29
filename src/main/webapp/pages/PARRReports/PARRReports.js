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
Page.onReady = function() {
    /*
     * variables can be accessed through 'Page.Variables' property here
     * e.g. to get dataSet in a staticVariable named 'loggedInUser' use following script
     * Page.Variables.loggedInUser.getData()
     *
     * widgets can be accessed through 'Page.Widgets' property here
     * e.g. to get value of text widget named 'username' use following script
     * 'Page.Widgets.username.datavalue'
     */

};

function messageTimeout() {
    Page.Variables.errorMsg.dataSet.dataValue = null;
}


Page.button1Click = function($event, widget) {


    Page.Widgets.entityRiskSelect.datavalue = "";

    Page.Widgets.parrStatusSelect.datavalue = "";
    Page.Widgets.evalSelect.datavalue = "";

    Page.Widgets.createdBySelect.datavalue = "";

    Page.Widgets.createdTeamSelect.datavalue = "";

    Page.Widgets.creationDate.bsDataValue = "";

    Page.Widgets.completionDate.bsDataValue = "";

    Page.Variables.ParrReportServiceGetParrReport.invoke();

};
Page.button2Click = function($event, widget) {
    debugger;
    var fromDate = Page.Widgets.creationDate.bsDataValue;
    var toDate = Page.Widgets.completionDate.bsDataValue;

    //ToCalculate Timezone
    if (Page.Widgets.completionDate.bsDataValue != undefined && Page.Widgets.completionDate.bsDataValue != '') {
        var timezoneOffset = Page.Widgets.completionDate.bsDataValue.getTimezoneOffset();
        var absTimezoneOffset = Math.abs(timezoneOffset);
        var currentTimeZone = (timezoneOffset < 0 ? "+" : "-") + ("00" + Math.floor(absTimezoneOffset / 60)).slice(-2) + ":" + ("00" + (absTimezoneOffset % 60)).slice(-2);

        var hours = ("00" + Page.Widgets.completionDate.bsDataValue.getHours()).slice(-2);
        var minutes = ("00" + Page.Widgets.completionDate.bsDataValue.getMinutes()).slice(-2);
        var seconds = ("00" + Page.Widgets.completionDate.bsDataValue.getSeconds()).slice(-2);
        var datePart = Page.Widgets.completionDate.datavalue;

        var completionDateTime = datePart + "T" + hours + ":" + minutes + ":" + seconds + ".000" + currentTimeZone;

        alert(completionDateTime);


    }



    var fromDateMonth = new Date(Page.Widgets.creationDate.bsDataValue).getMonth();
    var toDateMonth = new Date(Page.Widgets.completionDate.bsDataValue).getMonth();

    var fromDate = new Date(Page.Widgets.creationDate.bsDataValue).getDate();
    var toDate = new Date(Page.Widgets.completionDate.bsDataValue).getDate();


    if (toDateMonth < fromDateMonth) {
        Page.Variables.errorMsg.dataSet.dataValue = "Completion date can not be less than Creation date";
    } else if (fromDateMonth >= toDateMonth) {
        debugger;
        if (toDate < fromDate) {
            Page.Variables.errorMsg.dataSet.dataValue = "Completion date can not be less than Creation date";
        }
    }

    setTimeout(messageTimeout, 10000);
    Page.Variables.ParrReportServiceGetParrReport.invoke();

};

Page.openPARRdetailsDailog = function($event, widget, row) {
    debugger;
    Page.Variables.getPaymentArrangement_parrReports.setInput({
        "id": row.parrId
    });
    Page.Variables.getPaymentArrangement_parrReports.invoke();
    //Page.Variables.getPaymentArrangement_parrReports.dataSet;


    var getPaymentArrangementVar = Page.Variables.getPaymentArrangement_parrReports;

    /* Partial.Variables.getPaymentArrangement.setInput({
         "id": Partial.pageParams.ParrId
     });
     Partial.Variables.getPaymentArrangement.invoke(); */

    getPaymentArrangementVar.invoke({
            "inputFields": {
                "id": row.parrId
            },
        },
        function(data) {

            var billingAccountRefIds = data.billingAccountRefs;
            var billingAccountRefIdArray = [];
            billingAccountRefIds.forEach(function(d) {
                billingAccountRefIdArray.push(d.id);
            });
            // console.log("success", "Inside Success block");

            billingAccountRefIdArray.join(",");

            var getBillingAccountNameIdVariable = Page.Variables.getBillingAccountNameIdVar_parrReports;
            getBillingAccountNameIdVariable.invoke({
                    "inputFields": {
                        "billingAccountRefIds": billingAccountRefIdArray
                    }
                },
                function(data) {
                    // billingAccountIdNameListVar
                    Page.Variables.billingAccountIdNameListVar_parrReports.dataSet = [];

                    data.forEach(function(d) {

                        Page.billingAccountRefIdAndNameArr = {
                            "billingAccountId": data.billingAccountId,
                            "billingAccountName": d.billingAccountName
                        }


                        Page.Variables.billingAccountIdNameListVar_parrReports.dataSet.push(Page.billingAccountRefIdAndNameArr);
                    });
                },
                function(error) {
                    // Error Callback
                    console.log("error", error);
                });


        },
        function(error) {
            // Error Callback
            console.log("error", error);
        }
    );


    Page.Widgets.PARRdetailsDailog.open();
}