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

$('document').ready(() => {
    document.getElementsByTagName("html")[0].style.visibility = "hidden";
});

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

    initPage();
};

function initPage() {
    const intervalId = setInterval(function() {
        if (App.permissionsLoaded) {
            clearInterval(intervalId);
            console.log('Permissions loaded...');

            if (!App.IsUserHasAccess('Setup_TimerSettings')) {
                window.location.href = '#/ErrorLanding';
            } else {
                document.getElementsByTagName("html")[0].style.visibility = "visible";
            }
        } else {
            //determining the time elapsed since App started in minutes
            const timeElapsedSinceAppStart = moment(new Date()).diff(moment(App.appStartTime), 'minutes');

            if (timeElapsedSinceAppStart < 1)
                console.log('Waiting to load permissions...');
            else {
                clearInterval(intervalId);

                //if the active page is not 'ErrorLanding'
                if (window.location.hash !== '#/ErrorLanding')
                    window.location.href = '#/ErrorLanding';
            }
        }
    }, 10);
}

Page.ScheduleJobonError = function(variable, data, xhrObj) {
    var errorObj = JSON.parse(xhrObj.error);
    var errorMessage = errorObj;
    Page.Actions.TimerFailed.setMessage(errorMessage);
    Page.Actions.TimerFailed.invoke();
};

Page.ScheduleJobonSuccess = function(variable, data) {
    Page.Actions.TimerSuccess.setMessage(data.value);
    Page.Actions.TimerSuccess.invoke();
};

Page.UpdateScheduledJobonError = function(variable, data, xhrObj) {
    var errorObj = JSON.parse(xhrObj.error);
    var errorMessage = errorObj;
    Page.Actions.TimerFailed.setMessage(errorMessage);
    Page.Actions.TimerFailed.invoke();
};

Page.UpdateScheduledJobonSuccess = function(variable, data) {
    Page.Actions.TimerSuccess.setMessage(data.value);
    Page.Actions.TimerSuccess.invoke();
};

Page.deleteJobonError = function(variable, data, xhrObj) {
    var errorObj = JSON.parse(xhrObj.error);
    var errorMessage = errorObj;
    Page.Actions.TimerFailed.setMessage(errorMessage);
    Page.Actions.TimerFailed.invoke();
};

Page.deleteJobonSuccess = function(variable, data) {
    Page.Actions.TimerSuccess.setMessage(data.value);
    Page.Actions.TimerSuccess.invoke();
};

Page.ActivateJobonSuccess = function(variable, data) {
    Page.Actions.TimerSuccess.setMessage(data.value);
    Page.Actions.TimerSuccess.invoke();
};

Page.DeActivateJobonSuccess = function(variable, data) {
    Page.Actions.TimerSuccess.setMessage(data.value);
    Page.Actions.TimerSuccess.invoke();
};

Page.RunNowonSuccess = function(variable, data) {
    Page.Actions.TimerSuccess.setMessage(data.value);
    Page.Actions.TimerSuccess.invoke();
};

Page.ActivateJobonError = function(variable, data, xhrObj) {
    var errorObj = JSON.parse(xhrObj.error);
    var errorMessage = errorObj;
    Page.Actions.TimerFailed.setMessage(errorMessage);
    Page.Actions.TimerFailed.invoke();
};

Page.DeActivateJobonError = function(variable, data, xhrObj) {
    var errorObj = JSON.parse(xhrObj.error);
    var errorMessage = errorObj;
    Page.Actions.TimerFailed.setMessage(errorMessage);
    Page.Actions.TimerFailed.invoke();
};

Page.RunNowonError = function(variable, data, xhrObj) {
    var errorObj = JSON.parse(xhrObj.error);
    var errorMessage = errorObj;
    Page.Actions.TimerFailed.setMessage(errorMessage);
    Page.Actions.TimerFailed.invoke();
};