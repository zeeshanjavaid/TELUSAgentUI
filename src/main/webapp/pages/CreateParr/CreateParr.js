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
    var amount = Partial.Widgets.ParrTotal.datavalue / Partial.Variables.NoOfInstallments.dataSet.datavalue;
    for (var i = 0; i < Partial.Variables.NoOfInstallments.dataSet.datavalue; i++) {

        var collectionPaymentInstallment = {};
        collectionPaymentInstallment.sequenceId = i;
        var tempDate = new Date();
        collectionPaymentInstallment.date = new Date(tempDate.setMonth(tempDate.getMonth() + i));
        collectionPaymentInstallment.amount = amount;
        if (i == 0) {
            collectionPaymentInstallment.cummPmtAmount = collectionPaymentInstallment.amount;
        } else {
            collectionPaymentInstallment.cummPmtAmount = installmentSchedule[i - 1].amount + collectionPaymentInstallment.amount;
        }
        alert("CummAmount : " + collectionPaymentInstallment.cummPmtAmount);
        installmentSchedule.push(collectionPaymentInstallment);
        alert("installmentSchedule length : " +
            installmentSchedule.length);
    }
    alert("Schedule size :" + installmentSchedule.length);
    Partial.Variables.ParrInstallmentSchedule.dataSet.push(...installmentSchedule);
    alert("ParrInstallmentSchedule size :" + Partial.Variables.ParrInstallmentSchedule.length);

};

Partial.noOfInstlmntChange = function($event, widget, newVal, oldVal) {

    Partial.Variables.NoOfInstallments.dataSet.datavalue = newVal;
};