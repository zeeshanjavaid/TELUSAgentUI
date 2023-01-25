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

    if (!Partial.pageParams.queueId) {
        Partial.Variables.sortConfig.dataSet = [];
    }

    Partial.isDuplicateQueue = false;
    initPage();
};

function initPage() {
    const intervalId = setInterval(function() {
        if (App.queueFiltersLoaded) {
            clearInterval(intervalId);
            console.log('Queue filters loaded...');

            Partial.Variables.queueData.dataSet = [];
            Partial.Variables.leftGroupsList.dataSet = [];
            Partial.Variables.selectedGroups.dataSet = [];

            //Partial.Widgets.queueConditions.configuration = [];
            //Partial.Variables.queueErrorMessage.dataSet.dataValue = "";
            //Partial.Variables.queueSuccessMessage.dataSet.dataValue = "";
            if (Partial.App.activePage.Variables.partialQueueMsg.dataSet.dataValue !== "") {
                Partial.Variables.queueSuccessMessage.dataSet.dataValue = Partial.App.activePage.Variables.partialQueueMsg.dataSet.dataValue
                Partial.App.activePage.Variables.partialQueueMsg.dataSet.dataValue = "";
            }

            if (Partial.pageParams.queueId) {
                let QgetEvent = new CustomEvent("queue_ev", {
                    "detail": {
                        "operation": "GET"
                    }
                });
                document.querySelector("div[name='AddQueueContainer']").dispatchEvent(QgetEvent);

                Partial.Variables.readMode.dataSet.dataValue = true;
                Partial.Variables.queueMode.dataSet.dataValue = "View Queue";
            } else {

                App.Variables.queueConfigMV.setData({
                    'conditions': {
                        'fields': App.Variables.queueFieldsMV.dataSet,
                        'rules': null
                    },
                    'displayConfig': {
                        'fielddataset': [],
                        "savedconfig": {
                            "searchableFields": [],
                            "sortableFields": []
                        }
                    }
                });

                Partial.Variables.queueMode.dataSet.dataValue = "Add Queue";
                Partial.Variables.readMode.dataSet.dataValue = false;
                Partial.Variables.queueData.dataSet = {
                    "isActive": true
                };
            }

            Partial.Variables.getQueue.invoke();

        } else {
            //determining the time elapsed since App started in minutes
            const timeElapsedSinceAppStart = moment(new Date()).diff(moment(App.appStartTime), 'minutes');

            if (timeElapsedSinceAppStart < 1)
                console.log('Waiting to load Queue filter configuration...');
            else {
                clearInterval(intervalId);

                //if the active page is not 'ErrorLanding'
                if (window.location.hash !== '#/ErrorLanding')
                    window.location.href = '#/ErrorLanding';
            }
        }
    }, 10);
}

