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

// function added to clear all the fields in the filter grid
Page.clearFilterFields = function($event, widget) {
    Page.Widgets.AssignedTeamSelect.datavalue = "All";
    Page.Widgets.AssignedPersonSelect.datavalue = "All";
    Page.Widgets.EntityOwnerSelect.datavalue = "All";
    Page.Widgets.WorkCategorySelect.datavalue = "All";
    Page.Widgets.ActionTypeSelect.datavalue = "All";
    Page.Widgets.StatusSelect.datavalue = "All";
    Page.Widgets.creationDate.datavalue = "";
    Page.Widgets.completionDate.datavalue = "";
};

Page.applyFilter = function($event, widget) {

    Page.Variables.CollectionDataServiceGetActionViewByTeam.setInput({

        'assignedTeam': Page.Widgets.AssignedTeamSelect.datavalue,
        'assignedAgent': Page.Widgets.AssignedPersonSelect.datavalue,
        'entityOwner': Page.Widgets.EntityOwnerSelect.datavalue,
        'workCategory': Page.Widgets.WorkCategorySelect.datavalue,
        'actionType': Page.Widgets.ActionTypeSelect.datavalue,
        'status': Page.Widgets.StatusSelect.datavalue,
        'fromDueDate': Page.Widgets.creationDate.datavalue,
        'toDueDate': Page.Widgets.completionDate.datavalue,

    });

    Page.Variables.CollectionDataServiceGetActionViewByTeam.invoke();
};

Page.goToEnityPage = function(row) {
    window.open("#/Lookup?entityId=" + (!row.entityId ? 0 : row.entityId), "_blank");
}