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
Partial.timeZones = [];

Partial.onReady = function() {

    /*
    
             * variables can be accessed through 'Partial.Variables' property here
             * e.g. to get dataSet in a staticVariable named 'loggedInUser' use following script
             * Partial.Variables.loggedInUser.getData()
             *
             * widgets can be accessed through 'Partial.Widgets' property here
             * e.g. to get value of text widget named 'username' use following script
             * 'Partial.Widgets.username.datavalue'
             */
    //Partial.usersPartial = [];
    //Partial.Variables.userCreateMV.dataSet = [];
    Partial.timeZones = Intl.supportedValuesOf('timeZone');
    App.Variables.errorMsg.dataSet.dataValue = null;
    App.Variables.successMessage.dataSet.dataValue = null;
    Partial.Variables.userMV.dataSet = [];
    Partial.Variables.userMVPre.dataSet = [];
    Partial.Variables.PermissionsMV.dataSet = [];
    Partial.Variables.permissionCopy.dataSet = [];
    var selectedIds = [];
    Partial.TestTeamId = [];
    Partial.Variables.dataTeam.dataSet = [];

    /* if (App.Variables.appUsersMV.dataSet && App.Variables.appUsersMV.dataSet.length < 1) {
         App.Variables.getTenantUsersSV.invoke();
     } else {
         App.Variables.appUsersMV.dataSet.forEach(function(au) {
             Partial.usersPartial = {
                 "Username": au.cn,
                 "Emailaddress": au.mail,
                 "Groups": "0",
                 "Roles": "0",
                 "IsActive": true
             }

             Partial.Variables.userCreateMV.dataSet.push(Partial.usersPartial);
         });
         App.allUsers();
     }*/


    Partial.statusData = [{
        id: 'New',
        title: 'New'
    }, {
        id: 'Open',
        title: 'Open'
    }, {
        id: 'Active',
        title: 'Active'
    }, {
        id: 'Closed',
        title: 'Closed'
    }];


    subComboBox = $('#subStatusInputBox').comboTree({
        source: Partial.statusData,
        isMultiple: true,
        cascadeSelect: true,
        collapse: true
    });



};

Partial.userCreateMVTable1_firstNameOnClick = function($event, widget, row) {

};


Partial.gotoUserDetails = function(row) {
    Partial.Variables.PermissionsMV.dataSet = [];
    Partial.Variables.permissionCopy.dataSet = [];

    Partial.Variables.dialogUserId.dataSet = {};
    Object.assign(Partial.Variables.dialogUserId.dataSet, row);
    Partial.Variables.executeGetRoleByUserId.setInput({
        'UserId': row.id
    });
    Partial.Variables.executeGetRoleByUserId.invoke();
    // Partial.Variables.executeGetPermissionByUserId.setInput({
    //     'UserId': row.id
    // });
    // Partial.Variables.executeGetPermissionByUserId.invoke();
    Partial.Variables.executeGetGroupByUserId.setInput({
        'UserId': row.id
    });
    Partial.Variables.executeGetGroupByUserId.invoke();
}


