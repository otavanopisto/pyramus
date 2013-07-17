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
    <jsp:include page="/templates/generic/studentinfopopup_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>
    
    <script type="text/javascript">
      /**
       * Called when this dialog loads. Initializes the search navigation and student tables.
       *
       * @param event The page load event
       */
      function onLoad(event) {
      }
      
      function reloadStudentImage() {
        var imageContainer = $('studentImage');
        
        var imageUrl = 'viewstudentimage.binary?studentId=${student.id}&time=' + new Date().getTime();
    
        imageContainer.setStyle({
          backgroundImage : 'url("' + imageUrl + '")'
        });
      }
    </script>

  </head>
  <body onload="onLoad(event);">

    <form action="editstudentimage.json" target="_uploadFrame" method="post" enctype="multipart/form-data">
      <input type="hidden" name="studentId" value="${student.id}"/>
    
      <div class="editStudentImage_container">
        <div class="editStudentImage_vcontainer">
          <div class="editStudentImage_hcontainer editStudentImage_imagecontainer">
            <c:choose>
              <c:when test="${studentHasImage}">
                <div id="studentImage" style="background-image: url('viewstudentimage.binary?studentId=${student.id}');" class="editStudentImage_image"> </div>
              </c:when>
              <c:otherwise>
                <div id="studentImage" class="editStudentImage_image"> </div>
              </c:otherwise>
            </c:choose>
          </div>
          
          <div class="editStudentImage_hcontainer">
            <div>
              <div><fmt:message key="students.editStudentImageDialog.changeImage"/></div>
              <div><input type="file" name="studentImage"/></div>
              <div><input type="submit" value="<fmt:message key="students.editStudentImageDialog.updateImage"/>"/></div>
            </div>
            <div><input type="submit" name="deleteImage" value="<fmt:message key="students.editStudentImageDialog.deleteImage"/>"/></div>
          </div>
        </div>
      </div>
    </form>

    <iframe id="_uploadFrame" name="_uploadFrame" style="display:none" onLoad="reloadStudentImage();"> </iframe>

  </body>
</html>