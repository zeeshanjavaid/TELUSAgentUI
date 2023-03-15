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



Partial.isEditDV = false;
Partial.isEditLocale = false;
Partial.localeListWithoughtEn = {};
Partial.initialObj = {}; //This hold the page form data before any changes
Partial.relationToAction = null;
Partial.showLocaleErrorMsg = false;

/* these two added for select input workaround on relationdetails form 
 * as data was getting vanished from one dropdown when selecting the other one
 */
Partial.parent1DVSelected = null;
Partial.parent2DVSelected = null;
Partial.filteredLocale = null;

// $('document').ready(() => {
//     document.getElementsByTagName("html")[0].style.visibility = "hidden";
// });

/* perform any action on widgets/variables within this block */

Partial.onReady = function() {
    debugger;
    // initPage();
    Partial.filteredLocale = JSON.parse(JSON.stringify(App.Variables.supportedLocale.dataSet), (key, value) => {
        if (key === 'en') {
            return
        }
        return value;
    });

};




function initPage() {

    debugger;
    // const intervalId = setInterval(function() {
    //  if (App.permissionsLoaded) {
    // if (true) {
    //     clearInterval(intervalId);
    //     console.log('Permissions loaded...');

    //     //  if (!App.IsUserHasAccess('Setup_DomainValues')) {
    //     if (!App.IsUserHasAccess('System Administration')) {
    //         window.location.href = '#/ErrorLanding';
    //     } else {
    //   document.getElementsByTagName("html")[0].style.visibility = "visible";

    //if (Partial.pageParams.domainValueId) {

    if (4) {



        debugger;
        //    Partial.Variables.getDomainValueById.setInput('domainValueId', Partial.Variables.pageParams.domainValueId);

        var ar = Partial.pageParams.domainValueId;

        Partial.Variables.getDomainValueById.setInput({
            'domainValueId': ar
        });
        Partial.Variables.getDomainValueById.invoke();
        Partial.isEditDV = true;

    } else {
        debugger;
        Partial.Variables.mv_domainValue.dataSet = {
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
            "active": false,
            "code": "",
            "defaultLocale": {
                "deleted": false,
                "locale": "en",
                "description": ""
            },
            "default": false
        };
        // Partial.initialObj = JSON.parse(JSON.stringify(Partial.Widgets.domainValueForm.dataset));
    }
    //Converting supported locale json into list
    let obj = Partial.Variables.supportedLocale.dataSet;
    let result = Object.keys(obj).map(e => {
        let ret = {};
        ret = {
            "locale": e,
            "localeName": obj[e]
        };
        return ret;
    });
    debugger;
    Partial.Variables.localesListWithoutDesc.dataSet = result.filter(item => item.locale != "en");

    if (Partial.isEditDV) {
        Partial.Widgets.code.class = 'noteditable';
        Partial.Widgets.createdOn.class = 'noteditable';
        Partial.Widgets.updatedOn.class = 'noteditable';
    }
    Partial.Widgets.createdOn.class = 'noteditable';
    Partial.Widgets.updatedOn.class = 'noteditable';

    // Partial.initialObj = JSON.parse(JSON.stringify(Partial.Widgets.domainValueForm.dataset));
    debugger;
    //   }

    //     } else {
    //         //determining the time elapsed since App started in minutes
    //         const timeElapsedSinceAppStart = moment(new Date()).diff(moment(App.appStartTime), 'minutes');

    //         if (timeElapsedSinceAppStart < 1)
    //             console.log('Waiting to load permissions...');
    //         else {
    //             clearInterval(intervalId);

    //             //if the active page is not 'ErrorLanding'
    //             if (window.location.hash !== '#/ErrorLanding')
    //                 window.location.href = '#/ErrorLanding';
    //         }
    //     }
    // }, );
}

