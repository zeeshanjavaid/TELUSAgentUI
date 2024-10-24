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

function isAssignmentValid(assignedAgentId, assignedTeam) {
    return (assignedAgentId && assignedAgentId != "Select") || (assignedTeam && assignedTeam != "Select");
}

Partial.CreateSuspentionRequestClick = function($event, widget) {
    debugger;
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
    var isAssignedPerson = '';
    if (Partial.Widgets.assignedPersonSelect.datavalue == "" || Partial.Widgets.assignedPersonSelect.datavalue == undefined) {
        isAssignedPerson = "Request Created";
    } else {
        isAssignedPerson = "Request Assigned";
    }
    if (Partial.Widgets.susReasonCode.datavalue == "" || Partial.Widgets.susReasonCode.datavalue == undefined) {
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "Reason code is mandatory";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined) {
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "Priority is mandatory";
    } else if (!isAssignmentValid(Partial.Widgets.assignedPersonSelect.datavalue, Partial.Widgets.assignedTeamSelect.datavalue)) {
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "Assigned Person or Assigned Team is mandatory";
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
                    // 'partitionKey': getCurrentDate()
                    'partitionKey': Partial.Variables.getCollectionTreatmentByCollENtityId.dataSet[0].partitionKey
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
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "Reason code is mandatory";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined) {
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "Priority is mandatory";
    } else if (!isAssignmentValid(Partial.Widgets.assignedPersonSelect.datavalue, Partial.Widgets.assignedTeamSelect.datavalue)) {
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "Assigned Person or Assigned Team is mandatory";
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
                    // 'partitionKey': getCurrentDate()
                    'partitionKey': Partial.Variables.getCollectionTreatmentByCollENtityId.dataSet[0].partitionKey
                },
                'channel': {
                    'originatorAppId': "FAWBTELUSAGENT",
                    'channelOrgId': "FAWBTELUSAGENT",
                    'userId': Partial.Variables.getLoggedInUserDetails.dataSet.emplId
                },
                'billingAccountRefs': Partial.Variables.BanListRefIds.dataSet,
            },
        });
        Partial.Variables.createOrderManagmentForRestoral.invoke();
    }
};

Partial.createbuttonCeaseClick = function($event, widget) {
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
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "Reason code is mandatory";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined) {
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "Priority is mandatory";
    } else if (!isAssignmentValid(Partial.Widgets.assignedPersonSelect.datavalue, Partial.Widgets.assignedTeamSelect.datavalue)) {
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "Assigned Person or Assigned Team is mandatory";
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
                    // 'partitionKey': getCurrentDate()
                    'partitionKey': Partial.Variables.getCollectionTreatmentByCollENtityId.dataSet[0].partitionKey
                },
                'channel': {
                    'originatorAppId': "FAWBTELUSAGENT",
                    'channelOrgId': "FAWBTELUSAGENT",
                    'userId': Partial.Variables.getLoggedInUserDetails.dataSet.emplId
                },
                'billingAccountRefs': Partial.Variables.BanListRefIds.dataSet,
            },
        });
        Partial.Variables.createOrderManagmentForCease.invoke();
    }
};

