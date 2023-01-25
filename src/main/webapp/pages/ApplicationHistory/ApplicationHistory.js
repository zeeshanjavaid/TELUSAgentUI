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
Page.applicationNumber = "";
Page.applicationStatus = "";
Page.applicationStatusId;
const paginationEventName = "fi-paginationEvent";
const fixedPageSize = 10;

/* perform any action on widgets/variables within this block */
Page.onReady = function() {
    /*
     * variables can be accessed through 'Page.Variables' property here
     * e.g. to get dataSet in a staticVariable named 'loggedInUser' use following script
     * Page.Variables.loggedInUser.getData()
     *
     * widgets can be accessed through 'Page.Widgets' property here
     * e.g. to get value of text widget named 'username' use following script
     * 'Page.Widgets.username.datavalue'
     */
    initPage();

};

function initPage() {
    const intervalId = setInterval(function() {
        if (App.permissionsLoaded) {
            clearInterval(intervalId);
            console.log('Permissions loaded...');

            if (!App.IsUserHasAccess('Access_ApplicationHistory')) {
                window.location.href = "#/ErrorLanding"
            } else {
                document.getElementsByTagName("html")[0].style.visibility = "visible";
                // console.log("on Ready is called");
                if (Page.pageParams.applicationId) {
                    Page.Variables.getApplicationById.setInput({
                        "applicantionId": Page.pageParams.applicationId
                    });

                    Page.Variables.getApplicationById.invoke();
                }

                //listening to pagination events from partial
                let paginationContainer = document.querySelector("div[name='dataChange_PaginationContainer']");
                paginationContainer.addEventListener(paginationEventName, (ev) => {
                    //search/fetch service invocation below
                    let dataChangeFetchSVpaginated = Page.Variables.sv_DataChangeHistoryList;
                    dataChangeFetchSVpaginated.setInput({
                        "applicationId": Page.pageParams.applicationId,
                        "currentPage": ev.detail.pageNumber,
                        "pageSize": Page.Variables.pageSize.getValue('dataValue'),
                        "sortOrders": Page.Variables.sortProperties.getValue('dataValue'),
                        "userLocaleArg": Page.Variables.defaultLocale.getValue('dataValue')
                    });
                    dataChangeFetchSVpaginated.invoke();
                });

                //set the initial data set
                Page.Variables.defaultLocale.setValue('dataValue', (App.getCurrentLocale() ? App.getCurrentLocale() : 'en'));
                Page.Variables.totalRecordCount.setValue('dataValue', 0);
                Page.Variables.currentPage.setValue('dataValue', 0);
                Page.Variables.pageSize.setValue('dataValue', fixedPageSize);
                Page.Variables.sortProperties.setValue('dataValue', 'createdOn DESC'); //-> default sort order, update as necessary as on Header clicks of Data table widget

                let dataChangeFetchSV = Page.Variables.sv_DataChangeHistoryList;
                dataChangeFetchSV.setInput({
                    "applicationId": Page.pageParams.applicationId,
                    "currentPage": Page.Variables.currentPage.getValue('dataValue'),
                    "pageSize": Page.Variables.pageSize.getValue('dataValue'),
                    "sortOrders": Page.Variables.sortProperties.getValue('dataValue'),
                    "userLocaleArg": Page.Variables.defaultLocale.getValue('dataValue')
                });

                dataChangeFetchSV.invoke();

                Page.Widgets.processingHistoryPane.select(); // default pane selected
            }
        } else {
            //determining the time elapsed since App started in minutes
            const timeElapsedSinceAppStart = moment(new Date()).diff(moment(App.appStartTime), 'minutes');

            if (timeElapsedSinceAppStart < 1)
                console.log('Waiting to load permissions...');
            else {
                clearInterval(intervalId);
                if (window.location.hash !== '#/ErrorLanding')
                    window.location.href = '#/ErrorLanding';
            }
        }
    }, 10);
}

Page.dataChangePaneSelect = function($event, widget) {
    console.log("when is this called? tabpane2Select");
};

Page.sv_DataChangeHistoryListonSuccess = function(variable, data) {
    console.log(data);
    //set pagination values
    Page.Variables.currentPage.setValue('dataValue', data.pageNumber);

    if (Page.Variables.totalRecordCount.getValue('dataValue') !== data.totalRecords) {
        Page.Variables.totalRecordCount.setValue('dataValue', data.totalRecords);
    }
};

