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
    $("#digitalBtn").css("background-color", "#4B286D");
    $("#digitalBtn").css("color", "white");
    $('#mailingTableGrid').hide();
};

Partial.digitalContactBtnClick = function($event, widget) {
    // to make buttons selected
    $("#digitalBtn").css("background-color", "#4B286D");
    $("#digitalBtn").css("color", "white");
    $("#mailingBtn").css("background-color", "white");
    $("#mailingBtn").css("color", "#4B286D");

    // display TO-DO table and hide Completed table
    $('#digitalTableGrid').show();
    $('#buttonsLayoutGrid').show();
    $('#mailingTableGrid').hide();


};

Partial.mailingContactBtnClick = function($event, widget) {
    // to make buttons selected
    $("#mailingBtn").css("background-color", "#4B286D");
    $("#mailingBtn").css("color", "white");
    $("#digitalBtn").css("background-color", "white");
    $("#digitalBtn").css("color", "#4B286D");

    // display Completed table and hide TO-DO table
    $('#mailingTableGrid').show();
    $('#digitalTableGrid').hide();
    $('#buttonsLayoutGrid').hide();

};


Partial.CreateButtonClick = function($event, widget) {
    Partial.Variables.ContactPageName.dataSet.dataValue = 'CreateDigitalContact';
};
Partial.getEntityContactsTable1_customRowAction = function($event, row) {
    Partial.Variables.ContactPageName.dataSet.dataValue = 'UpdateDigitalContact';
};