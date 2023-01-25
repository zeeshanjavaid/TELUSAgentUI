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

const fixedPageSize = 20;
const paginationEventName = "fi-paginationEvent";
Page.SEARCH_RESULTS_FOR_DAYS = 30;
Page.showErrorMsg = false;
Page.firstNameErrorMsg = false;
Page.MAX_NUMBER_OF_APPLICATIONS = 3;
Page.previousApplicationId = 0;

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
    //setting sitE properties
    // Page.Variables.getSitePropertyByName.setInput('name', 'SEARCH_RESULTS_FOR_DAYS');

    initPage();
};

function initPage() {
    const intervalId = setInterval(function() {
        if (App.permissionsLoaded) {
            clearInterval(intervalId);
            console.log('Permissions loaded...');

            if (!App.IsUserHasAccess('Access_ApplicationSearch')) {
                window.location.href = "#/ErrorLanding"
            } else {
                document.getElementsByTagName("html")[0].style.visibility = "visible";

                Page.Variables.getSitePropertyByName.setInput('name', 'MAX_NUMBER_OF_APPLICATIONS');
                Page.Variables.getSitePropertyByName.invoke();

                //listening to pagination events from partial
                let paginationContainer = document.querySelector("div[name='appSearch_Pagination']");
                paginationContainer.addEventListener(paginationEventName, (ev) => {
                    Page.previousApplicationId = 0;
                    //get the search application variable
                    let sv_searchAPI = Page.Variables.sv_applicationSearch;
                    //set inputs
                    sv_searchAPI.setInput({
                        "sortingColumn": Page.Variables.mv_sortProperties.getData().sortingColumn,
                        "sortingOrder": Page.Variables.mv_sortProperties.getData().sortingOrder,
                        "applicationLockedByUser": 'SYSTEM',
                        "applicationCreatedBy": 'SYSTEM',
                        "applicationUpdatedByUser": 'SYSTEM',
                        "applicationNumber": App.Variables.mv_searchFilters.getData().referenceNumber,
                        "firstName": App.Variables.mv_searchFilters.getData().firstName + "%",
                        "lastName": App.Variables.mv_searchFilters.getData().lastName + "%",
                        "phoneNumber": App.Variables.mv_searchFilters.getData().phoneNumber + "%",
                        "userLocale": Page.Variables.mv_userLocale.getValue('dataValue'),
                        "idNumber": App.Variables.mv_searchFilters.getData().advancedFilters.personIdNumber,
                        "postalCode": App.Variables.mv_searchFilters.getData().advancedFilters.postalCode,
                        "appStatus": App.Variables.mv_searchFilters.getData().advancedFilters.applicationStatus,
                        "dob": App.Variables.mv_searchFilters.getData().advancedFilters.dateOfBirth,
                        "email": App.Variables.mv_searchFilters.getData().advancedFilters.email,
                        "channel": App.Variables.mv_searchFilters.getData().advancedFilters.channel,
                        "employer": App.Variables.mv_searchFilters.getData().advancedFilters.employer,
                        "createdOnStart": (App.Variables.mv_searchFilters.getData().advancedFilters.createdOnStart ? new Date(App.Variables.mv_searchFilters.getData().advancedFilters.createdOnStart).getTime() : null),
                        "createdOnEnd": (App.Variables.mv_searchFilters.getData().advancedFilters.createdOnEnd ? new Date(App.Variables.mv_searchFilters.getData().advancedFilters.createdOnEnd).getTime() : new Date().getTime()),
                        "currentPage": ev.detail.pageNumber,
                        "pageSize": Page.Variables.mv_pageSize.getValue('dataValue')
                    });
                    //invoke application search API
                    sv_searchAPI.invoke();
                });

                //listening to search inputs key-up events
                $("select.searchInput").on("keydown", (ev) => {
                    //if 'ENTER' is keyed -> invoke search
                    if (ev.keyCode && ev.keyCode === 13)
                        Page.searchApplications_btnClick(ev, Page.Widgets.searchApplications_btn);
                });

                $(".searchInput").not("select").on("keyup", (ev) => {
                    //if 'ENTER' is keyed -> invoke search
                    if (ev.keyCode && ev.keyCode === 13)
                        Page.searchApplications_btnClick(ev, Page.Widgets.searchApplications_btn);
                });

                //style panels
                $("div[class~='panel-section']").each((index, panel) => {
                    App.togglePanelIcons(panel);
                });

                //set user locale
                Page.Variables.mv_userLocale.setValue('dataValue', (App.getCurrentLocale() ? App.getCurrentLocale() : 'en'));

                //set up default values
                Page.setDefaults();
                //set up default pagination values
                Page.setPaginationDefaults();
            }
        } else {
            //determining the time elapsed since App started in minutes
            const timeElapsedSinceAppStart = moment(new Date()).diff(moment(App.appStartTime), 'minutes');

            if (timeElapsedSinceAppStart < 2)
                console.log('Waiting to load permissions...');
            else {
                clearInterval(intervalId);

                //if the active page is not 'ErrorLanding'
                if (window.location.hash !== '#/ErrorLanding')
                    window.location.href = '#/ErrorLanding';
            }
        }
    }, 10);
}

/*utility functions */

Page.setPaginationDefaults = function() {
    //pagination related
    Page.Variables.mv_currentPage.setValue('dataValue', 0);
    Page.Variables.mv_pageSize.setValue('dataValue', fixedPageSize);
    Page.Variables.mv_totalRecords.setValue('dataValue', 0);

    if (!Page.Variables.mv_sortProperties.getData()) {
        Page.Variables.mv_sortProperties.setData({
            "sortingColumn": "DEFAULT",
            "sortingOrder": ""
        });
    }
};

