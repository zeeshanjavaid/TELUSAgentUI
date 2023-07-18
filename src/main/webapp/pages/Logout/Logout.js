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

    // Page.Variables.sv_unlockAppsForSessionUser.invoke();

    // window.location.href = "removeSession";
    setTimeout(function() {

        // console.log("window.sessionStorage" + window.sessionStorage);
        // window.sessionStorage.clear();
        // delete window.sessionStorage;
        // window.localStorage.clear();

        // window.location.href = "https://www.ficoanalyticcloud.com/fac_fawb_login/#/singlelogout";

        window.location.href = "https://console.dms.cact1.ficoanalyticcloud.com/com.fico.fusion/logout?logoutType=idp";
    }, 500);

};

Page.sv_unlockAppsForSessionUseronSuccess = function(variable, data) {
    console.log(data);
};