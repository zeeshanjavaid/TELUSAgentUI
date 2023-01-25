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
Prefab.leftFilteredList = [];
Prefab.rightFilteredList = [];

Prefab.onPropertyChange = function(key, newVal, oldVal) {
    //debugger;
    switch (key) {
        case "leftdataset":
            //debugger;
            if (Array.isArray(newVal)) {
                newVal = newVal.filter(function(x) {
                    return x[Prefab.displayfield] !== null;
                });
                var rhsData = Prefab.Variables.rightDatasetModel &&
                    Prefab.Variables.rightDatasetModel.getData();
                if (!!rhsData && rhsData.length > 0 && Prefab.reducerightdataset) {
                    // remove selected options from the left data set
                    newVal = reduceRightDataSetFromLeftDataSet(newVal, rhsData);
                }
                Prefab.Variables.leftDatasetModel.setData(newVal);
                Prefab.Widgets.selectLeft.dataset = [];
                Prefab.Widgets.selectLeft.dataset = Prefab.Variables.leftDatasetModel.dataSet;
            }
            break;

        case "rightdataset":
            if (Array.isArray(newVal)) {
                newVal = newVal.filter(function(x) {
                    return x[Prefab.displayfield] !== null;
                });
                var lhsData = Prefab.Variables.leftDatasetModel &&
                    Prefab.Variables.leftDatasetModel.getData();
                if (!!lhsData && lhsData.length > 0 && Prefab.reducerightdataset) {
                    // remove selected options from the left data set
                    lhsData = reduceRightDataSetFromLeftDataSet(lhsData, newVal);
                    Prefab.Variables.leftDatasetModel.setData(lhsData);
                    Prefab.Widgets.selectLeft.dataset = [];
                    Prefab.Widgets.selectLeft.dataset = Prefab.Variables.leftDatasetModel.dataSet;
                }
                // if (Prefab.sortonload) {
                //     Prefab.sortData(newVal);
                // } else {
                Prefab.Variables.rightDatasetModel.setData(newVal);
                Prefab.Widgets.selectRight.dataset = [];
                Prefab.Widgets.selectRight.dataset = Prefab.Variables.rightDatasetModel.dataSet;
                // }
                // Prefab.onSelecteditemschange(newVal);

            } else if (Prefab.new) {
                //debugger;
                if (!newVal.length > 0) {
                    Prefab.buttonRemoveAllClick();
                }
            }
            break;
    }
};

// Prefab.sortData = function(rightDataSet) {
//     rightDataSet.sort(function(a, b) {
//         if (a[Prefab.displayfield] < b[Prefab.displayfield]) //sort string ascending
//             return -1
//         if (a[Prefab.displayfield] > b[Prefab.displayfield])
//             return 1
//         return 0 //default return value (no sorting)
//     });
//     Prefab.Variables.rightDatasetModel.setData(rightDataSet);
//     Prefab.sortonload = false;
// }
/**
 * Removes the selected options in the rhs data set from the lhs data set
 * lhsDataSet: Array<object>
 * rhsDataSet: Array<object>
 */
function reduceRightDataSetFromLeftDataSet(lhsDataSet, rhsDataSet) {
    for (var i = 0; i < rhsDataSet.length; i++) {
        var rhsOption = rhsDataSet[i];
        lhsDataSet = lhsDataSet.filter(function(lhsOption) {
            return lhsOption[Prefab.displayfield] !== rhsOption[Prefab.displayfield];
        });
    }
    return lhsDataSet;
}

// Polyfill for 'remove()' to support in IE browser 
var removeElemPolyFill = function() {
    if (!('remove' in Element.prototype)) {
        Element.prototype.remove = function() {
            if (this.parentNode) {
                this.parentNode.removeChild(this);
            }
        };
    }
};

// remove the initial empty option from select tag
var removeBlankOption = function(nativeElem) {
    var selectControl = nativeElem.getElementsByClassName('dualList-select');

    if (!!selectControl && selectControl.length > 0) {
        if (selectControl[0].options[0].value === "0: ''") {
            selectControl[0].options[0].remove(); // if initial option in every select tag is empty, then remove it
        }
    }
};

function removeEmptyOptions() {
    $('[name="selectLeft"] > option').each(function(index, element) {
        if (element.value == "0: ''")
            $(element).remove()
    });

    $('[name="selectRight"] > option').each(function(index, element) {
        if (element.value == "0: ''")
            $(element).remove()
    });
}

