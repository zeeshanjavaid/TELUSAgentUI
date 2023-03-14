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
     * e.g. to get value of text widget named 'username' use following script
     * 'Partial.Widgets.username.datavalue'
     */
    //    debugger;
    Partial.Variables.groupData.dataSet = [];
    Partial.Variables.uploadGroup.dataSet = [];
    Partial.Variables.uploadGroupItem.dataSet = [];
    App.Variables.errorMsg.dataSet.dataValue = null;
    App.Variables.successMessage.dataSet.dataValue = null;
    Partial.canceledClicked = false;
    Partial.groupExists = false;
    Partial.groupNameExists = false;
    Partial.currentRoleName = "";
    Partial.isDeleteGroup = false;
    Partial.Variables.leftRolesList.dataSet = [];
    Partial.Variables.leftUserList.dataSet = [];

    // if (Partial.pageParams.roleId === undefined) {
    Partial.Variables.readOnlyMode.dataSet.dataValue = true;
    // }

};

Partial.SaveButtonClick = function($event, widget) {

    // SaveButton
    // $('button[name="' + 'SaveButton' + '"]').eq(0).blur();
    // debugger;
    Partial.isDeleteGroup = false;
    Partial.groupExists = false;
    Partial.groupNameExists = false;
    Partial.Widgets.GroupNameText.required = false;
    App.Variables.errorMsg.dataSet.dataValue = null;
    App.Variables.successMessage.dataSet.dataValue = null;
    let pattern = Partial.Widgets.GroupNameText.regexp;

    Partial.Variables.readOnlyMode.dataSet.dataValue = true;

    App.Variables.GroupPageCommunication.currentGroupInFocusId = Partial.pageParams.groupId;
    //if ((Partial.Widgets.GroupNameText.datavalue == undefined ? Partial.Widgets.GroupNameText.datavalue == undefined : Partial.Widgets.GroupNameText.datavalue == "")) {
    if (Partial.Widgets.GroupNameText.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = Partial.appLocale.TEAM_NAME_MANDATORY;
        Partial.scrollToTop();
        Partial.Widgets.GroupNameText.required = true;

    } else if (Partial.Widgets.GroupNameText.datavalue.match(pattern) == null) {
        App.Variables.errorMsg.dataSet.dataValue = "Team name is invalid.";
        Partial.scrollToTop();
    } else {

        //    debugger;
        if (Partial.pageParams.groupId) {
            Partial.Variables.getAllTeams.dataSet.forEach(function(group) {
                if (group.teamName.toLowerCase() == Partial.Widgets.GroupNameText.datavalue.toLowerCase() && group.id != Partial.pageParams.groupId) {
                    Partial.groupNameExists = true;
                }
            });
        } else {
            Partial.Variables.getAllTeams.dataSet.forEach(function(group) {
                if (group.teamName.toLowerCase() == Partial.Widgets.GroupNameText.datavalue.toLowerCase()) {
                    Partial.groupExists = true;
                }
            });
        }

        //    debugger;
        if (!Partial.groupExists && !Partial.groupNameExists) {
            if (Partial.pageParams.groupId && Partial.pageParams.groupId > 0) {
                Partial.Variables.updateTeam.setInput({
                    'id': Partial.pageParams.groupId,
                    'teamId': Partial.Variables.teamData.dataSet.TeamId,
                    'teamName': Partial.Variables.teamData.dataSet.TeamName,
                    'description': Partial.Variables.teamData.dataSet.TeamDescription,
                    'createdBy': Partial.Variables.getTeam.dataSet[0].createdBy,
                    'createdOn': Partial.Variables.getTeam.dataSet[0].createdOn,
                    'updatedBy': App.Variables.getLoggedInUserId.dataSet[0].id,
                    'updatedOn': new Date()
                });
                //    debugger;
                Partial.Variables.updateTeam.update();
            } else {
                //    debugger;
                Partial.Variables.createTeam.setInput({
                    'teamId': Partial.Variables.teamData.dataSet.TeamId,
                    'teamName': Partial.Variables.teamData.dataSet.TeamName,
                    'description': Partial.Variables.teamData.dataSet.TeamDescription,
                    'createdBy': App.Variables.getLoggedInUserId.dataSet[0].id,
                    'createdOn': new Date(),
                    'updatedBy': App.Variables.getLoggedInUserId.dataSet[0].id,
                    'updatedOn': new Date()
                });
                Partial.Variables.createTeam.invoke();
            }


        } else if (Partial.groupExists) {
            App.Variables.errorMsg.dataSet.dataValue = Partial.appLocale.TEAM_ALREADY_EXIST;
            Partial.scrollToTop();

        } else {
            App.Variables.errorMsg.dataSet.dataValue = Partial.appLocale.TEAMNAME_ALREADY_EXIST;
            Partial.scrollToTop();
        }


    }

};

