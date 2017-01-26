<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/templates/generic/glasspane_support.jsp"></jsp:include>

<c:choose>
  <c:when test="${pageformSupportIncluded != true}">
    <script type="text/javascript">
      function initializePageForms() {
        var forms = $$("form[ix:pageform='true']");
        
        for (var i = 0; i < forms.length; i++) {
          var form = forms[i];
          
          Event.observe(form, "submit", function (event) {
            var formElement = Event.element(event);
            formElement._globalGlassPane = new IxGlassPane(document.body, { });
            formElement._globalGlassPane.show();
            
            return true;
          });
        }
      }
      
      Event.observe(window, "load", function (event) {
    	  initializePageForms();
      });
    </script>
    
    <c:set scope="request" var="pageformSupportIncluded" value="true"/>
  </c:when>
</c:choose>