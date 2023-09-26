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
var portfolioDataArray = [];
var billingSystemDataArray = [];
var collStatusDataArray = [];
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

    //      For multi Select work category


    Page.statusData = [];

    App.Variables.getWorkCatByEmplIdForMultiSelectList.dataSet

    Page.Variables.getWorkCatByEmplIdForMultiSelectList.dataSet.forEach(function(item) {
        Page.statusData.push({
            // id: item.code.replace(/\s/g, ''),
            id: item.code,
            title: item.code
        });
    });

    subComboBox = $('#WorkCategoryMutliSel').comboTree({
        source: Page.statusData,
        isMultiple: true,
        // selected: false,

        // cascadeSelect: true,
        // collapse: true
    });



    subComboBox1 = $('#WorkCategoryMutliSelForBanView').comboTree({
        source: Page.statusData,
        isMultiple: true,
        // selected: false,

        // cascadeSelect: true,
        // collapse: true
    });

    Page.Variables.errorMsg.dataSet.dataValue = '';
    Page.Variables.UserLoggedInVar_home.dataSet.empId = App.Variables.getLoggedInUserDetails.dataSet.emplId;
    Page.Widgets.AssignedTeamSelectEV.datavalue = "ALL";
    Page.Widgets.AssignedTeamSelectBV.datavalue = "ALL";
    Page.Widgets.portfolioSelectEV.datavalue = "ALL";
    Page.Widgets.portfolioSelectBV.datavalue = "ALL";
    Page.Widgets.collStatusSelectEV.datavalue = "ALL";
    Page.Widgets.collStatusSelectBV.datavalue = "ALL";
    Page.Widgets.billingSystemSelectEV.datavalue = "CES9";
    Page.Widgets.billingSystemSelectBV.datavalue = "CES9";
    $('#banViewTableGrid').hide();
    $('#filterGridBanView').hide();
    $("#entityViewBtn").css("background-color", "#4B286D");
    $("#entityViewBtn").css("color", "white");
    Page.Variables.workCategoryValues_HomeEV.invoke();
    setTimeout(messageTimeout, 10000);
    Page.Variables.workCategoryValues_HomeBV.invoke();

    Page.Variables.workcategoriesByEmpId_homeBV.setInput({
        'emplId': App.Variables.getLoggedInUserDetails.dataSet.emplId
    });
    Page.Variables.workcategoriesByEmpId_homeBV.invoke();

    Page.Variables.workcategoriesByEmpId_homeEV.setInput({
        'emplId': App.Variables.getLoggedInUserDetails.dataSet.emplId
    });
    Page.Variables.workcategoriesByEmpId_homeEV.invoke();



};

function messageTimeout() {
    Page.Variables.errorMsg.dataSet.dataValue = null;
}

Page.entityViewButtonClick = function($event, widget) {
    Page.Variables.errorMsg.dataSet.dataValue = '';
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
    Page.Variables.errorMsg.dataSet.dataValue = '';
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

    debugger;
    Page.Widgets.AssignedTeamSelectEV.datavalue = "ALL";
    Page.Widgets.portfolioSelectEV.datavalue = "ALL";
    Page.Widgets.includeCurrentCreditSelectEV.datavalue = "Y";
    Page.Widgets.entityOwnerSelectEV.datavalue = App.Variables.getLoggedInUserDetails.dataSet.emplId;
    Page.Widgets.billingSystemSelectEV.datavalue = "CES9";
    /*Page.Widgets.ARExcludedInternalSelectEV.datavalue = "Y";*/
    // Page.Widgets.workCategorySelectEV.datavalue = Page.Variables.workCategoryValues_HomeEV.invoke();
    Page.Widgets.collStatusSelectEV.datavalue = "ALL";

    subComboBox.clearSelection();
    checkedItem = $("input:checked")
    checkedItem.prop('checked', false)

    if (workCategoryDataArray.length > 1) {
        var finalWorkCategoriesBV = workCategoryDataArray.join("|");
    } else {
        var finalWorkCategoriesBV = workCategoryDataArray;
    }
    Page.Variables.CollectionDataServiceGetAssignedEntitiesInEntityView3.setInput({
        'entityOwner': App.Variables.getLoggedInUserDetails.dataSet.emplId,
        'workCategory': finalWorkCategoriesBV,
        'portfolio': 'ALL',
        'billingSystem': 'CES9',
        'collectionStatus': 'ALL'

    });
    Page.Variables.CollectionDataServiceGetAssignedEntitiesInEntityView3.invoke();





}

