var subjects = JSDATA["subjects"].evalJSON();
var timeUnits = JSDATA["timeUnits"].evalJSON();
var courses = JSDATA["courses"].evalJSON();

function addCoursesTableRow() {
  var table = getIxTableById('coursesTable');
  var defaultCurriculum = '';
  var templateCurriculumInput = $('templateCurriculum');
  if (templateCurriculumInput)
    defaultCurriculum = templateCurriculumInput.value;
  
  rowIndex = table.addRow([ '', -1, 0, -1, 0, -1, defaultCurriculum, '', -1 ]);
  for ( var i = 0; i < table.getColumnCount(); i++) {
    table.setCellEditable(rowIndex, i, true);
  }

  $('noCoursesAddedMessageContainer').setStyle({
    display : 'none'
  });
  table.showCell(rowIndex, table.getNamedColumnIndex('removeButton'));

  // Ensure that the newly added row is visible on screen

  var rowElement = table.getRowElement(rowIndex);
  var dimensions = rowElement.getDimensions();
  var offset = rowElement.cumulativeOffset();
  var viewportDimensions = document.viewport.getDimensions();
  var viewportScrollOffsets = document.viewport.getScrollOffsets();
  var elementBottom = offset.top + dimensions.height;
  var viewportBottom = viewportScrollOffsets.top + viewportDimensions.height;
  if (viewportBottom <= elementBottom) {
    Effect.ScrollTo(rowElement, {
      duration : '0.2',
      offset : -20
    });
  }
}

function onLoad(event) {
  tabControl = new IxProtoTabs($('tabs'));
  var curriculums = JSDATA["curriculums"].evalJSON();

  var curriculumOptions = [{text: '', value: ''}];
  for (var i = 0, len = curriculums.length; i < len; i++) {
    curriculumOptions.push({
      text: curriculums[i].name,
      value: curriculums[i].id
    });
  }

  var coursesTable = new IxTable($('coursesTable'), {
    id : "coursesTable",
    columns : [ {
      header : getLocale().getText("settings.editTransferCreditTemplate.coursesTableCourseNameHeader"),
      left : 8,
      right : 8 + 22 + 8 + 90 + 8 + 100 + 8 + 100 + 8 + 200 + 8 + 100 + 8 + 100 + 8,
      dataType : 'text',
      editable : true,
      paramName : 'courseName',
      sortAttributes : {
        sortAscending : {
          toolTip : getLocale().getText("generic.sort.ascending"),
          sortAction : IxTable_ROWSTRINGSORT
        },
        sortDescending : {
          toolTip : getLocale().getText("generic.sort.descending"),
          sortAction : IxTable_ROWSTRINGSORT
        }
      }
    }, {
      header : getLocale().getText("settings.editTransferCreditTemplate.coursesTableCourseOptionalityHeader"),
      width : 100,
      right : 8 + 22 + 8 + 90 + 8 + 100 + 8 + 100 + 8 + 200 + 8 + 100 + 8,
      dataType : 'select',
      editable : true,
      paramName : 'courseOptionality',
      options : [ {
        text : getLocale().getText("settings.editTransferCreditTemplate.coursesTableCourseOptionalityOptional"),
        value : 'OPTIONAL'
      }, {
        text : getLocale().getText("settings.editTransferCreditTemplate.coursesTableCourseOptionalityMandatory"),
        value : 'MANDATORY'
      } ],
      contextMenu : [ {
        text : getLocale().getText("generic.action.copyValues"),
        onclick : new IxTable_COPYVALUESTOCOLUMNACTION(true)
      } ]
    }, {
      header : getLocale().getText("settings.editTransferCreditTemplate.coursesTableCourseNumberHeader"),
      width : 100,
      right : 8 + 22 + 8 + 90 + 8 + 100 + 8 + 100 + 8 + 200 + 8,
      dataType : 'number',
      editable : true,
      paramName : 'courseNumber'
    }, {
      header : getLocale().getText("settings.editTransferCreditTemplate.coursesTableSubjectHeader"),
      width : 200,
      right : 8 + 22 + 8 + 90 + 8 + 100 + 8 + 100 + 8,
      dataType : 'select',
      editable : true,
      paramName : 'subject',
      options : (function() {
        var result = [];
        for ( var i = 0, l = subjects.length; i < l; i++) {
          var text = subjects[i].name;
          if (subjects[i].code != "") {
            text = subjects[i].code + " - " + text;
          }
          if (subjects[i].educationTypeName != "") {
            text = text + " (" + subjects[i].educationTypeName + ")";
          }
          result.push({
            text : text,
            value : subjects[i].id
          });
        }
        return result;
      })(),
      contextMenu : [ {
        text : getLocale().getText("generic.action.copyValues"),
        onclick : new IxTable_COPYVALUESTOCOLUMNACTION(true)
      } ]
    }, {
      header : getLocale().getText("settings.editTransferCreditTemplate.coursesTableLengthHeader"),
      width : 100,
      right : 8 + 22 + 8 + 90 + 8 + 100 + 8,
      dataType : 'number',
      required: true,
      editable : true,
      paramName : 'courseLength',
      contextMenu : [ {
        text : getLocale().getText("generic.action.copyValues"),
        onclick : new IxTable_COPYVALUESTOCOLUMNACTION(true)
      } ]
    }, {
      header : getLocale().getText("settings.editTransferCreditTemplate.coursesTableLengthUnitHeader"),
      width : 100,
      right : 8 + 22 + 8 + 90 + 8,
      dataType : 'select',
      editable : true,
      paramName : 'courseLengthUnit',
      options : (function() {
        var result = [];
        for ( var i = 0, l = timeUnits.length; i < l; i++) {
          result.push({
            text : jsonEscapeHTML(timeUnits[i].name),
            value : timeUnits[i].id
          });
        }
        return result;
      })(),
      contextMenu : [ {
        text : getLocale().getText("generic.action.copyValues"),
        onclick : new IxTable_COPYVALUESTOCOLUMNACTION(true)
      } ]
    }, {
      header : getLocale().getText("settings.editTransferCreditTemplate.coursesTableCurriculumHeader"),
      width: 90,
      right: 8 + 22 + 8,
      dataType: 'select',
      editable: true,
      paramName: 'curriculum',
      options: curriculumOptions,
      contextMenu: [
        {
          text: '<fmt:message key="generic.action.copyValues"/>',
          onclick: new IxTable_COPYVALUESTOCOLUMNACTION(true)
        }
      ]            
    }, {
      right : 8,
      width : 22,
      dataType : 'button',
      imgsrc : GLOBAL_contextPath + '/gfx/list-remove.png',
      tooltip : getLocale().getText("settings.editTransferCreditTemplate.coursesTableRemoveTooltip"),
      onclick : function(event) {
        event.tableComponent.deleteRow(event.row);
        if (event.tableComponent.getRowCount() == 0) {
          $('noCoursesAddedMessageContainer').setStyle({
            display : ''
          });
        }
      },
      paramName : 'removeButton',
      hidden : true
    }, {
      dataType : 'hidden',
      paramName : 'courseId'
    } ]
  });

  var rows = new Array();
  for ( var i = 0, l = courses.length; i < l; i++) {
    var course = courses[i];
    rows.push([ jsonEscapeHTML(course.courseName), course.optionality, course.courseNumber, course.subjectId, course.courseLengthUnits, course.courseLengthUnitId,
        course.curriculumId, '', course.id ]);
  }
  coursesTable.addRows(rows);

  if (coursesTable.getRowCount() > 0) {
    $('noCoursesAddedMessageContainer').setStyle({
      display : 'none'
    });
  }
}
