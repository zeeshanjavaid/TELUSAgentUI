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
var isClicked = false;
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
    $('#filterGrid').hide();
    $('#completionDateGrid').hide();
    $('#completedTableGrid').hide();
    $("#toDoBtn").css("background-color", "#4B286D");
    $("#toDoBtn").css("color", "white");


};

function messageTimeout() {
    App.Variables.successMessage.dataSet.dataValue = null;
}

Partial.CreateActionLinkClick = function($event, widget) {

    Partial.Widgets.CreateActionPopOver.hidePopover();
    Partial.Widgets.SelectActionDialog.open();
    Partial.Variables.errorMsg.dataSet.dataValue = "";
};


Partial.nextButtonClick = function($event, widget) {
    if (Partial.Widgets.select1.datavalue == "" || Partial.Widgets.select1.datavalue == undefined) {
        Partial.Variables.errorMsg.dataSet.dataValue = "Action is mandatory";
    } else {
        Partial.Variables.errorMsg.dataSet.dataValue = "";
    }

    // Call Outbound Action 
    if (Partial.Widgets.select1.datavalue == 'Call Outbound') {
        Partial.Variables.actionName.dataValue = Partial.Widgets.select1.datavalue;
        Partial.Variables.actionStatusDefaultVar.dataSet.dataValue = 'Open';
        Partial.Variables.actionPriorityDefaultVar.dataSet.dataValue = 'High';
        Partial.Widgets.dueDate.datavalue = createDueDate();
        Partial.Variables.UserLoggedInVar.dataSet.dataValue = App.Variables.getLoggedInUserDetails.dataSet.emplId;

        Partial.Variables.getLoggedInUserTeamIdVar.setInput({
            'userId': App.Variables.getLoggedInUserId.dataSet[0].id
        });
        Partial.Variables.getLoggedInUserTeamIdVar.invoke();

        // hiding select action form
        hideSelectActionForm();
        // displaying Call Outbound action form
        $('#callOutBoundActionForm').show();
        // hiding non-required fields
        $('#callInBoundActionForm').hide();
        $('#customerName').hide();
        $('#nonMandatoryEmail').hide();
        $('#mandatoryEmail').hide();
        // making fields empty so that the same value does not carry over to another forms
        Partial.Widgets.Comments.datavalue = '';
        Partial.Widgets.dueDate.datavalue = '';
    }


    // Call Inbound Action
    if (Partial.Widgets.select1.datavalue == 'Call Inbound') {
        Partial.Variables.actionName.dataValue = Partial.Widgets.select1.datavalue;
        Partial.Variables.actionStatusDefaultVar.dataSet.dataValue = 'Closed';
        Partial.Variables.actionPriorityDefaultVar.dataSet.dataValue = 'Medium';
        Partial.Widgets.dueDate.datavalue = new Date();
        Partial.Variables.UserLoggedInVar.dataSet.dataValue = App.Variables.getLoggedInUserDetails.dataSet.emplId;

        Partial.Variables.getLoggedInUserTeamIdVar.setInput({
            'userId': App.Variables.getLoggedInUserId.dataSet[0].id
        });
        Partial.Variables.getLoggedInUserTeamIdVar.invoke();

        // hiding select action form
        hideSelectActionForm();
        // displaying Call Outbound action form
        $('#callOutBoundActionForm').show();
        // displaying customer name
        $('#customerName').show();
        // displaying Call Inbound action form
        $('#callInBoundActionForm').show();
        // hiding non-required fields
        $('#nonMandatoryEmail').hide();
        $('#mandatoryEmail').hide();
        // making fields empty so that the same value does not carry over to another forms
        Partial.Widgets.Comments.datavalue = '';
        Partial.Widgets.dueDate.datavalue = '';
        Partial.Widgets.custName.datavalue = '';
        Partial.Widgets.phnNumber.datavalue = '';
        Partial.Widgets.callduration.datavalue = '';
    }

    // Email Inbound Action 
    if (Partial.Widgets.select1.datavalue == 'Email Inbound') {
        Partial.Variables.actionName.dataValue = Partial.Widgets.select1.datavalue;
        Partial.Variables.actionStatusDefaultVar.dataSet.dataValue = 'Closed';
        Partial.Variables.actionPriorityDefaultVar.dataSet.dataValue = 'Medium';
        Partial.Widgets.dueDate.datavalue = new Date();
        Partial.Variables.UserLoggedInVar.dataSet.dataValue = App.Variables.getLoggedInUserDetails.dataSet.emplId;

        Partial.Variables.getLoggedInUserTeamIdVar.setInput({
            'userId': App.Variables.getLoggedInUserId.dataSet[0].id
        });
        Partial.Variables.getLoggedInUserTeamIdVar.invoke();

        // hiding select action form
        hideSelectActionForm();
        // displaying Call Outbound action form
        $('#callOutBoundActionForm').show();
        // displaying Email address
        $('#mandatoryEmail').show();
        // hiding non-required fields
        $('#customerName').hide();
        $('#callInBoundActionForm').hide();
        $('#nonMandatoryEmail').hide();
        // making fields empty so that the same value does not carry over to another forms
        Partial.Widgets.Comments.datavalue = '';
        Partial.Widgets.dueDate.datavalue = '';
        Partial.Widgets.mandatoryEmail.datavalue = '';
    }

    // General Follow-up Action 
    if (Partial.Widgets.select1.datavalue == 'General Follow-up') {
        Partial.Variables.actionName.dataValue = Partial.Widgets.select1.datavalue;
        Partial.Variables.actionStatusDefaultVar.dataSet.dataValue = 'Open';
        Partial.Variables.actionPriorityDefaultVar.dataSet.dataValue = 'Low';
        Partial.Widgets.dueDate.datavalue = createDueDate();
        Partial.Variables.UserLoggedInVar.dataSet.dataValue = App.Variables.getLoggedInUserDetails.dataSet.emplId;

        Partial.Variables.getLoggedInUserTeamIdVar.setInput({
            'userId': App.Variables.getLoggedInUserId.dataSet[0].id
        });
        Partial.Variables.getLoggedInUserTeamIdVar.invoke();

        // hiding select action form
        hideSelectActionForm();
        // displaying Call Outbound action form
        $('#callOutBoundActionForm').show();
        // hiding non-required fields
        $('#nonMandatoryEmail').hide();
        $('#mandatoryEmail').hide();
        $('#callInBoundActionForm').hide();
        $('#customerName').hide();
        // making fields empty so that the same value does not carry over to another forms
        Partial.Widgets.Comments.datavalue = '';
        Partial.Widgets.dueDate.datavalue = '';
    }

    //  Notice Actions
    if (Partial.Widgets.select1.datavalue == 'Overdue Notice' || Partial.Widgets.select1.datavalue == 'Payment Reminder Notice' || Partial.Widgets.select1.datavalue == 'Disconnect Notice' || Partial.Widgets.select1.datavalue == 'Cancellation Notice') {
        Partial.Variables.actionName.dataValue = Partial.Widgets.select1.datavalue;
        Partial.Variables.actionStatusDefaultVar.dataSet.dataValue = 'Closed';
        Partial.Variables.actionPriorityDefaultVar.dataSet.dataValue = 'Medium';
        Partial.Widgets.dueDate.datavalue = new Date();
        Partial.Variables.UserLoggedInVar.dataSet.dataValue = App.Variables.getLoggedInUserDetails.dataSet.emplId;

        Partial.Variables.getLoggedInUserTeamIdVar.setInput({
            'userId': App.Variables.getLoggedInUserId.dataSet[0].id
        });
        Partial.Variables.getLoggedInUserTeamIdVar.invoke();

        // hiding select action form
        hideSelectActionForm();
        // displaying Call Outbound action form
        $('#callOutBoundActionForm').show();
        // displaying customer name
        $('#customerName').show();
        // displaying Email address
        $('#nonMandatoryEmail').show();
        // hiding non-required fields
        $('#callInBoundActionForm').hide();
        $('#mandatoryEmail').hide();
        // making fields empty so that the same value does not carry over to another forms
        Partial.Widgets.Comments.datavalue = '';
        Partial.Widgets.dueDate.datavalue = '';
        Partial.Widgets.custName.datavalue = '';
        Partial.Widgets.nonMandatoryEmail.datavalue = '';
    }

};

