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

    Partial.Variables.roleErrorMessage.dataSet.dataValue = "";
    Partial.Variables.roleSuccessMessage.dataSet.dataValue = "";
    Partial.allRoles = [];
    Partial.role = [];



};

//var fileSelector;
Partial.ImportExportMenuSelect = function($event, widget, $item) {
    App.Variables.errorMsg.dataSet.dataValue = null;
    App.Variables.successMessage.dataSet.dataValue = null;
    Partial.allRoles = [];
    Partial.role = [];
    Partial.rolePermission = [];
    Partial.rolePermissionFinal = [];

    if ($item.name == "Export") {
        exportToExcelFile();

        // //Partial.Variables.getAllRoles.invoke();
        // Partial.Variables.getAllRoles.dataSet.forEach(function(r) {

        //     //Partial.Variables.roleId.dataSet.dataValue = r.id;
        //     Partial.Variables.RolePermission.filterExpressions.rules[0].value = '';
        //     //Partial.Variables.RolePermission.invoke();

        //     Partial.Variables.RolePermission.invoke({}, function(rm) {
        //         //console.log(rm)
        //         Partial.rolePermissionFinal = [];
        //         Partial.rolePermission = rm.filter(function(g) {
        //             //
        //             //return r.id == g.roleId;
        //             if (r.id == g.roleId) {
        //                 delete g.permission.id;
        //                 Partial.rolePermissionFinal.push(g.permission);

        //             }
        //             return
        //         });

        //         Partial.role = {

        //             "createdBy": r.createdBy,
        //             "createdOn": r.createdOn,
        //             "description": r.description,
        //             "active": r.active,
        //             "permission": Partial.rolePermissionFinal,
        //             "role": r.role,
        //             "updatedBy": r.updatedBy,
        //             "updatedOn": r.updatedOn

        //         }

        //         Partial.allRoles.push(Partial.role);

        //         if (Partial.Variables.getAllRoles.dataSet.length == Partial.allRoles.length) {
        //             //setTimeout(function() {
        //             exportToJsonFile(Partial.allRoles);
        //             // }, 1000);

        //         }

        //     });
        // });

        // // exportToJsonFile(Partial.Variables.getAllRoles.dataSet);
    } else {
        const fileSelector = $('<input id="fu_roles" type="file" accept=".xlsx" onchange="processFile(this);">');
        fileSelector.click();
    }
};

exportToExcelFile = function() {

    App.Variables.errorMsg.dataSet.dataValue = null;
    App.Variables.successMessage.dataSet.dataValue = null;

    const linkElement = document.createElement('a');
    const pageURL = window.location.href.split("#/")[0];
    linkElement.setAttribute('href', pageURL + "services/fileRelatedBS/downloadRoleAndAssociations");
    linkElement.setAttribute('target', '_blank');

    linkElement.click();
    linkElement.remove();
}

function exportToJsonFile(jsonData) {
    //
    //console.log(jsonData);
    Partial.Variables.roleSuccessMessage.dataSet.dataValue = Partial.appLocale.ROLES_EXPORTED_SUCCESSFULLY;

    let dataStr = JSON.stringify(jsonData);
    let dataUri = 'data:application/json;charset=utf-8,' + encodeURIComponent(dataStr);

    let exportFileDefaultName = 'roles.json';

    let linkElement = document.createElement('a');
    linkElement.setAttribute('href', dataUri);
    linkElement.setAttribute('download', exportFileDefaultName);
    linkElement.click();
}

