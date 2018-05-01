<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title><fmt:message key="settings.editOrganization.pageTitle"/></title>

    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/draftapi_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/ajax_support.jsp"></jsp:include>

    <script type="text/javascript">
      function editOrganizationFormSubmit(event) {
        Event.stop(event);
        
        var organizationId = document.querySelector("#organizationId").value;
        var organizationName = document.querySelector("#name").value;
        
        axios.put("/organizations/{0}".format(organizationId), {
          name: organizationName
        }).then(function (response) {
          location.reload();
        });
      }
  
      function onLoad(event) {
        var tabControl = new IxProtoTabs($('tabs'));
        
        var form = $("createOrganizationForm");
        Event.observe(form, "submit", editOrganizationFormSubmit);
      }
    </script>
  </head>

  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
  
    <h1 class="genericPageHeader"><fmt:message key="settings.editOrganization.pageTitle" /></h1>
  
    <div class="genericFormContainer"> 
      <div class="tabLabelsContainer" id="tabs">
        <a class="tabLabel" href="#basic">
          <fmt:message key="settings.createOrganization.tabLabelBasic"/>
        </a>
      </div>
      
      <form id="createOrganizationForm">
        <input id="organizationId" name="organizationId" type="hidden" value="${organization.id}"/>
        
        <div id="basic" class="tabContent">
          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="settings.editOrganization.nameTitle"/>
              <jsp:param name="helpLocale" value="settings.editOrganization.nameHelp"/>
            </jsp:include>
            <input id="name" type="text" name="name" class="required" size="40" value="${organization.name}"/>
          </div>
        </div>
  
        <div class="genericFormSubmitSectionOffTab">
          <input type="submit" class="formvalid" value="<fmt:message key="generic.form.saveButton"/>">
        </div>
      </form>
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>