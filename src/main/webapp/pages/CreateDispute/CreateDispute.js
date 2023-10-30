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

};

function isEmail(email) {
    var regex = /[A-Za-z0-9]+@[A-Za-z]+\.[A-Za-z]{2,3}/;
    return regex.test(email);
}

function messageTimeout() {
    App.Variables.successMessage.dataSet.dataValue = null;
    App.Variables.errorMsg.dataSet.dataValue = null;
}

Partial.SubmitDisputeBanClick = function($event, widget) {
    debugger;
    Partial.Variables.selectedDisputeBanVar.dataSet.dataValue = Partial.Widgets.selectBanDisputeTable1.selecteditem.banRefId;
    Partial.Widgets.selectedDisputeBan.datavalue = Partial.Widgets.selectBanDisputeTable1.selecteditem.banId;
    Partial.Widgets.SelectDisputeBanDialog.close();
};
Partial.CancelDisputeClick = function($event, widget) {
    Partial.Widgets.selectedDisputeBan.datavalue = "";
    Partial.Widgets.exclusionDropdown.datavalue = "";
    Partial.Widgets.disputeAmt.datavalue = "";
    Partial.Widgets.chargeTypeDropDown.datavalue = "";
    Partial.Widgets.reasonDropdown.datavalue = "";
    Partial.Widgets.productsDropdown.datavalue = "";
    Partial.Widgets.AdjustmentToDate.datavalue = "";
    Partial.Widgets.custEmailText.datavalue = "";
    Partial.Widgets.CreateCommentsDispute.datavalue = "";
    Partial.Widgets.AssignedDisputePrime.datavalue = "";
    App.Variables.errorMsg.dataSet.dataValue = "";
    App.Variables.successMessage.dataSet.dataValue = "";
    Partial.Variables.DisputePageName.dataSet.dataValue = 'DisputeList';
};

