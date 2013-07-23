<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
	<head>
	  <title><fmt:message key="system.systemInfo.pageTitle"/></title>
	  <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
	  <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
	  <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
	  
	  <script type="text/javascript">
	    
	    function onLoad(event) {
	      var tabControl = new IxProtoTabs($('tabs'));
	    }
	
	  </script>
    
  </head>
  <body onload="onLoad(event)">
	  <jsp:include page="/templates/generic/header.jsp"></jsp:include>
	  
	  <h1 class="genericPageHeader"><fmt:message key="system.systemInfo.pageTitle" /></h1>
	  
      <div class="genericFormContainer"> 
        <div class="tabLabelsContainer" id="tabs">
          <a class="tabLabel" href="#general">
            <fmt:message key="system.systemInfo.tabLabelGeneral"/>
          </a>

          <a class="tabLabel" href="#properties">
            <fmt:message key="system.systemInfo.tabLabelProperties"/>
          </a>

          <a class="tabLabel" href="#environment">
            <fmt:message key="system.systemInfo.tabLabelEnvironment"/>
          </a>
        </div>
        
        <div id="general" class="tabContent">
          <div>Time: ${date}</div>
          <div>Free Memory: ${freeMemory}</div>
          <div>Total Memory: ${totalMemory}</div>
          <div>Available Processors: ${availableProcessors}</div>
        </div>

        <div id="properties" class="tabContent">
			    <table>
			      <tr>
			        <td>key</td>
			        <td>value</td>
			      </tr>

            <c:forEach var="prop" items="${properties}">            
              <tr>
                <td>${prop.key}</td>
                <td>${prop.value}</td>
              </tr>
            </c:forEach>
			    </table>
				</div>

        <div id="environment" class="tabContent">
          <table>
            <tr>
              <td>key</td>
              <td>value</td>
            </tr>

            <c:forEach var="envItem" items="${env}">            
              <tr>
                <td>${envItem.key}</td>
                <td>${envItem.value}</td>
              </tr>
            </c:forEach>
          </table>
        </div>
          
      </div>    
	  
	  <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>