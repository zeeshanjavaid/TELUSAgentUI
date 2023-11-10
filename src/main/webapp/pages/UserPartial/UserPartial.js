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
    App.Variables.errorMsg.dataSet.dataValue = null;
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

        title: 'New'
    }, {

        title: 'Open'
    }, {

        title: 'Active'
    }, {

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


    }
};


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
    const pageURL = window.location.href.split("/")[0];
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
    let showIcon = document.querySelector('.show-icon');
    let hideIcon = document.querySelector('.hide-icon');
    if (hideIcon) {
        hideIcon.style.display = 'none';
    }
    if (showIcon) {
        showIcon.style.display = 'inline-block';
    }

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
    debugger;
    Partial.Variables.dialogUserId.dataSet = {};

    Object.assign(Partial.Variables.dialogUserId.dataSet, row);


    // debugger;
    var lv = Partial.Variables.getWorkCategoryByUserId;

    lv.listRecords({
            filterFields: {
                "user.id": {
                    "value": Partial.Variables.dialogUserId.dataSet.id

                }
            }
        },
        function(data) {
            //debugger;
            //console.log('success', success)
            Partial.Variables.getWorkCategoryByUserIdQuery.dataSet = [];
            Partial.Variables.getWorkCategoryByUserIdQuery.dataSet = data;
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
            // id: item.code.replace(/\s/g, ''),
            id: item.code,
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
    // Partial.Variables.searchUsers.dataSet = [];
    // Partial.Variables.searchUsers.dataSet = Partial.Variables.ResetPREValue.dataSet;
    Partial.Variables.searchUsers.setInput({
        'role': null,
        'userCriteria': null,
        'work_category': null,
        'managerId': null,
        'teamID': null
    });
    Partial.Variables.searchUsers.invoke();



};

// Partial.resetTableDataonSuccess = function(variable, data) {
//     Partial.Variables.ResetPREValue.dataSet = data;
// };

Partial.button3Click = function($event, widget) {
    debugger;


    Partial.Widgets.createUserPage.open();
    debugger;
    Partial.Variables.getCodeFromDomainValueAsWorkCategory.dataSet;


    Partial.statusData = [];
    Partial.Variables.getCodeFromDomainValueAsWorkCategory.dataSet.forEach(function(item) {
        Partial.statusData.push({

            //item.code
            //id: item.code.replace(/\s/g, ''),
            id: item.code,
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

    var isEmplIdExists;
    var isError = false;
    // validateFirstLetterOfEmplId(Partial.Widgets.createUserForm1.dataoutput.UserDTO.emplId);
    Partial.Variables.checkUserByEmplId.setInput({
        'emplid': Partial.Widgets.createUserForm1.dataoutput.UserDTO.emplId.charAt(0).toLowerCase() + Partial.Widgets.createUserForm1.dataoutput.UserDTO.emplId.slice(1)
    });


    Partial.Variables.executeSelectedGetManagerByTeamId.setInput({
        'teamId': Partial.Widgets.createUserForm1.dataoutput.UserDTO.teamId
    });


    Partial.Variables.executeSelectedGetManagerByTeamId.invoke();




    Partial.Variables.checkUserByEmplId.invoke({},
        function(data) {
            debugger;
            if (data.content != '') {
                isEmplIdExists = true;

            } else {
                isEmplIdExists = false;
            }

            Partial.Variables.UserManagementServiceCreateUser.dataBinding.UserDTO.workCategory = subComboBox.getSelectedIds();


            if (Partial.Variables.UserManagementServiceCreateUser.dataBinding.UserDTO.emplId.charAt(0).toUpperCase() && Partial.Variables.UserManagementServiceCreateUser.dataBinding.UserDTO.emplId.charAt(0) === 'X' || Partial.Variables.UserManagementServiceCreateUser.dataBinding.UserDTO.emplId.charAt(0) === 'T') {
                Partial.Variables.UserManagementServiceCreateUser.dataBinding.UserDTO.emplId = Partial.Variables.UserManagementServiceCreateUser.dataBinding.UserDTO.emplId.charAt(0).toLowerCase() + Partial.Variables.UserManagementServiceCreateUser.dataBinding.UserDTO.emplId.slice(1);
            } else if (Partial.Variables.UserManagementServiceCreateUser.dataBinding.UserDTO.emplId.charAt(0).toLowerCase() && !(Partial.Variables.UserManagementServiceCreateUser.dataBinding.UserDTO.emplId.charAt(0) === 'x' || Partial.Variables.UserManagementServiceCreateUser.dataBinding.UserDTO.emplId.charAt(0) === 't')) {

                App.Variables.createUserErrormsg.dataSet.dataValue = "Empl Id should start with letter x ot t.";
                isError = true;

            }


            if (!validateEmail(Partial.Widgets.createUserForm1.dataoutput.UserDTO.email)) {
                App.Variables.createUserErrormsg.dataSet.dataValue = "Please enter valid Email Address";
                isError = true;
            } else if (isEmplIdExists) {
                App.Variables.createUserErrormsg.dataSet.dataValue = "Empl ID already exists";
                isError = true;
            } else if (Partial.Widgets.createUserForm1.dataoutput.UserDTO.firstName != undefined && Partial.Widgets.createUserForm1.dataoutput.UserDTO.lastName != undefined && Partial.Widgets.createUserForm1.dataoutput.UserDTO.emplId != undefined && Partial.Widgets.createUserForm1.dataoutput.UserDTO.email != undefined && Partial.Widgets.createUserForm1.dataoutput.UserDTO.role != undefined && Partial.Widgets.createUserForm1.dataoutput.UserDTO.teamId != undefined &&
                Partial.Widgets.createUserForm1.dataoutput.UserDTO.userId != undefined) {
                if (!isError) {
                    Partial.Variables.UserManagementServiceCreateUser.invoke();
                    Partial.Variables.searchUsers.invoke();
                }
                //  setTimeout(messageTimeout, 4000);
                // Partial.Widgets.createUserPage.close();

            }

        },
        function(error) {
            return false;

        });

};


Partial.searchUsersFinal = function($event) {
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
                console.log("success", error);

            });
    }

};

Partial.userMVTable1_customRow1Action = function($event, row) {
    //Partial.TestTeamId = row;

    debugger;
    Partial.Variables.dialogUserId.dataSet = {};
    Partial.Variables.dialogUserId.dataSet = row;
    //  Object.assign(Partial.Variables.dialogUserId.dataSet, row);

    Partial.Widgets.updateUser.open();


    if (row.teamManager != null) {

        Partial.Variables.TELUSAgentUIDBGetUser.setInput({
            'id': row.id
        });
        Partial.Variables.TELUSAgentUIDBGetUser.invoke();
    } else {
        Partial.Variables.getCurrentManager.dataSet = null;
    }

    Partial.TestTeamId = row;
    Partial.Variables.getCodeFromDomainValueAsWorkCategory.dataSet;

    if (row.teamId != null) {

        Partial.TestTeamIdFinal = Partial.Variables.dataTeam.dataSet.filter(value => value.teamId == row.teamId)
        var teamId = Partial.TestTeamIdFinal[0].id;

        Partial.Variables.executeGetManagerByTeamIdVar.setInput({
            'teamId': teamId
        });
        Partial.Variables.executeGetManagerByTeamIdVar.invoke();
    }

    // Partial.Variables.dialogUserId.dataSet.workCategory.split(",");


    // Partial.statusData = [];
    Partial.statusData = [];

    if (row.workCategory != null) {

        var ar = row.workCategory.split(",");
    }
    Partial.Variables.getCodeFromDomainValueAsWorkCategory.dataSet.forEach(function(item) {
        Partial.statusData.push({
            // id: item.code.replace(/\s/g, ''),
            id: item.code,
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

    debugger;

    Partial.Widgets.updateUserForm1.dataoutput.UserDTO;
    if (subComboBox1.getSelectedIds() == null) {
        Partial.Variables.UserManagementServiceUpdateUser.dataBinding.UserDTO.workCategory = [];
    } else {
        Partial.Variables.UserManagementServiceUpdateUser.dataBinding.UserDTO.workCategory = subComboBox1.getSelectedIds();
    }

    Partial.TestTeamIdFinal = Partial.Variables.dataTeam.dataSet.filter(value => value.teamId == Partial.Widgets.updateUserForm1.dataoutput.UserDTO.teamId)
    Partial.Variables.UserManagementServiceUpdateUser.dataBinding.UserDTO.teamId = Partial.TestTeamIdFinal[0].id;

    var type = typeof Partial.Widgets.updateUserForm1.dataoutput.UserDTO.teamManagerId

    if (Partial.Widgets.UserDTO_teamManager_formWidget.displayValue === undefined) {
        Partial.Variables.UserManagementServiceUpdateUser.dataBinding.UserDTO.teamManagerId = null;
    } else if (type == "string" && Partial.Variables.executeGetManagerByTeamIdVar.dataSet.length > 0) {


        Partial.Variables.UserManagementServiceUpdateUser.dataBinding.UserDTO.teamManagerId = Partial.Variables.executeGetManagerByTeamIdVar.dataSet.filter(a => a.managerName === Partial.Widgets.updateUserForm1.dataoutput.UserDTO.teamManagerId)[0].id;

    } else {
        Partial.Variables.UserManagementServiceUpdateUser.dataBinding.UserDTO.teamManagerId = null;
    }

    if (!validateEmail(Partial.Widgets.updateUserForm1.dataoutput.UserDTO.email)) {
        debugger;
        App.Variables.updateUserErrormsg.dataSet.dataValue = "Please enter valid Email Address";
    } else if (Partial.Widgets.updateUserForm1.dataoutput.UserDTO.firstName != undefined && Partial.Widgets.updateUserForm1.dataoutput.UserDTO.lastName != undefined && Partial.Widgets.updateUserForm1.dataoutput.UserDTO.emplId != undefined && Partial.Widgets.updateUserForm1.dataoutput.UserDTO.email != undefined && Partial.Widgets.updateUserForm1.dataoutput.UserDTO.role != undefined && Partial.Widgets.updateUserForm1.dataoutput.UserDTO.teamId != undefined && Partial.Widgets.updateUserForm1.dataoutput.UserDTO.userId != undefined) {
        debugger;
        Partial.Variables.UserManagementServiceUpdateUser.invoke();
        Partial.Variables.searchUsers.invoke();
        Partial.Variables.searchUsers.setInput({
            'page': Partial.Widgets.userMVTable1.dataNavigator.dn.currentPage,
            'size': 10
        });
        Partial.Variables.searchUsers.invoke();
        setTimeout(messageTimeout, 4000);
        Partial.Widgets.updateUser.close();

    }
};



Partial.getTeamonSuccess = function(variable, data) {
    Partial.Variables.dataTeam.dataSet = data;
    debugger;


    Partial.Variables.getManagerNameByTeamId.setInput({
        'teamId': data[data.length - 1].id
    });

    Partial.Variables.getManagerNameByTeamId.invoke();

};
Partial.createUserForm1Error = function($event, widget, $data) {
    debugger;
};

Partial.panel1Expand = function($event, widget) {
    let showIcon = document.querySelector('.show-icon');
    let hideIcon = document.querySelector('.hide-icon');
    if (hideIcon) {
        hideIcon.style.display = 'none';
    }
    if (showIcon) {
        showIcon.style.display = 'inline-block';
    }
    Partial.Variables.isExpand.dataSet.dataValue = !Partial.Variables.isExpand.dataSet.dataValue;
};

Partial.panel1Collapse = function($event, widget) {
    let showIcon = document.querySelector('.show-icon');
    let hideIcon = document.querySelector('.hide-icon');
    if (hideIcon) {
        hideIcon.style.display = 'inline-block';
    }
    if (showIcon) {
        showIcon.style.display = 'none';
    }
    Partial.Variables.isExpand.dataSet.dataValue = !Partial.Variables.isExpand.dataSet.dataValue;

};

Partial.executeSearchUsersForm1_saveAction = function($event) {

    if (Partial.Widgets.executeSearchUsersForm1.formWidgets.role.datavalue != undefined && Partial.Widgets.executeSearchUsersForm1.formWidgets.role.datavalue != "") {
        debugger;

        Partial.Variables.searchUsers.setInput({
            'role': Partial.Widgets.executeSearchUsersForm1.formWidgets.role.datavalue,
            'userCriteria': Partial.Widgets.executeSearchUsersForm1.formWidgets.userCriteria.datavalue,
            'work_category': Partial.Widgets.executeSearchUsersForm1.formWidgets.work_category.datavalue,
            'managerId': Partial.Widgets.executeSearchUsersForm1.formWidgets.TeamManager.datavalue,
            'teamID': Partial.Widgets.executeSearchUsersForm1.formWidgets.TeamID.datavalue
        });
        Partial.Variables.searchUsers.invoke();
        App.Variables.errorMsg.dataSet.dataValue = "";
    } else {
        debugger;
        App.Variables.errorMsg.dataSet.dataValue = "Role is mandatory";
    }


};

Partial.UserManagementServiceCreateUseronError = function(variable, data) {
    debugger;
    //  setTimeout(messageTimeout, 4000);
    //  Partial.Widgets.createUserPage.close();

};
Partial.createUserForm1_resetAction = function($event) {

    App.Variables.errorMsg.dataSet.dataValue = "";
    App.Variables.createUserErrormsg.dataSet.dataValue = "";

};
Partial.createUserPageClose = function($event, widget) {

    App.Variables.errorMsg.dataSet.dataValue = "";
    App.Variables.createUserErrormsg.dataSet.dataValue = "";

};
Partial.updateUserForm1_Action1 = function($event) {

};

function validateEmail(email) {
    return email.match(/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/);
};


Partial.updateUserClose = function($event, widget) {
    App.Variables.updateUserErrormsg.dataSet.dataValue = "";
};
Partial.updateUserForm1Error = function($event, widget, $data) {

};
Partial.UserDTO_teamManagerClick = function($event, widget) {
    debugger;
};
Partial.UserDTO_teamIdChange = function($event, widget, newVal, oldVal) {
    debugger;

    if (typeof(newVal) == "number") {
        Partial.TestTeamIdFinal = Partial.Variables.dataTeam.dataSet.filter(value => value.id == newVal);
        var teamId = Partial.TestTeamIdFinal[0].id;

        Partial.Variables.executeGetManagerByTeamIdVar.setInput({
            'teamId': teamId
        });
        Partial.Variables.executeGetManagerByTeamIdVar.invoke();
    } else {


        Partial.TestTeamIdFinal = Partial.Variables.dataTeam.dataSet.filter(value => value.teamId == newVal);
        var teamId = Partial.TestTeamIdFinal[0].id;

        Partial.Variables.executeGetManagerByTeamIdVar.setInput({
            'teamId': teamId
        });
        Partial.Variables.executeGetManagerByTeamIdVar.invoke();


    }
}


Partial.userCriteriaKeypress = function($event, widget) {

    if ($event.keyCode == 13) {
        if (Partial.Widgets.executeSearchUsersForm1.formWidgets.role.datavalue != undefined && Partial.Widgets.executeSearchUsersForm1.formWidgets.role.datavalue != "") {
            debugger;

            Partial.Variables.searchUsers.setInput({
                'role': Partial.Widgets.executeSearchUsersForm1.formWidgets.role.datavalue,
                'userCriteria': Partial.Widgets.executeSearchUsersForm1.formWidgets.userCriteria.datavalue,
                'work_category': Partial.Widgets.executeSearchUsersForm1.formWidgets.work_category.datavalue,
                'managerId': Partial.Widgets.executeSearchUsersForm1.formWidgets.TeamManager.datavalue,
                'teamID': Partial.Widgets.executeSearchUsersForm1.formWidgets.TeamID.datavalue
            });
            Partial.Variables.searchUsers.invoke();
            App.Variables.errorMsg.dataSet.dataValue = "";
        } else {
            debugger;
            App.Variables.errorMsg.dataSet.dataValue = "Role is mandatory";
            $event.preventDefault();


        }
    }

};

Partial.UserManagementServiceUpdateUseronSuccess = function(variable, data) {
    debugger;
    Partial.Variables.getWorkCatByEmplIdForMultiSelect.invoke();
    App.refreshTeamManager();
    Partial.Variables.getCurrentManager.dataSet = null;
    Partial.Variables.successMessage.dataSet.dataValue = "User updated successfully";


};

Partial.TELUSAgentUIDBGetUseronSuccess = function(variable, data) {

    Partial.Variables.getCurrentManager.dataSet = data.managerId

    debugger;

};

Partial.UserManagementServiceCreateUseronSuccess = function(variable, data) {



    setTimeout(messageTimeout, 4000);
    App.refreshTeamManager();
    Partial.Widgets.createUserPage.close();
    Partial.Variables.successMessage.dataSet.dataValue = "User created successfully";




};

function messageTimeout() {
    App.Variables.successMessage.dataSet.dataValue = null;
}

App.refreshTeamsOnAdminPage = function() {

    Partial.Variables.getTeam.invoke();

};