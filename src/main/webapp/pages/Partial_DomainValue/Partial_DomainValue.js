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
const fixedPageSize = 10;
const paginationEventName = "fi-paginationEvent";
Partial.title = "";

// $('document').ready(() => {
//     document.getElementsByTagName("html")[0].style.visibility = "hidden";
// });

/* perform any action on widgets/variables within this block */
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
    //  Partial.pageParams.domainValueTypeId = 4;
    initPage();
};

function initPage() {
    //  const intervalId = setInterval(function() {
    //if (App.permissionsLoaded) {
    // clearInterval(intervalId);
    // console.log('Permissions loaded...');

    //Removed Permission for Domain Value aka Work Category

    // if (!App.IsUserHasAccess('System Administration')) {
    //     window.location.href = '#/ErrorLanding';
    // } else {
    document.getElementsByTagName("html")[0].style.visibility = "visible";

    debugger;

    //listening to search inputs key-up events

    debugger;
    $(".searchInput").on("keyup", (ev) => {
        //if 'ENTER' is keyed -> invoke search
        if (ev.keyCode && ev.keyCode === 13)
            Partial.searchDV_ButtonClick(ev, Partial.Widgets.searchDV_Button);
    });

    //listening to pagination events from partial
    let paginationContainer = document.querySelector("div[name='dv_PaginationContainer']");
    paginationContainer.addEventListener(paginationEventName, (ev) => {
        // console.log(ev.detail.pageNumber);

        //search/fetch service invocation below
        let dvFetchSV = Partial.Variables.sv_getDVListPaginated;
        dvFetchSV.setInput({
            "domainValueTypeId": (!Partial.pageParams.domainValueTypeId) ? 0 : Partial.pageParams.domainValueTypeId,
            "showAll": Partial.Variables.showAllDVs.getValue('dataValue'),
            "isActiveFlag": Partial.Variables.showActiveDVs.getValue('dataValue'),
            "searchValue": "%" + Partial.Variables.searchValue.getValue('dataValue') + "%",
            "defaultLocale": Partial.Variables.defaultLocale.getValue('dataValue'),
            "currentPage": ev.detail.pageNumber,
            "pageSize": Partial.Variables.pageSize.getValue('dataValue'),
            "sortOrders": Partial.Variables.sortProperties.getValue('dataValue')
        });

        dvFetchSV.invoke();
    });

    //DV fetch service level params
    Partial.Variables.defaultLocale.setValue('dataValue', (App.getCurrentLocale() ? App.getCurrentLocale() : 'en'));
    Partial.Variables.totalRecordCount.setValue('dataValue', 0);
    Partial.Variables.currentPage.setValue('dataValue', 0);
    Partial.Variables.pageSize.setValue('dataValue', fixedPageSize);
    Partial.Variables.sortProperties.setValue('dataValue',
        (Partial.Variables.sortProperties.getValue('dataValue') ?
            Partial.Variables.sortProperties.getValue('dataValue') : 'code ASC')); //-> default sort criteria


    Partial.Variables.showAllDVs.setValue('dataValue', false);
    Partial.Variables.showActiveDVs.setValue('dataValue', true);
    Partial.Variables.showInactiveDVs.setValue('dataValue', false);

    Partial.Widgets.activate_Button.disabled = true;
    Partial.Widgets.deactivate_Button.disabled = true;
    // Page.Widgets.showActive_Link.display = false;
    // Page.Widgets.showInactive_Link.display = true;
    // Page.Widgets.showAll_Link.display = true;

    //search/fetch service invocation below
    let dvFetchSV = Partial.Variables.sv_getDVListPaginated;
    dvFetchSV.setInput({
        "domainValueTypeId": (!Partial.pageParams.domainValueTypeId) ? 0 : Partial.pageParams.domainValueTypeId,
        "showAll": Partial.Variables.showAllDVs.getValue('dataValue'),
        "isActiveFlag": Partial.Variables.showActiveDVs.getValue('dataValue'),
        "searchValue": "%" + Partial.Variables.searchValue.getValue('dataValue') + "%",
        "defaultLocale": Partial.Variables.defaultLocale.getValue('dataValue'),
        "currentPage": Partial.Variables.currentPage.getValue('dataValue'),
        "pageSize": Partial.Variables.pageSize.getValue('dataValue'),
        "sortOrders": Partial.Variables.sortProperties.getValue('dataValue')
    });

    dvFetchSV.invoke();
    //   }
    //  } else {
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
    //   }
    //  }, 10);
}

