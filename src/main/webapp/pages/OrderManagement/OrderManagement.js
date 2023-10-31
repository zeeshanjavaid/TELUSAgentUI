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
    //  Partial.Variables.UserLoggedInVar.dataSet.dataValue = App.Variables.getLoggedInUserDetails.dataSet.emplId;


    // Partial.Variables.getLoggedInUserTeamIdVar.setInput({
    //     'userId': App.Variables.getLoggedInUserId.dataSet[0].id
    // });
    // Partial.Variables.getLoggedInUserTeamIdVar.invoke();




    Partial.Variables.getCollectionTreatmentByCollENtityId.setInput({
        'collectionEntityId': Partial.pageParams.entityId,
    });

    Partial.Variables.getCollectionTreatmentByCollENtityId.invoke();

    App.Variables.errorMsg.dataSet.dataValue = '';
    Partial.Variables.popUperrorMsg.dataSet.dataValue = '';


};

Partial.getCollectionTreatmentStep_orderMngt_OnRowexpand = function($event, widget, row, $data) {
    debugger;
    App.showRowExpansionOrderManagement(row, $data);
};

function getCurrentDate() {
    var currentDate = new Date().toJSON().slice(0, 10);
    return currentDate;
}

Partial.CreateSuspentionRequestClick = function($event, widget) {

    debugger;
    // Partial.Variables.getCollectionTreatMent.setInput({
    //     'collectionEntityId': Partial.pageParams.entityId


    // });

    // Partial.Variables.getCollectionTreatMent.invoke();
    if (Partial.Variables.getCollectionTreatmentByCollENtityId.dataSet.length == 0) {
        App.Variables.errorMsg.dataSet.dataValue = "You cannot create an action for this entity. Entity is not yet in collection treatment.";
        setTimeout(messageTimeout, 5000);
    } else {
        Partial.Widgets.OrderPopOver.hidePopover();
        Partial.Variables.errorMsg.dataSet.dataValue = "";
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "";

        Partial.Variables.UserLoggedInVar.dataSet.dataValue = App.Variables.getLoggedInUserDetails.dataSet.emplId;
        Partial.Variables.getLoggedInUserTeamIdVar.setInput({
            'userId': App.Variables.getLoggedInUserId.dataSet[0].id
        });
        Partial.Variables.getLoggedInUserTeamIdVar.invoke();

        Partial.Widgets.CreateSuspensionRequestdialog.open();
    }


};


Partial.CreateRestoralRequestClick = function($event, widget) {
    if (Partial.Variables.getCollectionTreatmentByCollENtityId.dataSet.length == 0) {
        App.Variables.errorMsg.dataSet.dataValue = "You cannot create an action for this entity. Entity is not yet in collection treatment.";
        setTimeout(messageTimeout, 5000);
    } else {
        Partial.Widgets.OrderPopOver.hidePopover();
        getBanDetails();
        Partial.Variables.errorMsg.dataSet.dataValue = "";
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "";
        Partial.Variables.UserLoggedInVar.dataSet.dataValue = App.Variables.getLoggedInUserDetails.dataSet.emplId;
        Partial.Variables.getLoggedInUserTeamIdVar.setInput({
            'userId': App.Variables.getLoggedInUserId.dataSet[0].id
        });
        Partial.Variables.getLoggedInUserTeamIdVar.invoke();


        Partial.Widgets.CreateRestoralRequestdialog.open();
    }
};
Partial.CreateCeaseRequestClick = function($event, widget) {
    if (Partial.Variables.getCollectionTreatmentByCollENtityId.dataSet.length == 0) {
        App.Variables.errorMsg.dataSet.dataValue = "You cannot create an action for this entity. Entity is not yet in collection treatment.";
        setTimeout(messageTimeout, 5000);
    } else {
        Partial.Widgets.OrderPopOver.hidePopover();
        getBanDetails();
        Partial.Variables.errorMsg.dataSet.dataValue = "";
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "";
        Partial.Variables.UserLoggedInVar.dataSet.dataValue = App.Variables.getLoggedInUserDetails.dataSet.emplId;
        Partial.Variables.getLoggedInUserTeamIdVar.setInput({
            'userId': App.Variables.getLoggedInUserId.dataSet[0].id
        });
        Partial.Variables.getLoggedInUserTeamIdVar.invoke();

        Partial.Widgets.CreateCeaseRequestdialog.open();
    }
}

Partial.closeCreateSuspensionActionDialog = function() {
    Partial.Variables.errorMsg.dataSet.dataValue = "";
    Partial.Variables.UserLoggedInVar.dataSet.dataValue = App.Variables.getLoggedInUserDetails.dataSet.emplId;

    Partial.Widgets.CreateSuspensionRequestdialog.close();
};

