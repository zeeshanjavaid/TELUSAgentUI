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
    Partial.Variables.UserLoggedInVar.dataSet.dataValue = App.Variables.getLoggedInUserDetails.dataSet.emplId;

};

Partial.getCollectionTreatmentStep_orderMngt_OnRowexpand = function($event, widget, row, $data) {
    debugger;
    App.showRowExpansionOrderManagement(row, $data);
};

Partial.CreateSuspentionRequestClick = function($event, widget) {
    Partial.Widgets.OrderPopOver.hidePopover();
    Partial.Variables.errorMsg.dataSet.dataValue = "";
    Partial.Variables.UserLoggedInVar.dataSet.dataValue = App.Variables.getLoggedInUserDetails.dataSet.emplId;

    Partial.Widgets.CreateSuspensionRequestdialog.open();
};


Partial.CreateRestoralRequestClick = function($event, widget) {
    Partial.Widgets.OrderPopOver.hidePopover();
    Partial.Variables.errorMsg.dataSet.dataValue = "";
    Partial.Variables.UserLoggedInVar.dataSet.dataValue = App.Variables.getLoggedInUserDetails.dataSet.emplId;

    Partial.Widgets.CreateRestoralRequestdialog.open();
};
Partial.CreateCeaseRequestClick = function($event, widget) {
    Partial.Widgets.OrderPopOver.hidePopover();
    Partial.Variables.errorMsg.dataSet.dataValue = "";
    Partial.Variables.UserLoggedInVar.dataSet.dataValue = App.Variables.getLoggedInUserDetails.dataSet.emplId;
    Partial.Widgets.CreateCeaseRequestdialog.open();
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
                'collectionTreatment': {
                    'id': 0
                },
                'channel': {
                    'channelOrgId': "string",
                    'userId': Partial.Variables.loggedInUser.dataSet.id
                },
            },
        });
        Partial.Variables.createOrderManagment.invoke();

        Partial.Widgets.CreateSuspensionRequestdialog.close();
        App.Variables.successMessage.dataSet.dataValue = "Action Suspended Successfully.";

        setTimeout(messageTimeout, 4000);


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
            "id": d.banMapRefId,

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
                'collectionTreatment': {
                    'id': 0
                },
                'channel': {
                    'channelOrgId': "string",
                    'userId': Partial.Variables.loggedInUser.dataSet.id
                },
                'billingAccountIdRefs': Partial.Variables.BanListRefIds.dataSet,
            },
        });
        Partial.Variables.createOrderManagment.invoke();

        Partial.Widgets.CreateRestoralRequestdialog.close();
        App.Variables.successMessage.dataSet.dataValue = "Action Restored Successfully.";

        setTimeout(messageTimeout, 4000);

    }

    // Partial.Variables.getCollectionTreatmentStep_orderMngt.invoke();


};
Partial.createbuttonCeaseClick = function($event, widget) {

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
            "id": d.banMapRefId,

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
                'collectionTreatment': {
                    'id': 0
                },
                'channel': {
                    'channelOrgId': "string",
                    'userId': Partial.Variables.loggedInUser.dataSet.id
                },
                'billingAccountIdRefs': Partial.Variables.BanListRefIds.dataSet,
            },
        });
        Partial.Variables.createOrderManagment.invoke();
        Partial.Widgets.CreateCeaseRequestdialog.close();
        App.Variables.successMessage.dataSet.dataValue = "Action Ceased Successfully.";

        setTimeout(messageTimeout, 4000);


    }

    // Partial.Variables.getCollectionTreatmentStep_orderMngt.invoke();


};
Partial.getCollectionTreatmentStep_orderMngt_customRow1Action = function($event, row) {
    debugger;
    if (row.stepTypeCode == 'SUSPEND') {
        if (row.status == 'Request Assigned' || row.status == 'Request Created') {
            debugger;
            Partial.Widgets.EditNotSentdialog.title = "Edit Suspension Request";
            Partial.Widgets.EditNotSentdialog.open();
            Partial.Widgets.label59.caption = "zoozozozozoz";
            Partial.Widgets.BansHeading = "BANS";
            /*  $('#bansSuspend').show();
              $('#bansRestore').hide();
              $('#bansCease').hide();*/
        } else if (row.status == 'Order Assigned' || row.status == 'Order Created') {
            Partial.Widgets.EditAndFulfillSentdialog.title = "Edit and Fulfill Service Suspension";
            Partial.Widgets.EditAndFulfillSentdialog.open();
            Partial.Widgets.label59.caption = "zoozozozozoz";
        }

    } else if (row.stepTypeCode == 'RESTORE') {
        if (row.status == 'Request Assigned' || row.status == 'Request Created') {
            Partial.Widgets.EditNotSentdialog.title = "Edit Restoral Request";
            Partial.Widgets.EditNotSentdialog.open();
        } else if (row.status == 'Order Assigned' || row.status == ' Order Created') {
            Partial.Widgets.EditAndFulfillSentdialog.title = "Edit and Fulfill Service Restoration";
            Partial.Widgets.EditAndFulfillSentdialog.open();
        }
    } else if (row.stepTypeCode == 'CEASE') {
        if (row.status == 'Request Assigned' || row.status == 'Request Created') {
            Partial.Widgets.EditNotSentdialog.title = "Edit Cease Request";
            Partial.Widgets.EditNotSentdialog.open();
        } else if (row.status == 'Order Assigned' || row.status == ' Order Created') {
            Partial.Widgets.EditAndFulfillSentdialog.title = "Edit and Fulfill Cease";
            Partial.Widgets.EditAndFulfillSentdialog.open();
        }

    }
};