Page.setDefaults = function() {
    //simple filters
    App.Variables.mv_searchFilters.getData().referenceNumber = App.Variables.mv_searchFilters.getData().referenceNumber ? App.Variables.mv_searchFilters.getData().referenceNumber : null;
    App.Variables.mv_searchFilters.getData().firstName = App.Variables.mv_searchFilters.getData().firstName ? App.Variables.mv_searchFilters.getData().firstName : '';
    App.Variables.mv_searchFilters.getData().lastName = App.Variables.mv_searchFilters.getData().lastName ? App.Variables.mv_searchFilters.getData().lastName : '';
    App.Variables.mv_searchFilters.getData().phoneNumber = App.Variables.mv_searchFilters.getData().phoneNumber ? App.Variables.mv_searchFilters.getData().phoneNumber : '';

    //advanced filters
    App.Variables.mv_searchFilters.getData().advancedFilters.personIdNumber = App.Variables.mv_searchFilters.getData().advancedFilters.personIdNumber ? App.Variables.mv_searchFilters.getData().advancedFilters.personIdNumber : null;
    App.Variables.mv_searchFilters.getData().advancedFilters.postalCode = App.Variables.mv_searchFilters.getData().advancedFilters.postalCode ? App.Variables.mv_searchFilters.getData().advancedFilters.postalCode : null;
    App.Variables.mv_searchFilters.getData().advancedFilters.applicationStatus = App.Variables.mv_searchFilters.getData().advancedFilters.applicationStatus ? App.Variables.mv_searchFilters.getData().advancedFilters.applicationStatus : null;
    App.Variables.mv_searchFilters.getData().advancedFilters.dateOfBirth = App.Variables.mv_searchFilters.getData().advancedFilters.dateOfBirth ? App.Variables.mv_searchFilters.getData().advancedFilters.dateOfBirth : null;
    App.Variables.mv_searchFilters.getData().advancedFilters.email = App.Variables.mv_searchFilters.getData().advancedFilters.email ? App.Variables.mv_searchFilters.getData().advancedFilters.email : null;
    App.Variables.mv_searchFilters.getData().advancedFilters.channel = App.Variables.mv_searchFilters.getData().advancedFilters.channel ? App.Variables.mv_searchFilters.getData().advancedFilters.channel : null;
    App.Variables.mv_searchFilters.getData().advancedFilters.employer = App.Variables.mv_searchFilters.getData().advancedFilters.employer ? App.Variables.mv_searchFilters.getData().advancedFilters.employer : null;
    App.Variables.mv_searchFilters.getData().advancedFilters.createdOnStart = App.Variables.mv_searchFilters.getData().advancedFilters.createdOnStart ? App.Variables.mv_searchFilters.getData().advancedFilters.createdOnStart : null;
    App.Variables.mv_searchFilters.getData().advancedFilters.createdOnEnd = App.Variables.mv_searchFilters.getData().advancedFilters.createdOnEnd ? App.Variables.mv_searchFilters.getData().advancedFilters.createdOnEnd : null;

    //setting the 'createdOn' start value to 30days before from current/today's date
    /* let today = new Date();
     today.setDate(today.getDate() - Page.SEARCH_RESULTS_FOR_DAYS);
     today.setHours(0, 0, 0, 0);
     App.Variables.mv_searchFilters.getData().advancedFilters.createdOnStart = App.Variables.mv_searchFilters.getData().advancedFilters.createdOnStart ? App.Variables.mv_searchFilters.getData().advancedFilters.createdOnStart : today; */
};

Page.setPartyDisplayName = function(firstName, lastName) {
    if (!firstName) {
        if (!lastName)
            return "";
        else
            return lastName;
    } else {
        if (!lastName)
            return firstName;
        else
            return firstName + " " + lastName;
    }
};

// Page.dateDisplayFormat = function(dateTimeStrValue) {
//     if (dateTimeStrValue) {
//         const extractedDate = dateTimeStrValue.substring(0, 23) + "Z";
//         const formattedDate = moment(new Date(extractedDate)).format('YYYY-MM-DD hh:mm:ss A');
//         return formattedDate;
//     } else
//         return '';
// }

// Page.setTimestampDisplayFm = function(timestampValue) {
//     if (timestampValue) {
//         //check length
//         if (timestampValue.length === 29) {
//             let formatttedValue = "";
//             let timeZonePart = timestampValue.substring(timestampValue.length - 6);
//             let partBeforeTimeZone = timestampValue.substring(0, timestampValue.length - 6);
//             timeZonePart = timeZonePart.trim().replace(":", "");
//             partBeforeTimeZone = partBeforeTimeZone.trim().replace(".", ":");

//             formatttedValue = partBeforeTimeZone.concat(timeZonePart);
//             return formatttedValue;
//         } else
//             return timestamp;
//     }

//     return "";
// };

Page.maskPersonIDNumber = function(personIdNumber) {
    if (personIdNumber) {
        let regex = /.(?=.{4,}$)/gmi;
        let match = personIdNumber.match(regex);

        //check for regex match
        if (match)
            return personIdNumber.replaceAll(regex, "*");
        else
            return personIdNumber;
    } else
        return "";
};

Page.displayPageInfo = function() {
    if (Page.Variables.mv_totalRecords.getValue('dataValue') > 0) {
        let start = (Page.Variables.mv_currentPage.getValue('dataValue') * fixedPageSize) + 1;
        let end = ((start + (fixedPageSize - 1)) > Page.Variables.mv_totalRecords.getValue('dataValue')) ?
            Page.Variables.mv_totalRecords.getValue('dataValue') : (start + (fixedPageSize - 1));

        return start + " to " + end + " from " + (Page.Variables.mv_totalRecords.getValue('dataValue')) + " Applications";
    } else
        return "No Applications";
};

