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
debugger;
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
    //alert("ParrId: " + Partial.Variables.getPaymentArrangement.dataSet.id);


    // var entityIdStr = Partial.pageParams.entityId
    // var entityIdInt = parseInt(entityIdStr);
    // var parrIdStr = Partial.pageParams.ParrId
    // var parrIdInt = parseInt(parrIdStr);
    debugger;
    Partial.Variables.getCollectionHistoryViewParr.setInput({

        'collectionEntityId': Partial.pageParams.entityId,
        'relatedBusinessEntityId': Partial.pageParams.ParrId,
        'relatedBusinessEntityType': 'CollectionPaymentArrangement'

    });
    debugger;
    Partial.Variables.getCollectionHistoryViewParr.invoke();
    if (Partial.pageParams.ParrId) {

        var getPaymentArrangementVar = Partial.Variables.getPaymentArrangement;

        /* Partial.Variables.getPaymentArrangement.setInput({
             "id": Partial.pageParams.ParrId
         });
         Partial.Variables.getPaymentArrangement.invoke(); */

        getPaymentArrangementVar.invoke({
                "inputFields": {
                    "id": Partial.pageParams.ParrId
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

                var getBillingAccountNameIdVariable = Partial.Variables.getBillingAccountNameIdVar;
                getBillingAccountNameIdVariable.invoke({
                        "inputFields": {
                            "billingAccountRefIds": billingAccountRefIdArray
                        }
                    },
                    function(data) {
                        // billingAccountIdNameListVar
                        Partial.Variables.billingAccountIdNameListVar.dataSet = [];

                        data.forEach(function(d) {

                            Partial.billingAccountRefIdAndNameArr = {
                                "billingAccountId": d.billingAccountId,
                                "billingAccountName": d.billingAccountName
                            }


                            Partial.Variables.billingAccountIdNameListVar.dataSet.push(Partial.billingAccountRefIdAndNameArr);
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

    }
};

Partial.installmentScheduleExpand = function($event, widget) {
    debugger;
    let showIcon1 = document.querySelector('.show-icon1');
    let hideIcon1 = document.querySelector('.hide-icon1');
    if (hideIcon1) {
        hideIcon1.style.display = 'none';
    }
    if (showIcon1) {
        showIcon1.style.display = 'inline-block';
    }
    Partial.Variables.parrIsExpand.dataSet.dataValue = !Partial.Variables.parrIsExpand.dataSet.dataValue;
};

Partial.installmentScheduleCollapse = function($event, widget) {
    debugger;
    let showIcon1 = document.querySelector('.show-icon1');
    let hideIcon1 = document.querySelector('.hide-icon1');
    if (hideIcon1) {
        hideIcon1.style.display = 'inline-block';
    }
    if (showIcon1) {
        showIcon1.style.display = 'none';
    }
    Partial.Variables.parrIsExpand.dataSet.dataValue = !Partial.Variables.parrIsExpand.dataSet.dataValue;
};

Partial.parrHistoryExpand = function($event, widget) {
    debugger;
    let showIcon2 = document.querySelector('.show-icon2');
    let hideIcon2 = document.querySelector('.hide-icon2');
    if (hideIcon2) {
        hideIcon2.style.display = 'none';
    }
    if (showIcon2) {
        showIcon2.style.display = 'inline-block';
    }
    Partial.Variables.parrIsExpand.dataSet.dataValue = !Partial.Variables.parrIsExpand.dataSet.dataValue;
};

Partial.parrHistoryCollapse = function($event, widget) {
    debugger;
    let showIcon2 = document.querySelector('.show-icon2');
    let hideIcon2 = document.querySelector('.hide-icon2');
    if (hideIcon2) {
        hideIcon2.style.display = 'inline-block';
    }
    if (showIcon2) {
        showIcon2.style.display = 'none';
    }
    Partial.Variables.parrIsExpand.dataSet.dataValue = !Partial.Variables.parrIsExpand.dataSet.dataValue;
};

Partial.YesCancelButtonClick = function($event, widget) {
    debugger;
    Partial.Variables.CancelPaymentArrangement.setInput({
        "CollectionPaymentArrangementUpdate": {
            'id': Partial.pageParams.ParrId,
            'comment': Partial.Widgets.CancelComments.datavalue,
            'status': 'Cancelled',
            'channel': {
                'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId,
                'originatorAppId': "FAWBTELUSAGENT"
            }
        }
    });
    //Invoke POST updateParrStatus service
    Partial.Variables.CancelPaymentArrangement.invoke();

};

Partial.RenegotiateParrAmountChange = function($event, widget, newVal, oldVal) {
    debugger;
    Partial.Variables.getPaymentArrangement.dataSet.amount = newVal;
    var size = Partial.Variables.ParrInstallmentSchedule.dataSet.length;
    var totalInstallmentAmt = 0;
    for (var i = 0; i < size; i++) {

        totalInstallmentAmt = parseFloat(totalInstallmentAmt) + parseFloat(Partial.Variables.ParrInstallmentSchedule.dataSet[i].amount);
    }
    Partial.Variables.ParrInstallmentSchedule.dataSet[size - 1].sequenceId = size;
    Partial.AmtOverUnder = parseFloat(newVal) - totalInstallmentAmt;
};

Partial.getInstallmentScheduleTableRowupdate = function($event, widget, row) {
    debugger;
    var size = Partial.Variables.ParrInstallmentSchedule.dataSet.length;
    var totalInstallmentAmt = 0;
    for (var i = 0; i < size; i++) {

        totalInstallmentAmt = parseFloat(totalInstallmentAmt) + parseFloat(Partial.Variables.ParrInstallmentSchedule.dataSet[i].amount);
    }
    Partial.AmtOverUnder = parseFloat(Partial.Widgets.ParrAmount.datavalue) - totalInstallmentAmt;
};

Partial.renegotiatePARRdialogOpened = function($event, widget) {
    debugger;
    Partial.Variables.successMessage.dataSet.dataValue = null;
    Partial.Variables.errorMsg.dataSet.dataValue = null;
    Partial.Variables.ParrInstallmentSchedule.dataSet.splice(0, Partial.Variables.ParrInstallmentSchedule.dataSet.length);
    Partial.Variables.ParrInstallmentSchedule.dataSet.push(...Partial.Variables.getPaymentArrangement.dataSet.installments);
    var size = Partial.Variables.ParrInstallmentSchedule.dataSet.length;
    var totalInstallmentAmt = 0;
    for (var i = 0; i < size; i++) {

        totalInstallmentAmt = parseFloat(totalInstallmentAmt) + parseFloat(Partial.Variables.ParrInstallmentSchedule.dataSet[i].amount);
    }
    Partial.AmtOverUnder = parseFloat(Partial.Variables.getPaymentArrangement.dataSet.amount) - totalInstallmentAmt;
};

Partial.SubmitButtonClick = function($event, widget) {
    debugger;
    var installmentLength = Partial.Variables.ParrInstallmentSchedule.dataSet.length;
    var installmentScheduleDataSet = Partial.Variables.ParrInstallmentSchedule.dataSet;
    var breakCheck1 = false;
    for (i = 0; i < installmentLength; i++) {
        for (k = i + 1; k < installmentLength; k++) {
            if (installmentScheduleDataSet[i].date == installmentScheduleDataSet[k].date) {
                breakCheck1 = true;
                break;
            }
        }
        if (breakCheck1) break;
    }

    if (breakCheck1) {
        Partial.Variables.errMsgRenegotiateInstallment.dataSet.dataValue = "Installment dates cannot be same. Please provide different installment dates";
        setTimeout(messageTimeout, 6000);
    } else {
        Partial.Variables.errMsgRenegotiateInstallment.dataSet.dataValue = null;
        Partial.Variables.updatePaymentArrangement.setInput({
            "CollectionPaymentArrangementUpdate": {
                'amount': Partial.Variables.getPaymentArrangement.dataSet.amount,
                'installments': Partial.Variables.ParrInstallmentSchedule.dataSet,
                'id': Partial.pageParams.ParrId,
                'comment': Partial.Widgets.Comments.datavalue,
                'recurrence': Partial.Variables.getPaymentArrangement.dataSet.recurrence,
                'status': 'Renegotiated',
                'channel': {
                    'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId,
                    'originatorAppId': "FAWBTELUSAGENT"
                }
            }
        });
        //Invoke PATCH UpdateParr service
        Partial.Variables.updatePaymentArrangement.invoke();
        Partial.Widgets.renegotiatePARRdialog.close();
    }

};
Partial.getInstallmentScheduleTableRowinsert = function($event, widget, row) {
    debugger;
    var size = Partial.Variables.ParrInstallmentSchedule.dataSet.length;
    var totalInstallmentAmt = 0;
    for (var i = 0; i < size; i++) {

        totalInstallmentAmt = parseFloat(totalInstallmentAmt) + parseFloat(Partial.Variables.ParrInstallmentSchedule.dataSet[i].amount);
    }
    Partial.Variables.ParrInstallmentSchedule.dataSet[size - 1].sequenceId = size;
    Partial.AmtOverUnder = parseFloat(Partial.Widgets.ParrAmount.datavalue) - totalInstallmentAmt;
};

Partial.BackLinkClick = function($event, widget) {

    Partial.Variables.successMessage.dataSet.dataValue = null;
    Partial.Variables.errorMsg.dataSet.dataValue = null;
    Partial.Variables.ParrPageName.dataSet.dataValue = 'ParrList';
    App.refreshParrList();
};

Partial.CancelParrClick = function($event, widget) {
    Partial.Variables.successMessage.dataSet.dataValue = null;
    Partial.Variables.errorMsg.dataSet.dataValue = null;
};

function messageTimeout() {
    Partial.Variables.successMessage.dataSet.dataValue = null;
    App.Variables.errorMsg.dataSet.dataValue = null;
    Partial.Variables.errMsgRenegotiateInstallment.dataSet.dataValue = null;
}

Partial.updatePaymentArrangementonError = function(variable, data, xhrObj) {
    debugger;
    var reasonIndex = xhrObj.error.replaceAll('\\', '').indexOf("reason") + 9;
    var codeIndex = xhrObj.error.replaceAll('\\', '').lastIndexOf("code") - 3;
    var errorMessage = xhrObj.error.replaceAll('\\', '').substring(reasonIndex, codeIndex);
    //App.Variables.errorMsg.dataSet.dataValue = "PARR renegotiation failed as it can be renegotiated only once."
    App.Variables.errorMsg.dataSet.dataValue = errorMessage;
    setTimeout(messageTimeout, 5000);
};


Partial.updatePaymentArrangementonSuccess = function(variable, data) {
    debugger;
    App.Variables.successMessage.dataSet.dataValue = "PARR renegotiated successfully."
    var getPaymentArrangementVar = Partial.Variables.getPaymentArrangement;

    getPaymentArrangementVar.invoke({
            "inputFields": {
                "id": Partial.pageParams.ParrId
            },
        },
        function(data) {
            debugger;
            Partial.Variables.getCollectionHistoryViewParr.setInput({
                'collectionEntityId': Partial.pageParams.entityId,
                'relatedBusinessEntityId': Partial.pageParams.ParrId,
                'relatedBusinessEntityType': 'CollectionPaymentArrangement'

            });

            Partial.Variables.getCollectionHistoryViewParr.invoke();
        },
        function(error) {
            // Error Callback
            console.log("error", error);
        }
    );

    setTimeout(messageTimeout, 5000);
};

Partial.CancelPaymentArrangementonError = function(variable, data, xhrObj) {
    debugger;
    var reasonIndex = xhrObj.error.replaceAll('\\', '').indexOf("reason") + 9;
    var codeIndex = xhrObj.error.replaceAll('\\', '').lastIndexOf("code") - 3;
    var errorMessage = xhrObj.error.replaceAll('\\', '').substring(reasonIndex, codeIndex);
    App.Variables.errorMsg.dataSet.dataValue = errorMessage;
    setTimeout(messageTimeout, 5000);
};


Partial.CancelPaymentArrangementonSuccess = function(variable, data) {
    debugger;
    App.Variables.successMessage.dataSet.dataValue = "PARR cancelled successfully."
    var getPaymentArrangementVar = Partial.Variables.getPaymentArrangement;

    getPaymentArrangementVar.invoke({
            "inputFields": {
                "id": Partial.pageParams.ParrId
            },
        },
        function(data) {
            debugger;

            Partial.Variables.getCollectionHistoryViewParr.setInput({
                'collectionEntityId': Partial.pageParams.entityId,
                'relatedBusinessEntityId': Partial.pageParams.ParrId,
                'relatedBusinessEntityType': 'CollectionPaymentArrangement'

            });

            Partial.Variables.getCollectionHistoryViewParr.invoke();
        },
        function(error) {
            // Error Callback
            console.log("error", error);
        }
    );



    setTimeout(messageTimeout, 5000);

};

Partial.getCollectionHistoryViewParronSuccess = function(variable, data) {

    // App.refreshParrSummary();
    // App.refreshEntProfCancelParrSummary();
};

Partial.getCollectionHistoryViewParronError = function(variable, data) {

};
Partial.getInstallmentScheduleTable_dateOnChange = function($event, widget, row) {
    debugger;
    var widgetDateValue = widget.bsDataValue;
    // var widgetDate = new Date(widgetDateValue);
    //widgetDate.setHours(0, 0, 0, 0);
    var currentDate = new Date();
    widgetDate = new Date(widgetDateValue.toDateString());
    currentDate = new Date(currentDate.toDateString());
    //currentDate.setHours(0, 0, 0, 0);
    if (widgetDate.valueOf() < currentDate.valueOf()) {
        Partial.Variables.errMsgRenegotiateInstallment.dataSet.dataValue = "Installment date cannot be less than current date";
        $(".cancel.row-action-button.btn.app-button.btn-danger.cancel-edit-row-button.btn-xs").click();
        setTimeout(messageTimeout, 6000);
    } else {
        Partial.Variables.errMsgRenegotiateInstallment.dataSet.dataValue = null;
    }

};