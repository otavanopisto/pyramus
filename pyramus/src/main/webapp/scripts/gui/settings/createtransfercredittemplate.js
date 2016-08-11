var subjects = JSDATA["subjects"].evalJSON();
var timeUnits = JSDATA["timeUnits"].evalJSON();


function addCoursesTableRow() {
  var table = getIxTableById('coursesTable');
  var defaultCurriculum = '';
  var templateCurriculumInput = $('templateCurriculum');
  if (templateCurriculumInput)
    defaultCurriculum = templateCurriculumInput.value;
  
  rowIndex = table.addRow([ '', -1, 0, -1, 0, -1, defaultCurriculum, '' ]);
  for ( var i = 0; i < table.getColumnCount(); i++) {
    table.setCellEditable(rowIndex, i, true);
  }

  $('noCoursesAddedMessageContainer').setStyle({
    display : 'none'
  });
  table.showCell(rowIndex, table.getNamedColumnIndex('removeButton'));
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
      header : getLocale().getText("settings.createTransferCreditTemplate.coursesTableCourseNameHeader"),
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
      header : getLocale().getText("settings.createTransferCreditTemplate.coursesTableCourseOptionalityHeader"),
      width : 100,
      right : 8 + 22 + 8 + 90 + 8 + 100 + 8 + 100 + 8 + 200 + 8 + 100 + 8,
      dataType : 'select',
      editable : true,
      paramName : 'courseOptionality',
      options : [ {
        text : getLocale().getText("settings.createTransferCreditTemplate.coursesTableCourseOptionalityOptional"),
        value : 'OPTIONAL'
      }, {
        text : getLocale().getText("settings.createTransferCreditTemplate.coursesTableCourseOptionalityMandatory"),
        value : 'MANDATORY'
      } ],
      contextMenu : [ {
        text : getLocale().getText("generic.action.copyValues"),
        onclick : new IxTable_COPYVALUESTOCOLUMNACTION(true)
      } ]
    }, {
      header : getLocale().getText("settings.createTransferCreditTemplate.coursesTableCourseNumberHeader"),
      width : 100,
      right : 8 + 22 + 8 + 90 + 8 + 100 + 8 + 100 + 8 + 200 + 8,
      dataType : 'number',
      editable : true,
      paramName : 'courseNumber'
    }, {
      header : getLocale().getText("settings.createTransferCreditTemplate.coursesTableSubjectHeader"),
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
      header : getLocale().getText("settings.createTransferCreditTemplate.coursesTableLengthHeader"),
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
      header : getLocale().getText("settings.createTransferCreditTemplate.coursesTableLengthUnitHeader"),
      width : 100,
      right : 8 + 22 + 8 + 90 + 8,
      dataType : 'select',
      editable : true,
      paramName : 'courseLengthUnit',
      options : (function() {
        var result = [];
        for ( var i = 0, l = timeUnits.length; i < l; i++) {
          result.push({
            text : timeUnits[i].name,
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
      header : getLocale().getText("settings.createTransferCreditTemplate.coursesTableCurriculumHeader"),
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
      tooltip : getLocale().getText("settings.createTransferCreditTemplate.coursesTableRemoveTooltip"),
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
    } ]
  });
}