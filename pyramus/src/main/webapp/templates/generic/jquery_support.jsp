<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${jQuerySupportIncluded != true}">
  <script defer="defer" type="text/javascript" src="https://code.jquery.com/jquery-1.12.4.min.js" integrity="sha256-ZosEbRLbNQzLpnKIkEdrPv7lOy9C27hHQ+Xp8a4MxAQ=" crossorigin="anonymous"></script>
  <script defer="defer" type="text/javascript" src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js" integrity="sha256-VazP97ZCwtekAsvgPBSUwPFKdrwD3unUfSGVYrahUqU=" crossorigin="anonymous"></script>
  <c:set scope="request" var="jQuerySupportIncluded" value="true"/>
</c:if>

<c:if test="${jQueryUICSSSupportIncluded != true && param.includeCSS == true}">
  <link type="text/css" rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css" integrity="sha384-xewr6kSkq3dBbEtB6Z/3oFZmknWn7nHqhLVLrYgzEFRbU/DHSxW7K3B44yWUN60D" crossorigin="anonymous"/>
  <c:set scope="request" var="jQueryUICSSSupportIncluded" value="true"/>
</c:if>

<c:if test="${jQueryFileUploadSupportIncluded != true && param.includeFileUpload == true}">
  <script defer="defer" type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/blueimp-file-upload/9.28.0/js/jquery.fileupload.min.js" integrity="sha512-x9gUkDaeSZOt9sw5EfThx9yA5LAL45/o8Xa7q2kv+1A1KHod5y9l+SSoeu5KLL8dK6AseCLbdnE8AiyiejoKhQ==" crossorigin="anonymous"></script>
  <c:set scope="request" var="jQueryFileUploadSupportIncluded" value="true"/>
</c:if>

