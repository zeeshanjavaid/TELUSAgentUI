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
const FILE_EMPTY_RESPONE_MSG = "File upload failure";
Page.isEditMode = false;
Page.isEditLocale = false;
Page.localeListWithoughtEn = {};
Page.initialObj = {}; //This hold the page form data before any changes
Page.relationToAction = null;
Page.showLocaleErrorMsg = false;



Page.uploadedFileData = null;

$('document').ready(() => {
    document.getElementsByTagName("html")[0].style.visibility = "hidden";
});

/* perform any action on widgets/variables within this block */
Page.onReady = function() {


    initPage();
}

function initPage() {
    const intervalId = setInterval(function() {
        if (true) {
            clearInterval(intervalId);
            console.log('Permissions loaded...');

            if (!App.IsUserHasAccess('Setup_DomainValues')) {
                window.location.href = '#/ErrorLanding';
            } else {
                document.getElementsByTagName("html")[0].style.visibility = "visible";

                if (Page.pageParams.lookupFileIdentifier) {

                    Page.isEditMode = true;
                } else {

                    Page.isEditMode = false;

                }


                if (Page.isEditMode) {
                    Page.Widgets.name.class = 'noteditable';

                }
                Page.Widgets.createdOn.class = 'noteditable';
                Page.Widgets.updatedOn.class = 'noteditable';

                // Page.initialObj = JSON.parse(JSON.stringify(Page.Widgets.domainValueForm.dataset));
            }
        } else {
            //determining the time elapsed since App started in minutes
            const timeElapsedSinceAppStart = moment(new Date()).diff(moment(App.appStartTime), 'minutes');

            if (timeElapsedSinceAppStart < 1)
                console.log('Waiting to load permissions...');
            else {
                clearInterval(intervalId);

                //if the active page is not 'ErrorLanding'
                if (window.location.hash !== '#/ErrorLanding')
                    window.location.href = '#/ErrorLanding';
            }
        }
    }, 10);
}

// Page.getDomainValueByIdonSuccess = function(variable, data) {
//     if (data) {
//         Page.Variables.mv_domainValue.dataSet = data;
//     }
//     Page.initialObj = JSON.parse(JSON.stringify(Page.Variables.mv_domainValue.dataSet));
//     console.log("Page.initialObj", Page.initialObj);
// };

// Page.getDomainValueTypeonSuccess = function(variable, data) {
//     Page.Variables.mv_domainValue.dataSet.domainValueType = {
//         "description": data[0].description,
//         "code": data[0].code,
//         "id": data[0].id
//     };
//     Page.initialObj = JSON.parse(JSON.stringify(Page.Variables.mv_domainValue.dataSet));
//     console.log("Page.initialObj", Page.initialObj);


// };

Page.displayCustomUserAndDateValues = function(timestamp, id) {
    debugger;
    let jsTimeStamp = new Date(timestamp);
    let ts_toShow = jsTimeStamp.toLocaleString().replace(",", "").toLowerCase();
    let username = (id != "null") ? id : "";
    return ts_toShow + " by " + (!username ? "" : username);
};