Partial.CancelbuttonClick = function($event, widget) {

    Partial.Variables.readOnlyMode.dataSet.dataValue = true;

    App.Variables.errorMsg.dataSet.dataValue = null;
    App.Variables.successMessage.dataSet.dataValue = null;
    Partial.isDeleteGroup = false;
    Partial.canceledClicked = true;

    if (Partial.pageParams.groupId) {
        if (Partial.Widgets.DualListRoles_TD && Partial.Widgets.DualListUsers_TD) {
            Partial.Widgets.DualListUsers_TD.rightdataset = Partial.Variables.selectedUsers.dataSet;
            Partial.Widgets.DualListRoles_TD.rightdataset = Partial.Variables.selectedRoles.dataSet;
            Partial.Widgets.DualListUsers_TD.leftdataset = Partial.Variables.leftUserList.dataSet;
            Partial.Widgets.DualListRoles_TD.leftdataset = Partial.Variables.leftRolesList.dataSet;
        }

    } else {
        Partial.Variables.teamData.dataSet = [];
        Partial.pageParams.groupId = undefined;
        if (Partial.Widgets.DualListRoles_TD && Partial.Widgets.DualListUsers_TD) {
            Partial.Widgets.DualListUsers_TD.rightdataset = [];
            Partial.Widgets.DualListRoles_TD.rightdataset = [];
            Partial.Widgets.DualListUsers_TD.leftdataset = Partial.Variables.leftUserList.dataSet;
            Partial.Widgets.DualListRoles_TD.leftdataset = Partial.Variables.leftRolesList.dataSet;
        }
    }
    App.refreshAllTeams();
};

App.addGroups = function() {
    App.Variables.errorMsg.dataSet.dataValue = null;
    App.Variables.successMessage.dataSet.dataValue = null;
    Partial.Variables.groupData.dataSet = [];

    Partial.Variables.leftRolesList.dataSet = [];
    Partial.Variables.leftUserList.dataSet = [];
    Partial.Variables.selectedRoles.dataSet = [];
    Partial.Variables.selectedUsers.dataSet = [];
    /*Partial.Variables.getAllRole.invoke();
    Partial.Variables.getAllUsers.invoke();*/

    if (Partial.Widgets.DualListUsers_TD !== undefined) {
        Partial.Widgets.DualListUsers_TD.rightdataset = [];
        Partial.Widgets.DualListUsers_TD.leftdataset = [];
        // Partial.Widgets.DualListUsers_TD.new = true;
    }
    if (Partial.Widgets.DualListRoles_TD !== undefined) {
        Partial.Widgets.DualListRoles_TD.rightdataset = [];
        Partial.Widgets.DualListRoles_TD.leftdataset = [];
        // Partial.Widgets.DualListRoles_TD.new = true;
    }

    Partial.pageParams.groupId = undefined;
    Partial.Variables.getGroup.invoke();
};

App.addTeams = function() {
    App.Variables.errorMsg.dataSet.dataValue = null;
    App.Variables.successMessage.dataSet.dataValue = null;
    Partial.Variables.teamData.dataSet = [];

    Partial.Variables.leftRolesList.dataSet = [];
    Partial.Variables.leftUserList.dataSet = [];
    Partial.Variables.selectedRoles.dataSet = [];
    Partial.Variables.selectedUsers.dataSet = [];
    /*Partial.Variables.getAllRole.invoke(); */
    Partial.Variables.getAllUsers.invoke();

    if (Partial.Widgets.DualListUsers_TD !== undefined) {
        Partial.Widgets.DualListUsers_TD.rightdataset = [];
        Partial.Widgets.DualListUsers_TD.leftdataset = [];
        // Partial.Widgets.DualListUsers_TD.new = true;
    }
    if (Partial.Widgets.DualListRoles_TD !== undefined) {
        Partial.Widgets.DualListRoles_TD.rightdataset = [];
        Partial.Widgets.DualListRoles_TD.leftdataset = [];
        // Partial.Widgets.DualListRoles_TD.new = true;
    }

    Partial.pageParams.groupId = undefined;
    //Partial.Variables.getGroup.invoke();
};

