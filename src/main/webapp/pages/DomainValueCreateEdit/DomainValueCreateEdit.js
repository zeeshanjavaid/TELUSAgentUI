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

Page.isEditDV = false;
Page.isEditLocale = false;
Page.localeListWithoughtEn = {};
Page.initialObj = {}; //This hold the page form data before any changes
Page.relationToAction = null;
Page.showLocaleErrorMsg = false;

/* these two added for select input workaround on relationdetails form 
 * as data was getting vanished from one dropdown when selecting the other one
 */
Page.parent1DVSelected = null;
Page.parent2DVSelected = null;
Page.filteredLocale = null;

// $('document').ready(() => {
//     document.getElementsByTagName("html")[0].style.visibility = "hidden";
// });

/* perform any action on widgets/variables within this block */
Page.onReady = function() {

    // debugger;
    //Render in read-only if not for Create Domain Value action
    if (Page.pageParams.domainValueId === undefined) {
        Page.Variables.readOnlyMode.dataSet.dataValue = false;
    }

    initPage();
    Page.filteredLocale = JSON.parse(JSON.stringify(App.Variables.supportedLocale.dataSet), (key, value) => {
        if (key === 'en') {
            return
        }
        return value;
    });
};




function initPage() {
    //   const intervalId = setInterval(function() {
    //     if (App.permissionsLoaded) {
    //         clearInterval(intervalId);
    //         console.log('Permissions loaded...');

    //         if (!App.IsUserHasAccess('System Administration')) {
    //             window.location.href = '#/ErrorLanding';
    //         } else {
    //             document.getElementsByTagName("html")[0].style.visibility = "visible";


    debugger;

    if (Page.pageParams.domainValueId) {
        Page.Variables.getDomainValueById.invoke();
        Page.isEditDV = true;
    } else {
        Page.Variables.mv_domainValue.dataSet = {
            "domainValueDescription": [],
            "domainValueRelation": [],
            "rankOrder": null,
            "domainValueType": {
                "description": "",
                "code": "",
                "id": 0
            },
            "domainValueTypeRelation": {
                "relationPresent": false,
                "parentDV1Type": null,
                "parentDV2Type": null
            },
            "createdOn": "",
            "updatedOn": "",
            "updatedBy": "",
            "createdBy": "",
            "active": true,
            "code": "",
            "defaultLocale": {
                "deleted": false,
                "locale": "en",
                "description": ""
            },
            "default": false
        };
        //   Page.initialObj = JSON.parse(JSON.stringify(Page.Widgets.domainValueForm.dataset));
    }
    //Converting supported locale json into list
    let obj = Page.Variables.supportedLocale.dataSet;
    let result = Object.keys(obj).map(e => {
        let ret = {};
        ret = {
            "locale": e,
            "localeName": obj[e]
        };
        return ret;
    });
    Page.Variables.localesListWithoutDesc.dataSet = result.filter(item => item.locale != "en");

    if (Page.isEditDV) {
        Page.Widgets.code.class = 'noteditable';
        Page.Widgets.createdOn.class = 'noteditable';
        Page.Widgets.updatedOn.class = 'noteditable';
        Page.Widgets.active.class = 'noteditable';
    }
    Page.Widgets.createdOn.class = 'noteditable';
    Page.Widgets.updatedOn.class = 'noteditable';
    Page.Widgets.active.class = 'noteditable';


    // Page.initialObj = JSON.parse(JSON.stringify(Page.Widgets.domainValueForm.dataset));
    // }
    // } else {
    //     //determining the time elapsed since App started in minutes
    //     const timeElapsedSinceAppStart = moment(new Date()).diff(moment(App.appStartTime), 'minutes');

    //     if (timeElapsedSinceAppStart < 1)
    //         console.log('Waiting to load permissions...');
    //     else {
    //         clearInterval(intervalId);

    //         //if the active page is not 'ErrorLanding'
    //         if (window.location.hash !== '#/ErrorLanding')
    //             window.location.href = '#/ErrorLanding';
    //     }
    // }
    // }, 50);
}

Page.getDomainValueByIdonSuccess = function(variable, data) {
    if (data) {
        Page.Variables.mv_domainValue.dataSet = data;
    }
    Page.initialObj = JSON.parse(JSON.stringify(Page.Variables.mv_domainValue.dataSet));
    console.log("Page.initialObj", Page.initialObj);
};

