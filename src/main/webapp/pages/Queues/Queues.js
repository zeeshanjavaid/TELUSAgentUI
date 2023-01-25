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
Page.queueOperation = "GET";

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

            if (!App.IsUserHasAccess('Setup_Queues')) {
                window.location.href = "#/ErrorLanding";
            } else {
                document.getElementsByTagName("html")[0].style.visibility = "visible";

                document.querySelector("div[name='AddQueueContainer']").addEventListener("queue_ev", (ev) => {
                    if (Page.queueOperation !== ev.detail.operation)
                        Page.queueOperation = ev.detail.operation;
                    else if (ev.detail.operation === "ADD")
                        Page.queueOperation = "FORCE_REFRESH";
                });

                Page.Variables.showAdminMiddleContainer.dataSet.dataValue = false;
                //Page.Widgets.OMConditionBuilder_test1.Configuration = Page.Variables.queueConfigMV.dataSet.conditions;
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


Page.adminGeneralNameClick = function($event, widget, item, currentItemWidgets) {


    if (item.name == "Settings") {
        //
        Page.Variables.showAdminMiddleContainer.dataSet.dataValue = false;
        if (Page.Widgets.PropertiesContainer.widget.Variables.propertiesErrorMessage.dataSet.dataValue !== undefined) {
            Page.Widgets.PropertiesContainer.widget.Variables.propertiesErrorMessage.dataSet.dataValue = "";
        }
        if (Page.Widgets.PropertiesContainer.widget.Variables.propertiesSuccessMessage.dataSet.dataValue !== undefined) {
            Page.Widgets.PropertiesContainer.widget.Variables.propertiesSuccessMessage.dataSet.dataValue = "";
        }
        Page.Widgets.PropertiesContainer.widget.Variables.TDUSFawbPropertySourceData.invoke();

    } else {
        Page.Variables.showAdminMiddleContainer.dataSet.dataValue = true;
    }

    if (Page.Widgets.AddLookupContainer.widget.Variables.msg !== undefined) {
        Page.Widgets.AddLookupContainer.widget.Variables.msg = "";
    }
    if (Page.Widgets.Lookupcontainer.widget.Variables.msg !== undefined) {
        Page.Widgets.Lookupcontainer.widget.Variables.msg = "";
    }

};

Page.adminMidContaineranchorClick = function($event, widget) {
    if (Page.Variables.showAdminMiddleContainer.dataSet.dataValue == true) {
        Page.Variables.showAdminMiddleContainer.dataSet.dataValue = false;
    } else {
        Page.Variables.showAdminMiddleContainer.dataSet.dataValue = true;
    }
};

Page.GeneralpanelExpand = function($event, widget) {
    Page.Variables.isExpandedGeneral.dataSet.dataValue = true;
};

Page.GeneralpanelCollapse = function($event, widget) {
    Page.Variables.isExpandedGeneral.dataSet.dataValue = false;
};
Page.panel1Expand = function($event, widget) {
    Page.Variables.isExpandedUM.dataSet.dataValue = true;
};

Page.panel1Collapse = function($event, widget) {
    Page.Variables.isExpandedUM.dataSet.dataValue = false;

};
Page.adminGeneralcontainerClick = function($event, widget, item, currentItemWidgets) {

    App.Variables.errorMessage.dataSet.dataValue = "";
    App.Variables.successMessage.dataSet.dataValue = "";
    App.Variables.roleErrorMessage.dataSet.dataValue = "";
    App.Variables.roleSuccessMessage.dataSet.dataValue = "";
    App.Variables.groupErrorMessage.dataSet.dataValue = "";
    App.Variables.groupSuccessMessage.dataSet.dataValue = "";

    if (Page.Variables.tempAdminVariable.dataSet.dataValue !== "" && Page.Variables.tempAdminVariable.dataSet.dataValue == item.name) {

        Page.ITEM = document.querySelectorAll(".admin-general-list  .list-group-item");
        for (let i = 0; i < Page.ITEM.length; i++) {
            if (Page.ITEM[i].firstElementChild.innerText == Page.Variables.tempAdminVariable.dataSet.dataValue)
                Page.ITEM[i].classList.add("active");

        }
    }
    Page.Variables.tempAdminVariable.dataSet.dataValue = item.name;
    if ((document.querySelector(".admin-um-list  .list-group-item.active")) !== null) {
        (document.querySelector(".admin-um-list  .list-group-item.active")).classList.remove("active");
    }
    if (item.name == "Settings") {
        Page.Variables.showAdminMiddleContainer.dataSet.dataValue = false;
        if (Page.Widgets.PropertiesContainer.widget.Variables.propertiesErrorMessage.dataSet.dataValue !== undefined) {
            Page.Widgets.PropertiesContainer.widget.Variables.propertiesErrorMessage.dataSet.dataValue = "";
        }
        if (Page.Widgets.PropertiesContainer.widget.Variables.propertiesSuccessMessage.dataSet.dataValue !== undefined) {
            Page.Widgets.PropertiesContainer.widget.Variables.propertiesSuccessMessage.dataSet.dataValue = "";
        }
        Page.Widgets.PropertiesContainer.widget.Variables.TDUSFawbPropertySourceData.invoke();
    } else {
        Page.Variables.showAdminMiddleContainer.dataSet.dataValue = true;
    }
    if (Page.Widgets.AddLookupContainer.widget.Variables.msg !== undefined) {
        Page.Widgets.AddLookupContainer.widget.Variables.msg = "";
    }
    if (Page.Widgets.Lookupcontainer.widget.Variables.msg !== undefined) {
        Page.Widgets.Lookupcontainer.widget.Variables.msg = "";
    }

    Page.Variables.sectionName.dataSet.dataValue = item.name;
    //
    if (item.name == "Queues") {
        Page.Variables.queueConfigMV.dataSet = [];
        Page.Variables.queueConfigMV.setData({
            'conditions': {
                'fields': Page.Variables.queueFieldsMV.dataSet,
                'rules': [] //Page.Variables.queueRulesMV.dataSet.filter
            },
            'displayConfig': {
                'fielddataset': [],
                "savedconfig": {
                    "searchableFields": [],
                    "sortableFields": []
                }
            }
        });
        if (Page.Widgets.queueConditions !== undefined) {
            Page.Widgets.queueConditions.Configuration = Page.Variables.queueConfigMV.dataSet.conditions;
        }
    }
};
Page.adminUMlNamecontainerClick = function($event, widget, item, currentItemWidgets) {

    Page.Widgets.UserContainer.Variables.userMV.dataSet = [];
    App.Variables.errorMessage.dataSet.dataValue = "";
    App.Variables.successMessage.dataSet.dataValue = "";
    App.Variables.roleErrorMessage.dataSet.dataValue = "";
    App.Variables.roleSuccessMessage.dataSet.dataValue = "";
    App.Variables.groupErrorMessage.dataSet.dataValue = "";
    App.Variables.groupSuccessMessage.dataSet.dataValue = "";

    if (item.name == "Groups") {
        App.groupsPageReload();
    }
    if (item.name == "Roles") {
        App.rolesPageReload();
    }

    if (item.name == "Users") {
        if (Page.Variables.showAdminMiddleContainer.dataSet.dataValue == false)
            Page.Variables.showAdminMiddleContainer.dataSet.dataValue = true;
        App.usersPageReload();
    }

    if (item.name == "Users") {
        if (Page.Variables.showAdminMiddleContainer.dataSet.dataValue == true)
            Page.Variables.showAdminMiddleContainer.dataSet.dataValue = false;
    }


    if (Page.Variables.tempUserMVariable.dataSet.dataValue !== "" && Page.Variables.tempUserMVariable.dataSet.dataValue == item.name) {

        Page.ITEMUM = document.querySelectorAll(".admin-um-list  .list-group-item");
        for (let i = 0; i < Page.ITEMUM.length; i++) {
            if (Page.ITEMUM[i].firstElementChild.innerText == Page.Variables.tempUserMVariable.dataSet.dataValue)
                Page.ITEMUM[i].classList.add("active");

        }
    }

    Page.Variables.tempUserMVariable.dataSet.dataValue = item.name;
    if ((document.querySelector(".admin-general-list  .list-group-item.active")) !== null) {
        (document.querySelector(".admin-general-list  .list-group-item.active")).classList.remove("active");
    }
    Page.Variables.sectionName.dataSet.dataValue = "";
    if (item.name !== "Users") {
        Page.Variables.showAdminMiddleContainer.dataSet.dataValue = true;
    }
    Page.Variables.sectionName.dataSet.dataValue = item.name;

};
Page.button1Click = function($event, widget) {
    //
    var conditions = Page.Widgets.queueConditions.getRules();
    //console.log(conditions);
};

Page.button2Click = function($event, widget) {
    //
    App.Widgets.AddLookupContainer.Variables.TDUSLookupEntityData.invoke();

    //App.activePage.Variables.TDUSLookupEntityData.invoke();
    //App.Widgets.container2.Variables.TDUSLookupEntitysData.invoke();

};

Page.usermenucontainerMouseleave = function($event, widget) {
    /* App.Variables.showUserMenu.dataSet.dataValue = false;*/
};

CallParent1 = function() {
    alert(" Parent window Alert");
}

CallParent = function(input) {
    Partial.Variables.TDUSLookupData.invoke();
    Partial.Widgets.AddLookupContainer.Variables.TDUSLookupEntitysData.invoke();
    alert("check");
};
Page.GeneralpanelActionsclick = function($event, action, widget) {

};