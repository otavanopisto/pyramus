function addGrade() {
  var table = getIxTableById('gradesTable');
  table.addRow([ true, '', '', 0, '', '' ]);
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
      header : getLocale().getText("settings.createGradingScale.gradesTablePassingGradeHeader"),
      width : 82,
      left : 9,
      dataType : 'checkbox',
      editable : true,
      paramName : 'passingGrade'
    }, {
      header : getLocale().getText("settings.createGradingScale.gradesTableNameHeader"),
      left : 98,
      width : 236,
      dataType : 'text',
      editable : true,
      paramName : 'name',
      required : true
    }, {
      header : getLocale().getText("settings.createGradingScale.gradesTableQualificationHeader"),
      left : 338,
      width : 176,
      dataType : 'text',
      editable : true,
      paramName : 'qualification'
    }, {
      header : getLocale().getText("settings.createGradingScale.gradesTableGPAHeader"),
      left : 518,
      width : 50,
      dataType : 'number',
      editable : true,
      paramName : 'GPA'
    }, {
      header : getLocale().getText("settings.createGradingScale.gradesTableDescriptionHeader"),
      left : 572,
      right : 28,
      dataType : 'text',
      editable : true,
      paramName : 'description'
    }, {
      right : 2,
      dataType : 'button',
      imgsrc : GLOBAL_contextPath + '/gfx/list-remove.png',
      tooltip : getLocale().getText("settings.createGradingScale.gradingScaleTableRemoveTooltip"),
      onclick : function(event) {
        event.tableComponent.deleteRow(event.row);
        if (event.tableComponent.getRowCount() == 0) {
          $('noGradesAddedMessageContainer').setStyle({
            display : ''
          });
        }
      }
    } ]
  });
}