Partial.gotoIsActiveUser = function(row, widget) {
    App.Variables.successMessage.dataSet.dataValue = null;
    App.Variables.errorMsg.dataSet.dataValue = null;
    let usersToUpdate = [];
    let userRow = row;

    setTimeout(() => {
        //if no selection is made -> update the clicked ROW data
        if (Partial.Widgets.userMVTable1.selecteditem.length === 0) {
            if ($("label > input[type='checkbox']", widget.$element).is(':checked')) {
                usersToUpdate.push({
                    'userId': userRow.id,
                    'isActivate': 1
                });
            } else {
                usersToUpdate.push({
                    'userId': userRow.id,
                    'isActivate': 0
                });
            }

        } else {
            //if only one selection is made
            if (Partial.Widgets.userMVTable1.selecteditem.length === 1) {
                //selection made is same as userRow clicked
                if (Partial.Widgets.userMVTable1.selecteditem[0].id === userRow.id) {
                    if ($("label > input[type='checkbox']", widget.$element).is(':checked')) {
                        usersToUpdate.push({
                            'userId': userRow.id,
                            'isActivate': 1
                        });
                    } else {
                        usersToUpdate.push({
                            'userId': userRow.id,
                            'isActivate': 0
                        });
                    }

                } else {
                    //selection made is different to clicked ROW
                    //update selected item
                    let user_index = Partial.Widgets.userMVTable1.dataset.findIndex((userData) => {
                        return userData.id === Partial.Widgets.userMVTable1.selecteditem[0].id;
                    });

                    let activeToggle_Element = $("input[type='checkbox']", $("td:last-child", $("div[name='userMVTable1'] .app-grid-content > table tr")[user_index]))[0];
                    if ($(activeToggle_Element).is(':checked')) {
                        usersToUpdate.push({
                            'userId': Partial.Widgets.userMVTable1.selecteditem[0].id,
                            'isActivate': 0
                        });
                    } else {
                        usersToUpdate.push({
                            'userId': Partial.Widgets.userMVTable1.selecteditem[0].id,
                            'isActivate': 1
                        });
                    }

                    //clicked ROW data update
                    if ($("label > input[type='checkbox']", widget.$element).is(':checked')) {
                        usersToUpdate.push({
                            'userId': userRow.id,
                            'isActivate': 1
                        });
                    } else {
                        usersToUpdate.push({
                            'userId': userRow.id,
                            'isActivate': 0
                        });
                    }
                }
            } else {
                let matchedData = Partial.Widgets.userMVTable1.selecteditem.find((userItem) => {
                    return userItem.id === userRow.id;
                });

                //if the clicked ROW is part of the selected items
                if (matchedData) {
                    Partial.Widgets.userMVTable1.selecteditem.forEach((i) => {
                        let user_index = Partial.Widgets.userMVTable1.dataset.findIndex((userData) => {
                            return userData.id === i.id;
                        });

                        let activeToggle_Element = $("input[type='checkbox']", $("td:last-child", $("div[name='userMVTable1'] .app-grid-content > table tr")[user_index]))[0];
                        if (i.id === userRow.id) {
                            if ($(activeToggle_Element).is(':checked')) {
                                usersToUpdate.push({
                                    'userId': i.id,
                                    'isActivate': 1
                                });
                            } else {
                                usersToUpdate.push({
                                    'userId': i.id,
                                    'isActivate': 0
                                });
                            }
                        } else {
                            if ($(activeToggle_Element).is(':checked')) {
                                usersToUpdate.push({
                                    'userId': i.id,
                                    'isActivate': 0
                                });
                            } else {
                                usersToUpdate.push({
                                    'userId': i.id,
                                    'isActivate': 1
                                });
                            }
                        }
                    });

                } else {
                    //for clicked ROW
                    if ($("label > input[type='checkbox']", widget.$element).is(':checked')) {
                        usersToUpdate.push({
                            'userId': userRow.id,
                            'isActivate': 1
                        });
                    } else {
                        usersToUpdate.push({
                            'userId': userRow.id,
                            'isActivate': 0
                        });
                    }

                    //for selected items
                    Partial.Widgets.userMVTable1.selecteditem.forEach((i) => {
                        let user_index = Partial.Widgets.userMVTable1.dataset.findIndex((userData) => {
                            return userData.id === i.id;
                        });

                        let activeToggle_Element = $("input[type='checkbox']", $("td:last-child", $("div[name='userMVTable1'] .app-grid-content > table tr")[user_index]))[0];
                        if ($(activeToggle_Element).is(':checked')) {
                            usersToUpdate.push({
                                'userId': i.id,
                                'isActivate': 0
                            });
                        } else {
                            usersToUpdate.push({
                                'userId': i.id,
                                'isActivate': 1
                            });
                        }
                    });

                }
            }
        }

        Partial.Widgets.userMVTable1.selecteditem = [];
        // console.log(usersToUpdate);
        Partial.Variables.sv_bulkActivateDeactivate.invoke({
            "inputFields": {
                "userJSONList": JSON.stringify(usersToUpdate)
            }
        });

    }, 50);
};


Partial.userCreateMVTable1_IsActiveOnClick = function($event, widget, row) {

};


Partial.getAllUsersonError = function(variable, data) {
    App.Variables.errorMsg.dataSet.dataValue = "Unexpected error occurred while fetching Users.";
    $("html, body").animate({
        scrollTop: 0
    });
};

