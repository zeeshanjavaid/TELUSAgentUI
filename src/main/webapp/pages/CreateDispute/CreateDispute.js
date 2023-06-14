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
    var regex = /[a-z0-9]+@[a-z]+\.[a-z]{2,3}/;
    return regex.test(email);
}

function messageTimeout() {
    App.Variables.successMessage.dataSet.dataValue = null;
}

Partial.SubmitDisputeBanClick = function($event, widget) {

    Partial.Variables.selectedDisputeBanVar.dataSet.dataValue = Partial.Widgets.selectBanDisputeTable1.selecteditem.banId;
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
    } else if (Partial.Widgets.custEmailText.datavalue !== "") {
        if (!isEmail(Partial.Widgets.custEmailText.datavalue)) {
            App.Variables.errorMsg.dataSet.dataValue = "Please provide valid email id";
        } else {


            App.Variables.errorMsg.dataSet.dataValue = "";
            var selectedBanInt = parseInt(Partial.Widgets.selectedDisputeBan.datavalue);

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
                    'billingAccountRef': {
                        'id': selectedBanInt
                    }
                }
            });

            //Invoke POST createDispute service
            Partial.Variables.CreateDisputeService.invoke();

            App.Variables.successMessage.dataSet.dataValue = "Dispute created successfully"
            Partial.Variables.DisputePageName.dataSet.dataValue = 'DisputeList';
            setTimeout(messageTimeout, 10000);
        }
    }

};