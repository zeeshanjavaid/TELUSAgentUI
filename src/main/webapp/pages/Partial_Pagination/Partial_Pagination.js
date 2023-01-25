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
    Partial.Variables.mv_pageNumber.setValue('dataValue', 1);


    let totalPages = 1;

    totalPages = (Partial.pageParams.totalRecords === 0) ? 1 : (Partial.pageParams.totalRecords % Partial.pageParams.pageSize) === 0 ?
        (Partial.pageParams.totalRecords / Partial.pageParams.pageSize) :
        parseInt((Partial.pageParams.totalRecords / Partial.pageParams.pageSize)) + 1;

    totalPages = totalPages !== 0 ? totalPages : 1;

    Partial.Variables.mv_totalPages.setValue('dataValue', totalPages);

    // console.log("Inside partial onReady. CurrentPage:" + Partial.Variables.mv_pageNumber.getValue('dataValue') +
    //     ", TotalPages: " + Partial.Variables.mv_totalPages.getValue('dataValue'));
};

Partial.firstPage_LinkClick = function($event, widget) {
    Partial.Variables.mv_pageNumber.setValue('dataValue', 1);
    let parentContainer = document.querySelector("div[name='" + Partial.pageParams.parentContainerName + "']");

    let paginationEvent = new CustomEvent("fi-paginationEvent", {
        "detail": {
            "pageNumber": 0
        }
    });

    //dispatch event to parent enclosing container
    parentContainer.dispatchEvent(paginationEvent);
};

Partial.previousPage_LinkClick = function($event, widget) {
    Partial.Variables.mv_pageNumber.setValue('dataValue', (Partial.Variables.mv_pageNumber.getValue('dataValue') - 1));
    let parentContainer = document.querySelector("div[name='" + Partial.pageParams.parentContainerName + "']");

    let paginationEvent = new CustomEvent("fi-paginationEvent", {
        "detail": {
            "pageNumber": Partial.Variables.mv_pageNumber.getValue('dataValue') - 1
        }
    });

    //dispatch event to parent enclosing container
    parentContainer.dispatchEvent(paginationEvent);
};

Partial.nextPage_LinkClick = function($event, widget) {
    Partial.Variables.mv_pageNumber.setValue('dataValue', (Partial.Variables.mv_pageNumber.getValue('dataValue') + 1));
    let parentContainer = document.querySelector("div[name='" + Partial.pageParams.parentContainerName + "']");

    let paginationEvent = new CustomEvent("fi-paginationEvent", {
        "detail": {
            "pageNumber": Partial.Variables.mv_pageNumber.getValue('dataValue') - 1
        }
    });

    //dispatch event to parent enclosing container
    parentContainer.dispatchEvent(paginationEvent);
};

Partial.lastPage_LinkClick = function($event, widget) {
    Partial.Variables.mv_pageNumber.setValue('dataValue', Partial.Variables.mv_totalPages.getValue('dataValue'));
    let parentContainer = document.querySelector("div[name='" + Partial.pageParams.parentContainerName + "']");

    let paginationEvent = new CustomEvent("fi-paginationEvent", {
        "detail": {
            "pageNumber": Partial.Variables.mv_totalPages.getValue('dataValue') - 1
        }
    });

    //dispatch event to parent enclosing container
    parentContainer.dispatchEvent(paginationEvent);
};