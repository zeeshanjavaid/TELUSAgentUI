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

        var billingAccountIdRefs1 = [];
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

        Partial.selectedBanList = [];
        Partial.Variables.BanListRefIds.dataSet = [];

        if (row.billingAccountIdRefs != null) {
            row.billingAccountIdRefs.forEach(function(d) {
                Partial.selectedBanList = {
                    "id": d.id,
                    //   Partial.Variables.BanListRefIds.dataSet.push(d.id);
                }
                Partial.Variables.BanListRefIds.dataSet.push(Partial.selectedBanList.id);

            });


            Partial.Variables.getBanIdforOD.setInput({

                "id": Partial.Variables.BanListRefIds.dataSet


            });
            Partial.Variables.getBanIdforOD.invoke();
        }

        debugger;
        Partial.Variables.BanListRefIds.dataSet;


        Partial.Widgets.type.caption = type;
        Partial.Widgets.DueDate.caption = row.stepDate;
        Partial.Widgets.assignedPerson.caption = row.assignedAgentId;
        Partial.Widgets.Comment.caption = row.comment;
        Partial.Widgets.Priority.caption = row.priority;
        Partial.Widgets.assignedTeam.caption = row.assignedTeam;
        Partial.Widgets.Status.caption = row.status;
        Partial.Widgets.ReasonCode.caption = row.reasonCode;

    }
};


function populateBANIds(items) {

    var bansLength = items.length;
    var banId1 = items[0];
    var banId2 = items[1];
    var banId3 = items[2];

    Partial.Widgets.BanID1.caption = banId1;
    Partial.Widgets.BanID2.caption = banId2;
    Partial.Widgets.BanID3.caption = banId3;

    if (bansLength > 3) {

        document.getElementById("moreLabel").style.display = "initial";
    }




};

Partial.getBanIdforODonSuccess = function(variable, data) {

    debugger;


    Partial.selectedBanList = [];
    Partial.Variables.BanListRefIds.dataSet = [];



    data.forEach(function(d) {
        Partial.selectedBanList = {
            "id": d.billingAccount.id,
        }
        Partial.Variables.BanListRefIds.dataSet.push(Partial.selectedBanList.id);
    });

    populateBANIds(Partial.Variables.BanListRefIds.dataSet);
    /* var data = Partial.Variables.BanListRefIds.dataSet;
     for (let i = 0; i < data.length; i++) {
         Partial.Variables.BanListRefIds.dataSet = data[i];
     }*/
    /*Partial.Variables.BanListRefIds.dataSet = data.join("\n");
    Partial.Variables.BanListRefIds.dataSet;*/

};