// function added to clear all the fields in the filter for Ban View
Page.clearFilterFieldsBanView = function($event, widget) {
    debugger;
    Page.Widgets.AssignedTeamSelectBV.datavalue = "ALL";
    Page.Widgets.portfolioSelectBV.datavalue = "ALL";
    Page.Widgets.includeCurrentCreditSelectBV.datavalue = "Y";
    Page.Widgets.entityOwnerSelectBV.datavalue = App.Variables.getLoggedInUserDetails.dataSet.emplId;
    Page.Widgets.billingSystemSelectBV.datavalue = "CES9";
    /* Page.Widgets.ARExcludedInternalSelectBV.datavalue = "Y";*/
    // Page.Widgets.workCategorySelectBV.datavalue = Page.Variables.workCategoryValues_HomeBV.invoke();
    Page.Widgets.collStatusSelectBV.datavalue = "ALL";
    subComboBox1.clearSelection();
    checkedItem = $("input:checked")
    checkedItem.prop('checked', false)

    if (workCategoryDataArray.length > 1) {
        var finalWorkCategoriesBV = workCategoryDataArray.join("|");
    } else {
        var finalWorkCategoriesBV = workCategoryDataArray;
    }
    Page.Variables.CollectionDataServiceGetassignedEntitiesInClassicView2.setInput({
        'entityOwner': App.Variables.getLoggedInUserDetails.dataSet.emplId,
        'workCategory': finalWorkCategoriesBV,
        'portfolio': 'ALL',
        'billingSystem': 'CES9',
        'collectionStatus': 'ALL'

    });
    Page.Variables.CollectionDataServiceGetassignedEntitiesInClassicView2.invoke();


}

