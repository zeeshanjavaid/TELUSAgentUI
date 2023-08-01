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

    Partial.Variables.getLatestNotesByEntityId.setInput({

        'entityId': window.location.href.toString().split("=")[1]

    });
    Partial.Variables.getLatestNotesByEntityId.invoke();
};
Partial.button1Click = function($event, widget) {
    Partial.Widgets.createNoteButton.hidePopover();
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
        var entityId = Partial.Variables.getLatestNotesByEntityId.dataSet[0].entityId;

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
                                throw "Please enter the note"
                            } catch (err) {
                                message.innerHTML = err;
                            }
                        } else {
                            var banId = Partial.Widgets.getEntityDetailsTable1_1.selecteditem.banId;
                            var note = Partial.Widgets.textarea1.datavalue;
                            var entityId = Partial.Variables.getLatestNotesByEntityId.dataSet[0].entityId;

                            Partial.Variables.saveNote.setInput({

                                'entityId': entityId,
                                'banId': banId,
                                'notes': note,
                                'docId': documentId

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

                                        'entityId': window.location.href.toString().split("=")[1]

                                    });
                                    Partial.Variables.getLatestNotesByEntityId.invoke();

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
            var entityId = Partial.Variables.getLatestNotesByEntityId.dataSet[0].entityId;

            Partial.Variables.saveNote.setInput({

                'entityId': entityId,
                'banId': banId,
                'notes': note,
                'docId': documentId

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

                        'entityId': window.location.href.toString().split("=")[1]

                    });
                    Partial.Variables.getLatestNotesByEntityId.invoke();

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

    debugger;

    Partial.Variables.getBanDetailsForNotes.setInput({

        'entityId': window.location.href.toString().split("=")[1]

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