<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title>Sähköpostin allekirjoitus</title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/ckeditor_support.jsp"></jsp:include>
    <script type="text/javascript">
      function onLoad(event) {
        var tabControl = new IxProtoTabs($('tabs'));
      }
    </script>
  </head>
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    <h1 class="genericPageHeader">Sähköpostin allekirjoitus</h1>
    <div class="genericFormContainer"> 
      <div class="tabLabelsContainer" id="tabs">
        <a class="tabLabel" href="#basic">Allekirjoitus</a>
      </div>
      <form action="createemailsignature.page" method="post">
        <div id="basic" class="tabContent">
          <div class="genericFormSection">
            <textarea name="signature" ix:cktoolbar="studentAdditionalInformation" ix:ckeditor="true">${signature}</textarea>
          </div>
        </div>
        <div class="genericFormSubmitSectionOffTab">
          <input type="submit" value="Tallenna">
        </div>
      </form>
    </div>
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>