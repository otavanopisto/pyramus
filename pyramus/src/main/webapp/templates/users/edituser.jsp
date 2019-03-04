<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title><fmt:message key="users.editUser.pageTitle"></fmt:message></title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/hovermenu_support.jsp"></jsp:include>
    
    <script type="text/javascript">

      function addAddressTableRow(addressTable) {
        addressTable.addRow([-1, '', <c:out value="${contactTypes[0].id}" />, '', '', '', '', '', '', '']);
      }

      function addEmailTableRow() {
        getIxTableById('emailTable').addRow([-1, '', <c:out value="${contactTypes[0].id}" />, '', '', '']);
      }

      function addPhoneTableRow(phoneTable) {
        phoneTable.addRow([-1, '', <c:out value="${contactTypes[0].id}" />, '', '', '']);
      }

      function setupUserVariablesTable() {
        var variablesTable = new IxTable($('variablesTableContainer'), {
          id : "variablesTable",
          columns : [{
            left: 8,
            width: 30,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            onclick: function (event) {
              var table = event.tableComponent;
              var valueColumn = table.getNamedColumnIndex('value');
              table.setCellEditable(event.row, valueColumn, table.isCellEditable(event.row, valueColumn) == false);
            }
          }, {
            dataType : 'hidden',
            editable: false,
            paramName: 'key'
          },{
            left : 38,
            width: 150,
            dataType : 'text',
            editable: false,
            paramName: 'name'
          }, {
            left : 188,
            width : 750,
            dataType: 'text',
            editable: false,
            paramName: 'value'
          }]
        });
        
        var variables = JSDATA["variables"].evalJSON();
        if (variables) {
          for (var i = 0, l = variables.length; i < l; i++) {
            var rowNumber = variablesTable.addRow([
              '',
              variables[i].key,
              variables[i].name,
              variables[i].value
            ]);

            switch (variables[i].type) {
              case 'NUMBER':
                variablesTable.setCellDataType(rowNumber, 3, 'number');
              break;
              case 'DATE':
                variablesTable.setCellDataType(rowNumber, 3, 'date');
              break;
              case 'BOOLEAN':
                variablesTable.setCellDataType(rowNumber, 3, 'checkbox');
              break;
              default:
                variablesTable.setCellDataType(rowNumber, 3, 'text');
              break;
            }
          }
        }
      }
          
      function setupStaffMemberPropertiesTable() {
        var variablesTable = new IxTable($('propertiesTableContainer'), {
          id : "propertiesTable",
          columns : [{
            left: 8,
            width: 30,
            dataType: 'button',
            imgsrc: GLOBAL_contextPath + '/gfx/accessories-text-editor.png',
            onclick: function (event) {
              var table = event.tableComponent;
              var valueColumn = table.getNamedColumnIndex('value');
              table.setCellEditable(event.row, valueColumn, table.isCellEditable(event.row, valueColumn) == false);
            }
          }, {
            dataType : 'hidden',
            editable: false,
            paramName: 'key'
          },{
            left : 38,
            width: 150,
            dataType : 'text',
            editable: false,
            paramName: 'name'
          }, {
            left : 188,
            width : 750,
            dataType: 'text',
            editable: false,
            paramName: 'value'
          }]
        });
        
        var variables = JSDATA["properties"].evalJSON();
        if (variables) {
          for (var i = 0, l = variables.length; i < l; i++) {
            var rowNumber = variablesTable.addRow([
              '',
              variables[i].key,
              variables[i].name,
              variables[i].value
            ]);

            switch (variables[i].type) {
              case 'NUMBER':
                variablesTable.setCellDataType(rowNumber, 3, 'number');
              break;
              case 'DATE':
                variablesTable.setCellDataType(rowNumber, 3, 'date');
              break;
              case 'BOOLEAN':
                variablesTable.setCellDataType(rowNumber, 3, 'checkbox');
              break;
              default:
                variablesTable.setCellDataType(rowNumber, 3, 'text');
              break;
            }
          }
        }
      }
          
      function setupTags() {
        JSONRequest.request("tags/getalltags.json", {
          onSuccess: function (jsonResponse) {
            new Autocompleter.Local("tags", "tags_choices", jsonResponse.tags, {
              tokens: [',', '\n', ' ']
            });
          }
        });   
      }
      
      function onLoad(event) {
        var tabControl = new IxProtoTabs($('tabs'));
        
        setupTags();
        setupRelatedCommandsBasic();
            
        var addressTable = new IxTable($('addressTable'), {
          id : "addressTable",
          columns : [{
            dataType : 'hidden',
            left : 0,
            width : 0,
            paramName : 'addressId'
          }, {
            left : 0,
            width : 30,
            dataType: 'radiobutton',
            editable: true,
            paramName: 'defaultAddress',
            tooltip: '<fmt:message key="users.editUser.addressTableDefaultTooltip"/>',
          }, {
            header : '<fmt:message key="users.editUser.addressTableTypeHeader"/>',
            left : 30,
            width : 150,
            dataType: 'select',
            editable: true,
            paramName: 'contactTypeId',
            options: [
              <c:forEach var="contactType" items="${contactTypes}" varStatus="vs">
                {text: "${contactType.name}", value: ${contactType.id}}
                <c:if test="${not vs.last}">,</c:if>
              </c:forEach>
            ]
          }, {
            header : '<fmt:message key="users.editUser.addressTableNameHeader"/>',
            left : 188,
            width : 150,
            dataType: 'text',
            editable: true,
            paramName: 'name'
          }, {
            header : '<fmt:message key="users.editUser.addressTableStreetHeader"/>',
            left : 344,
            width : 150,
            dataType: 'text',
            editable: true,
            paramName: 'street'
          }, {
            header : '<fmt:message key="users.editUser.addressTablePostalCodeHeader"/>',
            left : 502,
            width : 100,
            dataType: 'text',
            editable: true,
            paramName: 'postal'
          }, {
            header : '<fmt:message key="users.editUser.addressTableCityHeader"/>',
            left : 610,
            width : 150,
            dataType: 'text',
            editable: true,
            paramName: 'city'
          }, {
            header : '<fmt:message key="users.editUser.addressTableCountryHeader"/>',
            left : 768,
            width : 100,
            dataType: 'text',
            editable: true,
            paramName: 'country'
          }, {
            width: 30,
            left: 874,
            dataType: 'button',
            paramName: 'addButton',
            hidden: true,
            imgsrc: GLOBAL_contextPath + '/gfx/list-add.png',
            tooltip: '<fmt:message key="users.editUser.addressTableAddTooltip"/>',
            onclick: function (event) {
              addAddressTableRow(event.tableComponent);
            }
          }, {
            width: 30,
            left: 874,
            dataType: 'button',
            paramName: 'removeButton',
            hidden: true,
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="users.editUser.addressTableRemoveTooltip"/>',
            onclick: function (event) {
              event.tableComponent.deleteRow(event.row);
            }
          }]
        });

        addressTable.addListener("rowAdd", function (event) {
          var addressTable = event.tableComponent; 
          var enabledButton = event.row == 0 ? 'addButton' : 'removeButton';
          addressTable.showCell(event.row, addressTable.getNamedColumnIndex(enabledButton));
        });

        <c:forEach var="address" items="${user.contactInfo.addresses}">
          addressTable.addRow([
            ${address.id},
            ${address.defaultAddress},
            ${address.contactType.id},
            '${fn:escapeXml(address.name)}',
            '${fn:escapeXml(address.streetAddress)}',
            '${fn:escapeXml(address.postalCode)}',
            '${fn:escapeXml(address.city)}',
            '${fn:escapeXml(address.country)}',
            '',
            '']);
        </c:forEach>
  
        if (addressTable.getRowCount() == 0) {
          addAddressTableRow(addressTable);
          addressTable.setCellValue(0, 1, true);
        }

        var emailTable = new IxTable($('emailTable'), {
          id : "emailTable",
          columns : [ {
            dataType : 'hidden',
            left : 0,
            width : 0,
            paramName : 'emailId'
          }, {
            left : 0,
            width : 30,
            dataType: 'radiobutton',
            editable: true,
            paramName: 'defaultAddress',
            tooltip: '<fmt:message key="users.editUser.emailTableDefaultTooltip"/>',
          }, {
            header : '<fmt:message key="users.editUser.emailTableTypeHeader"/>',
            width: 150,
            left : 30,
            dataType: 'select',
            editable: true,
            paramName: 'contactTypeId',
            options: [
              <c:forEach var="contactType" items="${contactTypes}" varStatus="vs">
                {text: "${contactType.name}", value: ${contactType.id}}
                <c:if test="${not vs.last}">,</c:if>
              </c:forEach>
            ]
          }, {
            header : '<fmt:message key="users.editUser.emailTableAddressHeader"/>',
            left : 188,
            width : 200,
            dataType: 'text',
            editable: true,
            paramName: 'email',
            editorClassNames: 'email'
          }, {
            width: 30,
            left: 396,
            dataType: 'button',
            paramName: 'addButton',
            hidden: true,
            imgsrc: GLOBAL_contextPath + '/gfx/list-add.png',
            tooltip: '<fmt:message key="users.editUser.emailTableAddTooltip"/>',
            onclick: function (event) {
              addEmailTableRow(event.tableComponent);
            }
          }, {
            width: 30,
            left: 396,
            dataType: 'button',
            paramName: 'removeButton',
            hidden: true,
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="users.editUser.emailTableRemoveTooltip"/>',
            onclick: function (event) {
              event.tableComponent.deleteRow(event.row);
            }
          }]
        });
        
        emailTable.addListener("rowAdd", function (event) {
          var emailTable = event.tableComponent; 
          var enabledButton = event.row == 0 ? 'addButton' : 'removeButton';
          emailTable.showCell(event.row, emailTable.getNamedColumnIndex(enabledButton));
        });

        <c:forEach var="email" items="${user.contactInfo.emails}">
          emailTable.addRow([
            ${email.id},
            ${email.defaultAddress},
            ${email.contactType.id},
            '${fn:escapeXml(email.address)}',
            '',
            '']);
        </c:forEach>

        if (emailTable.getRowCount() == 0) {
          addEmailTableRow();
          emailTable.setCellValue(0, 1, true);
        }

        var phoneTable = new IxTable($('phoneTable'), {
          id : "phoneTable",
          columns : [ {
            dataType : 'hidden',
            left : 0,
            width : 0,
            paramName : 'phoneId'
          }, {
            left : 0,
            width : 30,
            dataType: 'radiobutton',
            editable: true,
            paramName: 'defaultNumber',
            tooltip: '<fmt:message key="users.editUser.phoneTableDefaultTooltip"/>',
          }, {
            header : '<fmt:message key="users.editUser.phoneTableTypeHeader"/>',
            width: 150,
            left : 30,
            dataType: 'select',
            editable: true,
            paramName: 'contactTypeId',
            options: [
              <c:forEach var="contactType" items="${contactTypes}" varStatus="vs">
                {text: "${contactType.name}", value: ${contactType.id}}
                <c:if test="${not vs.last}">,</c:if>
              </c:forEach>
            ]
          }, {
            header : '<fmt:message key="users.editUser.phoneTableNumberHeader"/>',
            left : 188,
            width : 200,
            dataType: 'text',
            editable: true,
            paramName: 'phone'
          }, {
            width: 30,
            left: 396,
            dataType: 'button',
            paramName: 'addButton',
            hidden: true,
            imgsrc: GLOBAL_contextPath + '/gfx/list-add.png',
            tooltip: '<fmt:message key="users.editUser.phoneTableAddTooltip"/>',
            onclick: function (event) {
              addPhoneTableRow(event.tableComponent);
            }
          }, {
            width: 30,
            left: 396,
            dataType: 'button',
            paramName: 'removeButton',
            hidden: true,
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="users.editUser.phoneTableRemoveTooltip"/>',
            onclick: function (event) {
              event.tableComponent.deleteRow(event.row);
            }
          }]
        });

        phoneTable.addListener("rowAdd", function (event) {
          var phoneTable = event.tableComponent; 
          var enabledButton = event.row == 0 ? 'addButton' : 'removeButton';
          phoneTable.showCell(event.row, phoneTable.getNamedColumnIndex(enabledButton));
        });

        <c:forEach var="phone" items="${user.contactInfo.phoneNumbers}">
          phoneTable.addRow([
            ${phone.id},
            ${phone.defaultNumber},
            ${phone.contactType.id},
            '${fn:escapeXml(phone.number)}',
            '',
            '']);
        </c:forEach>

        if (phoneTable.getRowCount() == 0) {
          addPhoneTableRow(phoneTable);
          phoneTable.setCellValue(0, 1, true);
        }

        <c:choose>
          <c:when test="${(loggedUserRole == 'ADMINISTRATOR')}">
            setupUserVariablesTable();
            setupStaffMemberPropertiesTable();
          </c:when>
        </c:choose>
      }

      function setupRelatedCommandsBasic() {
        var relatedActionsHoverMenu = new IxHoverMenu($('basicRelatedActionsHoverMenuContainer'), {
          text: '<fmt:message key="users.editUser.basicTabRelatedActionsLabel"/>'
        });
    
        relatedActionsHoverMenu.addItem(new IxHoverMenuClickableItem({
          iconURL: GLOBAL_contextPath + '/gfx/icons/16x16/actions/edit-work-resource.png',
          text: '<fmt:message key="users.editUser.basicTabRelatedActionCreateResourceLabel"/>',
          onclick: function (event) {
            redirectTo(GLOBAL_contextPath + '/resources/createworkresource.page?name=' + encodeURIComponent('${user.lastName}, ${user.firstName}'));
          }
        }));
        

        <c:if test="${loggedUserRole == 'ADMINISTRATOR' && user.role ne 'ADMINISTRATOR'}">
        relatedActionsHoverMenu.addItem(new IxHoverMenuClickableItem({
          iconURL: GLOBAL_contextPath + '/gfx/icons/16x16/apps/attention.png',
          text: '<fmt:message key="users.editUser.basicTabRelatedActionsPoseAsLabel"/>',
          onclick: function (event) {
            JSONRequest.request("users/pose.json", {
              parameters: {
                userId: ${user.id}
              },
              onSuccess: function (jsonResponse) {
                window.location = GLOBAL_contextPath + "/";
              }
            }); 
          }  
        }));
        </c:if>     
      }
    </script>
  </head>

  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
  
    <h1 class="genericPageHeader"><fmt:message key="users.editUser.pageTitle" /></h1>
  
    <div id="editUserEditFormContainer"> 
      <div class="genericFormContainer"> 

        <form action="edituser.json" method="post" ix:jsonform="true" ix:useglasspane="true">
          <div class="tabLabelsContainer" id="tabs">
            <a class="tabLabel" href="#basic">
              <fmt:message key="users.editUser.tabLabelEditUser"/>
            </a>
          </div>
    
          <div id="basic" class="tabContent">    
            <input type="hidden" name="userId" value="${user.id}"/>
            
            <div id="basicRelatedActionsHoverMenuContainer" class="tabRelatedActionsContainer"></div>
            
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="terms.organization"/>
                <jsp:param name="helpLocale" value="users.editUser.organizationHelp"/>
              </jsp:include>                  
              <select name="organizationId">
                <option value=""></option>
                <c:forEach items="${organizations}" var="organization">
                  <c:choose>
                    <c:when test="${user.organization.id == organization.id}">
                      <option value="${organization.id}" selected="selected">${organization.name}</option>
                    </c:when>
                    <c:otherwise>
                      <option value="${organization.id}">${organization.name}</option>
                    </c:otherwise>
                  </c:choose>
                </c:forEach>
              </select>
            </div>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="users.editUser.firstNameTitle"/>
                <jsp:param name="helpLocale" value="users.editUser.firstNameHelp"/>
              </jsp:include>                  
              <input type="text" name="firstName" value="${fn:escapeXml(user.firstName)}" size="20" class="required">
            </div>
  
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="users.editUser.lastNameTitle"/>
                <jsp:param name="helpLocale" value="users.editUser.lastNameHelp"/>
              </jsp:include>                  
              <input type="text" name="lastName" value="${fn:escapeXml(user.lastName)}" size="30" class="required">
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="users.editUser.titleTitle"/>
                <jsp:param name="helpLocale" value="users.editUser.titleHelp"/>
              </jsp:include>                  
              <input type="text" name="title" value="${fn:escapeXml(user.title)}" size="30">
            </div>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="users.editUser.ssnTitle"/>
                <jsp:param name="helpLocale" value="users.editUser.ssnHelp"/>
              </jsp:include>
              <input type="text" name="ssn" value="${fn:escapeXml(user.person.socialSecurityNumber)}" size="11">
            </div>

            <c:if test="${hasInternalAuthenticationStrategies}">
              <div id="editUserCredentialsContainer">
                <div class="genericFormSection">  
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="users.editUser.usernameTitle"/>
                    <jsp:param name="helpLocale" value="users.editUser.usernameHelp"/>
                  </jsp:include>                  
                  <input type="text" autocomplete="off" name="username" value="${username}" size="30">
                </div>
                
                <div class="genericFormSection">  
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="users.editUser.password1Title"/>
                    <jsp:param name="helpLocale" value="users.editUser.password1Help"/>
                  </jsp:include>                  
                  <input type="password" autocomplete="off" class="equals equals-password2" name="password1" value="" size="30">
                </div>
                
                <div class="genericFormSection">  
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="users.editUser.password2Title"/>
                    <jsp:param name="helpLocale" value="users.editUser.password2Help"/>
                  </jsp:include>                  
                  <input type="password" autocomplete="off" class="equals equals-password1" name="password2" value="" size="30">
                </div>
              </div>
            </c:if>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="users.editUser.tagsTitle"/>
                <jsp:param name="helpLocale" value="users.editUser.tagsHelp"/>
              </jsp:include>
              <input type="text" id="tags" name="tags" size="40" value="${fn:escapeXml(tags)}"/>
              <div id="tags_choices" class="autocomplete_choices"></div>
            </div> 
  
            <div class="genericFormSection">                
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="users.editUser.addressesTitle"/>
                <jsp:param name="helpLocale" value="users.editUser.addressesHelp"/>
              </jsp:include>
              <div id="addressTable"></div>
            </div>

            <div class="genericFormSection">               
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="users.editUser.emailTableEmailsTitle"/>
                <jsp:param name="helpLocale" value="users.editUser.emailTableEmailsHelp"/>
              </jsp:include>
              <div id="emailTable"></div>
            </div>

            <div class="genericFormSection">                
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="users.editUser.phoneNumbersTitle"/>
                <jsp:param name="helpLocale" value="users.editUser.phoneNumbersHelp"/>
              </jsp:include>
              <div id="phoneTable"></div>
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="users.editUser.roleTitle"/>
                <jsp:param name="helpLocale" value="users.editUser.roleHelp"/>
              </jsp:include>
              <c:choose>
                <c:when test="${loggedUserRole == 'ADMINISTRATOR'}">
                  <select name="role">
                    <option value="10" <c:if test="${user.role == 'CLOSED'}">selected="selected"</c:if>><fmt:message key="users.editUser.roleClosedTitle"/></option>
                    <option value="1" <c:if test="${user.role == 'GUEST'}">selected="selected"</c:if>><fmt:message key="users.editUser.roleGuestTitle"/></option>
                    <option value="2" <c:if test="${user.role == 'USER'}">selected="selected"</c:if>><fmt:message key="users.editUser.roleUserTitle"/></option>
                    <option value="7" <c:if test="${user.role == 'TEACHER'}">selected="selected"</c:if>><fmt:message key="users.editUser.roleTeacherTitle"/></option>
                    <option value="8" <c:if test="${user.role == 'STUDY_GUIDER'}">selected="selected"</c:if>><fmt:message key="users.editUser.roleStudyGuiderTitle"/></option>
                    <option value="9" <c:if test="${user.role == 'STUDY_PROGRAMME_LEADER'}">selected="selected"</c:if>><fmt:message key="users.editUser.roleStudyProgrammeLeaderTitle"/></option>
                    <option value="3" <c:if test="${user.role == 'MANAGER'}">selected="selected"</c:if>><fmt:message key="users.editUser.roleManagerTitle"/></option>
                    <option value="4" <c:if test="${user.role == 'ADMINISTRATOR'}">selected="selected"</c:if>><fmt:message key="users.editUser.roleAdministratorTitle"/></option>
                  </select>
                </c:when>
                <c:otherwise>
                  <input type="hidden" name="role" value="${user.role.value}"/>
                  <c:choose>
                    <c:when test="${user.role == 'CLOSED'}"><div><fmt:message key="users.editUser.roleClosedTitle"/></div></c:when>
                    <c:when test="${user.role == 'GUEST'}"><div><fmt:message key="users.editUser.roleGuestTitle"/></div></c:when>
                    <c:when test="${user.role == 'USER'}"><div><fmt:message key="users.editUser.roleUserTitle"/></div></c:when>
                    <c:when test="${user.role == 'TEACHER'}"><div><fmt:message key="users.editUser.roleTeacherTitle"/></div></c:when>
                    <c:when test="${user.role == 'STUDY_GUIDER'}"><div><fmt:message key="users.editUser.roleStudyGuiderTitle"/></div></c:when>
                    <c:when test="${user.role == 'STUDY_PROGRAMME_LEADER'}"><div><fmt:message key="users.editUser.roleStudyProgrammeLeaderTitle"/></div></c:when>
                    <c:when test="${user.role == 'MANAGER'}"><div><fmt:message key="users.editUser.roleManagerTitle"/></div></c:when>
                    <c:when test="${user.role == 'ADMINISTRATOR'}"><div><fmt:message key="users.editUser.roleAdministratorTitle"/></div></c:when>
                  </c:choose>
                </c:otherwise>
              </c:choose>
            </div>
                
            <c:choose>
              <c:when test="${(loggedUserRole == 'ADMINISTRATOR')}">
                <div class="genericFormSection">  
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="users.editUser.propertiesTitle"/>
                    <jsp:param name="helpLocale" value="users.editUser.propertiesHelp"/>
                  </jsp:include>         
                  <div id="propertiesTableContainer"></div>
                </div>

                <div class="genericFormSection">  
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="users.editUser.variablesTitle"/>
                    <jsp:param name="helpLocale" value="users.editUser.variablesHelp"/>
                  </jsp:include>         
                  <div id="variablesTableContainer"></div>
                </div>
              </c:when>
            </c:choose>
          </div>

          <div class="genericFormSubmitSectionOffTab">
            <input type="submit" value="<fmt:message key="users.editUser.saveButton"/>" class="formvalid">
          </div>

        </form>

      </div>
    </div>  

    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>