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
const paginationEventName = "fi-paginationEvent";

Page.errorMsg = null;
Page.pageNumber = 0;
Page.pageSize = 20;
Page.totalRecords = 0;
Page.sortField = null;
Page.sortOrder = null;
Page.isPriorityQueue = false;
Page.noAppsInQueue = false;
Page.searchInputValue = null;

$('document').ready(() => {
    document.getElementsByTagName("html")[0].style.visibility = "hidden";
});

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
        if (App.permissionsLoaded && App.queueFiltersLoaded) {
            clearInterval(intervalId);
            console.log('Permissions & Queue filters loaded...');

            if (!App.IsUserHasAccess('Access_QueueMenu')) {
                window.location.href = '/ErrorLanding';
            } else {
                document.getElementsByTagName("html")[0].style.visibility = "visible";

                if (Page.pageParams.noAppsInQueue) {
                    Page.noAppsInQueue = true;
                } else {
                    let paginationContainer = document.querySelector("div[name='QPagination_container']");
                    paginationContainer.addEventListener(paginationEventName, (ev) => {
                        Page.pageNumber = ev.detail.pageNumber;
                        Page.Variables.sv_getQueueApplications.setInput({
                            "pageNumber": Page.pageNumber
                        });
                        Page.Variables.sv_getQueueApplications.invoke();
                    });

                    //listening to search input - ENTER key event
                    $(".searchInput").on("keyup", (ev) => {
                        //if 'ENTER' is keyed -> invoke search
                        if (ev.keyCode && ev.keyCode === 13)
                            Page.search_btnClick(ev, Page.Widgets.search_btn);
                    });

                    //call getQueue API
                    Page.Variables.getQueue.invoke();
                }
            }
        } else {
            //determining the time elapsed since App started in minutes
            const timeElapsedSinceAppStart = moment(new Date()).diff(moment(App.appStartTime), 'minutes');

            if (timeElapsedSinceAppStart < 1)
                console.log('Waiting to load permissions & Queue filters...');
            else {
                clearInterval(intervalId);

                //if the active page is not 'ErrorLanding'
                if (window.location.hash !== '/ErrorLanding')
                    window.location.href = '/ErrorLanding';
            }
        }
    }, 10);
}


Page.getQueueonError = function(variable, data) {
    Page.errorMsg = "Failed to fetch QUEUE information - Please try again later";
};

Page.getQueueonSuccess = function(variable, data) {
    let QResultPageDV = Page.Variables.dvs_QueueResultPage.dataSet.find((QSRPageDV) => {
        return QSRPageDV.id === Page.Variables.getQueue.dataSet[0].queueResultPage;
    });
    Page.Variables.getQueue.dataSet[0].QResultPageDVCode = (QResultPageDV ? QResultPageDV.code : "");
    //when the Queue is a priority queue
    debugger
    if (data[0].category == 1) {
        Page.isPriorityQueue = true;
        Page.Variables.sv_getQueueApplications.setInput("pageNumber", Page.pageNumber);
        Page.Variables.sv_getQueueApplications.setInput("pageSize", "1");
        Page.Variables.sv_getQueueApplications.invoke();
    } else {
        Page.Variables.sv_getQueueApplications.invoke();
    }
};

Page.sv_getQueueApplicationsonSuccess = function(variable, data) {
    if (Page.totalRecords !== data.totalRecords)
        Page.totalRecords = data.totalRecords;

    Page.noAppsInQueue = (data.totalRecords === 0);
    debugger
    //For priority queue
    if (Page.isPriorityQueue && !Page.noAppsInQueue) {
        if (data.queueData) {
            if (Page.pageSize !== data.pageSize)
                Page.pageSize = data.pageSize;
            if (Page.pageNumber !== data.pageNumber)
                Page.pageNumber = data.pageNumber;
            //Check if application is locked, call next unlocked application    
            if (data.queueData[0].applicationLockedBy === "") {
                Page.Actions.goToPage_ApplicationBroker.invoke({
                    "data": {
                        "applicationId": data.queueData[0].applicationId,
                        "applicationStatusId": data.queueData[0].applicationStatus,
                        "queueId": Page.pageParams.queueId
                    }
                });
            } else {
                Page.Variables.sv_getQueueApplications.setInput("pageNumber", Page.pageNumber + 1);
                Page.Variables.sv_getQueueApplications.setInput("pageSize", Page.pageSize);
                Page.Variables.sv_getQueueApplications.invoke();
            }
        } else {
            Page.noAppsInQueue = true;
        }
    }
};

