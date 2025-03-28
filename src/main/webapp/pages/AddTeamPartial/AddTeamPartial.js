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



    // if (Partial.pageParams.id != Partial.Variables.getManagerNameByTeamId.dataBinding.teamId) {
    //     App.Variables.getManagerSelected.datsSet = undefined;
    // }
    // debugger;
    // App.selectedTM = [];

    // if (Partial.pageParams.id != undefined) {
    //     //     // Partial.Variables.getManagerNameByTeamId.setInput({
    //     //     //     'teamId': Partial.pageParams.id
    //     //     // });

    //     //     // Partial.Variables.getManagerNameByTeamId.invoke();
    // }

    // Partial.Variables.executeGetTeamManagerName.invoke();


    // if (App.Variables.getManagerSelected.datsSet != undefined) {

    //     for (let i = 0; i < App.Variables.getManagerSelected.datsSet.length; i++) {
    //         App.selectedTM[i] = App.Variables.getManagerSelected.datsSet[i].id;
    //     }
    // } else {
    //     App.selectedTM = null;

    // }

    debugger;

    // $('#teamManagerMutliSel').prop('disabled', true);
    // $("#teamManagerMutliSel").css("cursor", "not-allowed");
    // $("#teamManagerMutliSel").css("background-color", "#F2F2F2");

    /*$('#comboTree507078ArrowBtn').prop('disabled', true);*/



    // $('#teamManagerMutliSel_button').prop('disabled', true);




    // For multi Select manager
    // Partial.statusData = [];



    // Partial.Variables.getUserWhoIsManager.dataSet.forEach(function(item) {
    //     Partial.statusData.push({

    //         id: item.userId,
    //         title: item.firstName
    //     });
    // });

    // debugger;

    // subComboBox = $('#teamManagerMutliSel').comboTree({

    //     source: Partial.statusData,
    //     isMultiple: true,
    //     cascadeSelect: true,
    //     collapse: true,
    //     selected: selectedTM
    // });




    Partial.Variables.teamsErrorMsg.dataSet.dataValue = null;
    Partial.Variables.teamsSuccessMessage.dataSet.dataValue = null;
    Partial.canceledClicked = false;
    Partial.teamExists = false;
    Partial.teamIdExists = false;
    Partial.isDeleteTeam = false;
    Partial.Variables.leftUserList.dataSet = [];
    Partial.pageParams.isDeletedTeam = false;

    if (Partial.pageParams.id === undefined) {
        Partial.Variables.readOnlyMode.dataSet.dataValue = false;
    }

};

function messageTimeout() {
    Partial.Variables.teamsSuccessMessage.dataSet.dataValue = null;
    Partial.Variables.teamsErrorMsg.dataSet.dataValue = null;
}

App.addTeams = function() {

    debugger;

    // subComboBox.clearSelection();
    // checkedItem = $("input:checked")
    // checkedItem.prop('checked', false)

    Partial.Widgets.TeamManagerMultiSelect.deselectAll()
    Partial.Variables.teamsErrorMsg.dataSet.dataValue = null;
    Partial.Variables.teamsSuccessMessage.dataSet.dataValue = null;
    Partial.Variables.leftUserList.dataSet = [];
    Partial.Variables.selectedUsers.dataSet = [];
    Partial.Variables.teamData.dataSet = [];
    if (Partial.Widgets.DualListUsers_TD !== undefined) {
        Partial.Widgets.DualListUsers_TD.rightdataset = [];
        Partial.Widgets.DualListUsers_TD.leftdataset = [];
    }
    Partial.pageParams.id = undefined;
    Partial.Variables.getTeam.invoke();
    Partial.Variables.readOnlyMode.dataSet.dataValue = false;

};


