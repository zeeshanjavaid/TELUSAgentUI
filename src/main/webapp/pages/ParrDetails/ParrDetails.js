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
    //alert("ParrId: " + Partial.Variables.getPaymentArrangement.dataSet.id);
    if (Partial.pageParams.ParrId) {

        Partial.Variables.getPaymentArrangement.setInput({
            "id": Partial.pageParams.ParrId
        });
        Partial.Variables.getPaymentArrangement.invoke();
        debugger;
    }
};

Partial.installmentScheduleExpand = function($event, widget) {
    let showIcon1 = document.querySelector('.show-icon1');
    let hideIcon1 = document.querySelector('.hide-icon1');
    if (hideIcon1) {
        hideIcon1.style.display = 'none';
    }
    if (showIcon1) {
        showIcon1.style.display = 'inline-block';
    }
    Partial.Variables.parrIsExpand.dataSet.dataValue = !Partial.Variables.parrIsExpand.dataSet.dataValue;
};

Partial.installmentScheduleCollapse = function($event, widget) {
    let showIcon1 = document.querySelector('.show-icon1');
    let hideIcon1 = document.querySelector('.hide-icon1');
    if (hideIcon1) {
        hideIcon1.style.display = 'inline-block';
    }
    if (showIcon1) {
        showIcon1.style.display = 'none';
    }
    Partial.Variables.parrIsExpand.dataSet.dataValue = !Partial.Variables.parrIsExpand.dataSet.dataValue;
};

Partial.parrHistoryExpand = function($event, widget) {
    let showIcon2 = document.querySelector('.show-icon2');
    let hideIcon2 = document.querySelector('.hide-icon2');
    if (hideIcon2) {
        hideIcon2.style.display = 'none';
    }
    if (showIcon2) {
        showIcon2.style.display = 'inline-block';
    }
    Partial.Variables.parrIsExpand.dataSet.dataValue = !Partial.Variables.parrIsExpand.dataSet.dataValue;
};

Partial.parrHistoryCollapse = function($event, widget) {
    let showIcon2 = document.querySelector('.show-icon2');
    let hideIcon2 = document.querySelector('.hide-icon2');
    if (hideIcon2) {
        hideIcon2.style.display = 'inline-block';
    }
    if (showIcon2) {
        showIcon2.style.display = 'none';
    }
    Partial.Variables.parrIsExpand.dataSet.dataValue = !Partial.Variables.parrIsExpand.dataSet.dataValue;
};

Partial.YesCancelButtonClick = function($event, widget) {
    Partial.Variables.updateParrStatus.setInput({

        'status': 'Cancel',
        'parrId': Partial.pageParams.ParrId
    });
    //Invoke POST updateParrStatus service
    Partial.Variables.updateParrStatus.invoke();
    App.Variables.successMessage.dataSet.dataValue = "PARR cancelled successfully."
    Partial.Variables.getPaymentArrangement.setInput({
        "id": Partial.pageParams.ParrId
    });
    Partial.Variables.getPaymentArrangement.invoke();
    setTimeout(messageTimeout, 5000);
};

Partial.RenegotiateParrAmountChange = function($event, widget, newVal, oldVal) {
    Partial.Variables.getPaymentArrangement.dataSet.amount = newVal;
    var size = Partial.Variables.ParrInstallmentSchedule.dataSet.length;
    var totalInstallmentAmt = 0;
    for (var i = 0; i < size; i++) {

        totalInstallmentAmt = parseInt(totalInstallmentAmt) + parseInt(Partial.Variables.ParrInstallmentSchedule.dataSet[i].amount);
    }
    Partial.Variables.ParrInstallmentSchedule.dataSet[size - 1].sequenceId = size;
    Partial.AmtOverUnder = parseInt(newVal) - totalInstallmentAmt;
};

Partial.getInstallmentScheduleTableRowupdate = function($event, widget, row) {
    var size = Partial.Variables.ParrInstallmentSchedule.dataSet.length;
    var totalInstallmentAmt = 0;
    for (var i = 0; i < size; i++) {

        totalInstallmentAmt = parseInt(totalInstallmentAmt) + parseInt(Partial.Variables.ParrInstallmentSchedule.dataSet[i].amount);
    }
    Partial.AmtOverUnder = parseInt(Partial.Widgets.ParrAmount.datavalue) - totalInstallmentAmt;
};

Partial.renegotiatePARRdialogOpened = function($event, widget) {

    Partial.Variables.successMessage.dataSet.dataValue = null;
    Partial.Variables.errorMsg.dataSet.dataValue = null;
    Partial.Variables.ParrInstallmentSchedule.dataSet.splice(0, Partial.Variables.ParrInstallmentSchedule.dataSet.length);
    Partial.Variables.ParrInstallmentSchedule.dataSet.push(...Partial.Variables.getPaymentArrangement.dataSet.installments);
    var size = Partial.Variables.ParrInstallmentSchedule.dataSet.length;
    var totalInstallmentAmt = 0;
    for (var i = 0; i < size; i++) {

        totalInstallmentAmt = parseInt(totalInstallmentAmt) + parseInt(Partial.Variables.ParrInstallmentSchedule.dataSet[i].amount);
    }
    Partial.AmtOverUnder = parseInt(Partial.Variables.getPaymentArrangement.dataSet.amount) - totalInstallmentAmt;
};

Partial.SubmitButtonClick = function($event, widget) {
    Partial.Variables.updatePaymentArrangement.setInput({
        "CollectionPaymentArrangementUpdate": {
            'amount': Partial.Variables.getPaymentArrangement.dataSet.amount,
            'installments': Partial.Variables.ParrInstallmentSchedule.dataSet,
            'id': Partial.pageParams.ParrId
        },
    });
    //Invoke POST UpdateParr service
    Partial.Variables.updatePaymentArrangement.invoke();
    App.Variables.successMessage.dataSet.dataValue = "PARR renegotiated successfully."
    Partial.Variables.getPaymentArrangement.setInput({
        "id": Partial.pageParams.ParrId
    });
    Partial.Variables.getPaymentArrangement.invoke();
    setTimeout(messageTimeout, 5000);
};
Partial.getInstallmentScheduleTableRowinsert = function($event, widget, row) {
    var size = Partial.Variables.ParrInstallmentSchedule.dataSet.length;
    var totalInstallmentAmt = 0;
    for (var i = 0; i < size; i++) {

        totalInstallmentAmt = parseInt(totalInstallmentAmt) + parseInt(Partial.Variables.ParrInstallmentSchedule.dataSet[i].amount);
    }
    Partial.Variables.ParrInstallmentSchedule.dataSet[size - 1].sequenceId = size;
    Partial.AmtOverUnder = parseInt(Partial.Widgets.ParrAmount.datavalue) - totalInstallmentAmt;
};

Partial.BackLinkClick = function($event, widget) {

    Partial.Variables.successMessage.dataSet.dataValue = null;
    Partial.Variables.errorMsg.dataSet.dataValue = null;
    Partial.Variables.ParrPageName.dataSet.dataValue = 'ParrList';
};

Partial.CancelParrClick = function($event, widget) {
    Partial.Variables.successMessage.dataSet.dataValue = null;
    Partial.Variables.errorMsg.dataSet.dataValue = null;
};

function messageTimeout() {
    Partial.Variables.successMessage.dataSet.dataValue = null;
}