Partial.DuplicatebuttonClick = function($event, widget) {

    Partial.isDeleteGroup = false;
    Partial.groupExists = false;
    // Partial.Widgets.GroupNameText.required = false;
    App.Variables.errorMsg.dataSet.dataValue = null;
    App.Variables.successMessage.dataSet.dataValue = null;
    Partial.newGroupName = "";

    if (Partial.Variables.groupData.dataSet !== null) {

        //The group name should be unique - currently appended _Copy at name end.
        Partial.newGroupName = Partial.Variables.groupData.dataSet.GroupName + "_Copy";

        Partial.Variables.getAllGroups.dataSet.forEach(function(group) {
            if (group.name.toLowerCase() == Partial.newGroupName.toLowerCase()) {
                Partial.groupExists = true;
            }
        });

        if (Partial.groupExists !== true) {
            Partial.Variables.createGroup.setInput({
                'name': Partial.newGroupName,
                'description': Partial.Variables.groupData.dataSet.GroupDescription,
                'lendingLimit': Partial.Variables.groupData.dataSet.GroupLendingLimit,
                'active': true,
                'createdBy': App.Variables.getLoggedInUserId.dataSet[0].id,
                'createdOn': new Date(),
                'updatedBy': App.Variables.getLoggedInUserId.dataSet[0].id,
                'updatedOn': new Date()
            });
            Partial.Variables.createGroup.invoke();

        } else {
            App.Variables.errorMsg.dataSet.dataValue = Partial.appLocale.GROUP_ALREADY_EXIST;
            Partial.scrollToTop();
        }
    }
}


Partial.deleteGroupConfirmOkClick = function($event, widget) {

    Partial.isDeleteGroup = true;

    Partial.Widgets.deleteGroupDialog.close();
    Partial.Variables.executeDeleteQueueGroup.setInput({
        'GroupId': Partial.pageParams.groupId,
        'QueueId': 0
    });
    Partial.Variables.executeDeleteQueueGroup.invoke();
};

Partial.executeDeleteQueueGrouponSuccess = function(variable, data) {
    Partial.Variables.executeDeleteGroupRole.setInput({
        'GroupId': Partial.pageParams.groupId
    });
    Partial.Variables.executeDeleteGroupRole.invoke();
};


App.fileGroupsUpload = function(data) {
    App.Variables.errorMsg.dataSet.dataValue = null;
    App.Variables.successMessage.dataSet.dataValue = null;

    Partial.Variables.uploadGroup.dataSet = [];
    Partial.Variables.uploadGroupItem.dataSet = [];
    Partial.Variables.uploadGroup.dataSet = data;
    data.forEach(function(group) {
        Partial.Variables.uploadGroupItem.dataSet = [];
        Partial.Variables.uploadGroupItem.dataSet = group;
        Partial.Variables.createGroup.setInput({
            'name': group.name,
            'description': group.description,
            'active': true,
            'createdBy': App.Variables.getLoggedInUserId.dataSet[0].id,
            'createdOn': new Date(),
            'updatedBy': App.Variables.getLoggedInUserId.dataSet[0].id,
            'updatedOn': new Date()
        });

        if (group.name !== "") {
            //Partial.Variables.createGroup.invoke();
            Partial.Variables.createGroup.invoke({}, function(cr) {

                //Partial.Variables.successMessage.dataSet.dataValue = Partial.appLocale.GROUP_CREATED_SUCCESSFULLY;
                if (group.roles.length > 0) {
                    group.roles.forEach(function(role) {
                        Partial.Variables.CreateGroupRole.setInput({
                            'groupId': cr.id,
                            'roleId': role.id
                        });
                        Partial.Variables.CreateGroupRole.invoke();

                    });
                }

                if (group.users.length > 0) {
                    group.users.forEach(function(usr) {
                        Partial.Variables.CreateGroupUser.setInput({
                            'groupId': cr.id,
                            'userId': usr.id
                        });
                        Partial.Variables.CreateGroupUser.invoke();

                    });
                }
            });

        }
    });


};

