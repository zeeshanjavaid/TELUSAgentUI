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

App.appStartTime = new Date();
App.userPermission = null;
App.permissionsLoaded = false;
App.queueFiltersLoaded = false;
App.timeZone = null;

/* perform any action on the variables within this block(on-page-load) */
App.onAppVariablesReady = function() {
    /*
     * variables can be accessed through 'App.Variables' property here
     * e.g. App.Variables.staticVariable1.getData()
     */
};

/* perform any action on session timeout here, e.g clearing some data, etc */
App.onSessionTimeout = function() {
    /*
     * NOTE:
     * On re-login after session timeout:
     * if the same user logs in(through login dialog), app will retain its state
     * if a different user logs in, app will be reloaded and user is redirected to respective landing page configured in Security.
     */
};

/*
 * This application level callback function will be invoked after the invocation of PAGE level onPageReady function.
 * Use this function to write common logic across the pages in the application.
 * activePageName : name of the page
 * activePageScope: scope of the page
 * $activePageEl  : page jQuery element
 */
App.onPageReady = function(activePageName, activePageScope, $activePageEl) {

};

/*
 * This application level callback function will be invoked after a Variable receives an error from the target service.
 * Use this function to write common error handling logic across the application.
 * source:      Variable object or Widget Scope
 * errorMsg:    The error message returned by the target service. This message will be displayed through appNotification variable
 *              You can change this though App.Variables.appNotification.setMessage(YOUR_CUSTOM_MESSAGE)
 * xhrObj:      The xhrObject used to make the service call
 *              This object contains useful information like statusCode, url, request/response body.
 */
App.onServiceError = function(source, errorMsg, xhrObj) {


    if (source.name === 'UserManagementServiceCreateUser') {
        App.Variables.errorMsg.dataSet.dataValue = xhrObj.error;
    }



};

App.select2 = function(elementName, displayData, dataSet, dataField, displayField) {

    console.log("## Display data printed below");
    console.log(displayData);

    $("*[name='" + elementName + "']").selectize()[0].selectize.destroy();

    var $mSelect = $("*[name='" + elementName + "']").selectize({
        placeholder: "Select a value",
        plugins: ["remove_button"],
        delimiter: ",",
        onItemAdd: function(value, $item) {
            var data = this.options[value];
            // console.log(data.id);
            dataSet.push(data.id);
        },
        onItemRemove: function(value, $item) {
            var data = this.options[value];
            console.log(data.id);
            console.log('### before dataSet delete ###');
            console.log(dataSet);

            let indexToDelete = dataSet.indexOf(referReasonId => referReasonId == data.id);
            dataSet.splice(indexToDelete, 1);
            console.log('### after dataSet delete ###');
            console.log(dataSet);

        },
        persist: false,
        maxItems: null,
        valueField: dataField,
        labelField: displayField,
        searchField: displayField,

        options: displayData,

        create: function(input) {
            return {
                value: input,
                text: input,
            };
        },
    });

};

/* 
 * This function applies 'wm-panel' level expand/collapse icons for global use
 * panelRef:    DOM reference of the panel widget
 * toggleMode:  toggle mode specifies values 'expand' & 'collapse' to specify panel behaviour. This field is optional
 *              for use in 'Page.onReady()' lifecycle method, but is required when panel expand/collapse events occur
 *              (i.e., when bound to panel callback events of 'OnExpand' and 'OnCollapse')
 */
App.togglePanelIcons = function(panelRef, toggleMode) {
    if (panelRef) {
        if ($(".card-actions button", panelRef)[0]) {
            if (toggleMode === undefined || toggleMode === null || toggleMode === '') {
                if ($(".card-actions button i", panelRef).attr("class") === undefined) {
                    $(".card-actions button i", panelRef).addClass("fico-icon-chevron-up");
                }
                if ($(".card-actions button i", panelRef).attr("class").indexOf("wi-minus") > -1) {
                    $(".card-actions button i", panelRef).addClass("fico-icon-chevron-up");
                }
                if ($(".card-actions button i", panelRef).attr("class").indexOf("wi-plus") > -1) {
                    $(".card-actions button i", panelRef).addClass("fico-icon-chevron-down");
                }
            } else {
                if (toggleMode === "expand") {
                    $(".card-actions button i", panelRef).removeClass("fico-icon-chevron-down").addClass("fico-icon-chevron-up");
                }
                if (toggleMode === "collapse") {
                    $(".card-actions button i", panelRef).removeClass("fico-icon-chevron-up").addClass("fico-icon-chevron-down");
                }
            }
        } //check if panel is expandable/collapsible
    }
}

/*
 * Maps the respective image/icon to the associated ACTIVITY TYPE domain value code
 * and returns the image/icon path
 * activityTypeCode: ACTIVITY TYPE domain value code
 */
App.mapActivityTypeToIcons = function(activityTypeCode) {
    let imagePath = "resources/images/imagelists/ActivityIcons/";

    if (activityTypeCode) {
        switch (activityTypeCode.toUpperCase()) {
            case "EVENT":
                imagePath = imagePath.concat("EVENT.png");
                break;
            case "USER_ACTION":
                imagePath = imagePath.concat("USER_ACTION.png");
                break;
            case "CALL_ACTIVITY":
                imagePath = imagePath.concat("CALL_ACTIVITY.png");
                break;
            case "CALL_DECISION_ACTIVITY":
                imagePath = imagePath.concat("CALL_DECISION_ACTIVITY.png");
                break;
            case "SCRIPT_TASK":
                imagePath = imagePath.concat("SCRIPT_TASK.png");
                break;
            case "SUB_PROCESS":
                imagePath = imagePath.concat("SUB_PROCESS.png");
                break;
            case "START":
                imagePath = imagePath.concat("START.png");
                break;
            case "END":
                imagePath = imagePath.concat("END.png");
                break;
            case "ERROR":
                imagePath = imagePath.concat("ERROR.png");
                break;
            case "GATEWAY":
                imagePath = imagePath.concat("GATEWAY.png");
                break;
            default:
                imagePath = "";
                break;
        }
    }

    return imagePath;
}

