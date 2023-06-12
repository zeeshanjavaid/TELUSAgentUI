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
var reachedCustomer;
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
    App.showRowExpansionToDo = function(row, data) {
        var type = row.stepTypeCode;
        Partial.Widgets.status.caption = row.status;
        Partial.Widgets.activityType.caption = row.stepTypeCode;
        populateDataInRowExpansion(row, type);

        if (type == 'CALL-OB' && reachedCustomer == 'Y') { // reached outbound call
            $('.reachedCustomer').show();
            $('.phone').show();
            $('.actionID').show();
            $('.outcome').hide();
            $('.customerName').hide();
            $('.callDuration').hide();
            $('.eventID').hide();
            $('.email').hide();
            $('.blankGrid').hide();
            $('.activityType').hide();
            $('.noticeEmail').hide();
        } else if (type == 'CALL-OB' && reachedCustomer == 'N') { // notReached outbound call
            $('.reachedCustomer').show();
            $('.phone').show();
            $('.outcome').show();
            $('.blankGrid').show();
            $('.actionID').show();
            $('.customerName').hide();
            $('.callDuration').hide();
            $('.eventID').hide();
            $('.activityType').hide();
            $('.email').hide();
            $('.noticeEmail').hide();
        } else if (type == 'CALL-IB') {
            $('.phone').show();
            $('.actionID').show();
            $('.reachedCustomer').hide();
            $('.outcome').hide();
            $('.email').hide();
            $('.noticeEmail').hide();
            $('.activityType').hide();
            $('.eventID').hide();
            $('.customerName').show();
            $('.callDuration').show();
            $('.blankGrid').show();
        } else if (type == 'EM-IN') {
            $('.reachedCustomer').hide();
            $('.outcome').hide();
            $('.actionID').show();
            $('.customerName').hide();
            $('.callDuration').hide();
            $('.phone').hide();
            $('.noticeEmail').hide();
            $('.activityType').hide();
            $('.eventID').hide();
            $('.email').show();
            $('.blankGrid').show();
        } else if (type == 'FOLLOWUP') {
            $('.reachedCustomer').hide();
            $('.outcome').hide();
            $('.customerName').hide();
            $('.callDuration').hide();
            $('.phone').hide();
            $('.noticeEmail').hide();
            $('.blankGrid').hide();
            $('.activityType').hide();
            $('.eventID').hide();
            $('.email').hide();
            $('.actionID').show();
        } else if (type == 'NOTC1-PMTR' || type == 'NOTC2-OD' || type == 'NOTC3-DIST' || type == 'NOTC4-CANL') {
            $('.customerName').show();
            $('.email').hide();
            $('.noticeEmail').show();
            $('.reachedCustomer').hide();
            $('.outcome').hide();
            $('.eventID').hide();
            $('.blankGrid').hide();
            $('.callDuration').hide();
            $('.activityType').hide();
            $('.phone').hide();
            $('.actionID').show();
        } else if (type == 'RESTORE' || type == 'CEASE' || type == 'SUS' || type == 'SUSPEND') {
            $('.eventID').show();
            $('.activityType').show();
            $('.customerName').hide();
            $('.email').hide();
            $('.noticeEmail').hide();
            $('.reachedCustomer').hide();
            $('.outcome').hide();
            $('.blankGrid').hide();
            $('.callDuration').hide();
            $('.actionID').hide();
            $('.phone').hide();
        }

    }

    App.showRowExpansionCompleted = function(row, data) {
        var type = row.collectionActivityType;
        Partial.Widgets.status.caption = row.relatedBusinessEntityStatus;
        Partial.Widgets.activityType.caption = row.collectionActivityType;
        populateDataInRowExpansion(row, type);

        if (type == 'CALL-OB' && reachedCustomer == 'Y') { // reached outbound call
            $('.reachedCustomer').show();
            $('.phone').show();
            $('.actionID').show();
            $('.outcome').hide();
            $('.customerName').hide();
            $('.callDuration').hide();
            $('.eventID').hide();
            $('.email').hide();
            $('.blankGrid').hide();
            $('.activityType').hide();
            $('.noticeEmail').hide();
        } else if (type == 'CALL-OB' && reachedCustomer == 'N') { // notReached outbound call
            $('.reachedCustomer').show();
            $('.phone').show();
            $('.outcome').show();
            $('.blankGrid').show();
            $('.actionID').show();
            $('.customerName').hide();
            $('.callDuration').hide();
            $('.eventID').hide();
            $('.activityType').hide();
            $('.email').hide();
            $('.noticeEmail').hide();
        } else if (type == 'CALL-IB') {
            $('.phone').show();
            $('.actionID').show();
            $('.reachedCustomer').hide();
            $('.outcome').hide();
            $('.email').hide();
            $('.noticeEmail').hide();
            $('.activityType').hide();
            $('.eventID').hide();
            $('.customerName').show();
            $('.callDuration').show();
            $('.blankGrid').show();
        } else if (type == 'EM-IN') {
            $('.reachedCustomer').hide();
            $('.outcome').hide();
            $('.actionID').show();
            $('.customerName').hide();
            $('.callDuration').hide();
            $('.phone').hide();
            $('.noticeEmail').hide();
            $('.activityType').hide();
            $('.eventID').hide();
            $('.email').show();
            $('.blankGrid').show();
        } else if (type == 'FOLLOWUP') {
            $('.reachedCustomer').hide();
            $('.outcome').hide();
            $('.customerName').hide();
            $('.callDuration').hide();
            $('.phone').hide();
            $('.noticeEmail').hide();
            $('.blankGrid').hide();
            $('.activityType').hide();
            $('.eventID').hide();
            $('.email').hide();
            $('.actionID').show();
        } else if (type == 'NOTC1-PMTR' || type == 'NOTC2-OD' || type == 'NOTC3-DIST' || type == 'NOTC4-CANL') {
            $('.customerName').show();
            $('.email').hide();
            $('.noticeEmail').show();
            $('.reachedCustomer').hide();
            $('.outcome').hide();
            $('.eventID').hide();
            $('.blankGrid').hide();
            $('.callDuration').hide();
            $('.activityType').hide();
            $('.phone').hide();
            $('.actionID').show();
        } else if (type == 'RESTORE' || type == 'CEASE' || type == 'SUS' || type == 'SUSPEND') {
            $('.eventID').show();
            $('.activityType').show();
            $('.customerName').hide();
            $('.email').hide();
            $('.noticeEmail').hide();
            $('.reachedCustomer').hide();
            $('.outcome').hide();
            $('.blankGrid').hide();
            $('.callDuration').hide();
            $('.actionID').hide();
            $('.phone').hide();
        }
    }
};

