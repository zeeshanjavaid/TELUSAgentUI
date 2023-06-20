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

    App.showRowExpansionOrderManagement = function(row, data) {
        debugger;
        var type = row.stepTypeCode;
        Partial.Widgets.type.caption = row.stepTypeCode;
        Partial.Widgets.DueDate.caption = row.stepTypeCode;
        Partial.Widgets.BansToSuspend.caption = row.stepTypeCode;
        Partial.Widgets.assignedPerson.caption = row.stepTypeCode;
        Partial.Widgets.Comment.caption = row.stepTypeCode;
        Partial.Widgets.Description.caption = row.stepTypeCode;
        Partial.Widgets.Priority.caption = row.stepTypeCode;
        Partial.Widgets.assignedTeam.caption = row.stepTypeCode;
        Partial.Widgets.Status.caption = row.status;
        Partial.Widgets.ReasonCode.caption = row.stepTypeCode;
    }
};