/*
 * This method prettifies any valid JSON string supplied
 */
App.JSONPrettify = function(JSONString) {
    if (JSONString && typeof JSONString === "string") {
        JSONString = JSONString.replace(/^"/g, "").replace(/"$/g, "");
        JSONString = JSONString.replace(/\\n/gm, "").replace(/\\r/gm, "").replace(/\\t/gm, "").replace(/[\\\s]+/gm, "");

        return JSON.stringify(JSON.parse(JSONString), undefined, 4);
    } else
        return null;
}

App.decodeUrl = function(urlParam) {
    return window.decodeURIComponent(urlParam);
};

App.encodeUrl = function(urlParam) {
    return window.encodeURIComponent(urlParam);
};

App.htmlEncode = function htmlEncode(value) {
    return $('<div>').text(value).html();
};

App.getCurrentLocale = function() {
    // console.log(navigator.language);
    // return navigator.language;
    return App.i18nService.getSelectedLocale();
}

App.applicationLockedByLoggedInUseronError = function(variable, data) {
    console.log("Error in applicationLockedByLoggedInUseronError: ", data);
};

App.applicationLockedByLoggedInUseronSuccess = function(variable, data) {
    if (data.value == "false") {
        App.Variables.isLocked.dataSet.dataValue = true;
    } else {
        App.Variables.isLocked.dataSet.dataValue = false;
    }
};

App.lockApplication = function(data) {
    App.Variables.lockApplication.setInput("applicationId", data);
    App.Variables.lockApplication.invoke();

};

App.unlockApplicationByApplicationIdonSuccess = function(variable, data) {
    App.Variables.isLocked.dataSet.dataValue = false;
};

App.lockApplicationonSuccess = function(variable, data) {
    if (data) {
        if (App.Variables.loggedInUser.dataSet.id.toLowerCase() == data.email.toLowerCase()) {
            App.Variables.isLocked.dataSet.dataValue = false;
        } else {
            App.Variables.isLocked.dataSet.dataValue = true;
            App.Variables.isLockedBy.dataSet = data;
        }
    }
    App.disableFeilds();

};

App.onBeforePageLeave = function(activePageName, activePageScope) {
    // perform checks
    // to prevent page navigation return false;
    App.appStartTime = new Date();
    App.permissionsLoaded = false;
    App.queueFiltersLoaded = false;


    if (activePageScope.applicationId !== undefined) {
        App.Variables.unlockApplicationByApplicationId.setInput("applicationId", activePageScope.applicationId);
        App.Variables.unlockApplicationByApplicationId.invoke();
    }

}

App.disableFeilds = function() {
    if (App.Variables.isLocked.dataSet.dataValue) {
        $(document).ready(function() {
            // Create application sections
            $("[name='mv_applicationDataForm'] input, [name='mv_applicationDataForm'] select").attr('disabled', true);
            $("[name='mv_applicationDataForm1'] *").attr('disabled', true);
            $("[name='notesForm1']").hide();
            $("button[name='AuthorizedBtn']").attr('disabled', true);
            // $("[name='AuthorizedBtn']").hide();
            // Review application sections
            $("[name='mv_manualDecisionForm'] textarea, [name='mv_manualDecisionForm'] select").attr('disabled', true);
            // $("select").attr("disabled", true);
            // $("textarea").attr("disabled", true);
            $("button[name='AuthorizedBtn_Review']").attr('disabled', true);
            // AuthorizedBtn_Review

            $("span.input-group-append").hide();
            $("div[name='mv_applicationDataList1']").css({
                "pointer-events": "none",
                "opacity": "0.7"
            });

            $("[name='condition_container'] *").prop('disabled', true);
            $("[name='document_container'] *").prop('disabled', true);

            $("select[name='selectOwner']").attr('disabled', true);

            $("button[name='btnAddEditAssets']").hide()
            $("button[name='ManageLiability_btn']").hide()

            $("button[name='AddCondition_btn']").hide()
            $("button[name='AddDocument_btn']").hide()
            $("button[name='btn_pullCreditReport']").hide()
        });
    }

};

App.getErrorMessage = function(form, field, errorMessageMap) {

    var control = form.ngform.controls[field];
    var errorMessage;
    errorMessageMap = errorMessageMap || {};
    if (control.invalid) {
        var errors = control.errors;
        if (errors.required) {
            errorMessage = errorMessageMap['required'] || App.appLocale.ERROR_MESSAGE_REQUIRED_FIELD;
        } else if (errors.pattern) {
            errorMessage = errorMessageMap['pattern'] || App.appLocale.ERROR_MESSAGE_PATTERN;
        } else if (errors.length) {
            errorMessage = errorMessageMap['length'] || App.appLocale.ERROR_MESSAGE_MAX_LENGTH;
        } else if (errors.regex) {
            errorMessage = errorMessageMap['regex'] || App.appLocale.ERROR_MESSAGE_REGEX;
        } else if (errors.max) {
            errorMessage = errorMessageMap['max'];
        } else if (errors.min) {
            errorMessage = errorMessageMap['min'];
        }
    }
    return errorMessage;
};

App.requiredValidator = function(control) {
    var value = control.value || '';
    try {
        if (value !== undefined || value !== null)
            value = value.trim();
    } catch (ex) {
        value = '';
    }
    var validationErrors = null;
    if (value.length === 0) {
        validationErrors = {
            required: true
        }
    }
    return validationErrors;
};

/* Custom utility methods for dealing with JSON datasets (START) */

/*
 * Used to create a deep copy of a given object recursively & returns it
 */