/* Utility functions */
Partial.displayCustomUserAndDateValues = function(timestamp, username) {
    //let jsTimeStamp = new Date(timestamp);
    let ts_toShow = App.localizeTimestampToAppTimezone(timestamp)

    return ts_toShow + " by " + (!username ? "" : username);
};

Partial.displayPageInfo = function() {
    if (Partial.Variables.totalRecordCount.getValue('dataValue') > 0) {
        let start = (Partial.Variables.currentPage.getValue('dataValue') * fixedPageSize) + 1;
        let end = ((start + (fixedPageSize - 1)) > Partial.Variables.totalRecordCount.getValue('dataValue')) ?
            Partial.Variables.totalRecordCount.getValue('dataValue') : (start + (fixedPageSize - 1));

        return start + " to " + end + " from " + (Partial.Variables.totalRecordCount.getValue('dataValue')) + " Domain values";
    } else
        return "No domain values";
};

Partial.navigateURLBuilder = function(domainValueId) {
    let URI = "#/DomainValueCreateEdit?domainValueTypeId=" + Partial.pageParams.domainValueTypeId + "&domainValueId=" + domainValueId;
    return URI;
};

/* UI level functions */
Partial.sv_getDVListPaginatedonSuccess = function(variable, data) {
    //de-select all DV table rows and redraw table

    debugger;
    Partial.Widgets.DomainValueList_Table.selecteditem = [];

    //set pagination values
    Partial.Variables.currentPage.setValue('dataValue', data.pageNumber);

    if (Partial.Variables.totalRecordCount.getValue('dataValue') !== data.totalRecords) {
        Partial.Variables.totalRecordCount.setValue('dataValue', data.totalRecords);
    }
};

Partial.sv_getDVListPaginatedonError = function(variable, data) {
    console.log(data);

    //de-select all DV table rows and redraw table
    Partial.Widgets.DomainValueList_Table.selecteditem = [];
};

Partial.sv_getDVTypeByIdonError = function(variable, data) {
    console.log(data);
};

Partial.sv_activateDeactivateDVByIdsonSuccess = function(variable, data) {
    //set defaults for DV fetch service
    Partial.Variables.totalRecordCount.setValue('dataValue', 0);
    Partial.Variables.currentPage.setValue('dataValue', 0);
    Partial.Variables.pageSize.setValue('dataValue', fixedPageSize);
    Partial.Variables.sortProperties.setValue('dataValue',
        (Partial.Variables.sortProperties.getValue('dataValue') ?
            Partial.Variables.sortProperties.getValue('dataValue') : 'code ASC'));

    //search/fetch service invocation below
    let dvFetchSV = Partial.Variables.sv_getDVListPaginated;
    dvFetchSV.setInput({
        "domainValueTypeId": (!Partial.pageParams.domainValueTypeId) ? 0 : Partial.pageParams.domainValueTypeId,
        "showAll": Partial.Variables.showAllDVs.getValue('dataValue'),
        "isActiveFlag": Partial.Variables.showActiveDVs.getValue('dataValue'),
        "searchValue": "%" + Partial.Variables.searchValue.getValue('dataValue') + "%",
        "defaultLocale": Partial.Variables.defaultLocale.getValue('dataValue'),
        "currentPage": Partial.Variables.currentPage.getValue('dataValue'),
        "pageSize": Partial.Variables.pageSize.getValue('dataValue'),
        "sortOrders": Partial.Variables.sortProperties.getValue('dataValue')
    });

    dvFetchSV.invoke();
};

