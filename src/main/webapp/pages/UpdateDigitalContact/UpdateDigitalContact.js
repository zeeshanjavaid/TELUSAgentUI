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

    App.rowDataValues = function(row) {
        if (!(row.sourceOfContact == 'FAWB')) {
            Partial.Widgets.TELUSContactsSelect.disabled = true;
            Partial.Widgets.TITLESelect.disabled = true;
            Partial.Widgets.firstName.disabled = true;
            Partial.Widgets.lastName.disabled = true;
            Partial.Widgets.emailText.disabled = true;
            Partial.Widgets.cellPhone.disabled = true;
            Partial.Widgets.ext.disabled = true;
            Partial.Widgets.workNo.disabled = true;
            Partial.Widgets.fax.disabled = true;
            Partial.Widgets.comments.disabled = true;
        }

        Partial.Widgets.contactID.caption = row.contactId;
        Partial.Widgets.TELUSContactsSelect.datavalue = row.telusContacts ? 'Y' : 'N';
        Partial.Widgets.dataSource.caption = row.sourceOfContact;
        Partial.Widgets.TITLESelect.datavalue = row.title;
        Partial.Widgets.firstName.datavalue = row.firstName;
        Partial.Widgets.lastName.datavalue = row.lastName;
        Partial.Widgets.emailText.datavalue = row.email;
        Partial.Widgets.EmailForNoticesSelect.datavalue = row.contactForNotices ? 'Y' : 'N';
        Partial.Widgets.cellPhone.datavalue = row.mobileNumber;
        Partial.Widgets.ext.datavalue = '';
        Partial.Widgets.workNo.datavalue = row.workNumber;
        Partial.Widgets.fax.datavalue = '';
        Partial.Widgets.comments.datavalue = '';

    }
};

function messageTimeout() {
    App.Variables.successMessage.dataSet.dataValue = null;
    App.Variables.errorMsg.dataSet.dataValue = null;
}

function validateEmail(email) {
    return email.match(/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/);
};


Partial.CancelClick = function($event, widget) {
    Partial.Variables.ContactPageName.dataSet.dataValue = 'Contact';
};


Partial.updateContact = function($event, widget) {
    Partial.Widgets.emailText._datavalue;
    if (Partial.Widgets.TELUSContactsSelect.datavalue == "" || Partial.Widgets.TELUSContactsSelect.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Telus Contact is mandatory";
    } else if (Partial.Widgets.EmailForNoticesSelect.datavalue == "" || Partial.Widgets.EmailForNoticesSelect.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Email for Notices is mandatory";
    } else if (Partial.Widgets.firstName.datavalue == "" || Partial.Widgets.firstName.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "First Name is mandatory";
    } else if (Partial.Widgets.lastName.datavalue == "" || Partial.Widgets.lastName.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Last Name is mandatory";
    } else if (Partial.Widgets.EmailForNoticesSelect.datavalue == 'Y' && (Partial.Widgets.emailText._datavalue == "" || Partial.Widgets.emailText._datavalue == undefined)) {
        App.Variables.errorMsg.dataSet.dataValue = "Please provide the Email";
    } else if ((!Partial.Widgets.emailText._datavalue == "" || !Partial.Widgets.emailText._datavalue == undefined) && !validateEmail(Partial.Widgets.emailText._datavalue)) {
        App.Variables.errorMsg.dataSet.dataValue = "Please enter valid Email Address";
    } else if ((!Partial.Widgets.ext.datavalue == "" || !Partial.Widgets.ext.datavalue == undefined) && (Partial.Widgets.workNo.datavalue == null || Partial.Widgets.workNo.datavalue == undefined)) {
        App.Variables.errorMsg.dataSet.dataValue = "Please provide the Work Phone no";
    } else if (!Partial.Widgets.TELUSContactsSelect.datavalue == "" && !Partial.Widgets.EmailForNoticesSelect.datavalue == "" && !Partial.Widgets.firstName.datavalue == "" && !Partial.Widgets.lastName.datavalue == "") {

        if ((!Partial.Widgets.emailText._datavalue == "" || !Partial.Widgets.emailText._datavalue == undefined) && validateEmail(Partial.Widgets.emailText._datavalue)) {
            App.Variables.errorMsg.dataSet.dataValue = "";
        }
        // API Call will come here
        App.Variables.successMessage.dataSet.dataValue = "Contact updated successfully.";
        Partial.Variables.ContactPageName.dataSet.dataValue = 'Contact';

    }

    setTimeout(messageTimeout, 10000);
};