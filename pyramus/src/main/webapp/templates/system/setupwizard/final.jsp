<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>

<t:setupwizard-template>
  <jsp:attribute name="script">
  </jsp:attribute>
  <jsp:body>
  	<a href="${requestContext.request.contextPath}/"><fmt:message key="system.setupwizard.final.frontpage" /></a>
  </jsp:body>
</t:setupwizard-template>