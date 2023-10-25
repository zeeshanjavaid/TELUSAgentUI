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
    debugger;
    Page.Widgets.text4._datavalue = '';
    Page.Widgets.text5._datavalue = '';
    // var ex1 = document.getElementById('name');
    // // var ex2 = document.getElementById('numID');
    App.Variables.errorMsg.dataSet.dataValue = null;
    Page.Variables.number_IdCriteria.dataSet = [];
    if (Page.Variables.entityLevelTypeVal.dataSet.length > 0) {
        Page.Variables.number_IdCriteria.dataSet = Page.Variables.entityLevelTypeVal.dataSet;
    } else {
        Page.Variables.entityLevelTypeVal.invoke();
    }
    // ex1.onclick = handle1;
    // ex2.onclick = handle2;

    handleBanPhoneNumber();

    Page.Variables.getEntitySearch.setInput({

        'inputType': Page.pageParams.inputType,
        'inputValue': Page.pageParams.entityId,
        'level': Page.pageParams.inputlevel,
        'billingSystem': Page.pageParams.billingSystem,
        'searchMatchCriteria': Page.pageParams.searchMatchCriteria,
        'limit': 10,
        'offset': 0

    });

    Page.Variables.getEntitySearch.invoke();



}


/*
 * variables can be accessed through 'Page.Variables' property here
 * e.g. to get dataSet in a staticVariable named 'loggedInUser' use following script
 * Page.Variables.loggedInUser.getData()
 *
 * widgets can be accessed through 'Page.Widgets' property here
 * e.g. to get value of text widget named 'username' use following script
 * 'Page.Widgets.username.datavalue'
 */

// function handle1() {
//     debugger;
//     alert(ex1);
//     //  ex1.checked = true;
//     ex2.checked = false;

// }

// function handle2() {
//     debugger;
//     alert('clicked');
//     $(ex1).removeAttr('checked');
//     //  ex2.checked = true;
//     ex1.checked = false;

// }

Page.navigateToEntityPage = function(entityId) {
    debugger;



    // if (!App.IsUserHasAccess('DNTL')) {
    //     window.location.href = '#/ErrorLanding';
    // } else {
    //     // document.getElementsByTagName("html")[0].style.visibility = "visible";
    //     // return "#/Lookup?entityId=" + (!entityId ? 0 : entityId);
    // }






    return "#/Lookup?entityId=" + (!entityId ? 0 : entityId);
}


Page.button1Click = function($event, widget) {
    //  App.Variables.errorMsg.dataSet.dataValue = null;
    debugger;
    const message = document.getElementById("p01");
    const message1 = document.getElementById("p02");
    const message2 = document.getElementById("p03");
    const message3 = document.getElementById("p04");
    const message4 = document.getElementById("p05");

    var isError = false;

    message.innerHTML = "";
    message1.innerHTML = "";
    message2.innerHTML = "";
    message3.innerHTML = "";
    message4.innerHTML = "";
    var intputType = "";
    var inputValue = "";
    var searchCriteria = "";

    var value = $("input:radio[name=checkAndUncheck]:checked").val();

    if (Page.Widgets.select1.datavalue == "BAN" && value === undefined) {
        intputType = "PHONENUMBER";
        inputValue = Page.Widgets.text3._datavalue;
        searchCriteria = 'exactMatch'
    } else {

        var value = $("input:radio[name=checkAndUncheck]:checked").val();

        if (Page.Widgets.text3._datavalue != '' && Page.Widgets.text3._datavalue != undefined) {
            try {
                isError = true;
                throw "Please clear phone number"
            } catch (err) {
                message.innerHTML = err;
            }
        }

        if (value === undefined && Page.Widgets.text4._datavalue !== "") {
            try {
                isError = true;
                throw "Please click on Name"
            } catch (err) {
                message3.innerHTML = err;
            }
        }
        if (value === undefined && Page.Widgets.text5._datavalue !== "") {

            try {
                isError = true;
                throw "Please click on Number/ID"
            } catch (err) {
                message4.innerHTML = err;
            }
        }




        if (value == "NAME") {
            intputType = "NAME";
            searchCriteria = Page.Widgets.select3.datavalue;
            if (Page.Widgets.text4._datavalue === undefined || Page.Widgets.text4._datavalue == "") {
                try {
                    isError = true;
                    throw "Required field";
                } catch (err) {
                    message1.innerHTML = err;
                }
            } else {
                inputValue = Page.Widgets.text4._datavalue;

            }
        } else if (value == "numID") {
            intputType = "ID";
            searchCriteria = Page.Widgets.select4.datavalue;
            if (Page.Widgets.text5._datavalue === undefined || Page.Widgets.text5._datavalue === "") {
                try {
                    isError = true;
                    throw "Required field";
                } catch (err) {
                    message2.innerHTML = err;
                }
            } else {
                inputValue = Page.Widgets.text5._datavalue;


            }

        }
    }
    if (!isError) {
        Page.Variables.getEntitySearch.setInput({

            'inputType': intputType,
            'inputValue': inputValue,
            'level': Page.Widgets.select1.datavalue,
            'billingSystem': Page.Widgets.select5.datavalue,
            'searchMatchCriteria': searchCriteria,
            'limit': 10,
            'offset': 0

        });

        Page.Variables.getEntitySearch.invoke();
    }


};
Page.button2Click = function($event, widget) {

    debugger;


    const message = document.getElementById("p01");
    const message2 = document.getElementById("p02");
    const message3 = document.getElementById("p03");
    // const message4 = document.getElementById("p05");

    Page.Widgets.select1.datavalue = 'Entity';
    Page.Widgets.select3.datavalue = 'exactMatch';
    Page.Widgets.select4.datavalue = 'exactMatch';
    //Page.Widgets.select5.datavalue = 'Select';
    Page.Widgets.text3._datavalue = '';
    Page.Widgets.text4._datavalue = '';
    Page.Widgets.text5._datavalue = '';

    message.innerHTML = "";
    message2.innerHTML = "";
    message3.innerHTML = "";
    // message4.innerHTML = "";
    Page.Widgets.text5.disabled = true;
    Page.Widgets.text4.disabled = true;
    Page.Widgets.select3.disabled = false;
    Page.Widgets.select4.disabled = false;
    $('#name').prop('disabled', false);
    $('#numID').prop('disabled', false);

    $("input:radio[name=checkAndUncheck]:checked").prop('checked', false);

    // console.clear();

    Page.Variables.getEntitySearch.setInput({

        'inputType': "",
        'inputValue': "",
        'level': "",
        'billingSystem': "",
        'searchMatchCriteria': ""

    });

    Page.Variables.getEntitySearch.invoke();



    debugger;
};