App.objectDeepClone = function(obj) {
    try {
        if (obj && typeof(obj) == "object") {
            let finalObj = {};
            let finalArrayObj = [];
            let lastObjPoint = null;
            let lastArrayPoint = null;

            for (prop in obj) {
                if (typeof(obj[prop]) == "object" && obj[prop] != null && !Array.isArray(obj[prop]) && obj[prop].constructor !== Date) {
                    lastObjPoint = prop;

                    const returnedCopy = App.objectDeepClone(obj[prop]);
                    let copyOfObjProp = {};
                    Object.assign(copyOfObjProp, returnedCopy);

                    if (Array.isArray(obj))
                        finalArrayObj.push(copyOfObjProp);
                    else
                        finalObj[lastObjPoint] = copyOfObjProp;
                } else if (typeof(obj[prop]) == "object" && obj[prop] != null && Array.isArray(obj[prop])) {
                    lastArrayPoint = prop;

                    const returnedCopy = App.objectDeepClone(obj[prop]);
                    let copyOfObjProp = [];
                    Object.assign(copyOfObjProp, returnedCopy);

                    finalObj[lastArrayPoint] = copyOfObjProp;
                } else {
                    if (typeof(obj[prop]) == "object" && obj[prop] != null && obj[prop].constructor === Date)
                        finalObj[prop] = new Date(obj[prop]);
                    else
                        finalObj[prop] = obj[prop];
                }
            }

            if (Array.isArray(obj))
                return finalArrayObj;
            else
                return finalObj;
        } else
            return null;
    } catch (err) {
        console.error(`Failed to create deep copy of supplied object due to: ${err}`);
    }
};

/*
 * Used to normalize a JSON dataset. Any keys(at any given level) of an object satisfying following conditions:
 * ----------------------
 * (a) obj[key] == '',
 * (b) obj[key] == 0,
 * (c) obj[key] == false
 * ----------------------
 * are treated as null(s) & then recursively deleted/removed from the keyset.
 * This reduces the overhead of the data getting passed to-and-fro from UI to backend & vice-versa as well as
 * also assists for validation & handling of the dataset as the focus is only on the dataset that is not null
 */
App.normalizeJSON = function(obj) {
    try {
        if (obj && typeof(obj) == "object") {
            App.setNullForEmptyObjectKeys(obj);
            App.setEmptyForArraysForNullDataset(obj);
            App.setEmptyForObjectsForNullDataset(obj);
            App.deleteNullOnObjectKeys(obj);
            App.cleanEmptyObjects(obj);

            if (Array.isArray(obj) && JSON.stringify(obj) == '[null]')
                obj = [];
        } else
            return null;
    } catch (err) {
        console.error(`Failed to normalize JSON object due to: ${err}`);
    }
};

App.setNullForEmptyObjectKeys = function(obj) {
    for (prop in obj) {
        if (typeof(obj[prop]) == "object" && obj[prop] != null && obj[prop].constructor !== Date) {
            App.setNullForEmptyObjectKeys(obj[prop]);
        } else {
            if (!obj[prop] || obj[prop] == "" || obj[prop] == 0) {
                if (typeof(obj[prop]) == "boolean")
                    obj[prop] = false;
                else
                    obj[prop] = null;
            }
        }
    }
};

App.deleteNullOnObjectKeys = function(obj) {
    for (prop in obj) {
        if (typeof(obj[prop]) == "undefined") {
            delete obj[prop];
        } else if (typeof(obj[prop]) == "object" && obj[prop] == null) {
            delete obj[prop];
        } else if (typeof(obj[prop]) == "object" && obj[prop].constructor !== Date) {
            App.deleteNullOnObjectKeys(obj[prop]);
        } else {
            if (obj[prop] == false) {
                delete obj[prop];
            }
        }
    }
};

App.cleanEmptyObjects = function(obj) {
    for (prop in obj) {
        let array_prop = null;

        if (typeof(obj[prop]) == "object" && obj[prop] != null && Array.isArray(obj[prop])) {
            array_prop = prop;
            App.cleanEmptyObjects(obj[prop]);
        } else if (typeof(obj[prop]) == "object" && obj[prop] != null && obj[prop].constructor !== Date && JSON.stringify(obj[prop]) !== '{}') {
            App.cleanEmptyObjects(obj[prop]);
        } else if (typeof(obj[prop]) == "object" && obj[prop] != null && JSON.stringify(obj[prop]) === '{}') {
            delete obj[prop];
        }

        if (array_prop) {
            obj[array_prop] = obj[array_prop].filter((prop_in_arr) => {
                if (typeof(prop_in_arr) == "object" && JSON.stringify(prop_in_arr) !== '{}')
                    return true;
                else
                    return false;
            });
        }
    }
};

App.checkForNullDataset = function(obj) {
    let isNull = true;
    for (prop in obj) {
        if (typeof(obj[prop]) == "object" && obj[prop] != null && obj[prop].constructor !== Date) {
            isNull = isNull && App.checkForNullDataset(obj[prop]);
        } else {
            if (!obj[prop] || obj[prop] == '' || obj[prop] == 0 || (obj[prop] == false));
            else
                isNull = false;
        }
    }
    return isNull;
};

App.setEmptyForArraysForNullDataset = function(obj) {
    for (prop in obj) {
        let array_prop = null;

        if (typeof(obj[prop]) == "object" && obj[prop] != null && Array.isArray(obj[prop])) {
            array_prop = prop;
            App.setEmptyForArraysForNullDataset(obj[prop]);
        } else if (typeof(obj[prop]) == "object" && obj[prop] != null && obj[prop].constructor !== Date) {
            App.setEmptyForArraysForNullDataset(obj[prop]);
        }

        if (array_prop) {
            let isNullObj = true;

            for (let i = 0; i < obj[array_prop].length; i++) {
                isNullObj = isNullObj && App.checkForNullDataset(obj[array_prop][i]);
            }

            if (isNullObj) {
                obj[array_prop] = [];
                delete obj[array_prop];
            }
        }
    }
};

