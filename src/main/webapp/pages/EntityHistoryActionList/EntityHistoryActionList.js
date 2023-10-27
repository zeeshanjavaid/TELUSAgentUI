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
var isReachedClickedYes = true;
var toDoTable = true;
var completedTable = false;
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

    Partial.Variables.getCollectionTreatmentStep_1.setInput({
        'IsOdManagement': false,
        'collectionEntityId': Partial.pageParams.entityId,
        'type': ''

    });

    Partial.Variables.getCollectionTreatmentStep_1.invoke();


    Partial.Variables.getCollectionTreatMentByEntId.setInput({
        'collectionEntityId': Partial.pageParams.entityId
    });
    Partial.Variables.getCollectionTreatMentByEntId.invoke();



    $('#filterGrid').hide();
    $('#completionDateGrid').hide();
    $('#completedTableGrid').hide();
    $("#toDoBtn").css("background-color", "#4B286D");
    $("#toDoBtn").css("color", "white");



};

function messageTimeout() {
    App.Variables.successMessage.dataSet.dataValue = null;
}

function getCurrentDate() {
    var currentDate = new Date().toJSON().slice(0, 10);
    return currentDate;
}

Partial.CreateActionLinkClick = function($event, widget) {

    Partial.Widgets.CreateActionPopOver.hidePopover();
    Partial.Widgets.SelectActionDialog.open();
    Partial.Variables.errorMsg.dataSet.dataValue = "";
};


Partial.nextButtonClick = function($event, widget) {

    debugger;

    if (Partial.Variables.getCollectionTreatMentByEntId.dataSet.length == 0) {

        App.Variables.errorMsg.dataSet.dataValue = "You cannot create an action for this entity. Entity is not yet in collection treatment.";

    } else {


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

    debugger;

    if (Partial.Widgets.actionStatusSelect.datavalue == "" || Partial.Widgets.actionStatusSelect.datavalue == undefined || Partial.Widgets.actionStatusSelect.datavalue == "Select") {
        App.Variables.errorMsg.dataSet.dataValue = "Status is mandatory";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined || Partial.Widgets.prioritySelect.datavalue == "Select") {
        App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
    } else if (!Partial.Widgets.actionStatusSelect.datavalue == "" && !Partial.Widgets.prioritySelect.datavalue == "") {
        // API Call will come here


        Partial.Variables.createEntityHistoryAction.setInput({
            "CollectionTreatmentStepCreate": {
                'stepTypeCode': "CALL-OB",
                'stepDate': Partial.Widgets.dueDate.datavalue,
                'comment': Partial.Widgets.Comment.datavalue,
                'status': Partial.Widgets.actionStatusSelect.datavalue,
                'priority': Partial.Widgets.prioritySelect.datavalue,
                'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                'partitionKey': getCurrentDate(),
                'collectionTreatment': {
                    'id': Partial.Variables.getCollectionTreatMentByEntId.dataSet[0].id,
                    'partitionKey': getCurrentDate()
                },
                'channel': {
                    'originatorAppId': "FAWBTELUSAGENT",
                    'channelOrgId': "FAWBTELUSAGENT",
                    'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
                }
            }
        });
        Partial.Variables.createEntityHistoryAction.invoke();
        App.Variables.successMessage.dataSet.dataValue = "Call Outbound Action created successfully.";
        Partial.Widgets.SelectActionDialog.close();
        setTimeout(messageTimeout, 4000);

        setTimeout(function() {
            Partial.Variables.getCollectionTreatmentStep_1.invoke();
        }, 1000);

    }

};

function callInboundAction($event, widget) {
    // Status and Priority fields are mandatory
    debugger;

    var phnumber;
    if (!Partial.Widgets.phnNumber.datavalue == "" || !Partial.Widgets.phnNumber.datavalue == undefined) {
        phnumber = Math.floor(Math.log10(Partial.Widgets.phnNumber.datavalue)) + 1;
    }

    if (phnumber < 10) {
        App.Variables.errorMsg.dataSet.dataValue = "Phone number should not be less than 10 digit";
    } else if (Partial.Widgets.actionStatusSelect.datavalue == "" || Partial.Widgets.actionStatusSelect.datavalue == undefined || Partial.Widgets.actionStatusSelect.datavalue == "Select") {
        App.Variables.errorMsg.dataSet.dataValue = "Status is mandatory";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined || Partial.Widgets.prioritySelect.datavalue == "Select") {
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
                'stepDate': Partial.Widgets.dueDate.datavalue,
                'comment': Partial.Widgets.Comment.datavalue,
                'status': Partial.Widgets.actionStatusSelect.datavalue,
                'priority': Partial.Widgets.prioritySelect.datavalue,
                'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                'partitionKey': getCurrentDate(),
                'additionalCharacteristics': characteristicList,
                'collectionTreatment': {
                    'id': Partial.Variables.getCollectionTreatMentByEntId.dataSet[0].id,
                    'partitionKey': getCurrentDate()
                },
                'channel': {
                    'originatorAppId': "FAWBTELUSAGENT",
                    'channelOrgId': "FAWBTELUSAGENT",
                    'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
                }
            }
        });
        Partial.Variables.createEntityHistoryAction.invoke();
        App.Variables.successMessage.dataSet.dataValue = "Call Inbound Action created successfully.";
        Partial.Widgets.SelectActionDialog.close();

        setTimeout(messageTimeout, 4000);

        setTimeout(function() {
            Partial.Variables.getCollectionTreatmentStep_1.invoke();
        }, 1000);
    }

};

