<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
  <title><fmt:message key="users.login.pageTitle"></fmt:message></title>
  <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
  <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
  <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
  <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
  <jsp:include page="/templates/generic/loginstyles_support.jsp"></jsp:include>
</head>

<body class="muikku-login-body">
  <div class="muikku-login-card-wrapper">
    <form action="authorize.page" method="post">
      <section class="muikku-login-card">
        <header class="muikku-logo-container">
          <img class="muikku-logo" src="//cdn.muikkuverkko.fi/assets/muikku/oo-branded-site-logo.png" role="presentation"/>
          <span class="muikku-logo-text">Muikku</span>
        </header>
    
        <main class="muikku-login-container">
          <h3>
            <fmt:message key="authorizeclientapp.header">
              <fmt:param value="${clientAppName}"/>
            </fmt:message>
          </h3>
          
          <c:if test="${not empty authScopes}">
            <div class="muikku-login-container-row">
              <fmt:message key="authorizeclientapp.accessListHeader">
                <fmt:param value="${clientAppName}"/>
              </fmt:message>
              
              <ul>
                <c:if test="${authScopes.contains('legacy')}">
                  <li><fmt:message key="authorizeclientapp.scope.legacy.line1"/></li>
                </c:if>
    
                <c:if test="${authScopes.contains('atomi')}">
                  <li><fmt:message key="authorizeclientapp.scope.atomi.line1"/></li>
                  <li><fmt:message key="authorizeclientapp.scope.atomi.line2"/></li>
                  <li><fmt:message key="authorizeclientapp.scope.atomi.line3"/></li>
                  <li><fmt:message key="authorizeclientapp.scope.atomi.line4"/></li>
                  <li><fmt:message key="authorizeclientapp.scope.atomi.line5"/></li>
                </c:if>
              </ul>
            </div>
          </c:if>
          
          <div class="muikku-login-footer">
            <input type="submit" class="muikku-login-button" name="authorize" value="<fmt:message key="authorizeclientapp.scope.buttons.authorize"/>" /> 
            <input type="submit" class="muikku-login-button" name="deny" value="<fmt:message key="authorizeclientapp.scope.buttons.deny"/>" />
          </div>
        </main>
      </section>
    </form>
  </div>
</body>
</html>