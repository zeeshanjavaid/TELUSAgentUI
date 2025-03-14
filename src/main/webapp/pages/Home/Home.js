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
var includeCurrOrCreDataArray = [];
Page.onReady = function() {
    debugger;
    // callwhileload();
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


    // Page.statusData = [];

    // Page.Variables.getWorkCatByEmplIdForMultiSelectList.dataSet

    // Page.Variables.getWorkCatByEmplIdForMultiSelectList.dataSet.forEach(function(item) {
    //     Page.statusData.push({
    //         // id: item.code.replace(/\s/g, ''),
    //         id: item.code,
    //         title: item.code
    //     });
    // });

    // subComboBox = $('#WorkCategoryMutliSel').comboTree({
    //     source: Page.statusData,
    //     isMultiple: true,
    //     // selected: false,

    //     // cascadeSelect: true,
    //     // collapse: true
    // });



    // subComboBox1 = $('#WorkCategoryMutliSelForBanView').comboTree({
    //     source: Page.statusData,
    //     isMultiple: true,
    //     // selected: false,

    //     // cascadeSelect: true,
    //     // collapse: true
    // });

    Page.isClear = false;
    Page.Variables.getLoggedInUserTeamForBanView.invoke();
    // Page.Variables.includeCurrentCredit_homeEV.invoke();
    Page.Variables.getWorkCatByEmplIdForMultiSelect.setInput({
        'emplId': Page.Variables.getLoggedInUserDetails.dataSet.emplId

    });
    Page.Variables.getWorkCatByEmplIdForMultiSelect.invoke();

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
    Page.Widgets.includeCurrentCreditSelectEV.datavalue = "N";
    Page.Widgets.includeCurrentCreditSelectBV.datavalue = "N";

    $('#banViewTableGrid').hide();
    $('#filterGridBanView').hide();
    $("#entityViewBtn").css("background-color", "#4B286D");
    $("#entityViewBtn").css("color", "white");
    Page.Variables.workCategoryValues_HomeEV.invoke();
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

    debugger;
    var entityOwner = App.Variables.getLoggedInUserDetails.dataSet.emplId;
    // Page.Variables.workCategoryValues_HomeBV.dataSet = data;

    if (Page.Variables.workCategoryValues_HomeBV.dataSet != undefined) {
        Page.Variables.workCategoryValues_HomeBV.dataSet.forEach(workCategoryData);
        workCategoryDataArray = [];
        Page.Variables.workCategoryValues_HomeBV.dataSet.forEach(function(item) {
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


    if (Page.Variables.includeCurrentCredit_homeBV.dataSet != undefined) {
        Page.Variables.includeCurrentCredit_homeBV.dataSet.forEach(includeCurrOrCreData);
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

    if (includeCurrOrCreDataArray.length > 1) {
        var includeCurrOrCreDataview = includeCurrOrCreDataArray;
    }

    var teamName = Page.Variables.getLoggedInUserTeamForBanView.dataSet;

    for (let i = 0; i < teamName.length; i++) {
        if (teamName[i].teamId === 'TIG AR') {
            entityOwner = 'NULL';
        }
    }
    // api call to display data in table for ban view
    Page.Variables.CollectionDataServiceGetassignedEntitiesInClassicView2.setInput({
        'entityOwner': entityOwner,
        'workCategory': finalWCbanview,
        'portfolio': 'ALL',
        'billingSystem': 'CES9',
        'collectionStatus': 'ALL',
        'includeCurrentOrCredit': 'N',
        'limit': 10,
        'offset': 0

    });
    Page.Variables.CollectionDataServiceGetassignedEntitiesInClassicView2.invoke();

};

// function added to clear all the fields in the filter for Entity View
Page.clearFilterFieldsEntityView = function($event, widget) {


    debugger;
    Page.Variables.getAllActiveUserList_HomeEV_forALL.invoke();
    Page.isClear = true;
    Page.Widgets.AssignedTeamSelectEV.datavalue = "ALL";
    Page.Widgets.portfolioSelectEV.datavalue = "ALL";
    Page.Widgets.includeCurrentCreditSelectEV.datavalue = "N";
    Page.Widgets.entityOwnerSelectEV.datavalue = App.Variables.getLoggedInUserDetails.dataSet.emplId;
    Page.Widgets.billingSystemSelectEV.datavalue = "CES9";
    /*Page.Widgets.ARExcludedInternalSelectEV.datavalue = "Y";*/
    /*Page.Widgets.workCategorySelectEV.datavalue = Page.Variables.workCategoryValues_HomeEV.invoke();*/
    Page.Widgets.collStatusSelectEV.datavalue = "ALL";
    Page.Widgets.workCategorySelectEVNew.deselectAll();

    Page.Widgets.AssignedTeamSelectEV.disabled = false;
    Page.Widgets.entityOwnerSelectEV.disabled = false;

    /*subComboBox.clearSelection();
    checkedItem = $("input:checked")
    checkedItem.prop('checked', false)*/

    if (workCategoryDataArray.length > 1) {
        var finalWorkCategoriesEV = workCategoryDataArray.join("|");
    } else {
        var finalWorkCategoriesEV = workCategoryDataArray;
    }
    Page.Variables.CollectionDataServiceGetAssignedEntitiesInEntityView3.setInput({
        'entityOwner': App.Variables.getLoggedInUserDetails.dataSet.emplId,
        'workCategory': finalWorkCategoriesEV,
        'portfolio': 'ALL',
        'billingSystem': 'CES9',
        'collectionStatus': 'ALL',
        'includeCurrentOrCredit': 'N',
        'limit': 10,
        'offset': 0


    });
    Page.Variables.CollectionDataServiceGetAssignedEntitiesInEntityView3.invoke();





}

// function added to clear all the fields in the filter for Ban View
Page.clearFilterFieldsBanView = function($event, widget) {
    debugger;

    Page.Variables.getAllActiveUserList_HomeBV_forALL.invoke();
    Page.isClear = true;
    Page.Widgets.AssignedTeamSelectBV.datavalue = "ALL";
    Page.Widgets.portfolioSelectBV.datavalue = "ALL";
    Page.Widgets.includeCurrentCreditSelectBV.datavalue = "N";
    Page.Widgets.entityOwnerSelectBV.datavalue = App.Variables.getLoggedInUserDetails.dataSet.emplId;
    Page.Widgets.billingSystemSelectBV.datavalue = "CES9";
    /* Page.Widgets.ARExcludedInternalSelectBV.datavalue = "Y";*/
    // Page.Widgets.workCategorySelectBV.datavalue = Page.Variables.workCategoryValues_HomeBV.invoke();
    Page.Widgets.collStatusSelectBV.datavalue = "ALL";
    Page.Widgets.workCategorySelectBV.deselectAll();

    Page.Widgets.AssignedTeamSelectBV.disabled = false;
    Page.Widgets.entityOwnerSelectBV.disabled = false;

    //  subComboBox1.clearSelection();
    //  checkedItem = $("input:checked")
    // checkedItem.prop('checked', false)

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
        'collectionStatus': 'ALL',
        'includeCurrentOrCredit': 'N',
        'limit': 10,
        'offset': 0
    });
    Page.Variables.CollectionDataServiceGetassignedEntitiesInClassicView2.invoke();


}

// function added to display table based on the filters for entity view
Page.applyFiltersEntityView = function($event, widget) {
    debugger;
    //  var workCategoriesEV = subComboBox.getSelectedIds();

    // var workCategoriesEV = subComboBox._selectedItems.map(({
    //     id
    // }) => id);


    // var assignedTeamEV = Page.Widgets.AssignedTeamSelectEV.datavalue;
    // if (assignedTeamEV != '' && assignedTeamEV != undefined) {
    //     var assignedTeamEVarrEmpId = [];
    //     Page.Variables.getAllActiveUserList_HomeEV.dataSet.forEach(function(e) {
    //         assignedTeamEVarrEmpId.push(e.empId)
    //     });

    //     assignedTeamEV = assignedTeamEVarrEmpId.join("|");
    // }

    if (Page.Widgets.AssignedTeamSelectEV.datavalue == 'ALL') {
        var workCategoriesEV = Page.Widgets.workCategorySelectEVNew.datavalue;
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
                'collectionStatus': Page.Widgets.collStatusSelectEV.datavalue,
                'includeCurrentOrCredit': Page.Widgets.includeCurrentCreditSelectEV.datavalue,
                'manualFlag': Page.Widgets.manualFlagDrop.datavalue,
                'limit': 10,
                'offset': 0

            });
            Page.Variables.CollectionDataServiceGetAssignedEntitiesInEntityView3.invoke();
        }
    } else {
        Page.Variables.getUserListByTeamId_homeEV.setInput({
            'teamId': Page.Widgets.AssignedTeamSelectEV.datavalue
        });
        Page.Variables.getUserListByTeamId_homeEV.invoke({},
            function(data) {
                var assignedTeamEV = '';
                if (data.length == 0) {
                    assignedTeamEV = 'NULL';
                } else {
                    var assignedTeamEVarrEmpId = [];
                    debugger; // Success Callback
                    console.log("success", data);
                    data.forEach(function(e) {
                        assignedTeamEVarrEmpId.push(e.empId)
                    });
                    assignedTeamEV = assignedTeamEVarrEmpId.join("|");
                }
                var workCategoriesEV = Page.Widgets.workCategorySelectEVNew.datavalue;
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
                        'entityOwner': assignedTeamEV,
                        'workCategory': finalWorkCategoriesEV,
                        'portfolio': Page.Widgets.portfolioSelectEV.datavalue,
                        'billingSystem': Page.Widgets.billingSystemSelectEV.datavalue,
                        'collectionStatus': Page.Widgets.collStatusSelectEV.datavalue,
                        'includeCurrentOrCredit': Page.Widgets.includeCurrentCreditSelectEV.datavalue,
                        'manualFlag': Page.Widgets.manualFlagDrop.datavalue,
                        'limit': 10,
                        'offset': 0

                    });
                    Page.Variables.CollectionDataServiceGetAssignedEntitiesInEntityView3.invoke();
                }
            },
            function(error) {
                debugger; // Error Callback
                console.log("success", error);

            });

    }

    /* var workCategoriesEV = Page.Widgets.workCategorySelectEVNew.datavalue;
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
             'collectionStatus': Page.Widgets.collStatusSelectEV.datavalue,
             'includeCurrentOrCredit': Page.Widgets.includeCurrentCreditSelectEV.datavalue,
             'limit': 10,
             'offset': 0

         });
         Page.Variables.CollectionDataServiceGetAssignedEntitiesInEntityView3.invoke();
     }*/


}