function emailInboundAction($event, widget) {
    // Status and Priority fields are mandatory
    debugger;
    if (Partial.Widgets.actionStatusSelect.datavalue == "" || Partial.Widgets.actionStatusSelect.datavalue == undefined || Partial.Widgets.actionStatusSelect.datavalue == "Select") {
        App.Variables.errorMsg.dataSet.dataValue = "Status is mandatory";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined || Partial.Widgets.prioritySelect.datavalue == "Select") {
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
                    'stepDate': Partial.Widgets.dueDate.datavalue,
                    'comment': Partial.Widgets.Comment.datavalue,
                    'status': Partial.Widgets.actionStatusSelect.datavalue,
                    'priority': Partial.Widgets.prioritySelect.datavalue,
                    'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                    'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                    'partitionKey': getCurrentDate(),
                    'additionalCharacteristics': characteristicList,
                    'collectionTreatment': {
                        'id': Partial.Variables.getCollectionTreatMentByEntId.dataSet[0].id,
                        'partitionKey': getCurrentDate(),
                    },
                    'channel': {
                        'originatorAppId': "FAWBTELUSAGENT",
                        'channelOrgId': "FAWBTELUSAGENT",
                        'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
                    }
                }
            });
            Partial.Variables.createEntityHistoryAction.invoke();

            App.Variables.successMessage.dataSet.dataValue = "Email Inbound Action created successfully.";
            Partial.Widgets.SelectActionDialog.close();
            setTimeout(messageTimeout, 4000);

            setTimeout(function() {
                Partial.Variables.getCollectionTreatmentStep_1.invoke();
            }, 1000);
        } else if (!validateEmail(Partial.Widgets.mandatoryEmail._datavalue)) {
            App.Variables.errorMsg.dataSet.dataValue = "Please enter valid Email Address";
        }
    }

};

function generalFollowUpAction($event, widget) {
    // Status and Priority fields are mandatory
    if (Partial.Widgets.actionStatusSelect.datavalue == "" || Partial.Widgets.actionStatusSelect.datavalue == undefined || Partial.Widgets.actionStatusSelect.datavalue == "Select") {
        App.Variables.errorMsg.dataSet.dataValue = "Status is mandatory";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined || Partial.Widgets.prioritySelect.datavalue == "Select") {
        App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
    } else if (!Partial.Widgets.actionStatusSelect.datavalue == "" && !Partial.Widgets.prioritySelect.datavalue == "") {
        // API Call will come here
        Partial.Variables.createEntityHistoryAction.setInput({
            "CollectionTreatmentStepCreate": {
                'stepTypeCode': "FOLLOWUP",
                'stepDate': Partial.Widgets.dueDate.datavalue,
                'comment': Partial.Widgets.Comment.datavalue,
                'status': Partial.Widgets.actionStatusSelect.datavalue,
                'priority': Partial.Widgets.prioritySelect.datavalue,
                'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                'partitionKey': getCurrentDate(),
                'collectionTreatment': {
                    'id': Partial.Variables.getCollectionTreatMentByEntId.dataSet[0].id,
                    'partitionKey': getCurrentDate()
                },
                'channel': {
                    'originatorAppId': "FAWBTELUSAGENT",
                    'channelOrgId': "FAWBTELUSAGENT",
                    'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
                }
            }
        });
        Partial.Variables.createEntityHistoryAction.invoke();


        App.Variables.successMessage.dataSet.dataValue = "General Follow-up Action created successfully.";
        Partial.Widgets.SelectActionDialog.close();
        setTimeout(messageTimeout, 4000);

        setTimeout(function() {
            Partial.Variables.getCollectionTreatmentStep_1.invoke();
        }, 1000);
    }

};

function overdueNoticeAction($event, widget) {
    // Status and Priority fields are mandatory

    debugger;
    if (Partial.Widgets.actionStatusSelect.datavalue == "" || Partial.Widgets.actionStatusSelect.datavalue == undefined || Partial.Widgets.actionStatusSelect.datavalue == "Select") {
        App.Variables.errorMsg.dataSet.dataValue = "Status is mandatory";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined || Partial.Widgets.prioritySelect.datavalue == "Select") {
        App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
        // } else if (!validateEmail(Partial.Widgets.nonMandatoryEmail.datavalue)) {

    } else if (Partial.Widgets.nonMandatoryEmail._datavalue != "" && Partial.Widgets.nonMandatoryEmail._datavalue != undefined && !validateEmail(Partial.Widgets.nonMandatoryEmail._datavalue)) {



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
                'stepDate': Partial.Widgets.dueDate.datavalue,
                'comment': Partial.Widgets.Comment.datavalue,
                'status': Partial.Widgets.actionStatusSelect.datavalue,
                'priority': Partial.Widgets.prioritySelect.datavalue,
                'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                'partitionKey': getCurrentDate(),
                'additionalCharacteristics': characteristicList,
                'collectionTreatment': {
                    'id': Partial.Variables.getCollectionTreatMentByEntId.dataSet[0].id,
                    'partitionKey': getCurrentDate()
                },
                'channel': {
                    'originatorAppId': "FAWBTELUSAGENT",
                    'channelOrgId': "FAWBTELUSAGENT",
                    'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
                }
            }
        });
        Partial.Variables.createEntityHistoryAction.invoke();


        App.Variables.successMessage.dataSet.dataValue = "Overdue Notice Action created successfully.";
        Partial.Widgets.SelectActionDialog.close();
        setTimeout(messageTimeout, 4000);

        setTimeout(function() {
            Partial.Variables.getCollectionTreatmentStep_1.invoke();
        }, 1000);
    }

};

