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

const fixedPageSize = 10;
const paginationEventName = "fi-paginationEvent";
Page.title = "";

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
        if (App.permissionsLoaded) {
            clearInterval(intervalId);
            console.log('Permissions loaded...');

            //Removed Permission for Domain Value aka Work Category

            // if (!App.IsUserHasAccess('System Administration')) {
            //     window.location.href = '#/ErrorLanding';
            // } else {
            document.getElementsByTagName("html")[0].style.visibility = "visible";

            //listening to search inputs key-up events

            //debugger;
            $(".searchInput").on("keyup", (ev) => {
                //if 'ENTER' is keyed -> invoke search
                if (ev.keyCode && ev.keyCode === 13)
                    Page.searchDV_ButtonClick(ev, Page.Widgets.searchDV_Button);
            });

            //listening to pagination events from partial
            let paginationContainer = document.querySelector("div[name='dv_PaginationContainer']");
            paginationContainer.addEventListener(paginationEventName, (ev) => {
                // console.log(ev.detail.pageNumber);

                //search/fetch service invocation below
                let dvFetchSV = Page.Variables.sv_getDVListPaginated;
                dvFetchSV.setInput({
                    "domainValueTypeId": (!Page.pageParams.domainValueTypeId) ? 0 : Page.pageParams.domainValueTypeId,
                    "showAll": Page.Variables.showAllDVs.getValue('dataValue'),
                    "isActiveFlag": Page.Variables.showActiveDVs.getValue('dataValue'),
                    "searchValue": "%" + Page.Variables.searchValue.getValue('dataValue') + "%",
                    "defaultLocale": Page.Variables.defaultLocale.getValue('dataValue'),
                    "currentPage": ev.detail.pageNumber,
                    "pageSize": Page.Variables.pageSize.getValue('dataValue'),
                    "sortOrders": Page.Variables.sortProperties.getValue('dataValue')
                });

                dvFetchSV.invoke();
            });

            //DV fetch service level params
            Page.Variables.defaultLocale.setValue('dataValue', (App.getCurrentLocale() ? App.getCurrentLocale() : 'en'));
            Page.Variables.totalRecordCount.setValue('dataValue', 0);
            Page.Variables.currentPage.setValue('dataValue', 0);
            Page.Variables.pageSize.setValue('dataValue', fixedPageSize);
            Page.Variables.sortProperties.setValue('dataValue',
                (Page.Variables.sortProperties.getValue('dataValue') ?
                    Page.Variables.sortProperties.getValue('dataValue') : 'code ASC')); //-> default sort criteria


            Page.Variables.showAllDVs.setValue('dataValue', false);
            Page.Variables.showActiveDVs.setValue('dataValue', true);
            Page.Variables.showInactiveDVs.setValue('dataValue', false);

            Page.Widgets.activate_Button.disabled = true;
            Page.Widgets.deactivate_Button.disabled = true;
            // Page.Widgets.showActive_Link.display = false;
            // Page.Widgets.showInactive_Link.display = true;
            // Page.Widgets.showAll_Link.display = true;

            //search/fetch service invocation below
            let dvFetchSV = Page.Variables.sv_getDVListPaginated;
            dvFetchSV.setInput({
                "domainValueTypeId": (!Page.pageParams.domainValueTypeId) ? 0 : Page.pageParams.domainValueTypeId,
                "showAll": Page.Variables.showAllDVs.getValue('dataValue'),
                "isActiveFlag": Page.Variables.showActiveDVs.getValue('dataValue'),
                "searchValue": "%" + Page.Variables.searchValue.getValue('dataValue') + "%",
                "defaultLocale": Page.Variables.defaultLocale.getValue('dataValue'),
                "currentPage": Page.Variables.currentPage.getValue('dataValue'),
                "pageSize": Page.Variables.pageSize.getValue('dataValue'),
                "sortOrders": Page.Variables.sortProperties.getValue('dataValue')
            });

            dvFetchSV.invoke();
            //   }
        } else {
            //determining the time elapsed since App started in minutes
            const timeElapsedSinceAppStart = moment(new Date()).diff(moment(App.appStartTime), 'minutes');

            // if (timeElapsedSinceAppStart < 1)
            //     console.log('Waiting to load permissions...');
            // else {
            //     clearInterval(intervalId);

            //     //if the active page is not 'ErrorLanding'
            //     if (window.location.hash !== '#/ErrorLanding')
            //         window.location.href = '#/ErrorLanding';
            // }
        }
    }, 10);
}

