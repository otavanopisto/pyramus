<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title><fmt:message key="system.layoutElementCheatSheet.pageTitle"/></title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
  
    <script type="text/javascript">
      function onLoad(event) {
        var tabControl = new IxProtoTabs($('tabs'));
      };
    </script>
  
  </head>
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
  
    <h1 class="genericPageHeader"><fmt:message key="system.layoutElementCheatSheet.pageTitle" /></h1>
  
    <div id="layoutElementCheatSheetContainer">
      <div class="genericFormContainer">
        <div class="tabLabelsContainer" id="tabs">
          <a class="tabLabel" href="#layoutElements">
            <fmt:message key="system.layoutElementCheatSheet.tabLabelLayoutElements"/>
          </a>

          <a class="tabLabel" href="#pageTemplate">
            <fmt:message key="system.layoutElementCheatSheet.tabLabelPageTemplates"/>
          </a>
        </div>
       
        <div id="layoutElements" class="tabContent">
          <h1 class="elementsCheatSheetHeader1">Tabs (TabLabel &amp; TabContent)</h1>
          <h3 class="elementsCheatSheetHeader2">TabLabel:</h3>
          <div class="elementsCheatSheetDescription">
<pre>
&lt;div class="tabLabelsContainer" id="tabs"&gt;

  &lt;a class="tabLabel" href="#firstTab"&gt;
    &lt;fmt:message key=""/&gt;
  &lt;/a&gt;
  
  &lt;a class="tabLabel" href="#secondTab"&gt;
    &lt;fmt:message key=""/&gt;
  &lt;/a&gt;
  
&lt;/div&gt;
</pre>
          </div>
          <h3 class="elementsCheatSheetHeader2">TabContent:</h3>
          <div class="elementsCheatSheetDescription">
<pre>
&lt;div id="firstTab" class="tabContent"&gt;
  &lt;!--
  <i>Tab Content:firstTab</i>
  --&gt;
&lt;/div&gt;

&lt;div id="secondTab" class="tabContent"&gt;
  &lt;!--
  <i>Tab Content:secondTab</i>
  --&gt;
&lt;/div&gt;
</pre>
          </div>
        </div>
       
        <div id="pageTemplate" class="tabContent">
       
        </div>
      
      
      
      </div>
    </div>
    
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>
