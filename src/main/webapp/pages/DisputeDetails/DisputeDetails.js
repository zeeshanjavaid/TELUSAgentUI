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

    if (Partial.pageParams.DisputeId) {

        Partial.Variables.getDisputeDetails.setInput({
            "id": Partial.pageParams.DisputeId
        });
        Partial.Variables.getDisputeDetails.invoke();
    }

};

function messageTimeout() {
    App.Variables.successMessage.dataSet.dataValue = null;
}


Partial.anchor3Click = function($event, widget) {
    Partial.Variables.successMessage.dataSet.dataValue = null;
    Partial.Variables.errorMsg.dataSet.dataValue = null;
    Partial.Variables.DisputePageName.dataSet.dataValue = 'DisputeList';
    App.refreshDisputeList();
};

/* for panel expand and collapse*/
Partial.disputeHistoryExpand = function($event, widget) {
    Partial.IsExpandedIS = true;
};
Partial.disputeHistoryCollapse = function($event, widget) {
    Partial.IsExpandedIS = false;
};


Partial.CancelDisputeSubmitClick = function($event, widget) {
    debugger;
    if (!Partial.Widgets.cancelledReasonValue.datavalue && !Partial.Widgets.CommentsCancelDispute.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Please enter mandatory fields";
    } else if (!Partial.Widgets.cancelledReasonValue.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Cancelled Reason is a mandatory field";
    } else if (!Partial.Widgets.CommentsCancelDispute.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Comments is a mandatory field";
    } else {
        App.Variables.errorMsg.dataSet.dataValue = "";
        Partial.Variables.CancelDisputeServiceVar.setInput({
            "id": Partial.pageParams.DisputeId,
            "CollectionDisputeUpdate": {
                "id": Partial.pageParams.DisputeId,
                'disputeReason': Partial.Widgets.cancelledReasonValue.datavalue,
                'comment': Partial.Widgets.CommentsCancelDispute.datavalue,
                'status': "Cancelled",
                "channel": {
                    "userId": App.Variables.getLoggedInUserDetails.dataSet.emplId,
                    "originatorAppId": "FAWBTELUSAGENT"
                }
            }
        });


        Partial.Variables.CancelDisputeServiceVar.invoke();
    }

};
Partial.CloseDisputeSubmitClick = function($event, widget) {
    debugger;
    if (!Partial.Widgets.ClosedDisputeReason.datavalue && !Partial.Widgets.CloseDisputeComments.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Please enter mandatory fields";
    } else if (!Partial.Widgets.ClosedDisputeReason.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Closed Reason is a mandatory field";
    } else if (!Partial.Widgets.CloseDisputeComments.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Comments is a mandatory field";
    } else {
        App.Variables.errorMsg.dataSet.dataValue = "";
        Partial.Variables.CloseDisputeServiceVar.setInput({
            "id": Partial.pageParams.DisputeId,
            "CollectionDisputeUpdate": {
                "id": Partial.pageParams.DisputeId,
                'disputeReason': Partial.Widgets.ClosedDisputeReason.datavalue,
                'comment': Partial.Widgets.CloseDisputeComments.datavalue,
                'status': "Closed",
                "channel": {
                    "userId": App.Variables.getLoggedInUserDetails.dataSet.emplId,
                    "originatorAppId": "FAWBTELUSAGENT"
                }
            }
        });


        Partial.Variables.CloseDisputeServiceVar.invoke();

    }


};
Partial.UpdateDisputeSubmitClick = function($event, widget) {
    debugger;
    if (!Partial.Widgets.updateDisputeExclusionDropdown.datavalue && !Partial.Widgets.diputeAmtUpdate.datavalue && !Partial.Widgets.chargeTypeDropdownUpdate.datavalue && !Partial.Widgets.reasonDropdownUpdate.datavalue && !Partial.Widgets.productDropdown.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Please enter mandatory fields";
    } else if (!Partial.Widgets.diputeAmtUpdate.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Dispute Amount is mandatory";
    } else if (!Partial.Widgets.chargeTypeDropdownUpdate.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Charge Type is mandatory";
    } else if (!Partial.Widgets.reasonDropdownUpdate.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Reason is mandatory";
    } else if (!Partial.Widgets.productDropdown.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Product & Services is mandatory";
    } else {
        App.Variables.errorMsg.dataSet.dataValue = "";
        Partial.Variables.updateDisputeService.setInput({
            "id": Partial.pageParams.DisputeId,
            "CollectionDisputeUpdate": {
                "id": Partial.pageParams.DisputeId,
                'adjustmentToDate': Partial.Widgets.AdjustmentToDateUpdate.datavalue,
                'amount': Partial.Widgets.diputeAmtUpdate.datavalue,
                'collectionExclusionIndicator': Partial.Widgets.updateDisputeExclusionDropdown.datavalue,
                'chargeType': Partial.Widgets.chargeTypeDropdownUpdate.datavalue,
                'disputeReason': Partial.Widgets.reasonDropdownUpdate.datavalue,
                'product': Partial.Widgets.productDropdown.datavalue,
                'customerEmail': Partial.Widgets.customerEmailUpdate.datavalue,
                'disputePrime': Partial.Widgets.AssignedDisputePrime.datavalue,
                'comment': Partial.Widgets.commentsUpdate.datavalue,
                "channel": {
                    "userId": App.Variables.getLoggedInUserDetails.dataSet.emplId,
                    "originatorAppId": "FAWBTELUSAGENT"
                }
            }
        });

        Partial.Variables.updateDisputeService.invoke();

    }

};
Partial.CloseDisputeClick = function($event, widget) {
    App.Variables.errorMsg.dataSet.dataValue = "";
    App.Variables.successMessage.dataSet.dataValue = "";
    Partial.Widgets.closeDisputeDialog.open();
};
Partial.CancelDisputeClick = function($event, widget) {
    App.Variables.errorMsg.dataSet.dataValue = "";
    App.Variables.successMessage.dataSet.dataValue = "";
    Partial.Widgets.cancelDisputeConfirmation.open();
};
Partial.UpdateDisputeClick = function($event, widget) {
    debugger;
    App.Variables.errorMsg.dataSet.dataValue = "";
    App.Variables.successMessage.dataSet.dataValue = "";
    billingAccountRefId = Partial.Variables.getDisputeDetails.dataSet.billingAccountRef.id;

    var getBillingAccountNameIdVariable = Partial.Variables.getBillingAccountNameIdVar;
    getBillingAccountNameIdVariable.invoke({
            "inputFields": {
                "billingAccountRefIds": billingAccountRefId
            }
        },
        function(data) {

            Partial.Variables.billingAccountIdNameDisputeListVar.dataSet = [];

            data.forEach(function(d) {

                Partial.billingAccountRefIdAndNameArr = {
                    "billingAccountId": data.billingAccountId,
                    "billingAccountName": d.billingAccountName
                }


                Partial.Variables.billingAccountIdNameDisputeListVar.dataSet.push(Partial.billingAccountRefIdAndNameArr);
            });
        },
        function(error) {
            // Error Callback
            console.log("error", error);
        }

    );
    Partial.Widgets.updateDisputeConfirmation.open();
};