Partial.getAllUsersonSuccess = async function(variable, data) {
    Partial.Widgets.userMVTable1.clearFilter(true);
    Partial.Widgets.userMVTable1.selecteditem = [];
    App.Variables.successMessage.dataSet.dataValue = null;
    App.Variables.errorMsg.dataSet.dataValue = null;

    // if (Partial.Variables.userMV.dataSet.length <= 0) {
    //     Partial.Widgets.userMVTable1.nodatamessage = Partial.appLocale.MESSAGE_NO_DATA;
    // }

    Partial.usersLength = 0;
    Partial.check = false;
    Partial.Variables.userMV.dataSet = [];
    Partial.Variables.userMVPre.dataSet = [];
    Partial.usersObj = [];
    Partial.usersLength = data.length;

    for (let d of data) {
        // if (Partial.usersLength > 0) {
        //     Partial.Widgets.userMVTable1.nodatamessage = Partial.appLocale.MESSAGE_LOADING;
        // } else {
        //     Partial.Widgets.userMVTable1.nodatamessage = Partial.appLocale.MESSAGE_NO_DATA;
        // }
        //Partial.Variables.currenRowId.dataSet.dataValue = d.id
        // setTimeout(function() {

        Partial.usersObj = {
            "id": d.id,
            "Username": d.firstName + " " + d.lastName,
            "userId": d.userId,
            "Emailaddress": d.email,
            "IsActive": d.active,
            'firstName': d.firstName,
            'lastName': d.lastName,
            'createdBy': d.createdBy,
            'createdOn': d.createdOn,
            'updatedBy': d.updatedBy,
            'updatedOn': d.updatedOn,
            'lendingLimit': d.lendingLimit,
            'timeZone': d.timeZone,
            'Groups': 0,
            'Roles': 0
        };

        Partial.Variables.groupsCount.dataSet.dataValue = "";
        await Partial.Variables.executeGetGroupsRolesByUserId.invoke({
            "inputFields": {
                "UserId": d.id
            }
        });

        if (Partial.Variables.executeGetGroupsRolesByUserId.dataSet.length > 1) {
            Partial.usersObj.Groups = Partial.Variables.executeGetGroupsRolesByUserId.dataSet[0].count___;
            Partial.usersObj.Roles = Partial.Variables.executeGetGroupsRolesByUserId.dataSet[1].count___;
        } else if (Partial.Variables.executeGetGroupsRolesByUserId.dataSet.length = 1 && Partial.Variables.executeGetGroupsRolesByUserId.dataSet[0].count___) {
            Partial.usersObj.Groups = Partial.Variables.executeGetGroupsRolesByUserId.dataSet[0].count___;
            Partial.usersObj.Roles = Partial.Variables.executeGetGroupsRolesByUserId.dataSet[0].count___;
        } else {
            Partial.usersObj.Groups = 0;
            Partial.usersObj.Roles = 0;
        }

        let c = Partial.Variables.userMVPre.dataSet.find((element) => {
            return element.userId === Partial.usersObj.userId;
        });

        if (!c) {
            Partial.Variables.userMVPre.dataSet.push(Partial.usersObj);
            Partial.Variables.userMV.dataSet.push(Partial.usersObj);
        }

        // Partial.Variables.executeGetGroupsRolesByUserId.invoke({}, function(rm) {
        //     Partial.Variables.groupsCount.dataSet.dataValue = rm.content.length;
        //     console.log("Processing user (inside): " + d.userId);
        //     Partial.usersObj = {
        //         "id": d.id,
        //         "Username": d.firstName + " " + d.lastName,
        //         "userId": d.userId,
        //         "Emailaddress": d.email,
        //         "IsActive": d.active,
        //         'firstName': d.firstName,
        //         'lastName': d.lastName,
        //         'createdBy': d.createdBy,
        //         'createdOn': d.createdOn,
        //         'updatedBy': d.updatedBy,
        //         'updatedOn': d.updatedOn
        //     };

        //     if (rm.content.length > 1) {
        //         Partial.usersObj.Groups = rm.content[0].count___;
        //         Partial.usersObj.Roles = rm.content[1].count___;
        //     } else if (rm.content.length = 1 && rm.content[0].count___) {
        //         Partial.usersObj.Groups = rm.content[0].count___;
        //         Partial.usersObj.Roles = rm.content[0].count___;
        //     } else {
        //         Partial.usersObj.Groups = 0;
        //         Partial.usersObj.Roles = 0;
        //     }

        //     let c = Partial.Variables.userMVPre.dataSet.find((element) => {
        //         return element.userId === Partial.usersObj.userId;
        //     });

        //     if (!c) {
        //         console.log("Pushing user: " + Partial.usersObj.userId);
        //         Partial.Variables.userMVPre.dataSet.push(Partial.usersObj);
        //         Partial.Variables.userMV.dataSet.push(Partial.usersObj);
        //     }
        // });
    }
};

