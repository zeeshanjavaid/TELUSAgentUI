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
    $('#banViewTableGrid').hide();
    $("#entityViewBtn").css("background-color", "#4B286D");
    $("#entityViewBtn").css("color", "white");
};


Page.entityViewButtonClick = function($event, widget) {

    // to make buttons selected
    $("#entityViewBtn").css("background-color", "#4B286D");
    $("#entityViewBtn").css("color", "white");
    $("#banViewBtn").css("background-color", "white");
    $("#banViewBtn").css("color", "#4B286D");

    // display entity view table and hide ban table
    $('#entityViewTableGrid').show();
    $('#banViewTableGrid').hide();

};

Page.banViewButtonClick = function($event, widget) {

    // to make buttons selected
    $("#banViewBtn").css("background-color", "#4B286D");
    $("#banViewBtn").css("color", "white");
    $("#entityViewBtn").css("background-color", "white");
    $("#entityViewBtn").css("color", "#4B286D");

    // display ban table and hide entity view table
    $('#banViewTableGrid').show();
    $('#entityViewTableGrid').hide();

};

// function added to clear all the fields in the filter
Page.clearFilterFields = function($event, widget) {
    Page.Widgets.AssignedTeamSelect.datavalue = "";
    Page.Widgets.portfolioSelect.datavalue = "";
    Page.Widgets.includeCurrentCreditSelect.datavalue = "";
    Page.Widgets.entityOwnerSelect.datavalue = "";
    Page.Widgets.billingSystemSelect.datavalue = "";
    Page.Widgets.ARExcludedInternalSelect.datavalue = "";
    Page.Widgets.workCategorySelect.datavalue = "";
    Page.Widgets.collStatusSelect.datavalue = "";
}