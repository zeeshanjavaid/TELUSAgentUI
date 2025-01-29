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
    debugger;
    App.showRowExpandedCollectionContact = function(row, data) {
        debugger;
        console.log("Row Details:", row.details); // Log the row details
        Partial.Variables.expandedRowData.dataSet = row.details; // Assign the data
        /* Initialize isEditable for each row
        Partial.Variables.expandedRowData.dataSet.forEach(row => {
            row.isEditable = false; // Ensure all rows start as read-only
        });*/

        console.log("Assigned DataSet:", Partial.Variables.expandedRowData.dataSet); // Log the updated dataset
    }
};

Partial.collectionContactExpansionTable_contactForNoticesOnChange = function($event, widget, row) {
    debugger;
    App.Variables.successMessage.dataSet.dataValue = "Updated Successfully";
    App.Variables.errorMsg.dataSet.dataValue = null;
};
Partial.collectionContactExpansionTable_customRow1Action = function($event, row) {
    debugger;
    Partial.Widgets.editContactDialog.open();
};
Partial.collectionContactExpansionTable_customRow2Action = function($event, row) {
    debugger;
    Partial.Widgets.invalidateContactDialog.open();
};

//close assigned person
Partial.closeAction_NoClick = function($event, widget) {
    Partial.Widgets.invalidateContactDialog.close();
    Partial.Widgets.editContactDialog.close();
};