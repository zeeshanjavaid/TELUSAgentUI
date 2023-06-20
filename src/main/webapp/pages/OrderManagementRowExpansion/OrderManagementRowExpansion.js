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
        if (type == 'SUSPEND') {
            Partial.Widgets.BansLabel.caption = 'BANs to Suspend:';
        } else if (type == 'RESTORE') {
            Partial.Widgets.BansLabel.caption = 'BANs to Restore:';
        } else if (type == 'CEASE') {
            Partial.Widgets.BansLabel.caption = 'BANs to Cease:';
        } else {
            Partial.Widgets.BansLabel.caption = 'BANs to ' + type + ':';
        }

        Partial.Widgets.type.caption = type;
        Partial.Widgets.DueDate.caption = row.stepDate;
        Partial.Widgets.BansLabelValue.caption = '';
        Partial.Widgets.assignedPerson.caption = row.assignedAgentId;
        Partial.Widgets.Comment.caption = row.comment;
        Partial.Widgets.Description.caption = '';
        Partial.Widgets.Priority.caption = row.priority;
        Partial.Widgets.assignedTeam.caption = row.assignedTeam;
        Partial.Widgets.Status.caption = row.status;
        Partial.Widgets.ReasonCode.caption = row.reasonCode;
    }
};