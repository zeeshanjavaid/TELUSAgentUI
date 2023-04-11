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
    Partial.Variables.teamsErrorMsg.dataSet.dataValue = null;
    Partial.Variables.teamsSuccessMessage.dataSet.dataValue = null;
    Partial.canceledClicked = false;
    Partial.teamExists = false;
    Partial.teamIdExists = false;
    Partial.isDeleteTeam = false;
    Partial.Variables.leftUserList.dataSet = [];

    if (Partial.pageParams.id === undefined) {
        Partial.Variables.readOnlyMode.dataSet.dataValue = false;
    }

};


App.addTeams = function() {

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

    Partial.isDeleteTeam = false;
    Partial.teamExists = false;
    Partial.teamIdExists = false;
    Partial.Widgets.TeamNameText.required = false;
    Partial.Variables.teamsErrorMsg.dataSet.dataValue = null;
    Partial.Variables.teamsSuccessMessage.dataSet.dataValue = null;
    let pattern = Partial.Widgets.TeamNameText.regexp;

    App.Variables.TeamPageCommunication.currentTeamInFocusId = Partial.pageParams.id;
    if (Partial.Widgets.TeamIdText.datavalue == undefined || Partial.Widgets.TeamIdText.datavalue == "") {
        Partial.Variables.teamsErrorMsg.dataSet.dataValue = "Team ID is mandatory";
        Partial.scrollToTop();
        Partial.Widgets.TeamIdText.required = true;

    } else if (Partial.Widgets.TeamNameText.datavalue == undefined || Partial.Widgets.TeamNameText.datavalue == "") {
        Partial.Variables.teamsErrorMsg.dataSet.dataValue = Partial.appLocale.TEAM_NAME_MANDATORY;
        Partial.scrollToTop();
        Partial.Widgets.TeamNameText.required = true;

    } else
    if (Partial.Widgets.TeamNameText.datavalue.match(pattern) == null) {
        Partial.Variables.teamsErrorMsg.dataSet.dataValue = "Team Name is invalid.";
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
    /*

        Partial.Variables.leftUserList.dataSet = data;
        data.forEach(function(u) {
            u.fullName = App.htmlEncode(u.userId);
        });
    */
};

Partial.scrollToTop = function() {
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
    Partial.Variables.teamsErrorMsg.dataSet.dataValue = null;
    Partial.Variables.teamsSuccessMessage.dataSet.dataValue = null;

    Partial.Variables.successMessage.dataSet.dataValue = Partial.appLocale.TEAM_CREATED_SUCCESSFULLY;
    Partial.scrollToTop();

    Partial.Widgets.DualListUsers_TD.rightdataset.forEach(function(user) {
        Partial.Variables.CreateTeamUser.setInput({
            'teamId': data.id,
            'userId': user.id
        });
        Partial.Variables.CreateTeamUser.invoke();
    })
    if (Partial.Widgets.DualListUsers_TD.rightdataset == null || Partial.Widgets.DualListUsers_TD.rightdataset.length == 0) {
        Partial.Variables.readOnlyMode.dataSet.dataValue = true;
        App.refreshAllTeams();
    }
    //App.Variables.TeamPageCommunication.currentTeamInFocusId = data.id;
};

Partial.CreateTeamUseronSuccess = function(variable, data) {

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

    Partial.Variables.executeDeleteTeamUser.setInput({
        'teamId': Partial.pageParams.id
    });
    Partial.Variables.executeDeleteTeamUser.invoke();

};

Partial.deleteTeamConfirmOkClick = function($event, widget) {

    Partial.isDeleteTeam = true;

    Partial.Widgets.deleteTeamDialog.close();
    Partial.Variables.executeDeleteTeamUser.setInput({
        'teamId': Partial.pageParams.id
    });
    Partial.Variables.executeDeleteTeamUser.invoke();


};

Partial.deleteTeamonSuccess = function(variable, data) {

    Partial.Variables.teamsErrorMsg.dataSet.dataValue = null;
    Partial.Variables.teamsSuccessMessage.dataSet.dataValue = null;
    Partial.Variables.teamsSuccessMessage.dataSet.dataValue = Partial.appLocale.TEAM_DELETED_SUCCESSFULLY;
    App.refreshAllTeams();

};

Partial.executeDeleteTeamUseronSuccess = function(variable, data) {

    Partial.Variables.teamsErrorMsg.dataSet.dataValue = null;
    Partial.Variables.teamsSuccessMessage.dataSet.dataValue = null;
    if (Partial.isDeleteTeam !== true) {
        Partial.Variables.teamsSuccessMessage.dataSet.dataValue = Partial.appLocale.TEAM_UPDATED_SUCCESSFULLY;

        Partial.Widgets.DualListUsers_TD.rightdataset.forEach(function(user) {
            Partial.Variables.CreateTeamUser.setInput({
                'teamId': Partial.pageParams.id,
                'userId': user.id
            });
            Partial.Variables.CreateTeamUser.invoke();
        })

        // Updating the team List
        App.refreshAllTeams();

    } else {

        Partial.Variables.deleteTeam.setInput({
            'id': Partial.pageParams.id
        });

        Partial.Variables.deleteTeam.invoke();
    }

};
Partial.EditTeamButtonClick = function($event, widget) {
    Partial.Variables.readOnlyMode.dataSet.dataValue = false;
};
Partial.DeleteButtonClick = function($event, widget) {
    Partial.Variables.readOnlyMode.dataSet.dataValue = false;
};
Partial.deleteTeamConfirmNoClick = function($event, widget) {
    Partial.Variables.readOnlyMode.dataSet.dataValue = true;

};