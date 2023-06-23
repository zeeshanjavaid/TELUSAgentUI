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
    Partial.Variables.UserLoggedInVar.dataSet.dataValue = App.Variables.getLoggedInUserDetails.dataSet.emplId;

    Partial.Widgets.CreateSuspensionRequestdialog.open();
};


Partial.CreateRestoralRequestClick = function($event, widget) {
    Partial.Widgets.OrderPopOver.hidePopover();
    Partial.Variables.UserLoggedInVar.dataSet.dataValue = App.Variables.getLoggedInUserDetails.dataSet.emplId;

    Partial.Widgets.CreateRestoralRequestdialog.open();
};
Partial.CreateCeaseRequestClick = function($event, widget) {
    Partial.Widgets.OrderPopOver.hidePopover();
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

    if (Partial.Widgets.reasonCodeSelect.datavalue == "" || Partial.Widgets.reasonCodeSelect.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Reason code is mandatory";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
    } else {
        // API Call will come here

        Partial.Variables.createOrderManagment.setInput({
            "CollectionTreatmentStepCreate": {
                'stepTypeCode': "SUSPEND",
                'reasonCode': Partial.Widgets.reasonCodeSelect.datavalue,
                'stepDate': Partial.Widgets.dueDate.datavalue,
                'comment': Partial.Widgets.Comment.datavalue,
                'status': isAssignedPerson,
                'priority': Partial.Widgets.prioritySelect.datavalue,
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

        App.Variables.successMessage.dataSet.dataValue = "Action Suspended successfully.";
        Partial.Widgets.CreateSuspensionRequestdialog.close();

    }

    Partial.Variables.getCollectionTreatmentStep_orderMngt.invoke();


};
Partial.createbuttonRestoralClick = function($event, widget) {


    var isAssignedPerson = '';

    if (Partial.Widgets.assignedPersonSelect.datavalue == "" || Partial.Widgets.assignedPersonSelect.datavalue == "Select") {
        isAssignedPerson = "Request Created";
    } else {
        isAssignedPerson = "Request Assigned";
    }
    if (Partial.Widgets.reasonCodeSelect.datavalue == "" || Partial.Widgets.reasonCodeSelect.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Reason code is mandatory";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
    } else {
        // API Call will come here

        Partial.Variables.createOrderManagment.setInput({
            "CollectionTreatmentStepCreate": {
                'stepTypeCode': "RESTORE",
                'reasonCode': Partial.Widgets.reasonCodeSelect.datavalue,
                'stepDate': Partial.Widgets.dueDate.datavalue,
                'comment': Partial.Widgets.Comment.datavalue,
                'status': isAssignedPerson,
                'priority': Partial.Widgets.prioritySelect.datavalue,
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

        App.Variables.successMessage.dataSet.dataValue = "Action Restore successfully.";
        Partial.Widgets.CreateRestoralRequestdialog.close();

    }

    Partial.Variables.getCollectionTreatmentStep_orderMngt.invoke();


};
Partial.createbuttonCeaseClick = function($event, widget) {

    var isAssignedPerson = '';

    if (Partial.Widgets.assignedPersonSelect.datavalue == "" || Partial.Widgets.assignedPersonSelect.datavalue == "Select") {
        isAssignedPerson = "Request Created";
    } else {
        isAssignedPerson = "Request Assigned";
    }

    if (Partial.Widgets.reasonCodeSelect.datavalue == "" || Partial.Widgets.reasonCodeSelect.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Reason code is mandatory";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
    } else {
        // API Call will come here

        Partial.Variables.createOrderManagment.setInput({
            "CollectionTreatmentStepCreate": {
                'stepTypeCode': "CEASE",
                'reasonCode': Partial.Widgets.reasonCodeSelect.datavalue,
                'stepDate': Partial.Widgets.dueDate.datavalue,
                'comment': Partial.Widgets.Comment.datavalue,
                'status': isAssignedPerson,
                'priority': Partial.Widgets.prioritySelect.datavalue,
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
        App.Variables.successMessage.dataSet.dataValue = "Action Ceased successfully.";
        Partial.Widgets.CreateCeaseRequestdialog.close();

    }

    Partial.Variables.getCollectionTreatmentStep_orderMngt.invoke();


};
Partial.getCollectionTreatmentStep_orderMngt_customRow1Action = function($event, row) {
    if (row.stepTypeCode == 'SUSPEND') {
        if (row.status == 'Request Assigned' || row.status == 'Request Created') {
            Partial.Widgets.EditNotSentdialog.title = "Edit Suspention Request";
            Partial.Widgets.EditNotSentdialog.open();
        } else if (status == 'Order Assigned' || status == ' Order Created') {
            Partial.Widgets.EditNotSentdialog.title = "Edit and Fulfill Service Suspention ";
            Partial.Widgets.EditAndFullfillSentdialog.open();
        }

    } else if (row.stepTypeCode == 'RESTORE') {
        /*  Partial.Widgets.EditNotSentdialog.title = "Edit Restoral Request";
           Partial.Widgets.EditNotSentdialog.open()*/

        if (row.status == 'Request Assigned' || row.status == 'Request Created') {
            Partial.Widgets.EditNotSentdialog.title = "Edit Restoral Request";
            Partial.Widgets.EditNotSentdialog.open();
        } else if (status == 'Order Assigned' || status == ' Order Created') {
            Partial.Widgets.EditNotSentdialog.title = "Edit and Fulfill Service Restoration ";
            Partial.Widgets.EditAndFullfillSentdialog.open();
        }
    } else if (row.stepTypeCode == 'CEASE') {
        /*Partial.Widgets.EditNotSentdialog.title = "Edit Cease Request";
        Partial.Widgets.EditNotSentdialog.open();*/
        if (row.status == 'Request Assigned' || row.status == 'Request Created') {
            Partial.Widgets.EditNotSentdialog.title = "Edit Cease Request";
            Partial.Widgets.EditNotSentdialog.open();
        } else if (status == 'Order Assigned' || status == ' Order Created') {
            Partial.Widgets.EditNotSentdialog.title = "Edit and Fulfill Cease ";
            Partial.Widgets.EditAndFullfillSentdialog.open();
        }

    }
};

Partial.updatebuttonClick = function($event, widget) {

};