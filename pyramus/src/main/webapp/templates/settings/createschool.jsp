<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title><fmt:message key="settings.createSchool.pageTitle"/></title>

    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/draftapi_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>

    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/settings/createschool.js">
    </script>
    
    <style type="text/css">
      .billingDetailsRow label {
        display: inline-block;
        min-width: 150px;
        vertical-align: top;
        margin: 3px 0 0 0;
      }
      .billingDetailsRow input[type="text"] {
        width: 300px;
      }
      .billingDetailsRow textarea {
        width: 300px;
        min-height: 4em;
      }
      input[type="email"] {
        width: 300px;
        border-radius: 3px;
        font-family: "trebuchet ms";
        height: 17px;
        max-width: 100%;
        padding: 1px 0 2px;
        border: 1px solid #739de7;
      }      
    </style>
  </head>
  <body onload="onLoad(event);" ix:enabledrafting="true">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
  
    <h1 class="genericPageHeader"><fmt:message key="settings.createSchool.pageTitle" /></h1>
  
    <div class="genericFormContainer"> 
      <div class="tabLabelsContainer" id="tabs">
        <a class="tabLabel" href="#basic">
          <fmt:message key="settings.createSchool.tabLabelBasic"/>
        </a>
      </div>
      
      <form action="createschool.json" method="post" ix:jsonform="true" ix:useglasspane="true">
        <div id="basic" class="tabContent">

        <div class="genericFormSection">
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="settings.createSchool.codeTitle"/>
            <jsp:param name="helpLocale" value="settings.createSchool.codeHelp"/>
          </jsp:include>           
          <input type="text" name="code" class="required" size="20"/>
        </div>

        <div class="genericFormSection">
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="settings.createSchool.nameTitle"/>
            <jsp:param name="helpLocale" value="settings.createSchool.nameHelp"/>
          </jsp:include>           
          <input type="text" name="name" class="required" size="40"/>
        </div>
        
        <div class="genericFormSection">
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="settings.createSchool.fieldTitle"/>
            <jsp:param name="helpLocale" value="settings.createSchool.fieldHelp"/>
          </jsp:include> 

          <select name="schoolFieldId">
            <option value="-1"></option>
            <c:forEach var="field" items="${schoolFields}">
              <option value="${field.id}">${fn:escapeXml(field.name)}</option>
            </c:forEach>
          </select>
        </div>

        <div class="genericFormSection">
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="settings.createSchool.tagsTitle"/>
            <jsp:param name="helpLocale" value="settings.createSchool.tagsHelp"/>
          </jsp:include>
          <input type="text" id="tags" name="tags" size="40"/>
          <div id="tags_choices" class="autocomplete_choices"></div>
        </div>

        <div class="genericFormSection">  
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="settings.createSchool.addressesTitle"/>
            <jsp:param name="helpLocale" value="settings.createSchool.addressesHelp"/>
          </jsp:include>                                         
          <div id="addressTable"></div>
        </div>

        <div class="genericFormSection">  
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="settings.createSchool.emailTableEmailsTitle"/>
            <jsp:param name="helpLocale" value="settings.createSchool.emailTableEmailsHelp"/>
          </jsp:include>                                         
          <div id="emailTable"></div>
        </div>

        <div class="genericFormSection">  
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="settings.createSchool.phoneNumbersTitle"/>
            <jsp:param name="helpLocale" value="settings.createSchool.phoneNumbersHelp"/>
          </jsp:include>                                         
          <div id="phoneTable"></div>
        </div>

        <div class="genericFormSection">
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="settings.createSchool.billingDetailsTitle"/>
            <jsp:param name="helpLocale" value="settings.createSchool.billingDetailsHelp"/>
          </jsp:include>
          
          <div>
            <div class="billingDetailsRow">
              <label for="billing-details-personName"><fmt:message key="billingDetails.personName"/></label>
              <input type="text" name="billingDetailsPersonName" value=""/>
            </div>
            
            <div class="billingDetailsRow">
              <label for="billing-details-companyName"><fmt:message key="billingDetails.companyName"/></label>
              <input type="text" name="billingDetailsCompanyName" value=""/>
            </div>
              
            <div class="billingDetailsRow">
              <label for="billing-details-streetAddress1"><fmt:message key="billingDetails.streetAddress1"/></label>
              <input type="text" name="billingDetailsStreetAddress1" value=""/>
            </div>
            
            <div class="billingDetailsRow">
              <label for="billing-details-streetAddress2"><fmt:message key="billingDetails.streetAddress2"/></label>
              <input type="text" name="billingDetailsStreetAddress2" value=""/>
            </div>
            
            <div class="billingDetailsRow">
              <label for="billing-details-postalCode"><fmt:message key="billingDetails.postalCode"/></label>
              <input type="text" name="billingDetailsPostalCode" value=""/>
            </div>
            
            <div class="billingDetailsRow">
              <label for="billing-details-city"><fmt:message key="billingDetails.city"/></label>
              <input type="text" name="billingDetailsCity" value=""/>
            </div>
            
            <div class="billingDetailsRow">
              <label for="billing-details-region"><fmt:message key="billingDetails.region"/></label>
              <input type="text" name="billingDetailsRegion" value=""/>
            </div>
              
            <div class="billingDetailsRow">
              <label for="billing-details-country"><fmt:message key="billingDetails.country"/></label>
              <input type="text" name="billingDetailsCountry" value=""/>
            </div>
            
            <div class="billingDetailsRow">
              <label for="billing-details-phoneNumber"><fmt:message key="billingDetails.phoneNumber"/></label>
              <input type="text" name="billingDetailsPhoneNumber" value=""/>
            </div>
            
            <div class="billingDetailsRow">
              <label for="billing-details-emailAddress"><fmt:message key="billingDetails.emailAddress"/></label>
              <input type="email" name="billingDetailsEmailAddress" value=""/>
            </div>
              
            <div class="billingDetailsRow">
              <label for="billing-details-companyIdentifier"><fmt:message key="billingDetails.companyIdentifier"/></label>
              <input type="text" name="billingDetailsCompanyIdentifier" value=""/>
            </div>
            
            <div class="billingDetailsRow">
              <label for="billing-details-referenceNumber"><fmt:message key="billingDetails.referenceNumber"/></label>
              <input type="text" name="billingDetailsReferenceNumber" value=""/>
            </div>
            
            <div class="billingDetailsRow">
              <label for="billing-details-electronicBillingAddress"><fmt:message key="billingDetails.electronicBillingAddress"/></label>
              <textarea name="billingDetailsElectronicBillingAddress"></textarea>
            </div>
            
            <div class="billingDetailsRow">
              <label for="billing-details-notes"><fmt:message key="billingDetails.notes"/></label>
              <textarea name="billingDetailsNotes"></textarea>
            </div>
          </div>
        </div>

        <div class="genericFormSection">  
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="settings.createSchool.variablesTitle"/>
            <jsp:param name="helpLocale" value="settings.createSchool.variablesHelp"/>
          </jsp:include>           
          <div id="variablesTable"></div>
        </div>

      </div>
      <div class="genericFormSubmitSectionOffTab">
        <input type="submit" class="formvalid" value="<fmt:message key="settings.createSchool.saveButton"/>">
      </div>
    </form>
  </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>