Partial.SaveButtonClick = function($event, widget) {
    debugger;

    Partial.isDeleteTeam = false;
    Partial.teamExists = false;
    Partial.teamIdExists = false;
    Partial.Widgets.TeamNameText.required = false;
    Partial.Variables.teamsErrorMsg.dataSet.dataValue = null;
    Partial.Variables.teamsSuccessMessage.dataSet.dataValue = null;
    let pattern = Partial.Widgets.TeamNameText.regexp;

    App.Variables.TeamPageCommunication.currentTeamInFocusId = undefined;
    if (Partial.Widgets.TeamIdText.datavalue == undefined || Partial.Widgets.TeamIdText.datavalue == "") {
        Partial.Variables.teamsErrorMsg.dataSet.dataValue = "Team ID is mandatory";
        Partial.scrollToTop();
        Partial.Widgets.TeamIdText.required = true;
        setTimeout(messageTimeout, 5000);

    } else if (Partial.Widgets.TeamNameText.datavalue == undefined || Partial.Widgets.TeamNameText.datavalue == "") {
        Partial.Variables.teamsErrorMsg.dataSet.dataValue = Partial.appLocale.TEAM_NAME_MANDATORY;
        Partial.scrollToTop();
        Partial.Widgets.TeamNameText.required = true;
        setTimeout(messageTimeout, 5000);

    } else
    if (Partial.Widgets.TeamNameText.datavalue.match(pattern) == null) {
        Partial.Variables.teamsErrorMsg.dataSet.dataValue = "Team Name is invalid.";
        setTimeout(messageTimeout, 5000);
        Partial.scrollToTop();

    } else {
        if (Partial.pageParams.id) {
            Partial.Variables.getAllTeams.dataSet.forEach(function(team) {
                if (team.teamId.toLowerCase() == Partial.Widgets.TeamIdText.datavalue.toLowerCase() && team.id != Partial.pageParams.id) {
                    Partial.teamExists = true;
                }
            });
        } else {
            Partial.Variables.getAllTeams.dataSet.forEach(function(team) {
                if (team.teamId.toLowerCase() == Partial.Widgets.TeamIdText.datavalue.toLowerCase()) {
                    Partial.teamIdExists = true;
                }
            });
        }

        if (!Partial.teamExists && !Partial.teamIdExists) {
            if (Partial.pageParams.id && Partial.pageParams.id > 0) {
                Partial.Variables.updateTeam.setInput({
                    'id': Partial.pageParams.id,
                    'teamId': Partial.Variables.teamData.dataSet.TeamId,
                    'teamName': Partial.Variables.teamData.dataSet.TeamName,
                    'description': Partial.Variables.teamData.dataSet.TeamDescription,
                    'createdBy': Partial.Variables.getTeam.dataSet[0].createdBy,
                    'createdOn': Partial.Variables.getTeam.dataSet[0].createdOn,
                    'updatedBy': App.Variables.getLoggedInUserId.dataSet[0].id,
                    'updatedOn': new Date()
                });
                Partial.Variables.updateTeam.update();
            } else {
                Partial.Variables.createTeam.setInput({
                    'teamId': Partial.Variables.teamData.dataSet.TeamId,
                    'teamName': Partial.Variables.teamData.dataSet.TeamName,
                    'description': Partial.Variables.teamData.dataSet.TeamDescription,
                    'createdBy': App.Variables.getLoggedInUserId.dataSet[0].id,
                    'createdOn': new Date(),
                    'updatedBy': App.Variables.getLoggedInUserId.dataSet[0].id,
                    'updatedOn': new Date()
                });
                Partial.Variables.createTeam.invoke();
                /* Partial.Variables.teamsSuccessMessage.dataSet.dataValue = "Team is created. ";
                 setTimeout(messageTimeout, 5000);*/
            }


        } else if (Partial.teamExists) {
            Partial.Variables.teamsErrorMsg.dataSet.dataValue = Partial.appLocale.TEAM_ALREADY_EXIST;
            Partial.scrollToTop();

        } else {
            Partial.Variables.teamsErrorMsg.dataSet.dataValue = "TeamId already exists.";
            Partial.scrollToTop();
        }
    }

};

Partial.CancelbuttonClick = function($event, widget) {

    Partial.Variables.readOnlyMode.dataSet.dataValue = true;


    Partial.Variables.teamsErrorMsg.dataSet.dataValue = null;
    Partial.Variables.teamsSuccessMessage.dataSet.dataValue = null;
    Partial.isDeleteTeam = false;
    Partial.canceledClicked = true;

    if (Partial.pageParams.id) {
        if (Partial.Widgets.DualListUsers_TD !== undefined) {
            Partial.Widgets.DualListUsers_TD.rightdataset = Partial.Variables.selectedUsers.dataSet;
            Partial.Widgets.DualListUsers_TD.leftdataset = Partial.Variables.leftUserList.dataSet;
        }

    } else {
        Partial.Variables.teamData.dataSet = [];
        Partial.pageParams.id = undefined;
        if (Partial.Widgets.DualListUsers_TD !== undefined) {
            Partial.Widgets.DualListUsers_TD.rightdataset = [];
            Partial.Widgets.DualListUsers_TD.leftdataset = Partial.Variables.leftUserList.dataSet;
        }
    }
    App.refreshAllTeams();
};