Partial.SaveButtonClick = function($event, widget) {
    Partial.isDeleteGroup = false;
    Partial.Widgets.NameTextarea.required = false;
    Partial.Variables.queueErrorMessage.dataSet.dataValue = "";
    Partial.Variables.queueSuccessMessage.dataSet.dataValue = "";
    Partial.App.activePage.Variables.partialQueueMsg.dataSet.dataValue = "";
    var conditions = Partial.Widgets.queueConditions.getRules();
    let pattern = Partial.Widgets.NameTextarea.regexp;
    //console.log(conditions);

    if ((Partial.Widgets.NameTextarea.datavalue == undefined ? Partial.Widgets.NameTextarea.datavalue == undefined : Partial.Widgets.NameTextarea.datavalue == "")) {
        Partial.Variables.queueErrorMessage.dataSet.dataValue = "Queue name is mandatory.";
        Partial.Widgets.NameTextarea.required = true;
        Partial.scrollToTop();
        return false;
    }

    if (Partial.Widgets.NameTextarea.datavalue.match(pattern) == null) {
        Partial.Variables.queueErrorMessage.dataSet.dataValue = "Queue name is invalid.";
        Partial.scrollToTop();
        return false;
    }

    if ((Partial.Widgets.rank.datavalue < 0 || Partial.Widgets.rank.datavalue > 99999) || (Partial.Widgets.rank.datavalue === null || Partial.Widgets.rank.datavalue === 0)) {
        Partial.Variables.queueErrorMessage.dataSet.dataValue = "Queue rank is not a valid number.";
        Partial.scrollToTop();
        return false;
    }

    if (Partial.Widgets.DualListGroups_TD.rightdataset.length === 0) {
        Partial.Variables.queueErrorMessage.dataSet.dataValue = "Group assignment is mandatory.";
        Partial.scrollToTop();
        return false;
    }

    if (Partial.Widgets.rank.datavalue > 0 && Partial.Widgets.rank.datavalue <= 99999) {
        var duplicate = false;
        if (Partial.App.activePage.Widgets.Queuecontainer.Variables.allQueues.dataSet && Partial.App.activePage.Widgets.Queuecontainer.Variables.allQueues.dataSet.length > 0) {
            Partial.App.activePage.Widgets.Queuecontainer.Variables.allQueues.dataSet.forEach(function(queueObj) {
                if (queueObj.rank === Partial.Widgets.rank.datavalue && queueObj.id !== Partial.pageParams.queueId) {
                    Partial.Variables.queueErrorMessage.dataSet.dataValue = "Queue rank must be unique.";
                    duplicate = true;
                }
            });
            if (duplicate) {
                Partial.scrollToTop();
                return false;
            }
        }
    }

    if (!Partial.Widgets.radiosetQExecutionMode.datavalue) {
        Partial.Variables.queueErrorMessage.dataSet.dataValue = "Queue execution mode is mandatory.";
        Partial.scrollToTop();
        return false;
    }

    if (!Partial.Widgets.selectQResultPage.datavalue) {
        Partial.Variables.queueErrorMessage.dataSet.dataValue = "Queue search result page is mandatory.";
        Partial.scrollToTop();
        return false;
    }

    if (!Partial.Variables.sortConfig.dataSet || Partial.Variables.sortConfig.dataSet.length === 0) {
        Partial.Variables.queueErrorMessage.dataSet.dataValue = "Queue sort criteria is mandatory.";
        Partial.scrollToTop();
        return false;
    }

    if (Partial.Variables.sortConfig.dataSet.length > 0) {
        let sortValid = true;
        Partial.Variables.sortConfig.dataSet.forEach((sortCfg) => {
            if (!sortCfg.fieldId || !sortCfg.direction) {
                Partial.Variables.queueErrorMessage.dataSet.dataValue = "Queue sort field & sort order are mandatory.";
                sortValid = false;
            }
        });

        if (!sortValid) {
            Partial.scrollToTop();
            return false;
        }
    }

    if (Partial.Variables.sortConfig.dataSet.length > 0) {
        const sortCfgMap = Partial.Variables.sortConfig.dataSet.map(sortCfg => sortCfg.fieldId);
        if (new Set(sortCfgMap).size !== sortCfgMap.length) {
            Partial.Variables.queueErrorMessage.dataSet.dataValue = "Queue sort criteria contains duplicate sort fields.";
            Partial.scrollToTop();
            return false;
        }
    }

    if (Partial.Widgets.checkboxPersonalQ.datavalue) {
        if (!Partial.Widgets.selectPersonalQ.datavalue) {
            Partial.Variables.queueErrorMessage.dataSet.dataValue = "Personal queue filter field is mandatory when Personal queue is enabled.";
            Partial.scrollToTop();
            return false;
        }
    }

    if (conditions) {
        if ((conditions.filterCriteria.length === 0 && conditions.filterGroup.length === 0)) {
            Partial.Variables.queueErrorMessage.dataSet.dataValue = "Queue filter criteria is mandatory.";
            Partial.scrollToTop();
            return false;
        } else {

            let id = null,
                createdBy = null,
                createdOn = null,
                isLocked = null,
                lockedBy = null;

            if (Partial.pageParams.queueId) {
                id = Partial.pageParams.queueId;
                createdBy = Partial.Variables.queueData.dataSet.createdBy;
                createdOn = Partial.Variables.queueData.dataSet.createdOn;
                isLocked = Partial.Variables.queueData.dataSet.isLocked;
                lockedBy = Partial.Variables.queueData.dataSet.lockedBy;
            } else {
                createdBy = App.Variables.getLoggedInUserId.dataSet[0].id;
                createdOn = new Date();
            }

            Partial.Variables.sortConfig.dataSet.forEach((sortCfg) => {
                delete sortCfg.$index;
            });
            var queue = {
                'id': id,
                'category': Partial.Widgets.radiosetQExecutionMode.displayValue == "Priority" ? 1 : 0,
                'rank': Partial.Widgets.rank.datavalue,
                'name': Partial.Widgets.NameTextarea.datavalue,
                'description': Partial.Widgets.textarea1.datavalue,
                'createdBy': createdBy,
                'createdOn': createdOn,
                'updatedBy': App.Variables.getLoggedInUserId.dataSet[0].id,
                'updatedOn': new Date(),
                'jsonFilterString': JSON.stringify(conditions),
                'sortField': JSON.stringify(Partial.Variables.sortConfig.dataSet),
                // 'sortOrder': Partial.Widgets.sortOrder.datavalue,
                'isActive': Partial.Widgets.active.datavalue,
                "isLocked": isLocked,
                "lockedBy": lockedBy,
                'queueResultPage': Partial.Widgets.selectQResultPage.datavalue,
                'isPersonalQueue': Partial.Widgets.checkboxPersonalQ.datavalue,
                'personalQueueField': Partial.Widgets.selectPersonalQ.datavalue
            };

            var queueDate = {
                'QueueRank': Partial.Widgets.rank.datavalue,
                'QueueName': Partial.Widgets.NameTextarea.datavalue,
                'QueueDescription': Partial.Widgets.textarea1.datavalue,
                /*                'QueueAssignGroup': "Group4,Group2",
                 */
                'QueueFilterCriteria': JSON.stringify(conditions),
                'isActive': Partial.Widgets.active.datavalue,
                'sortOrder': Partial.Widgets.sortField.datavalue,
                'sortOrder': Partial.Widgets.sortOrder.datavalue,
                'createdBy': createdBy,
                'createdOn': createdOn
                // "userByLockedBy": userByLockedBy
            };

            //save the Queue information
            saveQueueInformation(queue);
        }
    }

};

