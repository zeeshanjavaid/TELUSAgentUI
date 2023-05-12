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
    $('#filterGrid').hide();
    $('#completionDateGrid').hide();
    $('#completedTableGrid').hide();
    $("#toDoBtn").css("background-color", "#4B286D");
    $("#toDoBtn").css("color", "white");
    Partial.Variables.CollectionTreatmentServiceGetCollectionTreatmentStep.dataSet;
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
        Partial.Widgets.actionStatusSelect.datavalue = 'Open';
        Partial.Widgets.prioritySelect.datavalue = 'High';
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
        Partial.Widgets.actionStatusSelect.datavalue = 'Closed';
        Partial.Widgets.prioritySelect.datavalue = 'Medium';
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
        Partial.Widgets.actionStatusSelect.datavalue = 'Closed';
        Partial.Widgets.prioritySelect.datavalue = 'Medium';
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
        Partial.Widgets.actionStatusSelect.datavalue = 'Open';
        Partial.Widgets.prioritySelect.datavalue = 'Low';
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
        Partial.Widgets.actionStatusSelect.datavalue = 'Closed';
        Partial.Widgets.prioritySelect.datavalue = 'Medium';
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
}

Partial.createButtonClick = function($event, widget) {

    // Status and Priority fields are mandatory for all the create actions
    if (Partial.Widgets.actionStatusSelect.datavalue == "" || Partial.Widgets.actionStatusSelect.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Status is mandatory";
    } else if (Partial.Widgets.prioritySelect.datavalue == "" || Partial.Widgets.prioritySelect.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Priority is mandatory";
    } else if (!Partial.Widgets.actionStatusSelect.datavalue == "" && !Partial.Widgets.prioritySelect.datavalue == "" && Partial.Variables.actionName.dataValue != 'Email Inbound') {
        App.Variables.successMessage.dataSet.dataValue = "Action created successfully.";
        Partial.Widgets.SelectActionDialog.close();
    }



    // Email validation for Email inbound action
    if (Partial.Variables.actionName.dataValue == 'Email Inbound') {
        if (Partial.Widgets.mandatoryEmail._datavalue == undefined || Partial.Widgets.mandatoryEmail._datavalue == "") {
            App.Variables.errorMsg.dataSet.dataValue = "Email Address is mandatory";
        } else {
            if (validateEmail(Partial.Widgets.mandatoryEmail._datavalue) && !Partial.Widgets.actionStatusSelect.datavalue == "" && !Partial.Widgets.prioritySelect.datavalue == "") {
                App.Variables.errorMsg.dataSet.dataValue = "";
                App.Variables.successMessage.dataSet.dataValue = "Action created successfully.";
                Partial.Widgets.SelectActionDialog.close();
            } else if (!validateEmail(Partial.Widgets.mandatoryEmail._datavalue)) {
                App.Variables.errorMsg.dataSet.dataValue = "Please enter valid Email Address";
            }
        }
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
    if (completedTable.style.display === "none") {
        $('#completionDateGrid').hide();
    } else if (toDoTable.style.display === "none") {
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

}

Partial.toDoButtonClick = function($event, widget) {
    // to make buttons selected
    $("#toDoBtn").css("background-color", "#4B286D");
    $("#toDoBtn").css("color", "white");
    $("#completedBtn").css("background-color", "white");
    $("#completedBtn").css("color", "#4B286D");

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

    // display Completed table and hide TO-DO table
    $('#completedTableGrid').show();
    $('#completionDateGrid').show();
    $('#toDoTableGrid').hide();
};
Partial.getCollectionTreatmentStepTable2_customFieldOnClick = function($event, widget, row) {
    Partial.Widgets.ToDoActionPopover.open();
};