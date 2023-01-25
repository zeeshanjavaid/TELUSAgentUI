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
Partial.onReady = function() {
    /*
     * variables can be accessed through 'Partial.Variables' property here
     * e.g. to get dataSet in a staticVariable named 'loggedInUser' use following script
     * Partial.Variables.loggedInUser.getData()
     *
     * widgets can be accessed through 'Partial.Widgets' property here
     * e.g. to get value of text widget naamed 'username' use following script
     * 'Partial.Widgets.username.datavalue'
     */
    Partial.Variables.groupErrorMessage.dataSet.dataValue = "";
    Partial.Variables.groupSuccessMessage.dataSet.dataValue = "";
    Partial.allRoles = [];
    Partial.role = [];
    Partial.isImportExport = false;


};

Partial.ImportExportMenuSelect = function($event, widget, $item) {

    App.Variables.errorMsg.dataSet.dataValue = null;
    App.Variables.successMessage.dataSet.dataValue = null;

    // Partial.allGroup = [];
    Partial.role = [];
    Partial.rolePermission = [];
    Partial.rolePermissionFinal = [];
    Partial.isImportExport = true;

    if ($item.name == "Export") {
        exportToExcelFile();

        // Partial.Variables.getAllGroups.dataSet.forEach(function(r) {

        //     Partial.Variables.getGroupRole.filterExpressions.rules[0].value = '';
        //     Partial.Variables.getGroupRole.invoke({}, function(rm) {
        //         debugger
        //         Partial.groupRoleFinal = [];
        //         Partial.groupRole = rm.filter(function(g) {
        //             debugger
        //             if (r.id == g.groupId) {
        //                 Partial.groupRoleFinal.push(g.role);
        //             }
        //             return
        //         });

        //         Partial.group = {
        //             "createdBy": r.createdBy,
        //             "createdOn": r.createdOn,
        //             "description": r.description,
        //             "id": r.id,
        //             "active": r.active,
        //             "roles": Partial.groupRoleFinal,
        //             "name": r.name,
        //             "updatedBy": r.updatedBy,
        //             "updatedOn": r.updatedOn
        //         }

        //         Partial.allGroup.push(Partial.group);

        //         if (Partial.Variables.getAllGroups.dataSet.length == Partial.allGroup.length) {

        //             addUsersRecord(Partial.allGroup);
        //         }

        //     });
        // });
    } else {
        const fileSelector = $('<input id="fu_groups" type="file" accept=".xlsx" onchange="processGroup(this);">');
        fileSelector.click();
    }
};

exportToExcelFile = function() {

    App.Variables.errorMsg.dataSet.dataValue = null;
    App.Variables.successMessage.dataSet.dataValue = null;

    const linkElement = document.createElement('a');
    const pageURL = window.location.href.split("#/")[0];
    linkElement.setAttribute('href', pageURL + "services/fileRelatedBS/downloadGroupAndAssociations");
    linkElement.setAttribute('target', '_blank');

    linkElement.click();
    linkElement.remove();
}

function addUsersRecord(Data) {

    Partial.allGroupFinal = [];
    Data.forEach(function(rn) {
        Partial.Variables.getGroupUser.invoke({}, function(gu) {
            Partial.groupUserFinal = [];
            Partial.GroupUser = gu.filter(function(g) {
                if (rn.id == g.groupId)
                    Partial.groupUserFinal.push(g.userByUserId);
                return
            });
            rn.users = Partial.groupUserFinal;
            Partial.allGroupFinal.push(rn);

            if (Partial.Variables.getAllGroups.dataSet.length == Partial.allGroupFinal.length) {
                exportToJsonFile(Partial.allGroupFinal);
            }

        });
    });


};

function exportToJsonFile(jsonData) {

    //console.log(jsonData);
    //
    Partial.Variables.groupSuccessMessage.dataSet.dataValue = Partial.appLocale.GROUPS_EXPORTED_SUCCESSFULLY;
    let dataStr = JSON.stringify(jsonData);
    let dataUri = 'data:application/json;charset=utf-8,' + encodeURIComponent(dataStr);

    let exportFileDefaultName = 'Groups.json';

    let linkElement = document.createElement('a');
    linkElement.setAttribute('href', dataUri);
    linkElement.setAttribute('download', exportFileDefaultName);
    linkElement.click();
}

