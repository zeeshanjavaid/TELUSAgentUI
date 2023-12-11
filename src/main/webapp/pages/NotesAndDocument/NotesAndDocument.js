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
var documentId = "";
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
    Partial.Variables.getLatestNotesBy_EntityId.setInput({

        'entityId': Partial.pageParams.entityId

    });
    Partial.Variables.getLatestNotesBy_EntityId.invoke();

    Partial.Variables.getUserDetailsByEmplIdForNotesAndDoc.invoke();

};

Partial.createUserNotebuttonClick = function($event, widget) {
    Partial.Widgets.createNoteButtonpopover.hidePopover();
    Partial.Variables.errorMsg.dataSet.dataValue = "";
    Partial.Variables.successMessage.dataSet.dataValue = "";
    Partial.Widgets.CreateUserNotes_dialog.open();
    Partial.Variables.banList_Show.dataSet.dataValue = true;

}
Partial.BananchorClick = function($event, widget) {
    Partial.Variables.getBanDetailsForNotesAndDoc.setInput({

        'entityId': Partial.pageParams.entityId

    });
    Partial.Variables.getBanDetailsForNotesAndDoc.invoke();
    document.getElementById("associateBanLink").style.color = 'gray';
    Partial.Variables.banList_Show.dataSet.dataValue = false;
};

Partial.getBanDetailsForNotesAndDoconError = function(variable, data, xhrObj) {

};
Partial.ClearanchorClick = function($event, widget) {
    Partial.Widgets.getEntityDetailsTable1_2.selecteditem = false;
};
Partial.cancleButtonClick = function($event, widget) {

    Partial.Variables.errorMsg.dataSet.dataValue = "";
    Partial.Variables.banList_Show.dataSet.dataValue = true;
};
Partial.createButtonClick = function($event, widget) {

    //for file upload
    debugger;

    documentId = "";
    const message = document.getElementById("p01");
    message.innerHTML = "";


    if (Partial.Widgets.fileupload_1.selectedFiles != undefined) {
        var entityId = Partial.pageParams.entityId;

        var docName = Partial.Widgets.fileupload_1.selectedFiles[0].name;

        var docSize = Partial.Widgets.fileupload_1.selectedFiles[0].size;

        if (Partial.Widgets.textarea1.datavalue != undefined) {
            //Restricting it to 500KB
            if (docSize <= 512000) {

                Partial.Variables.SaveDocument.setInput({
                    'documentName': docName,
                    'document': Partial.Widgets.fileupload_1.selectedFiles[0]
                });



                Partial.Variables.SaveDocument.invoke({},
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
                            var banId = Partial.Widgets.getEntityDetailsTable1_2.selecteditem.banId;
                            var note = Partial.Widgets.textarea1.datavalue;
                            var entityId = Partial.pageParams.entityId;

                            Partial.Variables.SaveNote.setInput({

                                'entityId': entityId,
                                'banId': banId,
                                'notes': note,
                                'docId': documentId,
                                // 'createdBy': App.Variables.getLoggedInUserDetails.dataSet.firstName.concat(" " + App.Variables.getLoggedInUserDetails.dataSet.lastName),
                                'createdByEmplId': Partial.Variables.getUserDetailsByEmplIdForNotesAndDoc.dataSet[0].firstName.concat(" " + Partial.Variables.getUserDetailsByEmplIdForNotesAndDoc.dataSet[0].lastName)

                            });



                            Partial.Variables.SaveNote.invoke({},
                                function(data) {
                                    // Success Callback
                                    debugger;
                                    console.log("success", data);
                                    documentId = "";
                                    // App.pageRefresh();
                                    Partial.Variables.successMessage.dataSet.dataValue = "Note created successfully";
                                    Partial.Variables.errorMsg.dataSet.dataValue = "";
                                    setTimeout(function() {
                                        Partial.Widgets.CreateUserNotes_dialog.close();

                                    }, 1000);
                                    // window.location.reload();
                                    //  Partial.Variables.getLatestNotesBy_EntityId.invoke();
                                    Partial.Variables.getLatestNotesBy_EntityId.setInput({

                                        'entityId': Partial.pageParams.entityId

                                    });
                                    Partial.Variables.getLatestNotesBy_EntityId.invoke();
                                    App.refreshLatestNotes();


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
            var banId = Partial.Widgets.getEntityDetailsTable1_2.selecteditem.banId;
            var note = Partial.Widgets.textarea1.datavalue;
            var entityId = Partial.pageParams.entityId;

            Partial.Variables.SaveNote.setInput({

                'entityId': entityId,
                'banId': banId,
                'notes': note,
                'docId': documentId,
                // 'createdBy': App.Variables.getLoggedInUserDetails.dataSet.firstName.concat(" " + App.Variables.getLoggedInUserDetails.dataSet.lastName),
                'createdByEmplId': Partial.Variables.getUserDetailsByEmplIdForNotesAndDoc.dataSet[0].firstName.concat(" " + Partial.Variables.getUserDetailsByEmplIdForNotesAndDoc.dataSet[0].lastName)

            });



            Partial.Variables.SaveNote.invoke({},
                function(data) {
                    // Success Callback
                    debugger;
                    console.log("success", data);
                    documentId = "";
                    // App.pageRefresh();
                    Partial.Variables.errorMsg.dataSet.dataValue = "";
                    Partial.Variables.successMessage.dataSet.dataValue = "Note created successfully";

                    setTimeout(function() {
                        Partial.Widgets.CreateUserNotes_dialog.close();

                    }, 1300);


                    // window.location.reload();
                    Partial.Variables.getLatestNotesBy_EntityId.setInput({

                        'entityId': Partial.pageParams.entityId

                    });
                    Partial.Variables.getLatestNotesBy_EntityId.invoke();
                    App.refreshLatestNotes();


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


Partial.downloadDoc = function(row) {

    debugger;
    var docId = row.docId;

    Partial.Variables.getDocumentByDocId.setInput({

        'docId': docId

    });

    Partial.Variables.getDocumentByDocId.invoke({},
        function(data) {

            //   $event.preventDefault();
            download(data.content[0].document, data.content[0].documentName)


        },
        function(error) {
            // Error Callback
            console.log("error", error)

        });

}

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

function messageTimeout() {
    Partial.Variables.successMessage.dataSet.dataValue = null;
}

App.refreshLatestNotesAndDoc = function() {

    Partial.Variables.getLatestNotesBy_EntityId.setInput({

        'entityId': Partial.pageParams.entityId

    });

    Partial.Variables.getLatestNotesBy_EntityId.invoke();

};
Partial.createNoteButtonpopoverShow = function($event, widget) {
    debugger;
    Partial.Variables.getUserDetailsByEmplIdForNotesAndDoc.invoke();

};