Page.getDomainValueTypeonSuccess = function(variable, data) {
    Page.Variables.mv_domainValue.dataSet.domainValueType = {
        "description": data[0].description,
        "code": data[0].code,
        "id": data[0].id
    };
    Page.initialObj = JSON.parse(JSON.stringify(Page.Variables.mv_domainValue.dataSet));
    console.log("Page.initialObj", Page.initialObj);


};

Page.displayCustomUserAndDateValues = function(timestamp, id) {
    //debugger;
    //let jsTimeStamp = new Date(timestamp);
    let ts_toShow = App.localizeTimestampToAppTimezone(timestamp)
    let username = (id != "null") ? Page.getUserName(id) : "";
    return ts_toShow + " by " + (!username ? "" : username);
};

Page.saveLocaleBtnClick = function($event, widget) {


    debugger
    if (Page.Widgets.localeDetailsForm.invalid) {
        Page.Widgets.localeDetailsForm.highlightInvalidFields();
    } else {
        if (!Page.isEditLocale) {
            var localeAlreadyexists = false;
            Page.Variables.mv_domainValue.dataSet.domainValueDescription.forEach(function(dvsItem) {
                if (dvsItem.locale === Page.Widgets.localeDetailsForm.formWidgets.locale.datavalue)
                    localeAlreadyexists = true;
            });
            if (localeAlreadyexists) {
                //debugger
                Page.Variables.localePopupMessage.dataSet.dataValue = "Locale already exists";
                Page.showLocaleErrorMsg = true;
            } else {
                Page.Variables.localePopupMessage.dataSet.dataValue = "";
                Page.showLocaleErrorMsg = false;
                Page.Variables.mv_domainValue.dataSet.domainValueDescription.push({
                    "deleted": false,
                    "locale": Page.Widgets.localeDetailsForm.formWidgets.locale.datavalue,
                    "description": Page.Widgets.localeDetailsForm.formWidgets.description.datavalue
                });

                Page.Widgets.domainValueDescriptionTable.dataset = Page.Variables.mv_domainValue.dataSet.domainValueDescription;
                Page.Widgets.localeDetailsDialog.close();
            }
        } else {
            Page.localePopupMessage = "";
            Page.showLocaleErrorMsg = false;
            Page.Variables.mv_domainValue.dataSet.domainValueDescription.forEach(function(dvsItem) {
                if (dvsItem.locale === Page.Widgets.localeDetailsForm.formWidgets.locale.datavalue)
                    dvsItem.description = Page.Widgets.localeDetailsForm.formWidgets.description.datavalue;
            });

            Page.Widgets.domainValueDescriptionTable.dataset = Page.Variables.mv_domainValue.dataSet.domainValueDescription;
            Page.Widgets.localeDetailsDialog.close();
        }
    }
};
Page.domainValueDescriptionTable_updaterowAction = function($event, row) {
    // debugger
    Page.isEditLocale = true;
    Page.Variables.localeDetails.dataSet = row;
    Page.Widgets.localeDetailsDialog.open();
};
Page.domainValueDescriptionTable_deleterowAction = function($event, row) {
    //debugger
    Page.Variables.mv_domainValue.dataSet.domainValueDescription.forEach(function(domainValueDescription, index) {
        if (domainValueDescription.locale === row.locale)
            Page.Variables.mv_domainValue.dataSet.domainValueDescription.splice(index, 1);
    });
    Page.Widgets.domainValueDescriptionTable.dataset = Page.Variables.mv_domainValue.dataSet.domainValueDescription;
};

Page.getDomainValueTypeRelationshipByIdonSuccess = function(variable, data) {
    //debugger
    if (data.length > 0) {
        if (Page.Variables.getDomainValueTypeRelationshipById.dataSet.data[0].domainValueTypeByParentDomainValueTypeId1 != null) {
            Page.Variables.getDVlistParent1.invoke();
        }
        if (Page.Variables.getDomainValueTypeRelationshipById.dataSet.data[0].domainValueTypeByParentDomainValueTypeId2 != null) {
            Page.Variables.getDVlistParent2.invoke();
        }
    }
};

