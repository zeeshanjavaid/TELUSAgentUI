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

    // Call Outbound Action 
    if (Partial.Widgets.select1.datavalue == 'Call Outbound') {
        Partial.Variables.actionName.dataValue = Partial.Widgets.select1.datavalue;
        // hiding create action dialog
        hideSelectActionForm();
        // displaying Call Outbound action form
        $('#callOutBoundActionForm').show();
    }


    // Call Inbound Action
    if (Partial.Widgets.select1.datavalue == 'Call Inbound') {
        Partial.Variables.actionName.dataValue = Partial.Widgets.select1.datavalue;
        // hiding create action dialog
        hideSelectActionForm();
        // displaying Call Outbound action form
        $('#callOutBoundActionForm').show();
        // displaying Call Inbound action form
        $('#callInBoundActionForm').show();
    }
};

Partial.cancelClick = function() {
    showSelectActionForm();
    // hiding Call Outbound action form
    $('#callOutBoundActionForm').hide();
    // hiding Call Inbound action form
    $('#callInBoundActionForm').hide();
};

// function created to hide select action form
function hideSelectActionForm() {
    $('#selectActionForm').hide();
    $('#selectActionBtns').hide();
    $('#createActionBtns').show();
};

// function created to show select action form
function showSelectActionForm() {
    $('#selectActionForm').show();
    $('#selectActionBtns').show();
    $('#createActionBtns').hide();
};