App.addOrUpdateDomainValue = function() {



    debugger;
    //  document.getElementsByTagName("html")[0].style.visibility = "visible";





    if (App.Variables.domainValueData.dataSet.id) {



        debugger;
        //    Partial.Variables.getDomainValueById.setInput('domainValueId', Partial.Variables.pageParams.domainValueId);

        var ar = App.Variables.domainValueData.dataSet.id;

        Partial.Variables.getDomainValueById.setInput({
            'domainValueId': ar
        });
        Partial.Variables.getDomainValueById.invoke();
        Partial.isEditDV = true;
    } else {
        debugger;
        Partial.Variables.mv_domainValue.dataSet = {
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
            "active": false,
            "code": "",
            "defaultLocale": {
                "deleted": false,
                "locale": "en",
                "description": ""
            },
            "default": false
        };
        Partial.initialObj = JSON.parse(JSON.stringify(Partial.Widgets.domainValueForm.dataset));
    }
    //Converting supported locale json into list
    let obj = Partial.Variables.supportedLocale.dataSet;
    let result = Object.keys(obj).map(e => {
        let ret = {};
        ret = {
            "locale": e,
            "localeName": obj[e]
        };
        return ret;
    });
    debugger;
    Partial.Variables.localesListWithoutDesc.dataSet = result.filter(item => item.locale != "en");

    if (Partial.isEditDV) {
        Partial.Widgets.code.class = 'noteditable';
        Partial.Widgets.createdOn.class = 'noteditable';
        Partial.Widgets.updatedOn.class = 'noteditable';
    }
    Partial.Widgets.createdOn.class = 'noteditable';
    Partial.Widgets.updatedOn.class = 'noteditable';

    Partial.initialObj = JSON.parse(JSON.stringify(Partial.Widgets.domainValueForm.dataset));
    debugger;
    $("#create_div").show();



};

App.createDomainValue = function() {



    debugger;
    //  document.getElementsByTagName("html")[0].style.visibility = "visible";





    // if (Partial.pageParams.domainValueId) {



    //     debugger;
    //     //    Partial.Variables.getDomainValueById.setInput('domainValueId', Partial.Variables.pageParams.domainValueId);

    //     var ar = Partial.pageParams.domainValueId;

    //     Partial.Variables.getDomainValueById.setInput({
    //         'domainValueId': ar
    //     });
    //     Partial.Variables.getDomainValueById.invoke();
    //     Partial.isEditDV = true;
    // } else {
    debugger;
    Partial.Variables.mv_domainValue.dataSet = {
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
        "active": false,
        "code": "",
        "defaultLocale": {
            "deleted": false,
            "locale": "en",
            "description": ""
        },
        "default": false
    };
    Partial.initialObj = JSON.parse(JSON.stringify(Partial.Widgets.domainValueForm.dataset));
    // }
    //Converting supported locale json into list
    let obj = Partial.Variables.supportedLocale.dataSet;
    let result = Object.keys(obj).map(e => {
        let ret = {};
        ret = {
            "locale": e,
            "localeName": obj[e]
        };
        return ret;
    });
    debugger;
    Partial.Variables.localesListWithoutDesc.dataSet = result.filter(item => item.locale != "en");

    // if (Partial.isEditDV) {
    //     Partial.Widgets.code.class = 'noteditable';
    //     Partial.Widgets.createdOn.class = 'noteditable';
    //     Partial.Widgets.updatedOn.class = 'noteditable';
    // }
    Partial.Widgets.createdOn.class = 'noteditable';
    Partial.Widgets.updatedOn.class = 'noteditable';

    Partial.initialObj = JSON.parse(JSON.stringify(Partial.Widgets.domainValueForm.dataset));
    debugger;
    $("#create_div").show();


};



Partial.getDomainValueByIdonSuccess = function(variable, data) {
    if (data) {
        Partial.Variables.mv_domainValue.dataSet = data;
    }
    Partial.initialObj = JSON.parse(JSON.stringify(Partial.Variables.mv_domainValue.dataSet));
    console.log("Partial.initialObj", Partial.initialObj);
};

Partial.getDomainValueTypeonSuccess = function(variable, data) {
    Partial.Variables.mv_domainValue.dataSet.domainValueType = {
        "description": data[0].description,
        "code": data[0].code,
        "id": data[0].id
    };
    Partial.initialObj = JSON.parse(JSON.stringify(Partial.Variables.mv_domainValue.dataSet));
    console.log("Partial.initialObj", Partial.initialObj);


};

Partial.displayCustomUserAndDateValues = function(timestamp, id) {
    //debugger;
    //let jsTimeStamp = new Date(timestamp);
    let ts_toShow = App.localizeTimestampToAppTimezone(timestamp)
    let username = (id != "null") ? Partial.getUserName(id) : "";
    return ts_toShow + " by " + (!username ? "" : username);
};

