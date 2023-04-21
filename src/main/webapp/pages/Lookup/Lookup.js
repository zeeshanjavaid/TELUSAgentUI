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



    var sv = Page.Variables.PermissionsForLoggedInUserId;
    sv.invoke({}, function(data) {
            // Success Callback        
            console.log("success", data);
            // alert('Permission :' + Page.Variables.PermissionsForLoggedInUserId.dataSet.length);
        },
        function(error) {
            // Error Callback        
            console.log("error", error)
        });


};

Page.CreateClick = function($event, widget) {
    Page.Variables.ParrPageName.dataSet.dataValue = 'CreateParr';
};
Page.CreateDisputeClick = function($event, widget) {
    App.Variables.errorMsg.dataSet.dataValue = "";
    App.Variables.successMessage.dataSet.dataValue = "";
    App.activePage.Widgets.CreateDisputePanel.Widgets.chargeTypeDropDown.datavalue = ""
    App.activePage.Widgets.CreateDisputePanel.Widgets.selectedDisputeBan.datavalue = "";
    App.activePage.Widgets.CreateDisputePanel.Widgets.exclusionDropdown.datavalue = "";
    App.activePage.Widgets.CreateDisputePanel.Widgets.disputeAmt.datavalue = "";
    App.activePage.Widgets.CreateDisputePanel.Widgets.chargeTypeDropDown.datavalue = "";
    App.activePage.Widgets.CreateDisputePanel.Widgets.reasonDropdown.datavalue = "";
    App.activePage.Widgets.CreateDisputePanel.Widgets.productsDropdown.datavalue = "";
    App.activePage.Widgets.CreateDisputePanel.Widgets.AdjustmentToDate.datavalue = "";
    App.activePage.Widgets.CreateDisputePanel.Widgets.custEmailText.datavalue = "";
    App.activePage.Widgets.CreateDisputePanel.Widgets.CreateCommentsDispute.datavalue = "";
    App.activePage.Widgets.CreateDisputePanel.Widgets.AssignedDisputePrime.datavalue = "";
    Page.Variables.DisputePageName.dataSet.dataValue = 'CreateDispute';
};
Page.DisputeSelect = function($event, widget) {
    debugger;
    App.Variables.errorMsg.dataSet.dataValue = "";
    App.Variables.successMessage.dataSet.dataValue = "";
    Page.Variables.DisputePageName.dataSet.dataValue = 'DisputeList';
};
Page.DisputeDeselect = function($event, widget) {
    App.Variables.errorMsg.dataSet.dataValue = "";
    App.Variables.successMessage.dataSet.dataValue = "";
    Page.Variables.DisputePageName.dataSet.dataValue = 'DisputeList';
};

Page.openARbyDelinqCycle = function() {

    Page.Widgets.ArByDelinqCycle.open();



}


Page.TransferBanToExistingEntityClick = function($event, widget) {
    debugger;
    Page.Widgets.entityNamePopOver.hidePopover();
    Page.Widgets.TransferBanToExistEntDialog.open();
    App.Variables.errorMsg.dataSet.dataValue = null;
    Page.Variables.selectedBanLengthVar.dataSet.dataValue = 0;
    Page.Variables.entityIdTextVar.dataSet.dataValue = Page.Widgets.label13_1.caption;
    Page.Variables.entityNameTextVar.dataSet.dataValue = Page.Widgets.label10.caption;
    Page.Variables.entityTypeTextVar.dataSet.dataValue = Page.Widgets.label14.caption;
};

Page.getEntityBanDetailsTable1Select = function($event, widget, row) {
    Page.Variables.selectedBanLengthVar.dataSet.dataValue = Page.Widgets.getEntityBanDetailsTable1.selectedItems.length;
};

Page.getEntityBanDetailsTable1Deselect = function($event, widget, row) {
    Page.Variables.selectedBanLengthVar.dataSet.dataValue = Page.Widgets.getEntityBanDetailsTable1.selectedItems.length;
};
Page.TransferBansToExistingEntityBtnClick = function($event, widget) {

    debugger;
    App.Variables.errorMsg.dataSet.dataValue = "";
    if (Page.Widgets.getEntityBanDetailsTable1.selectedItems.length == 0 && !Page.Widgets.entityToTransferBanDropdown.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Please select required BANS and the entity that needs to transferred";
    } else if (Page.Widgets.getEntityBanDetailsTable1.selectedItems.length == 0) {
        App.Variables.errorMsg.dataSet.dataValue = "Please select required BANs to transfer from Current entity";
    } else if (!Page.Widgets.entityToTransferBanDropdown.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Please select an entity to transfer the BAN";
    } else {

        Page.Variables.BanListForTransferToExistingEntVar.dataSet = [];
        Page.Widgets.getEntityBanDetailsTable1.selectedItems.forEach(function(d) {
            Page.selectedBanList = [];
            Page.selectedBanList = {
                "entityId": d.entityId,
                "disputeFlag": d.disputeFlag,
                "banStatus": d.banStatus,
                "banOverdueAmount": d.banOverdueAmount,
                "banName": d.banName,
                "banMapRefId": d.banMapRefId,
                "banId": d.banId,
                "banArAmount": d.banArAmount,
                "suppresionFlag": d.suppresionFlag
            }

            Page.Variables.BanListForTransferToExistingEntVar.dataSet.push(Page.selectedBanList);


        });
        Page.Widgets.TransferBanToExistEntDialog.close();
    }
};

Page.TransferBanToNewEntityClick = function($event, widget) {
    debugger;
    Page.Widgets.entityNamePopOver.hidePopover();
    Page.Widgets.TransferBanToNewEntDialog.open();
    Page.Variables.entityIdTextVar.dataSet.dataValue = Page.Widgets.label13_1.caption;
    Page.Variables.entityNameTextVar.dataSet.dataValue = Page.Widgets.label10.caption;
    Page.Variables.entityTypeTextVar.dataSet.dataValue = Page.Widgets.label14.caption;
};


Page.ParrSelect = function($event, widget) {
    App.Variables.errorMsg.dataSet.dataValue = "";
    App.Variables.successMessage.dataSet.dataValue = "";
    Page.Variables.ParrPageName.dataSet.dataValue = 'ParrList';
};
Page.ParrDeselect = function($event, widget) {
    App.Variables.errorMsg.dataSet.dataValue = "";
    App.Variables.successMessage.dataSet.dataValue = "";
    Page.Variables.ParrPageName.dataSet.dataValue = 'ParrList';
};
Page.popover4Show = function($event, widget) {

};
Page.getEntityBanDetailsTable1Datarender = function(widget, $data) {
    Page;
    debugger;
};

Page.TransferBanToNewEntityTableSelect = function($event, widget, row) {
    Page.Variables.SelectedBanForCurrEntityVar.dataSet.dataValue = Page.Widgets.TransferBanToNewEntityTable.selectedItems.length;
};

Page.TransferBanToNewEntityTableDeselect = function($event, widget, row) {
    Page.Variables.SelectedBanForCurrEntityVar.dataSet.dataValue = Page.Widgets.TransferBanToNewEntityTable.selectedItems.length;
};
Page.CreateEntityAndTransBansButtonClick = function($event, widget) {

};