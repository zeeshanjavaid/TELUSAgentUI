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


    if (Page.pageParams.entityId) {
        var getEntityProfileDetailsVar = Page.Variables.getEntityProfileDetails;

        getEntityProfileDetailsVar.invoke({
                "inputFields": {
                    "entityId": Page.pageParams.entityId
                },
            },
            function(data) {
                debugger;
                var ar180DaysPlus = parseFloat(data.entityDetails.ar180DaysPlus);
                var ar180Days = parseFloat(data.entityDetails.ar180Days);
                var ar150Days = parseFloat(data.entityDetails.ar150Days);
                var ar120Days = parseFloat(data.entityDetails.ar120Days);
                var ar90Days = parseFloat(data.entityDetails.ar90Days);
                var ar60Days = parseFloat(data.entityDetails.ar60Days);
                var ar30Days = parseFloat(data.entityDetails.ar30Days);
                Page.Widgets.deliqCycle.caption = 0;
                if (ar180DaysPlus > 0) {
                    Page.Widgets.deliqCycle.caption = ar180DaysPlus;
                } else if (ar180Days > 0) {
                    Page.Widgets.deliqCycle.caption = ar180Days;
                } else if (ar150Days > 0) {
                    Page.Widgets.deliqCycle.caption = ar150Days;
                } else if (ar120Days > 0) {
                    Page.Widgets.deliqCycle.caption = ar120Days;
                } else if (ar90Days > 0) {
                    Page.Widgets.deliqCycle.caption = ar90Days;
                } else if (ar60Days > 0) {
                    Page.Widgets.deliqCycle.caption = ar60Days;
                } else if (ar30Days > 0) {
                    Page.Widgets.deliqCycle.caption = ar30Days;
                }

                if (data.banDetails[0].acctStatus == 'C') {
                    var getBillingAccountRefProfileDetailsVar = Page.Variables.getBillingAccountRefProfileDetails;
                    getBillingAccountRefProfileDetailsVar.invoke({
                            "inputFields": {
                                "ban": data.banDetails[0].banId
                            },
                        },
                        function(data1) {
                            debugger;
                            Page.Widgets.previousEntVal.caption = data1[0].previousCollectionEntity.id

                        },
                        function(error1) {
                            // Error Callback
                            console.log("error", error);
                        }
                    );
                }

            },
            function(error) {
                // Error Callback
                console.log("error", error);
            }
        );
    }

};