function paymentReminderNoticeAction($event, widget) {
    // Status and Priority fields are mandatory
    if (Partial.Widgets.actionStatusSelect.datavalue == "" || Partial.Widgets.actionStatusSelect.datavalue == undefined || Partial.Widgets.actionStatusSelect.datavalue == "Select") {
        App.Variables.errorMsg.dataSet.dataValue = "Status is mandatory";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined || Partial.Widgets.prioritySelect.datavalue == "Select") {
        App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
    } else if (Partial.Widgets.nonMandatoryEmail._datavalue != "" && Partial.Widgets.nonMandatoryEmail._datavalue != undefined && !validateEmail(Partial.Widgets.nonMandatoryEmail._datavalue)) {

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
                'stepDate': Partial.Widgets.dueDate.datavalue,
                'comment': Partial.Widgets.Comment.datavalue,
                'status': Partial.Widgets.actionStatusSelect.datavalue,
                'priority': Partial.Widgets.prioritySelect.datavalue,
                'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                'partitionKey': getCurrentDate(),
                'additionalCharacteristics': characteristicList,
                'collectionTreatment': {
                    'id': Partial.Variables.getCollectionTreatMentByEntId.dataSet[0].id,
                    'partitionKey': getCurrentDate()
                },
                'channel': {
                    'originatorAppId': "FAWBTELUSAGENT",
                    'channelOrgId': "FAWBTELUSAGENT",
                    'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
                }
            }
        });
        Partial.Variables.createEntityHistoryAction.invoke();


        App.Variables.successMessage.dataSet.dataValue = "Payment Reminder Notice Action created successfully.";
        Partial.Widgets.SelectActionDialog.close();
        setTimeout(messageTimeout, 4000);

        setTimeout(function() {
            Partial.Variables.getCollectionTreatmentStep_1.invoke();
        }, 1000);
    }

};

function disconnectNoticeAction($event, widget) {
    // Status and Priority fields are mandatory
    if (Partial.Widgets.actionStatusSelect.datavalue == "" || Partial.Widgets.actionStatusSelect.datavalue == undefined || Partial.Widgets.actionStatusSelect.datavalue == "Select") {
        App.Variables.errorMsg.dataSet.dataValue = "Status is mandatory";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined || Partial.Widgets.prioritySelect.datavalue == "Select") {
        App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
    } else if (Partial.Widgets.nonMandatoryEmail._datavalue != "" && Partial.Widgets.nonMandatoryEmail._datavalue != undefined && !validateEmail(Partial.Widgets.nonMandatoryEmail._datavalue)) {

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
                'stepDate': Partial.Widgets.dueDate.datavalue,
                'comment': Partial.Widgets.Comment.datavalue,
                'status': Partial.Widgets.actionStatusSelect.datavalue,
                'priority': Partial.Widgets.prioritySelect.datavalue,
                'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                'partitionKey': getCurrentDate(),
                'additionalCharacteristics': characteristicList,
                'collectionTreatment': {
                    'id': Partial.Variables.getCollectionTreatMentByEntId.dataSet[0].id,
                    'partitionKey': getCurrentDate()
                },
                'channel': {
                    'originatorAppId': "FAWBTELUSAGENT",
                    'channelOrgId': "FAWBTELUSAGENT",
                    'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
                }
            }
        });
        Partial.Variables.createEntityHistoryAction.invoke();


        App.Variables.successMessage.dataSet.dataValue = "Disconnect Notice Action created successfully.";
        Partial.Widgets.SelectActionDialog.close();
        setTimeout(messageTimeout, 4000);

        setTimeout(function() {
            Partial.Variables.getCollectionTreatmentStep_1.invoke();
        }, 1000);
    }

};

function cancellationNoticeAction($event, widget) {
    // Status and Priority fields are mandatory
    if (Partial.Widgets.actionStatusSelect.datavalue == "" || Partial.Widgets.actionStatusSelect.datavalue == undefined || Partial.Widgets.actionStatusSelect.datavalue == "Select") {
        App.Variables.errorMsg.dataSet.dataValue = "Status is mandatory";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined || Partial.Widgets.prioritySelect.datavalue == "Select") {
        App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
    } else if (Partial.Widgets.nonMandatoryEmail._datavalue != "" && Partial.Widgets.nonMandatoryEmail._datavalue != undefined && !validateEmail(Partial.Widgets.nonMandatoryEmail._datavalue)) {

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
                'stepDate': Partial.Widgets.dueDate.datavalue,
                'comment': Partial.Widgets.Comment.datavalue,
                'status': Partial.Widgets.actionStatusSelect.datavalue,
                'priority': Partial.Widgets.prioritySelect.datavalue,
                'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                'partitionKey': getCurrentDate(),
                'additionalCharacteristics': characteristicList,
                'collectionTreatment': {
                    'id': Partial.Variables.getCollectionTreatMentByEntId.dataSet[0].id,
                    'partitionKey': getCurrentDate()
                },
                'channel': {
                    'originatorAppId': "FAWBTELUSAGENT",
                    'channelOrgId': "FAWBTELUSAGENT",
                    'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
                }
            }
        });
        Partial.Variables.createEntityHistoryAction.invoke();
        App.Variables.successMessage.dataSet.dataValue = "Cancellation Notice Action created successfully.";
        Partial.Widgets.SelectActionDialog.close();
        setTimeout(messageTimeout, 4000);

        setTimeout(function() {
            Partial.Variables.getCollectionTreatmentStep_1.invoke();
        }, 1000);
    }

};