Partial.cancelClick = function() {
    Partial.Variables.errorMsg.dataSet.dataValue = "";
    showSelectActionForm();
    // hiding Call Outbound action form
    $('#callOutBoundActionForm').hide();
    // hiding Call Inbound action form
    $('#callInBoundActionForm').hide();
};


Partial.closeSelectActionDialog = function() {
    Partial.Variables.errorMsg.dataSet.dataValue = "";
    Partial.Widgets.SelectActionDialog.close();
};

// function created to hide select action form
function hideSelectActionForm() {
    Partial.Variables.errorMsg.dataSet.dataValue = "";
    $('#selectActionForm').hide();
    $('#selectActionBtns').hide();
    $('#createActionBtns').show();
};

// function created to show select action form
function showSelectActionForm() {
    Partial.Variables.errorMsg.dataSet.dataValue = "";
    $('#selectActionForm').show();
    $('#selectActionBtns').show();
    $('#createActionBtns').hide();
};

function createDueDate() {
    var date = new Date();
    date.setDate(date.getDate() + 2); // add 2 days
    return date;
};

function validateEmail(email) {
    return email.match(/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/);
};

function callOutboundAction($event, widget) {
    // Status and Priority fields are mandatory
    if (Partial.Widgets.actionStatusSelect.datavalue == "" || Partial.Widgets.actionStatusSelect.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Status is mandatory";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
    } else if (!Partial.Widgets.actionStatusSelect.datavalue == "" && !Partial.Widgets.prioritySelect.datavalue == "") {
        // API Call will come here

        Partial.Variables.createEntityHistoryAction.setInput({
            "CollectionTreatmentStepCreate": {
                'stepTypeCode': "CALL-OB",
                'comment': Partial.Widgets.Comment.datavalue,
                'status': Partial.Widgets.actionStatusSelect.datavalue,
                'priority': Partial.Widgets.prioritySelect.datavalue,
                'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue
            },
        });
        Partial.Variables.createEntityHistoryAction.invoke();
        App.Variables.successMessage.dataSet.dataValue = "Call Outbound Action created successfully.";
        Partial.Widgets.SelectActionDialog.close();
    }

};

