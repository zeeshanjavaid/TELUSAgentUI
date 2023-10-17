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


const fixedPageSize = 20;
const exportSheetName = "ACTIVITY_LOG";
const paginationEvent = "fi-paginationEvent";
Page.showErrorMsg = false;
Page.formattedDataDownload = "";
Page.formattedData = "";
Page.isPaginationChange = false;

// $('document').ready(() => {
//     $("input[name='activity_end_date']").blur();
// });

$('document').ready(() => {
    document.getElementsByTagName("html")[0].style.visibility = "hidden";
});


/* perform any action on widgets/variables within this block */
Page.onReady = function() {


    initPage();
};

function initPage() {
    const intervalId = setInterval(function() {
        if (App.permissionsLoaded) {
            clearInterval(intervalId);
            console.log('Permissions loaded...');

            if (!App.IsUserHasAccess('Access_ActivityLog')) {
                window.location.href = "/ErrorLanding"
            } else {
                document.getElementsByTagName("html")[0].style.visibility = "visible";
                Page.Variables.mv_fileServiceStatus.setData({
                    "serviceStatus": null,
                    "message": null
                });

                //listening to pagination events from partial
                let paginationContainer = document.querySelector("div[name='al_PaginationContainer']");
                paginationContainer.addEventListener(paginationEvent, (ev) => {
                    Page.isPaginationChange = true;

                    //updating the current page value (extract purposes only)
                    Page.Variables.prevSearchFilters.dataSet.currentPage = ev.detail.pageNumber;

                    //search/fetch service invocation below
                    let dvFetchSV = Page.Variables.sv_activityLogSearchResult;
                    dvFetchSV.setInput({
                        "pageNumber": ev.detail.pageNumber,
                        "pageSize": Page.Variables.mv_pageSize.getValue('dataValue'),
                        "sortProperties": Page.Variables.mv_sortProperties.getValue('dataValue'),
                        "applicationNumber": Page.Variables.mv_activityLogSearchFilter.getData().appnum,
                        "activityType": Page.Variables.mv_activityLogSearchFilter.getData().activitytype,
                        "activityName": (Page.Variables.mv_activityLogSearchFilter.getData().activityname) ? (Page.Variables.mv_activityLogSearchFilter.getData().activityname + "%") : null,
                        "isAppHistory": false,
                        "createdDateStart": Page.Variables.mv_activityLogSearchFilter.getData().startdate,
                        "createdDateEnd": Page.Variables.mv_activityLogSearchFilter.getData().enddate,
                    });


                    dvFetchSV.invoke();
                });



                //listening to search inputs key-up events
                $("select.searchInput").on("keydown", (ev) => {
                    //if 'ENTER' is keyed -> invoke search
                    if (ev.keyCode && ev.keyCode === 13)
                        Page.btn_SearchClick(ev, Page.Widgets.btn_Search);
                });

                $(".searchInput").not("select").on("keyup", (ev) => {
                    //if 'ENTER' is keyed -> invoke search
                    if (ev.keyCode && ev.keyCode === 13)
                        Page.btn_SearchClick(ev, Page.Widgets.btn_Search);
                });

                //setting initial vars
                Page.Variables.mv_currentPage.setValue('dataValue', 0);
                Page.Variables.mv_pageSize.setValue('dataValue', fixedPageSize);
                Page.Variables.mv_totalRecords.setValue('dataValue', 0);
                Page.Variables.mv_sortProperties.setValue('dataValue', 'activityStartTime DESC');
                Page.btn_ClearClick();

                //storing the search, sorting & pagination filters
                Page.Variables.prevSearchFilters.setData({
                    "applicationNumber": null,
                    "activityType": null,
                    "activityName": null,
                    "startDate": null,
                    "endDate": null,
                    "pageNumber": null,
                    "pageSize": null,
                    "sortProperties": null
                });
            }
        } else {
            //determining the time elapsed since App started in minutes
            const timeElapsedSinceAppStart = moment(new Date()).diff(moment(App.appStartTime), 'minutes');

            if (timeElapsedSinceAppStart < 1)
                console.log('Waiting to load permissions...');
            else {
                clearInterval(intervalId);
                if (window.location.hash !== '/ErrorLanding')
                    window.location.href = '/ErrorLanding';
            }
        }
    }, 10);
}



Page.displayPageInfo = function() {
    if (Page.Variables.mv_totalRecords.getValue('dataValue') > 0) {
        let start = (Page.Variables.mv_currentPage.getValue('dataValue') * fixedPageSize) + 1;
        let end = ((start + (fixedPageSize - 1)) > Page.Variables.mv_totalRecords.getValue('dataValue')) ?
            Page.Variables.mv_totalRecords.getValue('dataValue') : (start + (fixedPageSize - 1));

        return start + " to " + end + " from " + (Page.Variables.mv_totalRecords.getValue('dataValue')) + " Activities";
    } else
        return "No Activities";
};



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