Partial.sv_activateDeactivateDVByIdsonError = function(variable, data) {
    console.log(data);

    //set defaults for DV fetch service
    Partial.Variables.totalRecordCount.setValue('dataValue', 0);
    Partial.Variables.currentPage.setValue('dataValue', 0);
    Partial.Variables.pageSize.setValue('dataValue', fixedPageSize);
    Partial.Variables.sortProperties.setValue('dataValue',
        (Partial.Variables.sortProperties.getValue('dataValue') ?
            Partial.Variables.sortProperties.getValue('dataValue') : 'code ASC'));

    //search/fetch service invocation below
    let dvFetchSV = Partial.Variables.sv_getDVListPaginated;
    dvFetchSV.setInput({
        "domainValueTypeId": (!Partial.pageParams.domainValueTypeId) ? 0 : Page.pageParams.domainValueTypeId,
        "showAll": Partial.Variables.showAllDVs.getValue('dataValue'),
        "isActiveFlag": Partial.Variables.showActiveDVs.getValue('dataValue'),
        "searchValue": "%" + Partial.Variables.searchValue.getValue('dataValue') + "%",
        "defaultLocale": Partial.Variables.defaultLocale.getValue('dataValue'),
        "currentPage": Partial.Variables.currentPage.getValue('dataValue'),
        "pageSize": Partial.Variables.pageSize.getValue('dataValue'),
        "sortOrders": Partial.Variables.sortProperties.getValue('dataValue')
    });

    dvFetchSV.invoke();
};

Partial.DomainValueList_TableSelect = function($event, widget, row) {
    Partial.Widgets.activate_Button.disabled = false;
    Partial.Widgets.deactivate_Button.disabled = false;
};

Partial.DomainValueList_TableDeselect = function($event, widget, row) {
    if (widget.selecteditem.length === 0) {
        Partial.Widgets.activate_Button.disabled = true;
        Partial.Widgets.deactivate_Button.disabled = true;
    }
};

Partial.sv_getDVTypeByIdonSuccess = function(variable, data) {
    //console.log(data);
    const regex = /(^|\b(?!(and?|at?|the|for|to|but|by)\b))\w+/g;
    Partial.title = data.description.toLowerCase().replace(regex, s => s[0].toUpperCase() + s.slice(1));
}

Partial.showInactive_LinkClick = function($event, widget) {
    Partial.Variables.totalRecordCount.setValue('dataValue', 0);
    Partial.Variables.currentPage.setValue('dataValue', 0);
    Partial.Variables.pageSize.setValue('dataValue', fixedPageSize);
    Partial.Variables.sortProperties.setValue('dataValue',
        (Partial.Variables.sortProperties.getValue('dataValue') ?
            Partial.Variables.sortProperties.getValue('dataValue') : 'code ASC'));


    Partial.Variables.showAllDVs.setValue('dataValue', false);
    Partial.Variables.showActiveDVs.setValue('dataValue', false);
    Partial.Variables.showInactiveDVs.setValue('dataValue', true);

    Partial.Widgets.activate_Button.disabled = true;
    Partial.Widgets.deactivate_Button.disabled = true;

    //search/fetch service invocation below
    let dvFetchSV = Partial.Variables.sv_getDVListPaginated;
    dvFetchSV.setInput({
        "domainValueTypeId": (!Partial.pageParams.domainValueTypeId) ? 0 : Partial.pageParams.domainValueTypeId,
        "showAll": Partial.Variables.showAllDVs.getValue('dataValue'),
        "isActiveFlag": Partial.Variables.showActiveDVs.getValue('dataValue'),
        "searchValue": "%" + Partial.Variables.searchValue.getValue('dataValue') + "%",
        "defaultLocale": Partial.Variables.defaultLocale.getValue('dataValue'),
        "currentPage": Partial.Variables.currentPage.getValue('dataValue'),
        "pageSize": Partial.Variables.pageSize.getValue('dataValue'),
        "sortOrders": Partial.Variables.sortProperties.getValue('dataValue')
    });

    dvFetchSV.invoke();
};

