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

var workCategoryDataArray = [];
var actionTypeDataArray = [];
var statusTypeDataArray = [];
var finalWorkCategoriesAR;
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
    Page.Widgets.AssignedPersonSelect.datavalue = "ALL";
    Page.Widgets.ActionTypeSelect.datavalue = "ALL";
    Page.Widgets.StatusSelect.datavalue = "ALL";
    Page.Variables.workCategoryValues_ARAgent.invoke();
    Page.Variables.workcategoriesByEmpId_ARAgentView.setInput({
        'emplId': App.Variables.getLoggedInUserDetails.dataSet.emplId
    });
    Page.Variables.workcategoriesByEmpId_ARAgentView.invoke();



};

function messageTimeout() {
    Page.Variables.errorMsg.dataSet.dataValue = null;
}

// function added to clear all the fields in the filter grid
Page.clearFilterFields = function($event, widget) {
    debugger;
    Page.Widgets.AssignedTeamSelect.datavalue = "ALL";
    Page.Widgets.AssignedPersonSelect.datavalue = "ALL";
    Page.Widgets.EntityOwnerSelect.datavalue = App.Variables.getLoggedInUserDetails.dataSet.emplId;
    Page.Widgets.WorkCategorySelect.datavalue = Page.Variables.workCategoryValues_ARAgent.invoke();
    Page.Widgets.ActionTypeSelect.datavalue = "ALL";
    Page.Widgets.StatusSelect.datavalue = "ALL";
    Page.Widgets.creationDate.datavalue = "";
    Page.Widgets.completionDate.datavalue = "";

    if (workCategoryDataArray.length > 1) {
        finalWorkCategoriesAR = workCategoryDataArray.join("|");
    } else {
        finalWorkCategoriesAR = workCategoryDataArray;
    }

    Page.Variables.CollectionDataServiceGetActionViewByTeam.setInput({
        'assignedTeam': '',
        'assignedAgent': '',
        'entityOwner': App.Variables.getLoggedInUserDetails.dataSet.emplId,
        'workCategory': finalWorkCategoriesAR,
        'actionType': '',
        'status': '',
        'fromDueDate': '',
        'toDueDate': '',
        'viewType ': '1',
        'limit': 20,
        'offset': 0
    });
    Page.Variables.CollectionDataServiceGetActionViewByTeam.invoke();


};

// function added to display table based on the filters applied
Page.applyFilter = function($event, widget) {
    debugger;
    // Your code when both creationDate and completionDate have valid values.
    if (Page.Widgets.creationDate.datavalue !== '' && Page.Widgets.creationDate.datavalue !== undefined &&
        Page.Widgets.completionDate.datavalue !== '' && Page.Widgets.completionDate.datavalue !== undefined) {

        var creationDateValue = new Date(Page.Widgets.creationDate.bsDataValue.toDateString());
        var completionDateValue = new Date(Page.Widgets.completionDate.bsDataValue.toDateString());

        if (completionDateValue.valueOf() < creationDateValue.valueOf()) {

            Page.Variables.errorMsg.dataSet.dataValue = "Created To Due Date Can Not Be Less Than Created From Due Date";
            setTimeout(messageTimeout, 4000);
        }

    }
    workCategoryDataArray = Page.Widgets.WorkCategorySelect.datavalue;
    if (workCategoryDataArray == '' || workCategoryDataArray == undefined) {
        Page.Variables.errorMsg.dataSet.dataValue = 'Work Category is mandatory';
        setTimeout(messageTimeout, 4000);
    } else {
        if (workCategoryDataArray.length > 1) {
            var finalWorkCategoriesAR = workCategoryDataArray.join("|");
        } else {
            var finalWorkCategoriesAR = workCategoryDataArray;
        }

        Page.Variables.CollectionDataServiceGetActionViewByTeam.setInput({
            'assignedTeam': Page.Widgets.AssignedTeamSelect.datavalue,
            'assignedAgent': Page.Widgets.AssignedPersonSelect.datavalue,
            'entityOwner': Page.Widgets.EntityOwnerSelect.datavalue,
            'workCategory': finalWorkCategoriesAR,
            'actionType': Page.Widgets.ActionTypeSelect.datavalue,
            'status': Page.Widgets.StatusSelect.datavalue,
            'fromDueDate': Page.Widgets.creationDate.datavalue,
            'toDueDate': Page.Widgets.completionDate.datavalue,
            'viewType ': '1',
            'limit': 20,
            'offset': 0
        });
        Page.Variables.CollectionDataServiceGetActionViewByTeam.invoke();
    }
};

