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
        var projectData = JSDATA["projectData"].evalJSON();
        var subjectData = JSDATA["subjectData"].evalJSON();

        var projects = [];
        for (var i = 0; i < projectData.length; i++) {
          projects.push({
            text: projectData[i].name,
            value: projectData[i].id
          });
        }
	  
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
            header : 'Projekti',
            left :  8 + 120 + 8,
            width: 200,
            dataType : 'select',
            editable: true,
            paramName: 'projectId',
            options: projects
          }, {
            header : 'Koepäivämäärä',
            left: 8 + 120 + 8 + 200 + 8, 
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
            subjectData[i].projectId,
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
              <jsp:param name="titleLocale" value="matriculation.settings.signupGradeTitle"/>
            </jsp:include>
            <select name="signupGradeId">
              <option value=""></option>
            
              <c:forEach var="gradingScale" items="${gradingScales}">
                <optgroup label="${fn:escapeXml(gradingScale.name)}">
                  <c:forEach var="grade" items="${gradingScale.grades}">
                    <c:choose>
                      <c:when test="${exam.signupGrade.id eq grade.id}">
                        <option value="${grade.id}" selected="selected">${fn:escapeXml(grade.name)}</option>
                      </c:when>
                      <c:otherwise>
                        <option value="${grade.id}">${fn:escapeXml(grade.name)}</option>
                      </c:otherwise>
                    </c:choose>
                  </c:forEach>
                </optgroup>
              </c:forEach>
            </select>
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