Partial.showActive_LinkClick = function($event, widget) {
    Partial.Variables.totalRecordCount.setValue('dataValue', 0);
    Partial.Variables.currentPage.setValue('dataValue', 0);
    Partial.Variables.pageSize.setValue('dataValue', fixedPageSize);
    Partial.Variables.sortProperties.setValue('dataValue',
        (Partial.Variables.sortProperties.getValue('dataValue') ?
            Partial.Variables.sortProperties.getValue('dataValue') : 'code ASC'));


    Partial.Variables.showAllDVs.setValue('dataValue', false);
    Partial.Variables.showActiveDVs.setValue('dataValue', true);
    Partial.Variables.showInactiveDVs.setValue('dataValue', false);

    Partial.Widgets.activate_Button.disabled = true;
    Partial.Widgets.deactivate_Button.disabled = true;

    //search/fetch service invocation below
    let dvFetchSV = Partial.Variables.sv_getDVListPaginated;
    dvFetchSV.setInput({
        "domainValueTypeId": (!Partial.pageParams.domainValueTypeId) ? 0 : Partial.pageParams.domainValueTypeId,
        "showAll": Partial.Variables.showAllDVs.getValue('dataValue'),
        "isActiveFlag": Partial.Variables.showActiveDVs.getValue('dataValue'),
        "searchValue": "%" + Partial.Variables.searchValue.getValue('dataValue') + "%",
        "defaultLocale": Partial.Variables.defaultLocale.getValue('dataValue'),
        "currentPage": Partial.Variables.currentPage.getValue('dataValue'),
        "pageSize": Partial.Variables.pageSize.getValue('dataValue'),
        "sortOrders": Partial.Variables.sortProperties.getValue('dataValue')
    });

    dvFetchSV.invoke();
};

Partial.showAll_LinkClick = function($event, widget) {
    Partial.Variables.totalRecordCount.setValue('dataValue', 0);
    Partial.Variables.currentPage.setValue('dataValue', 0);
    Partial.Variables.pageSize.setValue('dataValue', fixedPageSize);
    Partial.Variables.sortProperties.setValue('dataValue',
        (Partial.Variables.sortProperties.getValue('dataValue') ?
            Partial.Variables.sortProperties.getValue('dataValue') : 'code ASC'));


    Partial.Variables.showAllDVs.setValue('dataValue', true);
    Partial.Variables.showActiveDVs.setValue('dataValue', false);
    Partial.Variables.showInactiveDVs.setValue('dataValue', false);

    Partial.Widgets.activate_Button.disabled = true;
    Partial.Widgets.deactivate_Button.disabled = true;

    //search/fetch service invocation below
    let dvFetchSV = Partial.Variables.sv_getDVListPaginated;
    dvFetchSV.setInput({
        "domainValueTypeId": (!Partial.pageParams.domainValueTypeId) ? 0 : Partial.pageParams.domainValueTypeId,
        "showAll": Partial.Variables.showAllDVs.getValue('dataValue'),
        "isActiveFlag": Partial.Variables.showActiveDVs.getValue('dataValue'),
        "searchValue": "%" + Partial.Variables.searchValue.getValue('dataValue') + "%",
        "defaultLocale": Partial.Variables.defaultLocale.getValue('dataValue'),
        "currentPage": Partial.Variables.currentPage.getValue('dataValue'),
        "pageSize": Partial.Variables.pageSize.getValue('dataValue'),
        "sortOrders": Partial.Variables.sortProperties.getValue('dataValue')
    });

    dvFetchSV.invoke();
};