Partial.saveLocaleBtnClick = function($event, widget) {


    debugger
    if (Partial.Widgets.localeDetailsForm.invalid) {
        Partial.Widgets.localeDetailsForm.highlightInvalidFields();
    } else {
        if (!Partial.isEditLocale) {
            var localeAlreadyexists = false;
            Partial.Variables.mv_domainValue.dataSet.domainValueDescription.forEach(function(dvsItem) {
                if (dvsItem.locale === Partial.Widgets.localeDetailsForm.formWidgets.locale.datavalue)
                    localeAlreadyexists = true;
            });
            if (localeAlreadyexists) {
                //debugger
                Partial.Variables.localePopupMessage.dataSet.dataValue = "Locale already exists";
                Partial.showLocaleErrorMsg = true;
            } else {
                Partial.Variables.localePopupMessage.dataSet.dataValue = "";
                Partial.showLocaleErrorMsg = false;
                Partial.Variables.mv_domainValue.dataSet.domainValueDescription.push({
                    "deleted": false,
                    "locale": Partial.Widgets.localeDetailsForm.formWidgets.locale.datavalue,
                    "description": Partial.Widgets.localeDetailsForm.formWidgets.description.datavalue
                });

                Partial.Widgets.domainValueDescriptionTable.dataset = Partial.Variables.mv_domainValue.dataSet.domainValueDescription;
                Partial.Widgets.localeDetailsDialog.close();
            }
        } else {
            Partial.localePopupMessage = "";
            Partial.showLocaleErrorMsg = false;
            Partial.Variables.mv_domainValue.dataSet.domainValueDescription.forEach(function(dvsItem) {
                if (dvsItem.locale === Partial.Widgets.localeDetailsForm.formWidgets.locale.datavalue)
                    dvsItem.description = Partial.Widgets.localeDetailsForm.formWidgets.description.datavalue;
            });

            Partial.Widgets.domainValueDescriptionTable.dataset = Partial.Variables.mv_domainValue.dataSet.domainValueDescription;
            Partial.Widgets.localeDetailsDialog.close();
        }
    }
};
Partial.domainValueDescriptionTable_updaterowAction = function($event, row) {
    // debugger
    Partial.isEditLocale = true;
    Partial.Variables.localeDetails.dataSet = row;
    Partial.Widgets.localeDetailsDialog.open();
};
Partial.domainValueDescriptionTable_deleterowAction = function($event, row) {
    //debugger
    Partial.Variables.mv_domainValue.dataSet.domainValueDescription.forEach(function(domainValueDescription, index) {
        if (domainValueDescription.locale === row.locale)
            Partial.Variables.mv_domainValue.dataSet.domainValueDescription.splice(index, 1);
    });
    Partial.Widgets.domainValueDescriptionTable.dataset = Partial.Variables.mv_domainValue.dataSet.domainValueDescription;
};

Partial.getDomainValueTypeRelationshipByIdonSuccess = function(variable, data) {
    //debugger
    if (data.length > 0) {
        if (Partial.Variables.getDomainValueTypeRelationshipById.dataSet.data[0].domainValueTypeByParentDomainValueTypeId1 != null) {
            Partial.Variables.getDVlistParent1.invoke();
        }
        if (Partial.Variables.getDomainValueTypeRelationshipById.dataSet.data[0].domainValueTypeByParentDomainValueTypeId2 != null) {
            Partial.Variables.getDVlistParent2.invoke();
        }
    }
};

