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
var creationDateTime;
var completionDateTime;
Page.onReady = function() {
    debugger;
    /*
     * variables can be accessed through 'Page.Variables' property here
     * e.g. to get dataSet in a staticVariable named 'loggedInUser' use following script
     * Page.Variables.loggedInUser.getData()
     *
     * widgets can be accessed through 'Page.Widgets' property here
     * e.g. to get value of text widget named 'username' use following script
     * 'Page.Widgets.username.datavalue'
     */

    const date = new Date();
    var currentDate = DateCalc(date); //created to
    var prvDateMonth = new Date(new Date().getFullYear(), new Date().getMonth() - 3, new Date().getDate());
    var previousDate = DateCalc(prvDateMonth); //created from


    function DateCalc(date) {
        var day = String(date.getDate()).padStart(2, '0');
        var month = String(date.getMonth() + 1).padStart(2, "0");
        var year = date.getFullYear();

        var calculatedDate = year + '-' + month + '-' + day;
        return calculatedDate;
    }



    var timezoneOffset = date.getTimezoneOffset();
    var absTimezoneOffset = Math.abs(timezoneOffset);

    if (timezoneOffset < 0) {
        var currentTimeZone = 'Z';
        completionDateTime = currentDate + "T" + "00" + ":" + "00" + ":" + "00" + ".00000" + currentTimeZone;
        creationDateTime = previousDate + "T" + "00" + ":" + "00" + ":" + "00" + ".00000" + currentTimeZone;
    } else {
        var currentTimeZone = "-" + ("00" + Math.floor(absTimezoneOffset / 60)).slice(-2) + ":" + ("00" + (absTimezoneOffset % 60)).slice(-2);
        completionDateTime = currentDate + "T" + "00" + ":" + "00" + ":" + "00" + ".00000" + currentTimeZone;
        creationDateTime = previousDate + "T" + "00" + ":" + "00" + ":" + "00" + ".00000" + currentTimeZone;
    }

    Page.Variables.ParrReportServiceGetParrReport.setInput({
        'entityRisk': '',
        'evaluation': '',
        'status': '',
        'createdFrom': creationDateTime,
        'createdTo': completionDateTime,
        'createdBy': ''
    });

    Page.Variables.ParrReportServiceGetParrReport.invoke();

};

function messageTimeout() {
    Page.Variables.errorMsg.dataSet.dataValue = null;
}


