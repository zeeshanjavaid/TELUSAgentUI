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

function groupByAttributes(data) {
    const groupedData = {};

    data.forEach(item => {
        // Create a unique key for grouping using the specified fields
        item.entityId = Partial.pageParams.entityId;
        const key = `${item.firstName} ${item.jobTitle} ${item.lastName} ${item.prefLang} ${item.salutation} ${item.telusContacts}`;

        if (!groupedData[key]) {
            groupedData[key] = {
                firstName: item.firstName,
                jobTitle: item.jobTitle,
                lastName: item.lastName,
                prefLang: item.prefLang,
                salutation: item.salutation,
                telusContacts: item.telusContacts,
                details: [] // Store grouped details
            };
        }

        groupedData[key].details.push(item);
    });

    return Object.values(groupedData); // Convert grouped object to array
}

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
    //App.refreshContactList();
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
    //App.refreshContactList();

    $('#mailingTableGrid').show();
    $('#buttonsLayoutGrid').hide();
    $('#digitalTableGrid').hide();



};


Partial.CreateButtonClick = function($event, widget) {
    //App.ClearContacts();
    Partial.Variables.ContactPageName.dataSet.dataValue = 'CreateDigitalContact';

};
Partial.getEntityContactsTable1_customRowAction = function($event, row) {
    debugger;
    Partial.Variables.ContactPageName.dataSet.dataValue = 'UpdateDigitalContact';
    App.rowDataValues(row);
};


App.refreshContactList = function() {
    debugger;
    //Partial.Widgets.getCollectionContactsTable.spinner.show = true;
    //Partial.Widgets.getCustomerContactsTable.spinner.show = true;
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
    debugger;
    // Group the data and bind it directly
    if (Partial.Variables.getCollectionEntityContacts.dataSet.digitalCustomerContacts != null && Partial.Variables.getCollectionEntityContacts.dataSet.digitalCustomerContacts.length > 0) {
        App.Variables.customerContactsdata.dataSet = groupByFullName(Partial.Variables.getCollectionEntityContacts.dataSet.digitalCustomerContacts);
        Partial.Variables.getCollectionEntityContacts.dataSet.digitalCustomerContacts = App.Variables.customerContactsdata.dataSet;
    }
    if (Partial.Variables.getCollectionEntityContacts.dataSet.digitalCollectionContacts != null && Partial.Variables.getCollectionEntityContacts.dataSet.digitalCollectionContacts.length > 0) {
        App.Variables.collectionContactsdata.dataSet = groupByAttributes(Partial.Variables.getCollectionEntityContacts.dataSet.digitalCollectionContacts);
        Partial.Variables.getCollectionEntityContacts.dataSet.digitalCollectionContacts = App.Variables.collectionContactsdata.dataSet;
    }
    //Partial.Widgets.getCustomerContactsTable.spinner.show = false;
    //Partial.Widgets.getCollectionContactsTable.spinner.show = false;
};

Partial.getCollectionEntityContactsonError = function(variable, data, xhrObj) {
    //Partial.Widgets.getCustomerContactsTable.spinner.show = false;
    //Partial.Widgets.getCollectionContactsTable.spinner.show = false;
};
Partial.getCollectionContactsTable_OnRowexpand = function($event, widget, row, $data) {
    debugger;
    App.showRowExpandedCollectionContact(row, $data);
};