function callInboundAction($event, widget) {
    // Status and Priority fields are mandatory
    if (Partial.Widgets.actionStatusSelect.datavalue == "" || Partial.Widgets.actionStatusSelect.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Status is mandatory";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
    } else if (!Partial.Widgets.actionStatusSelect.datavalue == "" && !Partial.Widgets.prioritySelect.datavalue == "") {
        // API Call will come here
        var characteristicList = [];

        characteristicList.push({
            name: 'CustomerName',
            value: Partial.Widgets.custName.datavalue
        });
        characteristicList.push({
            name: 'PhoneNumber',
            value: Partial.Widgets.phnNumber.datavalue
        });
        characteristicList.push({
            name: 'CallDuration',
            value: Partial.Widgets.callduration.datavalue
        });


        Partial.Variables.createEntityHistoryAction.setInput({
            "CollectionTreatmentStepCreate": {
                'stepTypeCode': "CALL-IB",
                'comment': Partial.Widgets.Comment.datavalue,
                'status': Partial.Widgets.actionStatusSelect.datavalue,
                'priority': Partial.Widgets.prioritySelect.datavalue,
                'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                'additionalCharacteristics': characteristicList
            },
        });
        Partial.Variables.createEntityHistoryAction.invoke();

        App.Variables.successMessage.dataSet.dataValue = "Call Inbound Action created successfully.";
        Partial.Widgets.SelectActionDialog.close();
    }

};

function emailInboundAction($event, widget) {
    // Status and Priority fields are mandatory
    if (Partial.Widgets.actionStatusSelect.datavalue == "" || Partial.Widgets.actionStatusSelect.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Status is mandatory";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
    }

    // email field is mandatory for email inbound call
    if (Partial.Widgets.mandatoryEmail._datavalue == "" || Partial.Widgets.mandatoryEmail._datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Email Address is mandatory";
    } else {
        if (validateEmail(Partial.Widgets.mandatoryEmail._datavalue) && !Partial.Widgets.actionStatusSelect.datavalue == "" && !Partial.Widgets.prioritySelect.datavalue == "") {
            App.Variables.errorMsg.dataSet.dataValue = "";
            // API Call will come here
            var characteristicList = [];

            characteristicList.push({
                name: 'EmailAddress',
                value: Partial.Widgets.mandatoryEmail.datavalue
            });
            Partial.Variables.createEntityHistoryAction.setInput({
                "CollectionTreatmentStepCreate": {
                    'stepTypeCode': "EM-IN",
                    'comment': Partial.Widgets.Comment.datavalue,
                    'status': Partial.Widgets.actionStatusSelect.datavalue,
                    'priority': Partial.Widgets.prioritySelect.datavalue,
                    'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                    'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                    'additionalCharacteristics': characteristicList
                },
            });
            Partial.Variables.createEntityHistoryAction.invoke();

            App.Variables.successMessage.dataSet.dataValue = "Email Inbound Action created successfully.";
            Partial.Widgets.SelectActionDialog.close();
        } else if (!validateEmail(Partial.Widgets.mandatoryEmail._datavalue)) {
            App.Variables.errorMsg.dataSet.dataValue = "Please enter valid Email Address";
        }
    }

};

function generalFollowUpAction($event, widget) {
    // Status and Priority fields are mandatory
    if (Partial.Widgets.actionStatusSelect.datavalue == "" || Partial.Widgets.actionStatusSelect.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Status is mandatory";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
    } else if (!Partial.Widgets.actionStatusSelect.datavalue == "" && !Partial.Widgets.prioritySelect.datavalue == "") {
        // API Call will come here
        Partial.Variables.createEntityHistoryAction.setInput({
            "CollectionTreatmentStepCreate": {
                'stepTypeCode': "FOLLOWUP",
                'comment': Partial.Widgets.Comment.datavalue,
                'status': Partial.Widgets.actionStatusSelect.datavalue,
                'priority': Partial.Widgets.prioritySelect.datavalue,
                'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue
            },
        });
        Partial.Variables.createEntityHistoryAction.invoke();
        App.Variables.successMessage.dataSet.dataValue = "General Follow-up Action created successfully.";
        Partial.Widgets.SelectActionDialog.close();
    }

};

