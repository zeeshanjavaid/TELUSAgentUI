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
    debugger;
    var getOwerIdFromEntityDetailsVar = Partial.Variables.getOwerIdFromEntityDetails;

    getOwerIdFromEntityDetailsVar.invoke({
            "inputFields": {
                "entityId": Partial.pageParams.entityId
            },
        },
        function(data) {
            debugger;
            var getNameUsingEmpIdVar = Partial.Variables.getNameUsingEmpIdVar;
            getNameUsingEmpIdVar.invoke({
                    "inputFields": {
                        "empId": data.entityDetails.entityOwnerId
                    },
                },
                function(data1) {
                    Partial.Variables.entityOwnerNameVar.dataSet.dataValue = data1;

                    debugger;
                    var getTeamIdUsingEmpIdVar = Partial.Variables.getTeamIdUsingEmpId;

                    getTeamIdUsingEmpIdVar.invoke({
                            "inputFields": {
                                "empId": data.entityDetails.entityOwnerId
                            },
                        },
                        function(data2) {
                            debugger;
                            Partial.Variables.entityTeamVar.dataSet.dataValue = data2;
                        },
                        function(error2) {
                            // Error Callback
                            console.log("error", error);
                        }
                    );

                },
                function(error1) {
                    // Error Callback
                    console.log("error", error);
                }
            );


        },
        function(error) {
            // Error Callback
            console.log("error", error);
        }
    );

};