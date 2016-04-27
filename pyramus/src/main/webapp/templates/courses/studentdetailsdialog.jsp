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
    <script type="text/javascript">
      function getResults() {
        var form = $('studentDetailsForm');
        
        return {
          roomId: form.roomId.value,
          lodgingFee: form.lodgingFee.value,
          lodgingFeeCurrency: form.lodgingFeeCurrency.value
        };
      }
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
      
    </form>
  
  <!-- 
    TODO:
    e-Billing address
    Organization (free-form field)
     -->
  </body>
</html>