async function saveQueueInformation(queue) {
    if (!Partial.pageParams.queueId || Partial.pageParams.queueId === 0) {
        await Partial.Variables.checkQueueWithNameExists.invoke();
    }

    //check if QUEUE with same name exists -> only for a new QUEUE save
    if ((!Partial.pageParams.queueId || Partial.pageParams.queueId === 0) && Partial.Variables.checkQueueWithNameExists.dataSet.length > 0) {
        Partial.Variables.queueErrorMessage.dataSet.dataValue = "Queue with same name already exists.";
        Partial.scrollToTop();
        return false;
    } else {
        let groupIds = [];
        Partial.Widgets.DualListGroups_TD.rightdataset.forEach((selectedGroup) => {
            groupIds.push(selectedGroup.id);
        })

        Partial.Variables.createQueue.invoke({
            "inputFields": {
                "Queue": queue,
                "listGroups": groupIds
            }
        }, function(data) {
            /* if (Partial.pageParams.queueId > 0) {
                 Partial.Variables.queueSuccessMessage.dataSet.dataValue = "Queue updated successfully";
                 Partial.App.activePage.Variables.partialQueueMsg.dataSet.dataValue = "Queue updated successfully";
             } else {
                 Partial.Variables.queueSuccessMessage.dataSet.dataValue = "Queue created successfully";
                 Partial.App.activePage.Variables.partialQueueMsg.dataSet.dataValue = "Queue updated successfully";
             }*/
            //App.refreshAllQueues();
            Partial.App.activePage.Widgets.Queuecontainer.Variables.allQueues.invoke();
            //Partial.Variables.getQueue.invoke();
            if (Partial.pageParams.queueId) {
                let QaddEvent = new CustomEvent("queue_ev", {
                    "detail": {
                        "operation": "ADD"
                    }
                });
                document.querySelector("div[name='AddQueueContainer']").dispatchEvent(QaddEvent);

                Partial.Variables.queueSuccessMessage.dataSet.dataValue = "Queue updated successfully";
                Partial.scrollToTop();
                //App.refreshAllQueues();
                // Partial.App.activePage.Variables.partialQueueMsg.dataSet.dataValue = "Queue updated successfully";

            } else {
                // Changing navbar visibility to hidden
                $('.app-top-nav')[0].style.visibility = 'hidden';

                let QaddEvent = new CustomEvent("queue_ev", {
                    "detail": {
                        "operation": "ADD"
                    }
                });
                document.querySelector("div[name='AddQueueContainer']").dispatchEvent(QaddEvent);

                Partial.Variables.queueSuccessMessage.dataSet.dataValue = "Queue created successfully";
                Partial.scrollToTop();
                // Partial.App.activePage.Variables.partialQueueMsg.dataSet.dataValue = "Queue updated successfully";
            }
            /*Partial.Widgets.DualListGroups_TD.rightdataset.forEach(function(selectedPermission) {
                Partial.Variables.CreateQueueGroup.setInput({
                    'queueId': Partial.pageParams.queueId,
                    'groupId': selectedPermission.id,
                });
                Partial.Variables.CreateQueueGroup.invoke();
            })*/
        }, function(error) {
            // Error Callback 
            console.log("error", error)
            Partial.Variables.queueErrorMessage.dataSet.dataValue = error;
        });
    }

};

Partial.CancelbuttonClick = function($event, widget) {

    Partial.App.activePage.Variables.partialQueueMsg.dataSet.dataValue = "";
    Partial.Variables.queueErrorMessage.dataSet.dataValue = "";
    Partial.Variables.queueSuccessMessage.dataSet.dataValue = "";

    if (Partial.pageParams.queueId) {
        if (Partial.Widgets.DualListGroups_TD !== undefined) {
            Partial.Widgets.DualListGroups_TD.rightdataset = Partial.Variables.selectedGroups.dataSet;
            Partial.Widgets.DualListGroups_TD.leftdataset = Partial.Variables.leftGroupsList.dataSet;
        }

        Partial.Variables.sortConfig.dataSet = [];
        Object.assign(Partial.Variables.sortConfig.dataSet, Partial.Variables.queueData.dataSet.sortField);
        Partial.queryChanges(Partial.Variables.queueData.dataSet.jsonFilterString);
        App.Variables.queueConfigMV.setData({
            'conditions': {
                'fields': App.Variables.queueFieldsMV.dataSet,
                'rules': App.Variables.queueConfigMV.dataSet.conditions.rules
            },
            'displayConfig': {
                'fielddataset': [],
                "savedconfig": {
                    "searchableFields": [],
                    "sortableFields": []
                }
            }
        });

    } else {
        Partial.Variables.sortConfig.dataSet = [];
        Partial.Variables.queueData.dataSet = [];

        if (Partial.Widgets.DualListGroups_TD !== undefined) {
            Partial.Widgets.DualListGroups_TD.rightdataset = [];
            Partial.Widgets.DualListGroups_TD.leftdataset = Partial.Variables.leftGroupsList.dataSet;
        }

        App.Variables.queueConfigMV.setData({
            'conditions': {
                'fields': App.Variables.queueFieldsMV.dataSet,
                'rules': null
            },
            'displayConfig': {
                'fielddataset': [],
                "savedconfig": {
                    "searchableFields": [],
                    "sortableFields": []
                }
            }
        });
    }

    Partial.Widgets.queueConditions.configuration = App.Variables.queueConfigMV.dataSet.conditions;
};