Page.navigateToAppBroker = function(applicationId, applicationStatusId) {
    return "#/ApplicationBroker?applicationId=" + applicationId + "&applicationStatusId=" + applicationStatusId;
};

//for JAVA API support only
Page.formatDateToString = function(dateValue) {
    if (dateValue) {
        if (dateValue.constructor.name === "Date") {
            let dateAsString = null;
            dateAsString = dateValue.getFullYear() + "-" +
                ((dateValue.getMonth() + 1) < 10 ? "0" + (dateValue.getMonth() + 1) : (dateValue.getMonth() + 1)) + "-" +
                (dateValue.getDate() < 10 ? "0" + dateValue.getDate() : dateValue.getDate()) + " " +
                (dateValue.getHours() < 10 ? "0" + dateValue.getHours() : dateValue.getHours()) + ":" +
                (dateValue.getMinutes() < 10 ? "0" + dateValue.getMinutes() : dateValue.getMinutes()) + ":" +
                (dateValue.getSeconds() < 10 ? "0" + dateValue.getSeconds() : dateValue.getSeconds()) + ":" +
                (dateValue.getMilliseconds() < 100 && dateValue.getMilliseconds() > 9 ? "0" + dateValue.getMilliseconds() :
                    (dateValue.getMilliseconds() < 10 ? "00" + dateValue.getMilliseconds() : dateValue.getMilliseconds()));

            return dateAsString;
        } else {
            //  let regex = /^\d{4}-\d{2}-\d{2}\s{1}\d{2}:\d{2}:\d{2}:\d{3}$/gmi;
            //    if (dateValue.match(regex))
            return dateValue;
        }
    } else
        return null;
}

/*UI level funtions*/

Page.advancedFilters_PanelExpand = function($event, widget, item, currentItemWidgets) {
    App.togglePanelIcons(widget.$element, "expand");
};

Page.advancedFilters_PanelCollapse = function($event, widget, item, currentItemWidgets) {
    App.togglePanelIcons(widget.$element, "collapse");
};

Page.clearInputs_btnClick = function($event, widget) {
    //clear input fields
    //simple filters
    App.Variables.mv_searchFilters.getData().referenceNumber = null;
    App.Variables.mv_searchFilters.getData().firstName = '';
    App.Variables.mv_searchFilters.getData().lastName = '';
    App.Variables.mv_searchFilters.getData().phoneNumber = '';

    //advanced filters
    App.Variables.mv_searchFilters.getData().advancedFilters.personIdNumber = null;
    App.Variables.mv_searchFilters.getData().advancedFilters.postalCode = null;
    App.Variables.mv_searchFilters.getData().advancedFilters.applicationStatus = null;
    App.Variables.mv_searchFilters.getData().advancedFilters.dateOfBirth = null;
    App.Variables.mv_searchFilters.getData().advancedFilters.email = null;
    App.Variables.mv_searchFilters.getData().advancedFilters.channel = null;
    App.Variables.mv_searchFilters.getData().advancedFilters.employer = null;
    App.Variables.mv_searchFilters.getData().advancedFilters.createdOnEnd = null;
    //App.Variables.mv_searchFilters.getData().advancedFilters.createdOnStart = null;

    //setting the 'createdOn' start value to 30days before from current/today's date
    let today = new Date();
    //  today.setDate(today.getDate() - Page.SEARCH_RESULTS_FOR_DAYS);
    today.setDate(today.getDate());
    today.setHours(0, 0, 0, 0);
    App.Variables.mv_searchFilters.getData().advancedFilters.createdOnStart = null;

};

