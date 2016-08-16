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
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>
    
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/courses/searchmodulesdialog.js">
    </script>

  </head>
  <body onload="onLoad(event);">

    <div id="changeModuleDialogSearchContainer">
      <div class="modalSearchTabLabel"><fmt:message key="modules.changeCourseModuleDialog.searchTitle"/></div> 
      <div class="modalSearchTabContent">
	    <div class="genericFormContainer"> 
          <form id="searchModulesForm" method="post" onsubmit="onSearchModules(event);">
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="courses.changeCourseModuleDialog.currentModuleTitle"/>
                <jsp:param name="helpLocale" value="courses.changeCourseModuleDialog.currentModuleHelp"/>
              </jsp:include>
              <span><i>${course.module.name}</i></span>    
            </div>
          
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="modules.changeCourseModuleDialog.nameTitle"/>
                <jsp:param name="helpLocale" value="modules.changeCourseModuleDialog.nameHelp"/>
              </jsp:include>          
              <input type="text" name="name" size="45"/>
            </div>
          
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="modules.changeCourseModuleDialog.moduleSubjectTitle"/>
                <jsp:param name="helpLocale" value="modules.changeCourseModuleDialog.moduleSubjectHelp"/>
              </jsp:include>          
              <div class="searchModulesModuleSubjectContainer">
                <select name="subject">
                  <option></option>
                  <c:forEach var="educationType" items="${educationTypes}">
                    <c:if test="${subjectsByEducationType[educationType.id] ne null}">
                      <optgroup label="${educationType.name}">
                        <c:forEach var="subject" items="${subjectsByEducationType[educationType.id]}">
                          <c:choose>
                            <c:when test="${empty subject.code}">
                              <c:set var="subjectName">${subject.name}</c:set>
                            </c:when>
                            <c:otherwise>
                              <c:set var="subjectName">
                                <fmt:message key="generic.subjectFormatterNoEducationType">
                                  <fmt:param value="${subject.code}"/>
                                  <fmt:param value="${subject.name}"/>
                                </fmt:message>
                              </c:set>
                            </c:otherwise>
                          </c:choose>
  
                          <option value="${subject.id}">${subjectName}</option> 
                        </c:forEach>
                      </optgroup>
                    </c:if>
                  </c:forEach>
  
                  <c:forEach var="subject" items="${subjectsByNoEducationType}">
                    <c:choose>
                      <c:when test="${empty subject.code}">
                        <c:set var="subjectName">${subject.name}</c:set>
                      </c:when>
                      <c:otherwise>
                        <c:set var="subjectName">
                          <fmt:message key="generic.subjectFormatterNoEducationType">
                            <fmt:param value="${subject.code}"/>
                            <fmt:param value="${subject.name}"/>
                          </fmt:message>
                        </c:set>
                      </c:otherwise>
                    </c:choose>
  
                    <option value="${subject.id}">${subjectName}</option> 
                  </c:forEach>
                </select>
              </div>
            </div>
      
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="modules.changeCourseModuleDialog.moduleCurriculumTitle"/>
                <jsp:param name="helpLocale" value="modules.changeCourseModuleDialog.moduleCurriculumHelp"/>
              </jsp:include>          
              <div class="searchModulesModuleCurriculumContainer">
                <select name="curriculum">
                  <option></option>
                  <c:forEach var="curriculum" items="${curriculums}">
                    <option value="${curriculum.id}">${curriculum.name}</option>
                  </c:forEach>
                </select>
              </div>
            </div>
      
	          <div class="genericFormSubmitSection">
	            <input type="submit" value="<fmt:message key="modules.changeCourseModuleDialog.searchButton"/>"/>
	          </div>
	    
	        </form>
	      </div>
      </div>
      
      <div id="moduleSearchResultsTableContainer" class="modalSearchResultsContainer modalChangeCourseModuleResultsContainer">
        <div class="modalSearchResultsTabLabel"><fmt:message key="modules.changeCourseModuleDialog.searchResultsTitle"/></div>
        <div id="modalSearchResultsStatusMessageContainer" class="modalSearchResultsMessageContainer"></div>    
        <div id="searchResultsTableContainer" class="modalSearchResultsTabContent"></div>
        <div id="modalSearchResultsPagesContainer" class="modalSearchResultsPagesContainer"></div>
      </div>
    </div>

  </body>
</html>