processFile = function(input) {

    App.Variables.errorMsg.dataSet.dataValue = null;
    App.Variables.successMessage.dataSet.dataValue = null;

    if (input.files.length > 0) {
        try {
            const importFile = input.files[0];
            Partial.Variables.sv_importRoles.invoke({
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

    // let file = files[0];
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
    //                     if (r.role == "") {
    //                         Partial.Variables.roleErrorMessage.dataSet.dataValue = Partial.appLocale.INVALID_JSON;
    //                     }
    //                 });
    //             if (Partial.Variables.roleErrorMessage.dataSet.dataValue == "") {

    //                 Partial.fileUploadDuplicateCheck(result);
    //             }
    //         } catch (e) {
    //             //alert(e);
    //             //throw new Error('Error occured: ', e)
    //             Partial.Variables.roleErrorMessage.dataSet.dataValue = Partial.appLocale.INVALID_JSON;

    //             setTimeout(function() {
    //                 const x = document.getElementById("container_message");
    //                 if (x !== null) {
    //                     x.click();
    //                 }
    //             }, 100);
    //             return;
    //         }
    //         //var result = JSON.parse(event.target.result);
    //         //console.log(result);
    //         //var formatted = JSON.stringify(result, null, 2);
    //         //console.log(formatted);
    //         //Partial.fileUploadDuplicateCheck(result);
    //         //App.fileUpload(result);
    //     };
    //     reader.readAsText(file);

    // } else {

    //     Partial.Variables.roleErrorMessage.dataSet.dataValue = Partial.appLocale.FILE_SIZE_EXCEED;

    //     setTimeout(function() {
    //         const x = document.getElementById("container_message");
    //         if (x !== null) {
    //             x.click();
    //         }
    //     }, 100);
    //     return;
    // }
}


App.getRoleIndex = function(roleId) {
    let QIndex = 0;
    console.log("Role id received :" + roleId);
    if (roleId && Partial.Widgets.RoleList.dataset) {
        QIndex = Partial.Widgets.RoleList.dataset.findIndex((QData) => {
            return QData.id === roleId;
        });
    }


    return QIndex;
};


Partial.container5Click = function($event, widget, item, currentItemWidgets) {

    App.Variables.errorMsg.dataSet.dataValue = "";
    App.Variables.successMessage.dataSet.dataValue = "";
    Partial.Variables.roleId.dataSet.dataValue = item.id;
    //App.getRolePermission(item.id);
};
Partial.AddRolesButtonClick = function($event, widget) {
    // debugger
    alert('hii there');
    App.addRoles();
};
Partial.fileupload1Select = function($event, widget, selectedFiles) {};

Partial.fileUploadDuplicateCheck = function(data) {

    Partial.Variables.roleErrorMessage.dataSet.dataValue = "";
    var hasDuplicate = false;
    data.map(v => v.role).sort().sort((a, b) => {
        if (a === b) hasDuplicate = true
    })
    //console.log('hasDuplicate', hasDuplicate)
    if (hasDuplicate == true) {
        Partial.Variables.roleErrorMessage.dataSet.dataValue = Partial.appLocale.DUPLICATE_ENTRY_FOUND;
    }

    if (Partial.Variables.roleErrorMessage.dataSet.dataValue == "") {
        Partial.Variables.getAllRoles.dataSet.forEach(function(role) {
            data.forEach(function(newRole) {
                if (role.role == newRole.role)
                    Partial.Variables.roleErrorMessage.dataSet.dataValue = Partial.appLocale.ROLE_ALREADY_EXIST;

            });
        });
    }

    if (Partial.Variables.roleErrorMessage.dataSet.dataValue !== "") {

        setTimeout(function() {
            const x = document.getElementById("container_message");
            if (x !== null) {
                x.click();
            }
        }, 100);
    } else {
        //Partial.Variables.roleSuccessMessage.dataSet.dataValue = Partial.appLocale.ROLES_IMPORTED_SUCCESSFULLY;
        App.fileUpload(data);
    }
};

Partial.RolePermissiononSuccess = function(variable, data) {
    //

    /*Partial.rolePermission = [];
    Partial.rolePermissionFinal = [];
    Partial.rolePermission = data.filter(function(g) {
        return Partial.Variables.roleId.dataSet.dataValue == g.roleId
        //if (Partial.Variables.roleId.dataSet.dataValue == g.roleId)
           //  Partial.rolePermissionFinal.push(g.permission);

    });
    Partial.roleUI = {
        "createdBy": Partial.rolePermission[0].role.createdBy,
        "createdOn": Partial.rolePermission[0].role.createdOn,
        "description": Partial.rolePermission[0].role.description,
        "id": Partial.rolePermission[0].role.id,
        "isActive": Partial.rolePermission[0].role.isActive,
        "permission": Partial.rolePermission,
        "role": Partial.rolePermission[0].role.role,
        "updatedBy": Partial.rolePermission[0].role.updatedBy,
        "updatedOn": Partial.rolePermission[0].role.updatedOn,
        "PermissionCount": Partial.rolePermission.length
    }
    Partial.Variables.allRolesUI.dataSet.push(Partial.roleUI);
    // setTimeout(function() {

    Partial.Variables.rolesCopy.dataSet = Partial.Variables.allRolesUI.dataSet;
    //}, 500);*/


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
    Partial.Widgets.RoleList.selectItem(itemIndex);
    $('li[listitemindex="' + itemIndex + '"]').click();


};

App.refreshAllRoles = function() {
    //Partial.Variables.allRoles.invoke();
    Partial.Variables.getAllRolesPartial.invoke();

}

Partial.allRolesonSuccess = function(variable, data) {
    //
};

App.getAllRolesonSuccess = function(variable, data) {
    //

    //Partial.Variables.allRolesUI.dataSet = data;
    //Partial.Variables.rolesCopy.dataSet = data;
    //Partial.roleUI = [];
    //Partial.Variables.allRolesUI.dataSet = [];
    //Partial.allRolesUITemp = [];
    //Partial.Variables.roleId.dataSet.dataValue = "";
    /*Partial.Variables.getAllRoles.dataSet.forEach(function(r) {
        Partial.Variables.roleId.dataSet.dataValue = r.id;
        Partial.rolePermission = [];
        Partial.roleUI = [];*/
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


    /*Partial.Variables.RolePermission.invoke({}, function(rm) {
            // Partial.rolePermission = [];
            // Partial.rolePermissionFinal = [];
            Partial.rolePermission = rm.filter(function(g) {
                return r.id == g.roleId;
                //if (Partial.Variables.roleId.dataSet.dataValue == g.roleId)
                //     Partial.rolePermissionFinal.push(g.permission);

            });
            Partial.roleUI = {
                "createdBy": Partial.rolePermission[0].role.createdBy,
                "createdOn": Partial.rolePermission[0].role.createdOn,
                "description": Partial.rolePermission[0].role.description,
                "id": Partial.rolePermission[0].role.id,
                "isActive": Partial.rolePermission[0].role.isActive,
                "permission": Partial.rolePermission,
                "role": Partial.rolePermission[0].role.role,
                "updatedBy": Partial.rolePermission[0].role.updatedBy,
                "updatedOn": Partial.rolePermission[0].role.updatedOn,
                "PermissionCount": Partial.rolePermission.length
            }
            //Partial.Variables.allRolesUI.dataSet.push(Partial.roleUI);
            Partial.allRolesUITemp.push(Partial.roleUI);
        });
        // setTimeout(function() {

        //Partial.Variables.rolesCopy.dataSet = Partial.Variables.allRolesUI.dataSet;
        //Partial.Variables.rolesCopy.dataSet = Partial.allRolesUITemp;
        //}, 500);
        //}, 500);
        //console.log(Partial.Variables.allRolesUI.dataSet)
    });
    //console.log(Partial.Variables.allRolesUI.dataSet)
    if (Partial.Variables.getAllRoles.dataSet.length == Partial.allRolesUITemp.length) {
        Partial.Variables.allRolesUI.dataSet = Partial.allRolesUITemp;
        Partial.Variables.rolesCopy.dataSet = Partial.allRolesUITemp;
    }
    console.log(Partial.Variables.allRolesUI.dataSet);*/
};


// Partial.searchRoleBlur = function($event, widget) {};

Partial.roleSearchTextKeyup = function($event, widget) {
    //Partial.Variables.rolesCopy.dataSet = Partial.Variables.allRolesUI.dataSet;
    // const input = document.getElementsByClassName("rolesearchText");
    // const val = input.roleSearchText.value.toLowerCase();
    //console.log(App.Variables.featuresCopy.dataSet);
    if (widget.datavalue) {
        let results = Partial.allRolesUITemp.filter((item) => {
            return (item.role.toLowerCase().includes(widget.datavalue.toLowerCase()) || (item.description && item.description.toLowerCase().includes(widget.datavalue.toLowerCase())));
        });

        Partial.Variables.allRolesUI.dataSet = [];
        Object.assign(Partial.Variables.allRolesUI.dataSet, results);
        //App.activePage.Variables.featureListState.dataSet.dataValue = (results.length === 0) ? 3 : 2;

    } else {
        Partial.Variables.allRolesUI.dataSet = [];
        Object.assign(Partial.Variables.allRolesUI.dataSet, Partial.allRolesUITemp);
    }
};

Partial.getRolesPermissiononSuccess = function(variable, data) {

    //

    /*    Partial.rolePermissionFinal = [];

        Partial.rolePermissionFinal.push(data.permission);
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

        //setTimeout(function() {
        console.log(Partial.Variables.allRolesUI.dataSet)
        Partial.Variables.rolesCopy.dataSet = Partial.Variables.allRolesUI.dataSet;
        //}, 500);*/
};

Partial.getAllRolesPartialonSuccess = async function(variable, data) {

    //Partial.Variables.allRolesUI.dataSet = data;
    //Partial.Variables.rolesCopy.dataSet = data;
    Partial.Variables.allRolesUI.dataSet = [];
    Partial.allRolesUITemp = [];
    Partial.Variables.rolesCopy.dataSet = [];
    Partial.Variables.roleId.dataSet.dataValue = "";

    for (let r of data) {
        Partial.Variables.roleId.dataSet.dataValue = r.id;
        Partial.rolePermission = [];

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


        /*Partial.Variables.RolePermission.invoke({}, function(rm) {
            // Partial.rolePermission = [];
            // Partial.rolePermissionFinal = [];
            Partial.rolePermission = rm.filter(function(g) {
                return r.id == g.roleId;
                //if (Partial.Variables.roleId.dataSet.dataValue == g.roleId)
                //     Partial.rolePermissionFinal.push(g.permission);

            });*/
        /*Partial.Variables.executeGetPermissionGroupByRoleId.setInput({
            'RoleId': r.id
        });
        Partial.Variables.executeGetPermissionGroupByRoleId.invoke({}, function(group) {*/
        // Partial.rolePermission = [];
        // Partial.rolePermissionFinal = [];
        /*Partial.rolePermission = rm.filter(function(g) {
            return r.id == g.roleId;
            //if (Partial.Variables.roleId.dataSet.dataValue == g.roleId)
            //     Partial.rolePermissionFinal.push(g.permission);

        });*/

        Partial.roleUI = {
            "createdBy": r.createdBy,
            "createdOn": r.createdOn,
            "description": r.description,
            "id": r.id,
            "isActive": r.active,
            "permission": null,
            "group": null,
            "role": r.role,
            "updatedBy": r.updatedBy,
            "updatedOn": r.updatedOn,
            "PermissionCount": 0,
            "GroupCount": 0
        };

        Partial.Variables.RolePermission.filterExpressions.rules[0].value = r.id;
        Partial.Variables.RoleGroups.filterExpressions.rules[0].value = r.id;
        await Partial.Variables.RolePermission.invoke();
        await Partial.Variables.RoleGroups.invoke();


        Partial.roleUI.permission = (Partial.Variables.RolePermission.dataSet && Partial.Variables.RolePermission.dataSet.length > 0) ?
            Partial.Variables.RolePermission.dataSet : null;
        Partial.roleUI.PermissionCount = (Partial.Variables.RolePermission.dataSet && Partial.Variables.RolePermission.dataSet.length > 0) ?
            Partial.Variables.RolePermission.dataSet.length : 0;
        Partial.roleUI.group = (Partial.Variables.RoleGroups.dataSet && Partial.Variables.RoleGroups.dataSet.length > 0) ?
            Partial.Variables.RoleGroups.dataSet : null;
        Partial.roleUI.GroupCount = (Partial.Variables.RoleGroups.dataSet && Partial.Variables.RoleGroups.dataSet.length > 0) ?
            Partial.Variables.RoleGroups.dataSet.length : 0;

        // });

        let d = Partial.allRolesUITemp.find((element) => {
            return element.role === Partial.roleUI.role;
        });

        if (!d) {
            Partial.allRolesUITemp.push(Partial.roleUI);
            Partial.Variables.allRolesUI.dataSet.push(Partial.roleUI);
            // if (Partial.allRolesUITemp.length == data.length) {
            //[PD] - Commented after retrofit
            //addGroupCount(Partial.allRolesUITemp);
            // }
        }



        // setTimeout(function() {

        //Partial.Variables.rolesCopy.dataSet = Partial.Variables.allRolesUI.dataSet;
        //Partial.Variables.rolesCopy.dataSet = Partial.allRolesUITemp;
        //}, 500);
        //}, 500);
        //console.log(Partial.Variables.allRolesUI.dataSet)
    }

    if (App.Variables.RolePageCommunication.currentRoleInFocusId) {

        let selectedItem = App.getRoleIndex(App.Variables.RolePageCommunication.currentRoleInFocusId);
        let pageSize = App.Variables.RolePageCommunication.currentPageSize;
        console.log("Selected item index:" + selectedItem + " pageSize :" + pageSize);
        App.navigateToPageNo(selectedItem, pageSize);
        App.clickListItemByIndex('RoleList', (selectedItem) % pageSize);
        // Partial.Widgets.QueueList.selectItem(selectedItem);
        // let selectItem = Partial.Widgets.QueueList.firstSelectedItem;
        // Partial.Widgets.QueueList.firstSelectedItem.$element.addClass('active');
        // console.log("Selected Item class list:" + Partial.Widgets.QueueList.firstSelectedItem.itemClass);
    }
    //console.log(Partial.Variables.allRolesUI.dataSet)
    //if (Partial.Variables.getAllRoles.dataSet.length == Partial.allRolesUITemp.length) {
    /*Partial.Variables.allRolesUI.dataSet = Partial.allRolesUITemp;
    Partial.Variables.rolesCopy.dataSet = Partial.allRolesUITemp;*/
    //console.log(Partial.Variables.allRolesUI.dataSet);



};

function addGroupCount(data) {

    //setTimeout(function() {

    Partial.groupRoles = [];
    data.forEach(function(m) {
        // Partial.Variables.roleId.dataSet.dataValue =ra.id;
        //Partial.rolePermission = [];
        //Partial.roleUI = [];


        /*Partial.Variables.executeGetPermissionGroupByRoleId.setInput({
            'RoleId': ra.id
        });
        Partial.Variables.executeGetPermissionGroupByRoleId.invoke({}, function(group) {
            
            if (group.content.length > 1) {
                //Partial.roleUI.PermissionCount = group.content[0].count___;
                ra.GroupCount = group.content[1].count___;
            } else if (group.content.length > 0) {
                //Partial.roleUI.PermissionCount = group.content[0].count___;
                ra.roleUI.GroupCount = 0
            } else {
                //Partial.roleUI.PermissionCount = 0;
                ra.roleUI.GroupCount = 0;
            }


        });*/


        Partial.Variables.groupRole.invoke({}, function(rma) {
            // Partial.rolePermission = [];
            // Partial.rolePermissionFinal = [];
            Partial.groupRoles = rma.filter(function(ga) {
                return m.id == ga.roleId;
                //if (Partial.Variables.roleId.dataSet.dataValue == g.roleId)
                //     Partial.rolePermissionFinal.push(g.permission);

            });
            if (Partial.groupRoles && Partial.groupRoles.length > 0) {
                m.GroupCount = Partial.groupRoles.length;
            } else {
                m.GroupCount = 0
            }

            /*Partial.roleUI = {
                "createdBy": Partial.rolePermission[0].role.createdBy,
                "createdOn": Partial.rolePermission[0].role.createdOn,
                "description": Partial.rolePermission[0].role.description,
                "id": Partial.rolePermission[0].role.id,
                "isActive": Partial.rolePermission[0].role.isActive,
                "permission": Partial.rolePermission,
                "role": Partial.rolePermission[0].role.role,
                "updatedBy": Partial.rolePermission[0].role.updatedBy,
                "updatedOn": Partial.rolePermission[0].role.updatedOn,
                "PermissionCount": Partial.rolePermission.length,
                "GroupCount": Partial.groupRoles.length
            }*/

            /*if (group.content.length > 1) {
                Partial.roleUI.PermissionCount = group.content[0].count___;
                Partial.roleUI.GroupCount = group.content[1].count___;
            } else if (group.content.length > 0) {
                Partial.roleUI.PermissionCount = group.content[0].count___;
                Partial.roleUI.GroupCount = 0
            } else {
                Partial.roleUI.PermissionCount = 0;
                Partial.roleUI.GroupCount = 0;
            }*/



            /* if (!Array.isArray(Partial.roleUI)) {
                 Partial.allRolesUITemp.push(Partial.roleUI);
             }*/




            // }
            //console.log(Partial.Variables.allRolesUI.dataSet);

            //Partial.Variables.allRolesUI.dataSet = data;
            //Partial.Variables.rolesCopy.dataSet = data;
        });
    });
    //Partial.Variables.allRolesUI.dataSet = Partial.allRolesUITemp;

    /* Partial.Variables.allRolesUI.dataSet = Partial.allRolesUITemp;
     Partial.Variables.rolesCopy.dataSet = Partial.allRolesUITemp;*/
    Partial.Variables.allRolesUI.dataSet = data;
    Partial.Variables.rolesCopy.dataSet = data;
    //Partial.Variables.rolesCopy.dataSet = Partial.Variables.allRolesUI.dataSet;
    //console.log(Partial.Variables.allRolesUI.dataSet);
    // }, 1000);

}

App.rolesPageReload = function() {

    Partial.Variables.getAllRolesPartial.invoke();

}

Partial.sv_importRolesonError = function(variable, data) {
    $("div[name='AlertMenu']").blur();
    App.Variables.errorMsg.dataSet.dataValue = "Unexpected error occurred while importing Roles.";
    $("html, body").animate({
        scrollTop: 0
    });
};

Partial.sv_importRolesonSuccess = function(variable, data) {
    $("div[name='AlertMenu']").blur();
    App.Variables.successMessage.dataSet.dataValue = Partial.appLocale.ROLES_IMPORTED_SUCCESSFULLY;
    $("html, body").animate({
        scrollTop: 0
    });
    Partial.Variables.getAllRolesPartial.invoke();
};

Partial.sv_downloadRolesonError = function(variable, data) {
    App.Variables.errorMsg.dataSet.dataValue = "Unexpected error occurred while exporting Roles.";
    $("html, body").animate({
        scrollTop: 0
    });
};
Partial.RoleListRender = function(widget, $data) {


    // widget.selectItem(2);

};
Partial.RoleListSetrecord = function(widget, $data) {

    App.Variables.RolePageCommunication.currentPageSize = widget.pagesize;

};

Partial.RoleListClick = function(widget, $data) {

    App.Variables.RolePageCommunication.currentPageSize = widget.pagesize;
    console.log("Role List clicked !! " + App.Variables.RolePageCommunication.currentPageSize)

};