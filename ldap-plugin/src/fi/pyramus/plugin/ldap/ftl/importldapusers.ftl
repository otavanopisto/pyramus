<#assign fmt=JspTaglibs["http://java.sun.com/jsp/jstl/fmt"]>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>	  
    <@include_page path="/templates/generic/head_generic.jsp"/> 
	  <@include_page path="/templates/generic/scriptaculous_support.jsp"/> 
	  <@include_page path="/templates/generic/table_support.jsp"/> 
	  <@include_page path="/templates/generic/tabs_support.jsp"/> 
	  <title><@fmt.message key="system.importldapusers.pageTitle"/></title>
	  
	  <style type="text/css">
      #importLDAPUsersImportFormContainer .ixTableRow{
        padding-top:4px;
        cursor:normal;
      }
      
      #importLDAPUsersImportFormContainer .ixTableRow:hover{
        background:#D7E3F9;
        -moz-border-radius:3px;
        -webkit-border-radius:3px;
        border-radius:3px;
      }
	  </style>
	  
	  <script type="text/javascript">
	    function onLoad(event) {
	      var tabControl = new IxProtoTabs($('tabs'));
	      var importTable = new IxTable($('importTableContainer'), {
	        id : "importTable",
	        columns : [{
	          header : '<@fmt.message key="system.importldapusers.importTableImportHeader"/>',
	          left : 8,
	          width: 20,
	          dataType: 'checkbox',
	          editable: true,
	          paramName: 'import'
	        }, {
	          header : '<@fmt.message key="system.importldapusers.importTableUsernameHeader"/>',
	          left : 32,
	          right: 134,
	          dataType: 'text',
	          editable: false
	        }, {
	          header : '<@fmt.message key="system.importldapusers.importTableFirstNameHeader"/>',
	          left : 272,
	          width : 204,
	          dataType: 'text',
	          editable: true,
	          paramName: 'firstName'
	        }, {
	          header : '<@fmt.message key="system.importldapusers.importTableLastNameNameHeader"/>',
	          left : 480,
	          width : 204,
	          dataType : 'text',
	          editable: true,
	          paramName: 'lastName'
	        }, {
	          header : '<@fmt.message key="system.importldapusers.importTableEmailHeader"/>',
	          left : 688,
	          width : 204,
	          dataType: 'text',
	          editable: true,
	          paramName: 'email'
	        }, {
	          header : '<@fmt.message key="system.importldapusers.importTableRoleHeader"/>',
	          left : 896,
	          width : 200,
	          dataType: 'select',
	          editable: true,
	          paramName: 'role', 
	          options: [
	            {
	              value: 'GUEST', 
	              text: '<@fmt.message key="system.importldapusers.importTableRoleGuest"/>'
	            },
	            {
	              value: 'USER', 
	              text: '<@fmt.message key="system.importldapusers.importTableRoleUser"/>'
	            },
	            {
	              value: 'MANAGER', 
	              text: '<@fmt.message key="system.importldapusers.importTableRoleManager"/>'
	            },
	            {
	              value: 'ADMINISTRATOR',
	              text: '<@fmt.message key="system.importldapusers.importTableRoleAdministrator"/>'
	            }
	          ]
	        }, {
	          dataType: 'hidden',
	          paramName: 'id'
	        }]
	      });
	
	      <#list users as user>
	        <#if (user.email?length > 0)>
	          importTable.addRow([true, '${user.username?html}', '${user.firstName?html}', '${user.lastName?html}', '${user.email?html}', 'USER', '${user.id?html}']); 
          <#else>
            importTable.addRow([false, '${user.username?html}', '${user.firstName?html}', '${user.lastName?html}', '${user.email?html}', 'USER', '${user.id?html}']); 
          </#if>
	      </#list>
	    }
	  </script>
  
  </head>
  <body onload="onLoad(event)">
    <@include_page path="/templates/generic/header.jsp"/> 
	<h1 class="genericPageHeader"><@fmt.message key="system.importldapusers.pageTitle" /></h1>
	  
	<div id="importLDAPUsersImportFormContainer"> 
      <div class="genericFormContainer"> 
        <div class="tabLabelsContainer" id="tabs">
          <a class="tabLabel" href="#importLDAPUsers">
            <@fmt.message key="system.importldapusers.tabLabelImportLDAPUsers"/>
          </a>
        </div>
        
        <form action="importldapusers.page" method="post">
          <div id="importLDAPUsers" class="tabContent">
  		    <div id="importTableContainer"></div>
		  </div>
	      <div class="genericFormSubmitSectionOffTab">
            <input type="submit" value="<@fmt.message key="system.importldapusers.importButton"/>">
          </div>
		</form>
		
	  </div>
	</div>
    <@include_page path="/templates/generic/footer.jsp"/> 
  </body>
</html>