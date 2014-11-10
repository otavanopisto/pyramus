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

<script type="text/javascript">
	function onLoad(event) {
		var tabControl = new IxProtoTabs($('tabs'));
	};
</script>

</head>
<body onload="onLoad(event);">
	<jsp:include page="/templates/generic/header.jsp"></jsp:include>

	<div id="authorizationFormContainer">
		<div class="genericFormContainer" id="authorizationForm">
			<div class="tabLabelsContainer" id="tabs">
				<a class="tabLabel" href="#auth"> Authorize </a>
			</div>

			<div id="auth" class="tabContent">
				<form action="authorize.page" method="post">
					<div class="genericFormSection">
						<h3>Authorize 3rd party app</h3>
						<p><b>${clientAppName}</b> wants to act on your behalf.</p>
						<input type="submit" name="authorize" value="Authorize" /> 
						<input type="submit" name="deny" value="Deny" />
					</div>
				</form>
			</div>
		</div>
	</div>

	<jsp:include page="/templates/generic/footer.jsp"></jsp:include>
</body>
</html>