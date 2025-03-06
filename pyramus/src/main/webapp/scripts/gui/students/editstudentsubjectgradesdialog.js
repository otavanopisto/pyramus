window.__editstudentsubjectgradesdialog_grades = [];

function editstudentsubjectgradesdialog_onLoad(event) {
  window.__editstudentsubjectgradesdialog_grades = JSDATA["grades"].evalJSON();
  var studyApprovers = JSDATA["studyApprovers"].evalJSON();
  var studentSubjectGrades = JSDATA["studentSubjectGrades"].evalJSON();
  
  var studentSubjectGradesTable = new IxTable($('studentSubjectGradesTableContainer'), {
    id : "studentSubjectGradesTable",
    rowHoverEffect: true,
    columns : [{
      left: 8,
      width: 22,
      dataType: 'button',
      paramName: 'studentSubjectGradeEditButton',
      imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
      tooltip: getLocale().getText("students.editSubjectGrades.table.editTooltip"),
      onclick: function (event) {
        var table = event.tableComponent;
        var row = event.row;
        var editMode = !(table.getCellValue(row, table.getNamedColumnIndex('edited')) == '1');
        editstudentsubjectgradesdialog_setSubjectRowEditable(table, row, editMode);
      }
    }, {
      header: getLocale().getText("students.editSubjectGrades.table.subjectName"),
      left : 8 + 22 + 8,
      width: 160,
      dataType : 'text',
      editable: false,
      paramName: 'subjectName'
    }, {
      header: getLocale().getText("students.editSubjectGrades.table.subjectState"),
      left : 8 + 22 + 8 + 160 + 8,
      width: 80,
      dataType: 'text',
      editable: false,
      paramName: 'subjectState'
    }, {
      header: getLocale().getText("students.editSubjectGrades.table.computedGrade"),
      left : 8 + 22 + 8 + 160 + 8 + 80 + 8,
      width: 80,
      dataType: 'text',
      editable: false,
      paramName: 'computedGrade'
    }, {
      header: getLocale().getText("students.editSubjectGrades.table.grade"),
      left : 8 + 22 + 8 + 160 + 8 + 80 + 8 + 80 + 8,
      width: 100,
      dataType: 'select',
      editable: false,
      required: true,
      paramName: 'gradeId',
      options: window.__editstudentsubjectgradesdialog_grades
    }, {
      header: getLocale().getText("students.editSubjectGrades.table.gradeDate"),
      left : 8 + 22 + 8 + 160 + 8 + 80 + 8 + 80 + 8 + 100 + 8,
      width: 130,
      dataType: 'date',
      editable: false,
      required: true,
      paramName: 'gradeDate'
    }, {
      header: getLocale().getText("students.editSubjectGrades.table.gradeApprover"),
      left : 8 + 22 + 8 + 160 + 8 + 80 + 8 + 80 + 8 + 100 + 8 + 130 + 8,
      width: 150,
      dataType: 'select',
      editable: false,
      required: true,
      paramName: 'gradeApproverId',
      options: studyApprovers
    }, {
      dataType: 'hidden',
      paramName: 'arithmeticMeanGrade'
    }, {
      dataType: 'hidden',
      paramName: 'subjectId'
    }, {
      dataType: 'hidden',
      paramName: 'subjectCompleted'
    }, {
      dataType: 'hidden',
      paramName: 'originalGradeId'
    }, {
      dataType: 'hidden',
      paramName: 'originalGradeDate'
    }, {
      dataType: 'hidden',
      paramName: 'originalGradeApproverId'
    }, {
      dataType: 'hidden',
      paramName: 'edited'
    }]
  });

  var subjectCreditsData = JSDATA["subjectCredits"].evalJSON();
  var rows = [];

  for (var i = 0, l = subjectCreditsData.subjects.length; i < l; i++) {
    var subject = subjectCreditsData.subjects[i];
    
    // Skip MAY - not applicable
    if (subject.code == "MAY") {
      continue;
    }
    
    var numMandatoryCompletedCount = subject.mandatoryCourseCount ? subject.mandatoryCourseCompletedCount + "/" + subject.mandatoryCourseCount : subject.passedCoursesCount;
    var subjectGrade = studentSubjectGrades[subject.id];

    rows.push([
      '',
      subject.name,
      numMandatoryCompletedCount,
      subject.computedMeanGrade,
      subjectGrade ? subjectGrade.gradeId : '',
      subjectGrade ? subjectGrade.gradeDate : '',
      subjectGrade ? subjectGrade.gradeApproverId : '',
      subject.arithmeticMeanGrade,
      subject.id,
      subject.completed ? '1' : '0',
      subjectGrade ? subjectGrade.gradeId : '',
      subjectGrade ? subjectGrade.gradeDate : '',
      subjectGrade ? subjectGrade.gradeApproverId : '',
      '0'
    ]);
  }
  
  studentSubjectGradesTable.addRows(rows);
}