/* Utility functions */
Page.displayCustomUserAndDateValues = function(timestamp, username) {
    //let jsTimeStamp = new Date(timestamp);
    let ts_toShow = App.localizeTimestampToAppTimezone(timestamp)

    return ts_toShow + " by " + (!username ? "" : username);
};

Page.displayPageInfo = function() {
    if (Page.Variables.totalRecordCount.getValue('dataValue') > 0) {
        let start = (Page.Variables.currentPage.getValue('dataValue') * fixedPageSize) + 1;
        let end = ((start + (fixedPageSize - 1)) > Page.Variables.totalRecordCount.getValue('dataValue')) ?
            Page.Variables.totalRecordCount.getValue('dataValue') : (start + (fixedPageSize - 1));

        return start + " to " + end + " from " + (Page.Variables.totalRecordCount.getValue('dataValue')) + " Domain values";
    } else
        return "No domain values";
};

Page.navigateURLBuilder = function(domainValueId) {
    let URI = "#/DomainValueCreateEdit?domainValueTypeId=" + Page.pageParams.domainValueTypeId + "&domainValueId=" + domainValueId;
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

Page.sv_activateDeactivateDVByIdsonSuccess = function(variable, data) {
    //set defaults for DV fetch service
    Page.Variables.totalRecordCount.setValue('dataValue', 0);
    Page.Variables.currentPage.setValue('dataValue', 0);
    Page.Variables.pageSize.setValue('dataValue', fixedPageSize);
    Page.Variables.sortProperties.setValue('dataValue',
        (Page.Variables.sortProperties.getValue('dataValue') ?
            Page.Variables.sortProperties.getValue('dataValue') : 'code ASC'));

    //search/fetch service invocation below
    let dvFetchSV = Page.Variables.sv_getDVListPaginated;
    dvFetchSV.setInput({
        "domainValueTypeId": (!Page.pageParams.domainValueTypeId) ? 0 : Page.pageParams.domainValueTypeId,
        "showAll": Page.Variables.showAllDVs.getValue('dataValue'),
        "isActiveFlag": Page.Variables.showActiveDVs.getValue('dataValue'),
        "searchValue": "%" + Page.Variables.searchValue.getValue('dataValue') + "%",
        "defaultLocale": Page.Variables.defaultLocale.getValue('dataValue'),
        "currentPage": Page.Variables.currentPage.getValue('dataValue'),
        "pageSize": Page.Variables.pageSize.getValue('dataValue'),
        "sortOrders": Page.Variables.sortProperties.getValue('dataValue')
    });

    dvFetchSV.invoke();
};

Page.sv_activateDeactivateDVByIdsonError = function(variable, data) {
    console.log(data);

    //set defaults for DV fetch service
    Page.Variables.totalRecordCount.setValue('dataValue', 0);
    Page.Variables.currentPage.setValue('dataValue', 0);
    Page.Variables.pageSize.setValue('dataValue', fixedPageSize);
    Page.Variables.sortProperties.setValue('dataValue',
        (Page.Variables.sortProperties.getValue('dataValue') ?
            Page.Variables.sortProperties.getValue('dataValue') : 'code ASC'));

    //search/fetch service invocation below
    let dvFetchSV = Page.Variables.sv_getDVListPaginated;
    dvFetchSV.setInput({
        "domainValueTypeId": (!Page.pageParams.domainValueTypeId) ? 0 : Page.pageParams.domainValueTypeId,
        "showAll": Page.Variables.showAllDVs.getValue('dataValue'),
        "isActiveFlag": Page.Variables.showActiveDVs.getValue('dataValue'),
        "searchValue": "%" + Page.Variables.searchValue.getValue('dataValue') + "%",
        "defaultLocale": Page.Variables.defaultLocale.getValue('dataValue'),
        "currentPage": Page.Variables.currentPage.getValue('dataValue'),
        "pageSize": Page.Variables.pageSize.getValue('dataValue'),
        "sortOrders": Page.Variables.sortProperties.getValue('dataValue')
    });

    dvFetchSV.invoke();
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
    //console.log(data);
    const regex = /(^|\b(?!(and?|at?|the|for|to|but|by)\b))\w+/g;
    Page.title = data.description.toLowerCase().replace(regex, s => s[0].toUpperCase() + s.slice(1));
}

Page.showInactive_LinkClick = function($event, widget) {
    Page.Variables.totalRecordCount.setValue('dataValue', 0);
    Page.Variables.currentPage.setValue('dataValue', 0);
    Page.Variables.pageSize.setValue('dataValue', fixedPageSize);
    Page.Variables.sortProperties.setValue('dataValue',
        (Page.Variables.sortProperties.getValue('dataValue') ?
            Page.Variables.sortProperties.getValue('dataValue') : 'code ASC'));


    Page.Variables.showAllDVs.setValue('dataValue', false);
    Page.Variables.showActiveDVs.setValue('dataValue', false);
    Page.Variables.showInactiveDVs.setValue('dataValue', true);

    Page.Widgets.activate_Button.disabled = true;
    Page.Widgets.deactivate_Button.disabled = true;

    //search/fetch service invocation below
    let dvFetchSV = Page.Variables.sv_getDVListPaginated;
    dvFetchSV.setInput({
        "domainValueTypeId": (!Page.pageParams.domainValueTypeId) ? 0 : Page.pageParams.domainValueTypeId,
        "showAll": Page.Variables.showAllDVs.getValue('dataValue'),
        "isActiveFlag": Page.Variables.showActiveDVs.getValue('dataValue'),
        "searchValue": "%" + Page.Variables.searchValue.getValue('dataValue') + "%",
        "defaultLocale": Page.Variables.defaultLocale.getValue('dataValue'),
        "currentPage": Page.Variables.currentPage.getValue('dataValue'),
        "pageSize": Page.Variables.pageSize.getValue('dataValue'),
        "sortOrders": Page.Variables.sortProperties.getValue('dataValue')
    });

    dvFetchSV.invoke();
};

Page.showActive_LinkClick = function($event, widget) {
    Page.Variables.totalRecordCount.setValue('dataValue', 0);
    Page.Variables.currentPage.setValue('dataValue', 0);
    Page.Variables.pageSize.setValue('dataValue', fixedPageSize);
    Page.Variables.sortProperties.setValue('dataValue',
        (Page.Variables.sortProperties.getValue('dataValue') ?
            Page.Variables.sortProperties.getValue('dataValue') : 'code ASC'));


    Page.Variables.showAllDVs.setValue('dataValue', false);
    Page.Variables.showActiveDVs.setValue('dataValue', true);
    Page.Variables.showInactiveDVs.setValue('dataValue', false);

    Page.Widgets.activate_Button.disabled = true;
    Page.Widgets.deactivate_Button.disabled = true;

    //search/fetch service invocation below
    let dvFetchSV = Page.Variables.sv_getDVListPaginated;
    dvFetchSV.setInput({
        "domainValueTypeId": (!Page.pageParams.domainValueTypeId) ? 0 : Page.pageParams.domainValueTypeId,
        "showAll": Page.Variables.showAllDVs.getValue('dataValue'),
        "isActiveFlag": Page.Variables.showActiveDVs.getValue('dataValue'),
        "searchValue": "%" + Page.Variables.searchValue.getValue('dataValue') + "%",
        "defaultLocale": Page.Variables.defaultLocale.getValue('dataValue'),
        "currentPage": Page.Variables.currentPage.getValue('dataValue'),
        "pageSize": Page.Variables.pageSize.getValue('dataValue'),
        "sortOrders": Page.Variables.sortProperties.getValue('dataValue')
    });

    dvFetchSV.invoke();
};

Page.showAll_LinkClick = function($event, widget) {
    Page.Variables.totalRecordCount.setValue('dataValue', 0);
    Page.Variables.currentPage.setValue('dataValue', 0);
    Page.Variables.pageSize.setValue('dataValue', fixedPageSize);
    Page.Variables.sortProperties.setValue('dataValue',
        (Page.Variables.sortProperties.getValue('dataValue') ?
            Page.Variables.sortProperties.getValue('dataValue') : 'code ASC'));


    Page.Variables.showAllDVs.setValue('dataValue', true);
    Page.Variables.showActiveDVs.setValue('dataValue', false);
    Page.Variables.showInactiveDVs.setValue('dataValue', false);

    Page.Widgets.activate_Button.disabled = true;
    Page.Widgets.deactivate_Button.disabled = true;

    //search/fetch service invocation below
    let dvFetchSV = Page.Variables.sv_getDVListPaginated;
    dvFetchSV.setInput({
        "domainValueTypeId": (!Page.pageParams.domainValueTypeId) ? 0 : Page.pageParams.domainValueTypeId,
        "showAll": Page.Variables.showAllDVs.getValue('dataValue'),
        "isActiveFlag": Page.Variables.showActiveDVs.getValue('dataValue'),
        "searchValue": "%" + Page.Variables.searchValue.getValue('dataValue') + "%",
        "defaultLocale": Page.Variables.defaultLocale.getValue('dataValue'),
        "currentPage": Page.Variables.currentPage.getValue('dataValue'),
        "pageSize": Page.Variables.pageSize.getValue('dataValue'),
        "sortOrders": Page.Variables.sortProperties.getValue('dataValue')
    });

    dvFetchSV.invoke();
};

Page.activate_ButtonClick = function($event, widget) {
    Page.Widgets.activate_Button.disabled = true;
    Page.Widgets.deactivate_Button.disabled = true;

    //iterate over selected records, prepare DV ID list and activate them
    let DVIds = [];
    Page.Widgets.DomainValueList_Table.selecteditem.forEach((domainValueItem) => {
        DVIds.push(domainValueItem.id);
    });

    //activate/deactivate service invocation below
    let dvActivateSV = Page.Variables.sv_activateDeactivateDVByIds;
    dvActivateSV.setInput({
        "isToActivate": true,
        "ids": DVIds
    });

    dvActivateSV.invoke();
};

Page.deactivate_ButtonClick = function($event, widget) {
    Page.Widgets.activate_Button.disabled = true;
    Page.Widgets.deactivate_Button.disabled = true;

    //iterate over selected records, prepare DV ID list and activate them
    let DVIds = [];
    Page.Widgets.DomainValueList_Table.selecteditem.forEach((domainValueItem) => {
        DVIds.push(domainValueItem.id);
    });

    //activate/deactivate service invocation below
    let dvDeActivateSV = Page.Variables.sv_activateDeactivateDVByIds;
    dvDeActivateSV.setInput({
        "isToActivate": false,
        "ids": DVIds
    });

    dvDeActivateSV.invoke();
};

Page.DomainValueList_TableSort = function($event, $data) {
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
        let dvFetchSV = Page.Variables.sv_getDVListPaginated;
        dvFetchSV.setInput({
            "domainValueTypeId": (!Page.pageParams.domainValueTypeId) ? 0 : Page.pageParams.domainValueTypeId,
            "showAll": Page.Variables.showAllDVs.getValue('dataValue'),
            "isActiveFlag": Page.Variables.showActiveDVs.getValue('dataValue'),
            "searchValue": "%" + Page.Variables.searchValue.getValue('dataValue') + "%",
            "defaultLocale": Page.Variables.defaultLocale.getValue('dataValue'),
            "currentPage": Page.Variables.currentPage.getValue('dataValue'),
            "pageSize": Page.Variables.pageSize.getValue('dataValue'),
            "sortOrders": Page.Variables.sortProperties.getValue('dataValue')
        });

        dvFetchSV.invoke();
    }, 50);

};

