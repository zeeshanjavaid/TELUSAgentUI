/* perform any action on widgets/variables within this block */

Partial.onReady = function() {};

// Partial.OMHeaderNew1Selecteditemchanged = function($event, $data) {
//     // To do: Invoke the update current lob api
//     Partial.Variables.updateCurrentLob.setInput({
//         'lobId': $event.lobId,
//         'emailId': Partial.Variables.loggedInUser.dataSet.id
//     })
//     Partial.Variables.updateCurrentLob.invoke({}, function() {
//         // Go to main page and set the pageParam to current lobId.
//         if (Partial.App.activePageName === 'Main') {
//             window.location.href = window.location.href.split('?')[0] + '?lobId=' + $event.lobId;
//             window.location.reload(true);
//         } else {
//             Partial.Actions.goToPage_Main.invoke({
//                 data: {
//                     lobId: $event.lobId
//                 }
//             });
//         }
//     });
// };


App.subscribe("openSessionExpireDialog", function() {
    Partial.Widgets.sessionExpireBeforeDialog.open();
});
App.subscribe("sessionOver", function() {
    if (Partial.Widgets.sessionExpireBeforeDialog) {
        Partial.Widgets.sessionExpireBeforeDialog.close();
    }

    Partial.Widgets.sessionExpiredDialog.open();
});
Partial.sessionExpireBeforeDialogOpened = function($event, widget) {
    App.setSessionMessage();
};

Partial.stayLoggedInClick = function($event, widget) {
    App.Variables.getRemainingSessionTime.spinnerContext = "page";
    App.Variables.getRemainingSessionTime.invoke({}, function() {
        App.Variables.appAuthHeader.invoke();
    });
};

Partial.cancelSessionExpireClick = function($event, widget) {
    window.sessionStorage['lastActiveEventTime'] = moment().add(-2, 'minutes');
};
Partial.btnLoginClick = function($event, widget) {
    App.Actions.goToPage_Main.navigate();
};