<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    <title>YO-ilmoittautumiset</title>

    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/searchnavigation_support.jsp"></jsp:include>
    
    <script type="text/javascript">
    
      function onLoad(event) {
        var currentYear = new Date().getFullYear();
        var termOptions = [];
        
        for (var y = 2016; y <= currentYear + 4; y++) {
          termOptions.push({value: "SPRING" + y, text: "Kevät " + y});
          termOptions.push({value: "AUTUMN" + y, text: "Syksy " + y});
        }

        var subjectOptions = [
          {value:"AI", text:"Äidinkieli"},
          {value:"S2", text:"Suomi toisena kielenä"},
          {value:"ENA", text:"Englanti, A-taso"},
          {value:"RAA", text:"Ranska, A-taso"},
          {value:"ESA", text:"Espanja, A-taso"},
          {value:"SAA", text:"Saksa, A-taso"},
          {value:"VEA", text:"Venäjä, A-taso"},
          {value:"UE", text:"Uskonto"},
          {value:"ET", text:"Elämänkatsomustieto"},
          {value:"YO", text:"Yhteiskuntaoppi"},
          {value:"KE", text:"Kemia"},
          {value:"GE", text:"Maantiede"},
          {value:"TT", text:"Terveystieto"},
          {value:"ENC", text:"Englanti, C-taso"},
          {value:"RAC", text:"Ranska, C-taso"},
          {value:"ESC", text:"Espanja, C-taso"},
          {value:"SAC", text:"Saksa, C-taso"},
          {value:"VEC", text:"Venäjä, C-taso"},
          {value:"ITC", text:"Italia, C-taso"},
          {value:"POC", text:"Portugali, C-taso"},
          {value:"LAC", text:"Latina, C-taso"},
          {value:"SM_DC", text:"Pohjoissaame, C-taso"},
          {value:"SM_ICC", text:"Inarinsaame, C-taso"},
          {value:"SM_QC", text:"Koltansaame, C-taso"},
          {value:"RUA", text:"Ruotsi, A-taso"},
          {value:"RUB", text:"Ruotsi, B-taso"},
          {value:"PS", text:"Psykologia"},
          {value:"FI", text:"Filosofia"},
          {value:"HI", text:"Historia"},
          {value:"FY", text:"Fysiikka"},
          {value:"BI", text:"Biologia"},
          {value:"MAA", text:"Matematiikka, pitkä"},
          {value:"MAB", text:"Matematiikka, lyhyt"}
        ];
        var mandatorityOptions = [
          {value: "", text: ""},
          {value: "MANDATORY", text: "Pakollinen"},
          {value: "OPTIONAL", text: "Valinnainen"}
        ];
        var repeatOptions = [
          {value: "FIRST_TIME", text: "Ensimmäinen suorituskerta"},
          {value: "REPEAT", text: "Uusinta"}
        ];
        var gradeOptions = [
          {value: "IMPROBATUR", text: "Improbatur"},
          {value: "APPROBATUR", text: "Approbatur"},
          {value: "LUBENTER_APPROBATUR", text: "Lubenter approbatur"},
          {value: "CUM_LAUDE_APPROBATUR", text: "Cum laude approbatur"},
          {value: "MAGNA_CUM_LAUDE_APPROBATUR", text: "Magna cum laude approbatur"},
          {value: "EXIMIA_CUM_LAUDE_APPROBATUR", text: "Eximia cum laude approbatur"},
          {value: "LAUDATUR", text: "Laudatur"},
          {value: "UNKNOWN", text: "Ei tiedossa"},
          {value: "NO_RIGHT_TO_PARTICIPATE", text: "Ei osallistumisoikeutta"},
          {value: "INVALIDATED", text: "Mitätöity"},
          {value: "K", text: "Keskeytynyt"}
        ];
        var fundingOptions = [
          {value: "", text: ""},
          {value: "SELF_FUNDED", text: "Omarahoitteinen"},
          {value: "COMPULSORYEDUCATION_FREE", text: "Oppivelvollinen, maksuton"},
          {value: "COMPULSORYEDUCATION_FREE_RETRY", text: "Oppivelvollinen, maksuton uusinta"}
        ];
        var tabControl = new IxProtoTabs($('tabs'));

        var enrolledAttendances = new IxTable($('enrolledAttendancesTableContainer'), {
          id: 'enrolledAttendances',
          columns : [{
            dataType: 'hidden',
            paramName: 'attendanceId'
          }, {
            header : 'Ajankohta',
            left: 0,
            width: 150,
            dataType : 'select',
            editable: false,
            paramName: 'term',
            options: termOptions
          }, {
            header : 'Aine',
            left: 0 + 150 + 8,
            width: 300,
            dataType : 'select',
            editable: false,
            paramName: 'subject',
            options: subjectOptions
          }, {
            header : 'Pakollisuus',
            width: 300,
            left: 0 + 150 + 8 + 300 + 8,
            dataType : 'select',
            editable: true,
            paramName: 'mandatority',
            options: mandatorityOptions
          }, {
            header : 'Uusiminen',
            width: 200,
            left: 0 + 150 + 8 + 300 + 8 + 300 + 8,
            dataType : 'select',
            editable: true,
            paramName: 'repeat',
            options: repeatOptions
          }, {
            header : 'Rahoitus',
            width: 200,
            left: 0 + 150 + 8 + 300 + 8 + 300 + 8 + 200 + 8,
            dataType : 'select',
            editable: true,
            paramName: 'funding',
            options: fundingOptions
          }, {
            header : 'Koepäivämäärä',
            width: 200,
            left: 0 + 150 + 8 + 300 + 8 + 300 + 8 + 200 + 8 + 200 + 8,
            dataType : 'date',
            editable: false,
            paramName: 'examDate'
          }, {
            width : 22,
            right : 8,
            dataType : 'button',
            imgsrc : GLOBAL_contextPath + '/gfx/edit-delete.png',
            tooltip : 'Poista',
            onclick : confirmAttendanceDeletionClick
          }]
        });
        enrolledAttendances.addRows(JSON.parse(JSDATA["enrolledAttendances"]));
        document.getElementById("addEnrolledTableRow").addEventListener('click', function() {
          var rowIndex = enrolledAttendances.addRow([-1, '', 'ENA', 'MANDATORY', 'FIRST_TIME', '', '', '']);
          var subjectColumn = enrolledAttendances.getNamedColumnIndex('subject');
          enrolledAttendances.setCellEditable(rowIndex, subjectColumn, true);
        });

        var finishedAttendances = new IxTable($('finishedAttendancesTableContainer'), {
          id: 'finishedAttendances',
          columns : [{
            dataType: 'hidden',
            paramName: 'attendanceId'
          }, {
            header : 'Ajankohta',
            left: 0,
            width: 150,
            dataType : 'select',
            editable: true,
            paramName: 'term',
            options: termOptions
          }, {
            header : 'Aine',
            width: 300,
            left: 0 + 150 + 8,
            dataType : 'select',
            editable: true,
            paramName: 'subject',
            options: subjectOptions
          }, {
            header : 'Pakollisuus',
            width: 200,
            left: 0 + 150 + 8 + 300 + 8,
            dataType : 'select',
            editable: true,
            paramName: 'mandatority',
            options: mandatorityOptions
          }, {
            header : 'Rahoitus',
            width: 200,
            left: 0 + 150 + 8 + 300 + 8 + 200 + 8,
            dataType : 'select',
            editable: true,
            paramName: 'funding',
            options: fundingOptions
          }, {
            header : 'Arvosana',
            width: 200,
            left: 0 + 150 + 8 + 300 + 8 + 200 + 8 + 200 + 8,
            dataType : 'select',
            editable: true,
            paramName: 'grade',
            options: gradeOptions
          }, {
            width : 22,
            right : 8,
            dataType : 'button',
            imgsrc : GLOBAL_contextPath + '/gfx/edit-delete.png',
            tooltip : 'Poista',
            onclick : confirmAttendanceDeletionClick
          }]
        });
        finishedAttendances.addRows(JSON.parse(JSDATA["finishedAttendances"]));
        document.getElementById("addFinishedTableRow").addEventListener('click', function() {
          finishedAttendances.addRow([-1, 'SPRING2019', 'ENA', 'MANDATORY', 'IMPROBATUR', '', '']);
        });

        var plannedAttendances = new IxTable($('plannedAttendancesTableContainer'), {
          id: 'plannedAttendances',
          columns : [{
            dataType: 'hidden',
            paramName: 'attendanceId'
          }, {
            header : 'Ajankohta',
            left: 0,
            width: 150,
            dataType : 'select',
            editable: true,
            paramName: 'term',
            options: termOptions
          }, {
            header : 'Aine',
            width: 300,
            left: 0 + 150 + 8,
            dataType : 'select',
            editable: true,
            paramName: 'subject',
            options: subjectOptions
          }, {
            header : 'Pakollisuus',
            width: 200,
            left: 0 + 150 + 8 + 300 + 8,
            dataType : 'select',
            editable: true,
            paramName: 'mandatority',
            options: mandatorityOptions
          }, {
            width : 22,
            right : 8,
            dataType : 'button',
            imgsrc : GLOBAL_contextPath + '/gfx/edit-delete.png',
            tooltip : 'Poista',
            onclick : confirmAttendanceDeletionClick
          }]
        });
        plannedAttendances.addRows(JSON.parse(JSDATA["plannedAttendances"]));
        document.getElementById("addPlannedTableRow").addEventListener('click', function() {
          plannedAttendances.addRow([-1, 'SPRING2019', 'ENA', 'MANDATORY', '']);
        });
      };

      function confirmAttendanceDeletionClick(event) {
        var table = event.tableComponent;
        var rowIndex = event.row;
        var attendanceId = table.getCellValue(event.row, table.getNamedColumnIndex('attendanceId'));

        if (attendanceId != -1) {
          var url = GLOBAL_contextPath + "/simpledialog.page?localeId=matriculation.editEnrollment.archiveAttendanceConfirmDialogContent";
          
          var dialog = new IxDialog({
            id : 'confirmRemoval',
            contentURL : url,
            centered : true,
            showOk : true,  
            showCancel : true,
            autoEvaluateSize: true,
            title : '<fmt:message key="generic.dialog.title.areyousure"/>',
            okLabel : '<fmt:message key="generic.dialog.remove"/>',
            cancelLabel : '<fmt:message key="generic.dialog.cancel"/>'
          });
        
          dialog.addDialogListener(function(event) {
            switch (event.name) {
              case 'okClick':
                JSONRequest.request("matriculation/archiveexamattendance.json", {
                  parameters: {
                    examAttendanceId: attendanceId
                  },
                  onSuccess: function (jsonResponse) {
                    table.deleteRow(rowIndex);
                  }
                });   
              break;
            }
          });
        
          dialog.open();
        } else {
          table.deleteRow(rowIndex);
        }
      }
    </script>

    <style type="text/css">
      .changeLogEntryRow {
        margin: 4px 0px;
      }
    </style>
  </head> 
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>

    <h1 class="genericPageHeader">
      Muokkaa YO-ilmoittautumisia
      <a href="${pageContext.request.contextPath}/students/viewstudent.page?person=${enrollment.student.person.id}" class="pyramusViewLink pyramusStudentLink">Siirry opiskelijan tietoihin</a>
    </h1>
    
    <div class="genericFormContainer">
      <div class="tabLabelsContainer" id="tabs">
        <a class="tabLabel" href="#enrollment">Ilmoittautuminen</a>
      </div>
      <div id="enrollment" class="tabContent">
        <form method="post">
          <div>
            <div class="genericViewInfoWapper">
              <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="matriculation.editEnrollment.handler"/>
                  <jsp:param name="helpLocale" value="matriculation.editEnrollment.handler.help"/>
                </jsp:include>
                <select name="handler" id="handlerSelect">
                  <option></option>
                  <c:forEach var="handlerStaffMember" items="${handlers}">
                    <option ${handlerStaffMember.id == enrollment.handler.id ? 'selected="selected"' : ''} value="${handlerStaffMember.id}">${fn:escapeXml(handlerStaffMember.lastName)}, ${fn:escapeXml(handlerStaffMember.firstName)}</option>
                  </c:forEach>
                </select> <button type="button" onclick="document.getElementById('handlerSelect').value = '${loggedUserId}';"><fmt:message key="matriculation.editEnrollment.setAsHandlerButtonLabel"/></button>
              </div>
              
              <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="matriculation.editEnrollment.handlerNotes"/>
                  <jsp:param name="helpLocale" value="matriculation.editEnrollment.handlerNotes.help"/>
                </jsp:include>
                <textarea name="handlerNotes" cols="80" rows="6">${fn:escapeXml(enrollment.handlerNotes)}</textarea>
              </div>
            </div>

            <div class="genericViewInfoWapper">
              <div class="genericFormSection">  
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="matriculation.editEnrollment.enrollmentDate"/>
                  <jsp:param name="helpLocale" value="matriculation.editEnrollment.enrollmentDate.help"/>
                </jsp:include>
                <div><fmt:formatDate type="both" value="${enrollmentDate}"/></div>
              </div>

              <div class="genericFormSection">  
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="matriculation.editEnrollment.changeLog.title"/>
                  <jsp:param name="helpLocale" value="matriculation.editEnrollment.changeLog.title.help"/>
                </jsp:include>
                
                <div>
                  <table>
                    <c:forEach var="changeLogEntry" items="${changeLog}">
                      <tr class="changeLogEntryRow">
                        <td align="right"><fmt:formatDate type="date" dateStyle="short" value="${changeLogEntry.timestamp}"/></td>
                        <td align="right"><fmt:formatDate type="time" timeStyle="short" value="${changeLogEntry.timestamp}"/></td>
                        <td>${changeLogEntry.modifier.fullName}</td>
                        <td><fmt:message key="generic.matriculation.changeLogTypes.${changeLogEntry.changeType}"/></td>
                        <td>
                          <c:if test="${changeLogEntry.newState != null}">
                            <span class="matriculationEnrollmentStateInline">
                              <fmt:message key="generic.matriculation.enrollmentStates.${changeLogEntry.newState}"/>
                            </span>
                          </c:if>
                          
                          <c:if test="${changeLogEntry.message != null}">
                            <span class="matriculationEnrollmentStateInline">
                              ${fn:escapeXml(changeLogEntry.message)}
                            </span>
                          </c:if>
                        </td>
                      </tr>
                    </c:forEach>
                  </table>
                </div>
              </div>

              <div class="genericFormSection">  
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="matriculation.editEnrollment.name"/>
                  <jsp:param name="helpLocale" value="matriculation.editEnrollment.name.help"/>
                </jsp:include>            
                ${fn:escapeXml(name)}
              </div>
  
              <div class="genericFormSection">  
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="matriculation.editEnrollment.email"/>
                  <jsp:param name="helpLocale" value="matriculation.editEnrollment.email.help"/>
                </jsp:include>            
                ${fn:escapeXml(email)}
              </div>
  
              <div class="genericFormSection">  
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="matriculation.editEnrollment.phone"/>
                  <jsp:param name="helpLocale" value="matriculation.editEnrollment.phone.help"/>
                </jsp:include>            
                ${fn:escapeXml(phone)}
              </div>
            </div>

            <div class="genericViewInfoWapper">
              <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="matriculation.editEnrollment.degreeStructure"/>
                  <jsp:param name="helpLocale" value="matriculation.editEnrollment.degreeStructure.help"/>
                </jsp:include>            
                <select class="required" name="degreeStructure">
                  <option></option>
                  <option ${degreeStructure=='PRE2022' ? 'selected="selected"' : ''} value="PRE2022"><fmt:message key="matriculation.editEnrollment.degreeStructure.PRE2022"/></option>
                  <option ${degreeStructure=='POST2022' ? 'selected="selected"' : ''} value="POST2022"><fmt:message key="matriculation.editEnrollment.degreeStructure.POST2022"/></option>
                </select>
              </div>

              <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="matriculation.editEnrollment.nationalStudentNumber"/>
                  <jsp:param name="helpLocale" value="matriculation.editEnrollment.nationalStudentNumber.help"/>
                </jsp:include>            
                <input type="text" name="nationalStudentNumber" value="${fn:escapeXml(nationalStudentNumber)}">
              </div>
  
              <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="matriculation.editEnrollment.guidanceCounselor"/>
                  <jsp:param name="helpLocale" value="matriculation.editEnrollment.guidanceCounselor.help"/>
                </jsp:include>            
                <input type="text" name="guidanceCounselor" value="${fn:escapeXml(guidanceCounselor)}" size="40"/>
              </div>
  
              <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="matriculation.editEnrollment.enrollAs"/>
                  <jsp:param name="helpLocale" value="matriculation.editEnrollment.enrollAs.help"/>
                </jsp:include>            
                <select class="required" name="enrollAs">
                  <option ${enrollAs=='UPPERSECONDARY' ? 'selected="selected"' : ''} value="UPPERSECONDARY">Lukio-opinnot</option>
                  <option ${enrollAs=='VOCATIONAL' ? 'selected="selected"' : ''} value="VOCATIONAL">Ammatilliset opinnot</option>
                  <option ${enrollAs=='UNKNOWN' ? 'selected="selected"' : ''} value="UNKNOWN">Muu tausta</option>
                </select>
              </div>
  
              <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="matriculation.editEnrollment.numMandatoryCourses"/>
                  <jsp:param name="helpLocale" value="matriculation.editEnrollment.numMandatoryCourses.help"/>
                </jsp:include>            
                <input type="number" name="numMandatoryCourses" value="${fn:escapeXml(numMandatoryCourses)}">
              </div>
  
              <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="matriculation.editEnrollment.restartExam"/>
                  <jsp:param name="helpLocale" value="matriculation.editEnrollment.restartExam.help"/>
                </jsp:include>            
                <input type="checkbox" name="restartExam" value="true" ${restartExam ? 'checked="checked"' : ''}/>
              </div>
  
              <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="matriculation.editEnrollment.degreeType"/>
                  <jsp:param name="helpLocale" value="matriculation.editEnrollment.degreeType.help"/>
                </jsp:include>            
                <select class="required" name="degreeType">
                  <option ${enrollment.degreeType=='MATRICULATIONEXAMINATION' ? 'selected="selected"' : ''} value="MATRICULATIONEXAMINATION">Yo-tutkinto</option>
                  <option ${enrollment.degreeType=='MATRICULATIONEXAMINATIONSUPPLEMENT' ? 'selected="selected"' : ''} value="MATRICULATIONEXAMINATIONSUPPLEMENT">Tutkinnon korottaja tai täydentäjä</option>
                  <option ${enrollment.degreeType=='SEPARATEEXAM' ? 'selected="selected"' : ''} value="SEPARATEEXAM">Erillinen koe (ilman yo-tutkintoa)</option>
                </select>
              </div>
  
              <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="matriculation.editEnrollment.location"/>
                  <jsp:param name="helpLocale" value="matriculation.editEnrollment.location.help"/>
                </jsp:include>            
                <input type="text" name="location" value="${fn:escapeXml(location)}">
              </div>
  
              <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="matriculation.editEnrollment.message"/>
                  <jsp:param name="helpLocale" value="matriculation.editEnrollment.message.help"/>
                </jsp:include>            
                <textarea name="message" cols="80" rows="8">${fn:escapeXml(message)}</textarea>
              </div>
  
              <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="matriculation.editEnrollment.canPublishName"/>
                  <jsp:param name="helpLocale" value="matriculation.editEnrollment.canPublishName.help"/>
                </jsp:include>            
                <select name="canPublishName">
                  <option ${enrollment.canPublishName ? 'selected="selected"' : ''} value="true">Haluan nimeni julkaistavan valmistujalistauksissa</option>
                  <option ${!enrollment.canPublishName ? 'selected="selected"' : ''} value="false">En halua nimeäni julkaistavan valmistujaislistauksissa</option>
                </select>
              </div>
            </div>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="matriculation.editEnrollment.enrolled"/>
              </jsp:include>            
              <div class="genericTableAddRowContainer">
                <span class="genericTableAddRowLinkContainer" id="addEnrolledTableRow"><fmt:message key="matriculation.editEnrollment.enrolled.addRow"/></span>
              </div>
              <div id="enrolledAttendancesTableContainer"></div>
            </div>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="matriculation.editEnrollment.finished"/>
              </jsp:include>            
              <div class="genericTableAddRowContainer">
                <span class="genericTableAddRowLinkContainer" id="addFinishedTableRow"><fmt:message key="matriculation.editEnrollment.finished.addRow"/></span>
              </div>
              <div id="finishedAttendancesTableContainer"></div>
            </div>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="matriculation.editEnrollment.planned"/>
              </jsp:include>            
              <div class="genericTableAddRowContainer">
                <span class="genericTableAddRowLinkContainer" id="addPlannedTableRow"><fmt:message key="matriculation.editEnrollment.planned.addRow"/></span>
              </div>
              <div id="plannedAttendancesTableContainer"></div>
            </div>
            
            <div class="genericViewInfoWapper">
              <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="matriculation.editEnrollment.state"/>
                  <jsp:param name="helpLocale" value="matriculation.editEnrollment.state.help"/>
                </jsp:include>            
                <select class="required" name="state">
                  <option ${state=='PENDING' ? 'selected="selected"' : ''} ${!allowedStates.contains('PENDING') ? 'disabled="disabled"' : ''} value="PENDING"><fmt:message key="generic.matriculation.enrollmentStates.PENDING"/></option>
                  <option ${state=='SUPPLEMENTATION_REQUEST' ? 'selected="selected"' : ''} ${!allowedStates.contains('SUPPLEMENTATION_REQUEST') ? 'disabled="disabled"' : ''} value="SUPPLEMENTATION_REQUEST"><fmt:message key="generic.matriculation.enrollmentStates.SUPPLEMENTATION_REQUEST"/></option>
                  <option ${state=='SUPPLEMENTED' ? 'selected="selected"' : ''} ${!allowedStates.contains('SUPPLEMENTED') ? 'disabled="disabled"' : ''} value="SUPPLEMENTED"><fmt:message key="generic.matriculation.enrollmentStates.SUPPLEMENTED"/></option>
                  <option ${state=='APPROVED' ? 'selected="selected"' : ''} ${!allowedStates.contains('APPROVED') ? 'disabled="disabled"' : ''} value="APPROVED"><fmt:message key="generic.matriculation.enrollmentStates.APPROVED"/></option>
                  <option ${state=='REJECTED' ? 'selected="selected"' : ''} ${!allowedStates.contains('REJECTED') ? 'disabled="disabled"' : ''} value="REJECTED"><fmt:message key="generic.matriculation.enrollmentStates.REJECTED"/></option>
                  <option ${state=='CONFIRMED' ? 'selected="selected"' : ''} ${!allowedStates.contains('CONFIRMED') ? 'disabled="disabled"' : ''} value="CONFIRMED"><fmt:message key="generic.matriculation.enrollmentStates.CONFIRMED"/></option>
                </select>
              </div>

              <div class="genericFormSection">
                <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                  <jsp:param name="titleLocale" value="matriculation.editEnrollment.changeLogMessage"/>
                  <jsp:param name="helpLocale" value="matriculation.editEnrollment.changeLogMessage.help"/>
                </jsp:include>
                <textarea name="changeLogMessage" cols="80" rows="4"></textarea>
              </div>
            </div>

            <button>Tallenna</button>
          </div>
        </form>
      </div>
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>