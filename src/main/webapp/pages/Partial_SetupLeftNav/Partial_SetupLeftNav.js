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

// Partial.MenuData = "";
/* perform any action on widgets/variables within this block */
Partial.onReady = function() {
    /*
     * variables can be accessed through 'Partial.Variables' property here
     * e.g. to get dataSet in a staticVariable named 'loggedInUser' use following script
     * Partial.Variables.loggedInUser.getData()
     *
     * widgets can be accessed through 'Partial.Widgets' property here
     * e.g. to get value of text widget named 'username' use following script
     * 'Partial.Widgets.username.datavalue'
     */
    // Partial.redirect = false;

    loadSetupMenus();
};

Partial.listItemMouseover = function($event, widget, item, currentItemWidgets) {
    $(widget.nativeElement.lastElementChild).removeClass('collapse');
};
Partial.listItemMouseout = function($event, widget, item, currentItemWidgets) {
    $(widget.nativeElement.lastElementChild).addClass('collapse');
};

Partial.hideListClick = function($event, widget) {
    $('.icons-grid').addClass('collapse');
    $('.app-left-panel').removeClass('width-2-per');
    setTimeout(function() {
        $('.app-content-column').removeClass('width-98-per');
        $('.nav-grid').removeClass('collapse');
        $('.app-left-panel').addClass('min-width-278px');
    }, 100);
};


Partial.hideNavClick = function($event, widget) {

    $('.nav-grid').addClass('collapse');
    $('.icons-grid').removeClass('collapse');
    $('.app-left-panel').addClass('width-2-per');
    $('.app-left-panel').removeClass('min-width-278px');
    $('.app-content-column').addClass('width-98-per');
    $('.collapse-list .activeClass').parents('.app-list-item')[0].classList.add('activeClass');
};

Partial.addActiveClass = function(pageName) {


    var isMenuActivated = false;
    Partial.itemIndex = "";

    Partial.MenuData.forEach(function(d, i) {
        if (d.url.substring(d.url.lastIndexOf("/") + 1) === pageName) {
            d.class = "activeClass";
            Partial.itemIndex = i;
            isMenuActivated = true;
        } else {
            if (d.children) {
                d.children.forEach(function(c) {
                    if (c.url.substring(c.url.lastIndexOf("/") + 1) === pageName) {
                        d.class = "activeClass";
                        c.class = "activeClass"
                        Partial.itemIndex = i;
                        isMenuActivated = true;
                    } else if (document.getElementById(c.identifier) != null) {
                        Partial.itemIndex = i;
                        c.class = "activeClass";
                        d.class = "activeClass";
                        isMenuActivated = true;
                    } else if ((pageName === "DocumentTemplate" || pageName === "DocumentTemplates" || pageName === "ManageTemplateImages") && (c.name === "Documents")) {
                        c.class = "activeClass";
                        d.class = "activeClass";
                        isMenuActivated = true;
                    }
                });
            }
        }
        $('.activeClass').addClass('active');
    });

    if (isMenuActivated) {
        Partial.Variables.SetupLeftNavData.setData(Partial.MenuData);
    }

    return isMenuActivated;
}


//  On this action, app will redirect to the first link which is avalaible in the side nav
Partial.navigateToFirstLink = function() {
    var dataSet = Partial.Variables.SetupLeftNavData.dataSet;
    if (dataSet && dataSet.length > 0) {
        if (dataSet[0].children &&
            dataSet[0].children.length > 0) {
            var url = dataSet[0].children[0].url;
            if (url) {
                window.location.href = url;
            }
        }
    } else {
        Partial.redirect = true;
    }
};

