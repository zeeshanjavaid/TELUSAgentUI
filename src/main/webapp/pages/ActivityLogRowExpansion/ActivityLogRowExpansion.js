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
        let inputParams = {
            'collectionEntityId': row.collectionEntity.id,
            'relatedBusinessEntityId': row.relatedBusinessEntityId
        };

        // Add another attribute if row.id is 1
        if (row.relatedBusinessEntitySubType == "NOTICE") {
            inputParams.businessEntityEventType = "CCSResponse";
        }

        Partial.Variables.getActivityLogPerRef.setInput(inputParams);
        Partial.Variables.getActivityLogPerRef.invoke();
    }
};

Partial.popoverClicked = function(comment) {
    console.log("Popover clicked with comment:", comment);
    // Add your custom logic here
    debugger;
}

Partial.getActivityLogPerRefonSuccess = function(variable, data) {
    debugger;
    //change data or set values here
};

function getFirstAvailableContactMediumType(data) {
    for (var i = 0; i < data.additionalCharacteristics.length; i++) {
        var characteristic = data.additionalCharacteristics[i];

        if (characteristic.name === "treatmentTaskApiRequest") {
            var ccsPackage = characteristic.value.ccsPackage;

            if (ccsPackage && ccsPackage.ccsContactList) {
                for (var j = 0; j < ccsPackage.ccsContactList.length; j++) {
                    if (ccsPackage.ccsContactList[j].contactMediumType) {
                        return ccsPackage.ccsContactList[j].contactMediumType; // Return immediately when found
                    }
                }
            }
        }
    }
    return null; // Return null if no contactMediumType is found
}

function getAllEmails(data) {
    let emails = [];
    for (var i = 0; i < data.additionalCharacteristics.length; i++) {
        var characteristic = data.additionalCharacteristics[i];

        if (characteristic.name === "cssRequest") {
            var cssRequest = characteristic.value;
            var index = 1;

            while (cssRequest[`Contact_${index}_Email`]) {
                emails.push(cssRequest[`Contact_${index}_Email`]);
                index++;
            }
        }
    }
    return emails.join("\n"); // Join emails with newline separator
}

function getAllAccountDetails(data) {
    let accounts = [];
    for (var i = 0; i < data.additionalCharacteristics.length; i++) {
        var characteristic = data.additionalCharacteristics[i];

        if (characteristic.name === "cssRequest") {
            var cssRequest = characteristic.value;
            var index = 1;

            while (cssRequest[`Account_Number_${index}`]) {
                accounts.push({
                    BAN: cssRequest[`Account_Number_${index}`],
                    DUE: cssRequest[`Account_${index}_Amount_Due`],
                    OD: cssRequest[`Account_${index}_Amount_Overdue`]
                });
                index++;
            }
        }
    }
    return accounts; // Return an array of objects
}

function extractInfoFromJson(data) {
    debugger;
    // Extract contentTypeCode and Data_Time_Stamp
    const contentTypeCode = data.additionalCharacteristics.find(item => item.name === 'contentTypeCode').value;
    const dataTimeStamp = data.additionalCharacteristics.find(item => item.name === 'cssRequest').value.Data_Time_Stamp;
    const totalOD = data.additionalCharacteristics.find(item => item.name === 'cssRequest').value.Current_Balance;

    // Extract BANS, Amount Due, and Amount Overdue
    const agedTrialBalances = data.additionalCharacteristics.find(item => item.name === 'agedTrialBalances').value;
    const accountsInfo = agedTrialBalances.map(account => {
        const accountNumber = account.bacctNum;
        const amountDue = account.amountDue;
        const amountOverdue = account.amountOverdue;
        return `BAN: ${accountNumber} AMT DUE: ${amountDue} AMT OD: ${amountOverdue}`;
    }).join("\n");

    // Construct the final formatted string
    const result = `${contentTypeCode} delivered on ${dataTimeStamp}\n${accountsInfo}\nTotal OD: ${totalOD}`;

    return result;
}

function formatTextWithLineBreaks(text) {
    return text.replace(/\n/g, '<br/>');
}