Partial.closeCreateRestoralActionDialog = function() {
    Partial.Variables.errorMsg.dataSet.dataValue = "";
    Partial.Variables.UserLoggedInVar.dataSet.dataValue = App.Variables.getLoggedInUserDetails.dataSet.emplId;

    Partial.Widgets.CreateRestoralRequestdialog.close();
};
Partial.closeCreateCeaseActionDialogClick = function($event, widget) {
    Partial.Variables.errorMsg.dataSet.dataValue = "";
    Partial.Variables.UserLoggedInVar.dataSet.dataValue = App.Variables.getLoggedInUserDetails.dataSet.emplId;
    Partial.Widgets.CreateCeaseRequestdialog.close();
};
Partial.createbuttonClick = function($event, widget) {


    debugger;

    var isAssignedPerson = '';

    if (Partial.Widgets.assignedPersonSelect.datavalue == "" || Partial.Widgets.assignedPersonSelect.datavalue == undefined) {
        isAssignedPerson = "Request Created";
    } else {
        isAssignedPerson = "Request Assigned";
    }


    if (Partial.Widgets.susReasonCode.datavalue == "" || Partial.Widgets.susReasonCode.datavalue == undefined) {
        //App.Variables.errorMsg.dataSet.dataValue = "Reason code is mandatory";
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "Reason code is mandatory";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined) {
        //App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "Priority is mandatory";
    } else {
        // API Call will come here

        Partial.Variables.createOrderManagmentForSuspension.setInput({
            "CollectionTreatmentStepCreate": {
                'stepTypeCode': "SUSPEND",
                'reasonCode': Partial.Widgets.susReasonCode.datavalue.dataValue,
                'stepDate': Partial.Widgets.dueDate.datavalue,
                'comment': Partial.Widgets.Comment.datavalue,
                'status': isAssignedPerson,
                'priority': Partial.Widgets.prioritySelect.datavalue.dataValue,
                'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                'partitionKey': getCurrentDate(),
                'collectionTreatment': {
                    'id': Partial.Variables.getCollectionTreatmentByCollENtityId.dataSet[0].id,
                    'partitionKey': getCurrentDate()
                },
                'channel': {
                    'originatorAppId': "FAWBTELUSAGENT",
                    'channelOrgId': "FAWBTELUSAGENT",
                    'userId': Partial.Variables.getLoggedInUserDetails.dataSet.emplId
                },
            },
        });
        Partial.Variables.createOrderManagmentForSuspension.invoke();


    }


    // Partial.Variables.getCollectionTreatmentStep_orderMngt.invoke();


};
Partial.createbuttonRestoralClick = function($event, widget) {

    debugger;

    var isAssignedPerson = '';

    var isAlreadyRestored = false;

    if (Partial.Widgets.assignedPersonSelect.datavalue == "" || Partial.Widgets.assignedPersonSelect.datavalue == undefined) {
        isAssignedPerson = "Request Created";
    } else {
        isAssignedPerson = "Request Assigned";
    }

    Partial.Variables.BanListRefIds.dataSet = [];

    Partial.Widgets.getEntityBanDetailsTable1.selectedItems;
    Partial.selectedBanList = [];
    Partial.Widgets.getEntityBanDetailsTable1.selectedItems.forEach(function(d) {

        if (d.banCollectionStatus != "SUSPEND") {
            //  
            isAlreadyRestored = true;

        } else {

            Partial.selectedBanList = {
                "id": d.banRefId,

            }
            Partial.Variables.BanListRefIds.dataSet.push(Partial.selectedBanList);
        }



    });

    if (isAlreadyRestored) {
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "BAN selected is not Suspended in order to be Restored.";
    } else if (Partial.Widgets.restoralReasonCode.datavalue == "" || Partial.Widgets.restoralReasonCode.datavalue == undefined) {
        //App.Variables.errorMsg.dataSet.dataValue = "Reason code is mandatory";
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "Reason code is mandatory";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined) {
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "Priority is mandatory";
    } else {
        // API Call will come here

        Partial.Variables.createOrderManagmentForRestoral.setInput({
            "CollectionTreatmentStepCreate": {
                'stepTypeCode': "RESTORE",
                'reasonCode': Partial.Widgets.restoralReasonCode.datavalue.dataValue,
                'stepDate': Partial.Widgets.dueDate.datavalue,
                'comment': Partial.Widgets.Comment.datavalue,
                'status': isAssignedPerson,
                'priority': Partial.Widgets.prioritySelect.datavalue.dataValue,
                'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                'partitionKey': getCurrentDate(),
                'collectionTreatment': {
                    'id': Partial.Variables.getCollectionTreatmentByCollENtityId.dataSet[0].id,
                    'partitionKey': getCurrentDate()
                },
                'channel': {
                    'originatorAppId': "FAWBTELUSAGENT",
                    'channelOrgId': "FAWBTELUSAGENT",
                    'userId': Partial.Variables.getLoggedInUserDetails.dataSet.emplId
                },
                'billingAccountIdRefs': Partial.Variables.BanListRefIds.dataSet,
            },
        });
        Partial.Variables.createOrderManagmentForRestoral.invoke();



    }

    // Partial.Variables.getCollectionTreatmentStep_orderMngt.invoke();


};
Partial.createbuttonCeaseClick = function($event, widget) {

    debugger;

    var isAssignedPerson = '';

    if (Partial.Widgets.assignedPersonSelect.datavalue == "" || Partial.Widgets.assignedPersonSelect.datavalue == undefined) {
        isAssignedPerson = "Request Created";
    } else {
        isAssignedPerson = "Request Assigned";
    }


    Partial.Variables.BanListRefIds.dataSet = [];

    Partial.Widgets.getEntityBanDetailsTable1.selectedItems;
    Partial.selectedBanList = [];
    Partial.Widgets.getEntityBanDetailsTable1.selectedItems.forEach(function(d) {

        Partial.selectedBanList = {
            "id": d.banRefId,

        }
        Partial.Variables.BanListRefIds.dataSet.push(Partial.selectedBanList);

    });

    if (Partial.Widgets.ceaseReasonCode.datavalue == "" || Partial.Widgets.ceaseReasonCode.datavalue == undefined) {
        //App.Variables.errorMsg.dataSet.dataValue = "Reason code is mandatory";
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "Reason code is mandatory";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined) {
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "Priority is mandatory";
    } else {
        // API Call will come here

        Partial.Variables.createOrderManagmentForCease.setInput({
            "CollectionTreatmentStepCreate": {
                'stepTypeCode': "CEASE",
                'reasonCode': Partial.Widgets.ceaseReasonCode.datavalue.dataValue,
                'stepDate': Partial.Widgets.dueDate.datavalue,
                'comment': Partial.Widgets.Comment.datavalue,
                'status': isAssignedPerson,
                'priority': Partial.Widgets.prioritySelect.datavalue.dataValue,
                'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                'partitionKey': getCurrentDate(),
                'collectionTreatment': {
                    'id': Partial.Variables.getCollectionTreatmentByCollENtityId.dataSet[0].id,
                    'partitionKey': getCurrentDate()
                },
                'channel': {
                    'originatorAppId': "FAWBTELUSAGENT",
                    'channelOrgId': "FAWBTELUSAGENT",
                    'userId': Partial.Variables.getLoggedInUserDetails.dataSet.emplId
                },
                'billingAccountIdRefs': Partial.Variables.BanListRefIds.dataSet,
            },
        });
        Partial.Variables.createOrderManagmentForCease.invoke();

    }


};
Partial.getCollectionTreatmentStep_orderMngt_customRow1Action = function($event, row) {

    debugger;



    getBanListForAutoSelect(row);
    getBanDetails();
    if (row.stepTypeCode == 'SUSPEND') {
        if (row.status == 'Request Assigned' || row.status == 'Request Created') {

            Partial.Widgets.EditNotSentdialog.title = "Edit Suspension Request";
            Partial.Widgets.EditNotSentdialog.open();


        } else if (row.status == 'Order Assigned' || row.status == 'Order Created') {
            Partial.Widgets.EditAndFulfillSentdialog.title = "Edit and Fulfill Service Suspension";
            Partial.Widgets.EditAndFulfillSentdialog.open();

        }

    } else if (row.stepTypeCode == 'RESTORE') {
        if (row.status == 'Request Assigned' || row.status == 'Request Created') {
            Partial.Widgets.EditNotSentdialog.title = "Edit Restoral Request";
            Partial.Widgets.EditNotSentdialog.open();
        } else if (row.status == 'Order Assigned' || row.status == 'Order Created') {
            Partial.Widgets.EditAndFulfillSentdialog.title = "Edit and Fulfill Service Restoration";
            Partial.Widgets.EditAndFulfillSentdialog.open();
        }
    } else if (row.stepTypeCode == 'CEASE') {
        if (row.status == 'Request Assigned' || row.status == 'Request Created') {
            Partial.Widgets.EditNotSentdialog.title = "Edit Cease Request";
            Partial.Widgets.EditNotSentdialog.open();
        } else if (row.status == 'Order Assigned' || row.status == 'Order Created') {
            Partial.Widgets.EditAndFulfillSentdialog.title = "Edit and Fulfill Cease";
            Partial.Widgets.EditAndFulfillSentdialog.open();
        }

    }
};

