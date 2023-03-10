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
    Partial.Variables.installmentBANCreateParr.dataSet = [];

};
Partial.button4Click = function($event, widget) {
    debugger;
    //selectBanParrTable1
};
Partial.button8_1Click = function($event, widget) {
    debugger;
};
Partial.button10_1Click = function($event, widget) {
    debugger;
};

Partial.button4Click = function($event, widget) {
    debugger;
    Partial.Variables.installmentBANCreateParr.dataSet = [];
    //BANName
    Partial.Widgets.selectBanParrTable1.selectedItems.forEach(function(a) {

        Partial.obj = {
            "BAN": a.BAN,
            "BANName": a.BANName
        }
        Partial.Variables.installmentBANCreateParr.dataSet.push(Partial.obj);
    })

    Partial.Widgets.dialog1.close();
};