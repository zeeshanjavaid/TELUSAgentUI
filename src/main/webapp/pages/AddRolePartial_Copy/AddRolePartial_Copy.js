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

    // Partial.pageParams.roleId

    Partial.Variables.roleData.dataSet = [];
    Partial.Variables.uploadRole.dataSet = [];
    Partial.Variables.uploadRoleItem.dataSet = [];
    Partial.Variables.rolesErrorMsg.dataSet.dataValue = null;
    Partial.Variables.rolesSuccessMessage.dataSet.dataValue = null;
    Partial.canceledClicked = false;
    Partial.roleexists = false;
    Partial.currentRoleName = "";
    Partial.isDelete = false;
    if (Partial.pageParams.roleId === undefined) {
        Partial.Variables.readOnlyMode.dataSet.dataValue = false;
    }
    // else {
    //     // Partial.Variables.readOnlyMode.dataSet.dataValue = false;
    // }

};

function messageTimeout() {
    Partial.Variables.rolesSuccessMessage.dataSet.dataValue = null;

}

Partial.SaveButtonClick = function($event, widget) {

    Partial.isDelete = false;
    Partial.roleexists = false;
    Partial.Widgets.NameText.required = false;
    Partial.Variables.rolesErrorMsg.dataSet.dataValue = null;
    Partial.Variables.rolesSuccessMessage.dataSet.dataValue = null;

    let pattern = Partial.Widgets.NameText.regexp;
    if (Partial.Widgets.NameText.datavalue == undefined || Partial.Widgets.NameText.datavalue == "") {
        Partial.Variables.rolesErrorMsg.dataSet.dataValue = Partial.appLocale.ROLE_NAME_MANDATORY;
        Partial.Widgets.NameText.required = true;

    } else if (Partial.Widgets.NameText.datavalue.match(pattern) == null) {
        Partial.Variables.rolesErrorMsg.dataSet.dataValue = "Role name is invalid.";
    } else if (!Partial.Widgets.DualList_TD1.rightdataset.length > 0) {
        Partial.Variables.rolesErrorMsg.dataSet.dataValue = "Permission assignment is mandatory.";
    } else {

        if (Partial.pageParams.roleId) {

            Partial.Variables.getAllRolesPartial.dataSet.forEach(function(role) {
                if (role.role !== null) {

                    if (role.role.toLowerCase() == Partial.Widgets.NameText.datavalue.toLowerCase() && role.id != Partial.pageParams.roleId) {
                        Partial.roleexists = true;
                    }
                }
            });

        } else {

            Partial.Variables.getAllRolesPartial.dataSet.forEach(function(role) {
                if (role.role !== null) {
                    if (role.role.toLowerCase() == Partial.Widgets.NameText.datavalue.toLowerCase()) {
                        Partial.roleexists = true;
                    }
                }
            });
        }

        if (Partial.roleexists !== true) {
            if (Partial.pageParams.roleId && Partial.pageParams.roleId > 0) {
                Partial.Variables.updateRole.setInput({
                    'id': Partial.pageParams.roleId,
                    'role': Partial.Variables.roleData.dataSet.RoleName,
                    'description': Partial.Variables.roleData.dataSet.RoleDescription,
                    'active': true,
                    'createdBy': Partial.Variables.getRole.dataSet[0].createdBy,
                    'createdOn': Partial.Variables.getRole.dataSet[0].createdOn,
                    'updatedBy': App.Variables.getLoggedInUserId.dataSet[0].id,
                    'updatedOn': new Date()
                });
                App.Variables.RolePageCommunication.currentRoleInFocusId = Partial.pageParams.roleId;
                Partial.Variables.updateRole.update();
            } else {
                Partial.Variables.createRole.setInput({
                    'role': Partial.Variables.roleData.dataSet.RoleName,
                    'description': Partial.Variables.roleData.dataSet.RoleDescription,
                    'active': true,
                    'createdBy': App.Variables.getLoggedInUserId.dataSet[0].id,
                    'createdOn': new Date(),
                    'updatedBy': App.Variables.getLoggedInUserId.dataSet[0].id,
                    'updatedOn': new Date()
                });
                Partial.Variables.createRole.invoke();
                App.Variables.getAllRoles.invoke();
            }

        } else {
            Partial.Variables.rolesErrorMsg.dataSet.dataValue = Partial.appLocale.ROLE_ALREADY_EXIST;

        }
    }
};

