<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/ix" prefix="ix"%>

<!-- Header starts -->

<div id="GUI_headerContainer">

<ix:extensionHook name="generic.header.javascript"/>

  <div id="GUI_headerRepeat">
    <div id="GUI_headerLeft">
      <div id="GUI_headerLogo"></div>
      <div id="GUI_headerRight"></div>
    </div>
  </div>
  <div id="GUI_headerNavigationBackground">
  
  </div>
  <div id="GUI_headerNavigationContainer">
    <ul id="GUI_MENU_indexContainer">
      <li id="GUI_MENU_indexLink">
        <a href="${pageContext.request.contextPath}/index.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.index"/></a>
      </li>
    </ul>
  
    <ul id="GUI_MENU_studentsContainer">
      <li id="GUI_MENU_studentsLink">
        <fmt:message key="generic.navigation.students"/>
        <ul id="GUI_MENU_studentsItemContainer" class="GUI_MENU_itemContainer">
          <li class="GUI_MENU_top"></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/students/createstudent.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.createStudent"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/students/searchstudents.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.searchStudents"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/students/createstudentgroup.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.createStudentGroup"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/students/searchstudentgroups.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.searchStudentGroups"/></a></li>
          <ix:extensionHook name="generic.navigation.studentsMenu"/>
        </ul>
      </li>
    </ul>

    <ul id="GUI_MENU_modulesContainer">
      <li id="GUI_MENU_modulesLink">
        <fmt:message key="generic.navigation.modules"/>
        <ul id="GUI_MENU_modulesItemContainer" class="GUI_MENU_itemContainer">
          <li class="GUI_MENU_top"></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/modules/createmodule.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.createModule"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/modules/searchmodules.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.searchModules"/></a></li>
          <ix:extensionHook name="generic.navigation.modulesMenu"/>
        </ul>
      </li>
    </ul>

    <ul id="GUI_MENU_coursesContainer">
      <li id="GUI_MENU_coursesLink">
        <fmt:message key="generic.navigation.courses"/>
        <ul id="GUI_MENU_coursesItemContainer" class="GUI_MENU_itemContainer">
          <li class="GUI_MENU_top"></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/courses/createcoursewizard.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.createCourse"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/courses/searchcourses.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.searchCourses"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/courses/courseplanner.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.coursePlanner"/></a></li>
          <ix:extensionHook name="generic.navigation.coursesMenu"/>
        </ul>
      </li>
    </ul>
    
    <ul id="GUI_MENU_projectsContainer">
      <li id="GUI_MENU_projectsLink">
        <fmt:message key="generic.navigation.projects"/>
        <ul id="GUI_MENU_projectsItemContainer" class="GUI_MENU_itemContainer">
          <li class="GUI_MENU_top"></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/projects/createproject.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.createProject"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/projects/searchprojects.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.searchProjects"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/projects/createstudentproject.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.createStudentProject"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/projects/searchstudentprojects.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.searchStudentProjects"/></a></li>
          <ix:extensionHook name="generic.navigation.projectsMenu"/>
        </ul>
      </li>
    </ul>

    <ul id="GUI_MENU_resourcesContainer">
      <li id="GUI_MENU_resourcesLink">
        <fmt:message key="generic.navigation.resources"/>
        <ul id="GUI_MENU_resourcesItemContainer" class="GUI_MENU_itemContainer">
          <li class="GUI_MENU_top"></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/resources/resourcecategories.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.manageResourceCategories"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/resources/creatematerialresource.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.createMaterialResource"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/resources/createworkresource.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.createWorkResource"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/resources/searchresources.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.searchResources"/></a></li>
          <ix:extensionHook name="generic.navigation.resourcesMenu"/>
        </ul>
      </li>
    </ul>

    <ul id="GUI_MENU_reportsContainer">
      <li id="GUI_MENU_reportsLink">
        <fmt:message key="generic.navigation.reports"/>
        <ul id="GUI_MENU_reportsItemContainer" class="GUI_MENU_itemContainer">
          <li class="GUI_MENU_top"></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/reports/listreports.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.listReports"/></a></li>
          <ix:extensionHook name="generic.navigation.reportsMenu"/>
        </ul>
      </li>
    </ul>

    <ul id="GUI_MENU_applicationsContainer">
      <li id="GUI_MENU_applicationsLink">
        <fmt:message key="generic.navigation.applications"/>
        <ul id="GUI_MENU_applicationsItemContainer" class="GUI_MENU_itemContainer">
          <li class="GUI_MENU_top"></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/applications/browse.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.applications.browse"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/applications/createnotification.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.applications.createNotification"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/applications/listnotifications.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.applications.manageNotifications"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/applications/createmailtemplate.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.applications.createMailTemplate"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/applications/listmailtemplates.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.applications.manageMailTemplates"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/users/createemailsignature.page" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.createEmailSignature"/></a></li>
          <ix:extensionHook name="generic.navigation.applicationsMenu"/>
        </ul>
      </li>
    </ul>

    <ul id="GUI_MENU_matriculationContainer">
      <li id="GUI_MENU_matriculationLink">
        <fmt:message key="generic.navigation.matriculation"/>
        <ul id="GUI_MENU_matriculationItemContainer" class="GUI_MENU_itemContainer">
          <li class="GUI_MENU_top"></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/matriculation/browse.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.matriculation.browse"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/matriculation/settings.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.matriculation.settings"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/matriculation/ytljson.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.matriculation.ytljson"/></a></li>
          <ix:extensionHook name="generic.navigation.matriculationMenu"/>
        </ul>
      </li>
    </ul>
    
    <ul id="GUI_MENU_settingsContainer">
      <li id="GUI_MENU_settingsLink">
        <fmt:message key="generic.navigation.settings"/>
        <ul id="GUI_MENU_settingsItemContainer" class="GUI_MENU_itemContainer">
          <li class="GUI_MENU_top"></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/users/createuser.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.createUser"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/users/searchusers.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.searchUsers"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/settings/createschool.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.createSchool"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/settings/searchschools.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.searchSchools"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/settings/creategradingscale.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.createGradingScale"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/settings/listgradingscales.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.listGradingScales"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/settings/educationtypes.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.manageEducationTypes"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/settings/educationsubtypes.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.manageEducationSubtypes"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/settings/subjects.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.manageSubjects"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/settings/academicterms.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.manageAcademicTerms"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/settings/studyprogrammes.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.manageStudyProgrammes"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/settings/studyprogrammecategories.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.manageStudyProgrammeCategories"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/settings/reportcategories.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.manageReportCategories"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/settings/municipalities.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.manageMunicipalities"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/settings/timeunits.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.manageTimeUnits"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/settings/coursestates.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.manageCourseStates"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/settings/coursetypes.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.manageCourseTypes"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/settings/managetransfercredittemplates.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.manageTransferCreditTemplates"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/settings/courseparticipationtypes.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.manageCourseParticipationTypes"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/settings/manageschoolfields.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.manageSchoolFields"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/settings/managechangelog.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.manageChangeLog"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/settings/coursedescriptioncategories.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.manageCourseDescriptionCategories"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/settings/managefiletypes.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.manageFileTypes"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/settings/studyendreasons.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.studyEndReasons"/></a></li>
          <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/settings/curriculums.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.manageCurriculums"/></a></li>
          <c:if test="${loggedUserRole == 'ADMINISTRATOR'}">
            <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/settings/courseuserroles.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.manageCourseUserRoles"/></a></li>
          </c:if>
          <ix:extensionHook name="generic.navigation.settingsMenu"/>
        </ul>
      </li>
    </ul>
    
    <c:if test="${loggedUserRole == 'ADMINISTRATOR'}">
      <ul id="GUI_MENU_systemContainer">
        <li id="GUI_MENU_systemLink">
          <fmt:message key="generic.navigation.system"/>
          <ul id="GUI_MENU_systemItemContainer" class="GUI_MENU_itemContainer">
            <li class="GUI_MENU_top"></li>
            <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/system/systemsettings.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.manageSystemSettings"/></a></li>
            <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/system/plugins.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.plugins"/></a></li>
            <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/system/initialdata.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.importInitialData"/></a></li>
            <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/system/importcsv.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.importCSV"/></a></li>
            <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/system/importdata.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.importData"/></a></li>
            <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/system/importscripteddata.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.importScriptedData"/></a></li>
            <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/system/importreport.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.importReport"/></a></li>
            <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/system/reindexhibernateobjects.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.reindexHibernateEntities"/></a></li>
            <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/system/elementcheatsheet.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.layoutElementsCheatSheet"/></a></li>
            <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/system/clientapplications.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.clientApplications"/></a></li>
            <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/system/managepermissions.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.managePermissions"/></a></li>
            <li class="GUI_MENU_item"><a href="${pageContext.request.contextPath}/system/importexportpermissions.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.importexportPermissions"/></a></li>
            <ix:extensionHook name="generic.navigation.systemMenu"/>
          </ul>
        </li>
      </ul>
    </c:if>
    
    <ul id="GUI_MENU_helpContainer">
      <li id="GUI_MENU_helpLink">
        <a href="${pageContext.request.contextPath}/help/viewhelp.page?resetbreadcrumb=1" class="GUI_MENU_itemLink"><fmt:message key="generic.navigation.help"/></a>
      </li>
    </ul>
  </div>
	<div id="GUI_headerLoginInformationContainer">   
	  <c:choose>
	    <c:when test="${loggedUserId != null}">
	       <span id="GUI_headerLoggedInAs"><fmt:message key="generic.loggedInAs"/></span><a href="${pageContext.request.contextPath}/users/edituser.page?userId=${loggedUserId}" class="GUI_headerEditUserLink">${loggedUserName}</a>           
	       <a href="${pageContext.request.contextPath}/users/logout.page" class="GUI_headerLogoutLoginLink"><fmt:message key="generic.logoutLink"/></a>
	    </c:when>
	    <c:otherwise>
	      <span class="loginLinkContainer"><a href="${pageContext.request.contextPath}/users/login.page" class="GUI_headerLogoutLoginLink"><fmt:message key="generic.loginLink"/></a></span>
	    </c:otherwise>
	  </c:choose>
	</div>
	<div id="GUI_headerLocaleSelectionContainer">
	  <a href="#" class="GUI_headerLocaleSelectionLink" onclick="setLocale('fi_FI');">FI</a><a href="#" class="GUI_headerLocaleSelectionLink" onclick="setLocale('en_US');">EN</a>
	</div>
</div>
<!-- Header ends -->

<!--  Breadcrumbs -->
<div id="breadcrumbsContainer">
<jsp:include page="/templates/generic/breadcrumbs.jsp"></jsp:include>
</div>
<!-- Content starts -->

<div id="GUI_contentContainer">
