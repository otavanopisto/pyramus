<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>	  
    <@include_page path="/templates/generic/head_generic.jsp"/> 
    <@include_page path="/templates/generic/dialog_support.jsp"/>
    <@include_page path="/templates/generic/scriptaculous_support.jsp"/>
    <@include_page path="/templates/generic/tabs_support.jsp"/>
    <@include_page path="/templates/generic/table_support.jsp"/>
    <@include_page path="/templates/generic/jsonrequest_support.jsp"/>
    <@include_page path="/templates/generic/jsonform_support.jsp"/>
    <@include_page path="/templates/generic/draftapi_support.jsp"/>
    <@include_page path="/templates/generic/validation_support.jsp"/>
    <@include_page path="/templates/generic/locale_support.jsp"/>
    <title>Ainevalinnat</title>
    <#-- TODO: Apply context path somehow (${...requestContext...} doesnt work) -->
    <script type="text/javascript" src="/plugin/static/lukio/subjectchoices.js"></script>
    </script>
  </head>
  <body onload="onLoad(event);" ix:enabledrafting="true">
    <@include_page path="/templates/generic/header.jsp"/> 
    <h1 id="pageHeader" class="genericPageHeader">Ainevalinnat</h1>
    
    <div id="manageStudyProgrammesFormContainer"> 
	    <div class="genericFormContainer"> 
	      <form action="savesubjectchoices.json" method="post" ix:jsonform="true" ix:useglasspane="true">
	  
	        <div class="tabLabelsContainer" id="tabs">
	          <a class="tabLabel" href="#manageSubjectChoices">
	            Ainevalinnat
	          </a>
	        </div>
	        
          <div id="manageSubjectChoices" class="tabContentixTableFormattedData">
            <div class="genericTableAddRowContainer">
              <span class="genericTableAddRowLinkContainer" onclick="addSubjectChoicesTableRow();">Lisää ainevalinta</span>
            </div>
              
            <div id="noSubjectChoicesAddedMessageContainer" class="genericTableNotAddedMessageContainer">
              <span>
               Ei ainevalintoja lisättynä. <span onclick="addSubjectChoicesTableRow();" class="genericTableAddRowLink">Lisää ainevalinta</span>.
              </span>
            </div>
            
            <div id="subjectChoicesTableContainer" style="margin-bottom: 32pt;"></div>
            
            <div class="genericTableAddRowContainer">
              <span class="genericTableAddRowLinkContainer" onclick="addPassFailGradeOptionsTableRow();">Lisää S-merkintä</span>
            </div>
              
            <div id="noPassFailGradeOptionsAddedMessageContainer" class="genericTableNotAddedMessageContainer">
              <span>
               Ei S-merkintöjä lisättynä. <span onclick="addPassFailGradeOptionsTableRow();" class="genericTableAddRowLink">Lisää merkintä</span>.
              </span>
            </div>
            
            <div id="passFailGradeOptionsTableContainer"></div>
          </div>
	  
          <div class="genericFormSubmitSectionOffTab">
            <input type="submit" class="formvalid" value="Tallenna">
          </div>

	      </form>
	    </div>
	  </div>
    
    <@include_page path="/templates/generic/footer.jsp"/> 
  </body>
</html>