Page.goToEnityPage = function(row) {
    window.open("Lookup?entityId=" + (!row.entityId ? 0 : row.entityId), "_blank");
}

// assigned Team on Change
debugger;
Page.AssignedTeamSelectChange = function($event, widget, newVal, oldVal) {
    if (Page.Widgets.AssignedTeamSelect.datavalue == 'ALL') {
        Page.Variables.getAllActiveUserList_ARAgentView_forALL.invoke();
    } else if (Page.Widgets.AssignedTeamSelect.datavalue == 'NULL') {
        Page.Variables.getAllActiveUserList_ARAgentView_forALL.invoke();
    } else {
        Page.Variables.getUserListByTeamId_ARAgentV.setInput({
            'teamId': Page.Widgets.AssignedTeamSelect.datavalue
        });
        Page.Variables.getUserListByTeamId_ARAgentV.invoke();
    }
};

// adding all in Assigned Team
Page.getAllTeamList_ARAgentViewonSuccess = function(variable, data) {
    debugger;
    if (Page.Variables.getAllTeamList_ARAgentView.dataSet.length > 1) {
        Page.Variables.getAllTeamList_ARAgentView.dataSet.unshift({
            id: 0,
            teamId: 'ALL',
            teamName: 'ALL'
        });
        Page.Variables.getAllTeamList_ARAgentView.dataSet = Page.Variables.getAllTeamList_ARAgentView.dataSet;
    }
};

// adding 'All' in the dropdown list for entityOwner dropdown 
Page.getAllActiveUserList_ARAgentViewonSuccess = function(variable, data) {
    debugger;
    if (Page.Variables.getAllActiveUserList_ARAgentView.dataSet.length > 1) {
        Page.Variables.getAllActiveUserList_ARAgentView.dataSet.unshift({
            empId: 'ALL',
            firstName: 'ALL',
            lastName: ''
        });
        Page.Variables.getAllActiveUserList_ARAgentView.dataSet = Page.Variables.getAllActiveUserList_ARAgentView.dataSet;
    }
};

Page.getUserListByTeamId_ARAgentVonSuccess = function(variable, data) {
    debugger;
    Page.Variables.getAllActiveUserList_ARAgentView.dataSet = data;
    if (data.length > 0) {
        if (data.length > 1) {
            Page.Variables.getAllActiveUserList_ARAgentView.dataSet.unshift({
                empId: 'ALL',
                firstName: 'ALL',
                lastName: ''
            });
            Page.Widgets.EntityOwnerSelect.datavalue = Page.Variables.getAllActiveUserList_ARAgentView.dataSet[0].empId;
            Page.Widgets.AssignedPersonSelect.datavalue = Page.Variables.getAllActiveUserList_ARAgentView.dataSet[0].empId;
        }
        Page.Widgets.EntityOwnerSelect.datavalue = Page.Variables.getAllActiveUserList_ARAgentView.dataSet[0].empId;
        Page.Widgets.AssignedPersonSelect.datavalue = Page.Variables.getAllActiveUserList_ARAgentView.dataSet[0].empId;

    }
};