Partial.saveBtnRelationDetailsClick = function($event, widget) {
    if (Partial.Widgets.relationDetailsForm.invalid) {
        Partial.Widgets.relationDetailsForm.highlightInvalidFields();
    } else {
        if (Partial.Variables.mv_domainValue.dataSet.domainValueRelation.length != 0) {
            //check for uniqueness of this parent combination
            let isCombinationExists = false,
                matchingCombinations = [];
            matchingCombinations = Partial.Variables.mv_domainValue.dataSet.domainValueRelation.filter((combinationItem, index) => {
                if (Partial.Variables.getDomainValueTypeRelationshipById.dataSet.data[0].parentDomainValueTypeId2 != null) {
                    return ((combinationItem.parent1DomainValueId === Partial.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue.id) &&
                        (combinationItem.parent2DomainValueId === Partial.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue.id));
                } else
                    return ((combinationItem.parent1DomainValueId === Partial.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue.id));
            });

            isCombinationExists = (matchingCombinations && matchingCombinations.length > 0);

            if (isCombinationExists) {
                let combinationItem = null;
                combinationItem = matchingCombinations.find((combination, index) => {
                    return (!combination.deleted);
                });


                if (combinationItem) {
                    //display error
                    Partial.Variables.relationPopupMessage.setValue('dataValue', 'Parent already exists for this domain value. Select a unique parent.');
                    $("p[name='relationPopupMsg']").removeClass("display-none").addClass("display-block");
                    setTimeout(() => {
                        $("p[name='relationPopupMsg']").removeClass("display-block").addClass("display-none");
                        Partial.Variables.relationPopupMessage.setValue('dataValue', null);
                    }, 5000);

                } else {
                    if (Partial.relationToAction.$index === -1) {
                        Partial.Variables.mv_domainValue.dataSet.domainValueRelation.push({

                            "deleted": false,
                            "parent1DomainValueId": Partial.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue ? Partial.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue.id : null,
                            "parent2DomainValueId": Partial.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue ? Partial.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue.id : null,
                            "parent1DomainValueDescription": Partial.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue ? Partial.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue.description : null,
                            "parent2DomainValueDescription": Partial.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue ? Partial.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue.description : null,
                        });
                    } else {
                        Partial.relationToAction.data.parent1DomainValueId = Partial.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue ? Partial.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue.id : null;
                        Partial.relationToAction.data.parent2DomainValueId = Partial.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue ? Partial.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue.id : null;
                        Partial.relationToAction.data.parent1DomainValueDescription = Partial.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue ? Partial.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue.description : null;
                        Partial.relationToAction.data.parent2DomainValueDescription = Partial.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue ? Partial.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue.description : null;

                        Partial.Variables.mv_domainValue.dataSet.domainValueRelation[Partial.relationToAction.$index] = Partial.relationToAction.data;
                    }

                    Partial.Widgets.domainValueRelationTable.dataset = Partial.Variables.mv_domainValue.dataSet.domainValueRelation;
                    Partial.Widgets.relationDetailsDialog.close();
                }
            } else {
                if (Partial.relationToAction.$index === -1) {
                    Partial.Variables.mv_domainValue.dataSet.domainValueRelation.push({

                        "deleted": false,
                        "parent1DomainValueId": Partial.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue ? Partial.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue.id : null,
                        "parent2DomainValueId": Partial.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue ? Partial.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue.id : null,
                        "parent1DomainValueDescription": Partial.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue ? Partial.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue.description : null,
                        "parent2DomainValueDescription": Partial.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue ? Partial.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue.description : null,
                    });
                } else {
                    Partial.relationToAction.data.parent1DomainValueId = Partial.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue ? Partial.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue.id : null;
                    Partial.relationToAction.data.parent2DomainValueId = Partial.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue ? Partial.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue.id : null;
                    Partial.relationToAction.data.parent1DomainValueDescription = Partial.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue ? Partial.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue.description : null;
                    Partial.relationToAction.data.parent2DomainValueDescription = Partial.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue ? Partial.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue.description : null;

                    Partial.Variables.mv_domainValue.dataSet.domainValueRelation[Partial.relationToAction.$index] = Partial.relationToAction.data;
                }

                Partial.Widgets.domainValueRelationTable.dataset = Partial.Variables.mv_domainValue.dataSet.domainValueRelation;
                Partial.Widgets.relationDetailsDialog.close();
            }

        } else {
            if (Partial.relationToAction.$index === -1) {
                Partial.Variables.mv_domainValue.dataSet.domainValueRelation.push({

                    "deleted": false,
                    "parent1DomainValueId": Partial.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue ? Partial.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue.id : null,
                    "parent2DomainValueId": Partial.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue ? Partial.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue.id : null,
                    "parent1DomainValueDescription": Partial.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue ? Partial.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue.description : null,
                    "parent2DomainValueDescription": Partial.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue ? Partial.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue.description : null,
                });
            } else {
                Partial.relationToAction.data.parent1DomainValueId = Partial.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue ? Partial.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue.id : null;
                Partial.relationToAction.data.parent2DomainValueId = Partial.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue ? Partial.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue.id : null;
                Partial.relationToAction.data.parent1DomainValueDescription = Partial.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue ? Partial.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue.description : null;
                Partial.relationToAction.data.parent2DomainValueDescription = Partial.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue ? Partial.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue.description : null;

                Partial.Variables.mv_domainValue.dataSet.domainValueRelation[Partial.relationToAction.$index] = Partial.relationToAction.data;
            }

            Partial.Widgets.domainValueRelationTable.dataset = Partial.Variables.mv_domainValue.dataSet.domainValueRelation;
            Partial.Widgets.relationDetailsDialog.close();
        }
    }
};