App.setEmptyForObjectsForNullDataset = function(obj) {
    for (prop in obj) {
        let isNullObj = true;
        let property = null;

        if (typeof(obj[prop]) == "object" && obj[prop] != null && !Array.isArray(obj[prop]) && obj[prop].constructor !== Date) {
            property = prop;
            App.setEmptyForObjectsForNullDataset(obj[prop]);
        } else if (typeof(obj[prop]) != "object") {
            if (!obj[prop] || obj[prop] == '' || obj[prop] == 0 || obj[prop] == false)
                delete obj[prop];
        }

        if (property) {
            isNullObj = App.checkForNullDataset(obj[property]);

            if (isNullObj) {
                delete obj[property];
            }
        }
    }
};
/* Custom utility methods for normalizing JSON datasets for NULL datasets (END) */

/*
 * Method to format datetime values to locale & timezone based string.
 * Supported for following date string patterns/js timestamp/js date
 * (a)YYYY-MM-ddTHH:mm:ss (java.time.LocalDateTime)
 * (b)YYYY-MM-ddTHH:mm:ss.SSS (java.time.ZonedDateTime)
 * (c)YYYY-MM-ddTHH:mm:ss.SSS+ZZ:ZZ (java.sql.Timestamp)
 */
App.localizeTimestampToAppTimezone = function(dateTimeValue) {

    if (dateTimeValue && (typeof dateTimeValue === 'string')) {
        if (new Date(dateTimeValue).toString() === 'Invalid Date') {
            console.warn('Invalid datetime value supplied for localization');
            return '';
        } else {
            let dateValueTrimmed = null;


            if (dateTimeValue.length === 10) {
                return new Date(dateTimeValue).toLocaleString(App.getCurrentLocale(), {
                    timeZone: App.timeZone || 'UTC',
                    hour12: false,
                    year: 'numeric',
                    month: '2-digit',
                    day: '2-digit',
                    hour: '2-digit',
                    minute: '2-digit',
                    second: '2-digit',
                    timeZoneName: 'short'

                });
            } else if (dateTimeValue.length >= 19 && (dateTimeValue.length === 19 || dateTimeValue.length === 23 || dateTimeValue.length === 29))
                dateValueTrimmed = dateTimeValue.substring(0, 19).trim();

            //process formatting
            if (dateValueTrimmed) {
                dateValueTrimmed = dateValueTrimmed + 'Z';
                return new Date(dateValueTrimmed).toLocaleString(App.getCurrentLocale(), {
                    timeZone: App.timeZone || 'UTC',
                    hour12: false,
                    year: 'numeric',
                    month: '2-digit',
                    day: '2-digit',
                    hour: '2-digit',
                    minute: '2-digit',
                    second: '2-digit',
                    timeZoneName: 'short'
                });
            } else {
                console.warn('Unable to format datetime value for localization');
                return '';
            }
        }
    } else if (dateTimeValue && (typeof dateTimeValue === 'number')) {
        return new Date(dateTimeValue).toLocaleString(App.getCurrentLocale(), {
            timeZone: App.timeZone || 'UTC',
            hour12: false,
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit',
            timeZoneName: 'short'
        });
    } else if (dateTimeValue && (typeof dateTimeValue === 'object') && (dateTimeValue.constructor === Date)) {
        return dateTimeValue.toLocaleString(App.getCurrentLocale(), {
            timeZone: App.timeZone || 'UTC',
            hour12: false,
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit',
            timeZoneName: 'short'
        });
    } else {
        console.warn("Unable to perform datetime localization. Supplied datetime value is not a valid 'string/number/Date object'");
        return '';
    }
};


/*
 * Method to get the User's or Default timezone to be used accross APP for time value localization
 */
App.getUserTimeZone = async function() {
    try {
        if (!App.timeZone) {
            await App.Variables.sv_getUserTimezone.invoke();
            App.timeZone = App.Variables.sv_getUserTimezone.dataSet.value;
        }
    } catch (err) {
        console.error("Something failed while trying to fetch user timezone (falling back to 'UTC'), due to error: " + err);
        App.timeZone = "UTC";
    }

    console.log('Application level timezone for current user is set to: ' + App.timeZone);
};

/*
 * Method to load USER's app level permissions (Controls APP's access levels for the entire user's browser session)
 * Note: This is an async method. Loads the user's APP permission into the browser session.
 * Ideally meant to be called on the 'onReady()' callback of any Partial/Page. 
 * But can be called elsewhere as well since this works in conjuction with browser's cache.
 *
 */
App.loadAppPermissions = async function() {
    App.permissionsLoaded = false;

    try {
        if (!cache_utils.isStoredInCache("SessionStorage", "Permissions", "APP_PERMISSIONS", App.Variables.PermissionsForLoggedInUserId)) {
            await App.Variables.PermissionsForLoggedInUserId.invoke().then((data) => {
                if (!data)
                    throw " Failed to fetch APP permissions";
            }).catch((err) => {
                throw err;
            });
            debugger;
            //  when no permissions assigned to user / user is inactive
            // if (App.Variables.PermissionsForLoggedInUserId.dataSet.length === 0) {
            if (App.Variables.PermissionsForLoggedInUserId.dataSet.length === 0) {
                App.permissionsLoaded = false;
                window.location.href = '#/ErrorLanding';
            } else {
                cache_utils.storeInCache("SessionStorage", "Permissions", "APP_PERMISSIONS", App.Variables.PermissionsForLoggedInUserId);

                //storing the app permissions to MAP
                App.userPermission = new Map();
                App.Variables.PermissionsForLoggedInUserId.dataSet.forEach(function(source) {
                    App.userPermission.set(source.name, 1);
                });
                App.permissionsLoaded = true;
                debugger;
            }

        } else {
            cache_utils.fetchUsingCache(App, "SessionStorage", "Permissions", "APP_PERMISSIONS", App.Variables.PermissionsForLoggedInUserId);

            if (!App.userPermission || App.userPermission.size === 0) {
                //storing the app permissions to MAP
                App.userPermission = new Map();
                App.Variables.PermissionsForLoggedInUserId.dataSet.forEach(function(source) {
                    App.userPermission.set(source.name, 1);
                });
                App.permissionsLoaded = true;
            } else {
                const mapKeys = Array.from(App.userPermission.keys()).join();
                const perNames = App.Variables.PermissionsForLoggedInUserId.dataSet.map(source => source.name).join();

                if (mapKeys !== perNames) {
                    //storing the app permissions to MAP
                    App.userPermission = new Map();
                    App.Variables.PermissionsForLoggedInUserId.dataSet.forEach(function(source) {
                        App.userPermission.set(source.name, 1);
                    });
                }
                App.permissionsLoaded = true;
            }

        }
    } catch (err) {
        console.error("Something failed while initializing App permissions with following error: " + err);

        //removing all stored keys from the session
        sessionStorage.removeItem('APP_PERMISSIONS');

        App.permissionsLoaded = false;
        App.userPermission = null;
        window.location.href = '#/ErrorLanding';
    }
};

