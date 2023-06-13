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
    Partial.Variables.DisputePageName.dataSet.dataValue = 'DisputeList';
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
        Partial.Variables.updateDisputeService.setInput({
            "id": App.Variables.disputeIdAppVar.dataSet.dataValue,
            "CollectionDisputeUpdate": {
                'statusReason': Partial.Widgets.cancelledReasonValue.datavalue,
                'comment': Partial.Widgets.CommentsCancelDispute.datavalue,
                'status': "CANCEL"
            }
        });


        Partial.Variables.updateDisputeService.invoke();
        Partial.Widgets.cancelDisputeConfirmation.close();
        App.Variables.successMessage.dataSet.dataValue = "Dispute Cancelled successfully";
        setTimeout(messageTimeout, 8000);
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
        Partial.Variables.updateDisputeService.setInput({
            "id": App.Variables.disputeIdAppVar.dataSet.dataValue,
            "CollectionDisputeUpdate": {
                'statusReason': Partial.Widgets.ClosedDisputeReason.datavalue,
                'comment': Partial.Widgets.CloseDisputeComments.datavalue,
                'status': "CLOSED"
            }
        });


        Partial.Variables.updateDisputeService.invoke();
        Partial.Widgets.closeDisputeDialog.close();
        App.Variables.successMessage.dataSet.dataValue = "Dispute Closed successfully";
        setTimeout(messageTimeout, 8000);
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
            "id": App.Variables.disputeIdAppVar.dataSet.dataValue,
            "CollectionDisputeUpdate": {
                'adjustmentToDate': Partial.Widgets.AdjustmentToDateUpdate.datavalue,
                'amount': Partial.Widgets.diputeAmtUpdate.datavalue,
                'collectionExclusionIndicator': Partial.Widgets.updateDisputeExclusionDropdown.datavalue,
                'chargeType': Partial.Widgets.chargeTypeDropdownUpdate.datavalue,
                'disputeReason': Partial.Widgets.reasonDropdownUpdate.datavalue,
                'product': Partial.Widgets.productDropdown.datavalue,
                'customerEmail': Partial.Widgets.customerEmailUpdate.datavalue,
                'disputePrime': Partial.Widgets.AssignedDisputePrime.datavalue,
                'comment': Partial.Widgets.commentsUpdate.datavalue
            }
        });

        Partial.Variables.updateDisputeService.invoke();
        Partial.Widgets.updateDisputeConfirmation.close();
        App.Variables.successMessage.dataSet.dataValue = "Dispute Updated successfully";
        setTimeout(messageTimeout, 8000);
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
    App.Variables.errorMsg.dataSet.dataValue = "";
    App.Variables.successMessage.dataSet.dataValue = "";
    Partial.Widgets.updateDisputeConfirmation.open();
};