Partial.CancelbuttonClick = function($event, widget) {
    // debugger;
    Partial.Variables.readOnlyMode.dataSet.dataValue = true;
    Partial.isDelete = false;
    Partial.Variables.rolesErrorMsg.dataSet.dataValue = null;
    Partial.Variables.rolesSuccessMessage.dataSet.dataValue = null;
    Partial.canceledClicked = true;
    Partial.Widgets.textarea1.setWidgetProperty('datavalue', Partial.Variables.roleDescription.dataValue);
    Partial.Widgets.textarea1.updatePrevDatavalue();

    if (Partial.pageParams.roleId) {
        if (Partial.Widgets.DualList_TD1 !== undefined) {
            Partial.Widgets.DualList_TD1.rightdataset = Partial.Variables.selectedpermissions.dataSet;
            Partial.Widgets.DualList_TD1.leftdataset = Partial.Variables.leftPermissionList.dataSet;
        }
    } else {
        Partial.Variables.roleData.dataSet = [];
        Partial.pageParams.roleId = undefined;

        if (Partial.Widgets.DualList_TD1 !== undefined) {
            Partial.Widgets.DualList_TD1.rightdataset = [];
            Partial.Widgets.DualList_TD1.leftdataset = Partial.Variables.leftPermissionList.dataSet;
        }
    }
};

App.addRoles = function() {

    Partial.Variables.rolesErrorMsg.dataSet.dataValue = null;
    Partial.Variables.rolesSuccessMessage.dataSet.dataValue = null;
    Partial.Variables.roleData.dataSet = [];
    Partial.Variables.leftPermissionList.dataSet = [];
    Partial.Variables.selectedpermissions.dataSet = [];

    if (Partial.Widgets.DualList_TD1 !== undefined) {
        Partial.Widgets.DualList_TD1.rightdataset = [];
        Partial.Widgets.DualList_TD1.leftdataset = [];
    }

    Partial.pageParams.roleId = undefined;
    Partial.Variables.getRole.invoke();

    Partial.Variables.readOnlyMode.dataSet.dataValue = false;

};

Partial.createRoleonError = function(variable, data, xhrobject) {
    // debugger;
    Partial.Variables.rolesErrorMsg.dataSet.dataValue = null;
    Partial.Variables.rolesSuccessMessage.dataSet.dataValue = null;

    Partial.Variables.rolesErrorMsg.dataSet.dataValue = Partial.appLocale.INTERNAL_ERROR_MSG;

};
Partial.createRoleonSuccess = function(variable, data) {
    // debugger;
    Partial.Variables.rolesErrorMsg.dataSet.dataValue = null;
    Partial.Variables.rolesSuccessMessage.dataSet.dataValue = null;

    Partial.Variables.rolesSuccessMessage.dataSet.dataValue = Partial.appLocale.ROLE_CREATED_SUCCESSFULLY;
    /* setTimeout(messageTimeout, 5000);*/
    Partial.Widgets.DualList_TD1.rightdataset.forEach(function(permission) {
        Partial.Variables.CreateRolePermission.setInput({
            'roleId': data.id,
            'permissionId': permission.id,
        });
        Partial.Variables.CreateRolePermission.invoke();
    })

};

Partial.getRoleonSuccess = function(variable, data) {
    if (!Partial.pageParams.roleId)
        data = [];

    Partial.Variables.selectedpermissions.dataSet = [];
    Partial.Variables.leftPermissionList.dataSet = [];

    if (data.length > 0) {
        Partial.Variables.roleData.dataSet = {
            "RoleName": data[0].role,
            "RoleDescription": data[0].description,
        };
        Partial.Variables.roleDescription.dataValue = data[0].description;
    }

    Partial.Variables.getRolePermission.invoke();
};

Partial.CreateRolePermissiononError = function(variable, data) {
    Partial.Variables.rolesErrorMsg.dataSet.dataValue = null;
    Partial.Variables.rolesSuccessMessage.dataSet.dataValue = null;

    App.Variables.roleErrorMessage.dataSet.dataValue = "";
    App.Variables.roleSuccessMessage.dataSet.dataValue = "";
    App.Variables.roleErrorMessage.dataSet.dataValue = Partial.appLocale.INVALID_JSON;
};

Partial.CreateRolePermissiononSuccess = function(variable, data) {

    //removing the cached app level permissions, so that they are loaded again on navigation
    cache_utils.removeFromCache("SessionStorage", "Permissions", "APP_PERMISSIONS", App.Variables.PermissionsForLoggedInUserId);
    //App.activePage.pageRefresh();
    Partial.Variables.readOnlyMode.dataSet.dataValue = true;

    App.refreshAllRoles();

};

Partial.updateRoleonSuccess = async function(variable, data) {

    Partial.Variables.deleteRolePermission.setInput({
        'RoleId': data.id
    });
    Partial.Variables.deleteRolePermission.invoke();
};

