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
    //Partial.Widgets.InstallmentOptions.dataValue = 'NoOfInstallments';
    App.Variables.errorMsg.dataSet.dataValue = null;
    App.Variables.successMessage.dataSet.dataValue = null;
    Partial.Widgets.ParrTotal.datavalue = Partial.pageParams.parrTotal;
};

Partial.createInstalmntScheduleClick = function($event, widget) {

    if (Partial.Widgets.ParrTotal._datavalue == "" || Partial.Widgets.ParrTotal._datavalue == null) {
        App.Variables.errorMsg.dataSet.dataValue = "ParrTotal is mandatory";
    } else if (Partial.Widgets.RecurrenceDropdown.datavalue == undefined || Partial.Widgets.RecurrenceDropdown.datavalue == "") {
        App.Variables.errorMsg.dataSet.dataValue = "Recurrence is mandatory";
    } else if (Partial.Variables.installmentBANCreateParr.dataSet.length == 0) {
        App.Variables.errorMsg.dataSet.dataValue = "Installment Ban is mandatory";
    } else if (Partial.Widgets.InstallmentOptionRadio.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Installment type is mandatory";
    } else if (Partial.Widgets.InstallmentOptionRadio.datavalue == 'NoOfInstallments' && Partial.Variables.NoOfInstallments.dataSet.dataValue <= 0) {
        App.Variables.errorMsg.dataSet.dataValue = "Number of Installments is mandatory";
    } else if (Partial.Widgets.InstallmentOptionRadio.datavalue == 'AmtPerInstallment' && Partial.Variables.AmountPerInstallment.dataSet.dataValue <= 0) {
        App.Variables.errorMsg.dataSet.dataValue = "Amount per Installment is mandatory";
    } else {

        document.getElementById("cancelButton").style.display = "none";

        var installmentSchedule = new Array();
        var amount = '';
        var remainder = 0;
        var installmentSize = '';
        var totalInstallmentAmt = 0;
        //Psuedo code start
        //check if numOfInstallmetn is selected 
        if (Partial.Widgets.InstallmentOptionRadio.datavalue == 'NoOfInstallments') {
            installmentSize = Partial.Variables.NoOfInstallments.dataSet.datavalue;
            amount = Partial.Widgets.ParrTotal.datavalue / Partial.Variables.NoOfInstallments.dataSet.datavalue;
        }
        //check if amtPerInstallment is selected 
        if (Partial.Widgets.InstallmentOptionRadio.datavalue == 'AmtPerInstallment') {
            remainder = Partial.Widgets.ParrTotal.datavalue % Partial.Variables.AmountPerInstallment.dataSet.dataValue;
            installmentSize = parseInt(Partial.Widgets.ParrTotal.datavalue / Partial.Variables.AmountPerInstallment.dataSet.dataValue);
            amount = Partial.Variables.AmountPerInstallment.dataSet.dataValue;
            var lastInstallmentAmount = 0;
            if (remainder !== 0) {
                installmentSize = installmentSize + 1;
                lastInstallmentAmount = remainder;
                //input logic to add remainder into the last row of the collection PaymentInstallment, if it exists 
            }
            //ask abou installment Schedule size limitations 
        }
        // //Psuedo code End
        for (var i = 0; i < installmentSize; i++) {

            var collectionPaymentInstallment = {};
            collectionPaymentInstallment.sequenceId = i + 1;
            var tempDate = '';
            if (i == installmentSize - 1 && remainder !== 0) {
                amount = lastInstallmentAmount;
            }
            if (i > 0) {
                tempDate = new Date(installmentSchedule[i - 1].date);
            } else {
                tempDate = new Date();
            }
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

                tempDate = new Date(tempDate.setDate(tempDate.getDate() + 1));
                collectionPaymentInstallment.date = new Date(tempDate);
                //collectionPaymentInstallment.date = tempDate.getMonth() + "/" + tempDate.getDate() + "/" + tempDate.getFullYear();
            }

            collectionPaymentInstallment.amount = amount;
            if (i === 0) {
                //collectionPaymentInstallment.cummPmtAmount = collectionPaymentInstallment.amount;
            } else {
                //collectionPaymentInstallment.cummPmtAmount = installmentSchedule[i - 1].amount + collectionPaymentInstallment.amount;
            }
            totalInstallmentAmt = totalInstallmentAmt + amount;

            installmentSchedule.push(collectionPaymentInstallment);
        }
        Partial.AmtOverUnder = Partial.Widgets.ParrTotal.datavalue - totalInstallmentAmt;
        Partial.Variables.isCreateScheduleClicked.dataSet.datavalue = 'true';
        Partial.Variables.ParrInstallmentSchedule.dataSet.splice(0, Partial.Variables.ParrInstallmentSchedule.dataSet.length);
        Partial.Variables.ParrInstallmentSchedule.dataSet.push(...installmentSchedule);
        App.Variables.errorMsg.dataSet.dataValue = "";
    }
    //App.Variables.errorMsg.dataSet.dataValue = null;
};

Partial.noOfInstlmntChange = function($event, widget, newVal, oldVal) {

    Partial.Variables.NoOfInstallments.dataSet.datavalue = newVal;
};