function extractCharacteristics(jsonData) {
    if (!jsonData || !jsonData.additionalCharacteristics) {
        return "No characteristics found";
    }

    return jsonData.additionalCharacteristics.map(char => {
        let value = char.value;

        // Convert objects or arrays to JSON strings
        if (typeof value === 'object') {
            value = JSON.stringify(value, null, 2); // Pretty-print for readability
        }

        return `${char.name}: ${value}`;
    }).join('\n');
}

Partial.expandedRowDataTable1Beforedatarender = function(widget, $data, $columns) {
    //control table column show or hide here
    debugger;
    $data.forEach((item, index) => {
        console.log(`Index: ${index}, relatedBusinessEntitySubType:`, item.relatedBusinessEntitySubType);
        console.log(`Index: ${index}, relatedBusinessEntityStatus:`, item.relatedBusinessEntityStatus);
        console.log(`Index: ${index}, businessEntityEventType:`, item.businessEntityEventType);

        if (item.relatedBusinessEntitySubType.toUpperCase() == 'NOTC' || item.relatedBusinessEntitySubType.toUpperCase() == 'NOTICE') {
            //$columns.type.show = false;
            $columns.deliveryType.show = true;
            item.comment = formatTextWithLineBreaks(extractCharacteristics(item));
            debugger;
            $columns.comment.show = true;
            $columns.commentLabel.show = false;
            /*if (item.relatedBusinessEntityStatus.toUpperCase() == "CLOSED" && item.businessEntityEventType.toUpperCase() == "STATUS") {
                            debugger;
                            var mediumType = getFirstAvailableContactMediumType(item);
                            item.deliveryType = mediumType;

                            var emailAddresses;
                            var accountDetails;

                            if (mediumType.toUpperCase() == 'EMAIL') {
                                console.log(extractInfoFromJson(item));
                                emailAddresses = getAllEmails(item);
                                accountDetails = extractInfoFromJson(item);
                                item.comment = formatTextWithLineBreaks(emailAddresses + "\n" + accountDetails);
                            }

                            item.comment = formatTextWithLineBreaks(extractCharacteristics(item)); //remove later
                        } else {
                            item.comment = formatTextWithLineBreaks(extractCharacteristics(item));
                        }*/

            /*if (item.relatedBusinessEntityStatus.toUpperCase() == "CLOSED" && item.businessEntityEventType.toUpperCase() == "STATUS") {
                item.collectionActivityPerformedBy = item.relatedBusinessEntityCreatedBy;
            }*/
        }

        if (item.relatedBusinessEntitySubType.toUpperCase() == 'CALL-OB') {
            $columns.dueDate.show = true;
            item.dueDate = item.relatedBusinessEntityDueDate;
            $columns.phoneNumber.show = true;
            item.phoneNumber = item.additionalCharacteristics.find(item => item.name === 'PhoneNumber').value;
            $columns.reachedCustomer.show = true;
            item.reachedCustomer = item.additionalCharacteristics.find(item => item.name === 'ReachedCustomer').value;
            $columns.relatedBusinessEntityAssignedTo.show = true;
            $columns.relatedBusinessEntityAssignedTeam.show = true;
        }

        if (item.relatedBusinessEntitySubType.toUpperCase() == 'CALL-IB' || item.relatedBusinessEntitySubType.toUpperCase() == 'SUSPEND' ||
            item.relatedBusinessEntitySubType.toUpperCase() == 'RESTORE' || item.relatedBusinessEntitySubType.toUpperCase() == 'CEASE') {
            $columns.dueDate.show = true;
            item.dueDate = item.relatedBusinessEntityDueDate;
            $columns.relatedBusinessEntityAssignedTo.show = true;
            $columns.relatedBusinessEntityAssignedTeam.show = true;
        }
        /*if (item.additionalCharacteristics != undefined) {
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
        }*/
    });
    debugger;
    /* Ensure columns exist
    if ($columns) {
        $columns.type.show = !$data.some(row => row.relatedBusinessEntitySubType === 'CALL-OB');
    } else {
        console.error("Columns not found.");
    }*/
};
Partial.expandedRowDataTable1_commentOnClick = function($event, widget, row) {
    debugger;
    App.Variables.fullComment.dataSet.dataValue = row.comment;
};