Partial.getRolePermissiononError = function(variable, data) {

};
Partial.getRolePermissiononSuccess = function(variable, data) {

    Partial.RoleAssignPermission = [];
    if (!Partial.pageParams.roleId) {
        data = [];
        Partial.RoleAssignPermission = [];
    }

    data.forEach(function(d) {
        Partial.RoleAssignPermission.push(d.permission);
    });

    Partial.Variables.roleData.dataSet = {
        "RoleAssignPermission": Partial.RoleAssignPermission,
        "RoleName": Partial.Variables.roleData.dataSet.RoleName,
        "RoleDescription": Partial.Variables.roleData.dataSet.RoleDescription
    }
    Partial.RoleAssignPermission.forEach(function(sp) {
        sp.fullPermissionName = App.htmlEncode(sp.description);
        Partial.Variables.selectedpermissions.dataSet.push(sp);
    });
    if (Partial.Widgets.DualList_TD1 !== undefined) {
        Partial.Widgets.DualList_TD1.rightdataset = Partial.Variables.selectedpermissions.dataSet;
    }
    Partial.Variables.getAllPermission.dataSet.forEach((availableleftPermission) => {
        let count = 0;
        data.forEach(function(i) {
            if (availableleftPermission.id == i.permissionId) {
                count = count + 1;
            }
        });

        if (count == 0) {
            Partial.Variables.leftPermissionList.dataSet.push(availableleftPermission);
        }
    });

    Partial.Variables.leftPermissionList.dataSet.forEach((p) => {
        p.fullPermissionName = App.htmlEncode(p.description);
    });

    if (Partial.Widgets.DualList_TD1 !== undefined) {
        Partial.Widgets.DualList_TD1.leftdataset = Partial.Variables.leftPermissionList.dataSet;
    }
};

Partial.deleteRolePermissiononError = function(variable, data) {

};
Partial.deleteRolePermissiononSuccess = function(variable, data) {

    Partial.Variables.rolesErrorMsg.dataSet.dataValue = null;
    Partial.Variables.rolesSuccessMessage.dataSet.dataValue = null;

    if (Partial.isDelete !== true) {
        Partial.Widgets.DualList_TD1.rightdataset.forEach(function(selectedPermission) {
            Partial.Variables.CreateRolePermission.setInput({
                'roleId': Partial.pageParams.roleId,
                'permissionId': selectedPermission.id,
            });
            Partial.Variables.CreateRolePermission.invoke();

        })
        Partial.Variables.rolesSuccessMessage.dataSet.dataValue = Partial.appLocale.ROLE_UPDATED_SUCCESSFULLY;
        setTimeout(messageTimeout, 3000);
    } else {

        Partial.Variables.deleteRole.setInput({
            'id': Partial.pageParams.roleId
        });
        Partial.Variables.deleteRole.invoke();
    }

    App.Variables.getAllRoles.invoke();

};

Partial.deleteRoleConfirmOkClick = function($event, widget) {

    Partial.isDelete = true;
    Partial.Widgets.deleteRoleDialog.close();
    //to delete permmission before deleting role
    Partial.Variables.deleteRolePermission.setInput({
        'RoleId': Partial.pageParams.roleId
    });
    Partial.Variables.deleteRolePermission.invoke();


    //to delete user before deleting role
    Partial.Variables.executeDeleteUserRole.setInput({
        'roleId': Partial.pageParams.roleId
    });
    Partial.Variables.executeDeleteUserRole.invoke();
    App.Variables.getAllRoles.invoke();
};

Partial.deleteRoleonSuccess = function(variable, data) {
    Partial.Variables.rolesErrorMsg.dataSet.dataValue = null;
    Partial.Variables.rolesSuccessMessage.dataSet.dataValue = null;

    Partial.Variables.rolesSuccessMessage.dataSet.dataValue = Partial.appLocale.ROLE_DELETED_SUCCESSFULLY;

    cache_utils.removeFromCache("SessionStorage", "Permissions", "APP_PERMISSIONS", App.Variables.PermissionsForLoggedInUserId);
    App.Variables.getAllRoles.invoke();
    App.refreshAllRoles();
};

Partial.updateRoleonError = function(variable, data, xhrobject) {
    Partial.Variables.rolesErrorMsg.dataSet.dataValue = null;
    Partial.Variables.rolesSuccessMessage.dataSet.dataValue = null;
    Partial.Variables.rolesErrorMsg.dataSet.dataValue = Partial.appLocale.INTERNAL_ERROR_MSG;
};

Partial.deleteRoleonError = function(variable, data) {

};

Partial.renderScreenReadOnly = function() {

    Partial.Variables.readOnlyMode.dataSet.dataValue = true;
    Partial.Widgets.EditRoleButton.show = true;
    Partial.Widgets.SaveButton.show = false;
    Partial.Widgets.CancelButton.show = false;
    Partial.Widgets.DeleteButton.show = false;
}
Partial.EditRoleButtonClick = function($event, widget) {

    Partial.Variables.rolesErrorMsg.dataSet.dataValue = "";
    Partial.Variables.rolesSuccessMessage.dataSet.dataValue = "";
    Partial.Variables.readOnlyMode.dataSet.dataValue = false;

};
Partial.DeleteButtonClick = function($event, widget) {

};

Partial.getAllPermissiononSuccess = function(variable, data) {
    /*

        Partial.Variables.getAllPermission.dataSet = data;
        Partial.Variables.leftPermissionList.dataSet = Partial.Variables.getAllPermission.dataSet;
        Partial.Variables.getAllPermission.dataSet.forEach(function(p) {
            p.fullPermissionName = App.htmlEncode(p.description);
        });
    */
};