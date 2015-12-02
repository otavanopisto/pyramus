<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head> 
    <title><fmt:message key="students.manageStudentContactEntries.pageTitle"></fmt:message></title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/datefield_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/ckeditor_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/hovermenu_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>    

    <!-- Used to render memo values with line breaks; for some reason this is the only approach that works -->
    <% pageContext.setAttribute("newLineChar", "\n"); %>

    <script type="text/javascript">
      function setupTabRelatedActions(personId, studentId) {
        var basicTabRelatedActionsHoverMenu = new IxHoverMenu($('basicTabRelatedActionsHoverMenuContainer.' + studentId), {
          text: '<fmt:message key="students.manageStudentContactEntries.basicTabRelatedActionsLabel"/>'
        });
    
        basicTabRelatedActionsHoverMenu.addItem(new IxHoverMenuLinkItem({
          iconURL: GLOBAL_contextPath + '/gfx/eye.png',
          text: '<fmt:message key="students.manageStudentContactEntries.basicTabRelatedActionsViewStudentLabel"/>',
          link: GLOBAL_contextPath + '/students/viewstudent.page?person=' + personId  
        }));

        basicTabRelatedActionsHoverMenu.addItem(new IxHoverMenuLinkItem({
          iconURL: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
          text: '<fmt:message key="students.manageStudentContactEntries.basicTabRelatedActionsEditStudentLabel"/>',
          link: GLOBAL_contextPath + '/students/editstudent.page?person=' + personId  
        }));
      }

      function onLoad(event) {
        <c:forEach var="student" items="${students}">
          // Setup basics
          setupTabRelatedActions(${person.id}, ${student.id}); 
        </c:forEach>
        
        var tabControl2 = new IxProtoTabs($('studentTabs'));

        <c:forEach var="student" items="${students}">
          resetEntryForm(${student.id});
        </c:forEach>

        <c:if test="${!empty param.activeTab}">
          tabControl.setActiveTab("${param.activeTab}");  
        </c:if>
      }

      function resetEntryForm(studentId) {
        var entryForm = $("newContactEntryForm." + studentId);
        entryForm.entryType.value = 'OTHER';
        entryForm.entryCreatorName.value = '${loggedUserName}';
        var dField = getIxDateField("entryDate." + studentId);
        if (dField != null)
          dField.setTimestamp(new Date().getTime());
        entryForm["entryText." + studentId].value = '';
        CKEDITOR.instances["entryText." + studentId].setData('');
        entryForm.entryId.value = '-1';
        entryForm.submitContactLogEntryButton.value = "<fmt:message key="students.manageStudentContactEntries.newContactLogEntryBtn"/>";
      }

      function resetEntryForm2(event, studentId) {
        $('studentContactLogEditEntryTitle').hide();
        $('studentContactLogNewEntryTitle').show();
        Event.stop(event);
        resetEntryForm(studentId);
      }

      function editEntry(contactEntryId, studentId) {
        $('studentContactLogEditEntryTitle').show();
        $('studentContactLogNewEntryTitle').hide();
        JSONRequest.request("students/getcontactentry.json", {
          parameters: {
            entryId: contactEntryId
          },
          onSuccess: function (jsonResponse) {
            var results = jsonResponse.results;
            var entryId = results.id;
            var studentId = results.studentId;
            var entryDate = new Date(results.timestamp);
            var creatorName = results.creatorName;
            var entryType = results.type;
            var entryText = results.text;

            var entryForm = $("newContactEntryForm." + studentId);
            entryForm.entryType.value = entryType;
            entryForm.entryCreatorName.value = creatorName;
            var dField = getIxDateField("entryDate." + studentId);
            if (dField != null) {
              if (entryDate != null)
                dField.setTimestamp(entryDate.getTime());
              else
                dField.setTimestamp(new Date().getTime());
            }
            entryForm["entryText." + studentId].value = entryText;
            CKEDITOR.instances["entryText." + studentId].setData(entryText);
            entryForm.entryId.value = entryId;
            entryForm.submitContactLogEntryButton.value = "<fmt:message key="students.manageStudentContactEntries.editContactLogEntryBtn"/>";
          } 
        });
      }

      function saveEvent(event, studentId) {
        var entryForm = $("newContactEntryForm." + studentId);
        var entryId = entryForm.entryId.value;
        
        if (entryId == -1)
          newContactEntryFormSubmit(event, studentId);
        else
          modifyContactEntryFormSubmit(event, studentId);        
      }

      function getEntryTypeName(entryType) {
        var entryTypeName = "???";
        
        if (entryType == 'OTHER')
          entryTypeName = '<fmt:message key="students.manageStudentContactEntries.contactEntry.types.other"/>';
        else
        if (entryType == 'LETTER')
          entryTypeName = '<fmt:message key="students.manageStudentContactEntries.contactEntry.types.letter"/>';
        else
        if (entryType == 'EMAIL')
          entryTypeName = '<fmt:message key="students.manageStudentContactEntries.contactEntry.types.email"/>';
        else
        if (entryType == 'PHONE')
          entryTypeName = '<fmt:message key="students.manageStudentContactEntries.contactEntry.types.phone"/>';
        else
        if (entryType == 'CHATLOG')
          entryTypeName = '<fmt:message key="students.manageStudentContactEntries.contactEntry.types.chatlog"/>';
        else
        if (entryType == 'SKYPE')
          entryTypeName = '<fmt:message key="students.manageStudentContactEntries.contactEntry.types.skype"/>';
        else
        if (entryType == 'FACE2FACE')
          entryTypeName = '<fmt:message key="students.manageStudentContactEntries.contactEntry.types.face2face"/>';

        return entryTypeName;
      }

      function archiveEntry(entryId, studentId) {
        var entryShort = $("entry." + entryId + ".text").textContent;
        entryShort = entryShort.stripScripts().stripTags().strip();
        entryShort = entryShort.truncate(20, "...");
        var url = GLOBAL_contextPath + "/simpledialog.page?localeId=students.manageStudentContactEntries.archiveContactEntryConfirmDialogContent&localeParams=" + encodeURIComponent(entryShort);
        var dialog = new IxDialog({
          id : 'confirmRemoval',
          contentURL : url,
          centered : true,
          showOk : true,  
          showCancel : true,
          autoEvaluateSize: true,
          title : '<fmt:message key="students.manageStudentContactEntries.archiveContactEntryConfirmDialogTitle"/>',
          okLabel : '<fmt:message key="students.manageStudentContactEntries.archiveContactEntryConfirmDialogOkLabel"/>',
          cancelLabel : '<fmt:message key="students.manageStudentContactEntries.archiveContactEntryConfirmDialogCancelLabel"/>'
        });
      
        dialog.addDialogListener(function(event) {
          switch (event.name) {
            case 'okClick':
              JSONRequest.request("students/archivecontactentry.json", {
                parameters: {
                  entryId: entryId
                },
                onSuccess: function (jsonResponse) {
                  var entryItem = $('studentContactEntryItem.' + entryId);

                  if (entryItem != null)
                    entryItem.remove();                  
                }
              });   
            break;
          }
        });

        dialog.open();
      }
      
      function addEntryRow(studentId, entryId, entryDate, entryType, entryCreatorName, entryText) {
        var listDiv = $('contactEntries.' + studentId);
        var dateStr = getLocale().getDate(entryDate, false);

        var entryTypeName = getEntryTypeName(entryType);

        var newEntryDiv = listDiv.appendChild(new Element("div", {id: "studentContactEntryItem." + entryId, className: "studentContactEntryItem"}));
        var newEntryCaptionDiv = newEntryDiv.appendChild(new Element("div", {id: "entry." + entryId + ".caption", className: "studentContactEntryCaption"}));
        
        var newEntryCaptionDateSpan = new Element("span", { id: "entryDate." + entryId + ".caption", className: "studentContactEntryDate" });
        newEntryCaptionDateSpan.update(dateStr); 
        var newEntryCaptionTypeSpan = new Element("span", { id: "entryType." + entryId + ".caption", className: "studentContactEntryType" });
        newEntryCaptionTypeSpan.update(entryTypeName); 
        var newEntryCaptionCreatorSpan = new Element("span", { id: "entryCreator." + entryId + ".caption", className: "studentContactEntryCreator" });
        newEntryCaptionCreatorSpan.update(entryCreatorName); 

        newEntryCaptionDiv.appendChild(newEntryCaptionDateSpan); 
        newEntryCaptionDiv.appendChild(newEntryCaptionTypeSpan); 
        newEntryCaptionDiv.appendChild(newEntryCaptionCreatorSpan); 
        
        var buttonsDiv = newEntryDiv.appendChild(new Element("div", { className: "studentContactEntryButtons" }));
        buttonsDiv.appendChild(new Element("img", {
          id: "entry." + entryId + ".commentbtn", 
          className: "studentContactEntryEditButton iconButton", 
          src: "${pageContext.request.contextPath}/gfx/list-add.png", 
          onClick: "addComment(" + entryId + ", " + studentId + ")"
        }));
        buttonsDiv.appendChild(new Element("img", {
          id: "entry." + entryId + ".editbtn", 
          className: "studentContactEntryEditButton iconButton", 
          src: "${pageContext.request.contextPath}/gfx/accessories-text-editor.png", 
          onClick: "editEntry(" + entryId + ", " + studentId + ")"
        }));
        buttonsDiv.appendChild(new Element("img", {
          id: "entry." + entryId + ".archivebnt", 
          className: "studentContactEntryArchiveButton iconButton", 
          src: "${pageContext.request.contextPath}/gfx/edit-delete.png", 
          onClick: "archiveEntry(" + entryId + ", " + studentId + ")"
        }));

        var node = new Element("div", { id: "entry." + entryId + ".text" });
        node.update(entryText);
        newEntryDiv.appendChild(node);
       
        node = new Element("div", { id: "contactEntryComments." + entryId, className: "contactEntryCommentsWrapper" });
        newEntryDiv.appendChild(node);
      }
      
      /**
       * 
       *
       * @param event The submit event
       */
      function newContactEntryFormSubmit(event, studentId) {
        Event.stop(event);

        var entryForm = Event.element(event);
        JSONRequest.request("students/createcontactentry.json", {
          parameters: {
            entryType: entryForm.entryType.value,
            entryCreatorName: entryForm.entryCreatorName.value,
            entryDate: entryForm["entryDate." + studentId].value,
            entryText: CKEDITOR.instances["entryText." + studentId].getData(),
            studentId: entryForm.studentId.value
          },
          onSuccess: function (jsonResponse) {
            window.location.reload();
          } 
        });
      }
      
      function modifyContactEntryFormSubmit(event, studentId) {
        Event.stop(event);

        var entryForm = Event.element(event);
        JSONRequest.request("students/editcontactentry.json", {
          parameters: {
            entryType: entryForm.entryType.value,
            entryCreatorName: entryForm.entryCreatorName.value,
            entryId: entryForm.entryId.value,
            entryDate: entryForm["entryDate." + studentId].value,
            entryText: CKEDITOR.instances["entryText." + studentId].getData()
          },
          onSuccess: function (jsonResponse) {
            window.location.reload();
          } 
        });
      }
      
      // Comments
      
      function addComment(entryId, studentId) {
        resetCommentForm(studentId);
        
        var entryForm = $("newContactEntryCommentForm." + studentId);
        entryForm.entryId.value = entryId;

        var parentNode = $("contactEntryComments." + entryId); /* studentContactEntryItem */
        showCommentForm(parentNode, studentId); 
      }
      
      function addCommentRow(entryId, commentId, studentId, commentDate, commentCreatorName, commentText) {
        var listDiv = $('contactEntryComments.' + entryId);
        var dateStr = getLocale().getDate(commentDate, false);

        var newEntryDiv = listDiv.appendChild(new Element("div", { id: "studentContactEntryCommentItem." + commentId, className: "studentContactCommentEntryItem" }));
        var newEntryCaptionDiv = newEntryDiv.appendChild(new Element("div", { id: "entry." + entryId + ".caption", className: "studentContactCommentEntryCaption" }));
        
        var newEntryCaptionDateSpan = new Element("span", { id: "commentDate." + commentId + ".caption", className: "studentContactCommentEntryDate" });
        newEntryCaptionDateSpan.update(dateStr); 
        var newEntryCaptionCreatorSpan = new Element("span", { id: "commentCreator." + commentId + ".caption", className: "studentContactCommentEntryCreator" });
        newEntryCaptionCreatorSpan.update(commentCreatorName);
        
        newEntryCaptionDiv.appendChild(newEntryCaptionDateSpan); 
        newEntryCaptionDiv.appendChild(newEntryCaptionCreatorSpan); 
        
        var buttonsDiv = newEntryDiv.appendChild(new Element("div", { className: "studentContactCommentEntryButtons" }));
        buttonsDiv.appendChild(new Element("img", { 
          id: "comment." + entryId + ".editbtn", 
          className: "studentContactEntryEditButton iconButton", 
          src: "${pageContext.request.contextPath}/gfx/accessories-text-editor.png", 
          onClick: "editComment(" + commentId + ", " + entryId + ", " + studentId + ")"
        }));
        buttonsDiv.appendChild(new Element("img", { 
          id: "comment." + entryId + ".archivebnt", 
          className: "studentContactEntryArchiveButton iconButton", 
          src: "${pageContext.request.contextPath}/gfx/edit-delete.png", 
          onClick: "archiveComment(" + commentId + ", " + entryId + ")"
        }));

        var node = new Element("div", { id: "comment." + commentId + ".text" });
        node.update(commentText);
        newEntryDiv.appendChild(node);
      }

      function saveEntryComment(event, studentId) {
        var entryForm = $("newContactEntryCommentForm." + studentId);
        var commentId = entryForm.commentId.value;
        
        if (commentId == -1)
          newContactEntryCommentFormSubmit(event, studentId);
        else
          modifyContactEntryCommentFormSubmit(event, studentId);

        hideCommentForm(studentId);        
      }

      /**
       * 
       *
       * @param event The submit event
       */
      function newContactEntryCommentFormSubmit(event, studentId) {
        Event.stop(event);

        var entryForm = Event.element(event);
        JSONRequest.request("students/createcontactentrycomment.json", {
          parameters: {
            commentCreatorName: entryForm.commentCreatorName.value,
            commentDate: entryForm["commentDate." + studentId].value,
            commentText: CKEDITOR.instances["commentText." + studentId].getData(),
            entryId: entryForm.entryId.value
          },
          onSuccess: function (jsonResponse) {
            window.location.reload();
          } 
        });
      }
      
      function modifyContactEntryCommentFormSubmit(event, studentId) {
        Event.stop(event);

        var entryForm = Event.element(event);
        JSONRequest.request("students/editcontactentrycomment.json", {
          parameters: {
            commentId: entryForm.commentId.value,
            commentCreatorName: entryForm.commentCreatorName.value,
            commentDate: entryForm["commentDate." + studentId].value,
            commentText: CKEDITOR.instances["commentText." + studentId].getData()
          },
          onSuccess: function (jsonResponse) {
            window.location.reload();
          } 
        });
      }

      function resetCommentForm(studentId) {
        var entryForm = $("newContactEntryCommentForm." + studentId);
        entryForm.commentCreatorName.value = '${loggedUserName}';

        // Set time to zero (UTC), same as with datepicker
        var commentDate = new Date();
        commentDate.setUTCHours(0, 0, 0, 0);
        
        entryForm["commentDate." + studentId].value = commentDate.getTime();
        entryForm["commentText." + studentId].value = '';
        CKEDITOR.instances["commentText." + studentId].setData('');
        entryForm.entryId.value = '-1';
        entryForm.commentId.value = '-1';
        entryForm.submitContactLogEntryButton.value = "<fmt:message key="students.manageStudentContactEntries.newCommentBtn"/>";
      }

      function resetCommentForm2(event, studentId) {
        Event.stop(event);
        resetCommentForm(studentId);
        hideCommentForm(studentId);
      }

      function showCommentForm(parentNode, studentId) {
        var container = $("commentFormContainer." + studentId);
        container.remove();
        
        parentNode.appendChild(container);

        container.show();

        var elementBottom = container.cumulativeOffset().top + container.getDimensions().height;
        var viewportBottom = document.viewport.getScrollOffsets().top + document.viewport.getDimensions().height;
        
        if (viewportBottom < elementBottom) {
          Effect.ScrollTo(container, { duration:'0.2', offset: -20 });
        }
      }

      function hideCommentForm(studentId) {
        var container = $("commentFormContainer." + studentId);

        container.hide();
      }
      
      function editComment(commentId, entryId, studentId) {
        JSONRequest.request("students/getcontactentrycomment.json", {
          parameters: {
            commentId: commentId
          },
          onSuccess: function (jsonResponse) {
            var results = jsonResponse.results;
            var commentId = results.id;
            var entryId = results.entryId;
            var entryDate = new Date(results.timestamp);
            var creatorName = results.creatorName;
            var entryText = results.text;

            var commentForm = $("newContactEntryCommentForm." + studentId);
            commentForm.commentCreatorName.value = creatorName;
            commentForm["commentDate." + studentId].value = entryDate.getTime();
            commentForm["commentText." + studentId].value = entryText;
            CKEDITOR.instances["commentText." + studentId].setData(entryText);
            commentForm.entryId.value = entryId;
            commentForm.commentId.value = commentId;
            commentForm.submitContactLogEntryButton.value = "<fmt:message key="students.manageStudentContactEntries.editCommentEntryBtn"/>";

            var parentNode = $("studentContactEntryCommentItem." + commentId);
            
            showCommentForm(parentNode, studentId); 
          } 
        });
      }

      function archiveComment(commentId, entryId) {
        var entryShort = $("comment." + commentId + ".text").textContent;
        entryShort = entryShort.stripScripts().stripTags().strip();
        entryShort = entryShort.truncate(20, "...");
        
        var url = GLOBAL_contextPath + "/simpledialog.page?localeId=students.manageStudentContactEntries.archiveCommentConfirmDialogContent&localeParams=" + encodeURIComponent(entryShort);
        var dialog = new IxDialog({
          id : 'confirmRemoval',
          contentURL : url,
          centered : true,
          showOk : true,  
          showCancel : true,
          autoEvaluateSize: true,
          title : '<fmt:message key="students.manageStudentContactEntries.archiveCommentConfirmDialogTitle"/>',
          okLabel : '<fmt:message key="students.manageStudentContactEntries.archiveCommentConfirmDialogOkLabel"/>',
          cancelLabel : '<fmt:message key="students.manageStudentContactEntries.archiveCommentConfirmDialogCancelLabel"/>'
        });
      
        dialog.addDialogListener(function(event) {
          switch (event.name) {
            case 'okClick':
              JSONRequest.request("students/archivecontactentrycomment.json", {
                parameters: {
                  commentId: commentId
                },
                onSuccess: function (jsonResponse) {
                  var entryItem = $('studentContactEntryCommentItem.' + commentId);

                  if (entryItem != null)
                    entryItem.remove();                  
                }
              });   
            break;
          }
        });

        dialog.open();
      }
      
    </script>
  </head>

  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
  
    <h1 class="genericPageHeader"><fmt:message key="students.manageStudentContactEntries.pageTitle" /> (${person.latestStudent.fullName})</h1>
  
    <div id="viewStudentViewContainer"> 
      <div class="genericFormContainer"> 
        <div class="tabLabelsContainer" id="studentTabs">
          <c:forEach var="student" items="${students}">
            <a class="tabLabel" href="#student.${student.id}">
              <c:choose>
                <c:when test="${student.studyProgramme == null}">
                   <fmt:message key="students.manageStudentContactEntries.noStudyProgrammeTabLabel"/>
                </c:when>
                <c:otherwise>
                  ${student.studyProgramme.name}
                </c:otherwise>
              </c:choose>
              <c:if test="${student.hasFinishedStudies}">*</c:if>
            </a>
          </c:forEach>
        </div>
    
        <c:forEach var="student" items="${students}">
          <div id="student.${student.id}" class="tabContent">
            <div id="basicTabRelatedActionsHoverMenuContainer.${student.id}" class="tabRelatedActionsContainer"></div>
  
            <div id="viewStudentViewContainer"> 
              <div class="genericFormContainer genericAbsolutePositioningWrapper"> 
                <div id="studentContactEntryList.${student.id}" class="studentContactEntryWrapper">
                  <div class="studentContactLogViewTitle"><fmt:message key="students.viewStudent.contactLogEntriesTitle"/></div>
                  <div id="contactEntries.${student.id}"></div>

                  <script type="text/javascript">
                    <c:forEach var="contactEntry" items="${contactEntries[student.id]}">
                      addEntryRow(
                          ${student.id}, 
                          ${contactEntry.id}, 
                          ${contactEntry.entryDate.time}, 
                          '${contactEntry.type}', 
                          '${fn:escapeXml(contactEntry.creatorName)}', 
                          '${fn:replace(fn:replace(contactEntry.text, newLineChar, ""), "'", "\\'")}'
                      );
    
                      <c:forEach var="comment" items="${contactEntryComments[contactEntry.id]}">
                      <c:if test="${!comment.archived}">
                      addCommentRow(
                          ${comment.entry.id}, 
                          ${comment.id},
                          ${student.id}, 
                          ${comment.commentDate.time}, 
                          '${fn:escapeXml(comment.creatorName)}', 
                          '${fn:replace(fn:replace(comment.text, newLineChar, ""), "'", "\\'")}'
                      );
                      </c:if>
                      </c:forEach>
                    </c:forEach>
                  </script>
                </div>

                <div id="commentFormContainer.${student.id}" style="display: none" class="studentCommentContainer">
                  <form method="post" id="newContactEntryCommentForm.${student.id}" onsubmit="saveEntryComment(event, ${student.id});">
                    <input type="hidden" name="entryId" value="-1"/>
                    <input type="hidden" name="commentId" value="-1"/>
                    <input type="hidden" name="commentCreatorName" value=""/>
                    <input type="hidden" name="commentDate.${student.id}" value=""/>
                    <div class="genericFormSection">  
                      <textarea name="commentText.${student.id}" cols="40" rows="4" ix:cktoolbar="studentContactEntryText" ix:ckeditor="true"></textarea>
                    </div>            
                    <div>
                      <input type="submit" name="submitContactLogEntryButton" value="<fmt:message key="students.manageStudentContactEntries.newCommentBtn"/>">
                      <input type="button" name="clearContactLogEntryButton" value="<fmt:message key="students.manageStudentContactEntries.resetCommentFormBtn"/>" onClick="resetCommentForm2(event, ${student.id});">
                    </div> 
                  </form>
                </div>

                <div class="studentContactNewEntryWrapper">
                  <div class="studentContactLogViewTitle" id="studentContactLogNewEntryTitle"><fmt:message key="students.viewStudent.contactLogNewEntryTitle"/></div>
                  <div class="studentContactLogViewTitle" id="studentContactLogEditEntryTitle" style="display:none;"><fmt:message key="students.viewStudent.contactLogEditEntryTitle"/></div>
                  <div class="studentContactNewEntryFormContainer">
                    <form method="post" id="newContactEntryForm.${student.id}" onsubmit="saveEvent(event, ${student.id});">
                      <input type="hidden" name="studentId" value="${student.id}"/>
                      <input type="hidden" name="entryId" value="-1"/>
  
                      <div class="genericFormSection">                            
                        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale" value="students.manageStudentContactEntries.contactEntry.typeTitle"/>
                          <jsp:param name="helpLocale" value="students.manageStudentContactEntries.contactEntry.typeHelp"/>
                        </jsp:include> 
                        <select name="entryType">
                          <option value="OTHER"><fmt:message key="students.manageStudentContactEntries.contactEntry.types.other"/></option>
                          <option value="LETTER"><fmt:message key="students.manageStudentContactEntries.contactEntry.types.letter"/></option>
                          <option value="EMAIL"><fmt:message key="students.manageStudentContactEntries.contactEntry.types.email"/></option>
                          <option value="PHONE"><fmt:message key="students.manageStudentContactEntries.contactEntry.types.phone"/></option>
                          <option value="CHATLOG"><fmt:message key="students.manageStudentContactEntries.contactEntry.types.chatlog"/></option>
                          <option value="SKYPE"><fmt:message key="students.manageStudentContactEntries.contactEntry.types.skype"/></option>
                          <option value="FACE2FACE"><fmt:message key="students.manageStudentContactEntries.contactEntry.types.face2face"/></option>
                        </select>
                      </div>            
                      <div class="genericFormSection">                            
                        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale" value="students.manageStudentContactEntries.contactEntry.fromTitle"/>
                          <jsp:param name="helpLocale" value="students.manageStudentContactEntries.contactEntry.fromHelp"/>
                        </jsp:include> 
                        <input type="text" name="entryCreatorName"/>
                      </div> 
                      <div class="genericFormSection">                            
                        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale" value="students.manageStudentContactEntries.contactEntry.dateTitle"/>
                          <jsp:param name="helpLocale" value="students.manageStudentContactEntries.contactEntry.dateHelp"/>
                        </jsp:include> 
                        <input type="text" name="entryDate.${student.id}" ix:datefieldid="entryDate.${student.id}" class="ixDateField"/>
                      </div>
                      <div class="genericFormSection">                            
                        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                          <jsp:param name="titleLocale" value="students.manageStudentContactEntries.contactEntry.textTitle"/>
                          <jsp:param name="helpLocale" value="students.manageStudentContactEntries.contactEntry.textHelp"/>
                        </jsp:include> 
                        <textarea name="entryText.${student.id}" cols="60" rows="6" ix:cktoolbar="studentContactEntryText" ix:ckeditor="true"></textarea>
                      </div>            
                      <div>
                        <input type="submit" name="submitContactLogEntryButton" value="<fmt:message key="students.manageStudentContactEntries.newContactLogEntryBtn"/>">
                        <input type="button" name="clearContactLogEntryButton" value="<fmt:message key="students.manageStudentContactEntries.resetContactLogEntryFormBtn"/>" onClick="resetEntryForm2(event, ${student.id});">
                      </div> 
                    </form>
                  </div>
                </div>  
                <div class="columnClear"></div>         
              </div>
            </div>  
          </div>
        </c:forEach>
      </div>
    </div>  

    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>