Partial.activate_ButtonClick = function($event, widget) {
    Partial.Widgets.activate_Button.disabled = true;
    Partial.Widgets.deactivate_Button.disabled = true;

    //iterate over selected records, prepare DV ID list and activate them
    let DVIds = [];
    Partial.Widgets.DomainValueList_Table.selecteditem.forEach((domainValueItem) => {
        DVIds.push(domainValueItem.id);
    });

    //activate/deactivate service invocation below
    let dvActivateSV = Partial.Variables.sv_activateDeactivateDVByIds;
    dvActivateSV.setInput({
        "isToActivate": true,
        "ids": DVIds
    });

    dvActivateSV.invoke();
};

Partial.deactivate_ButtonClick = function($event, widget) {
    Partial.Widgets.activate_Button.disabled = true;
    Partial.Widgets.deactivate_Button.disabled = true;

    //iterate over selected records, prepare DV ID list and activate them
    let DVIds = [];
    Partial.Widgets.DomainValueList_Table.selecteditem.forEach((domainValueItem) => {
        DVIds.push(domainValueItem.id);
    });

    //activate/deactivate service invocation below
    let dvDeActivateSV = Partial.Variables.sv_activateDeactivateDVByIds;
    dvDeActivateSV.setInput({
        "isToActivate": false,
        "ids": DVIds
    });

    dvDeActivateSV.invoke();
};

Partial.DomainValueList_TableSort = function($event, $data) {
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
        let dvFetchSV = Partial.Variables.sv_getDVListPaginated;
        dvFetchSV.setInput({
            "domainValueTypeId": (!Partial.pageParams.domainValueTypeId) ? 0 : Partial.pageParams.domainValueTypeId,
            "showAll": Partial.Variables.showAllDVs.getValue('dataValue'),
            "isActiveFlag": Partial.Variables.showActiveDVs.getValue('dataValue'),
            "searchValue": "%" + Partial.Variables.searchValue.getValue('dataValue') + "%",
            "defaultLocale": Partial.Variables.defaultLocale.getValue('dataValue'),
            "currentPage": Partial.Variables.currentPage.getValue('dataValue'),
            "pageSize": Partial.Variables.pageSize.getValue('dataValue'),
            "sortOrders": Partial.Variables.sortProperties.getValue('dataValue')
        });

        dvFetchSV.invoke();
    }, 50);

};

Partial.searchDV_ButtonClick = function($event, widget) {
    Partial.Variables.totalRecordCount.setValue('dataValue', 0);
    Partial.Variables.currentPage.setValue('dataValue', 0);
    Partial.Variables.pageSize.setValue('dataValue', fixedPageSize);
    Partial.Variables.sortProperties.setValue('dataValue',
        (Partial.Variables.sortProperties.getValue('dataValue') ?
            Partial.Variables.sortProperties.getValue('dataValue') : 'code ASC'));

    //search/fetch service invocation below
    let dvFetchSV = Partial.Variables.sv_getDVListPaginated;
    dvFetchSV.setInput({
        "domainValueTypeId": (!Partial.pageParams.domainValueTypeId) ? 0 : Partial.pageParams.domainValueTypeId,
        "showAll": Partial.Variables.showAllDVs.getValue('dataValue'),
        "isActiveFlag": Partial.Variables.showActiveDVs.getValue('dataValue'),
        "searchValue": "%" + Partial.Variables.searchValue.getValue('dataValue') + "%",
        "defaultLocale": Partial.Variables.defaultLocale.getValue('dataValue'),
        "currentPage": Partial.Variables.currentPage.getValue('dataValue'),
        "pageSize": Partial.Variables.pageSize.getValue('dataValue'),
        "sortOrders": Partial.Variables.sortProperties.getValue('dataValue')
    });

    dvFetchSV.invoke();
};

Partial.createDV_ButtonClick = function($event, widget) {

    Partial.Actions.goToPage_DomainValueCreateEdit.invoke({
        data: {
            'domainValueTypeId': Partial.pageParams.domainValueTypeId
        }
    });
};
Partial.DomainValueList_TableHeaderclick = function($event, $data) {

};