Partial.createButtonClick = function($event, widget) {

    var actionName = Partial.Variables.actionName.dataValue;
    switch (actionName) {
        case 'Call Outbound':
            callOutboundAction($event, widget);
            Partial.Variables.getCollectionTreatmentStep_1.invoke();

            break;
        case 'Call Inbound':
            callInboundAction($event, widget);
            Partial.Variables.getCollectionTreatmentStep_1.invoke();


            break;
        case 'Email Inbound':
            emailInboundAction($event, widget);
            Partial.Variables.getCollectionTreatmentStep_1.invoke();

            break;
        case 'General Follow-up':
            generalFollowUpAction($event, widget);
            Partial.Variables.getCollectionTreatmentStep_1.invoke();

            break;
        case 'Overdue Notice':
            overdueNoticeAction($event, widget);
            Partial.Variables.getCollectionTreatmentStep_1.invoke();

            break;
        case 'Payment Reminder Notice':
            paymentReminderNoticeAction($event, widget);
            Partial.Variables.getCollectionTreatmentStep_1.invoke();

            break;
        case 'Disconnect Notice':
            disconnectNoticeAction($event, widget);
            Partial.Variables.getCollectionTreatmentStep_1.invoke();

            break;
        case 'Cancellation Notice':
            cancellationNoticeAction($event, widget);
            Partial.Variables.getCollectionTreatmentStep_1.invoke();

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
        Partial.Variables.actionStatus.dataSet = Partial.Variables.statusSelectToDo.dataSet;
        $('.categorySelectToDo').show();
        $('.categorySelectCompleted').hide();
        $('#completionDateGrid').hide();
        $('#eventTypeColl').hide();
    } else if (toDoTable.style.display === "none") { // completed table
        Partial.Variables.categoryFilter.dataSet = Partial.Variables.categorySelectCompletedfilter.dataSet;
        // Partial.Variables.actionFilter.dataSet = "";
        Partial.Variables.actionFilter.dataSet = Partial.Variables.actionTypeFilterCompleted.dataSet;
        Partial.Variables.actionStatus.dataSet = Partial.Variables.allStatusForHistory.dataSet;
        //  Partial.Variables.actionStatus.dataSet = "";
        $('.categorySelectToDo').hide();
        $('.categorySelectCompleted').show();
        $('#completionDateGrid').show();

    }

    //Partial.Variables.CollectionTreatmentServiceGetCollectionTreatmentStep.dataSet;
};

// function added to clear all the fields in the filter
Partial.clearFilterFields = function($event, widget) {
    debugger;
    if (toDoTable == true) {
        Partial.Widgets.toDoCategorySelect.datavalue = 'COLL_TRTMT_STEP';
        Partial.Widgets.completedCategorySelect.datavalue = "All";
        Partial.Widgets.typeSelect.datavalue = "";
        Partial.Widgets.creationDate.datavalue = "";
        Partial.Widgets.completionDate.datavalue = "";
        Partial.Widgets.statusSelect.datavalue = "";
        Partial.Widgets.createdBySelect.datavalue = "";
        Partial.Widgets.assignedPersonSelectfilter.datavalue = "";
        Partial.Widgets.assignedTeamSelectfilter.datavalue = "";

        Partial.Variables.getCollectionTreatmentStep_1.setInput({
            'IsOdManagement': false,
            'collectionEntityId': Partial.pageParams.entityId,
            'type': '',
            'createdBy': '',
            'status': '',
            'assignedAgentId': '',
            'assignedTeam': '',
            'createdDate': ''


        });

        Partial.Variables.getCollectionTreatmentStep_1.invoke();
    } else if (completedTable == true) {
        Partial.Widgets.toDoCategorySelect.datavalue = "All";
        Partial.Widgets.completedCategorySelect.datavalue = "All";
        Partial.Widgets.typeSelect.datavalue = "";
        Partial.Widgets.creationDate.datavalue = "";
        Partial.Widgets.completionDate.datavalue = "";
        Partial.Widgets.statusSelect.datavalue = "";
        Partial.Widgets.createdBySelect.datavalue = "";
        Partial.Widgets.assignedPersonSelectfilter.datavalue = "";
        Partial.Widgets.assignedTeamSelectfilter.datavalue = "";
        Partial.Widgets.EventTypeSelect.datavalue = "ALL";

        Partial.Variables.GetCollectionActivityLogList.setInput({
            'collectionEntityId': Partial.pageParams.entityId,
            'businessEntityEventType': '',
            'relatedBusinessEntitySubType': '',
            'relatedBusinessEntityType': '',
            'relatedBusinessEntityStatus': '',
            'relatedBusinessEntityCreatedDate': '',
            'relatedBusinessEntityCreatedBy': '',
            'relatedBusinessEntityAssignedTo': '',
            'relatedBusinessEntityAssignedTeam': ''
        });

        Partial.Variables.GetCollectionActivityLogList.invoke();

    }




}

