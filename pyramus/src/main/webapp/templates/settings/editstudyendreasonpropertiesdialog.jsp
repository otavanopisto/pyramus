<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/searchnavigation_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/studentinfopopup_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/glasspane_support.jsp"></jsp:include>
  </head>
  
  <script>
    function onLoad(event) {
      setupPropertiesTable();
    }
  
    function setupPropertiesTable() {
      var variablesTable = new IxTable($('studyEndReasonPropertiesTableContainer'), {
        id : "studyEndReasonPropertiesTable",
        columns : [{
          left: 8,
          width: 30,
          dataType: 'button',
          imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          onclick: function (event) {
            var table = event.tableComponent;
            var valueColumn = table.getNamedColumnIndex('value');
            table.setCellEditable(event.row, valueColumn, table.isCellEditable(event.row, valueColumn) == false);
          }
        }, {
          dataType : 'hidden',
          editable: false,
          paramName: 'key'
        },{
          left : 38,
          width: 150,
          dataType : 'text',
          editable: false,
          paramName: 'name'
        }, {
          left : 188,
          width : 180,
          dataType: 'text',
          editable: false,
          paramName: 'value'
        }]
      });
      
      var variables = JSDATA["properties"].evalJSON();
      console.log(variables);
      
      if (variables) {
        for (var i = 0, l = variables.length; i < l; i++) {
          var rowNumber = variablesTable.addRow([
            '',
            variables[i].key,
            variables[i].name,
            variables[i].value
          ]);
  
          switch (variables[i].type) {
            case 'NUMBER':
              variablesTable.setCellDataType(rowNumber, 3, 'number');
            break;
            case 'DATE':
              variablesTable.setCellDataType(rowNumber, 3, 'date');
            break;
            case 'BOOLEAN':
              variablesTable.setCellDataType(rowNumber, 3, 'checkbox');
            break;
            default:
              variablesTable.setCellDataType(rowNumber, 3, 'text');
            break;
          }
        }
      }
    }

  </script>
  
  <body onload="onLoad(event);">
    <div class="genericFormContainer">
      <form id="editStudyEndReasonPropertiesForm">
        <input type="hidden" name="studyEndReasonId" value="${studyEndReason.id}"/>

        <div class="genericFormSection">  
          <div id="studyEndReasonPropertiesTableContainer"></div>
        </div>
      </form>
    </div>

  </body>
</html>