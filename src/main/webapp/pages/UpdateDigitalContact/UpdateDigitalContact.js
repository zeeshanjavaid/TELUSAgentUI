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

    //Added condition to check for CES9 datasource contacts
    if (row.contactId != null && row.sourceOfContact == 'Internal') {
        Partial.Widgets.contactIDLabel.caption = row.contactId;
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
                if (data.mobilePhoneNumber != null) {
                    Partial.Widgets.cellPhone.datavalue = data.mobilePhoneNumber.length == 10 ? '(' + data.mobilePhoneNumber.substring(0, 3) + ')' + data.mobilePhoneNumber.substring(3, 6) + '-' + data.mobilePhoneNumber.substring(6, 10) : data.mobilePhoneNumber;
                }
                if (data.workPhoneNumber != null) {
                    Partial.Widgets.workNo.datavalue = data.workPhoneNumber.length == 10 ? '(' + data.workPhoneNumber.substring(0, 3) + ')' + data.workPhoneNumber.substring(3, 6) + '-' + data.workPhoneNumber.substring(6, 10) : data.workPhoneNumber;
                }
                Partial.Widgets.ext.datavalue = data.workPhoneNumberExtension;
                if (data.faxNumber != null) {
                    Partial.Widgets.fax.datavalue = data.faxNumber.length == 10 ? '(' + data.faxNumber.substring(0, 3) + ')' + data.faxNumber.substring(3, 6) + '-' + data.faxNumber.substring(6, 10) : data.faxNumber;
                }
                Partial.Widgets.comments.datavalue = data.comment;
                Partial.Widgets.lastUpdatedOn.caption = data.auditInfo.lastUpdatedDateTime;
                //Partial.Widgets.lastUpdatedBy.caption = data.auditInfo.lastUpdatedBy;
                Partial.Widgets.createdOn.caption = data.auditInfo.createdDateTime;
                // Partial.Widgets.createdBy.caption = data.auditInfo.createdBy;

                var endDateTime = data.validFor.endDateTime;
                if (endDateTime == null || endDateTime == undefined) {
                    Partial.Variables.isContactExpired.dataSet.dataValue = false;
                } else {
                    Partial.Variables.isContactExpired.dataSet.dataValue = true;
                }

                var GetCreatedByNameUsingAgentIdVar = Partial.Variables.GetCreatedByNameUsingAgentId;
                GetCreatedByNameUsingAgentIdVar.invoke({
                        "inputFields": {
                            "empId": data.auditInfo.createdBy
                        }
                    },
                    function(data1) {
                        Partial.Widgets.createdBy.caption = data1;
                        var GetUpdatedByNameUsingAgentIdVar = Partial.Variables.GetUpdatedByNameUsingAgentId;
                        GetUpdatedByNameUsingAgentIdVar.invoke({
                                "inputFields": {
                                    "empId": data.auditInfo.lastUpdatedBy
                                }
                            },
                            function(data2) {
                                Partial.Widgets.lastUpdatedBy.caption = data2;
                            },
                            function(error2) {
                                // Error Callback
                                console.log("error", error2);
                            }

                        );
                    },
                    function(error1) {
                        // Error Callback
                        console.log("error", error1);
                    }

                );
            },
            function(error) {
                // Error Callback
                console.log("error", error);
            }

        );
    } else {
        if (row.contactId != null) {
            Partial.Widgets.lastUpdatedBy.caption = null;
            Partial.Widgets.createdBy.caption = null;
            Partial.Widgets.contactIDLabel.caption = row.contactId;
            Partial.Widgets.TELUSContactsSelect.datavalue = row.telusContacts;
            Partial.Widgets.TITLESelect.datavalue = row.title;
            Partial.Widgets.firstName.datavalue = row.firstName;
            Partial.Widgets.lastName.datavalue = row.lastName;
            if (row.mobileNumber != null) {
                Partial.Widgets.cellPhone.datavalue = row.mobileNumber.length == 10 ? '(' + row.mobileNumber.substring(0, 3) + ')' + row.mobileNumber.substring(3, 6) + '-' + row.mobileNumber.substring(6, 10) : row.mobileNumber;
            }
            if (row.workNumber != null) {
                Partial.Widgets.workNo.datavalue = row.workNumber.length == 10 ? '(' + row.workNumber.substring(0, 3) + ')' + row.workNumber.substring(3, 6) + '-' + row.workNumber.substring(6, 10) : row.workNumber;
            }
            Partial.Widgets.ext.datavalue = row.workPhoneExt;
            if (row.faxNumber != null) {
                Partial.Widgets.fax.datavalue = row.faxNumber.length == 10 ? '(' + row.faxNumber.substring(0, 3) + ')' + row.faxNumber.substring(3, 6) + '-' + row.faxNumber.substring(6, 10) : row.faxNumber;
            }
            var GetContactDetailsByIdVar = Partial.Variables.GetContactDetailsById;
            GetContactDetailsByIdVar.invoke({
                    "inputFields": {
                        "id": row.contactId
                    }
                },
                function(data) {
                    Partial.Widgets.EmailForNoticesSelect.datavalue = data.notificationIndicator;
                    Partial.Widgets.emailText.datavalue = data.email;

                    Partial.Widgets.comments.datavalue = data.comment;
                    Partial.Widgets.lastUpdatedOn.caption = data.auditInfo.lastUpdatedDateTime;
                    Partial.Widgets.createdOn.caption = data.auditInfo.createdDateTime;

                    var GetCreatedByNameUsingAgentIdVar = Partial.Variables.GetCreatedByNameUsingAgentId;
                    GetCreatedByNameUsingAgentIdVar.invoke({
                            "inputFields": {
                                "empId": data.auditInfo.createdBy
                            }
                        },
                        function(data1) {
                            // Partial.Widgets.createdBy.caption = data1;
                            //Partial.Widgets.createdBy.caption = null;
                            var GetUpdatedByNameUsingAgentIdVar = Partial.Variables.GetUpdatedByNameUsingAgentId;
                            GetUpdatedByNameUsingAgentIdVar.invoke({
                                    "inputFields": {
                                        "empId": data.auditInfo.lastUpdatedBy
                                    }
                                },
                                function(data2) {
                                    //  Partial.Widgets.lastUpdatedBy.caption = data2;
                                    // Partial.Widgets.lastUpdatedBy.caption = null;
                                },
                                function(error2) {
                                    // Error Callback
                                    console.log("error", error2);
                                }

                            );
                        },
                        function(error1) {
                            // Error Callback
                            console.log("error", error1);
                        }

                    );
                },
                function(error) {
                    // Error Callback
                    console.log("error", error);
                }

            );

        } else {
            Partial.Widgets.contactIDLabel.caption = null;
            Partial.Widgets.TELUSContactsSelect.datavalue = row.telusContacts;
            Partial.Widgets.TITLESelect.datavalue = row.title;
            Partial.Widgets.firstName.datavalue = row.firstName;
            Partial.Widgets.lastName.datavalue = row.lastName;
            Partial.Widgets.EmailForNoticesSelect.datavalue = row.contactForNotices;
            Partial.Widgets.emailText.datavalue = row.email;
            if (row.mobileNumber != null) {
                Partial.Widgets.cellPhone.datavalue = row.mobileNumber.length == 10 ? '(' + row.mobileNumber.substring(0, 3) + ')' + row.mobileNumber.substring(3, 6) + '-' + row.mobileNumber.substring(6, 10) : row.mobileNumber;
            }
            if (row.workNumber != null) {
                Partial.Widgets.workNo.datavalue = row.workNumber.length == 10 ? '(' + row.workNumber.substring(0, 3) + ')' + row.workNumber.substring(3, 6) + '-' + row.workNumber.substring(6, 10) : row.workNumber;
            }
            Partial.Widgets.ext.datavalue = row.workPhoneExt;
            if (row.faxNumber != null) {
                Partial.Widgets.fax.datavalue = row.faxNumber.length == 10 ? '(' + row.faxNumber.substring(0, 3) + ')' + row.faxNumber.substring(3, 6) + '-' + row.faxNumber.substring(6, 10) : row.faxNumber;
            }
        }


    }


    if (!(row.sourceOfContact == 'Internal')) {
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
    if ((Partial.Widgets.contactIDLabel.caption != null) && (Partial.Widgets.dataSource.caption == 'Internal')) {
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
            // API Call will come here

            Partial.Variables.updateDigitalContact.setInput({
                "id": Partial.Widgets.contactIDLabel.caption,
                "CollectionContactUpdate": {
                    'id': Partial.Widgets.contactIDLabel.caption,
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
                    }
                }
            });

            //Invoke POST createDispute service
            Partial.Variables.updateDigitalContact.invoke();
        }
    } else if ((Partial.Widgets.contactIDLabel.caption != null) && (Partial.Widgets.dataSource.caption == 'CES9')) {
        if (Partial.Widgets.EmailForNoticesSelect.datavalue === "" || Partial.Widgets.EmailForNoticesSelect.datavalue == undefined) {
            App.Variables.errorMsg.dataSet.dataValue = "Email for Notices is mandatory";
        } else {
            Partial.Variables.updateDigitalContact.setInput({
                "id": Partial.Widgets.contactIDLabel.caption,
                "CollectionContactUpdate": {
                    'id': Partial.Widgets.contactIDLabel.caption,
                    'notificationIndicator': Partial.Widgets.EmailForNoticesSelect.datavalue,
                    'channel': {
                        'originatorAppId': "CES9",
                        'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
                    }
                }
            });
            Partial.Variables.updateDigitalContact.invoke();
        }
    } else {
        Partial.Variables.AddContactForCES9.setInput({
            "CollectionContactCreate": {
                'notificationIndicator': Partial.Widgets.EmailForNoticesSelect.datavalue,
                'email': Partial.Widgets.emailText.datavalue,
                'channel': {
                    'originatorAppId': "CES9",
                    'userId': App.Variables.getLoggedInUserDetails.dataSet.emplId
                },
                'collectionEntity': {
                    'id': Partial.pageParams.entityId
                }
            }
        });

        Partial.Variables.AddContactForCES9.invoke();
    }

};


Partial.workNoKeypress = function($event, widget) {
    var value = $event.key;
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
    var value = $event.key;
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
    Partial.Widgets.lastUpdatedOn.datavalue = "";
    Partial.Widgets.createdOn.datavalue = "";
    Partial.Widgets.lastUpdatedBy.datavalue = "";
    Partial.Widgets.createdBy.datavalue = "";
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

Partial.cellPhoneKeydown = function($event, widget) {
    var value = $event.key;
    if (value === 'Backspace') {
        Partial.Widgets.cellPhone.datavalue = "";
    }
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

Partial.AddContactForCES9onSuccess = function(variable, data) {
    App.Variables.successMessage.dataSet.dataValue = "Digital Contact updated successfully.";
    App.Variables.errorMsg.dataSet.dataValue = null;
    Partial.Clear();
    Partial.Variables.ContactPageName.dataSet.dataValue = 'Contact';
    App.refreshContactList();
    setTimeout(messageTimeout, 10000);
};