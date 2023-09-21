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
    Page.Widgets.ActionTypeSelect.datavalue = "ALL";
    Page.Widgets.StatusSelect.datavalue = "ALL";
    Page.Variables.workCategoryValues_OrderDesk.invoke();
};

// function added to clear all the fields in the filter grid
Page.clearFilterFields = function($event, widget) {
    debugger;
    Page.Widgets.AssignedTeamSelect.datavalue = "ALL";
    Page.Widgets.AssignedPersonSelect.datavalue = "ALL";
    Page.Widgets.EntityOwnerSelect.datavalue = App.Variables.getLoggedInUserDetails.dataSet.emplId;
    Page.Widgets.WorkCategorySelect.datavalue = Page.Variables.workCategoryValues_OrderDesk.invoke();
    Page.Widgets.ActionTypeSelect.datavalue = "ALL";
    Page.Widgets.StatusSelect.datavalue = "ALL";
    Page.Widgets.creationDate.datavalue = "";
    Page.Widgets.completionDate.datavalue = "";
};

// function added to display table based on the filters applied
Page.applyFilter = function($event, widget) {
    debugger;
    var workCategoriesOD = Page.Widgets.WorkCategorySelect.datavalue;
    if (workCategoriesOD == '' || workCategoriesOD == undefined) {
        Page.Variables.errorMsg.dataSet.dataValue = 'Work Category is mandatory';
        setTimeout(messageTimeout, 10000);
    } else {
        if (workCategoriesOD.length > 1) {
            var finalWorkCategoriesOD = workCategoriesOD.join("|");
        } else {
            var finalWorkCategoriesOD = workCategoriesOD;
        }
        Page.Variables.CollectionDataServiceGetActionViewByTeam.setInput({
            'assignedTeam': Page.Widgets.AssignedTeamSelect.datavalue,
            'assignedAgent': Page.Widgets.AssignedPersonSelect.datavalue,
            'entityOwner': Page.Widgets.EntityOwnerSelect.datavalue,
            'workCategory': finalWorkCategoriesOD,
            'actionType': Page.Widgets.ActionTypeSelect.datavalue,
            'status': Page.Widgets.StatusSelect.datavalue,
            'fromDueDate': Page.Widgets.creationDate.datavalue,
            'toDueDate': Page.Widgets.completionDate.datavalue,
            'viewType ': '2'
        });

        Page.Variables.CollectionDataServiceGetActionViewByTeam.invoke();
    }
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

Page.CollectionDataServiceGetActionViewByTeamonError = function(variable, data, xhrObj) {

};

Page.actionTypeSelect_orderDeskViewonSuccess = function(variable, data) {
    if (Page.Variables.actionTypeSelect_orderDeskView.dataSet.length > 1) {
        Page.Variables.actionTypeSelect_orderDeskView.dataSet.unshift({
            id: 0,
            code: 'ALL',

        });
        Page.Variables.actionTypeSelect_orderDeskView.dataSet = Page.Variables.actionTypeSelect_orderDeskView.dataSet;
    }
};


Page.statusSelect_orderDeskViewonSuccess = function(variable, data) {
    if (Page.Variables.statusSelect_orderDeskView.dataSet.length > 1) {
        Page.Variables.statusSelect_orderDeskView.dataSet.unshift({
            id: 0,
            code: 'ALL',

        });
        Page.Variables.statusSelect_orderDeskView.dataSet = Page.Variables.statusSelect_orderDeskView.dataSet;
    }
};