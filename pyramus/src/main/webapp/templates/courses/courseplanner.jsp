<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title>
      <fmt:message key="courses.coursePlanner.pageTitle"/>
    </title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/datefield_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/hovermenu_support.jsp"></jsp:include>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/courseplanner.css"/>      
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/courses/courseplanner.js"></script>
    
    <script type="text/javascript">
      function setEducationSubtypeFilter(coursePlanner, educationSubtypes) {
        coursePlanner.clearFilters();
        coursePlanner.addFilter(new CoursePlannerEducationSubtypeFilter(educationSubtypes));
        coursePlanner.arrageCoursesToTracks();
      }
      
      function applySettings(coursePlanner) {
        var selectedSubtypes = new Array(); 
        
        var educationSubtypes = $$('input[name="educationSubtype"]');
        for (var i = 0, l = educationSubtypes.length; i < l; i++) {
          if (educationSubtypes[i].checked) {
            selectedSubtypes.push(educationSubtypes[i].value);
          } 
        }
        
        setEducationSubtypeFilter(coursePlanner, selectedSubtypes);
      }
    
      function onLoad(event) {
        var tabControl = new IxProtoTabs($('tabs'));
        
        var coursePlanner = new CoursePlanner($('coursePlannerContainer'), {
          showMonthLines: true,
          showWeekLines: true,
          showDayLines: true,
          showMonthLabels: true,
          showWeekLabels: true,
          showDayLabels: false,
          showYearLabel: true,
          showPointerLabel: true,
          courseHeight: 50,
          trackHeight: 60,
          trackSpacing: 4,
          initialTimeFrame: 1000 * 60 * 60 * 24 * 30 * 6,
          onYearLabelClick: function(event) {
            var from = new Date();
            from.setFullYear(event.year, 0, 1);
            from.setHours(0, 0, 0, 0);
            var to = new Date();
            to.setFullYear(event.year + 1, 0, 0);
            to.setHours(0, 0, 0, 0);
            this.setDateRange(from, to);
          }, 
          onMonthLabelClick: function(event) {
            var from = new Date();
            from.setFullYear(event.year, event.month, 1);
            from.setHours(0, 0, 0, 0);
            var to = new Date();
            to.setFullYear(event.year, event.month + 1, 0);
            to.setHours(0, 0, 0, 0);
            this.setDateRange(from, to);
          }
        });
        
        Event.observe($('coursePlannerSettingsHandle'), "click", function (event) {
          if ($('coursePlannerSettingsHandle')._expanded == true) {
            $('coursePlannerSettingsHandle').update('&lt;&lt;');
            new Effect.Morph($('coursePlannerSettingsContainer'), {
              style: 'width:20px',
              duration: 0.8 
            });
            $('coursePlannerSettingsHandle')._expanded = false;
          } else {
            $('coursePlannerSettingsHandle').update('&gt;&gt;');
            new Effect.Morph($('coursePlannerSettingsContainer'), {
              style: 'width:800px',
              duration: 0.8 
            });
            $('coursePlannerSettingsHandle')._expanded = true;
          }
        });
        
        Event.observe($('settingsApply'), "click", function (event) {
          applySettings(coursePlanner);
        });
        
        $('coursePlannerSettingsContainer').setStyle({
          width: '20px'
        }); 
        
        var courses = new Array();
        var course;
        <c:forEach var="courseBean" items="${courseBeans}">
          <c:if test="${courseBean.course.beginDate.time ne courseBean.course.endDate.time}">
	          course = new CoursePlannerCourse({
	            name : '${fn:escapeXml(courseBean.courseName)}', 
	            courseStartDate : new Date(${courseBean.course.beginDate.time}),
	            courseEndDate : new Date(${courseBean.course.endDate.time}), 
	            track: -1,
	            resizable: false
	          });
	          
	          course.setEducationTypes(${courseBean.educationTypes});
	          course.setEducationSubtypes(${courseBean.educationSubtypes});
	          courses.push(course);
	        </c:if>
	      </c:forEach>
	      
	      coursePlanner.addCourses(courses, false);
	      applySettings(coursePlanner);
      };
    </script>
  </head>
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    <h1 class="genericPageHeader">
      <fmt:message key="courses.coursePlanner.pageTitle"/>
    </h1>
  
    <div class="genericFormContainer">
      <div class="tabLabelsContainer" id="tabs">
        <a class="tabLabel" href="#planner"><fmt:message key="courses.coursePlanner.plannerTabTitle" /></a>
      </div>
        
      <div id="planner" class="tabContent">
        <div id="coursePlannerContainer">
          <div id="coursePlannerSettingsContainer">
            <div id="coursePlannerSettingsBackground"></div>
            <div id="coursePlannerSettingsHandle"> &lt;&lt; </div>
            <div id="coursePlannerSettingsContent">
	            <h2>
	              <fmt:message key="courses.coursePlanner.plannerSettingsTitle" />
					    </h2>
					     
	            <div class="genericFormSection">
	              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
	                <jsp:param name="titleLocale" value="courses.coursePlanner.plannerEducationTypeFilterTitle"/>
	                <jsp:param name="helpLocale" value="courses.coursePlanner.plannerEducationTypeFilterHelp"/>
	              </jsp:include>
	              <div class="coursePlannerEducationTypesFilter">
	                <c:forEach var="educationType" items="${educationTypes}">
	                  <div class="coursePlannerEducationTypeFilter">
	                    <div class="coursePlannerEducationTypeFilterTitleContainer">
	                      <div class="coursePlannerEducationTypeFilterTitle">${educationType.name}</div>
	                    </div>
	                    <c:forEach var="educationSubtype" items="${educationSubtypes[educationType.id]}">
	                      <input type="checkbox" value="${educationSubtype.id}" name="educationSubtype" checked="checked"/> <span>${educationSubtype.name}</span> <br/>
	                    </c:forEach>
	                  </div>
	                </c:forEach>
	              </div>
	            </div>
	            
	            <div class="genericFormSubmitSection">
	              <input type="submit" id="settingsApply" value="<fmt:message key="courses.coursePlanner.plannerSettingsApply"/>">
	            </div>
	          </div>
          </div>
        </div>
      </div>
    </div>
  
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>