// function added to apply filter to the table
Partial.applyFilter = function($event, widget) {
    debugger;

    var typeCode = '';
    var typeCodeForCompleted = '';
    var createdBy = '';
    var assignedAgentId = '';
    var categroyForCompleted = '';
    var statusForHistory = '';
    var eventTypeSelect = '';

    if (Partial.Widgets.createdBySelect.datavalue != '') {
        createdBy = Partial.Widgets.createdBySelect.datavalue.emplId;
    }

    if (Partial.Widgets.assignedPersonSelectfilter.datavalue != '' && Partial.Widgets.assignedPersonSelectfilter.datavalue != undefined) {
        assignedAgentId = Partial.Widgets.assignedPersonSelectfilter.datavalue.emplId;
    }



    if (Partial.Widgets.typeSelect.datavalue == undefined || Partial.Widgets.typeSelect.datavalue == '') {
        // typeCode = 'ALL';
        //   typeCode = 'CALL-OB,CALL-IB,EM-IN,FOLLOWUP,NOTC1-PMTR,NOTC2-OD,NOTC3-DIST,NOTC4-CANL,RESTORE,CEASE,SUSPEND';
        typeCode = '';

    } else {
        typeCode = Partial.Widgets.typeSelect.datavalue;

    }

    if (toDoTable == true) {
        debugger;
        Partial.Variables.getCollectionTreatmentStep_1.setInput({
            'IsOdManagement': false,
            'collectionEntityId': Partial.pageParams.entityId,
            'category': Partial.Widgets.toDoCategorySelect.datavalue,
            'type': typeCode,
            'createdDate': Partial.Widgets.creationDate.datavalue,
            'status': Partial.Widgets.statusSelect.datavalue,
            'createdBy': createdBy,
            'assignedAgentId': assignedAgentId,
            'assignedTeam': Partial.Widgets.assignedTeamSelectfilter.datavalue,

        });

        Partial.Variables.getCollectionTreatmentStep_1.invoke();

    } else if (completedTable == true) {
        debugger;
        if (Partial.Widgets.completedCategorySelect.displayValue == "COLL_TRTMT_STEP") {
            categroyForCompleted = 'CollectionTreatmentStep';
            if (Partial.Widgets.typeSelect.datavalue == '' || Partial.Widgets.typeSelect.datavalue == undefined) {
                typeCodeForCompleted = '';
            } else {
                typeCodeForCompleted = Partial.Widgets.typeSelect.datavalue;
            }


            if (Partial.Widgets.statusSelect.datavalue == '' || Partial.Widgets.statusSelect.datavalue == undefined) {
                statusForHistory = '';
            } else {

                if (Partial.Widgets.typeSelect.datavalue == "SUSPEND" || Partial.Widgets.typeSelect.datavalue == "RESTORE" || Partial.Widgets.typeSelect.datavalue == "CEASE") {
                    if (Partial.Widgets.statusSelect.datavalue == '' || Partial.Widgets.statusSelect.datavalue == undefined) {


                        statusForHistory = "Request Created,Request Assigned,Order Created,Order Assigned,Order Fullfilled";
                    } else {
                        statusForHistory = Partial.Widgets.statusSelect.datavalue;
                    }
                } else {

                    if (Partial.Widgets.statusSelect.datavalue == '' || Partial.Widgets.statusSelect.datavalue == undefined) {


                        statusForHistory = "Open,Closed,Cancelled";
                    } else {

                        statusForHistory = Partial.Widgets.statusSelect.datavalue;
                    }
                }
            }


        } else if (Partial.Widgets.completedCategorySelect.displayValue == "PYMT_ARRNGMT") {
            categroyForCompleted = 'CollectionPaymentArrangement';

            if (Partial.Widgets.typeSelect.datavalue == '' || Partial.Widgets.typeSelect.datavalue == undefined) {
                typeCodeForCompleted = '';
            } else {
                typeCodeForCompleted = Partial.Widgets.typeSelect.datavalue;
            }
            if (Partial.Widgets.statusSelect.datavalue == '' || Partial.Widgets.statusSelect.datavalue == undefined) {
                statusForHistory = '';
            } else {
                statusForHistory = Partial.Widgets.statusSelect.datavalue;
            }


        } else if (Partial.Widgets.completedCategorySelect.displayValue == "COLL_DISPUTE") {
            categroyForCompleted = 'CollectionDispute';

            if (Partial.Widgets.typeSelect.datavalue == '' || Partial.Widgets.typeSelect.datavalue == undefined) {
                typeCodeForCompleted = '';
            } else {
                typeCodeForCompleted = Partial.Widgets.typeSelect.datavalue;
            }
            if (Partial.Widgets.statusSelect.datavalue == '' || Partial.Widgets.statusSelect.datavalue == undefined) {
                statusForHistory = '';
            } else {
                statusForHistory = Partial.Widgets.statusSelect.datavalue;
            }

        } else {
            categroyForCompleted = '';
            typeCodeForCompleted = Partial.Widgets.typeSelect.datavalue;
            statusForHistory = Partial.Widgets.statusSelect.datavalue;

        }



        if (Partial.Widgets.EventTypeSelect.datavalue === 'ALL') {
            eventTypeSelect = '';
        } else {
            eventTypeSelect = Partial.Widgets.EventTypeSelect.datavalue;
        }

        var completionDateTime

        if (Partial.Widgets.creationDate.datavalue != "") {

            completionDateTime = Partial.Widgets.creationDate.datavalue + "T" + "00" + ":" + "00" + ":" + "00" + ".00000" + "-08:00";
        }

        Partial.Variables.GetCollectionActivityLogList.setInput({
            'collectionEntityId': Partial.pageParams.entityId,
            'businessEntityEventType': eventTypeSelect,
            'relatedBusinessEntitySubType': typeCodeForCompleted,
            'relatedBusinessEntityType': categroyForCompleted,
            'relatedBusinessEntityStatus': statusForHistory,
            'relatedBusinessEntityCreatedDate': completionDateTime,
            'relatedBusinessEntityCreatedBy': createdBy,
            'relatedBusinessEntityAssignedTo': assignedAgentId,
            'relatedBusinessEntityAssignedTeam': Partial.Widgets.assignedTeamSelectfilter.datavalue
        });

        Partial.Variables.GetCollectionActivityLogList.invoke();

    }

}

Partial.toDoButtonClick = function($event, widget) {
    debugger;
    toDoTable = true;
    completedTable = false;

    // to make buttons selected
    $("#toDoBtn").css("background-color", "#4B286D");
    $("#toDoBtn").css("color", "white");
    $("#completedBtn").css("background-color", "white");
    $("#completedBtn").css("color", "#4B286D");
    Partial.Widgets.toDoCategorySelect.datavalue = 'COLL_TRTMT_STEP';
    Partial.Widgets.completedCategorySelect.datavalue = "All";
    Partial.Widgets.typeSelect.datavalue = "";
    Partial.Widgets.creationDate.datavalue = "";
    Partial.Widgets.completionDate.datavalue = "";
    Partial.Widgets.statusSelect.datavalue = "";
    Partial.Widgets.createdBySelect.datavalue = "";
    Partial.Widgets.assignedPersonSelectfilter.datavalue = "";
    Partial.Widgets.assignedTeamSelectfilter.datavalue = "";

    // changing dataset for category dropdown
    Partial.Variables.categoryFilter.dataSet = Partial.Variables.categorySelectTODOfilter.dataSet;
    Partial.Variables.actionFilter.dataSet = Partial.Variables.actionTypeFilterTODO.dataSet;
    Partial.Variables.actionStatus.dataSet = Partial.Variables.statusSelectToDo.dataSet;

    $('.categorySelectToDo').show();
    $('.categorySelectCompleted').hide();
    $('#eventTypeColl').hide();

    // display TO-DO table and hide Completed table
    $('#toDoTableGrid').show();
    $('#completedTableGrid').hide();
    $('#completionDateGrid').hide();

};

