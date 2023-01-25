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

/* Actvity log page loads all the available activity logs which takes considerable amount of time
   So before invoking the search added a filter to only filter the activity logs of current date starting from midnight
*/
Page.ActivitylogFilter1Beforeservicecall = function($data) {
    setFicoIdFromPageParam($data);
    if (!$data.activitylogname.value &&
        !$data.activitylogtype.value &&
        !$data.activitypayload.maxValue && !$data.activitypayload.minValue &&
        !$data['activitypayload.timeTakeninMilliSec'].maxValue && !$data['activitypayload.timeTakeninMilliSec'].minValue &&
        !$data['application.userRecordId'].value &&
        !$data.applicationId.value &&
        !$data.description.value) {
        var date = new Date();
        // comment this if it is required to be reflacted in the widget as well
        Page.Widgets.ActivitylogFilter1.filterWidgets.activitypayload.maxValue = date.getTime();
        $data.activitypayload.maxValue = date.getTime();
        date.setHours(0, 0, 0, 0); // reset to midnight
        $data.activitypayload.minValue = date.getTime();
        Page.Widgets.ActivitylogFilter1.filterWidgets.activitypayload.minValue = date.getTime();
    }
};