//methods for loading FILTER fields for QUEUE configuration - STARTS here
App.initializeQueueFilterFields = async function() {
    App.queueFiltersLoaded = false;

    try {
        console.log("Initializing QUEUE filter configuration...");
        await loadQueueFilterFieldsAsync();
        App.queueFiltersLoaded = true;
        console.log("QUEUE filter configuration loaded...");
    } catch (err) {
        console.error("Something failed while initializing Queue filters with following error: " + err);

        //removing all QUEUE filters related cached keys from the session
        const queueFilterKeys = Object.keys(sessionStorage);
        if (Array.isArray(queueFilterKeys)) {
            queueFilterKeys.forEach((keyName) => {
                if (keyName.startsWith('Queue_CFGFilter_'))
                    sessionStorage.removeItem(keyName);
            });
        }

        App.queueFiltersLoaded = false;
        App.Variables.queueFieldsMV.dataSet = [];
        // window.location.href = '#/ErrorLanding';
    }
};

async function loadQueueFilterFieldsAsync() {
    App.Variables.queueFieldsMV.dataSet = [];
    try {
        //wait until basic filters are loaded
        await loadBasicQueueFieldsAsync();

        //load all domain value filter fields
        console.log("Loading DomainValue QUEUE filters...");

        /*
         * 1. For Application status
         */
        if (!cache_utils.isStoredInCache("SessionStorage", "Queues", "Queue_CFGFilter_AppStatusDV", App.Variables.QCFG_AppStatus_DV)) {
            await App.Variables.QCFG_AppStatus_DV.invoke().then((data) => {
                if (!data)
                    throw "Failed to configure Queue filters";
            }).catch((err) => {
                throw err;
            });
            cache_utils.storeInCache("SessionStorage", "Queues", "Queue_CFGFilter_AppStatusDV", App.Variables.QCFG_AppStatus_DV);
        } else {
            cache_utils.fetchUsingCache(App, "SessionStorage", "Queues", "Queue_CFGFilter_AppStatusDV", App.Variables.QCFG_AppStatus_DV);
        }

        //preparing the filter value list
        let appStatus_list = [];
        Object.assign(appStatus_list, App.Variables.QCFG_AppStatus_DV.dataSet);
        let appStatus_filterConfigVal = [];
        appStatus_list.forEach((appStatusDV) => {
            let filterObj = {};
            filterObj[appStatusDV.id] = appStatusDV.description;
            appStatus_filterConfigVal.push(filterObj);
        });

        let FILTER_APPSTATUS = {
            "id": "APPLICATION_DETAILS.applicationStatus",
            "data": {
                "title": "Application Status"
            },
            "type": "integer",
            "input": "select",
            "operators": [
                "is equal to",
                "is not equal to",
                "is empty",
                "is not empty"
            ],
            "values": appStatus_filterConfigVal
        };
        App.Variables.queueFieldsMV.dataSet.push(FILTER_APPSTATUS);

        /*
         * 2. For CreditReview type
         */
        if (!cache_utils.isStoredInCache("SessionStorage", "Queues", "Queue_CFGFilter_CreditReviewTypeDV", App.Variables.QCFG_CaseReviewType_DV)) {
            await App.Variables.QCFG_CaseReviewType_DV.invoke().then((data) => {
                if (!data)
                    throw "Failed to configure Queue filters";
            }).catch((err) => {
                throw err;
            });
            cache_utils.storeInCache("SessionStorage", "Queues", "Queue_CFGFilter_CreditReviewTypeDV", App.Variables.QCFG_CaseReviewType_DV);
        } else {
            cache_utils.fetchUsingCache(App, "SessionStorage", "Queues", "Queue_CFGFilter_CreditReviewTypeDV", App.Variables.QCFG_CaseReviewType_DV);
        }

        //preparing the filter value list
        let crType_list = [];
        Object.assign(crType_list, App.Variables.QCFG_CaseReviewType_DV.dataSet);
        let crType_filterConfigVal = [];
        crType_list.forEach((crTypeDV) => {
            let filterObj = {};
            filterObj[crTypeDV.id] = crTypeDV.description;
            crType_filterConfigVal.push(filterObj);
        });

        let FILTER_CREDITREVIEW_TYPE = {
            "id": "CaseReview.reviewType",
            "data": {
                "title": "Case review type"
            },
            "type": "integer",
            "input": "select",
            "operators": [
                "is equal to",
                "is not equal to",
                "is empty",
                "is not empty"
            ],
            "values": crType_filterConfigVal
        };
        App.Variables.queueFieldsMV.dataSet.push(FILTER_CREDITREVIEW_TYPE);

        /*
         * 3. For Application channel
         */
        if (!cache_utils.isStoredInCache("SessionStorage", "Queues", "Queue_CFGFilter_AppChannelDV", App.Variables.QCFG_AppChannel_DV)) {
            await App.Variables.QCFG_AppChannel_DV.invoke().then((data) => {
                if (!data)
                    throw "Failed to configure Queue filters";
            }).catch((err) => {
                throw err;
            });
            cache_utils.storeInCache("SessionStorage", "Queues", "Queue_CFGFilter_AppChannelDV", App.Variables.QCFG_AppChannel_DV);
        } else {
            cache_utils.fetchUsingCache(App, "SessionStorage", "Queues", "Queue_CFGFilter_AppChannelDV", App.Variables.QCFG_AppChannel_DV);
        }

        //preparing the filter value list
        let channel_list = [];
        Object.assign(channel_list, App.Variables.QCFG_AppChannel_DV.dataSet);
        let channel_filterConfigVal = [];
        channel_list.forEach((channelDV) => {
            let filterObj = {};
            filterObj[channelDV.id] = channelDV.description;
            channel_filterConfigVal.push(filterObj);
        });

        let FILTER_CHANNEL = {
            "id": "APPLICATION_DETAILS.channel",
            "data": {
                "title": "Channel"
            },
            "type": "integer",
            "input": "select",
            "operators": [
                "is equal to",
                "is not equal to",
                "is empty",
                "is not empty"
            ],
            "values": channel_filterConfigVal
        };
        App.Variables.queueFieldsMV.dataSet.push(FILTER_CHANNEL);

        /*
         * 4. For Application preferred language
         */
        if (!cache_utils.isStoredInCache("SessionStorage", "Queues", "Queue_CFGFilter_AppPrefLangDV", App.Variables.QCFG_AppPrefLang_DV)) {
            await App.Variables.QCFG_AppPrefLang_DV.invoke().then((data) => {
                if (!data)
                    throw "Failed to configure Queue filters";
            }).catch((err) => {
                throw err;
            });
            cache_utils.storeInCache("SessionStorage", "Queues", "Queue_CFGFilter_AppPrefLangDV", App.Variables.QCFG_AppPrefLang_DV);
        } else {
            cache_utils.fetchUsingCache(App, "SessionStorage", "Queues", "Queue_CFGFilter_AppPrefLangDV", App.Variables.QCFG_AppPrefLang_DV);
        }

        //preparing the filter value list
        let prefLang_list = [];
        Object.assign(prefLang_list, App.Variables.QCFG_AppPrefLang_DV.dataSet);
        let prefLang_filterConfigVal = [];
        prefLang_list.forEach((langDV) => {
            let filterObj = {};
            filterObj[langDV.id] = langDV.description;
            prefLang_filterConfigVal.push(filterObj);
        });

        let FILTER_APPPREF_LANG = {
            "id": "APPLICATION_DETAILS.languagecode",
            "data": {
                "title": "Application Language"
            },
            "type": "integer",
            "input": "select",
            "operators": [
                "is equal to",
                "is not equal to",
                "is empty",
                "is not empty"
            ],
            "values": prefLang_filterConfigVal
        };
        App.Variables.queueFieldsMV.dataSet.push(FILTER_APPPREF_LANG);

        /*
         * 5. For CreditReview Status
         */
        if (!cache_utils.isStoredInCache("SessionStorage", "Queues", "Queue_CFGFilter_CreditReviewStatusDV", App.Variables.QCFG_CaseReviewStatus_DV)) {
            await App.Variables.QCFG_CaseReviewStatus_DV.invoke().then((data) => {
                if (!data)
                    throw "Failed to configure Queue filters";
            }).catch((err) => {
                throw err;
            });
            cache_utils.storeInCache("SessionStorage", "Queues", "Queue_CFGFilter_CreditReviewStatusDV", App.Variables.QCFG_CaseReviewStatus_DV);
        } else {
            cache_utils.fetchUsingCache(App, "SessionStorage", "Queues", "Queue_CFGFilter_CreditReviewStatusDV", App.Variables.QCFG_CaseReviewStatus_DV);
        }

        //preparing the filter value list
        let crStatus_list = [];
        Object.assign(crStatus_list, App.Variables.QCFG_CaseReviewStatus_DV.dataSet);
        let crStatus_filterConfigVal = [];
        crStatus_list.forEach((crStatusDV) => {
            let filterObj = {};
            filterObj[crStatusDV.id] = crStatusDV.description;
            crStatus_filterConfigVal.push(filterObj);
        });

        let FILTER_CREDITREVIEW_STATUS = {
            "id": "CaseReview.status",
            "data": {
                "title": "Case review status"
            },
            "type": "integer",
            "input": "select",
            "operators": [
                "is equal to",
                "is not equal to",
                "is empty",
                "is not empty"
            ],
            "values": crStatus_filterConfigVal
        };
        App.Variables.queueFieldsMV.dataSet.push(FILTER_CREDITREVIEW_STATUS);

        /*
         * 6. For Users
         */
        if (!cache_utils.isStoredInCache("SessionStorage", "Queues", "Queue_CFGFilter_Users", App.Variables.QCFG_Users)) {
            await App.Variables.QCFG_Users.invoke().then((data) => {
                if (!data)
                    throw "Failed to configure Queue filters";
            }).catch((err) => {
                throw err;
            });
            cache_utils.storeInCache("SessionStorage", "Queues", "Queue_CFGFilter_Users", App.Variables.QCFG_Users);
        } else {
            cache_utils.fetchUsingCache(App, "SessionStorage", "Queues", "Queue_CFGFilter_Users", App.Variables.QCFG_Users);
        }

        //preparing the filter value list
        let users_list = [];
        Object.assign(users_list, App.Variables.QCFG_Users.dataSet);
        let users_filterConfigVal = [];
        users_list.forEach((user) => {
            let filterObj = {};
            filterObj[user.id] = (user.firstName + " " + user.lastName);
            users_filterConfigVal.push(filterObj);
        });

        /* 6.a -> For application lock by */
        let FILTER_APPLOCK_BY = {
            "id": "ApplicationLock.lockedBy",
            "data": {
                "title": "Application lock"
            },
            "type": "integer",
            "input": "select",
            "operators": [
                "is equal to",
                "is not equal to",
                "is empty",
                "is not empty"
            ],
            "values": users_filterConfigVal
        };
        App.Variables.queueFieldsMV.dataSet.push(FILTER_APPLOCK_BY);

        //preparing the filter value list
        let userId_filterConfigVal = [];
        users_list.forEach((user) => {
            let filterObj = {};
            filterObj[user.userId] = (user.firstName + " " + user.lastName);
            userId_filterConfigVal.push(filterObj);
        });

        /* 6.b -> For application owning officer */
        let FILTER_APP_OWNINGOFFICER = {
            "id": "APPLICATION_DETAILS.owningOfficer",
            "data": {
                "title": "Application owner"
            },
            "type": "string",
            "input": "select",
            "operators": [
                "is equal to",
                "is not equal to",
                "contains",
                "does not contain",
                "is empty",
                "is not empty"
            ],
            "values": userId_filterConfigVal
        };
        App.Variables.queueFieldsMV.dataSet.push(FILTER_APP_OWNINGOFFICER);

        /*
         * 7. For Products
         */
        if (!cache_utils.isStoredInCache("SessionStorage", "Queues", "Queue_CFGFilter_Products", App.Variables.QCFG_Products)) {
            await App.Variables.QCFG_Products.invoke().then((data) => {
                if (!data)
                    throw "Failed to configure Queue filters";
            }).catch((err) => {
                throw err;
            });
            cache_utils.storeInCache("SessionStorage", "Queues", "Queue_CFGFilter_Products", App.Variables.QCFG_Products);
        } else {
            cache_utils.fetchUsingCache(App, "SessionStorage", "Queues", "Queue_CFGFilter_Products", App.Variables.QCFG_Products);
        }

        //preparing the filter value list
        let products_list = [];
        Object.assign(products_list, App.Variables.QCFG_Products.dataSet);
        let products_filterConfigVal = [];
        products_list.forEach((product) => {
            let filterObj = {};
            filterObj[product.id] = product.name;
            products_filterConfigVal.push(filterObj);
        });

        let FILTER_PRODUCTS = {
            "id": "APPLICATION_DETAILS.requestedProduct",
            "data": {
                "title": "Requested product"
            },
            "type": "integer",
            "input": "select",
            "operators": [
                "is equal to",
                "is not equal to",
                "is empty",
                "is not empty"
            ],
            "values": products_filterConfigVal
        };
        App.Variables.queueFieldsMV.dataSet.push(FILTER_PRODUCTS);

        //sorting filter fields alphabetically
        App.Variables.queueFieldsMV.dataSet.sort((a, b) => {
            return a.data.title.localeCompare(b.data.title);
        });
    } catch (err) {
        throw err;
    }
};

