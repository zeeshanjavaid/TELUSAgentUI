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
Partial.onReady = function() {
    /*
     * variables can be accessed through 'Partial.Variables' property here
     * e.g. to get dataSet in a staticVariable named 'loggedInUser' use following script
     * Partial.Variables.loggedInUser.getData()
     *
     * widgets can be accessed through 'Partial.Widgets' property here
     * e.g. to get value of text widget naamed 'username' use following script
     * 'Partial.Widgets.username.datavalue'
     */
    Partial.Variables.teamErrorMessage.dataSet.dataValue = "";
    Partial.Variables.teamSuccessMessage.dataSet.dataValue = "";
};

App.getTeamIndex = function(teamId) {
    debugger;
    let QIndex = 0;

    if (teamId && Partial.Widgets.TeamList.dataset) {
        QIndex = Partial.Widgets.TeamList.dataset.findIndex((QData) => {
            return QData.id === teamId;
        });
    }
    return QIndex;
};


Partial.container5Click = function($event, widget, item, currentItemWidgets) {

    debugger;

    // Partial.Variables.roleId.dataSet.dataValue = item.id;
    //App.getRolePermission(item.id);
};

App.navigateToPageNo = function(selectedItemIndex, pageSize) {

    debugger;
    let pageNo = Math.ceil((selectedItemIndex + 1) / pageSize);


    for (i = 0; i < (pageNo - 1); i++) {
        $('a[name="next"]')[0].click();

    }
    console.log("Navigation to pageno:" + pageNo);

};

App.clickListItemByIndex = function(listName, itemIndex) {

    debugger;
    console.log("List " + listName + " click item: " + itemIndex);
    Partial.Widgets.TeamList.selectItem(itemIndex);
    $('li[listitemindex="' + itemIndex + '"]').click();


};

Partial.teamSearchTextKeyup = function($event, widget) {

    debugger;

    if (widget.datavalue) {
        let results = Partial.allTeamUITemp.filter((item) => {
            return item.name.toLowerCase().includes(widget.datavalue.toLowerCase());
        });

        Partial.Variables.getTeamsAssociatedUsers.dataSet = [];
        Object.assign(Partial.Variables.getTeamsAssociatedUsers.dataSet, results);
    } else {
        Partial.Variables.getTeamsAssociatedUsers.dataSet = [];
        Object.assign(Partial.Variables.getTeamsAssociatedUsers.dataSet, Partial.allTeamUITemp);
    }

};

App.refreshAllTeams = function() {

    debugger;

    Partial.Variables.getTeamsAssociatedUsers.invoke();
}

Partial.TeamListClick = function(widget, $data) {

    debugger;

    Partial.Variables.getManagerNameByTeamId.setInput({
        'teamId': widget.selecteditem.id
    });

    Partial.Variables.getManagerNameByTeamId.invoke();

    App.Variables.TeamPageCommunication.currentPageSize = widget.pagesize;
    console.log("Team List clicked !! " + App.Variables.TeamPageCommunication.currentPageSize);

};
Partial.TeamListSetrecord = function(widget, $data) {

    debugger;

    App.Variables.TeamPageCommunication.currentPageSize = widget.pagesize;

};

Partial.AddTeamButtonClick = function($event, widget) {
    App.addTeams();
};

Partial.getTeamsAssociatedUsersonSuccess = function(variable, data) {

    debugger;
    if (App.Variables.TeamPageCommunication.currentTeamInFocusId) {

        let selectedItem = App.getTeamIndex(App.Variables.TeamPageCommunication.currentTeamInFocusId);
        let pageSize = App.Variables.TeamPageCommunication.currentPageSize;
        console.log("Selected item index:" + selectedItem + " pageSize :" + pageSize);
        App.navigateToPageNo(selectedItem, pageSize);
        App.clickListItemByIndex('TeamList', (selectedItem) % pageSize);
    }
};

Partial.getManagerNameByTeamIdonSuccess = function(variable, data) {

    App.Variables.getManagerSelected.datsSet = data;



};
Partial.container4Load = function(widget, item, currentItemWidgets) {
    debugger;
};
Partial.containerTeamsLoad = function(widget, item, currentItemWidgets) {
    debugger;
};