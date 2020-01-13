<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title><fmt:message key="users.createUser.pageTitle"></fmt:message></title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>
    
    <script type="text/javascript">
      function addEmailTableRow(values) {
        getIxTableById('emailTable').addRow(values || ['', <c:out value="${contactTypes[0].id}" />, '', '', '']);
      };
  
      function addPhoneTableRow(values) {
        getIxTableById('phoneTable').addRow(values || ['', <c:out value="${contactTypes[0].id}" />, '', '', '']);
      };
  
      function addAddressTableRow(values) {
        getIxTableById('addressTable').addRow(values || ['', <c:out value="${contactTypes[0].id}" />, '', '', '', '', '', '', '']);
      };
          
      function setupTags() {
        JSONRequest.request("tags/getalltags.json", {
          onSuccess: function (jsonResponse) {
            new Autocompleter.Local("tags", "tags_choices", jsonResponse.tags, {
              tokens: [',', '\n', ' ']
            });
          }
        });   
      }

      function canUpdateCredentials(strategyName) {
        <c:if test="${fn:length(authenticationProviders) gt 0}">
          switch (strategyName) {
	          <c:forEach var="authenticationProvider" items="${authenticationProviders}">
	            <c:choose>
	              <c:when test="${authenticationProvider.active eq true}">
	                case '${authenticationProvider.name}':
	                  return ${authenticationProvider.canUpdateCredentials};
	              </c:when>
	            </c:choose>
	          </c:forEach>
	        }
        </c:if>
        return false;
      }
      
      function onLoad(event) {
        var tabControl = new IxProtoTabs($('tabs'));
        setupTags();

        var emails_values = undefined;
        var address_values = undefined;
        var phone_values = undefined;
        
        if (window.JSDATA) {
          emails_values = JSDATA["createuser_emails"] ? JSDATA["createuser_emails"].evalJSON() : undefined;
          address_values = JSDATA["createuser_addresses"] ? JSDATA["createuser_addresses"].evalJSON() : undefined;
          phone_values = JSDATA["createuser_phones"] ? JSDATA["createuser_phones"].evalJSON() : undefined;
        }
        
        // E-mail address

        var emailTable = new IxTable($('emailTable'), {
          id : "emailTable",
          columns : [{
            left : 0,
            width : 30,
            dataType: 'radiobutton',
            editable: true,
            paramName: 'defaultAddress',
            tooltip: '<fmt:message key="users.createUser.emailTableDefaultTooltip"/>',
          }, {
            header : '<fmt:message key="users.createUser.emailTableTypeHeader"/>',
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
            header : '<fmt:message key="users.createUser.emailTableAddressHeader"/>',
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
            tooltip: '<fmt:message key="users.createUser.emailTableAddTooltip"/>',
            onclick: function (event) {
              addEmailTableRow();
            }
          }, {
            width: 30,
            left: 396,
            dataType: 'button',
            paramName: 'removeButton',
            hidden: true,
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="users.createUser.emailTableRemoveTooltip"/>',
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

        if (emails_values && emails_values.length > 0) {
          for (var i = 0; i < emails_values.length; i++) {
            addEmailTableRow([emails_values[i].defaultAddress, 
                              emails_values[i].contactType != undefined ? emails_values[i].contactType.id : '',
                              emails_values[i].address, '', '']);
          }
        } else {
          addEmailTableRow();
          emailTable.setCellValue(0, 0, true);
        }

        // Addresses

        var addressTable = new IxTable($('addressTable'), {
          id : "addressTable",
          columns : [{
            left : 0,
            width : 30,
            dataType: 'radiobutton',
            editable: true,
            paramName: 'defaultAddress',
            tooltip: '<fmt:message key="users.createUser.addressTableDefaultTooltip"/>',
          }, {
            header : '<fmt:message key="users.createUser.addressTableTypeHeader"/>',
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
            header : '<fmt:message key="users.createUser.addressTableNameHeader"/>',
            left : 188,
            width : 150,
            dataType: 'text',
            editable: true,
            paramName: 'name'
          }, {
            header : '<fmt:message key="users.createUser.addressTableStreetHeader"/>',
            left : 344,
            width : 150,
            dataType: 'text',
            editable: true,
            paramName: 'street'
          }, {
            header : '<fmt:message key="users.createUser.addressTablePostalCodeHeader"/>',
            left : 502,
            width : 100,
            dataType: 'text',
            editable: true,
            paramName: 'postal'
          }, {
            header : '<fmt:message key="users.createUser.addressTableCityHeader"/>',
            left : 610,
            width : 150,
            dataType: 'text',
            editable: true,
            paramName: 'city'
          }, {
            header : '<fmt:message key="users.createUser.addressTableCountryHeader"/>',
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
            tooltip: '<fmt:message key="users.createUser.addressTableAddTooltip"/>',
            onclick: function (event) {
              addAddressTableRow();
            }
          }, {
            width: 30,
            left: 874,
            dataType: 'button',
            paramName: 'removeButton',
            hidden: true,
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="users.createUser.addressTableRemoveTooltip"/>',
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

        if (address_values && address_values.length > 0) {
          for (var i = 0; i < address_values.length; i++) {
            addAddressTableRow([address_values[i].defaultAddress, 
                                address_values[i].contactType != undefined ? address_values[i].contactType.id : '', 
                                address_values[i].name != undefined ? address_values[i].name : '', 
                                address_values[i].streetAddress, 
                                address_values[i].postalCode, 
                                address_values[i].city, 
                                address_values[i].country, '', '']);
          }
        } else {
          addAddressTableRow();
          addressTable.setCellValue(0, 0, true);
        }

        // Phone numbers

        var phoneTable = new IxTable($('phoneTable'), {
          id : "phoneTable",
          columns : [{
            left : 0,
            width : 30,
            dataType: 'radiobutton',
            editable: true,
            paramName: 'defaultNumber',
            tooltip: '<fmt:message key="users.createUser.phoneTableDefaultTooltip"/>',
          }, {
            header : '<fmt:message key="users.createUser.phoneTableTypeHeader"/>',
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
            header : '<fmt:message key="users.createUser.phoneTableNumberHeader"/>',
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
            tooltip: '<fmt:message key="users.createUser.phoneTableAddTooltip"/>',
            onclick: function (event) {
              addPhoneTableRow();
            }
          }, {
            width: 30,
            left: 396,
            dataType: 'button',
            paramName: 'removeButton',
            hidden: true,
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="users.createUser.phoneTableRemoveTooltip"/>',
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
        
        if (phone_values && phone_values.length > 0) {
          for (var i = 0; i < phone_values.length; i++) {
            addPhoneTableRow([phone_values[i].defaultNumber, 
                              phone_values[i].contactType != undefined ? phone_values[i].contactType.id : '',
                              phone_values[i].number, '', '']);
          }
        } else {
          addPhoneTableRow();
          phoneTable.setCellValue(0, 0, true);
        }
      };
    </script>
    
  </head>
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
  
    <h1 class="genericPageHeader"><fmt:message key="users.createUser.pageTitle" /></h1>
  
    <div id="createUserCreateFormContainer"> 
      <div class="genericFormContainer"> 
        <div class="tabLabelsContainer" id="tabs">
          <a class="tabLabel" href="#createUser">
            <fmt:message key="users.createUser.tabLabelCreateUser"/>
          </a>
        </div>
        
        <form action="createuser.json" method="post" ix:jsonform="true">
          <input type="hidden" name="personId" value="${person.id}" />
        
          <div id="createUser" class="tabContent">

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="terms.organization"/>
                <jsp:param name="helpLocale" value="users.createUser.organizationHelp"/>
              </jsp:include>                  
              <select name="organizationId" class="required">
                <option value=""></option>
                <c:forEach items="${organizations}" var="organization">
                  <option value="${organization.id}">${organization.name}</option>
                </c:forEach>
              </select>
            </div>

            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="users.createUser.firstNameTitle"/>
                <jsp:param name="helpLocale" value="users.createUser.firstNameHelp"/>
              </jsp:include>                  
              <input type="text" name="firstName" size="20" class="required" value="${student.firstName}" />
            </div>
            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="users.createUser.lastNameTitle"/>
                <jsp:param name="helpLocale" value="users.createUser.lastNameHelp"/>
              </jsp:include>                  
              <input type="text" name="lastName" size="30" class="required" value="${student.lastName}" />
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="users.createUser.titleTitle"/>
                <jsp:param name="helpLocale" value="users.createUser.titleHelp"/>
              </jsp:include>                  
              <input type="text" name="title" size="30">
            </div>
        
            <c:if test="${hasInternalAuthenticationStrategies}">
              <div id="createUserCredentialsContainer">
                <div class="genericFormSection">  
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="users.createUser.usernameTitle"/>
                    <jsp:param name="helpLocale" value="users.createUser.usernameHelp"/>
                  </jsp:include>                  
                  <input type="text" autocomplete="new-username" name="username" size="30">
                </div>
                
                <div class="genericFormSection">  
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="users.createUser.password1Title"/>
                    <jsp:param name="helpLocale" value="users.createUser.password1Help"/>
                  </jsp:include>                  
                  <input type="password" autocomplete="new-password" class="equals equals-password2" name="password1" value="" size="30">
                </div>
                
                <div class="genericFormSection">  
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="users.createUser.password2Title"/>
                    <jsp:param name="helpLocale" value="users.createUser.password2Help"/>
                  </jsp:include>                  
                  <input type="password" autocomplete="new-password" class="equals equals-password1" name="password2" value="" size="30">
                </div>
              </div>
            </c:if>
            
            <div class="genericFormSection">
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="users.createUser.tagsTitle"/>
                <jsp:param name="helpLocale" value="users.createUser.tagsHelp"/>
              </jsp:include>
              <input type="text" id="tags" name="tags" size="40"/>
              <div id="tags_choices" class="autocomplete_choices"></div>
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="users.createUser.addressesTitle"/>
                <jsp:param name="helpLocale" value="users.createUser.addressesHelp"/>
              </jsp:include>                                         
              <div id="addressTable"></div>
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="users.createUser.emailTableEmailsTitle"/>
                <jsp:param name="helpLocale" value="users.createUser.emailTableEmailsHelp"/>
              </jsp:include>                                         
              <div id="emailTable"></div>
            </div>

            <div class="genericFormSection">  
              <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                <jsp:param name="titleLocale" value="users.createUser.phoneNumbersTitle"/>
                <jsp:param name="helpLocale" value="users.createUser.phoneNumbersHelp"/>
              </jsp:include>                                         
              <div id="phoneTable"></div>
            </div>

            <c:choose>
              <c:when test="${loggedUserRole == 'ADMINISTRATOR'}">
                <div class="genericFormSection">  
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="users.createUser.roleTitle"/>
                    <jsp:param name="helpLocale" value="users.createUser.roleHelp"/>
                  </jsp:include>                  
                  <select name="role">
                    <option value="10"><fmt:message key="users.createUser.roleClosedTitle"/></option>
                    <option value="1"><fmt:message key="users.createUser.roleGuestTitle"/></option>
                    <option value="2"><fmt:message key="users.createUser.roleUserTitle"/></option>
                    <option value="7"><fmt:message key="users.createUser.roleTeacherTitle"/></option>
                    <option value="8"><fmt:message key="users.createUser.roleStudyGuiderTitle"/></option>
                    <option value="9"><fmt:message key="users.createUser.roleStudyProgrammeLeaderTitle"/></option>
                    <option value="3"><fmt:message key="users.createUser.roleManagerTitle"/></option>
                    <option value="4"><fmt:message key="users.createUser.roleAdministratorTitle"/></option>
                  </select>
                </div>
              </c:when>
              <c:otherwise>
                <input type="hidden" name="role" value="1"/>
              </c:otherwise>
            </c:choose>
          </div>
          <div class="genericFormSubmitSectionOffTab">
            <input type="submit" name="login" value="<fmt:message key="users.createUser.submitButton"/>" class="formvalid">
          </div>
        </form>
      </div>
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>