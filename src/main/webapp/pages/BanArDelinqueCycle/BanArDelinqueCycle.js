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


    App.showRowExpansionEntityDetails = function(row, data) {

        // const formatter = new Intl.NumberFormat('en-US', {
        //     style: 'currency',

        // });
        Partial.Widgets.ar30D.caption = row.ar30Days.toLocaleString('en-US');
        Partial.Widgets.ar60D.caption = row.ar60Days.toLocaleString('en-US');
        Partial.Widgets.ar90D.caption = row.ar90Days.toLocaleString('en-US');
        Partial.Widgets.ar120D.caption = row.ar120Days.toLocaleString('en-US');
        Partial.Widgets.ar150D.caption = row.ar150Days.toLocaleString('en-US');
        Partial.Widgets.ar180D.caption = row.ar180Days.toLocaleString('en-US');
        Partial.Widgets.ar180Dplus.caption = row.ar180DaysPlus.toLocaleString('en-US');
        Partial.Widgets.lastPaymentDate.caption = row.lastPaymentDate;

    }
};