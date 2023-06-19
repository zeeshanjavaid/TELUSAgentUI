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

App.rowDataValues = function(row) {
    debugger;
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
    } else {
        Partial.Widgets.TELUSContactsSelect.disabled = false;
        Partial.Widgets.TITLESelect.disabled = false;
        Partial.Widgets.firstName.disabled = false;
        Partial.Widgets.lastName.disabled = false;
        Partial.Widgets.emailText.disabled = false;
        Partial.Widgets.cellPhone.disabled = false;
        Partial.Widgets.ext.disabled = false;
        Partial.Widgets.workNo.disabled = false;
        Partial.Widgets.fax.disabled = false;
        Partial.Widgets.comments.disabled = false;
    }

    Partial.Widgets.contactID.caption = row.contactId;
    Partial.Widgets.TELUSContactsSelect.datavalue = row.telusContacts;
    Partial.Widgets.dataSource.caption = row.sourceOfContact;
    Partial.Widgets.TITLESelect.datavalue = row.title;
    Partial.Widgets.firstName.datavalue = row.firstName;
    Partial.Widgets.lastName.datavalue = row.lastName;
    Partial.Widgets.emailText.datavalue = row.email;
    Partial.Widgets.EmailForNoticesSelect.datavalue = row.contactForNotices;
    Partial.Widgets.cellPhone.datavalue = row.mobileNumber;
    Partial.Widgets.ext.datavalue = '';
    Partial.Widgets.workNo.datavalue = row.workNumber;
    Partial.Widgets.fax.datavalue = '';
    Partial.Widgets.comments.datavalue = '';

}

function messageTimeout() {
    App.Variables.successMessage.dataSet.dataValue = null;
    App.Variables.errorMsg.dataSet.dataValue = null;
}

function validateEmail(email) {
    return email.match(/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/);
};

function isEmail(email) {
    var regex = /[a-z0-9]+@[a-z]+\.[a-z]{2,3}/;
    return regex.test(email);
}


Partial.CancelClick = function($event, widget) {
    Partial.Variables.ContactPageName.dataSet.dataValue = 'Contact';
};


Partial.updateContact = function($event, widget) {
    debugger;
    if (Partial.Widgets.TELUSContactsSelect.datavalue === "" || Partial.Widgets.TELUSContactsSelect.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Telus Contact is mandatory";
    } else if (Partial.Widgets.EmailForNoticesSelect.datavalue === "" || Partial.Widgets.EmailForNoticesSelect.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Email for Notices is mandatory";
    } else if (Partial.Widgets.firstName.datavalue == "" || Partial.Widgets.firstName.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "First Name is mandatory";
    } else if (Partial.Widgets.lastName.datavalue == "" || Partial.Widgets.lastName.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Last Name is mandatory";
    } else if (Partial.Widgets.EmailForNoticesSelect.datavalue && (Partial.Widgets.emailText.datavalue == "" || Partial.Widgets.emailText.datavalue == undefined)) {
        App.Variables.errorMsg.dataSet.dataValue = "Please provide the Email";
    } else if (Partial.Widgets.EmailForNoticesSelect.datavalue && Partial.Widgets.emailText.datavalue !== "" && !isEmail(Partial.Widgets.emailText.datavalue)) {
        App.Variables.errorMsg.dataSet.dataValue = "Please enter valid Email Address";
    } else if ((Partial.Widgets.ext.datavalue !== "") && ((Partial.Widgets.workNo.datavalue == undefined) || (Partial.Widgets.workNo.datavalue == ""))) {
        App.Variables.errorMsg.dataSet.dataValue = "Please provide the Work Phone number";
    } else {
        // API Call will come here

        Partial.Variables.updateDigitalContact.setInput({
            "CollectionContactUpdate": {
                'firstName': Partial.Widgets.firstName.datavalue,
                'lastName': Partial.Widgets.lastName.datavalue,
                'mobilePhoneNumber': Partial.Widgets.cellPhone.datavalue,
                'notificationIndicator': Partial.Widgets.EmailForNoticesSelect.datavalue,
                'telusContactIndicator': Partial.Widgets.TELUSContactsSelect.datavalue,
                'title': Partial.Widgets.TITLESelect.datavalue,
                'workPhoneNumber': Partial.Widgets.workNo.datavalue,
                'workPhoneNumberExtension': Partial.Widgets.ext.datavalue,
                'comment': Partial.Widgets.comments.datavalue,
                'email': Partial.Widgets.emailText.datavalue,
                'faxNumber': Partial.Widgets.fax.datavalue
            }
        });

        //Invoke POST createDispute service
        Partial.Variables.updateDigitalContact.invoke();

        App.Variables.successMessage.dataSet.dataValue = "Digital Contact updated successfully.";
        Partial.Variables.ContactPageName.dataSet.dataValue = 'Contact';
        setTimeout(messageTimeout, 10000);
    }


};


Partial.workNoKeypress = function($event, widget) {
    var value = $event.key;
    isNotANumber(value);
};

Partial.extKeypress = function($event, widget) {
    var value = $event.key;
    isNotANumber(value);
};

Partial.faxKeypress = function($event, widget) {
    var value = $event.key;
    isNotANumber(value);
};

Partial.cellPhoneKeypress = function($event, widget) {
    var value = $event.key;
    isNotANumber(value);
};

function isNotANumber(value) {
    if (isNaN(value)) {
        App.Variables.errorMsg.dataSet.dataValue = "Invalid. Value must be numeric";
    }
    setTimeout(messageTimeout, 10000);
};