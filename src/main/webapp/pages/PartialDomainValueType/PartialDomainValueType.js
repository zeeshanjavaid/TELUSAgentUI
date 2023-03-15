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

const FILE_EMPTY_RESPONE_MSG = "Uploaded file is empty";
const fixedPageSize = 10;
const paginationEventName = "fi-paginationEvent";

let domainValueUploadFile = null;
$("#create_div").css("display", "none");
Partial.onReady = function() {

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



    //parent.document.getElementById('#create_div').style.visibility = "hidden";
    $("#create_div").css("display", "none");

    initPage();
};


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




function initPage() {
    //  const intervalId = setInterval(function() {
    //  if (App.permissionsLoaded) {
    // clearInterval(intervalId);
    // console.log('Permissions loaded...');

    // if (!App.IsUserHasAccess('Setup_DomainValues')) {
    //   if (!App.IsUserHasAccess('System Administration')) {
    //   window.location.href = '#/ErrorLanding';
    //   } else {
    //document.getElementsByTagName("html")[0].style.visibility = "visible";

    //listening to search inputs key-up events
    $(".searchInput").on("keyup", (ev) => {
        //if 'ENTER' is keyed -> invoke search
        if (ev.keyCode && ev.keyCode === 13)
            Partial.searchDV_ButtonClick(ev, Partial.Widgets.searchDV_Button);
    });

    debugger;
    //listening to pagination events from partial
    let paginationContainer = document.querySelector("div[name='dvType_PaginationContainer']");
    paginationContainer.addEventListener(paginationEventName, (ev) => {
        //search/fetch service invocation below
        let dvFetchSV = Partial.Variables.sv_getDVTypeByDescriptionPaginated;
        dvFetchSV.setInput({
            "currentPage": ev.detail.pageNumber,
            "pageSize": Partial.Variables.pageSize.getValue('dataValue'),
            "sortOrders": Partial.Variables.sortProperties.getValue('dataValue'),
            "description": Partial.Variables.searchValue.getValue('dataValue')
        });
        dvFetchSV.invoke();
    });

    //change styling of import DV upload
    $("div[name='importDV'] > div[class~='app-fileupload'] button").addClass("upDown");

    //hiding the message
    $("p[class~='msg']").removeClass("display-block").addClass("display-none");

    Partial.Variables.mv_fileServiceStatus.setData({
        "serviceStatus": null,
        "message": null
    });

    //set the initial data set
    Partial.Variables.currentPage.setValue('dataValue', 0);
    Partial.Variables.pageSize.setValue('dataValue', fixedPageSize);
    Partial.Variables.totalRecordCount.setValue('dataValue', 0);
    Partial.Variables.sortProperties.setValue('dataValue', 'description ASC'); //-> default sort order, update as necessary as on Header clicks of Data table widget



    //call the DV type search service
    let dvTypeSV = Partial.Variables.sv_getDVTypeByDescriptionPaginated;
    dvTypeSV.setInput({
        "description": Partial.Variables.searchValue.getValue('dataValue'),
        "currentPage": Partial.Variables.currentPage.getValue('dataValue'),
        "pageSize": Partial.Variables.pageSize.getValue('dataValue'),
        "sortOrders": Partial.Variables.sortProperties.getValue('dataValue')
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
    Partial.Variables.mv_fileServiceStatus.setData({
        "serviceStatus": status,
        "message": statusMessage
    });

    $("p[name='" + msgContainerName + "']").removeClass("display-none").addClass("display-block");
    setTimeout(function() {
        $("p[name='" + msgContainerName + "']").removeClass("display-block").addClass("display-none");

        //reset status
        Partial.Variables.mv_fileServiceStatus.setData({
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

// Partial.navigateToDVPage = function(domainValueTypeId) {
//     debugger;
//     return "#/DomainValue?domainValueTypeId=" + (!domainValueTypeId ? 0 : domainValueTypeId);
// }

Partial.dvTypeUploadSelect = function($event, widget, selectedFiles) {
    toggleFileUploadDisplay(true, widget.$element);

    if (selectedFiles[0].size > 0) {
        //set up the upload service
        let uploadSV = Partial.Variables.sv_DVTypeUpload;
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

Partial.sv_DVTypeUploadonError = function(variable, data) {
    // console.log(data);
    toggleFileUploadDisplay(false, document.querySelector("div[name='dvTypeUpload']"));
    showUploadStatusMsg("error", "Something went wrong. Please try again!", "statusMessage");
};

Partial.sv_DVTypeUploadonSuccess = function(variable, data) {
    // console.log(data);
    toggleFileUploadDisplay(false, document.querySelector("div[name='dvTypeUpload']"));

    if (data.value === FILE_EMPTY_RESPONE_MSG) {
        showUploadStatusMsg("error", FILE_EMPTY_RESPONE_MSG, "statusMessage");
    } else {
        showUploadStatusMsg("success", "Uploaded file bootstraped successfully", "statusMessage");
    }

    //reset the pagination variables
    Partial.Variables.currentPage.setValue('dataValue', 0);
    Partial.Variables.pageSize.setValue('dataValue', fixedPageSize);
    Partial.Variables.totalRecordCount.setValue('dataValue', 0);
    Partial.Variables.sortProperties.setValue('dataValue', 'code ASC'); //-> default sort order, update as necessary as on Header clicks of Data table widget


    //call the DV type search service
    let dvTypeSV = Partial.Variables.sv_getDVTypeByDescriptionPaginated;
    dvTypeSV.setInput({
        "description": Partial.Variables.searchValue.getValue('dataValue'),
        "currentPage": Partial.Variables.currentPage.getValue('dataValue'),
        "pageSize": Partial.Variables.pageSize.getValue('dataValue'),
        "sortOrders": Partial.Variables.sortProperties.getValue('dataValue')
    });

    //invoke the search service
    dvTypeSV.invoke();
};


/* UI level functions */
Partial.sv_getDVTypeByDescriptionPaginatedonSuccess = function(variable, data) {
    //set pagination values
    Partial.Variables.currentPage.setValue('dataValue', data.pageNumber);

    if (Partial.Variables.totalRecordCount.getValue('dataValue') !== data.totalRecords) {
        Partial.Variables.totalRecordCount.setValue('dataValue', data.totalRecords);
    }
};


Partial.sv_getDVTypeByDescriptionPaginatedonError = function(variable, data) {
    //handle DV search error logic here	
    console.log(data);
};

Partial.exportDV_LinkClick = function($event, widget) {

    Partial.Variables.sv_extractDomainValues.invoke();
};

Partial.sv_extractDomainValuesonSuccess = function(variable, data) {
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


Partial.sv_extractDomainValuesonError = function(variable, data) {
    // console.log(data);
    showUploadStatusMsg("error", "Something went wrong. Please try again!", "statusMessage");
};

Partial.sv_importDomainValuesonError = function(variable, data) {
    // console.log(data);
    toggleFileUploadDisplay(false, document.querySelector("div[name='importDV']"));
    showUploadStatusMsg("error", "Something went wrong. Please try again!", "dvUploadMessage");
};

Partial.sv_importDomainValuesonSuccess = function(variable, data) {
    Partial.Widgets.DVUpload_designDialog.close();
    showUploadStatusMsg("success", "Domain values imported successfully", "statusMessage");
};

Partial.importDVSelect = function($event, widget, selectedFiles) {
    toggleFileUploadDisplay(true, widget.$element);

    if (selectedFiles[0].size <= 0) {
        showUploadStatusMsg("error", FILE_EMPTY_RESPONE_MSG, "dvUploadMessage");
        toggleFileUploadDisplay(false, widget.$element);
    } else
        domainValueUploadFile = selectedFiles[0];
};


Partial.searchDV_ButtonClick = function($event, widget) {

    debugger;
    Partial.Variables.totalRecordCount.setValue('dataValue', 0);
    Partial.Variables.currentPage.setValue('dataValue', 0);
    Partial.Variables.pageSize.setValue('dataValue', fixedPageSize);
    Partial.Variables.sortProperties.setValue('dataValue',
        (Partial.Variables.sortProperties.getValue('dataValue') ?
            Partial.Variables.sortProperties.getValue('dataValue') : 'description ASC'));

    //search/fetch service invocation below
    let dvTypeSV = Partial.Variables.sv_getDVTypeByDescriptionPaginated;
    dvTypeSV.setInput({
        "description": Partial.Variables.searchValue.getValue('dataValue'),
        "currentPage": Partial.Variables.currentPage.getValue('dataValue'),
        "pageSize": Partial.Variables.pageSize.getValue('dataValue'),
        "sortOrders": Partial.Variables.sortProperties.getValue('dataValue')
    });

    dvTypeSV.invoke();
};

// Page.DomainValueTypeTable1Datarender = function(widget, $data) {


//     Page.Variables.currentPage.setValue('dataValue', 0);
//     Page.Variables.pageSize.setValue('dataValue', fixedPageSize);
//     Page.Variables.totalRecordCount.setValue('dataValue', 0);
//     // Page.Variables.sortProperties.setValue('dataValue', 'code ASC'); //-> default sort order, update as necessary as on Header clicks of Data table widget

// };

Partial.displayPageInfo = function() {

    if (Partial.Variables.totalRecordCount.getValue('dataValue') > 0) {
        let start = (Partial.Variables.currentPage.getValue('dataValue') * fixedPageSize) + 1;
        let end = ((start + (fixedPageSize - 1)) > Partial.Variables.totalRecordCount.getValue('dataValue')) ?
            Partial.Variables.totalRecordCount.getValue('dataValue') : (start + (fixedPageSize - 1));

        return start + " to " + end + " from " + (Partial.Variables.totalRecordCount.getValue('dataValue')) + " Domain value types";
    } else
        return "No domain value types";
};

Partial.DomainValueTypeTable1Sort = function($event, $data) {
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
        Partial.Variables.sortProperties.setValue('dataValue',
            (sortDirection === 'DEFAULT') ? "code ASC" : columnName + " " + sortDirection);

        //search/fetch service invocation below
        let dvFetchSV = Partial.Variables.sv_getDVTypeByDescriptionPaginated;
        dvFetchSV.setInput({
            "description": Partial.Variables.searchValue.getValue('dataValue'),
            "currentPage": Partial.Variables.currentPage.getValue('dataValue'),
            "pageSize": Partial.Variables.pageSize.getValue('dataValue'),
            "sortOrders": Partial.Variables.sortProperties.getValue('dataValue')
        });

        dvFetchSV.invoke();
    }, 50);
};

Partial.sv_validateDVUploadFileonError = function(variable, data) {
    showUploadStatusMsg("error", "Unknown error occurred. Please try again later", "dvUploadMessage");
};

Partial.sv_validateDVUploadFileonSuccess = function(variable, data) {
    if (data.value.toUpperCase() === "OK") {
        Partial.Widgets.DVUpload_confirmDialog.open();
    } else
        showUploadStatusMsg("error", data.value, "dvUploadMessage");
};

Partial.SaveDVUpload_btnClick = function($event, widget) {
    debugger;
    if (!domainValueUploadFile)
        showUploadStatusMsg("error", "No file selected. Please select a file first", "dvUploadMessage");
    else {
        //check for file validity
        let sv_ValidateUpload = Partial.Variables.sv_validateDVUploadFile;
        sv_ValidateUpload.setInput({
            "importFile": domainValueUploadFile
        });

        //invoke the validate service
        sv_ValidateUpload.invoke();
    }
};

Partial.DVUpload_designDialogClose = function($event, widget) {
    toggleFileUploadDisplay(false, document.querySelector("div[name='importDV']"));
    domainValueUploadFile = null; //resetting when the DV upload design pop-up is closed
};

Partial.DVUpload_confirmDialogOk = function($event, widget) {
    //close the confirm dialog
    Partial.Widgets.DVUpload_confirmDialog.close();

    let sv_importDV = Partial.Variables.sv_importDomainValues;
    sv_importDV.setInput({
        "importFile": domainValueUploadFile
    });

    //invoke the import service
    sv_importDV.invoke();
};

Partial.DVUpload_designDialogOpened = function($event, widget) {
    $("p[name='dvUploadMessage']").removeClass("display-block").addClass("display-none");
};

Partial.DomainValueTypeTable1Headerclick = function($event, $data) {

};

Partial.searchDV_ButtonDblclick = function($event, widget) {
    debugger;
};

Partial.button6Click = function($event, widget) {
    debugger;
};

Partial.goToDomainValue = function($event, row) {
    debugger;
    Partial.ACtions.goToPage_DomainValue.invoke();
};

Partial.gotoDomainValuePartial = function($event, widget, row) {
    debugger;
    //  $("#create_div").css("display", "none");

    $("#create_div").hide();
    Partial.Variables.RedirectPage.dataSet.dataValue = "RedirectDomainPage";
}

// Page.container2Click = function($event, widget, item, currentItemWidgets) {
//     debugger;
//     if (item.dataValue == 'Users') {
//         Page.Actions.goToPage_Users.invoke();
//     }
// };