Partial.getAllUsersonError = function(variable, data) {

};
Partial.getAllUsersonSuccess = function(variable, data) {
    //
    /* if (Partial.Widgets.DualListUsers_TD !== undefined) {
         Partial.Widgets.DualListUsers_TD.leftdataset = [];
     }*/
    Partial.Variables.leftUserList.dataSet = data;
    //    debugger;
    data.forEach(function(u) {
        u.fullName = App.htmlEncode(u.userId);
    });
    if (Partial.pageParams.groupId == undefined) {
        Partial.Variables.leftUserList.dataSet = data;
        setTimeout(function() {
            if (Partial.Widgets.DualListUsers_TD !== undefined) {
                Partial.Widgets.DualListUsers_TD.leftdataset = data;
            }
        }, 100);
    }

    /*if (Partial.pageParams.groupId == undefined) {
        Partial.Variables.getGroupRole.invoke();
        Partial.Variables.getGroupUser.invoke();
    }*/

};

Partial.getAllRoleonError = function(variable, data) {

};
Partial.getAllRoleonSuccess = function(variable, data) {
    //
    /*if (Partial.Widgets.DualListRoles_TD !== undefined) {
        Partial.Widgets.DualListRoles_TD.leftdataset = [];
    }*/
    //Partial.Variables.leftUserList.dataSet = data;
    data.forEach(function(r) {
        r.fullRoleName = App.htmlEncode(r.role);
    });
    if (Partial.pageParams.groupId == undefined) {
        Partial.Variables.leftRolesList.dataSet = data;
        setTimeout(function() {
            if (Partial.Widgets.DualListRoles_TD !== undefined) {
                Partial.Widgets.DualListRoles_TD.leftdataset = data;
            }
        }, 100);
    }
    /* if (Partial.pageParams.groupId == undefined) {
         Partial.Variables.getGroupRole.invoke();
         Partial.Variables.getGroupUser.invoke();
     }*/

};

Partial.getAllGroupsonError = function(variable, data) {

};
Partial.getAllGroupsonSuccess = function(variable, data) {

};

Partial.updateGrouponSuccess = function(variable, data) {

    //    debugger;
    Partial.Variables.executeDeleteGroupRole.setInput({
        'GroupId': Partial.pageParams.groupId
    });
    Partial.Variables.executeDeleteGroupRole.invoke();


    /*Partial.Variables.deleteUserGroup.setInput({
        'id': data.id
    });
    Partial.Variables.deleteUserGroup.invoke();*/

};

Partial.createGrouponSuccess = function(variable, data) {
    App.Variables.errorMsg.dataSet.dataValue = null;
    App.Variables.successMessage.dataSet.dataValue = null;

    if (!Partial.Variables.uploadGroup.dataSet.length > 0) {
        Partial.Variables.successMessage.dataSet.dataValue = Partial.appLocale.GROUP_CREATED_SUCCESSFULLY;
        Partial.scrollToTop();
        Partial.Widgets.DualListRoles_TD.rightdataset.forEach(function(role) {
            Partial.Variables.CreateGroupRole.setInput({
                'groupId': data.id,
                'roleId': role.id
            });
            Partial.Variables.CreateGroupRole.invoke();
        })

        Partial.Widgets.DualListUsers_TD.rightdataset.forEach(function(user) {
            Partial.Variables.CreateGroupUser.setInput({
                'groupId': data.id,
                'userId': user.id
            });
            Partial.Variables.CreateGroupUser.invoke();
        })

        App.Variables.GroupPageCommunication.currentGroupInFocusId = data.id;

    }

    App.refreshAllGroups();
};

Partial.CreateGroupRoleonSuccess = function(variable, data) {


};