// function added to display table based on the filters for entity view
Page.applyFiltersEntityView = function($event, widget) {
    debugger;
    var workCategoriesEV = subComboBox.getSelectedIds();

    // var workCategoriesEV = subComboBox._selectedItems.map(({
    //     id
    // }) => id);
    if (workCategoriesEV == '' || workCategoriesEV == undefined) {
        Page.Variables.errorMsg.dataSet.dataValue = 'Work Category is mandatory';
        setTimeout(messageTimeout, 10000);
    } else {
        if (workCategoriesEV.length > 1) {
            var finalWorkCategoriesEV = workCategoriesEV.join("|");
        } else {
            var finalWorkCategoriesEV = workCategoriesEV;
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


}

// function added to display table based on the filters for ban view
Page.applyFiltersBanView = function($event, widget) {
    debugger;
    var workCategoriesBV = subComboBox1.getSelectedIds();
    if (workCategoriesBV == '' || workCategoriesBV == undefined) {
        Page.Variables.errorMsg.dataSet.dataValue = 'Work Category is mandatory';
        setTimeout(messageTimeout, 10000);
    } else {
        if (workCategoriesBV.length > 1) {
            var finalWorkCategoriesBV = workCategoriesBV.join("|");
        } else {
            var finalWorkCategoriesBV = workCategoriesBV;
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

}

Page.goToEnityPage = function(row) {

    debugger;
    // event.preventDefault();
    setTimeout(messageTimeout, 10000);

    window.open("#/Lookup?entityId=" + (!row.entityId ? 0 : row.entityId), "_blank");
    event.preventDefault();





    // window.location.reload(true);
};

Page.entityViewTable_OnRowexpand = function($event, widget, row, $data) {
    App.showRowExpansionEntityViewHome(row, $data);
};

Page.banViewTable_OnRowexpand = function($event, widget, row, $data) {
    App.showRowExpansionBanViewHome(row, $data);
};

// assigned team Onchange for EV
Page.assignedTeamSelectEV_onChange = function($event, widget, newVal, oldVal) {
    if (Page.Widgets.AssignedTeamSelectEV.datavalue == 'ALL') {
        Page.Variables.getAllActiveUserList_HomeEV_forALL.invoke();
    } else {
        Page.Variables.getUserListByTeamId_homeEV.setInput({
            'teamId': Page.Widgets.AssignedTeamSelectEV.datavalue
        });
        Page.Variables.getUserListByTeamId_homeEV.invoke();
    }
};

// assigned team Onchange for BV
Page.assignedTeamSelectBV_onChange = function($event, widget, newVal, oldVal) {
    if (Page.Widgets.AssignedTeamSelectBV.datavalue == 'ALL') {
        Page.Variables.getAllActiveUserList_HomeBV_forALL.invoke();
    } else {
        Page.Variables.getUserListByTeamId_homeBV.setInput({
            'teamId': Page.Widgets.AssignedTeamSelectBV.datavalue
        });
        Page.Variables.getUserListByTeamId_homeBV.invoke();
    }
};

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
    debugger;
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

Page.getUserListByTeamId_homeEVonSuccess = function(variable, data) {
    Page.Variables.getAllActiveUserList_HomeEV.dataSet = data;
    if (data.length > 0) {
        if (data.length > 1) {

            Page.Variables.getAllActiveUserList_HomeEV.dataSet.unshift({
                empId: 'NULL',
                firstName: 'NULL',
                lastName: ''
            });
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
                empId: 'NULL',
                firstName: 'NULL',
                lastName: ''
            });

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
    debugger;
    Page.Variables.workCategoryValues_HomeEV.dataSet = data;

    if (data != undefined) {
        data.forEach(workCategoryData);
    }

    if (Page.Variables.portfolioEntityView_home.dataSet != undefined) {
        Page.Variables.portfolioEntityView_home.dataSet.forEach(portfolioData);
    }

    if (Page.Variables.billingSystemEntityView_home.dataSet != undefined) {
        Page.Variables.billingSystemEntityView_home.dataSet.forEach(billingSystemData);
    }

    if (Page.Variables.CollStatus_home.dataSet != undefined) {
        Page.Variables.CollStatus_home.dataSet.forEach(collStatusData);
    }

    if (workCategoryDataArray.length > 1) {
        var finalWCentityview = workCategoryDataArray.join("|");
    } else {
        var finalWCentityview = workCategoryDataArray;
    }

    if (portfolioDataArray.length > 1) {
        var portfolioview = portfolioDataArray;
    }

    if (billingSystemDataArray.length >= 1) {
        var billingSystemview = billingSystemDataArray;
    }

    if (collStatusDataArray.length > 1) {
        var collStatusDataview = collStatusDataArray;
    }

    // api call to display data in table for entity view


    Page.Variables.CollectionDataServiceGetAssignedEntitiesInEntityView3.setInput({
        'entityOwner': App.Variables.getLoggedInUserDetails.dataSet.emplId,
        'workCategory': finalWCentityview,
        /*'portfolio': Page.Variables.portfolioEntityView_home.dataSet[0].dataValue,
        'billingSystem': Page.Variables.billingSystemEntityView_home.dataSet[0].dataValue,
        'collectionStatus': Page.Variables.CollStatus_home.dataSet[0].dataValue*/
        'portfolio': 'ALL', // portfolioview,
        'billingSystem': 'CES9', //billingSystemview,
        'collectionStatus': 'ALL' //collStatusDataview
    });
    Page.Variables.CollectionDataServiceGetAssignedEntitiesInEntityView3.invoke();
};

Page.workcategoriesByEmpId_homeBVonSuccess = function(variable, data) {
    debugger;
    Page.Variables.workCategoryValues_HomeBV.dataSet = data;

    if (data != undefined) {
        data.forEach(workCategoryData);
        workCategoryDataArray = [];
        data.forEach(function(item) {
            workCategoryDataArray.push(item.code)

        });
    }

    if (Page.Variables.portfolioEntityView_home.dataSet != undefined) {
        Page.Variables.portfolioEntityView_home.dataSet.forEach(portfolioData);
    }

    if (Page.Variables.billingSystemEntityView_home.dataSet != undefined) {
        Page.Variables.billingSystemEntityView_home.dataSet.forEach(billingSystemData);
    }

    if (Page.Variables.CollStatus_home.dataSet != undefined) {
        Page.Variables.CollStatus_home.dataSet.forEach(collStatusData);
    }

    if (workCategoryDataArray.length > 1) {
        var finalWCbanview = workCategoryDataArray.join("|");
    } else {
        var finalWCbanview = workCategoryDataArray;
    }

    if (portfolioDataArray.length > 1) {
        var portfolioview = portfolioDataArray;
    }

    if (billingSystemDataArray.length >= 1) {
        var billingSystemview = billingSystemDataArray;
    }

    if (collStatusDataArray.length > 1) {
        var collStatusDataview = collStatusDataArray;
    }

    // api call to display data in table for ban view
    Page.Variables.CollectionDataServiceGetassignedEntitiesInClassicView2.setInput({
        'entityOwner': App.Variables.getLoggedInUserDetails.dataSet.emplId,
        'workCategory': finalWCbanview,
        /*'portfolio': Page.Variables.portfolioEntityView_home.dataSet[0].dataValue,
        'billingSystem': Page.Variables.billingSystemEntityView_home.dataSet[0].dataValue,
        'collectionStatus': Page.Variables.CollStatus_home.dataSet[0].dataValue*/
        'portfolio': 'ALL', //portfolioview,
        'billingSystem': 'CES9', //billingSystemview,
        'collectionStatus': 'ALL' //collStatusDataview

    });
    Page.Variables.CollectionDataServiceGetassignedEntitiesInClassicView2.invoke();

};

function workCategoryData(item, index) {
    var item = item;
    workCategoryDataArray.push(item.code);
};

function portfolioData(item, index) {
    var item = item;
    portfolioDataArray.push(item.code);
};

function billingSystemData(item, index) {
    var item = item;
    billingSystemDataArray.push(item.code);
};

function collStatusData(item, index) {
    var item = item;
    collStatusDataArray.push(item.code);
};


Page.getAllActiveUserList_HomeEV_forALLonSuccess = function(variable, data) {
    if (Page.Variables.getAllActiveUserList_HomeEV_forALL.dataSet.length > 1) {
        Page.Variables.getAllActiveUserList_HomeEV_forALL.dataSet.unshift({
            empId: 'ALL',
            firstName: 'ALL',
            lastName: ''
        });
        Page.Variables.getAllActiveUserList_HomeEV.dataSet = data;
        Page.Widgets.entityOwnerSelectEV.datavalue = Page.Variables.getAllActiveUserList_HomeEV_forALL.dataSet[0].empId;
    }
};

Page.getAllActiveUserList_HomeBV_forALLonSuccess = function(variable, data) {
    if (Page.Variables.getAllActiveUserList_HomeBV_forALL.dataSet.length > 1) {
        Page.Variables.getAllActiveUserList_HomeBV_forALL.dataSet.unshift({
            empId: 'ALL',
            firstName: 'ALL',
            lastName: ''
        });
        Page.Variables.getAllActiveUserList_HomeBV.dataSet = data;
        Page.Widgets.entityOwnerSelectBV.datavalue = Page.Variables.getAllActiveUserList_HomeBV_forALL.dataSet[0].empId;
    }
};

Page.CollectionDataServiceGetassignedEntitiesInClassicView2onError = function(variable, data, xhrObj) {

};