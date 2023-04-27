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
};
Partial.button1Click = function($event, widget) {
    debugger;

    Partial.Widgets.createNoteButton.hidePopover();

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

    // if (Partial.Widgets.textarea1.datavalue == undefined || Partial.Widgets.textarea1.datavalue == "") {
    //     try {
    //         isError = true;
    //         throw "Please enter the note"
    //     } catch (err) {
    //         message.innerHTML = err;
    //     }
    // } else {
    if (Partial.Widgets.fileupload1.selectedFiles != undefined) {
        var entityId = Partial.Widgets.getEntityDetailsTable1_1.dataset[0].entityId;

        //   var checkDocument = Partial.Variables.checkIfDocIsPresent.dataSet.length;

        //    if (checkDocument == 0) {

        var docName = Partial.Widgets.fileupload1.selectedFiles[0].name;

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
                    var entityId = Partial.Widgets.getEntityDetailsTable1_1.dataset[0].entityId;

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
                            window.location.reload();

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

        //   }
        // else {
        //     const message1 = document.getElementById("p02");
        //     message1.innerHTML = "Attachment already exists with entityId: " + entityId;
        // }
    } else {


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
            var entityId = Partial.Widgets.getEntityDetailsTable1_1.dataset[0].entityId;

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
                    window.location.reload();

                },
                function(error) {
                    // Error Callback
                    console.log("error", error)

                });

        }

        //   }
    }

};

Partial.anchor1Click = function($event, widget) {


    Partial.Widgets.getEntityDetailsTable1_1.selecteditem = false;

};
// Partial.fileupload1Select = function($event, widget, selectedFiles) {

//     debugger;
//     documentId = "";
//     var entityId = Partial.Widgets.getEntityDetailsTable1_1.dataset[0].entityId;

//     var checkDocument = Partial.Variables.checkIfDocIsPresent.dataSet.length;

//     if (checkDocument == 0) {

//         var docName = Partial.Widgets.fileupload1.selectedFiles[0].name;

//         Partial.Variables.saveDocument.setInput({

//             'documentName': docName,
//             'document': Partial.Widgets.fileupload1.selectedFiles
//         });

//         Partial.Variables.saveDocument.invoke({},
//             function(data) {
//                 // Success Callback
//                 debugger;
//                 console.log("success", data);
//                 documentId = data.id;

//             },
//             function(error) {
//                 // Error Callback
//                 console.log("error", error)

//             });

//     } else {
//         const message1 = document.getElementById("p02");
//         message1.innerHTML = "Attachment already exists with entityId: " + entityId;
//     }





// };
// Partial.button4_1Click = function($event, widget) {

//     Partial.Widgets.fileupload1.selectedFiles = "";
//     const message1 = document.getElementById("p02");
//     message1.innerHTML = "";

// };


Partial.button4_1Click = function($event, widget) {

    debugger;
    var docId = Partial.Variables.getLatestNotesByEntityId.dataSet[0].docId;

    Partial.Variables.getDocumentByDocId.setInput({

        'docId': docId

    });


    Partial.Variables.getDocumentByDocId.invoke({},
        function(data) {
            // Success Callback
            debugger;
            //  console.log("success", data);
            $event.preventDefault();
            window.location.href = data.content[0].document;
            //  let blob = base64toBlob(data.content[0].document, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            // let blob = new Blob([data.value], {
            //     type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            // })

            let url = window.URL || window.webkitURL;
            link = url.createObjectURL(blob);

            console.log(link);

            let a = document.createElement("a");
            a.setAttribute("download", "DomainValueExport.xlsx");
            a.setAttribute("href", link);
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);

        },
        function(error) {
            // Error Callback
            console.log("error", error)

        });





};
Partial.button5Click = function($event, widget) {

    debugger;
    var docId = Partial.Variables.getLatestNotesByEntityId.dataSet[1].docId;

    Partial.Variables.getDocumentByDocId.setInput({

        'docId': docId

    });


    Partial.Variables.getDocumentByDocId.invoke({},
        function(data) {
            // Success Callback
            // debugger;
            // $event.preventDefault();
            // window.location.href = data.content[0].document;

            debugger;
            var link = document.createElement("a");
            link.download = "Sample.txt";
            link.href = data.content[0].document;
            link.click();
            link.remove();
            window.URL.revokeObjectURL(link.href);

        },
        function(error) {
            // Error Callback
            console.log("error", error)

        });


};

Partial.button6Click = function($event, widget) {

    debugger;
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
            //   window.location.href = data.content[0].document;

            // const link = document.createElement("a");
            // link.download = "test";
            // const data1 = await fetch(data1.content[0].document).then((res) => res.blob())
            // link.href = window.URL.createObjectURL(
            //     new Blob([data1], {
            //         type: 'application/pdf'
            //     })
            // );
            // link.click();
            // link.remove();
            // window.URL.revokeObjectURL(link.href);


        },
        function(error) {
            // Error Callback
            console.log("error", error)

        });


};