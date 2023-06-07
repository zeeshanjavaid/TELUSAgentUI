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
        debugger;
        var type = row.stepTypeCode;
        Partial.Widgets.status.caption = row.status;
        Partial.Widgets.actionID.caption = row.id;
        Partial.Widgets.description.caption = row.comment;

        if (!type == 'RESTORE' || !type == 'CEASE' || !type == 'SUS' || !type == 'SUSPEND') {
            var additionalChars = row.additionalCharacteristics;
            additionalChars.forEach(populateDataInRowExpansion);
        }

        if (type == 'CALL-OB') {
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
        } else if (type == 'CALL-OB' && reachedCustomer == 'No') {
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
            $('.email').show();
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
        var type = row.collectionActivityType;;
        if (type == 'CALL-OB') {
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
        } else if (type == 'CALL-OB' && reachedCustomer == 'No') {
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
            $('.email').show();
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

function populateDataInRowExpansion(item, index) {
    debugger;
    var item = item;
    if (item.name == "CustomerName") {
        Partial.Widgets.custName.caption = item.value;
    } else if (item.name == "PhoneNumber") {
        Partial.Widgets.phoneNum.caption = item.value;
    } else if (item.name == "CallDuration") {
        Partial.Widgets.callDuration.caption = item.value;
    } else if (item.name == "ReachedCustomer") {
        Partial.Widgets.reachedCust.caption = item.value;
        reachedCustomer = item.value;
    } else if (item.name == "Email") {
        Partial.Widgets.email.caption = item.value;
    } else if (item.name == "EventID") {
        Partial.Widgets.eventID.caption = item.value;
    } else if (item.name == "ActivityType") {
        Partial.Widgets.activityType.caption = item.value;
    } else if (item.name == "NoticeEmail") {
        Partial.Widgets.noticeEmail.caption = item.value;
    }

}