<meta charset="UTF-8"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta content="width=device-width, initial-scale=1.0" name="viewport" />
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/scripts/parsley/parsley.css"/>
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/application-management.css"/>
<jsp:include page="/templates/generic/jquery_support.jsp">
  <jsp:param name="includeCSS" value="true"/>
  <jsp:param name="includeFileUpload" value="true"/>
</jsp:include>
<script defer="defer" type="text/javascript" src="https://cdn.muikkuverkko.fi/libs/ckeditor/4.22.1/ckeditor.js" integrity="sha384-9kZFxdSZJ1KoJ6+CwPyP3yYY1Y6wptfLNIS0Wdr5O9ouRaDf+ZVa3gznEb9zbBSe" crossorigin="anonymous"></script>
<script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/parsley/parsley.min.js"></script>
<script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/parsley/fi.js"></script>
<script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/moment/moment.min.js"></script>
<script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/notificationqueue/notificationqueue.js"></script>
<script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/applications/application.js"></script>
<script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/applications/application-management.js"></script>