function overdueNoticeAction($event, widget) {
    // Status and Priority fields are mandatory
    if (Partial.Widgets.actionStatusSelect.datavalue == "" || Partial.Widgets.actionStatusSelect.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Status is mandatory";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
    } else if (!validateEmail(Partial.Widgets.nonMandatoryEmail._datavalue)) {
        App.Variables.errorMsg.dataSet.dataValue = "Please enter valid Email Address";
    } else if (!Partial.Widgets.actionStatusSelect.datavalue == "" && !Partial.Widgets.prioritySelect.datavalue == "") {
        // API Call will come here
        var characteristicList = [];

        characteristicList.push({
            name: 'CustomerName',
            value: Partial.Widgets.custName.datavalue
        });
        characteristicList.push({
            name: 'EmailAddress',
            value: Partial.Widgets.nonMandatoryEmail.datavalue
        });

        Partial.Variables.createEntityHistoryAction.setInput({
            "CollectionTreatmentStepCreate": {
                'stepTypeCode': "NOTC2-OD",
                'comment': Partial.Widgets.Comment.datavalue,
                'status': Partial.Widgets.actionStatusSelect.datavalue,
                'priority': Partial.Widgets.prioritySelect.datavalue,
                'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                'additionalCharacteristics': characteristicList
            },
        });
        Partial.Variables.createEntityHistoryAction.invoke();
        App.Variables.successMessage.dataSet.dataValue = "Overdue Notice Action created successfully.";
        Partial.Widgets.SelectActionDialog.close();
    }

};

function paymentReminderNoticeAction($event, widget) {
    // Status and Priority fields are mandatory
    if (Partial.Widgets.actionStatusSelect.datavalue == "" || Partial.Widgets.actionStatusSelect.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Status is mandatory";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
    } else if (!validateEmail(Partial.Widgets.nonMandatoryEmail._datavalue)) {
        App.Variables.errorMsg.dataSet.dataValue = "Please enter valid Email Address";
    } else if (!Partial.Widgets.actionStatusSelect.datavalue == "" && !Partial.Widgets.prioritySelect.datavalue == "") {
        // API Call will come here
        var characteristicList = [];

        characteristicList.push({
            name: 'CustomerName',
            value: Partial.Widgets.custName.datavalue
        });
        characteristicList.push({
            name: 'EmailAddress',
            value: Partial.Widgets.nonMandatoryEmail.datavalue
        });

        Partial.Variables.createEntityHistoryAction.setInput({
            "CollectionTreatmentStepCreate": {
                'stepTypeCode': "NOTC1-PMTR",
                'comment': Partial.Widgets.Comment.datavalue,
                'status': Partial.Widgets.actionStatusSelect.datavalue,
                'priority': Partial.Widgets.prioritySelect.datavalue,
                'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                'additionalCharacteristics': characteristicList
            },
        });
        Partial.Variables.createEntityHistoryAction.invoke();
        App.Variables.successMessage.dataSet.dataValue = "Payment Reminder Notice Action created successfully.";
        Partial.Widgets.SelectActionDialog.close();
    }

};

function disconnectNoticeAction($event, widget) {
    // Status and Priority fields are mandatory
    if (Partial.Widgets.actionStatusSelect.datavalue == "" || Partial.Widgets.actionStatusSelect.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Status is mandatory";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
    } else if (!validateEmail(Partial.Widgets.nonMandatoryEmail._datavalue)) {
        App.Variables.errorMsg.dataSet.dataValue = "Please enter valid Email Address";
    } else if (!Partial.Widgets.actionStatusSelect.datavalue == "" && !Partial.Widgets.prioritySelect.datavalue == "") {
        // API Call will come here
        var characteristicList = [];

        characteristicList.push({
            name: 'CustomerName',
            value: Partial.Widgets.custName.datavalue
        });
        characteristicList.push({
            name: 'EmailAddress',
            value: Partial.Widgets.nonMandatoryEmail.datavalue
        });

        Partial.Variables.createEntityHistoryAction.setInput({
            "CollectionTreatmentStepCreate": {
                'stepTypeCode': "NOTC3-DIST",
                'comment': Partial.Widgets.Comment.datavalue,
                'status': Partial.Widgets.actionStatusSelect.datavalue,
                'priority': Partial.Widgets.prioritySelect.datavalue,
                'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                'additionalCharacteristics': characteristicList
            },
        });
        Partial.Variables.createEntityHistoryAction.invoke();
        App.Variables.successMessage.dataSet.dataValue = "Disconnect Notice Action created successfully.";
        Partial.Widgets.SelectActionDialog.close();
    }

};