Page.btn_SearchClick = function($event, widget) {
    Page.isPaginationChange = false;
    Page.ErrorMsg = "";
    Page.showErrorMsg = false;

    if (!Page.Variables.mv_activityLogSearchFilter.getData().appnum && !Page.Variables.mv_activityLogSearchFilter.getData().activitytype && !Page.Variables.mv_activityLogSearchFilter.getData().activityname && !Page.Variables.mv_activityLogSearchFilter.getData().startdate && !Page.Variables.mv_activityLogSearchFilter.getData().enddate) {
        Page.ErrorMsg = "At least one search field is required";
        Page.showErrorMsg = true;

    } else {
        Page.Variables.mv_currentPage.setValue('dataValue', 0);
        Page.Variables.mv_totalRecords.setValue('dataValue', 0);

        //storing the search, sorting & pagination filters
        Page.Variables.prevSearchFilters.setData({
            "applicationNumber": Page.Variables.mv_activityLogSearchFilter.getData().appnum,
            "activityType": Page.Variables.mv_activityLogSearchFilter.getData().activitytype,
            "activityName": Page.Variables.mv_activityLogSearchFilter.getData().activityname,
            "startDate": Page.Variables.mv_activityLogSearchFilter.getData().startdate,
            "endDate": Page.Variables.mv_activityLogSearchFilter.getData().enddate,
            "pageNumber": Page.Variables.mv_currentPage.getValue('dataValue'),
            "pageSize": Page.Variables.mv_pageSize.getValue('dataValue'),
            "sortProperties": Page.Variables.mv_sortProperties.getValue('dataValue')

        });

        Page.Variables.sv_activityLogSearchResult.setInput({
            "applicationNumber": Page.Variables.mv_activityLogSearchFilter.getData().appnum,
            "activityType": Page.Variables.mv_activityLogSearchFilter.getData().activitytype,
            "activityName": (Page.Variables.mv_activityLogSearchFilter.getData().activityname) ? (Page.Variables.mv_activityLogSearchFilter.getData().activityname + "%") : null,
            "isAppHistory": false,
            "createdDateStart": Page.Variables.mv_activityLogSearchFilter.getData().startdate,
            "createdDateEnd": Page.Variables.mv_activityLogSearchFilter.getData().enddate,
            "pageNumber": Page.Variables.mv_currentPage.getValue('dataValue'),
            "pageSize": Page.Variables.mv_pageSize.getValue('dataValue'),
            "sortProperties": Page.Variables.mv_sortProperties.getValue('dataValue')
        });

        Page.Variables.sv_activityLogSearchResult.invoke();
    }

};


Page.btn_ClearClick = function($event, widget) {
    Page.Variables.mv_activityLogSearchFilter.getData().appnum = null;
    Page.Variables.mv_activityLogSearchFilter.getData().activitytype = null;
    Page.Variables.mv_activityLogSearchFilter.getData().activityname = null;
    Page.Variables.mv_activityLogSearchFilter.getData().startdate = null;
    Page.Variables.mv_activityLogSearchFilter.getData().enddate = null;
};


Page.sv_activityLogSearchResultonError = function(variable, data) {
    console.log(data);
};