Page.saveBtnRelationDetailsClick = function($event, widget) {
    if (Page.Widgets.relationDetailsForm.invalid) {
        Page.Widgets.relationDetailsForm.highlightInvalidFields();
    } else {
        if (Page.Variables.mv_domainValue.dataSet.domainValueRelation.length != 0) {
            //check for uniqueness of this parent combination
            let isCombinationExists = false,
                matchingCombinations = [];
            matchingCombinations = Page.Variables.mv_domainValue.dataSet.domainValueRelation.filter((combinationItem, index) => {
                if (Page.Variables.getDomainValueTypeRelationshipById.dataSet.data[0].parentDomainValueTypeId2 != null) {
                    return ((combinationItem.parent1DomainValueId === Page.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue.id) &&
                        (combinationItem.parent2DomainValueId === Page.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue.id));
                } else
                    return ((combinationItem.parent1DomainValueId === Page.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue.id));
            });

            isCombinationExists = (matchingCombinations && matchingCombinations.length > 0);

            if (isCombinationExists) {
                let combinationItem = null;
                combinationItem = matchingCombinations.find((combination, index) => {
                    return (!combination.deleted);
                });


                if (combinationItem) {
                    //display error
                    Page.Variables.relationPopupMessage.setValue('dataValue', 'Parent already exists for this domain value. Select a unique parent.');
                    $("p[name='relationPopupMsg']").removeClass("display-none").addClass("display-block");
                    setTimeout(() => {
                        $("p[name='relationPopupMsg']").removeClass("display-block").addClass("display-none");
                        Page.Variables.relationPopupMessage.setValue('dataValue', null);
                    }, 5000);

                } else {
                    if (Page.relationToAction.$index === -1) {
                        Page.Variables.mv_domainValue.dataSet.domainValueRelation.push({

                            "deleted": false,
                            "parent1DomainValueId": Page.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue ? Page.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue.id : null,
                            "parent2DomainValueId": Page.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue ? Page.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue.id : null,
                            "parent1DomainValueDescription": Page.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue ? Page.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue.description : null,
                            "parent2DomainValueDescription": Page.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue ? Page.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue.description : null,
                        });
                    } else {
                        Page.relationToAction.data.parent1DomainValueId = Page.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue ? Page.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue.id : null;
                        Page.relationToAction.data.parent2DomainValueId = Page.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue ? Page.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue.id : null;
                        Page.relationToAction.data.parent1DomainValueDescription = Page.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue ? Page.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue.description : null;
                        Page.relationToAction.data.parent2DomainValueDescription = Page.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue ? Page.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue.description : null;

                        Page.Variables.mv_domainValue.dataSet.domainValueRelation[Page.relationToAction.$index] = Page.relationToAction.data;
                    }

                    Page.Widgets.domainValueRelationTable.dataset = Page.Variables.mv_domainValue.dataSet.domainValueRelation;
                    Page.Widgets.relationDetailsDialog.close();
                }
            } else {
                if (Page.relationToAction.$index === -1) {
                    Page.Variables.mv_domainValue.dataSet.domainValueRelation.push({

                        "deleted": false,
                        "parent1DomainValueId": Page.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue ? Page.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue.id : null,
                        "parent2DomainValueId": Page.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue ? Page.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue.id : null,
                        "parent1DomainValueDescription": Page.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue ? Page.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue.description : null,
                        "parent2DomainValueDescription": Page.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue ? Page.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue.description : null,
                    });
                } else {
                    Page.relationToAction.data.parent1DomainValueId = Page.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue ? Page.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue.id : null;
                    Page.relationToAction.data.parent2DomainValueId = Page.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue ? Page.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue.id : null;
                    Page.relationToAction.data.parent1DomainValueDescription = Page.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue ? Page.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue.description : null;
                    Page.relationToAction.data.parent2DomainValueDescription = Page.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue ? Page.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue.description : null;

                    Page.Variables.mv_domainValue.dataSet.domainValueRelation[Page.relationToAction.$index] = Page.relationToAction.data;
                }

                Page.Widgets.domainValueRelationTable.dataset = Page.Variables.mv_domainValue.dataSet.domainValueRelation;
                Page.Widgets.relationDetailsDialog.close();
            }

        } else {
            if (Page.relationToAction.$index === -1) {
                Page.Variables.mv_domainValue.dataSet.domainValueRelation.push({

                    "deleted": false,
                    "parent1DomainValueId": Page.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue ? Page.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue.id : null,
                    "parent2DomainValueId": Page.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue ? Page.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue.id : null,
                    "parent1DomainValueDescription": Page.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue ? Page.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue.description : null,
                    "parent2DomainValueDescription": Page.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue ? Page.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue.description : null,
                });
            } else {
                Page.relationToAction.data.parent1DomainValueId = Page.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue ? Page.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue.id : null;
                Page.relationToAction.data.parent2DomainValueId = Page.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue ? Page.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue.id : null;
                Page.relationToAction.data.parent1DomainValueDescription = Page.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue ? Page.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue.description : null;
                Page.relationToAction.data.parent2DomainValueDescription = Page.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue ? Page.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue.description : null;

                Page.Variables.mv_domainValue.dataSet.domainValueRelation[Page.relationToAction.$index] = Page.relationToAction.data;
            }

            Page.Widgets.domainValueRelationTable.dataset = Page.Variables.mv_domainValue.dataSet.domainValueRelation;
            Page.Widgets.relationDetailsDialog.close();
        }
    }
};