function cancellationNoticeAction($event, widget) {
    // Status and Priority fields are mandatory
    if (Partial.Widgets.actionStatusSelect.datavalue == "" || Partial.Widgets.actionStatusSelect.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Status is mandatory";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
    } else if (!validateEmail(Partial.Widgets.nonMandatoryEmail._datavalue)) {
        App.Variables.errorMsg.dataSet.dataValue = "Please enter valid Email Address";
    } else if (!Partial.Widgets.actionStatusSelect.datavalue == "" && !Partial.Widgets.prioritySelect.datavalue == "") {
        // API Call will come here
        // API Call will come here
        var characteristicList = [];

        characteristicList.push({
            name: 'CustomerName',
            value: Partial.Widgets.custName.datavalue
        });
        characteristicList.push({
            name: 'EmailAddress',
            value: Partial.Widgets.nonMandatoryEmail.datavalue
        });

        Partial.Variables.createEntityHistoryAction.setInput({
            "CollectionTreatmentStepCreate": {
                'stepTypeCode': "NOTC4-CANL",
                'comment': Partial.Widgets.Comment.datavalue,
                'status': Partial.Widgets.actionStatusSelect.datavalue,
                'priority': Partial.Widgets.prioritySelect.datavalue,
                'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                'additionalCharacteristics': characteristicList
            },
        });
        Partial.Variables.createEntityHistoryAction.invoke();
        App.Variables.successMessage.dataSet.dataValue = "Cancellation Notice Action created successfully.";
        Partial.Widgets.SelectActionDialog.close();
    }

};

Partial.createButtonClick = function($event, widget) {

    var actionName = Partial.Variables.actionName.dataValue;
    switch (actionName) {
        case 'Call Outbound':
            callOutboundAction($event, widget);
            break;
        case 'Call Inbound':
            callInboundAction($event, widget);
            break;
        case 'Email Inbound':
            emailInboundAction($event, widget);
            break;
        case 'General Follow-up':
            generalFollowUpAction($event, widget);
            break;
        case 'Overdue Notice':
            overdueNoticeAction($event, widget);
            break;
        case 'Payment Reminder Notice':
            paymentReminderNoticeAction($event, widget);
            break;
        case 'Disconnect Notice':
            disconnectNoticeAction($event, widget);
            break;
        case 'Cancellation Notice':
            cancellationNoticeAction($event, widget);
            break;
        default:
            App.Variables.errorMsg.dataSet.dataValue = "Not a valid action.";
    }

    setTimeout(messageTimeout, 10000);
};

// On opening of select action dialog, we are hiding the fields present in create action dialog
Partial.SelectActionDialogOpened = function($event, widget) {
    $('#callOutBoundActionForm').hide();
    $('#customerName').hide();
    $('#callInBoundActionForm').hide();
    $('#nonMandatoryEmail').hide();
    $('#mandatoryEmail').hide();
    $('#createActionBtns').hide();
};

// function added to toggle between show and hide the filter grid on click of filter icon
Partial.openFilterGrid = function($event, widget) {
    var filterGrid = document.getElementById("filterGrid");
    if (filterGrid.style.display === "none") {
        filterGrid.style.display = "block";
    } else {
        filterGrid.style.display = "none";
    }

    // to display completion date filter only for completed table
    var completedTable = document.getElementById("completedTableGrid");
    var toDoTable = document.getElementById("toDoTableGrid");
    if (completedTable.style.display === "none") { // to-do table
        Partial.Variables.categoryFilter.dataSet = Partial.Variables.categorySelectTODOfilter.dataSet;
        Partial.Variables.actionFilter.dataSet = Partial.Variables.actionTypeFilterTODO.dataSet;
        $('.categorySelectToDo').show();
        $('.categorySelectCompleted').hide();
        $('#completionDateGrid').hide();
    } else if (toDoTable.style.display === "none") { // completed table
        Partial.Variables.categoryFilter.dataSet = Partial.Variables.categorySelectCompletedfilter.dataSet;
        Partial.Variables.actionFilter.dataSet = Partial.Variables.actionTypeFilterCompleted.dataSet;
        $('.categorySelectToDo').hide();
        $('.categorySelectCompleted').show();
        $('#completionDateGrid').show();
    }

    Partial.Variables.CollectionTreatmentServiceGetCollectionTreatmentStep.dataSet;
};

// function added to clear all the fields in the filter
Partial.clearFilterFields = function($event, widget) {
    Partial.Widgets.categorySelect.datavalue = "";
    Partial.Widgets.typeSelect.datavalue = "";
    Partial.Widgets.creationDate.datavalue = "";
    Partial.Widgets.completionDate.datavalue = "";
    Partial.Widgets.statusSelect.datavalue = "";
    Partial.Widgets.createdBySelect.datavalue = "";
    Partial.Widgets.assignedPersonSelectfilter.datavalue = "";
    Partial.Widgets.assignedTeamSelectfilter.datavalue = "";
}

