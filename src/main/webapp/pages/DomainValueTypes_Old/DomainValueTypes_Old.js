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

const FILE_EMPTY_RESPONE_MSG = "Uploaded file is empty";
const fixedPageSize = 10;
const paginationEventName = "fi-paginationEvent";

let domainValueUploadFile = null;

// $('document').ready(() => {
//     document.getElementsByTagName("html")[0].style.visibility = "hidden";
// });

/* perform any action on widgets/variables within this block */
Page.onReady = function() {
    initPage();
};

function initPage() {
    //  const intervalId = setInterval(function() {
    //  if (App.permissionsLoaded) {
    // clearInterval(intervalId);
    // console.log('Permissions loaded...');

    // if (!App.IsUserHasAccess('Setup_DomainValues')) {
    //   if (!App.IsUserHasAccess('System Administration')) {
    //   window.location.href = '#/ErrorLanding';
    //   } else {
    document.getElementsByTagName("html")[0].style.visibility = "visible";

    //listening to search inputs key-up events
    $(".searchInput").on("keyup", (ev) => {
        //if 'ENTER' is keyed -> invoke search
        if (ev.keyCode && ev.keyCode === 13)
            Page.searchDV_ButtonClick(ev, Page.Widgets.searchDV_Button);
    });

    debugger;
    //listening to pagination events from partial
    let paginationContainer = document.querySelector("div[name='dvType_PaginationContainer']");
    paginationContainer.addEventListener(paginationEventName, (ev) => {
        //search/fetch service invocation below
        let dvFetchSV = Page.Variables.sv_getDVTypeByDescriptionPaginated;
        dvFetchSV.setInput({
            "currentPage": ev.detail.pageNumber,
            "pageSize": Page.Variables.pageSize.getValue('dataValue'),
            "sortOrders": Page.Variables.sortProperties.getValue('dataValue'),
            "description": Page.Variables.searchValue.getValue('dataValue')
        });
        dvFetchSV.invoke();
    });

    //change styling of import DV upload
    $("div[name='importDV'] > div[class~='app-fileupload'] button").addClass("upDown");

    //hiding the message
    $("p[class~='msg']").removeClass("display-block").addClass("display-none");

    Page.Variables.mv_fileServiceStatus.setData({
        "serviceStatus": null,
        "message": null
    });

    //set the initial data set
    Page.Variables.currentPage.setValue('dataValue', 0);
    Page.Variables.pageSize.setValue('dataValue', fixedPageSize);
    Page.Variables.totalRecordCount.setValue('dataValue', 0);
    Page.Variables.sortProperties.setValue('dataValue', 'description ASC'); //-> default sort order, update as necessary as on Header clicks of Data table widget



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
    //  }
    //        } else {
    //        //determining the time elapsed since App started in minutes
    //        const timeElapsedSinceAppStart = moment(new Date()).diff(moment(App.appStartTime), 'minutes');
    //
    //        if (timeElapsedSinceAppStart < 1)
    //        console.log('Waiting to load permissions...');
    //        else {
    //        clearInterval(intervalId);
    //
    //        //if the active page is not 'ErrorLanding'
    //        if (window.location.hash !== '#/ErrorLanding')
    //        window.location.href = '#/ErrorLanding';
    //        }
    //        }
    // }, 10);
}

/* utility and helper functions */

function showUploadStatusMsg(status, statusMessage, msgContainerName) {
    //set the status and message
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
}

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

function toggleFileUploadDisplay(isToShow, elemRef) {
    debugger;
    $("div[class~='app-fileupload'] > div[class~='app-fileupload'] ul[class~='file-upload']").removeClass("display-block").addClass("display-none");

    if (isToShow)
        $("div[class~='app-fileupload'] ul[class~='file-upload']", elemRef).removeClass("display-none").addClass("display-block");
    else
        $("div[class~='app-fileupload'] ul[class~='file-upload']", elemRef).removeClass("display-block").addClass("display-none");
}