Partial.CreateGroupUseronSuccess = function(variable, data) {

    if (Partial.Variables.uploadGroup.dataSet.length > 0) {
        App.Variables.errorMsg.dataSet.dataValue = null;
        App.Variables.successMessage.dataSet.dataValue = null;
        App.Variables.groupErrorMessage.dataSet.dataValue = "";
        App.Variables.groupSuccessMessage.dataSet.dataValue = "";

        Partial.Variables.groupSuccessMessage.dataSet.dataValue = Partial.appLocale.GROUPS_IMPORTED_SUCCESSFULLY;
    } else {
        App.Variables.successMessage.dataSet.dataValue = Partial.appLocale.GROUP_CREATED_SUCCESSFULLY;
        Partial.scrollToTop();
    }

    //removing the cached app level permissions, so that they are loaded again on navigation
    cache_utils.removeFromCache("SessionStorage", "Permissions", "APP_PERMISSIONS", App.Variables.PermissionsForLoggedInUserId);

    //App.refreshAllGroups();

};

Partial.getGrouponSuccess = function(variable, data) {
    if (!Partial.pageParams.groupId)
        data = [];

    Partial.Variables.selectedRoles.dataSet = [];
    Partial.Variables.leftRolesList.dataSet = [];
    Partial.Variables.selectedUsers.dataSet = [];
    Partial.Variables.leftUserList.dataSet = [];

    if (data.length > 0) {
        Partial.Variables.groupData.dataSet = {
            "GroupName": data[0].name,
            "GroupDescription": data[0].description != null ? data[0].description.replaceAll('&amp;', '&') : data[0].description,
            "GroupLendingLimit": data[0].lendingLimit,
        }
    }

    Partial.Variables.getGroupRole.invoke();
    Partial.Variables.getGroupUser.invoke();
};


Partial.getGroupRoleonSuccess = function(variable, data) {
    // if (Partial.pageParams.groupId !== undefined) {
    Partial.GroupAssignRole = [];

    if (!Partial.pageParams.groupId) {
        data = [];
        Partial.GroupAssignRole = [];
    }
    data.forEach((d) => {
        Partial.GroupAssignRole.push(d.role)
    });
    Partial.Variables.groupData.dataSet = {
        "GroupAssignRole": Partial.GroupAssignRole,
        "GroupName": Partial.Variables.groupData.dataSet.GroupName,
        "GroupDescription": Partial.Variables.groupData.dataSet.GroupDescription,
        "GroupLendingLimit": Partial.Variables.groupData.dataSet.GroupLendingLimit
    }
    // if (Partial.canceledClicked == true) {} else {
    Partial.GroupAssignRole.forEach(function(sp) {
        sp.fullRoleName = App.htmlEncode(sp.role);
        Partial.Variables.selectedRoles.dataSet.push(sp);
    });
    if (Partial.Widgets.DualListRoles_TD !== undefined) {
        Partial.Widgets.DualListRoles_TD.rightdataset = Partial.Variables.selectedRoles.dataSet;
    }
    //   }

    Partial.Variables.getAllRole.dataSet.forEach(function(availableleftRoles) {
        let count = 0;
        data.forEach(function(i) {
            if (availableleftRoles.id == i.roleId) {
                count = count + 1;
            }
        });

        if (count == 0) {
            Partial.Variables.leftRolesList.dataSet.push(availableleftRoles);
        }
    });

    Partial.Variables.leftRolesList.dataSet.forEach(function(p) {
        p.fullRoleName = App.htmlEncode(p.role);

    });

    if (Partial.Widgets.DualListRoles_TD !== undefined) {
        Partial.Widgets.DualListRoles_TD.leftdataset = Partial.Variables.leftRolesList.dataSet;
    }
    // }
};