Partial.domainValueRelationTable_deleterowAction = function($event, row) {
    Partial.relationToAction = {};
    Partial.relationToAction.$index = -1;
    Partial.relationToAction.data = row;

    Partial.relationToAction.$index = Partial.Variables.mv_domainValue.getData().domainValueRelation.findIndex((relationRec) => {
        return (relationRec.parent1DomainValueId === row.parent1DomainValueId &&
            relationRec.parent2DomainValueId === row.parent2DomainValueId && !(relationRec.deleted));
    });

    Partial.Widgets.relationDelete_confirmDialog.open();
};

Partial.domainValueRelationTable_updaterowAction = function($event, row) {
    Partial.relationToAction = {};
    Partial.relationToAction.$index = -1;
    Partial.relationToAction.data = row;

    Partial.relationToAction.$index = Partial.Variables.mv_domainValue.getData().domainValueRelation.findIndex((relationRec) => {
        return (relationRec.parent1DomainValueId === row.parent1DomainValueId &&
            relationRec.parent2DomainValueId === row.parent2DomainValueId);
    });

    //find the parent1DV & parent2DV
    let parent1DVRecord = null,
        parent2DVRecord = null;
    parent1DVRecord = Partial.Variables.getDVlistParent1.getData().pageContent.find((dvrec) => {
        return (dvrec.id === row.parent1DomainValueId);
    });

    if (Partial.Variables.getDomainValueTypeRelationshipById.dataSet.data[0].parentDomainValueTypeId2 != null) {
        parent2DVRecord = Partial.Variables.getDVlistParent2.getData().pageContent.find((dvrec) => {
            return (dvrec.id === row.parent2DomainValueId);
        });
    }

    Partial.Variables.relationDetails.setData({
        "parent1DomainValue": parent1DVRecord ? parent1DVRecord : null,
        "parent2DomainValue": parent2DVRecord ? parent2DVRecord : null
    });

    Partial.Widgets.relationDetailsDialog.open();

};