Partial.getAllUsersonError = function(variable, data) {

};
Partial.getAllUsersonSuccess = function(variable, data) {


    Partial.Variables.leftUserList.dataSet = data;
    data.forEach(function(u) {
        u.fullName = App.htmlEncode(u.userId);
    });

};

Partial.scrollToTop = function() {
    debugger;
    const element = document.getElementById("title");
    if (element != null) {
        element.scrollIntoView({
            behavior: "smooth",
            block: "end",
            inline: "nearest"
        });
    }
};

Partial.getTeamonSuccess = function(variable, data) {
    debugger;
    if (!Partial.pageParams.id)
        data = [];

    Partial.Variables.selectedUsers.dataSet = [];
    Partial.Variables.leftUserList.dataSet = [];

    if (data.length > 0) {
        Partial.Variables.teamData.dataSet = {
            "TeamId": data[0].teamId,
            "TeamName": data[0].teamName,
            "TeamDescription": data[0].description != null ? data[0].description.replaceAll('&amp;', '&') : data[0].description

        }
    }
    Partial.Variables.getTeamUser.invoke();
};

Partial.getTeamUseronSuccess = function(variable, data) {
    debugger;
    Partial.TeamAssignUser = [];

    if (!Partial.pageParams.id) {
        data = [];
        Partial.TeamAssignUser = [];
    }
    data.forEach((d) => {
        Partial.TeamAssignUser.push(d.user);
    });

    Partial.Variables.teamData.dataSet = {
        "TeamAssignUser": Partial.TeamAssignUser,
        "TeamId": Partial.Variables.teamData.dataSet.TeamId,
        "TeamName": Partial.Variables.teamData.dataSet.TeamName,
        "TeamDescription": Partial.Variables.teamData.dataSet.TeamDescription

    }
    Partial.TeamAssignUser.forEach(function(sp) {
        sp.fullName = App.htmlEncode(sp.userId);
        Partial.Variables.selectedUsers.dataSet.push(sp);
    });
    if (Partial.Widgets.DualListUsers_TD !== undefined) {
        Partial.Widgets.DualListUsers_TD.rightdataset = Partial.Variables.selectedUsers.dataSet;
    }

    Partial.Variables.getAllUsers.dataSet.forEach((availableleftUser) => {
        let count = 0;
        data.forEach(function(i) {

            if (availableleftUser.id == i.userId) {
                count = count + 1;
            }
        });
        if (count == 0) {
            Partial.Variables.leftUserList.dataSet.push(availableleftUser);
        }
    });

    Partial.Variables.leftUserList.dataSet.forEach(function(p) {
        p.fullName = App.htmlEncode(p.userId);

    });

    if (Partial.Widgets.DualListUsers_TD !== undefined) {
        Partial.Widgets.DualListUsers_TD.leftdataset = Partial.Variables.leftUserList.dataSet;
    }
    // }

};