Partial.getGroupUseronSuccess = function(variable, data) {
    debugger
    //if (Partial.pageParams.groupId !== undefined) {
    Partial.GroupAssignUser = [];

    if (!Partial.pageParams.groupId) {
        data = [];
        Partial.GroupAssignUser = [];
    }
    data.forEach((d) => {
        Partial.GroupAssignUser.push(d.userByUserId);
    });

    Partial.Variables.groupData.dataSet = {
        "GroupAssignRole": Partial.GroupAssignUser,
        "GroupName": Partial.Variables.groupData.dataSet.GroupName,
        "GroupDescription": Partial.Variables.groupData.dataSet.GroupDescription,
        "GroupLendingLimit": Partial.Variables.groupData.dataSet.GroupLendingLimit

    }
    //if (Partial.canceledClicked == true) {} else {
    Partial.GroupAssignUser.forEach(function(sp) {
        sp.fullName = App.htmlEncode(sp.userId);
        Partial.Variables.selectedUsers.dataSet.push(sp);
    });
    if (Partial.Widgets.DualListUsers_TD !== undefined) {
        Partial.Widgets.DualListUsers_TD.rightdataset = Partial.Variables.selectedUsers.dataSet;
    }
    // }

    Partial.Variables.getAllUsers.dataSet.forEach((availableleftUser) => {
        let count = 0;
        data.forEach(function(i) {

            if (availableleftUser.id == i.userId) {
                count = count + 1;
            }
        });
        if (count == 0) {
            Partial.Variables.leftUserList.dataSet.push(availableleftUser);
        }
    });

    Partial.Variables.leftUserList.dataSet.forEach(function(p) {
        p.fullName = App.htmlEncode(p.userId);

    });

    if (Partial.Widgets.DualListUsers_TD !== undefined) {
        Partial.Widgets.DualListUsers_TD.leftdataset = Partial.Variables.leftUserList.dataSet;
    }
    // }

};

Partial.deleteGrouponSuccess = function(variable, data) {
    App.Variables.errorMsg.dataSet.dataValue = null;
    App.Variables.successMessage.dataSet.dataValue = null;
    App.Variables.successMessage.dataSet.dataValue = Partial.appLocale.GROUP_DELETED_SUCCESSFULLY;

    //removing the cached app level permissions, so that they are loaded again on navigation
    cache_utils.removeFromCache("SessionStorage", "Permissions", "APP_PERMISSIONS", App.Variables.PermissionsForLoggedInUserId);

    App.refreshAllGroups();
};

Partial.deleteGroupRoleonSuccess = function(variable, data) {};


Partial.deleteUserGrouponSuccess = function(variable, data) {


};

Partial.createGrouponError = function(variable, data, xhrobject) {
    App.Variables.errorMsg.dataSet.dataValue = null;
    App.Variables.successMessage.dataSet.dataValue = null;

    if (Partial.Variables.uploadGroup.dataSet.length > 0) {
        App.Variables.groupErrorMessage.dataSet.dataValue = "";
        App.Variables.groupSuccessMessage.dataSet.dataValue = "";
        App.Variables.groupErrorMessage.dataSet.dataValue = Partial.appLocale.INVALID_JSON;

    } else {

        //Partial.Variables.errorMessage.dataSet.dataValue = xhrobject.error.error.desc + "." + " " + "Please check input data.";
        App.Variables.errorMsg.dataSet.dataValue = Partial.appLocale.INTERNAL_ERROR_MSG;
        Partial.scrollToTop();
    }
};

Partial.updateGrouponError = function(variable, data, xhrobject) {
    App.Variables.errorMsg.dataSet.dataValue = null;
    App.Variables.successMessage.dataSet.dataValue = null;

    //Partial.Variables.errorMessage.dataSet.dataValue = xhrobject.error.error.desc + "." + " " + "Please check input data.";
    App.Variables.errorMsg.dataSet.dataValue = Partial.appLocale.INTERNAL_ERROR_MSG;
    Partial.scrollToTop();
};

Partial.executeDeleteGroupRoleonSuccess = function(variable, data) {

    Partial.Variables.executeDeleteGroupUser.setInput({
        'GroupId': Partial.pageParams.groupId

    });
    Partial.Variables.executeDeleteGroupUser.invoke();
};