Partial.DuplicatebuttonClick = function($event, widget) {

    Partial.Widgets.NameTextarea.required = false;
    Partial.Variables.queueErrorMessage.dataSet.dataValue = "";
    Partial.Variables.queueSuccessMessage.dataSet.dataValue = "";
    Partial.App.activePage.Variables.partialQueueMsg.dataSet.dataValue = "";
    var conditions = Partial.Widgets.queueConditions.getRules();
    let pattern = Partial.Widgets.NameTextarea.regexp;

    // Enable Name and Rank field
    Partial.isDuplicateQueue = true;

    Partial.scrollToTop();


    if ((Partial.Widgets.NameTextarea.datavalue == undefined ? Partial.Widgets.NameTextarea.datavalue == undefined : Partial.Widgets.NameTextarea.datavalue == "")) {
        Partial.Variables.queueErrorMessage.dataSet.dataValue = "Queue name is mandatory.";
        Partial.Widgets.NameTextarea.required = true;
        return false;
    }

    if (Partial.Widgets.NameTextarea.datavalue.match(pattern) == null) {
        Partial.Variables.queueErrorMessage.dataSet.dataValue = "Queue name is invalid.";
        return false;
    }

    if ((Partial.Widgets.rank.datavalue < 0 || Partial.Widgets.rank.datavalue > 99999) || (Partial.Widgets.rank.datavalue === null || Partial.Widgets.rank.datavalue === 0)) {
        Partial.Variables.queueErrorMessage.dataSet.dataValue = "Queue rank is not a valid number.";
        return false;
    }

    if (Partial.Widgets.DualListGroups_TD.rightdataset.length === 0) {
        Partial.Variables.queueErrorMessage.dataSet.dataValue = "Group assignment is mandatory.";
        return false;
    }

    // Queue Name and Rank Duplicate Check
    if (Partial.Widgets.NameTextarea.datavalue != "") {
        var duplicate = false;
        if (Partial.App.activePage.Widgets.Queuecontainer.Variables.allQueues.dataSet && Partial.App.activePage.Widgets.Queuecontainer.Variables.allQueues.dataSet.length > 0) {
            Partial.App.activePage.Widgets.Queuecontainer.Variables.allQueues.dataSet.forEach(function(queueObj) {
                if (queueObj.name === Partial.Widgets.NameTextarea.datavalue) {
                    Partial.Variables.queueErrorMessage.dataSet.dataValue = "Queue with same name already exists.";
                    duplicate = true;
                }
            });
            if (duplicate) {
                return false;
            }
        }
    }

    if (Partial.Widgets.rank.datavalue > 0 && Partial.Widgets.rank.datavalue <= 99999) {
        var duplicate = false;
        if (Partial.App.activePage.Widgets.Queuecontainer.Variables.allQueues.dataSet && Partial.App.activePage.Widgets.Queuecontainer.Variables.allQueues.dataSet.length > 0) {
            Partial.App.activePage.Widgets.Queuecontainer.Variables.allQueues.dataSet.forEach(function(queueObj) {
                if (queueObj.rank === Partial.Widgets.rank.datavalue) {
                    Partial.Variables.queueErrorMessage.dataSet.dataValue = "Queue rank must be unique.";
                    duplicate = true;
                }
            });
            if (duplicate) {
                return false;
            }
        }
    }
    //

    if (!Partial.Widgets.radiosetQExecutionMode.datavalue) {
        Partial.Variables.queueErrorMessage.dataSet.dataValue = "Queue execution mode is mandatory.";
        return false;
    }

    if (!Partial.Widgets.selectQResultPage.datavalue) {
        Partial.Variables.queueErrorMessage.dataSet.dataValue = "Queue search result page is mandatory.";
        return false;
    }

    if (!Partial.Variables.sortConfig.dataSet || Partial.Variables.sortConfig.dataSet.length === 0) {
        Partial.Variables.queueErrorMessage.dataSet.dataValue = "Queue sort criteria is mandatory.";
        Partial.scrollToTop();
        return false;
    }

    if (Partial.Variables.sortConfig.dataSet.length > 0) {
        let sortValid = true;
        Partial.Variables.sortConfig.dataSet.forEach((sortCfg) => {
            if (!sortCfg.fieldId || !sortCfg.direction) {
                Partial.Variables.queueErrorMessage.dataSet.dataValue = "Queue sort field & sort order are mandatory.";
                sortValid = false;
            }
        });

        if (!sortValid) {
            Partial.scrollToTop();
            return false;
        }
    }

    if (Partial.Variables.sortConfig.dataSet.length > 0) {
        const sortCfgMap = Partial.Variables.sortConfig.dataSet.map(sortCfg => sortCfg.fieldId);
        if (new Set(sortCfgMap).size !== sortCfgMap.length) {
            Partial.Variables.queueErrorMessage.dataSet.dataValue = "Queue sort criteria contains duplicate sort fields.";
            Partial.scrollToTop();
            return false;
        }
    }

    if (Partial.Widgets.checkboxPersonalQ.datavalue) {
        if (!Partial.Widgets.selectPersonalQ.datavalue) {
            Partial.Variables.queueErrorMessage.dataSet.dataValue = "Personal queue filter field is mandatory when Personal queue is enabled.";
            return false;
        }
    }

    if (conditions) {
        if ((conditions.filterCriteria.length === 0 && conditions.filterGroup.length === 0)) {
            Partial.Variables.queueErrorMessage.dataSet.dataValue = "Queue filter criteria is mandatory.";
            return false;
        } else {
            let id = null,
                createdBy = null,
                createdOn = null,
                isLocked = null,
                lockedBy = null;


            createdBy = App.Variables.getLoggedInUserId.dataSet[0].id;
            createdOn = new Date();

            var queue = {
                'id': id,
                'category': Partial.Widgets.radiosetQExecutionMode.displayValue == "Priority" ? 1 : 0,
                'rank': Partial.Widgets.rank.datavalue,
                'name': Partial.Widgets.NameTextarea.datavalue,
                'description': Partial.Widgets.textarea1.datavalue,
                'createdBy': createdBy,
                'createdOn': createdOn,
                'updatedBy': App.Variables.getLoggedInUserId.dataSet[0].id,
                'updatedOn': new Date(),
                'jsonFilterString': JSON.stringify(conditions),
                'sortField': JSON.stringify(Partial.Variables.sortConfig.dataSet),
                // 'sortOrder': Partial.Widgets.sortOrder.datavalue,
                'isActive': Partial.Widgets.active.datavalue,
                "isLocked": isLocked,
                "lockedBy": lockedBy,
                'queueResultPage': Partial.Widgets.selectQResultPage.datavalue,
                'isPersonalQueue': Partial.Widgets.checkboxPersonalQ.datavalue,
                'personalQueueField': Partial.Widgets.selectPersonalQ.datavalue
            };

            var queueDate = {
                'QueueRank': Partial.Widgets.rank.datavalue,
                'QueueName': Partial.Widgets.NameTextarea.datavalue,
                'QueueDescription': Partial.Widgets.textarea1.datavalue,
                /*                'QueueAssignGroup': "Group4,Group2",
                 */
                'QueueFilterCriteria': JSON.stringify(conditions),
                'isActive': Partial.Widgets.active.datavalue,
                'sortOrder': Partial.Widgets.sortField.datavalue,
                'sortOrder': Partial.Widgets.sortOrder.datavalue,
                'createdBy': createdBy,
                'createdOn': createdOn
                // "userByLockedBy": userByLockedBy
            };

            let groupIds = [];
            Partial.Widgets.DualListGroups_TD.rightdataset.forEach((selectedGroup) => {
                groupIds.push(selectedGroup.id);
            })

            Partial.Variables.createQueue.invoke({
                "inputFields": {
                    "Queue": queue,
                    "listGroups": groupIds
                }
            }, function(data) {
                Partial.App.activePage.Widgets.Queuecontainer.Variables.allQueues.invoke();

                let QaddEvent = new CustomEvent("queue_ev", {
                    "detail": {
                        "operation": "ADD"
                    }
                });
                document.querySelector("div[name='AddQueueContainer']").dispatchEvent(QaddEvent);

                Partial.Variables.queueSuccessMessage.dataSet.dataValue = "Queue created successfully";

            }, function(error) {
                // Error Callback 
                console.log("error", error)
                Partial.Variables.queueErrorMessage.dataSet.dataValue = error;
            });

        }
    }

}

