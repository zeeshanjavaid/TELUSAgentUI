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

Partial.SubmitDisputeBanClick = function($event, widget) {

    Partial.Variables.selectedDisputeBanVar.dataSet.dataValue = Partial.Widgets.selectBanDisputeTable1.selecteditem.banId;
    Partial.Widgets.SelectDisputeBanDialog.close();
};
Partial.CancelDisputeClick = function($event, widget) {
    Partial.Variables.DisputePageName.dataSet.dataValue = 'DisputeList';


};
Partial.CreateDisputeClick = function($event, widget) {
    debugger;
    if (!Partial.Widgets.selectedDisputeBan.datavalue && !Partial.Widgets.disputeAmt.datavalue && !Partial.Widgets.exclusionDropdown.datavalue && !Partial.Widgets.chargeTypeDropDown.datavalue && !Partial.Widgets.reasonDropdown.datavalue && !Partial.Widgets.productsDropdown.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Please enter mandatory fields";
    } else if (!Partial.Widgets.selectedDisputeBan.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Select BAN is mandatory";
    } else if (!Partial.Widgets.exclusionDropdown.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Exclusion is mandatory";
    } else if (!Partial.Widgets.disputeAmt.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Dispute Amount is mandatory";
    } else if (!Partial.Widgets.chargeTypeDropDown.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Charge Type is mandatory";
    } else if (!Partial.Widgets.reasonDropdown.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Reason is mandatory";
    } else if (!Partial.Widgets.productsDropdown.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Product & Services is mandatory";
    } else {
        App.Variables.errorMsg.dataSet.dataValue = "";
        //Invoke Post dispute service
    }

};