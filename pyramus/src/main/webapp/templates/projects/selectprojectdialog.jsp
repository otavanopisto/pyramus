<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/searchnavigation_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/studentinfopopup_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/ckeditor_support.jsp"></jsp:include>
    
    <script type="text/javascript">
      /**
       * Returns the selected project id
       *
       * @return The students selected in this dialog
       */
      function getResults() {
        var selectedProjectId = $('selectedProject').value;
        var optionality = $('optionality').value;
        
        return {
          selectedProjectId: selectedProjectId,
          optionality: optionality
        };
      }
      
      function onLoad(event) {
      }
    </script>
  </head>

  <body onload="onLoad(event);">
    <div id="selectProjectDialogContainer" class="selectProjectDialogContainer">
      <div class="selectProjectEditorContainer">

        <div class="genericFormSection">
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="projects.selectProjectDialog.projectHeader"/>
            <jsp:param name="helpLocale" value="projects.selectProjectDialog.projectHeaderHelp"/>
          </jsp:include>

          <select name="selectedProject" id="selectedProject">
            <c:forEach var="project" items="${projects}">
              <option value="${project.id}">${project.name}</option>
            </c:forEach>
          </select>
        </div>

        <div class="genericFormSection">
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="projects.selectProjectDialog.optionalityHeader"/>
            <jsp:param name="helpLocale" value="projects.selectProjectDialog.optionalityHeaderHelp"/>
          </jsp:include>
          <select name="optionality" id="optionality">
            <option></option>
            <option value="MANDATORY"><fmt:message key="projects.selectProjectDialog.optionalityMandatory"/></option>
            <option value="OPTIONAL"><fmt:message key="projects.selectProjectDialog.optionalityOptional"/></option>
          </select>
        </div>
        
      </div>
    </div>
  </body>
</html>