function populateDataInRowExpansion(row, type) {
    Partial.Widgets.actionID.caption = row.id;
    Partial.Widgets.eventID.caption = row.id;

    var additionalChars = row.additionalCharacteristics;
    if (additionalChars != undefined) {
        additionalChars.forEach(populateAdditionalCharacteristicsInRowExpansion);
    }

    if (type == 'SUSPEND' || type == 'SUS') {
        Partial.Widgets.description.caption = 'Suspension';
    } else if (type == 'RESTORE') {
        Partial.Widgets.description.caption = 'Restorel';
    } else if (type == 'CEASE') {
        Partial.Widgets.description.caption = 'Cease';
    } else {
        Partial.Widgets.description.caption = 'Other';
    }
};


function populateAdditionalCharacteristicsInRowExpansion(item, index) {
    var item = item;
    if (item.name == "CustomerName") {
        Partial.Widgets.custName.caption = item.value;
    } else if (item.name == "PhoneNumber" || item.name == "Phone") {
        Partial.Widgets.phoneNum.caption = item.value;
    } else if (item.name == "CallDuration") {
        Partial.Widgets.callDuration.caption = item.value;
    } else if (item.name == "ReachedCustomer") {
        Partial.Widgets.reachedCust.caption = item.value;
        reachedCustomer = item.value;
    } else if (item.name == "Outcome") {
        Partial.Widgets.outcome.caption = item.value;
    } else if (item.name == "EmailAddress") {
        Partial.Widgets.email.caption = item.value;
        Partial.Widgets.noticeEmail.caption = item.value;
    }
};