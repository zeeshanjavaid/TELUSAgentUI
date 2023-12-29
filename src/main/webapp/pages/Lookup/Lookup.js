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
                } else {
                    Page.Widgets.previousEntVal.caption = "Previous Entity not found";
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
    App.refreshDisputeList();
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

    if (Page.Widgets.getEntityBanDetailsTable1.dataset.length == 0) {
        App.Variables.errorMsg.dataSet.dataValue = "BAN's not available to transfer";
        return;
    }

    Page.Variables.TransferToExistEntSucessMessageVar.dataSet.dataValue = Page.Widgets.entityToTransferBanDropdown.displayValue;

    //To see whether the entity is a cancelled or not.
    var cancelledEntityVar = Page.Variables.getEntityProfileDetails.dataSet.banDetails[0].acctStatus;

    //To Get the entity level collection status
    var entityLevelCollectionStatus = Page.Variables.getCollectionEntityById.dataSet.collectionStatus;

    if (Page.Widgets.getEntityBanDetailsTable1.selectedItems.length == 0) {
        App.Variables.errorMsg.dataSet.dataValue = "Please select required BANs to transfer from Current Entity";
    } else if (Page.Widgets.getEntityBanDetailsTable1.selectedItems.length == 0 && !Page.Widgets.entityToTransferBanDropdown.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Please select required BANS and the Entity that needs to transferred";
    } else if (!Page.Widgets.entityToTransferBanDropdown.datavalue) {
        App.Variables.errorMsg.dataSet.dataValue = "Please select an Entity to transfer the BAN";
    } else if (entityLevelCollectionStatus == 'INARRG' || entityLevelCollectionStatus == 'CEASE' || entityLevelCollectionStatus == 'SUSPEND') {
        App.Variables.errorMsg.dataSet.dataValue = "Transfer not allowed from Entity as the entity should not be in INARRG / CEASE / SUSPEND collection status";
    } else if (Page.Variables.selectedEntityToTransferStr.dataSet.dataValue == 'C') {
        App.Variables.errorMsg.dataSet.dataValue = "Transfer not allowed to the canceled entity";
    } else if (cancelledEntityVar == 'C') {
        App.Variables.errorMsg.dataSet.dataValue = "Transfer not allowed from Cancelled Entity";
    } else if (Page.Variables.selectedEntCollStatusOfTrans.dataSet.dataValue == 'CEASE' || Page.Variables.selectedEntCollStatusOfTrans.dataSet.dataValue == 'SUSPEND') {
        App.Variables.errorMsg.dataSet.dataValue = "Transfer not allowed into the entity " + Page.Widgets.entityToTransferBanDropdown.displayValue + " because its in " + Page.Variables.selectedEntCollStatusOfTrans.dataSet.dataValue + " status";
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
                "id": billingAccountRefIdUsingEntityId.toString(),
                "validFor": {
                    "endDateTime": todaysDateJsonFormat
                }
            }

            billingAccountRef1 = {
                "billingAccountRef": {
                    "id": d.banRefId.toString(),
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
                        // "agentId": App.Variables.getLoggedInUserDetails.dataSet.emplId, // need to remove
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
                        // "agentId": App.Variables.getLoggedInUserDetails.dataSet.emplId, // need to remove
                        "channel": {
                            "originatorAppId": "FAWBTELUSAGENT",
                            "userId": App.Variables.getLoggedInUserDetails.dataSet.emplId
                        },
                        billingAccountRefMaps,
                        "relatedEntity": {
                            "id": null,
                            "role": "BG"
                        },
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
    App.refreshParrList();
};
Page.ParrDeselect = function($event, widget) {
    App.Variables.errorMsg.dataSet.dataValue = "";
    App.Variables.successMessage.dataSet.dataValue = "";
    Page.Variables.ParrPageName.dataSet.dataValue = 'ParrList';
    //App.refreshParrList();
};
Page.ContactSelect = function($event, widget) {
    App.Variables.errorMsg.dataSet.dataValue = "";
    App.Variables.successMessage.dataSet.dataValue = "";
    App.Widgets.Parr.isActive = false;
    Page.Variables.ContactPageName.dataSet.dataValue = 'Contact';
    App.refreshContactList();
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

    //To Get the entity level collection status
    var entityLevelCollectionStatus = Page.Variables.getCollectionEntityById.dataSet.collectionStatus;

    var cancelledEntityVar = Page.Variables.getEntityProfileDetails.dataSet.banDetails[0].acctStatus;

    if (Page.Widgets.TransferBanToNewEntityTable.selectedItems.length == 0) {
        App.Variables.errorMsg.dataSet.dataValue = "Please select required BANs to transfer from Current Entity";
    } else if (Page.Widgets.TransferBanToNewEntityTable.dataset.length == 1) {
        App.Variables.errorMsg.dataSet.dataValue = "BAN not eligible for transfer as only one BAN exists for the Entity";
    } else if (Page.Widgets.TransferBanToNewEntityTable.selectedItems.length == Page.Widgets.TransferBanToNewEntityTable.dataset.length) {
        App.Variables.errorMsg.dataSet.dataValue = "Cannot select all the BAN's that needs to be transferred.";
    } else if (entityLevelCollectionStatus == 'INARRG' || entityLevelCollectionStatus == 'CEASE' || entityLevelCollectionStatus == 'SUSPEND') {
        App.Variables.errorMsg.dataSet.dataValue = "Transfer not allowed from Entity as the entity should not be in INARRG / CEASE / SUSPEND collection status";
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
                "id": billingAccountRefIdUsingEntityId.toString(),
                "validFor": {
                    "endDateTime": todaysDateJsonFormat
                }
            }


            billingAccountRef1 = {
                "billingAccountRef": {
                    "id": d.banRefId.toString(),
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
                        // "agentId": App.Variables.getLoggedInUserDetails.dataSet.emplId, //need to remove
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
                            'id': null,
                            'role': "BG"
                        },
                        'validFor': {
                            'startDateTime': todaysDateJsonFormat
                        },
                        'engagedCustomerParty': {
                            'cbucid': Page.Variables.getCollectionEntityById.dataSet.engagedCustomerParty.cbucid,
                            'cbuCode': Page.Variables.getCollectionEntityById.dataSet.engagedCustomerParty.cbuCode,
                            'cbuName': Page.Variables.getCollectionEntityById.dataSet.engagedCustomerParty.cbuName
                        },
                        'engagedRegionalCustomerParty': {
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
        "cbucid": Page.Variables.getCollectionEntityById.dataSet.engagedCustomerParty.cbucid,
        "offset": 0,
        "limit": 100
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
    App.refreshParrSummary();
    App.refreshEntProfCancelParrSummary();
};
Page.OrderManagementSelect = function($event, widget) {
    App.Widgets.Parr.isActive = false;
    App.refreshCollOrderMgmtList();
};
Page.UserNotesSelect = function($event, widget) {
    App.Widgets.Parr.isActive = false;
    App.refreshLatestNotesAndDoc();
};
Page.HistoryActionsSelect = function($event, widget) {
    App.Widgets.Parr.isActive = false;
    App.refreshCollActionList();

};

Page.getCollectionEntityServiceonError = function(variable, data, xhrObj) {

};

Page.getCollectionEntityByIdonError = function(variable, data, xhrObj) {

};
Page.entityToTransferBanDropdownChange = function($event, widget, newVal, oldVal) {
    debugger;
    if (Page.Widgets.entityToTransferBanDropdown.datavalue != undefined && Page.Widgets.entityToTransferBanDropdown.datavalue != '') {
        Page.Variables.getCollectionEntityIdForTransferredEntity.setInput({
            "id": Page.Widgets.entityToTransferBanDropdown.datavalue
        });

        Page.Variables.getCollectionEntityIdForTransferredEntity.invoke();


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
    if (widget.caption == 'Previous Entity not found') {
        $event.defaultPrevented = true;
    } else {
        $event.defaultPrevented = false;
        var getEntityProfileDetailsVar = Page.Variables.getEntityProfileDetails;

        getEntityProfileDetailsVar.invoke({
                "inputFields": {
                    "entityId": widget.caption
                },
            },
            function(data) {
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

    }
};
Page.anchor3Click = function($event, widget) {
    debugger;

    Page.Actions.goToPage_lookupEntity.setData({
        "entityId": Page.pageParams.entityId,
        "inputlevel": Page.pageParams.inputlevel,
        "inputType": Page.pageParams.inputType,
        "searchMatchCriteria": Page.pageParams.searchMatchCriteria,
        "billingSystem": Page.pageParams.billingSystem
    })
    Page.Actions.goToPage_lookupEntity.navigate();
};


Page.getCollectionEntityIdForTransferredEntityonSuccess = function(variable, data) {
    Page.Variables.selectedEntCollStatusOfTrans.dataSet.dataValue = data.collectionStatus;
};

Page.getEntityDetailsForCancelledEntitiesonSuccess = function(variable, data) {
    Page.Variables.selectedEntityToTransferStr.dataSet.dataValue = data.banDetails[0].acctStatus;
};
Page.entityToTransferBanDropdownChange1 = function($event, widget, newVal, oldVal) {
    if (Page.Widgets.entityToTransferBanDropdown.datavalue != undefined && Page.Widgets.entityToTransferBanDropdown.datavalue != '') {
        Page.Variables.getEntityDetailsForCancelledEntities.setInput({
            "entityId": Page.Widgets.entityToTransferBanDropdown.datavalue
        });

        Page.Variables.getEntityDetailsForCancelledEntities.invoke();


    }
};