Page.checkAndUncheck = function($event, widget) {
    debugger;
}

Page.goToEnityPage = function(row) {

    debugger;

    var id = Page.Variables.loggedInUser.dataSet.id;

    if (row.dntlFlag) {

        var permissionList = Page.Variables.getPermissionByUserId.dataSet;
        var permissionName = permissionList.find(item => item.name === 'DNTL');
        if (permissionName === undefined) {
            // alert("User do nt have permission");

            Page.Widgets.inactivateUserDialog.open();
        } else {
            Page.Actions.goToPage_Lookup.setData({
                "entityId": row.entityId,
                "inputlevel": Page.Variables.getEntitySearch.dataBinding.level,
                "inputType": Page.Variables.getEntitySearch.dataBinding.inputType,
                "searchMatchCriteria": Page.Variables.getEntitySearch.dataBinding.searchMatchCriteria,
                "billingSystem": Page.Variables.getEntitySearch.dataBinding.billingSystem
            })
            Page.Actions.goToPage_Lookup.navigate();
            //window.location.href = "#/Lookup?entityId=" + (!row.entityId ? 0 : row.entityId);
        }

    } else {
        Page.Actions.goToPage_Lookup.setData({
            "entityId": row.entityId,
            "inputlevel": Page.Variables.getEntitySearch.dataBinding.level,
            "inputType": Page.Variables.getEntitySearch.dataBinding.inputType,
            "searchMatchCriteria": Page.Variables.getEntitySearch.dataBinding.searchMatchCriteria,
            "billingSystem": Page.Variables.getEntitySearch.dataBinding.billingSystem
        })
        Page.Actions.goToPage_Lookup.navigate();
        //window.location.href = "#/Lookup?entityId=" + (!row.entityId ? 0 : row.entityId);
    }

}

function resetVals() {
    $("name:checkAndUncheck").each(function() {
        $(this)[0].checked = false;
        //console.log($(this)); //print id element
    });
}

$("#mySelection").change(function() {
    debugger;
    resetVals();
});





// Jquery to enable test area on select radio button
$(function() {
    $("#name, #numID").change(function() {
        const message1 = document.getElementById("p02");
        const message2 = document.getElementById("p03");

        if ($("#name").is(":checked")) {
            Page.Widgets.text4.disabled = false;
            message2.innerHTML = "";
            Page.Widgets.text5._datavalue = '';
            Page.Widgets.text5.disabled = true;

        } else if ($("#numID").is(":checked")) {
            Page.Widgets.text5.disabled = false
            Page.Widgets.text4._datavalue = '';
            message1.innerHTML = "";
            Page.Widgets.text4.disabled = true;
        }
    });
});


Page.getEntitySearchonError = function(variable, data) {

    // App.Variables.errorMsg.dataSet.dataValue = "No data found";

};

// Page.select1Change = function($event, widget, newVal, oldVal) {


//     if (Page.Widgets.select1.datavalue == 'BAN') {

//         $("input:radio[name=checkAndUncheck]:checked").prop('checked', false);

//         Page.Widgets.select3.disabled = true;

//         Page.Widgets.select4.disabled = true;

//         Page.Widgets.text4._datavalue = '';

//         Page.Widgets.text4._datavalue = '';