async function loadBasicQueueFieldsAsync() {
    console.log("Loading basic Queue filters...");

    const FILTER_CASEREVIEW_LVL = {
        "id": "CaseReview.reviewLevel",
        "data": {
            "title": "Case review level"
        },
        "type": "integer",
        "input": "number",
        "operators": [
            "is equal to",
            "is not equal to",
            "is less than",
            "is less than or equal to",
            "is greater than",
            "is greater than or equal to"
        ]
    };

    const FILTER_APPDETAILS_CREATEDON = {
        "id": "APPLICATION_DETAILS.createdOn",
        "data": {
            "title": "Application created on"
        },
        "type": "date",
        "operators": [
            "is equal to",
            "is not equal to",
            "is less than",
            "is less than or equal to",
            "is greater than",
            "is greater than or equal to"
        ]
    };

    const FILTER_APPDETAILS_UPDATEDON = {
        "id": "APPLICATION_DETAILS.updatedOn",
        "data": {
            "title": "Application updated on"
        },
        "type": "date",
        "operators": [
            "is equal to",
            "is not equal to",
            "is less than",
            "is less than or equal to",
            "is greater than",
            "is greater than or equal to"
        ]
    };

    const FILTER_APP_LOCKEDON = {
        "id": "ApplicationLock.lockedOn",
        "data": {
            "title": "Application locked on"
        },
        "type": "date",
        "operators": [
            "is equal to",
            "is not equal to",
            "is less than",
            "is less than or equal to",
            "is greater than",
            "is greater than or equal to"
        ]
    };

    //Place on hold flag
    const FILTER_APPDETAILS_ISONHOLD = {
        "id": "APPLICATION_DETAILS.isOnHold",
        "data": {
            "title": "On Hold Flag"
        },
        "type": "boolean",
        "operators": [
            "true",
            "false"
        ]
    };

    //TD specific filters
    const FILTER_APPDETAILS_HOLDUNTIL = {
        "id": "APPLICATION_DETAILS.onHoldUntilDate",
        "data": {
            "title": "On Hold Until Date"
        },
        "type": "date",
        "operators": [
            "is equal to",
            "is not equal to",
            "is less than",
            "is less than or equal to",
            "is greater than",
            "is greater than or equal to"
        ]
    };

    //push basic queue filter's data into the app level field configuration
    App.Variables.queueFieldsMV.dataSet.push(FILTER_CASEREVIEW_LVL);
    App.Variables.queueFieldsMV.dataSet.push(FILTER_APPDETAILS_CREATEDON);
    App.Variables.queueFieldsMV.dataSet.push(FILTER_APPDETAILS_UPDATEDON);
    App.Variables.queueFieldsMV.dataSet.push(FILTER_APP_LOCKEDON);
    App.Variables.queueFieldsMV.dataSet.push(FILTER_APPDETAILS_ISONHOLD);

    //adding TD specific filters to Filter list
    App.Variables.queueFieldsMV.dataSet.push(FILTER_APPDETAILS_HOLDUNTIL);
};

