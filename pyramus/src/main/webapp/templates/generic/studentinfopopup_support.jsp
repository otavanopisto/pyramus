<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/templates/generic/hoverpanel_support.jsp"></jsp:include>

<c:choose>
  <c:when test="${studentInfoPopupSupportIncluded != true}">
    <script type="text/javascript">
      function openStudentInfoPopupOnElement(element, personId) {

          var hoverPanel = new IxHoverPanel({
            contentURL: GLOBAL_contextPath + '/students/studentinfopopup.page?person=' + personId
          });
  
          hoverPanel.showOverElement(element);
      }
      
    </script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/studentinfopopup.css"/>
    <c:set scope="request" var="studentInfoPopupSupportIncluded" value="true"/>
  </c:when>
</c:choose>