/*Partial.DeletebuttonClick = function($event, widget) {
    Partial.Variables.queueErrorMessage.dataSet.dataValue = "";
    Partial.Variables.queueSuccessMessage.dataSet.dataValue = "";
    Partial.Variables.queueData.dataSet = [];
    App.Variables.queueConfigMV.setData({
        'conditions': {
            'fields': App.Variables.queueFieldsMV.dataSet,
            'rules': [] //App.Variables.queueRulesMV.dataSet.filter
        },
        'displayConfig': {
            'fielddataset': [],
            "savedconfig": {
                "searchableFields": [],
                "sortableFields": []
            }
        }
    });
    if (Partial.Widgets.queueConditions.getRules() != undefined) {
        Partial.Widgets.queueConditions.getRules().filterCriteria = [];
    }
    if (Partial.Widgets.queueConditions !== undefined) {
        Partial.Widgets.queueConditions.configuration = Partial.Variables.queueConfigMV.dataSet.conditions;
    }

    Partial.Variables.queueSuccessMessage.dataSet.dataValue = "Queue deleted successfully.";

};*/

Partial.adjustSortData = function(sortInformation, sortDirection) {
    if (sortInformation) {
        try {
            let sortInformationCP = sortInformation.replaceAll("&quot;", '"');
            sortInformationCP = JSON.parse(sortInformationCP);
            sortInformationCP.forEach((sortInf, index) => {
                sortInf.$index = index;
            });
            return sortInformationCP;
        } catch (err) {
            let sortmeta = [];
            sortmeta.push({
                'fieldId': sortInformation,
                'direction': sortDirection,
                'rankOrder': 1
            });

            sortmeta.forEach((sortInf, index) => {
                sortInf.$index = index;
            });
            return sortmeta;
        }
    }
};

