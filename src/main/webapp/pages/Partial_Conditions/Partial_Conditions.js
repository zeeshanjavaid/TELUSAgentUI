const addEditConditionEventName = "addEditCondition_Event";
Partial.addEditDocumentEventName = "addEditDocument_Event";
Partial.parentContainerDOMElement = null;
Partial.docParentContainerDOMElement = null;

Partial.requiredConditionReason = false;

Partial.onReady = function() {
    Partial.parentContainerDOMElement = document.querySelector("div[name='" + Partial.pageParams.parentContainerName + "']");
    Partial.docParentContainerDOMElement = document.querySelector("div[name='" + Partial.pageParams.docsParentContainerName + "']");
};

Partial.displayFormatLastUpdated = function(dateValue) {
    if (dateValue) {
        return App.localizeTimestampToAppTimezone(dateValue);
    }
}


Partial.displayConditionStatusDescription = function(conditionCodeId) {
    if (conditionCodeId && Partial.Variables.dvs_getConditionStatus.dataSet) {
        let description = '';
        let match = Partial.Variables.dvs_getConditionStatus.dataSet.find((conditionCode) => {
            return conditionCode.id === conditionCodeId;
        });
        if (match)
            description = match.description;

        return description;
    }
}

Partial.displayConditionCategoryTypeDescription = function(conditionTypeId) {
    if (conditionTypeId && Partial.Variables.dvs_getConditionCategoryType.dataSet) {
        let description = '';
        let match = Partial.Variables.dvs_getConditionCategoryType.dataSet.find((conditionType) => {
            return conditionType.id === conditionTypeId;
        });
        if (match)
            description = match.description;

        return description;
    }
}

Partial.displayConditionCodeDescription = function(conditionCodeId) {
    if (conditionCodeId && Partial.Variables.dvs_getConditionCategoryType.dataSet) {
        let description = '';
        let match = Partial.Variables.dvs_getConditionCode.dataSet.find((conditionCode) => {
            return conditionCode.id === conditionCodeId;
        });
        if (match)
            description = match.description;

        return description;
    }
}

Partial.dvs_getConditionStatusonSuccess = function(variable, data) {
    data.forEach(o => {
        switch (o.code) {
            case "Accepted":
                Partial.Variables.conditionAcceptId.dataSet.dataValue = o.id;
                break;
            case "Rejected":
                Partial.Variables.conditionRejectId.dataSet.dataValue = o.id;
                break;
            case "Waived":
                Partial.Variables.conditionWaiveId.dataSet.dataValue = o.id;
                break;
            case "Received":
                Partial.Variables.conditionReceivedId.dataSet.dataValue = o.id;
                break;
            case "Pending":
                Partial.Variables.conditionPendingId.dataSet.dataValue = o.id;
                break;
            default:
        };
    });

};

Partial.AddCondition_btnClick = function($event, widget) {
    let addEvent = new CustomEvent(addEditConditionEventName, {
        "detail": {
            "operation": "ADD",
            "documentData": null
        }
    });

    Partial.parentContainerDOMElement.dispatchEvent(addEvent);
};

Partial.getAllConditionsonError = function(variable, data) {

};

Partial.getAllConditionsonSuccess = function(variable, data) {
    console.log("---------------------------------");
    console.log(data);
};

Partial.conditionsDataTable_customRowAction = function($event, row) {
    Partial.Variables.deleteCondition.setInput("conditionId", row.id);
    Partial.Variables.deleteCondition.invoke();
};

Partial.deleteConditiononSuccess = function(variable, data) {
    Partial.Variables.getAllConditions.invoke();
};

Partial.UpdateConditionStatusAccept = function($event, widget, row) {
    Partial.requiredConditionReason = false;
    // Accept the condition
    let statusCode = Partial.Variables.dvs_getConditionStatus.dataSet.find(o => {
        if (o.id == row.status)
            return o;
    });

    Partial.Variables.getAllConditions.dataSet[row.$index - 1].status = Partial.Variables.conditionAcceptId.dataSet.dataValue;
    row.status = Partial.Variables.conditionAcceptId.dataSet.dataValue;
    Partial.updateRowValues(row);
};

Partial.UpdateConditionStatusReject = function($event, widget, row) {
    Partial.requiredConditionReason = true;

    let statusCode = Partial.Variables.dvs_getConditionStatus.dataSet.find(o => {
        if (o.id == row.status)
            return o;
    });

    Partial.Variables.getAllConditions.dataSet[row.$index - 1].status = Partial.Variables.conditionRejectId.dataSet.dataValue;
    row.status = Partial.Variables.conditionRejectId.dataSet.dataValue;

    Partial.updateRowValues(row);
};

Partial.UpdateConditionStatusWaived = function($event, widget, row) {
    Partial.requiredConditionReason = true;

    let statusCode = Partial.Variables.dvs_getConditionStatus.dataSet.find(o => {
        if (o.id == row.status)
            return o;
    });

    Partial.Variables.getAllConditions.dataSet[row.$index - 1].status = Partial.Variables.conditionWaiveId.dataSet.dataValue;
    row.status = Partial.Variables.conditionWaiveId.dataSet.dataValue;
    Partial.updateRowValues(row);
};

Partial.updateRowValues = function(row) {
    row.updatedxOn = moment(Date.now()).format("YYYY-MM-DD[T]HH:mm:ss.SSSZZ");
    // row.updatedBy = App.Variables.loggedInUser.name;

    Partial.Variables.getAllConditions.dataSet[row.$index - 1] = row;
    var conditionData = JSON.parse(JSON.stringify(Partial.Variables.getAllConditions.dataSet));
    Partial.Variables.getAllConditions.dataSet = conditionData;

    Partial.Widgets.conditionsDataTable.redraw()

};

Partial.selectConditionReasonChange = function($event, widget, row, newVal, oldVal) {
    Partial.Variables.getAllConditions.dataSet[row.$index - 1].conditionReason = (newVal !== "") ? newVal : null;
    row.conditionReason = (newVal !== "") ? newVal : null;
};

Partial.condDoc_uploadBtnClick = function(row) {
    let docAddEvent = new CustomEvent(Partial.addEditDocumentEventName, {
        "detail": {
            "operation": "ADD",
            "documentData": {
                "applicationId": row.applicationId,
                "partyId": row.partyId
            },
            "conditionId": row.id
        }
    });

    Partial.docParentContainerDOMElement.dispatchEvent(docAddEvent);
};

Partial.checkConReasonRequired = function(status) {
    return status == Partial.Variables.conditionRejectId.dataSet.dataValue || status == Partial.Variables.conditionWaiveId.dataSet.dataValue;
}

Partial.documentDownload_Click = function(rowData) {
    Partial.Variables.sv_downloadUploadedFile.setInput({
        "documentId": rowData.documentId
    });
    Partial.Variables.sv_downloadUploadedFile.invoke();
}


Partial.sv_downloadUploadedFileonError = function(variable, data) {

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