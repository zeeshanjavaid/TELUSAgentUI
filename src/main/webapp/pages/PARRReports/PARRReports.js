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

Page.button1Click = function($event, widget) {


    Page.Widgets.entityRiskSelect.datavalue = "";

    Page.Widgets.parrStatusSelect.datavalue = "";
    Page.Widgets.evalSelect.datavalue = "";

    Page.Widgets.createdBySelect.datavalue = "";

    Page.Widgets.createTeam.datavalue = "";

    Page.Widgets.creationDateFrom.datavalue = "";

    Page.Widgets.creationDateTo.datavalue = "";

    Page.Variables.ParrReportServiceGetParrReport.invoke();

};
Page.button2Click = function($event, widget) {

    Page.Variables.ParrReportServiceGetParrReport.invoke();

};