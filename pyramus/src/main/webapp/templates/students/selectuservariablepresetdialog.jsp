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
    
    <script type="text/javascript">
      var result = "";

      function getResults() {
        return result;
      }
      
      function onLoad(event) {
        var tabs = new IxProtoTabs($('tabContainer'));
        var variableKey = '${variableKey}';

        var presetSelectionTable = new IxTable($('selectUserVariablePresetTableContainer'), {
          id: 'selectUserVariablePresetTable',
          columns : [
            {
              left: 8,
              right: 8,
              dataType: 'text',
              editable: false,
              selectable: false,
              paramName: 'presetValue',
              onclick: function (event) {
                var table = event.tableComponent;
                result = table.getCellValue(event.row, table.getNamedColumnIndex('presetValue'));
                getDialog("selectUserVariablePresetDialog").clickOk();
              }
            }
          ]
        });

        var variablePresets = JSDATA["userVariablePresets"].evalJSON();
        var rows = [];
        if (variablePresets[variableKey]) {
          var presets = variablePresets[variableKey];
          if (presets.presets) {
            for (var presetInd = 0; presetInd < presets.presets.length; presetInd++) {
              var preset = presets.presets[presetInd];
              rows.push([preset.value]);
            }
          }
        }

        if (rows.length > 0) {
          presetSelectionTable.addRows(rows);
        } else {
          getDialog("selectUserVariablePresetDialog").clickCancel();
        }
      }
    </script>

  </head>
  <body onload="onLoad(event);">

    <div class="genericFormContainer">
      <div class="tabLabelsContainer" id="tabContainer">
        <a class="tabLabel" href="#presets"><fmt:message key="students.editStudent.selectUserVariablePresetDialog.tabLabel" /></a>
      </div>

      <div id="presets" class="tabContent">
        <div class="genericFormSection ixTableRowHoverEffect ixTableCellPointer">
          <div id="selectUserVariablePresetTableContainer"></div>            
        </div>
      </div>
      
    </div>

  </body>
</html>