Partial.createTeamonSuccess = function(variable, data) {
    /* Partial.Variables.teamsErrorMsg.dataSet.dataValue = null;
     Partial.Variables.teamsSuccessMessage.dataSet.dataValue = null;*/
    debugger;
    /*Partial.Variables.teamsSuccessMessage.dataSet.dataValue = "TEAM CREATED SUCCESSFULLY";*/
    Partial.Variables.teamsSuccessMessage.dataSet.dataValue = Partial.appLocale.TEAM_CREATED_SUCCESSFULLY;
    setTimeout(messageTimeout, 5000);
    App.refreshTeamsOnAdminPage();
    Partial.scrollToTop();

    if (Partial.Widgets.DualListUsers_TD.rightdataset.length > 0) {
        Partial.Widgets.DualListUsers_TD.rightdataset.forEach(function(user) {
            Partial.Variables.CreateTeamUser.setInput({
                'teamId': data.id,
                'userId': user.id
            });
            Partial.Variables.CreateTeamUser.invoke();
        })
    }

    if (Partial.Widgets.TeamManagerMultiSelect.datavalue.length > 0) {
        // var managerId = subComboBox.getSelectedIds();

        //  for (let i = 0; i <= subComboBox.getSelectedIds().length; i++) {



        Partial.Variables.saveManagerOnTeamCreateVar.setInput({
            'teamId': data.id,
            'managerIdListString': Partial.Widgets.TeamManagerMultiSelect.datavalue.toString(),
            'createdBy': App.Variables.getLoggedInUserId.dataSet[0].id,
            'createdOn': getCurrentDate(),
            'updatedBy': App.Variables.getLoggedInUserId.dataSet[0].id,
            'updatedOn': getCurrentDate()
        });
        Partial.Variables.saveManagerOnTeamCreateVar.invoke();

        //     }


    }

    if (Partial.Widgets.DualListUsers_TD.rightdataset == null || Partial.Widgets.DualListUsers_TD.rightdataset.length == 0) {
        Partial.Variables.readOnlyMode.dataSet.dataValue = true;
        App.refreshAllTeams();
    }
    //App.Variables.TeamPageCommunication.currentTeamInFocusId = data.id;
};

Partial.CreateTeamUseronSuccess = function(variable, data) {
    debugger;
    if (Partial.pageParams.id && Partial.pageParams.id > 0 && Partial.Variables.teamsSuccessMessage.dataSet.dataValue == null || Partial.Variables.teamsSuccessMessage.dataSet.dataValue == "") {

        Partial.Variables.teamsSuccessMessage.dataSet.dataValue = Partial.appLocale.TEAM_UPDATED_SUCCESSFULLY;
        //App.Variables.TeamPageCommunication.currentTeamInFocusId = Partial.pageParams.id;
    } else if (Partial.Variables.teamsSuccessMessage.dataSet.dataValue == null || Partial.Variables.teamsSuccessMessage.dataSet.dataValue == "") {
        Partial.Variables.teamsSuccessMessage.dataSet.dataValue = Partial.appLocale.TEAM_CREATED_SUCCESSFULLY;

    }
    Partial.scrollToTop();
    Partial.Variables.readOnlyMode.dataSet.dataValue = true;
    App.refreshAllTeams();

};

Partial.updateTeamonSuccess = function(variable, data) {

    debugger;

    var selectedManagerName = Partial.Widgets.TeamManagerMultiSelect.datavalue;
    setTimeout(messageTimeout, 5000);
    App.refreshTeamsOnAdminPage();
    App.refreshAllTeams();
    Partial.scrollToTop();


    Partial.Variables.executeDeleteTeamUser.setInput({
        'teamId': Partial.pageParams.id
    });
    Partial.Variables.executeDeleteTeamUser.invoke();

    // if (selectedManagerName != null) {

    // Partial.Variables.deleteTeamManager.setInput({
    //     'id': Partial.Variables.executeGetManagerByTeamId.dataSet[0].id
    // });
    // Partial.Variables.deleteTeamManager.invoke();

    Partial.Variables.deleteTeamManagerOnUpdateVar.setInput({
        'teamId': Partial.pageParams.id
    });

    Partial.Variables.deleteTeamManagerOnUpdateVar.invoke();

    if (selectedManagerName.length > 0) {

        Partial.Variables.saveManagerOnTeamCreateVar.setInput({
            'teamId': data.id,
            'managerIdListString': selectedManagerName.toString(),
            'createdBy': App.Variables.getLoggedInUserId.dataSet[0].id,
            'createdOn': getCurrentDate(),
            'updatedBy': App.Variables.getLoggedInUserId.dataSet[0].id,
            'updatedOn': getCurrentDate()
        });

        Partial.Variables.saveManagerOnTeamCreateVar.invoke();

    }

    Partial.scrollToTop();
    Partial.Variables.readOnlyMode.dataSet.dataValue = true;
    App.refreshAllTeams();



};