Page.button1Click = function($event, widget) {

    debugger;

    Page.Widgets.entityRiskSelect.datavalue = "";

    Page.Widgets.parrStatusSelect.datavalue = "";
    Page.Widgets.evalSelect.datavalue = "";

    Page.Widgets.createdBySelect.datavalue = "";

    Page.Widgets.createdTeamSelect.datavalue = "";

    Page.Widgets.creationDate.bsDataValue = "";

    Page.Widgets.completionDate.bsDataValue = "";

    Page.Widgets.createdBySelect.disabled = false;
    Page.Widgets.createdTeamSelect.disabled = false;

    Page.Variables.ParrReportServiceGetParrReport.setInput({
        'entityRisk': '',
        'evaluation': '',
        'status': '',
        'createdFrom': '',
        'createdTo': '',
        'createdBy': ''
    });

    Page.Variables.ParrReportServiceGetParrReport.invoke();

};
Page.button2Click = function($event, widget) {
    debugger;
    var fromDate = Page.Widgets.creationDate.bsDataValue;
    var toDate = Page.Widgets.completionDate.bsDataValue;

    var createdByReq = null;
    var evaluationReq;
    //ToCalculate Timezone
    if (Page.Widgets.completionDate.bsDataValue != undefined && Page.Widgets.completionDate.bsDataValue != '') {
        var timezoneOffset = Page.Widgets.completionDate.bsDataValue.getTimezoneOffset();

        var absTimezoneOffset = Math.abs(timezoneOffset);

        if (timezoneOffset < 0) {
            var currentTimeZone = 'Z';
            var datePart = Page.Widgets.completionDate.datavalue;
            var completionDateTime = datePart + "T" + "00" + ":" + "00" + ":" + "00" + ".00000" + currentTimeZone;
        } else {
            var currentTimeZone = "-" + ("00" + Math.floor(absTimezoneOffset / 60)).slice(-2) + ":" + ("00" + (absTimezoneOffset % 60)).slice(-2);
            var datePart = Page.Widgets.completionDate.datavalue;
            var completionDateTime = datePart + "T" + "00" + ":" + "00" + ":" + "00" + ".00000" + currentTimeZone;
        }

    }

    if (Page.Widgets.creationDate.bsDataValue != undefined && Page.Widgets.creationDate.bsDataValue != '') {
        var timezoneOffset = Page.Widgets.creationDate.bsDataValue.getTimezoneOffset();
        var absTimezoneOffset = Math.abs(timezoneOffset);

        if (timezoneOffset < 0) {
            var currentTimeZone = 'Z';
            var datePart = Page.Widgets.creationDate.datavalue;
            var creationDateTime = datePart + "T" + "00" + ":" + "00" + ":" + "00" + ".00000" + currentTimeZone;
        } else {
            var currentTimeZone = "-" + ("00" + Math.floor(absTimezoneOffset / 60)).slice(-2) + ":" + ("00" + (absTimezoneOffset % 60)).slice(-2);
            var datePart = Page.Widgets.creationDate.datavalue;
            var creationDateTime = datePart + "T" + "00" + ":" + "00" + ":" + "00" + ".00000" + currentTimeZone;
        }


    }

    /*var createdBy = Page.Widgets.createdTeamSelect.datavalue;*/
    if (Page.Widgets.createdBySelect.datavalue != '' && Page.Widgets.createdBySelect.datavalue != undefined) {
        createdByReq = Page.Widgets.createdBySelect.datavalue;
    } else if (Page.Widgets.createdTeamSelect.datavalue != '' && Page.Widgets.createdTeamSelect.datavalue != undefined) {
        var arrEmpId = [];
        Page.Variables.getUserListByTeamId_Report.dataSet.forEach(function(d) {
            arrEmpId.push(d.empId)
        });

        createdByReq = arrEmpId.toString();
    }


    if (Page.Widgets.evalSelect.datavalue == 'All') {
        evaluationReq = '';
    } else {
        evaluationReq = Page.Widgets.evalSelect.datavalue;
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

    var entityRiskReq;

    if (Page.Widgets.entityRiskSelect.datavalue == 'High') {
        entityRiskReq = 'H';
    } else if (Page.Widgets.entityRiskSelect.datavalue == 'Medium') {
        entityRiskReq = 'M';
    } else if (Page.Widgets.entityRiskSelect.datavalue == 'Low') {
        entityRiskReq = 'L';
    }

    Page.Variables.ParrReportServiceGetParrReport.setInput({
        'entityRisk': entityRiskReq,
        'evaluation': evaluationReq,
        'status': Page.Widgets.parrStatusSelect.datavalue,
        'createdFrom': creationDateTime,
        'createdTo': completionDateTime,
        'createdBy': createdByReq
    });
    Page.Variables.ParrReportServiceGetParrReport.invoke();

};

Page.openPARRdetailsDailog = function($event, widget, row) {
    debugger;
    Page.Variables.getParrHistoryViewForParrReport.setInput({
        'collectionEntityId': row.entityId,
        'relatedBusinessEntityId': row.parrId,
        'relatedBusinessEntityType': 'CollectionPaymentArrangement'

    });
    Page.Variables.getParrHistoryViewForParrReport.invoke();
    var getPaymentArrangementVar = Page.Variables.getPaymentArrangement_parrReports;

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
                    debugger;
                    Page.Variables.billingAccountIdNameListVar_parrReports.dataSet = [];

                    data.forEach(function(d) {

                        Page.billingAccountRefIdAndNameArr = {
                            "billingAccountId": d.billingAccountId,
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
Page.createdTeamSelectChange = function($event, widget, newVal, oldVal) {
    debugger;



    if (newVal != "") {
        Page.Widgets.createdBySelect.disabled = true;
    } else {
        Page.Widgets.createdBySelect.disabled = false;
    }

    if (Page.Widgets.createdTeamSelect.datavalue) {

        Page.Variables.getUserListByTeamId_Report.setInput({
            'teamId': Page.Widgets.createdTeamSelect.datavalue
        });
        Page.Variables.getUserListByTeamId_Report.invoke();

    }
};



Page.createdBySelectChange = function($event, widget, newVal, oldVal) {
    debugger;

    if (newVal != "") {
        Page.Widgets.createdTeamSelect.disabled = true;
    } else {
        Page.Widgets.createdTeamSelect.disabled = false;
    }
};