// Edit Suspension/Restore/Cease- Not Sent button 


Partial.updateDONotSentbuttonClick = function($event, widget) {
    debugger;
    var stepTypeCode;
    var updateStatus = '';
    var isAlreadySusOrRes = '';
    Partial.Variables.newlyAssignedPerson.dataset = '';
    App.Variables.errorMsg.dataSet.dataValue = null;
    Partial.Variables.popUperrorMsg.dataSet.dataValue = null;
    var originalAgentId = Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.assignedAgentId;
    var selectedAgentId = Partial.Widgets.assignedPersonSelect.datavalue;
    Partial.Variables.BanListRefIds.dataSet = [];

    //get checked bans


    Partial.Widgets.getEntityBanDetailsTable1.selectedItems;
    Partial.selectedBanList = [];
    if (Partial.Widgets.EditNotSentdialog.title == "Edit Suspension Request") {
        stepTypeCode = "SUSPEND";
    } else if (Partial.Widgets.EditNotSentdialog.title == "Edit Restoral Request") {
        stepTypeCode = "RESTORE";
    } else if (Partial.Widgets.EditNotSentdialog.title == "Edit Cease Request") {
        stepTypeCode = "CEASE";
    }
    Partial.Widgets.getEntityBanDetailsTable1.selectedItems.forEach(function(d) {

        if (stepTypeCode == "SUSPEND" && d.banCollectionStatus == "SUSPEND") {
            isAlreadySusOrRes = stepTypeCode;
        } else if (stepTypeCode == "RESTORE" && d.banCollectionStatus != "SUSPEND") {

            isAlreadySusOrRes = stepTypeCode;
        } else {

            Partial.selectedBanList = {
                "id": d.banRefId,

            }
        }
        Partial.Variables.BanListRefIds.dataSet.push(Partial.selectedBanList);


    });


    if (Partial.Widgets.assignedPersonSelect.datavalue == "" || Partial.Widgets.assignedPersonSelect.datavalue == "Select" || Partial.Widgets.assignedPersonSelect.datavalue == null) {
        updateStatus = "Request Created";
    } else {
        updateStatus = "Request Assigned";
    }
    Partial.Variables.getStatusIfAssignedPersonChanged.dataset = updateStatus;


    if (isAlreadySusOrRes == "SUSPEND") {
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "BAN is already Suspended";
    } else if (isAlreadySusOrRes == "RESTORE") {
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "BAN selected is not Suspended in order to be Restored.";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined) {
        //App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "Priority is mandatory";
    } else {


        if (originalAgentId != selectedAgentId) {

            Partial.Variables.newlyAssignedPerson.dataset = Partial.Widgets.assignedPersonSelect.displayValue;

            Partial.Variables.updatedAssignedPerson.dataset = Partial.Widgets.assignedPersonSelect.datavalue;
            Partial.Variables.getStatusIfAssignedPersonChanged.dataset = updateStatus;
            Partial.Variables.updateSelectedBans.dataset = Partial.Variables.BanListRefIds.dataSet;
            Partial.Variables.updatePriority.dataset = Partial.Widgets.prioritySelect.datavalue;
            Partial.Variables.updateAssignedTeam.dataset = Partial.Widgets.assignedTeamSelect.datavalue;
            Partial.Variables.updateDueDateOrStepDate.dataset = Partial.Widgets.dueDate.datavalue;



            Partial.Widgets.EditNotSentdialog.close();

            Partial.Widgets.update_ActionDialog.open();

        } else {

            Partial.Variables.UpdateODManagemntAndDonotSend.setInput({
                'id': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.id,
                'partitionKey': getCurrentDate(),
                "CollectionTreatmentStepUpdate": {
                    'stepTypeCode': stepTypeCode,
                    'status': updateStatus,
                    'priority': Partial.Widgets.prioritySelect.datavalue,
                    'comment': Partial.Widgets.AddComment.datavalue,
                    'stepDate': Partial.Widgets.dueDate.datavalue,
                    'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                    'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                    'channel': {
                        'originatorAppId': "FAWBTELUSAGENT",
                        'channelOrgId': "FAWBTELUSAGENT",
                        'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
                    },
                    'billingAccountIdRefs': Partial.Variables.BanListRefIds.dataSet,
                }
            });

            //Invoke POST createDispute service
            Partial.Variables.UpdateODManagemntAndDonotSend.invoke();

        }
    }



};
Partial.editNotSentCancelbuttonClick = function($event, widget) {
    Partial.Variables.errorMsg.dataSet.dataValue = "";
    Partial.Variables.UserLoggedInVar.dataSet.dataValue = App.Variables.getLoggedInUserDetails.dataSet.emplId;
    Partial.Widgets.EditNotSentdialog.close();
};
Partial.updateandsendbuttonClick = function($event, widget) {



    Partial.Variables.newlyAssignedPerson.dataset = '';
    var updateStatus = '';
    var isAlreadySusOrRes = '';
    var originalAgentId = Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.assignedAgentId;
    var selectedAgentId = Partial.Widgets.assignedPersonSelect.datavalue;
    Partial.Variables.updatedAssignedPerson.dataset = Partial.Widgets.assignedPersonSelect.datavalue;
    Partial.Variables.BanListRefIds.dataSet = [];

    Partial.Widgets.getEntityBanDetailsTable1.selectedItems;
    Partial.selectedBanList = [];
    var stepTypeCode;
    if (Partial.Widgets.EditNotSentdialog.title == "Edit Suspension Request") {
        stepTypeCode = "SUSPEND";
    } else if (Partial.Widgets.EditNotSentdialog.title == "Edit Restoral Request") {
        stepTypeCode = "RESTORE";
    } else if (Partial.Widgets.EditNotSentdialog.title == "Edit Cease Request") {
        stepTypeCode = "CEASE";
    }

    Partial.Widgets.getEntityBanDetailsTable1.selectedItems.forEach(function(d) {

        // Partial.selectedBanList = {
        //     "id": d.banRefId,

        // }
        // Partial.Variables.BanListRefIds.dataSet.push(Partial.selectedBanList);

        if (stepTypeCode == "SUSPEND" && d.banCollectionStatus == "SUSPEND") {
            isAlreadySusOrRes = stepTypeCode;
        } else if (stepTypeCode == "RESTORE" && d.banCollectionStatus != "SUSPEND") {

            isAlreadySusOrRes = stepTypeCode;
        } else {

            Partial.selectedBanList = {
                "id": d.banRefId,

            }
        }
        Partial.Variables.BanListRefIds.dataSet.push(Partial.selectedBanList);


    });

    if (Partial.Widgets.assignedPersonSelect.datavalue == "" || Partial.Widgets.assignedPersonSelect.datavalue == "Select" || Partial.Widgets.assignedPersonSelect.datavalue == null) {
        updateStatus = "Order Created";
    } else {
        updateStatus = "Order Assigned";
    }


    if (isAlreadySusOrRes == "SUSPEND") {
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "BAN is already Suspended";
    } else if (isAlreadySusOrRes == "RESTORE") {
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "BAN selected is not Suspended in order to be Restored.";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined) {
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "Priority is mandatory";
    } else {



        if (originalAgentId != selectedAgentId) {

            Partial.Variables.newlyAssignedPerson.dataset = Partial.Widgets.assignedPersonSelect.displayValue;


            Partial.Variables.updatedAssignedPerson.dataset = Partial.Widgets.assignedPersonSelect.datavalue;
            Partial.Variables.getStatusIfAssignedPersonChanged.dataset = updateStatus;

            Partial.Variables.updateSelectedBans.dataset = Partial.Variables.BanListRefIds.dataSet;
            Partial.Variables.updatePriority.dataset = Partial.Widgets.prioritySelect.datavalue;
            Partial.Variables.updateAssignedTeam.dataset = Partial.Widgets.assignedTeamSelect.datavalue;
            Partial.Variables.updateDueDateOrStepDate.dataset = Partial.Widgets.dueDate.datavalue;

            Partial.Widgets.EditNotSentdialog.close();

            Partial.Widgets.update_ActionDialog.open();


        } else {
            Partial.Variables.UpdateODManagemntAndSend.setInput({
                'id': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.id,
                'partitionKey': getCurrentDate(),
                "CollectionTreatmentStepUpdate": {
                    'stepTypeCode': stepTypeCode,
                    'status': updateStatus,
                    'priority': Partial.Widgets.prioritySelect.datavalue,
                    'comment': Partial.Widgets.AddComment.datavalue,
                    'stepDate': Partial.Widgets.dueDate.datavalue,
                    'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                    'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                    'channel': {
                        'originatorAppId': "FAWBTELUSAGENT",
                        'channelOrgId': "FAWBTELUSAGENT",
                        'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
                    },
                    'billingAccountIdRefs': Partial.Variables.BanListRefIds.dataSet,
                }
            });

            //Invoke POST createDispute service
            Partial.Variables.UpdateODManagemntAndSend.invoke();


        }
    }
};

