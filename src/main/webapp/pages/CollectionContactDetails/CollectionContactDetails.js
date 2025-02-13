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

function messageTimeout() {
    App.Variables.successMessage.dataSet.dataValue = null;
    App.Variables.errorMsg.dataSet.dataValue = null;
}

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
Partial.expandedRowDataTable1Beforerowupdate = function($event, widget, row, options) {
    debugger;
    console.log(" New Row Details:", row.comments); // Log the row details
    // Find the original row data using a unique identifier, e.g., 'contactId'
    // Find the original row data using a unique identifier, e.g., 'contactId'
    const originalRow = Partial.Variables.expandedRowData.dataSet.find(original => original.contactId === row.contactId);

    if (originalRow) {
        // Compare the 'comments' field
        if (originalRow.comments === row.comments && originalRow.contactForNotices === row.contactForNotices) {
            App.notify("No changes detected in the 'comments' field.", "error");
            console.log("No changes detected in the 'comments' field.");
            App.Variables.errorMsg.dataSet.dataValue = "No Changes to Save";
            App.Variables.successMessage.dataSet.dataValue = null;
            setTimeout(messageTimeout, 5000);
            return;
        } else {
            Partial.Variables.updateEntityContact.setInput({
                "id": row.contactId,
                "CollectionContactUpdate": {
                    'id': row.contactId,
                    'channel': {
                        'originatorAppId': "Internal",
                        'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
                    },
                    'comment': row.comments,
                    'notificationIndicator': row.contactForNotices
                }
            });

            //Invoke POST createDispute service
            Partial.Variables.updateEntityContact.invoke();
        }
    }
};
Partial.expandedRowDataTable1Rowdelete = function($event, widget, row) {
    debugger;
    var today = new Date();
    Partial.Variables.updateEntityContact.setInput({
        "id": row.contactId,
        "CollectionContactUpdate": {
            'id': row.contactId,
            'channel': {
                'originatorAppId': "Internal",
                'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
            },
            'validFor': {
                'endDateTime': today,
            }
        }
    });

    //Invoke POST createDispute service
    Partial.Variables.updateEntityContact.invoke();
};

Partial.updateEntityContactonError = function(variable, data, xhrObj) {
    debugger;
    if (data.validFor.endDateTime != null) {
        App.Variables.successMessage.dataSet.dataValue = null;
        App.Variables.errorMsg.dataSet.dataValue = "Contact Removal Failed";
    } else {
        App.Variables.successMessage.dataSet.dataValue = null;
        App.Variables.errorMsg.dataSet.dataValue = "Contact Update Failed";

    }
    setTimeout(messageTimeout, 5000);
};
Partial.updateEntityContactonSuccess = function(variable, data) {
    debugger;
    if (data.validFor.endDateTime != null) {
        App.Variables.successMessage.dataSet.dataValue = "Contact Removed Successfully";
        App.Variables.errorMsg.dataSet.dataValue = null;
    } else {
        App.Variables.successMessage.dataSet.dataValue = "Contact Updated Successfully";
        App.Variables.errorMsg.dataSet.dataValue = null;

    }
    setTimeout(messageTimeout, 5000);
    if (Partial.Variables.expandedRowData.dataSet.length === 0) {
        App.refreshContactList();
    }
};
Partial.expandedRowDataTable1_commentsOnKeydown = function($event, widget, row) {
    debugger;
    if (event.key === "Enter") {
        event.stopPropagation(); // Prevents triggering the row update
        return false;
    }
};