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
    Page.Variables.UserLoggedInVar_home.dataSet.empId = App.Variables.getLoggedInUserDetails.dataSet.emplId;
    Page.Widgets.AssignedTeamSelectEV.datavalue = "All";
    Page.Widgets.AssignedTeamSelectBV.datavalue = "All";
    $('#banViewTableGrid').hide();
    $('#filterGridBanView').hide();
    $("#entityViewBtn").css("background-color", "#4B286D");
    $("#entityViewBtn").css("color", "white");

};


Page.entityViewButtonClick = function($event, widget) {
    debugger;
    Page.Variables.UserLoggedInVar_home.dataSet.empId = App.Variables.getLoggedInUserDetails.dataSet.emplId;

    // to make buttons selected
    $("#entityViewBtn").css("background-color", "#4B286D");
    $("#entityViewBtn").css("color", "white");
    $("#banViewBtn").css("background-color", "white");
    $("#banViewBtn").css("color", "#4B286D");

    // display entity view table and hide ban table
    $('#entityViewTableGrid').show();
    $('#banViewTableGrid').hide();

    // display filter grid for entity view and hide for ban view
    $('#filterGridEntityView').show();
    $('#filterGridBanView').hide();

};

Page.banViewButtonClick = function($event, widget) {
    debugger;
    Page.Variables.UserLoggedInVar_home.dataSet.empId = App.Variables.getLoggedInUserDetails.dataSet.emplId;

    // to make buttons selected
    $("#banViewBtn").css("background-color", "#4B286D");
    $("#banViewBtn").css("color", "white");
    $("#entityViewBtn").css("background-color", "white");
    $("#entityViewBtn").css("color", "#4B286D");

    // display ban table and hide entity view table
    $('#banViewTableGrid').show();
    $('#entityViewTableGrid').hide();

    // display filter grid for ban view and hide for entity view
    $('#filterGridBanView').show();
    $('#filterGridEntityView').hide();

};

// function added to clear all the fields in the filter for Entity View
Page.clearFilterFieldsEntityView = function($event, widget) {
    Page.Widgets.AssignedTeamSelectEV.datavalue = "";
    Page.Widgets.portfolioSelectEV.datavalue = "";
    Page.Widgets.includeCurrentCreditSelectEV.datavalue = "Y";
    Page.Widgets.entityOwnerSelectEV.datavalue = "";
    Page.Widgets.billingSystemSelectEV.datavalue = "CES";
    Page.Widgets.ARExcludedInternalSelectEV.datavalue = "Y";
    Page.Widgets.workCategorySelectEV.datavalue = "";
    Page.Widgets.collStatusSelectEV.datavalue = "";
}

// function added to clear all the fields in the filter for Ban View
Page.clearFilterFieldsBanView = function($event, widget) {
    Page.Widgets.AssignedTeamSelectBV.datavalue = "";
    Page.Widgets.portfolioSelectBV.datavalue = "";
    Page.Widgets.includeCurrentCreditSelectBV.datavalue = "Y";
    Page.Widgets.entityOwnerSelectBV.datavalue = "";
    Page.Widgets.billingSystemSelectBV.datavalue = "CES";
    Page.Widgets.ARExcludedInternalSelectBV.datavalue = "Y";
    Page.Widgets.workCategorySelectBV.datavalue = "";
    Page.Widgets.collStatusSelectBV.datavalue = "";
}

// function added to display table based on the filters for entity view
Page.applyFiltersEntityView = function($event, widget) {
    debugger;
    Page.Widgets.AssignedTeamSelectEV.datavalue;
    Page.Widgets.portfolioSelectEV.datavalue;
    Page.Widgets.includeCurrentCreditSelectEV.datavalue;
    Page.Widgets.entityOwnerSelectEV.datavalue;
    Page.Widgets.billingSystemSelectEV.datavalue;
    Page.Widgets.ARExcludedInternalSelectEV.datavalue;
    Page.Widgets.workCategorySelectEV.datavalue;
    Page.Widgets.collStatusSelectEV.datavalue;
}

// function added to display table based on the filters for ban view
Page.applyFiltersBanView = function($event, widget) {
    debugger;
    Page.Widgets.AssignedTeamSelectBV.datavalue;
    Page.Widgets.portfolioSelectBV.datavalue;
    Page.Widgets.includeCurrentCreditSelectBV.datavalue;
    Page.Widgets.entityOwnerSelectBV.datavalue;
    Page.Widgets.billingSystemSelectBV.datavalue;
    Page.Widgets.ARExcludedInternalSelectBV.datavalue;
    Page.Widgets.workCategorySelectBV.datavalue;
    Page.Widgets.collStatusSelectBV.datavalue;
}