function loadSetupMenus() {
    const intervalId = setInterval(function() {
        if (App.permissionsLoaded) {
            clearInterval(intervalId);
            console.log('Permissions loaded...');

            let permissionedItems = [];
            Partial.Variables.SetupLeftNavData.dataSet.forEach(function(m) {
                let parentMenuItem = {};
                Object.assign(parentMenuItem, m);
                if (m.code === 'ApplicationProcessing') {
                    //for individual children of this parent
                    if (m.children && m.children.length > 0) {
                        delete parentMenuItem.children;
                        parentMenuItem.children = [];
                        m.children.forEach((child) => {
                            if (child.code === 'SiteProperties' && App.IsUserHasAccess('Support'))
                                parentMenuItem.children.push(child);
                            else if (child.code === 'Organizations')
                                parentMenuItem.children.push(child);
                            else if (child.code === 'Products' /*&& App.IsUserHasAccess('Setup_Products')*/ ) //TODO: uncomment when permission is available
                                parentMenuItem.children.push(child);

                        });

                        //the parent menu
                        if (parentMenuItem.children.length > 0)
                            permissionedItems.push(parentMenuItem);
                    }
                } else if (m.code === 'SystemAdministration') {
                    //for individual children of this parent
                    if (m.children && m.children.length > 0) {
                        delete parentMenuItem.children;
                        parentMenuItem.children = [];
                        m.children.forEach((child) => {
                            // if (child.code === 'DomainValues' && App.IsUserHasAccess('Setup_DomainValues'))
                            if (child.code === 'DomainValues' && App.IsUserHasAccess('Support'))
                                parentMenuItem.children.push(child);
                            else if (child.code === 'Queues' && App.IsUserHasAccess('Setup_Queues'))
                                parentMenuItem.children.push(child);
                            else if (child.code === 'LookupFiles' /*&& App.IsUserHasAccess('Setup_LookupFile')*/ ) //TODO: uncomment when permission is available
                                parentMenuItem.children.push(child);
                        });

                        //the parent menu
                        if (parentMenuItem.children.length > 0)
                            permissionedItems.push(parentMenuItem);
                    }
                } else if (m.code === 'SystemMaintenance') {
                    //for individual children of this parent
                    if (m.children && m.children.length > 0) {
                        delete parentMenuItem.children;
                        parentMenuItem.children = [];
                        m.children.forEach((child) => {
                            if (child.code === 'Timers' && App.IsUserHasAccess('Setup_TimerSettings'))
                                parentMenuItem.children.push(child);
                        });

                        //the parent menu
                        if (parentMenuItem.children.length > 0)
                            permissionedItems.push(parentMenuItem);
                    }
                } else if (m.code === 'Auditing') {
                    //for individual children of this parent
                    if (m.children && m.children.length > 0) {
                        delete parentMenuItem.children;
                        parentMenuItem.children = [];
                        m.children.forEach((child) => {
                            if (child.code === 'AccessLog' && App.IsUserHasAccess('Setup_AccessLogs'))
                                parentMenuItem.children.push(child);
                            else if (child.code === 'Entities' && App.IsUserHasAccess('Setup_Entities'))
                                parentMenuItem.children.push(child);
                            else if (child.code === 'UserSession' && App.IsUserHasAccess('Setup_UserSession'))
                                parentMenuItem.children.push(child);
                        });

                        //the parent menu
                        if (parentMenuItem.children.length > 0)
                            permissionedItems.push(parentMenuItem);
                    }
                }
            });

            Partial.Variables.SetupLeftNavData.dataSet = permissionedItems;
            Partial.Widgets.setup.dataset = [];
            Partial.Widgets.setup.dataset = Partial.Variables.SetupLeftNavData.dataSet


            //active menu factoring logic
            Partial.MenuData = Partial.Variables.SetupLeftNavData.dataSet;
            var traversedPageNames = "";
            var menuActivated = Partial.addActiveClass(App.activePageName);
            if (!menuActivated) {
                if (!!App.getTraversedPageNames) {
                    var traversedPageNames = App.getTraversedPageNames();
                }
                if (traversedPageNames !== "") {
                    var traversedPageNamesArr = traversedPageNames.split("|");
                    for (var i = traversedPageNamesArr.length - 1; i >= 0; i--) {
                        if (Partial.addActiveClass(traversedPageNamesArr[i])) {
                            break;
                        }
                    }
                }
            }
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