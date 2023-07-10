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

function messageTimeout() {
    Page.Variables.successMessageEntManagementVar.dataSet.dataValue = null;
}

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
    Page.combinedSuccessMessageVar = Page.Widgets.entityToTransferBanDropdown.displayValue;
    if (Page.Widgets.getEntityBanDetailsTable1.selectedItems.length == 0 && !Page.Widgets.entityToTransferBanDropdown.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Please select required BANS and the Entity that needs to transferred";
    } else if (Page.Widgets.getEntityBanDetailsTable1.selectedItems.length == 0) {
        App.Variables.errorMsg.dataSet.dataValue = "Please select required BANs to transfer from Current Entity";
    } else if (!Page.Widgets.entityToTransferBanDropdown.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Please select an Entity to transfer the BAN";
    } else {
        let todaysDateJsonFormat = new Date().toJSON();
        Page.Variables.BanListForTransferToExistingEntVar.dataSet = [];
        var billingAccountRefMaps = []
        var billingAccountRefMaps1 = []
        Page.Widgets.getEntityBanDetailsTable1.selectedItems.forEach(function(d) {
            Page.selectedBanList = [];
            var billingAccountRef = [];
            var billingAccountRef1 = [];
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

            billingAccountRef = {
                "id": d.banMapRefId,
                "billingAccountRef": {
                    "id": d.banId,
                    "name": d.banName
                },
                "validFor": {
                    "endDateTime": todaysDateJsonFormat
                }
            }

            billingAccountRef1 = {
                "id": d.banMapRefId,
                "billingAccountRef": {
                    "id": d.banId,
                    "name": d.banName
                },
                "validFor": {
                    "startDateTime": todaysDateJsonFormat
                }
            }


            billingAccountRefMaps.push(billingAccountRef);
            billingAccountRefMaps1.push(billingAccountRef1);
            Page.Variables.BanListForTransferToExistingEntVar.dataSet.push(Page.selectedBanList);



        });

        //PATCH the Moving out and Moving using /entity API.


        //PATCH for Moving out
        Page.Variables.UpdateCollectionEntityServiceVar.setInput({
            "id": parseInt(Page.pageParams.entityId),
            "CollectionEntityUpdate": {
                "id": parseInt(Page.pageParams.entityId),
                "agentId": App.Variables.getLoggedInUserDetails.dataSet.emplId,
                "channel": {
                    "originatorAppId": "FAWBTELUSAGENT",
                    "userId": App.Variables.getLoggedInUserDetails.dataSet.emplId
                },
                billingAccountRefMaps
            }
        });


        Page.Variables.UpdateCollectionEntityServiceVar.invoke();

        //PATCH for Moving In
        billingAccountRefMaps = billingAccountRefMaps1;
        Page.Variables.UpdateCollectionEntityServiceVar.setInput({
            "id": Page.Widgets.entityToTransferBanDropdown.datavalue,
            "CollectionEntityUpdate": {
                "id": Page.Widgets.entityToTransferBanDropdown.datavalue,
                "agentId": App.Variables.getLoggedInUserDetails.dataSet.emplId,
                "channel": {
                    "originatorAppId": "FAWBTELUSAGENT",
                    "userId": App.Variables.getLoggedInUserDetails.dataSet.emplId
                },
                billingAccountRefMaps
            }
        });
        Page.Variables.UpdateCollectionEntityServiceVar.invoke();


        Page.Widgets.TransferBanToExistEntDialog.close();
        Page.Variables.successMessageEntManagementVar.dataSet.dataValue = "BANs transferred to Entity: " + Page.combinedSuccessMessageVar;
        setTimeout(messageTimeout, 10000);
    }
};



Page.TransferBanToNewEntityClick = function($event, widget) {
    debugger;
    Page.Widgets.entityNamePopOver.hidePopover();
    Page.Widgets.TransferBanToNewEntDialog.open();
    App.Variables.errorMsg.dataSet.dataValue = null;
    Page.Variables.entityIdTextVar.dataSet.dataValue = Page.Widgets.label13_1.caption;
    Page.Variables.entityNameTextVar.dataSet.dataValue = Page.Widgets.label10.caption;
    Page.Variables.entityTypeTextVar.dataSet.dataValue = Page.Widgets.label14.caption;
};

