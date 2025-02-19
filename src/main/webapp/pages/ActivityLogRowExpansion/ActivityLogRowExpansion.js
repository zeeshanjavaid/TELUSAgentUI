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
    //change data or set values here
};
Partial.expandedRowDataTable1Beforedatarender = function(widget, $data, $columns) {
    //control table column show or hide here
    debugger;
    $data.forEach((item, index) => {
        console.log(`Index: ${index}, relatedBusinessEntitySubType:`, item.relatedBusinessEntitySubType);
        if (item.additionalCharacteristics != undefined) {
            item.additionalCharacteristics.forEach(char => {
                if (char.name === 'cssRequest') {
                    const emails = [];
                    debugger;
                    Object.keys(char.value).forEach(key => {
                        if (key.startsWith("Contact_") && key.endsWith("_Email")) {
                            console.log(`${key}:`, char.value[key]);
                            emails.push(char.value[key]); // Collect emails
                        }
                    });
                    if (emails.length > 0) {
                        console.log("Concatenated Emails:\n" + emails.join("\n"));
                        $columns.email.show = true;
                        item.email = emails.join("\n");
                    }
                }
                if (char.name === 'cssResponse') {
                    debugger;
                    console.log("Message:", char.value.message);
                }
                if (char.name === 'EmailAddress') {
                    //                if (item.relatedBusinessEntitySubType === 'EM-IN' && char.name === 'EmailAddress') {
                    debugger;
                    console.log("Email Address:", char.value);
                    $columns.email.show = true;
                    item.email = char.value; // + ' ' + index;
                }
                if (item.relatedBusinessEntitySubType === 'CALL-OB' && char.name === 'PhoneNumber') {
                    debugger;
                    console.log("Phone Number:", char.value);
                    $columns.phoneNumber.show = true;
                    item.phoneNumber = char.value; // + ' ' + index;
                }
            });
        }
    });
    debugger;
    /* Ensure columns exist
    if ($columns) {
        $columns.type.show = !$data.some(row => row.relatedBusinessEntitySubType === 'CALL-OB');
    } else {
        console.error("Columns not found.");
    }*/
};