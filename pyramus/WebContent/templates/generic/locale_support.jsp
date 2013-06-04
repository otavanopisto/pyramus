<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="event_support.jsp"></jsp:include>

<c:choose>
  <c:when test="${localeSupportIncluded != true}">    
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/fnilocale/fnilocale.js"></script>
    <script type="text/javascript">
      var __LOCALE = null;

      function getLocale() {
        if (!__LOCALE) {
          __LOCALE = new fni.locale.FNILocale();
          __LOCALE.loadLocale("${pageContext.request.locale}", "${pageContext.request.contextPath}/locale/getjavascriptlocale.json");
          __LOCALE.setLocale("${pageContext.request.locale}");
        }
        
        return __LOCALE;
      }
    </script>
    
    <c:set scope="request" var="localeSupportIncluded" value="true"/>
  </c:when>
</c:choose>