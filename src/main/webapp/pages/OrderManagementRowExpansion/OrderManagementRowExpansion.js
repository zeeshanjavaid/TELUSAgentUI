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

    App.showRowExpansionOrderManagement = function(row, data) {
        debugger;
        var type = row.stepTypeCode;
        if (type == 'SUSPEND') {
            Partial.Widgets.BansLabel.caption = 'BANs to Suspend:';
            Partial.Widgets.Description.caption = 'Suspension Request';
        } else if (type == 'RESTORE') {
            Partial.Widgets.BansLabel.caption = 'BANs to Restore:';
            Partial.Widgets.Description.caption = 'Restore Request';
        } else if (type == 'CEASE') {
            Partial.Widgets.BansLabel.caption = 'BANs to Cease:';
            Partial.Widgets.Description.caption = 'Cease Request';
        } else {
            Partial.Widgets.BansLabel.caption = 'BANs to ' + type + ':';
            Partial.Widgets.Description.caption = 'Other Request';
        }


        Partial.Widgets.type.caption = type;
        Partial.Widgets.DueDate.caption = row.stepDate;
        //Partial.Widgets.BanID1.caption = 'BAN #11';
        //Partial.Widgets.BanID2.caption = 'BAN #22';
        //Partial.Widgets.BanID3.caption = 'BAN #33' + ' ' + Partial.Widgets.BanID3.caption;
        Partial.Widgets.assignedPerson.caption = row.assignedAgentId;
        Partial.Widgets.Comment.caption = row.comment;
        Partial.Widgets.Priority.caption = row.priority;
        Partial.Widgets.assignedTeam.caption = row.assignedTeam;
        Partial.Widgets.Status.caption = row.status;
        Partial.Widgets.ReasonCode.caption = row.reasonCode;

        //var billingAccountIdRefs = row.billingAccountIdRefs;
        var billingAccountIdRefs = [{
                id: 23,
                name: null,
                baseType: null,
                type: null
            },
            {
                id: 46,
                name: null,
                baseType: null,
                type: null
            },
            {
                id: 38,
                name: null,
                baseType: null,
                type: null
            },
            {
                id: 51,
                name: null,
                baseType: null,
                type: null
            },
            {
                id: 29,
                name: null,
                baseType: null,
                type: null
            },
            {
                id: 34,
                name: null,
                baseType: null,
                type: null
            }, {
                id: 37,
                name: null,
                baseType: null,
                type: null
            }
        ];

        populateBANIds(billingAccountIdRefs);
    }
};


function populateBANIds(items) {
    debugger;
    var bansLength = items.length;
    var banId1 = items[0].id;
    var banId2 = items[1].id;
    var banId3 = items[2].id;

    Partial.Widgets.BanID1.caption = banId1;
    Partial.Widgets.BanID2.caption = banId2;
    Partial.Widgets.BanID3.caption = banId3;

    if (bansLength > 3) {
        debugger;
        document.getElementById("moreLabel").style.display = "initial";
    }



};