Page.searchDV_ButtonClick = function($event, widget) {
    Page.Variables.totalRecordCount.setValue('dataValue', 0);
    Page.Variables.currentPage.setValue('dataValue', 0);
    Page.Variables.pageSize.setValue('dataValue', fixedPageSize);
    Page.Variables.sortProperties.setValue('dataValue',
        (Page.Variables.sortProperties.getValue('dataValue') ?
            Page.Variables.sortProperties.getValue('dataValue') : 'code ASC'));

    //search/fetch service invocation below
    let dvFetchSV = Page.Variables.sv_getDVListPaginated;
    dvFetchSV.setInput({
        "domainValueTypeId": (!Page.pageParams.domainValueTypeId) ? 0 : Page.pageParams.domainValueTypeId,
        "showAll": Page.Variables.showAllDVs.getValue('dataValue'),
        "isActiveFlag": Page.Variables.showActiveDVs.getValue('dataValue'),
        "searchValue": "%" + Page.Variables.searchValue.getValue('dataValue') + "%",
        "defaultLocale": Page.Variables.defaultLocale.getValue('dataValue'),
        "currentPage": Page.Variables.currentPage.getValue('dataValue'),
        "pageSize": Page.Variables.pageSize.getValue('dataValue'),
        "sortOrders": Page.Variables.sortProperties.getValue('dataValue')
    });

    dvFetchSV.invoke();
};

Page.createDV_ButtonClick = function($event, widget) {
    Page.Actions.goToPage_DomainValueCreateEdit.invoke({
        data: {
            'domainValueTypeId': Page.pageParams.domainValueTypeId
        }
    });
};
Page.DomainValueList_TableHeaderclick = function($event, $data) {

};