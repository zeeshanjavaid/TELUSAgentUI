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