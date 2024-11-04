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

    debugger;


    /*
     * variables can be accessed through 'Partial.Variables' property here
     * e.g. to get dataSet in a staticVariable named 'loggedInUser' use following script
     * Partial.Variables.loggedInUser.getData()
     *
     * widgets can be accessed through 'Partial.Widgets' property here
     * e.g. to get value of text widget named 'username' use following script
     * 'Partial.Widgets.username.datavalue'
     
     
     */
    //  alert(Partial.pageParams.entityId);

    Partial.Variables.getUserDetailsByEmplId.invoke();

    Partial.Variables.entityIdForNotes.dataSet = Partial.pageParams.entityId;

    Partial.Variables.getLatestNotesByEntityId.setInput({

        'entityId': Partial.Variables.entityIdForNotes.dataSet

    });
    Partial.Variables.getLatestNotesByEntityId.invoke();

    App.refreshParrSummary();
    App.refreshNextTreatment();



};
Partial.button1Click = function($event, widget) {
    /*Partial.Widgets.createNoteButton.hidePopover();*/
    Partial.Variables.getUserDetailsByEmplId.invoke();
    Partial.Variables.errorMsg.dataSet.dataValue = "";
    Partial.Variables.successMessage.dataSet.dataValue = "";
    Partial.Widgets.CreateUserNotesdialog1.open();



};

Partial.button3Click = function($event, widget) {
    debugger;
    documentId = "";

    if (Partial.Widgets.fileupload1.selectedFiles != undefined) {

        var docName = Partial.Widgets.fileupload1.selectedFiles[0].name;

        Partial.Variables.saveDocument.setInput({

            'documentName': docName,
            'document': Partial.Widgets.fileupload1.selectedFiles
        });

        Partial.Variables.saveDocument.invoke({},
            function(data) {
                // Success Callback
                debugger;
                console.log("success", data);
                documentId = data.id;

            },
            function(error) {
                // Error Callback
                console.log("error", error)

            });



    }

};



