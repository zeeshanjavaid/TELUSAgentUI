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
    debugger;
    Page.Variables.UserLoggedInVar_ARAgent.dataSet.empId = App.Variables.getLoggedInUserDetails.dataSet.emplId;
    Page.Widgets.AssignedTeamSelect.datavalue = "ALL";
    Page.Variables.workCategoryValues_ARAgent.invoke();
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

Page.getAllActiveUserList_ARAgentViewonSuccess = function(variable, data) {
    if (Page.Variables.getAllActiveUserList_ARAgentView.dataSet.length > 1) {
        debugger;
        Page.Variables.getAllActiveUserList_ARAgentView.dataSet.unshift({
            empId: 'ALL',
            firstName: 'ALL',
            lastName: ''
        });
        Page.Variables.getAllActiveUserList_ARAgentView.dataSet = Page.Variables.getAllActiveUserList_ARAgentView.dataSet;
    }
};
Page.EntityOwnerSelectChange = function($event, widget, newVal, oldVal) {
    debugger;
    if (Page.Widgets.EntityOwnerSelect.datavalue == 'ALL') {
        Page.Variables.workCategoryValues_ARAgent.invoke();
    } else {
        Page.Variables.workcategoriesByEmpId_ARAgentView.setInput({
            'emplId': Page.Widgets.EntityOwnerSelect.datavalue
        });
        Page.Variables.workcategoriesByEmpId_ARAgentView.invoke();
    }
};

Page.workcategoriesByEmpId_ARAgentViewonSuccess = function(variable, data) {
    Page.Variables.workCategoryValues_ARAgent.dataSet = data;
};
Page.WorkCategorySelectChange = function($event, widget, newVal, oldVal) {
    debugger;
    var dropdown = document.getElementById('WorkCategorySelect');
    var selectedOptions = Page.Widgets.WorkCategorySelect.datavalue;
    var isAllSelected = selectedOptions.includes('ALL');
    var otherOptionsSelected = selectedOptions.length > 1;
    if (isAllSelected && otherOptionsSelected) {
        debugger;
        let valuesWithoutAll = selectedOptions.shift();
        Page.Widgets.WorkCategorySelect.datavalue = valuesWithoutAll;
    }
};

Page.workCategorySelect_ARAgentViewonSuccess = function(variable, data) {

    if (Page.Widgets.EntityOwnerSelect.datavalue == 'ALL') {
        Page.Variables.workCategoryValues_ARAgent.dataSet = data;
    }
};
Page.AssignedTeamSelectChange = function($event, widget, newVal, oldVal) {
    if (Page.Widgets.AssignedTeamSelect.datavalue == 'ALL') {
        Page.Variables.getAllActiveUserList_ARAgentViewonSuccess.invoke();
    } else {
        Page.Variables.getUserListByTeamId_ARAgentV.setInput({
            'teamId': Page.Widgets.AssignedTeamSelect.datavalue
        });
        Page.Variables.getUserListByTeamId_ARAgentV.invoke();
    }
};

Page.getUserListByTeamId_ARAgentVonSuccess = function(variable, data) {
    debugger;
    Page.Variables.getAllActiveUserList_ARAgentView.dataSet = data;
    if (data.length > 0) {
        if (data.length > 1) {
            debugger;
            Page.Variables.getAllActiveUserList_ARAgentView.dataSet.unshift({
                empId: 'ALL',
                firstName: 'ALL',
                lastName: ''
            });
            Page.Widgets.EntityOwnerSelect.datavalue = Page.Variables.getAllActiveUserList_ARAgentView.dataSet[0].empId;
        }
        Page.Widgets.EntityOwnerSelect.datavalue = Page.Variables.getAllActiveUserList_ARAgentView.dataSet[0].empId;
    }
};

// adding all in Assigned Team
Page.getAllTeamList_ARAgentViewonSuccess = function(variable, data) {
    if (Page.Variables.getAllTeamList_ARAgentView.dataSet.length > 1) {
        debugger;
        Page.Variables.getAllTeamList_ARAgentView.dataSet.unshift({
            id: 0,
            teamId: 'ALL',
            teamName: 'ALL'
        });
        Page.Variables.getAllTeamList_ARAgentView.dataSet = Page.Variables.getAllTeamList_ARAgentView.dataSet;
    }
};