Page.domainValueRelationTable_deleterowAction = function($event, row) {
    Page.relationToAction = {};
    Page.relationToAction.$index = -1;
    Page.relationToAction.data = row;

    Page.relationToAction.$index = Page.Variables.mv_domainValue.getData().domainValueRelation.findIndex((relationRec) => {
        return (relationRec.parent1DomainValueId === row.parent1DomainValueId &&
            relationRec.parent2DomainValueId === row.parent2DomainValueId && !(relationRec.deleted));
    });

    Page.Widgets.relationDelete_confirmDialog.open();
};

Page.domainValueRelationTable_updaterowAction = function($event, row) {
    Page.relationToAction = {};
    Page.relationToAction.$index = -1;
    Page.relationToAction.data = row;

    Page.relationToAction.$index = Page.Variables.mv_domainValue.getData().domainValueRelation.findIndex((relationRec) => {
        return (relationRec.parent1DomainValueId === row.parent1DomainValueId &&
            relationRec.parent2DomainValueId === row.parent2DomainValueId);
    });

    //find the parent1DV & parent2DV
    let parent1DVRecord = null,
        parent2DVRecord = null;
    parent1DVRecord = Page.Variables.getDVlistParent1.getData().pageContent.find((dvrec) => {
        return (dvrec.id === row.parent1DomainValueId);
    });

    if (Page.Variables.getDomainValueTypeRelationshipById.dataSet.data[0].parentDomainValueTypeId2 != null) {
        parent2DVRecord = Page.Variables.getDVlistParent2.getData().pageContent.find((dvrec) => {
            return (dvrec.id === row.parent2DomainValueId);
        });
    }

    Page.Variables.relationDetails.setData({
        "parent1DomainValue": parent1DVRecord ? parent1DVRecord : null,
        "parent2DomainValue": parent2DVRecord ? parent2DVRecord : null
    });

    Page.Widgets.relationDetailsDialog.open();

};

