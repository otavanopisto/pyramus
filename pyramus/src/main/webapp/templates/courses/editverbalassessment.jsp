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
    <jsp:include page="/templates/generic/ckeditor_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    
    <script type="text/javascript">
      /**
       * Returns the identifiers of the students selected in this dialog.
       *
       * @return The students selected in this dialog
       */
      function getResults() {
        var verbalText = CKEDITOR.instances["verbalAssessment"].getData();
        
        return {
          verbalAssessment: verbalText
        };
      }
      
      function onLoad(event) {
        CKEDITOR.on(
            'instanceReady', function(evt) {
              var editor = evt.editor;
              var dialog = getDialog();
              if (dialog) {
                if (dialog._modifiedVerbalAssessment) {
                  editor.setData(dialog._modifiedVerbalAssessment);
                }
              }
            }
        );
      }
    </script>
  </head>

  <body onload="onLoad(event);">
    <div id="editVerbalAssessmentDialogContainer" class="editVerbalAssessmentDialogContainer">
      <div class="editVerbalAssessmentEditorContainer"> 
        <textarea name="verbalAssessment" ix:cktoolbar="courseGradeText" ix:ckeditor="true">${verbalAssessment}</textarea>
      </div>
    </div>
  </body>
</html>