//extracts only the filter configuration fields of type 'date'
App.getDateTypeFilterFields = function() {
    let dateFieldIds = [];
    if (App.Variables.queueFieldsMV.dataSet) {
        dateFields = App.Variables.queueFieldsMV.dataSet.filter((filterField) => {
            return filterField.type === 'date';
        });

        if (dateFields.length > 0) {
            dateFieldIds = dateFields.map((dateField) => {
                return dateField.id;
            });
        }
    }

    return (dateFieldIds ? dateFieldIds.join() : null);
};

function convertToSentenceCase(theString) {
    var newString = "";
    if (theString != null && theString != "") {
        newString = theString.toLowerCase().replace(/(^\s*\w|[\.\!\?]\s*\w)/g, function(c) {
            return c.toUpperCase()
        });
    }
    return newString;
};

//methods for loading FILTER fields for QUEUE configuration - ENDS here

App.getAllRolesonSuccess = function(variable, data) {

};

App.IsUserHasAccess = function(e) {
    if (App.userPermission && App.userPermission.has(e)) {
        return true;
    } else {
        return false;
    }
}

App.applicationReadOnly = function(applicationStatus) {

    if (applicationStatus != null) {
        // Create application sections
        $(document).ready(function() {
            $("[name='mv_applicationDataForm'] input, [name='mv_applicationDataForm'] select").attr('disabled', true);
            $("[name='mv_applicationDataForm1'] *").attr('disabled', true);
            // $("[name='notesForm1']").hide();
        });
        // Create application sections
        $(document).ready(function() {
            $("button[name='AuthorizedBtn']").attr('disabled', true);
            // Review application sections
            $("[name='mv_manualDecisionForm'] textarea, [name='mv_manualDecisionForm'] select").attr('disabled', true);

            $("button[name='AuthorizedBtn_Review']").attr('disabled', true);
            // AuthorizedBtn_Review
            $("span.input-group-append").hide();
            $("div[name='mv_applicationDataList1']").css({
                "pointer-events": "none",
                "opacity": "0.7"
            });

            $("[name='condition_container'] *").prop('disabled', true);
            $("[name='document_container'] *").prop('disabled', true);

            $("select[name='selectOwner']").attr('disabled', true);

            $("button[name='btnAddEditAssets']").hide()
            $("button[name='ManageLiability_btn']").hide()

            $("button[name='AddCondition_btn']").hide()
            $("button[name='AddDocument_btn']").hide()
            $("button[name='btn_pullCreditReport']").hide()
        });
    }

};