Partial.getQueueonSuccess = function(variable, data) {
    Partial.Variables.sortConfig.dataSet = [];
    if (!Partial.pageParams.queueId)
        data = [];
    debugger;
    Partial.Variables.selectedGroups.dataSet = [];
    Partial.Variables.leftGroupsList.dataSet = [];

    if (data.length > 0) {
        let userByLockedBy = "";
        if (data[0].userByLockedBy) {
            userByLockedBy = (data[0].userByLockedBy.firstName + " " + data[0].userByLockedBy.lastName);
        }
        Partial.Variables.queueData.dataSet = {
            "id": data[0].id,
            "category": data[0].category == 1 ? "Priority" : "Manual",
            "QueueName": data[0].name,
            "QueueDescription": data[0].description,
            "QueueRank": data[0].rank,
            "jsonFilterString": data[0].jsonFilterString,
            "isActive": data[0].isActive,
            "sortField": Partial.adjustSortData(data[0].sortField, data[0].sortOrder),
            // "sortOrder": data[0].sortOrder,
            "isLocked": data[0].isLocked,
            "lockedBy": data[0].lockedBy,
            "userByLockedBy": userByLockedBy,
            "createdBy": data[0].createdBy,
            "createdOn": data[0].createdOn,
            'queueResultPage': data[0].queueResultPage,
            'isPersonalQueue': data[0].isPersonalQueue,
            'personalQueueField': data[0].personalQueueField
        }

        if (data[0].lockedBy === App.Variables.getLoggedInUserId.dataSet[0].id) {
            Partial.Variables.queueMode.dataSet.dataValue = "Edit Queue";
            Partial.Variables.readMode.dataSet.dataValue = false;
        } else {
            Partial.Variables.queueMode.dataSet.dataValue = "View Queue";
            Partial.Variables.readMode.dataSet.dataValue = true;
        }

        Partial.Variables.sortConfig.dataSet = [];
        Object.assign(Partial.Variables.sortConfig.dataSet, Partial.Variables.queueData.dataSet.sortField);
        Partial.queryChanges(data[0].jsonFilterString);
        App.Variables.queueConfigMV.setData({
            'conditions': {
                'fields': App.Variables.queueFieldsMV.dataSet,
                'rules': App.Variables.queueConfigMV.dataSet.conditions.rules
            },
            'displayConfig': {
                'fielddataset': [],
                "savedconfig": {
                    "searchableFields": [],
                    "sortableFields": []
                }
            }
        });

        /*if (Partial.Variables.queueMode.dataSet.dataValue === "View Queue") {
            document.getElementsByClassName("btn app-button btn-default btn-listctrl").addbtn.disabled = true;
            document.getElementsByClassName("btn app-button btn-default btn-listctrl").addallbtn.disabled = true;
            document.getElementsByClassName("btn app-button btn-default btn-listctrl").buttonRemoveAll.disabled = true;
            document.getElementsByClassName("btn app-button btn-default btn-listctrl").removebtn.disabled = true;
        }*/

    }

    Partial.Variables.getQueueGroups.invoke();
};

App.addQueue = function() {
    Partial.App.activePage.Variables.partialQueueMsg.dataSet.dataValue = "";
    Partial.Variables.queueMode.dataSet.dataValue = "Add Queue";
    Partial.Variables.readMode.dataSet.dataValue = false;
    Partial.Variables.errorMessage.dataSet.dataValue = "";
    Partial.Variables.successMessage.dataSet.dataValue = "";
    Partial.Variables.queueErrorMessage.dataSet.dataValue = "";
    Partial.Variables.queueSuccessMessage.dataSet.dataValue = "";
    Partial.Variables.queueData.dataSet = [];
    Partial.Variables.selectedGroups.dataSet = [];
    Partial.Variables.leftGroupsList.dataSet = [];
    Partial.Variables.sortConfig.dataSet = [];
    App.Variables.queueConfigMV.setData({
        'conditions': {
            'fields': App.Variables.queueFieldsMV.dataSet,
            "rules": null
        },
        'displayConfig': {
            'fielddataset': [],
            "savedconfig": {
                "searchableFields": [],
                "sortableFields": []
            }
        }
    });

    // Setting default values in queue-execution mode and queue-search result page
    var defalutQueueResultPageId = 0;
    if (Partial.Variables.dvs_qSearchResultPage.dataSet != null && Partial.Variables.dvs_qSearchResultPage.dataSet.length > 0) {
        Partial.Variables.dvs_qSearchResultPage.dataSet.forEach(function(data) {
            var item = JSON.parse(JSON.stringify(data));
            if (item.code == "DEFAULT") {
                defalutQueueResultPageId = item.id;
            }
        });
    }
    Partial.Variables.queueData.dataSet = {
        "isActive": true,
        "category": "Manual",
        "queueResultPage": defalutQueueResultPageId
    };

    // if (Partial.Widgets.queueConditions.getRules() != undefined) {
    //     Partial.Widgets.queueConditions.getRules().filterCriteria = [];
    // }
    // if (Partial.Widgets.queueConditions !== undefined) {
    //     Partial.Widgets.queueConditions.configuration = Partial.Variables.queueConfigMV.dataSet.conditions;
    // }

    if (Partial.Widgets.DualListGroups_TD !== undefined) {
        Partial.Widgets.DualListGroups_TD.rightdataset = [];
        Partial.Widgets.DualListGroups_TD.leftdataset = [];
        // Partial.Widgets.DualListGroups_TD.new = true;
    }

    Partial.pageParams.queueId = undefined;
    Partial.Variables.getQueue.invoke();
};

Partial.deleteQueueConfirmOkClick = function($event, widget) {
    Partial.isDelete = true;
    Partial.Widgets.deleteQueueDialog.close();
    Partial.Variables.executeDeleteQueueGroup.setInput({
        'QueueId': Partial.pageParams.queueId,
        'GroupId': 0
    });
    Partial.Variables.executeDeleteQueueGroup.invoke();

};