// function added to apply filter to the table
Partial.applyFilter = function($event, widget) {

    debugger;
    var category = Partial.Widgets.categorySelect.datavalue;
    var type = Partial.Widgets.typeSelect.datavalue;
    var createdDate = Partial.Widgets.creationDate.datavalue;
    var completionDate = Partial.Widgets.completionDate.datavalue;
    var status = Partial.Widgets.statusSelect.datavalue;
    var createdBy = Partial.Widgets.createdBySelect.datavalue;
    var assignedAgentId = Partial.Widgets.assignedPersonSelectfilter.datavalue;
    var assignedTeam = Partial.Widgets.assignedTeamSelectfilter.datavalue;

    Partial.Variables.getCollectionTreatmentStep_1.setInput({

        'category': Partial.Widgets.categorySelect.datavalue,
        'type': Partial.Widgets.typeSelect.datavalue,
        'createdDate': Partial.Widgets.creationDate.datavalue,
        /*'completionDate': Partial.Widgets.completionDate.datavalue,*/
        'status': Partial.Widgets.statusSelect.datavalue,
        'createdBy': Partial.Widgets.createdBySelect.datavalue,
        'assignedAgentId': Partial.Widgets.assignedPersonSelectfilter.datavalue,
        'assignedTeam': Partial.Widgets.assignedTeamSelectfilter.datavalue,

    });

    Partial.Variables.getCollectionTreatmentStep_1.invoke();
}

Partial.toDoButtonClick = function($event, widget) {
    // to make buttons selected
    $("#toDoBtn").css("background-color", "#4B286D");
    $("#toDoBtn").css("color", "white");
    $("#completedBtn").css("background-color", "white");
    $("#completedBtn").css("color", "#4B286D");

    // changing dataset for category dropdown
    Partial.Variables.categoryFilter.dataSet = Partial.Variables.categorySelectTODOfilter.dataSet;
    Partial.Variables.actionFilter.dataSet = Partial.Variables.actionTypeFilterTODO.dataSet;

    $('.categorySelectToDo').show();
    $('.categorySelectCompleted').hide();

    // display TO-DO table and hide Completed table
    $('#toDoTableGrid').show();
    $('#completedTableGrid').hide();
    $('#completionDateGrid').hide();

};

Partial.completedButtonClick = function($event, widget) {
    // to make buttons selected
    $("#completedBtn").css("background-color", "#4B286D");
    $("#completedBtn").css("color", "white");
    $("#toDoBtn").css("background-color", "white");
    $("#toDoBtn").css("color", "#4B286D");

    // changing dataset for category dropdown
    Partial.Variables.categoryFilter.dataSet = Partial.Variables.categorySelectCompletedfilter.dataSet;
    Partial.Variables.actionFilter.dataSet = Partial.Variables.actionTypeFilterCompleted.dataSet;

    $('.categorySelectToDo').hide();
    $('.categorySelectCompleted').show();

    // display Completed table and hide TO-DO table
    $('#completedTableGrid').show();
    $('#completionDateGrid').show();
    $('#toDoTableGrid').hide();

};

/*Partial.getCollectionTreatmentStepTable2_customRowAction = function($event, widget, row) {
    debugger;

    Partial.Widgets.ToDoActionPopover.showPopover();
};*/
// display the close action Dailog
Partial.CloseActionDialogOpened = function($event, widget) {
    $("#yesBtn").css("background-color", "#4B286D");
    $("#yesBtn").css("color", "white");
    $('#Outcome').hide();
    $('#actionOutcomeSelect').hide();
};
Partial.yesButtonClick = function($event, widget) {
    // to make buttons selected
    isClicked = false;
    $("#yesBtn").css("background-color", "#4B286D");
    $("#yesBtn").css("color", "white");
    $("#noBtn").css("background-color", "white");
    $("#noBtn").css("color", "#4B286D");
    // hide outcome
    $('#Outcome').hide();
    $('#actionOutcomeSelect').hide();
};
Partial.noButtonClick = function($event, widget) {
    isClicked = true;
    $("#noBtn").css("background-color", "#4B286D");
    $("#noBtn").css("color", "white");
    $("#yesBtn").css("background-color", "white");
    $("#yesBtn").css("color", "#4B286D");
    // show outcome
    $('#Outcome').show();
    $('#actionOutcomeSelect').show();
};
Partial.closeButtonClick = function($event, widget) {
    debugger;

    var isError = false;
    // if (Partial.Widgets.getCollectionTreatmentStepTable2.selectedItems[0].assignedAgentId == '') {

    //  if (Partial.Widgets.getCollectionTreatmentStepTable2.selectedItems[0].assignedAgentId == '') {

    var phnumber = Math.floor(Math.log10(Partial.Widgets.phnNumber.datavalue)) + 1;

    if (Partial.Widgets.phnNumber.datavalue === undefined || Partial.Widgets.phnNumber.datavalue == null) {

        App.Variables.errorMsg.dataSet.dataValue = "Please enter mandatory fields.";
        isError = true;
    } else if (phnumber > 10) {
        App.Variables.errorMsg.dataSet.dataValue = "Invalid phone number.";
        isError = true;
    }
    if (isClicked) {

        if (Partial.Widgets.actionOutcomeSelect.datavalue === undefined || Partial.Widgets.actionOutcomeSelect.datavalue == '') {
            isError = true;
            App.Variables.errorMsg.dataSet.dataValue = "Outcome option is not selected";

        }

    }

    if (!isError) {

        App.Variables.errorMsg.dataSet.dataValue = "";
        Partial.Widgets.CloseActionDialog.close();
        Partial.Variables.successMessage.dataSet.dataValue = "Action was closed."
        setTimeout(messageTimeout, 5000);

    }
    // Partial.Widgets.notAssigned_closeActionDialog.open();

    // } else {
    //     Partial.Widgets.CloseActionDialog.close();
    //     Partial.Widgets.assigned_closeActionDialog.open();
    //     /* Partial.Widgets.notAssigned_closeActionDialog.open();*/
    // }
};

