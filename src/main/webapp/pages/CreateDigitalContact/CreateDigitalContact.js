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
    Partial.Widgets.TELUSContactsSelect.datavalue = "N";
    Partial.Widgets.EmailForNoticesSelect.datavalue = "Y";
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
    } else {
        Partial.Variables.CreateContactServiceVar.setInput({
            "CollectionContactCreate": {
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
                // 'collectionEntity': {
                //     'id': '12345'
                // }
            }
        });

        //Invoke POST createDispute service
        Partial.Variables.CreateContactServiceVar.invoke();


        App.Variables.successMessage.dataSet.dataValue = "Digital Contact created successfully.";
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
        Partial.Variables.ContactPageName.dataSet.dataValue = 'Contact';
        setTimeout(messageTimeout, 10000);
    }


};


Partial.workNoKeypress = function($event, widget) {
    var value = $event.key;
    isNotANumber(value);
};


/*Partial.workNoKeypress = function($event, widget) {
    debugger;
    var value = $event.key;
    isNotANumber(value);
    const input = event.target; // Get the input element
    const inputValue = input.value; // Get the current input value
    const pressedKey = String.fromCharCode(event.which); // Get the pressed key
    const fullInputValue = inputValue + pressedKey; // Combine the current value with the pressed key
    //input_value = "1234567890"  # Replace with your actual 10-digit input


    var area_code = fullInputValue.substring(0, 3);
    var first_three_digits = fullInputValue.substring(3, 6);
    var last_four_digits = fullInputValue.substring(6, 10);

    if (fullInputValue.length == 10 && !isNaN(fullInputValue)) {
        debugger;
        Partial.Widgets.workNo.datavalue = "(" + area_code + ")" + first_three_digits + "-" + last_four_digits;
    }

    /*if (fullInputValue.length == 13) {
        fullInputValue = fullInputValue.split('-').join('');
        fullInputValue = fullInputValue.split('(').join('');
        fullInputValue = fullInputValue.split(')').join('');
        fullInputValue;

        Partial.Widgets.workNo.datavalue = "(" + area_code + ")" + first_three_digits + "-" + last_four_digits;
    }*/

// var z = fullInputValue.substring(8, 9);
// if (fullInputValue.substring(8, 9) !== '-' && fullInputValue.length == 8 && isNaN(fullInputValue)) {
//     debugger;
//     var a1 = fullInputValue.substring(0, 5);
//     var a2 = fullInputValue.substring(5, 8);
//     Partial.Widgets.workNo.datavalue = a1 + a2 + "-";
// }


//};

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