Partial.button3Click1 = function($event, widget) {

    //for file upload
    debugger;

    documentId = "";
    const message = document.getElementById("p01");
    message.innerHTML = "";


    if (Partial.Widgets.fileupload1.selectedFiles != undefined) {
        var entityId = Partial.Variables.entityIdForNotes.dataSet;

        var docName = Partial.Widgets.fileupload1.selectedFiles[0].name;

        var docSize = Partial.Widgets.fileupload1.selectedFiles[0].size;

        if (Partial.Widgets.textarea1.datavalue != undefined) {
            //Restricting it to 500KB
            if (docSize <= 512000) {

                Partial.Variables.saveDocument.setInput({
                    'documentName': docName,
                    'document': Partial.Widgets.fileupload1.selectedFiles[0]
                });



                Partial.Variables.saveDocument.invoke({},
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
                            var entityId = Partial.Variables.entityIdForNotes.dataSet;

                            Partial.Variables.saveNote.setInput({

                                'entityId': entityId,
                                'banId': banId,
                                'notes': note,
                                'docId': documentId,
                                'createdByEmplId': Partial.Variables.getUserDetailsByEmplId.dataSet[0].firstName.concat(" " + Partial.Variables.getUserDetailsByEmplId.dataSet[0].lastName)


                            });



                            Partial.Variables.saveNote.invoke({},
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
                                    Partial.Variables.getLatestNotesByEntityId.setInput({

                                        'entityId': Partial.Variables.entityIdForNotes.dataSet

                                    });
                                    Partial.Variables.getLatestNotesByEntityId.invoke();

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
            var entityId = Partial.Variables.entityIdForNotes.dataSet;

            Partial.Variables.saveNote.setInput({

                'entityId': entityId,
                'banId': banId,
                'notes': note,
                'docId': documentId,
                'createdByEmplId': Partial.Variables.getUserDetailsByEmplId.dataSet[0].firstName.concat(" " + Partial.Variables.getUserDetailsByEmplId.dataSet[0].lastName)


            });



            Partial.Variables.saveNote.invoke({},
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
                    Partial.Variables.getLatestNotesByEntityId.setInput({

                        'entityId': Partial.Variables.entityIdForNotes.dataSet

                    });
                    Partial.Variables.getLatestNotesByEntityId.invoke();
                    App.refreshLatestNotesAndDoc();

                },
                function(error) {
                    // Error Callback
                    console.log("error", error)

                });

        }

        //   }
    }
    setTimeout(messageTimeout, 2000);

};

Partial.anchor1Click = function($event, widget) {


    Partial.Widgets.getEntityDetailsTable1_1.selecteditem = false;

};
Partial.fileupload1Select = function($event, widget, selectedFiles) {

    debugger;



};


Partial.button4_1Click = function($event, widget) {

    debugger;
    var docId = Partial.Variables.getLatestNotesByEntityId.dataSet[0].docId;

    Partial.Variables.getDocumentByDocId.setInput({

        'docId': docId

    });


    Partial.Variables.getDocumentByDocId.invoke({},
        function(data) {

            $event.preventDefault();
            download(data.content[0].document, data.content[0].documentName)


        },
        function(error) {
            // Error Callback
            console.log("error", error)

        });





};
Partial.button5Click = function($event, widget) {


    var docId = Partial.Variables.getLatestNotesByEntityId.dataSet[1].docId;

    Partial.Variables.getDocumentByDocId.setInput({

        'docId': docId

    });


    Partial.Variables.getDocumentByDocId.invoke({},
        function(data) {



            download(data.content[0].document, data.content[0].documentName)

        },
        function(error) {
            // Error Callback
            console.log("error", error)

        });


};

Partial.button6Click = function($event, widget) {

    var docId = Partial.Variables.getLatestNotesByEntityId.dataSet[2].docId;

    Partial.Variables.getDocumentByDocId.setInput({

        'docId': docId

    });


    Partial.Variables.getDocumentByDocId.invoke({},
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
Partial.button4Click = function($event, widget) {

    Partial.Variables.errorMsg.dataSet.dataValue = "";
    Partial.Variables.banListShow.dataSet.dataValue = true;

};

Partial.anchor2Click = function($event, widget) {

    Partial.Variables.getBanDetailsForNotes.setInput({

        'entityId': Partial.pageParams.entityId

    });
    Partial.Variables.getBanDetailsForNotes.invoke();
    document.getElementById("associateBanLink").style.color = 'gray';
    Partial.Variables.banListShow.dataSet.dataValue = false;
};

Partial.getBanDetailsForNotesonError = function(variable, data, xhrObj) {

};

function messageTimeout() {
    Partial.Variables.successMessage.dataSet.dataValue = null;
}
Partial.getEntityDetailsTable1_OnRowexpand = function($event, widget, row, $data) {
    App.showRowExpansionEntityDetails(row, $data);
};

Partial.CollectionDataServiceGetEntityDetailsonError = function(variable, data, xhrObj) {
    debugger;
    Partial.Variables.CollectionDataServiceGetEntityDetails.dataSet.banDetails = [];
    //Page.Widgets.getEntityDetailsTable1.refresh();
};

App.refreshLatestNotes = function() {

    Partial.Variables.getLatestNotesByEntityId.invoke();

};
App.refreshNextTreatment = function() {
    debugger;
    Partial.Variables.getUpcomingTreatmentForEntity.setInput({

        'entityId': Partial.pageParams.entityId

    });
    Partial.Variables.getUpcomingTreatmentForEntity.invoke();
}

Partial.getUpcomingTreatmentForEntityonError = function(variable, data, xhrObj) {
    debugger;
    console.log("Error upcoming treatment");
};
Partial.getUpcomingTreatmentForEntityonSuccess = function(variable, data) {
    debugger;
    console.log("Success upcoming treatment");
    Partial.Widgets.stepDate.caption = data.stepDate;
    Partial.Widgets.stepDesc.caption = data.stepCode;
};

App.refreshParrSummary = function() {
    var getPaymentArrangementsForEntityProfileVar = Partial.Variables.getPaymentArrangementsForEntityProfile;

    getPaymentArrangementsForEntityProfileVar.invoke({
            "inputFields": {
                "entityId": Partial.pageParams.entityId,
                "status": 'Open'
            },
        },
        function(data) {
            debugger;
            if (data.length > 0) {
                Partial.Widgets.parrSummaryId.caption = data[0].id;
                Partial.Widgets.totalAmtParrSummary.caption = data[0].amount.toLocaleString('en-US');;
                Partial.Widgets.parrSumStatus.caption = data[0].status;
                Partial.Widgets.cummPaymentExp.caption = data[0].expectedPaymentAmountToDate.toLocaleString('en-US');;
                Partial.Widgets.cummPmtRvcd.caption = data[0].receivedPaymentAmountToDate.toLocaleString('en-US');;
                Partial.Widgets.recurrenceParrSummary.caption = data[0].recurrence;
                Partial.Widgets.evaluationResultParrSum.caption = data[0].evaluationResult;
                var installmentLength = data[0].installments.length;
                Partial.Widgets.NoOfInstallmentParrSum.caption = data[0].installments[installmentLength - 1].sequenceId;
                if (data[0].receivedPaymentAmountToDate != 0.0) {
                    Partial.Widgets.percPymtVsExpRcvd.caption = ((data[0].expectedPaymentAmountToDate / data[0].receivedPaymentAmountToDate) * 100);
                } else {
                    Partial.Widgets.percPymtVsExpRcvd.caption = 0.0;
                }

                var totalInstallmentIntermediateAmt = 0;
                data[0].installments.forEach(function(d) {
                    totalInstallmentIntermediateAmt = parseFloat(totalInstallmentIntermediateAmt) + parseFloat(d.amount);
                });

                var totalInstallmentAmount = Math.round((totalInstallmentIntermediateAmt) * 100) / 100;

                Partial.Widgets.installmentAmtParrSum.caption = totalInstallmentAmount.toLocaleString('en-US');;

                Partial.Widgets.parrSummaryId.show = true;
                Partial.Widgets.parrSummaryIdLabel.show = true;
                Partial.Widgets.parrSumStatusLabel.show = true;
                Partial.Widgets.parrSumStatus.show = true;
                Partial.Widgets.evaluationResultParrSumLabel.show = true;
                Partial.Widgets.evaluationResultParrSum.show = true;
                Partial.Widgets.recurrenceParrSummaryLabel.show = true;
                Partial.Widgets.recurrenceParrSummary.show = true;
                Partial.Widgets.NoOfInstallmentParrSumLabel.show = true;
                Partial.Widgets.NoOfInstallmentParrSum.show = true;
                Partial.Widgets.percPymtVsExpRcvd.show = true;
                Partial.Widgets.percPymtVsExpRcvdLabel.show = true;
                Partial.Widgets.cummPmtRvcd.show = true;
                Partial.Widgets.cummPmtRvcdLabel.show = true;
                Partial.Widgets.cummPaymentExp.show = true;
                Partial.Widgets.cummPaymentExpLabel.show = true;
                Partial.Widgets.totalAmtParrSummary.show = true;
                Partial.Widgets.totalAmtParrSummaryLabel.show = true;
                Partial.Widgets.installmentAmtParrSumLabel.show = true;
                Partial.Widgets.installmentAmtParrSum.show = true;
                Partial.Widgets.ParrSummaryNotActive.show = false;
                Partial.Widgets.createParrInEntityProfile.show = false;

            } else {
                //check for Renegotiated status
                App.refreshParrSummaryForRenegotiated();
            }

        },
        function(error) {
            // Error Callback
            console.log("error", error);
        }
    );
}

App.refreshParrSummaryForRenegotiated = function() {
    var getPaymentArrangementsForEntityProfileVar = Partial.Variables.getPaymentArrangementsForEntityProfile;

    getPaymentArrangementsForEntityProfileVar.invoke({
            "inputFields": {
                "entityId": Partial.pageParams.entityId,
                "status": 'Renegotiated'
            },
        },
        function(data) {
            debugger;
            if (data.length > 0) {
                Partial.Widgets.parrSummaryId.caption = data[0].id;
                Partial.Widgets.totalAmtParrSummary.caption = data[0].amount.toLocaleString('en-US');;
                Partial.Widgets.parrSumStatus.caption = data[0].status;
                Partial.Widgets.cummPaymentExp.caption = data[0].expectedPaymentAmountToDate.toLocaleString('en-US');;
                Partial.Widgets.cummPmtRvcd.caption = data[0].receivedPaymentAmountToDate.toLocaleString('en-US');;
                Partial.Widgets.recurrenceParrSummary.caption = data[0].recurrence;
                Partial.Widgets.evaluationResultParrSum.caption = data[0].evaluationResult;
                var installmentLength = data[0].installments.length;
                Partial.Widgets.NoOfInstallmentParrSum.caption = data[0].installments[installmentLength - 1].sequenceId;
                if (data[0].receivedPaymentAmountToDate != 0.0) {
                    Partial.Widgets.percPymtVsExpRcvd.caption = ((data[0].expectedPaymentAmountToDate / data[0].receivedPaymentAmountToDate) * 100);
                } else {
                    Partial.Widgets.percPymtVsExpRcvd.caption = 0.0;
                }

                var totalInstallmentIntermediateAmt = 0;
                data[0].installments.forEach(function(d) {
                    totalInstallmentIntermediateAmt = parseFloat(totalInstallmentIntermediateAmt) + parseFloat(d.amount);
                });

                var totalInstallmentAmount = Math.round((totalInstallmentIntermediateAmt) * 100) / 100;

                Partial.Widgets.installmentAmtParrSum.caption = totalInstallmentAmount.toLocaleString('en-US');;

                Partial.Widgets.parrSummaryId.show = true;
                Partial.Widgets.parrSummaryIdLabel.show = true;
                Partial.Widgets.parrSumStatusLabel.show = true;
                Partial.Widgets.parrSumStatus.show = true;
                Partial.Widgets.evaluationResultParrSumLabel.show = true;
                Partial.Widgets.evaluationResultParrSum.show = true;
                Partial.Widgets.recurrenceParrSummaryLabel.show = true;
                Partial.Widgets.recurrenceParrSummary.show = true;
                Partial.Widgets.NoOfInstallmentParrSumLabel.show = true;
                Partial.Widgets.NoOfInstallmentParrSum.show = true;
                Partial.Widgets.percPymtVsExpRcvd.show = true;
                Partial.Widgets.percPymtVsExpRcvdLabel.show = true;
                Partial.Widgets.cummPmtRvcd.show = true;
                Partial.Widgets.cummPmtRvcdLabel.show = true;
                Partial.Widgets.cummPaymentExp.show = true;
                Partial.Widgets.cummPaymentExpLabel.show = true;
                Partial.Widgets.totalAmtParrSummary.show = true;
                Partial.Widgets.totalAmtParrSummaryLabel.show = true;
                Partial.Widgets.installmentAmtParrSumLabel.show = true;
                Partial.Widgets.installmentAmtParrSum.show = true;
                Partial.Widgets.ParrSummaryNotActive.show = false;
                Partial.Widgets.createParrInEntityProfile.show = false;

            } else {
                Partial.Widgets.parrSummaryId.show = false;
                Partial.Widgets.parrSummaryIdLabel.show = false;
                Partial.Widgets.parrSumStatusLabel.show = false;
                Partial.Widgets.parrSumStatus.show = false;
                Partial.Widgets.evaluationResultParrSumLabel.show = false;
                Partial.Widgets.evaluationResultParrSum.show = false;
                Partial.Widgets.recurrenceParrSummaryLabel.show = false;
                Partial.Widgets.recurrenceParrSummary.show = false;
                Partial.Widgets.NoOfInstallmentParrSumLabel.show = false;
                Partial.Widgets.NoOfInstallmentParrSum.show = false;
                Partial.Widgets.percPymtVsExpRcvd.show = false;
                Partial.Widgets.percPymtVsExpRcvdLabel.show = false;
                Partial.Widgets.cummPmtRvcd.show = false;
                Partial.Widgets.cummPmtRvcdLabel.show = false;
                Partial.Widgets.cummPaymentExp.show = false;
                Partial.Widgets.cummPaymentExpLabel.show = false;
                Partial.Widgets.totalAmtParrSummary.show = false;
                Partial.Widgets.totalAmtParrSummaryLabel.show = false;
                Partial.Widgets.installmentAmtParrSumLabel.show = false;
                Partial.Widgets.installmentAmtParrSum.show = false;
                Partial.Widgets.ParrSummaryNotActive.show = true;
                Partial.Widgets.createParrInEntityProfile.show = true;
            }

        },
        function(error) {
            // Error Callback
            console.log("error", error);
        }
    );
}
Partial.createParrInEntityProfileClick = function($event, widget) {
    App.Widgets.ActiveEntity.isActive = false;
    App.Widgets.Parr.isActive = true;
    App.Widgets.Parr.show = true;
    Partial.Variables.ParrPageName.dataSet.dataValue = 'CreateParr';
};

App.refreshEntityBanDetails = function() {
    Partial.Variables.CollectionDataServiceGetEntityDetails.invoke();
};


Partial.getEntityDetailsTable1Beforedatarender = function(widget, $data, $columns) {
    debugger;

    $data.forEach(function(e) {
        var TO_AR;
        if (e.totalAr != null) {
            TO_AR = (e.totalAr).toFixed(2);
            var formattedString = TO_AR.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            e.totalAr = formattedString;
        }
    });

    $data.forEach(function(e) {
        var TO_OD;
        if (e.totalOverDue != null) {
            TO_OD = (e.totalOverDue).toFixed(2);
            var formattedString = TO_OD.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            e.totalOverDue = formattedString;
        }
    });

    $data.forEach(function(e) {
        var dis_amount;

        if (e.disputeAmount != null) {
            dis_amount = (e.disputeAmount).toFixed(2);
            var formattedString = dis_amount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            e.disputeAmount = formattedString;
        }
    });
};
/*Partial.createNoteButtonShow = function($event, widget) {

    Partial.Variables.getUserDetailsByEmplId.invoke();

};*/
Partial.banStatusSelectionChange = function($event, widget, newVal, oldVal) {
    console.log("Old Val : " + oldVal + " New Val : " + newVal);
    var status;
    if (newVal == 'Active') {
        status = 'O'
    } else if (newVal == 'Cancelled') {
        status = 'C'
    } else {
        status = 'ALL'
    }
    Partial.Variables.CollectionDataServiceGetEntityDetails.setInput({

        'entityId': Partial.pageParams.entityId,
        'accountStatus': status

    });
    Partial.Variables.CollectionDataServiceGetEntityDetails.invoke();
};