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

    App.showRowExpansionHistory = function(row, data) {

        debugger;

        Partial.Variables.getActivityLogPerRef.setInput({
            'collectionEntityId': row.collectionEntity.id,
            'relatedBusinessEntityId': row.relatedBusinessEntityId
        });
        Partial.Variables.getActivityLogPerRef.invoke();
    }
};

Partial.getActivityLogPerRefonSuccess = function(variable, data) {
    debugger;
};
Partial.expandedRowDataTable1Beforedatarender = function(widget, $data, $columns) {
    debugger;
    console.log(moment);


    // Ensure columns exist
    if ($columns) {
        $columns.type.show = !$data.some(row => row.relatedBusinessEntitySubType === 'CALL-OB');
    } else {
        console.error("Columns not found.");
    }
};