Page.searchApplications_btnClick = function($event, widget) {
    Page.errorMsg = "";
    Page.showErrorMsg = false;
    Page.previousApplicationId = 0;

    if ((App.Variables.mv_searchFilters.getData().referenceNumber == null || App.Variables.mv_searchFilters.getData().referenceNumber == "") && (App.Variables.mv_searchFilters.getData() == null || App.Variables.mv_searchFilters.getData() == "") &&
        (App.Variables.mv_searchFilters.getData().firstName == "" || App.Variables.mv_searchFilters.getData().firstName == null) && (App.Variables.mv_searchFilters.getData().lastName == "" || App.Variables.mv_searchFilters.getData().lastName == null) && (App.Variables.mv_searchFilters.getData().phoneNumber == "" || App.Variables.mv_searchFilters.getData().phoneNumber == null) && (App.Variables.mv_searchFilters.getData().advancedFilters.personIdNumber == null || App.Variables.mv_searchFilters.getData().advancedFilters.personIdNumber == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.postalCode == null || App.Variables.mv_searchFilters.getData().advancedFilters.postalCode == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.applicationStatus == null || App.Variables.mv_searchFilters.getData().advancedFilters.applicationStatus == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.dateOfBirth == null || App.Variables.mv_searchFilters.getData().advancedFilters.dateOfBirth == "") &&
        (App.Variables.mv_searchFilters.getData().advancedFilters.email == null || App.Variables.mv_searchFilters.getData().advancedFilters.email == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.channel == null || App.Variables.mv_searchFilters.getData().advancedFilters.channel == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.employer == null || App.Variables.mv_searchFilters.getData().advancedFilters.employer == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.createdOnStart == null || App.Variables.mv_searchFilters.getData().advancedFilters.createdOnStart == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.createdOnEnd == null || App.Variables.mv_searchFilters.getData().advancedFilters.createdOnEnd == "")) {

        Page.Widgets.message1.show = true;
        Page.errorMsg = "At least one field is required for search";
        Page.showErrorMsg = true;
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });

    }

    console.log(App.Variables.mv_searchFilters);
    if ((App.Variables.mv_searchFilters.getData().firstName != null && App.Variables.mv_searchFilters.getData().firstName != "") && ((
                App.Variables.mv_searchFilters.getData().referenceNumber == null || App.Variables.mv_searchFilters.getData().referenceNumber == "") && (App.Variables.mv_searchFilters.getData() == null || App.Variables.mv_searchFilters.getData() == "") &&
            (App.Variables.mv_searchFilters.getData().lastName == "" || App.Variables.mv_searchFilters.getData().lastName == null) && (App.Variables.mv_searchFilters.getData().phoneNumber == "" || App.Variables.mv_searchFilters.getData().phoneNumber == null) && (App.Variables.mv_searchFilters.getData().advancedFilters.personIdNumber == null || App.Variables.mv_searchFilters.getData().advancedFilters.personIdNumber == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.postalCode == null || App.Variables.mv_searchFilters.getData().advancedFilters.postalCode == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.applicationStatus == null || App.Variables.mv_searchFilters.getData().advancedFilters.applicationStatus == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.dateOfBirth == null || App.Variables.mv_searchFilters.getData().advancedFilters.dateOfBirth == "") &&
            (App.Variables.mv_searchFilters.getData().advancedFilters.email == null || App.Variables.mv_searchFilters.getData().advancedFilters.email == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.channel == null || App.Variables.mv_searchFilters.getData().advancedFilters.channel == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.employer == null || App.Variables.mv_searchFilters.getData().advancedFilters.employer == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.createdOnStart == null || App.Variables.mv_searchFilters.getData().advancedFilters.createdOnStart == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.createdOnEnd == null || App.Variables.mv_searchFilters.getData().advancedFilters.createdOnEnd == ""))) {

        Page.Widgets.message1.show = true;
        if (Page.errorMsg != "") {
            Page.errorMsg = Page.errorMsg + "<br/>" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;First Name cannot be used without another field, please enter at least one additional search criteria";
        } else {
            Page.errorMsg = "First Name cannot be used without another field, please enter at least one additional search criteria";
        }
        //Page.errorMsg = "First Name cannot be used without another field, please enter at least one additional search criteria";
        Page.showErrorMsg = true;
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });


    }
    if ((App.Variables.mv_searchFilters.getData().lastName != null && App.Variables.mv_searchFilters.getData().lastName != "") && (
            (App.Variables.mv_searchFilters.getData().referenceNumber == null || App.Variables.mv_searchFilters.getData().referenceNumber == "") && (App.Variables.mv_searchFilters.getData() == null || App.Variables.mv_searchFilters.getData() == "") &&
            (App.Variables.mv_searchFilters.getData().firstName == "" || App.Variables.mv_searchFilters.getData().firstName == null) && (App.Variables.mv_searchFilters.getData().phoneNumber == "" || App.Variables.mv_searchFilters.getData().phoneNumber == null) && (App.Variables.mv_searchFilters.getData().advancedFilters.personIdNumber == null || App.Variables.mv_searchFilters.getData().advancedFilters.personIdNumber == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.postalCode == null || App.Variables.mv_searchFilters.getData().advancedFilters.postalCode == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.applicationStatus == null || App.Variables.mv_searchFilters.getData().advancedFilters.applicationStatus == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.dateOfBirth == null || App.Variables.mv_searchFilters.getData().advancedFilters.dateOfBirth == "") &&
            (App.Variables.mv_searchFilters.getData().advancedFilters.email == null || App.Variables.mv_searchFilters.getData().advancedFilters.email == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.channel == null || App.Variables.mv_searchFilters.getData().advancedFilters.channel == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.employer == null || App.Variables.mv_searchFilters.getData().advancedFilters.employer == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.createdOnStart == null || App.Variables.mv_searchFilters.getData().advancedFilters.createdOnStart == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.createdOnEnd == null || App.Variables.mv_searchFilters.getData().advancedFilters.createdOnEnd == ""))) {

        Page.Widgets.message1.show = true;
        if (Page.errorMsg != "") {
            Page.errorMsg = Page.errorMsg + "<br/>" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Last Name cannot be used without another field, please enter at least one additional search criteria";
        } else {
            Page.errorMsg = "Last Name cannot be used without another field, please enter at least one additional search criteria";
        }
        // Page.errorMsg = "Last Name cannot be used without another field, please enter at least one additional search criteria";
        Page.showErrorMsg = true;
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });


    }


    if ((App.Variables.mv_searchFilters.getData().advancedFilters.postalCode != null && App.Variables.mv_searchFilters.getData().advancedFilters.postalCode != "") && (
            (App.Variables.mv_searchFilters.getData().referenceNumber == null || App.Variables.mv_searchFilters.getData().referenceNumber == "") && (App.Variables.mv_searchFilters.getData() == null || App.Variables.mv_searchFilters.getData() == "") &&
            (App.Variables.mv_searchFilters.getData().firstName == "" || App.Variables.mv_searchFilters.getData().firstName == null) && (App.Variables.mv_searchFilters.getData().lastName == "" || App.Variables.mv_searchFilters.getData().lastName == null) && (App.Variables.mv_searchFilters.getData().phoneNumber == "" || App.Variables.mv_searchFilters.getData().phoneNumber == null) && (App.Variables.mv_searchFilters.getData().advancedFilters.personIdNumber == null || App.Variables.mv_searchFilters.getData().advancedFilters.personIdNumber == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.applicationStatus == null || App.Variables.mv_searchFilters.getData().advancedFilters.applicationStatus == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.dateOfBirth == null || App.Variables.mv_searchFilters.getData().advancedFilters.dateOfBirth == "") &&
            (App.Variables.mv_searchFilters.getData().advancedFilters.email == null || App.Variables.mv_searchFilters.getData().advancedFilters.email == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.channel == null || App.Variables.mv_searchFilters.getData().advancedFilters.channel == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.employer == null || App.Variables.mv_searchFilters.getData().advancedFilters.employer == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.createdOnStart == null || App.Variables.mv_searchFilters.getData().advancedFilters.createdOnStart == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.createdOnEnd == null || App.Variables.mv_searchFilters.getData().advancedFilters.createdOnEnd == ""))) {

        Page.Widgets.message1.show = true;
        if (Page.errorMsg != "") {
            Page.errorMsg = Page.errorMsg + "<br/>" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Postal code cannot be used without another field, please enter at least one additional search criteria";
        } else {
            Page.errorMsg = "Postal code cannot be used without another field, please enter at least one additional search criteria";
        }

        //Page.errorMsg = "Postal code cannot be used without another field, please enter at least one additional search criteria";
        Page.showErrorMsg = true;
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    }

    if ((App.Variables.mv_searchFilters.getData().advancedFilters.dateOfBirth != null && App.Variables.mv_searchFilters.getData().advancedFilters.dateOfBirth != "") && (
            (App.Variables.mv_searchFilters.getData().referenceNumber == null || App.Variables.mv_searchFilters.getData().referenceNumber == "") && (App.Variables.mv_searchFilters.getData() == null || App.Variables.mv_searchFilters.getData() == "") &&
            (App.Variables.mv_searchFilters.getData().firstName == "" || App.Variables.mv_searchFilters.getData().firstName == null) && (App.Variables.mv_searchFilters.getData().lastName == "" || App.Variables.mv_searchFilters.getData().lastName == null) && (App.Variables.mv_searchFilters.getData().phoneNumber == "" || App.Variables.mv_searchFilters.getData().phoneNumber == null) && (App.Variables.mv_searchFilters.getData().advancedFilters.personIdNumber == null || App.Variables.mv_searchFilters.getData().advancedFilters.personIdNumber == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.applicationStatus == null || App.Variables.mv_searchFilters.getData().advancedFilters.applicationStatus == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.postalCode == null || App.Variables.mv_searchFilters.getData().advancedFilters.postalCode == "") &&
            (App.Variables.mv_searchFilters.getData().advancedFilters.email == null || App.Variables.mv_searchFilters.getData().advancedFilters.email == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.channel == null || App.Variables.mv_searchFilters.getData().advancedFilters.channel == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.employer == null || App.Variables.mv_searchFilters.getData().advancedFilters.employer == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.createdOnStart == null || App.Variables.mv_searchFilters.getData().advancedFilters.createdOnStart == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.createdOnEnd == null || App.Variables.mv_searchFilters.getData().advancedFilters.createdOnEnd == ""))) {

        Page.Widgets.message1.show = true;
        if (Page.errorMsg != "") {
            Page.errorMsg = Page.errorMsg + "<br/>" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Date of Birth cannot be used without another field, please enter at least one additional search criteria";
        } else {
            Page.errorMsg = "Date of Birth cannot be used without another field, please enter at least one additional search criteria";
        }

        //Page.errorMsg = "Postal code cannot be used without another field, please enter at least one additional search criteria";
        Page.showErrorMsg = true;
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    }

    if ((App.Variables.mv_searchFilters.getData().advancedFilters.employer != null && App.Variables.mv_searchFilters.getData().advancedFilters.employer != "") && (
            (App.Variables.mv_searchFilters.getData().referenceNumber == null || App.Variables.mv_searchFilters.getData().referenceNumber == "") && (App.Variables.mv_searchFilters.getData() == null || App.Variables.mv_searchFilters.getData() == "") &&
            (App.Variables.mv_searchFilters.getData().firstName == "" || App.Variables.mv_searchFilters.getData().firstName == null) && (App.Variables.mv_searchFilters.getData().lastName == "" || App.Variables.mv_searchFilters.getData().lastName == null) && (App.Variables.mv_searchFilters.getData().phoneNumber == "" || App.Variables.mv_searchFilters.getData().phoneNumber == null) && (App.Variables.mv_searchFilters.getData().advancedFilters.personIdNumber == null || App.Variables.mv_searchFilters.getData().advancedFilters.personIdNumber == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.applicationStatus == null || App.Variables.mv_searchFilters.getData().advancedFilters.applicationStatus == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.postalCode == null || App.Variables.mv_searchFilters.getData().advancedFilters.postalCode == "") &&
            (App.Variables.mv_searchFilters.getData().advancedFilters.email == null || App.Variables.mv_searchFilters.getData().advancedFilters.email == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.channel == null || App.Variables.mv_searchFilters.getData().advancedFilters.channel == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.dateOfBirth == null || App.Variables.mv_searchFilters.getData().advancedFilters.dateOfBirth == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.createdOnStart == null || App.Variables.mv_searchFilters.getData().advancedFilters.createdOnStart == "") && (App.Variables.mv_searchFilters.getData().advancedFilters.createdOnEnd == null || App.Variables.mv_searchFilters.getData().advancedFilters.createdOnEnd == ""))) {

        Page.Widgets.message1.show = true;
        if (Page.errorMsg != "") {
            Page.errorMsg = Page.errorMsg + "<br/>" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Employer cannot be used without another field, please enter at least one additional search criteria";
        } else {
            Page.errorMsg = "Employer cannot be used without another field, please enter at least one additional search criteria";
        }

        //Page.errorMsg = "Postal code cannot be used without another field, please enter at least one additional search criteria";
        Page.showErrorMsg = true;
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    }


    if (App.Variables.mv_searchFilters.getData().firstName && App.Variables.mv_searchFilters.getData().firstName.length != 0 && App.Variables.mv_searchFilters.getData().firstName.length < 2) {
        if (Page.errorMsg != "") {
            Page.errorMsg = Page.errorMsg + "<br/>" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Please enter at least  2 characters in first name";
        } else {
            Page.errorMsg = "Please enter at least  2 characters in first name";
        }
        Page.Widgets.message1.show = true;
        // Page.errorMsg = Page.errorMsg + "<br/>" + "Please enter at least  2 characters in first name";
        Page.showErrorMsg = true;
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    }

    if (App.Variables.mv_searchFilters.getData().lastName && App.Variables.mv_searchFilters.getData().lastName.length != 0 && App.Variables.mv_searchFilters.getData().lastName.length < 2) {
        if (Page.errorMsg != "") {
            Page.errorMsg = Page.errorMsg + "<br/>" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Please enter at least  2 characters in last name";
        } else {
            Page.errorMsg = "Please enter at least  2 characters in last name";
        }
        Page.Widgets.message1.show = true;
        // Page.errorMsg = Page.errorMsg + "<br/>" + "Please enter at least  2 characters in last name";
        Page.showErrorMsg = true;
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    }

    // if ((App.Variables.mv_searchFilters.getData().phoneNumber != null && App.Variables.mv_searchFilters.getData().phoneNumber != "" && App.Variables.mv_searchFilters.getData().phoneNumber.toString().length < 10) || App.Variables.mv_searchFilters.getData().phoneNumber.toString().length >= 11) {
    //     if (Page.errorMsg != "") {
    //         Page.errorMsg = Page.errorMsg + "<br/>" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Please enter valid phone number";
    //     } else {
    //         Page.errorMsg = "Please enter valid phone number";
    //     }
    //     Page.Widgets.message1.show = true;
    //     // Page.errorMsg = Page.errorMsg + "<br/>" + "Please enter at least  2 characters in last name";
    //     Page.showErrorMsg = true;
    //     window.scrollTo({
    //         top: 0,
    //         behavior: 'smooth'
    //     });
    // }


    if ((App.Variables.mv_searchFilters.getData().phoneNumber) && (isNaN(App.Variables.mv_searchFilters.getData().phoneNumber) || App.Variables.mv_searchFilters.getData().phoneNumber.length > 10 || App.Variables.mv_searchFilters.getData().phoneNumber.length < 10)) {
        if (Page.errorMsg != "") {
            Page.errorMsg = Page.errorMsg + "<br/>" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Please enter valid phone number";
        } else {
            Page.errorMsg = "Please enter valid phone number";
        }
        Page.Widgets.message1.show = true;
        // Page.errorMsg = Page.errorMsg + "<br/>" + "Please enter at least  2 characters in last name";
        Page.showErrorMsg = true;
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    }



    if (Page.showErrorMsg == false) {
        //reset pagination properties
        Page.Widgets.message1.show = false;
        Page.setPaginationDefaults();

        //get the search application service variable
        let sv_searchAPI = Page.Variables.sv_applicationSearch;
        //set inputs
        console.log(Page.Variables.sv_applicationSearch);

        sv_searchAPI.setInput({
            "sortingColumn": Page.Variables.mv_sortProperties.getData().sortingColumn,
            "sortingOrder": Page.Variables.mv_sortProperties.getData().sortingOrder,
            "applicationLockedByUser": 'SYSTEM',
            "applicationCreatedBy": 'SYSTEM',
            "applicationUpdatedByUser": 'SYSTEM',
            "applicationNumber": App.Variables.mv_searchFilters.getData().referenceNumber,
            "firstName": App.Variables.mv_searchFilters.getData().firstName + "%",
            "lastName": App.Variables.mv_searchFilters.getData().lastName + "%",
            "phoneNumber": App.Variables.mv_searchFilters.getData().phoneNumber + "%",
            "userLocale": Page.Variables.mv_userLocale.getValue('dataValue'),
            "idNumber": App.Variables.mv_searchFilters.getData().advancedFilters.personIdNumber,
            "postalCode": App.Variables.mv_searchFilters.getData().advancedFilters.postalCode,
            "appStatus": App.Variables.mv_searchFilters.getData().advancedFilters.applicationStatus,
            "dob": App.Variables.mv_searchFilters.getData().advancedFilters.dateOfBirth,
            "email": App.Variables.mv_searchFilters.getData().advancedFilters.email,
            "channel": App.Variables.mv_searchFilters.getData().advancedFilters.channel,
            "employer": App.Variables.mv_searchFilters.getData().advancedFilters.employer,
            "createdOnStart": (App.Variables.mv_searchFilters.getData().advancedFilters.createdOnStart ? new Date(App.Variables.mv_searchFilters.getData().advancedFilters.createdOnStart).getTime() : null),
            "createdOnEnd": (App.Variables.mv_searchFilters.getData().advancedFilters.createdOnEnd ? new Date(App.Variables.mv_searchFilters.getData().advancedFilters.createdOnEnd).getTime() : new Date().getTime()),
            "currentPage": Page.Variables.mv_currentPage.getValue('dataValue'),
            "pageSize": Page.Variables.mv_pageSize.getValue('dataValue')
        });

        //invoke application search API

        sv_searchAPI.invoke();
        // }
    }
};