Partial.getCollectionTreatmentStep_orderMngt_customRow1Action = function($event, row) {
    Partial.Variables.dafualtAssignedUser.dataSet.empId = row.assignedPersonForDefaultValue;
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
    setTimeout(function() {
        getBanListForAutoSelect(row);
    }, 999);
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
    Partial.Variables.treatmentStepOriginalStepDate.dataset = Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.stepDate;
    var originalAgentId = Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.assignedPersonForDefaultValue;
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
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "Priority is mandatory";
    } else if (Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.assignedAgentId && (!Partial.Widgets.assignedPersonSelect.datavalue || Partial.Widgets.assignedPersonSelect.datavalue == "Select")) {
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "Cannot unassign person";
    } else if (Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.assignedTeam && (!Partial.Widgets.assignedTeamSelect.datavalue || Partial.Widgets.assignedTeamSelect.datavalue == "Select")) {
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "Cannot unassign team";
    } else {
        if (originalAgentId != selectedAgentId) {
            //  Partial.Variables.newlyAssignedPerson.dataset = Partial.Widgets.assignedPersonSelect.displayValue;
            Partial.Variables.newlyAssignedPerson.dataset = Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.assignedAgentId;
            Partial.Variables.updatedAssignedPerson.dataset = Partial.Widgets.assignedPersonSelect.datavalue;
            Partial.Variables.getStatusIfAssignedPersonChanged.dataset = updateStatus;
            Partial.Variables.updateSelectedBans.dataset = Partial.Variables.BanListRefIds.dataSet;
            Partial.Variables.updatePriority.dataset = Partial.Widgets.prioritySelect.datavalue;
            Partial.Variables.updateAssignedTeam.dataset = Partial.Widgets.assignedTeamSelect.datavalue;
            Partial.Variables.updateDueDateOrStepDate.dataset = Partial.Widgets.dueDate.datavalue;
            Partial.Variables.selectedOrderMgmtId.dataset = Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.id;
            Partial.Variables.selectedOrderMgmtPartitionKey.dataset = Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.partitionKey;
            Partial.Variables.stepTypeCodeForOrderMgmt.dataset = stepTypeCode;

            var payload = {
                'id': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.id,
                'partitionKey': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.partitionKey,
                "CollectionTreatmentStepUpdate": {
                    'stepTypeCode': stepTypeCode,
                    'status': updateStatus,
                    'priority': Partial.Widgets.prioritySelect.datavalue,
                    'comment': Partial.Widgets.AddComment.datavalue,
                    'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                    'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                    'channel': {
                        'originatorAppId': "FAWBTELUSAGENT",
                        'channelOrgId': "FAWBTELUSAGENT",
                        'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
                    }
                }
            };
            if (Partial.Variables.treatmentStepOriginalStepDate.dataset != Partial.Widgets.dueDate.datavalue) {
                payload.CollectionTreatmentStepUpdate = { ...payload.CollectionTreatmentStepUpdate,
                    stepDate: Partial.Widgets.dueDate.datavalue
                };
            }
            if (Partial.Variables.BanListRefIds.dataSet && Partial.Variables.BanListRefIds.dataSet.length > 0) {
                payload.CollectionTreatmentStepUpdate = { ...payload.CollectionTreatmentStepUpdate,
                    billingAccountRefs: Partial.Variables.BanListRefIds.dataSet
                };
            }
            Partial.Variables.UpdateODManagemntAndDonotSendIfAssignedChanged.setInput(payload);
            Partial.Variables.UpdateODManagemntAndDonotSendIfAssignedChanged.invoke();
            Partial.Widgets.EditNotSentdialog.close();
            Partial.Widgets.update_ActionDialog.open();
        } else {
            var payload = {
                'id': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.id,
                'partitionKey': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.partitionKey,
                "CollectionTreatmentStepUpdate": {
                    'stepTypeCode': stepTypeCode,
                    'status': updateStatus,
                    'priority': Partial.Widgets.prioritySelect.datavalue,
                    'comment': Partial.Widgets.AddComment.datavalue,
                    'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                    'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                    'channel': {
                        'originatorAppId': "FAWBTELUSAGENT",
                        'channelOrgId': "FAWBTELUSAGENT",
                        'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
                    }
                }
            };
            if (Partial.Variables.treatmentStepOriginalStepDate.dataset != Partial.Widgets.dueDate.datavalue) {
                payload.CollectionTreatmentStepUpdate = { ...payload.CollectionTreatmentStepUpdate,
                    stepDate: Partial.Widgets.dueDate.datavalue
                };
            }

            if (Partial.Variables.BanListRefIds.dataSet && Partial.Variables.BanListRefIds.dataSet.length > 0) {
                payload.CollectionTreatmentStepUpdate = { ...payload.CollectionTreatmentStepUpdate,
                    billingAccountRefs: Partial.Variables.BanListRefIds.dataSet
                };
            }
            Partial.Variables.UpdateODManagemntAndDonotSend.setInput(payload);
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
    var originalAgentId = Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.assignedPersonForDefaultValue;
    var selectedAgentId = Partial.Widgets.assignedPersonSelect.datavalue;
    Partial.Variables.treatmentStepOriginalStepDate.dataset = Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.stepDate;
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
    } else if (Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.assignedAgentId && (!Partial.Widgets.assignedPersonSelect.datavalue || Partial.Widgets.assignedPersonSelect.datavalue == "Select")) {
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "Cannot unassign person";
    } else if (Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.assignedTeam && (!Partial.Widgets.assignedTeamSelect.datavalue || Partial.Widgets.assignedTeamSelect.datavalue == "Select")) {
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "Cannot unassign team";
    } else {
        if (originalAgentId != selectedAgentId) {
            //  Partial.Variables.newlyAssignedPerson.dataset = Partial.Widgets.assignedPersonSelect.displayValue;
            Partial.Variables.newlyAssignedPerson.dataset = Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.assignedAgentId;
            Partial.Variables.updatedAssignedPerson.dataset = Partial.Widgets.assignedPersonSelect.datavalue;
            Partial.Variables.getStatusIfAssignedPersonChanged.dataset = updateStatus;
            Partial.Variables.updateSelectedBans.dataset = Partial.Variables.BanListRefIds.dataSet;
            Partial.Variables.updatePriority.dataset = Partial.Widgets.prioritySelect.datavalue;
            Partial.Variables.updateAssignedTeam.dataset = Partial.Widgets.assignedTeamSelect.datavalue;
            Partial.Variables.updateDueDateOrStepDate.dataset = Partial.Widgets.dueDate.datavalue;
            Partial.Variables.selectedOrderMgmtId.dataset = Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.id;
            Partial.Variables.selectedOrderMgmtPartitionKey.dataset = Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.partitionKey;
            Partial.Variables.stepTypeCodeForOrderMgmt.dataset = stepTypeCode;

            var payload = {
                'id': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.id,
                'partitionKey': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.partitionKey,
                "CollectionTreatmentStepUpdate": {
                    'stepTypeCode': stepTypeCode,
                    'status': updateStatus,
                    'priority': Partial.Widgets.prioritySelect.datavalue,
                    'comment': Partial.Widgets.AddComment.datavalue,
                    'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                    'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                    'channel': {
                        'originatorAppId': "FAWBTELUSAGENT",
                        'channelOrgId': "FAWBTELUSAGENT",
                        'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
                    }
                }
            };
            if (Partial.Variables.treatmentStepOriginalStepDate.dataset != Partial.Widgets.dueDate.datavalue) {
                payload.CollectionTreatmentStepUpdate = { ...payload.CollectionTreatmentStepUpdate,
                    stepDate: Partial.Widgets.dueDate.datavalue
                };
            }
            if (Partial.Variables.BanListRefIds.dataSet && Partial.Variables.BanListRefIds.dataSet.length > 0) {
                payload.CollectionTreatmentStepUpdate = { ...payload.CollectionTreatmentStepUpdate,
                    billingAccountRefs: Partial.Variables.BanListRefIds.dataSet
                };
            }
            Partial.Variables.UpdateODManagemntAndSendIfAssigendChanged.setInput(payload);
            Partial.Variables.UpdateODManagemntAndSendIfAssigendChanged.invoke();
            Partial.Widgets.EditNotSentdialog.close();
            Partial.Widgets.update_ActionDialog.open();
        } else {
            var payload = {
                'id': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.id,
                'partitionKey': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.partitionKey,
                "CollectionTreatmentStepUpdate": {
                    'stepTypeCode': stepTypeCode,
                    'status': updateStatus,
                    'priority': Partial.Widgets.prioritySelect.datavalue,
                    'comment': Partial.Widgets.AddComment.datavalue,
                    'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                    'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                    'channel': {
                        'originatorAppId': "FAWBTELUSAGENT",
                        'channelOrgId': "FAWBTELUSAGENT",
                        'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
                    }
                }
            };
            if (Partial.Variables.treatmentStepOriginalStepDate.dataset != Partial.Widgets.dueDate.datavalue) {
                payload.CollectionTreatmentStepUpdate = { ...payload.CollectionTreatmentStepUpdate,
                    stepDate: Partial.Widgets.dueDate.datavalue
                };
            }
            if (Partial.Variables.BanListRefIds.dataSet && Partial.Variables.BanListRefIds.dataSet.length > 0) {
                payload.CollectionTreatmentStepUpdate = { ...payload.CollectionTreatmentStepUpdate,
                    billingAccountRefs: Partial.Variables.BanListRefIds.dataSet
                };
            }
            Partial.Variables.UpdateODManagemntAndSend.setInput(payload);
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
    Partial.Variables.treatmentStepOriginalStepDate.dataset = Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.stepDate;
    var originalAgentId = Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.assignedPersonForDefaultValue;
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
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "Priority is mandatory";
    } else if (Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.assignedAgentId && (!Partial.Widgets.assignedPersonSelect.datavalue || Partial.Widgets.assignedPersonSelect.datavalue == "Select")) {
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "Cannot unassign person";
    } else if (Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.assignedTeam && (!Partial.Widgets.assignedTeamSelect.datavalue || Partial.Widgets.assignedTeamSelect.datavalue == "Select")) {
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "Cannot unassign team";
    } else {
        if (originalAgentId != selectedAgentId) {
            Partial.Variables.updatedAssignedPerson.dataset = Partial.Widgets.assignedPersonSelect.datavalue;
            //  Partial.Variables.newlyAssignedPerson.dataset = Partial.Widgets.assignedPersonSelect.displayValue;
            Partial.Variables.newlyAssignedPerson.dataset = Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.assignedAgentId
            Partial.Variables.getStatusIfAssignedPersonChanged.dataset = updateStatus;
            Partial.Variables.updateSelectedBans.dataset = Partial.Variables.BanListRefIds.dataSet;
            Partial.Variables.updatePriority.dataset = Partial.Widgets.prioritySelect.datavalue;
            Partial.Variables.updateAssignedTeam.dataset = Partial.Widgets.assignedTeamSelect.datavalue;
            Partial.Variables.updateDueDateOrStepDate.dataset = Partial.Widgets.dueDate.datavalue;
            Partial.Variables.selectedOrderMgmtId.dataset = Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.id;
            Partial.Variables.selectedOrderMgmtPartitionKey.dataset = Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.partitionKey;
            Partial.Variables.stepTypeCodeForOrderMgmt.dataset = stepTypeCode;
            var payload = {
                'id': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.id,
                'partitionKey': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.partitionKey,
                "CollectionTreatmentStepUpdate": {
                    'stepTypeCode': stepTypeCode,
                    'status': updateStatus,
                    'priority': Partial.Widgets.prioritySelect.datavalue,
                    'comment': Partial.Widgets.AddComment2.datavalue,
                    'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                    'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                    'channel': {
                        'originatorAppId': "FAWBTELUSAGENT",
                        'channelOrgId': "FAWBTELUSAGENT",
                        'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
                    }
                }
            };
            if (Partial.Variables.treatmentStepOriginalStepDate.dataset != Partial.Widgets.dueDate.datavalue) {
                payload.CollectionTreatmentStepUpdate = { ...payload.CollectionTreatmentStepUpdate,
                    stepDate: Partial.Widgets.dueDate.datavalue
                };
            }
            if (Partial.Variables.BanListRefIds.dataSet && Partial.Variables.BanListRefIds.dataSet.length > 0) {
                payload.CollectionTreatmentStepUpdate = { ...payload.CollectionTreatmentStepUpdate,
                    billingAccountRefs: Partial.Variables.BanListRefIds.dataSet
                };
            }
            Partial.Variables.UpdateODManagemntAndDonotFullfillIfAssignedChange.setInput(payload);
            Partial.Variables.UpdateODManagemntAndDonotFullfillIfAssignedChange.invoke();
            Partial.Widgets.EditAndFulfillSentdialog.close();
            Partial.Widgets.update_ActionDialog.open();
        } else {
            var payload = {
                'id': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.id,
                'partitionKey': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.partitionKey,
                "CollectionTreatmentStepUpdate": {
                    'stepTypeCode': stepTypeCode,
                    'status': updateStatus,
                    'priority': Partial.Widgets.prioritySelect.datavalue,
                    'comment': Partial.Widgets.AddComment2.datavalue,
                    'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                    'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                    'channel': {
                        'originatorAppId': "FAWBTELUSAGENT",
                        'channelOrgId': "FAWBTELUSAGENT",
                        'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
                    }
                }
            };
            if (Partial.Variables.treatmentStepOriginalStepDate.dataset != Partial.Widgets.dueDate.datavalue) {
                payload.CollectionTreatmentStepUpdate = { ...payload.CollectionTreatmentStepUpdate,
                    stepDate: Partial.Widgets.dueDate.datavalue
                };
            }
            if (Partial.Variables.BanListRefIds.dataSet && Partial.Variables.BanListRefIds.dataSet.length > 0) {
                payload.CollectionTreatmentStepUpdate = { ...payload.CollectionTreatmentStepUpdate,
                    billingAccountRefs: Partial.Variables.BanListRefIds.dataSet
                };
            }
            Partial.Variables.UpdateODManagemntAndDonotFullfill.setInput(payload);
            Partial.Variables.UpdateODManagemntAndDonotFullfill.invoke();
        }
    }
};

Partial.updateAndFulfilbuttonClick = function($event, widget) {
    debugger;
    var isAlreadySusOrRes = '';
    Partial.Variables.treatmentStepOriginalStepDate.dataset = Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.stepDate;
    var originalAgentId = Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.assignedPersonForDefaultValue;
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
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "Priority is mandatory";
    } else if (Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.assignedAgentId && (!Partial.Widgets.assignedPersonSelect.datavalue || Partial.Widgets.assignedPersonSelect.datavalue == "Select")) {
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "Cannot unassign person";
    } else if (Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.assignedTeam && (!Partial.Widgets.assignedTeamSelect.datavalue || Partial.Widgets.assignedTeamSelect.datavalue == "Select")) {
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "Cannot unassign team";
    } else if (originalAgentId == null && selectedAgentId == null || selectedAgentId == "") {
        Partial.Variables.popUperrorMsg.dataSet.dataValue = "Assigned Person must be selected to Fulfill Order";
    } else {
        if (originalAgentId != selectedAgentId) {
            //  Partial.Variables.newlyAssignedPerson.dataset = Partial.Widgets.assignedPersonSelect.displayValue;
            Partial.Variables.newlyAssignedPerson.dataset = Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.assignedAgentId
            Partial.Variables.updatedAssignedPerson.dataset = Partial.Widgets.assignedPersonSelect.datavalue;
            Partial.Variables.updateSelectedBans.dataset = Partial.Variables.BanListRefIds.dataSet;
            Partial.Variables.getStatusIfAssignedPersonChanged.dataset = 'Order Fulfilled';
            Partial.Variables.updatePriority.dataset = Partial.Widgets.prioritySelect.datavalue;
            Partial.Variables.updateAssignedTeam.dataset = Partial.Widgets.assignedTeamSelect.datavalue;
            Partial.Variables.updateDueDateOrStepDate.dataset = Partial.Widgets.dueDate.datavalue;
            Partial.Variables.selectedOrderMgmtId.dataset = Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.id;
            Partial.Variables.selectedOrderMgmtPartitionKey.dataset = Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.partitionKey;
            Partial.Variables.stepTypeCodeForOrderMgmt.dataset = stepTypeCode;
            var payload = {
                'id': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.id,
                'partitionKey': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.partitionKey,
                "CollectionTreatmentStepUpdate": {
                    'stepTypeCode': stepTypeCode,
                    //'status': 'Order Fulfilled',
                    'priority': Partial.Widgets.prioritySelect.datavalue,
                    'comment': Partial.Widgets.AddComment2.datavalue,
                    'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                    'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                    'channel': {
                        'originatorAppId': "FAWBTELUSAGENT",
                        'channelOrgId': "FAWBTELUSAGENT",
                        'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
                    }
                }
            };
            if (Partial.Variables.treatmentStepOriginalStepDate.dataset != Partial.Widgets.dueDate.datavalue) {
                payload.CollectionTreatmentStepUpdate = { ...payload.CollectionTreatmentStepUpdate,
                    stepDate: Partial.Widgets.dueDate.datavalue
                };
            }
            if (Partial.Variables.BanListRefIds.dataSet && Partial.Variables.BanListRefIds.dataSet.length > 0) {
                payload.CollectionTreatmentStepUpdate = { ...payload.CollectionTreatmentStepUpdate,
                    billingAccountRefs: Partial.Variables.BanListRefIds.dataSet
                };
            }
            Partial.Variables.UpdateODManagemntAndFullfillIfAssignedChange.setInput(payload);
            Partial.Variables.UpdateODManagemntAndFullfillIfAssignedChange.invoke();
            Partial.Widgets.EditAndFulfillSentdialog.close();
            Partial.Widgets.update_ActionDialog.open();
        } else {
            var payload = {
                'id': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.id,
                'partitionKey': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.partitionKey,
                "CollectionTreatmentStepUpdate": {
                    'stepTypeCode': stepTypeCode,
                    'status': 'Order Fulfilled',
                    'priority': Partial.Widgets.prioritySelect.datavalue,
                    'comment': Partial.Widgets.AddComment2.datavalue,
                    'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                    'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                    'channel': {
                        'originatorAppId': "FAWBTELUSAGENT",
                        'channelOrgId': "FAWBTELUSAGENT",
                        'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
                    }
                }
            };
            if (Partial.Variables.treatmentStepOriginalStepDate.dataset != Partial.Widgets.dueDate.datavalue) {
                payload.CollectionTreatmentStepUpdate = { ...payload.CollectionTreatmentStepUpdate,
                    stepDate: Partial.Widgets.dueDate.datavalue
                };
            }
            if (Partial.Variables.BanListRefIds.dataSet && Partial.Variables.BanListRefIds.dataSet.length > 0) {
                payload.CollectionTreatmentStepUpdate = { ...payload.CollectionTreatmentStepUpdate,
                    billingAccountRefs: Partial.Variables.BanListRefIds.dataSet
                };
            }
            Partial.Variables.UpdateODManagemntAndFullfill.setInput(payload);
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
    if (Partial.Widgets.closeComment.datavalue == "" || Partial.Widgets.closeComment.datavalue == undefined) {
        Partial.Widgets.assigned_closeActionDialog.close();
    } else {
        Partial.Variables.UpdateODManagemntAndCloseAction.setInput({
            'id': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.id,
            'partitionKey': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.partitionKey,
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
        Partial.Variables.UpdateODManagemntAndCloseAction.invoke();
    }
};

// cancle assigned person
Partial.assigned_cancleYesBtnClick = function($event, widget) {
    if (Partial.Widgets.cancelComment.datavalue == "" || Partial.Widgets.cancelComment.datavalue == undefined) {
        Partial.Widgets.assigned_cancleActionDialog.close();
    } else {
        Partial.Variables.UpdateODManagemntAndCancelledActiion.setInput({
            'id': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.id,
            'partitionKey': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.partitionKey,
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
        Partial.Variables.UpdateODManagemntAndCancelledActiion.invoke();
    }
};

Partial.assigned_cancleNoBtnClick = function($event, widget) {
    Partial.Widgets.assigned_cancleActionDialog.close();
};

Partial.update_YesBtnClick = function($event, widget) {
    debugger;
    if (Partial.Widgets.Comment.datavalue == "" || Partial.Widgets.Comment.datavalue == undefined) {
        Partial.Widgets.update_ActionDialog.close();
    } else {
        var payload = {
            'id': Partial.Variables.selectedOrderMgmtId.dataset,
            'partitionKey': Partial.Variables.selectedOrderMgmtPartitionKey.dataset,
            "CollectionTreatmentStepUpdate": {
                'stepTypeCode': Partial.Variables.stepTypeCodeForOrderMgmt.dataset,
                'status': Partial.Variables.getStatusIfAssignedPersonChanged.dataset,
                'priority': Partial.Variables.updatePriority.dataset,
                'comment': Partial.Widgets.Comment.datavalue,
                'assignedAgentId': Partial.Variables.updatedAssignedPerson.dataset,
                'assignedTeam': Partial.Variables.updateAssignedTeam.dataset,
                'channel': {
                    'originatorAppId': "FAWBTELUSAGENT",
                    'channelOrgId': "FAWBTELUSAGENT",
                    'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
                }
            }
        };
        if (Partial.Variables.treatmentStepOriginalStepDate.dataset != Partial.Variables.updateDueDateOrStepDate.dataset) {
            payload.CollectionTreatmentStepUpdate = { ...payload.CollectionTreatmentStepUpdate,
                stepDate: Partial.Variables.updateDueDateOrStepDate.dataset
            };
        }
        if (Partial.Variables.updateSelectedBans.dataset && Partial.Variables.updateSelectedBans.dataset.length > 0) {
            payload.CollectionTreatmentStepUpdate = { ...payload.CollectionTreatmentStepUpdate,
                billingAccountRefs: Partial.Variables.updateSelectedBans.dataset
            };
        }
        Partial.Variables.UpdateODManagemntWhenAssignChange.setInput(payload);
        //Invoke POST createDispute service
        Partial.Variables.UpdateODManagemntWhenAssignChange.invoke();
        if (payload.CollectionTreatmentStepUpdate.status == 'Order Fulfilled') {
            intervalRefreshCollOrderMgmtList(5000, 15000);
        }
    }
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
    debugger;
    if (Partial.Variables.newlyAssignedPerson.dataset == undefined) {
        Partial.Widgets.label61.caption = 'This action ' + Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.stepTypeCode + " (" + Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.id + ')' + ' has not been assigned to a person.'
    } else {
        Partial.Widgets.label61.caption = 'This action ' + Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.stepTypeCode + " (" + Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.id + ')' + ' has been assigned to ' + Partial.Variables.newlyAssignedPerson.dataset + "  (" + Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.assignedPersonForDefaultValue + ')' + ' who may be working on it.'
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

Partial.CollectionDataServiceGetEntityBanDetailsonError = function(variable, data, xhrObj) {};

Partial.getCollectionTreatmentStep_orderMngtonError = function(variable, data, xhrObj) {};

Partial.getCollectionTreatMentonError = function(variable, data, xhrObj) {};

App.refreshCollOrderMgmtList = function() {
    Partial.Variables.getCollectionTreatmentStep_orderMngt.setInput({
        'limit': 10,
        'offset': 0
    });
    Partial.Variables.getCollectionTreatmentStep_orderMngt.invoke();
};

var getOrderMgmtIntervalObj = null;

function intervalRefreshCollOrderMgmtList(interval, timeout) {
    function setIntervalObj() {
        const currentTime = new Date().getTime();
        const intervalId = setInterval(() => {
            App.refreshCollOrderMgmtList();
        }, interval);
        getOrderMgmtIntervalObj = {
            id: intervalId,
            timestamp: currentTime
        };
        const timeoutId = setTimeout(() => {
            clearInterval(getOrderMgmtIntervalObj.id);
            getOrderMgmtIntervalObj = null;
        }, timeout);
        getOrderMgmtIntervalObj.timeoutId = timeoutId;
    }
    if (!getOrderMgmtIntervalObj) {
        setIntervalObj();
    } else {
        const currentTime = new Date().getTime();
        const diff = currentTime - getOrderMgmtIntervalObj.timestamp;
        if (diff > (timeout / 2)) {
            clearTimeout(getOrderMgmtIntervalObj.timeoutId);
            clearInterval(getOrderMgmtIntervalObj.id);
            setIntervalObj();
        }
    }
}

Partial.getOrderdMgmtHistoryonError = function(variable, data, xhrObj) {};

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
        // Partial.Variables.getCollectionTreatmentStep_orderMngt.invoke();
        App.refreshCollOrderMgmtList();
    }, 1000);
};

Partial.createOrderManagmentForRestoralonSuccess = function(variable, data) {
    Partial.Widgets.CreateRestoralRequestdialog.close();
    App.Variables.successMessage.dataSet.dataValue = "Action Restored successfully.";
    setTimeout(messageTimeout, 4000);
    setTimeout(function() {
        //   Partial.Variables.getCollectionTreatmentStep_orderMngt.invoke();
        App.refreshCollOrderMgmtList();
    }, 1000);
};

Partial.createOrderManagmentForCeaseonSuccess = function(variable, data) {
    Partial.Widgets.CreateCeaseRequestdialog.close();
    App.Variables.successMessage.dataSet.dataValue = "Action Ceased successfully.";
    setTimeout(messageTimeout, 4000);
    setTimeout(function() {
        //   Partial.Variables.getCollectionTreatmentStep_orderMngt.invoke();
        App.refreshCollOrderMgmtList();
    }, 1000);
};

Partial.UpdateODManagemntAndDonotSendonSuccess = function(variable, data) {
    Partial.Widgets.EditNotSentdialog.close();
    App.Variables.successMessage.dataSet.dataValue = "Updated Without Sent To Fulfillment successfully.";
    setTimeout(messageTimeout, 5000);
    App.refreshCollOrderMgmtList();
};

Partial.UpdateODManagemntAndSendonSuccess = function(variable, data) {
    Partial.Widgets.EditNotSentdialog.close();
    App.Variables.successMessage.dataSet.dataValue = " Updated And Sent successfully.";
    setTimeout(messageTimeout, 5000);
    App.refreshCollOrderMgmtList();
};

Partial.UpdateODManagemntAndDonotFullfillonSuccess = function(variable, data) {
    Partial.Widgets.EditAndFulfillSentdialog.close();
    App.Variables.successMessage.dataSet.dataValue = "Updated Without Sent to Fulfilment successfully.";
    setTimeout(messageTimeout, 5000);
    App.refreshCollOrderMgmtList();
};

Partial.UpdateODManagemntAndFullfillonSuccess = function(variable, data) {
    Partial.Widgets.EditAndFulfillSentdialog.close();
    App.Variables.successMessage.dataSet.dataValue = " Updated And Fullfill successfully";
    setTimeout(messageTimeout, 5000);
    App.refreshCollOrderMgmtList();
    intervalRefreshCollOrderMgmtList(5000, 15000);
};

Partial.UpdateODManagemntAndCloseActiononSuccess = function(variable, data) {
    Partial.Widgets.assigned_closeActionDialog.close();
    App.Variables.successMessage.dataSet.dataValue = " Action Closed successfully";
    setTimeout(messageTimeout, 5000);
    App.refreshCollOrderMgmtList();
};

Partial.UpdateODManagemntAndCancelledActiiononSuccess = function(variable, data) {
    Partial.Widgets.assigned_cancleActionDialog.close();
    App.Variables.successMessage.dataSet.dataValue = " Action Cancelled successfully";
    setTimeout(messageTimeout, 5000);
    App.refreshCollOrderMgmtList();
};

Partial.UpdateODManagemntWhenAssignChangeonSuccess = function(variable, data) {
    Partial.Widgets.update_ActionDialog.close();
    App.Variables.successMessage.dataSet.dataValue = " Action Updated Successfully";
    setTimeout(messageTimeout, 5000);
    App.refreshCollOrderMgmtList();
};

Partial.getOrderdMgmtHistoryonSuccess = function(variable, data) {};

Partial.getOrderdMgmtHistoryonBeforeDatasetReady = function(variable, data) {};

function getBanListForAutoSelect(row) {
    Partial.Variables.getBanListFromHisForPreselect.setInput({
        'collectionEntityId': Partial.pageParams.entityId,
        'relatedBusinessEntityId': row.id,
        'relatedBusinessEntityType': 'CollectionTreatmentStep'
    });
    Partial.Variables.getBanListFromHisForPreselect.invoke();
}

Partial.getBanListFromHisForPreselectonSuccess = function(variable, data) {
    var bans = data[0].banList;
    if (data[0].banList.length > 0) {
        var banList1 = $('#getEntityBanDetailsTableEdit1ForTest  tbody tr');
        for (let j = 0; j < bans.length; j++) {

            for (let i = 0; i < banList1.length; i++) {
                if (bans[j].toString() === banList1[i].cells[1].innerHTML) {
                    var cb = banList1.find("input[type='checkbox']").eq(i).attr('checked', 'checked')
                    cb.trigger('click');
                }
            }
        }
    }
    if (data[0].banList.length > 0) {
        var banList2 = $('#getEntityBanDetailsTableEdit2ForTest  tbody tr');
        for (let j = 0; j < bans.length; j++) {
            for (let i = 0; i < banList2.length; i++) {
                if (bans[j].toString() === banList2[i].cells[1].innerHTML) {
                    var cb = banList2.find("input[type='checkbox']").eq(i).attr('checked', 'checked')
                    cb.trigger('click');
                }
            }
        }
    }
};

Partial.UpdateODManagemntAndDonotFullfillonError = function(variable, data, xhrObj) {
    if (data === 'Unexpected error 400 Bad Request: "{"Error":"At least one valid field with changed values, excluding the mandatory fields (id, collectionTreatment, and channel), is required."}",please check server logs for more information') {
        Partial.Variables.popUperrorMsg.dataSet.dataValue = 'At least one valid field with changed values, excluding the mandatory fields (id, collectionTreatment, and channel), is required.';
    }
    setTimeout(messageTimeout, 8000);
};

Partial.UpdateODManagemntAndSendonError = function(variable, data, xhrObj) {
    if (data === 'Unexpected error 400 Bad Request: "{"Error":"At least one valid field with changed values, excluding the mandatory fields (id, collectionTreatment, and channel), is required."}",please check server logs for more information') {
        Partial.Variables.popUperrorMsg.dataSet.dataValue = 'At least one valid field with changed values, excluding the mandatory fields (id, collectionTreatment, and channel), is required.';
    }
    setTimeout(messageTimeout, 8000);
};

Partial.UpdateODManagemntAndDonotSendonError = function(variable, data, xhrObj) {
    if (data === 'Unexpected error 400 Bad Request: "{"Error":"At least one valid field with changed values, excluding the mandatory fields (id, collectionTreatment, and channel), is required."}",please check server logs for more information') {
        Partial.Variables.popUperrorMsg.dataSet.dataValue = 'At least one valid field with changed values, excluding the mandatory fields (id, collectionTreatment, and channel), is required.';
    }
    setTimeout(messageTimeout, 8000);
};

Partial.UpdateODManagemntAndDonotSendIfAssignedChangedonSuccess = function(variable, data) {
    Partial.Widgets.EditNotSentdialog.close();
    setTimeout(messageTimeout, 5000);
    App.refreshCollOrderMgmtList();
};

Partial.UpdateODManagemntAndSendIfAssigendChangedonSuccess = function(variable, data) {
    Partial.Widgets.EditNotSentdialog.close();
    setTimeout(messageTimeout, 5000);
    App.refreshCollOrderMgmtList();
};

Partial.UpdateODManagemntAndDonotFullfillIfAssignedChangeonSuccess = function(variable, data) {
    Partial.Widgets.EditAndFulfillSentdialog.close();
    setTimeout(messageTimeout, 5000);
    App.refreshCollOrderMgmtList();
};

Partial.UpdateODManagemntAndFullfillIfAssignedChangeonSuccess = function(variable, data) {
    Partial.Widgets.EditAndFulfillSentdialog.close();
    setTimeout(messageTimeout, 5000);
    App.refreshCollOrderMgmtList();
};

Partial.Telus_PaginatonPagechangeForOD = function($event, $data) {
    debugger;
    Partial.size = $event.pageSize
    Partial.page = $event.pageNumber
    Partial.RefreshData();
};

Partial.getCollectionTreatmentStep_orderMngtTable1Beforedatarender = function(widget, $data, $columns) {
    var sortColumn;
    var sortDirection;
    try {
        sortColumn = widget.sortInfo.field; // Get the currently sorted column
        sortDirection = widget.sortInfo.direction; // Get the sort direction (ascending or descending)
    } catch (error) {
        sortColumn = 'id';
        sortDirection = 'desc'
    }
    // Sort the data based on the sortColumn and sortDirection
    if (sortColumn === null) {
        // Sort by "id" column when the page loads for the first time
        $data.sort((a, b) => a.id - b.id);
    } else {
        // Sort based on the clicked column
        $data.sort(function(a, b) {
            var valueA = a[sortColumn];
            var valueB = b[sortColumn];
            // Handle null or empty values
            if (valueA === null || valueA === '') {
                if (valueB === null || valueB === '') {
                    return 0; // Both values are null or empty, no change in order
                }
                return sortDirection === 'asc' ? -1 : 1; // Move null or empty values to the beginning or end based on sortDirection
            }
            if (valueB === null || valueB === '') {
                return sortDirection === 'asc' ? 1 : -1; // Move null or empty values to the beginning or end based on sortDirection
            }
            if (sortDirection === 'asc') {
                if (valueA < valueB) return -1;
                if (valueA > valueB) return 1;
            } else {
                if (valueA > valueB) return -1;
                if (valueA < valueB) return 1;
            }
            return 0;
        });
    }
    //$data.sort((a, b) => b.id - a.id); // Sort the data based on the id property
};

Partial.RefreshData = function() {
    debugger;
    var offset = Partial.size * (Partial.page - 1);
    Partial.Variables.getCollectionTreatmentStep_orderMngt.setInput({
        'limit': Partial.size,
        'offset': offset
    });
    Partial.Variables.getCollectionTreatmentStep_orderMngt.invoke();
}

Partial.createRequest_ButtonClick = function($event, widget) {
    Partial.Widgets.OrderPopOver.showPopover();
};