Partial.completedButtonClick = function($event, widget) {
    debugger;
    completedTable = true;
    toDoTable = false;

    // to make buttons selected
    $("#completedBtn").css("background-color", "#4B286D");
    $("#completedBtn").css("color", "white");
    $("#toDoBtn").css("background-color", "white");
    $("#toDoBtn").css("color", "#4B286D");
    Partial.Widgets.toDoCategorySelect.datavalue = "All";
    Partial.Widgets.completedCategorySelect.datavalue = "All";
    Partial.Widgets.typeSelect.datavalue = "";
    Partial.Widgets.creationDate.datavalue = "";
    Partial.Widgets.completionDate.datavalue = "";
    Partial.Widgets.statusSelect.datavalue = "";
    Partial.Widgets.createdBySelect.datavalue = "";
    Partial.Widgets.assignedPersonSelectfilter.datavalue = "";
    Partial.Widgets.assignedTeamSelectfilter.datavalue = "";
    Partial.Widgets.EventTypeSelect.datavalue = "ALL";

    // changing dataset for category dropdown
    Partial.Variables.categoryFilter.dataSet = Partial.Variables.categorySelectCompletedfilter.dataSet;
    Partial.Variables.actionFilter.dataSet = Partial.Variables.actionTypeFilterCompleted.dataSet;
    Partial.Variables.actionStatus.dataSet = Partial.Variables.allStatusForHistory.dataSet;
    // Partial.Variables.actionStatus.dataSet = "";

    $('.categorySelectToDo').hide();
    $('.categorySelectCompleted').show();

    // display Completed table and hide TO-DO table
    $('#completedTableGrid').show();
    $('#completionDateGrid').show();
    $('#toDoTableGrid').hide();
    $('#eventTypeColl').show();

};


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
    isReachedClickedYes = true;
    App.Variables.errorMsg.dataSet.dataValue = "";
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
    isReachedClickedYes = false;
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
    var characteristicList = [];

    var phnumber = Math.floor(Math.log10(Partial.Widgets.phnNumber.datavalue)) + 1;

    if (Partial.Widgets.phnNumber.datavalue === undefined || Partial.Widgets.phnNumber.datavalue == null) {

        App.Variables.errorMsg.dataSet.dataValue = "Invalid Phone number.";
        isError = true;
    } else if (phnumber != 10) {
        App.Variables.errorMsg.dataSet.dataValue = "Phone number should not be greater or less than 10 digit.";
        isError = true;
    } else {
        characteristicList.push({
            name: 'PhoneNumber',
            value: Partial.Widgets.phnNumber.datavalue
        });
    }
    if (isClicked) {

        if (Partial.Widgets.actionOutcomeSelect.datavalue === undefined || Partial.Widgets.actionOutcomeSelect.datavalue == '') {
            isError = true;
            App.Variables.errorMsg.dataSet.dataValue = "Outcome option is not selected.";

        } else {
            characteristicList.push({
                name: 'ReachedCustomer',
                value: 'N'
            });
            characteristicList.push({
                name: 'Outcome',
                value: Partial.Widgets.actionOutcomeSelect.datavalue

            });
        }

    }

    if (isReachedClickedYes) {
        characteristicList.push({
            name: 'ReachedCustomer',
            value: 'Y'
        });
    }

    Partial.Variables.UpdateCollectionTreatmentVar.setInput({

        'id': Partial.Widgets.getCollectionTreatmentStepTable2.selecteditem.id,
        'partitionKey': getCurrentDate(),
        "CollectionTreatmentStepUpdate": {
            'stepTypeCode': Partial.Widgets.getCollectionTreatmentStepTable2.selecteditem.stepTypeCode,
            'status': 'Closed',
            'comment': Partial.Widgets.Comment.datavalue,
            'assignedAgentId': Partial.Widgets.getCollectionTreatmentStepTable2.selecteditem.assignedAgentId,
            'additionalCharacteristics': characteristicList,
            'channel': {
                'originatorAppId': "FAWBTELUSAGENT",
                'channelOrgId': "FAWBTELUSAGENT",
                'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
            }
        }
    });

    if (!isError) {
        debugger;
        Partial.Variables.UpdateCollectionTreatmentVar.invoke();
        App.Variables.errorMsg.dataSet.dataValue = "";
        Partial.Widgets.CloseActionDialog.close();
        Partial.Variables.successMessage.dataSet.dataValue = "Action was closed."
        setTimeout(messageTimeout, 3000);
        App.refreshCollActionList();

    }

};

Partial.cancleButtonClick = function($event, widget) {
    App.Variables.errorMsg.dataSet.dataValue = "";
    Partial.Widgets.CloseActionDialog.close();

};
Partial.getCollectionTreatmentStepTable2_customRow1Action = function($event, row) {
    debugger;
    if (row.status != 'Closed' && row.status != 'Cancelled') {
        App.Variables.errorMsg.dataSet.dataValue = "";
        Partial.Variables.dialogNameBool.dataSet.dataValue = false;
        Partial.Widgets.EditActionDialog.open();
    }


};
Partial.getCollectionTreatmentStepTable2_customRow2Action = function($event, row) {
    debugger;
    if (row.status != 'Closed' && row.status != 'Cancelled') {
        if (row.assignedAgentId == '' || row.assignedAgentId == null) {
            Partial.Widgets.notAssigned_closeActionDialog.open();
        } else if (row.stepTypeCode == 'CALL-OB') {
            Partial.Widgets.CloseActionDialog.open();
        } else {
            Partial.Widgets.assigned_closeActionDialog.open();
        }
    }

};
Partial.button15_1Click = function($event, widget) {


    if (Partial.Widgets.getCollectionTreatmentStepTable2.selectedItems[0].assignedAgentId == '' || Partial.Widgets.getCollectionTreatmentStepTable2.selectedItems[0].assignedAgentId == null) {

        Partial.Widgets.notAssigned_closeActionDialog.close();


    } else {
        Partial.Widgets.assigned_closeActionDialog.close();
    }


};