Page.goToEnityPage = function(row) {
    window.open("#/Lookup?entityId=" + (!row.entityId ? 0 : row.entityId), "_blank");
}

Page.entityViewTable_OnRowexpand = function($event, widget, row, $data) {
    App.showRowExpansionEntityViewHome(row, $data);
};

Page.banViewTable_OnRowexpand = function($event, widget, row, $data) {
    App.showRowExpansionBanViewHome(row, $data);
};

Page.assignedTeamSelectEV_onChange = function($event, widget, newVal, oldVal) {
    debugger;
    if (Page.Widgets.AssignedTeamSelectEV.datavalue == 'All') {
        Page.Variables.getAllActiveUserList_HomeEV.invoke();
    } else {
        Page.Variables.getUserListByTeamId_homeEV.setInput({
            'teamId': Page.Widgets.AssignedTeamSelectEV.datavalue
        });
        Page.Variables.getUserListByTeamId_homeEV.invoke();
    }
};

Page.assignedTeamSelectBV_onChange = function($event, widget, newVal, oldVal) {
    debugger;
    if (Page.Widgets.AssignedTeamSelectBV.datavalue == 'All') {
        Page.Variables.getAllActiveUserList_HomeBV.invoke();
    } else {
        Page.Variables.getUserListByTeamId_homeBV.setInput({
            'teamId': Page.Widgets.AssignedTeamSelectBV.datavalue
        });
        Page.Variables.getUserListByTeamId_homeBV.invoke();
    }
};

Page.entityOwnerEVSelectOn_Change = function($event, widget, newVal, oldVal) {
    debugger;
    if (Page.Widgets.entityOwnerSelectEV.datavalue == 'All') {
        Page.Variables.workCategorySelect_HomeEV.invoke();
    } else {
        Page.Variables.workcategoriesByEmpId_homeEV.setInput({
            'emplId': Page.Widgets.entityOwnerSelectEV.datavalue
        });
        Page.Variables.workcategoriesByEmpId_homeEV.invoke();
    }
};

Page.entityOwnerBVSelectOn_Change = function($event, widget, newVal, oldVal) {
    debugger;
    if (Page.Widgets.entityOwnerSelectBV.datavalue == 'All') {
        Page.Variables.workCategorySelect_HomeBV.invoke();
    } else {
        Page.Variables.workcategoriesByEmpId_homeBV.setInput({
            'emplId': Page.Widgets.entityOwnerSelectBV.datavalue
        });
        Page.Variables.workcategoriesByEmpId_homeBV.invoke();
    }
};

Page.workCategorySelectEVChange = function($event, widget, newVal, oldVal) {
    debugger;
    var dropdown = document.getElementById('workCategorySelectEV');
    var selectedOptions = Page.Widgets.workCategorySelectEV.datavalue;
    var isAllSelected = selectedOptions.includes('All');
    var otherOptionsSelected = selectedOptions.length > 1;
    if (isAllSelected && otherOptionsSelected) {
        debugger;
        let valuesWithoutAll = selectedOptions.shift();
        Page.Widgets.workCategorySelectEV.datavalue = valuesWithoutAll;
    }
};



// adding 'All' in the dropdown list for assignedTeam dropdown for ENTITY VIEW
Page.getTeamList_HomeEVonSuccess = function(variable, data) {
    debugger;
    if (Page.Variables.getTeamList_HomeEV.dataSet.length > 1) {
        debugger;
        Page.Variables.getTeamList_HomeEV.dataSet.unshift({
            id: 0,
            teamId: 'All',
            teamName: 'All'
        });
        Page.Variables.getTeamList_HomeEV.dataSet = Page.Variables.getTeamList_HomeEV.dataSet;
    }
};

// adding 'All' in the dropdown list for assignedTeam dropdown for BAN VIEW
Page.getTeamList_HomeBVonSuccess = function(variable, data) {
    debugger;
    if (Page.Variables.getTeamList_HomeBV.dataSet.length > 1) {
        debugger;
        Page.Variables.getTeamList_HomeBV.dataSet.unshift({
            id: 0,
            teamId: 'All',
            teamName: 'All'
        });
        Page.Variables.getTeamList_HomeBV.dataSet = Page.Variables.getTeamList_HomeBV.dataSet;
    }
};

// adding 'All' in the dropdown list for entityOwner dropdown for ENTITY VIEW
Page.getAllActiveUserList_HomeEVonSuccess = function(variable, data) {
    debugger;
    if (Page.Variables.getAllActiveUserList_HomeEV.dataSet.length > 1) {
        debugger;
        Page.Variables.getAllActiveUserList_HomeEV.dataSet.unshift({
            empId: 'All',
            firstName: 'All',
            lastName: ''
        });
        Page.Variables.getAllActiveUserList_HomeEV.dataSet = Page.Variables.getAllActiveUserList_HomeEV.dataSet;
    }
};