Partial.CloseDisputeServiceVaronSuccess = function(variable, data) {

    Partial.Widgets.closeDisputeDialog.close();
    App.Variables.successMessage.dataSet.dataValue = "Dispute Closed successfully";
    Partial.Variables.getDisputeDetails.setInput({
        "id": Partial.pageParams.DisputeId
    });
    Partial.Variables.getDisputeDetails.invoke();
    setTimeout(messageTimeout, 8000);

};

Partial.CancelDisputeServiceVaronSuccess = function(variable, data) {

    Partial.Widgets.cancelDisputeConfirmation.close();
    App.Variables.successMessage.dataSet.dataValue = "Dispute Cancelled successfully";
    Partial.Variables.getDisputeDetails.setInput({
        "id": Partial.pageParams.DisputeId
    });
    Partial.Variables.getDisputeDetails.invoke();
    setTimeout(messageTimeout, 8000);

};

Partial.updateDisputeServiceonSuccess = function(variable, data) {

    Partial.Widgets.updateDisputeConfirmation.close();
    App.Variables.successMessage.dataSet.dataValue = "Dispute Updated successfully";
    Partial.Variables.getDisputeDetails.setInput({
        "id": Partial.pageParams.DisputeId
    });
    Partial.Variables.getDisputeDetails.invoke();
    setTimeout(messageTimeout, 8000);

};