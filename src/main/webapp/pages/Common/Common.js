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

    debugger;
    /*
     * variables can be accessed through 'Partial.Variables' property here
     * e.g. to get dataSet in a staticVariable named 'loggedInUser' use following script
     * Partial.Variables.loggedInUser.getData()
     *
     * widgets can be accessed through 'Partial.Widgets' property here
     * e.g. to get value of text widget named 'username' use following script
     * 'Partial.Widgets.username.datavalue'
     */

    Partial.Variables.getWorkCatByEmplIdForMultiSelect.setInput({
        'emplId': Partial.Variables.getLoggedInUserDetails.dataSet.emplId

    });
    Partial.Variables.getWorkCatByEmplIdForMultiSelect.invoke();

    /*Partial.Widgets.SessionTimeoutdialog.open()*/

};
Partial.Login_buttonClick = function($event, widget) {
    debugger;
    /*App.Actions.goToPage_Home.invoke();*/
    /*location.reload();*/
    const currentUrl = window.location.href;
    const modifiedUrl = currentUrl.replace(/\/[^/]+$/, "/Home");
    window.location.href = modifiedUrl;

};