// function added to display table based on the filters for ban view
Page.applyFiltersBanView = function($event, widget) {
    debugger;
    //  var workCategoriesBV = subComboBox1.getSelectedIds();

    if (Page.Widgets.AssignedTeamSelectBV.datavalue == 'ALL') {
        var workCategoriesBV = Page.Widgets.workCategorySelectBV.datavalue;
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
                'collectionStatus': Page.Widgets.collStatusSelectBV.datavalue,
                'includeCurrentOrCredit': Page.Widgets.includeCurrentCreditSelectBV.datavalue,
                'limit': 10,
                'offset': 0

            });
            Page.Variables.CollectionDataServiceGetassignedEntitiesInClassicView2.invoke();
        }
    } else {
        Page.Variables.getUserListByTeamId_homeBV.setInput({
            'teamId': Page.Widgets.AssignedTeamSelectBV.datavalue
        });
        Page.Variables.getUserListByTeamId_homeBV.invoke({},
            function(data) {
                var assignedTeamBV = '';
                if (data.length == 0) {
                    assignedTeamBV = 'NULL';
                } else {
                    var assignedTeamBVarrEmpId = [];
                    debugger; // Success Callback
                    console.log("success", data);
                    data.forEach(function(e) {
                        assignedTeamBVarrEmpId.push(e.empId)
                    });
                    var assignedTeamBV = assignedTeamBVarrEmpId.join("|");;
                }
                var workCategoriesBV = Page.Widgets.workCategorySelectBV.datavalue;
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
                        'entityOwner': assignedTeamBV,
                        'workCategory': finalWorkCategoriesBV,
                        'portfolio': Page.Widgets.portfolioSelectBV.datavalue,
                        'billingSystem': Page.Widgets.billingSystemSelectBV.datavalue,
                        'collectionStatus': Page.Widgets.collStatusSelectBV.datavalue,
                        'includeCurrentOrCredit': Page.Widgets.includeCurrentCreditSelectBV.datavalue,
                        'limit': 10,
                        'offset': 0

                    });
                    Page.Variables.CollectionDataServiceGetassignedEntitiesInClassicView2.invoke();
                }
            },
            function(error) {
                debugger; // Error Callback
                console.log("success", error);

            });
    }




    /* var workCategoriesBV = Page.Widgets.workCategorySelectBV.datavalue;
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
             'collectionStatus': Page.Widgets.collStatusSelectBV.datavalue,
             'includeCurrentOrCredit': Page.Widgets.includeCurrentCreditSelectBV.datavalue,
             'limit': 10,
             'offset': 0

         });
         Page.Variables.CollectionDataServiceGetassignedEntitiesInClassicView2.invoke();
     }*/

}