Partial.executeDeleteQueueGrouponSuccess = function(variable, data) {
    Partial.Variables.errorMessage.dataSet.dataValue = "";
    Partial.Variables.successMessage.dataSet.dataValue = "";
    Partial.App.activePage.Variables.partialQueueMsg.dataSet.dataValue = ""

    if (Partial.isDelete !== true) {
        /*  Partial.Widgets.DualListGroups_TD.rightdataset.forEach(function(selectedPermission) {
              Partial.Variables.CreateRolePermission.setInput({
                  'roleId': Partial.pageParams.roleId,
                  'permissionId': selectedPermission.id,
              });
              Partial.Variables.CreateRolePermission.invoke();
          })
          Partial.Variables.successMessage.dataSet.dataValue = Partial.appLocale.ROLE_UPDATED_SUCCESSFULLY;*/
    } else {

        /* Partial.Variables.deleteQueue.setInput({
             'id': Partial.pageParams.queueId
         });*/
        Partial.Variables.deleteQueue.invoke({
            "inputFields": {
                "id": Partial.pageParams.queueId
            }
        }, function(data) {
            //App.refreshAllQueues();
            Partial.App.activePage.Widgets.Queuecontainer.Variables.allQueues.invoke();

            let QaddEvent = new CustomEvent("queue_ev", {
                "detail": {
                    "operation": "ADD"
                }
            });
            document.querySelector("div[name='AddQueueContainer']").dispatchEvent(QaddEvent);

            Partial.App.activePage.Variables.partialQueueMsg.dataSet.dataValue = "Queue deleted successfully";
        }, function(error) {
            // Error Callback 
            console.log("error", error)
            Partial.Variables.queueErrorMessage.dataSet.dataValue = error;
        });

    }


};

Partial.EditbuttonClick = function($event, widget) {
    Partial.Variables.queueErrorMessage.dataSet.dataValue = "";
    Partial.Variables.queueSuccessMessage.dataSet.dataValue = "";
    Partial.App.activePage.Variables.partialQueueMsg.dataSet.dataValue = ""
    App.Variables.QueuePageCommunication.currentQueueInFocusId = Partial.pageParams.queueId;
    console.log("AddQueuePartial : QueuePageCommunication - current queue Id:" + App.Variables.QueuePageCommunication.currentQueueInFocusId);
    Partial.Variables.getQueueLock.invoke({},
        function(data) {
            if (data.length === 0 || data.length === undefined) {
                Partial.Variables.queueLock.invoke({
                    "inputFields": {
                        "updatedOn": new Date(),
                        "lockedBy": App.Variables.getLoggedInUserId.dataSet[0].id,
                        "id": Partial.pageParams.queueId,
                        "updatedBy": App.Variables.getLoggedInUserId.dataSet[0].id
                    }
                }, function(data) {
                    Partial.Variables.getQueue.invoke();
                }, function(error) {
                    // Error Callback 
                    Partial.Variables.msg = error;
                    console.log("error queueLock", error)
                    Partial.Variables.queueErrorMessage.dataSet.dataValue = error;
                });
            }
        },
        function(error) {
            Partial.Variables.errorMessage.dataSet.dataValue = error;
            console.log("error getQueueLock", error)
        });



    //lock
};

Partial.unlockButtonClick = function($event, widget) {
    Partial.Variables.queueErrorMessage.dataSet.dataValue = "";
    Partial.Variables.queueSuccessMessage.dataSet.dataValue = "";
    Partial.App.activePage.Variables.partialQueueMsg.dataSet.dataValue = "";
    App.Variables.QueuePageCommunication.currentQueueInFocusId = Partial.pageParams.queueId;
    Partial.Variables.getQueueLock.invoke({},
        function(data) {
            if (data.length > 0) {
                Partial.Variables.queueUnLock.invoke({
                        "inputFields": {
                            "id": Partial.pageParams.queueId,
                            "updatedBy": App.Variables.getLoggedInUserId.dataSet[0].id,
                            "updatedOn": new Date()
                        }
                    },
                    function(data) {
                        /* Partial.Variables.queueMode.dataSet.dataValue = "View Queue";
                         Partial.Variables.readMode.dataSet.dataValue = false;*/
                        Partial.Variables.getQueue.invoke();
                        //App.refreshAllQueues();
                    },
                    function(error) {
                        Partial.Variables.errorMessage.dataSet.dataValue = error;
                        console.log("error", error)
                    });
            }
        },
        function(error) {
            Partial.Variables.errorMessage.dataSet.dataValue = error;
            console.log("error", error)
        });
};