Prefab.onReady = function() {
    removeEmptyOptions();
};

Prefab.buttonAddItemsClick = function($event, widget) {
    var selectedItems = Prefab.Widgets.selectLeft.datavalue || [];

    if (selectedItems.length > 0) {
        var rightList = Prefab.Variables.rightDatasetModel.getData().concat(selectedItems);
        var fieldId = Prefab.identifier || Prefab.displayfield;
        var fieldNameArr = selectedItems.map(function(x) {
            return x[fieldId];
        });
        var leftList = Prefab.Variables.leftDatasetModel.getData().filter(function(x) {
            return fieldNameArr.indexOf(x[fieldId]) === -1;
        });
        Prefab.Variables.leftDatasetModel.setData(leftList);
        Prefab.Variables.rightDatasetModel.setData(rightList);
        Prefab.onSelecteditemschange(rightList);
        Prefab.Widgets.selectLeft.datavalue = void 0;

        Prefab.clearFilters();
    }
    /***********FSTU-5437*************/
    //this code will restore button css even after clicked
    // widget.$element[0].after(widget.$element[0]);
    //FOR IE 11 - .after method was not supported in IE11
    widget.$element[0].parentNode.insertBefore(widget.$element[0], widget.$element[0].nextSibling);
    /***********FSTU-5437*************/
};

Prefab.buttonRemoveItemsClick = function($event, widget) {
    var selectedItems = Prefab.Widgets.selectRight.datavalue || [];

    if (selectedItems.length > 0) {
        var leftList = Prefab.Variables.leftDatasetModel.getData().concat(selectedItems);
        var fieldId = Prefab.identifier || Prefab.displayfield;
        var fieldNameArr = selectedItems.map(function(x) {
            return x[fieldId];
        });
        var rightList = Prefab.Variables.rightDatasetModel.getData().filter(function(x) {
            return fieldNameArr.indexOf(x[fieldId]) === -1;
        });

        Prefab.Variables.leftDatasetModel.setData(leftList);
        Prefab.Variables.rightDatasetModel.setData(rightList);

        Prefab.onSelecteditemschange(rightList);
        Prefab.Widgets.selectRight.datavalue = void 0;

        Prefab.clearFilters();
    }
    /***********FSTU-5437*************/
    //this code will restore button css even after clicked
    //FOR IE 11 - .after method was not supported in IE11
    widget.$element[0].parentNode.insertBefore(widget.$element[0], widget.$element[0].nextSibling);
    /***********FSTU-5437*************/
};

Prefab.buttonAddAllClick = function($event, widget) {
    var rightList = [];
    var leftList = []; // If active filter, then add only filtered values
    if (Prefab.leftFilteredList.length > 0) {
        rightList = Prefab.Variables.rightDatasetModel.getData().concat(Prefab.leftFilteredList);
        var fieldId = Prefab.identifier || Prefab.displayfield;
        var fieldNameArr = Prefab.leftFilteredList.map(function(x) {
            return x[fieldId];
        });
        leftList = Prefab.Variables.leftDatasetModel.getData().filter(function(x) {
            return fieldNameArr.indexOf(x[fieldId]) === -1;
        });
        Prefab.leftFilteredList = [];
        Prefab.Widgets.textFilterLeft.datavalue = '';
    } else {
        rightList = Prefab.Variables.rightDatasetModel.getData().concat(Prefab.Variables.leftDatasetModel.getData());
    }

    Prefab.Variables.leftDatasetModel.setData(leftList);
    Prefab.Variables.rightDatasetModel.setData(rightList);
    Prefab.onSelecteditemschange(rightList);
    Prefab.Widgets.selectLeft.dataset = Prefab.Variables.leftDatasetModel.getData();
    Prefab.Widgets.selectRight.dataset = Prefab.Variables.rightDatasetModel.getData();
    Prefab.rightFilteredList = [];
    Prefab.Widgets.textFilterRight.datavalue = '';
};

