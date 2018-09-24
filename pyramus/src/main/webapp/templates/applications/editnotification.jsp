<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    <title>Hakemusherätteen muokkaus</title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/draftapi_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/ckeditor_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/hovermenu_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/applications/application-notifications.js"></script>
  </head> 
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
  
    <h1 class="genericPageHeader">Hakemusherätteen muokkaus</h1>
  
    <div class="genericFormContainer"> 
      <div class="tabLabelsContainer" id="tabs">
        <a class="tabLabel" href="#basic">Perustiedot</a>
      </div>
      
      <form action="editnotification.json" method="post" ix:jsonform="true" ix:useglasspane="true">

        <input type="hidden" name="notificationId" value="${notification.id}"/>
        
        <div id="basic" class="tabContent">

          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleText" value="Linja"/>
            </jsp:include> 
            <select name="line">
              <option value="" <c:if test="${notification.line == ''}">selected="selected"</c:if>>Kaikki linjat</option>
              <option value="aineopiskelu" <c:if test="${notification.line == 'aineopiskelu'}">selected="selected"</c:if>>Aineopiskelu</option>
              <option value="nettilukio" <c:if test="${notification.line == 'nettilukio'}">selected="selected"</c:if>>Nettilukio</option>
              <option value="nettipk" <c:if test="${notification.line == 'nettipk'}">selected="selected"</c:if>>Nettiperuskoulu</option>
              <option value="aikuislukio" <c:if test="${notification.line == 'aikuislukio'}">selected="selected"</c:if>>Aikuislukio</option>
              <!-- <option value="bandilinja" <c:if test="${notification.line == 'bandilinja'}">selected="selected"</c:if>>Bändilinja</option>
              <option value="kasvatustieteet" <c:if test="${notification.line == 'kasvatustieteet'}">selected="selected"</c:if>>Kasvatustieteen linja</option>
              <option value="laakislinja" <c:if test="${notification.line == 'laakislinja'}">selected="selected"</c:if>>Lääkislinja</option> -->
              <option value="mk" <c:if test="${notification.line == 'mk'}">selected="selected"</c:if>>Maahanmuuttajakoulutukset</option>
            </select>
          </div>

          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleText" value="Tila"/>
            </jsp:include> 
            <select name="state">
              <option value="PENDING" <c:if test="${notification.state == 'PENDING'}">selected="selected"</c:if>>Jätetty</option>
              <option value="PROCESSING" <c:if test="${notification.state == 'PROCESSING'}">selected="selected"</c:if>>Käsittelyssä</option>
              <option value="WAITING_STAFF_SIGNATURE" <c:if test="${notification.state == 'WAITING_STAFF_SIGNATURE'}">selected="selected"</c:if>>Odottaa virallista hyväksyntää</option>
              <option value="STAFF_SIGNED" <c:if test="${notification.state == 'STAFF_SIGNED'}">selected="selected"</c:if>>Hyväksyntä allekirjoitettu</option>
              <option value="APPROVED_BY_SCHOOL" <c:if test="${notification.state == 'APPROVED_BY_SCHOOL'}">selected="selected"</c:if>>Hyväksytty</option>
              <option value="APPROVED_BY_APPLICANT" <c:if test="${notification.state == 'APPROVED_BY_APPLICANT'}">selected="selected"</c:if>>Opiskelupaikka vastaanotettu</option>
              <option value="TRANSFERRED_AS_STUDENT" <c:if test="${notification.state == 'TRANSFERRED_AS_STUDENT'}">selected="selected"</c:if>>Siirretty opiskelijaksi</option>
              <option value="REGISTERED_AS_STUDENT" <c:if test="${notification.state == 'REGISTERED_AS_STUDENT'}">selected="selected"</c:if>>Rekisteröitynyt aineopiskelijaksi</option>
              <option value="REGISTRATION_CHECKED" <c:if test="${notification.state == 'REGISTRATION_CHECKED'}">selected="selected"</c:if>>Tiedot tarkistettu</option>
              <option value="REJECTED" <c:if test="${notification.state == 'REJECTED'}">selected="selected"</c:if>>Hylätty</option>
            </select>
          </div>

          <div class="genericTableAddRowContainer">
            <span class="genericTableAddRowLinkContainer" onclick="openSearchUsersDialog();">Lisää henkilö</span>
          </div>
          <div id="usersTable"> </div>

        </div>

        <div class="genericFormSubmitSectionOffTab">
          <input type="submit" class="formvalid" value="Tallenna">
        </div>
      </form>
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>  

</html>