//         Page.Widgets.text5._datavalue = '';

//         Page.Widgets.text4.disabled = true;

//         Page.Widgets.text5.disabled = true;

//         $('#name').prop('disabled', true);

//         $('#numID').prop('disabled', true);

//     } else if (Page.Widgets.select1.datavalue == 'CBUCID' || Page.Widgets.select1.datavalue == 'RCID' || Page.Widgets.select1.datavalue == 'Entity') {
//         $("input:radio[name=checkAndUncheck]:checked").prop('checked', false);

//         Page.Widgets.select3.disabled = false;

//         Page.Widgets.select4.disabled = false;

//         Page.Widgets.text4._datavalue = '';

//         Page.Widgets.text5._datavalue = '';

//         Page.Widgets.text4.disabled = true;

//         Page.Widgets.text5.disabled = true;

//         $('#name').prop('disabled', false);

//         $('#numID').prop('disabled', false);

//     }
// };


/*Page.select1Change = function($event, widget, newVal, oldVal) {
    if (Page.Widgets.select1.datavalue == 'BAN') {
        $("input:radio[name=checkAndUncheck]:checked").prop('checked', false);
        Page.Widgets.select3.disabled = true;
        Page.Widgets.select4.disabled = true;
        Page.Widgets.text4._datavalue = '';
        Page.Widgets.text4._datavalue = '';
        Page.Widgets.text5._datavalue = '';
        Page.Widgets.text4.disabled = true;
        Page.Widgets.text5.disabled = true;
        $('#name').prop('disabled', true);
        $('#numID').prop('disabled', true);
    } else if (Page.Widgets.select1.datavalue == 'CBUCID' || Page.Widgets.select1.datavalue == 'RCID') {
        selectTypeChange();
        Page.Variables.number_IdCriteria.dataSet = Page.Variables.levelTypeValue.dataSet;
    } else if (Page.Widgets.select1.datavalue == 'Entity' || Page.Widgets.select1.datavalue == 'BAN') {
        selectTypeChange();
        Page.Variables.number_IdCriteria.dataSet = Page.Variables.entityLevelTypeVal.dataSet;
    }
};*/

Page.select1Change = function($event, widget, newVal, oldVal) {
    debugger;
    if (Page.Widgets.select1.datavalue == 'CBUCID' || Page.Widgets.select1.datavalue == 'RCID' || Page.Widgets.select1.datavalue == 'BAN') {
        Page.Variables.number_IdCriteria.dataSet = Page.Variables.levelTypeValue.dataSet;
    } else if (Page.Widgets.select1.datavalue == 'Entity') {
        Page.Variables.number_IdCriteria.dataSet = Page.Variables.entityLevelTypeVal.dataSet;
        Page.Widgets.text3._datavalue = '';
    }
};

function selectTypeChange() {
    $("input:radio[name=checkAndUncheck]:checked").prop('checked', false);
    Page.Widgets.select4._datavalue = '';
    Page.Widgets.select3.disabled = false;
    Page.Widgets.select4.disabled = false;
    Page.Widgets.text4._datavalue = '';
    Page.Widgets.text5._datavalue = '';
    Page.Widgets.text4.disabled = true;
    Page.Widgets.text5.disabled = true;
    $('#name').prop('disabled', false);
    $('#numID').prop('disabled', false);
};

function handleBanPhoneNumber() {
    $('#banPhNo').click(function(e) {
        $("input:radio[name=checkAndUncheck]:checked").prop('checked', false);
        Page.Widgets.select3.disabled = true;
        Page.Widgets.select4.disabled = true;
        Page.Widgets.text4._datavalue = '';
        Page.Widgets.text5._datavalue = '';
        Page.Widgets.text4.disabled = true;
        Page.Widgets.text5.disabled = true;
        $('#name').prop('disabled', true);
        $('#numID').prop('disabled', true);
    });
};

Page.text3Blur = function($event, widget) {

    debugger;

    $("input:radio[name=checkAndUncheck]:checked").prop('checked', false);
    Page.Widgets.select4._datavalue = '';
    Page.Widgets.select3.disabled = false;
    Page.Widgets.select4.disabled = false;
    Page.Widgets.text4._datavalue = '';
    Page.Widgets.text5._datavalue = '';
    Page.Widgets.text4.disabled = true;
    Page.Widgets.text5.disabled = true;
    $('#name').prop('disabled', false);
    $('#numID').prop('disabled', false);
    // $('input[type=text]').each(function() {
    //     $(this).val('');
    // });

};

Page.Telus_PaginatonPagechange = function($event, $data) {
    debugger;
    Page.size = $event.pageSize
    Page.page = $event.pageNumber
    Page.RefreshData();

};

Page.RefreshData = function() {
    debugger;
    var offset = Page.size * (Page.page - 1);
    Page.Variables.getEntitySearch.setInput({
        'limit': Page.size,
        'offset': offset
    });
    Page.Variables.getEntitySearch.invoke();

}