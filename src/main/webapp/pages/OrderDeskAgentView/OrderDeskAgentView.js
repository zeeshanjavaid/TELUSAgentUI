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
var finalWorkCategoriesOD;
Page.onReady = function() {
    debugger;
    /*
     * variables can be accessed through 'Page.Variables' property here
     * e.g. to get dataSet in a staticVariable named 'loggedInUser' use following script
     * Page.Variables.loggedInUser.getData()
     *
     * widgets can be accessed through 'Page.Widgets' property here
     * e.g. to get value of text widget named 'username' use following script
     * 'Page.Widgets.username.datavalue'
     */

    Page.Variables.getLoggedInUserTeamForBanView.invoke();

    Page.Variables.UserLoggedInVar_OrderDesk.dataSet.empId = App.Variables.getLoggedInUserDetails.dataSet.emplId;
    Page.Widgets.AssignedTeamSelect.datavalue = "ALL";
    Page.Widgets.AssignedPersonSelect.datavalue = "ALL";
    Page.Widgets.ActionTypeSelect.datavalue = "ALL";
    Page.Widgets.StatusSelect.datavalue = "ALL";
    Page.Variables.workCategoryValues_OrderDesk.invoke();
    Page.Variables.workcategoriesByEmpId_OrderDesk.setInput({
        'emplId': App.Variables.getLoggedInUserDetails.dataSet.emplId
    });
    Page.Variables.workcategoriesByEmpId_OrderDesk.invoke();
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
    Page.Widgets.WorkCategorySelect.datavalue = Page.Variables.workCategoryValues_OrderDesk.invoke();
    Page.Widgets.ActionTypeSelect.datavalue = "ALL";
    Page.Widgets.StatusSelect.datavalue = "ALL";
    Page.Widgets.creationDate.datavalue = "";
    Page.Widgets.completionDate.datavalue = "";

    if (workCategoryDataArray.length > 1) {
        finalWorkCategoriesOD = workCategoryDataArray.join("|");
    } else {
        finalWorkCategoriesOD = workCategoryDataArray;
    }

    Page.Variables.CollectionDataServiceGetActionViewByTeam.setInput({
        'assignedTeam': '',
        'assignedAgent': '',
        'entityOwner': App.Variables.getLoggedInUserDetails.dataSet.emplId,
        'workCategory': finalWorkCategoriesOD,
        'actionType': '',
        'status': '',
        'fromDueDate': '',
        'toDueDate': '',
        'viewType ': '2',
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



    var workCategoryDataArray = Page.Widgets.WorkCategorySelect.datavalue;
    if (workCategoryDataArray == '' || workCategoryDataArray == undefined) {
        Page.Variables.errorMsg.dataSet.dataValue = 'Work Category is mandatory';
        setTimeout(messageTimeout, 4000);
    } else {
        if (workCategoryDataArray.length > 1) {
            var finalWorkCategoriesOD = workCategoryDataArray.join("|");
        } else {
            var finalWorkCategoriesOD = workCategoryDataArray;
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
            'viewType ': '2',
            'limit': 20,
            'offset': 0
        });

        Page.Variables.CollectionDataServiceGetActionViewByTeam.invoke();
    }
};

Page.goToEnityPage = function(row) {
    window.open("Lookup?entityId=" + (!row.entityId ? 0 : row.entityId), "_blank");
}

// Assigned Team on change
Page.AssignedTeamSelecton_Change = function($event, widget, newVal, oldVal) {
    debugger;
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
    debugger;
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

Page.workcategoriesByEmpId_OrderDeskonSuccess = function(variable, data) {
    debugger;
    var entityOwner = App.Variables.getLoggedInUserDetails.dataSet.emplId;
    Page.Variables.workCategoryValues_OrderDesk.dataSet = data;
    if (data != undefined) {
        data.forEach(workCategoryData);
        workCategoryDataArray = [];
        data.forEach(function(item) {
            workCategoryDataArray.push(item.code)

        });
    }

    if (workCategoryDataArray.length > 1) {
        var finalWCODview = workCategoryDataArray.join("|");
    } else {
        var finalWCODview = workCategoryDataArray;
    }

    if (Page.Variables.actionTypeSelect_orderDeskView.dataSet != undefined) {
        Page.Variables.actionTypeSelect_orderDeskView.dataSet.forEach(actionTypeData);
    }

    if (Page.Variables.statusSelect_orderDeskView.dataSet != undefined) {
        Page.Variables.statusSelect_orderDeskView.dataSet.forEach(statusTypeData);
    }

    var teamName = Page.Variables.getLoggedInUserTeamForBanView.dataSet;

    for (let i = 0; i < teamName.length; i++) {
        if (teamName[i].teamId === 'TIG AR') {
            entityOwner = null;
        }
    }

    Page.Variables.CollectionDataServiceGetActionViewByTeam.setInput({
        'entityOwner': entityOwner,
        'workCategory': finalWCODview,
        'viewType ': '2',
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


Page.workCategorySelect_OrderDeskViewonSuccess = function(variable, data) {
    debugger;
    if (Page.Widgets.EntityOwnerSelect.datavalue == 'ALL') {
        Page.Variables.workCategoryValues_OrderDesk.dataSet = data;
    }
};

Page.workCategoryValues_OrderDeskonSuccess = function(variable, data) {
    debugger;
    Page.Variables.workCategoryValues_OrderDesk.dataSet = [];
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
    debugger;
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