function editstudentsubjectgradesdialog_activateAllCompletedSubjects() {
  var table = getIxTableById('studentSubjectGradesTable');
  for (var row = 0, l = table.getRowCount(); row < l; row++) {
    var subjectCompleted = table.getCellValue(row, table.getNamedColumnIndex('subjectCompleted')) == '1';
    var noPreviousGrade = table.getCellValue(row, table.getNamedColumnIndex('gradeId')) == '';
    
    if (subjectCompleted && noPreviousGrade) {
      editstudentsubjectgradesdialog_setSubjectRowEditable(table, row, true);
    }
  }
}

function editstudentsubjectgradesdialog_setSubjectRowEditable(table, row, editMode) {
  table.setCellValue(row, table.getNamedColumnIndex('edited'), editMode ? '1' : '0');
  table.setCellEditable(row, table.getNamedColumnIndex('gradeId'), editMode);
  table.setCellEditable(row, table.getNamedColumnIndex('gradeDate'), editMode);
  table.setCellEditable(row, table.getNamedColumnIndex('gradeApproverId'), editMode);

  if (!editMode) {
    // Return to the original (saved) values if the row edit mode is removed
    table.setCellValue(row, table.getNamedColumnIndex('gradeId'), table.getCellValue(row, table.getNamedColumnIndex('originalGradeId')));
    table.setCellValue(row, table.getNamedColumnIndex('gradeDate'), table.getCellValue(row, table.getNamedColumnIndex('originalGradeDate')));
    table.setCellValue(row, table.getNamedColumnIndex('gradeApproverId'), table.getCellValue(row, table.getNamedColumnIndex('originalGradeApproverId')));
  }
  
  if (editMode && document.getElementById("autoFillOnEdit").checked) {
    
    if (!table.getCellValue(row, table.getNamedColumnIndex('gradeId'))) {
      var arithmeticMeanGrade = table.getCellValue(row, table.getNamedColumnIndex('arithmeticMeanGrade'));
      var gradeId = undefined;
      
      // If arithmeticMeanGrade exists, attempt to round it and use that for searching, else use the 
      if (arithmeticMeanGrade) {
        var roundedGrade = Math.round(arithmeticMeanGrade);
        if (roundedGrade != NaN) {
          gradeId = editstudentsubjectgradesdialog_findMatchingGrade(roundedGrade, window.__editstudentsubjectgradesdialog_grades);
        }
      }
      
      if (!gradeId) {
        var computedGrade = table.getCellValue(row, table.getNamedColumnIndex('computedGrade'));
        if (computedGrade) {
          gradeId = editstudentsubjectgradesdialog_findMatchingGrade(computedGrade, window.__editstudentsubjectgradesdialog_grades);
        }
      }
      
      if (gradeId) {
        table.setCellValue(row, table.getNamedColumnIndex('gradeId'), gradeId);
      }
    }
    
    if (!table.getCellValue(row, table.getNamedColumnIndex('gradeDate'))) {
      table.setCellValue(row, table.getNamedColumnIndex('gradeDate'), new Date().getTime());
    }
    
    if (!table.getCellValue(row, table.getNamedColumnIndex('gradeApproverId'))) {
      var approverId = document.getElementById("defaultGradeApproverId").value;
      table.setCellValue(row, table.getNamedColumnIndex('gradeApproverId'), approverId);
    }
  }
}

function editstudentsubjectgradesdialog_findMatchingGrade(grade, gradingScalesList) {
  for (var i = 0, l = gradingScalesList.length; i < l; i++) {
    var gradingScale = gradingScalesList[i];
    for (var j = 0, l2 = gradingScale.options.length; j < l2; j++) {
      if (gradingScale.options[j].text == grade) {
        return gradingScale.options[j].value;
      }
    }
  }
  
  return null;
}
