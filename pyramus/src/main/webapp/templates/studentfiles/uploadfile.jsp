<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/searchnavigation_support.jsp"></jsp:include>
  </head>
  <body>
    <div>
      <form action="uploadstudentfile.json" target="_uploadFrame" method="post" enctype="multipart/form-data" id="uploadStudentFileForm">
        <input type="hidden" name="studentId" value="${studentId}"/>

        <div class="genericFormSection">                                  
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="studentFiles.uploadFileDialog.fileNameTitle"/>
            <jsp:param name="helpLocale" value="studentFiles.uploadFileDialog.fileNameHelp"/>
          </jsp:include>
          <input type="text" name="fileName" class="required" size="40"/>
        </div>
  
        <div class="genericFormSection">                                  
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="studentFiles.uploadFileDialog.fileTypeTitle"/>
            <jsp:param name="helpLocale" value="studentFiles.uploadFileDialog.fileTypeHelp"/>
          </jsp:include>
          <select name="fileType">
            <option value=""></option>
            <c:forEach var="fileType" items="${fileTypes}">
              <option value="${fileType.id}">${fileType.name}</option>
            </c:forEach>
          </select>
        </div>
  
        <div class="genericFormSection">                                  
          <jsp:include page="/templates/generic/fragments/formtitle.jsp">
            <jsp:param name="titleLocale" value="studentFiles.uploadFileDialog.fileTitle"/>
            <jsp:param name="helpLocale" value="studentFiles.uploadFileDialog.fileHelp"/>
          </jsp:include>
          <input type="file" name="file" class="required"/>
        </div>
      </form>
      
      <iframe id="_uploadFrame" name="_uploadFrame" style="display:none"> </iframe>
    </div>
  </body>
</html>