Page.sv_getQueueApplicationsonError = function(variable, data) {
    Page.errorMsg = "Unable to fetch applications from QUEUE - Please try again later";
};

Page.search_btnClick = function($event, widget) {
    Page.errorMsg = null;
    Page.pageNumber = 0;
    Page.pageSize = 10;
    Page.totalRecords = 0;

    Page.Variables.sv_getQueueApplications.setInput({
        "searchFilter": Page.searchInputValue
    });
    Page.Variables.sv_getQueueApplications.invoke();
};

Page.clear_btnClick = function($event, widget) {
    Page.errorMsg = null;
    Page.searchInputValue = null;
};

Page.formatDateInterval = function(daysInterval) {
    if (daysInterval !== undefined && daysInterval !== null && daysInterval !== -1) {
        return daysInterval;
    } else
        return '';
};

Page.formatPhoneNumForNewline = function(pipeSeparatedValue) {
    if (pipeSeparatedValue) {
        let content = null;
        let values = pipeSeparatedValue.split("|");

        values.forEach((value) => {
            content = (content) ? (content + "\r\n" + value) : value;
        });

        return content;
    } else
        return '';
};

Page.displayDateFormat = function(dateValue) {
    if (dateValue) {
        /*const extractedDate = dateValue.substring(0, 23) + "Z";
        const formattedDate = moment(new Date(extractedDate)).format('YYYY-MM-DD hh:mm:ss A');
        return formattedDate;*/
        return App.localizeTimestampToAppTimezone(dateValue);
    } else
        return '';
};

Page.displayPageInfo = function() {
    if (Page.totalRecords > 0) {
        let start = (Page.pageNumber * Page.pageSize) + 1;
        let end = ((start + (Page.pageSize - 1)) > Page.totalRecords) ?
            Page.totalRecords : (start + (Page.pageSize - 1));

        return start + " to " + end + " from " + (Page.totalRecords) + " Applications";
    } else
        return "No Applications";
};

Page.navigateToAppBroker = function(applicationId) {
    prepareAppRedirectURL(applicationId);
};

async function prepareAppRedirectURL(applicationId) {
    if (applicationId) {
        Page.Variables.sv_getApplicationDetails.filterExpressions.rules[0].value = applicationId;
        await Page.Variables.sv_getApplicationDetails.invoke();

        //redirecting to ApplicationBroker
        Page.Actions.goToPage_ApplicationBroker.invoke({
            "data": {
                "applicationId": applicationId,
                "applicationStatusId": Page.Variables.sv_getApplicationDetails.dataSet[0].applicationStatus,
                "queueId": Page.pageParams.queueId
            }
        });

    }
}

Page.default_SearchTableHeaderclick = function($event, $data) {
    setTimeout(() => {
        let columnName = $($event.currentTarget).attr("data-col-field");
        let sortDirection = "ASC"; //-> default direction
        let sortClass = $("span[class~='sort-buttons-container'] > i", $event.currentTarget).attr("class");

        if (sortClass.toLowerCase().indexOf("desc") > -1)
            sortDirection = "DESC";
        else if (sortClass.toLowerCase().indexOf("asc") > -1)
            sortDirection = "ASC";
        else
            sortDirection = "ASC";

        //updating the sort criteria prior invoking Queue data
        Page.sortColumn = columnName;
        Page.sortOrder = sortDirection;

        Page.Variables.sv_getQueueApplications.setInput({
            "sortField": Page.sortColumn,
            "sortOrder": Page.sortOrder
        });
        Page.Variables.sv_getQueueApplications.invoke();

    }, 50);
};