/*Partial.getAllUsersCreateonError = function(variable, data) {

};
Partial.getAllUsersCreateonSuccess = function(variable, data) {

    //
    if (data.length > 0) {
        Partial.Variables.userCreateMV.dataSet.forEach(function(user) {
            // data.forEach(function(dbusers) {
            ///  if (user.Username !== dbusers.userId) {
            //  if (!data.includes(user)) {

            var userData = data.find(element => element.userId === user.Username);
            if (!userData) {
                Partial.Variables.createUser.setInput({
                    'firstName': user.firstName,
                    'lastName': user.lastName,
                    'userId': user.Username,
                    'email': user.Emailaddress,
                    'isActive': true,
                    'createdBy': Partial.Variables.loggedInUser.dataSet.name,
                    'createdOn': new Date(),
                    'updatedBy': Partial.Variables.loggedInUser.dataSet.name,
                    'updatedOn': new Date()
                });
                Partial.Variables.createUser.invoke();
            }

        });
    }

};
*/
// Partial.updateUseronError = function(variable, data) {

// };

// Partial.updateUseronSuccess = function(variable, data) {
//     /*setTimeout(fade_out, 5000);

//     function fade_out() {
//         $("#message").fadeOut().empty();
//     }*/

//     //$("#message").delay(3200).fadeOut(300);
//     //setTimeout(, 5000);
//     App.Variables.successMessage.dataSet.dataValue = Partial.appLocale.USER_UPDATED_SUCCESSFULLY;
// };

Partial.toggle1Click = function($event, widget) {
    //
};

Partial.toggle1Change = function($event, widget, newVal, oldVal) {
    //
};
Partial.AlertMenuUserSelect = function($event, widget, $item) {

    if ($item.name == "Export") {
        exportToExcelFile();
    } else {
        let dynamicFileInput = $('<input type="file" id="fu_Users" name="Users_fileUpload" accept=".xlsx" onchange="processUsersImport(this);">');
        dynamicFileInput.click();
    }

};

