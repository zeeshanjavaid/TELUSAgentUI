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
    Page.Widgets.AssignedTeamSelectEV.datavalue = "ALL";
    Page.Widgets.AssignedTeamSelectBV.datavalue = "ALL";
    $('#banViewTableGrid').hide();
    $('#filterGridBanView').hide();
    $("#entityViewBtn").css("background-color", "#4B286D");
    $("#entityViewBtn").css("color", "white");
    Page.Variables.workCategoryValues_HomeEV.invoke();
    Page.Variables.workCategoryValues_HomeBV.invoke();

};


Page.entityViewButtonClick = function($event, widget) {
    Page.Variables.UserLoggedInVar_home.dataSet.empId = App.Variables.getLoggedInUserDetails.dataSet.emplId;
    Page.Variables.hideBanTableGrid.dataValue = false;
    Page.Variables.hideBanFilterGrid.dataValue = false;

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
    Page.Variables.UserLoggedInVar_home.dataSet.empId = App.Variables.getLoggedInUserDetails.dataSet.emplId;
    Page.Variables.hideBanTableGrid.dataValue = true;
    Page.Variables.hideBanFilterGrid.dataValue = true;

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
    Page.Widgets.AssignedTeamSelectEV.datavalue = "ALL";
    Page.Widgets.portfolioSelectEV.datavalue = "ALL";
    Page.Widgets.includeCurrentCreditSelectEV.datavalue = "Y";
    Page.Widgets.entityOwnerSelectEV.datavalue = App.Variables.getLoggedInUserDetails.dataSet.emplId;
    Page.Widgets.billingSystemSelectEV.datavalue = "CES9";
    Page.Widgets.ARExcludedInternalSelectEV.datavalue = "Y";
    Page.Widgets.workCategorySelectEV.datavalue = Page.Variables.workCategoryValues_HomeEV.invoke();
    Page.Widgets.collStatusSelectEV.datavalue = "ALL";
}

// function added to clear all the fields in the filter for Ban View
Page.clearFilterFieldsBanView = function($event, widget) {
    Page.Widgets.AssignedTeamSelectBV.datavalue = "ALL";
    Page.Widgets.portfolioSelectBV.datavalue = "ALL";
    Page.Widgets.includeCurrentCreditSelectBV.datavalue = "Y";
    Page.Widgets.entityOwnerSelectBV.datavalue = App.Variables.getLoggedInUserDetails.dataSet.emplId;
    Page.Widgets.billingSystemSelectBV.datavalue = "CES9";
    Page.Widgets.ARExcludedInternalSelectBV.datavalue = "Y";
    Page.Widgets.workCategorySelectBV.datavalue = Page.Variables.workCategoryValues_HomeBV.invoke();
    Page.Widgets.collStatusSelectBV.datavalue = "ALL";
}

// function added to display table based on the filters for entity view
Page.applyFiltersEntityView = function($event, widget) {
    debugger;

    var workCategoriesEV = Page.Widgets.workCategorySelectEV.datavalue;
    if (workCategoriesEV) {
        if (workCategoriesEV.length > 1) {
            var finalWorkCategoriesEV = workCategoriesEV.join("|");
        } else {
            var finalWorkCategoriesEV = workCategoriesEV;
        }
    }
    Page.Variables.CollectionDataServiceGetAssignedEntitiesInEntityView3.setInput({
        'entityOwner': Page.Widgets.entityOwnerSelectEV.datavalue,
        'workCategory': finalWorkCategoriesEV,
        'portfolio': Page.Widgets.portfolioSelectEV.datavalue,
        'billingSystem': Page.Widgets.billingSystemSelectEV.datavalue,
        'collectionStatus': Page.Widgets.collStatusSelectEV.datavalue

    });
    Page.Variables.CollectionDataServiceGetAssignedEntitiesInEntityView3.invoke();

}

