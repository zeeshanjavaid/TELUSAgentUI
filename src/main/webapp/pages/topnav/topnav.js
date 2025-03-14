/** 
 ****************************************************************************
 **
 **   This material is the confidential, proprietary, unpublished property
 **   of Fair Isaac Corporation.  Receipt or possession of this material
 **   does not convey rights to divulge, reproduce, use, or allow others
 **   to use it without the specific written authorization of Fair Isaac
 **   Corporation and use must conform strictly to the license agreement.
 **
 **   Copyright (c) 2022 Fair Isaac Corporation.  All rights reserved.
 **
 ****************************************************************************
 **/
Partial.QueueChildren = [];
//Partial.UserAssignedMenus = [];
Partial.isUserAutoProvisionEnabled = false;
Partial.Variables.getWorkCategoriesByCode.dataSet;

Partial.onReady = function() {
    debugger;

    //Partial.Variables.getWorkCategoriesByCode.dataSet;

    // loadAppMetadata();
    Partial.Variables.getWorkCatByEmplIdForMultiSelect.setInput({
        'emplId': Partial.Variables.getLoggedInUserDetails.dataSet.emplId

    });
    Partial.Variables.getWorkCatByEmplIdForMultiSelect.invoke();
    Partial.Variables.storeworkCategoryForMultiSelect = Partial.Variables.getWorkCategoriesByCode.dataSet;

};


Partial.checkUserAutoProvisioning = function() {

    //Set Site properties
    Partial.Variables.getSitePropertyByName.setInput('name', 'EnableUserAutoProvision');
    Partial.Variables.getSitePropertyByName.invoke();




}


Partial.menuSelect = function($event, widget, $item) {

    App.Variables.priorityQueuePageNumber.dataSet.dataValue = -1;
    var currentPgName = App.activePageName;
    const pageURL = $item.url ? $item.url : $item.link;

    if (pageURL) {
        // We need to force a reload if sub menu keeps the user on same page
        // E.g. Queue sub menu, just has query param change, so this does not force a page refresh

        var goToPage = pageURL.split('#/')[1].split('?')[0];
        var currentPage = window.location.href.split('#/')[1].split('?')[0];
        if (goToPage === currentPage) {
            window.location.replace(pageURL);
            window.location.reload(true);
        } else {
            window.location.replace(pageURL);
        }
    }
};

Partial.getApplicationByApplicationNumberonError = function(variable, data) {
    App.Variables.errorMsg.dataSet.dataValue = "The Search did not return any results. Modify search criteria and try again."
};

Partial.textSearchKeyup = function($event, widget) {
    var keyCode = ($event.keyCode ? $event.keyCode : $event.which);
    if (keyCode == '13') {
        // keyCode for enter is 13.
        Partial.OMTopNav1Search(Partial.Widgets.textSearch.datavalue.trim());
    }
};
Partial.containerSearchIconClick = function($event, widget) {
    $('.search-input').toggleClass('search-transition');
    Partial.Widgets.textSearch.focus();
};
Partial.OMTopNav1Search = function(data) {
    if (Partial.Widgets.textSearch.datavalue != "") {
        Partial.Variables.getApplicationIdByApplicationNumber.invoke();
    }
};

Partial.textSearchBlur = function($event, widget) {
    if (Partial.Widgets.textSearch.datavalue != "") {
        Partial.Variables.getApplicationIdByApplicationNumber.invoke();
    }
};

Partial.getApplicationIdByApplicationNumberonSuccess = function(variable, data) {
    // App.Actions.goToPage_ApplicationCreateEdit.invoke({
    //     data: {
    //         'applicationId': data.value
    //     }
    // });
    window.location.href = "/ApplicationBroker?applicationId=" + data.value;
    document.location.reload(true);
};

Partial.getApplicationIdByApplicationNumberonError = function(variable, data) {
    App.Variables.errorMsg.dataSet.dataValue = "The Search did not return any results. Modify search criteria and try again."
    var elem = document.querySelector('.search-input');
    elem.style.backgroundColor = 'red';
};

Partial.db_AllQueuesonError = function(variable, data) {
    console.error(data);
};

Partial.db_AllQueuesonSuccess = function(variable, data) {
    // Partial.QueueChildren = [];

    //only add the Queue option if there are queues available

    Partial.Widgets.leftMenu.dataset = [];
    Partial.Widgets.leftMenu.dataset = Partial.Variables.leftTopMenuMV.dataSet;

};

function initializeUserPermission() {

    if (!cache_utils.isStoredInCache("SessionStorage", "PERMISSION", "Permission_Access", App.Variables.PermissionsForLoggedInUserId)) {
        App.Variables.PermissionsForLoggedInUserId.invoke();
        if (App.Variables.PermissionsForLoggedInUserId.dataSet.length > 0)
            cache_utils.storeInCache("SessionStorage", "PERMISSION", "Permission_Access", App.Variables.PermissionsForLoggedInUserId);
    } else {
        cache_utils.fetchUsingCache(App, "SessionStorage", "PERMISSION", "Permission_Access", App.Variables.PermissionsForLoggedInUserId);
    }
    App.Variables.PermissionsForLoggedInUserId.dataSet.forEach(function(source) {
        Partial.userPermission.set(source.name, 1);
    });
}