Partial.CreateDisputeClick = function($event, widget) {
    debugger;
    App.Variables.errorMsg.dataSet.dataValue = "";
    App.Variables.successMessage.dataSet.dataValue = "";

    var custEmailText = Partial.Widgets.custEmailText.datavalue;
    var AssignedDisputePrime = Partial.Widgets.AssignedDisputePrime.datavalue;


    if (!Partial.Widgets.selectedDisputeBan.datavalue && !Partial.Widgets.disputeAmt.datavalue && Partial.Widgets.exclusionDropdown.datavalue === "" && !Partial.Widgets.chargeTypeDropDown.datavalue && !Partial.Widgets.reasonDropdown.datavalue && !Partial.Widgets.productsDropdown.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Please enter mandatory fields";
    } else if (!Partial.Widgets.selectedDisputeBan.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Select BAN is mandatory";
    } else if (Partial.Widgets.exclusionDropdown.datavalue === "") {
        App.Variables.errorMsg.dataSet.dataValue = "Exclusion is mandatory";
    } else if (!Partial.Widgets.reasonDropdown.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Reason is mandatory";
    } else if (!Partial.Widgets.disputeAmt.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Dispute Amount is mandatory";
    } else if (!Partial.Widgets.chargeTypeDropDown.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Charge Type is mandatory";
    } else if (!Partial.Widgets.productsDropdown.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Product & Services is mandatory";
    } else if (Partial.Widgets.custEmailText.datavalue !== "" && !isEmail(Partial.Widgets.custEmailText.datavalue)) {
        App.Variables.errorMsg.dataSet.dataValue = "Please provide valid Customer email id";
    } else if (Partial.Widgets.AssignedDisputePrime.datavalue !== "" && !isEmail(Partial.Widgets.AssignedDisputePrime.datavalue)) {
        App.Variables.errorMsg.dataSet.dataValue = "Please provide valid Assigned prime email id";
    } else if (Partial.Widgets.custEmailText.datavalue !== "" && Partial.Widgets.AssignedDisputePrime.datavalue !== "" && (custEmailText.toLowerCase() === AssignedDisputePrime.toLowerCase())) {
        App.Variables.errorMsg.dataSet.dataValue = "Customer email id and Assigned prime email id should be different";
    } else if (Partial.Widgets.disputeAmt.datavalue !== "" && Partial.Widgets.disputeAmt.datavalue.toString().length > 10) {
        App.Variables.errorMsg.dataSet.dataValue = "Dispute Amount cannot be greater than 10 digits";
    } else if (Partial.Widgets.AdjustmentToDate.datavalue != "" && Partial.Widgets.AdjustmentToDate.datavalue != null) {
        if (Partial.Widgets.AdjustmentToDate.datavalue.toString().length > 10) {
            App.Variables.errorMsg.dataSet.dataValue = "Adjustment(s) to Date cannot be greater than 10 digits";
        }
    } else {


        App.Variables.errorMsg.dataSet.dataValue = "";
        //var selectedBanInt = parseInt(Partial.Widgets.selectedDisputeBan.datavalue);
        var selectedBanInt = parseInt(Partial.Variables.selectedDisputeBanVar.dataSet.dataValue);

        Partial.Variables.CreateDisputeService.setInput({
            "CollectionDisputeCreate": {
                'amount': Partial.Widgets.disputeAmt.datavalue,
                'chargeType': Partial.Widgets.chargeTypeDropDown.datavalue,
                'collectionExclusionIndicator': Partial.Widgets.exclusionDropdown.datavalue,
                'disputeReason': Partial.Widgets.reasonDropdown.datavalue,
                'product': Partial.Widgets.productsDropdown.datavalue,
                'adjustmentToDate': Partial.Widgets.AdjustmentToDate.datavalue,
                'customerEmail': Partial.Widgets.custEmailText.datavalue,
                'comment': Partial.Widgets.CreateCommentsDispute.datavalue,
                'disputePrime': Partial.Widgets.AssignedDisputePrime.datavalue,
                'billingAdjustmentRequestId': 'string',
                'status': 'Open',
                'billingAccountRef': {
                    'id': selectedBanInt
                },
                "channel": {
                    "userId": App.Variables.getLoggedInUserDetails.dataSet.emplId,
                    "originatorAppId": "FAWBTELUSAGENT"
                }
            }
        });

        //Invoke POST createDispute service
        Partial.Variables.CreateDisputeService.invoke();


    }

};
Partial.SelectBanClick = function($event, widget) {
    debugger;
    var entityIdStr = Partial.pageParams.entityId
    var entityIdInt = parseInt(entityIdStr);
    Partial.Variables.getEntityBanDetailsServiceVar.setInput({
        'entityId': entityIdInt
    });

    Partial.Variables.getEntityBanDetailsServiceVar.invoke();

};

Partial.CreateDisputeServiceonSuccess = function(variable, data) {

    App.Variables.successMessage.dataSet.dataValue = "Dispute created successfully"
    Partial.Variables.DisputePageName.dataSet.dataValue = 'DisputeList';
    App.refreshDisputeList();
    App.refreshHistoryActionList();
    App.refreshEntityBanDetails();
    setTimeout(messageTimeout, 10000);

};

Partial.CreateDisputeServiceonError = function(variable, data, xhrObj) {
    debugger;
    var reasonIndex = xhrObj.error.replaceAll('\\', '').indexOf("reason") + 9;
    var codeIndex = xhrObj.error.replaceAll('\\', '').lastIndexOf("code") - 3;
    var errorMessage = xhrObj.error.replaceAll('\\', '').substring(reasonIndex, codeIndex);
    App.Variables.errorMsg.dataSet.dataValue = errorMessage;
    // Partial.Variables.DisputePageName.dataSet.dataValue = 'DisputeList';
    setTimeout(messageTimeout, 8000);
};

Partial.getEntityBanDetailsServiceVaronSuccess = function(variable, data) {

    Partial.Widgets.SelectDisputeBanDialog.open();

};

Partial.getEntityBanDetailsServiceVaronError = function(variable, data, xhrObj) {
    Partial.Widgets.SelectDisputeBanDialog.open();
};