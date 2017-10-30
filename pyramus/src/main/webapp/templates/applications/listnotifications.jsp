<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title>Hakemusherätteet</title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/applications/listnotifications.js"></script>
    
  </head>
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
  
    <h1 class="genericPageHeader">Hakemusherätteet</h1>
  
    <div>
      <div class="genericFormContainer"> 
		    <div class="tabLabelsContainer" id="tabs">
		      <a class="tabLabel" href="#listNotifications">
		        <span class="tabLabelLeftTopCorner">
		          <span class="tabLabelRightTopCorner">Hakemusherätteet</span>
		        </span>
		      </a>
		    </div>
		    
		    <div id="listNotifications" class="tabContentixTableFormattedData">
          <div id="notificationListTableContainer"></div>
		    </div>
		  </div>
		</div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>