// Edit Suspension/Restore/Cease- Not Sent button 


Partial.updateDONotSentbuttonClick = function($event, widget) {
    debugger;

    App.Variables.errorMsg.dataSet.dataValue = null;
    var originalAgentId = Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.assignedAgentId;
    var selectedAgentId = Partial.Widgets.assignedPersonSelect.datavalue;
    if (originalAgentId != selectedAgentId) {

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

            Partial.selectedBanList = {
                "id": d.banMapRefId,

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
                'partitionKey': '20231-01-01',
                "CollectionTreatmentStepUpdate": {
                    'stepTypeCode': stepTypeCode,
                    'status': updateStatus,
                    'priority': Partial.Widgets.prioritySelect.datavalue,
                    'comment': Partial.Widgets.Comment.datavalue,
                    'stepDate': Partial.Widgets.dueDate.datavalue,
                    'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                    'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                    'channel': {},
                    'billingAccountIdRefs': Partial.Variables.BanListRefIds.dataSet,
                }
            });

            //Invoke POST createDispute service
            Partial.Variables.UpdateODManagemntVar.invoke();
            Partial.Widgets.EditNotSentdialog.close();

            App.Variables.successMessage.dataSet.dataValue = "Updated Without Sent To Fulfillment Successfully.";
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

    debugger;

    var originalAgentId = Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.assignedAgentId;
    var selectedAgentId = Partial.Widgets.assignedPersonSelect.datavalue;
    if (originalAgentId != selectedAgentId) {

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
                "id": d.banMapRefId,

            }
            Partial.Variables.BanListRefIds.dataSet.push(Partial.selectedBanList);

        });

        if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined) {
            App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
        } else {
            Partial.Variables.UpdateODManagemntVar.setInput({
                'id': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.id,
                'partitionKey': '20231-01-01',
                "CollectionTreatmentStepUpdate": {
                    'stepTypeCode': stepTypeCode,
                    'status': 'Order Created',
                    'priority': Partial.Widgets.prioritySelect.datavalue,
                    'comment': Partial.Widgets.Comment.datavalue,
                    'stepDate': Partial.Widgets.dueDate.datavalue,
                    'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                    'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                    'channel': {},
                    'billingAccountIdRefs': Partial.Variables.BanListRefIds.dataSet,
                }
            });

            //Invoke POST createDispute service
            Partial.Variables.UpdateODManagemntVar.invoke();

            Partial.Widgets.EditNotSentdialog.close();

            App.Variables.successMessage.dataSet.dataValue = " Updated And Sent Successfully.";
            setTimeout(messageTimeout, 3000);
        }
    }
};

// Edit Suspension/Restore/Cease- Sent button 

Partial.editSentcancelbuttonClick = function($event, widget) {
    Partial.Variables.errorMsg.dataSet.dataValue = "";
    Partial.Variables.UserLoggedInVar.dataSet.dataValue = App.Variables.getLoggedInUserDetails.dataSet.emplId;
    Partial.Widgets.EditAndFullfillSentdialog.close();
};
Partial.updateAndDoNotFulfillbuttonClick = function($event, widget) {
    var originalAgentId = Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.assignedAgentId;
    var selectedAgentId = Partial.Widgets.assignedPersonSelect.datavalue;
    if (originalAgentId != selectedAgentId) {

        Partial.Widgets.EditNotSentdialog.close();
        Partial.Widgets.update_ActionDialog.open();

    } else {
        var stepTypeCode;
        if (Partial.Widgets.EditNotSentdialog.title == "Edit and Fulfill Service Suspension") {
            stepTypeCode = "SUSPEND";
        } else if (Partial.Widgets.EditNotSentdialog.title == "Edit and Fulfill Service Suspension") {
            stepTypeCode = "RESTORE";
        } else if (Partial.Widgets.EditNotSentdialog.title == "Edit and Fulfill Cease") {
            stepTypeCode = "CEASE";
        }

        Partial.Variables.BanListRefIds.dataSet = [];

        Partial.Widgets.getEntityBanDetailsTable1.selectedItems;
        Partial.selectedBanList = [];
        Partial.Widgets.getEntityBanDetailsTable1.selectedItems.forEach(function(d) {

            Partial.selectedBanList = {
                "id": d.banMapRefId,

            }
            Partial.Variables.BanListRefIds.dataSet.push(Partial.selectedBanList);

        });



        var updateStatus = '';

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
                'partitionKey': '20231-01-01',
                "CollectionTreatmentStepUpdate": {
                    'stepTypeCode': stepTypeCode,
                    'status': updateStatus,
                    'priority': Partial.Widgets.prioritySelect.datavalue,
                    'comment': Partial.Widgets.Comment.datavalue,
                    'stepDate': Partial.Widgets.dueDate.datavalue,
                    'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                    'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                    'channel': {},
                    'billingAccountIdRefs': Partial.Variables.BanListRefIds.dataSet,
                }
            });

            //Invoke POST createDispute service
            Partial.Variables.UpdateODManagemntVar.invoke();

            Partial.Widgets.EditNotSentdialog.close();

            App.Variables.successMessage.dataSet.dataValue = "Updated Without Sent to Fulfilment successfully.";
            setTimeout(messageTimeout, 3000);
        }
    }
};

