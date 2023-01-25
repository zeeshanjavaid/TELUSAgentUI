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
    console.log("Ready form queue partial is called");
    Partial.Variables.roleErrorMessage.dataSet.dataValue = "";
    Partial.Variables.roleSuccessMessage.dataSet.dataValue = "";
    Partial.allRoles = [];
    Partial.role = [];
    //Partial.Variables.getAllRoles.invoke();
};
Partial.container5Click = function($event, widget, item, currentItemWidgets) {
    Partial.Variables.errorMessage.dataSet.dataValue = "";
    Partial.Variables.successMessage.dataSet.dataValue = "";
    Partial.Variables.queueId.dataSet.dataValue = item.id;
    //App.getRolePermission(item.id);
};
Partial.AddQueueButtonClick = function($event, widget) {
    App.addQueue();
};

App.refreshAllQueues = function() {
    Partial.Variables.allQueues.invoke();
    Partial.App.activePage.Widgets.AddQueueContainer.Variables.getQueue.invoke();
    debugger;
    if (App.Variables.QueuePageCommunication.currentQueueInFocusId) {

        let selectedItem = App.getQueueIndex(App.Variables.QueuePageCommunication.currentQueueInFocusId);
        console.log("Selected item index:" + selectedItem);
        Partial.Widgets.QueueList.selectItem(selectedItem);
        // let selectItem = Partial.Widgets.QueueList.firstSelectedItem;
        // Partial.Widgets.QueueList.firstSelectedItem.itemClass = selectItem.itemClass + " active";
        // console.log("Selected Item class list:" + Partial.Widgets.QueueList.firstSelectedItem.itemClass);
    }
}

Partial.searchRoleBlur = function($event, widget) {};
Partial.queueSearchTextKeyup = function($event, widget) {

    if (widget.datavalue) {
        let results = Partial.Variables.queuesCopy.dataSet.filter((item) => {
            return item.name.toLowerCase().includes(widget.datavalue.toLowerCase());
        });

        Partial.Variables.allQueuesUI.dataSet = [];
        Object.assign(Partial.Variables.allQueuesUI.dataSet, results);
    } else {
        Partial.Variables.allQueuesUI.dataSet = [];
        Object.assign(Partial.Variables.allQueuesUI.dataSet, Partial.Variables.queuesCopy.dataSet);
    }

};

Partial.allQueuesonSuccess = function(variable, data) {
    console.log("allQueuesonSuccess is called");
    Partial.Variables.allQueuesUI.dataSet = [];
    Partial.Variables.queuesCopy.dataSet = [];

    Object.assign(Partial.Variables.allQueuesUI.dataSet, data);
    Object.assign(Partial.Variables.queuesCopy.dataSet, data);
};

App.getQueueIndex = function(queueId) {
    let QIndex = 0;

    if (queueId && Partial.Widgets.QueueList.dataset) {
        QIndex = Partial.Widgets.QueueList.dataset.findIndex((QData) => {
            return QData.id === queueId;
        });
    }

    Partial.selectedQIndex = QIndex;
    return QIndex;
};

Partial.getAllRolesPartialonSuccess = function(variable, data) {
    //
    //Partial.Variables.allRolesUI.dataSet = data;
    //Partial.Variables.rolesCopy.dataSet = data;
    Partial.roleUI = [];
    Partial.Variables.allRolesUI.dataSet = [];
    Partial.allRolesUITemp = [];
    Partial.Variables.rolesCopy.dataSet = [];
    Partial.Variables.roleId.dataSet.dataValue = "";
    data.forEach(function(r) {
        Partial.Variables.roleId.dataSet.dataValue = r.id;
        Partial.rolePermission = [];
        Partial.roleUI = [];



        Partial.Variables.RolePermission.invoke({}, function(rm) {
            // Partial.rolePermission = [];
            // Partial.rolePermissionFinal = [];
            Partial.rolePermission = rm.filter(function(g) {
                //return g;
                //return g.roleId == Partial.Variables.roleId.dataSet.dataValue;
                return g.roleId == r.id;
                //if (Partial.Variables.roleId.dataSet.dataValue == g.roleId)
                //     Partial.rolePermissionFinal.push(g.permission);

            });

            //
            if (Partial.rolePermission && Partial.rolePermission.length > 0) {
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
                    "PermissionCount": Partial.rolePermission.length,
                    //"GroupCount": 0
                }





                if (!Array.isArray(Partial.roleUI)) {

                    var d = Partial.allRolesUITemp.find(element => element.role === Partial.roleUI.role);
                    if (!d) {
                        Partial.allRolesUITemp.push(Partial.roleUI);
                        // if (Partial.allRolesUITemp.length == data.length) {

                        addGroupCount(Partial.allRolesUITemp);
                        // }
                    }

                }
            } else {
                Partial.roleUI = {
                    "createdBy": r.createdBy,
                    "createdOn": r.createdOn,
                    "description": r.description,
                    "id": r.id,
                    "isActive": r.isActive,
                    "role": r.role,
                    "updatedBy": r.updatedBy,
                    "updatedOn": r.updatedOn,
                    "PermissionCount": 0
                }

                var d = Partial.allRolesUITemp.find(element => element.role === Partial.roleUI.role);
                if (!d) {
                    Partial.allRolesUITemp.push(Partial.roleUI);
                }
            }


            // });



            //Partial.Variables.allRolesUI.dataSet.push(Partial.roleUI);
            //

        });
        // setTimeout(function() {

        //Partial.Variables.rolesCopy.dataSet = Partial.Variables.allRolesUI.dataSet;
        //Partial.Variables.rolesCopy.dataSet = Partial.allRolesUITemp;
        //}, 500);
        //}, 500);
        //console.log(Partial.Variables.allRolesUI.dataSet)
    });
    //console.log(Partial.Variables.allRolesUI.dataSet)
    //if (Partial.Variables.getAllRoles.dataSet.length == Partial.allRolesUITemp.length) {
    /*Partial.Variables.allRolesUI.dataSet = Partial.allRolesUITemp;
    Partial.Variables.rolesCopy.dataSet = Partial.allRolesUITemp;*/
    //console.log(Partial.Variables.allRolesUI.dataSet);



};



App.queuesPageReload = function() {

    Partial.Variables.allQueues.invoke();

}