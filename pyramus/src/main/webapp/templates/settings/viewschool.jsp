<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title><fmt:message key="settings.viewSchool.pageTitle"/></title>

    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>

    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/settings/viewschool.js">
    </script>
    
    <style type="text/css">
      .billingDetailsRow label {
        display: inline-block;
        min-width: 150px;
        vertical-align: top;
        margin: 3px 0 0 0;
      }
      .billingDetailsRow span {
        width: 300px;
      }
    </style>
    
  </head>
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
  
    <h1 class="genericPageHeader">
      <fmt:message key="settings.viewSchool.pageTitle" />
      <a href="editschool.page?school=${school.id}"><img src="${pageContext.request.contextPath}/gfx/accessories-text-editor.png"></a>
    </h1>
    
  
    <div class="genericFormContainer"> 
      <div class="tabLabelsContainer" id="tabs">
        <a class="tabLabel" href="#basic">
          <fmt:message key="settings.viewSchool.tabLabelBasic"/>
        </a>
      </div>
      
      <input type="hidden" name="schoolId" value="${school.id}"></input>

      <div id="basic" class="tabContent">
        <div class="genericFormSection">
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="settings.viewSchool.codeTitle"/>
            <jsp:param name="helpLocale" value="settings.viewSchool.codeHelp"/>
          </jsp:include>
          ${fn:escapeXml(school.code)}
        </div>
        <div class="genericFormSection">
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="settings.viewSchool.nameTitle"/>
            <jsp:param name="helpLocale" value="settings.viewSchool.nameHelp"/>
          </jsp:include>
          ${fn:escapeXml(school.name)}
        </div>
        <div class="genericFormSection">
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="settings.viewSchool.fieldTitle"/>
            <jsp:param name="helpLocale" value="settings.viewSchool.fieldHelp"/>
          </jsp:include>
          ${fn:escapeXml(school.field.name)}
        </div>
        <div class="genericFormSection">
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="settings.viewSchool.tagsTitle"/>
            <jsp:param name="helpLocale" value="settings.viewSchool.tagsHelp"/>
          </jsp:include>
          ${fn:escapeXml(tags)}
        </div>
        <div class="genericFormSection">  
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="settings.viewSchool.variablesTitle"/>
            <jsp:param name="helpLocale" value="settings.viewSchool.variablesHelp"/>
          </jsp:include>
          <div id="variablesTable"></div>
        </div>
        <div class="genericFormSection">  
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="settings.viewSchool.addressesTitle"/>
            <jsp:param name="helpLocale" value="settings.viewSchool.addressesHelp"/>
          </jsp:include>
          <div id="addressTable"></div>
        </div>
        <div class="genericFormSection">  
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="settings.viewSchool.emailsTitle"/>
            <jsp:param name="helpLocale" value="settings.viewSchool.emailsHelp"/>
          </jsp:include>
          <div id="emailTable"></div>
        </div>
        <div class="genericFormSection">  
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="settings.viewSchool.phoneNumbersTitle"/>
            <jsp:param name="helpLocale" value="settings.viewSchool.phoneNumbersHelp"/>
          </jsp:include>
          <div id="phoneNumbersTable"></div>
        </div>
        <div class="genericFormSection">  
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="settings.viewSchool.studentGroupTitle"/>
            <jsp:param name="helpLocale" value="settings.viewSchool.studentGroupHelp"/>
          </jsp:include>
          ${fn:escapeXml(school.studentGroup.name)}
        </div>
        <div class="genericFormSection">
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="settings.viewSchool.billingDetailsTitle"/>
            <jsp:param name="helpLocale" value="settings.viewSchool.billingDetailsHelp"/>
          </jsp:include>
          
          <div>
            <div class="billingDetailsRow">
              <label for="billing-details-personName"><fmt:message key="billingDetails.personName"/></label>
              <span>${school.billingDetails.personName}</span>
            </div>
            
            <div class="billingDetailsRow">
              <label for="billing-details-companyName"><fmt:message key="billingDetails.companyName"/></label>
              <span>${school.billingDetails.companyName}</span>
            </div>
              
            <div class="billingDetailsRow">
              <label for="billing-details-streetAddress1"><fmt:message key="billingDetails.streetAddress1"/></label>
              <span>${school.billingDetails.streetAddress1}</span>
            </div>
            
            <div class="billingDetailsRow">
              <label for="billing-details-streetAddress2"><fmt:message key="billingDetails.streetAddress2"/></label>
              <span>${school.billingDetails.streetAddress2}</span>
            </div>
            
            <div class="billingDetailsRow">
              <label for="billing-details-postalCode"><fmt:message key="billingDetails.postalCode"/></label>
              <span>${school.billingDetails.postalCode}</span>
            </div>
            
            <div class="billingDetailsRow">
              <label for="billing-details-city"><fmt:message key="billingDetails.city"/></label>
              <span>${school.billingDetails.city}</span>
            </div>
            
            <div class="billingDetailsRow">
              <label for="billing-details-region"><fmt:message key="billingDetails.region"/></label>
              <span>${school.billingDetails.region}</span>
            </div>
              
            <div class="billingDetailsRow">
              <label for="billing-details-country"><fmt:message key="billingDetails.country"/></label>
              <span>${school.billingDetails.country}</span>
            </div>
            
            <div class="billingDetailsRow">
              <label for="billing-details-phoneNumber"><fmt:message key="billingDetails.phoneNumber"/></label>
              <span>${school.billingDetails.phoneNumber}</span>
            </div>
            
            <div class="billingDetailsRow">
              <label for="billing-details-emailAddress"><fmt:message key="billingDetails.emailAddress"/></label>
              <span>${school.billingDetails.emailAddress}</span>
            </div>
              
            <div class="billingDetailsRow">
              <label for="billing-details-companyIdentifier"><fmt:message key="billingDetails.companyIdentifier"/></label>
              <span>${school.billingDetails.companyIdentifier}</span>
            </div>
            
            <div class="billingDetailsRow">
              <label for="billing-details-referenceNumber"><fmt:message key="billingDetails.referenceNumber"/></label>
              <span>${school.billingDetails.referenceNumber}</span>
            </div>
            
            <div class="billingDetailsRow">
              <label for="billing-details-electronicBillingAddress"><fmt:message key="billingDetails.electronicBillingAddress"/></label>
              <span>${school.billingDetails.electronicBillingAddress}</span>
            </div>
            
            <div class="billingDetailsRow">
              <label for="billing-details-notes"><fmt:message key="billingDetails.notes"/></label>
              <span>${school.billingDetails.notes}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>