Partial.updateAndFulfilbuttonClick = function($event, widget) {

    var originalAgentId = Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.assignedAgentId;
    var selectedAgentId = Partial.Widgets.assignedPersonSelect.datavalue;
    if (originalAgentId != selectedAgentId) {

        Partial.Widgets.EditNotSentdialog.close();
        Partial.Widgets.update_ActionDialog.open();

    } else {

        var stepTypeCode;
        if (Partial.Widgets.EditNotSentdialog.title == "Edit and Fulfill Service Suspension") {
            stepTypeCode = "SUSPEND";
        } else if (Partial.Widgets.EditNotSentdialog.title == "Edit and Fulfill Service Suspension") {
            stepTypeCode = "RESTORE";
        } else if (Partial.Widgets.EditNotSentdialog.title == "Edit and Fulfill Cease") {
            stepTypeCode = "CEASE";
        }

        Partial.Variables.BanListRefIds.dataSet = [];

        Partial.Widgets.getEntityBanDetailsTable1.selectedItems;
        Partial.selectedBanList = [];
        Partial.Widgets.getEntityBanDetailsTable1.selectedItems.forEach(function(d) {

            Partial.selectedBanList = {
                "id": d.banMapRefId,

            }
            Partial.Variables.BanListRefIds.dataSet.push(Partial.selectedBanList);

        });

        if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined) {
            App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
        } else {
            Partial.Variables.UpdateODManagemntVar.setInput({
                'id': Partial.Widgets.getCollectionTreatmentStep_orderMngt.selecteditem.id,
                'partitionKey': '20231-01-01',
                "CollectionTreatmentStepUpdate": {
                    'stepTypeCode': stepTypeCode,
                    'status': 'Order Fulfilled',
                    'priority': Partial.Widgets.prioritySelect.datavalue,
                    'comment': Partial.Widgets.Comment.datavalue,
                    'stepDate': Partial.Widgets.dueDate.datavalue,
                    'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                    'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                    'channel': {},
                    'billingAccountIdRefs': Partial.Variables.BanListRefIds.dataSet,
                }
            });

            //Invoke POST createDispute service
            Partial.Variables.UpdateODManagemntVar.invoke();

            Partial.Widgets.EditNotSentdialog.close();

            App.Variables.successMessage.dataSet.dataValue = " Updated And Fullfill Successfully";
            setTimeout(messageTimeout, 3000);
        }
    }
};

function messageTimeout() {
    Partial.Variables.successMessage.dataSet.dataValue = null;
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
    Partial.Widgets.assigned_closeActionDialog.close();
    App.Variables.successMessage.dataSet.dataValue = " Action Closed Successfully";
    setTimeout(messageTimeout, 3000);
};

// cancle assigned person
Partial.assigned_cancleYesBtnClick = function($event, widget) {
    Partial.Widgets.assigned_cancleActionDialog.close();
    App.Variables.successMessage.dataSet.dataValue = " Action Cancelled Successfully";
    setTimeout(messageTimeout, 3000);
};
Partial.assigned_cancleNoBtnClick = function($event, widget) {
    Partial.Widgets.assigned_cancleActionDialog.close();
};

// for Update 
Partial.update_YesBtnClick = function($event, widget) {

    Partial.Widgets.update_ActionDialog.close();
    App.Variables.successMessage.dataSet.dataValue = " Action Updated Successfully";
    setTimeout(messageTimeout, 3000);


};

Partial.update_NoBtnClick = function($event, widget) {

    Partial.Widgets.update_ActionDialog.close();

    Partial.Widgets.EditNotSentdialog.open();


};