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
Partial.addEditDocumentEventName = "addEditDocument_Event";
Partial.conditionDocDeleteEventName = "conditionDocDelete_Event";
Partial.parentContainerDOMElement = null;
Partial.documentToDeleteId = null;
Partial.docIsOfCondition = false;

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
    Partial.parentContainerDOMElement = document.querySelector("div[name='" + Partial.pageParams.parentContainerName + "']");
};

Partial.base64toBlob = function(base64Data, contentType) {
    contentType = contentType || '';
    let sliceSize = 1024;
    let byteCharacters = atob(base64Data);
    let bytesLength = byteCharacters.length;
    let slicesCount = Math.ceil(bytesLength / sliceSize);
    let byteArrays = new Array(slicesCount);

    for (var sliceIndex = 0; sliceIndex < slicesCount; ++sliceIndex) {
        var begin = sliceIndex * sliceSize;
        var end = Math.min(begin + sliceSize, bytesLength);

        var bytes = new Array(end - begin);
        for (let offset = begin, i = 0; offset < end; ++i, ++offset) {
            bytes[i] = byteCharacters[offset].charCodeAt(0);
        }
        byteArrays[sliceIndex] = new Uint8Array(bytes);
    }

    return new Blob(byteArrays, {
        type: contentType
    });
}

Partial.getAllDocumentsonError = function(variable, data) {

};

Partial.getAllDocumentsonSuccess = function(variable, data) {
    previousTD = null;
    previousData = null;
    matchCount = 1;
};

Partial.displayFormatLastUpdated = function(dateValue) {
    if (dateValue) {
        return App.localizeTimestampToAppTimezone(dateValue);
    }
}

Partial.displayDocumentDescription = function(documentCode) {
    if (documentCode && Partial.Variables.dvs_getAllDocumentCode.dataSet) {
        let docDescription = '';
        let matchedDocCode = Partial.Variables.dvs_getAllDocumentCode.dataSet.find((docCode) => {
            return docCode.code === documentCode;
        });

        if (matchedDocCode)
            docDescription = matchedDocCode.description;

        return docDescription;
    }
}

Partial.displayDocumentTypeDescription = function(documentTypeId) {
    if (documentTypeId && Partial.Variables.dvs_getAllDocumentTypes.dataSet) {
        let docTypeDescription = '';
        let matchedDocType = Partial.Variables.dvs_getAllDocumentTypes.dataSet.find((documentType) => {
            return documentType.id === documentTypeId;
        });

        if (matchedDocType)
            docTypeDescription = matchedDocType.description;

        return docTypeDescription;
    }
}

Partial.sv_downloadUploadedFileonError = function(variable, data) {
    console.log("Document download failed!");
};

Partial.sv_downloadUploadedFileonSuccess = function(variable, data) {
    let blob = Partial.base64toBlob(data.encodedContents, data.contentType);

    let url = window.URL || window.webkitURL;
    link = url.createObjectURL(blob);

    console.log(link);

    let a = document.createElement("a");
    a.setAttribute("download", data.fileName);
    a.setAttribute("href", link);
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
};

Partial.editDocument = function(rowData) {
    let editEvent = new CustomEvent(Partial.addEditDocumentEventName, {
        "detail": {
            "operation": "EDIT",
            "documentData": rowData
        }
    });

    Partial.parentContainerDOMElement.dispatchEvent(editEvent);
}

Partial.deleteDocument = function(rowData) {
    Partial.documentToDeleteId = rowData.id;
    Partial.Widgets.docDelete_ConfirmDialog.open();
}

Partial.AddDocument_btnClick = function($event, widget) {
    let addEvent = new CustomEvent(Partial.addEditDocumentEventName, {
        "detail": {
            "operation": "ADD",
            "documentData": null
        }
    });

    Partial.parentContainerDOMElement.dispatchEvent(addEvent);
};

Partial.sv_deleteDocumentonError = function(variable, data) {
    Partial.docIsOfCondition = false;
    Partial.documentToDeleteId = null;
    Partial.Widgets.docDelete_ConfirmDialog.close();
};

Partial.sv_deleteDocumentonSuccess = function(variable, data) {
    Partial.Widgets.docDelete_ConfirmDialog.close();

    //invoke the getAllDocuments
    Partial.Variables.getAllDocuments.invoke();
    if (Partial.docIsOfCondition) {
        let docDeleteEvent = new CustomEvent(Partial.conditionDocDeleteEventName, {
            "detail": {
                "operation": "DELETE"
            }
        });

        Partial.parentContainerDOMElement.dispatchEvent(docDeleteEvent);
    }

    Partial.docIsOfCondition = false;
    Partial.documentToDeleteId = null;
};

Partial.docDelete_ConfirmDialogOk = function($event, widget) {
    Partial.Variables.sv_isDocumentOfCondition.filterExpressions.rules[0].value = Partial.documentToDeleteId;
    Partial.Variables.sv_isDocumentOfCondition.invoke();
};

Partial.documentDownload_Click = function(rowData) {
    Partial.Variables.sv_downloadUploadedFile.setInput({
        "documentId": rowData.id
    });
    Partial.Variables.sv_downloadUploadedFile.invoke();
}

Partial.documentsDataTableDatarender = function(widget, $data) {
    let isSameAsPrevious = false;
    let previousData = null,
        previousTD = null;
    let matchCount = 1;
    // $("div[name='documentsDataTable'].parent().DataTable({autowidth:false})");


    $("div[name='documentsDataTable'] .app-grid-content tr").each((index, tableRow) => {
        let firstCol = $(tableRow).children("td")[0];

        if (index === 0) {
            previousTD = firstCol;
            previousData = Partial.Variables.getAllDocuments.dataSet[index].enitityName;
            $(firstCol).addClass('column-right-border');
        } else {
            isSameAsPrevious = (previousData === Partial.Variables.getAllDocuments.dataSet[index].enitityName);
            if (isSameAsPrevious) {
                previousTD = firstCol;
                matchCount += 1;
                $(firstCol).addClass('column-right-border');
            } else {
                $(previousTD).addClass('column-bottom-border');

                previousTD = firstCol;
                matchCount = 1;
                $(firstCol).addClass('column-right-border');
            }

            previousData = Partial.Variables.getAllDocuments.dataSet[index].enitityName;
        }

    });
};

Partial.sv_isDocumentOfConditiononSuccess = function(variable, data) {
    if (data.length === 1)
        Partial.docIsOfCondition = true;

    //call service to delete document
    Partial.Variables.sv_deleteDocument.setInput({
        "documentId": Partial.documentToDeleteId
    });
    Partial.Variables.sv_deleteDocument.invoke();
};