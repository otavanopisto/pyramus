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
    <jsp:include page="/templates/generic/searchnavigation_support.jsp"></jsp:include>
    
    <script type="text/javascript">
      function onLoad(event) {
      };
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
						<input type="text" name="starts" class="ixDateField" value="${starts}"/>
					</div>
					<div class="genericFormSection">  
						<jsp:include page="/templates/generic/fragments/formtitle.jsp">
							<jsp:param name="titleLocale" value="matriculation.settings.endDate"/>
						</jsp:include>                                           
						<input type="text" name="ends" class="ixDateField" value="${ends}"/>
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