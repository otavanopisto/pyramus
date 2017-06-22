<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <link href="${pageContext.request.contextPath}/css/studentdetailsdialog.css" rel="stylesheet"></link>
    <script type="text/javascript">
      function getResults() {
        var form = $('studentDetailsForm');
        
        return {
          organization: form.organization.value,
          additionalInfo: form.additionalInfo.value,
          roomId: form.roomId.value,
          roomAdditionalInfo: form.roomAdditionalInfo.value,
          lodgingFee: form.lodgingFee.value,
          lodgingFeeCurrency: form.lodgingFeeCurrency.value,
          reservationFee: form.reservationFee.value,
          reservationFeeCurrency: form.reservationFeeCurrency.value,
          billingDetailsId: form.billingDetailsId.value,
          billingDetailsPersonName: form.billingDetailsPersonName.value,
          billingDetailsCompanyName: form.billingDetailsCompanyName.value,
          billingDetailsStreetAddress1: form.billingDetailsStreetAddress1.value,
          billingDetailsStreetAddress2: form.billingDetailsStreetAddress2.value,
          billingDetailsPostalCode: form.billingDetailsPostalCode.value,
          billingDetailsCity: form.billingDetailsCity.value,
          billingDetailsRegion: form.billingDetailsRegion.value,
          billingDetailsCountry: form.billingDetailsCountry.value,
          billingDetailsPhoneNumber: form.billingDetailsPhoneNumber.value,
          billingDetailsEmailAddress: form.billingDetailsEmailAddress.value,
          billingDetailsCompanyIdentifier: form.billingDetailsCompanyIdentifier.value,
          billingDetailsReferenceNumber: form.billingDetailsReferenceNumber.value,
          billingDetailsElectronicBillingAddress: form.billingDetailsElectronicBillingAddress.value,
          billingDetailsElectronicBillingOperator: form.billingDetailsElectronicBillingOperator.value,
          billingDetailsNotes: form.billingDetailsNotes.value
        };
      }

      function getBillingDetailsText() {
        var form = $('studentDetailsForm');

        var personName = form.billingDetailsPersonName.value;
        var companyName = form.billingDetailsCompanyName.value;
        var streetAddress1 = form.billingDetailsStreetAddress1.value;
        var postalCode = form.billingDetailsPostalCode.value;
        var city = form.billingDetailsCity.value;
        var country = form.billingDetailsCountry.value;

        var result = '';
        if (personName) {
          result += personName;
        }
        
        if (companyName) {
          if (result) {
            result += ' / ';
          }
          
          result += companyName;
        }
        
        if (streetAddress1) {
          if (result) {
            result += ", ";
          }
          
          result += streetAddress1;
        }
        
        if (postalCode) {
          if (result) {
            result += ' ';
          }
          
          result += postalCode;
        }
        
        if (city) {
          if (result) {
            result += ' ';
          }
          
          result += city;
        }
        
        if (country) {
          if (result) {
            result += ', ';
          }
          
          result += country;
        }

        if (!result) {
          result = getLocale().getText("courses.studentDetailsDialog.newBillingDetails")
        }
        
        return result;
      }
      
      document.observe("dom:loaded", function() {
        $('billingDetailsId').observe("change", function (event) {
          if ($(this).value) {
            $('new-billing-details-container').hide();
          } else {
            $('new-billing-details-container').show();
          }        
        });

        $$('#new-billing-details-container input').each(function(input) {
          $(input).observe("change", function (event) {
            $('billingDetailsId').options[0].text = getBillingDetailsText();
          });
        });

        $('billingDetailsId').options[0].text = getBillingDetailsText();
      });
    </script>
  </head>
  <body>
    <form id="studentDetailsForm">
      
      <div class="genericFormSection">
        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
          <jsp:param name="titleLocale" value="courses.studentDetails.roomTitle"/>
          <jsp:param name="helpLocale" value="courses.studentDetails.roomHelp"/>
        </jsp:include>
        
        <select name="roomId">
          <c:choose>
            <c:when test="${courseStudent.room eq null}">
              <option selected="selected"><fmt:message key="courses.studentDetails.noRoom"/></option>
            </c:when>
            <c:otherwise>
              <option><fmt:message key="courses.studentDetails.noRoom"/></option>
            </c:otherwise>
          </c:choose>
          
          <c:forEach var="entry" items="${rooms}">
            <optgroup label="${entry.key.name}">
              <c:forEach var="room" items="${entry.value}">
                <c:choose>
                  <c:when test="${room.id eq courseStudent.room.id}">
                    <option value="${room.id}" selected="selected">${room.name}</option>
                  </c:when>
                  <c:otherwise>
                    <option value="${room.id}">${room.name}</option>
                  </c:otherwise>
                </c:choose>
              </c:forEach>
            </optgroup>    
          </c:forEach>
        </select>
      </div>
      
      <div class="genericFormSection">
        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
          <jsp:param name="titleLocale" value="courses.studentDetails.roomAdditionalInfoTitle"/>
          <jsp:param name="helpLocale" value="courses.studentDetails.roomAdditionalInfoHelp"/>
        </jsp:include>
        
        <input type="text" name="roomAdditionalInfo" value="${courseStudent.roomAdditionalInfo}"/>
      </div>  
      
      <div class="genericFormSection">
        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
          <jsp:param name="titleLocale" value="courses.studentDetails.lodgingFeeTitle"/>
          <jsp:param name="helpLocale" value="courses.studentDetails.lodgingFeeHelp"/>
        </jsp:include>
        
        <input type="text" name="lodgingFee" value="${courseStudent.lodgingFee}"/>
        <select name="lodgingFeeCurrency">
          <c:forEach var="currency" items="${currencies}">
            <option value="${currency.currencyCode}">${currency.currencyCode}</option>
          </c:forEach>
        </select>
      </div> 
      
      <div class="genericFormSection">
        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
          <jsp:param name="titleLocale" value="courses.studentDetails.reservationFeeTitle"/>
          <jsp:param name="helpLocale" value="courses.studentDetails.reservationFeeHelp"/>
        </jsp:include>
        
        <input type="text" name="reservationFee" value="${courseStudent.reservationFee}"/>
        <select name="reservationFeeCurrency">
          <c:forEach var="currency" items="${currencies}">
            <option value="${currency.currencyCode}">${currency.currencyCode}</option>
          </c:forEach>
        </select>
      </div> 
      
      <div class="genericFormSection">
        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
          <jsp:param name="titleLocale" value="courses.studentDetails.organizationTitle"/>
          <jsp:param name="helpLocale" value="courses.studentDetails.organizationHelp"/>
        </jsp:include>
        
        <input type="text" name="organization" value="${courseStudent.organization}"/>
      </div>    
      
      <div class="genericFormSection">
        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
          <jsp:param name="titleLocale" value="courses.studentDetails.additionalInfoTitle"/>
          <jsp:param name="helpLocale" value="courses.studentDetails.additionalInfoHelp"/>
        </jsp:include>
        
        <input type="text" name="additionalInfo" value="${courseStudent.additionalInfo}"/>
      </div>  
      
      <div class="genericFormSection">
        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
          <jsp:param name="titleLocale" value="courses.studentDetails.billingDetailsTitle"/>
          <jsp:param name="helpLocale" value="courses.studentDetails.billingDetailsHelp"/>
        </jsp:include>
        
        <select id="billingDetailsId" name="billingDetailsId">
          <option value=""></option>
          <c:forEach var="existing" items="${existingBillingDetails}">
            <option value="${existing.id}">${existing.toLine()}</option>
          </c:forEach>
        </select>
        
        <div id="new-billing-details-container">
          <div class="billingDetailsRow">
            <label for="billing-details-personName"><fmt:message key="courses.studentDetails.billingDetails.personName"/></label>
            <input id="billing-details-personName" type="text" name="billingDetailsPersonName" value="${courseStudent.billingDetails.personName}"/>
          </div>
          
          <div class="billingDetailsRow">
            <label for="billing-details-companyName"><fmt:message key="courses.studentDetails.billingDetails.companyName"/></label>
            <input id="billing-details-companyName" type="text" name="billingDetailsCompanyName" value="${courseStudent.billingDetails.companyName}"/>
          </div>
            
          <div class="billingDetailsRow">
            <label for="billing-details-streetAddress1"><fmt:message key="courses.studentDetails.billingDetails.streetAddress1"/></label>
            <input id="billing-details-streetAddress1" type="text" name="billingDetailsStreetAddress1" value="${courseStudent.billingDetails.streetAddress1}"/>
          </div>
          
          <div class="billingDetailsRow">
            <label for="billing-details-streetAddress2"><fmt:message key="courses.studentDetails.billingDetails.streetAddress2"/></label>
            <input id="billing-details-streetAddress2" type="text" name="billingDetailsStreetAddress2" value="${courseStudent.billingDetails.streetAddress2}"/>
          </div>
          
          <div class="billingDetailsRow">
            <label for="billing-details-postalCode"><fmt:message key="courses.studentDetails.billingDetails.postalCode"/></label>
            <input id="billing-details-postalCode" type="text" name="billingDetailsPostalCode" value="${courseStudent.billingDetails.postalCode}"/>
          </div>
          
          <div class="billingDetailsRow">
            <label for="billing-details-city"><fmt:message key="courses.studentDetails.billingDetails.city"/></label>
            <input id="billing-details-city" type="text" name="billingDetailsCity" value="${courseStudent.billingDetails.city}"/>
          </div>
          
          <div class="billingDetailsRow">
            <label for="billing-details-region"><fmt:message key="courses.studentDetails.billingDetails.region"/></label>
            <input id="billing-details-region" type="text" name="billingDetailsRegion" value="${courseStudent.billingDetails.region}"/>
          </div>
            
          <div class="billingDetailsRow">
            <label for="billing-details-country"><fmt:message key="courses.studentDetails.billingDetails.country"/></label>
            <input id="billing-details-country" type="text" name="billingDetailsCountry" value="${courseStudent.billingDetails.country}"/>
          </div>
          
          <div class="billingDetailsRow">
            <label for="billing-details-phoneNumber"><fmt:message key="courses.studentDetails.billingDetails.phoneNumber"/></label>
            <input id="billing-details-phoneNumber" type="text" name="billingDetailsPhoneNumber" value="${courseStudent.billingDetails.phoneNumber}"/>
          </div>
          
          <div class="billingDetailsRow">
            <label for="billing-details-emailAddress"><fmt:message key="courses.studentDetails.billingDetails.emailAddress"/></label>
            <input id="billing-details-emailAddress" type="email" name="billingDetailsEmailAddress" value="${courseStudent.billingDetails.emailAddress}"/>
          </div>
            
          <div class="billingDetailsRow">
            <label for="billing-details-companyIdentifier"><fmt:message key="courses.studentDetails.billingDetails.companyIdentifier"/></label>
            <input id="billing-details-companyIdentifier" type="text" name="billingDetailsCompanyIdentifier" value="${courseStudent.billingDetails.companyIdentifier}"/>
          </div>
          
          <div class="billingDetailsRow">
            <label for="billing-details-referenceNumber"><fmt:message key="courses.studentDetails.billingDetails.referenceNumber"/></label>
            <input id="billing-details-referenceNumber" type="text" name="billingDetailsReferenceNumber" value="${courseStudent.billingDetails.referenceNumber}"/>
          </div>
          
          <div class="billingDetailsRow">
            <label for="billing-details-electronicBillingAddress"><fmt:message key="courses.studentDetails.billingDetails.electronicBillingAddress"/></label>
            <input id="billing-details-electronicBillingAddress" type="text" name="billingDetailsElectronicBillingAddress" value="${courseStudent.billingDetails.electronicBillingAddress}" />
          </div>
          
          <div class="billingDetailsRow">
            <label for="billing-details-electronicBillingOperator"><fmt:message key="courses.studentDetails.billingDetails.electronicBillingOperator"/></label>
            <input id="billing-details-electronicBillingOperator" type="text" name="billingDetailsElectronicBillingOperator" value="${courseStudent.billingDetails.electronicBillingOperator}"/>
          </div>
          
          <div class="billingDetailsRow">
            <label for="billing-details-notes"><fmt:message key="courses.studentDetails.billingDetails.notes"/></label>
            <textarea id="billing-details-notes" name="billingDetailsNotes">${courseStudent.billingDetails.notes}</textarea>
          </div>
        </div>
      </div>   
      
    </form>

  </body>
</html>