Page.manager_SearchTableHeaderclick = function($event, $data) {
    setTimeout(() => {
        let columnName = $($event.currentTarget).attr("data-col-field");
        let sortDirection = "ASC"; //-> default direction
        let sortClass = $("span[class~='sort-buttons-container'] > i", $event.currentTarget).attr("class");

        if (sortClass.toLowerCase().indexOf("desc") > -1)
            sortDirection = "DESC";
        else if (sortClass.toLowerCase().indexOf("asc") > -1)
            sortDirection = "ASC";
        else
            sortDirection = "ASC";

        //updating the sort criteria prior invoking Queue data
        Page.sortColumn = columnName;
        Page.sortOrder = sortDirection;

        Page.Variables.sv_getQueueApplications.setInput({
            "sortField": Page.sortColumn,
            "sortOrder": Page.sortOrder
        });
        Page.Variables.sv_getQueueApplications.invoke();

    }, 50);
};

Page.fraud_SearchTableHeaderclick = function($event, $data) {
    setTimeout(() => {
        let columnName = $($event.currentTarget).attr("data-col-field");
        let sortDirection = "ASC"; //-> default direction
        let sortClass = $("span[class~='sort-buttons-container'] > i", $event.currentTarget).attr("class");

        if (sortClass.toLowerCase().indexOf("desc") > -1)
            sortDirection = "DESC";
        else if (sortClass.toLowerCase().indexOf("asc") > -1)
            sortDirection = "ASC";
        else
            sortDirection = "ASC";

        //updating the sort criteria prior invoking Queue data
        Page.sortColumn = columnName;
        Page.sortOrder = sortDirection;

        Page.Variables.sv_getQueueApplications.setInput({
            "sortField": Page.sortColumn,
            "sortOrder": Page.sortOrder
        });
        Page.Variables.sv_getQueueApplications.invoke();

    }, 50);
};

Page.default_SearchTableDatarender = function(widget, $data) {
    adjustTableBorders(widget.$element);
};

Page.manager_SearchTableDatarender = function(widget, $data) {
    adjustTableBorders(widget.$element);
};

Page.fraud_SearchTableDatarender = function(widget, $data) {
    adjustTableBorders(widget.$element);
};

function adjustTableBorders(dataTableElement) {
    let isSameAsPrevious = false;
    let previousData = null,
        previousTD = null;
    let matchCount = 1;

    $(".app-grid-content tr", dataTableElement).each((index, tableRow) => {
        let firstCol = $(tableRow).children("td")[0];

        if (index === 0) {
            previousTD = firstCol;
            previousData = Page.Variables.sv_getQueueApplications.dataSet.queueData[index].applicationId;
            $(firstCol).addClass('column-right-border');
        } else {
            isSameAsPrevious = (previousData === Page.Variables.sv_getQueueApplications.dataSet.queueData[index].applicationId);
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
            previousData = Page.Variables.sv_getQueueApplications.dataSet.queueData[index].applicationId;
        }

    });
};
Page.validateMobilePhone = function(data) {
    debugger;
    var ans = "";
    var a = data.split("|");
    console.log(a);
    for (let i = 0; i < a.length; i++) {
        var str = a[i].substring(11);
        console.log(str.length);
        console.log(str);
        if (str.length >= 7) {
            str = str.substring(4, str.length);
        }
        console.log("after " + str);
        var cleaned = ('' + a[i]).replace(/\D/g, '');
        var match = cleaned.match(/^(\d{3})(\d{3})(\d{4})$/);
        if (match) {
            a[i] = '(' + match[1] + ') ' + match[2] + '-' + match[3];



        } else {
            data = ('' + a[i]).replace(/\D/g, '');
        }

        ans = ans + a[i] + str + " | ";
        console.log(ans);



    }
    return ans;

};