Page.sv_applicationSearchonError = function(variable, data) {
    console.log(data);
};

Page.sv_applicationSearchonSuccess = function(variable, data) {
    //set up the pagination parameters
    // console.log(App.Variables.mv_searchFilters);
    Page.Variables.mv_currentPage.setValue('dataValue', data.pageNumber);
    // if (data.totalElements > 3) {
    //     data.totalElements = 3;
    // }
    if (Page.Variables.mv_totalRecords.getValue('dataValue') !== data.totalElements) {
        Page.Variables.mv_totalRecords.setValue('dataValue', data.totalElements);
    }

    if (data.applicationList) {
        let modifiedList = adjustDuplicateAppIds(data.applicationList);
        Object.assign(Page.Variables.sv_applicationSearch.dataSet.applicationList, modifiedList);
        Page.Widgets.searchResult_DataTable.dataset = Page.Variables.sv_applicationSearch.dataSet.applicationList;
    } else {
        Page.Widgets.searchResult_DataTable.dataset = [];
    }

};

function adjustDuplicateAppIds(applicationDataList) {
    let copyList = [];
    if (applicationDataList) {
        Object.assign(copyList, applicationDataList);
        copyList.forEach((appData) => {
            //if application ID is different don't hide visibility
            if (Page.previousApplicationId !== appData.applicationId) {
                Page.previousApplicationId = appData.applicationId;
                appData.isShow = true;
            } else {
                appData.isShow = false;
            }
        });
    }

    return copyList;
}