Page.saveBtnClick = function($event, widget) {
    // debugger;
    console.log(Page.Variables.mv_domainValue.getData().code);
    console.log(Page.Variables.getDVlistByDomainValueTypeId.getData());
    // To check unique domain value code

    // debugger;
    let isUniqueDV = true;

    if (Page.pageParams.domainValueId === undefined || Page.pageParams.domainValueId === 0) {
        let dvRecord = Page.Variables.getDVlistByDomainValueTypeId.getData().pageContent.find((dvData) => {
            return (dvData.code === Page.Variables.mv_domainValue.getData().code);
        });

        isUniqueDV = dvRecord ? false : true;
    }

    if (Page.Widgets.domainValueForm.invalid || Page.Widgets.desc.datavalue == undefined || Page.Widgets.desc.datavalue == "") {

        if (Page.Widgets.desc.datavalue == undefined || Page.Widgets.desc.datavalue == "") {
            Page.ErrorMessage = "Review invalid fields";
            Page.ShowErrorMessage = true;
            Page.showDescriptionErrorMsg = true;
            Page.Widgets.label10.widget.class = 'custom-font-color-red';
            Page.Widgets.desc.class = 'bordercol';
        } else {
            Page.ErrorMessage = "";
            Page.ShowErrorMessage = false;
            Page.showDescriptionErrorMsg = false;
            Page.Widgets.label10.widget.class = '';
            Page.Widgets.desc.class = 'middlebox';
        }

        Page.ErrorMessage = "Review invalid fields";
        Page.ShowErrorMessage = true;
        Page.Widgets.domainValueForm.highlightInvalidFields();

        $('html, body').animate({
            scrollTop: $('[name="errorMsg"]').offset().top - 20
        }, 200);
    } else if (!isUniqueDV) {
        Page.ErrorMessage = "Domain value with this code already exists. Enter a unique code.";
        Page.ShowErrorMessage = true;
        $('html, body').animate({
            scrollTop: $('[name="errorMsg"]').offset().top - 20
        }, 200);
    } else {
        if (!Page.pageParams.domainValueId) {
            if (Page.Variables.getDomainValueTypeRelationshipById.dataSet.data.length > 0 && Page.Variables.getDomainValueTypeRelationshipById.dataSet.data[0].domainValueTypeByParentDomainValueTypeId1 != null) {
                Page.Variables.mv_domainValue.dataSet.domainValueTypeRelation.parentDV1Type = {
                    "description": Page.Variables.getDomainValueTypeRelationshipById.dataSet.data[0].domainValueTypeByParentDomainValueTypeId1.description,
                    "code": Page.Variables.getDomainValueTypeRelationshipById.dataSet.data[0].domainValueTypeByParentDomainValueTypeId1.code,
                    "id": Page.Variables.getDomainValueTypeRelationshipById.dataSet.data[0].domainValueTypeByParentDomainValueTypeId1.id
                };
                Page.Variables.mv_domainValue.dataSet.domainValueTypeRelation.relationPresent = true;
            }
            if (Page.Variables.getDomainValueTypeRelationshipById.dataSet.data.length > 0 && Page.Variables.getDomainValueTypeRelationshipById.dataSet.data[0].domainValueTypeByParentDomainValueTypeId2 != null) {
                Page.Variables.mv_domainValue.dataSet.domainValueTypeRelation.parentDV2Type = {
                    "description": Page.Variables.getDomainValueTypeRelationshipById.dataSet.data[0].domainValueTypeByParentDomainValueTypeId2.description,
                    "code": Page.Variables.getDomainValueTypeRelationshipById.dataSet.data[0].domainValueTypeByParentDomainValueTypeId2.code,
                    "id": Page.Variables.getDomainValueTypeRelationshipById.dataSet.data[0].domainValueTypeByParentDomainValueTypeId2.id
                };
                Page.Variables.mv_domainValue.dataSet.domainValueTypeRelation.relationPresent = true;
            }
            Page.Variables.mv_domainValue.dataSet.active = true;
            Page.Variables.mv_domainValue.dataSet.createdBy = Page.Variables.getLoggedInUserId.dataSet[0].id;
        }

        Page.Variables.mv_domainValue.dataSet.updatedBy = Page.Variables.getLoggedInUserId.dataSet[0].id;
        console.log(Page.Variables.mv_domainValue.dataSet);
        Page.Variables.saveDomainValue.setInput('DomainValueVO', Page.Variables.mv_domainValue.dataSet);
        Page.Variables.saveDomainValue.invoke();
    }

    Page.Variables.readOnlyMode.dataSet.dataValue = true;
};

Page.saveDomainValueonSuccess = function(variable, data) {
    //debugger
    if (data) {
        Page.Variables.mv_domainValue.dataSet = data;
    }
    Page.Actions.goToPage_DomainValuePage.invoke({
        data: {
            'domainValueTypeId': Page.pageParams.domainValueTypeId
        }
    });
};

Page.localeDetailsDialogClose = function($event, widget) {
    Page.isEditLocale = false;
    Page.Variables.localeDetails.dataSet = {};
};

Page.exitConfirmDialogCancel = function($event, widget) {
    Page.Actions.goToPage_DomainValuePage.invoke({
        data: {
            'domainValueTypeId': Page.pageParams.domainValueTypeId
        }
    });
};

Page.relationDetailsDialogOpened = function($event, widget) {
    //hide the popup msg section
    Page.Variables.relationPopupMessage.setValue('dataValue', null);
    $("p[name='relationPopupMsg']").removeClass("display-block").addClass("display-none");

    Page.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue = Page.Variables.relationDetails.getData().parent1DomainValue;
    Page.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue = Page.Variables.relationDetails.getData().parent2DomainValue;

    Page.parent1DomainValue = Page.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue;
    Page.parent2DomainValue = Page.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue;
};

