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
    $('#filterGridBanView').hide();
    $("#entityViewBtn").css("background-color", "#4B286D");
    $("#entityViewBtn").css("color", "white");
};


Page.entityViewButtonClick = function($event, widget) {
    debugger;
    var data = Page.Variables.CollectionDataServiceGetAssignedEntitiesInEntityView3.dataSet;

    // to make buttons selected
    $("#entityViewBtn").css("background-color", "#4B286D");
    $("#entityViewBtn").css("color", "white");
    $("#banViewBtn").css("background-color", "white");
    $("#banViewBtn").css("color", "#4B286D");

    // display entity view table and hide ban table
    $('#entityViewTableGrid').show();
    $('#banViewTableGrid').hide();

    // display filter grid for entity view and hide for ban view
    $('#filterGridEntityView').show();
    $('#filterGridBanView').hide();

};

Page.banViewButtonClick = function($event, widget) {
    debugger;
    var data = Page.Variables.CollectionDataServiceGetassignedEntitiesInClassicView2.dataSet;

    // to make buttons selected
    $("#banViewBtn").css("background-color", "#4B286D");
    $("#banViewBtn").css("color", "white");
    $("#entityViewBtn").css("background-color", "white");
    $("#entityViewBtn").css("color", "#4B286D");

    // display ban table and hide entity view table
    $('#banViewTableGrid').show();
    $('#entityViewTableGrid').hide();

    // display filter grid for ban view and hide for entity view
    $('#filterGridBanView').show();
    $('#filterGridEntityView').hide();

};

// function added to clear all the fields in the filter for Entity View
Page.clearFilterFieldsEntityView = function($event, widget) {
    Page.Widgets.AssignedTeamSelectEV.datavalue = "";
    Page.Widgets.portfolioSelectEV.datavalue = "";
    Page.Widgets.includeCurrentCreditSelectEV.datavalue = "Y";
    Page.Widgets.entityOwnerSelectEV.datavalue = "";
    Page.Widgets.billingSystemSelectEV.datavalue = "CES";
    Page.Widgets.ARExcludedInternalSelectEV.datavalue = "Y";
    Page.Widgets.workCategorySelectEV.datavalue = "";
    Page.Widgets.collStatusSelectEV.datavalue = "";
}

// function added to clear all the fields in the filter for Ban View
Page.clearFilterFieldsBanView = function($event, widget) {
    Page.Widgets.AssignedTeamSelectBV.datavalue = "";
    Page.Widgets.portfolioSelectBV.datavalue = "";
    Page.Widgets.includeCurrentCreditSelectBV.datavalue = "Y";
    Page.Widgets.entityOwnerSelectBV.datavalue = "";
    Page.Widgets.billingSystemSelectBV.datavalue = "CES";
    Page.Widgets.ARExcludedInternalSelectBV.datavalue = "Y";
    Page.Widgets.workCategorySelectBV.datavalue = "";
    Page.Widgets.collStatusSelectBV.datavalue = "";
}

Page.goToEnityPage = function(row) {
    window.open("#/Lookup?entityId=" + (!row.entityId ? 0 : row.entityId), "_blank");
}

Page.entityViewTable_OnRowexpand = function($event, widget, row, $data) {
    App.showRowExpansionEntityViewHome(row, $data);
};

Page.banViewTable_OnRowexpand = function($event, widget, row, $data) {
    App.showRowExpansionBanViewHome(row, $data);
};