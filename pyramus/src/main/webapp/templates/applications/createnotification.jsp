<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    <title>Hakemusherätteen luonti</title>
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
  
    <h1 class="genericPageHeader">Hakemusherätteen luonti</h1>
  
    <div class="genericFormContainer"> 
      <div class="tabLabelsContainer" id="tabs">
        <a class="tabLabel" href="#basic">Perustiedot</a>
      </div>
      
      <form action="createnotification.json" method="post" ix:jsonform="true" ix:useglasspane="true">

        <div id="basic" class="tabContent">

          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleText" value="Linja"/>
            </jsp:include> 
            <select name="line">
              <option value="" selected="selected">Kaikki linjat</option>
              <option value="aineopiskelu">Aineopiskelu</option>
              <option value="nettilukio">Nettilukio</option>
              <option value="nettipk">Nettiperuskoulu</option>
              <option value="aikuislukio">Aikuislukio</option>
              <option value="bandilinja">Bändilinja</option>
              <option value="kasvatustieteet">Kasvatustieteen linja</option>
              <option value="laakislinja">Lääkislinja</option>
              <option value="mk">Maahanmuuttajakoulutukset</option>
            </select>
          </div>

          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleText" value="Tila"/>
            </jsp:include> 
            <select name="state">
              <option value="PENDING" selected="selected">Jätetty</option>
              <option value="PROCESSING">Käsittelyssä</option>
              <option value="WAITING_STAFF_SIGNATURE">Odottaa virallista hyväksyntää</option>
              <option value="STAFF_SIGNED">Hyväksyntä allekirjoitettu</option>
              <option value="APPROVED_BY_SCHOOL">Hyväksytty</option>
              <option value="APPROVED_BY_APPLICANT">Opiskelupaikka vastaanotettu</option>
              <option value="TRANSFERRED_AS_STUDENT">Siirretty opiskelijaksi</option>
              <option value="REGISTERED_AS_STUDENT">Rekisteröitynyt aineopiskelijaksi</option>
              <option value="REJECTED">Hylätty</option>
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