Partial.CancelClick = function($event, widget) {
    App.Variables.errorMsg.dataSet.dataValue = null;
    document.getElementById("cancelButton").style.display = "inline";

    Partial.Variables.isCreateScheduleClicked.dataSet.datavalue = false;
    Partial.Widgets.noOfInstlmnt.datavalue = 0;
    Partial.Widgets.amountPerInstlmnt.datavalue = 0;
    Partial.Widgets.RecurrenceDropdown.datavalue = '';
    Partial.Widgets.ParrTotal.datavalue = '';
    Partial.Variables.installmentBANCreateParr.dataSet = [];
    Partial.Widgets.InstallmentOptionRadio.datavalue = '';
    Partial.Variables.ParrPageName.dataSet.dataValue = 'ParrList';
};
Partial.RecurrenceDropdownChange = function($event, widget, newVal, oldVal) {

};
Partial.ParrTotalChange = function($event, widget, newVal, oldVal) {};

Partial.InstallmentOptionRadioChange = function($event, widget, newVal, oldVal) {

    Partial.Widgets.noOfInstlmnt.datavalue = 0;
    Partial.Widgets.amountPerInstlmnt.datavalue = 0;
};

Partial.Clear = function() {
    App.Variables.errorMsg.dataSet.dataValue = null;
    Partial.Variables.isCreateScheduleClicked.dataSet.datavalue = false;
    Partial.Widgets.noOfInstlmnt.datavalue = 0;
    Partial.Widgets.amountPerInstlmnt.datavalue = 0;
    Partial.Widgets.RecurrenceDropdown.datavalue = '';
    Partial.Widgets.ParrTotal.datavalue = '';
    Partial.Variables.installmentBANCreateParr.dataSet = [];
    Partial.Widgets.InstallmentOptionRadio.datavalue = '';
};

Partial.ClearScheduleClick = function($event, widget) {
    Partial.Widgets.Comments.datavalue = '';
};

/*Partial.installmentScheduleTable_amountOnChange = function($event, widget, row) {

    var size = Partial.Variables.ParrInstallmentSchedule.dataSet.length;
    var totalInstallmentAmt = 0;
    for (var i = 0; i < size; i++) {

        totalInstallmentAmt = totalInstallmentAmt + Partial.Variables.ParrInstallmentSchedule[i].amount;
    }
    alert("totalInstallmentAmt :" + totalInstallmentAmt);
};*/

//selectBanParrTable1
/*Partial.button4Click = function($event, widget) {
    debugger;
    Partial.Variables.installmentBANCreateParr.dataSet = [];
    //BANName
    Partial.Widgets.selectBanParrTable1.selectedItems.forEach(function(a) {

        Partial.obj = {
            "BAN": a.BAN,
            "BANName": a.BANName
        }
        Partial.Variables.installmentBANCreateParr.dataSet.push(Partial.obj);
    })

    Partial.Widgets.dialog1.close();
};*/
Partial.SubmitBanClick = function($event, widget) {

    Partial.Variables.installmentBANCreateParr.dataSet = [];
    var selectedBans = new Array();
    var parrTotal = 0;
    Partial.Variables.installmentBANCreateParr.dataSet.push(...Partial.Widgets.selectBanParrTable1.selectedItems);
    Partial.Widgets.selectBanParrTable1.selectedItems.forEach(function(selectedBan) {

        var entityRef = {};
        entityRef.id = selectedBan.banMapRefId;
        entityRef.name = selectedBan.banName;
        parrTotal = parrTotal + selectedBan.banArAmount;
        selectedBans.push(entityRef);
    });
    Partial.Variables.SelectedBans.dataSet.splice(0, Partial.Variables.SelectedBans.dataSet.length);
    Partial.Variables.SelectedBans.dataSet.push(...selectedBans);
    Partial.Widgets.ParrTotal.datavalue = parrTotal;
    Partial.Widgets.selectBANdialog.close();
};
Partial.installmentScheduleTableRowupdate = function($event, widget, row) {
    var size = Partial.Variables.ParrInstallmentSchedule.dataSet.length;
    var totalInstallmentAmt = 0;
    for (var i = 0; i < size; i++) {

        totalInstallmentAmt = parseInt(totalInstallmentAmt) + parseInt(Partial.Variables.ParrInstallmentSchedule.dataSet[i].amount);
    }
    Partial.AmtOverUnder = parseInt(Partial.Widgets.ParrTotal.datavalue) - totalInstallmentAmt;

};

Partial.CancelInstallmentScheduleClick = function($event, widget) {
    document.getElementById("cancelButton").style.display = "inline";
    Partial.Variables.isCreateScheduleClicked.dataSet.datavalue = false;
    Partial.Widgets.noOfInstlmnt.datavalue = 0;
    Partial.Widgets.amountPerInstlmnt.datavalue = 0;
    Partial.Widgets.RecurrenceDropdown.datavalue = '';
    Partial.Widgets.ParrTotal.datavalue = '';
    Partial.Variables.installmentBANCreateParr.dataSet = [];
    Partial.Widgets.InstallmentOptionRadio.datavalue = '';
    Partial.Variables.ParrPageName.dataSet.dataValue = 'ParrList';
    App.Variables.errorMsg.dataSet.dataValue = "";
};

Partial.CreatePARRClick = function($event, widget) {
    Partial.Variables.CreatePaymentArrangement.setInput({
        "CollectionPaymentArrangementCreate": {
            'amount': Partial.Widgets.ParrTotal.datavalue,
            'billingAccountRefs': Partial.Variables.SelectedBans.dataSet,
            'installments': Partial.Variables.ParrInstallmentSchedule.dataSet,
            'recurrence': Partial.Widgets.RecurrenceDropdown.datavalue
        },
        "entityId": Partial.pageParams.entityId
    });
    //Invoke POST createDispute service
    Partial.Variables.CreatePaymentArrangement.invoke();
    App.Variables.successMessage.dataSet.dataValue = "PARR created successfully"
    Partial.Variables.ParrPageName.dataSet.dataValue = 'ParrList';
    Partial.Clear();
    App.refreshParrList();
};