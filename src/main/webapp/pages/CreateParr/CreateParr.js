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
    Partial.Variables.InstallmentOption.dataSet.datavalue = 'Number of Installments';
};

Partial.createInstalmntScheduleClick = function($event, widget) {

    var installmentSchedule = new Array();
    var amount = '';

    var installmentSize = '';
    //Psuedo code start
    if ('NumberOFinstallments') {
        installmentSize = Partial.Variables.NoOfInstallments.dataSet.datavalue;
        amount = Partial.Widgets.ParrTotal.datavalue / Partial.Variables.NoOfInstallments.dataSet.datavalue;
    } else {
        installmentSize = Partial.Widgets.ParrTotal.datavalue / Variables.AmountPerInstallment.dataSet.dataValue;
        amount = Variables.AmountPerInstallment.dataSet.dataValue;
    }
    // //Psuedo code End
    for (var i = 0; i < Partial.Variables.NoOfInstallments.dataSet.datavalue; i++) {

        var collectionPaymentInstallment = {};
        collectionPaymentInstallment.sequenceId = i + 1;
        var tempDate = '';
        if (i > 0) {
            tempDate = new Date(installmentSchedule[i - 1].date);
        } else {
            tempDate = new Date();
        }
        alert('tempDate:' + tempDate);
        alert("Recurrence : " + Partial.Widgets.RecurrenceDropdown.datavalue);
        if (Partial.Widgets.RecurrenceDropdown.datavalue == 'Weekly') {

            tempDate = new Date(tempDate.setDate(tempDate.getDate() + 7));
            collectionPaymentInstallment.date = new Date(tempDate);
            // collectionPaymentInstallment.date = tempDate.getMonth() + "/" + tempDate.getDate() + "/" + tempDate.getFullYear();
        } else if (Partial.Widgets.RecurrenceDropdown.datavalue == 'Bi-Weekly') {

            tempDate = new Date(tempDate.setDate(tempDate.getDate() + 15));
            collectionPaymentInstallment.date = new Date(tempDate);
            //collectionPaymentInstallment.date = tempDate.getMonth() + "/" + tempDate.getDate() + "/" + tempDate.getFullYear();
        } else if (Partial.Widgets.RecurrenceDropdown.datavalue == 'Monthly') {

            tempDate = new Date(tempDate.setMonth(tempDate.getMonth() + 1));
            collectionPaymentInstallment.date = new Date(tempDate);
            //collectionPaymentInstallment.date = tempDate.getMonth() + "/" + tempDate.getDate() + "/" + tempDate.getFullYear();
        } else {

            tempDate = new Date(tempDate.setMonth(tempDate.getMonth() + 1));
            collectionPaymentInstallment.date = new Date(tempDate);
            //collectionPaymentInstallment.date = tempDate.getMonth() + "/" + tempDate.getDate() + "/" + tempDate.getFullYear();
        }
        alert("collectionPaymentInstallment.date : " + collectionPaymentInstallment.date);

        collectionPaymentInstallment.amount = amount;
        if (i == 0) {
            collectionPaymentInstallment.cummPmtAmount = collectionPaymentInstallment.amount;
        } else {
            collectionPaymentInstallment.cummPmtAmount = installmentSchedule[i - 1].amount + collectionPaymentInstallment.amount;
        }
        installmentSchedule.push(collectionPaymentInstallment);
    }
    Partial.Variables.isCreateScheduleClicked.dataSet.datavalue = true;
    Partial.Variables.ParrInstallmentSchedule.dataSet = [];
    Partial.Variables.ParrInstallmentSchedule.dataSet.push(...installmentSchedule);
    alert("ParrInstallmentSchedule size fter push:" + Partial.Variables.ParrInstallmentSchedule.dataSet.length);
};

Partial.noOfInstlmntChange = function($event, widget, newVal, oldVal) {

    Partial.Variables.NoOfInstallments.dataSet.datavalue = newVal;
};

/*Partial.CancelClick = function($event, widget) {

    Partial.Variables.ParrPageName.dataSet.datavalue = 'ParrList';
};*/
Partial.RecurrenceDropdownChange = function($event, widget, newVal, oldVal) {

    alert("new recurrence :" + Partial.Widgets.RecurrenceDropdown.datavalue);
};

Partial.ParrTotalChange = function($event, widget, newVal, oldVal) {
    alert("ParrTotalChange :" + Partial.Widgets.ParrTotal.datavalue);
};