Page.workcategoriesByEmpId_ARAgentViewonSuccess = function(variable, data) {
    debugger;
    Page.Variables.workCategoryValues_ARAgent.dataSet = data;
    if (data != undefined) {
        data.forEach(workCategoryData);
        workCategoryDataArray = [];
        data.forEach(function(item) {
            workCategoryDataArray.push(item.code)

        });
    }

    if (workCategoryDataArray.length > 1) {
        var finalWCARview = workCategoryDataArray.join("|");
    } else {
        var finalWCARview = workCategoryDataArray;
    }

    if (Page.Variables.actionTypeSelect_ARAgentView.dataSet != undefined) {
        Page.Variables.actionTypeSelect_ARAgentView.dataSet.forEach(actionTypeData);
    }

    if (Page.Variables.statusSelect_ARAgentView.dataSet != undefined) {
        Page.Variables.statusSelect_ARAgentView.dataSet.forEach(statusTypeData);
    }

    Page.Variables.CollectionDataServiceGetActionViewByTeam.setInput({
        'entityOwner': App.Variables.getLoggedInUserDetails.dataSet.emplId,
        'workCategory': finalWCARview,
        'viewType ': '1',
        'limit': 20,
        'offset': 0
    });
    Page.Variables.CollectionDataServiceGetActionViewByTeam.invoke();
};

function workCategoryData(item, index) {
    var item = item;
    workCategoryDataArray.push(item.code);
};

function actionTypeData(item, index) {
    var item = item;
    actionTypeDataArray.push(item.code);
};


function statusTypeData(item, index) {
    var item = item;
    statusTypeDataArray.push(item.code);
};



Page.workCategorySelect_ARAgentViewonSuccess = function(variable, data) {
    debugger;
    if (Page.Widgets.EntityOwnerSelect.datavalue == 'ALL') {
        Page.Variables.workCategoryValues_ARAgent.dataSet = data;
    }
};

Page.workCategoryValues_ARAgentonSuccess = function(variable, data) {
    debugger;
    Page.Variables.workCategoryValues_ARAgent.dataSet = [];
    Page.Variables.workCategoryValues_ARAgent.dataSet = data;

};

Page.getAllActiveUserList_ARAgentView_forALLonSuccess = function(variable, data) {
    debugger;
    if (Page.Variables.getAllActiveUserList_ARAgentView_forALL.dataSet.length > 1) {
        Page.Variables.getAllActiveUserList_ARAgentView_forALL.dataSet.unshift({
            empId: 'ALL',
            firstName: 'ALL',
            lastName: ''
        });
        Page.Variables.getAllActiveUserList_ARAgentView.dataSet = data;
        Page.Widgets.EntityOwnerSelect.datavalue = Page.Variables.getAllActiveUserList_ARAgentView_forALL.dataSet[0].empId;
        Page.Widgets.AssignedPersonSelect.datavalue = Page.Variables.getAllActiveUserList_ARAgentView_forALL.dataSet[0].empId;
    }
};

Page.CollectionDataServiceGetActionViewByTeamonError = function(variable, data, xhrObj) {

};


// adding 'All' in the dropdown list for actiontype dropdown
Page.actionTypeSelect_ARAgentViewonSuccess = function(variable, data) {
    debugger;
    if (Page.Variables.actionTypeSelect_ARAgentView.dataSet.length > 1) {
        Page.Variables.actionTypeSelect_ARAgentView.dataSet.unshift({
            id: 0,
            code: 'ALL',

        });
        Page.Variables.actionTypeSelect_ARAgentView.dataSet = Page.Variables.actionTypeSelect_ARAgentView.dataSet;
    }
};


// adding 'All' in the dropdown list for selectstatus dropdown
Page.statusSelect_ARAgentViewonSuccess = function(variable, data) {
    debugger;
    if (Page.Variables.statusSelect_ARAgentView.dataSet.length > 1) {
        Page.Variables.statusSelect_ARAgentView.dataSet.unshift({
            id: 0,
            code: 'ALL',
        });
        Page.Variables.statusSelect_ARAgentView.dataSet = Page.Variables.statusSelect_ARAgentView.dataSet;
    }
};


Page.Telus_PaginatonPagechange = function($event, $data) {
    debugger;
    Page.size = $event.pageSize
    Page.page = $event.pageNumber
    Page.RefreshData();

};

Page.RefreshData = function() {
    debugger;
    var offset = Page.size * (Page.page - 1);
    Page.Variables.CollectionDataServiceGetActionViewByTeam.setInput({
        'limit': Page.size,
        'offset': offset
    });
    Page.Variables.CollectionDataServiceGetActionViewByTeam.invoke();

}