Prefab.buttonRemoveAllClick = function($event, widget) {
    //debugger;
    var rightList = [];
    var leftList = [];

    if (Prefab.rightFilteredList.length > 0) {
        leftList = Prefab.Variables.leftDatasetModel.getData().concat(Prefab.rightFilteredList);
        var fieldId = Prefab.identifier || Prefab.displayfield;
        var fieldNameArr = Prefab.rightFilteredList.map(function(x) {
            return x[fieldId];
        });
        rightList = Prefab.Variables.rightDatasetModel.getData().filter(function(x) {
            return fieldNameArr.indexOf(x[fieldId]) === -1;
        });
        Prefab.rightFilteredList = [];
        Prefab.Widgets.textFilterRight.datavalue = '';
    } else {
        leftList = Prefab.Variables.leftDatasetModel.getData().concat(Prefab.Variables.rightDatasetModel.getData());
    }

    Prefab.Variables.leftDatasetModel.setData(leftList);
    Prefab.Variables.rightDatasetModel.setData(rightList);
    Prefab.onSelecteditemschange(rightList);
    Prefab.Widgets.selectRight.dataset = Prefab.Variables.rightDatasetModel.getData(); // If left side has a filter, reset it.

    Prefab.Widgets.selectLeft.dataset = Prefab.Variables.leftDatasetModel.dataSet;
    Prefab.leftFilteredList = [];
    Prefab.Widgets.textFilterLeft.datavalue = '';
};

// textFilterLeftChange
Prefab.textFilterLeftChange = function($event, widget, newVal, oldVal) {
    if (newVal) {
        var fieldName = Prefab.displayfield;
        if (fieldName == "firstName + ' ' + lastName") {
            Prefab.leftFilteredList = Prefab.Variables.leftDatasetModel.getData().filter(function(x) {
                return (x['firstName'] && x['firstName'].toLowerCase().indexOf(newVal.toLowerCase()) > -1) ||
                    (x['lastName'] && x['lastName'].toLowerCase().indexOf(newVal.toLowerCase()) > -1);
            });
        } else {
            Prefab.leftFilteredList = Prefab.Variables.leftDatasetModel.getData().filter(function(x) {
                return x[fieldName] && x[fieldName].toLowerCase().indexOf(newVal.toLowerCase()) > -1;
            });
        }
        Prefab.Widgets.selectLeft.dataset = Prefab.leftFilteredList;
        Prefab.Widgets.selectLeft.datavalue = void 0;
    } else {
        Prefab.leftFilteredList = [];
        Prefab.Widgets.selectLeft.dataset = Prefab.Variables.leftDatasetModel.getData();
    }
};

Prefab.textFilterRightChange = function($event, widget, newVal, oldVal) {
    if (newVal) {
        var fieldName = Prefab.displayfield;
        if (fieldName == "firstName + ' ' + lastName") {
            Prefab.rightFilteredList = Prefab.Variables.rightDatasetModel.getData().filter(function(x) {
                return x['firstName'].toLowerCase().indexOf(newVal.toLowerCase()) > -1 || x['lastName'].toLowerCase().indexOf(newVal.toLowerCase()) > -1;
            });
        } else {
            Prefab.rightFilteredList = Prefab.Variables.rightDatasetModel.getData().filter(function(x) {
                return x[fieldName].toLowerCase().indexOf(newVal.toLowerCase()) > -1;
            });
        }
        Prefab.Widgets.selectRight.dataset = Prefab.rightFilteredList;
        Prefab.Widgets.selectRight.datavalue = void 0;
    } else {
        Prefab.rightFilteredList = [];
        Prefab.Widgets.selectRight.dataset = Prefab.Variables.rightDatasetModel.getData();
    }
};

Prefab.btnShowAllLeftClick = function($event, widget) {
    Prefab.Widgets.selectLeft.dataset = Prefab.Variables.leftDatasetModel.getData();
    Prefab.leftFilteredList = [];
    Prefab.Widgets.textFilterLeft.datavalue = '';
};

Prefab.btnShowAllRightClick = function($event, widget) {
    Prefab.Widgets.selectRight.dataset = Prefab.Variables.rightDatasetModel.getData();
    Prefab.rightFilteredList = [];
    Prefab.Widgets.textFilterRight.datavalue = '';
};

Prefab.clearFilters = function() {
    Prefab.Widgets.selectLeft.dataset = Prefab.Variables.leftDatasetModel.getData();
    Prefab.Widgets.selectRight.dataset = Prefab.Variables.rightDatasetModel.getData();
    Prefab.leftFilteredList = [];
    Prefab.rightFilteredList = [];
    Prefab.Widgets.textFilterLeft.datavalue = '';
    Prefab.Widgets.textFilterRight.datavalue = '';
};

/*
 * Returns the list of items selected by the user.
 */
Prefab.getSelectedItems = function() {
    return Prefab.Variables.rightDatasetModel.getData();
};