Partial.button15Click = function($event, widget) {

    debugger;

    if (Partial.Widgets.getCollectionTreatmentStepTable2.selectedItems[0].assignedAgentId == '' || Partial.Widgets.getCollectionTreatmentStepTable2.selectedItems[0].assignedAgentId == null) {

        Partial.Widgets.assigned_closeActionDialog.close();
        Partial.Widgets.notAssigned_closeActionDialog.open();


    } else {
        Partial.Variables.UpdateCollectionTreatmentVar.setInput({

            'id': Partial.Widgets.getCollectionTreatmentStepTable2.selecteditem.id,
            'partitionKey': getCurrentDate(),
            "CollectionTreatmentStepUpdate": {
                'stepTypeCode': Partial.Widgets.getCollectionTreatmentStepTable2.selecteditem.stepTypeCode,
                'status': 'Closed',
                'comment': Partial.Widgets.Comment.datavalue,
                'assignedAgentId': Partial.Widgets.getCollectionTreatmentStepTable2.selecteditem.assignedAgentId,
                'channel': {
                    'originatorAppId': "FAWBTELUSAGENT",
                    'channelOrgId': "FAWBTELUSAGENT",
                    'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
                }

            }
        });

        Partial.Variables.UpdateCollectionTreatmentVar.invoke();


        Partial.Widgets.assigned_closeActionDialog.close();
        Partial.Variables.successMessage.dataSet.dataValue = "Action was closed."
        setTimeout(messageTimeout, 3000);
        App.refreshCollActionList();

    }


};
Partial.getCollectionTreatmentStepTable2_customRow3Action = function($event, row) {

    debugger;
    if (row.status != 'Closed' && row.status != 'Cancelled') {
        if (row.assignedAgentId == '' || row.assignedAgentId == null) {
            Partial.Widgets.notAssigned_cancleActionDialog.open();

        } else {

            Partial.Widgets.assigned_cancleActionDialog.open();
        }
    }

};
Partial.button16_1Click = function($event, widget) {

    if (Partial.Widgets.getCollectionTreatmentStepTable2.selectedItems[0].assignedAgentId == '' || Partial.Widgets.getCollectionTreatmentStepTable2.selectedItems[0].assignedAgentId == null) {

        Partial.Widgets.assigned_cancleActionDialog.close();
        Partial.Widgets.notAssigned_cancleActionDialog.open();


    } else {

        Partial.Variables.UpdateCollectionTreatmentVar.setInput({

            'id': Partial.Widgets.getCollectionTreatmentStepTable2.selecteditem.id,
            'partitionKey': getCurrentDate(),
            "CollectionTreatmentStepUpdate": {
                'stepTypeCode': Partial.Widgets.getCollectionTreatmentStepTable2.selecteditem.stepTypeCode,
                'status': 'Cancelled',
                'comment': Partial.Widgets.Comment.datavalue,
                'assignedAgentId': Partial.Widgets.getCollectionTreatmentStepTable2.selecteditem.assignedAgentId,
                'channel': {
                    'originatorAppId': "FAWBTELUSAGENT",
                    'channelOrgId': "FAWBTELUSAGENT",
                    'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
                }
            }
        });

        Partial.Variables.UpdateCollectionTreatmentVar.invoke();

        Partial.Widgets.assigned_cancleActionDialog.close();
        Partial.Variables.successMessage.dataSet.dataValue = "Action was cancelled."
        setTimeout(messageTimeout, 3000);
        App.refreshCollActionList();
    }


};
Partial.button17_1Click = function($event, widget) {

    debugger;
    if (Partial.Widgets.getCollectionTreatmentStepTable2.selectedItems[0].assignedAgentId == '' || Partial.Widgets.getCollectionTreatmentStepTable2.selectedItems[0].assignedAgentId == null) {

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
    if (Partial.Widgets.toDoCategorySelect.datavalue == "") {
        Partial.Variables.actionFilter.dataSet = Partial.Variables.actionTypeFilterTODO.dataSet;
    }
}

Partial.categorySelectCompletedOnChange = function($event, widget, newVal, oldVal) {
    debugger;
    if (Partial.Widgets.completedCategorySelect.datavalue == "PYMT_ARRNGMT") {
        Partial.Variables.actionFilter.dataSet = "";
        Partial.Variables.actionStatus.dataSet = Partial.Variables.statusWhenTypePaymentArr.dataSet;
    } else if (Partial.Widgets.completedCategorySelect.datavalue == "COLL_TRTMT_STEP") {
        Partial.Widgets.typeSelect.datavalue = "";
        Partial.Widgets.statusSelect.datavalue = "";
        Partial.Variables.actionFilter.dataSet = Partial.Variables.collTrtmtStpCtgValues.dataSet;
        Partial.Variables.actionStatus.dataSet = Partial.Variables.allStatusForHistory.dataSet;
    } else if (Partial.Widgets.completedCategorySelect.datavalue == "COLL_DISPUTE") {
        Partial.Variables.actionFilter.dataSet = "";
        Partial.Variables.actionStatus.dataSet = Partial.Variables.statusWhenActionTypeCallOb_CallIb_And_Dispute.dataSet;
    } else if (Partial.Widgets.completedCategorySelect.datavalue == "" || Partial.Widgets.completedCategorySelect.datavalue == "ALL") {
        Partial.Widgets.typeSelect.datavalue = "";
        Partial.Widgets.statusSelect.datavalue = "";
        Partial.Variables.actionFilter.dataSet = Partial.Variables.actionTypeFilterCompleted.dataSet;
        Partial.Variables.actionStatus.dataSet = Partial.Variables.allStatusForHistory.dataSet;

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
    App.Variables.errorMsg.dataSet.dataValue = null;

    if (!Partial.Widgets.prioritySelect.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
    } else {
        App.Variables.errorMsg.dataSet.dataValue = null;
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
                'partitionKey': getCurrentDate(),
                "CollectionTreatmentStepUpdate": {
                    'stepTypeCode': Partial.Widgets.EditActionNameText.caption,
                    'priority': Partial.Widgets.prioritySelect.datavalue,
                    'stepDate': Partial.Widgets.dueDate.datavalue,
                    'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
                    'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
                    'channel': {
                        'originatorAppId': "FAWBTELUSAGENT",
                        'channelOrgId': "FAWBTELUSAGENT",
                        'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
                    }
                }
            });

            //Invoke POST coll treatment service
            Partial.Variables.UpdateCollectionTreatmentVar.invoke();
            App.Variables.successMessage.dataSet.dataValue = "Action ID (" + Partial.Widgets.EditActionIdText.caption + ") edited successfully."
            Partial.Widgets.EditActionDialog.close();
            setTimeout(messageTimeout, 3000);
            App.refreshCollActionList();



        }

    };
}