Partial.cancleButtonClick = function($event, widget) {
    App.Variables.errorMsg.dataSet.dataValue = "";
    Partial.Widgets.CloseActionDialog.close();
    // Partial.Widgets.assigned_cancleActionDialog.open();
    /* Partial.Widgets.notAssigned_notAssigned_cancleActionDialog.open();*/
};
Partial.getCollectionTreatmentStepTable2_customRow1Action = function($event, row) {
    debugger;
    Partial.Variables.dialogNameBool.dataSet.dataValue = false;
    Partial.Widgets.EditActionDialog.open();


};
Partial.getCollectionTreatmentStepTable2_customRow2Action = function($event, row) {
    debugger;
    if (row.assignedAgentId == '') {
        Partial.Widgets.notAssigned_closeActionDialog.open();
    } else if (row.stepTypeCode == 'CALL-OB') {
        Partial.Widgets.CloseActionDialog.open();
    } else {
        Partial.Widgets.assigned_closeActionDialog.open();
    }

};
Partial.button15_1Click = function($event, widget) {


    if (Partial.Widgets.getCollectionTreatmentStepTable2.selectedItems[0].assignedAgentId == '') {

        Partial.Widgets.notAssigned_closeActionDialog.close();


    } else {
        Partial.Widgets.assigned_closeActionDialog.close();
    }


};

Partial.button15Click = function($event, widget) {



    if (Partial.Widgets.getCollectionTreatmentStepTable2.selectedItems[0].assignedAgentId == '') {

        Partial.Widgets.assigned_closeActionDialog.close();
        Partial.Widgets.notAssigned_closeActionDialog.open();


    } else {
        Partial.Widgets.assigned_closeActionDialog.close();
    }


};
Partial.getCollectionTreatmentStepTable2_customRow3Action = function($event, row) {

    debugger;

    if (row.assignedAgentId == '') {
        Partial.Widgets.notAssigned_cancleActionDialog.open();

    } else {

        Partial.Widgets.assigned_cancleActionDialog.open();
    }

};
Partial.button16_1Click = function($event, widget) {

    if (Partial.Widgets.getCollectionTreatmentStepTable2.selectedItems[0].assignedAgentId == '') {

        Partial.Widgets.assigned_cancleActionDialog.close();
        Partial.Widgets.notAssigned_cancleActionDialog.open();


    } else {
        Partial.Widgets.assigned_cancleActionDialog.close();
        Partial.Variables.successMessage.dataSet.dataValue = "Action was cancelled."
        setTimeout(messageTimeout, 3000);
    }


};
Partial.button17_1Click = function($event, widget) {
    if (Partial.Widgets.getCollectionTreatmentStepTable2.selectedItems[0].assignedAgentId == '') {

        Partial.Widgets.notAssigned_cancleActionDialog.close();

    } else {
        Partial.Widgets.assigned_cancleActionDialog.close();
    }

};


Partial.button14_2Click = function($event, widget) {
    Partial.Widgets.assigned_cancleActionDialog.close();
};
Partial.closeActionClick = function($event, widget) {
    Partial.Widgets.assigned_closeActionDialog.close();

};


Partial.categorySelectToDoOnChange = function($event, widget, newVal, oldVal) {
    debugger;
    Partial.Widgets.categorySelect.datavalue;
    if (Partial.Widgets.categorySelect.datavalue == "") {
        Partial.Variables.actionFilter.dataSet = Partial.Variables.actionTypeFilterTODO.dataSet;
    }
}

