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
};

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
    Partial.Variables.roleId.dataSet.dataValue = item.roleId;
};

Partial.AddRolesButtonClick = function($event, widget) {
    // debugger
    App.addRoles();
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

    Partial.Variables.getRolesAssociatedUsersPermissions.invoke();
}

Partial.roleSearchTextKeyup = function($event, widget) {

    if (widget.datavalue) {
        let results = Partial.allRolesUITemp.filter((item) => {
            return (item.role.toLowerCase().includes(widget.datavalue.toLowerCase()) || (item.description && item.description.toLowerCase().includes(widget.datavalue.toLowerCase())));
        });

        Partial.Variables.getRolesAssociatedUsersPermissions.dataSet = [];
        Object.assign(Partial.Variables.getRolesAssociatedUsersPermissions.dataSet, results);

    } else {
        Partial.Variables.getRolesAssociatedUsersPermissions.dataSet = [];
        Object.assign(Partial.Variables.getRolesAssociatedUsersPermissions.dataSet, Partial.allRolesUITemp);
    }
};

Partial.RoleListRender = function(widget, $data) {

    if (App.Variables.RolePageCommunication.currentRoleInFocusId) {

        let selectedItem = App.getRoleIndex(App.Variables.RolePageCommunication.currentRoleInFocusId);
        let pageSize = App.Variables.RolePageCommunication.currentPageSize;
        console.log("Selected item index:" + selectedItem + " pageSize :" + pageSize);
        App.navigateToPageNo(selectedItem, pageSize);
        App.clickListItemByIndex('RoleList', (selectedItem) % pageSize);
    }
};
Partial.RoleListSetrecord = function(widget, $data) {

    App.Variables.RolePageCommunication.currentPageSize = widget.pagesize;

};

Partial.RoleListClick = function(widget, $data) {

    App.Variables.RolePageCommunication.currentPageSize = widget.pagesize;
};

Partial.getRolesAssociatedUsersPermissionsonSuccess = function(variable, data) {
    if (App.Variables.RolePageCommunication.currentRoleInFocusId) {

        let selectedItem = App.getRoleIndex(App.Variables.RolePageCommunication.currentRoleInFocusId);
        let pageSize = App.Variables.RolePageCommunication.currentPageSize;
        console.log("Selected item index:" + selectedItem + " pageSize :" + pageSize);
        App.navigateToPageNo(selectedItem, pageSize);
        App.clickListItemByIndex('RoleList', (selectedItem) % pageSize);
    }
};