App.fetchDate = function(unformattedDate) {

    if (unformattedDate) {
        return new Date(unformattedDate).toLocaleDateString(App.appLocale.CURRENT_SELECTED_LOCALE, {
            timeZone: 'UTC',
            year: "numeric",
            month: "2-digit",
            day: "2-digit",
        });
        //return moment(unformattedDate).format(App.i18nService.appDefaults.dateFormat.toLocaleUpperCase());
    }
};

App.fetchDateTime = function(unformattedDate) {
    if (unformattedDate) {
        // console.log("came " + unformattedDate);
        var dateFixed = new Date(unformattedDate).toLocaleString(App.appLocale.CURRENT_SELECTED_LOCALE, {
            year: "numeric",
            month: "2-digit",
            day: "2-digit",
            hour: "2-digit",
            minute: "2-digit",
            second: "2-digit",
            hour12: true,
            timezone: "UTC"
        }).toUpperCase().replace(",", "");
        //const timezone = Intl.DateTimeFormat().resolvedOptions().timeZone;
        //var nwDT = App.changeTimeZone(dateFixed, "America/Chicago");
        //console.log(dateFixed + " on " + timezone + " will be  " + nwDT)
        return dateFixed;
        // return new Date(unformattedDate).toLocaleString(Page.appLocale.CURRENT_SELECTED_LOCALE).toUpperCase().replace(",", "");
    }
};

App.getOffsetStr = function(input) {
    const isoDatePattern = /(\d{4}-\d{2}-\d{2})T(\d{2}:\d{2}:\d{2})(\.\d+)?([+-]\d{2}:?\d{2}|Z)$/
    const groups = input.match(isoDatePattern);
    return groups ? groups[4] : null;
}

App.hasOffset = function(input) {
    return (!!App.getOffsetStr(input));
}

App.getUTCOffsetMinutes = function(input) {
    const offset = App.getOffsetStr(input);
    return offset ? App.parseUTCOffset(offset) : null;
}

App.parseUTCOffset = function(offsetStr) {
    if (offsetStr.toUpperCase() === 'Z') {
        return 0;
    }
    const sign = offsetStr.startsWith('-') ? -1 : 1;
    const hour = +offsetStr.slice(1, 3);
    const min = +offsetStr.slice(-2);
    return sign * hour * 60 + min;
}

App.changeTimeZone = function(date, timeZone) {
    if (typeof date === 'string') {
        return new Date(
            new Date(date).toLocaleString(App.appLocale.CURRENT_SELECTED_LOCALE, {
                timeZone,
            }),
        );
    }

    return new Date(
        date.toLocaleString(App.appLocale.CURRENT_SELECTED_LOCALE, {
            timeZone,
        }),
    );
}

App.createUseronSuccess = function(variable, data) {
    console.log("User record successfully provisioned :" + JSON.stringify(data));
};


App.createUseronError = function(variable, data) {
    console.log("User record provisioning failed :" + JSON.stringify(data));
};


App.getLoggerInUserIdentificationonSuccess = function(variable, data) {
    // console.log("Logged in user details :" + JSON.stringify(data));
    if (data.hasOwnProperty('value') && !data.value) {
        console.log("Logged in user details do not exist ,fetching user security details");
        App.Variables.getLoggedInUserDetails.invoke();
    }
};

App.getLoggerInUserIdentificationonError = function(variable, data) {
    console.log("Logged in user security details fetch failed :" + JSON.stringify(data));
};

App.getLoggedInUserDetailsonError = function(variable, data) {

};

App.getLoggedInUserDetailsonSuccess = function(variable, data) {
        console.log("getLoggedInUserDetailsonSuccess - Logged in user security details :" + JSON.stringify(data));
        if (data) {
            console.log("Provisioning user record ..")
            //     App.Variables.createUser.setInput("UserDTO", data);
            //     App.Variables.createUser.invoke();
            // }
        };

        /*
        App.logoutActiononSuccess = function(variable, data) {
            // window.location.href = "removeSession";


        };
        */