Page.dataChangeHistoryTableSort = function($event, $data) {
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
            (sortDirection === 'DEFAULT') ? "createdOn DESC" : columnName + " " + sortDirection);

        //search/fetch service invocation below

        let dataChangeFetchSV = Page.Variables.sv_DataChangeHistoryList;
        dataChangeFetchSV.setInput({
            "applicationId": Page.pageParams.applicationId,
            "currentPage": Page.Variables.currentPage.getValue('dataValue'),
            "pageSize": Page.Variables.pageSize.getValue('dataValue'),
            "sortOrders": Page.Variables.sortProperties.getValue('dataValue'),
            "userLocaleArg": Page.Variables.defaultLocale.getValue('dataValue')
        });

        dataChangeFetchSV.invoke();
    }, 50);

};

Page.getPageTitle = function() {
    if (Page.applicationId === 0)
        return Page.appLocale.APPLICATION_HISTORY;
    else
        return Page.appLocale.APPLICATION_HISTORY + " " + Page.applicationNumber + " ";
};
Page.getPageStatus = function() {
    return Page.applicationStatus;
};
Page.getApplicationByIdonSuccess = function(variable, data) {
    if (data) {
        var applicationData = JSON.parse(JSON.stringify(data));
        Page.applicationNumber = applicationData.applicationNumber;
        // get Application Status DV
        let applicationStatusDV = Page.Variables.dvs_applicationStatus.dataSet.find((appStatusDV) => {
            return appStatusDV.id === applicationData.applicationDetails.applicationStatus;
        });
        Page.applicationStatusId = applicationData.applicationDetails.applicationStatus;
        Page.applicationStatus = applicationStatusDV.description;
    }
};
Page.backBtnClick = function($event, widget) {
    App.Actions.goToPage_ApplicationBroker.invoke({
        data: {
            "applicationId": Page.pageParams.applicationId,
            "applicationStatusId": Page.applicationStatusId
        }
    });
};

// Page.fetchDateTime = function(unformattedDate) {
//     if (unformattedDate) {
//         return new Date(unformattedDate).toLocaleString(Page.appLocale.CURRENT_SELECTED_LOCALE, {
//             year: "numeric",
//             month: "2-digit",
//             day: "2-digit",
//             hour: "2-digit",
//             minute: "2-digit",
//             second: "2-digit",
//             hour12: true
//         }).toUpperCase().replace(",", "");
//     }
// }

Page.updateColumnName = function(columnName) {
    if (columnName == "UPDATE")
        return "Update";
    else if (columnName == "CREATE")
        return "Add"
    else
        return "Delete"
};

Page.userName = function(userId) {
    var userName = "SYSTEM"; //default
    if (Page.Variables.mv_allUserData.dataSet != null && userId != null) {
        Page.Variables.mv_allUserData.dataSet.forEach((user) => {
            if (user.id == userId) {
                userName = user.firstName + " " + user.lastName;
            }
        });
    }
    return userName;
};

Page.displayPageInfo = function() {

    if (Page.Variables.totalRecordCount.getValue('dataValue') > 0) {
        let start = (Page.Variables.currentPage.getValue('dataValue') * Page.Variables.pageSize.getValue('dataValue')) + 1;
        let end = ((start + (Page.Variables.pageSize.getValue('dataValue') - 1)) > Page.Variables.totalRecordCount.getValue('dataValue')) ?
            Page.Variables.totalRecordCount.getValue('dataValue') : (start + (Page.Variables.pageSize.getValue('dataValue') - 1));

        return start + " to " + end + " from " + (Page.Variables.totalRecordCount.getValue('dataValue')) + " Activities";
    } else
        return "No Activities";

};

Page.getAllUseronSuccess = function(variable, data) {
    if (data != null) {
        Page.Variables.mv_allUserData.dataSet = data;
    }
};

Page.export_dataChangeHistory = function($event, widget) {
    Page.Variables.sv_getDataChaneHistoryLogExtract.setInput({
        "exportType": "Data Change History",
        "userLocale": "en",
        "applicationId": Page.pageParams.applicationId
    });
    Page.Variables.sv_getDataChaneHistoryLogExtract.invoke();
};

Page.sv_getDataChaneHistoryLogExtractonSuccess = function(variable, data) {
    let blob = base64toBlob(data.value, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    let url = window.URL || window.webkitURL;
    link = url.createObjectURL(blob);

    console.log(link);

    let a = document.createElement("a");
    a.setAttribute("download", "DataChangeHistory.xlsx");
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