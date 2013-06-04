var grades = JSDATA["grades"].evalJSON();

function addGrade() {
  var table = getIxTableById('gradesTable');
  var rowNumber = table.addRow([ true, '', '', 0, '', '', null, '', 1 ]);
  for ( var i = 0; i < table.getColumnCount(); i++)
    table.setCellEditable(rowNumber, i, true);

  if (table.getRowCount() > 0) {
    $('noGradesAddedMessageContainer').setStyle({
      display : 'none'
    });
  }
}

function onLoad(event) {
  var tabControl = new IxProtoTabs($('tabs'));

  var gradesTable = new IxTable($('gradesTableContainer'), {
    id : "gradesTable",
    columns : [ {
      width : 26,
      left : 8,
      dataType : 'button',
      imgsrc : GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
      tooltip : getLocale().getText("settings.editGradingScale.gradeTableEditTooltip"),
      onclick : function(event) {
        var table = event.tableComponent;
        for ( var i = 0; i < table.getColumnCount(); i++) {
          table.setCellEditable(event.row, i, table.isCellEditable(event.row, i) == false);
        }
        table.setCellValue(event.row, table.getNamedColumnIndex('modified'), 1);
      }
    }, {
      header : getLocale().getText("settings.editGradingScale.gradesTablePassingGradeHeader"),
      width : 90,
      left : 38,
      dataType : 'checkbox',
      editable : false,
      paramName : 'passingGrade'
    }, {
      header : getLocale().getText("settings.editGradingScale.gradesTableNameHeader"),
      left : 136,
      width : 236,
      dataType : 'text',
      editable : false,
      paramName : 'name',
      required : true
    }, {
      header : getLocale().getText("settings.editGradingScale.gradesTableQualificationHeader"),
      left : 376,
      width : 176,
      dataType : 'text',
      editable : false,
      paramName : 'qualification'
    }, {
      header : getLocale().getText("settings.editGradingScale.gradesTableGPAHeader"),
      left : 556,
      width : 50,
      dataType : 'number',
      editable : false,
      paramName : 'GPA'
    }, {
      header : getLocale().getText("settings.editGradingScale.gradesTableDescriptionHeader"),
      left : 610,
      right : 34,
      dataType : 'text',
      editable : false,
      paramName : 'description'
    }, {
      dataType : 'hidden',
      paramName : 'gradeId'
    }, {
      right : 8,
      dataType : 'button',
      imgsrc : GLOBAL_contextPath + '/gfx/list-remove.png',
      tooltip : getLocale().getText("settings.editGradingScale.gradeTableRemoveTooltip"),
      onclick : function(event) {
        event.tableComponent.deleteRow(event.row);
        if (event.tableComponent.getRowCount() == 0) {
          $('noGradesAddedMessageContainer').setStyle({
            display : ''
          });
        }
      }
    }, {
      dataType : 'hidden',
      paramName : 'modified'
    } ]
  });

  var rows = new Array();
  for ( var i = 0, l = grades.length; i < l; i++) {
    rows.push([ '',
                grades[i].passingGrade,
                jsonEscapeHTML(grades[i].name),
                jsonEscapeHTML(grades[i].qualification),
                grades[i].GPA,
                jsonEscapeHTML(grades[i].description),
                grades[i].id,
                '',
                0]);
  }
  gradesTable.addRows(rows);

  if (gradesTable.getRowCount() > 0) {
    $('noGradesAddedMessageContainer').setStyle({
      display : 'none'
    });
  }

}