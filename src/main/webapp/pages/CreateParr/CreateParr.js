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

    debugger;

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
        var parrTotalVar = Partial.Widgets.ParrTotal.datavalue;
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
        var installmentCostWithoutLastone = 0;
        var collectionPaymentInstallment = {};
        var tempDate = '';
        var installmentSizeNewVar = installmentSize - 1;
        for (var i = 0; i < installmentSizeNewVar; i++) {

            var installmentCost = Math.floor(amount * 100) / 100;
            installmentCostWithoutLastone = installmentCostWithoutLastone + installmentCost;

            collectionPaymentInstallment.sequenceId = i + 1;

            /* if (i == installmentSize - 1 && remainder !== 0) {
                 amount = lastInstallmentAmount;
             } */
            if (i > 0) {
                tempDate = new Date(installmentSchedule[i - 1].date);
            } else {
                tempDate = new Date();
            }

            var installmentDateVar = getInstallmentDate(Partial.Widgets.RecurrenceDropdown.datavalue, tempDate);
            collectionPaymentInstallment.date = installmentDateVar;
            collectionPaymentInstallment.amount = installmentCost;
            //totalInstallmentAmt = totalInstallmentAmt + installmentCost;

            installmentSchedule.push(collectionPaymentInstallment);
            collectionPaymentInstallment = {};
        }

        //To populate last installment
        if (installmentSize == 1) {
            tempDate = new Date();
        }
        var lastInstallmentCost = parrTotalVar - installmentCostWithoutLastone;
        lastInstallmentCost = Math.round(lastInstallmentCost * 100) / 100;
        collectionPaymentInstallment.sequenceId = installmentSize;
        collectionPaymentInstallment.date = getInstallmentDate(Partial.Widgets.RecurrenceDropdown.datavalue, tempDate);
        collectionPaymentInstallment.amount = lastInstallmentCost;
        installmentSchedule.push(collectionPaymentInstallment);
        //To populate last installment ends
        totalInstallmentAmt = installmentCostWithoutLastone + lastInstallmentCost;
        totalInstallmentAmt = Math.round(totalInstallmentAmt * 100) / 100;
        Partial.Variables.AmtOverUnder.dataSet.dataValue = Partial.Widgets.ParrTotal.datavalue - totalInstallmentAmt;
        Partial.Variables.isCreateScheduleClicked.dataSet.datavalue = 'true';
        Partial.Variables.ParrInstallmentSchedule.dataSet.splice(0, Partial.Variables.ParrInstallmentSchedule.dataSet.length);
        Partial.Variables.ParrInstallmentSchedule.dataSet.push(...installmentSchedule);
        App.Variables.errorMsg.dataSet.dataValue = "";
    }
    //App.Variables.errorMsg.dataSet.dataValue = null;

};

function getInstallmentDate(recurrence, tempDate) {
    debugger;
    var collectionPaymentDate;
    if (recurrence == 'Weekly') {

        tempDate = new Date(tempDate.setDate(tempDate.getDate() + 7));
        collectionPaymentDate = new Date(tempDate).toJSON().slice(0, 10);
    } else if (recurrence == 'Bi-Weekly') {

        tempDate = new Date(tempDate.setDate(tempDate.getDate() + 15));
        collectionPaymentDate = new Date(tempDate).toJSON().slice(0, 10);
    } else if (recurrence == 'Monthly') {

        tempDate = new Date(tempDate.setMonth(tempDate.getMonth() + 1));
        collectionPaymentDate = new Date(tempDate).toJSON().slice(0, 10);
    } else {

        tempDate = new Date(tempDate.setDate(tempDate.getDate() + 1));
        collectionPaymentDate = new Date(tempDate).toJSON().slice(0, 10);
    }
    return collectionPaymentDate;
}


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

Partial.InstallmentOptionRadioChange = function($event, widget, newVal, oldVal) {

    Partial.Widgets.noOfInstlmnt.datavalue = 0;
    Partial.Widgets.amountPerInstlmnt.datavalue = 0;
};

