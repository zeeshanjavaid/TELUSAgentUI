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

function groupByFullName(data) {
    const groupedData = {};

    data.forEach(item => {
        const key = `${item.firstName} ${item.lastName}`;
        if (!groupedData[key]) {
            groupedData[key] = {
                firstName: item.firstName,
                lastName: item.lastName,
                details: [] // Store grouped details
            };
        }
        groupedData[key].details.push(item);
    });

    return Object.values(groupedData); // Convert grouped object to array
}


Partial.digitalContactBtnClick = function($event, widget) {
    // to make buttons selected
    $("#digitalBtn").css("background-color", "#4B286D");
    $("#digitalBtn").css("color", "white");
    $("#mailingBtn").css("background-color", "white");
    $("#mailingBtn").css("color", "#4B286D");
    Partial.Variables.ShowHideCreateBtnGrid.dataSet.dataValue = true;
    // display TO-DO table and hide Completed table
    App.refreshContactList();
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
    Partial.Variables.ShowHideCreateBtnGrid.dataSet.dataValue = false;
    // display Completed table and hide TO-DO table
    App.refreshContactList();

    $('#mailingTableGrid').show();
    $('#buttonsLayoutGrid').hide();
    $('#digitalTableGrid').hide();



};


Partial.CreateButtonClick = function($event, widget) {
    App.ClearContacts();
    Partial.Variables.ContactPageName.dataSet.dataValue = 'CreateDigitalContact';

};
Partial.getEntityContactsTable1_customRowAction = function($event, row) {
    debugger;
    Partial.Variables.ContactPageName.dataSet.dataValue = 'UpdateDigitalContact';
    App.rowDataValues(row);
};


App.refreshContactList = function() {
    debugger;
    Partial.Widgets.getCustomerContactsTable.spinner.show = true;
    Partial.Variables.getCollectionEntityContacts.setInput({
        "id": Partial.pageParams.entityId
    });
    Partial.Variables.getCollectionEntityContacts.invoke();
}
Partial.getCustomerContactsTable_OnRowexpand = function($event, widget, row, $data) {
    debugger;
    App.showRowExpansionCustomer(row, $data);
};

Partial.getCollectionEntityContactsonSuccess = function(variable, data) {
    const staticData = [{
            "fName": "John",
            "lName": "Wang",
            "columnA": "email@test.com",
            "columnB": "",
            "columnC": false,
            "columnD": "French",
            "columnE": "test comments"
        },
        {
            "fName": "John",
            "lName": "Wang",
            "columnA": "",
            "columnB": 4164164160,
            "columnC": false,
            "columnD": "French",
            "columnE": "test comments"
        },
        {
            "fName": "Test",
            "lName": "Name",
            "columnA": "test@email.com",
            "columnB": "",
            "columnC": false,
            "columnD": "French",
            "columnE": "test comments"
        }
    ];
    debugger;
    // Group the data and bind it directly
    App.Variables.testContactsdata1.dataSet = groupByFullName(Partial.Variables.getCollectionEntityContacts.dataSet.digitalCustomerContacts);
    Partial.Widgets.getCustomerContactsTable.spinner.show = false;


};