Page.goToEnityPage = function(row) {

    debugger;
    // event.preventDefault();

    window.open("Lookup?entityId=" + (!row.entityId ? 0 : row.entityId), "_blank", 'noopener, noreferrer');


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
    debugger;
    if (Page.Widgets.AssignedTeamSelectEV.datavalue == 'ALL') {
        Page.Widgets.entityOwnerSelectEV.disabled = false;
        Page.Variables.getAllActiveUserList_HomeEV_forALL.invoke();
    } else {
        Page.Widgets.entityOwnerSelectEV.disabled = true;
        Page.Variables.getUserListByTeamId_homeEV.setInput({
            'teamId': Page.Widgets.AssignedTeamSelectEV.datavalue
        });
        Page.Variables.getUserListByTeamId_homeEV.invoke();
    }
};

// assigned team Onchange for BV
Page.assignedTeamSelectBV_onChange = function($event, widget, newVal, oldVal) {
    if (Page.Widgets.AssignedTeamSelectBV.datavalue == 'ALL') {
        Page.Widgets.entityOwnerSelectBV.disabled = false;
        Page.Variables.getAllActiveUserList_HomeBV_forALL.invoke();
    } else {
        Page.Widgets.entityOwnerSelectBV.disabled = true;
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
    debugger;
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
    var entityOwner = App.Variables.getLoggedInUserDetails.dataSet.emplId;
    Page.Variables.workCategoryValues_HomeEV.dataSet = data;

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

    if (Page.Variables.includeCurrentCredit_homeEV.dataSet != undefined) {
        Page.Variables.includeCurrentCredit_homeEV.dataSet.forEach(includeCurrOrCreData);
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

    if (includeCurrOrCreDataArray.length > 1) {
        var includeCurrOrCreDataview = includeCurrOrCreDataArray;
    }

    // api call to display data in table for entity view

    var teamName = Page.Variables.getLoggedInUserTeamForBanView.dataSet;

    for (let i = 0; i < teamName.length; i++) {
        if (teamName[i].teamId === 'TIG AR') {
            entityOwner = 'NULL';
        }
    }


    Page.Variables.CollectionDataServiceGetAssignedEntitiesInEntityView3.setInput({
        'entityOwner': entityOwner,
        'workCategory': finalWCentityview,
        'portfolio': 'ALL',
        'billingSystem': 'CES9',
        'collectionStatus': 'ALL',
        'includeCurrentOrCredit': 'N',
        'limit': 10,
        'offset': 0
    });
    Page.Variables.CollectionDataServiceGetAssignedEntitiesInEntityView3.invoke();
};

Page.workcategoriesByEmpId_homeBVonSuccess = function(variable, data) {
    debugger;
    // var entityOwner = App.Variables.getLoggedInUserDetails.dataSet.emplId;
    Page.Variables.workCategoryValues_HomeBV.dataSet = data;

    // if (data != undefined) {
    //     data.forEach(workCategoryData);
    //     workCategoryDataArray = [];
    //     data.forEach(function(item) {
    //         workCategoryDataArray.push(item.code)

    //     });
    // }

    // if (Page.Variables.portfolioEntityView_home.dataSet != undefined) {
    //     Page.Variables.portfolioEntityView_home.dataSet.forEach(portfolioData);
    // }

    // if (Page.Variables.billingSystemEntityView_home.dataSet != undefined) {
    //     Page.Variables.billingSystemEntityView_home.dataSet.forEach(billingSystemData);
    // }

    // if (Page.Variables.CollStatus_home.dataSet != undefined) {
    //     Page.Variables.CollStatus_home.dataSet.forEach(collStatusData);
    // }

    // if (workCategoryDataArray.length > 1) {
    //     var finalWCbanview = workCategoryDataArray.join("|");
    // } else {
    //     var finalWCbanview = workCategoryDataArray;
    // }

    // if (portfolioDataArray.length > 1) {
    //     var portfolioview = portfolioDataArray;
    // }

    // if (billingSystemDataArray.length >= 1) {
    //     var billingSystemview = billingSystemDataArray;
    // }

    // if (collStatusDataArray.length > 1) {
    //     var collStatusDataview = collStatusDataArray;
    // }

    // var teamName = Page.Variables.getLoggedInUserTeam.dataSet;

    // for (let i = 0; i < teamName.length; i++) {
    //     if (teamName[i].teamId === 'TIG AR') {
    //         entityOwner = null;
    //     }
    // }


    // if (Page.Variables.getLoggedInUserRole.dataSet[0].role === "") {
    //     entityOwner = null;
    // } else {
    //     entityOwner = App.Variables.getLoggedInUserDetails.dataSet.emplId;
    // }

    // // api call to display data in table for ban view
    // Page.Variables.CollectionDataServiceGetassignedEntitiesInClassicView2.setInput({
    //     'entityOwner': entityOwner,
    //     'workCategory': finalWCbanview,
    //     /*'portfolio': Page.Variables.portfolioEntityView_home.dataSet[0].dataValue,
    //     'billingSystem': Page.Variables.billingSystemEntityView_home.dataSet[0].dataValue,
    //     'collectionStatus': Page.Variables.CollStatus_home.dataSet[0].dataValue*/
    //     'portfolio': 'ALL', //portfolioview,
    //     'billingSystem': 'CES9', //billingSystemview,
    //     'collectionStatus': 'ALL' //collStatusDataview

    // });
    // Page.Variables.CollectionDataServiceGetassignedEntitiesInClassicView2.invoke();

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
    debugger;
    var item = item;
    collStatusDataArray.push(item.code);
};

function includeCurrOrCreData(item, index) {
    debugger;
    var item = item;
    includeCurrOrCreDataArray.push(item.code)

}

Page.getAllActiveUserList_HomeEV_forALLonSuccess = function(variable, data) {
    debugger;
    if (Page.Variables.getAllActiveUserList_HomeEV_forALL.dataSet.length > 1) {
        Page.Variables.getAllActiveUserList_HomeEV_forALL.dataSet.unshift({
            empId: 'ALL',
            firstName: 'ALL',
            lastName: ''
        });
        Page.Variables.getAllActiveUserList_HomeEV.dataSet = data;
        if (Page.isClear == false) {
            Page.Widgets.entityOwnerSelectEV.datavalue = Page.Variables.getAllActiveUserList_HomeEV_forALL.dataSet[0].empId;
        } else {
            Page.isClear = false;
            Page.Widgets.entityOwnerSelectEV.datavalue = App.Variables.getLoggedInUserDetails.dataSet.emplId;
        }
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
        if (Page.isClear == false) {
            Page.Widgets.entityOwnerSelectBV.datavalue = Page.Variables.getAllActiveUserList_HomeBV_forALL.dataSet[0].empId;
        } else {
            Page.isClear = false;
            Page.Widgets.entityOwnerSelectBV.datavalue = App.Variables.getLoggedInUserDetails.dataSet.emplId;
        }
    }
};

Page.CollectionDataServiceGetassignedEntitiesInClassicView2onError = function(variable, data, xhrObj) {

};

// async function callwhileload() {

//     Page.Variables.getWorkCatByEmplIdForMultiSelect.setInput({
//         'emplId': Page.Variables.getLoggedInUserDetails.dataSet.emplId

//     });
//     await Page.Variables.getWorkCatByEmplIdForMultiSelect.invoke();
//}


Page.table1_OnRowexpand = function($event, widget, row, $data) {
    App.showRowExpansionEntityViewHome(row, $data);
};

Page.table2_OnRowexpand = function($event, widget, row, $data) {
    App.showRowExpansionBanViewHome(row, $data);
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
    Page.Variables.CollectionDataServiceGetAssignedEntitiesInEntityView3.setInput({
        'limit': Page.size,
        'offset': offset
    });
    Page.Variables.CollectionDataServiceGetAssignedEntitiesInEntityView3.invoke();

}

Page.Telus_PaginatonBanViewPagechange = function($event, $data) {
    debugger;
    Page.size = $event.pageSize
    Page.page = $event.pageNumber
    Page.RefreshBanViewData();

};

Page.RefreshBanViewData = function() {
    debugger;
    var offset = Page.size * (Page.page - 1);
    Page.Variables.CollectionDataServiceGetassignedEntitiesInClassicView2.setInput({
        'limit': Page.size,
        'offset': offset
    });
    Page.Variables.CollectionDataServiceGetassignedEntitiesInClassicView2.invoke();

}

Page.entityOwnerSelectEVChange = function($event, widget, newVal, oldVal) {
    if (Page.Widgets.entityOwnerSelectEV.datavalue != "" && Page.Widgets.entityOwnerSelectEV.datavalue != 'ALL') {
        Page.Widgets.AssignedTeamSelectEV.disabled = true;
    } else {
        Page.Widgets.AssignedTeamSelectEV.disabled = false;
    }

};
Page.entityOwnerSelectBVChange = function($event, widget, newVal, oldVal) {
    if (Page.Widgets.entityOwnerSelectBV.datavalue != "" && Page.Widgets.entityOwnerSelectBV.datavalue != 'ALL') {
        Page.Widgets.AssignedTeamSelectBV.disabled = true;
    } else {
        Page.Widgets.AssignedTeamSelectBV.disabled = false;
    }
};