// Edit Suspension/Restore/Cease- Sent button 

Partial.editSentcancelbuttonClick = function($event, widget) {
    Partial.Variables.errorMsg.dataSet.dataValue = "";
    Partial.Variables.UserLoggedInVar.dataSet.dataValue = App.Variables.getLoggedInUserDetails.dataSet.emplId;
    Partial.Widgets.EditAndFulfillSentdialog.close();
};
Partial.updateAndDoNotFulfillbuttonClick = function($event, widget) {
    debugger;
    var updateStatus = '';
    Partial.Variables.newlyAssignedPerson.dataset = '';
    var isAlreadySusOrRes = '';
    var originalAgentId = Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.assignedAgentId;
    var selectedAgentId = Partial.Widgets.assignedPersonSelect.datavalue;
    Partial.Variables.BanListRefIds.dataSet = [];

    Partial.Widgets.getEntityBanDetailsTable1.selectedItems;
    Partial.selectedBanList = [];
    var stepTypeCode;
    if (Partial.Widgets.EditAndFulfillSentdialog.title == "Edit and Fulfill Service Suspension") {
        stepTypeCode = "SUSPEND";
    } else if (Partial.Widgets.EditAndFulfillSentdialog.title == "Edit and Fulfill Service Restoration") {
        stepTypeCode = "RESTORE";
    } else if (Partial.Widgets.EditAndFulfillSentdialog.title == "Edit and Fulfill Cease") {
        stepTypeCode = "CEASE";
    }
    Partial.Widgets.getEntityBanDetailsTable1.selectedItems.forEach(function(d) {

        if (stepTypeCode == "SUSPEND" && d.banCollectionStatus == "SUSPEND") {
            isAlreadySusOrRes = stepTypeCode;
        } else if (stepTypeCode == "RESTORE" && d.banCollectionStatus != "SUSPEND") {

            isAlreadySusOrRes = stepTypeCode;
        } else {

            Partial.selectedBanList = {
                "id": d.banRefId,

            }
        }
        Partial.Variables.BanListRefIds.dataSet.push(Partial.selectedBanList);

    });
    if (Partial.Widgets.assignedPersonSelect.datavalue == "" || Partial.Widgets.assignedPersonSelect.datavalue == "Select" || Partial.Widgets.assignedPersonSelect.datavalue == null) {
        updateStatus = "Order Created";
    } else {
        updateStatus = "Order Assigned";
    }

    if (isAlreadySusOrRes == "SUSPEND") {
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "BAN is already Suspended";
    } else if (isAlreadySusOrRes == "RESTORE") {
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "BAN selected is not Suspended in order to be Restored.";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined) {
        //App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "Priority is mandatory";
    } else {


        if (originalAgentId != selectedAgentId) {
            Partial.Variables.updatedAssignedPerson.dataset = Partial.Widgets.assignedPersonSelect.datavalue;
            Partial.Variables.newlyAssignedPerson.dataset = Partial.Widgets.assignedPersonSelect.displayValue;
            Partial.Variables.getStatusIfAssignedPersonChanged.dataset = updateStatus;
            Partial.Variables.updateSelectedBans.dataset = Partial.Variables.BanListRefIds.dataSet;
            Partial.Variables.updatePriority.dataset = Partial.Widgets.prioritySelect.datavalue;
            Partial.Variables.updateAssignedTeam.dataset = Partial.Widgets.assignedTeamSelect.datavalue;
            Partial.Variables.updateDueDateOrStepDate.dataset = Partial.Widgets.dueDate.datavalue;


            Partial.Widgets.EditAndFulfillSentdialog.close();
            Partial.Widgets.update_ActionDialog.open();

        } else {
            Partial.Variables.UpdateODManagemntAndDonotFullfill.setInput({
                'id': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.id,
                'partitionKey': getCurrentDate(),
                "CollectionTreatmentStepUpdate": {
                    'stepTypeCode': stepTypeCode,
                    'status': updateStatus,
                    'priority': Partial.Widgets.prioritySelect.datavalue,
                    'comment': Partial.Widgets.AddComment2.datavalue,
                    'stepDate': Partial.Widgets.dueDate.datavalue,
                    'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                    'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                    'channel': {
                        'originatorAppId': "FAWBTELUSAGENT",
                        'channelOrgId': "FAWBTELUSAGENT",
                        'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
                    },
                    'billingAccountIdRefs': Partial.Variables.BanListRefIds.dataSet,
                }
            });

            //Invoke POST createDispute service
            Partial.Variables.UpdateODManagemntAndDonotFullfill.invoke();

        }
    }
};

