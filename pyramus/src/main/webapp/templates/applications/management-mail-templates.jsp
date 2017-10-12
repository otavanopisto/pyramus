<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    <title>Pyramus - Hakemusten sähköpostipohjat</title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/ckeditor_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/application/application-mail-templates.js"></script> 
  </head> 
  <body>
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader">Hakemusten sähköpostipohjat</h1>
    
    <div class="genericFormContainer">
      
      <div class="template-editor-container">
        <form>
          <div>
            <div>Sähköpostipohjat</div>
            <select id="template-field-id" name="template-field-id">
              <option value="">-- Luo uusi --</option>
            </select>
            <button id="template-button-delete" name="template-button-delete">Poista</button>
          </div>
          <div>
            <div>Linja</div>
            <div>
              <select id="template-field-line" name="template-field-line">
                <option value=""></option>
                <option value="aineopiskelu">Aineopiskelu</option>
                <option value="nettilukio">Nettilukio</option>
                <option value="nettipk">Nettiperuskoulu</option>
                <option value="aikuislukio">Aikuislukio</option>
                <option value="bandilinja">Bändilinja</option>
                <option value="mk">Maahanmuuttajakoulutukset</option>
              </select>
            </div>
          </div>
          <div>
            <div>Nimi</div>
            <div><input id="template-field-name" name="template-field-name" type="text" size="40"/></div>
          </div>
          <div>
            <div>Otsikko</div>
            <div><input id="template-field-subject" name="template-field-subject" type="text" size="40"/></div>
          </div>
          <div>
            <div>Sisältö</div>
            <div><textarea id="template-field-content" name="template-field-content" rows="10" cols="80"></textarea></div>
          </div>
          <div>
            <button id="template-button-save" name="template-button-save">Tallenna</button>
          </div>
        </form>
      </div>
      
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>