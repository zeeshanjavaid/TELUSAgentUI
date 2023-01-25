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
Prefab.builderValidationError = '';
Prefab.enums = {};
Prefab.dupCheckTypes = {
    'dupcheck.string': 'string',
    'dupcheck.number': 'number',
    'dupcheck.date': 'date'
};

/*Prefab.onPropertyChange = function(key, newVal, oldVal) {
    if (Prefab.omtenantcontext && Prefab.configuration && Prefab.configuration.fields.length > 0) {
        // Refresh the querybuilder. Call destroy first.
        // Querybuilder was not resetting when rules was set to blank. 
        $('#containerQueryBuilder').queryBuilder('destroy');
        Prefab.builderValidationError = '';
        Prefab.Variables.getEnums.invoke({}, function(enumsData) {
            enumsData.forEach(function(x) {
                Prefab.enums[x.path] = x.values;
            });

            if (Prefab.configuration.rules) {
                Prefab.filterToRules(Prefab.configuration.rules, Prefab.configuration.mode);
            }

            Prefab.initializeQueryBuilder(Prefab.configuration.fields, Prefab.configuration.rules);
        });
    }
};
*/


Prefab.onPropertyChange = function(key, newVal, oldVal) {
    if (Prefab.configuration && Prefab.configuration.fields && Prefab.configuration.fields.length > 0) {
        //debugger;
        // Refresh the querybuilder. Call destroy first.
        // Querybuilder was not resetting when rules was set to blank. 
        $('#containerQueryBuilder').queryBuilder('destroy');
        Prefab.builderValidationError = '';
        /*Prefab.Variables.getEnums.invoke({}, function(enumsData) {
            enumsData.forEach(function(x) {
                Prefab.enums[x.path] = x.values;
            });
        });*/
        /*if (Prefab.configuration.rules) {
            Prefab.filterToRules(Prefab.configuration.rules, Prefab.configuration.mode);
        }

        Prefab.initializeQueryBuilder(Prefab.configuration.fields, Prefab.configuration.rules);*/

        //   if (Prefab.configuration.conditions !== undefined) {

        /*if (Prefab.configuration.conditions.rules) {
            Prefab.filterToRules(Prefab.configuration.conditions.rules);
        }
        Prefab.initializeQueryBuilder(Prefab.configuration.conditions.fields, Prefab.configuration.conditions.rules);*/

        if (Prefab.configuration.rules) {
            Prefab.filterToRules(Prefab.configuration.rules);
        }

        Prefab.initializeQueryBuilder(Prefab.configuration.fields, Prefab.configuration.rules);
        //}
    }
};

Prefab.onReady = function() {};

