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
};

Partial.CreateDisputeClick = function($event, widget) {
    App.Variables.errorMsg.dataSet.dataValue = "";
    App.Variables.successMessage.dataSet.dataValue = "";
    App.activePage.Widgets.CreateDisputePanel.Widgets.chargeTypeDropDown.datavalue = ""
    App.activePage.Widgets.CreateDisputePanel.Widgets.selectedDisputeBan.datavalue = "";
    App.activePage.Widgets.CreateDisputePanel.Widgets.exclusionDropdown.datavalue = "";
    App.activePage.Widgets.CreateDisputePanel.Widgets.disputeAmt.datavalue = "";
    App.activePage.Widgets.CreateDisputePanel.Widgets.chargeTypeDropDown.datavalue = "";
    App.activePage.Widgets.CreateDisputePanel.Widgets.reasonDropdown.datavalue = "";
    App.activePage.Widgets.CreateDisputePanel.Widgets.productsDropdown.datavalue = "";
    App.activePage.Widgets.CreateDisputePanel.Widgets.AdjustmentToDate.datavalue = "";
    App.activePage.Widgets.CreateDisputePanel.Widgets.custEmailText.datavalue = "";
    App.activePage.Widgets.CreateDisputePanel.Widgets.CreateCommentsDispute.datavalue = "";
    App.activePage.Widgets.CreateDisputePanel.Widgets.AssignedDisputePrime.datavalue = "";
    Partial.Variables.DisputePageName.dataSet.dataValue = 'CreateDispute';
};

Partial.getdisputeTable1_idOnClick = function($event, widget, row) {
    App;
    debugger;
    App.Variables.disputeIdAppVar.dataSet.dataValue = row.id;
    Partial.Variables.DisputePageName.dataSet.dataValue = 'DisputeDetail';
};


App.refreshDisputeList = function() {
    Partial.Variables.getAllDisputesList.setInput({
        "entityId": Partial.pageParams.entityId,
        'limit': 10,
        'offset': 0
    });
    Partial.Variables.getAllDisputesList.invoke();
};
Partial.getdisputeTable1Beforedatarender = function(widget, $data, $columns) {
    debugger;
    var dis_amount;
    $data.forEach(function(e) {
        if (e.disputeAmount != null)
            dis_amount = (e.disputeAmount).toFixed(2);
        var formattedString = dis_amount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        e.disputeAmount = formattedString;
    });

};

Partial.Telus_PaginatonPagechangeForDisputeList = function($event, $data) {
    debugger;
    Partial.size = $event.pageSize
    Partial.page = $event.pageNumber
    Partial.RefreshData();

};

Partial.RefreshData = function() {
    debugger;
    var offset = Partial.size * (Partial.page - 1);
    Partial.Variables.getAllDisputesList.setInput({
        'limit': Partial.size,
        'offset': offset
    });
    Partial.Variables.getAllDisputesList.invoke();

}