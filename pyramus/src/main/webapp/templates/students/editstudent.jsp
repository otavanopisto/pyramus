<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/ix" prefix="ix"%>
<%@ page import="fi.otavanopisto.pyramus.domainmodel.users.Role" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title>
      <fmt:message key="students.editStudent.pageTitle">
        <fmt:param value="${person.latestStudent.fullName}"/>
      </fmt:message>
    </title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/draftapi_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/ckeditor_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/hovermenu_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>

    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/moment/moment.min.js"></script>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/students/koski.js"></script>
    
    <script type="text/javascript">
      function addAddressTableRow(addressTable) {
        addressTable.addRow([-1, '', <c:out value="${contactTypes[0].id}" />, '', '', '', '', '', '', '']);
      }

      function addPhoneTableRow(phoneTable) {
        phoneTable.addRow([-1, '', <c:out value="${contactTypes[0].id}" />, '', '', '']);
      }

      function addEmailTableRow(emailTable) {
        emailTable.addRow([-1, '', <c:out value="${contactTypes[0].id}" />, '', '', '']);
      }

      function addLodgingPeriodTableRow(lodgingPeriodTable) {
        lodgingPeriodTable.addRow([-1, '', '', '']);
      }

      function addStudyPeriodTableRow(studyPeriodTable) {
        studyPeriodTable.addRow([-1, '', 'TEMPORARILY_SUSPENDED', '', '']);
      }

      function initPhoneTable(studentId) {
        var phoneTable = new IxTable($('phoneTable.' + studentId), {
          id : "phoneTable." + studentId,
          columns : [ {
            dataType : 'hidden',
            left : 0,
            width : 0,
            paramName : 'phoneId'
          }, {
            left : 0,
            width : 30,
            dataType: 'radiobutton',
            editable: true,
            paramName: 'defaultNumber',
            tooltip: '<fmt:message key="students.editStudent.phoneTableDefaultTooltip"/>',
          }, {
            header : '<fmt:message key="students.editStudent.phoneTableTypeHeader"/>',
            width: 150,
            left : 30,
            dataType: 'select',
            editable: true,
            paramName: 'contactTypeId',
            options: [
              <c:forEach var="contactType" items="${contactTypes}" varStatus="vs">
                {text: "${contactType.name}", value: ${contactType.id}}
                <c:if test="${not vs.last}">,</c:if>
              </c:forEach>
            ]
          }, {
            header : '<fmt:message key="students.editStudent.phoneTableNumberHeader"/>',
            left : 188,
            width : 200,
            dataType: 'text',
            editable: true,
            paramName: 'phone'
          }, {
            width: 30,
            left: 396,
            dataType: 'button',
            paramName: 'addButton',
            hidden: true,
            imgsrc: GLOBAL_contextPath + '/gfx/list-add.png',
            tooltip: '<fmt:message key="students.editStudent.phoneTableAddTooltip"/>',
            onclick: function (event) {
              addPhoneTableRow(event.tableComponent);
            }
          }, {
            width: 30,
            left: 396,
            dataType: 'button',
            paramName: 'removeButton',
            hidden: true,
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="students.editStudent.phoneTableRemoveTooltip"/>',
            onclick: function (event) {
              event.tableComponent.deleteRow(event.row);
            }
          }]
        });

        phoneTable.addListener("rowAdd", function (event) {
          var phoneTable = event.tableComponent; 
          var enabledButton = event.row == 0 ? 'addButton' : 'removeButton';
          phoneTable.showCell(event.row, phoneTable.getNamedColumnIndex(enabledButton));
        });

        return phoneTable;
      }

      function initEmailTable(studentId) {
        var emailTable = new IxTable($('emailTable.' + studentId), {
          id : "emailTable." + studentId,
          columns : [ {
            dataType : 'hidden',
            left : 0,
            width : 0,
            paramName : 'emailId'
          }, {
            left : 0,
            width : 30,
            dataType: 'radiobutton',
            editable: true,
            paramName: 'defaultAddress',
            tooltip: '<fmt:message key="students.editStudent.emailTableDefaultTooltip"/>',
          }, {
            header : '<fmt:message key="students.editStudent.emailTableTypeHeader"/>',
            width: 150,
            left : 30,
            dataType: 'select',
            editable: true,
            paramName: 'contactTypeId',
            options: [
              <c:forEach var="contactType" items="${contactTypes}" varStatus="vs">
                {text: "${contactType.name}", value: ${contactType.id}}
                <c:if test="${not vs.last}">,</c:if>
              </c:forEach>
            ]
          }, {
            header : '<fmt:message key="students.editStudent.emailTableAddressHeader"/>',
            left : 188,
            width : 200,
            dataType: 'text',
            editable: true,
            paramName: 'email',
            editorClassNames: 'email'
          }, {
            width: 30,
            left: 396,
            dataType: 'button',
            paramName: 'addButton',
            hidden: true,
            imgsrc: GLOBAL_contextPath + '/gfx/list-add.png',
            tooltip: '<fmt:message key="students.editStudent.emailTableAddTooltip"/>',
            onclick: function (event) {
              addEmailTableRow(event.tableComponent);
            }
          }, {
            width: 30,
            left: 396,
            dataType: 'button',
            paramName: 'removeButton',
            hidden: true,
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="students.editStudent.emailTableRemoveTooltip"/>',
            onclick: function (event) {
              event.tableComponent.deleteRow(event.row);
            }
          }]
        });

        emailTable.addListener("rowAdd", function (event) {
          var emailTable = event.tableComponent; 
          var enabledButton = event.row == 0 ? 'addButton' : 'removeButton';
          emailTable.showCell(event.row, emailTable.getNamedColumnIndex(enabledButton));
        });

        return emailTable;
      }

      function initAddressTable(studentId) {
        var addressTable = new IxTable($('addressTable.' + studentId), {
          id : "addressTable." + studentId,
          columns : [{
            dataType : 'hidden',
            left : 0,
            width : 0,
            paramName : 'addressId'
          }, {
            left : 0,
            width : 30,
            dataType: 'radiobutton',
            editable: true,
            paramName: 'defaultAddress',
            tooltip: '<fmt:message key="students.editStudent.addressTableDefaultTooltip"/>',
          }, {
            header : '<fmt:message key="students.editStudent.addressTableTypeHeader"/>',
            left : 30,
            width : 150,
            dataType: 'select',
            editable: true,
            paramName: 'contactTypeId',
            options: [
              <c:forEach var="contactType" items="${contactTypes}" varStatus="vs">
                {text: "${contactType.name}", value: ${contactType.id}}
                <c:if test="${not vs.last}">,</c:if>
              </c:forEach>
            ]
          }, {
            header : '<fmt:message key="students.editStudent.addressTableNameHeader"/>',
            left : 188,
            width : 150,
            dataType: 'text',
            editable: true,
            paramName: 'name'
          }, {
            header : '<fmt:message key="students.editStudent.addressTableStreetHeader"/>',
            left : 344,
            width : 150,
            dataType: 'text',
            editable: true,
            paramName: 'street'
          }, {
            header : '<fmt:message key="students.editStudent.addressTablePostalCodeHeader"/>',
            left : 502,
            width : 100,
            dataType: 'text',
            editable: true,
            paramName: 'postal'
          }, {
            header : '<fmt:message key="students.editStudent.addressTableCityHeader"/>',
            left : 610,
            width : 150,
            dataType: 'text',
            editable: true,
            paramName: 'city'
          }, {
            header : '<fmt:message key="students.editStudent.addressTableCountryHeader"/>',
            left : 768,
            width : 100,
            dataType: 'text',
            editable: true,
            paramName: 'country'
          }, {
            width: 30,
            left: 874,
            dataType: 'button',
            paramName: 'addButton',
            hidden: true,
            imgsrc: GLOBAL_contextPath + '/gfx/list-add.png',
            tooltip: '<fmt:message key="students.editStudent.addressTableAddTooltip"/>',
            onclick: function (event) {
              addAddressTableRow(event.tableComponent);
            }
          }, {
            width: 30,
            left: 874,
            dataType: 'button',
            paramName: 'removeButton',
            hidden: true,
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="students.editStudent.addressTableRemoveTooltip"/>',
            onclick: function (event) {
              event.tableComponent.deleteRow(event.row);
            }
          }]
        });

        addressTable.addListener("rowAdd", function (event) {
          var addressTable = event.tableComponent; 
          var enabledButton = event.row == 0 ? 'addButton' : 'removeButton';
          addressTable.showCell(event.row, addressTable.getNamedColumnIndex(enabledButton));
        });

        return addressTable;
      }

      function initStudentVariableTable(studentId) {
        var variablesTable = new IxTable($('variablesTableContainer.' + studentId), {
          id : "variablesTable." + studentId,
          columns : [{
            left: 8,
            width: 30,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            tooltip: '<fmt:message key="students.editStudent.variablesTableEditTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var valueColumn = table.getNamedColumnIndex('value');
              var editable = table.isCellEditable(event.row, valueColumn) == false;
              table.setCellEditable(event.row, valueColumn, editable);
              table.setCellValue(event.row, table.getNamedColumnIndex('edited'), editable ? "1" : "0");
            }
          }, {
            dataType : 'hidden',
            editable: false,
            paramName: 'key'
          },{
            left : 30 + 8,
            width: 250,
            dataType : 'text',
            editable: false,
            paramName: 'name'
          }, {
            left : 30 + 8 + 250 + 8,
            width : 30,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/overwrite-column-values.png',
            paramName: 'presetsButton',
            hidden: true,
            onclick: function (event) {
              var table = event.tableComponent;
              var rowIndex = event.row;
              var valueColumn = table.getNamedColumnIndex('value');

              var variableKey = table.getCellValue(event.row, table.getNamedColumnIndex('key'));

              var dialog = new IxDialog({
                id : 'selectUserVariablePresetDialog',
                contentURL : GLOBAL_contextPath + '/students/selectuservariablepresetdialog.page?variableKey=' + variableKey,
                centered : true,
                showOk : false,
                showCancel : true,
                title: '<fmt:message key="students.editStudent.selectUserVariablePresetDialog.title"/>',
                cancelLabel: '<fmt:message key="generic.dialog.cancel"/>'
              });

              dialog.addDialogListener(function(event) {
                var dlg = event.dialog;
                switch (event.name) {
                  case 'okClick':
                    event.preventDefault(true);

                    table.setCellEditable(rowIndex, valueColumn, true);
                    table.setCellValue(rowIndex, valueColumn, event.results);
                    table.setCellValue(rowIndex, table.getNamedColumnIndex('edited'), "1");
            
                    dlg.close();
                  break;
                }
              });
              
              dialog.setSize("600px", "300px");
              dialog.open();
            }
          }, {
            left : 30 + 8 + 250 + 8 + 30 + 8,
            width : 350,
            dataType: 'text',
            editable: false,
            paramName: 'value'
          }, {
            dataType: 'hidden',
            editable: false,
            paramName: 'edited'
          }]
        });

        return variablesTable;
      }

      function initPersonVariablesTable() {
        var variablesTable = new IxTable($('personVariablesTableContainer'), {
          id : "personVariablesTable",
          columns : [{
            left: 8,
            width: 30,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            tooltip: '<fmt:message key="students.editStudent.variablesTableEditTooltip"/>',
            onclick: function (event) {
              var table = event.tableComponent;
              var valueColumn = table.getNamedColumnIndex('value');
              var editable = table.isCellEditable(event.row, valueColumn) == false;
              table.setCellEditable(event.row, valueColumn, editable);
              table.setCellValue(event.row, table.getNamedColumnIndex('edited'), editable ? "1" : "0");
            }
          }, {
            dataType : 'hidden',
            editable: false,
            paramName: 'key'
          },{
            left : 30 + 8,
            width: 250,
            dataType : 'text',
            editable: false,
            paramName: 'name'
          }, {
            left : 30 + 8 + 250 + 8,
            width : 350,
            dataType: 'text',
            editable: false,
            paramName: 'value'
          }, {
            dataType: 'hidden',
            editable: false,
            paramName: 'edited'
          }]
        });

        return variablesTable;
      }
      
      function initStudentLodgingPeriodsTable(studentId) {
        var lodgingPeriodsTable = new IxTable($('lodgingPeriodsTableContainer.' + studentId), {
          id : "lodgingPeriodsTable." + studentId,
          columns : [{
            dataType : 'hidden',
            left : 0,
            width : 0,
            paramName : 'id'
          }, {
            header: '<fmt:message key="students.editStudent.lodgingPeriodsTable.begin"/>',
            left : 8,
            width: 160,
            dataType : 'date',
            editable: true,
            paramName: 'begin'
          }, {
            header: '<fmt:message key="students.editStudent.lodgingPeriodsTable.end"/>',
            left : 168,
            width : 160,
            dataType: 'date',
            editable: true,
            paramName: 'end'
          }, {
            width: 30,
            left: 8 + 160 + 8 + 160 + 8,
            dataType: 'button',
            paramName: 'removeButton',
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="students.editStudent.lodgingPeriodsTable.removeTooltip"/>',
            onclick: function (event) {
              event.tableComponent.deleteRow(event.row);
            }
          }]
        });

        return lodgingPeriodsTable;
      }
        
      function initStudentStudyPeriodsTable(studentId) {
        var studentStudyPeriodTypes = JSDATA["studentStudyPeriodTypes"].evalJSON();
        var studentStudyPeriodTypeOptions = [];

        for (var i = 0; i < studentStudyPeriodTypes.length; i++) {
          studentStudyPeriodTypeOptions.push({
            text: studentStudyPeriodTypes[i].displayName,
            value: studentStudyPeriodTypes[i].id
          });
        }

        var studentStudyPeriodsTable = new IxTable($('studentStudyPeriodsTableContainer.' + studentId), {
          id : "studentStudyPeriodsTable." + studentId,
          columns : [{
            dataType : 'hidden',
            left : 0,
            width : 0,
            paramName : 'id'
          }, {
            header: '<fmt:message key="students.editStudent.studyPeriodsTable.begin"/>',
            left : 8,
            width: 160,
            dataType : 'date',
            editable: true,
            required: true,
            paramName: 'begin'
          }, {
            header : '<fmt:message key="students.editStudent.studyPeriodsTable.type"/>',
            left : 8 + 160 + 8,
            width : 200,
            dataType: 'select',
            editable: true,
            required: true,
            paramName: 'type',
            options: studentStudyPeriodTypeOptions
          }, {
            header: '<fmt:message key="students.editStudent.studyPeriodsTable.end"/>',
            left : 8 + 160 + 8 + 200 + 8,
            width: 160,
            dataType : 'date',
            editable: true,
            paramName: 'end'
          }, {
            width: 30,
            left: 8 + 160 + 8 + 200 + 8 + 160 + 8,
            dataType: 'button',
            paramName: 'removeButton',
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="students.editStudent.studyPeriodsTable.removeTooltip"/>',
            onclick: function (event) {
              event.tableComponent.deleteRow(event.row);
            }
          }]
        });

        studentStudyPeriodsTable.addListener("cellValueChange", function (event) {
          var table = event.tableComponent;
          var typeColumnIndex = table.getNamedColumnIndex('type');
          if (event.column == typeColumnIndex) {
            for (var i = 0, l = studentStudyPeriodTypes.length; i < l; i++) {
              if (studentStudyPeriodTypes[i].id == event.value) {
                var endDateColumnIndex = table.getNamedColumnIndex('end');

                studentStudyPeriodTypes[i].beginOnly == true 
                    ? table.hideCell(event.row, endDateColumnIndex)
                    : table.showCell(event.row, endDateColumnIndex);
              }
            }
          }
        });
          
        return studentStudyPeriodsTable;
      }
        
      function setupTags() {
        JSONRequest.request("tags/getalltags.json", {
          onSuccess: function (jsonResponse) {
            <c:forEach var="student" items="${students}">
              new Autocompleter.Local("tags.${student.id}", "tags_choices.${student.id}", jsonResponse.tags, {
                tokens: [',', '\n', ' ']
              });
            </c:forEach>  
          }
        });   
      }
      
      function onLoad(event) {
        var tabControl = new IxProtoTabs($('tabs'));

        var studentLodgingPeriodsContainer = JSDATA["studentLodgingPeriods"].evalJSON();
        var studentStudyPeriodsContainer = JSDATA["studentStudyPeriods"].evalJSON();

        var data = {
          studentLodgingPeriodsContainer : studentLodgingPeriodsContainer,
          studentStudyPeriodsContainer: studentStudyPeriodsContainer
        };
        
        setupRelatedCommandsBasic();
        setupTags();

        Event.observe($('koski-status'), 'click', toggleKoskiLogDetailsVisibility);
        loadLogEntries(${person.id});
        
        var personVariables = JSDATA["personVariables"].evalJSON();
        if (personVariables && personVariables.length > 0) {
          var personVariablesTable = initPersonVariablesTable();
          
          for (var i = 0, l = personVariables.length; i < l; i++) {
            var rowNumber = personVariablesTable.addRow([
              '',
              personVariables[i].key,
              personVariables[i].name,
              personVariables[i].value,
              '0'
            ]);

            switch (personVariables[i].type) {
              case 'NUMBER':
                personVariablesTable.setCellDataType(rowNumber, 3, 'text');
              break;
              case 'DATE':
                personVariablesTable.setCellDataType(rowNumber, 3, 'date');
              break;
              case 'BOOLEAN':
                personVariablesTable.setCellDataType(rowNumber, 3, 'checkbox');
              break;
              default:
                personVariablesTable.setCellDataType(rowNumber, 3, 'text');
              break;
            }
          }
        }

        var addressTable;
        var phoneTable;
        var emailTable;
        var variablesTable;
        var value;
        var studentId;
        
        <c:forEach var="student" items="${students}">
          <c:choose>
            <c:when test="${student.studyProgramme eq null}">
              <c:set var="sprogName">
                <fmt:message key="students.editStudent.noStudyProgrammeTabLabel" />
              </c:set>
            </c:when>
            <c:otherwise>
              <c:set var="sprogName">${fn:escapeXml(student.studyProgramme.name)}</c:set>
            </c:otherwise>
          </c:choose>

          studentId = ${student.id};
          setupRelatedCommands(studentId, '${sprogName}', ${studentHasCredits[student.id]});
          setupStudent(studentId, data);

          // Addresses

          addressTable = initAddressTable(studentId);
  
          <c:forEach var="address" items="${student.contactInfo.addresses}">
            addressTable.addRow([
              ${address.id},
              ${address.defaultAddress},
              ${address.contactType.id},
              '${fn:escapeXml(address.name)}',
              '${fn:escapeXml(address.streetAddress)}',
              '${fn:escapeXml(address.postalCode)}',
              '${fn:escapeXml(address.city)}',
              '${fn:escapeXml(address.country)}',
              '',
              '']);
          </c:forEach>
    
          if (addressTable.getRowCount() == 0) {
            addAddressTableRow(addressTable);
            addressTable.setCellValue(0, 1, true);
          }

          // E-mail addresses

          emailTable = initEmailTable(studentId);

          <c:forEach var="email" items="${student.contactInfo.emails}">
            emailTable.addRow([
              ${email.id},
              ${email.defaultAddress},
              ${email.contactType.id},
              '${fn:escapeXml(email.address)}',
              '',
              '']);
          </c:forEach>

          if (emailTable.getRowCount() == 0) {
            addEmailTableRow(emailTable);
            emailTable.setCellValue(0, 1, true);
          }

          // Phones

          phoneTable = initPhoneTable(studentId);
  
          <c:forEach var="phone" items="${student.contactInfo.phoneNumbers}">
            phoneTable.addRow([
              ${phone.id},
              ${phone.defaultNumber},
              ${phone.contactType.id},
              '${fn:escapeXml(phone.number)}',
              '',
              '']);
          </c:forEach>
    
          if (phoneTable.getRowCount() == 0) {
            addPhoneTableRow(phoneTable);
            phoneTable.setCellValue(0, 1, true);
          }
        </c:forEach>
      }

      function setupStudent(studentId, data) {
        var variables = JSDATA["variables." + studentId].evalJSON();
        var variablePresets = JSDATA["userVariablePresets"].evalJSON();

        if (variables && variables.length > 0) {
          // Student variables
          var variablesTable = initStudentVariableTable(studentId);
          var valueColumn = variablesTable.getNamedColumnIndex('value');

          
          for (var i = 0, l = variables.length; i < l; i++) {
            var rowNumber = variablesTable.addRow([
              '',
              variables[i].key,
              variables[i].name,
              '',
              variables[i].value,
              '0'
            ]);

            switch (variables[i].type) {
              case 'NUMBER':
                variablesTable.setCellDataType(rowNumber, valueColumn, 'text');
              break;
              case 'DATE':
                variablesTable.setCellDataType(rowNumber, valueColumn, 'date');
              break;
              case 'BOOLEAN':
                variablesTable.setCellDataType(rowNumber, valueColumn, 'checkbox');
              break;
              default:
                variablesTable.setCellDataType(rowNumber, valueColumn, 'text');
              break;
            }

            if (variablePresets[variables[i].key]) {
              var presets = variablePresets[variables[i].key];
              if (presets.presets) {
                if (presets.presets.length > 0) {
                  variablesTable.showCell(rowNumber, variablesTable.getNamedColumnIndex('presetsButton'));
                }
              }
            }
          }
        }
          
        var lodgingPeriods = data.studentLodgingPeriodsContainer[studentId.toString()];
        var lodgingPeriodsTable = initStudentLodgingPeriodsTable(studentId);

        if (lodgingPeriods && lodgingPeriods.length > 0) {
          var lodgingPeriodRows = new Array();
          for (var i = 0, l = lodgingPeriods.length; i < l; i++) {
            lodgingPeriodRows.push([
              lodgingPeriods[i].id,
              lodgingPeriods[i].begin,
              lodgingPeriods[i].end,
              ''
            ]);
          }
          lodgingPeriodsTable.addRows(lodgingPeriodRows);
        }

        var studyPeriodsTable = initStudentStudyPeriodsTable(studentId);
        var studyPeriods = data.studentStudyPeriodsContainer[studentId.toString()];

        if (studyPeriods && studyPeriods.length > 0) {
          var studyPeriodRows = new Array();
          for (var i = 0, l = studyPeriods.length; i < l; i++) {
            studyPeriodRows.push([
              studyPeriods[i].id,
              studyPeriods[i].begin,
              studyPeriods[i].type,
              studyPeriods[i].end,
              ''
            ]);
          }
          studyPeriodsTable.addRows(studyPeriodRows);
        }

        Event.observe($('studyEndReason.' + studentId), 'change', _onStudyEndReasonChange);
      }

      function _onStudyEndReasonChange(event) {
        var studyEndReasonElement = Event.element(event);
        var studentId = studyEndReasonElement.getAttribute('data-studentid');
        var studyApproverElement = $('studyApprover.' + studentId);
        var approvalRequired = false;

        var selectedOption = studyEndReasonElement.selectedIndex >= 0 ? studyEndReasonElement.options[studyEndReasonElement.selectedIndex] : undefined;

        if (selectedOption) {
          approvalRequired = selectedOption.getAttribute("data-approvalrequired") == "1";
        }
        
        if (approvalRequired) {
          studyApproverElement.addClassName('required');
          initializeValidation(studyApproverElement.parentNode);
        } else {
          studyApproverElement.removeClassName('required');
          studyApproverElement.removeClassName('valid');
          studyApproverElement.removeClassName('invalid');
          deinitializeValidation(studyApproverElement.parentNode);
        }        
        forceRevalidateAll(true);
      }
      
      function setupRelatedCommandsBasic() {
        var relatedActionsHoverMenu = new IxHoverMenu($('basicRelatedActionsHoverMenuContainer'), {
          text: '<fmt:message key="students.editStudent.basicTabRelatedActionsLabel"/>'
        });
    
        relatedActionsHoverMenu.addItem(new IxHoverMenuClickableItem({
          iconURL: GLOBAL_contextPath + '/gfx/eye.png',
          text: '<fmt:message key="students.editStudent.basicTabRelatedActionLabel"/>',
          onclick: function (event) {
            redirectTo(GLOBAL_contextPath + '/students/viewstudent.page?person=${person.id}');
          }
        }));

        relatedActionsHoverMenu.addItem(new IxHoverMenuLinkItem({
          iconURL: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          text: '<fmt:message key="students.editStudent.basicTabRelatedActionsManageContactEntriesLabel"/>',
          link: GLOBAL_contextPath + '/students/managestudentcontactentries.page?person=${person.id}'  
        }));
      }

      function setupRelatedCommands(studentId, studyProgrammeName, studentHasCredits) {
        var studentRelatedActionsHoverMenu = new IxHoverMenu($('studentRelatedActionsHoverMenuContainer.' + studentId), {
          text: '<fmt:message key="students.editStudent.studentTabRelatedActionsLabel"/>'
        });
    
        studentRelatedActionsHoverMenu.addItem(new IxHoverMenuClickableItem({
          iconURL: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          text: '<fmt:message key="students.editStudent.studentTabRelatedActionsCopyAsNewStudyProgrammeLabel"/>',
          onclick: function (event) {
            var dialog = new IxDialog({
              id : 'chooseCopyMethod',
              contentURL : GLOBAL_contextPath + '/students/studyprogrammecopydialog.page?student=' + studentId,
              centered : true,
              showOk : true,  
              showCancel : true,
              disableOk: true,
              title : '<fmt:message key="students.copyStudyProgrammePopup.dialogTitle"/>',
              okLabel : '<fmt:message key="students.copyStudyProgrammePopup.okLabel"/>',
              cancelLabel : '<fmt:message key="students.copyStudyProgrammePopup.cancelLabel"/>'
            });

            var dHeight = studentHasCredits ? "320px" : "240px";
            
            dialog.setSize("360px", dHeight);
            dialog.addDialogListener( function(event) {
              var dlg = event.dialog;
          
              switch (event.name) {
                case 'okClick':
                  var pelem = $(dlg.getContentDocument().documentElement);
                  var cbox = pelem.down("input[name='linkStudentCreditsCheckbox']");
                  var linkCredits = cbox.checked == true ? true : false;

                  var defaultUserCheckBox = pelem.down("input[name='defaultUserCheckBox']");
                  var setAsDefaultUser = defaultUserCheckBox.checked == true ? true : false;

                  var newStudyProgrammeIdSelect = pelem.down("select[name='newStudyProgrammeId']");
                  var newStudyProgrammeId = newStudyProgrammeIdSelect.value;
                  
                  JSONRequest.request("students/copystudyprogramme.json", {
                    parameters: {
                      studentId: studentId,
                      newStudyProgrammeId: newStudyProgrammeId,
                      linkCredits: linkCredits,
                      setAsDefaultUser: setAsDefaultUser
                    },
                    onSuccess: function (jsonResponse) {
                      window.location.reload();
                    }
                  });   
                break;
              }
            });
          
            dialog.open();
          }
        }));       
    
        studentRelatedActionsHoverMenu.addItem(new IxHoverMenuLinkItem({
          iconURL: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          text: '<fmt:message key="students.editStudent.studentTabRelatedActionsManageTransferCreditsLabel"/>',
          link: GLOBAL_contextPath + '/grading/managetransfercredits.page?studentId=' + studentId  
        }));      

        <c:if test="${loggedUserRoles.contains(Role.ADMINISTRATOR)}">
        studentRelatedActionsHoverMenu.addItem(new IxHoverMenuClickableItem({
          iconURL: GLOBAL_contextPath + '/gfx/edit-delete.png',
          text: '<fmt:message key="students.editStudent.studentTabRelatedActionsArchiveStudentLabel"/>',
          onclick: function (event) {
            var url = GLOBAL_contextPath + "/simpledialog.page?localeId=students.editStudent.archiveStudentConfirmDialogContent&localeParams=" + encodeURIComponent(studyProgrammeName);
            
            var dialog = new IxDialog({
              id : 'confirmRemoval',
              contentURL : url,
              centered : true,
              showOk : true,  
              showCancel : true,
              autoEvaluateSize: true,
              title : '<fmt:message key="students.editStudent.archiveStudentConfirmDialogTitle"/>',
              okLabel : '<fmt:message key="students.editStudent.archiveStudentConfirmDialogOkLabel"/>',
              cancelLabel : '<fmt:message key="students.editStudent.archiveStudentConfirmDialogCancelLabel"/>'
            });
          
            dialog.addDialogListener( function(event) {
              var dlg = event.dialog;
          
              switch (event.name) {
                case 'okClick':
                  JSONRequest.request("students/archivestudent.json", {
                    parameters: {
                      student: studentId
                    },
                    onSuccess: function (jsonResponse) {
                      window.location.reload();
                    }
                  });   
                break;
              }
            });
          
            dialog.open(); 
          }
        }));
        </c:if>
      }
    </script>
    
    <ix:extensionHook name="students.editStudent.head" />
  </head>

  <body onload="onLoad(event);" ix:enabledrafting="true">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
  
    <h1 class="genericPageHeader">
      <fmt:message key="students.editStudent.pageTitle">
        <fmt:param value="${person.latestStudent.fullName}"/>
      </fmt:message>
      
      <span id="koski-status" class="koski-status">KOSKI</span>
    </h1>

    <div id="koski-status-details" style="display: none;">
    </div>

    <div id="editStudentEditFormContainer"> 
      <div class="genericFormContainer"> 

        <form action="editstudent.json" method="post" ix:jsonform="true" ix:useglasspane="true" autocomplete="off">
          <input type="hidden" name="version" value="${person.version}"/>
        
          <div class="tabLabelsContainer" id="tabs">
            <a class="tabLabel" href="#basic">
              <fmt:message key="students.editStudent.studentBasicInfoTabLabel"/>
            </a>

            <c:if test="${hasInternalAuthenticationStrategies and allowEditCredentials}">
              <a class="tabLabel" href="#credentials">
                <fmt:message key="students.editStudent.studentCredentialsTabLabel"/>
              </a>
            </c:if>

            <c:forEach var="student" items="${students}">
              <a class="tabLabel" href="#student.${student.id}">
                <c:choose>
                  <c:when test="${student.studyProgramme == null}">
                     <fmt:message key="students.editStudent.noStudyProgrammeTabLabel"/>
                  </c:when>
                  <c:otherwise>
                    ${student.studyProgramme.name}
                  </c:otherwise>
                </c:choose>
                
                <c:if test="${student.hasFinishedStudies}">*</c:if>
              </a>
            </c:forEach>
            <ix:extensionHook name="students.editStudent.tabLabels"/>
          </div>
          
          <div id="basic" class="tabContent">    
            <div id="basicRelatedActionsHoverMenuContainer" class="tabRelatedActionsContainer"></div>
            
            <input type="hidden" name="personId" value="${person.id}"/>
            
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.editStudent.firstNameTitle"/>
                <jsp:param name="helpLocale" value="students.editStudent.firstNameHelp"/>
              </jsp:include>            
              ${person.latestStudent.firstName}
            </div>
  
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.editStudent.lastNameTitle"/>
                <jsp:param name="helpLocale" value="students.editStudent.lastNameHelp"/>
              </jsp:include>            
              ${person.latestStudent.lastName}
            </div>
            
            <div class="genericFormSection">   
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.editStudent.birthdayTitle"/>
                <jsp:param name="helpLocale" value="students.editStudent.birthdayHelp"/>
              </jsp:include>            
              <input type="text" name="birthday" class="ixDateField" value="${person.birthday.time}">
            </div>
      
            <div class="genericFormSection">     
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.editStudent.ssecIdTitle"/>
                <jsp:param name="helpLocale" value="students.editStudent.ssecIdHelp"/>
              </jsp:include>            
              <input type="text" name="ssecId" value="${person.socialSecurityNumber}" size="15" class="mask" data-validatemask="^([0-9]{6})[-A]([0-9A-Z]{4})$">
            </div>

            <div class="genericFormSection">       
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.editStudent.genderTitle"/>
                <jsp:param name="helpLocale" value="students.editStudent.genderHelp"/>
              </jsp:include>            
              <select name="gender">
                <option value="MALE" ${person.sex == 'MALE' ? 'selected="selected"' : ''}><fmt:message key="generic.genders.male"/></option>
                <option value="FEMALE" ${person.sex == 'FEMALE' ? 'selected="selected"' : ''}><fmt:message key="generic.genders.female"/></option>
                <option value="OTHER" ${person.sex == 'OTHER' ? 'selected="selected"' : ''}><fmt:message key="generic.genders.other"/></option>
              </select>
            </div>

            <div class="genericFormSection">       
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.editStudent.secureInfoTitle"/>
                <jsp:param name="helpLocale" value="students.editStudent.secureInfoHelp"/>
              </jsp:include>
              <c:choose>
                <c:when test="${person.secureInfo}"><input type="checkbox" name="secureInfo" value="true" checked="checked"/></c:when>
                <c:otherwise><input type="checkbox" name="secureInfo" value="true"/></c:otherwise>              
              </c:choose>
              <fmt:message key="students.editStudent.secureInfoCheckboxLabel"/>
            </div>

            <c:choose>
              <c:when test="${fn:length(personVariableKeys) gt 0}">
                <div class="genericFormSection">  
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="students.editStudent.personVariablesTitle"/>
                    <jsp:param name="helpLocale" value="students.editStudent.personVariablesHelp"/>
                  </jsp:include>
                  <div id="personVariablesTableContainer"></div>
                </div>
              </c:when>
            </c:choose>

            <div class="genericFormSection">         
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="students.editStudent.personBasicInfoTitle"/>
                <jsp:param name="helpLocale" value="students.editStudent.personBasicInfoHelp"/>
              </jsp:include>            
              <textarea name="basicInfo" ix:cktoolbar="studentAdditionalInformation" ix:ckeditor="true">${person.basicInfo}</textarea>
            </div>
            <ix:extensionHook name="students.editStudent.tabs.basic"/>
          </div>

          <c:if test="${hasInternalAuthenticationStrategies and allowEditCredentials}">
            <div id="credentials" class="tabContent">    
              <div id="basicRelatedActionsHoverMenuContainer" class="tabRelatedActionsContainer"></div>
  
              <div id="editUserCredentialsContainer">
                <div class="genericFormSection">  
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="students.editStudent.usernameTitle"/>
                    <jsp:param name="helpLocale" value="students.editStudent.usernameHelp"/>
                  </jsp:include>                  
                  <input type="text" name="username" autocomplete="new-username" value="${username}" size="30">
                </div>
                
                <div class="genericFormSection">  
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="students.editStudent.password1Title"/>
                    <jsp:param name="helpLocale" value="students.editStudent.password1Help"/>
                  </jsp:include>                  
                  <input type="password" class="equals equals-password2" autocomplete="new-password" name="password1" value="" size="30">
                </div>
                
                <div class="genericFormSection">  
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="students.editStudent.password2Title"/>
                    <jsp:param name="helpLocale" value="students.editStudent.password2Help"/>
                  </jsp:include>                  
                  <input type="password" class="equals equals-password1" autocomplete="new-password" name="password2" value="" size="30">
                </div>
              </div>
              
              <ix:extensionHook name="students.editStudent.tabs.credentials"/>
            </div>
          </c:if>
          
          <c:forEach var="student" items="${students}">
            <div id="student.${student.id}" class="tabContent">
              <input type="hidden" name="studentVersion.${student.id}" value="${student.version}"/>
                  
              <div id="studentRelatedActionsHoverMenuContainer.${student.id}" class="tabRelatedActionsContainer"></div>

              <div class="genericFormSection">           
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.studyProgrammeTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.studyProgrammeHelp"/>
                </jsp:include>            
                <div>${student.studyProgramme.name}<c:if test="${student.studyProgramme.archived}"> *</c:if></div>
              </div>
  
              <div class="genericFormSection">           
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.curriculumTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.curriculumHelp"/>
                </jsp:include>            
                <select name="curriculum.${student.id}">
                  <option></option>           
                  <c:forEach var="curriculum" items="${curriculums}">
                    <c:choose>
                      <c:when test="${curriculum.id eq student.curriculum.id}">
                        <option value="${curriculum.id}" selected="selected">${curriculum.name}</option> 
                      </c:when>
                      <c:otherwise>
                        <option value="${curriculum.id}">${curriculum.name}</option> 
                      </c:otherwise>
                    </c:choose>
                  </c:forEach>
                  <c:if test="${student.curriculum.archived}">
                    <option value="${student.curriculum.id}" selected="selected">${student.curriculum.name}*</option>
                  </c:if>
                </select>
              </div>
  
              <div class="genericFormSection">                                
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.studentFunding.ui.title"/>
                  <jsp:param name="helpLocale" value="students.studentFunding.ui.help"/>
                </jsp:include>
            
                <select name="funding.${student.id}">
                  <option value=""><fmt:message key="students.studentFunding.defaultFunding"/></option>
                  <option value="GOVERNMENT_FUNDING" ${student.funding == 'GOVERNMENT_FUNDING' ? 'selected="selected"' : ''}><fmt:message key="students.studentFunding.governmentFunding"/></option>
                  <option value="OTHER_FUNDING" ${student.funding == 'OTHER_FUNDING' ? 'selected="selected"' : ''}><fmt:message key="students.studentFunding.otherFunding"/></option>
                </select>
              </div>
            
              <div class="genericFormSection">           
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.firstNameTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.firstNameHelp"/>
                </jsp:include>            
                <input type="text" class="required" name="firstName.${student.id}" value="${fn:escapeXml(student.firstName)}" size="20">
              </div>
    
              <div class="genericFormSection">             
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.lastNameTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.lastNameHelp"/>
                </jsp:include>            
                <input type="text" class="required" name="lastName.${student.id}" value="${fn:escapeXml(student.lastName)}" size="30">
              </div>
              
              <div class="genericFormSection">  
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.nicknameTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.nicknameHelp"/>
                </jsp:include>          
                <input type="text" name="nickname.${student.id}" value="${fn:escapeXml(student.nickname)}" size="30">                                 
              </div>

              <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.tagsTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.tagsHelp"/>
                </jsp:include>
                <input type="text" id="tags.${student.id}" name="tags.${student.id}" size="40" value="${fn:escapeXml(tags[student.id])}"/>
                <div id="tags_choices.${student.id}" class="autocomplete_choices"></div>
              </div>
            
              <div class="genericFormSection">                
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.addressesTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.addressesHelp"/>
                </jsp:include>
                <div id="addressTable.${student.id}"></div>
              </div>

              <div class="genericFormSection">               
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.emailTableEmailsTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.emailTableEmailsHelp"/>
                </jsp:include>
                <div id="emailTable.${student.id}"></div>
              </div>

              <div class="genericFormSection">                
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.phoneNumbersTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.phoneNumbersHelp"/>
                </jsp:include>
                <div id="phoneTable.${student.id}"></div>
              </div>
      
              <div class="genericFormSection">                                  
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.otherContactInfoTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.otherContactInfoInfoHelp"/>
                </jsp:include>
                <textarea name="otherContactInfo.${student.id}" rows="5" cols="50">${student.contactInfo.additionalInfo}</textarea>
              </div>

              <div class="genericFormSection">  
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.parentBillingDetailsTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.parentBillingDetailsHelp"/>
                </jsp:include>
                <textarea name="parentBillingDetails.${student.id}" rows="5" cols="50">${student.parentBillingDetails}</textarea>
              </div>

              <div class="genericFormSection">                    
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.municipalityTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.municipalityHelp"/>
                </jsp:include>
                  <select name="municipality.${student.id}">           
                  <option></option>  
                  <c:forEach var="municipality" items="${municipalities}">
                    <c:choose>
                      <c:when test="${municipality.id eq student.municipality.id}">
                        <option value="${municipality.id}" selected="selected">${municipality.name}</option> 
                      </c:when>
                      <c:otherwise>
                        <option value="${municipality.id}">${municipality.name}</option> 
                      </c:otherwise>
                    </c:choose>
                  </c:forEach>
                </select>
              </div>
    
              <div class="genericFormSection">                      
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.languageTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.languageHelp"/>
                </jsp:include>
                <select name="language.${student.id}">           
                  <option></option>  
                  <c:forEach var="language" items="${languages}">
                    <c:choose>
                      <c:when test="${language.id eq student.language.id}">
                        <option value="${language.id}" selected="selected">${language.name}</option> 
                      </c:when>
                      <c:otherwise>
                        <option value="${language.id}">${language.name}</option> 
                      </c:otherwise>
                    </c:choose>
                  </c:forEach>
                </select>
              </div>
    
              <div class="genericFormSection">                        
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.nationalityTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.nationalityHelp"/>
                </jsp:include>
                <select name="nationality.${student.id}">
                  <option></option>  
                  <c:forEach var="nationality" items="${nationalities}">
                    <c:choose>
                      <c:when test="${nationality.id eq student.nationality.id}">
                        <option value="${nationality.id}" selected="selected">${nationality.name}</option> 
                      </c:when>
                      <c:otherwise>
                        <option value="${nationality.id}">${nationality.name}</option> 
                      </c:otherwise>
                    </c:choose>
                  </c:forEach>
                </select>
              </div>
    
              <div class="genericFormSection">                          
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.activityTypeTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.activityTypeHelp"/>
                </jsp:include>
                <select name="activityType.${student.id}">
                  <option></option>  
                  <c:forEach var="activityType" items="${activityTypes}">
                    <c:choose>
                      <c:when test="${activityType.id eq student.activityType.id}">
                        <option value="${activityType.id}" selected="selected">${activityType.name}</option> 
                      </c:when>
                      <c:otherwise>
                        <option value="${activityType.id}">${activityType.name}</option> 
                      </c:otherwise>
                    </c:choose>
                  </c:forEach>
                </select>
              </div>
  
              <div class="genericFormSection">                            
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.examinationTypeTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.examinationTypeHelp"/>
                </jsp:include>
                <select name="examinationType.${student.id}">
                  <option></option>  
                  <c:forEach var="examinationType" items="${examinationTypes}">
                    <c:choose>
                      <c:when test="${examinationType.id eq student.examinationType.id}">
                        <option value="${examinationType.id}" selected="selected">${examinationType.name}</option> 
                      </c:when>
                      <c:otherwise>
                        <option value="${examinationType.id}">${examinationType.name}</option> 
                      </c:otherwise>
                    </c:choose>
                  </c:forEach>
                </select>
              </div>
  
              <div class="genericFormSection">                              
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.educationalLevelTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.educationalLevelHelp"/>
                </jsp:include>
                <select name="educationalLevel.${student.id}">
                  <option></option>  
                  <c:forEach var="educationalLevel" items="${educationalLevels}">
                    <c:choose>
                      <c:when test="${educationalLevel.id eq student.educationalLevel.id}">
                        <option value="${educationalLevel.id}" selected="selected">${educationalLevel.name}</option> 
                      </c:when>
                      <c:otherwise>
                        <option value="${educationalLevel.id}">${educationalLevel.name}</option> 
                      </c:otherwise>
                    </c:choose>
                  </c:forEach>
                </select>
              </div>
  
              <div class="genericFormSection">                                
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.schoolTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.schoolHelp"/>
                </jsp:include>
            
                <select name="school.${student.id}">
                  <option value="-1"></option>           
                  <c:forEach var="school" items="${schools}">
                    <c:choose>
                      <c:when test="${school.id eq student.school.id}">
                        <option value="${school.id}" selected="selected">${school.name}</option> 
                      </c:when>
                      <c:otherwise>
                        <option value="${school.id}">${school.name}</option> 
                      </c:otherwise>
                    </c:choose>
                  </c:forEach>
                  <c:if test="${student.school.archived}">
                    <option value="${student.school.id}" selected="selected">${student.school.name}*</option>
                  </c:if>
                </select>
              </div>
            
              <div class="genericFormSection">  
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.educationTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.educationHelp"/>
                </jsp:include>                                    
                <input type="text" name="education.${student.id}" value="${fn:escapeXml(student.education)}" size="50">       
              </div>
              
              <div class="genericFormSection">                                    
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.previousStudiesTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.previousStudiesHelp"/>
                </jsp:include>
                <input type="text" name="previousStudies.${student.id}" value="${fn:escapeXml(student.previousStudies)}" size="5">
              </div>
  
              <div class="genericFormSection">                                      
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.studyTimeEndTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.studyTimeEndHelp"/>
                </jsp:include>
                <input type="text" name="studyTimeEnd.${student.id}" class="ixDateField" value="${fn:escapeXml(student.studyTimeEnd.time)}"/>
              </div>
  
              <div class="genericFormSection">  
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.studyStartDateTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.studyStartDateHelp"/>
                </jsp:include>
                <input type="text" name="studyStartDate.${student.id}" class="ixDateField" value="${fn:escapeXml(student.studyStartDate.time)}"/>
              </div>
  
              <div class="genericFormSection">  
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.studyPeriodsTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.studyPeriodsHelp"/>
                </jsp:include>
                
                <div class="genericTableAddRowContainer">
                  <span class="genericTableAddRowLinkContainer" onclick="addStudyPeriodTableRow(getIxTableById('studentStudyPeriodsTable.${student.id}'));"><fmt:message key="students.editStudent.addStudyPeriodLink"/></span>
                </div>

                <div id="studentStudyPeriodsTableContainer.${student.id}"></div>
              </div>
  
              <div class="genericFormSection">    
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.studyEndDateTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.studyEndDateHelp"/>
                </jsp:include>
                <input type="text" name="studyEndDate.${student.id}" class="ixDateField" value="${fn:escapeXml(student.studyEndDate.time)}"/>
              </div>
  
              <div class="genericFormSection">      
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.studyEndReasonTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.studyEndReasonHelp"/>
                </jsp:include>
                
                <select id="studyEndReason.${student.id}" name="studyEndReason.${student.id}" data-studentid="${student.id}">
                  <option></option>  
                  <c:forEach var="reason" items="${studyEndReasons}">
                    <c:choose>
                      <c:when test="${reason.id eq student.studyEndReason.id}">
                        <option value="${reason.id}" data-approvalrequired="${reason.properties['studyApprovalRequired']}" selected="selected">${reason.name}</option> 
                      </c:when>
                      <c:otherwise>
                        <option value="${reason.id}" data-approvalrequired="${reason.properties['studyApprovalRequired']}">${reason.name}</option> 
                      </c:otherwise>
                    </c:choose>
    
                    <c:if test="${fn:length(reason.childEndReasons) gt 0}">
                      <optgroup>
                        <c:forEach var="childReason" items="${reason.childEndReasons}">
                          <c:choose>
                            <c:when test="${childReason.id eq student.studyEndReason.id}">
                              <option value="${childReason.id}" data-approvalrequired="${reason.properties['studyApprovalRequired']}" selected="selected">${childReason.name}</option> 
                            </c:when>
                            <c:when test="${childReason.archived}">
                            </c:when>
                            <c:otherwise>
                              <option value="${childReason.id}" data-approvalrequired="${reason.properties['studyApprovalRequired']}">${childReason.name}</option> 
                            </c:otherwise>
                          </c:choose>
                        </c:forEach>
                      </optgroup>
                    </c:if>
                  </c:forEach>
                </select>
              </div>
  
              <div class="genericFormSection">      
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.studiesApprovedByTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.studiesApprovedByHelp"/>
                </jsp:include>

                <c:set var="studyApproverRequired"></c:set>
                <c:if test="${student.studyEndReason.properties['studyApprovalRequired'] eq '1'}">
                  <c:set var="studyApproverRequired">required</c:set>
                </c:if>
  
                <select id="studyApprover.${student.id}" name="studyApprover.${student.id}" class="${studyApproverRequired}">
                  <option></option>
                  <c:forEach var="studyApprover" items="${studyApprovers}">
                    <c:choose>
                      <c:when test="${studyApprover.title eq null}">
                        <c:set var="approverName">${studyApprover.lastName}, ${studyApprover.firstName}</c:set>
                      </c:when>
                      <c:otherwise>
                        <c:set var="approverName">${studyApprover.lastName}, ${studyApprover.firstName} (${studyApprover.title})</c:set>
                      </c:otherwise>
                    </c:choose>

                    <c:choose>
                      <c:when test="${studyApprover.id eq student.studyApprover.id}">
                        <option value="${studyApprover.id}" selected="selected">${approverName}</option> 
                      </c:when>
                      <c:otherwise>
                        <option value="${studyApprover.id}">${approverName}</option> 
                      </c:otherwise>
                    </c:choose>
                  </c:forEach>
                </select>
              </div>
  
              <div class="genericFormSection">  
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.studyEndTextTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.studyEndTextHelp"/>
                </jsp:include>
              
                <input type="text" name="studyEndText.${student.id}" size="50" value="${fn:escapeXml(student.studyEndText)}">
              </div>
              
              <div class="genericFormSection">    
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.lodgingTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.lodgingHelp"/>
                </jsp:include>

                <div class="genericTableAddRowContainer">
                  <span class="genericTableAddRowLinkContainer" onclick="addLodgingPeriodTableRow(getIxTableById('lodgingPeriodsTable.${student.id}'));"><fmt:message key="students.editStudent.addLodgingPeriodLink"/></span>
                </div>

                <div id="lodgingPeriodsTableContainer.${student.id}"></div>
              </div>
  
              <c:choose>
                <c:when test="${fn:length(variableKeys) gt 0}">
                  <div class="genericFormSection">  
                    <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                      <jsp:param name="titleLocale" value="students.editStudent.variablesTitle"/>
                      <jsp:param name="helpLocale" value="students.editStudent.variablesHelp"/>
                    </jsp:include>
                    <div id="variablesTableContainer.${student.id}"></div>
                  </div>
                </c:when>
              </c:choose>

              <div class="genericFormSection">  
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="students.editStudent.additionalInformationTitle"/>
                  <jsp:param name="helpLocale" value="students.editStudent.additionalInformationHelp"/>
                </jsp:include>
                <textarea name="additionalInfo.${student.id}" ix:cktoolbar="studentAdditionalInformation" ix:ckeditor="true">${student.additionalInfo}</textarea>
              </div>

              <ix:extensionHook name="students.editStudent.tabs.studyProgramme"/>
            </div>
          </c:forEach>

          <ix:extensionHook name="students.editStudent.tabs"/>

          <div class="genericFormSubmitSectionOffTab">
            <input type="submit" class="formvalid" value="<fmt:message key="students.editStudent.saveButton"/>">
          </div>

        </form>

      </div>
    </div>  

    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>