Partial.ValidationbuttonClick = function($event, widget) {
    Partial.Variables.queueErrorMessage.dataSet.dataValue = "";
    Partial.Variables.queueSuccessMessage.dataSet.dataValue = "";
    Partial.App.activePage.Variables.partialQueueMsg.dataSet.dataValue = ""

    var conditions = Partial.Widgets.queueConditions.getRules();
    if (conditions === undefined) {
        Partial.Variables.queueErrorMessage.dataSet.dataValue = "Queue filter criteria is mandatory.";
        Partial.scrollToTop();
        return false;
    } else if (conditions && (conditions.filterCriteria.length === 0 && conditions.filterGroup.length === 0)) {
        Partial.Variables.queueErrorMessage.dataSet.dataValue = "Queue filter criteria is mandatory.";
        Partial.scrollToTop();
        return false;
    } else {
        var queue = {
            'id': 0,
            'rank': "",
            'name': "",
            'description': "",
            'createdBy': "",
            'createdOn': "",
            'updatedBy': "",
            'updatedOn': "",
            'jsonFilterString': JSON.stringify(conditions),
            'sortField': JSON.stringify(Partial.Variables.sortConfig.dataSet),
            // 'sortOrder': Partial.Widgets.sortOrder.datavalue,
            'isActive': "",
            'queueResultPage': Partial.Widgets.selectQResultPage.datavalue,
            'isPersonalQueue': Partial.Widgets.checkboxPersonalQ.datavalue,
            'personalQueueField': Partial.Widgets.selectPersonalQ.datavalue
        };
        Partial.Variables.validateQueue.invoke({
            "inputFields": {
                "Queue": queue,
                "userLocale": App.getCurrentLocale(),
                "dateFilterFields": App.getDateTypeFilterFields()
            }
        }, function(data) {
            Partial.Variables.queueSuccessMessage.dataSet.dataValue = "Queue filter criteria is valid, available record count [" + data + "] ";
            Partial.scrollToTop();
        }, function(error) {
            console.log("error", error)
            Partial.Variables.queueErrorMessage.dataSet.dataValue = error;
            Partial.scrollToTop();
        });

    }
};

Partial.queryChanges = function(sql) {
    var query = sql.replaceAll("&quot;", '"');
    App.Variables.queueConfigMV.setData({
        'conditions': {
            'fields': App.Variables.queueFieldsMV.dataSet,
            'rules': JSON.parse(query)
        }
    });
};

Partial.checkboxPersonalQChange = function($event, widget) {

    // if checkbox disabled then remove the dropdown value
    if (!Partial.Widgets.checkboxPersonalQ.datavalue) {
        Partial.Widgets.selectPersonalQ.datavalue = null;
    }
};

Partial.getQueueGroupsonSuccess = function(variable, data) {
    Partial.QueueGroups = [];
    if (!Partial.pageParams.queueId) {
        data = [];
        Partial.QueueGroups = [];
    }

    data.forEach((d) => {
        Partial.QueueGroups.push(d._group);
    });

    Partial.QueueGroups.forEach((sp) => {
        sp.groupName = App.htmlEncode(sp.name);
        Partial.Variables.selectedGroups.dataSet.push(sp);
    });

    if (Partial.Widgets.DualListGroups_TD !== undefined) {
        Partial.Widgets.DualListGroups_TD.rightdataset = Partial.Variables.selectedGroups.dataSet;
    }

    Partial.Variables.getAllGroups.dataSet.forEach((availableleftGroup) => {
        let count = 0;
        data.forEach(function(i) {
            if (availableleftGroup.id == i.groupId) {
                count = count + 1;
            }
        });

        if (count == 0) {
            Partial.Variables.leftGroupsList.dataSet.push(availableleftGroup);
        }
    });

    Partial.Variables.leftGroupsList.dataSet.forEach((p) => {
        p.groupName = App.htmlEncode(p.name);
    });

    if (Partial.Widgets.DualListGroups_TD !== undefined) {
        Partial.Widgets.DualListGroups_TD.leftdataset = Partial.Variables.leftGroupsList.dataSet;
    }
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

Partial.btn_addSortConfigClick = function($event, widget) {
    Partial.Variables.sortConfig.dataSet.push({
        'fieldId': null,
        'direction': null,
        'rankOrder': (Partial.Variables.sortConfig.dataSet.length + 1),
        '$index': Partial.Variables.sortConfig.dataSet.length
    });
    // Partial.Widgets.sortConfigList.dataset = [];
    // Partial.Widgets.sortConfigList.dataset = Partial.Variables.sortConfig.dataSet;
};

Partial.deleteSortInfoClick = function($event, widget, item, currentItemWidgets) {
    $($(widget.$element).parents("div[name='sort-input-ph']")[0]).next().removeClass('display-block').addClass('display-none');
    Partial.Variables.sortConfig.dataSet.splice(item.$index, 1);
    Partial.Variables.sortConfig.dataSet.forEach((sortInf, index) => {
        sortInf.$index = index;
        sortInf.rankOrder = (index + 1);
    });
    // Partial.Widgets.sortConfigList.dataset = [];
    // Partial.Widgets.sortConfigList.dataset = Partial.Variables.sortConfig.dataSet;
};

Partial.sortFieldChange = function($event, widget, item, currentItemWidgets, newVal, oldVal) {
    if (newVal && Partial.Variables.sortConfig.dataSet.length > 1) {
        let sortCfgCopy = [];
        Object.assign(sortCfgCopy, Partial.Variables.sortConfig.dataSet);
        sortCfgCopy.splice(item.$index, 1);
        const matchedField = sortCfgCopy.find(sortCfg => sortCfg.fieldId === newVal);

        if (matchedField)
            $($(widget.$element).parents("div[name='sort-input-ph']")[0]).next().removeClass('display-none').addClass('display-block');
        else
            $($(widget.$element).parents("div[name='sort-input-ph']")[0]).next().removeClass('display-block').addClass('display-none');
    }
};

Partial.queueLockonSuccess = function(variable, data) {
    //debugger;
    App.refreshAllQueues();
};


Partial.queueUnLockonSuccess = function(variable, data) {
    App.refreshAllQueues();
    // debugger;
};