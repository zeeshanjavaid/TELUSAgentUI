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
};

Partial.CreateActionLinkClick = function($event, widget) {

    Partial.Widgets.CreateActionPopOver.hidePopover();
    Partial.Widgets.SelectActionDialog.open();

};


Partial.nextButtonClick = function($event, widget) {
    Partial.Widgets.select1.datavalue;

    // Call Outbound
    if (Partial.Widgets.select1.datavalue == 'Call Outbound') {
        Partial.Variables.actionName.dataValue = Partial.Widgets.select1.datavalue;
        // hiding create action dialog
        hideCreateActionDialog();
        // displaying Call Outbound action form
        $('#callOutBoundAction').show();
    }


    // Call Inbound
    if (Partial.Widgets.select1.datavalue == 'Call Inbound') {
        Partial.Variables.actionName.dataValue = Partial.Widgets.select1.datavalue;
        // hiding create action dialog
        hideCreateActionDialog();
        // displaying Call Outbound action form
        $('#callOutBoundAction').show();
        // displaying Call Inbound action form
        $('#callInBoundAction').show();
    }
};

Partial.cancelClick = function() {
    showCreateActionDialog();
    // hiding Call Outbound action form
    $('#callOutBoundAction').hide();
    // hiding Call Inbound action form
    $('#callInBoundAction').hide();
};

// function created to hide create action dialog
function hideCreateActionDialog() {
    $('#actionGrid').hide();
    $('#createActionSelectBox').hide();
    $('#nextBtn').hide();
    $('#createBtn').show();
};

// function created to show create action dialog
function showCreateActionDialog() {
    $('#actionGrid').show();
    $('#createActionSelectBox').show();
    $('#nextBtn').show();
    $('#createBtn').hide();
};