Page.sv_activityLogSearchResultonSuccess = function(variable, data) {
    Page.Variables.mv_currentPage.setValue('dataValue', data.pageNumber);

    if (!Page.isPaginationChange) {
        if (Page.Variables.mv_totalRecords.getValue('dataValue') !== data.totalElements)
            Page.Variables.mv_totalRecords.setValue('dataValue', data.totalElements);
    }
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


Page.export_activitiesClick = function($event, widget) {
    //  
    Page.Variables.sv_ExtractActivityLog.setInput({
        "exportType": exportSheetName,
        "applicationNumber": Page.Variables.prevSearchFilters.getData().applicationNumber,
        "activityType": Page.Variables.prevSearchFilters.getData().activityType,
        "activityName": (Page.Variables.prevSearchFilters.getData().activityName) ?
            (Page.Variables.prevSearchFilters.getData().activityName + "%") : null,
        "isAppHistory": false,
        "createdDateStart": Page.Variables.prevSearchFilters.getData().startDate,
        "createdDateEnd": Page.Variables.prevSearchFilters.getData().endDate,
        "pageNumber": Page.Variables.prevSearchFilters.getData().currentPage,
        "pageSize": Page.Variables.prevSearchFilters.getData().pageSize,
        "sortProperties": Page.Variables.prevSearchFilters.getData().sortProperties
    });


    Page.Variables.sv_ExtractActivityLog.invoke();

};

Page.sv_ExtractActivityLogonError = function(variable, data) {
    console.log(data);
    showUploadStatusMsg("error", "Something went wrong. Please try again!", "statusMessage");
};


Page.sv_ExtractActivityLogonSuccess = function(variable, data) {

    let blob = base64toBlob(data.value, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    // let blob = new Blob([data.value], {
    //     type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    // })

    let url = window.URL || window.webkitURL;
    link = url.createObjectURL(blob);

    console.log(link);

    let a = document.createElement("a");
    a.setAttribute("download", "ActivityLogs.xlsx");
    a.setAttribute("href", link);
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
};


Page.getActivityEventLogsTable1Sort = function($event, $data) {
    setTimeout(() => {
        let columnName = $($event.currentTarget).attr("data-col-field");
        let sortClass = $("span[class~='sort-buttons-container'] > i", $event.currentTarget).attr("class");
        //actual service invocation & vars update
        if (sortClass.indexOf("desc") > -1)
            sortDirection = "DESC";
        else if (sortClass.indexOf("asc") > -1)
            sortDirection = "ASC";
        else
            sortDirection = "DEFAULT";

        //set up service inputs
        Page.Variables.mv_sortProperties.setValue('dataValue', (sortDirection === 'DEFAULT') ? "activityStartTime DESC" : (columnName + " " + sortDirection));
        Page.Variables.prevSearchFilters.dataSet.sortProperties = Page.Variables.mv_sortProperties.getValue('dataValue');

        //search/fetch service invocation below
        let dvFetchSV = Page.Variables.sv_activityLogSearchResult;
        dvFetchSV.setInput({
            "pageNumber": Page.Variables.mv_currentPage.getValue('dataValue'),
            "pageSize": Page.Variables.mv_pageSize.getValue('dataValue'),
            "sortProperties": Page.Variables.mv_sortProperties.getValue('dataValue'),
            "applicationNumber": Page.Variables.mv_activityLogSearchFilter.getData().appnum,
            "activityType": Page.Variables.mv_activityLogSearchFilter.getData().activitytype,
            "activityName": (Page.Variables.mv_activityLogSearchFilter.getData().activityname) ? (Page.Variables.mv_activityLogSearchFilter.getData().activityname + "%") : null,
            "isAppHistory": false,
            "createdDateStart": Page.Variables.mv_activityLogSearchFilter.getData().startdate,
            "createdDateEnd": Page.Variables.mv_activityLogSearchFilter.getData().enddate,
        });


        dvFetchSV.invoke();

    }, 50);
};

// Page.getActivityEventLogsTable1Headerclick = function($event, $data) {

// };

// Dialog
Page.dialog1Opened = function($event, widget) {

    Page.Widgets.requestResponseTabs.defaultpaneindex = 0;
};

Page.getDownloadButtonLabel = function() {
    //  
    var buttonLabel;
    if (Page.Variables.isRequestStructureAvailable.dataSet.dataValue && Page.Variables.isResponseStructureAvailable.dataSet.dataValue) {
        var activeTab = Page.Widgets.requestResponseTabs.activeTab.name;
        if (activeTab === 'requestPane') {
            buttonLabel = Page.appLocale.REQUEST_DOWNLOAD;
        } else if (activeTab === 'responsePane') {
            buttonLabel = Page.appLocale.RESPONSE_DOWNLOAD;
        }
    } else {
        if (Page.Variables.isRequestStructureAvailable.dataSet.dataValue) {
            buttonLabel = Page.appLocale.REQUEST_DOWNLOAD;
        } else if (Page.Variables.isResponseStructureAvailable.dataSet.dataValue) {
            buttonLabel = Page.appLocale.RESPONSE_DOWNLOAD;
        } else {
            throw new Error('Unable to detect dowload button label');
        }
    }
    return buttonLabel;
    // return "Download";
}


Page.downloadRequestResponseLinkClick = function($event, widget) {
    var fileName;
    var content = Page.formattedDataDownload;
    var blob = new Blob([Page.formattedDataDownload]);
    var url = URL.createObjectURL(blob);
    var anchor = document.createElement("a");
    anchor.href = url;

    if (Page.Variables.isRequestStructureAvailable.dataSet.dataValue && Page.Variables.isResponseStructureAvailable.dataSet.dataValue) {
        var activeTab = Page.Widgets.requestResponseTabs.activeTab.name;
        if (activeTab === 'requestPane') {
            fileName = Page.Widgets.getActivityEventLogsTable1.selecteditem.activityName.concat("_", Page.Widgets.getActivityEventLogsTable1.selecteditem.applicationNumber, "_", Page.appLocale.LABEL_REQUEST);
        } else if (activeTab === 'responsePane') {
            fileName = Page.Widgets.getActivityEventLogsTable1.selecteditem.activityName.concat("_", Page.Widgets.getActivityEventLogsTable1.selecteditem.applicationNumber, "_", Page.appLocale.LABEL_RESPONSE);
        }
    } else {
        if (Page.Variables.isRequestStructureAvailable.dataSet.dataValue) {
            fileName = Page.Widgets.getActivityEventLogsTable1.selecteditem.activityName.concat("_", Page.Widgets.getActivityEventLogsTable1.selecteditem.applicationNumber, "_", Page.appLocale.LABEL_REQUEST);
        } else if (Page.Variables.isResponseStructureAvailable.dataSet.dataValue) {
            fileName = Page.Widgets.getActivityEventLogsTable1.selecteditem.activityName.concat("_", Page.Widgets.getActivityEventLogsTable1.selecteditem.applicationNumber, "_", Page.appLocale.LABEL_RESPONSE);
        }
    }

    if (!fileName) {
        fileName = 'download'
    }

    if (content) {
        anchor.download = fileName + '.txt';
        anchor.click();
    }
};

Page.requestPaneSelect = function($event, widget) {

    Page.loadRequestResponseStructure(Page.Variables.selectedRequestResponse.requestContent);
};

Page.responsePaneSelect = function($event, widget) {

    Page.loadRequestResponseStructure(Page.Variables.selectedRequestResponse.responseContent);
};

Page.canViewContentType = function(contentType) {
    if (!contentType)
        return false;

    var content = contentType.split(';');
    var encoding = content[1];
    var type = content[0];
    var allowedTypes = ['json', 'text', 'xml', 'vnd.com.fico.studio.v1_0+json'];
    var isAllowed = false;
    if (type) {
        type = type.trim();
        isAllowed = allowedTypes.some(function(allowedType) {
            var parts = type.toLowerCase().split('/');
            return allowedType === parts[0] || allowedType === parts[1];
        });
    }
    return isAllowed;
}

Page.getActivityEventLogsTable1_actionsAction = function(row) {
    Page.Variables.currentActivityName.dataSet.dataValue = row.activityName;
    Page.Variables.sv_ProcessActivityPayloadData.setInput("activityPayloadId", row.activityPayloadId);
    Page.Variables.sv_ProcessActivityPayloadData.invoke();

};

Page.sv_ProcessActivityPayloadDataonError = function(variable, data) {

};
Page.sv_ProcessActivityPayloadDataonSuccess = function(variable, data) {
    Page.Variables.selectedRequestResponse = data;
    Page.Variables.isRequestStructureAvailable.dataSet.dataValue = !!data.requestContent;
    Page.Variables.isResponseStructureAvailable.dataSet.dataValue = !!data.responseContent;

    if (Page.Variables.isRequestStructureAvailable.dataSet.dataValue) {
        Page.loadRequestResponseStructure(data.requestContent);
    } else if (Page.Variables.isResponseStructureAvailable.dataSet.dataValue) {
        Page.loadRequestResponseStructure(data.responseContent);
    } else {
        console.warn('No request response identifier found');
    }
    Page.Widgets.dialog1.open();
};

Page.loadRequestResponseStructure = function(data) {
    Page.updateDownloadAvailability(data);
    var formattedData = "";

    if (!data)
        return;
    let isDataJson = Page.isJson(data);

    if (isDataJson) {
        formattedData = JSON.stringify(JSON.parse(data), null, 2);
        const unescapedString = Page.unescapeJson(formattedData);
        data = hljs.highlightAuto(unescapedString, ['xml', 'json', 'html']).value;
    } else {
        formattedData = html_beautify(data);
        // var highlightedCode = hljs.highlightAuto(formattedData, ['html', 'xml', 'json']);
        // var markup;
        // if (highlightedCode.value.indexOf('hljs-attr') !== -1) {
        //     markup = highlightedCode.value;
        // } else {
        //     markup = highlightedCode.second_best.value;
        // }
        // data.formattedValue = hljs.fixMarkup(markup);
    }

    // data.formattedValue = hljs.fixMarkup(markup);
    Page.formattedData = (data != null && data !== undefined) ? data : formattedData;
    Page.formattedDataDownload = formattedData;
};

Page.updateDownloadAvailability = function(data) {
    Page.Variables.isDownloadAvailable.dataSet.dataValue = !!data;
};

Page.isJson = function(value) {
    try {
        JSON.parse(value);
    } catch (e) {
        return false;
    }
    return true;
};

Page.unescapeJson = function(inputString) {
    const unescapedString = inputString
        .replace(/\\n/gm, '\n')
        .replace(/\\r/gm, '\r')
        .replace(/\\t/gm, '\t')
        .replace(/\\b/gm, '\b')
        .replace(/\\\"/gm, '\"');

    return unescapedString;
};