Partial.Clear = function() {
    //App.Variables.errorMsg.dataSet.dataValue = null;
    Partial.Variables.isCreateScheduleClicked.dataSet.datavalue = false;
    Partial.Widgets.noOfInstlmnt.datavalue = 0;
    Partial.Widgets.amountPerInstlmnt.datavalue = 0;
    Partial.Widgets.RecurrenceDropdown.datavalue = '';
    Partial.Widgets.ParrTotal.datavalue = '';
    Partial.Variables.installmentBANCreateParr.dataSet = [];
    Partial.Widgets.InstallmentOptionRadio.datavalue = '';
    Partial.Widgets.Comments.datavalue = '';
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
        var banRefIdInt = selectedBan.banRefId
        entityRef.id = banRefIdInt.toString();
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
    debugger;
    var size = Partial.Variables.ParrInstallmentSchedule.dataSet.length;
    var totalInstallmentAmt = 0;
    for (var i = 0; i < size; i++) {

        totalInstallmentAmt = parseFloat(totalInstallmentAmt) + parseFloat(Partial.Variables.ParrInstallmentSchedule.dataSet[i].amount);
    }
    Partial.Variables.AmtOverUnder.dataSet.dataValue = Math.round((parseFloat(Partial.Widgets.ParrTotal.datavalue) - totalInstallmentAmt) * 100) / 100;

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

    debugger;

    var installmentLength = Partial.Variables.ParrInstallmentSchedule.dataSet.length;
    var installmentScheduleDataSet = Partial.Variables.ParrInstallmentSchedule.dataSet;
    var breakCheck1 = false;
    for (i = 0; i < installmentLength; i++) {
        for (k = i + 1; k < installmentLength; k++) {
            if (installmentScheduleDataSet[i].date == installmentScheduleDataSet[k].date) {
                breakCheck1 = true;
                break;
            }
        }
        if (breakCheck1) break;
    }

    if (breakCheck1) {
        Partial.Variables.errormessageInstallmentSchedule.dataSet.dataValue = "Installment dates cannot be same. Please provide different installment dates";
        setTimeout(messageTimeout, 6000);
    } else {
        Partial.Variables.errormessageInstallmentSchedule.dataSet.dataValue = null;
        Partial.Variables.CreatePaymentArrangement.setInput({
            "CollectionPaymentArrangementCreate": {
                'collectionEntity': {
                    'id': Partial.pageParams.entityId
                },
                'amount': Partial.Widgets.ParrTotal.datavalue,
                'billingAccountRefs': Partial.Variables.SelectedBans.dataSet,
                'installments': Partial.Variables.ParrInstallmentSchedule.dataSet,
                'recurrence': Partial.Widgets.RecurrenceDropdown.datavalue,
                'comment': Partial.Widgets.Comments.datavalue,
                "channel": {
                    "userId": App.Variables.getLoggedInUserDetails.dataSet.emplId,
                    "originatorAppId": "FAWBTELUSAGENT"
                }
            }
        });

        //Invoke POST createDispute service
        Partial.Variables.CreatePaymentArrangement.invoke();
    }

};
Partial.button2_1Click = function($event, widget) {
    var entityIdStr = Partial.pageParams.entityId
    var entityIdInt = parseInt(entityIdStr);
    Partial.Variables.getEntityBanDetailsForParr.setInput({
        'entityId': entityIdInt
    });

    Partial.Variables.getEntityBanDetailsForParr.invoke();

    Partial.Widgets.selectBANdialog.open();

};

function messageTimeout() {
    Partial.Variables.successMessage.dataSet.dataValue = null;
    Partial.Variables.errorMsg.dataSet.dataValue = null;
    Partial.Variables.errormessageInstallmentSchedule.dataSet.dataValue = null;
}

Partial.CreatePaymentArrangementonError = function(variable, data, xhrObj) {
    debugger;
    //var errorObj = xhrObj.error;
    var reasonIndex = xhrObj.error.replaceAll('\\', '').indexOf("reason") + 9;
    var codeIndex = xhrObj.error.replaceAll('\\', '').lastIndexOf("code") - 3;
    var errorMessage = xhrObj.error.replaceAll('\\', '').substring(reasonIndex, codeIndex);
    // App.Variables.errorMsg.dataSet.dataValue = "PARR creation failed as Multiple Payment arrangements are not allowed"
    App.Variables.errorMsg.dataSet.dataValue = errorMessage;
    // Partial.Variables.ParrPageName.dataSet.dataValue = 'ParrList';
    //    Partial.Clear();
    setTimeout(messageTimeout, 5000);
};

Partial.CreatePaymentArrangementonSuccess = function(variable, data) {
    debugger;
    App.Variables.successMessage.dataSet.dataValue = "PARR created successfully"
    Partial.Variables.ParrPageName.dataSet.dataValue = 'ParrList';
    Partial.Clear();
    App.refreshParrList();
    // App.refreshParrSummary();
    // App.refreshEntProfCancelParrSummary();
    setTimeout(messageTimeout, 5000);
};

Partial.getEntityBanDetailsForParronError = function(variable, data, xhrObj) {

};
Partial.installmentScheduleTable_dateOnChange = function($event, widget, row) {
    debugger;
    var widgetDateValue = widget.bsDataValue;
    //var widgetDate = new Date(widgetDateValue);
    //widgetDate.setHours(0, 0, 0, 0);
    var currentDate = new Date();
    widgetDate = new Date(widgetDateValue.toDateString());
    currentDate = new Date(currentDate.toDateString());
    //currentDate.setHours(0, 0, 0, 0);
    if (widgetDate.valueOf() < currentDate.valueOf()) {
        Partial.Variables.errormessageInstallmentSchedule.dataSet.dataValue = "Installment date cannot be less than current date";
        $(".cancel.row-action-button.btn.app-button.btn-danger.cancel-edit-row-button.btn-xs").click();
        setTimeout(messageTimeout, 6000);
    } else {
        Partial.Variables.errormessageInstallmentSchedule.dataSet.dataValue = null;
    }

};

App.Clear = function() {
    debugger;
    Partial.Widgets.ParrTotal.datavalue = Partial.pageParams.parrTotal;
    Partial.Variables.isCreateScheduleClicked.dataSet.datavalue = false;
    Partial.Widgets.noOfInstlmnt.datavalue = 0;
    Partial.Widgets.amountPerInstlmnt.datavalue = 0;
    Partial.Widgets.RecurrenceDropdown.datavalue = '';
    Partial.Variables.installmentBANCreateParr.dataSet = [];
    Partial.Widgets.InstallmentOptionRadio.datavalue = '';
    Partial.Widgets.Comments.datavalue = '';
};