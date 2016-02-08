<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<jsp:include page="locale_support.jsp"></jsp:include>

<c:choose>
  <c:when test="${messageAdapterSupportIncluded != true}">    
      
    <script type="text/javascript">
    var messages = [];
    <%
      java.util.List<fi.internetix.smvc.SmvcMessage> messages = (java.util.List<fi.internetix.smvc.SmvcMessage>) request.getAttribute("messages");
      for (fi.internetix.smvc.SmvcMessage message : messages) {
        out.append("messages.push({'severity':'");
        out.append(StringEscapeUtils.escapeJavaScript(String.valueOf(message.getSeverity())));
        out.append("','message':'");
        out.append(StringEscapeUtils.escapeJavaScript(message.getMessage()));
        out.append("'});");
      }
    %>
    </script>
    
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ixmessageadapter.js"></script>
    <c:set scope="request" var="messageAdapterSupportIncluded" value="true"/>
  </c:when>
</c:choose>