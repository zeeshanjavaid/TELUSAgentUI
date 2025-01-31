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
    console.log("TEST");
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
    debugger;
    Partial.Variables.ContactPageName.dataSet.dataValue = 'Contact';
    App.Variables.errorMsg.dataSet.dataValue = null;

    // clearing the fields
    Partial.Widgets.salutation.datavalue = "";
    Partial.Widgets.TELUSContactsSelect.datavalue = false;
    Partial.Widgets.prefLangValue.datavalue = "EN";
    Partial.Widgets.firstName.datavalue = "";
    Partial.Widgets.lastName.datavalue = "";
    Partial.Widgets.comments.datavalue = "";
    Partial.Widgets.jobTitle.datavalue = "";
    App.refreshContactList();

};


Partial.createContact = function($event, widget) {
    debugger;
    if (Partial.Widgets.firstName.datavalue == "" || Partial.Widgets.firstName.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "First Name is mandatory";
    } else if (Partial.Widgets.lastName.datavalue == "" || Partial.Widgets.lastName.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Last Name is mandatory";
    } else if (Partial.Widgets.TELUSContactsSelect.datavalue === "" || Partial.Widgets.TELUSContactsSelect.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Telus Contact Selection is mandatory";
        /*} else if (Partial.Widgets.EmailForNoticesSelect.datavalue && (Partial.Widgets.emailText.datavalue == "" || Partial.Widgets.emailText.datavalue == undefined)) {
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
            App.Variables.errorMsg.dataSet.dataValue = "Value must be numeric for Work Ext.";*/
    } else if (Partial.Widgets.prefLangValue.datavalue === "" || Partial.Widgets.prefLangValue.datavalue == undefined) {
        App.Variables.errorMsg.dataSet.dataValue = "Preferred Language is mandatory";
    } else if (Partial.Widgets.tabs1.activeTab.name == "emailTab" && Partial.Widgets.emailAddressValue.datavalue !== "" && !isEmail(Partial.Widgets.emailAddressValue.datavalue)) {
        App.Variables.errorMsg.dataSet.dataValue = "Please enter valid Email Address";
    } else {
        debugger;
        Partial.Widgets.saveEmailButton.disabled = true;
        var contactMediumType = widget.name === "saveEmailButton" ? "EMAIL" : "PHONE";

        let contactMediumId;
        let notificationIndicator;
        if (widget.name === "saveEmailButton") {
            contactMediumId = Partial.Widgets.emailAddressValue.datavalue;
            notificationIndicator = Partial.Widgets.emailNoticeBox.checkboxEl.nativeElement.checked;
        } else if (widget.name === "cellPhoneSaveButton") {
            contactMediumId = Partial.Widgets.cellPhoneValue.datavalue;
            notificationIndicator = Partial.Widgets.cellPhoneNoticeBox.checkboxEl.nativeElement.checked;
        } else if (widget.name === "workPhoneSaveButton") {
            contactMediumId = Partial.Widgets.workPhoneValue.datavalue;
            notificationIndicator = Partial.Widgets.workPhoneNoticeBox.checkboxEl.nativeElement.checked;
        } else if (widget.name === "faxSaveButton") {
            contactMediumId = Partial.Widgets.faxPhoneValue.datavalue;
            notificationIndicator = Partial.Widgets.faxNoticeBox.checkboxEl.nativeElement.checked;
        } else if (widget.name === "homePhoneSaveButton") {
            contactMediumId = Partial.Widgets.homePhoneValue.datavalue;
            notificationIndicator = Partial.Widgets.homePhoneNoticeBox.checkboxEl.nativeElement.checked;
        } else if (widget.name === "othersSaveButton") {
            contactMediumId = Partial.Widgets.othersPhoneValue.datavalue;
            notificationIndicator = Partial.Widgets.othersNoticeBox.checkboxEl.nativeElement.checked;
        }



        // Prepare the contactPeople object
        let contactPeople = {
            "firstName": Partial.Widgets.firstName.datavalue,
            "lastName": Partial.Widgets.lastName.datavalue
        };

        // Include title only if it's not null or empty
        if (Partial.Widgets.salutation.datavalue) {
            contactPeople.salutation = Partial.Widgets.salutation.datavalue;
        }

        if (Partial.Widgets.jobTitle.datavalue) {
            contactPeople.jobTitle = Partial.Widgets.jobTitle.datavalue;
        }

        Partial.Variables.CreateContactServiceVar.setInput({
            "CollectionContactCreate": {
                'channel': {
                    'originatorAppId': "FAWBTELUSAGENT",
                    'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
                },
                'comment': Partial.Widgets.comments.datavalue,
                'collectionEntity': {
                    'id': Partial.pageParams.entityId
                },
                'contactMediumType': contactMediumType,
                'contactMediumId': contactMediumId, //Partial.Widgets.cellPhone.datavalue ? Partial.Widgets.cellPhone.datavalue.replace(/\D/g, '') : Partial.Widgets.cellPhone.datavalue,
                'contactMediumSubType': "OTHER",
                'contactMediumSubId': "OTHER",
                "contactPeople": [contactPeople],
                'notificationIndicator': notificationIndicator,
                'preferredLanguage': Partial.Widgets.prefLangValue.datavalue,
                'telusContactIndicator': Partial.Widgets.TELUSContactsSelect.datavalue
                //'workPhoneNumber': Partial.Widgets.workNo.datavalue ? Partial.Widgets.workNo.datavalue.replace(/\D/g, '') : Partial.Widgets.workNo.datavalue,
                //'workPhoneNumberExtension': Partial.Widgets.ext.datavalue,
                //'email': Partial.Widgets.emailText.datavalue,
                //'faxNumber': Partial.Widgets.fax.datavalue ? Partial.Widgets.fax.datavalue.replace(/\D/g, '') : Partial.Widgets.fax.datavalue,
            }
        });

        //Invoke POST createContact service
        Partial.Variables.CreateContactServiceVar.invoke();

    }
    setTimeout(messageTimeout, 5000);

};