Page.saveBtnClick = function($event, widget) {

    debugger;

    if (Page.Widgets.domainValueForm.invalid || (Page.Variables.mvLookupFileInfo.dataSet.fileName == "" || Page.Variables.mvLookupFileInfo.dataSet.fileName == undefined)) {

        // if (Page.Widgets.desc.datavalue == undefined || Page.Widgets.desc.datavalue == "") {
        //     Page.ErrorMessage = "Review invalid fields";
        //     Page.ShowErrorMessage = true;
        //     Page.showDescriptionErrorMsg = true;
        //     // Page.Widgets.label10.widget.class = 'custom-font-color-red';
        //     //Page.Widgets.lookupfileLabel.widget.class = 'custom-font-color-red';
        //     Page.Widgets.desc.class = 'bordercol';
        // } else {
        //     Page.ErrorMessage = "";
        //     Page.ShowErrorMessage = false;
        //     Page.showDescriptionErrorMsg = false;
        //     Page.Widgets.label10.widget.class = '';
        //     Page.Widgets.desc.class = 'middlebox';
        // }

        if (Page.Variables.mvLookupFileInfo.dataSet.fileName == "" || Page.Variables.mvLookupFileInfo.dataSet.fileName == undefined) {
            showUploadStatusMsg("error", "Lookup excel file upload is mandatory ", "statusMessage");
            Page.Widgets.lookupfileLabel.widget.class = 'custom-font-color-red';
            Page.ErrorMessage = "Review invalid fields";
            Page.ShowErrorMessage = true;
            Page.showDescriptionErrorMsg = true;
        }

        Page.ErrorMessage = "Review invalid fields";
        Page.ShowErrorMessage = true;
        Page.Widgets.domainValueForm.highlightInvalidFields();

        if ($('[name="statusMessage"]').length) {
            $('html, body').animate({
                scrollTop: $('[name="statusMessage"]').offset().top - 20
            }, 200);
        }

    } else {
        debugger;
        let uploadSV = Page.Variables.svc_createUpdateLookupFile;

        let createdByUser = Page.Variables.mvLookupFileInfo.dataSet.createdBy ? Page.Variables.mvLookupFileInfo.dataSet.createdBy : Page.getUserName(Page.Variables.getLoggedInUserId.dataSet[0].id);
        let updatedByUser = Page.getUserName(Page.Variables.getLoggedInUserId.dataSet[0].id);
        console.log("Created by:" + createdByUser + " updated by:" + updatedByUser);

        uploadSV.setInput("identifier", Page.isEditMode ? Page.Variables.mvLookupFileInfo.dataSet.identifier : "  ");
        uploadSV.setInput("name", Page.Variables.mvLookupFileInfo.dataSet.name);
        uploadSV.setInput("description", Page.Widgets.domainValueForm.formWidgets.desc.datavalue);
        uploadSV.setInput("excelFile", Page.uploadedFileData);
        uploadSV.setInput("createdBy", createdByUser);
        uploadSV.setInput("updatedBy", updatedByUser);

        uploadSV.invoke();
    }

};

Page.saveDomainValueonSuccess = function(variable, data) {

    //debugger

    if (data) {
        Page.Variables.mv_domainValue.dataSet = data;

    }
    Page.Actions.goToPage_DomainValuePage.invoke({
        data: {
            'domainValueTypeId': Page.pageParams.domainValueTypeId
        }
    });
};

Page.localeDetailsDialogClose = function($event, widget) {
    Page.isEditLocale = false;
    Page.Variables.localeDetails.dataSet = {};
};

Page.exitConfirmDialogCancel = function($event, widget) {
    Page.Actions.goToPage_LookupListing.invoke();
};



Page.buttonCancelClick = function($event, widget) {
    //debugger;
    if (JSON.stringify(Page.initialObj) == JSON.stringify(Page.Widgets.domainValueForm.dataset)) {
        Page.exitConfirmDialogCancel();
    } else if (_.isEqual(Page.initialObj, Page.Widgets.domainValueForm.dataset)) {
        Page.exitConfirmDialogCancel();
    } else {
        Page.Widgets.exitConfirmDialog.open();
    }
};


Page.descBlur = function($event, widget) {

    if (Page.Widgets.desc.datavalue == undefined || Page.Widgets.desc.datavalue == "") {
        Page.showDescriptionErrorMsg = true;
        Page.Widgets.label10.widget.class = 'custom-font-color-red';
        Page.Widgets.desc.class = 'bordercol';
    } else {
        Page.showDescriptionErrorMsg = false;
        Page.Widgets.label10.widget.class = '';
        Page.Widgets.desc.class = 'middlebox';
    }
};

Page.getUserName = function(id) {
    //debugger;
    id = parseInt(id);
    let user = Page.Variables.db_getallusers.dataSet.find(user => user.id === id);
    return (!(user == null || user == "") ? user.userId : id);

};

Page.lookupFileLinkClick = function($event, widget) {

    console.log("Downloading file identifier :" + Page.pageParams.lookupFileName);
    Page.Variables.svc_downloadLookupFile.setInput("lookupIdentifier", Page.pageParams.lookupFileIdentifier);
    Page.Variables.svc_downloadLookupFile.invoke();
};


