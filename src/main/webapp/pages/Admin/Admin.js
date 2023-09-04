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
Page.LeftNavAdminMenuListList1Click = function(widget, $data) {


    //alert(Partial.Variables.LeftNavAdminMenuList.dataSet)
};

Page.panel83Actionsclick = function($event, action, widget) {
    debugger;
};
Page.panel82Actionsclick = function($event, action, widget) {
    debugger;

};
Page.NameClick = function($event, widget, item, currentItemWidgets) {

    debugger;
    App.Variables.successMessage.dataSet.dataValue = "";
    App.Variables.errorMsg.dataSet.dataValue = "";

};