Partial.updateAndFulfilbuttonClick = function($event, widget) {


    debugger;


    var isAlreadySusOrRes = '';
    var originalAgentId = Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.assignedAgentId;
    var selectedAgentId = Partial.Widgets.assignedPersonSelect.datavalue;
    Partial.Variables.updatedAssignedPerson.dataset = Partial.Widgets.assignedPersonSelect.datavalue;
    var stepTypeCode;
    if (Partial.Widgets.EditAndFulfillSentdialog.title == "Edit and Fulfill Service Suspension") {
        stepTypeCode = "SUSPEND";
    } else if (Partial.Widgets.EditAndFulfillSentdialog.title == "Edit and Fulfill Service Restoration") {
        stepTypeCode = "RESTORE";
    } else if (Partial.Widgets.EditAndFulfillSentdialog.title == "Edit and Fulfill Cease") {
        stepTypeCode = "CEASE";
    }

    Partial.Variables.BanListRefIds.dataSet = [];

    Partial.Widgets.getEntityBanDetailsTable1.selectedItems;
    Partial.selectedBanList = [];
    Partial.Widgets.getEntityBanDetailsTable1.selectedItems.forEach(function(d) {

        if (stepTypeCode == "SUSPEND" && d.banCollectionStatus == "SUSPEND") {
            isAlreadySusOrRes = stepTypeCode;
        } else if (stepTypeCode == "RESTORE" && d.banCollectionStatus != "SUSPEND") {

            isAlreadySusOrRes = stepTypeCode;
        } else {

            Partial.selectedBanList = {
                "id": d.banRefId,

            }
        }
        Partial.Variables.BanListRefIds.dataSet.push(Partial.selectedBanList);

    });

    if (isAlreadySusOrRes == "SUSPEND") {
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "BAN is already Suspended";
    } else if (isAlreadySusOrRes == "RESTORE") {
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "BAN selected is not Suspended in order to be Restored";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined) {
        //App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "Priority is mandatory";

    } else if (originalAgentId == null && selectedAgentId == null || selectedAgentId == "") {
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "Assigned Person must be selected to Fulfill Order";

    } else {


        if (originalAgentId != selectedAgentId) {


            Partial.Variables.newlyAssignedPerson.dataset = Partial.Widgets.assignedPersonSelect.displayValue;


            Partial.Variables.updatedAssignedPerson.dataset = Partial.Widgets.assignedPersonSelect.datavalue;

            Partial.Variables.updateSelectedBans.dataset = Partial.Variables.BanListRefIds.dataSet;
            Partial.Variables.getStatusIfAssignedPersonChanged.dataset = 'Order Fulfilled';

            Partial.Variables.updatePriority.dataset = Partial.Widgets.prioritySelect.datavalue;
            Partial.Variables.updateAssignedTeam.dataset = Partial.Widgets.assignedTeamSelect.datavalue;
            Partial.Variables.updateDueDateOrStepDate.dataset = Partial.Widgets.dueDate.datavalue;


            Partial.Widgets.EditAndFulfillSentdialog.close();

            Partial.Widgets.update_ActionDialog.open();

        } else {
            Partial.Variables.UpdateODManagemntAndFullfill.setInput({
                'id': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.id,
                'partitionKey': getCurrentDate(),
                "CollectionTreatmentStepUpdate": {
                    'stepTypeCode': stepTypeCode,
                    'status': 'Order Fulfilled',
                    'priority': Partial.Widgets.prioritySelect.datavalue,
                    'comment': Partial.Widgets.AddComment2.datavalue,
                    'stepDate': Partial.Widgets.dueDate.datavalue,
                    'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                    'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                    'channel': {
                        'originatorAppId': "FAWBTELUSAGENT",
                        'channelOrgId': "FAWBTELUSAGENT",
                        'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
                    },
                    'billingAccountIdRefs': Partial.Variables.BanListRefIds.dataSet,
                }
            });

            //Invoke POST createDispute service
            Partial.Variables.UpdateODManagemntAndFullfill.invoke();

        }
    }
};