Page.searchResult_DataTableSort = function($event, $data) {
    Page.previousApplicationId = 0;

    let columnName = $($event.currentTarget).attr("data-col-field");
    let sortDirection = "ASC"; //-> default direction
    let sortClass = $("span[class~='sort-buttons-container'] > i", $event.currentTarget).attr("class");

    if (sortClass.toLowerCase().indexOf("desc") > -1) {
        sortDirection = "DESC";
    } else if (sortClass.toLowerCase().indexOf("asc") > -1) {
        sortDirection = "ASC";
    } else {
        sortDirection = "ASC";
    }

    //determing DB column sorting
    let DBColumnName = null;
    switch (columnName) {
        case "applicationNumber":
            DBColumnName = "ApplicationNumber";
            break;
        case "applicationStatus":
            DBColumnName = "ApplicationStatus";
            break;
        case "channel":
            DBColumnName = "Channel";
            break;
        case "postalCode":
            DBColumnName = "PostalCode";
            break;
        case "phoneNumber":
            DBColumnName = "PhoneNumber";
            break;
        case "employer":
            DBColumnName = "Employer";
            break;
        case "occupation":
            DBColumnName = "Occupation";
            break;
        case "email":
            DBColumnName = "Email";
            break;
        case "customerName":
            DBColumnName = "CustomerName";
            break;
        case "personIdNumber":
            DBColumnName = "PersonId";
            break;
        case "dateOfBirth":
            DBColumnName = "DateOfBirth";
            break;
        case "applicationCreatedOn":
            DBColumnName = "CreatedDate";
            break;
        case "applicationUpdatedOn":
            DBColumnName = "UpdatedDate";
            break;
        case "applicationCreatedBy":
            DBColumnName = "CreatedBy";
            break;
        case "applicationUpdatedBy":
            DBColumnName = "UpdatedBy";
            break;

        case "applicationLockedOn":
            DBColumnName = "LockedOn";
            break;
        case "applicationLockedBy":
            DBColumnName = "LockedBy";
            break;
        default:
            DBColumnName = "DEFAULT";
            break;
    }

    //set up service sorting
    Page.Variables.mv_sortProperties.setData({
        "sortingColumn": DBColumnName,
        "sortingOrder": sortDirection
    });

    //get the search application service variable
    let sv_searchAPI = Page.Variables.sv_applicationSearch;

    //set inputs
    sv_searchAPI.setInput({
        "sortingColumn": Page.Variables.mv_sortProperties.getData().sortingColumn,
        "sortingOrder": Page.Variables.mv_sortProperties.getData().sortingOrder,
        "applicationLockedByUser": 'SYSTEM',
        "applicationCreatedBy": 'SYSTEM',
        "applicationUpdatedByUser": 'SYSTEM',
        "applicationNumber": App.Variables.mv_searchFilters.getData().referenceNumber,
        "firstName": App.Variables.mv_searchFilters.getData().firstName + "%",
        "lastName": App.Variables.mv_searchFilters.getData().lastName + "%",
        "phoneNumber": App.Variables.mv_searchFilters.getData().phoneNumber + "%",
        "userLocale": Page.Variables.mv_userLocale.getValue('dataValue'),
        "idNumber": App.Variables.mv_searchFilters.getData().advancedFilters.personIdNumber,
        "postalCode": App.Variables.mv_searchFilters.getData().advancedFilters.postalCode,
        "appStatus": App.Variables.mv_searchFilters.getData().advancedFilters.applicationStatus,
        "dob": App.Variables.mv_searchFilters.getData().advancedFilters.dateOfBirth,
        "email": App.Variables.mv_searchFilters.getData().advancedFilters.email,
        "channel": App.Variables.mv_searchFilters.getData().advancedFilters.channel,
        "employer": App.Variables.mv_searchFilters.getData().advancedFilters.employer,
        "createdOnStart": (App.Variables.mv_searchFilters.getData().advancedFilters.createdOnStart ? new Date(App.Variables.mv_searchFilters.getData().advancedFilters.createdOnStart).getTime() : null),
        "createdOnEnd": (App.Variables.mv_searchFilters.getData().advancedFilters.createdOnEnd ? new Date(App.Variables.mv_searchFilters.getData().advancedFilters.createdOnEnd).getTime() : new Date().getTime()),
        "currentPage": Page.Variables.mv_currentPage.getValue('dataValue'),
        "pageSize": Page.Variables.mv_pageSize.getValue('dataValue')
    });

    //invoke application search API
    sv_searchAPI.invoke();
};

