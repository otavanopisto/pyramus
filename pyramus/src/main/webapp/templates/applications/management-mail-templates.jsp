<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    <title>Pyramus - Hakemusten sähköpostipohjat</title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/draftapi_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/ckeditor_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/hovermenu_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>
    <script defer="defer" type="text/javascript" src="${pageContext.request.contextPath}/scripts/gui/application/application-mail-templates.js"></script> 
    
    <script type="text/javascript">

    function onLoad(event) {
      var tabControl = new IxProtoTabs($('tabs'));
    }

    </script>
    
  </head> 
  
  <body onload="onLoad(event);" ix:enabledrafting="true">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader">Hakemusten sähköpostipohjat</h1>
    
    <div id="createMailTemplateFormContainer">
    
      <div class="genericFormContainer">
        <form method="post" ix:jsonform="true" ix:useglasspane="true">
          <div id="tabs" class="tabLabelsContainer">
            <a class="tabLabel activeTabLabel" href="#create">Luo sähköpostipohja</a>
            <a class="tabLabel" href="#templates">Sähköpostipohjat</a>
          </div>
          <div id="create" class="tabContent">
            <div class="genericFormSection">
              <select id="template-field-id" name="template-field-id">
                <option value="">-- Luo uusi --</option>
              </select>
              <button id="template-button-delete" name="template-button-delete">Poista</button>
            </div>
            
            <div class="genericFormSection">
              <div class="genericFormTitle">
                <div class="genericFormTitleText">Nimi</div>
              </div>
              <input id="template-field-name" name="template-field-name" type="text" size="40"/>
            </div>
            
    <!--         <div class="genericFormSection"> -->
    <!--           <div class="genericFormTitle"> -->
    <!--             <div class="genericFormTitleText">Tekijä</div> -->
    <!--           </div> -->
    <!--           <input id="template-field-name" name="template-field-name" type="text" size="40"/> -->
    <!--         </div> -->
            
            <div class="genericFormSection">
              <div class="genericFormTitle">
                <div class="genericFormTitleText">Linja</div>
              </div>
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
            
            <div class="genericFormSection">
              <div class="genericFormTitle">
                <div class="genericFormTitleText">Otsikko</div>
              </div>
              <input id="template-field-name" name="template-field-name" type="text" size="40"/>
            </div>
            
            <div class="genericFormSection">
              <div class="genericFormTitle">
                <div class="genericFormTitleText">Sisältö</div>
              </div>
              <textarea id="template-field-content" name="template-field-content" rows="10" cols="80"></textarea>
            </div>
          </div>
          
          <div id="templates" class="tabContent">
          
          </div>
          
          <div class="genericFormSubmitSectionOffTab">
            <input name="template-button-save" id="template-button-save" value="Tallenna" class="formvalid" disabled="disabled" type="submit">
          </div>
        </form>
      </div>
    
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>