<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    <title>Vaihda käsittelijää</title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/draftapi_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/ckeditor_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/hovermenu_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/applications/changehandler.js"></script>
  </head> 
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
  
    <h1 class="genericPageHeader">Vaihda käsittelijää</h1>
  
    <div class="genericFormContainer"> 
      <div class="tabLabelsContainer" id="tabs">
        <a class="tabLabel" href="#basic">Vaihda käsittelijää</a>
      </div>
      
      <form action="changehandler.json" method="post" ix:jsonform="true" ix:useglasspane="true">

        <div id="basic" class="tabContent">

          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleText" value="Keneltä"/>
            </jsp:include> 
            <select id="sourceStaffMember" name="sourceStaffMember">
              <c:forEach var="staffMember" items="${staffMembers}">
                <option value="${staffMember.id}">${staffMember.lastName}, ${staffMember.firstName}</option>
              </c:forEach>
            </select>
            <input id="showSourceStats" type="button" value="Näytä käsittelystatistiikka">
          </div>

          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleText" value="Kenelle"/>
            </jsp:include> 
            <select id="targetStaffMember" name="targetStaffMember">
              <c:forEach var="staffMember" items="${staffMembers}">
                <option value="${staffMember.id}">${staffMember.lastName}, ${staffMember.firstName}</option>
              </c:forEach>
            </select>
            <input id="showTargetStats" type="button" value="Näytä käsittelystatistiikka">
          </div>

        </div>

        <div class="genericFormSubmitSectionOffTab">
          <input type="submit" class="formvalid" value="Vaihda">
        </div>
      </form>
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>  

</html>