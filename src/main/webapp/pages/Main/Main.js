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

// $('document').ready(() => {
//     document.getElementsByTagName("html")[0].style.visibility = "hidden";
// });

/* perform any action on widgets/variables within this block */
Page.onReady = function() {

    debugger;

    Page.Variables.storeworkCategoryForMultiSelect = Page.Variables.getWorkCategoriesByCode.dataSet;

    /*
     * variables can be accessed through 'Page.Variables' property here
     * e.g. to get dataSet in a staticVariable named 'loggedInUser' use following script
     * Page.Variables.loggedInUser.getData()
     *
     * widgets can be accessed through 'Page.Widgets' property here
     * e.g. to get value of text widget named 'username' use following script
     * 'Page.Widgets.username.datavalue'
     */
    //  debugger
    //App.Variables.selectedLocale.dataValue = App.Variables.supportedLocale._context.Variables.supportedLocale._context.i18nService.selectedLocale;
    // debugger
    // console.log(App.Variables.mv_searchFilters);

    loadDashboardMenus();
};

Page.dummyUsedAsApplicationStatusOnChangeHandlerSVonBeforeUpdate = function(variable, inputData, options) {
    var filteredDV = App.filterDomainValue("ApplicationAction", inputData.locale);
    Page.Variables.applicationActionDV.setData(filteredDV);
    return false; // Prevents the service variable call being made
};

Page.listItemsList1Render = function(widget, $data) {
    $('.dashboard-list-items li').attr('tabindex', -1);
    $('.dashboard-list-items .dashboard-menu-item').attr('tabindex', 0);
};

Page.dashboardMenuItemClick = function($event, widget, item, currentItemWidgets) {
    widget.nativeElement.lastElementChild.firstElementChild.click();
    var currentPgName = App.activePageName;

    if (item.path) {
        // We need to force a reload if sub menu keeps the user on same page
        // E.g. Queue sub menu, just has query param change, so this does not force a page refresh
        var goToPage = item.path.split('#/')[1].split('?')[0];
        var currentPage = window.location.href.split('#/')[1].split('?')[0];
        if (goToPage === currentPage) {
            // window.location.replace(item.path);
            // window.location.reload(true);
            App.Actions.goToPage_Main.invoke();
        } else {
            //window.location.replace(item.path);
            switch (goToPage) {
                // case "ApplicationCreateEdit":
                //     App.Actions.goToPage_ApplicationCreateEdit.invoke();
                //     break;
                // case "ApplicationSearch":
                //     App.Actions.goToPage_ApplicationSearch.invoke();
                //     break;
                case "SetupLanding":
                    App.Actions.goToPage_SetupLanding.invoke();
                    break;
                case "Users":
                    App.Actions.goToPage_Users.invoke();
                    break;
                case "Groups":
                    App.Actions.goToPage_Groups.invoke();
                    break;
                case "Roles":
                    App.Actions.goToPage_Roles.invoke();
                    break;

            }
        }
    }
};


function loadDashboardMenus() {

    debugger;

    //  Page.Variables.storeworkCategoryForMultiSelect = Page.Variables.getWorkCategoriesByCode.dataSet;

    //  Page.workCategoryData = [];
    Page.Variables.getWorkCategoriesByCode.dataSet.forEach(function(item) {
        App.Variables.storeworkCategoryForMultiSelect.push({
            id: item.code.replace(/\s/g, ''),
            title: item.code
        });
    });

    /*    const intervalId = setInterval(function() {
            if (App.permissionsLoaded) {
                clearInterval(intervalId);
                console.log('Permissions loaded...');
                document.getElementsByTagName("html")[0].style.visibility = "visible";



                var dashboard = [];
                Page.Variables.staticMenuItems.dataSet.forEach(function(m) {
                    // if (m.path === '#/ApplicationCreateEdit' && App.IsUserHasAccess('Access_CreateApplication'))
                    //     dashboard.push(m);
                    // else if (m.path === '#/ApplicationSearch' && App.IsUserHasAccess('Access_ApplicationSearch'))
                    //     dashboard.push(m);
                    // else
                    // if (m.path === '#/SetupLanding' && App.IsUserHasAccess('Support'))
                    //     dashboard.push(m);
                    // else if (m.path === '#/Users' && App.IsUserHasAccess('Support'))
                    //     dashboard.push(m);
                    // else if (m.path === '#/Groups' && App.IsUserHasAccess('Security_Groups'))
                    //     dashboard.push(m);
                    // else if (m.path === '#/Roles' && App.IsUserHasAccess('Support'))
                    //     dashboard.push(m);

                    dashboard.push(m);
                });



                Page.Variables.staticMenuItems.dataSet = dashboard;
                Page.Widgets.staticMenuItemsList1.dataset = [];
                Page.Widgets.staticMenuItemsList1.dataset = Page.Variables.staticMenuItems.dataSet;
            } else {
                //determining the time elapsed since App started in minutes
                const timeElapsedSinceAppStart = moment(new Date()).diff(moment(App.appStartTime), 'minutes');

                if (timeElapsedSinceAppStart < 1) {
                    console.log('Waiting to load permissions...');
                } else {
                    clearInterval(intervalId);

                    //if the active page is not 'ErrorLanding'
                    // if (window.location.hash !== '#/ErrorLanding')
                    //     window.location.href = '#/ErrorLanding';
                }
            }
        }, 10); */
}