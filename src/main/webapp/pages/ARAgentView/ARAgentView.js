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

    Page.Variables.getLoggedInUserTeamForBanView.invoke();


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
        'viewType': '1',
        'limit': 20,
        'offset': 0
    });
    Page.Variables.CollectionDataServiceGetActionViewByTeam.invoke();


};

Page.assignActionsButtonClick = function($event, widget) {
    debugger;
    Page.Widgets.assignActionsDialog.open();
};

Page.assignActionsButtonClick1 = function($event, widget) {
    debugger;
    Page.Variables.dialogErrorMsg.dataSet.dataValue = null;
    if (Page.Widgets.getActionViewByTeamTable1.selectedItems.length > 0) {

        var selectedItems = Page.Widgets.getActionViewByTeamTable1.selectedItems;
        var firstAssignedTeam = selectedItems[0].assignedTeam;
        var allSameAssignedTeam = selectedItems.every(function(item) {
            return item.assignedTeam === firstAssignedTeam;
        });

        var closedStatusSelected = selectedItems.some(function(item) {
            return item.status.toLowerCase() == "closed";
        });

        if (!allSameAssignedTeam) {
            Page.Variables.dialogErrorMsg.dataSet.dataValue = "Please select actions belonging to same Team";
        } else if (closedStatusSelected) {
            Page.Variables.dialogErrorMsg.dataSet.dataValue = "Please select actions whose status is not CLOSED";
        } else {
            Page.Variables.selectedTeamId = selectedItems[0].assignedTeam;
        }
    } else {
        // Display an error message in a popup
        Page.Variables.dialogErrorMsg.dataSet.dataValue = "Please select an Action for Assignment";
        //        alert("Please select at least one item.");
    }
    //Page.Widgets.assignActionsDialog.open();
    Page.Variables.assignedActionsDialogOpened = true;
};

Page.dialogConfirmButtonClick = function($event, widget) {
    // Show the spinner before the for loop starts
    debugger;
    Page.Variables.showSpinner = true;
    var selectedItems = Page.Widgets.getActionViewByTeamTable1.selectedItems;
    // Extracting IDs using map function
    var selectedIds = selectedItems.map(item => "" + item.actionId);
    var partitionKeys = selectedItems.map(item => "" + item.partitionKey);
    try {
        Page.Variables.bulkStepsUpdate.setInput({
            'ids': selectedIds,
            'partitionKeys': partitionKeys,
            "collectionTreatmentStepUpdate": {
                'assignedAgentId': Page.Widgets.AssignedPersonSelectDialog.datavalue,
                'assignedTeam': Page.Widgets.AssignedTeamSelectDialog.datavalue,
                'channel': {
                    'originatorAppId': "FAWBTELUSAGENT",
                    'channelOrgId': "FAWBTELUSAGENT",
                    'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
                }
            }
        });
        // Invoke POST coll treatment service
        Page.Variables.bulkStepsUpdate.invoke();
    } catch (error) {
        debugger;
        console.error("Error invoking service for actionId:", selectedItem.actionId, error);
        // Handle the error as needed
    }
    debugger;
};

Page.bulkStepsUpdateonError = function(variable, data, xhrObj) {
    debugger;
};

Page.bulkStepsUpdateonSuccess = function(variable, data) {
    debugger;
    var selectedItems = Page.Widgets.getActionViewByTeamTable1.selectedItems;
    var totalActions = selectedItems.length;
    var failedCount = parseInt(data.value);
    var successCount = totalActions - failedCount;
    if (failedCount > 0) {
        Page.Widgets.dialogErrorMessage.type = 'error';
    } else {
        Page.Widgets.dialogErrorMessage.type = 'success';
    }
    Page.Variables.dialogErrorMsg.dataSet.dataValue = "Total Actions = " + totalActions + "<br>Success = " + successCount + "<br>Failed = " + failedCount;
    //Page.Variables.dialogErrorMsg.dataSet.dataValue = "Total Actions = " + totalActions + "\nSuccess = " + successCount + "\nFailed = " + failedCount;
    // Hide the spinner after the for loop completes
    setTimeout(App.refreshCollActionList, 1000);
    Page.Variables.showSpinner = false;
};

Page.dialogOkButtonClick = function($event, widget) {
    debugger;
    Page.Widgets.assignActionsDialog.close();
    //Page.Variables.assignedActionsDialogOpened = false;
};

App.refreshCollActionList = function() {
    debugger;
    Page.Variables.assignedActionsDialogOpened = false;
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
        'viewType': '1',
        'limit': 20,
        'offset': 0
    });
    Page.Variables.CollectionDataServiceGetActionViewByTeam.invoke();
};


Page.dialogCancelButtonClick = function($event, widget) {
    debugger;
    Page.Widgets.assignActionsDialog.close();
    Page.Variables.assignedActionsDialogOpened = false;
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
            'viewType': '1',
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
Page.AssignedTeamSelectChange = function($event, widget, newVal, oldVal) {
    /*    debugger;
        if (Page.Widgets.AssignedTeamSelect.datavalue == 'ALL') {
            Page.Variables.getAllActiveUserList_ARAgentView_forALL.invoke();
        } else if (Page.Widgets.AssignedTeamSelect.datavalue == 'NULL') {
            Page.Variables.getAllActiveUserList_ARAgentView_forALL.invoke();
        } else {
            Page.Variables.getUserListByTeamId_ARAgentV.setInput({
                'teamId': Page.Widgets.AssignedTeamSelect.datavalue
            });
            Page.Variables.getUserListByTeamId_ARAgentV.invoke();
        }*/
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
    var entityOwner = App.Variables.getLoggedInUserDetails.dataSet.emplId;
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

    var teamName = Page.Variables.getLoggedInUserTeamForBanView.dataSet;

    for (let i = 0; i < teamName.length; i++) {
        if (teamName[i].teamId === 'TIG AR') {
            entityOwner = 'NULL';
        }
    }

    Page.Variables.CollectionDataServiceGetActionViewByTeam.setInput({
        'entityOwner': entityOwner,
        'workCategory': finalWCARview,
        'viewType': '1',
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

Page.getAllActiveUserList_ARAgentViewDialog_forALLonSuccess = function(variable, data) {
    debugger;
    if (Page.Variables.getAllActiveUserList_ARAgentViewDialog_forALL.dataSet.length > 1) {
        Page.Variables.getAllActiveUserList_ARAgentViewDialog_forALL.dataSet.unshift({
            empId: 'ALL',
            firstName: 'ALL',
            lastName: ''
        });
        Page.Variables.getAllActiveUserList_ARAgentViewDialog.dataSet = data;
        Page.Widgets.AssignedPersonSelectDialog.datavalue = Page.Variables.getAllActiveUserList_ARAgentViewDialog_forALL.dataSet[0].empId;
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