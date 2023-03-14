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
    //alert("ParrId: " + Partial.Variables.getPaymentArrangement.dataSet.id);
    //alert("Page param ParrId: " + Partial.pageParams.ParrId);

};
Partial.anchor2Click = function($event, widget) {

    Partial.Variables.ParrPageName.dataSet.dataValue = 'ParrList';
};
Partial.installmentScheduleCollapse = function($event, widget) {
    Partial.IsExpandedIS = false;

};

Partial.installmentScheduleExpand = function($event, widget) {
    Partial.IsExpandedIS = true;
};


Partial.parrHistoryCollapse = function($event, widget) {
    Partial.IsExpandedIS = false;
};
Partial.parrHistoryExpand = function($event, widget) {
    Partial.IsExpandedIS = true;
};

Partial.SubmitButtonClick = function($event, widget) {

    alert("Update :" + Partial.Variables.getPaymentArrangement.dataSet.id);
    //Partial.Variables.getPaymentArrangement.dataSet.comment = Partial.Widgets.Comments.datavalue;
};

Partial.updatePaymentArrangementonBeforeUpdate = function(variable, inputData, options) {

    Partial;
    debugger;
};