Page.BansTravelHistoryClick = function($event, widget) {
    debugger;
    Page.Widgets.entityNamePopOver.hidePopover();
    Page.Widgets.BansTravelHistoryDialog.open();
    App.Variables.errorMsg.dataSet.dataValue = null;
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
Page.ContactSelect = function($event, widget) {
    App.Variables.errorMsg.dataSet.dataValue = "";
    App.Variables.successMessage.dataSet.dataValue = "";
    Page.Variables.ContactPageName.dataSet.dataValue = 'Contact';
};
Page.ContactDeselect = function($event, widget) {
    App.Variables.errorMsg.dataSet.dataValue = "";
    App.Variables.successMessage.dataSet.dataValue = "";
    Page.Variables.ContactPageName.dataSet.dataValue = 'Contact';
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
    debugger;
    if (Page.Widgets.TransferBanToNewEntityTable.selectedItems.length == 0) {
        App.Variables.errorMsg.dataSet.dataValue = "Please select required BANs to transfer from Current Entity";
    } else {

        //Page.Variables.getCollectionEntityById.setInput
        /*    Page.Variables.getCollectionEntityById.setInput({
                "id": parseInt(Page.pageParams.entityId)
            });


            Page.Variables.getCollectionEntityById.invoke(); 

        alert(Page.Variables.getCollectionEntityById.collectionStatus); */

        let todaysDateJsonFormat = new Date().toJSON();
        Page.Variables.BanListForTransferToNewEntVar.dataSet = [];
        var billingAccountRefMaps = [];
        var billingAccountRefMaps1 = [];
        Page.Widgets.TransferBanToNewEntityTable.selectedItems.forEach(function(d) {
            Page.selectedBanList = [];
            var billingAccountRef = {};
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

            billingAccountRef = {
                "id": d.banMapRefId,
                "billingAccountRef": {
                    "id": d.banId,
                    "name": d.banName
                },
                "validFor": {
                    "endDateTime": todaysDateJsonFormat
                }
            }


            billingAccountRef1 = {
                "id": d.banMapRefId,
                "billingAccountRef": {
                    "id": d.banId,
                    "name": d.banName
                },
                "validFor": {
                    "startDateTime": todaysDateJsonFormat
                }
            }

            billingAccountRefMaps.push(billingAccountRef);
            billingAccountRefMaps1.push(billingAccountRef1);
            Page.Variables.BanListForTransferToNewEntVar.dataSet.push(Page.selectedBanList);


        });

        //PATCH for Moving out
        Page.Variables.UpdateCollectionEntityServiceVar.setInput({
            "id": parseInt(Page.pageParams.entityId),
            "CollectionEntityUpdate": {
                "id": parseInt(Page.pageParams.entityId),
                "agentId": App.Variables.getLoggedInUserDetails.dataSet.emplId,
                "channel": {
                    "originatorAppId": "FAWBTELUSAGENT",
                    "userId": App.Variables.getLoggedInUserDetails.dataSet.emplId
                },
                billingAccountRefMaps
            }
        });

        Page.Variables.UpdateCollectionEntityServiceVar.invoke();

        //POST for creating new entity
        //need to write the logic
        billingAccountRefMaps = billingAccountRefMaps1;
        debugger;
        var roleType;
        var relatedEntityId;
        if (billingAccountRefMaps.length == 1) {
            roleType = 'BAN';
            relatedEntityId = billingAccountRefMaps[0].billingAccountRef.id;
        } else {
            roleType = 'BG';
        }


        if (Page.Variables.getCollectionEntityById.dataSet.relatedEntity.role == 'RCID') {
            relatedEntityId = Page.Variables.getCollectionEntityById.dataSet.relatedEntity.id;
        }

        Page.Variables.AddCollectionEntityServiceVar.setInput({
            "CollectionEntityCreate": {
                "agentId": Page.Variables.getCollectionEntityById.dataSet.agentId,
                "channel": {
                    "originatorAppId": "FAWBTELUSAGENT",
                    "userId": App.Variables.getLoggedInUserDetails.dataSet.emplId
                },
                billingAccountRefMaps,
                'name': Page.Variables.getCollectionEntityById.dataSet.name,
                'customerRisk': Page.Variables.getCollectionEntityById.dataSet.customerRisk,
                'customerRiskId': Page.Variables.getCollectionEntityById.dataSet.customerRiskId,
                'customerValue': Page.Variables.getCollectionEntityById.dataSet.customerValue,
                'customerValueId': Page.Variables.getCollectionEntityById.dataSet.customerValueId,
                'delinquentCycle': Page.Variables.getCollectionEntityById.dataSet.delinquentCycle,
                'lineOfBusiness': Page.Variables.getCollectionEntityById.dataSet.lineOfBusiness,
                'workCategory': Page.Variables.getCollectionEntityById.dataSet.workCategory,
                'tenure': Page.Variables.getCollectionEntityById.dataSet.tenure,
                'exclusionIndicatorCharacter': Page.Variables.getCollectionEntityById.dataSet.exclusionIndicatorCharacter,
                'exclusionIndicatorInteger': Page.Variables.getCollectionEntityById.dataSet.exclusionIndicatorInteger,
                'manualTreatmentIndicator': Page.Variables.getCollectionEntityById.dataSet.manualTreatmentIndicator,
                'notTouchListIndicator': Page.Variables.getCollectionEntityById.dataSet.notTouchListIndicator,

                'relatedEntity': {
                    'id': relatedEntityId,
                    'role': roleType
                },
                'validFor': {
                    'startDateTime': todaysDateJsonFormat
                },
                'engagedCustomerParty': {
                    'cbucid': Page.Variables.getCollectionEntityById.dataSet.engagedCustomerParty.cbucid,
                    'cbuCode': Page.Variables.getCollectionEntityById.dataSet.engagedCustomerParty.cbuCode,
                    'cbuName': Page.Variables.getCollectionEntityById.dataSet.engagedCustomerParty.cbuName,
                    'organizationType': Page.Variables.getCollectionEntityById.dataSet.engagedCustomerParty.organizationType,
                    'rcid': Page.Variables.getCollectionEntityById.dataSet.engagedCustomerParty.rcid,
                    'rcName': Page.Variables.getCollectionEntityById.dataSet.engagedCustomerParty.rcName,
                    'portfolioCategory': Page.Variables.getCollectionEntityById.dataSet.engagedCustomerParty.portfolioCategory,
                    'portfolioSubCategory': Page.Variables.getCollectionEntityById.dataSet.engagedCustomerParty.portfolioSubCategory,
                    'subMarketSegment': Page.Variables.getCollectionEntityById.dataSet.engagedCustomerParty.subMarketSegment
                },
                'engagedRegionalCustomerParty': {
                    'cbucid': Page.Variables.getCollectionEntityById.dataSet.engagedRegionalCustomerParty.cbucid,
                    'cbuCode': Page.Variables.getCollectionEntityById.dataSet.engagedRegionalCustomerParty.cbuCode,
                    'cbuName': Page.Variables.getCollectionEntityById.dataSet.engagedRegionalCustomerParty.cbuName,
                    'organizationType': Page.Variables.getCollectionEntityById.dataSet.engagedRegionalCustomerParty.organizationType,
                    'rcid': Page.Variables.getCollectionEntityById.dataSet.engagedRegionalCustomerParty.rcid,
                    'rcName': Page.Variables.getCollectionEntityById.dataSet.engagedRegionalCustomerParty.rcName,
                    'portfolioCategory': Page.Variables.getCollectionEntityById.dataSet.engagedRegionalCustomerParty.portfolioCategory,
                    'portfolioSubCategory': Page.Variables.getCollectionEntityById.dataSet.engagedRegionalCustomerParty.portfolioSubCategory,
                    'subMarketSegment': Page.Variables.getCollectionEntityById.dataSet.engagedRegionalCustomerParty.subMarketSegment
                }
            }
        });


        Page.Variables.AddCollectionEntityServiceVar.invoke();


        Page.Widgets.TransferBanToNewEntDialog.close();
        Page.Variables.successMessageEntManagementVar.dataSet.dataValue = "BANs transferred to newly created Entity: NewEntityName (NewEntityID)";
        setTimeout(messageTimeout, 10000);
    }
};
Page.TransferBanToNewEntityTableDatarender = function(widget, $data) {
    Page;
    debugger;
    $('#TransferBanToNewEntityTableID th input[type=checkbox]').hide();
};