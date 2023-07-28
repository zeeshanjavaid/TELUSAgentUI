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
    Page.Variables.UserLoggedInVar_OrderDesk.dataSet.empId = App.Variables.getLoggedInUserDetails.dataSet.emplId;
    Page.Widgets.AssignedTeamSelect.datavalue = "ALL";
    Page.Widgets.AssignedPersonSelect.datavalue = "ALL";
    Page.Variables.workCategoryValues_OrderDesk.invoke();
};

// function added to clear all the fields in the filter grid
Page.clearFilterFields = function($event, widget) {
    debugger;
    Page.Widgets.AssignedTeamSelect.datavalue = "All";
    Page.Widgets.AssignedPersonSelect.datavalue = "All";
    Page.Widgets.EntityOwnerSelect.datavalue = "All";
    Page.Widgets.WorkCategorySelect.datavalue = "All";
    Page.Widgets.ActionTypeSelect.datavalue = "All";
    Page.Widgets.StatusSelect.datavalue = "All";
    Page.Widgets.creationDate.datavalue = "";
    Page.Widgets.completionDate.datavalue = "";
};

// function added to display table based on the filters applied
Page.applyFilter = function($event, widget) {
    debugger;
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

// Assigned Team on change
Page.AssignedTeamSelecton_Change = function($event, widget, newVal, oldVal) {
    if (Page.Widgets.AssignedTeamSelect.datavalue == 'ALL') {
        Page.Variables.getAllActiveUserList_OrderDesk_forALL.invoke();
    } else if (Page.Widgets.AssignedTeamSelect.datavalue == 'NULL') {
        Page.Variables.getAllActiveUserList_OrderDesk_forALL.invoke();
    } else {
        Page.Variables.getUserListByTeamId_OrderDesk.setInput({
            'teamId': Page.Widgets.AssignedTeamSelect.datavalue
        });
        Page.Variables.getUserListByTeamId_OrderDesk.invoke();
    }
};

// adding all in Assigned Team
Page.getAllTeamList_OrderDeskViewonSuccess = function(variable, data) {
    if (Page.Variables.getAllTeamList_OrderDeskView.dataSet.length > 1) {
        Page.Variables.getAllTeamList_OrderDeskView.dataSet.unshift({
            id: 0,
            teamId: 'ALL',
            teamName: 'ALL'
        });
        Page.Variables.getAllTeamList_OrderDeskView.dataSet = Page.Variables.getAllTeamList_OrderDeskView.dataSet;
    }
};

// adding 'All' in the dropdown list for entityOwner dropdown
Page.getAllActiveUserList_OrderDeskonSuccess = function(variable, data) {
    if (Page.Variables.getAllActiveUserList_OrderDesk.dataSet.length > 1) {
        Page.Variables.getAllActiveUserList_OrderDesk.dataSet.unshift({
            empId: 'ALL',
            firstName: 'ALL',
            lastName: ''
        });
        Page.Variables.getAllActiveUserList_OrderDesk.dataSet = Page.Variables.getAllActiveUserList_OrderDesk.dataSet;
    }
};

Page.getUserListByTeamId_OrderDeskonSuccess = function(variable, data) {
    Page.Variables.getAllActiveUserList_OrderDesk.dataSet = data;
    if (data.length > 0) {
        if (data.length > 1) {
            Page.Variables.getAllActiveUserList_OrderDesk.dataSet.unshift({
                empId: 'ALL',
                firstName: 'ALL',
                lastName: ''
            });
            Page.Widgets.EntityOwnerSelect.datavalue = Page.Variables.getAllActiveUserList_OrderDesk.dataSet[0].empId;
            Page.Widgets.AssignedPersonSelect.datavalue = Page.Variables.getAllActiveUserList_OrderDesk.dataSet[0].empId;
        }
        Page.Widgets.EntityOwnerSelect.datavalue = Page.Variables.getAllActiveUserList_OrderDesk.dataSet[0].empId;
        Page.Widgets.AssignedPersonSelect.datavalue = Page.Variables.getAllActiveUserList_OrderDesk.dataSet[0].empId;
    }
};

Page.workCategoryValues_OrderDeskonSuccess = function(variable, data) {
    Page.Variables.workCategoryValues_OrderDesk.dataSet = [];
    Page.Variables.workCategoryValues_OrderDesk.dataSet = data;
};

Page.workCategorySelect_OrderDeskViewonSuccess = function(variable, data) {
    if (Page.Widgets.EntityOwnerSelect.datavalue == 'ALL') {
        Page.Variables.workCategoryValues_OrderDesk.dataSet = data;
    }
};

Page.workcategoriesByEmpId_OrderDeskonSuccess = function(variable, data) {
    Page.Variables.workCategoryValues_OrderDesk.dataSet = data;
};

Page.getAllActiveUserList_OrderDesk_forALLonSuccess = function(variable, data) {
    if (Page.Variables.getAllActiveUserList_OrderDesk_forALL.dataSet.length > 1) {
        Page.Variables.getAllActiveUserList_OrderDesk_forALL.dataSet.unshift({
            empId: 'ALL',
            firstName: 'ALL',
            lastName: ''
        });
        Page.Variables.getAllActiveUserList_OrderDesk.dataSet = data;
        Page.Widgets.EntityOwnerSelect.datavalue = Page.Variables.getAllActiveUserList_OrderDesk_forALL.dataSet[0].empId;
        Page.Widgets.AssignedPersonSelect.datavalue = Page.Variables.getAllActiveUserList_OrderDesk_forALL.dataSet[0].empId;
    }
};