// function added to display table based on the filters for ban view
Page.applyFiltersBanView = function($event, widget) {
    debugger;

    var workCategoriesBV = Page.Widgets.workCategorySelectEV.datavalue;
    if (workCategoriesBV) {
        if (workCategoriesBV.length > 1) {
            var finalWorkCategoriesBV = workCategoriesBV.join("|");
        } else {
            var finalWorkCategoriesBV = workCategoriesBV;
        }
    }
    Page.Variables.CollectionDataServiceGetassignedEntitiesInClassicView2.setInput({
        'entityOwner': Page.Widgets.entityOwnerSelectBV.datavalue,
        'workCategory': finalWorkCategoriesBV,
        'portfolio': Page.Widgets.portfolioSelectBV.datavalue,
        'billingSystem': Page.Widgets.billingSystemSelectBV.datavalue,
        'collectionStatus': Page.Widgets.collStatusSelectBV.datavalue

    });
    Page.Variables.CollectionDataServiceGetassignedEntitiesInClassicView2.invoke();
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
    if (Page.Widgets.AssignedTeamSelectEV.datavalue == 'ALL') {
        Page.Variables.getAllActiveUserList_HomeEV.invoke();
    } else {
        Page.Variables.getUserListByTeamId_homeEV.setInput({
            'teamId': Page.Widgets.AssignedTeamSelectEV.datavalue
        });
        Page.Variables.getUserListByTeamId_homeEV.invoke();
    }
};

Page.assignedTeamSelectBV_onChange = function($event, widget, newVal, oldVal) {
    if (Page.Widgets.AssignedTeamSelectBV.datavalue == 'ALL') {
        Page.Variables.getAllActiveUserList_HomeBV.invoke();
    } else {
        Page.Variables.getUserListByTeamId_homeBV.setInput({
            'teamId': Page.Widgets.AssignedTeamSelectBV.datavalue
        });
        Page.Variables.getUserListByTeamId_homeBV.invoke();
    }
};

Page.entityOwnerEVSelectOn_Change = function($event, widget, newVal, oldVal) {
    if (Page.Widgets.entityOwnerSelectEV.datavalue == 'ALL') {
        Page.Variables.workCategorySelect_HomeEV.invoke();
    } else if (Page.Widgets.entityOwnerSelectEV.datavalue == 'NULL') {
        Page.Variables.workcategoriesByEmpId_homeEV.setInput({
            'emplId': App.Variables.getLoggedInUserDetails.dataSet.emplId
        });
        Page.Variables.workcategoriesByEmpId_homeEV.invoke();
    } else {
        Page.Variables.workcategoriesByEmpId_homeEV.setInput({
            'emplId': Page.Widgets.entityOwnerSelectEV.datavalue
        });
        Page.Variables.workcategoriesByEmpId_homeEV.invoke();
    }
};

Page.entityOwnerBVSelectOn_Change = function($event, widget, newVal, oldVal) {
    if (Page.Widgets.entityOwnerSelectBV.datavalue == 'ALL') {
        Page.Variables.workCategorySelect_HomeBV.invoke();
    } else if (Page.Widgets.entityOwnerSelectBV.datavalue == 'NULL') {
        Page.Variables.workcategoriesByEmpId_homeBV.setInput({
            'emplId': App.Variables.getLoggedInUserDetails.dataSet.emplId
        });
        Page.Variables.workcategoriesByEmpId_homeBV.invoke();
    } else {
        Page.Variables.workcategoriesByEmpId_homeBV.setInput({
            'emplId': Page.Widgets.entityOwnerSelectBV.datavalue
        });
        Page.Variables.workcategoriesByEmpId_homeBV.invoke();
    }
};

Page.workCategorySelectEVChange = function($event, widget, newVal, oldVal) {};

Page.workCategorySelectBVChange = function($event, widget, newVal, oldVal) {};


// adding 'All' in the dropdown list for assignedTeam dropdown for ENTITY VIEW
Page.getTeamList_HomeEVonSuccess = function(variable, data) {
    if (Page.Variables.getTeamList_HomeEV.dataSet.length > 1) {
        Page.Variables.getTeamList_HomeEV.dataSet.unshift({
            id: 0,
            teamId: 'ALL',
            teamName: 'ALL'
        });
        Page.Variables.getTeamList_HomeEV.dataSet = Page.Variables.getTeamList_HomeEV.dataSet;
    }
};

