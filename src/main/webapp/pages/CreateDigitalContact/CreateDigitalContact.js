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
    App.Variables.errorMsg.dataSet.dataValue = null;

    // clearing the fields
    Partial.Widgets.TITLESelect.datavalue = "";
    Partial.Widgets.TELUSContactsSelect.datavalue = false;
    Partial.Widgets.EmailForNoticesSelect.datavalue = true;
    Partial.Widgets.firstName.datavalue = "";
    Partial.Widgets.lastName.datavalue = "";
    Partial.Widgets.emailText.datavalue = "";
    Partial.Widgets.workNo.datavalue = "";
    Partial.Widgets.cellPhone.datavalue = "";
    Partial.Widgets.ext.datavalue = "";
    Partial.Widgets.fax.datavalue = "";
    Partial.Widgets.comments.datavalue = "";
};


Partial.createContact = function($event, widget) {
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
    } else if (Partial.Widgets.fax.datavalue && Partial.Widgets.fax.datavalue.length <= 10 && isNaN(Partial.Widgets.fax.datavalue)) {
        App.Variables.errorMsg.dataSet.dataValue = "Value must be numeric for FAX";
    } else if (Partial.Widgets.workNo.datavalue && Partial.Widgets.workNo.datavalue.length <= 10 && isNaN(Partial.Widgets.workNo.datavalue)) {
        App.Variables.errorMsg.dataSet.dataValue = "Value must be numeric for Work No";
    } else if (Partial.Widgets.cellPhone.datavalue && Partial.Widgets.cellPhone.datavalue.length <= 10 && isNaN(Partial.Widgets.cellPhone.datavalue)) {
        App.Variables.errorMsg.dataSet.dataValue = "Value must be numeric for Cell Phone";
    } else if (Partial.Widgets.ext.datavalue && Partial.Widgets.ext.datavalue.length <= 10 && isNaN(Partial.Widgets.ext.datavalue)) {
        App.Variables.errorMsg.dataSet.dataValue = "Value must be numeric for Work Ext.";
    } else {
        debugger;
        Partial.Variables.CreateContactServiceVar.setInput({
            "CollectionContactCreate": {
                'firstName': Partial.Widgets.firstName.datavalue,
                'lastName': Partial.Widgets.lastName.datavalue,
                'mobilePhoneNumber': Partial.Widgets.cellPhone.datavalue ? Partial.Widgets.cellPhone.datavalue.replace(/\D/g, '') : Partial.Widgets.cellPhone.datavalue,
                'notificationIndicator': Partial.Widgets.EmailForNoticesSelect.datavalue,
                'telusContactIndicator': Partial.Widgets.TELUSContactsSelect.datavalue,
                'title': Partial.Widgets.TITLESelect.datavalue,
                'workPhoneNumber': Partial.Widgets.workNo.datavalue ? Partial.Widgets.workNo.datavalue.replace(/\D/g, '') : Partial.Widgets.workNo.datavalue,
                'workPhoneNumberExtension': Partial.Widgets.ext.datavalue,
                'comment': Partial.Widgets.comments.datavalue,
                'email': Partial.Widgets.emailText.datavalue,
                'faxNumber': Partial.Widgets.fax.datavalue ? Partial.Widgets.fax.datavalue.replace(/\D/g, '') : Partial.Widgets.fax.datavalue,
                'channel': {
                    'originatorAppId': "FAWBTELUSAGENT",
                    'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
                },
                'collectionEntity': {
                    'id': Partial.pageParams.entityId
                }
            }
        });

        //Invoke POST createContact service
        Partial.Variables.CreateContactServiceVar.invoke();

    }

};


Partial.workNoKeypress = function($event, widget) {
    var value = $event.key;
    var KeyID = $event.keyCode;
    isNotANumber(value);
    const input = event.target; // Get the input element
    const inputValue = input.value; // Get the current input value
    const pressedKey = String.fromCharCode(event.which); // Get the pressed key
    const fullInputValue = inputValue + pressedKey; // Combine the current value with the pressed key


    var area_code = fullInputValue.substring(0, 3);
    var first_three_digits = fullInputValue.substring(3, 6);
    var last_four_digits = fullInputValue.substring(6, 10);

    if (fullInputValue.length == 10 && !isNaN(fullInputValue)) {
        Partial.Widgets.workNo.datavalue = "(" + area_code + ")" + first_three_digits + "-" + last_four_digits;
    }

};

Partial.extKeypress = function($event, widget) {
    var value = $event.key;
    isNotANumber(value);
};

