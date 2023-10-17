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

$('document').ready(() => {
    document.getElementsByTagName("html")[0].style.visibility = "hidden";
});

/* perform any action on widgets/variables within this block */
Page.onReady = function() {
    /*
     * variables can be accessed through 'Page.Variables' property here
     * e.g. to get dataSet in a staticVariable named 'loggedInUser' use following script
     * Page.Variables.loggedInUser.getData()
     *
     * widgets can be accessed through 'Page.Widgets' property here
     * e.g. to get value of text widget named 'username' use following script
     * 'Page.Widgets.username.datavalue'
     */

    initPage();
};

function initPage() {
    const intervalId = setInterval(function() {
        if (App.permissionsLoaded) {
            clearInterval(intervalId);
            console.log('Permissions loaded...');

            if (!App.IsUserHasAccess('Security_Users')) {
                window.location.href = '/ErrorLanding';
            } else {
                document.getElementsByTagName("html")[0].style.visibility = "visible";
            }
        } else {
            //determining the time elapsed since App started in minutes
            const timeElapsedSinceAppStart = moment(new Date()).diff(moment(App.appStartTime), 'minutes');

            if (timeElapsedSinceAppStart < 1)
                console.log('Waiting to load permissions...');
            else {
                clearInterval(intervalId);

                //if the active page is not 'ErrorLanding'
                if (window.location.hash !== '/ErrorLanding')
                    window.location.href = '/ErrorLanding';
            }
        }
    }, 10);
}

Page.CreateUseronSuccess = function(variable, data) {
    Page.Actions.UserSuccess.setMessage(data.value);
    Page.Actions.UserSuccess.invoke();
    Page.Widgets.FormCreateUser.reset();

    // Set role and status to default value
    Page.Widgets.FormCreateUser.formfields.role.value = 'Default';
    Page.Widgets.FormCreateUser.formfields.status.value = 'Active';
};

Page.CreateUseronError = function(variable, data, xhrObj) {
    var errorObj = JSON.parse(xhrObj.error);
    var errorMessage = errorObj;
    Page.Actions.UserFailed.setMessage(errorMessage);
    Page.Actions.UserFailed.invoke();
    Page.Actions.UserFailed.invoke();
};

Page.UpdateUseronError = function(variable, data, xhrObj) {
    var errorObj = JSON.parse(xhrObj.error);
    var errorMessage = errorObj;
    Page.Actions.UserFailed.setMessage(errorMessage);
    Page.Actions.UserFailed.invoke();
    Page.Actions.UserFailed.invoke();
};

Page.UpdateUseronSuccess = function(variable, data) {
    Page.Actions.UserSuccess.setMessage(data.value);
    Page.Actions.UserSuccess.invoke();
};

Page.DeleteUseronError = function(variable, data, xhrObj) {
    var errorObj = JSON.parse(xhrObj.error);
    var errorMessage = errorObj;
    Page.Actions.UserFailed.setMessage(errorMessage);
    Page.Actions.UserFailed.invoke();
    Page.Actions.UserFailed.invoke();
};

Page.DeleteUseronSuccess = function(variable, data) {

    alert("OnDeleteSuccess");
    Page.Actions.UserSuccess.setMessage(data.value);
    Page.Actions.UserSuccess.invoke();
    window.location.reload();
    //Page.Widgets.UserRoleTable1.redraw();
    //Page.Widgets.UserRoleTable1.refreshData();
};

Page.CreateSystemRolesonSuccess = function(variable, data) {
    if (data.value) {
        Page.Actions.UserSuccess.setMessage('System Roles Created Successfully');
    } else {
        Page.Actions.UserSuccess.setMessage('Failed to create System Roles');
    }
    Page.Actions.UserSuccess.invoke();
};

Page.ConfirmDeleteonCancel = function(variable, data) {
    Page.Widgets.deleteUser.close();
    window.location.reload();
};


Page.FormCreateUserBeforesubmit = function($event, widget, $data) {
    function isValidData($data) {
        if ($data.firstName) {
            if ($data.firstName.match('<\/?[^>]*>')) {
                Page.Actions.UserFailed.setMessage('First Name value is not valid');
                return false;
            }
        }

        if ($data.lastName) {
            if ($data.lastName.match('<\/?[^>]*>')) {
                Page.Actions.UserFailed.setMessage('Last Name value is not valid');
                return false;
            }
        }

        if ($data.email) {
            if ($data.email.match('<\/?[^>]*>')) {
                Page.Actions.UserFailed.setMessage('Email value is not valid');
                return false;
            }
        }

        if ($data.userId) {
            if ($data.lastName.match('<\/?[^>]*>')) {
                Page.Actions.UserFailed.setMessage('User Id value is not valid');
                return false;
            }
        }

        return true;
    }

    if (!isValidData($data)) {
        //There is an issue with loading toast popup, so invoking it twice
        Page.Actions.UserFailed.invoke();
        Page.Actions.UserFailed.invoke();
        return false;
    }

    return true;
};
//HTML Injection check before submitting update user form
Page.FormEditUserBeforesubmit = function($event, widget, $data) {
    function isValidData($data) {
        if ($data.firstName) {
            if ($data.firstName.match('<\/?[^>]*>')) {
                Page.Actions.UserFailed.setMessage('First Name value is not valid');
                return false;
            }
        }

        if ($data.lastName) {
            if ($data.lastName.match('<\/?[^>]*>')) {
                Page.Actions.UserFailed.setMessage('Last Name value is not valid');
                return false;
            }
        }

        if ($data.email) {
            if ($data.email.match('<\/?[^>]*>')) {
                Page.Actions.UserFailed.setMessage('Email value is not valid');
                return false;
            }
        }

        return true;
    }

    if (!isValidData($data)) {
        //There is an issue with loading toast popup, so invoking it twice
        Page.Actions.UserFailed.invoke();
        Page.Actions.UserFailed.invoke();
        return false;
    }

    return true;
};

Page.ConfirmDeleteonOk = function(variable, data) {
    alert("OnDelete");
};

Page.ConfirmDeleteonClose = function(variable, data) {
    alert("OnConfirmClose");
};