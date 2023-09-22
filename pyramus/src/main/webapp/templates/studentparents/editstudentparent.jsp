<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="fi.otavanopisto.pyramus.domainmodel.users.Role" %>

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

      function onLoad(event) {
        var tabControl = new IxProtoTabs($('tabs'));
        
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

      }

      function setupRelatedCommandsBasic() {
        var relatedActionsHoverMenu = new IxHoverMenu($('basicRelatedActionsHoverMenuContainer'), {
          text: '<fmt:message key="users.editUser.basicTabRelatedActionsLabel"/>'
        });
    
        <c:if test="${loggedUserRoles.contains(Role.ADMINISTRATOR) && !user.roles.contains(Role.ADMINISTRATOR)}">
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

        <form action="editstudentparent.json" method="post" ix:jsonform="true" ix:useglasspane="true">
          <div class="tabLabelsContainer" id="tabs">
            <a class="tabLabel" href="#basic">
              <fmt:message key="users.editUser.tabLabelEditUser"/>
            </a>
          </div>
    
          <div id="basic" class="tabContent">    
            <input type="hidden" name="userId" value="${user.id}"/>
            
            <div id="basicRelatedActionsHoverMenuContainer" class="tabRelatedActionsContainer"></div>
            
            <c:choose>
              <c:when test="${loggedUserRoles.contains(Role.ADMINISTRATOR)}">
                <div class="genericFormSection">
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="users.editUser.accountEnabled"/>
                    <jsp:param name="helpLocale" value="users.editUser.accountEnabledHelp"/>
                  </jsp:include>                  
                  <select name="accountActive" class="required">
                    <option value="true" ${user.accountEnabled ? 'selected="selected"' : ''}><fmt:message key="users.editUser.accountEnabledStatus.active"/></option>
                    <option value="false" ${not user.accountEnabled ? 'selected="selected"' : ''}><fmt:message key="users.editUser.accountEnabledStatus.inactive"/></option>
                  </select>
                </div>
              </c:when>
            </c:choose>

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

            <c:if test="${hasInternalAuthenticationStrategies}">
              <div id="editUserCredentialsContainer">
                <div class="genericFormSection">  
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="users.editUser.usernameTitle"/>
                    <jsp:param name="helpLocale" value="users.editUser.usernameHelp"/>
                  </jsp:include>                  
                  <input type="text" autocomplete="new-username" name="username" value="${username}" size="30">
                </div>
                
                <div class="genericFormSection">  
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="users.editUser.password1Title"/>
                    <jsp:param name="helpLocale" value="users.editUser.password1Help"/>
                  </jsp:include>                  
                  <input type="password" autocomplete="new-password" class="equals equals-password2" name="password1" value="" size="30">
                </div>
                
                <div class="genericFormSection">  
                  <jsp:include page="/templates/generic/fragments/formtitle.jsp">
                    <jsp:param name="titleLocale" value="users.editUser.password2Title"/>
                    <jsp:param name="helpLocale" value="users.editUser.password2Help"/>
                  </jsp:include>                  
                  <input type="password" autocomplete="new-password" class="equals equals-password1" name="password2" value="" size="30">
                </div>
              </div>
            </c:if>

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