Partial.faxKeypress = function($event, widget) {
    debugger;
    var value = $event.key;
    var KeyID = $event.keyCode;
    isNotANumber(value);
    const input = event.target; // Get the input element
    const inputValue = input.value; // Get the current input value
    const pressedKey = String.fromCharCode(event.which); // Get the pressed key
    const fullInputValue = inputValue + pressedKey; // Combine the current value with the pressed key


    var area_code = fullInputValue.substring(0, 3);
    var first_three_digits = fullInputValue.substring(3, 6);
    var last_four_digits = fullInputValue.substring(6, 10);

    if (fullInputValue.length == 10 && !isNaN(fullInputValue)) {
        Partial.Widgets.fax.datavalue = "(" + area_code + ")" + first_three_digits + "-" + last_four_digits;
    }
};

Partial.cellPhoneKeypress = function($event, widget) {
    var value = $event.key;
    var KeyID = $event.keyCode;
    isNotANumber(value);
    const input = event.target; // Get the input element
    const inputValue = input.value; // Get the current input value
    const pressedKey = String.fromCharCode(event.which); // Get the pressed key
    const fullInputValue = inputValue + pressedKey; // Combine the current value with the pressed key


    var area_code = fullInputValue.substring(0, 3);
    var first_three_digits = fullInputValue.substring(3, 6);
    var last_four_digits = fullInputValue.substring(6, 10);

    if (fullInputValue.length == 10 && !isNaN(fullInputValue)) {
        Partial.Widgets.cellPhone.datavalue = "(" + area_code + ")" + first_three_digits + "-" + last_four_digits;
    }
};

function isNotANumber(value) {
    if (isNaN(value)) {
        App.Variables.errorMsg.dataSet.dataValue = "Invalid. Value must be numeric";
    }
    setTimeout(messageTimeout, 10000);
};

App.Clear = function() {
    Partial.Widgets.TITLESelect.datavalue = "";
    Partial.Widgets.TELUSContactsSelect.datavalue = false;
    Partial.Widgets.EmailForNoticesSelect.datavalue = true;
    Partial.Widgets.firstName.datavalue = "";
    Partial.Widgets.lastName.datavalue = "";
    Partial.Widgets.emailText.datavalue = "";
    Partial.Widgets.workNo.datavalue = "";
    Partial.Widgets.cellPhone.datavalue = "";
    Partial.Widgets.ext.datavalue = "";
    Partial.Widgets.fax.datavalue = "";
    Partial.Widgets.comments.datavalue = "";
};

Partial.CreateContactServiceVaronSuccess = function(variable, data) {
    App.Variables.successMessage.dataSet.dataValue = "Digital Contact created successfully.";
    App.Variables.errorMsg.dataSet.dataValue = null;
    App.Clear();
    Partial.Variables.ContactPageName.dataSet.dataValue = 'Contact';
    App.refreshContactList();
    setTimeout(messageTimeout, 10000);

};

Partial.workNoKeydown = function($event, widget) {
    var value = $event.key;
    if (value === 'Backspace') {
        Partial.Widgets.workNo.datavalue = "";
    }
};

Partial.faxKeydown = function($event, widget) {
    var value = $event.key;
    if (value === 'Backspace') {
        Partial.Widgets.fax.datavalue = "";
    }
};

Partial.cellPhoneKeydown = function($event, widget) {
    var value = $event.key;
    if (value === 'Backspace') {
        Partial.Widgets.cellPhone.datavalue = "";
    }
};
Partial.faxChange = function($event, widget, newVal, oldVal) {
    debugger;
    const fullInputValue = newVal
    var area_code = fullInputValue.substring(0, 3);
    var first_three_digits = fullInputValue.substring(3, 6);
    var last_four_digits = fullInputValue.substring(6, 10);

    if (fullInputValue.length == 10 && !isNaN(fullInputValue)) {
        Partial.Widgets.fax.datavalue = "(" + area_code + ")" + first_three_digits + "-" + last_four_digits;
    }
};
Partial.cellPhoneChange = function($event, widget, newVal, oldVal) {
    const fullInputValue = newVal
    var area_code = fullInputValue.substring(0, 3);
    var first_three_digits = fullInputValue.substring(3, 6);
    var last_four_digits = fullInputValue.substring(6, 10);

    if (fullInputValue.length == 10 && !isNaN(fullInputValue)) {
        Partial.Widgets.cellPhone.datavalue = "(" + area_code + ")" + first_three_digits + "-" + last_four_digits;
    }
};

Partial.workNoChange = function($event, widget, newVal, oldVal) {
    const fullInputValue = newVal
    var area_code = fullInputValue.substring(0, 3);
    var first_three_digits = fullInputValue.substring(3, 6);
    var last_four_digits = fullInputValue.substring(6, 10);

    if (fullInputValue.length == 10 && !isNaN(fullInputValue)) {
        Partial.Widgets.workNo.datavalue = "(" + area_code + ")" + first_three_digits + "-" + last_four_digits;
    }
};