// adding 'All' in the dropdown list for assignedTeam dropdown for BAN VIEW
Page.getTeamList_HomeBVonSuccess = function(variable, data) {
    if (Page.Variables.getTeamList_HomeBV.dataSet.length > 1) {
        Page.Variables.getTeamList_HomeBV.dataSet.unshift({
            id: 0,
            teamId: 'ALL',
            teamName: 'ALL'
        });
        Page.Variables.getTeamList_HomeBV.dataSet = Page.Variables.getTeamList_HomeBV.dataSet;
    }
};

// adding 'All' in the dropdown list for entityOwner dropdown for ENTITY VIEW
Page.getAllActiveUserList_HomeEVonSuccess = function(variable, data) {
    if (Page.Variables.getAllActiveUserList_HomeEV.dataSet.length > 1) {
        Page.Variables.getAllActiveUserList_HomeEV.dataSet.unshift({
            empId: 'ALL',
            firstName: 'ALL',
            lastName: ''
        });
        Page.Variables.getAllActiveUserList_HomeEV.dataSet = Page.Variables.getAllActiveUserList_HomeEV.dataSet;
    }
};

// adding 'All' in the dropdown list for entityOwner dropdown for BAN VIEW
Page.getAllActiveUserList_HomeBVonSuccess = function(variable, data) {
    if (Page.Variables.getAllActiveUserList_HomeBV.dataSet.length > 1) {
        Page.Variables.getAllActiveUserList_HomeBV.dataSet.unshift({
            empId: 'ALL',
            firstName: 'ALL',
            lastName: ''
        });
        Page.Variables.getAllActiveUserList_HomeBV.dataSet = Page.Variables.getAllActiveUserList_HomeBV.dataSet;
    }
};

Page.workCategoryValues_HomeEVonSuccess = function(variable, data) {
    Page.Variables.workCategoryValues_HomeEV.dataSet = [];
    Page.Variables.workCategoryValues_HomeEV.dataSet = data;

};

Page.workCategoryValues_HomeBVonSuccess = function(variable, data) {
    Page.Variables.workCategoryValues_HomeBV.dataSet = [];
    Page.Variables.workCategoryValues_HomeBV.dataSet = data;
};


Page.workCategorySelect_HomeEVonSuccess = function(variable, data) {
    if (Page.Widgets.entityOwnerSelectEV.datavalue == 'ALL') {
        Page.Variables.workCategoryValues_HomeEV.dataSet = data;
    }
};


Page.workCategorySelect_HomeBVonSuccess = function(variable, data) {
    if (Page.Widgets.entityOwnerSelectBV.datavalue == 'ALL') {
        Page.Variables.workCategoryValues_HomeBV.dataSet = data;
    }
};


Page.getUserListByTeamId_homeEVonSuccess = function(variable, data) {
    Page.Variables.getAllActiveUserList_HomeEV.dataSet = data;
    if (data.length > 0) {
        if (data.length > 1) {
            Page.Variables.getAllActiveUserList_HomeEV.dataSet.unshift({
                empId: 'ALL',
                firstName: 'ALL',
                lastName: ''
            });
            Page.Widgets.entityOwnerSelectEV.datavalue = Page.Variables.getAllActiveUserList_HomeEV.dataSet[0].empId;
        }
        Page.Widgets.entityOwnerSelectEV.datavalue = Page.Variables.getAllActiveUserList_HomeEV.dataSet[0].empId;
    }
};

Page.getUserListByTeamId_homeBVonSuccess = function(variable, data) {
    Page.Variables.getAllActiveUserList_HomeBV.dataSet = data;
    if (data.length > 0) {
        if (data.length > 1) {
            Page.Variables.getAllActiveUserList_HomeBV.dataSet.unshift({
                empId: 'ALL',
                firstName: 'ALL',
                lastName: ''
            });
            Page.Widgets.entityOwnerSelectBV.datavalue = Page.Variables.getAllActiveUserList_HomeBV.dataSet[0].empId;
        }
        Page.Widgets.entityOwnerSelectBV.datavalue = Page.Variables.getAllActiveUserList_HomeBV.dataSet[0].empId;
    }
};

Page.workcategoriesByEmpId_homeEVonSuccess = function(variable, data) {
    Page.Variables.workCategoryValues_HomeEV.dataSet = data;
};

Page.workcategoriesByEmpId_homeBVonSuccess = function(variable, data) {
    Page.Variables.workCategoryValues_HomeBV.dataSet = data;
};