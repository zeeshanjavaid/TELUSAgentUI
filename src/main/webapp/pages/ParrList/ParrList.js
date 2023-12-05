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
     * e.g. to get value of text widget named 'username' use following script
     * 'Partial.Widgets.username.datavalue'
     */
};

Partial.CreateClick = function($event, widget) {
    debugger;
    App.Variables.errorMsg.dataSet.dataValue = null;
    App.Variables.successMessage.dataSet.dataValue = null;
    App.Clear();
    Partial.Variables.ParrPageName.dataSet.dataValue = 'CreateParr';
};

Partial.getPaymentArrangementTable1_idOnClick = function($event, widget, row) {



    Partial.Variables.ParrPageName.dataSet.dataValue = 'ParrDetail';
};

App.refreshParrList = function() {

    Partial.Variables.CollectionEntityGetPaymentArrangement.setInput({

        "entityId": Partial.pageParams.entityId,
        'limit': 10,
        'offset': 0
    });
    Partial.Variables.CollectionEntityGetPaymentArrangement.invoke();
};

/*Partial.CollectionEntityGetPaymentArrangementonError = function(variable, data, xhrObj){
	
};*/

Partial.CollectionEntityGetPaymentArrangementonSuccess = function(variable, data) {

    debugger;

};
Partial.getPaymentArrangementTable1Beforedatarender = function(widget, $data, $columns) {
    debugger;
    var parr_amount;
    $data.forEach(function(e) {
        if (e.amount != null)
            parr_amount = (e.amount).toFixed(2);
        var formattedString = parr_amount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        e.amount = formattedString;
    });
};

Partial.Telus_PaginatonPagechange = function($event, $data) {
    debugger;
    Partial.size = $event.pageSize
    Partial.page = $event.pageNumber
    Partial.RefreshData();

};

Partial.RefreshData = function() {
    debugger;
    var offset = Partial.size * (Partial.page - 1);
    Partial.Variables.CollectionEntityGetPaymentArrangement.setInput({
        'limit': Partial.size,
        'offset': offset
    });
    Partial.Variables.CollectionEntityGetPaymentArrangement.invoke();

}