processGroup = function(input) {
    App.Variables.errorMsg.dataSet.dataValue = null;
    App.Variables.successMessage.dataSet.dataValue = null;

    if (input.files.length > 0) {
        try {
            const importFile = input.files[0];
            Partial.Variables.sv_importGroups.invoke({
                "inputFields": {
                    "seedFile": importFile
                }
            });

            /* Below code for JS level file read & then server API call */
            // let reader = new FileReader();
            // reader.onload = (event) => {
            //     const base64Content = btoa(event.target.result);
            //     Partial.Variables.sv_importUsers.invoke({
            //         "inputFields": {
            //             "importFileContents": base64Content
            //         }
            //     });
            // };
            // reader.readAsText(importFile);
        } catch (err) {
            console.error(err);
            App.Variables.errorMsg.dataSet.dataValue = "Something went wrong while importing. Please check console log for more details";
            $("html, body").animate({
                scrollTop: 0
            });
        }
    } else {
        App.Variables.errorMsg.dataSet.dataValue = "No file selected for import";
        $("html, body").animate({
            scrollTop: 0
        });
    }

    // if (file.size < Partial.Variables.fileMaxSize.dataSet.dataValue) {
    //     let reader = new FileReader();
    //     const self = this;
    //     reader.onload = (event) => {
    //         //console.log(event.target.result);

    //         try {
    //             var result = JSON.parse(event.target.result);
    //             var formatted = JSON.stringify(result, null, 2);
    //             if (result && result.length > 0)
    //                 result.forEach(function(r) {
    //                     if (r.name == "") {
    //                         Partial.Variables.groupErrorMessage.dataSet.dataValue = Partial.appLocale.INVALID_JSON;
    //                     }
    //                 });
    //             if (Partial.Variables.groupErrorMessage.dataSet.dataValue == "") {

    //                 Partial.fileUploadDuplicateCheck(result);
    //             }
    //         } catch (e) {
    //             //alert(e);
    //             //throw new Error('Error occured: ', e)
    //             setTimeout(function() {
    //                 const x = document.getElementById("container_message");
    //                 if (x !== null) {
    //                     x.click();
    //                 }
    //             }, 100);
    //             return;
    //         }
    //     };
    //     reader.readAsText(file);

    // } else {

    //     Partial.Variables.groupErrorMessage.dataSet.dataValue = Partial.appLocale.FILE_SIZE_EXCEED;

    //     setTimeout(function() {
    //         const x = document.getElementById("container_message");
    //         if (x !== null) {
    //             x.click();
    //         }
    //     }, 100);
    //     return;
    // }
}


App.getGroupIndex = function(groupId) {
    let QIndex = 0;

    if (groupId && Partial.Widgets.GroupList.dataset) {
        QIndex = Partial.Widgets.GroupList.dataset.findIndex((QData) => {
            return QData.id === groupId;
        });
    }


    return QIndex;
};


Partial.container5Click = function($event, widget, item, currentItemWidgets) {

    Partial.Variables.roleId.dataSet.dataValue = item.id;
    //App.getRolePermission(item.id);
};
Partial.AddRolesButtonClick = function($event, widget) {

};
Partial.fileupload1Select = function($event, widget, selectedFiles) {};

Partial.fileUploadDuplicateCheck = function(data) {
    Partial.Variables.groupErrorMessage.dataSet.dataValue = "";
    var hasDuplicate = false;
    data.map(v => v.name).sort().sort((a, b) => {
        if (a === b) hasDuplicate = true
    })
    //console.log('hasDuplicate', hasDuplicate)
    if (hasDuplicate == true) {
        Partial.Variables.groupErrorMessage.dataSet.dataValue = Partial.appLocale.DUPLICATE_ENTRY_FOUND;
    }

    if (Partial.Variables.groupErrorMessage.dataSet.dataValue == "") {
        Partial.Variables.getAllGroups.dataSet.forEach(function(group) {
            data.forEach(function(newGroup) {
                if (group.name == newGroup.name)
                    /*Partial.Variables.errorMessage.dataSet.dataValue = "Duplicate entry found."*/
                    Partial.Variables.groupErrorMessage.dataSet.dataValue = Partial.appLocale.GROUP_ALREADY_EXIST;

            });
        });
    }
    if (Partial.Variables.groupErrorMessage.dataSet.dataValue !== "") {

        setTimeout(function() {
            const x = document.getElementById("container_message");
            if (x !== null) {
                x.click();
            }
        }, 100);
    } else {
        //Partial.Variables.groupSuccessMessage.dataSet.dataValue = Partial.appLocale.GROUPS_IMPORTED_SUCCESSFULLY;
        App.fileGroupsUpload(data);
    }
};

Partial.RolePermissiononSuccess = function(variable, data) {};


Partial.allRolesonSuccess = function(variable, data) {

};

