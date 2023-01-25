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

    /* Note: This is done to make sure that any running intervals on the source page (or anywhere in the application) 
     * depeding upon this APP level timer variable are cleared immediately
     */
    let dateNow = new Date();
    dateNow.setDate(dateNow.getDate() - 1);
    App.appStartTime = dateNow;

    /* Note: This is important as in the source page the visibility is primarily hidden until
     * the user has access and hence the visibility continues to remain hidden, so resetting it
     * here
     */
    document.getElementsByTagName("html")[0].style.visibility = "visible";
};

Page.getErrorMessage = function() {
    let msg = '';

    if (!App.permissionsLoaded)
        msg = 'You are not authorised to access the page.';
    else if (!App.queueFiltersLoaded)
        msg = 'Failed to load Queue filter configuration.';
    else
        msg = 'Something went wrong while loading App metadata.';

    return msg;
};

Page.forceRefreshonSuccess = function(variable, data) {
    cache_utils.removeFromCache("SessionStorage", "Permissions", "APP_PERMISSIONS", App.Variables.PermissionsForLoggedInUserId)
    App.userPermission = null;
    App.permissionsLoaded = false;
};

Page.forceRefreshRolesonSuccess = function(variable, data) {
    cache_utils.removeFromCache("SessionStorage", "Permissions", "APP_PERMISSIONS", App.Variables.PermissionsForLoggedInUserId)
    App.userPermission = null;
    App.permissionsLoaded = false;
};