Partial.saveBtnClick = function($event, widget) {
    debugger;
    // console.log(Partial.Variables.mv_domainValue.getData().code);
    // console.log(Partial.Variables.getDVlistByDomainValueTypeId.getData());
    //To check unique domain value code
    Partial.Variables.isCreateDomainValue.dataSet.dataValue = true;
    let isUniqueDV = true;

    if (Partial.pageParams.domainValueId === undefined || Partial.pageParams.domainValueId === 0) {
        let dvRecord = Partial.Variables.getDVlistByDomainValueTypeId.getData().pageContent.find((dvData) => {
            return (dvData.code === Partial.Variables.mv_domainValue.getData().code);
        });

        isUniqueDV = dvRecord ? false : true;
    }

    if (Partial.Widgets.domainValueForm.invalid || Partial.Widgets.desc.datavalue == undefined || Partial.Widgets.desc.datavalue == "") {

        if (Partial.Widgets.desc.datavalue == undefined || Partial.Widgets.desc.datavalue == "") {
            Partial.ErrorMessage = "Review invalid fields";
            Partial.ShowErrorMessage = true;
            Partial.showDescriptionErrorMsg = true;
            Partial.Widgets.label10.widget.class = 'custom-font-color-red';
            Partial.Widgets.desc.class = 'bordercol';
        } else {
            Partial.ErrorMessage = "";
            Partial.ShowErrorMessage = false;
            Partial.showDescriptionErrorMsg = false;
            Partial.Widgets.label10.widget.class = '';
            Partial.Widgets.desc.class = 'middlebox';
        }

        Partial.ErrorMessage = "Review invalid fields";
        Partial.ShowErrorMessage = true;
        Partial.Widgets.domainValueForm.highlightInvalidFields();

        $('html, body').animate({
            scrollTop: $('[name="errorMsg"]').offset().top - 20
        }, 200);
    } else if (!isUniqueDV) {
        Partial.ErrorMessage = "Domain value with this code already exists. Enter a unique code.";
        Partial.ShowErrorMessage = true;
        $('html, body').animate({
            scrollTop: $('[name="errorMsg"]').offset().top - 20
        }, 200);
    } else {
        if (!Partial.pageParams.domainValueId) {
            if (Partial.Variables.getDomainValueTypeRelationshipById.dataSet.data.length > 0 && Partial.Variables.getDomainValueTypeRelationshipById.dataSet.data[0].domainValueTypeByParentDomainValueTypeId1 != null) {
                Partial.Variables.mv_domainValue.dataSet.domainValueTypeRelation.parentDV1Type = {
                    "description": Partial.Variables.getDomainValueTypeRelationshipById.dataSet.data[0].domainValueTypeByParentDomainValueTypeId1.description,
                    "code": Partial.Variables.getDomainValueTypeRelationshipById.dataSet.data[0].domainValueTypeByParentDomainValueTypeId1.code,
                    "id": Partial.Variables.getDomainValueTypeRelationshipById.dataSet.data[0].domainValueTypeByParentDomainValueTypeId1.id
                };
                Partial.Variables.mv_domainValue.dataSet.domainValueTypeRelation.relationPresent = true;
            }
            if (Partial.Variables.getDomainValueTypeRelationshipById.dataSet.data.length > 0 && Partial.Variables.getDomainValueTypeRelationshipById.dataSet.data[0].domainValueTypeByParentDomainValueTypeId2 != null) {
                Partial.Variables.mv_domainValue.dataSet.domainValueTypeRelation.parentDV2Type = {
                    "description": Partial.Variables.getDomainValueTypeRelationshipById.dataSet.data[0].domainValueTypeByParentDomainValueTypeId2.description,
                    "code": Partial.Variables.getDomainValueTypeRelationshipById.dataSet.data[0].domainValueTypeByParentDomainValueTypeId2.code,
                    "id": Partial.Variables.getDomainValueTypeRelationshipById.dataSet.data[0].domainValueTypeByParentDomainValueTypeId2.id
                };
                Partial.Variables.mv_domainValue.dataSet.domainValueTypeRelation.relationPresent = true;
            }
            Partial.Variables.mv_domainValue.dataSet.active = true;
            Partial.Variables.mv_domainValue.dataSet.createdBy = Partial.Variables.getLoggedInUserId.dataSet[0].id;
        }

        Partial.Variables.mv_domainValue.dataSet.updatedBy = Partial.Variables.getLoggedInUserId.dataSet[0].id;
        console.log(Partial.Variables.mv_domainValue.dataSet);
        Partial.Variables.saveDomainValue.setInput('DomainValueVO', Partial.Variables.mv_domainValue.dataSet);
        debugger;
        Partial.Variables.saveDomainValue.invoke();
    }

    debugger;

};

Partial.saveDomainValueonSuccess = function(variable, data) {
    //debugger
    if (data) {
        Partial.Variables.mv_domainValue.dataSet = data;
    }
    Partial.Actions.goToPage_DomainValuePartial.invoke({
        data: {
            'domainValueTypeId': Partial.pageParams.domainValueTypeId
        }
    });
};

Partial.localeDetailsDialogClose = function($event, widget) {
    Partial.isEditLocale = false;
    Partial.Variables.localeDetails.dataSet = {};
};

Partial.exitConfirmDialogCancel = function($event, widget) {
    // Partial.Actions.goToPage_DomainValuePartial.invoke({
    //     data: {
    //         'domainValueTypeId': Partial.pageParams.domainValueTypeId
    //     }
    // });
    //$("#create_div").hide();
    //    $("#dmValue").show();
    $("#create_div").css("display", "none");
    $("#dmValue").css("display", "block");


};

Partial.relationDetailsDialogOpened = function($event, widget) {
    //hide the popup msg section
    Partial.Variables.relationPopupMessage.setValue('dataValue', null);
    $("p[name='relationPopupMsg']").removeClass("display-block").addClass("display-none");

    Partial.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue = Partial.Variables.relationDetails.getData().parent1DomainValue;
    Partial.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue = Partial.Variables.relationDetails.getData().parent2DomainValue;

    Partial.parent1DomainValue = Partial.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue;
    Partial.parent2DomainValue = Partial.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue;
};