/*App.getAllRolesonSuccess = function(variable, data) {

    Partial.roleUI = [];
    Partial.Variables.getAllRoles.dataSet.forEach(function(r) {

        Partial.Variables.RolePermission.invoke({}, function(rm) {
            //console.log(rm)
            Partial.rolePermissionFinal = [];
            Partial.rolePermission = rm.filter(function(g) {
                if (r.id == g.roleId)
                    Partial.rolePermissionFinal.push(g.permission);
                return
            });

            Partial.roleUI = {

                "createdBy": r.createdBy,
                "createdOn": r.createdOn,
                "description": r.description,
                "id": r.id,
                "isActive": r.isActive,
                "permission": Partial.rolePermissionFinal,
                "role": r.role,
                "updatedBy": r.updatedBy,
                "updatedOn": r.updatedOn,
                "PermissionCount": Partial.rolePermissionFinal.length

            }

            Partial.Variables.allRolesUI.dataSet.push(Partial.roleUI);

        });

        //console.log(Partial.Variables.allRolesUI.dataSet)
        Partial.Variables.rolesCopy.dataSet = Partial.Variables.allRolesUI.dataSet;
    });





};*/
Partial.searchRoleBlur = function($event, widget) {};
Partial.roleSearchTextKeyup = function($event, widget) {};

Partial.getAllGroupsonSuccess = async function(variable, data) {

    /*Partial.Variables.allRolesUI.dataSet = data;
    Partial.Variables.rolesCopy.dataSet = data;
    Partial.roleUI = [];
    Partial.Variables.allRolesUI.dataSet = [];
    Partial.allRolesUITemp = [];
    Partial.rolePermission = [];
    Partial.Variables.roleId.dataSet.dataValue = "";*/
    Partial.allGroupUITemp = [];
    Partial.Variables.allGroupsUI.dataSet = [];
    Partial.Variables.groupCopy.dataSet = [];
    for (let r of data) {
        Partial.Variables.roleId.dataSet.dataValue = r.id;
        Partial.groupRole = [];
        Partial.groupUser = [];
        // Partial.Variables.getRolesPermission.setInput({
        //   'RoleId': r.id
        //});
        //Partial.Variables.getRolesPermission.invoke();
        //Partial.Variables.RolePermission.invoke();
        /* Partial.Variables.RolePermission.invoke({}, function(rm) {
                                 Partial.rolePermissionFinal = [];
                                 Partial.rolePermission = rm.filter(function(g) {
                                     if (r.id == g.roleId)
                                         Partial.rolePermissionFinal.push(g.permission);
                                     return
                                 });
                                 Partial.roleUI = {
                                     "createdBy": r.createdBy,
                                     "createdOn": r.createdOn,
                                     "description": r.description,
                                     "id": r.id,
                                     "isActive": r.isActive,
                                     "permission": Partial.rolePermissionFinal,
                                     "role": r.role,
                                     "updatedBy": r.updatedBy,
                                     "updatedOn": r.updatedOn,
                                     "PermissionCount": Partial.rolePermissionFinal.length
                                 }
                                 Partial.Variables.allRolesUI.dataSet.push(Partial.roleUI);
                             });
                             // setTimeout(function() {
                             console.log(Partial.Variables.allRolesUI.dataSet)
                             Partial.Variables.rolesCopy.dataSet = Partial.Variables.allRolesUI.dataSet;
                             //}, 500);*/

        /*Partial.Variables.executeGetRolesByGroupId.setInput({
            'GroupId': r.id
        });*/

        Partial.groupUI = {
            "createdBy": r.createdBy,
            "createdOn": r.createdOn,
            "description": r.description,
            "id": r.id,
            "active": r.active,
            "name": r.name,
            "updatedBy": r.updatedBy,
            "updatedOn": r.updatedOn,
            "roleCount": 0,
            "userCount": 0,
        };

        Partial.Variables.getGroupRole.filterExpressions.rules[0].value = r.id;
        await Partial.Variables.getGroupRole.invoke();

        Partial.groupUI.roleCount = (Partial.Variables.getGroupRole.dataSet && Partial.Variables.getGroupRole.dataSet.length > 0) ?
            Partial.Variables.getGroupRole.dataSet.length : 0;

        Partial.Variables.getGroupUser.filterExpressions.rules[0].value = r.id;
        await Partial.Variables.getGroupUser.invoke();

        Partial.groupUI.userCount = (Partial.Variables.getGroupUser.dataSet && Partial.Variables.getGroupUser.dataSet.length > 0) ?
            Partial.Variables.getGroupUser.dataSet.length : 0;

        let d = Partial.allGroupUITemp.find((element) => {
            return element.name === Partial.groupUI.name
        });

        if (!d) {
            Partial.allGroupUITemp.push(Partial.groupUI);
            Partial.Variables.allGroupsUI.dataSet.push(Partial.groupUI);
            Partial.Variables.groupCopy.dataSet.push(Partial.groupUI);
        }




        // setTimeout(function() {

        //Partial.Variables.rolesCopy.dataSet = Partial.Variables.allRolesUI.dataSet;
        //Partial.Variables.rolesCopy.dataSet = Partial.allRolesUITemp;
        //}, 500);
        //}, 500);
        //console.log(Partial.Variables.allRolesUI.dataSet)
    }
    //console.log(Partial.Variables.allRolesUI.dataSet)
    //if (Partial.Variables.getAllRoles.dataSet.length == Partial.allRolesUITemp.length) {
    // Partial.Variables.allGroupsUI.dataSet = Partial.allGroupUITemp;
    // Partial.Variables.groupCopy.dataSet = Partial.allGroupUITemp;
    // }
    //console.log(Partial.Variables.allGroupsUI.dataSet);

    //Partial.Variables.allRolesUI.dataSet = data;
    //Partial.Variables.rolesCopy.dataSet = data;
    if (App.Variables.GroupPageCommunication.currentGroupInFocusId) {

        let selectedItem = App.getGroupIndex(App.Variables.GroupPageCommunication.currentGroupInFocusId);
        let pageSize = App.Variables.GroupPageCommunication.currentPageSize;
        console.log("Selected item index:" + selectedItem + " pageSize :" + pageSize);
        App.navigateToPageNo(selectedItem, pageSize);
        App.clickListItemByIndex('GroupList', (selectedItem) % pageSize);

    }


};