Page.addRelationBtnClick = function($event, widget) {
    Page.relationToAction = {};
    Page.relationToAction.$index = -1;
    Page.relationToAction.data = null;

    //reset the relation variable
    Page.Variables.relationDetails.setData({
        "parent1DomainValue": null,
        "parent2DomainValue": null
    });

    Page.Widgets.relationDetailsDialog.open();
};

Page.relationDelete_confirmDialogOk = function($event, widget) {
    if (Page.relationToAction.data) {
        let row = Page.relationToAction.data;

        if ((Page.relationToAction.$index !== -1) && Page.Variables.mv_domainValue.dataSet.domainValueRelation[Page.relationToAction.$index].id &&
            Page.Variables.mv_domainValue.dataSet.domainValueRelation[Page.relationToAction.$index].id > 0) {
            Page.Variables.mv_domainValue.dataSet.domainValueRelation[Page.relationToAction.$index].deleted = true;
            Page.Widgets.domainValueRelationTable.dataset = Page.Variables.mv_domainValue.dataSet.domainValueRelation;
        } else if ((Page.relationToAction.$index !== -1) && !Page.Variables.mv_domainValue.dataSet.domainValueRelation[Page.relationToAction.$index].id) {
            Page.Variables.mv_domainValue.dataSet.domainValueRelation.splice(Page.relationToAction.$index, 1);
            Page.Widgets.domainValueRelationTable.dataset = Page.Variables.mv_domainValue.dataSet.domainValueRelation;
        }
    }

    Page.relationToAction = {};
    Page.relationToAction.$index = -1;
    Page.relationToAction.data = null;

    Page.Widgets.relationDelete_confirmDialog.close();
};

Page.relationDetailsDialogClose = function($event, widget) {
    Page.relationToAction = {};
    Page.relationToAction.$index = -1;
    Page.relationToAction.data = null;

    //reset the relation variable
    Page.Variables.relationDetails.setData({
        "parent1DomainValue": null,
        "parent2DomainValue": null
    });
};

Page.buttonCancelClick = function($event, widget) {
    //debugger;
    if (JSON.stringify(Page.initialObj) == JSON.stringify(Page.Widgets.domainValueForm.dataset)) {
        Page.exitConfirmDialogCancel();
    } else if (_.isEqual(Page.initialObj, Page.Widgets.domainValueForm.dataset)) {
        Page.exitConfirmDialogCancel();
    } else {
        // Page.Widgets.exitConfirmDialog.open();
        Page.exitConfirmDialogCancel();
    }

    Page.Variables.readOnlyMode.dataSet.dataValue = true;
};

/* these two added for select input workaround on relationdetails form 
 * as data was getting vanished from one dropdown when selecting the other one
 */
Page.dvParent1Change = function($event, widget, newVal, oldVal) {
    Page.parent1DomainValue = newVal;
    Page.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue = Page.parent2DomainValue;
};

Page.dvParent2Change = function($event, widget, newVal, oldVal) {
    Page.parent2DomainValue = newVal;
    Page.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue = Page.parent1DomainValue;
};
// Page.defaultLocaleBlur = function($event, widget) {
//     debugger;
//     if (Page.Widgets.defaultLocale.datavalue == undefined || Page.Widgets.defaultLocale.datavalue == "")
//         Page.Widgets.label7.widget.class = 'custom-font-color-red';
//     else
//         Page.Widgets.label7.widget.class = '';
// };

Page.descBlur = function($event, widget) {

    if (Page.Widgets.desc.datavalue == undefined || Page.Widgets.desc.datavalue == "") {
        Page.showDescriptionErrorMsg = true;
        Page.Widgets.label10.widget.class = 'custom-font-color-red';
        Page.Widgets.desc.class = 'bordercol';
    } else {
        Page.showDescriptionErrorMsg = false;
        Page.Widgets.label10.widget.class = '';
        Page.Widgets.desc.class = 'middlebox';
    }
};

Page.getUserName = function(id) {
    //debugger;
    id = parseInt(id);
    let user = Page.Variables.db_getallusers.dataSet.find(user => user.id === id);
    return user.firstName + " " + user.lastName;

};
Page.EditButtonClick = function($event, widget) {

    Page.Variables.readOnlyMode.dataSet.dataValue = false;
};