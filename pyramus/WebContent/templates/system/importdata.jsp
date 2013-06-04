<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
	<head>
	  <title><fmt:message key="system.importdata.pageTitle"/></title>
	  <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <script type="text/javascript">
      function onLoad(event) {
        var tabControl = new IxProtoTabs($('tabs'));
      }
    </script>
  </head>
  <body onload="onLoad(event)">
	  <jsp:include page="/templates/generic/header.jsp"></jsp:include>
	  <h1 class="genericPageHeader"><fmt:message key="system.importdata.pageTitle" /></h1>
	  
	  <div id="importDataImportFormContainer"> 
      <div class="genericFormContainer"> 
        <div class="tabLabelsContainer" id="tabs">
          <a class="tabLabel" href="#importData">
            <fmt:message key="system.importdata.tabLabelImportData"/>
          </a>
        </div>
        
        <div id="importData" class="tabContent">
    	  <form action="importdata.page" enctype="multipart/form-data" method="post">
    	    <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="system.importdata.fileTitle"/>
                <jsp:param name="helpLocale" value="system.importdata.fileHelp"/>
              </jsp:include>          
              <input type="file" name="file">
            </div>
    	      
    	    <div class="genericFormSubmitSection">
    	      <input type="submit" value="<fmt:message key="system.importdata.importButton"/>">
    	    </div>
    	  </form>
    	</div>
      </div>
	</div>
	  
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>