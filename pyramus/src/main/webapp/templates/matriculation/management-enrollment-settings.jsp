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
    
    <script type="text/javascript">
      function onLoad(event) {
        var tabControl = new IxProtoTabs($('tabs'));
        setupSubjectSettingsTable();
      };

      function setupSubjectSettingsTable() {
        var subjectData = JSDATA["subjectData"].evalJSON();

        var subjectSettingsTable = new IxTable($('subjectSettingsTableContainer'), {
          id: 'subjectSettingsTable',
          rowHoverEffect: true,
          columns : [{
            header : 'Aine',
            left: 8,
            width: 120,
            dataType: 'text',
            editable: false,
            paramName: 'subjectCode'
          }, {
            header : 'Koepäivämäärä',
            left: 8 + 120 + 8, 
            width: 150,
            dataType: 'date',
            editable: true,
            paramName: 'examDate'
          }]
        });

        var rows = [];

        for (var i = 0; i < subjectData.length; i++) {
          rows.push([
            subjectData[i].subjectCode,
            subjectData[i].examDate,
          ]);
        }
        
        subjectSettingsTable.addRows(rows);
        
        return subjectSettingsTable;
      }
    </script>
    
  </head> 
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader">YO-ilmoittautumisten asetukset</h1>
    
    <div class="genericFormContainer">
      <div class="tabLabelsContainer" id="tabs">
        <a class="tabLabel" href="#settings">Ilmoittautuminen</a>
      </div>
      <div id="settings" class="tabContent">
        <form method="post">
          <input type="hidden" name="examId" value="${examId}" />
        
          <div class="genericFormSection">  
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="matriculation.settings.examYear"/>
            </jsp:include>                                           
            <input type="number" name="examYear" value="${exam.examYear}"/>
          </div>

          <div class="genericFormSection">  
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="matriculation.settings.examTerm"/>
            </jsp:include>
            <select name="examTerm">
              <option value=""></option>
              <option value="SPRING" ${exam.examTerm=='SPRING' ? 'selected="selected"' : ''}><fmt:message key="terms.seasons.spring"/></option>
              <option value="AUTUMN" ${exam.examTerm=='AUTUMN' ? 'selected="selected"' : ''}><fmt:message key="terms.seasons.autumn"/></option>
            </select>
          </div>

          <div class="genericFormSection">  
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="matriculation.settings.enrollmentActive"/>
            </jsp:include>
            <select name="enrollmentActive">
              <option value="true" ${exam.enrollmentActive == true ? 'selected="selected"' : ''}><fmt:message key="terms.yes"/></option>
              <option value="false" ${exam.enrollmentActive != true ? 'selected="selected"' : ''}><fmt:message key="terms.no"/></option>
            </select>
          </div>
          
          <div class="genericFormSection">  
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="matriculation.settings.startDate"/>
            </jsp:include>                                           
            <input type="text" name="starts" class="ixDateField" value="${exam.starts.time}"/>
          </div>

          <div class="genericFormSection">  
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="matriculation.settings.endDate"/>
            </jsp:include>                                           
            <input type="text" name="ends" class="ixDateField" value="${exam.ends.time}"/>
          </div>

          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="matriculation.settings.confirmationDate"/>
              <jsp:param name="helpLocale" value="matriculation.settings.confirmationDateHelp"/>
            </jsp:include>
            <input type="text" name="confirmationDate" class="ixDateField" value="${exam.confirmationDate.time}"/>
          </div>

          <div class="genericFormSection">  
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="matriculation.settings.subjectList"/>
            </jsp:include>                                           
            <div id="subjectSettingsTableContainer"></div>
          </div>
          
          <div class="genericFormSubmitSection">
            <input type="submit" value="Tallenna">
          </div>
        </form>
      </div>
    </div>
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>