Partial.deleteTeamConfirmOkClick = function($event, widget) {

    debugger;

    Partial.isDeleteTeam = true;

    Partial.pageParams.isDeletedTeam = true;

    Partial.Widgets.deleteTeamDialog.close();


    //Del Team Manager
    Partial.Variables.deleteTeamManagerByTeamId.setInput({
        'teamId': Partial.pageParams.id
    });
    Partial.Variables.deleteTeamManagerByTeamId.invoke();



    // Del team user
    Partial.Variables.executeDeleteTeamUser.setInput({
        'teamId': Partial.pageParams.id
    });
    Partial.Variables.executeDeleteTeamUser.invoke();



    Partial.Variables.readOnlyMode.dataSet.dataValue = true;
    App.refreshAllTeams();



};

Partial.deleteTeamonSuccess = function(variable, data) {

    debugger;

    // if (Partial.pageParams.isDeletedTeam) {
    //     Partial.Variables.getManagerNameByTeamId.setInput({
    //         'teamId': Partial.Variables.getAllTeams.dataSet[Partial.Variables.getAllTeams.dataSet.length - 2].id
    //     });

    //     Partial.Variables.getManagerNameByTeamId.invoke();
    // }

    Partial.Variables.teamsErrorMsg.dataSet.dataValue = null;
    Partial.Variables.teamsSuccessMessage.dataSet.dataValue = null;
    Partial.Variables.teamsSuccessMessage.dataSet.dataValue = Partial.appLocale.TEAM_DELETED_SUCCESSFULLY;
    setTimeout(messageTimeout, 6000);
    App.refreshAllTeams();

};

Partial.executeDeleteTeamUseronSuccess = function(variable, data) {

    debugger;
    Partial.Variables.teamsErrorMsg.dataSet.dataValue = null;
    Partial.Variables.teamsSuccessMessage.dataSet.dataValue = null;
    if (Partial.isDeleteTeam !== true) {
        Partial.Variables.teamsSuccessMessage.dataSet.dataValue = Partial.appLocale.TEAM_UPDATED_SUCCESSFULLY;
        setTimeout(messageTimeout, 5000);
        Partial.Widgets.DualListUsers_TD.rightdataset.forEach(function(user) {
            Partial.Variables.CreateTeamUser.setInput({
                'teamId': Partial.pageParams.id,
                'userId': user.id
            });
            Partial.Variables.CreateTeamUser.invoke();

        })

    } else {

        Partial.Variables.deleteTeam.setInput({
            'id': Partial.pageParams.id
        });

        Partial.Variables.deleteTeam.invoke();
    }

    // Updating the team List
    // App.refreshAllTeams();
    // Partial.scrollToTop();
    // Partial.Variables.readOnlyMode.dataSet.dataValue = true;
    // $('#teamManagerMutliSel').prop('disabled', true);

    Partial.Variables.readOnlyMode.dataSet.dataValue = true;
    App.refreshAllTeams();

};
Partial.EditTeamButtonClick = function($event, widget) {
    debugger;
    Partial.Variables.readOnlyMode.dataSet.dataValue = false;
    $('#teamManagerMutliSel').prop('disabled', false);
    $("#teamManagerMutliSel").css("cursor", "default");
    $("#teamManagerMutliSel").css("background-color", "#ffffff");
    Partial.Variables.getTeam.invoke();
    App.refreshTeamManager();
};
Partial.DeleteButtonClick = function($event, widget) {
    Partial.Variables.readOnlyMode.dataSet.dataValue = false;
};
Partial.deleteTeamConfirmNoClick = function($event, widget) {
    Partial.Variables.readOnlyMode.dataSet.dataValue = true;

};

function getCurrentDate() {
    var currentDate = new Date().toJSON().slice(0, 10);
    return currentDate;
}

Partial.saveManagerOnTeamCreateVaronSuccess = function(variable, data) {
    debugger;
    Partial.Variables.getManagerNameByTeamId.setInput({
        'teamId': variable.dataBinding.teamId
    });

    Partial.Variables.getManagerNameByTeamId.invoke();

    App.refreshAllTeams();

};

App.refreshTeamManager = function() {
    debugger;
    Partial.Variables.getUserWithManager.invoke();
}

App.refreshTeam = function() {
    debugger;
    Partial.Variables.getTeam.setInput({
        'teamId': ''
    });
    Partial.Variables.getTeam.invoke();
}

App.refreshTeamUser = function() {
    Partial.Variables.getAllTeams.invoke();
}



Partial.getUserWithManageronSuccess = function(variable, data) {
    debugger;

    Partial.Variables.getUserWhoIsManager.dataSet = data;
};