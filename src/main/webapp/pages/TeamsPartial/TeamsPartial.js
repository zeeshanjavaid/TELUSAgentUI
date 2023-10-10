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
    //Partial.Variables.getAllTeams.invoke();
    Partial.allRoles = [];
    Partial.role = [];
    Partial.isImportExport = false;
};


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


    /*if (App.Variables.GroupPageCommunication.currentGroupInFocusId) {

        let selectedItem = App.getGroupIndex(App.Variables.GroupPageCommunication.currentGroupInFocusId);
        let pageSize = App.Variables.GroupPageCommunication.currentPageSize;
        console.log("Selected item index:" + selectedItem + " pageSize :" + pageSize);
        App.navigateToPageNo(selectedItem, pageSize);
        App.clickListItemByIndex('GroupList', (selectedItem) % pageSize);

    }*/


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

}


Partial.containerGroupsClick = function($event, widget, item, currentItemWidgets) {};

App.refreshAllGroups = function() {

    Partial.Variables.getAllGroups.invoke();
    Partial.Variables.allTeamsUI.invoke();

}

App.refreshAllTeams = function() {

    Partial.Variables.getAllTeams.invoke();
}

App.groupsPageReload = function() {

    Partial.Variables.getAllGroups.invoke();
}

Partial.RolePermissiononError = function(variable, data) {

};

Partial.GroupListClick = function(widget, $data) {

    App.Variables.GroupPageCommunication.currentPageSize = widget.pagesize;
    console.log("Group List clicked !! " + App.Variables.GroupPageCommunication.currentPageSize);

};
Partial.GroupListSetrecord = function(widget, $data) {

    App.Variables.GroupPageCommunication.currentPageSize = widget.pagesize;

};

Partial.getAllTeamsonSuccess = async function(variable, data) {
    Partial.allTeamUITemp = [];
    Partial.Variables.allTeamsUI.dataSet = [];
    Partial.Variables.teamCopy.dataSet = [];
    for (let r of data) {
        //Partial.Variables.roleId.dataSet.dataValue = r.id;

        Partial.TeamUI = {
            "createdBy": r.createdBy,
            "createdOn": r.createdOn,
            "description": r.description,
            "id": r.id,
            "teamId": r.teamId,
            "name": r.teamName,
            "updatedBy": r.updatedBy,
            "updatedOn": r.updatedOn,
            "userCount": 0
        };

        Partial.Variables.getTeamUser.filterExpressions.rules[0].value = r.id;
        await Partial.Variables.getTeamUser.invoke();
        Partial.TeamUI.userCount = (Partial.Variables.getTeamUser.dataSet && Partial.Variables.getTeamUser.dataSet.length > 0) ? Partial.Variables.getTeamUser.dataSet.length : 0;

        let d = Partial.allTeamUITemp.find((element) => {
            return element.name === Partial.TeamUI.name
        });


        if (!d) {
            Partial.allTeamUITemp.push(Partial.TeamUI);
            Partial.Variables.allTeamsUI.dataSet.push(Partial.TeamUI);
            Partial.Variables.teamCopy.dataSet.push(Partial.TeamUI);
        }
    }

    if (App.Variables.GroupPageCommunication.currentGroupInFocusId) {

        let selectedItem = App.getGroupIndex(App.Variables.GroupPageCommunication.currentGroupInFocusId);
        let pageSize = App.Variables.GroupPageCommunication.currentPageSize;
        console.log("Selected item index:" + selectedItem + " pageSize :" + pageSize);
        App.navigateToPageNo(selectedItem, pageSize);
        App.clickListItemByIndex('GroupList', (selectedItem) % pageSize);

    }


};
Partial.teamSearchTextKeyup = function($event, widget) {

    debugger;

    if (widget.datavalue) {
        let results = Partial.allTeamUITemp.filter((item) => {
            return item.teamId.toLowerCase().includes(widget.datavalue.toLowerCase());
        });

        Partial.Variables.allTeamsUI.dataSet = [];
        Object.assign(Partial.Variables.allTeamsUI.dataSet, results);
    } else {
        Partial.Variables.allTeamsUI.dataSet = [];
        Object.assign(Partial.Variables.allTeamsUI.dataSet, Partial.allTeamUITemp);
    }

};
Partial.AddTeamButtonClick = function($event, widget) {
    App.addTeams();
};