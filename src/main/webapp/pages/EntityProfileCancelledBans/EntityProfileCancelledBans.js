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
var documentId = "";
/* perform any action on widgets/variables within this block */
Partial.onReady = function() {

    documentId = "";
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

    Partial.Variables.getLatestNotes_ByEntityId.setInput({

        'entityId': Partial.pageParams.entityId

    });
    Partial.Variables.getLatestNotes_ByEntityId.invoke();

    App.refreshEntProfCancelParrSummary();
};

Partial.button1Click = function($event, widget) {
    debugger;

    Partial.Widgets.createNoteButton.hidePopover();
    Partial.Variables.errorMsg.dataSet.dataValue = "";
    Partial.Variables.successMessage.dataSet.dataValue = "";
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


Partial.notes1_buttonClick = function($event, widget) {

    var docId = Partial.Variables.getLatestNotes_ByEntityId.dataSet[0].docId;

    Partial.Variables.getDocument_ByDocId.setInput({

        'docId': docId

    });
    Partial.Variables.getDocument_ByDocId.invoke({},
        function(data) {

            $event.preventDefault();
            download(data.content[0].document, data.content[0].documentName)
        },
        function(error) {
            // Error Callback
            console.log("error", error)

        });
};
Partial.notes2_buttonClick = function($event, widget) {

    var docId = Partial.Variables.getLatestNotes_ByEntityId.dataSet[1].docId;

    Partial.Variables.getDocument_ByDocId.setInput({

        'docId': docId

    });
    Partial.Variables.getDocument_ByDocId.invoke({},
        function(data) {
            download(data.content[0].document, data.content[0].documentName)
        },
        function(error) {
            // Error Callback
            console.log("error", error)

        });
};
Partial.notes3_buttonClick = function($event, widget) {

    var docId = Partial.Variables.getLatestNotes_ByEntityId.dataSet[2].docId;

    Partial.Variables.getDocument_ByDocId.setInput({

        'docId': docId

    });


    Partial.Variables.getDocument_ByDocId.invoke({},
        function(data) {
            // Success Callback
            debugger;
            console.log("success", data);
            $event.preventDefault();
            download(data.content[0].document, data.content[0].documentName)


        },
        function(error) {
            // Error Callback      
            console.log("error", error)

        });
};

const download = async(url, filename) => {
    const data = await fetch(url)
    const blob = await data.blob()
    const objectUrl = URL.createObjectURL(blob)

    const link = document.createElement('a')

    link.setAttribute('href', objectUrl)
    link.setAttribute('download', filename)
    link.style.display = 'none'

    document.body.appendChild(link)

    link.click()

    document.body.removeChild(link)
}

Partial.getBanDetailsFor_NotesonError = function(variable, data, xhrObj) {

};

Partial.associateBAN_anchorClick = function($event, widget) {

    Partial.Variables.getBanDetailsForEntityCancel.setInput({

        'entityId': window.location.href.toString().split("=")[1]

    });
    Partial.Variables.getBanDetailsForEntityCancel.invoke();
    document.getElementById("associateBanLink").style.color = 'gray';
    Partial.Variables.BanList_Show.dataSet.dataValue = false;
};

Partial.clear_BananchorClick = function($event, widget) {

    Partial.Widgets.getEntityDetailsTable1_1.selecteditem = false;
};

Partial.cancle_BanbuttonClick = function($event, widget) {

    Partial.Variables.errorMsg.dataSet.dataValue = "";
    Partial.Variables.BanList_Show.dataSet.dataValue = true;
};
Partial.create_BanbuttonClick = function($event, widget) {

    documentId = "";
    const message = document.getElementById("p01");
    message.innerHTML = "";


    if (Partial.Widgets.fileupload.selectedFiles != undefined) {
        var entityId = Partial.pageParams.entityId;

        var docName = Partial.Widgets.fileupload.selectedFiles[0].name;

        var docSize = Partial.Widgets.fileupload.selectedFiles[0].size;

        if (Partial.Widgets.textarea1.datavalue != undefined) {
            //Restricting it to 500KB
            if (docSize <= 512000) {

                Partial.Variables.save_Document.setInput({
                    'documentName': docName,
                    'document': Partial.Widgets.fileupload.selectedFiles[0]
                });



                Partial.Variables.save_Document.invoke({},
                    function(data) {
                        // Success Callback
                        debugger;
                        console.log("success", data);
                        documentId = data.id;

                        //for note save
                        const message = document.getElementById("p01");
                        message.innerHTML = "";

                        if (Partial.Widgets.textarea1.datavalue == undefined || Partial.Widgets.textarea1.datavalue == "") {
                            try {
                                isError = true;
                                Partial.Variables.errorMsg.dataSet.dataValue = "Please enter the note";
                            } catch (err) {
                                message.innerHTML = err;
                            }
                        } else {
                            var banId = Partial.Widgets.getEntityDetailsTable1_1.selecteditem.banId;
                            var note = Partial.Widgets.textarea1.datavalue;
                            var entityId = Partial.pageParams.entityId;

                            Partial.Variables.save_Notes.setInput({

                                'entityId': entityId,
                                'banId': banId,
                                'notes': note,
                                'docId': documentId,
                                'createdByEmplId': App.Variables.getLoggedInUserDetails.dataSet.firstName.concat(" " + App.Variables.getLoggedInUserDetails.dataSet.lastName)


                            });



                            Partial.Variables.save_Notes.invoke({},
                                function(data) {
                                    // Success Callback
                                    debugger;
                                    console.log("success", data);
                                    documentId = "";
                                    // App.pageRefresh();
                                    Partial.Variables.successMessage.dataSet.dataValue = "Note created successfully";
                                    Partial.Variables.errorMsg.dataSet.dataValue = "";
                                    setTimeout(function() {
                                        Partial.Widgets.CreateUserNotesdialog1.close();

                                    }, 1000);
                                    // window.location.reload();
                                    //  Partial.Variables.getLatestNotesByEntityId.invoke();
                                    Partial.Variables.getLatestNotes_ByEntityId.setInput({

                                        'entityId': Partial.pageParams.entityId;

                                    });
                                    Partial.Variables.getLatestNotes_ByEntityId.invoke();

                                    App.refreshLatestNotesAndDoc();

                                },
                                function(error) {
                                    // Error Callback
                                    console.log("error", error)

                                });

                        }



                    },
                    function(error) {
                        // Error Callback
                        console.log("error", error)

                    });

            } else {
                Partial.Variables.errorMsg.dataSet.dataValue = "Attached file is greater than the specified limit.";
            }
        } else {
            Partial.Variables.errorMsg.dataSet.dataValue = "Please enter the note";
        }
    } else {


        //for note save
        const message = document.getElementById("p01");
        message.innerHTML = "";

        if (Partial.Widgets.textarea1.datavalue == undefined || Partial.Widgets.textarea1.datavalue == "") {
            Partial.Variables.errorMsg.dataSet.dataValue = "Please enter the note";

        } else {
            var banId = Partial.Widgets.getEntityDetailsTable1_1.selecteditem.banId;
            var note = Partial.Widgets.textarea1.datavalue;
            var entityId = Partial.pageParams.entityId;

            Partial.Variables.save_Notes.setInput({

                'entityId': entityId,
                'banId': banId,
                'notes': note,
                'docId': documentId,
                'createdByEmplId': App.Variables.getLoggedInUserDetails.dataSet.firstName.concat(" " + App.Variables.getLoggedInUserDetails.dataSet.lastName)


            });



            Partial.Variables.save_Notes.invoke({},
                function(data) {
                    // Success Callback
                    debugger;
                    console.log("success", data);
                    documentId = "";
                    // App.pageRefresh();
                    Partial.Variables.errorMsg.dataSet.dataValue = "";
                    Partial.Variables.successMessage.dataSet.dataValue = "Note created successfully";

                    setTimeout(function() {
                        Partial.Widgets.CreateUserNotesdialog1.close();

                    }, 1300);


                    // window.location.reload();
                    Partial.Variables.getLatestNotes_ByEntityId.setInput({

                        'entityId': Partial.pageParams.entityId;
                    });
                    Partial.Variables.getLatestNotes_ByEntityId.invoke();
                    App.refreshLatestNotesAndDoc();

                },
                function(error) {
                    // Error Callback
                    console.log("error", error)

                });

        }

        //   }
    }
    setTimeout(messageTimeout, 1500);

};
};


Partial.getBanDetailsForEntityCancelonError = function(variable, data, xhrObj) {

};

function messageTimeout() {
    Partial.Variables.successMessage.dataSet.dataValue = null;
}

App.refreshLatestNotes = function() {

    Partial.Variables.getLatestNotes_ByEntityId.invoke();

};