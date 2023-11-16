/**

****************************************************************************

**

**   This material is the confidential, proprietary, unpublished property

**   of Fair Isaac Corporation.  Receipt or possession of this material

**   does not convey rights to divulge, reproduce, use, or allow others

**   to use it without the specific written authorization of Fair Isaac

**   Corporation and use must conform strictly to the license agreement.

**

**   Copyright (c) 2021 Fair Isaac Corporation.  All rights reserved.

**

****************************************************************************

**/


/*
 * This function will be invoked when any of this prefab's property is changed
 * @key: property name
 * @newVal: new value of the property
 * @oldVal: old value of the property
 */


Prefab.onPropertyChange = function(key, newVal, oldVal) {
    debugger;
    App.changeLocale({
        'datavalue': Prefab.langcode
    })

    switch (key) {
        case "totalrecords":
            Prefab.setupPagination();
            break;
        case "pagesize":
            Prefab.setupPagination();
            break;
        case "paginationname":
            Prefab.setupPagination();
            break;
    }
};

Prefab.setupPagination = function() {
    debugger
    //Remove if already there .
    $('#' + Prefab.paginationname).remove();
    //Dynamic Id for multiple pagination in single page
    if (Prefab.paginationname) {
        $('<div/>', {
            id: Prefab.paginationname,
        }).appendTo(Prefab.Widgets.containerPagination.$element[0]);
    }

    if (Prefab.totalrecords && Prefab.pagesize && Prefab.paginationname) {
        var emptyArray = new Array(Prefab.totalrecords); // Creating an empty array to set up the pagination.

        debugger
        $('#' + Prefab.paginationname).pagination({
            dataSource: emptyArray,
            pageSize: Prefab.pagesize,
            showSizeChanger: false,
            showPageNumbers: true,
            showNavigator: true,
            showTotalPage: true,
            formatNavigator: App.appLocale.LABEL_DISPLAYING + ' <%= rangeStart %> - <%= rangeEnd %> ' + App.appLocale.LABEL_OF + ' <%= totalNumber %>',
            pageLink: 'javascript:void(0)',
            // fico ul pagination class
            ulClassName: 'pagination',
            pageCaption: App.appLocale.LABEL_PAGE,
            callback: function(data, pagination) {

                Prefab.onPagechange(pagination);
            }
        });
    } else {
        // Clear pagination if no records
        $('#' + Prefab.paginationname).html('');
    }

};

Prefab.onReady = function() {
    // this method will be triggered post initialization of the prefab.
};

/*
 * Refresh the pagination widget after activation or deactivation of the entity.
 */
Prefab.refreshPagination = function() {
    debugger;
    Prefab.setupPagination();
};