Partial.executeDeleteGroupUseronSuccess = function(variable, data) {

    App.Variables.errorMsg.dataSet.dataValue = null;
    App.Variables.successMessage.dataSet.dataValue = null;
    if (Partial.isDeleteGroup !== true) {
        App.Variables.successMessage.dataSet.dataValue = Partial.appLocale.GROUP_UPDATED_SUCCESSFULLY;
        /*Partial.Widgets.DualListRoles_TD.rightdataset.forEach(function(role) {
            Partial.Variables.CreateGroupRole.setInput({
                'groupId': Partial.pageParams.groupId,
                'roleId': role.id
            });
            Partial.Variables.CreateGroupRole.invoke();
        });

        Partial.Widgets.DualListUsers_TD.rightdataset.forEach(function(user) {
            Partial.Variables.CreateGroupUser.setInput({
                'groupId': Partial.pageParams.groupId,
                'userId': user.id
            });
            Partial.Variables.CreateGroupUser.invoke();
        });*/

        Partial.Widgets.DualListRoles_TD.rightdataset.forEach(function(role) {
            Partial.Variables.CreateGroupRole.setInput({
                'groupId': Partial.pageParams.groupId,
                'roleId': role.id
            });
            Partial.Variables.CreateGroupRole.invoke();
        })

        Partial.Widgets.DualListUsers_TD.rightdataset.forEach(function(user) {
            Partial.Variables.CreateGroupUser.setInput({
                'groupId': Partial.pageParams.groupId,
                'userId': user.id
            });
            Partial.Variables.CreateGroupUser.invoke();
        })

        // Updating the Group List
        App.refreshAllGroups();

    } else {
        Partial.Variables.deleteGroup.setInput({
            'id': Partial.pageParams.groupId
        });

        Partial.Variables.deleteGroup.invoke();
    }

};

Partial.CreateGroupRoleonError = function(variable, data) {
    App.Variables.errorMsg.dataSet.dataValue = null;
    App.Variables.successMessage.dataSet.dataValue = null;

    App.Variables.groupErrorMessage.dataSet.dataValue = Partial.appLocale.INVALID_JSON;

};

Partial.CreateGroupUseronError = function(variable, data) {
    App.Variables.errorMsg.dataSet.dataValue = null;
    App.Variables.successMessage.dataSet.dataValue = null;

    App.Variables.groupErrorMessage.dataSet.dataValue = Partial.appLocale.INVALID_JSON;
};


Partial.scrollToTop = function() {
    const element = document.getElementById("title");
    if (element != null) {
        element.scrollIntoView({
            behavior: "smooth",
            block: "end",
            inline: "nearest"
        });
    }
};

Partial.CreateRolePermissiononError = function(variable, data) {

};
Partial.CreateRolePermissiononSuccess = function(variable, data) {

};


Partial.createRoleonError = function(variable, data) {

};
Partial.createRoleonSuccess = function(variable, data) {

};


Partial.deleteRoleonSuccess = function(variable, data) {

};


Partial.getAllPermissiononError = function(variable, data) {

};
Partial.getAllPermissiononSuccess = function(variable, data) {

};


Partial.getRolePermissiononError = function(variable, data) {

};
Partial.getRolePermissiononSuccess = function(variable, data) {

};


Partial.updateRoleonSuccess = function(variable, data) {

};


Partial.deleteRolePermissiononError = function(variable, data) {

};
Partial.deleteRolePermissiononSuccess = function(variable, data) {

};


Partial.executeDeleteGroupRoleonError = function(variable, data) {

};


Partial.executeDeleteGroupRoleonBeforeDatasetReady = function(variable, data) {

};


Partial.executeDeleteGroupUseronError = function(variable, data) {

};


Partial.getRoleonSuccess = function(variable, data) {

};

Partial.getTeamonSuccess = function(variable, data) {


    if (!Partial.pageParams.groupId)
        data = [];

    Partial.Variables.selectedRoles.dataSet = [];
    Partial.Variables.leftRolesList.dataSet = [];
    Partial.Variables.selectedUsers.dataSet = [];
    Partial.Variables.leftUserList.dataSet = [];

    if (data.length > 0) {
        Partial.Variables.teamData.dataSet = {
            "TeamId": data[0].teamId,
            "TeamName": data[0].teamName,
            "TeamDescription": data[0].description != null ? data[0].description.replaceAll('&amp;', '&') : data[0].description

        }
    }

    //Partial.Variables.getGroupRole.invoke();
    //Partial.Variables.getGroupUser.invoke();

};

Partial.getTeamUseronSuccess = function(variable, data) {

};

Partial.createTeamonSuccess = function(variable, data) {
    //    debugger;
    App.Variables.errorMsg.dataSet.dataValue = null;
    App.Variables.successMessage.dataSet.dataValue = null;

    if (!Partial.Variables.uploadGroup.dataSet.length > 0) {
        Partial.Variables.successMessage.dataSet.dataValue = Partial.appLocale.TEAM_CREATED_SUCCESSFULLY;
        Partial.scrollToTop();


        Partial.Widgets.DualListUsers_TD.rightdataset.forEach(function(user) {
            Partial.Variables.CreateTeamUser.setInput({
                'teamId': data.id,
                'userId': user.id
            });
            Partial.Variables.CreateTeamUser.invoke();
        })

        App.Variables.GroupPageCommunication.currentGroupInFocusId = data.id;

    }

    //App.refreshAllGroups();

};