App.navigateToPageNo = function(selectedItemIndex, pageSize) {

    let pageNo = Math.ceil((selectedItemIndex + 1) / pageSize);


    for (i = 0; i < (pageNo - 1); i++) {
        $('a[name="next"]')[0].click();

    }
    console.log("Navigation to pageno:" + pageNo);

};




App.clickListItemByIndex = function(listName, itemIndex) {

    console.log("List " + listName + " click item: " + itemIndex);
    Partial.Widgets.GroupList.selectItem(itemIndex);
    $('li[listitemindex="' + itemIndex + '"]').click();


};

Partial.AddGroupButtonClick = function($event, widget) {
    App.addGroups();
};
Partial.groupSearchTextKeyup = function($event, widget) {

    //Partial.Variables.rolesCopy.dataSet = Partial.Variables.allRolesUI.dataSet;
    if (widget.datavalue) {
        let results = Partial.allGroupUITemp.filter((item) => {
            return item.name.toLowerCase().includes(widget.datavalue.toLowerCase());
        });

        Partial.Variables.allGroupsUI.dataSet = [];
        Object.assign(Partial.Variables.allGroupsUI.dataSet, results);
    } else {
        Partial.Variables.allGroupsUI.dataSet = [];
        Object.assign(Partial.Variables.allGroupsUI.dataSet, Partial.allGroupUITemp);
    }

};

Partial.containerGroupsClick = function($event, widget, item, currentItemWidgets) {

};

App.refreshAllGroups = function() {


    Partial.Variables.getAllGroups.invoke();

}

App.groupsPageReload = function() {

    Partial.Variables.getAllGroups.invoke();

    debugger;


}

Partial.RolePermissiononError = function(variable, data) {

};

Partial.sv_importGroupsonError = function(variable, data) {
    $("div[name='AlertMenu']").blur();
    App.Variables.errorMsg.dataSet.dataValue = "Unexpected error occurred while importing Groups.";
    $("html, body").animate({
        scrollTop: 0
    });
};

Partial.sv_importGroupsonSuccess = function(variable, data) {
    $("div[name='AlertMenu']").blur();
    App.Variables.successMessage.dataSet.dataValue = Partial.appLocale.GROUPS_IMPORTED_SUCCESSFULLY;
    $("html, body").animate({
        scrollTop: 0
    });
    Partial.Variables.getAllGroups.invoke();
};

Partial.sv_downloadGroupsonError = function(variable, data) {
    App.Variables.errorMsg.dataSet.dataValue = "Unexpected error occurred while exporting Groups.";
    $("html, body").animate({
        scrollTop: 0
    });
};
Partial.GroupListClick = function(widget, $data) {

    App.Variables.GroupPageCommunication.currentPageSize = widget.pagesize;
    console.log("Group List clicked !! " + App.Variables.GroupPageCommunication.currentPageSize);

};
Partial.GroupListSetrecord = function(widget, $data) {

    App.Variables.GroupPageCommunication.currentPageSize = widget.pagesize;
    debugger;

};