//added as a workaround as 'date' type inputs still holding value in underlying variable even when field is cleared. But this is not happenig
//for 'datetime' which eventually has the same behaviour as the former one
Page.applicantDOB_inputBlur = function($event, widget) {
    if (!widget.datavalue)
        App.Variables.mv_searchFilters.getData().advancedFilters.dateOfBirth = null;
};

// Page.test = function(row) {
//     if (row.applicantType != "PRIMARY")
//         return true;
//     else
//         return false;
// };

Page.getSitePropertyByNameonSuccess = function(variable, data) {
    // if (variable.dataBinding.name = "SEARCH_RESULTS_FOR_DAYS") {
    //     Page.SEARCH_RESULTS_FOR_DAYS = data.value;
    // }

    if (variable.dataBinding.name = "MAX_NUMBER_OF_APPLICATIONS") {
        Page.MAX_NUMBER_OF_APPLICATIONS = data.value;
    }

    let today = new Date();
    //today.setDate(today.getDate() - Page.SEARCH_RESULTS_FOR_DAYS);
    today.setDate(today.getDate());
    // today.setDate(today.getDate());
    today.setHours(0, 0, 0, 0);
    //App.Variables.mv_searchFilters.getData().advancedFilters.createdOnStart = App.Variables.mv_searchFilters.getData().advancedFilters.createdOnStart
    //set inputs
    Page.Variables.sv_applicationSearch.setInput({
        "sortingColumn": Page.Variables.mv_sortProperties.getData().sortingColumn,
        "sortingOrder": Page.Variables.mv_sortProperties.getData().sortingOrder,
        "applicationLockedByUser": 'SYSTEM',
        "applicationCreatedBy": 'SYSTEM',
        "applicationUpdatedByUser": 'SYSTEM',
        "applicationNumber": App.Variables.mv_searchFilters.getData().referenceNumber,
        "firstName": App.Variables.mv_searchFilters.getData().firstName + "%",
        "lastName": App.Variables.mv_searchFilters.getData().lastName + "%",
        "phoneNumber": App.Variables.mv_searchFilters.getData().phoneNumber + "%",
        "userLocale": Page.Variables.mv_userLocale.getValue('dataValue'),
        "idNumber": App.Variables.mv_searchFilters.getData().advancedFilters.personIdNumber,
        "postalCode": App.Variables.mv_searchFilters.getData().advancedFilters.postalCode,
        "appStatus": App.Variables.mv_searchFilters.getData().advancedFilters.applicationStatus,
        "dob": App.Variables.mv_searchFilters.getData().advancedFilters.dateOfBirth,
        "email": App.Variables.mv_searchFilters.getData().advancedFilters.email,
        "channel": App.Variables.mv_searchFilters.getData().advancedFilters.channel,
        "employer": App.Variables.mv_searchFilters.getData().advancedFilters.employer,
        "createdOnStart": (App.Variables.mv_searchFilters.getData().advancedFilters.createdOnStart ? new Date(App.Variables.mv_searchFilters.getData().advancedFilters.createdOnStart).getTime() : null),
        "createdOnEnd": (App.Variables.mv_searchFilters.getData().advancedFilters.createdOnEnd ? new Date(App.Variables.mv_searchFilters.getData().advancedFilters.createdOnEnd).getTime() : new Date().getTime()),
        "currentPage": Page.Variables.mv_currentPage.getValue('dataValue'),
        "pageSize": Page.Variables.mv_pageSize.getValue('dataValue')
    });

    //invoke application search API
    Page.Variables.sv_applicationSearch.invoke();
};