Partial.IsUserHasAccess = function(e) {
    if (Partial.userPermission.has(e)) {
        return true;
    } else {
        return false;
    }
}

async function loadAppMetadata() {
    //App.initializeQueueFilterFields();
    await App.getUserTimeZone();
    await App.loadAppPermissions();

    // Filtering the TopNav menus based on Permission the loggedIn user has
    // Partial.QueueChildren = [];
    // Partial.UserAssignedMenus = [];
    // Partial.Variables.leftTopMenuMV.dataSet.forEach(function(m) {

    //     if (m.code === 'SystemAdministration' && App.IsUserHasAccess('Support')) {
    //         Partial.UserAssignedMenus.push(m);
    //     } else if (m.code === 'LookUp' && App.IsUserHasAccess('Support')) {
    //         Partial.UserAssignedMenus.push(m);
    //     } else if (m.code === 'SearchApplication' && App.IsUserHasAccess('Access_ApplicationSearch')) {
    //         Partial.UserAssignedMenus.push(m);
    //     } else if (m.code === 'Setup' && App.IsUserHasAccess('Support')) {
    //         Partial.UserAssignedMenus.push(m);
    //     } else if (m.code === 'Security' && App.IsUserHasAccess('Support')) {
    //         var childrens = [];
    //         m.children.forEach(function(c) {
    //             if (c.code === 'Users' && App.IsUserHasAccess('Support'))
    //                 childrens.push(c);
    //             else if (c.code === 'Groups' && App.IsUserHasAccess('Support'))
    //                 childrens.push(c);
    //             else if (c.code === 'Roles' && App.IsUserHasAccess('Support'))
    //                 childrens.push(c);
    //             else if (c.code === 'LockedApplications' && App.IsUserHasAccess('Support'))
    //                 childrens.push(c);
    //         });
    //         m.children = childrens;
    //         Partial.UserAssignedMenus.push(m);
    //     } else if (m.code === 'ActivityLogs' && App.IsUserHasAccess('Support')) { //else if (m.code === 'ActivityLogs' && App.IsUserHasAccess('Access_ActivityLog'))
    //         Partial.UserAssignedMenus.push(m);
    //     } else if (m.code === 'Reports' && App.IsUserHasAccess('Support')) {
    //         Partial.UserAssignedMenus.push(m);
    //         var childrens = [];
    //         m.children.forEach(function(c) {
    //             if (c.code === 'ApplicationDashboardReport' && App.IsUserHasAccess('Support'))
    //                 childrens.push(c);
    //             else if (c.code === 'ManualReviewDashboardReport' && App.IsUserHasAccess('Support'))
    //                 childrens.push(c);
    //             else if (c.code === 'ReportOperatorPerformance' && App.IsUserHasAccess('Support'))
    //                 childrens.push(c);
    //             else if (c.code === 'QueueSummaryDashboardReport' && App.IsUserHasAccess('Support'))
    //                 childrens.push(c);
    //         });
    //         m.children = childrens;
    //         Partial.UserAssignedMenus.push(m);
    //     }
    // 
    // });



    // Overriding the previous menus with newly filtered Permissions
    Partial.Variables.leftTopMenuMV.dataSet = Partial.UserAssignedMenus;



    debugger;

    // for Queue related
    if (App.IsUserHasAccess('Access_QueueMenu'))
        Partial.Variables.db_AllQueues.invoke();
    else {
        Partial.Widgets.leftMenu.dataset = [];
        Partial.Widgets.leftMenu.dataset = Partial.Variables.leftTopMenuMV.dataSet;
    }
    // Changing navbar visibility to visible
    $('.app-top-nav')[0].style.visibility = 'visible';
}

Partial.getSitePropertyByNameonError = function(variable, data) {

    console.log("Site Property Error :" + data);

};

Partial.getSitePropertyByNameonSuccess = function(variable, data) {

    console.log("Site property on success :" + variable.dataBinding.name);

    if (variable.dataBinding.name == "EnableUserAutoProvision") {
        Partial.isUserAutoProvisionEnabled = data.value;
        console.log(" Is user auto provision enabled:" + Partial.isUserAutoProvisionEnabled);
        if (Partial.isUserAutoProvisionEnabled.toUpperCase() == 'TRUE') {
            console.log("User autoprovision enabled.");
            Partial.Variables.getLoggerInUserIdentification.invoke();

        } else {
            console.log("User autoprovision disabled.");

        }
    } else {
        Page.Variables.mv_MaxSelectedOffers.dataSet.dataValue = data.value;
    }

};
Partial.anchor1Click = function($event, widget) {

};
Partial.anchor4Select = function($event, widget, $item) {
    debugger;
};