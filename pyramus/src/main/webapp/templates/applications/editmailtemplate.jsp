<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    <title>Sähköpostipohjan muokkaus</title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/draftapi_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/ckeditor_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/hovermenu_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>
    <script type="text/javascript">
      function onLoad(event) {
        var tabControl = new IxProtoTabs($('tabs'));
      }
    </script>
  </head> 
  <body onload="onLoad(event);" ix:enabledrafting="true">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
  
    <h1 class="genericPageHeader">Sähköpostipohjan muokkaus</h1>
  
    <div class="genericFormContainer"> 
      <div class="tabLabelsContainer" id="tabs">
        <a class="tabLabel" href="#basic">Perustiedot</a>
      </div>
      
      <form action="editmailtemplate.json" method="post" ix:jsonform="true" ix:useglasspane="true">

        <input type="hidden" name="templateId" value="${template.id}"></input>
  
        <div id="basic" class="tabContent">

          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleText" value="Tekijä"/>
            </jsp:include> 
            <span><i>${fn:escapeXml(template.staffMember.fullName)}</i></span>
          </div>

          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleText" value="Nimi"/>
            </jsp:include> 
            <input type="text" name="name" class="required" size="40" value="${fn:escapeXml(template.name)}"/>
          </div>

          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleText" value="Linja"/>
            </jsp:include> 
            <select name="line">
              <option value="" <c:if test="${template.line == ''}">selected="selected"</c:if>></option>
              <option value="aineopiskelu" <c:if test="${template.line == 'aineopiskelu'}">selected="selected"</c:if>>Aineopiskelu</option>
              <option value="nettilukio" <c:if test="${template.line == 'nettilukio'}">selected="selected"</c:if>>Nettilukio</option>
              <option value="nettipk" <c:if test="${template.line == 'nettipk'}">selected="selected"</c:if>>Nettiperuskoulu</option>
              <option value="aikuislukio" <c:if test="${template.line == 'aikuislukio'}">selected="selected"</c:if>>Aikuislukio</option>
              <!-- <option value="bandilinja" <c:if test="${template.line == 'bandilinja'}">selected="selected"</c:if>>Bändilinja</option>
              <option value="kasvatustieteet" <c:if test="${template.line == 'kasvatustieteet'}">selected="selected"</c:if>>Kasvatustieteen linja</option>
              <option value="laakislinja" <c:if test="${template.line == 'laakislinja'}">selected="selected"</c:if>>Lääkislinja</option> -->
              <option value="mk" <c:if test="${template.line == 'mk'}">selected="selected"</c:if>>Maahanmuuttajakoulutukset</option>
            </select>
          </div>

          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleText" value="Aihe"/>
            </jsp:include> 
            <input type="text" name="subject" size="40" value="${fn:escapeXml(template.subject)}"/>
          </div>

          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleText" value="Sisältö"/>
            </jsp:include> 
            <textarea ix:cktoolbar="applicationMailTemplateContent" name="content" ix:ckeditor="true">${template.content}</textarea>
          </div>

        </div>

        <div class="genericFormSubmitSectionOffTab">
          <input type="submit" class="formvalid" value="Tallenna">
        </div>
      </form>
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>  

</html>