Page.getSitePropertyByNameonError = function(variable, data) {
    console.log("Site property could not be found!");
};
Page.searchResult_DataTableHeaderclick = function($event, $data) {

};
// Page.message1Close = function($event, widget) {

//     Page.showErrorMsg = false;
// };
// Page.text3Blur = function($event, widget) {
//     console.log(widget);
//     if (widget.length < 2) {
//         Page.Widgets.message1.show = true;
//         Page.errorMsg = "Please enter at least  2 characters in first name";
//         Page.showErrorMsg = true;
//     }
// };
// Page.text3Focus = function($event, widget) {
//     console.log(widget);
//     if (widget.length < 2) {
//         Page.Widgets.message1.show = true;
//         Page.errorMsg = "Please enter at least  2 characters in first name";
//         Page.showErrorMsg = true;
//     }
// };
Page.text3Change = function($event, widget, newVal, oldVal) {
    console.log(widget);
    Page.firstNameErrorMsg = false;
    if (widget.datavalue.length != 0 && widget.datavalue.length < 2) {
        // Page.errorMsg = "Please enter at least  2 characters in first name";
        Page.firstNameErrorMsg = true;
    }
};

Page.validateMobilePhone = function(data) {
    debugger;
    var ans = "";
    var a = data.split("\n");
    // console.log(a);
    for (let i = 0; i < a.length; i++) {
        var str = a[i].substring(11);
        console.log(str.length);
        console.log(str);
        if (str.length >= 7) {
            str = str.substring(4, str.length);
        }
        // console.log("after " + str);
        var cleaned = ('' + a[i]).replace(/\D/g, '');
        var match = cleaned.match(/^(\d{3})(\d{3})(\d{4})$/);
        if (match) {
            a[i] = '(' + match[1] + ') ' + match[2] + '-' + match[3];



        } else {
            data = ('' + a[i]).replace(/\D/g, '');
        }

        ans = ans + a[i] + str + "\n";
        // console.log(ans);



    }
    return ans;

};