/* other UI level functions */

Page.navigateToDVPage = function(domainValueTypeId) {
    return "#/DomainValue?domainValueTypeId=" + (!domainValueTypeId ? 0 : domainValueTypeId);
}

Page.dvTypeUploadSelect = function($event, widget, selectedFiles) {
    toggleFileUploadDisplay(true, widget.$element);

    if (selectedFiles[0].size > 0) {
        //set up the upload service
        let uploadSV = Page.Variables.sv_DVTypeUpload;
        uploadSV.setInput({
            "bootstrapExcel": selectedFiles[0]
        });

        //invoke the service
        uploadSV.invoke();
    } else {
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


/* UI level functions */
Page.sv_getDVTypeByDescriptionPaginatedonSuccess = function(variable, data) {
    //set pagination values
    Page.Variables.currentPage.setValue('dataValue', data.pageNumber);

    if (Page.Variables.totalRecordCount.getValue('dataValue') !== data.totalRecords) {
        Page.Variables.totalRecordCount.setValue('dataValue', data.totalRecords);
    }
};


Page.sv_getDVTypeByDescriptionPaginatedonError = function(variable, data) {
    //handle DV search error logic here	
    console.log(data);
};

Page.exportDV_LinkClick = function($event, widget) {

    Page.Variables.sv_extractDomainValues.invoke();
};

Page.sv_extractDomainValuesonSuccess = function(variable, data) {
    // console.log(data.value);

    let blob = base64toBlob(data.value, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

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
};


Page.sv_extractDomainValuesonError = function(variable, data) {
    // console.log(data);
    showUploadStatusMsg("error", "Something went wrong. Please try again!", "statusMessage");
};

Page.sv_importDomainValuesonError = function(variable, data) {
    // console.log(data);
    toggleFileUploadDisplay(false, document.querySelector("div[name='importDV']"));
    showUploadStatusMsg("error", "Something went wrong. Please try again!", "dvUploadMessage");
};

Page.sv_importDomainValuesonSuccess = function(variable, data) {
    Page.Widgets.DVUpload_designDialog.close();
    showUploadStatusMsg("success", "Domain values imported successfully", "statusMessage");
};

Page.importDVSelect = function($event, widget, selectedFiles) {
    toggleFileUploadDisplay(true, widget.$element);

    if (selectedFiles[0].size <= 0) {
        showUploadStatusMsg("error", FILE_EMPTY_RESPONE_MSG, "dvUploadMessage");
        toggleFileUploadDisplay(false, widget.$element);
    } else
        domainValueUploadFile = selectedFiles[0];
};


Page.searchDV_ButtonClick = function($event, widget) {

    debugger;
    Page.Variables.totalRecordCount.setValue('dataValue', 0);
    Page.Variables.currentPage.setValue('dataValue', 0);
    Page.Variables.pageSize.setValue('dataValue', fixedPageSize);
    Page.Variables.sortProperties.setValue('dataValue',
        (Page.Variables.sortProperties.getValue('dataValue') ?
            Page.Variables.sortProperties.getValue('dataValue') : 'description ASC'));

    //search/fetch service invocation below
    let dvTypeSV = Page.Variables.sv_getDVTypeByDescriptionPaginated;
    dvTypeSV.setInput({
        "description": Page.Variables.searchValue.getValue('dataValue'),
        "currentPage": Page.Variables.currentPage.getValue('dataValue'),
        "pageSize": Page.Variables.pageSize.getValue('dataValue'),
        "sortOrders": Page.Variables.sortProperties.getValue('dataValue')
    });

    dvTypeSV.invoke();
};

// Page.DomainValueTypeTable1Datarender = function(widget, $data) {


//     Page.Variables.currentPage.setValue('dataValue', 0);
//     Page.Variables.pageSize.setValue('dataValue', fixedPageSize);
//     Page.Variables.totalRecordCount.setValue('dataValue', 0);
//     // Page.Variables.sortProperties.setValue('dataValue', 'code ASC'); //-> default sort order, update as necessary as on Header clicks of Data table widget

// };

Page.displayPageInfo = function() {

    if (Page.Variables.totalRecordCount.getValue('dataValue') > 0) {
        let start = (Page.Variables.currentPage.getValue('dataValue') * fixedPageSize) + 1;
        let end = ((start + (fixedPageSize - 1)) > Page.Variables.totalRecordCount.getValue('dataValue')) ?
            Page.Variables.totalRecordCount.getValue('dataValue') : (start + (fixedPageSize - 1));

        return start + " to " + end + " from " + (Page.Variables.totalRecordCount.getValue('dataValue')) + " Domain value types";
    } else
        return "No domain value types";
};

Page.DomainValueTypeTable1Sort = function($event, $data) {
    setTimeout(() => {
        let columnName = $($event.currentTarget).attr("data-col-field");
        let sortDirection = "DEFAULT"; //-> default direction
        let sortClass = $("span[class~='sort-buttons-container'] > i", $event.currentTarget).attr("class");

        if (sortClass.toLowerCase().indexOf("desc") > -1)
            sortDirection = "DESC";
        else if (sortClass.toLowerCase().indexOf("asc") > -1)
            sortDirection = "ASC";
        else
            sortDirection = "DEFAULT";

        //set up service inputs
        Page.Variables.sortProperties.setValue('dataValue',
            (sortDirection === 'DEFAULT') ? "code ASC" : columnName + " " + sortDirection);

        //search/fetch service invocation below
        let dvFetchSV = Page.Variables.sv_getDVTypeByDescriptionPaginated;
        dvFetchSV.setInput({
            "description": Page.Variables.searchValue.getValue('dataValue'),
            "currentPage": Page.Variables.currentPage.getValue('dataValue'),
            "pageSize": Page.Variables.pageSize.getValue('dataValue'),
            "sortOrders": Page.Variables.sortProperties.getValue('dataValue')
        });

        dvFetchSV.invoke();
    }, 50);
};

Page.sv_validateDVUploadFileonError = function(variable, data) {
    showUploadStatusMsg("error", "Unknown error occurred. Please try again later", "dvUploadMessage");
};

Page.sv_validateDVUploadFileonSuccess = function(variable, data) {
    if (data.value.toUpperCase() === "OK") {
        Page.Widgets.DVUpload_confirmDialog.open();
    } else
        showUploadStatusMsg("error", data.value, "dvUploadMessage");
};

Page.SaveDVUpload_btnClick = function($event, widget) {
    if (!domainValueUploadFile)
        showUploadStatusMsg("error", "No file selected. Please select a file first", "dvUploadMessage");
    else {
        //check for file validity
        let sv_ValidateUpload = Page.Variables.sv_validateDVUploadFile;
        sv_ValidateUpload.setInput({
            "importFile": domainValueUploadFile
        });

        //invoke the validate service
        sv_ValidateUpload.invoke();
    }
};

Page.DVUpload_designDialogClose = function($event, widget) {
    toggleFileUploadDisplay(false, document.querySelector("div[name='importDV']"));
    domainValueUploadFile = null; //resetting when the DV upload design pop-up is closed
};

Page.DVUpload_confirmDialogOk = function($event, widget) {
    //close the confirm dialog
    Page.Widgets.DVUpload_confirmDialog.close();

    let sv_importDV = Page.Variables.sv_importDomainValues;
    sv_importDV.setInput({
        "importFile": domainValueUploadFile
    });

    //invoke the import service
    sv_importDV.invoke();
};

Page.DVUpload_designDialogOpened = function($event, widget) {
    $("p[name='dvUploadMessage']").removeClass("display-block").addClass("display-none");
};
Page.DomainValueTypeTable1Headerclick = function($event, $data) {

};
Page.searchDV_ButtonDblclick = function($event, widget) {
    debugger;
};
Page.button6Click = function($event, widget) {
    debugger;
};