// adding 'All' in the dropdown list for entityOwner dropdown for BAN VIEW
Page.getAllActiveUserList_HomeBVonSuccess = function(variable, data) {
    debugger;
    if (Page.Variables.getAllActiveUserList_HomeBV.dataSet.length > 1) {
        debugger;
        Page.Variables.getAllActiveUserList_HomeBV.dataSet.unshift({
            empId: 'All',
            firstName: 'All',
            lastName: ''
        });
        Page.Variables.getAllActiveUserList_HomeBV.dataSet = Page.Variables.getAllActiveUserList_HomeBV.dataSet;
    }
};

// adding 'All' in the dropdown list for workCategory dropdown for ENTITY VIEW
Page.workCategoryValues_HomeEVonSuccess = function(variable, data) {
    debugger;
    if (Page.Variables.workCategoryValues_HomeEV.dataSet.length > 1) {
        debugger;
        Page.Variables.workCategoryValues_HomeEV.dataSet.unshift({
            code: "All"
        });
        Page.Variables.workCategoryValues_HomeEV.dataSet = Page.Variables.workCategoryValues_HomeEV.dataSet;
    }
};

// adding 'All' in the dropdown list for workCategory dropdown for BAN VIEW
Page.workCategoryValues_HomeBVonSuccess = function(variable, data) {
    debugger;
    if (Page.Variables.workCategoryValues_HomeBV.dataSet.length > 1) {
        debugger;
        Page.Variables.workCategoryValues_HomeBV.dataSet.unshift({
            code: "All"
        });
        Page.Variables.workCategoryValues_HomeBV.dataSet = Page.Variables.workCategoryValues_HomeBV.dataSet;
    }
};


Page.workCategorySelect_HomeEVonSuccess = function(variable, data) {
    if (Page.Variables.workCategorySelect_HomeEV.dataSet.length > 1) {
        Page.Variables.workCategorySelect_HomeEV.dataSet.unshift({
            code: "All"
        });
        Page.Variables.workCategoryValues_HomeEV.dataSet = Page.Variables.workCategorySelect_HomeEV.dataSet;
    }
};


Page.workCategorySelect_HomeBVonSuccess = function(variable, data) {
    if (Page.Variables.workCategorySelect_HomeBV.dataSet.length > 1) {
        Page.Variables.workCategorySelect_HomeBV.dataSet.unshift({
            code: "All"
        });
        Page.Variables.workCategoryValues_HomeBV.dataSet = Page.Variables.workCategorySelect_HomeBV.dataSet;
    }
};


Page.getUserListByTeamId_homeEVonSuccess = function(variable, data) {
    debugger;
    Page.Variables.getAllActiveUserList_HomeEV.dataSet = data;
    if (data.length > 0) {
        if (data.length > 1) {
            debugger;
            Page.Variables.getAllActiveUserList_HomeEV.dataSet.unshift({
                empId: 'All',
                firstName: 'All',
                lastName: ''
            });
            Page.Widgets.entityOwnerSelectEV.datavalue = Page.Variables.getAllActiveUserList_HomeEV.dataSet[0].empId;
        }
        Page.Widgets.entityOwnerSelectEV.datavalue = Page.Variables.getAllActiveUserList_HomeEV.dataSet[0].empId;
    }
};

Page.getUserListByTeamId_homeBVonSuccess = function(variable, data) {
    debugger;
    Page.Variables.getAllActiveUserList_HomeBV.dataSet = data;
    if (data.length > 0) {
        if (data.length > 1) {
            Page.Variables.getAllActiveUserList_HomeBV.dataSet.unshift({
                empId: 'All',
                firstName: 'All',
                lastName: ''
            });
            Page.Widgets.entityOwnerSelectBV.datavalue = Page.Variables.getAllActiveUserList_HomeBV.dataSet[0].empId;
        }
        Page.Widgets.entityOwnerSelectBV.datavalue = Page.Variables.getAllActiveUserList_HomeBV.dataSet[0].empId;
    }
};

Page.workcategoriesByEmpId_homeEVonSuccess = function(variable, data) {
    debugger;
    Page.Variables.workCategoryValues_HomeEV.dataSet = data;
    if (data.length > 1) {
        Page.Variables.workCategoryValues_HomeEV.dataSet.unshift({
            code: "All"
        });
    }

};

Page.workcategoriesByEmpId_homeBVonSuccess = function(variable, data) {
    debugger;
    Page.Variables.workCategoryValues_HomeBV.dataSet = data;
    if (data.length > 1) {
        Page.Variables.workCategoryValues_HomeBV.dataSet.unshift({
            code: "All"
        });
    }

};