Partial.CreateContactServiceVaronSuccess = function(variable, data) {
    debugger;
    Partial.Widgets.saveEmailButton.disabled = false;
    if (Partial.Widgets.tabs1.activeTab.name == "emailTab") {
        App.Variables.successMessage.dataSet.dataValue = "Digital Contact created successfully with Email.";
    } else {
        App.Variables.successMessage.dataSet.dataValue = "Digital Contact created successfully with Number.";
    }
    App.Variables.errorMsg.dataSet.dataValue = null;
    App.ClearContacts();
    //Partial.Variables.ContactPageName.dataSet.dataValue = 'Contact';
    //App.refreshContactList();
    setTimeout(messageTimeout, 5000);

};

Partial.CreateContactServiceVaronError = function(variable, data, xhrObj) {
    debugger;
    Partial.Widgets.saveEmailButton.disabled = false;
    App.Variables.successMessage.dataSet.dataValue = "";
    App.Variables.errorMsg.dataSet.dataValue = "Digital Contact creation failed.";
    setTimeout(messageTimeout, 5000);
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

App.ClearContacts = function() {
    Partial.Widgets.emailAddressValue.datavalue = "";
    Partial.Widgets.cellPhoneValue.datavalue = "";
    Partial.Widgets.workPhoneValue.datavalue = "";
    Partial.Widgets.faxPhoneValue.datavalue = "";
    Partial.Widgets.homePhoneValue.datavalue = "";
    Partial.Widgets.othersPhoneValue.datavalue = "";

    Partial.Widgets.emailNoticeBox.datavalue = false;
    Partial.Widgets.cellPhoneNoticeBox.datavalue = false;
    Partial.Widgets.workPhoneNoticeBox.datavalue = false;
    Partial.Widgets.faxNoticeBox.datavalue = false;
    Partial.Widgets.homePhoneNoticeBox.datavalue = false;
    Partial.Widgets.othersNoticeBox.datavalue = false;
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
Partial.radioset1Change = function($event, widget, newVal, oldVal) {
    debugger;
    Partial.Widgets.TELUSContactsSelect.datavalue = "";
    Partial.Widgets.EmailForNoticesSelect.datavalue = "";
    Partial.Widgets.typeText.datavalue = "";
    if (newVal == 'Email') {
        Partial.Widgets.TELUSContactsSelect.disabled = false;
        Partial.Widgets.EmailForNoticesSelect.disabled = false;
        Partial.Widgets.subTypeText.disabled = true;
        Partial.Widgets.PhoneSubTypeSelect.disabled = true;
        Partial.Widgets.typeLabel.caption = 'Email';
    } else {
        Partial.Widgets.TELUSContactsSelect.disabled = true;
        Partial.Widgets.EmailForNoticesSelect.disabled = true;
        Partial.Widgets.subTypeText.disabled = false;
        Partial.Widgets.PhoneSubTypeSelect.disabled = false;
        Partial.Widgets.typeLabel.caption = 'Phone';
    }
};