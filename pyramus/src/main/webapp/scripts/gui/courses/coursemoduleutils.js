function addCourseModuleTableRow() {
  getIxTableById('courseModulesTable').addRow(['', '', '', '', '', -1]);
}

function setupCourseModulesTable() {
  var subjects = JSDATA["subjects"].evalJSON();
  var educationalTimeUnits = JSDATA["educationalTimeUnits"].evalJSON();
  var educationTypes = JSDATA["educationTypes"].evalJSON();

  var subjectOptions = constructCourseModuleSubjectTableOptions(educationTypes, subjects);
  var timeUnitOptions = constructEducationTimeUnitTableOptions(educationalTimeUnits);
  
  var courseModulesTable = new IxTable($('courseModulesTableContainer'), {
    id : "courseModulesTable",
    columns : [ {
      header : getLocale().getText("courses.generic.courseModulesTable.subjectHeader"),
      left : 8,
      width : 200,
      paramName: 'subject',
      dataType: 'select',
      options: subjectOptions,
      editable: true,
      required: true
    }, {
      header : getLocale().getText("courses.generic.courseModulesTable.courseNumberHeader"),
      left : 8 + 200 + 8,
      width : 100,
      paramName: 'courseNumber',
      dataType : 'number',
      editable: true
    }, {
      header : getLocale().getText("courses.generic.courseModulesTable.courseLengthHeader"),
      left: 8 + 200 + 8 + 100 + 8,
      width : 60,
      paramName: 'courseLength',
      dataType: 'number',
      editable: true,
      required: true
    }, {
      header : getLocale().getText("courses.generic.courseModulesTable.lengthUnitHeader"),
      left: 8 + 200 + 8 + 100 + 8 + 60 + 8,
      width : 120,
      paramName: 'courseLengthTimeUnit',
      dataType: 'select',
      options: timeUnitOptions,
      editable: true,
      required: true
    }, {
      left: 8 + 200 + 8 + 100 + 8 + 60 + 8 + 120 + 8,
      width: 26,
      dataType: 'button',
      paramName: 'removeButton',
      imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
      tooltip: '<fmt:message key="modules.editModule.componentsTableRemoveRowTooltip"/>',
      onclick: function (event) {
        event.tableComponent.deleteRow(event.row);
      } 
    }, { 
      dataType: 'hidden',
      paramName: 'courseModuleId'
    } ]
  });

  return courseModulesTable;
}

function constructCourseModuleSubjectTableOptions(educationTypes, subjects) {
  var subjectGroups = { "null": { subjects: [] } };
  var subjectGroupsIds = [ "null" ];

  for (var i = 0, l = educationTypes.length; i < l; i++) {
    var educationType = educationTypes[i];
    subjectGroups[educationType.id] = { name: educationType.name, subjects: [] };
    subjectGroupsIds.push(educationType.id);
  }
  
  for (var i = 0, l = subjects.length; i < l; i++) {
    var subject = subjects[i];
    subjectGroups[subject.educationTypeId].subjects.push(subject);
  }
  
  var subjectOptions = [];
  for (var i = 0, l = subjectGroupsIds.length; i < l; i++) {
    var subjectGroupId = subjectGroupsIds[i];
    var subjectGroup = subjectGroups[subjectGroupId];
    if (subjectGroup && subjectGroup.subjects && subjectGroup.subjects.length > 0) {
      if (subjectGroupId != "null" && subjectGroup.name) {
        var subjectOptGroup = { optionGroup: true, text: subjectGroup.name, options: [] };
        
        for (var j = 0; j < subjectGroup.subjects.length; j++) {
          var option = subjectGroup.subjects[j];
          subjectOptGroup.options.push({
            value: option.id,
            text: getLocale().getText("generic.subjectFormatterNoEducationType", option.code, option.name)
          });
        }
        subjectOptions.push(subjectOptGroup);
      } else {
        // No opt group
        
        for (var j = 0; j < subjectGroup.subjects.length; j++) {
          var option = subjectGroup.subjects[j];
          subjectOptions.push({
            value: option.id,
            text: getLocale().getText("generic.subjectFormatterNoEducationType", option.code, option.name)
          });
        }
      }
    }
  }

  return subjectOptions;
}

function constructEducationTimeUnitTableOptions(educationTimeUnits) {
  var timeUnitOptions = [];
  for (var i = 0; i < educationTimeUnits.length; i++) {
    var educationTimeUnit = educationTimeUnits[i];
    timeUnitOptions.push({
      value: educationTimeUnit.id,
      text: educationTimeUnit.name + " (" + educationTimeUnit.symbol + ")"
    });
  }
  return timeUnitOptions;
}