processUsersImport = function(input) {
    App.Variables.errorMsg.dataSet.dataValue = null;
    App.Variables.successMessage.dataSet.dataValue = null;

    if (input.files.length > 0) {
        try {
            const importFile = input.files[0];
            Partial.Variables.sv_importUsers.invoke({
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
}

exportToExcelFile = function() {

    App.Variables.errorMsg.dataSet.dataValue = null;
    App.Variables.successMessage.dataSet.dataValue = null;

    const linkElement = document.createElement('a');
    const pageURL = window.location.href.split("#/")[0];
    linkElement.setAttribute('href', pageURL + "services/fileRelatedBS/downloadUsers");
    linkElement.setAttribute('target', '_blank');

    linkElement.click();
    linkElement.remove();
}

Partial.executeGetGroupByUserIdonSuccess = function(variable, data) {
    Partial.Widgets.dialogUserDetails.open();
};

Partial.executeGetRoleByUserIdonSuccess = function(variable, data) {
    data.forEach(function(d) {

        Partial.Variables.executeGetPermissionByRoleId.setInput({
            'RoleId': d.id
        });
        Partial.Variables.executeGetPermissionByRoleId.invoke();

    });

    // Partial.Variables.permissionCopy.dataSet = Partial.Variables.PermissionsMV.dataSet;
};


Partial.executeGetPermissionByUserIdonSuccess = function(variable, data) {

};

Partial.executeGetPermissionByRoleIdonSuccess = function(variable, data) {

    data.forEach(function(d) {

        //if (!Partial.Variables.PermissionsMV.dataSet.includes(d)) {
        let cr = Partial.Variables.PermissionsMV.dataSet.find(element => element.name == d.name);
        if (!cr) {
            Partial.Variables.PermissionsMV.dataSet.push(d);
            Partial.Variables.permissionCopy.dataSet.push(d);
        }

    });

};

Partial.roleSearchTextKeyup = function($event, widget) {
    //Partial.Variables.rolesCopy.dataSet = Partial.Variables.allRolesUI.dataSet;
};

Partial.permissionSearchTextKeyup = function($event, widget) {
    if (widget.datavalue) {
        let results = Partial.Variables.permissionCopy.dataSet.filter((item) => {
            return item.description.toLowerCase().includes(widget.datavalue.toLowerCase());
        });

        Partial.Variables.PermissionsMV.dataSet = [];
        Object.assign(Partial.Variables.PermissionsMV.dataSet, results);
    } else {
        Partial.Variables.PermissionsMV.dataSet = [];
        Object.assign(Partial.Variables.PermissionsMV.dataSet, Partial.Variables.permissionCopy.dataSet);
    }
};

Partial.panel1Expand = function($event, widget) {
    Partial.Variables.isExpandedGroup.dataSet.dataValue = true;
};

Partial.panel1Collapse = function($event, widget) {
    Partial.Variables.isExpandedGroup.dataSet.dataValue = false;
};
Partial.panelPermissionExpand = function($event, widget) {
    Partial.Variables.isExpandedPermission.dataSet.dataValue = true;
};

Partial.panelPermissionCollapse = function($event, widget) {
    Partial.Variables.isExpandedPermission.dataSet.dataValue = false;
};

Partial.userMVTable1Click = function($event, widget, row) {

    App.Variables.errorMsg.dataSet.dataValue = null;
    App.Variables.successMessage.dataSet.dataValue = null;
};

App.usersPageReload = function() {


    Partial.Variables.getAllUsers.invoke();

}
Partial.userMVTable1Tap = function($event, $data) {
    /* */
    Partial.Widgets.userMVTable1.nodatamessage = Partial.appLocale.MESSAGE_NO_DATA;
};

Partial.saveUser_btnClick = function($event, widget) {
    Partial.Variables.userLocalMsg.dataSet.dataValue = null;

    //update selected USER data
    Partial.Variables.sv_updateUserDetails.invoke();
};

Partial.sv_updateUserDetailsonError = function(variable, data) {
    console.error(data);
    Partial.Variables.userLocalMsg.dataSet.dataValue = "Failed to update user details. Try again";
    $(".modal").animate({
        scrollTop: 0
    });
};

Partial.sv_updateUserDetailsonSuccess = function(variable, data) {
    //re-setting App level timezone to NULL so that on next-redirect (the APP level timezone API is called again on the top-nav)
    App.timeZone = null;

    Partial.Widgets.dialogUserDetails.close();
    Partial.Variables.getAllUsers.invoke();
};

Partial.dialogUserDetailsOpened = function($event, widget) {

    Partial.Variables.userLocalMsg.dataSet.dataValue = null;
};

Partial.sv_bulkActivateDeactivateonError = function(variable, data) {
    App.Variables.errorMsg.dataSet.dataValue = "Unexpected error occurred while bulk activating/de-activating.";
    $("html, body").animate({
        scrollTop: 0
    });
};

Partial.sv_bulkActivateDeactivateonSuccess = function(variable, data) {
    App.Variables.successMessage.dataSet.dataValue = Partial.appLocale.USER_UPDATED_SUCCESSFULLY;
    $("html, body").animate({
        scrollTop: 0
    });
    Partial.Variables.getAllUsers.invoke();
};

Partial.sv_importUsersonError = function(variable, data) {
    $("div[name='AlertMenuUser']").blur();
    App.Variables.errorMsg.dataSet.dataValue = "Unexpected error occurred while importing Users.";
    $("html, body").animate({
        scrollTop: 0
    });
};

Partial.sv_importUsersonSuccess = function(variable, data) {
    $("div[name='AlertMenuUser']").blur();
    App.Variables.successMessage.dataSet.dataValue = Partial.appLocale.USER_DATA_IMPORTED_SUCCESSFULLY;
    $("html, body").animate({
        scrollTop: 0
    });
    Partial.Variables.getAllUsers.invoke();
};

Partial.sv_downloadUsersonError = function(variable, data) {
    App.Variables.errorMsg.dataSet.dataValue = "Unexpected error occurred while exporting Users.";
    $("html, body").animate({
        scrollTop: 0
    });
};
Partial.tz_selectBlur = function($event, widget) {
    Partial.saveUser_btnClick($event, Partial.Widgets.saveUser_btnClick);
};

Partial.executeGetGroupByUserId1onSuccess = function(variable, data) {

};

Partial.dialogUserDetails = function(row) {
    //debugger;
    Partial.Variables.dialogUserId.dataSet = {};

    Object.assign(Partial.Variables.dialogUserId.dataSet, row);


    //Partial.Variables.getWorkCategoryByUserId.invoke();
    // debugger;
    var lv = Partial.Variables.getWorkCategoryByUserId;
    lv.listRecords({
            filterFields: {
                "user.email": {
                    "value": Partial.Variables.dialogUserId.dataSet.email

                }
            }
        },
        function(data) {
            //debugger;
            //console.log('success', success)
            Partial.Variables.getWorkCategoryByUserId.dataSet = [];
            Partial.Variables.getWorkCategoryByUserId.dataSet = data;
            Partial.Widgets.dialogUserDetails.open();
        },
        function(error) {
            //console.error('error', error)
        });
};

Partial.dialogUpdateUserDetailsAction = function($event, row) {
    debugger;
    Partial.Variables.dialogUserId.dataSet = {};
    Object.assign(Partial.Variables.dialogUserId.dataSet, row);

    Partial.Widgets.updateUser.open();
    Partial.Variables.getCodeFromDomainValueAsWorkCategory.dataSet;


    Partial.statusData = [];
    Partial.Variables.getCodeFromDomainValueAsWorkCategory.dataSet.forEach(function(item) {
        Partial.statusData.push({
            id: item.code.replace(/\s/g, ''),
            title: item.code
        });
    });


    setTimeout(function() {
        subComboBox = $('#subStatusInputBox').comboTree({
            source: Partial.statusData,
            isMultiple: true,
            cascadeSelect: true,
            collapse: true
        });


    }, 50);

}
Partial.userMVTable1_customRowAction = function($event, row) {
    // Partial.Variables.deletUser.setInput({
    //     'userId': row.email
    // });
    // Partial.Variables.deletUser.invoke();
    debugger;
    Partial.Variables.dialogUserId.dataSet = {};
    Object.assign(Partial.Variables.dialogUserId.dataSet, row);

    if (row.active) {

        Partial.Widgets.inactivateUserDialog.open();
    }
};


Partial.executeSearchUsersForm1_resetAction = function($event) {
    debugger;
    Partial.Variables.dialogUserId.dataSet = {};
    Partial.Variables.searchUsers.dataSet = [];
    Partial.Variables.searchUsers.dataSet = Partial.Variables.ResetPREValue.dataSet;


};

Partial.resetTableDataonSuccess = function(variable, data) {
    Partial.Variables.ResetPREValue.dataSet = data;
};

Partial.button3Click = function($event, widget) {


    Partial.Widgets.createUserPage.open();
    debugger;
    Partial.Variables.getCodeFromDomainValueAsWorkCategory.dataSet;


    Partial.statusData = [];
    Partial.Variables.getCodeFromDomainValueAsWorkCategory.dataSet.forEach(function(item) {
        Partial.statusData.push({
            id: item.code.replace(/\s/g, ''),
            title: item.code
        });
    });


    setTimeout(function() {
        subComboBox = $('#subStatusInputBox').comboTree({
            source: Partial.statusData,
            isMultiple: true,
            cascadeSelect: true,
            collapse: true
        });


    }, 50);

};



Partial.createUserForm1_saveAction = function($event) {
    debugger;
    Partial.Widgets.createUserForm1.dataoutput.UserDTO;
    Partial.Variables.UserManagementServiceCreateUser.dataBinding.UserDTO.workCategory = subComboBox.getSelectedIds();
    if (Partial.Widgets.createUserForm1.dataoutput.UserDTO.firstName != undefined && Partial.Widgets.createUserForm1.dataoutput.UserDTO.lastName != undefined && Partial.Widgets.createUserForm1.dataoutput.UserDTO.emplId != undefined && Partial.Widgets.createUserForm1.dataoutput.UserDTO.email != undefined && Partial.Widgets.createUserForm1.dataoutput.UserDTO.role != undefined && Partial.Widgets.createUserForm1.dataoutput.UserDTO.teamId != undefined &&
        Partial.Widgets.createUserForm1.dataoutput.UserDTO.userId != undefined) {


        Partial.Variables.UserManagementServiceCreateUser.invoke({},
            function(data) {
                debugger; // Success Callback
                console.log("success", data);
            },
            function(error) {
                debugger; // Error Callback

            });
    }

};

Partial.userMVTable1_customRow1Action = function($event, row) {
    //Partial.TestTeamId = row;
    Partial.Variables.dialogUserId.dataSet = {};
    Object.assign(Partial.Variables.dialogUserId.dataSet, row);

    Partial.Widgets.updateUser.open();
    Partial.TestTeamId = row;
    Partial.Variables.getCodeFromDomainValueAsWorkCategory.dataSet;

    // Partial.Variables.dialogUserId.dataSet.workCategory.split(",");


    // Partial.statusData = [];
    Partial.statusData = [];

    if (row.workCategory != null) {

        var ar = row.workCategory.split(",");
    }
    Partial.Variables.getCodeFromDomainValueAsWorkCategory.dataSet.forEach(function(item) {
        Partial.statusData.push({
            id: item.code.replace(/\s/g, ''),
            title: item.code
        });
    });

    debugger;
    setTimeout(function() {
        subComboBox1 = $('#updateUserInputBox').comboTree({
            source: Partial.statusData,
            isMultiple: true,
            cascadeSelect: true,
            collapse: true,
            selected: ar
        });

    }, 50);

};
Partial.updateUserForm1_saveAction = function($event) {

    Partial.Widgets.updateUserForm1.dataoutput.UserDTO;
    Partial.Variables.UserManagementServiceUpdateUser.dataBinding.UserDTO.workCategory = subComboBox1.getSelectedIds();
    if (Partial.TestTeamId.teamId != null && Partial.Widgets.updateUserForm1.dataoutput.UserDTO.teamId == null) {
        Partial.TestTeamIdFinal = Partial.Variables.dataTeam.dataSet.filter(value => value.teamId == Partial.TestTeamId.teamId)
        Partial.Variables.UserManagementServiceUpdateUser.dataBinding.UserDTO.teamId = Partial.TestTeamIdFinal[0].id;
    } else {
        Partial.Widgets.updateUserForm1.dataoutput.UserDTO.teamId

        Partial.TestTeamIdFinal = Partial.Variables.dataTeam.dataSet.filter(value => value.teamId == Partial.Widgets.updateUserForm1.dataoutput.UserDTO.teamId)
        Partial.Variables.UserManagementServiceUpdateUser.dataBinding.UserDTO.teamId = Partial.TestTeamIdFinal[0].id;
    }


    var type = typeof Partial.Widgets.updateUserForm1.dataoutput.UserDTO.teamManagerId

    if (type == 'string') {

        Partial.Variables.UserManagementServiceUpdateUser.dataBinding.UserDTO.teamManagerId = null;
    }
    //dataBinding
    if (Partial.Widgets.updateUserForm1.dataoutput.UserDTO.firstName != undefined && Partial.Widgets.updateUserForm1.dataoutput.UserDTO.lastName != undefined && Partial.Widgets.updateUserForm1.dataoutput.UserDTO.emplId != undefined && Partial.Widgets.updateUserForm1.dataoutput.UserDTO.email != undefined && Partial.Widgets.updateUserForm1.dataoutput.UserDTO.role != undefined && Partial.Widgets.updateUserForm1.dataoutput.UserDTO.teamId != undefined && Partial.Widgets.updateUserForm1.dataoutput.UserDTO.userId != undefined) {
        Partial.Variables.UserManagementServiceUpdateUser.invoke({}, function(data) {
            // Success Callback
            console.log("success", data);
        }, function(error) {
            // Error Callback
            console.log("error", error)
        });
    }
};

/*Partial.getmanagerForUpdateuseronSuccess = function(variable, data) {
    Partial.sadsa = data;
    debugger;
};*/

Partial.getTeamonSuccess = function(variable, data) {
    Partial.Variables.dataTeam.dataSet = data;
    debugger;
};
Partial.createUserForm1Error = function($event, widget, $data) {
    debugger;
};