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
    var GetContactDetailsByIdVar = Partial.Variables.GetContactDetailsById;
    GetContactDetailsByIdVar.invoke({
            "inputFields": {
                "id": row.contactId
            }
        },
        function(data) {
            Partial.Widgets.TELUSContactsSelect.datavalue = data.telusContactIndicator;
            Partial.Widgets.TITLESelect.datavalue = data.title;
            Partial.Widgets.firstName.datavalue = data.firstName;
            Partial.Widgets.lastName.datavalue = data.lastName;
            Partial.Widgets.EmailForNoticesSelect.datavalue = data.notificationIndicator;
            Partial.Widgets.emailText.datavalue = data.email;
            Partial.Widgets.cellPhone.datavalue = data.mobilePhoneNumber;
            Partial.Widgets.workNo.datavalue = data.workPhoneNumber;
            Partial.Widgets.ext.datavalue = data.workPhoneNumberExtension;
            Partial.Widgets.fax.datavalue = data.faxNumber;
            Partial.Widgets.comments.datavalue = data.comment;
            Partial.Widgets.lastUpdatedOn.caption = data.auditInfo.lastUpdatedDateTime;
            Partial.Widgets.lastUpdatedBy.caption = data.auditInfo.lastUpdatedBy;
            Partial.Widgets.createdOn.caption = data.auditInfo.createdDateTime;
            Partial.Widgets.createdBy.caption = data.auditInfo.createdBy;

            var endDateTime = data.validFor.endDateTime;
            if (endDateTime == null || endDateTime == undefined) {
                Partial.Variables.isContactExpired.dataSet.dataValue = false;
            } else {
                Partial.Variables.isContactExpired.dataSet.dataValue = true;
            }
        },
        function(error) {
            // Error Callback
            console.log("error", error);
        }

    );



    if (!(row.sourceOfContact == 'TCM')) {
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
        Partial.Widgets.Expire.show = false;
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
        Partial.Widgets.Expire.show = true;
    }

    Partial.Widgets.dataSource.caption = row.sourceOfContact;


}

function messageTimeout() {
    App.Variables.successMessage.dataSet.dataValue = null;
    App.Variables.errorMsg.dataSet.dataValue = null;
}

function validateEmail(email) {
    return email.match(/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/);
};

function isEmail(email) {
    var regex = /[A-Za-z0-9]+@[A-Za-z]+\.[A-Za-z]{2,3}/;
    return regex.test(email);
}


Partial.CancelClick = function($event, widget) {
    Partial.Clear();
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
    } else if (Partial.Widgets.ext.datavalue != null && Partial.Widgets.ext.datavalue != "" && ((Partial.Widgets.workNo.datavalue == undefined) || (Partial.Widgets.workNo.datavalue == ""))) {
        App.Variables.errorMsg.dataSet.dataValue = "Please provide the Work Phone number";
    } else {
        // API Call will come here

        Partial.Variables.updateDigitalContact.setInput({
            "id": Partial.Widgets.contactIDLabel.caption,
            "CollectionContactUpdate": {
                'id': Partial.Widgets.contactIDLabel.caption,
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
                'faxNumber': Partial.Widgets.fax.datavalue,
                'channel': {
                    'originatorAppId': "FAWBTELUSAGENT",
                    'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
                }
            }
        });

        //Invoke POST createDispute service
        Partial.Variables.updateDigitalContact.invoke();
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
Partial.ExpireClick = function($event, widget) {
    debugger;
    var today = new Date();
    /*if (Partial.Widgets.TELUSContactsSelect.datavalue === "" || Partial.Widgets.TELUSContactsSelect.datavalue == undefined) {
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
    } else if (Partial.Widgets.ext.datavalue != null && Partial.Widgets.ext.datavalue != "" && ((Partial.Widgets.workNo.datavalue == undefined) || (Partial.Widgets.workNo.datavalue == ""))) {
        App.Variables.errorMsg.dataSet.dataValue = "Please provide the Work Phone number";
    }  */
    // API Call will come here

    Partial.Variables.expireDigitalContact.setInput({
        "id": Partial.Widgets.contactIDLabel.caption,
        "CollectionContactUpdate": {
            'id': Partial.Widgets.contactIDLabel.caption,
            'channel': {
                'originatorAppId': "FAWBTELUSAGENT",
                'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
            },
            'validFor': {
                'endDateTime': today
            }
        }
    });

    //Invoke POST createDispute service
    Partial.Variables.expireDigitalContact.invoke();

};

Partial.Clear = function() {
    // Partial.Widgets.TELUSContactsSelect.datavalue = false;
    Partial.Widgets.TITLESelect.datavalue = "";
    Partial.Widgets.firstName.datavalue = "";
    Partial.Widgets.lastName.datavalue = "";
    //Partial.Widgets.EmailForNoticesSelect.datavalue = false;
    Partial.Widgets.emailText.datavalue = "";
    Partial.Widgets.cellPhone.datavalue = "";
    Partial.Widgets.workNo.datavalue = "";
    Partial.Widgets.ext.datavalue = "";
    Partial.Widgets.fax.datavalue = "";
    Partial.Widgets.comments.datavalue = "";
};



Partial.updateDigitalContactonSuccess = function(variable, data) {
    App.Variables.successMessage.dataSet.dataValue = "Digital Contact updated successfully.";
    App.Variables.errorMsg.dataSet.dataValue = null;
    Partial.Clear();
    Partial.Variables.ContactPageName.dataSet.dataValue = 'Contact';
    App.refreshContactList();
    setTimeout(messageTimeout, 10000);

};

Partial.expireDigitalContactonSuccess = function(variable, data) {
    App.Variables.successMessage.dataSet.dataValue = "Digital Contact expired successfully.";
    App.Variables.errorMsg.dataSet.dataValue = null;
    Partial.Clear();
    Partial.Variables.ContactPageName.dataSet.dataValue = 'Contact';
    App.refreshContactList();
    setTimeout(messageTimeout, 10000);
};