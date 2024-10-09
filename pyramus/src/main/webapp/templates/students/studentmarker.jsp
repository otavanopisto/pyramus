<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/searchnavigation_support.jsp"></jsp:include>
    
    <style>
      .submitBtn[disabled="disabled"] {
        background-color: #c5c5c5;
        cursor: default;
      }
      .ixTableCellEditorInternetixIcon {
        background-repeat: no-repeat;
        background-position: center;
        width: 16px;
        height: 16px;
        padding: 2px 2px 2px 2px;
      }
    </style>
    
    <script type="text/javascript">
      function onLoad(event) {

        IxTableEditorInternetixIconController = Class.create(IxTableEditorController, {
          buildViewer: function ($super, name, columnDefinition) {
            if (columnDefinition.imgsrc) {
              var cellViewer = new Element("img", { 
                src: columnDefinition.imgsrc, 
                title: columnDefinition.tooltip ? columnDefinition.tooltip : '', 
                className: "ixTableCellViewer ixTableCellEditorInternetixIcon"
              });
              
              if (columnDefinition.viewerClassNames) {
                var classNames = columnDefinition.viewerClassNames.split(' ');
                for (var i = 0, l = classNames.length; i < l; i++) {
                  cellViewer.addClassName(classNames[i]);
                }
              }
              
              cellViewer._editable = false;
              cellViewer._dataType = this.getDataType();
              cellViewer._name = name;
              cellViewer._columnDefinition = columnDefinition;
              
              return cellViewer;
            } else {
              throw new Error("Unable to build button without image");
            }
          },
          setEditorValue: function ($super, handlerInstance, value) {
            if (!value) {
              handlerInstance.setStyle({
                visibility: 'hidden'
              });
            }
          },
          disableEditor: function ($super, handlerInstance) {
            handlerInstance._disabled = true;
            handlerInstance.addClassName("ixTableButtonDisabled");
          },
          enableEditor: function ($super, handlerInstance) {
            handlerInstance._disabled = false;
            handlerInstance.removeClassName("ixTableButtonDisabled");
          },
          destroyEditor: function ($super, handlerInstance) {
            handlerInstance.remove();
          },
          isDisabled: function ($super, handlerInstance) {
            return handlerInstance._disabled == true;
          },
          getDataType: function ($super) {
            return "internetix_marker_icon";  
          },
          getMode: function ($super) {
            return IxTableControllers.EDITMODE_NOT_EDITABLE;
          }
        });
        IxTableControllers.registerController(new IxTableEditorInternetixIconController());
        
        var internetixMarkerTable = new IxTable($('internetixMarkerTable'), {
          id: 'internetixMarkerTable',
          rowHoverEffect: true,
          columns : [{
            headerimg: {
              imgsrc: GLOBAL_contextPath + '/gfx/check-uncheck.png',
              tooltip: 'Valitse/poista kaikki todistusmerkinnät',
              onclick: function (event) {
                var table = event.tableComponent;
                var len = table.getRowCount();
                var columnIndex = table.getNamedColumnIndex('todistus');
                
                if (len > 0) {
                  var selected = table.getCellValue(0, columnIndex) == "1";
                  
                  for (var i = 0; i < len; i++) {
                    if (!selected)
                      table.setCellValue(i, columnIndex, "1");
                    else                    
                      table.setCellValue(i, columnIndex, "0");
                  }
                }
              }
            },
            header : 'TT',
            tooltip: 'Todistus tehty',
            left: 8,
            width: 16,
            dataType: 'checkbox',
            paramName: 'todistus',
            editable: true
          }, {
            header : 'Laskutus',
            tooltip: 'Laskutusmääräys',
            left: 8 + 16 + 8,
            width: 90,
            dataType: 'select',
            options: [
                      {text: "", value: ""},
                      {text: "Opiskelija", value: "STUDENT"},
                      {text: "Oppilaitos", value: "SCHOOL"}
                      ],
            paramName: 'laskutusmaarays',
            editable: true
          }, {
            header : 'Opiskelija',
            left: 8 + 16 + 8 + 90 + 8,
            width: 200,
            dataType: 'text',
            editable: false
          }, {
            header : 'Oppilaitos',
            left: 8 + 16 + 8 + 90 + 8 + 200 + 8,
            width: 200,
            dataType: 'text',
            editable: false
          }, {
            header : 'OMO',
            left: 8 + 16 + 8 + 90 + 8 + 200 + 8 + 200 + 8,
            width: 24,
            imgsrc: GLOBAL_contextPath + '/gfx/icons/16x16/apps/attention.png',
            dataType: 'internetix_marker_icon',
            editable: false
          }, {
            header : 'Kurssi',
            left: 8 + 16 + 8 + 90 + 8 + 200 + 8 + 200 + 8 + 24 + 8,
            width: 250,
            dataType: 'text',
            editable: false
          }, {
            header : 'Arviointipvm',
            left: 8 + 16 + 8 + 90 + 8 + 200 + 8 + 200 + 8 + 24 + 8 + 250 + 8,
            width: 90,
            dataType: 'date',
            editable: false          
          }, {
            header : 'Arvosana',
            left: 8 + 16 + 8 + 90 + 8 + 200 + 8 + 200 + 8 + 24 + 8 + 250 + 8 + 90 + 8,
            width: 80,
            dataType: 'text',
            editable: false
          }, {
            header : 'Arvioija',
            left: 8 + 16 + 8 + 90 + 8 + 200 + 8 + 200 + 8 + 24 + 8 + 250 + 8 + 90 + 8 + 80 + 8,
            width: 200,
            dataType: 'text',
            editable: false
          }, {
            dataType: 'hidden',
            paramName: 'courseStudentId'
          }, {
            dataType: 'hidden',
            paramName: 'personId'
          }, {
            width: 30,
            right: 00,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/eye.png',
            tooltip: 'Näytä opiskelija',
            onclick: function (event) {
              var table = event.tableComponent;
              var personId = table.getCellValue(event.row, table.getNamedColumnIndex('personId'));
              window.open('/students/viewstudent.page?person=' + personId, "student");
            }
          }
          ]
        });
        
        updateView(event);
      }

      function showTodistus(event) {
        var parameters = "&startDate=" + $('startDate').value + "&endDate=" + $('endDate').value;
        
        window.open("/reports/viewreport.page?reportId=24" + parameters, "raportti");
      }
      
      function showLaskutus(event) {
        var parameters = "&startDate=" + $('startDate').value + "&endDate=" + $('endDate').value;
        
        window.open("/reports/viewreport.page?reportId=25" + parameters, "raportti");
      }
      
      function updateView(event) {
        $('internetix_progress').setStyle({ visibility: 'visible'});
        JSONRequest.request("students/internetixmarkerlist.json", {
          parameters: {
            startDate: $('startDate').value,
            endDate: $('endDate').value,
            showChecked: $('showChecked').checked ? "true" : "false"
          },
          onSuccess: function (jsonResponse) {
            var assessmentsTable = getIxTableById('internetixMarkerTable');
            assessmentsTable.detachFromDom();
            assessmentsTable.deleteAllRows();
            var assessments = jsonResponse.assessments;

            var rows = new Array();
            
            for (var i = 0, l = assessments.length; i < l; i++) {
              var assessment = assessments[i];
              
              rows.push([assessment.todistus || false, 
                         assessment.laskutus,
                         assessment.studentName,
                         assessment.schoolName,
                         assessment.contractSchool && !assessment.schoolPaysAllCourses ? true : false,
                         assessment.courseName,
                         assessment.assessmentDate,
                         assessment.grade, 
                         assessment.assessorName, 
                         assessment.courseStudentId,
                         assessment.personId,
                         ''
                         ]);
            }

            if (assessments.length == 0) {
              $('noResults').show();
              $('submitBtn').hide();
            } else {
              $('noResults').hide();
              $('submitBtn').show();
            }
            
            assessmentsTable.addRows(rows);

            assessmentsTable.reattachToDom();
            $('internetix_progress').setStyle({ visibility: 'hidden'});
          }
        });
      }
    </script>
    <style>
      .inl {
        display: table-cell;
        padding-right: 6px;
      }
      .searchBar {
        padding: 16px 8px 16px 8px;
      }
      .noResults {
        padding: 8px;
      }
      .rightSider {
        display: table-cell;
        padding-left: 100px;
      }
      #internetix_progress img {
        top: 2px;
        position: relative;
      }
    </style>
  </head>
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include> 

    <form action="saveinternetixmarkerlist.json" method="post" ix:jsonform="true" ix:useglasspane="true">
      <div id="searchBar" class="searchBar">
        <div class="inl">
          <span class="inl">Alkupäivämäärä:</span> 
          <span class="inl"><input type="text" id="startDate" name="startDate" class="ixDateField" value="${startDate}"/></span>
        </div>
        <div class="inl">
          <span class="inl">Loppupäivämäärä:</span>
          <span class="inl"><input type="text" id="endDate" name="endDate" class="ixDateField" value="${endDate}"/></span>
        </div>
        <div class="inl"><input type="checkbox" id="showChecked"/>Kuitatut</div>
        <div class="inl"><input type="button" name="updateRange" value="Hae" onclick="updateView();"/></div>
        <div class="inl" id="internetix_progress" style="visibility: hidden"><img src="/gfx/progress_small.gif"/></div>
        <div class="rightSider">
          <input type="button" onclick="showTodistus();" value="Todistus" />
          <input type="button" onclick="showLaskutus();" value="Laskutusmääräys" />
        </div>
      </div>
  
      <div id="noResults" class="noResults" style="display: none;">Ei tuloksia ajanjaksolla.</div>

      <div id="internetixMarkerTable"></div>
      
      <input id="submitBtn" type="submit" value="Tallenna" style="display: none;"/>
    </form>
  </body>
</html>