function messageTimeout() {
    Page.Variables.successMessageEntManagementVar.dataSet.dataValue = null;
    App.Variables.errorMsg.dataSet.dataValue = null;
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
    App.Widgets.Parr.isActive = false;
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

    Page.Variables.TransferToExistEntSucessMessageVar.dataSet.dataValue = Page.Widgets.entityToTransferBanDropdown.displayValue;
    var banStatus = [];
    var banCollectionStatus = [];
    Page.Widgets.getEntityBanDetailsTable1.selectedItems.forEach(function(d) {
        banStatus.push(d.banStatus);
        banCollectionStatus.push(d.banCollectionStatus);
    });

    //To see whether the entity is a cancelled or not.
    var cancelledEntityVar = Page.Variables.getEntityProfileDetails.dataSet.banDetails[0].acctStatus;

    //To Get the entity level collection status
    var entityLevelCollectionStatus = Page.Variables.getCollectionEntityById.dataSet.collectionStatus;

    var entityCollectionStatusForTransferredEntity;
    var getCollectionEntityIdForTransferredEntityVar = Page.Variables.getCollectionEntityIdForTransferredEntity;
    getCollectionEntityIdForTransferredEntityVar.invoke({
            "inputFields": {
                "id": Page.Widgets.entityToTransferBanDropdown.datavalue
            },
        },
        function(data) {
            entityCollectionStatusForTransferredEntity = data.collectionStatus;
        },
        function(error) {
            // Error Callback
            console.log("error", error);
        }
    );




    if (Page.Widgets.getEntityBanDetailsTable1.selectedItems.length == 0) {
        App.Variables.errorMsg.dataSet.dataValue = "Please select required BANs to transfer from Current Entity";
    } else if (Page.Widgets.getEntityBanDetailsTable1.dataset.length == 1) {
        App.Variables.errorMsg.dataSet.dataValue = "BAN not eligible for transfer as only one BAN exists for the Entity";
    } else if (Page.Widgets.getEntityBanDetailsTable1.selectedItems.length == 0 && !Page.Widgets.entityToTransferBanDropdown.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Please select required BANS and the Entity that needs to transferred";
    } else if (!Page.Widgets.entityToTransferBanDropdown.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Please select an Entity to transfer the BAN";
    } else if (Page.Variables.selectedEntityToTransferStr.dataSet.dataValue == 'C') {
        App.Variables.errorMsg.dataSet.dataValue = "Transfer not allowed to the canceled entity";
    } else if (entityLevelCollectionStatus == 'INARRG' || entityLevelCollectionStatus == 'CEASE' || entityLevelCollectionStatus == 'SUSPEND') {
        App.Variables.errorMsg.dataSet.dataValue = "Transfer not allowed from Entity as the entity should not be in INARRG / CEASE / SUSPEND collection status";
    } else if (banCollectionStatus.includes('CEASE') || banCollectionStatus.includes('SUSPEND') || banCollectionStatus.includes('INARRG')) {
        App.Variables.errorMsg.dataSet.dataValue = "Transfer not allowed from Entity as the BAN's selected should not be in either CEASE / SUSPEND / INARGG status.";
    } else if (banStatus.includes('C')) {
        App.Variables.errorMsg.dataSet.dataValue = "Transfer not allowed from Entity as the BAN's selected should not be in cancelled status";
    } else if (cancelledEntityVar == 'C') {
        App.Variables.errorMsg.dataSet.dataValue = "Transfer not allowed from Cancelled Entity";
    } else if (entityCollectionStatusForTransferredEntity == 'CEASE' || entityCollectionStatusForTransferredEntity == 'SUSPEND') {
        App.Variables.errorMsg.dataSet.dataValue = "Transfer not allowed into the entity " + Page.Widgets.entityToTransferBanDropdown.displayValue + " because its in SUSPEND / CEASE status";
    } else if (Page.Variables.getCollectionEntityById.dataSet.id == Page.Widgets.entityToTransferBanDropdown.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Please select different Entity to transfer the BAN";
    } else {
        App.Variables.errorMsg.dataSet.dataValue = null;
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
                "banRefId": d.banRefId,
                "banId": d.banId,
                "banArAmount": d.banArAmount,
                "suppresionFlag": d.suppresionFlag
            }

            var billingAccountRefIdUsingEntityId = {};
            Page.Variables.getCollectionEntityById.dataSet.billingAccountRefMaps.forEach(function(data) {
                if (data.billingAccountRef.id == d.banRefId) {
                    billingAccountRefIdUsingEntityId = data.id;
                }

            });

            billingAccountRef = {
                "id": billingAccountRefIdUsingEntityId,
                "validFor": {
                    "endDateTime": todaysDateJsonFormat
                }
            }

            billingAccountRef1 = {
                "billingAccountRef": {
                    "id": d.banRefId,
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

        debugger;
        var PatchOutCollectionEntityVar = Page.Variables.PatchOutCollectionEntity;
        PatchOutCollectionEntityVar.invoke({
                "inputFields": {
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
                }
            },

            function(data) {
                //PATCH for transferring
                billingAccountRefMaps = billingAccountRefMaps1;

                Page.Variables.PatchInCollectionEntity.setInput({
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
                Page.Variables.PatchInCollectionEntity.invoke();

            },
            function(error) {
                // Error Callback
                console.log("error", error);
            }

        );



    }

    setTimeout(messageTimeout, 8000);
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
    App.Widgets.Parr.isActive = true;
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
    App.Widgets.Parr.isActive = false;
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
    debugger;
    $('#getEntityBanDetailsTable1ID th input[type=checkbox]').hide();
};

Page.TransferBanToNewEntityTableSelect = function($event, widget, row) {
    Page.Variables.SelectedBanForCurrEntityVar.dataSet.dataValue = Page.Widgets.TransferBanToNewEntityTable.selectedItems.length;
};

Page.TransferBanToNewEntityTableDeselect = function($event, widget, row) {
    Page.Variables.SelectedBanForCurrEntityVar.dataSet.dataValue = Page.Widgets.TransferBanToNewEntityTable.selectedItems.length;
};
Page.CreateEntityAndTransBansButtonClick = function($event, widget) {
    debugger;

    var banStatus = [];
    var banCollectionStatus = [];
    Page.Widgets.TransferBanToNewEntityTable.selectedItems.forEach(function(d) {
        banStatus.push(d.banStatus);
        banCollectionStatus.push(d.banCollectionStatus);
    });

    var cancelledEntityVar = Page.Variables.getEntityProfileDetails.dataSet.banDetails[0].acctStatus;

    if (Page.Widgets.TransferBanToNewEntityTable.selectedItems.length == 0) {
        App.Variables.errorMsg.dataSet.dataValue = "Please select required BANs to transfer from Current Entity";
    } else if (Page.Widgets.TransferBanToNewEntityTable.dataset.length == 1) {
        App.Variables.errorMsg.dataSet.dataValue = "BAN not eligible for transfer as only one BAN exists for the Entity";
    } else if (banCollectionStatus.includes('CEASE') || banCollectionStatus.includes('SUSPEND') || banCollectionStatus.includes('INARRG')) {
        App.Variables.errorMsg.dataSet.dataValue = "Transfer not allowed from Entity as the BAN's selected should not be in either CEASE / SUSPEND / INARGG status.";
    } else if (banStatus.includes('C')) {
        App.Variables.errorMsg.dataSet.dataValue = "Transfer not allowed from Entity as the BAN's selected should not be in cancelled status";
    } else if (cancelledEntityVar == 'C') {
        App.Variables.errorMsg.dataSet.dataValue = "Transfer not allowed from Cancelled Entity";
    } else {

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
                "banRefId": d.banRefId,
                "banId": d.banId,
                "banArAmount": d.banArAmount,
                "suppresionFlag": d.suppresionFlag
            }

            var billingAccountRefIdUsingEntityId = {};
            Page.Variables.getCollectionEntityById.dataSet.billingAccountRefMaps.forEach(function(data) {
                if (data.billingAccountRef.id == d.banRefId) {
                    billingAccountRefIdUsingEntityId = data.id;
                }

            });

            billingAccountRef = {
                "id": billingAccountRefIdUsingEntityId,
                "validFor": {
                    "endDateTime": todaysDateJsonFormat
                }
            }


            billingAccountRef1 = {
                "billingAccountRef": {
                    "id": d.banRefId,
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
        var updateCollectionEntityServiceVar = Page.Variables.UpdateCollectionEntityServiceVar;
        updateCollectionEntityServiceVar.invoke({
                "inputFields": {
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
                }
            },

            function(data) {
                //POST for creating new entity
                //need to write the logic
                billingAccountRefMaps = billingAccountRefMaps1;
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

            },
            function(error) {
                // Error Callback
                console.log("error", error);
            }

        );

    }
    setTimeout(messageTimeout, 8000);
};
Page.TransferBanToNewEntityTableDatarender = function(widget, $data) {
    $('#TransferBanToNewEntityTableID th input[type=checkbox]').hide();
};
Page.TransferBanToExistEntDialogOpened = function($event, widget) {
    debugger;
    Page.Variables.getEntityBanDetailsService.setInput({
        "entityId": parseInt(Page.pageParams.entityId)
    });
    Page.Variables.getEntityBanDetailsService.invoke();

    Page.Variables.getCollectionEntityService.setInput({
        "cbucid": Page.Variables.getCollectionEntityById.dataSet.engagedCustomerParty.cbucid
    });
    Page.Variables.getCollectionEntityService.invoke();

};
Page.TransferBanToNewEntDialogOpened = function($event, widget) {
    Page.Variables.getEntityBanDetailsService.setInput({
        "entityId": parseInt(Page.pageParams.entityId)
    });
    Page.Variables.getEntityBanDetailsService.invoke();
};

Page.AddCollectionEntityServiceVaronSuccess = function(variable, data) {
    Page.Widgets.TransferBanToNewEntDialog.close();
    var newEntityId = data.id;
    var newEntityName = data.name;
    Page.Variables.successMessageEntManagementVar.dataSet.dataValue = "BANs transferred to newly created Entity: " + newEntityName + " (" + newEntityId + ")";
    setTimeout(messageTimeout, 10000);
};

Page.PatchInCollectionEntityonSuccess = function(variable, data) {

    Page.Widgets.TransferBanToExistEntDialog.close();
    Page.Variables.successMessageEntManagementVar.dataSet.dataValue = "BANs transferred to Entity: " + Page.Variables.TransferToExistEntSucessMessageVar.dataSet.dataValue;
    setTimeout(messageTimeout, 10000);

};
Page.BansTravelHistoryDialogOpened = function($event, widget) {
    Page.Variables.entityBanTravelHistoryVar.setInput({
        "id": parseInt(Page.pageParams.entityId)
    });
    Page.Variables.entityBanTravelHistoryVar.invoke();
};


Page.getEntityProfileDetailsonError = function(variable, data, xhrObj) {

};


Page.getEntityBanDetailsServiceonError = function(variable, data, xhrObj) {

};
Page.ActiveEntitySelect = function($event, widget) {

    App.Widgets.Parr.isActive = false;
    App.Widgets.ActiveEntity.isActive = true;
};
Page.OrderManagementSelect = function($event, widget) {
    App.Widgets.Parr.isActive = false;
};
Page.UserNotesSelect = function($event, widget) {
    App.Widgets.Parr.isActive = false;
};
Page.HistoryActionsSelect = function($event, widget) {
    App.Widgets.Parr.isActive = false;
};

Page.getCollectionEntityServiceonError = function(variable, data, xhrObj) {

};

Page.getCollectionEntityByIdonError = function(variable, data, xhrObj) {

};
Page.entityToTransferBanDropdownChange = function($event, widget, newVal, oldVal) {
    debugger;
    if (Page.Widgets.entityToTransferBanDropdown.datavalue != undefined && Page.Widgets.entityToTransferBanDropdown.datavalue != '') {
        var getEntityDetailsForCancelledEntitiesVar = Page.Variables.getEntityDetailsForCancelledEntities;
        getEntityDetailsForCancelledEntitiesVar.invoke({
                "inputFields": {
                    "entityId": Page.Widgets.entityToTransferBanDropdown.datavalue
                },
            },

            function(data) {
                Page.Variables.selectedEntityToTransferStr.dataSet.dataValue = data.banDetails[0].acctStatus;
            },
            function(error) {
                // Error Callback
                console.log("error", error);
            }

        );
    }

};
Page.CancelTransToExistBanBtnClick = function($event, widget) {
    Page.Widgets.TransferBanToExistEntDialog.close();
    App.Variables.errorMsg.dataSet.dataValue = null;
};
Page.TransferBanToExistEntDialogClose = function($event, widget) {
    Page.Widgets.TransferBanToExistEntDialog.close();
    App.Variables.errorMsg.dataSet.dataValue = null;
};
Page.button4_1Click = function($event, widget) {
    Page.Widgets.TransferBanToNewEntDialog.close();
    App.Variables.errorMsg.dataSet.dataValue = null;
};
Page.TransferBanToNewEntDialogClose = function($event, widget) {
    Page.Widgets.TransferBanToNewEntDialog.close();
    App.Variables.errorMsg.dataSet.dataValue = null;
};
Page.previousEntValClick = function($event, widget) {
    debugger;
    //Page.pageParams.entityId = widget.caption;

    var getEntityProfileDetailsVar = Page.Variables.getEntityProfileDetails;

    getEntityProfileDetailsVar.invoke({
            "inputFields": {
                "entityId": widget.caption
            },
        },
        function(data) {
            debugger;
            var ar180DaysPlus = parseFloat(data.entityDetails.ar180DaysPlus);
            var ar180Days = parseFloat(data.entityDetails.ar180Days);
            var ar150Days = parseFloat(data.entityDetails.ar150Days);
            var ar120Days = parseFloat(data.entityDetails.ar120Days);
            var ar90Days = parseFloat(data.entityDetails.ar90Days);
            var ar60Days = parseFloat(data.entityDetails.ar60Days);
            var ar30Days = parseFloat(data.entityDetails.ar30Days);
            Page.Widgets.deliqCycle.caption = 0;
            if (ar180DaysPlus > 0) {
                Page.Widgets.deliqCycle.caption = ar180DaysPlus;
            } else if (ar180Days > 0) {
                Page.Widgets.deliqCycle.caption = ar180Days;
            } else if (ar150Days > 0) {
                Page.Widgets.deliqCycle.caption = ar150Days;
            } else if (ar120Days > 0) {
                Page.Widgets.deliqCycle.caption = ar120Days;
            } else if (ar90Days > 0) {
                Page.Widgets.deliqCycle.caption = ar90Days;
            } else if (ar60Days > 0) {
                Page.Widgets.deliqCycle.caption = ar60Days;
            } else if (ar30Days > 0) {
                Page.Widgets.deliqCycle.caption = ar30Days;
            }

            if (data.banDetails[0].acctStatus == 'C') {
                var getBillingAccountRefProfileDetailsVar = Page.Variables.getBillingAccountRefProfileDetails;
                getBillingAccountRefProfileDetailsVar.invoke({
                        "inputFields": {
                            "ban": data.banDetails[0].banId
                        },
                    },
                    function(data1) {
                        debugger;
                        Page.Widgets.previousEntVal.caption = data1[0].previousCollectionEntity.id

                    },
                    function(error1) {
                        // Error Callback
                        console.log("error", error);
                    }
                );
            }

        },
        function(error) {
            // Error Callback
            console.log("error", error);
        }
    );

    Page.Actions.goToPage_Lookup.setData({
        "entityId": widget.caption
    })
    Page.Actions.goToPage_Lookup.navigate();
    Page.Actions.goToPage_Lookup.invoke();


};