Partial.addRelationBtnClick = function($event, widget) {
    Partial.relationToAction = {};
    Partial.relationToAction.$index = -1;
    Partial.relationToAction.data = null;

    //reset the relation variable
    Partial.Variables.relationDetails.setData({
        "parent1DomainValue": null,
        "parent2DomainValue": null
    });

    Partial.Widgets.relationDetailsDialog.open();
};

Partial.relationDelete_confirmDialogOk = function($event, widget) {
    if (Partial.relationToAction.data) {
        let row = Partial.relationToAction.data;

        if ((Partial.relationToAction.$index !== -1) && Partial.Variables.mv_domainValue.dataSet.domainValueRelation[Partial.relationToAction.$index].id &&
            Partial.Variables.mv_domainValue.dataSet.domainValueRelation[Partial.relationToAction.$index].id > 0) {
            Partial.Variables.mv_domainValue.dataSet.domainValueRelation[Partial.relationToAction.$index].deleted = true;
            Partial.Widgets.domainValueRelationTable.dataset = Partial.Variables.mv_domainValue.dataSet.domainValueRelation;
        } else if ((Partial.relationToAction.$index !== -1) && !Partial.Variables.mv_domainValue.dataSet.domainValueRelation[Partial.relationToAction.$index].id) {
            Partial.Variables.mv_domainValue.dataSet.domainValueRelation.splice(Partial.relationToAction.$index, 1);
            Partial.Widgets.domainValueRelationTable.dataset = Partial.Variables.mv_domainValue.dataSet.domainValueRelation;
        }
    }

    Partial.relationToAction = {};
    Partial.relationToAction.$index = -1;
    Partial.relationToAction.data = null;

    Partial.Widgets.relationDelete_confirmDialog.close();
};

Partial.relationDetailsDialogClose = function($event, widget) {
    Partial.relationToAction = {};
    Partial.relationToAction.$index = -1;
    Partial.relationToAction.data = null;

    //reset the relation variable
    Partial.Variables.relationDetails.setData({
        "parent1DomainValue": null,
        "parent2DomainValue": null
    });
};

Partial.buttonCancelClick = function($event, widget) {
    //debugger;
    //  Partial.Variables.isCreateDomainValue.dataSet.dataValue = false;
    // if (JSON.stringify(Partial.initialObj) == JSON.stringify(Partial.Widgets.domainValueForm.dataset)) {
    //     Partial.exitConfirmDialogCancel();
    // } else if (_.isEqual(Partial.initialObj, Partial.Widgets.domainValueForm.dataset)) {
    //     Partial.exitConfirmDialogCancel();
    // } else {
    //     Partial.Widgets.exitConfirmDialog.open();
    // }
    $("#create_div").css("display", "none");
    $("#dmValue").css("display", "block");
};

/* these two added for select input workaround on relationdetails form 
 * as data was getting vanished from one dropdown when selecting the other one
 */
Partial.dvParent1Change = function($event, widget, newVal, oldVal) {
    Partial.parent1DomainValue = newVal;
    Partial.Widgets.relationDetailsForm.formWidgets.dvParent2.datavalue = Partial.parent2DomainValue;
};

Partial.dvParent2Change = function($event, widget, newVal, oldVal) {
    Partial.parent2DomainValue = newVal;
    Partial.Widgets.relationDetailsForm.formWidgets.dvParent1.datavalue = Partial.parent1DomainValue;
};
// Partial.defaultLocaleBlur = function($event, widget) {
//     debugger;
//     if (Partial.Widgets.defaultLocale.datavalue == undefined || Partial.Widgets.defaultLocale.datavalue == "")
//         Partial.Widgets.label7.widget.class = 'custom-font-color-red';
//     else
//         Partial.Widgets.label7.widget.class = '';
// };

Partial.descBlur = function($event, widget) {

    if (Partial.Widgets.desc.datavalue == undefined || Partial.Widgets.desc.datavalue == "") {
        Partial.showDescriptionErrorMsg = true;
        Partial.Widgets.label10.widget.class = 'custom-font-color-red';
        Partial.Widgets.desc.class = 'bordercol';
    } else {
        Partial.showDescriptionErrorMsg = false;
        Partial.Widgets.label10.widget.class = '';
        Partial.Widgets.desc.class = 'middlebox';
    }
};

Partial.getUserName = function(id) {
    //debugger;
    id = parseInt(id);
    let user = Partial.Variables.db_getallusers.dataSet.find(user => user.id === id);
    return user.firstName + " " + user.lastName;

};