Page.svc_downloadLookupFileonError = function(variable, data) {

    console.log("Lookup download service call error :" + JSON.stringify(data));


};
Page.svc_downloadLookupFileonSuccess = function(variable, data) {

    debugger;
    console.log("Lookup download service call success :" + JSON.stringify(data));
    let blob = base64toBlob(data.value, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    let url = window.URL || window.webkitURL;
    link = url.createObjectURL(blob);

    console.log(link);

    let a = document.createElement("a");
    a.setAttribute("download", Page.Variables.mvLookupFileInfo.dataSet.fileName);
    a.setAttribute("href", link);
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);

};

function base64toBlob(base64Data, contentType) {
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

Page.svc_getLookupMetadataByIdonError = function(variable, data) {

    console.log("File metadata fetch failure :" + JSON.stringify(data));

};
Page.svc_getLookupMetadataByIdonSuccess = function(variable, data) {

    console.log("File metadata fetch success :" + JSON.stringify(data));

    flattenSheetData(data);

    debugger;

    Page.Widgets.lookupFileDefTable.dataset = Page.Variables.mv_sheetData.dataSet;

};

Page.blobToFile = function(blob, filename) {

    const myFile = new File([blob], filename, {
        type: blob.type,
    });

    return myFile;
}

Page.svc_getLookupFileInfoonError = function(variable, data) {
    console.log("Lookup file info failed :" + JSON.stringify(data));


};
Page.svc_getLookupFileInfoonSuccess = function(variable, data) {

    debugger;
    console.log("Lookup file info success :" + JSON.stringify(data));

    Page.Variables.mvLookupFileInfo.dataSet = data;

    //Below logic solely to decode encoded string for description

    Page.Variables.mvLookupFileInfo.dataSet.description = Page.decodeHtml(data.description);
    Page.initialObj = JSON.parse(JSON.stringify(Page.Variables.mvLookupFileInfo.dataSet));
    let blob = base64toBlob(data.fileContent, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    Page.uploadedFileData = Page.blobToFile(blob, data.fileName);
    Page.uploadedFileData.originalFileName = data.fileName;

};

Page.lookupFileUploadSelect = function($event, widget, selectedFiles) {
    debugger;
    toggleFileUploadDisplay(true, widget.$element);
    console.log("In lookupFileUploadSelect");
    if (selectedFiles[0].size > 0) {
        //set up the upload service
        Page.uploadedFileData = selectedFiles[0];
        Page.Variables.mvLookupFileInfo.dataSet.fileName = selectedFiles[0].name;
        Page.Variables.mvLookupFileInfo.dataSet.Created = (Page.Variables.mvLookupFileInfo.dataSet.createdOn ? Page.displayCustomUserAndDateValues(Page.Variables.mvLookupFileInfo.dataSet.createdOn, Page.Variables.mvLookupFileInfo.dataSet.createdBy) : "");
        Page.Variables.mvLookupFileInfo.dataSet.Updated = (Page.Variables.mvLookupFileInfo.dataSet.updatedOn ? Page.displayCustomUserAndDateValues(Page.Variables.mvLookupFileInfo.dataSet.updatedOn, Page.Variables.mvLookupFileInfo.dataSet.updatedBy) : "");
        Page.Widgets.lookupfileLabel.widget.class = '';
        //invoke the service

    } else {
        console.log("Not invoking upload lookup file");
        showUploadStatusMsg("error", FILE_EMPTY_RESPONE_MSG, "statusMessage");
        toggleFileUploadDisplay(false, widget.$element);
    }
};

Page.sv_DVTypeUploadonError = function(variable, data) {
    // console.log(data);
    toggleFileUploadDisplay(false, document.querySelector("div[name='dvTypeUpload']"));
    showUploadStatusMsg("error", "Something went wrong. Please try again!", "statusMessage");
};

Page.sv_DVTypeUploadonSuccess = function(variable, data) {
    // console.log(data);
    toggleFileUploadDisplay(false, document.querySelector("div[name='dvTypeUpload']"));

    if (data.value === FILE_EMPTY_RESPONE_MSG) {
        showUploadStatusMsg("error", FILE_EMPTY_RESPONE_MSG, "statusMessage");
    } else {
        showUploadStatusMsg("success", "Uploaded file bootstraped successfully", "statusMessage");
    }

    //reset the pagination variables
    Page.Variables.currentPage.setValue('dataValue', 0);
    Page.Variables.pageSize.setValue('dataValue', fixedPageSize);
    Page.Variables.totalRecordCount.setValue('dataValue', 0);
    Page.Variables.sortProperties.setValue('dataValue', 'code ASC'); //-> default sort order, update as necessary as on Header clicks of Data table widget


    //call the DV type search service
    let dvTypeSV = Page.Variables.sv_getDVTypeByDescriptionPaginated;
    dvTypeSV.setInput({
        "description": Page.Variables.searchValue.getValue('dataValue'),
        "currentPage": Page.Variables.currentPage.getValue('dataValue'),
        "pageSize": Page.Variables.pageSize.getValue('dataValue'),
        "sortOrders": Page.Variables.sortProperties.getValue('dataValue')
    });

    //invoke the search service
    dvTypeSV.invoke();
};

function toggleFileUploadDisplay(isToShow, elemRef) {
    $("div[class~='app-fileupload'] > div[class~='app-fileupload'] ul[class~='file-upload']").removeClass("display-block").addClass("display-none");

    if (isToShow)
        $("div[class~='app-fileupload'] ul[class~='file-upload']", elemRef).removeClass("display-none").addClass("display-block");
    else
        $("div[class~='app-fileupload'] ul[class~='file-upload']", elemRef).removeClass("display-block").addClass("display-none");
}

function flattenSheetData(sheetDataSet) {

    Page.Variables.mv_sheetData.dataSet = [];
    sheetDataSet.sheetData.forEach((element) => {
        element.columnData.forEach((column) => {
            Page.Variables.mv_sheetData.dataSet.push({
                "sheetName": element.sheetName,
                "columnName": column.columnName,
                "dataType": column.dataType

            });

        });




    });


    console.log("sheetData set generated :" + JSON.stringify(Page.Variables.mv_sheetData.dataSet));

}

Page.decodeHtml = function(html) {
    var txt = document.createElement("textarea");
    txt.innerHTML = html;
    return html == undefined ? "" : txt.value;
};

Page.svc_createUpdateLookupFileonError = function(variable, data) {
    if (data == "Service Call Failed")
        showUploadStatusMsg("error", 'File Not Found', "statusMessage");
    else
        showUploadStatusMsg("error", JSON.stringify(data), "statusMessage");

    console.log("createUpdateLookupFile failure :" + JSON.stringify(data));


};
Page.svc_createUpdateLookupFileonSuccess = function(variable, data) {
    console.log(data);
    debugger;

    toggleFileUploadDisplay(false, document.querySelector("div[name='dvTypeUpload']"));

    if (!(data.error == null)) {
        showUploadStatusMsg("error", data.error.innerError.message, "statusMessage");
    } else {
        showUploadStatusMsg("success", "File uploaded successfully", "statusMessage");
    }
    console.log("createUpdateLookupFile success :" + JSON.stringify(data));
};

function showUploadStatusMsg(status, statusMessage, msgContainerName) {
    //set the status and message
    console.log("showuploadstatusmsg: " + status + " message:" + statusMessage + " container:" + msgContainerName);
    Page.Variables.mv_fileServiceStatus.setData({
        "serviceStatus": status,
        "message": statusMessage
    });

    $("p[name='" + msgContainerName + "']").removeClass("display-none").addClass("display-block");
    setTimeout(function() {
        $("p[name='" + msgContainerName + "']").removeClass("display-block").addClass("display-none");

        //reset status
        Page.Variables.mv_fileServiceStatus.setData({
            "serviceStatus": null,
            "message": null
        });
    }, 5000);

    $('html, body').animate({
        scrollTop: $('[name="' + msgContainerName + '"]').offset().top - 20
    }, 200);
}

Page.getDomainValueTypeRelationshipByIdonSuccess = function(variable, data){
	
};
