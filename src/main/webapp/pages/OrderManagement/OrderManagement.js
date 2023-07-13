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

    App.Variables.errorMsg.dataSet.dataValue = '';

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
    if (Partial.Variables.getCollectionTreatMent.dataSet.length == 0) {
        App.Variables.errorMsg.dataSet.dataValue = "You cannot create an order for this entity. Entity is not yet in collection treatment.";
        setTimeout(messageTimeout, 5000);
    } else {
        Partial.Widgets.OrderPopOver.hidePopover();
        Partial.Variables.errorMsg.dataSet.dataValue = "";

        Partial.Variables.UserLoggedInVar.dataSet.dataValue = App.Variables.getLoggedInUserDetails.dataSet.emplId;
        Partial.Variables.getLoggedInUserTeamIdVar.setInput({
            'userId': App.Variables.getLoggedInUserId.dataSet[0].id
        });
        Partial.Variables.getLoggedInUserTeamIdVar.invoke();

        Partial.Widgets.CreateSuspensionRequestdialog.open();
    }
};


Partial.CreateRestoralRequestClick = function($event, widget) {
    if (Partial.Variables.getCollectionTreatMent.dataSet.length == 0) {
        App.Variables.errorMsg.dataSet.dataValue = "You cannot create an order for this entity. Entity is not yet in collection treatment.";
        setTimeout(messageTimeout, 5000);
    } else {
        Partial.Widgets.OrderPopOver.hidePopover();
        getBanDetails();
        Partial.Variables.errorMsg.dataSet.dataValue = "";
        Partial.Variables.UserLoggedInVar.dataSet.dataValue = App.Variables.getLoggedInUserDetails.dataSet.emplId;
        Partial.Variables.getLoggedInUserTeamIdVar.setInput({
            'userId': App.Variables.getLoggedInUserId.dataSet[0].id
        });
        Partial.Variables.getLoggedInUserTeamIdVar.invoke();


        Partial.Widgets.CreateRestoralRequestdialog.open();
    }
};
Partial.CreateCeaseRequestClick = function($event, widget) {
    if (Partial.Variables.getCollectionTreatMent.dataSet.length == 0) {
        App.Variables.errorMsg.dataSet.dataValue = "You cannot create an order for this entity. Entity is not yet in collection treatment.";
        setTimeout(messageTimeout, 5000);
    } else {
        Partial.Widgets.OrderPopOver.hidePopover();
        getBanDetails();
        Partial.Variables.errorMsg.dataSet.dataValue = "";
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

    if (Partial.Widgets.assignedPersonSelect.datavalue == "" || Partial.Widgets.assignedPersonSelect.datavalue == "Select") {
        isAssignedPerson = "Request Created";
    } else {
        isAssignedPerson = "Request Assigned";
    }


    if (Partial.Widgets.susReasonCode.datavalue == "" || Partial.Widgets.susReasonCode.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Reason code is mandatory";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
    } else {
        // API Call will come here

        Partial.Variables.createOrderManagment.setInput({
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
                    'id': Partial.Variables.getCollectionTreatMent.dataSet[0].id
                },
                'channel': {
                    'originatorAppId': "FAWBTELUSAGENT",
                    'channelOrgId': "FAWBTELUSAGENT",
                    'userId': Partial.Variables.getLoggedInUserDetails.dataSet.emplId
                },
            },
        });
        Partial.Variables.createOrderManagment.invoke();

        Partial.Widgets.CreateSuspensionRequestdialog.close();
        App.Variables.successMessage.dataSet.dataValue = "Action Suspended successfully.";

        setTimeout(messageTimeout, 4000);

        setTimeout(function() {
            Partial.Variables.getCollectionTreatmentStep_orderMngt.invoke();
        }, 1000);


    }


    // Partial.Variables.getCollectionTreatmentStep_orderMngt.invoke();


};
Partial.createbuttonRestoralClick = function($event, widget) {

    debugger;

    var isAssignedPerson = '';

    if (Partial.Widgets.assignedPersonSelect.datavalue == "" || Partial.Widgets.assignedPersonSelect.datavalue == "Select") {
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


    if (Partial.Widgets.restoralReasonCode.datavalue == "" || Partial.Widgets.restoralReasonCode.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Reason code is mandatory";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
    } else {
        // API Call will come here

        Partial.Variables.createOrderManagment.setInput({
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
                    'id': Partial.Variables.getCollectionTreatMent.dataSet[0].id
                },
                'channel': {
                    'originatorAppId': "FAWBTELUSAGENT",
                    'channelOrgId': "FAWBTELUSAGENT",
                    'userId': Partial.Variables.getLoggedInUserDetails.dataSet.emplId
                },
                'billingAccountIdRefs': Partial.Variables.BanListRefIds.dataSet,
            },
        });
        Partial.Variables.createOrderManagment.invoke();

        Partial.Widgets.CreateRestoralRequestdialog.close();
        App.Variables.successMessage.dataSet.dataValue = "Action Restored successfully.";

        setTimeout(messageTimeout, 4000);

        setTimeout(function() {
            Partial.Variables.getCollectionTreatmentStep_orderMngt.invoke();
        }, 1000);

    }

    // Partial.Variables.getCollectionTreatmentStep_orderMngt.invoke();


};
Partial.createbuttonCeaseClick = function($event, widget) {

    debugger;

    var isAssignedPerson = '';

    if (Partial.Widgets.assignedPersonSelect.datavalue == "" || Partial.Widgets.assignedPersonSelect.datavalue == "Select") {
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
        App.Variables.errorMsg.dataSet.dataValue = "Reason code is mandatory";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
    } else {
        // API Call will come here

        Partial.Variables.createOrderManagment.setInput({
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
                    'id': Partial.Variables.getCollectionTreatMent.dataSet[0].id
                },
                'channel': {
                    'originatorAppId': "FAWBTELUSAGENT",
                    'channelOrgId': "FAWBTELUSAGENT",
                    'userId': Partial.Variables.getLoggedInUserDetails.dataSet.emplId
                },
                'billingAccountIdRefs': Partial.Variables.BanListRefIds.dataSet,
            },
        });
        Partial.Variables.createOrderManagment.invoke();
        Partial.Widgets.CreateCeaseRequestdialog.close();
        App.Variables.successMessage.dataSet.dataValue = "Action Ceased successfully.";

        setTimeout(messageTimeout, 4000);

        setTimeout(function() {
            Partial.Variables.getCollectionTreatmentStep_orderMngt.invoke();
        }, 1000);


    }


};
Partial.getCollectionTreatmentStep_orderMngt_customRow1Action = function($event, row) {

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


    Partial.Variables.newlyAssignedPerson.dataset = '';
    App.Variables.errorMsg.dataSet.dataValue = null;
    var originalAgentId = Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.assignedAgentId;
    var selectedAgentId = Partial.Widgets.assignedPersonSelect.datavalue;
    if (originalAgentId != selectedAgentId) {

        Partial.Variables.newlyAssignedPerson.dataset = Partial.Widgets.assignedPersonSelect.displayValue;



        Partial.Widgets.EditNotSentdialog.close();

        Partial.Widgets.update_ActionDialog.open();

    } else {


        var stepTypeCode;
        if (Partial.Widgets.EditNotSentdialog.title == "Edit Suspension Request") {
            stepTypeCode = "SUSPEND";
        } else if (Partial.Widgets.EditNotSentdialog.title == "Edit Restoral Request") {
            stepTypeCode = "RESTORE";
        } else if (Partial.Widgets.EditNotSentdialog.title == "Edit Cease Request") {
            stepTypeCode = "CEASE";
        }

        // var actionIdLabel = Partial.Widgets.EditActionIdText.caption;
        var updateStatus = '';


        Partial.Variables.BanListRefIds.dataSet = [];

        Partial.Widgets.getEntityBanDetailsTable1.selectedItems;
        Partial.selectedBanList = [];
        Partial.Widgets.getEntityBanDetailsTable1.selectedItems.forEach(function(d) {

            if (stepTypeCode == "SUSPEND" && d.banStatus == "SUSPEND") {
                App.Variables.errorMsg.dataSet.dataValue = "Ban Id " + d.banMapRefId + " Ban name " + d.banName + " is already Suspended."
            } else if (stepTypeCode == "RESTORE" && d.banStatus == "RESTORE") {

                App.Variables.errorMsg.dataSet.dataValue = "Ban Id " + d.banMapRefId + " Ban name " + d.banName + " is already Restored."


            } else {

                Partial.selectedBanList = {
                    "id": d.banRefId,

                }
            }
            Partial.Variables.BanListRefIds.dataSet.push(Partial.selectedBanList);

        });

        if (!Partial.Widgets.assignedPersonSelect.datavalue == "" || !Partial.Widgets.assignedPersonSelect.datavalue == "Select") {
            updateStatus = "Request Assigned";
        } else {
            updateStatus = Partial.Widgets.Status_NotSent.datavalue;
        }

        if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined) {
            App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
        } else {
            Partial.Variables.UpdateODManagemntVar.setInput({
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
            Partial.Variables.UpdateODManagemntVar.invoke();
            Partial.Widgets.EditNotSentdialog.close();

            App.Variables.successMessage.dataSet.dataValue = "Updated Without Sent To Fulfillment successfully.";
            setTimeout(messageTimeout, 3000);

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

    var originalAgentId = Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.assignedAgentId;
    var selectedAgentId = Partial.Widgets.assignedPersonSelect.datavalue;
    if (originalAgentId != selectedAgentId) {

        Partial.Variables.newlyAssignedPerson.dataset = Partial.Widgets.assignedPersonSelect.displayValue;

        Partial.Widgets.EditNotSentdialog.close();
        Partial.Widgets.update_ActionDialog.open();


    } else {
        var stepTypeCode;
        if (Partial.Widgets.EditNotSentdialog.title == "Edit Suspension Request") {
            stepTypeCode = "SUSPEND";
        } else if (Partial.Widgets.EditNotSentdialog.title == "Edit Restoral Request") {
            stepTypeCode = "RESTORE";
        } else if (Partial.Widgets.EditNotSentdialog.title == "Edit Cease Request") {
            stepTypeCode = "CEASE";
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

        if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined) {
            App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
        } else {
            Partial.Variables.UpdateODManagemntVar.setInput({
                'id': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.id,
                'partitionKey': getCurrentDate(),
                "CollectionTreatmentStepUpdate": {
                    'stepTypeCode': stepTypeCode,
                    'status': 'Order Created',
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
            Partial.Variables.UpdateODManagemntVar.invoke();

            Partial.Widgets.EditNotSentdialog.close();

            App.Variables.successMessage.dataSet.dataValue = " Updated And Sent successfully.";
            setTimeout(messageTimeout, 3000);
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

    Partial.Variables.newlyAssignedPerson.dataset = '';
    var originalAgentId = Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.assignedAgentId;
    var selectedAgentId = Partial.Widgets.assignedPersonSelect.datavalue;
    if (originalAgentId != selectedAgentId) {

        Partial.Variables.newlyAssignedPerson.dataset = Partial.Widgets.assignedPersonSelect.displayValue;


        Partial.Widgets.EditAndFulfillSentdialog.close();
        Partial.Widgets.update_ActionDialog.open();

    } else {
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

            Partial.selectedBanList = {
                "id": d.banRefId,

            }
            Partial.Variables.BanListRefIds.dataSet.push(Partial.selectedBanList);

        });



        var updateStatus = '';

        if (!Partial.Widgets.assignedPersonSelect.datavalue == "" || !Partial.Widgets.assignedPersonSelect.datavalue == "Select") {
            updateStatus = "Request Assigned";
        } else {
            updateStatus = Partial.Widgets.Status_Sent.caption;
        }

        if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined) {
            App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
        } else {
            Partial.Variables.UpdateODManagemntVar.setInput({
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
            Partial.Variables.UpdateODManagemntVar.invoke();

            Partial.Widgets.EditAndFulfillSentdialog.close();

            App.Variables.successMessage.dataSet.dataValue = "Updated Without Sent to Fulfilment successfully.";
            setTimeout(messageTimeout, 3000);
        }
    }
};

Partial.updateAndFulfilbuttonClick = function($event, widget) {


    var originalAgentId = Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.assignedAgentId;
    var selectedAgentId = Partial.Widgets.assignedPersonSelect.datavalue;
    if (originalAgentId != selectedAgentId) {

        Partial.Variables.newlyAssignedPerson.dataset = Partial.Widgets.assignedPersonSelect.displayValue;


        Partial.Widgets.EditAndFulfillSentdialog.close();
        Partial.Widgets.update_ActionDialog.open();

    } else {

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

            Partial.selectedBanList = {
                "id": d.banRefId,

            }
            Partial.Variables.BanListRefIds.dataSet.push(Partial.selectedBanList);

        });

        if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined) {
            App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
        } else {
            Partial.Variables.UpdateODManagemntVar.setInput({
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
            Partial.Variables.UpdateODManagemntVar.invoke();

            Partial.Widgets.EditAndFulfillSentdialog.close();

            App.Variables.successMessage.dataSet.dataValue = " Updated And Fullfill successfully";
            setTimeout(messageTimeout, 3000);
        }
    }
};

function messageTimeout() {
    Partial.Variables.successMessage.dataSet.dataValue = null;
    Partial.Variables.errorMsg.dataSet.dataValue = null;
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

    Partial.Variables.UpdateODManagemntVar.setInput({
        'id': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.id,
        'partitionKey': getCurrentDate(),
        "CollectionTreatmentStepUpdate": {
            'status': 'Close',
            'comment': Partial.Widgets.closeComment.datavalue,
            'channel': {
                'originatorAppId': "FAWBTELUSAGENT",
                'channelOrgId': "FAWBTELUSAGENT",
                'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
            },
        }
    });

    //Invoke POST createDispute service
    Partial.Variables.UpdateODManagemntVar.invoke();


    Partial.Widgets.assigned_closeActionDialog.close();
    App.Variables.successMessage.dataSet.dataValue = " Action Closed successfully";
    setTimeout(messageTimeout, 3000);
};

// cancle assigned person
Partial.assigned_cancleYesBtnClick = function($event, widget) {


    Partial.Variables.UpdateODManagemntVar.setInput({
        'id': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.id,
        'partitionKey': getCurrentDate(),
        "CollectionTreatmentStepUpdate": {
            'status': 'Cancel',
            'comment': Partial.Widgets.cancelComment.datavalue,
            'channel': {
                'originatorAppId': "FAWBTELUSAGENT",
                'channelOrgId': "FAWBTELUSAGENT",
                'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
            },
        }
    });

    //Invoke POST createDispute service
    Partial.Variables.UpdateODManagemntVar.invoke();

    Partial.Widgets.assigned_cancleActionDialog.close();
    App.Variables.successMessage.dataSet.dataValue = " Action Cancelled successfully";
    setTimeout(messageTimeout, 3000);
};
Partial.assigned_cancleNoBtnClick = function($event, widget) {
    Partial.Widgets.assigned_cancleActionDialog.close();
};

// for Update 
Partial.update_YesBtnClick = function($event, widget) {

    //  Partial.Widgets.EditNotSentdialog.close();

    Partial.Widgets.update_ActionDialog.close();

    //Partial.Variables.getCollectionTreatmentStep_orderMngt.invoke();
    // Partial.Widgets.EditNotSentdialog.close();
    App.Variables.successMessage.dataSet.dataValue = " Action Updated Successfully";
    setTimeout(messageTimeout, 3000);




};

Partial.update_NoBtnClick = function($event, widget) {

    Partial.Widgets.update_ActionDialog.close();

    Partial.Widgets.EditNotSentdialog.open();


};
Partial.EditNotSentdialogOpened = function($event, widget) {




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

    Partial.Widgets.label61.caption = 'This action ' + Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.stepTypeCode + " (" + Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.id + ')' + ' has been assigned to ' + Partial.Variables.newlyAssignedPerson.dataset + "  (" + Partial.Widgets.assignedPersonSelect.datavalue + ')' + ' who may be working on it.'

};

Partial.getCollectionTreatmentStep_orderMngt_customRowAction = function($event, row) {


    var entityIdStr = Partial.pageParams.entityId
    var entityIdInt = parseInt(entityIdStr);
    Partial.Variables.getODMgmtHistory.setInput({
        'collectionEntityId': entityIdInt,
        'id': row.id

    });

    Partial.Variables.getODMgmtHistory.invoke();
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