Partial.CreateTeamUseronSuccess = function(variable, data) {
    //    debugger;
    if (Partial.Variables.uploadGroup.dataSet.length > 0) {
        App.Variables.errorMsg.dataSet.dataValue = null;
        App.Variables.successMessage.dataSet.dataValue = null;
        App.Variables.groupErrorMessage.dataSet.dataValue = "";
        App.Variables.groupSuccessMessage.dataSet.dataValue = "";

        Partial.Variables.groupSuccessMessage.dataSet.dataValue = Partial.appLocale.TEAMS_IMPORTED_SUCCESSFULLY;
    } else {
        App.Variables.successMessage.dataSet.dataValue = Partial.appLocale.TEAM_CREATED_SUCCESSFULLY;
        Partial.scrollToTop();
    }

    //removing the cached app level permissions, so that they are loaded again on navigation
    // cache_utils.removeFromCache("SessionStorage", "Permissions", "APP_PERMISSIONS", App.Variables.PermissionsForLoggedInUserId);

    App.refreshAllTeams();

};

Partial.updateTeamonSuccess = function(variable, data) {

    Partial.Variables.executeDeleteTeamUser.setInput({
        'teamId': Partial.pageParams.groupId
    });
    Partial.Variables.executeDeleteTeamUser.invoke();

};

Partial.deleteTeamConfirmOkClick = function($event, widget) {

    Partial.isDeleteTeam = true;

    Partial.Widgets.deleteTeamDialog.close();
    Partial.Variables.executeDeleteTeamUser.setInput({
        'teamId': Partial.pageParams.groupId
    });
    Partial.Variables.executeDeleteTeamUser.invoke();


};

Partial.deleteTeamonSuccess = function(variable, data) {

    App.Variables.errorMsg.dataSet.dataValue = null;
    App.Variables.successMessage.dataSet.dataValue = null;
    App.Variables.successMessage.dataSet.dataValue = Partial.appLocale.TEAM_DELETED_SUCCESSFULLY;
    App.refreshAllTeams();

};

Partial.executeDeleteTeamUseronSuccess = function(variable, data) {

    App.Variables.errorMsg.dataSet.dataValue = null;
    App.Variables.successMessage.dataSet.dataValue = null;
    if (Partial.isDeleteGroup !== true) {
        App.Variables.successMessage.dataSet.dataValue = Partial.appLocale.TEAM_UPDATED_SUCCESSFULLY;
        /*Partial.Widgets.DualListRoles_TD.rightdataset.forEach(function(role) {
            Partial.Variables.CreateGroupRole.setInput({
                'groupId': Partial.pageParams.groupId,
                'roleId': role.id
            });
            Partial.Variables.CreateGroupRole.invoke();
        });

        Partial.Widgets.DualListUsers_TD.rightdataset.forEach(function(user) {
            Partial.Variables.CreateGroupUser.setInput({
                'groupId': Partial.pageParams.groupId,
                'userId': user.id
            });
            Partial.Variables.CreateGroupUser.invoke();
        });*/

        Partial.Widgets.DualListUsers_TD.rightdataset.forEach(function(user) {
            Partial.Variables.CreateTeamUser.setInput({
                'teamId': Partial.pageParams.groupId,
                'userId': user.id
            });
            Partial.Variables.CreateTeamUser.invoke();
        })


        // Updating the Group List
        App.refreshAllTeams();

    } else {

        Partial.Variables.deleteTeam.setInput({
            'id': Partial.pageParams.groupId
        });

        Partial.Variables.deleteTeam.invoke();
    }

};
Partial.EditTeamButtonClick = function($event, widget) {
    Partial.Variables.readOnlyMode.dataSet.dataValue = false;
};
Partial.DeleteButtonClick = function($event, widget) {
    Partial.Variables.readOnlyMode.dataSet.dataValue = false;
};
Partial.deleteTeamConfirmNoClick = function($event, widget) {
    Partial.Variables.readOnlyMode.dataSet.dataValue = true;

};