function messageTimeout() {
    Partial.Variables.successMessage.dataSet.dataValue = null;
    Partial.Variables.errorMsg.dataSet.dataValue = null;
    Partial.Variables.popUperrorMsg.dataSet.dataValue = null;
}
Partial.getCollectionTreatmentStep_orderMngt_customRow2Action = function($event, row) {

    if (row.assignedAgentId == '' || row.assignedAgentId == null) {

        Partial.Widgets.notAssigned_closeActionDialog.open();

    } else {
        Partial.Widgets.assigned_closeActionDialog.open();
    }

};
Partial.getCollectionTreatmentStep_orderMngt_customRow3Action = function($event, row) {
    if (row.assignedAgentId == '' || row.assignedAgentId == null) {

        Partial.Widgets.notAssigned_cancleActionDialog.open();

    } else {
        Partial.Widgets.assigned_cancleActionDialog.open();
    }
};

// for goback button
Partial.button17_1Click = function($event, widget) {
    Partial.Widgets.notAssigned_cancleActionDialog.close();
};
Partial.button15_1closeActionClick = function($event, widget) {

    Partial.Widgets.notAssigned_closeActionDialog.close();

};


//close assigned person
Partial.closeAction_NoClick = function($event, widget) {
    Partial.Widgets.assigned_closeActionDialog.close();
};
Partial.assigned_closeYesBtnClick = function($event, widget) {
    debugger;
    Partial.Variables.UpdateODManagemntAndCloseAction.setInput({
        'id': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.id,
        'partitionKey': getCurrentDate(),
        "CollectionTreatmentStepUpdate": {
            'status': 'Closed',
            'comment': Partial.Widgets.closeComment.datavalue,
            'channel': {
                'originatorAppId': "FAWBTELUSAGENT",
                'channelOrgId': "FAWBTELUSAGENT",
                'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
            },
        }
    });

    //Invoke POST createDispute service
    Partial.Variables.UpdateODManagemntAndCloseAction.invoke();

};

// cancle assigned person
Partial.assigned_cancleYesBtnClick = function($event, widget) {


    Partial.Variables.UpdateODManagemntAndCancelledActiion.setInput({
        'id': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.id,
        'partitionKey': getCurrentDate(),
        "CollectionTreatmentStepUpdate": {
            'status': 'Cancelled',
            'comment': Partial.Widgets.cancelComment.datavalue,
            'channel': {
                'originatorAppId': "FAWBTELUSAGENT",
                'channelOrgId': "FAWBTELUSAGENT",
                'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
            },

        }
    });

    //Invoke POST createDispute service
    Partial.Variables.UpdateODManagemntAndCancelledActiion.invoke();

};
Partial.assigned_cancleNoBtnClick = function($event, widget) {
    Partial.Widgets.assigned_cancleActionDialog.close();
};

// for Update 
Partial.update_YesBtnClick = function($event, widget) {
    debugger;
    //  Partial.Widgets.EditNotSentdialog.close();

    Partial.Variables.UpdateODManagemntWhenAssignChange.setInput({
        'id': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.id,
        'partitionKey': getCurrentDate(),
        'collectionEntityId': Partial.pageParams.entityId,
        "CollectionTreatmentStepUpdate": {
            'stepTypeCode': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.stepTypeCode,
            'status': Partial.Variables.getStatusIfAssignedPersonChanged.dataset,
            'priority': Partial.Variables.updatePriority.dataset,
            'comment': Partial.Widgets.Comment.datavalue,
            'stepDate': Partial.Variables.updateDueDateOrStepDate.dataset,
            'assignedAgentId': Partial.Variables.updatedAssignedPerson.dataset,
            'assignedTeam': Partial.Variables.updateAssignedTeam.dataset,
            'channel': {
                'originatorAppId': "FAWBTELUSAGENT",
                'channelOrgId': "FAWBTELUSAGENT",
                'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
            },

            'billingAccountIdRefs': Partial.Variables.updateSelectedBans.dataset,
        }
    });

    //Invoke POST createDispute service
    Partial.Variables.UpdateODManagemntWhenAssignChange.invoke();

};