Partial.categorySelectCompletedOnChange = function($event, widget, newVal, oldVal) {
    debugger;
    Partial.Widgets.categorySelect.datavalue;
    if (Partial.Widgets.categorySelect.datavalue == "PYMT_ARRNGMT") {
        Partial.Variables.actionFilter.dataSet = Partial.Variables.pmtArrgntCtgValues.dataSet;
    } else if (Partial.Widgets.categorySelect.datavalue == "COLL_TRTMT_STEP") {
        Partial.Variables.actionFilter.dataSet = Partial.Variables.collTrtmtStpCtgValues.dataSet;
    } else if (Partial.Widgets.categorySelect.datavalue == "COLL_DISPUTE") {
        Partial.Variables.actionFilter.dataSet = Partial.Variables.collDisputeCtgValues.dataSet;
    } else if (Partial.Widgets.categorySelect.datavalue == "") {
        Partial.Variables.actionFilter.dataSet = Partial.Variables.actionTypeFilterCompleted.dataSet;
    }
}

function messageTimeout() {
    Partial.Variables.successMessage.dataSet.dataValue = null;
}
Partial.CloseActionDialogClose = function($event, widget) {

    App.Variables.errorMsg.dataSet.dataValue = null;

};

Partial.UpdateActionClick = function($event, widget) {
    debugger;
    Partial.Variables.dialogNameBool.dataSet.dataValue = false;

    if (!Partial.Widgets.prioritySelect.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
    } else {
        var originalAgentId = Partial.Widgets.getCollectionTreatmentStepTable2.selecteditem.assignedAgentId;
        var selectedAgentId = Partial.Widgets.assignedPersonSelect.datavalue;
        if (originalAgentId != selectedAgentId) {
            debugger;
            //Partial.Widgets.updateActionDialog.open();
            Partial.Variables.dialogNameBool.dataSet.dataValue = true;
            $("#layoutgrid5id").hide();
            $("#EditActionButtonId").hide();
            $("#updateActionDialogId").show();
            $("#UpdateActionButtonId").show();
        } else {
            Partial.Variables.UpdateCollectionTreatmentVar.setInput({
                'id': Partial.Widgets.EditActionIdText.caption,
                'partitionKey': '123',
                "CollectionTreatmentStepUpdate": {
                    'stepTypeCode': Partial.Widgets.EditActionNameText.caption,
                    'status': Partial.Widgets.EditStatusLabel.caption,
                    'priority': Partial.Widgets.prioritySelect.datavalue,
                    'comment': Partial.Widgets.EditActionComment.caption,
                    'stepDate': Partial.Widgets.dueDate.datavalue,
                    'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                    'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue
                }
            });

            //Invoke POST createDispute service
            Partial.Variables.UpdateCollectionTreatmentVar.invoke();
            App.Variables.successMessage.dataSet.dataValue = "Action ID (" + Partial.Widgets.EditActionIdText.caption + ") edited successfully."
            Partial.Widgets.EditActionDialog.close();
            setTimeout(messageTimeout, 5000);
        }
    }

};

Partial.EditUpdateYesButtonClick = function($event, widget) {
    debugger;
    var actionIdLabel = Partial.Widgets.EditActionIdText.caption;
    Partial.Variables.UpdateCollectionTreatmentVar.setInput({
        'id': Partial.Widgets.EditActionIdText.caption,
        'partitionKey': '123',
        "CollectionTreatmentStepUpdate": {
            'stepTypeCode': Partial.Widgets.EditActionNameText.caption,
            'status': Partial.Widgets.EditStatusLabel.caption,
            'priority': Partial.Widgets.prioritySelect.datavalue,
            'comment': Partial.Widgets.UpdateActionComment.datavalue,
            'stepDate': Partial.Widgets.dueDate.datavalue,
            'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
            'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue
        }
    });

    //Invoke POST createDispute service
    Partial.Variables.UpdateCollectionTreatmentVar.invoke();

    Partial.Widgets.EditActionDialog.close();

    App.Variables.successMessage.dataSet.dataValue = "Action ID (" + actionIdLabel + ") edited successfully."
    setTimeout(messageTimeout, 5000);

};

Partial.EditUpdateNoButtonClick = function($event, widget) {
    Partial.Variables.dialogNameBool.dataSet.dataValue = false;
    $("#updateActionDialogId").hide();
    $("#UpdateActionButtonId").hide();
    $("#layoutgrid5id").show();
    $("#EditActionButtonId").show();

};

Partial.updateActionDialogClose = function($event, widget) {

};

Partial.getCollectionTreatmentStepTable2_OnRowexpand = function($event, widget, row, $data) {

    App.showRowExpansionToDo(row, $data);
};

Partial.EditActionDialogOpened = function($event, widget) {
    $("#updateActionDialogId").hide();
    $("#UpdateActionButtonId").hide();
};

Partial.getCollectionActivityLog_1Table3_OnRowexpand = function($event, widget, row, $data) {
    App.showRowExpansionCompleted(row, $data);
};