Prefab.initializeQueryBuilder = function(fields, rules) {

    //if (Prefab.configuration.conditions == undefined) {
    //Prefab.initializeQueryBuilder = function(rules) {
    /* fields.sort(function(a, b) {
         return a.title.localeCompare(b.title);
     });*/

    var filters = [];
    fields.forEach(function(field) {
        var filter = {};
        var type = Prefab.getFieldType(field);
        filter = {
            'id': field.id,
            'label': field.data.title,
            'type': field.type,
            // "placeholder": "enter value",
            //'allow_empty': true
        };

        // SIDDU code
        if (field.input !== undefined) {
            filter.input = field.input;
        }
        if (field.values !== undefined) {
            filter.values = field.values;
        }
        if (field.operators !== undefined) {
            filter.operators = field.operators;
        }
        // END SIDDU code

        // Setting filter based on the type
        switch (type) {

            case 'date':

                filter.validation = {
                    format: 'MM/DD/YYYY' ///^((0|1)\d{1})\/((0|1|2)\d{1})\/((19|20)\d{2})$/
                };
                filter.plugin = 'datepicker';
                filter.plugin_config = {
                    format: 'mm/dd/yyyy',
                    todayBtn: 'linked',
                    todayHighlight: true,
                    autoclose: true
                };
                break;
            case 'dupcheck.string':
                filter.validation = {
                    format: /^[0-9]{1,2}$/,
                    messages: {
                        format: Prefab.appLocale.CONDITION_BUILDER_DUPCHECK_STRING_VALIDATION
                        // 'The selected condition "{{operator}}" should have an integer value between 0 and 99.'
                    }
                };
                break;
            case 'dupcheck.date':
                filter.validation = {
                    format: /^[0-9]{1,3}$/,
                    messages: {
                        format: Prefab.appLocale.CONDITION_BUILDER_DUPCHECK_DATE_VALIDATION
                        // 'The selected condition "{{operator}}" should have an integer value between 0 and 999.'
                    }
                };
                break;
            case 'array':
                // For array, input type is multi select
                filter.plugin = 'selectize';
                filter.plugin_config = {
                    valueField: 'id',
                    labelField: 'name',
                    searchField: 'name',
                    sortField: 'name',
                    create: false,
                    maxItems: 10,
                    fieldInfo: field,
                    plugins: ['remove_button'],
                    onInitialize: function onInitialize(e, g, fieldName) {
                        var that = this;
                        if (that.settings.fieldInfo.data.objectType === 'STATIC_VALUE') {
                            if (!Prefab.enums[that.settings.fieldInfo.path]) {
                                Prefab.enums[that.settings.fieldInfo.path] = [];
                            }

                            Prefab.enums[that.settings.fieldInfo.path].forEach(function(item) {
                                that.addOption({
                                    'id': item,
                                    'name': item
                                });
                            });

                            // function to invoke staticlist variable  
                            Prefab.Variables.getEnums.invoke({}, function(enumsData) {
                                enumsData.forEach(function(x) {
                                    Prefab.enums[x.path] = x.values;
                                    if (x.name == that.settings.fieldInfo.data.name) {
                                        x.elements.forEach(function(item) {
                                            that.addOption({
                                                'id': item.label,
                                                'name': item.value
                                            });
                                        });
                                    }

                                });

                            });


                        } else if (that.settings.fieldInfo.data.objectType === 'DOMAIN_VALUE') {
                            // Get the domain value list
                            // Domain type may have a parent.. So get the parent code as well
                            // and then pull all the values
                            var domainName = that.settings.fieldInfo.data.referred_domainvalue || that.settings.fieldInfo.data.name;

                            Prefab.Variables.getDomainMetadata.setInput({
                                'name': domainName
                            });
                            Prefab.Variables.getDomainMetadata.invoke({}, function(domainData) {
                                Prefab.Variables.getDomainValues.setInput({
                                    'domainValueType': domainName,
                                    'parentDomainValueType': domainData.parent_code
                                });
                                Prefab.Variables.getDomainValues.invoke({}, function(domainValues) {
                                    domainValues.forEach(function(val) {
                                        that.addOption({
                                            'id': val.code,
                                            'name': val.name
                                        });
                                    });
                                });
                            });
                        }
                    }
                };

                // Called when selectize need to be set with data on load
                filter.valueSetter = function(rule, value) {
                    var valArr = value.split(',');
                    var select = rule.$el.find('.rule-value-container [name$=_0]').selectize()[0].selectize;

                    // Domain Value as a special case, as we need to first pull the domain values
                    // and then select it
                    // TODO: Repeating code. Convert to one.
                    if (this.plugin_config.fieldInfo.data.objectType !== 'DOMAIN_VALUE') {
                        // valArr.forEach(function(item) {
                        //     select.addItem(item);
                        // });
                        // function to invoke staticlist variable setter 
                        var fieldName = this.plugin_config.fieldInfo.data.name;
                        Prefab.Variables.getEnums.invoke({}, function(enumsData) {
                            enumsData.forEach(function(x) {
                                if (x.name == fieldName) {
                                    x.elements.forEach(function(item) {
                                        select.addOption({
                                            'id': item.label,
                                            'name': item.value
                                        });
                                    });
                                }

                            });
                            valArr.forEach(function(item) {
                                select.addItem(item);
                            });
                        });


                    } else {
                        var domainName = this.plugin_config.fieldInfo.data.referred_domainvalue || this.plugin_config.fieldInfo.data.name;
                        Prefab.Variables.getDomainMetadata.setInput({
                            'name': domainName
                        });
                        Prefab.Variables.getDomainMetadata.invoke({}, function(domainData) {
                            Prefab.Variables.getDomainValues.setInput({
                                'domainValueType': domainName,
                                'parentDomainValueType': domainData.parent_code
                            });
                            Prefab.Variables.getDomainValues.invoke({}, function(domainValues) {
                                domainValues.forEach(function(val) {
                                    select.addOption({
                                        'id': val.code,
                                        'name': val.name
                                    });
                                });

                                valArr.forEach(function(item) {
                                    select.addItem(item);
                                });
                            });
                        });

                    }
                }.bind(filter);
                break;
            case 'string':
                //filter.placeholder = Prefab.appLocale.ENTER_VALUE;

                filter.validation = {
                    format: /^(?!\s*$)[a-zA-Z0-9ÑÁÉÍÓÚÜáéíñóúü¿¡«»\s-_\/'.:]{1,100}$/, // Can't be just spaces, and allowed -,_,/,',.,: and space along with letters, numbers.
                    messages: {
                        format: Prefab.appLocale.CONDITION_BUILDER_STRING_FORMAT_VALIDATION
                        // 'Enter a value with letters, numbers and spaces (max 100 characters).'
                    }
                };
                break;
            case 'number':
                //filter.placeholder = Prefab.appLocale.ENTER_VALUE;
                break;
        }

        filters.push(filter);



    });
    /*
        var filters = [{
              "allow_empty": true,
             "id": "Application.AnnualSalary",
             "label": "Annual salary",
             "placeholder": "enter value",
             "type": "number"
         },
         {
             "allow_empty": true,
             "id": "Application.Expenditure",
             "label": "Expenditure",
             "placeholder": "enter value",
             "type": "number"
         },
         {
             "allow_empty": true,
             "id": "Application.ApplicantName.First",
             "label": "First",
             "placeholder": "enter value",
             "type": "string"
        }]
    */


    // Initialize Querybuilder plugin

    $('#containerQueryBuilder').queryBuilder({
        lang_code: Prefab.App.i18nService.selectedLocale,
        select_placeholder: Prefab.appLocale.PLACEHOLDER_SELECT,
        plugins: ['bt-tooltip-errors'],
        filters: filters,
        rules: rules,
        allow_empty: true,
        allow_groups: 4
    });
    $('#containerQueryBuilder').on('validationError.queryBuilder', function(e, node, error, value) {
        if (error && error.length > 0) {
            var msg = error[0];
            if (msg === 'no_filter') {
                msg = Prefab.appLocale.CONDITION_FIELD_REQUIRED;
            } else if (msg === 'string_empty') {
                msg = "'" + node.filter.label + "'" + Prefab.appLocale.CONDITION_VALUE_INVALID;
            } else if (msg === 'datetime_empty') {
                msg = "'" + node.filter.label + "'" + Prefab.appLocale.CONDITION_VALUE_INVALID_DATE;
            } else if (msg === 'datetime_invalid') {
                msg = "'" + node.filter.label + "'" + Prefab.appLocale.CONDITION_VALUE_INVALID_DATE_FORMAT;
            } else if (node.filter.type === 'string') {
                msg = "'" + node.filter.label + "' " + Prefab.appLocale.CONDITION_VALUE_NOT_VALID + " " + msg;
            } else if (node.filter.type === 'dupcheck.date' || node.filter.type === 'dupcheck.string') {
                msg = msg.replace(/{{operator}}/g, node.operator.type);
            }


            // When there are multiple error msg, we are showing the errors in multiple lines.
            // This first line has an 'exclamation' icon (from message widget)
            // This space will give the messages in next lines some padding so that all the messages align properly.
            if (Prefab.builderValidationError) msg = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' + msg;
            Prefab.builderValidationError += (Prefab.builderValidationError ? '<br>' : '') + msg;
        }
    });

    $('#containerQueryBuilder').on('afterCreateRuleInput.queryBuilder', function(e, rule) {

        if (rule.filter.plugin === 'datepicker') {
            var $input = rule.$el.find('.rule-value-container [name*=_value_]');
            /* var $inputRule = rule.$el.find('.rule-value-container');
             $inputRule.find('input[type="text"]').attr('style', 'display:none !important');

             $inputRule.prepend('<select class="form-control"name="customDate" id="customDate"> <option value="today">Today</option><option value="tomorrow">Tomorrow</option><option value="customdate">Custom Date</option></select>');

             $inputRule.find('select').on('change', function(e, args) {

                 if ($inputRule.find("select option:selected").text() !== 'Custom Date') {
                     $inputRule.find('input[type="text"]').attr('style', 'display:none !important');

                 } else {
                     $inputRule.find('input[type="text"]').attr('style', 'display:inline-block !important');
                 }
             });*/

            $input.on('dp.change', function() {
                $input.trigger('change');
            });
        } else if (rule.filter.plugin == 'selectize') {
            rule.$el.find('.rule-value-container').css('min-width', '200px').find('.selectize-control').removeClass('form-control');
        }

        // To support other field support in queue configuration.
        // This is an example which can be used in future.
        // Commented because it is not part of the requirement
        // if (rule.filter.input === 'number' && rule.filter.type.indexOf('dupcheck') < 0) {
        //     var opEl = rule.$el.find('.rule-operator-container');
        //     opEl.append('<input type="checkbox" class="" text="Compare with other fields"/>');
        //     opEl.find('select').css('max-width', '350px');
        //     opEl.find('input').css('margin-left', '20px');
        //     opEl.find('input').on('change', function(e, args) {
        //         if (e.currentTarget.checked) {
        //             // Sample data
        //             rule.$el.find('.rule-value-container > input').selectize({
        //                 options: [{
        //                     'id': 1,
        //                     'val': 'one'
        //                 }, {
        //                     'id': 2,
        //                     'val': 'two'
        //                 }],
        //                 placeholder: 'Select',
        //                 labelField: 'val',
        //                 valueField: 'id'
        //             });
        //         }
        //     });
        // }
    });
    //}
};

Prefab.getFieldType = function(field) {
    var VALID_TYPES = ['string', 'boolean', 'number', 'date'];
    var type = field.type,
        format = field.format,
        typeCategory = field.typeCategory,
        fieldType = 'string';
    if (field.data.objectType === 'STATIC_VALUE' || field.data.objectType === 'DOMAIN_VALUE') fieldType = 'array';
    else if (type === 'DateTime' || type === 'date' || type === "ZonedDateTime") fieldType = 'date'; // show date picker
    else if (field.isCurrency) fieldType = typeCategory === 'dupcheck' ? 'currency' : 'number'; // show only exact match condition, for dupcheck. 
    else if (field.isEmail) fieldType = 'string'; // conditions same as that of string
    else if (type === 'boolean') fieldType = 'boolean'; // show only exact match condition
    else if (type === 'number' || type === 'integer' || type === 'double') fieldType = 'number'; // show only exact match condition

    // For dup check, prefix the type with 'dupcheck.' Condition for these type is different
    // for dupcheck. And it is different for queue.
    if (typeCategory === 'dupcheck' && ['string', 'date', 'number', 'boolean', 'currency'].includes(fieldType)) {
        fieldType = 'dupcheck.' + fieldType;
    }

    return fieldType;
};

// Transform filters (returned from service) to plugin rules
Prefab.filterToRules = function(obj, mode) {
    debugger;
    obj.rules = [];

    if (obj.filterCriteria) {
        obj.filterCriteria.forEach(function(f) {
            f.id = f.field;

            if (mode === 'dupcheck' && f.operator === 'contains') {
                f.operator = 'dupcheck_contains';
            }

            obj.rules.push(f);
        });
        delete obj.filterCriteria;
    }

    if (obj.filterGroup) {
        obj.filterGroup.forEach(function(x) {
            obj.rules.push(x);
            Prefab.filterToRules(x, mode);
        });
        delete obj.filterGroup;
    }
};

// Transform plugin rules to filters (expected format of rules in service)
Prefab.rulesToFilter = function(obj) {
    obj.filterGroup = [];
    obj.filterCriteria = [];
    obj.rules.forEach(function(r) {
        if (r.condition) {
            obj.filterGroup.push(r);
            if (r.rules) Prefab.rulesToFilter(r);
        } else {
            r.field = r.id; // Convert dupcheck types

            if (r.type.indexOf('dupcheck') > -1) {
                r.type = Prefab.dupCheckTypes[r.type] ? Prefab.dupCheckTypes[r.type] : r.type;
            }

            if (r.operator === 'dupcheck_contains') {
                r.operator = 'contains';
            }

            obj.filterCriteria.push(r);
        }
    });
    delete obj.rules;
};

// Prefab method
Prefab.getRules = function() {

    Prefab.builderValidationError = '';
    var rules = Prefab.Widgets.containerQueryBuilder.$element.queryBuilder('getRules');

    if (rules) {
        if (rules.valid) {
            delete rules.valid;
        }

        Prefab.rulesToFilter(rules);
        return rules;
    }
};