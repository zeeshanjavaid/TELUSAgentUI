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



    // console.log(Partial.Variables.CollectionDataServiceGetEntityDetails.dataSet);
    // debugger;

    App.refreshEntProfCancelParrSummary();
};

Partial.button1Click = function($event, widget) {
    debugger;

    Partial.Widgets.createNoteButton.hidePopover();

    Partial.Widgets.CreateUserNotesdialog1.open();


};


App.refreshEntProfCancelParrSummary = function() {
    var getPayArrangForEntityProfileCancelledVar = Partial.Variables.getPayArrangForEntityProfileCancelled;

    getPayArrangForEntityProfileCancelledVar.invoke({
            "inputFields": {
                "entityId": Partial.pageParams.entityId,
                "status": 'Open'
            },
        },
        function(data) {
            debugger;
            if (data.length > 0) {
                Partial.Widgets.parrSummaryEntProfCancId.caption = data[0].id;
                Partial.Widgets.totalAmtEntProfCancParrSumm.caption = '$' + data[0].amount;
                Partial.Widgets.parrSumEntProfCancStatus.caption = 'Active';
                Partial.Widgets.cummPayEntProfCancExp.caption = '$' + data[0].expectedPaymentAmountToDate;
                Partial.Widgets.cummPmtEntProfCancRvcd.caption = '$' + data[0].receivedPaymentAmountToDate;
                Partial.Widgets.recurEntProfCancParrSumm.caption = data[0].recurrence;
                Partial.Widgets.evalResEntProfCancParrSum.caption = data[0].evaluationResult;
                var installmentLength = data[0].installments.length;
                Partial.Widgets.NoOfInstallEntProfCancParrSum.caption = data[0].installments[installmentLength - 1].sequenceId;
                if (data[0].receivedPaymentAmountToDate != 0.0) {
                    Partial.Widgets.percPymtVsExpRcvdEntProfCanc.caption = ((data[0].expectedPaymentAmountToDate / data[0].receivedPaymentAmountToDate) * 100);
                } else {
                    Partial.Widgets.percPymtVsExpRcvdEntProfCanc.caption = 0.0;
                }

                var totalInstallmentIntermediateAmt = 0;
                data[0].installments.forEach(function(d) {
                    totalInstallmentIntermediateAmt = parseFloat(totalInstallmentIntermediateAmt) + parseFloat(d.amount);
                });

                var totalInstallmentAmount = Math.round((totalInstallmentIntermediateAmt) * 100) / 100;

                Partial.Widgets.installAmtEntProfCancParrSum.caption = '$' + totalInstallmentAmount;

                Partial.Widgets.parrSummaryEntProfCancId.show = true;
                Partial.Widgets.parrSummaryEntProfCancIdLabel.show = true;
                Partial.Widgets.parrSumStatusEntProfCancLabel.show = true;
                Partial.Widgets.parrSumEntProfCancStatus.show = true;
                Partial.Widgets.evalResEntProfCancParrSumLabel.show = true;
                Partial.Widgets.evalResEntProfCancParrSum.show = true;
                Partial.Widgets.recurEntProfCancParrSummLbl.show = true;
                Partial.Widgets.recurEntProfCancParrSumm.show = true;
                Partial.Widgets.NoOfInstallEntProfCancParrSumLbl.show = true;
                Partial.Widgets.NoOfInstallEntProfCancParrSum.show = true;
                Partial.Widgets.percPymtVsExpRcvdEntProfCanc.show = true;
                Partial.Widgets.percPymtVsExpRcvdEntProfCancLbl.show = true;
                Partial.Widgets.cummPmtEntProfCancRvcd.show = true;
                Partial.Widgets.cummPmtEntProfCancRvcdLbl.show = true;
                Partial.Widgets.cummPayEntProfCancExp.show = true;
                Partial.Widgets.cummPayEntProfCancExpLabel.show = true;
                Partial.Widgets.totalAmtEntProfCancParrSumm.show = true;
                Partial.Widgets.totalAmtEntProfCancParrSummLbl.show = true;
                Partial.Widgets.installAmtEntProfCancParrSumLbl.show = true;
                Partial.Widgets.installAmtEntProfCancParrSum.show = true;
                Partial.Widgets.ParrSummaryNotActive.show = false;
                Partial.Widgets.createParrInEntityProfileCancel.show = false;

            } else {
                Partial.Widgets.parrSummaryEntProfCancId.show = false;
                Partial.Widgets.parrSummaryEntProfCancIdLabel.show = false;
                Partial.Widgets.parrSumStatusEntProfCancLabel.show = false;
                Partial.Widgets.parrSumEntProfCancStatus.show = false;
                Partial.Widgets.evalResEntProfCancParrSumLabel.show = false;
                Partial.Widgets.evalResEntProfCancParrSum.show = false;
                Partial.Widgets.recurEntProfCancParrSummLbl.show = false;
                Partial.Widgets.recurEntProfCancParrSumm.show = false;
                Partial.Widgets.NoOfInstallEntProfCancParrSumLbl.show = false;
                Partial.Widgets.NoOfInstallEntProfCancParrSum.show = false;
                Partial.Widgets.percPymtVsExpRcvdEntProfCanc.show = false;
                Partial.Widgets.percPymtVsExpRcvdEntProfCancLbl.show = false;
                Partial.Widgets.cummPmtEntProfCancRvcd.show = false;
                Partial.Widgets.cummPmtEntProfCancRvcdLbl.show = false;
                Partial.Widgets.cummPayEntProfCancExp.show = false;
                Partial.Widgets.cummPayEntProfCancExpLabel.show = false;
                Partial.Widgets.totalAmtEntProfCancParrSumm.show = false;
                Partial.Widgets.totalAmtEntProfCancParrSummLbl.show = false;
                Partial.Widgets.installAmtEntProfCancParrSumLbl.show = false;
                Partial.Widgets.installAmtEntProfCancParrSum.show = false;
                Partial.Widgets.ParrSummaryNotActive.show = true;
                Partial.Widgets.createParrInEntityProfileCancel.show = true;
            }

        },
        function(error) {
            // Error Callback
            console.log("error", error);
        }
    );
}
Partial.createParrInEntityProfileCancelClick = function($event, widget) {
    App.Widgets.ActiveEntity.isActive = false;
    App.Widgets.Parr.isActive = true;
    App.Widgets.Parr.show = true;
    Partial.Variables.ParrPageName.dataSet.dataValue = 'CreateParr';

};