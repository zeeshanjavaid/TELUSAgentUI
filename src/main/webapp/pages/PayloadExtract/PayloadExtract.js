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


Page.svExtractPayloadonError = function(variable, data) {
    Page.Variables.mv_payloadData.setData({
        "request": null,
        "response": null
    });
};

Page.svExtractPayloadonSuccess = function(variable, data) {
    Page.Variables.mv_payloadData.setData({
        "request": data.requestContent,
        "response": data.responseContent
    });
};

Page.button3Click = function($event, widget) {
    Page.Variables.mv_payloadData.setData({
        "request": null,
        "response": null
    });
    Page.Variables.contentToShow.setValue('dataValue', null);

    Page.Variables.svExtractPayload.setInput({
        "activityPayloadId": Page.Widgets.payloadIDInput.datavalue
    });

    Page.Variables.svExtractPayload.invoke();
};

Page.anchor1Click = function($event, widget) {
    if (Page.Variables.mv_payloadData.dataSet.request) {
        try {
            let content = JSON.stringify(JSON.parse(App.JSONPrettify(Page.Variables.mv_payloadData.dataSet.request)), undefined, 4);
            Page.Variables.contentToShow.setValue('dataValue', "<pre><code>" + content + "</code></pre>");
        } catch (err) {
            console.error(err);
            Page.Variables.contentToShow.setValue('dataValue', "<pre>Some exception occurred while formatting data!</pre>");
        }
    } else
        Page.Variables.contentToShow.setValue('dataValue', "<pre/>");
};

Page.anchor2Click = function($event, widget) {
    if (Page.Variables.mv_payloadData.dataSet.response) {
        try {
            let content = JSON.stringify(JSON.parse(App.JSONPrettify(Page.Variables.mv_payloadData.dataSet.response)), undefined, 4);
            Page.Variables.contentToShow.setValue('dataValue', "<pre><code>" + content + "</code></pre>");
        } catch (err) {
            console.error(err);
            Page.Variables.contentToShow.setValue('dataValue', "<pre>Some exception occurred while formatting data!</pre>");
        }
    } else
        Page.Variables.contentToShow.setValue('dataValue', "<pre/>");
};