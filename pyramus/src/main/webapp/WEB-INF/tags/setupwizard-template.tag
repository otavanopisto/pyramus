<%@ tag description="Template for setup wizards" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ attribute name="script" fragment="true"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
<head>
<title><fmt:message key="system.importcsv.pageTitle" /></title>
<jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
<jsp:include page="/templates/generic/ckeditor_support.jsp"></jsp:include>
<jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
<jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
<jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
<jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
<jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
<jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
<jsp:include page="/templates/generic/draftapi_support.jsp"></jsp:include>
<jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>
<jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>

<script type="text/javascript">
	function onLoad(event) {
	}
</script>
<jsp:invoke fragment="script" />
</head>
<body onload="onLoad(event)">
	<jsp:include page="/templates/generic/header.jsp"></jsp:include>
	<h1 class="genericPageHeader">
		<fmt:message key="system.setupwizard.pageTitle" />
	</h1>

	<div id="setupWizardFormContainer">
		<div class="genericFormContainer">
			<div>
				<h2>
					<fmt:message key="system.setupwizard.${setupPhase}.title" />
				</h2>
				<p>
					<fmt:message key="system.setupwizard.${setupPhase}.description" />
				</p>
				<jsp:doBody />
			</div>
		</div>
	</div>
	<jsp:include page="/templates/generic/footer.jsp"></jsp:include>
</body>
</html>
