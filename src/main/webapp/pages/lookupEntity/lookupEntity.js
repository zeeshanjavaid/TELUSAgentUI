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

    Page.Widgets.text4._datavalue = '';
    Page.Widgets.text5._datavalue = '';
    // var ex1 = document.getElementById('name');
    // // var ex2 = document.getElementById('numID');


    // ex1.onclick = handle1;
    // ex2.onclick = handle2;

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

    debugger;
    const message = document.getElementById("p01");
    const message1 = document.getElementById("p02");
    const message2 = document.getElementById("p03");
    const message3 = document.getElementById("p04");
    const message4 = document.getElementById("p05");

    var isError = false;

    // message.innerHTML = "";
    message1.innerHTML = "";
    message2.innerHTML = "";
    message3.innerHTML = "";
    message4.innerHTML = "";
    var intputType = "";
    var inputValue = "";
    var searchCriteria = "";


    // if (Page.Widgets.select1.datavalue === "BAN") {

    //     if (Page.Widgets.text3._datavalue === undefined || Page.Widgets.text3._datavalue == "") {
    //         try {
    //             throw "Required field";
    //         } catch (err) {
    //             message.innerHTML = err;
    //         }
    //     } else {
    //         intputType = "phoneNumber";
    //         inputValue = Page.Widgets.text3._datavalue;
    //     }



    // } else {
    var value = $("input:radio[name=checkAndUncheck]:checked").val();

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
        intputType = "name";
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
        intputType = "id";
        searchCriteria = Page.Widgets.select4.datavalue;
        if (Page.Widgets.text5._datavalue === undefined || Page.Widgets.text5._datavalue == "") {
            try {
                isError = true;
                throw "Required field";
            } catch (err) {
                message2.innerHTML = err;
            }
        } else {
            inputValue = Page.Widgets.text5._datavalue;


        }
        //  }
    }

    if (!isError) {
        Page.Variables.searchEntity.setInput({

            'inputType': intputType,
            'inputValue': inputValue,
            'level': Page.Widgets.select1.datavalue,
            'billingSystem': Page.Widgets.select5.datavalue,
            'searchMatchCriteria': searchCriteria

        });

        Page.Variables.searchEntity.invoke();
    }


};
Page.button2Click = function($event, widget) {

    debugger;
    $("input:radio[name=checkAndUncheck]:checked").prop('checked', false);
    const message1 = document.getElementById("p02");
    const message2 = document.getElementById("p03");
    const message3 = document.getElementById("p04");
    const message4 = document.getElementById("p05");

    Page.Widgets.select1.datavalue = 'Entity';
    Page.Widgets.select3.datavalue = 'Exact match';
    Page.Widgets.select4.datavalue = 'Exact match';
    //Page.Widgets.select5.datavalue = 'Select';
    Page.Widgets.text3._datavalue = '';
    Page.Widgets.text4._datavalue = '';
    Page.Widgets.text5._datavalue = '';

    message2.innerHTML = "";
    message3.innerHTML = "";
    message1.innerHTML = "";
    message4.innerHTML = "";

    // console.clear();

    Page.Variables.searchEntity.invoke();



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
            window.location.href = "#/Lookup?entityId=" + (!row.entityId ? 0 : row.entityId);
        }

    } else {
        window.location.href = "#/Lookup?entityId=" + (!row.entityId ? 0 : row.entityId);
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