Partial.EditUpdateYesButtonClick = function($event, widget) {
    debugger;
    var actionIdLabel = Partial.Widgets.EditActionIdText.caption;
    Partial.Variables.UpdateCollectionTreatmentVar.setInput({
        'id': Partial.Widgets.EditActionIdText.caption,
        'partitionKey': getCurrentDate(),
        'collectionEntityId': Partial.pageParams.entityId,
        "CollectionTreatmentStepUpdate": {
            'stepTypeCode': Partial.Widgets.EditActionNameText.caption,
            'status': Partial.Widgets.EditStatusLabel.caption,
            'priority': Partial.Widgets.prioritySelect.datavalue,
            'comment': Partial.Widgets.UpdateActionComment.datavalue,
            'stepDate': Partial.Widgets.dueDate.datavalue,
            'assignedAgentId': Partial.Widgets.assignedPersonSelect.datavalue,
            'assignedTeam': Partial.Widgets.assignedTeamSelect.datavalue,
            'channel': {
                'originatorAppId': "FAWBTELUSAGENT",
                'channelOrgId': "FAWBTELUSAGENT",
                'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
            }
        }
    });

    //Invoke POST createDispute service
    Partial.Variables.UpdateCollectionTreatmentVar.invoke();
    Partial.Widgets.EditActionDialog.close();
    App.Variables.successMessage.dataSet.dataValue = "Action ID (" + actionIdLabel + ") edited successfully."
    setTimeout(messageTimeout, 3000);
    App.refreshCollActionList();

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
    debugger;
    App.showRowExpansionToDo(row, $data);
};

Partial.EditActionDialogOpened = function($event, widget) {
    $("#updateActionDialogId").hide();
    $("#UpdateActionButtonId").hide();
};

Partial.getCollectionActivityLog_1Table3_OnRowexpand = function($event, widget, row, $data) {
    App.showRowExpansionCompleted(row, $data);
};
Partial.SelectActionDialogClose = function($event, widget) {
    App.Variables.errorMsg.dataSet.dataValue = '';

};

Partial.getCollectionTreatmentStep_1onError = function(variable, data, xhrObj) {

};

Partial.GetCollectionActivityLogListonError = function(variable, data, xhrObj) {

};


Partial.getCollectionTreatMentonError = function(variable, data, xhrObj) {

};





App.refreshCollActionList = function() {
    setTimeout(function() {
        Partial.Variables.getCollectionTreatmentStep_1.invoke();
    }, 2000);


};
Partial.getCollectionTreatmentStepTable2_customRowAction = function($event, row) {

};
Partial.typeSelectChange = function($event, widget, newVal, oldVal) {
    debugger;
    if (toDoTable) {

        if (Partial.Widgets.typeSelect.datavalue == "SUSPEND" || Partial.Widgets.typeSelect.datavalue == "RESTORE" || Partial.Widgets.typeSelect.datavalue == "CEASE") {
            Partial.Widgets.statusSelect.datavalue = "";
            Partial.Variables.actionStatus.dataSet = Partial.Variables.statusWhenTypeIsSus_Res_Cease.dataSet;
        } else if (Partial.Widgets.typeSelect.datavalue == "") {
            Partial.Widgets.statusSelect.datavalue = "";
            Partial.Variables.actionStatus.dataSet = Partial.Variables.statusSelectToDo.dataSet;
        } else {
            Partial.Widgets.statusSelect.datavalue = "";
            Partial.Variables.actionStatus.dataSet = Partial.Variables.statusWhenTypeIsSusForTodo.dataSet;
        }
    } else {
        if (Partial.Widgets.typeSelect.datavalue == "SUSPEND" || Partial.Widgets.typeSelect.datavalue == "RESTORE" || Partial.Widgets.typeSelect.datavalue == "CEASE") {
            Partial.Widgets.statusSelect.datavalue = "";
            Partial.Variables.actionStatus.dataSet = Partial.Variables.statusWhenTypeIsSus_Res_Cease.dataSet;

        } else if (Partial.Widgets.typeSelect.datavalue == "") {
            Partial.Widgets.statusSelect.datavalue = "";
            Partial.Variables.actionStatus.dataSet = Partial.Variables.allStatusForHistory.dataSet;
        } else {
            Partial.Widgets.statusSelect.datavalue = "";
            Partial.Variables.actionStatus.dataSet = Partial.Variables.statusWhenActionTypeCallOb_CallIb_And_Dispute.dataSet;
        }
    }



};

App.refreshHistoryActionList = function() {

    Partial.Variables.GetCollectionActivityLogList.invoke();

};