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
Partial.UserAssignedMenus = [];
Partial.isUserAutoProvisionEnabled = false;

Partial.onReady = function() {
    Partial.Variables.leftTopMenuMV.dataSet = [{
            "name": Partial.appLocale.SETUP,
            "description": "Manage application processing parameters, credit bureau, and more.",
            "createdOn": "2022-01-11T11:38:32.838+0000",
            "updatedOn": "2022-01-12T10:09:40.556+0000",
            "createdBy": "cwayjones_otp@fico.com",
            "updatedBy": "chetanshettigar_otp@fico.com",
            "status": "Active",
            "code": "Setup",
            "categoryType": "top",
            "isExternalURL": false,
            "path": "#/SetupLanding",
            "project": "FicoStudioCore",
            "url": "#/SetupLanding",
            "isEditable": false,
            "parent": true,
            "target": "_self",
            "order": 3,
            "roles": [
                "7e1d0ffd-9670-43ea-a647-b45b0ebfff42",
                "e96c23d7-c8e6-445f-a503-50b475d7c76e",
                "62dea141-9b72-46e1-8101-fdf11757e8b7",
                "3a82c367-ae9e-437f-87ce-97446c5dbca1",
                "471f444e-6595-4783-a17c-9e3f2437daf5",
                "4fbac974-ae93-452b-82d5-04433dc9a2db",
                "850e553d-de24-4275-87fd-58b42be461ef",
                "e4aa48c5-83c8-4e11-9a61-b5d8b9fd9214",
                "fdaa1c10-b2fb-43bf-a095-242a5b3f0a57",
                "1192a4f4-c64c-4690-9098-464a42888ece",
                "cb2bfc10-af06-42ab-a2c7-12f3c1151a25",
                "93e1f11a-74cf-449b-8be7-2f045ad0e9b1"
            ],
            "deleted": false
        },
        {
            "name": Partial.appLocale.SECURITY,
            "description": "Manage security",
            "createdOn": "2022-01-11T11:38:32.838+0000",
            "updatedOn": "2022-01-12T10:09:40.556+0000",
            "createdBy": "cwayjones_otp@fico.com",
            "updatedBy": "chetanshettigar_otp@fico.com",
            "status": "Active",
            "code": "Security",
            "children": [{
                    "name": Partial.appLocale.USERS,
                    "description": "Manage users",
                    "updatedOn": "2022-01-12T10:09:40.556+0000",
                    "updatedBy": "chetanshettigar_otp@fico.com",
                    "status": "Active",
                    "code": "Users",
                    "categoryType": "top/Users",
                    "isExternalURL": false,
                    "path": "#/Users",
                    "project": "FICO-Applications-Workbench-Component",
                    "url": "#/Users",
                    "isEditable": true,
                    "parent": true,
                    "target": "_self",
                    "order": 1,
                    "roles": [
                        "1b56efc5-1130-4d3c-803e-2defdbab7c8b"
                    ],
                    "deleted": false
                },
                {
                    "name": Partial.appLocale.GROUPS,
                    "description": "Manage groups",
                    "updatedOn": "2022-01-12T10:09:40.557+0000",
                    "updatedBy": "chetanshettigar_otp@fico.com",
                    "status": "Active",
                    "code": "Groups",
                    "categoryType": "top/Groups",
                    "isExternalURL": false,
                    "path": "#/Groups",
                    "project": "FICO-Applications-Workbench-Component",
                    "url": "#/Groups",
                    "isEditable": true,
                    "parent": true,
                    "target": "_self",
                    "roles": [
                        "603c3cab-a80e-44bc-8bc2-ea822d5367a7"
                    ],
                    "deleted": false
                },
                {
                    "name": Partial.appLocale.ROLES,
                    "description": "Manage roles",
                    "updatedOn": "2022-01-12T10:09:40.557+0000",
                    "updatedBy": "chetanshettigar_otp@fico.com",
                    "status": "Active",
                    "code": "Roles",
                    "categoryType": "top/Roles",
                    "isExternalURL": false,
                    "path": "#/Roles",
                    "project": "FicoStudioCore",
                    "url": "#/Roles",
                    "isEditable": true,
                    "parent": true,
                    "target": "_self",
                    "order": 1,
                    "roles": [
                        "56527196-415d-47b1-a7be-dcba4e02ae6f"
                    ],
                    "deleted": false
                }
            ],
            "categoryType": "top",
            "isExternalURL": false,
            "path": "#/Security",
            "project": "FicoStudioCore",
            "url": "https://fstu-core-290jhnfqlaq.dms.euwt1.ficoanalyticcloud.com/#/Security",
            "isEditable": false,
            "parent": true,
            "target": "_self",
            "order": 4,
            "roles": [
                "1b56efc5-1130-4d3c-803e-2defdbab7c8b",
                "56527196-415d-47b1-a7be-dcba4e02ae6f",
                "603c3cab-a80e-44bc-8bc2-ea822d5367a7"
            ],
            "deleted": false
        },
        { //This needs to be configured for Audit
            "name": Partial.appLocale.AUDIT,
            "description": "Manage promotion of changes through environments",
            "createdOn": "2022-01-11T11:38:32.840+0000",
            "updatedOn": "2022-01-12T10:09:40.557+0000",
            "createdBy": "cwayjones_otp@fico.com",
            "updatedBy": "chetanshettigar_otp@fico.com",
            "status": "Active",
            "code": "ActivityLogs",
            "categoryType": "top",
            "isExternalURL": false,
            "path": "#/ActivityLogs",
            "project": "FicoStudioCore",
            "url": "#/ActivityLogs",
            "isEditable": true,
            "parent": true,
            "target": "_self",
            "order": 1,
            "roles": [
                "fdaa1c10-b2fb-43bf-a095-242a5b3f0a57"
            ],
            "deleted": false
        },
        {
            "name": Partial.appLocale.REPORTS,
            "createdOn": "2022-01-12T10:09:40.557+0000",
            "updatedOn": "2022-01-12T10:09:40.557+0000",
            "createdBy": "chetanshettigar_otp@fico.com",
            "updatedBy": "chetanshettigar_otp@fico.com",
            "status": "Active",
            "code": "Reports",
            "children": [{
                    "name": Partial.appLocale.APPLICATION_DASHBOARD_REPORT,
                    "updatedOn": "2022-01-12T10:09:40.557+0000",
                    "updatedBy": "rohanshetty_td@fico.com",
                    "status": "Active",
                    "code": "ApplicationDashboardReport",
                    "categoryType": "top/ApplicationDashboardReport",
                    "isExternalURL": false,
                    "path": "#/ApplicationDashboardReport",
                    "project": "FICO-Applications-Workbench-Component",
                    "url": "#/ApplicationDashboardReport",
                    "isEditable": true,
                    "parent": true,
                    "target": "_self",
                    "roles": [
                        "76b50056-422c-417b-9936-ea01752a5f82"
                    ],
                    "deleted": false
                },
                {
                    "name": Partial.appLocale.MANUAL_REVIEW_DASHBOARD_REPORT,
                    "updatedOn": "2022-01-12T10:09:40.557+0000",
                    "updatedBy": "rohanshetty_td@fico.com",
                    "status": "Active",
                    "code": "ManualReviewDashboardReport",
                    "categoryType": "top/ManualReviewDashboardReport",
                    "isExternalURL": false,
                    "path": "#/ManualReviewDashboardReport",
                    "project": "FICO-Applications-Workbench-Component",
                    "url": "#/ManualReviewDashboardReport",
                    "isEditable": true,
                    "parent": true,
                    "target": "_self",
                    "roles": [
                        "76b50056-422c-417b-9936-ea01752a5f82"
                    ],
                    "deleted": false
                },
                {
                    "name": Partial.appLocale.LABEL_OPERATOR_PERFORMANCE_REPORT,
                    "updatedOn": "2022-01-12T10:09:40.557+0000",
                    "updatedBy": "rohanshetty_td@fico.com",
                    "status": "Active",
                    "code": "ReportOperatorPerformance",
                    "categoryType": "top/ReportOperatorPerformance",
                    "isExternalURL": false,
                    "path": "#/ReportOperatorPerformance",
                    "project": "FICO-Applications-Workbench-Component",
                    "url": "#/ReportOperatorPerformance",
                    "isEditable": true,
                    "parent": true,
                    "target": "_self",
                    "roles": [
                        "76b50056-422c-417b-9936-ea01752a5f82"
                    ],
                    "deleted": false
                },
                {
                    "name": Partial.appLocale.QUEUE_SUMMARY_REPORT,
                    "updatedOn": "2022-01-12T10:09:40.557+0000",
                    "updatedBy": "rohanshetty_td@fico.com",
                    "status": "Active",
                    "code": "QueueSummaryDashboardReport",
                    "categoryType": "top/QueueSummaryDashboardReport",
                    "isExternalURL": false,
                    "path": "#/QueueSummaryDashboardReport",
                    "project": "FICO-Applications-Workbench-Component",
                    "url": "#/QueueSummaryDashboardReport",
                    "isEditable": true,
                    "parent": true,
                    "target": "_self",
                    "roles": [
                        "76b50056-422c-417b-9936-ea01752a5f82"
                    ],
                    "deleted": false
                }
            ],
            "categoryType": "top",
            "isExternalURL": false,
            "path": "#/About",
            "project": "FicoStudioCore",
            "url": "https://fstu-core-290jhnfqlaq.dms.euwt1.ficoanalyticcloud.com/#/About",
            "isEditable": true,
            "parent": true,
            "target": "_self",
            "roles": [
                "a7b47664-9a85-4eac-b0f2-f8162237ddb0",
                "76b50056-422c-417b-9936-ea01752a5f82"
            ],
            "deleted": false
        }

        // {
        //     "name": Partial.appLocale.QUEUES,
        //     "description": "Access queues",
        //     "createdOn": "2022-01-11T11:38:32.840+0000",
        //     "updatedOn": "2022-01-12T10:09:40.557+0000",
        //     "createdBy": "cwayjones_otp@fico.com",
        //     "updatedBy": "chetanshettigar_otp@fico.com",
        //     "status": "Active",
        //     "code": "Queues",
        //     "categoryType": "top",
        //     "isExternalURL": false,
        //     "path": "#/Queues",
        //     "project": "FICO-Applications-Workbench-Component",
        //     "url": "#/Queues",
        //     "isEditable": true,
        //     "parent": true,
        //     "target": "_self",
        //     "order": 1,
        //     "roles": [
        //         "fba38a82-cd26-4519-a536-a401903cab6b"
        //     ],
        //     "deleted": false
        // }
    ];


    //Partial.Variables.db_AllQueues.invoke();

    Partial.Variables.rightTopMenuMV.dataSet = [{
        "name": Partial.appLocale.HOME,
        "description": "Return to Home Page",
        "createdOn": "2021-08-24T06:42:38.196+0000",
        "updatedOn": "2021-08-24T06:42:41.461+0000",
        "createdBy": "ignasvaitkevicius_otp@fico.com",
        "updatedBy": "ignasvaitkevicius_otp@fico.com",
        "status": "Active",
        "code": "Home",
        "categoryType": "session",
        "isExternalURL": false,
        "path": "#/Main",
        "project": "FICO-Applications-Workbench-Component",
        "url": "#/Main",
        "isEditable": true,
        "parent": true,
        "target": "_self",
        "order": "1",
        "roles": [
            "7e1d0ffd-9670-43ea-a647-b45b0ebfff42",
            "f7a5212b-3c30-40eb-bdc1-6b1ebfe2d031"
        ],
        "deleted": false
    }];
    var menus = Partial.Variables.rightTopMenuMV.dataSet;
    var logoutPresent = false;
    Partial.checkUserAutoProvisioning();
    menus.forEach(function(m) {
        if (m.path && App.activePageName === m.path.substring(m.path.lastIndexOf("/") + 1)) {
            m.class = "activeClass";
        } else if (m.hasOwnProperty("children") && m.children && m.children.length > 0) {
            var childFound = false;
            m.children.forEach(function(c) {
                if (c.path !== null) {
                    if (App.activePageName === c.path.substring(c.path.lastIndexOf("/") + 1)) {
                        childFound = true;
                    }
                }
            });
            if (childFound) {
                m.class = "activeClass";
            } else {
                m.class = "";
            }
        } else if (m.class === 'activeClass') {
            m.class = '';
        }

        if (m.code === 'Logout') {
            logoutPresent = true;
        }
    });

    if ($(".search-input").val().length === 0) {
        $('.search-input').removeClass('search-transition');
    }
    Partial.Widgets.textSearch.datavalue = '';

    if (!logoutPresent) {
        var logout = {
            "categoryType": "session",
            "children": [],
            "code": "Logout",
            "description": "Logs out from the application",
            "identifier": null,
            "name": "Logout",
            "order": 10,
            "parent": true,
            "roles": [],
            "status": "Active",
            "url": "#/Logout"
        }
        Partial.Variables.rightTopMenuMV.dataSet.push(logout);
        if (Partial.Variables.rightTopMenuMV.dataSet) {
            for (var i = 0; i < Partial.Variables.rightTopMenuMV.dataSet.length; i++) {
                Partial.Variables.rightTopMenuMV.dataSet[i].name = Partial.appLocale[Partial.Variables.rightTopMenuMV.dataSet[i].code] ? Partial.appLocale[Partial.Variables.rightTopMenuMV.dataSet[i].code] : Partial.Variables.rightTopMenuMV.dataSet[i].name;
            }
        }
        if (Partial.Variables.rightTopMenuMV.dataSet && Partial.Variables.rightTopMenuMV.dataSet[1] && Partial.Variables.rightTopMenuMV.dataSet[1].children) {

            for (j = 0; j < Partial.Variables.rightTopMenuMV.dataSet[1].children.length; j++) {
                Partial.Variables.rightTopMenuMV.dataSet[1].children[j].name = Partial.appLocale[Partial.Variables.rightTopMenuMV.dataSet[1].children[j].code] ? Partial.appLocale[Partial.Variables.rightTopMenuMV.dataSet[1].children[j].code] : Partial.Variables.rightTopMenuMV.dataSet[1].children[j].name;
            }
        }
    }


    loadAppMetadata();
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
    window.location.href = "#/ApplicationBroker?applicationId=" + data.value;
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
    Partial.QueueChildren = [];

    //only add the Queue option if there are queues available
    if (data.length > 0) {
        for (let Q of data) {
            let child = {
                "name": Q.name,
                "updatedOn": "2022-01-12T10:09:40.557+0000",
                "updatedBy": "AnsuR_td@fico.com",
                "status": "Active",
                "code": Q.name,
                "categoryType": "top/" + Q.name,
                "isExternalURL": false,
                "path": "#/QueueSearch",
                "project": "FICO-Applications-Workbench-Component",
                "url": "#/QueueSearch?queueId=" + Q.id,
                "isEditable": true,
                "parent": true,
                "target": "_self",
                "order": 1,
                "roles": [
                    "a7b47664-9a85-4eac-b0f2-f8162237ddb0"
                ],
                "deleted": false
            };

            Partial.QueueChildren.push(child);
        }

        let queueMenuPresent = true;
        let QMenuObj = Partial.Variables.leftTopMenuMV.dataSet.find((menuObj) => {
            return menuObj.name === Partial.appLocale.QUEUES;
        });
        queueMenuPresent = QMenuObj ? true : false;

        if (!queueMenuPresent) {
            let queue = {
                "name": Partial.appLocale.QUEUES,
                "description": "Access queues",
                "createdOn": "2022-01-11T11:38:32.840+0000",
                "updatedOn": "2022-01-12T10:09:40.557+0000",
                "createdBy": "cwayjones_otp@fico.com",
                "updatedBy": "chetanshettigar_otp@fico.com",
                "status": "Active",
                "code": "Queues",
                "categoryType": "top",
                "isExternalURL": false,
                "path": "#/QueueSearch",
                "project": "FICO-Applications-Workbench-Component",
                "url": "#/QueueSearch",
                "isEditable": true,
                "parent": true,
                "target": "_self",
                "order": 1,
                "roles": [
                    "fba38a82-cd26-4519-a536-a401903cab6b"
                ],
                "children": Partial.QueueChildren,
                "deleted": false
            };

            Partial.Variables.leftTopMenuMV.dataSet.push(queue);
        } else {
            QMenuObj.children = [];
            QMenuObj.children = Partial.QueueChildren;
        }
    } else {
        let menu_index = Partial.Variables.leftTopMenuMV.dataSet.findIndex(menuoption => menuoption.name === Partial.appLocale.QUEUES);
        if (menu_index !== -1)
            Partial.Variables.leftTopMenuMV.dataSet.splice(menu_index, 1);
    }

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
    App.initializeQueueFilterFields();
    await App.getUserTimeZone();
    await App.loadAppPermissions();

    // Filtering the TopNav menus based on Permission the loggedIn user has
    Partial.QueueChildren = [];
    Partial.UserAssignedMenus = [];
    Partial.Variables.leftTopMenuMV.dataSet.forEach(function(m) {

        if (m.code === 'CreateApplication' && App.IsUserHasAccess('Access_CreateApplication')) {
            Partial.UserAssignedMenus.push(m);
        } else if (m.code === 'SearchApplication' && App.IsUserHasAccess('Access_ApplicationSearch')) {
            Partial.UserAssignedMenus.push(m);
        } else if (m.code === 'Setup' && App.IsUserHasAccess('Setup_Menu')) {
            Partial.UserAssignedMenus.push(m);
        } else if (m.code === 'Security' && App.IsUserHasAccess('Security_Menu')) {
            var childrens = [];
            m.children.forEach(function(c) {
                if (c.code === 'Users' && App.IsUserHasAccess('Security_Users'))
                    childrens.push(c);
                else if (c.code === 'Groups' && App.IsUserHasAccess('Security_Groups'))
                    childrens.push(c);
                else if (c.code === 'Roles' && App.IsUserHasAccess('Security_Roles'))
                    childrens.push(c);
                else if (c.code === 'LockedApplications' && App.IsUserHasAccess('Security_LockedApplications'))
                    childrens.push(c);
            });
            m.children = childrens;
            Partial.UserAssignedMenus.push(m);
        } else if (m.code === 'ActivityLogs' && App.IsUserHasAccess('Access_ActivityLog')) { //else if (m.code === 'ActivityLogs' && App.IsUserHasAccess('Access_ActivityLog'))
            Partial.UserAssignedMenus.push(m);
        } else if (m.code === 'Reports' && App.IsUserHasAccess('Report_Menu')) {
            // Partial.UserAssignedMenus.push(m);
            var childrens = [];
            m.children.forEach(function(c) {
                if (c.code === 'ApplicationDashboardReport' && App.IsUserHasAccess('Report_ApplicationDashboardReport'))
                    childrens.push(c);
                else if (c.code === 'ManualReviewDashboardReport' && App.IsUserHasAccess('Report_ManualSummaryReport'))
                    childrens.push(c);
                else if (c.code === 'ReportOperatorPerformance' && App.IsUserHasAccess('Report_OperatorPerformanceReport'))
                    childrens.push(c);
                else if (c.code === 'QueueSummaryDashboardReport' && App.IsUserHasAccess('Report_QueueSummaryReport'))
                    childrens.push(c);
            });
            m.children = childrens;
            Partial.UserAssignedMenus.push(m);
        }

    });

    // Overriding the previous menus with newly filtered Permissions
    Partial.Variables.leftTopMenuMV.dataSet = Partial.UserAssignedMenus;

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