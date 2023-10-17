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

    // Redirection to be controled via Left Menu First entry. Hard coded will be replaced as part of later stories  
    // window.location.href = "/DomainValueTypes";
    App.Actions.goToPage_DomainValueTypes.invoke();
};