Partial.update_NoBtnClick = function($event, widget) {

    Partial.Widgets.update_ActionDialog.close();

    Partial.Widgets.EditNotSentdialog.open();


};
Partial.EditNotSentdialogOpened = function($event, widget) {

    App.Variables.errorMsg.dataSet.dataValue = '';


    if (Partial.Widgets.EditNotSentdialog.title == "Edit Suspension Request" || Partial.Widgets.EditNotSentdialog.title == "Edit and Fulfill Service Suspension") {

        document.getElementById("myHeader").innerHTML = "BANs to Suspend";
    } else if (Partial.Widgets.EditNotSentdialog.title == "Edit Restoral Request" || Partial.Widgets.EditNotSentdialog.title == "Edit and Fulfill Service Restoration") {
        document.getElementById("myHeader").innerHTML = "BANs to Restore";
    } else if (Partial.Widgets.EditNotSentdialog.title == "Edit Cease Request" || Partial.Widgets.EditNotSentdialog.title == "Edit and Fulfill Cease") {
        document.getElementById("myHeader").innerHTML = "BANs to Cease Suspension";
    }

};


function getBanDetails() {


    var entityIdStr = Partial.pageParams.entityId
    var entityIdInt = parseInt(entityIdStr);
    Partial.Variables.CollectionDataServiceGetEntityBanDetails.setInput({
        'entityId': entityIdInt
    });

    Partial.Variables.CollectionDataServiceGetEntityBanDetails.invoke();

}


Partial.update_ActionDialogOpened = function($event, widget) {

    if (Partial.Variables.newlyAssignedPerson.dataset == undefined) {
        Partial.Widgets.label61.caption = 'This action ' + Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.stepTypeCode + " (" + Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.id + ')' + ' has not been assigned to a person.'
    } else {



        Partial.Widgets.label61.caption = 'This action ' + Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.stepTypeCode + " (" + Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.id + ')' + ' has been assigned to ' + Partial.Variables.newlyAssignedPerson.dataset + "  (" + Partial.Widgets.assignedPersonSelect.datavalue + ')' + ' who may be working on it.'
    }
};

Partial.getCollectionTreatmentStep_orderMngt_customRowAction = function($event, row) {


    var entityIdStr = Partial.pageParams.entityId
    var entityIdInt = parseInt(entityIdStr);
    Partial.Variables.getOrderdMgmtHistory.setInput({
        'collectionEntityId': entityIdInt,
        'relatedBusinessEntityId': row.id,
        'relatedBusinessEntityType': 'CollectionTreatmentStep'

    });

    Partial.Variables.getOrderdMgmtHistory.invoke();
    Partial.Widgets.ViewHistory.open();


};


Partial.EditAndFulfillSentdialogOpened = function($event, widget) {


    if (Partial.Widgets.EditAndFulfillSentdialog.title == "Edit Suspension Request" || Partial.Widgets.EditAndFulfillSentdialog.title == "Edit and Fulfill Service Suspension") {

        document.getElementById("myHeader2").innerHTML = "BANs to Suspend";
    } else if (Partial.Widgets.EditAndFulfillSentdialog.title == "Edit Restoral Request" || Partial.Widgets.EditAndFulfillSentdialog.title == "Edit and Fulfill Service Restoration") {
        document.getElementById("myHeader2").innerHTML = "BANs to Restore";
    } else if (Partial.Widgets.EditAndFulfillSentdialog.title == "Edit Cease Request" || Partial.Widgets.EditAndFulfillSentdialog.title == "Edit and Fulfill Cease") {
        document.getElementById("myHeader2").innerHTML = "BANs to Cease Suspension";
    }

}

Partial.CollectionDataServiceGetEntityBanDetailsonError = function(variable, data, xhrObj) {

};

Partial.getCollectionTreatmentStep_orderMngtonError = function(variable, data, xhrObj) {

};


Partial.getCollectionTreatMentonError = function(variable, data, xhrObj) {

};

App.refreshCollOrderMgmtList = function() {
    setTimeout(function() {
        Partial.Variables.getCollectionTreatmentStep_orderMngt.invoke();
    }, 1000);


};

Partial.getOrderdMgmtHistoryonError = function(variable, data, xhrObj) {

};
Partial.EditNotSentdialogClose = function($event, widget) {
    Partial.Variables.popUperrorMsg.dataSet.dataValue = "";
};
Partial.EditAndFulfillSentdialogClose = function($event, widget) {

    Partial.Variables.popUperrorMsg.dataSet.dataValue = "";

};


Partial.createOrderManagmentForSuspensiononSuccess = function(variable, data) {

    Partial.Widgets.CreateSuspensionRequestdialog.close();
    App.Variables.successMessage.dataSet.dataValue = "Action Suspended successfully.";

    setTimeout(messageTimeout, 4000);

    setTimeout(function() {
        Partial.Variables.getCollectionTreatmentStep_orderMngt.invoke();
    }, 1000);

    App.refreshCollActionList();
    App.refreshHistoryActionList();



};

Partial.createOrderManagmentForRestoralonSuccess = function(variable, data) {

    Partial.Widgets.CreateRestoralRequestdialog.close();
    App.Variables.successMessage.dataSet.dataValue = "Action Restored successfully.";

    setTimeout(messageTimeout, 4000);

    setTimeout(function() {
        Partial.Variables.getCollectionTreatmentStep_orderMngt.invoke();
    }, 1000);

    App.refreshCollActionList();
    App.refreshHistoryActionList();

};

Partial.createOrderManagmentForCeaseonSuccess = function(variable, data) {

    Partial.Widgets.CreateCeaseRequestdialog.close();
    App.Variables.successMessage.dataSet.dataValue = "Action Ceased successfully.";

    setTimeout(messageTimeout, 4000);

    setTimeout(function() {
        Partial.Variables.getCollectionTreatmentStep_orderMngt.invoke();
    }, 1000);

    App.refreshCollActionList();
    App.refreshHistoryActionList();


};

Partial.UpdateODManagemntAndDonotSendonSuccess = function(variable, data) {

    Partial.Widgets.EditNotSentdialog.close();

    App.Variables.successMessage.dataSet.dataValue = "Updated Without Sent To Fulfillment successfully.";
    setTimeout(messageTimeout, 5000);
    App.refreshCollOrderMgmtList();

    App.refreshCollActionList();
    App.refreshHistoryActionList();

};

