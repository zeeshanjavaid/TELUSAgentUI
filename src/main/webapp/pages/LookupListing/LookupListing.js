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

$('document').ready(() => {
    document.getElementsByTagName("html")[0].style.visibility = "hidden";
});

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
        if (true) {
            clearInterval(intervalId);
            console.log('Permissions loaded...');


            document.getElementsByTagName("html")[0].style.visibility = "visible";

            //listening to search inputs key-up events

            // //debugger;
            $(".searchInput").on("keyup", (ev) => {
                //if 'ENTER' is keyed -> invoke search
                if (ev.keyCode && ev.keyCode === 13)
                    Page.searchDV_ButtonClick(ev, Page.Widgets.searchDV_Button);
            });

            // listening to pagination events from partial
            let paginationContainer = document.querySelector("div[name='dv_PaginationContainer']");
            paginationContainer.addEventListener(paginationEventName, (ev) => {
                // console.log(ev.detail.pageNumber);

                //search/fetch service invocation below
                let lookupFetchSV = Page.Variables.svc_getLookupFileListByFilters;
                Page.Variables.lookupSearchCriteria.dataSet.pageSize = Page.Variables.pageSize.getValue('dataValue').toString();
                Page.Variables.lookupSearchCriteria.dataSet.pageNo = ev.detail.pageNumber.toString();
                lookupFetchSV.setInput("Map", Page.Variables.lookupSearchCriteria.dataSet);
                console.log("Invoking lookupFetchSV on pagination event");
                lookupFetchSV.invoke();


            });


            Page.Variables.showActiveDVs.setValue('dataValue', false);
            Page.Variables.showAllDVs.setValue('dataValue', true);
            Page.Variables.showInactiveDVs.setValue('dataValue', true);


            //search/fetch service invocation below
            let lookupFetchSV = Page.Variables.svc_getLookupFileListByFilters;
            Page.initializeSearchCriteria();
            debugger;
            lookupFetchSV.setInput("Map", Page.Variables.lookupSearchCriteria.dataSet);
            console.log("Invoking lookupFetchSV");
            lookupFetchSV.invoke();

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


Page.initializeSearchCriteria = function() {

    debugger;
    Page.Variables.totalRecordCount.setValue('dataValue', 0);
    Page.Variables.currentPage.setValue('dataValue', 0);
    Page.Variables.pageSize.setValue('dataValue', fixedPageSize);
    Page.Variables.lookupSearchCriteria.dataSet.status = "Active";
    Page.Variables.lookupSearchCriteria.dataSet.pageSize = Page.Variables.pageSize.getValue('dataValue').toString();

};

/* Utility functions */
Page.displayCustomUserAndDateValues = function(timestamp, username) {
    //let jsTimeStamp = new Date(timestamp);
    let ts_toShow = App.localizeTimestampToAppTimezone(timestamp);

    return ts_toShow + " by " + (!username ? "" : username);
};

Page.displayPageInfo = function() {
    if (Page.Variables.totalRecordCount.getValue('dataValue') > 0) {
        let start = (Page.Variables.currentPage.getValue('dataValue') * fixedPageSize) + 1;
        let end = ((start + (fixedPageSize - 1)) > Page.Variables.totalRecordCount.getValue('dataValue')) ?
            Page.Variables.totalRecordCount.getValue('dataValue') : (start + (fixedPageSize - 1));

        return start + " to " + end + " from " + (Page.Variables.totalRecordCount.getValue('dataValue')) + " lookup files";
    } else
        return "No lookup files";
};

Page.navigateURLBuilder = function(lookupFileIdentifier, lookupFileName, lookupFileDesc) {
    let URI = "#/LookupFileCreateEdit?lookupFileIdentifier=" + lookupFileIdentifier + "&lookupFileName=" + lookupFileName + "&lookupFileDesc=" + lookupFileDesc;
    console.log("navigate URL:" + URI);
    return URI;
};

/* UI level functions */
Page.sv_getDVListPaginatedonSuccess = function(variable, data) {
    //de-select all DV table rows and redraw table
    Page.Widgets.DomainValueList_Table.selecteditem = [];

    //set pagination values
    Page.Variables.currentPage.setValue('dataValue', data.pageNumber);

    if (Page.Variables.totalRecordCount.getValue('dataValue') !== data.totalRecords) {
        Page.Variables.totalRecordCount.setValue('dataValue', data.totalRecords);
    }
};

Page.sv_getDVListPaginatedonError = function(variable, data) {
    console.log(data);

    //de-select all DV table rows and redraw table
    Page.Widgets.DomainValueList_Table.selecteditem = [];
};

Page.sv_getDVTypeByIdonError = function(variable, data) {
    console.log(data);
};



Page.DomainValueList_TableSelect = function($event, widget, row) {
    Page.Widgets.activate_Button.disabled = false;
    Page.Widgets.deactivate_Button.disabled = false;
};

Page.DomainValueList_TableDeselect = function($event, widget, row) {
    if (widget.selecteditem.length === 0) {
        Page.Widgets.activate_Button.disabled = true;
        Page.Widgets.deactivate_Button.disabled = true;
    }
};

Page.sv_getDVTypeByIdonSuccess = function(variable, data) {
    console.log(data);
};

Page.showInactive_LinkClick = function($event, widget) {
    Page.Variables.totalRecordCount.setValue('dataValue', 0);
    Page.Variables.currentPage.setValue('dataValue', 0);
    Page.Variables.pageSize.setValue('dataValue', fixedPageSize);
    // Page.Variables.sortProperties.setValue('dataValue',
    //     (Page.Variables.sortProperties.getValue('dataValue') ?
    //         Page.Variables.sortProperties.getValue('dataValue') : 'code ASC'));


    Page.Variables.showAllDVs.setValue('dataValue', true);
    Page.Variables.showActiveDVs.setValue('dataValue', true);
    Page.Variables.showInactiveDVs.setValue('dataValue', false);

    // Page.Widgets.activate_Button.disabled = true;
    // Page.Widgets.deactivate_Button.disabled = true;

    //search/fetch service invocation below
    let lookupFetchSV = Page.Variables.svc_getLookupFileListByFilters;
    Page.Variables.lookupSearchCriteria.dataSet.pageSize = Page.Variables.pageSize.getValue('dataValue').toString();
    Page.Variables.lookupSearchCriteria.dataSet.status = "Inactive";
    // Page.Variables.lookupSearchCriteria.dataSet.pageNo = ev.detail.pageNumber.toString();
    lookupFetchSV.setInput("Map", Page.Variables.lookupSearchCriteria.dataSet);
    console.log("Invoking lookupFetchSV- inactive- on pagination event");
    lookupFetchSV.invoke();
};

Page.showActive_LinkClick = function($event, widget) {
    Page.Variables.totalRecordCount.setValue('dataValue', 0);
    Page.Variables.currentPage.setValue('dataValue', 0);
    Page.Variables.pageSize.setValue('dataValue', fixedPageSize);
    // Page.Variables.sortProperties.setValue('dataValue',
    //     (Page.Variables.sortProperties.getValue('dataValue') ?
    //         Page.Variables.sortProperties.getValue('dataValue') : 'code ASC'));

    Page.Variables.showAllDVs.setValue('dataValue', true);
    Page.Variables.showActiveDVs.setValue('dataValue', false);
    Page.Variables.showInactiveDVs.setValue('dataValue', true);


    //search/fetch service invocation below
    let lookupFetchSV = Page.Variables.svc_getLookupFileListByFilters;
    Page.Variables.lookupSearchCriteria.dataSet.pageSize = Page.Variables.pageSize.getValue('dataValue').toString();
    Page.Variables.lookupSearchCriteria.dataSet.status = "Active";
    // Page.Variables.lookupSearchCriteria.dataSet.pageNo = ev.detail.pageNumber.toString();
    lookupFetchSV.setInput("Map", Page.Variables.lookupSearchCriteria.dataSet);
    console.log("Invoking lookupFetchSV- active -  on pagination event");
    lookupFetchSV.invoke();


};

Page.showAll_LinkClick = function($event, widget) {
    Page.Variables.totalRecordCount.setValue('dataValue', 0);
    Page.Variables.currentPage.setValue('dataValue', 0);
    Page.Variables.pageSize.setValue('dataValue', fixedPageSize);
    // Page.Variables.sortProperties.setValue('dataValue',
    //     (Page.Variables.sortProperties.getValue('dataValue') ?
    //         Page.Variables.sortProperties.getValue('dataValue') : 'code ASC'));


    Page.Variables.showAllDVs.setValue('dataValue', false);
    Page.Variables.showActiveDVs.setValue('dataValue', true);
    Page.Variables.showInactiveDVs.setValue('dataValue', true);

    // Page.Widgets.activate_Button.disabled = true;
    // Page.Widgets.deactivate_Button.disabled = true;

    //search/fetch service invocation below
    let lookupFetchSV = Page.Variables.svc_getLookupFileListByFilters;
    Page.Variables.lookupSearchCriteria.dataSet.pageSize = Page.Variables.pageSize.getValue('dataValue').toString();
    // Page.Variables.lookupSearchCriteria.dataSet.pageNo = ev.detail.pageNumber.toString();
    Page.Variables.lookupSearchCriteria.dataSet.status = "";
    lookupFetchSV.setInput("Map", Page.Variables.lookupSearchCriteria.dataSet);
    console.log("Invoking lookupFetchSV on pagination event");
    lookupFetchSV.invoke();


};

//updates the lookupSearchCriteria sort criteria 
Page.updatelookupSearchSortCriteria = function(sortColumn) {
    let columnName = Page.Variables.lookupSearchCriteria.dataSet.sortBy;


    //Identifies the column selected on UI and maps it to underlying db column name
    switch (sortColumn) {

        case "Created":
            columnName = "createdBy";
            break;

        case "Name":
            columnName = "name";
            break;

        case "Description":
            columnName = "description";
            break;

        case "Updated":
            columnName = "updatedBy";
            break;

    }

    console.log("Sort column : " + sortColumn + " column name: " + columnName);
    if (Page.Variables.lookupSearchCriteria.dataSet.sortBy == columnName) {
        if (Page.Variables.lookupSearchCriteria.dataSet.orderBy == 'asc') {
            Page.Variables.lookupSearchCriteria.dataSet.orderBy = 'desc';
        } else {
            Page.Variables.lookupSearchCriteria.dataSet.orderBy = 'asc';
        }

    } else {
        Page.Variables.lookupSearchCriteria.dataSet.orderBy = 'asc';
    }

    Page.Variables.lookupSearchCriteria.dataSet.sortBy = columnName;
    console.log("Updated search criteria :" + JSON.stringify(Page.Variables.lookupSearchCriteria.dataSet));

}


Page.DomainValueList_TableSort = function($event, $data) {

    debugger;
    let selectedSortColumn = $event.currentTarget.firstChild.innerHTML;
    Page.updatelookupSearchSortCriteria(selectedSortColumn);
    let lookupFetchSV = Page.Variables.svc_getLookupFileListByFilters;
    lookupFetchSV.setInput("Map", Page.Variables.lookupSearchCriteria.dataSet);
    console.log("Invoking lookupFetchSV on sort event- criteria :" + JSON.stringify(Page.Variables.lookupSearchCriteria.dataSet));
    lookupFetchSV.invoke();

};

Page.searchDV_ButtonClick = function($event, widget) {
    Page.Variables.totalRecordCount.setValue('dataValue', 0);
    Page.Variables.currentPage.setValue('dataValue', 0);
    Page.Variables.pageSize.setValue('dataValue', fixedPageSize);
    // Page.Variables.sortProperties.setValue('dataValue',
    //     (Page.Variables.sortProperties.getValue('dataValue') ?
    //         Page.Variables.sortProperties.getValue('dataValue') : 'code ASC'));

    //search/fetch service invocation below
    let lookupFetchSV = Page.Variables.svc_getLookupFileListByFilters;
    Page.Variables.lookupSearchCriteria.dataSet.pageSize = Page.Variables.pageSize.getValue('dataValue').toString();
    Page.Variables.lookupSearchCriteria.dataSet.pageNo = "0";
    Page.Variables.lookupSearchCriteria.dataSet.searchText = Page.Variables.searchValue.getValue('dataValue');
    lookupFetchSV.setInput("Map", Page.Variables.lookupSearchCriteria.dataSet);
    console.log("Invoking lookupFetchSV on search event");
    lookupFetchSV.invoke();

};

Page.createDV_ButtonClick = function($event, widget) {
    Page.Actions.goToPage_LookupFileCreateEdit.invoke();
};
Page.DomainValueList_TableHeaderclick = function($event, $data) {

};

Page.svc_getLookupFileListByFiltersonError = function(variable, data) {

    console.log("Lookup file list failure :" + JSON.stringify(data));

};
Page.svc_getLookupFileListByFiltersonSuccess = function(variable, data) {
    debugger;
    console.log("Lookup file list success :" + JSON.stringify(data));
    //set defaults for DV fetch service
    Page.Variables.totalRecordCount.setValue('dataValue', data.totalRecords);
    Page.Variables.currentPage.setValue('dataValue', data.pageNumber);


};