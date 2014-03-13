<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>
<t:setupwizard-template>
	<jsp:attribute name="script">
</jsp:attribute>
	<jsp:body>
<form method="post" action="">
            <div class="genericFormSection">  
              <jsp:include
							page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale"
								value="system.settings.setupwizard.adminpassword.usernameTitle" />
                <jsp:param name="helpLocale"
								value="system.settings.setupwizard.adminpassword.usernameHelp" />
              </jsp:include>                  
              <input type="text" name="username" class="required"
							size="25" />
            </div>
            <div class="genericFormSection">  
              <jsp:include
							page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale"
								value="system.settings.setupwizard.adminpassword.passwordTitle" />
                <jsp:param name="helpLocale"
								value="system.settings.setupwizard.adminpassword.passwordHelp" />
              </jsp:include>    
              <input type="password" name="password" class="required"
							size="20">
            </div>
    <div class="genericFormSubmitSectionOffTab">
        <input type="submit" class="formvalid" name="next"
							value="<fmt:message key="system.setupwizard.next"/>">
    </div>
</form>

	
	</jsp:body>
</t:setupwizard-template>