Partial.UpdateODManagemntAndSendonSuccess = function(variable, data) {
    Partial.Widgets.EditNotSentdialog.close();

    App.Variables.successMessage.dataSet.dataValue = " Updated And Sent successfully.";
    setTimeout(messageTimeout, 5000);
    App.refreshCollOrderMgmtList();

    App.refreshCollActionList();
    App.refreshHistoryActionList();
};

Partial.UpdateODManagemntAndDonotFullfillonSuccess = function(variable, data) {
    Partial.Widgets.EditAndFulfillSentdialog.close();

    App.Variables.successMessage.dataSet.dataValue = "Updated Without Sent to Fulfilment successfully.";
    setTimeout(messageTimeout, 5000);
    App.refreshCollOrderMgmtList();

    App.refreshCollActionList();
    App.refreshHistoryActionList();
};

Partial.UpdateODManagemntAndFullfillonSuccess = function(variable, data) {

    Partial.Widgets.EditAndFulfillSentdialog.close();

    App.Variables.successMessage.dataSet.dataValue = " Updated And Fullfill successfully";
    setTimeout(messageTimeout, 5000);
    App.refreshCollOrderMgmtList();

    App.refreshCollActionList();
    App.refreshHistoryActionList();

};

Partial.UpdateODManagemntAndCloseActiononSuccess = function(variable, data) {
    Partial.Widgets.assigned_closeActionDialog.close();
    App.Variables.successMessage.dataSet.dataValue = " Action Closed successfully";
    setTimeout(messageTimeout, 5000);
    App.refreshCollOrderMgmtList();

    App.refreshCollActionList();
    App.refreshHistoryActionList();
};

Partial.UpdateODManagemntAndCancelledActiiononSuccess = function(variable, data) {

    Partial.Widgets.assigned_cancleActionDialog.close();
    App.Variables.successMessage.dataSet.dataValue = " Action Cancelled successfully";
    setTimeout(messageTimeout, 5000);
    App.refreshCollOrderMgmtList();

    App.refreshCollActionList();
    App.refreshHistoryActionList();

};

Partial.UpdateODManagemntWhenAssignChangeonSuccess = function(variable, data) {

    Partial.Widgets.update_ActionDialog.close();
    App.Variables.successMessage.dataSet.dataValue = " Action Updated Successfully";
    setTimeout(messageTimeout, 5000);
    App.refreshCollOrderMgmtList();

    App.refreshCollActionList();

    App.refreshHistoryActionList();

};

Partial.getOrderdMgmtHistoryonSuccess = function(variable, data) {
    debugger;
    //  var bans = Partial.Variables.getOrderdMgmtHistory.dataSet[0].banList;

    // var bans = data[0].banList;




    // if (Partial.Widgets.EditNotSentdialog.name == 'EditNotSentdialog') {

    //     var tr = $('#getEntityBanDetailsTableEdit1ForTest  tbody tr');
    //     for (let j = 0; j < bans.length; j++) {

    //         for (let i = 0; i < tr.length; i++) {
    //             if (bans[j].toString() === tr[i].cells[1].innerHTML) {
    //                 // tr.find("input[type='checkbox']").eq(i).prop('checked', true)
    //                 tr.find("input[type='checkbox']").eq(i).attr('checked', 'checked')

    //                 //   $(this).parent().parent().addClass("selected")
    //             }
    //         }
    //     }

    // }
    // if (Partial.Widgets.EditAndFulfillSentdialog.name === 'EditAndFulfillSentdialog') {
    //     var tr = $('#getEntityBanDetailsTableEdit2ForTest  tbody tr');
    //     for (let j = 0; j < bans.length; j++) {

    //         for (let i = 0; i < tr.length; i++) {
    //             if (bans[j].toString() === tr[i].cells[1].innerHTML) {
    //                 var cb = tr.find("input[type='checkbox']").eq(i).attr('checked', 'checked')
    //                 cb.trigger('click');

    //                 //   $(this).parent().parent().addClass("selected")
    //             }
    //         }
    //     }
    // }





};


Partial.getOrderdMgmtHistoryonBeforeDatasetReady = function(variable, data) {
    debugger;

    var bans = data[0].banList;




    if (Partial.Widgets.EditNotSentdialog.name == 'EditNotSentdialog') {

        var tr = $('#getEntityBanDetailsTableEdit1ForTest  tbody tr');
        for (let j = 0; j < bans.length; j++) {

            for (let i = 0; i < tr.length; i++) {
                if (bans[j].toString() === tr[i].cells[1].innerHTML) {
                    // tr.find("input[type='checkbox']").eq(i).prop('checked', true)
                    var cb = tr.find("input[type='checkbox']").eq(i).attr('checked', 'checked')
                    cb.trigger('click');
                    //   $(this).parent().parent().addClass("selected")
                }
            }
        }

    }
    if (Partial.Widgets.EditAndFulfillSentdialog.name === 'EditAndFulfillSentdialog') {
        var tr = $('#getEntityBanDetailsTableEdit2ForTest  tbody tr');
        for (let j = 0; j < bans.length; j++) {

            for (let i = 0; i < tr.length; i++) {
                if (bans[j].toString() === tr[i].cells[1].innerHTML) {
                    var cb = tr.find("input[type='checkbox']").eq(i).attr('checked', 'checked')
                    cb.trigger('click');

                    //   $(this).parent().parent().addClass("selected")
                }
            }
        }
    }


};


function getBanListForAutoSelect(row) {

    debugger;


    Partial.Variables.getOrderdMgmtHistory.setInput({
        'collectionEntityId': Partial.pageParams.entityId,
        'relatedBusinessEntityId': row.id,
        'relatedBusinessEntityType': 'CollectionTreatmentStep'

    });

    Partial.Variables.getOrderdMgmtHistory.invoke();

}