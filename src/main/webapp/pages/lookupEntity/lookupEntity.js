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

Page.navigateToEntityPage = function(entityId) {
    debugger;
    return "#/Lookup?entityId=" + (!entityId ? 0 : entityId);
}


Page.button1Click = function($event, widget) {

    debugger;

    Page.Variables.searchEntity.invoke();

};
Page.button2Click = function($event, widget) {

    Page.Widgets.select1.datavalue = 'Select';
    Page.Widgets.select3.datavalue = 'Select';
    Page.Widgets.select4.datavalue = 'Select';
    Page.Widgets.select5.datavalue = 'Select';
    Page.Widgets.text3._datavalue = '';
    Page.Widgets.text4._datavalue = '';
    Page.Widgets.text5._datavalue = '';

    Page.Variables.searchEntity.invoke();



    debugger;
};