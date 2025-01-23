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

function messageTimeout() {
    App.Variables.successMessage.dataSet.dataValue = null;
    App.Variables.errorMsg.dataSet.dataValue = null;
}

//Invalidate
Partial.testdataTable1_customRow1Action = function($event, row) {

};

//notify checkbox change
Partial.testdataTable1_columnCOnChange = function($event, widget, row) {
    debugger;
    setTimeout(messageTimeout, 5000);
    App.Variables.successMessage.dataSet.dataValue = "Digital Contact created successfully.";
    App.Variables.errorMsg.dataSet.dataValue = null;
    //    App.refreshContactList();
    setTimeout(messageTimeout, 5000);
};