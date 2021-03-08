<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title><fmt:message key="settings.editOrganization.pageTitle"/></title>

    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonform_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/draftapi_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/locale_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/ajax_support.jsp"></jsp:include>

    <script type="text/javascript">
      function formatDate(date) {
        var d = new Date(date);
        var month = '' + (d.getMonth() + 1);
        var day = '' + d.getDate();
        var year = d.getFullYear();
  
        if (month.length < 2) 
            month = '0' + month;
        if (day.length < 2) 
            day = '0' + day;
  
        return [year, month, day].join('-');
      }
      
      function editOrganizationFormSubmit(event) {
        Event.stop(event);

        var contractPeriods = [];
        var contactPersonTable = getIxTableById('organizationContractPeriodsTable');
        for (var row = 0, l = contactPersonTable.getRowCount(); row < l; row++) {
          var begin = contactPersonTable.getCellValue(row, contactPersonTable.getNamedColumnIndex('begin'));
          var end = contactPersonTable.getCellValue(row, contactPersonTable.getNamedColumnIndex('end'));

          contractPeriods.push({
            begin: begin ? formatDate(begin) : undefined,
            end: end ? formatDate(end) : undefined
          });
        }
        
        var contactPersons = [];
        var contactPersonTable = getIxTableById('organizationContactPersonsTable');
        for (var row = 0, l = contactPersonTable.getRowCount(); row < l; row++) {
          var id = contactPersonTable.getCellValue(row, contactPersonTable.getNamedColumnIndex('id'));
          var type = contactPersonTable.getCellValue(row, contactPersonTable.getNamedColumnIndex('type'));
          var name = contactPersonTable.getCellValue(row, contactPersonTable.getNamedColumnIndex('name'));
          var email = contactPersonTable.getCellValue(row, contactPersonTable.getNamedColumnIndex('email'));
          var phone = contactPersonTable.getCellValue(row, contactPersonTable.getNamedColumnIndex('phone'));
          var title = contactPersonTable.getCellValue(row, contactPersonTable.getNamedColumnIndex('title'));

          contactPersons.push({
            id: id,
            type: type,
            name: name,
            email: email,
            phone: phone,
            title: title
          });
        }

        var organizationId = document.querySelector("#organizationId").value;
        var organizationName = document.querySelector("#name").value;
        var bd_personName = document.querySelector("#billing-details-personName").value;
        var bd_companyName = document.querySelector("#billing-details-companyName").value;
        var bd_streetAddress1 = document.querySelector("#billing-details-streetAddress1").value;
        var bd_streetAddress2 = document.querySelector("#billing-details-streetAddress2").value;
        var bd_postalCode = document.querySelector("#billing-details-postalCode").value;
        var bd_city = document.querySelector("#billing-details-city").value;
        var bd_region = document.querySelector("#billing-details-region").value;
        var bd_country = document.querySelector("#billing-details-country").value;
        var bd_phoneNumber = document.querySelector("#billing-details-phoneNumber").value;
        var bd_emailAddress = document.querySelector("#billing-details-emailAddress").value;
        var bd_companyIdentifier = document.querySelector("#billing-details-companyIdentifier").value;
        var bd_referenceNumber = document.querySelector("#billing-details-referenceNumber").value;
        var bd_electronicBillingAddress = document.querySelector("#billing-details-electronicBillingAddress").value;
        var bd_electronicBillingOperator = document.querySelector("#billing-details-electronicBillingOperator").value;
        var bd_notes = document.querySelector("#billing-details-notes").value;

        var educationTypeId = document.querySelector("#educationType").value;
        var educationType = educationTypeId != "" ? { id: educationTypeId } : undefined;
        
        // Prototypejs fucks up how arrays are serialized so we have to remove the offending function
        delete Array.prototype.toJSON;
        
        axios.put("/organizations/{0}".format(organizationId), {
          "name": organizationName,
          "educationType": educationType,
          "contactPersons": contactPersons,
          "contractPeriods": contractPeriods,
          "billingDetails": {
            "personName": bd_personName,
            "companyName": bd_companyName,
            "streetAddress1": bd_streetAddress1,
            "streetAddress2": bd_streetAddress2,
            "postalCode": bd_postalCode,
            "city": bd_city,
            "region": bd_region,
            "country": bd_country,
            "phoneNumber": bd_phoneNumber,
            "emailAddress": bd_emailAddress,
            "companyIdentifier": bd_companyIdentifier,
            "referenceNumber": bd_referenceNumber,
            "electronicBillingAddress": bd_electronicBillingAddress,
            "electronicBillingOperator": bd_electronicBillingOperator,
            "notes": bd_notes
          }
        }).then(function (response) {
          location.reload();
        });
      }

      function initializeContactPersonsTable() {
        var contactPersonTypeOptions = [
          {
            text: "Pääkäyttäjä/yhteyshenkilö",
            value: "ORGANIZATION_ADMINISTRATOR"
          },
          {
            text: "Todistusyhteyshenkilö",
            value: "ORGANIZATION_REPORT"
          },
          {
            text: "Laskutusyhteyshenkilö",
            value: "ORGANIZATION_BILLING"
          },
          {
            text: "Allekirjoittajahenkilö",
            value: "ORGANIZATION_SIGNATORY"
          },
          {
            text: "Tiedotusyhteyshenkilö",
            value: "ORGANIZATION_INFO"
          }
        ];
        
        var contactPersonsTable = new IxTable($('organizationContactPersonsTableContainer'), {
          id : "organizationContactPersonsTable",
          columns : [{
            dataType : 'hidden',
            left : 0,
            width : 0,
            paramName : 'id'
          }, {
            header : '<fmt:message key="settings.editOrganization.contactPersonsTable.type"/>',
            left : 8,
            width : 200,
            dataType: 'select',
            editable: true,
            required: true,
            paramName: 'type',
            options: contactPersonTypeOptions
          }, {
            header: '<fmt:message key="settings.editOrganization.contactPersonsTable.name"/>',
            left : 8 + 200 + 8,
            width: 200,
            dataType : 'text',
            editable: true,
            required: false,
            paramName: 'name'
          }, {
            header: '<fmt:message key="settings.editOrganization.contactPersonsTable.email"/>',
            left : 8 + 200 + 8 + 200 + 8,
            width: 200,
            dataType : 'text',
            editable: true,
            required: true,
            paramName: 'email',
            editorClassNames: 'email'
          }, {
            header: '<fmt:message key="settings.editOrganization.contactPersonsTable.phone"/>',
            left : 8 + 200 + 8 + 200 + 8 + 200 + 8,
            width: 200,
            dataType : 'text',
            editable: true,
            required: false,
            paramName: 'phone'
          }, {
            header: '<fmt:message key="settings.editOrganization.contactPersonsTable.title"/>',
            left : 8 + 200 + 8 + 200 + 8 + 200 + 8 + 200 + 8,
            width: 200,
            dataType : 'text',
            editable: true,
            required: false,
            paramName: 'title'
          }, {
            width: 30,
            left: 8 + 200 + 8 + 200 + 8 + 200 + 8 + 200 + 8 + 200 + 8,
            dataType: 'button',
            paramName: 'removeButton',
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="terms.remove"/>',
            onclick: function (event) {
              event.tableComponent.deleteRow(event.row);
            }
          }]
        });

        return contactPersonsTable;
      }
      
      function initializeContractPeriodsTable() {
        var contactPersonsTable = new IxTable($('organizationContractPeriodsTableContainer'), {
          id : "organizationContractPeriodsTable",
          columns : [{
            dataType : 'hidden',
            left : 0,
            width : 0,
            paramName : 'id'
          }, {
            header : '<fmt:message key="terms.begin"/>',
            left : 8,
            width : 160,
            dataType: 'date',
            editable: true,
            required: true,
            paramName: 'begin'
          }, {
            header: '<fmt:message key="terms.end"/>',
            left : 8 + 160 + 8,
            width: 160,
            dataType : 'date',
            editable: true,
            required: false,
            paramName: 'end'
          }, {
            width: 30,
            left: 8 + 160 + 8 + 160 + 8,
            dataType: 'button',
            paramName: 'removeButton',
            imgsrc: GLOBAL_contextPath + '/gfx/list-remove.png',
            tooltip: '<fmt:message key="terms.remove"/>',
            onclick: function (event) {
              event.tableComponent.deleteRow(event.row);
            }
          }]
        });

        return contactPersonsTable;
      }
      
      function addContactPersonTableRow(contactPersonTable) {
        contactPersonTable.addRow([-1, 'ORGANIZATION_ADMINISTRATOR', '', '', '', '', '']);
      }
      
      function addContractPeriodTableRow(contractPeriodTable) {
        contractPeriodTable.addRow([-1, '', '', '']);
      }
      
      function onLoad(event) {
        var tabControl = new IxProtoTabs($('tabs'));

        var contractPeriodsTable = initializeContractPeriodsTable();
        var contractPeriodsData = JSDATA["contractPeriods"].evalJSON();
        console.log(contractPeriodsData);

        var rows = [];
        for (var i = 0, l = contractPeriodsData.length; i < l; i++) {
          var contractPeriodData = contractPeriodsData[i];
          rows.push([
            contractPeriodData.id,
            contractPeriodData.begin,
            contractPeriodData.end,
            '' 
          ]);
        }
        
        if (rows.length > 0) {
          contractPeriodsTable.addRows(rows);
        }
        
        
        var contactPersonsTable = initializeContactPersonsTable();   
        var contactPersonsData = JSDATA["contactPersons"].evalJSON();

        rows = [];
        for (var i = 0, l = contactPersonsData.length; i < l; i++) {
          var contactPersonData = contactPersonsData[i];
          rows.push([
            contactPersonData.id,
            contactPersonData.type,
            contactPersonData.name,
            contactPersonData.email,
            contactPersonData.phone,
            contactPersonData.title,
            '' 
          ]);
        }
        
        if (rows.length > 0) {
          contactPersonsTable.addRows(rows);
        }
        
        var form = $("createOrganizationForm");
        Event.observe(form, "submit", editOrganizationFormSubmit);
      }
    </script>
  </head>

  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
  
    <h1 class="genericPageHeader"><fmt:message key="settings.editOrganization.pageTitle" /></h1>
  
    <div class="genericFormContainer"> 
      <div class="tabLabelsContainer" id="tabs">
        <a class="tabLabel" href="#basic">
          <fmt:message key="settings.editOrganization.tabLabelBasic"/>
        </a>
      </div>
      
      <form id="createOrganizationForm">
        <input id="organizationId" name="organizationId" type="hidden" value="${organization.id}"/>
        
        <div id="basic" class="tabContent">
          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="settings.editOrganization.nameTitle"/>
              <jsp:param name="helpLocale" value="settings.editOrganization.nameHelp"/>
            </jsp:include>
            <input id="name" type="text" name="name" class="required" size="40" value="${organization.name}"/>
          </div>

          <div class="genericFormSection">  
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="settings.editOrganization.educationTypeTitle"/>
              <jsp:param name="helpLocale" value="settings.editOrganization.educationTypeHelp"/>
            </jsp:include>    
            <select id="educationType" name="educationType">           
              <option></option>
              <c:forEach var="educationType" items="${educationTypes}">
                <c:choose>
                  <c:when test="${educationType.id eq organization.educationType.id}">
                    <option value="${educationType.id}" selected="selected">${educationType.name}</option>
                  </c:when>
                  <c:otherwise>
                    <option value="${educationType.id}">${educationType.name}</option> 
                  </c:otherwise>
                </c:choose>
              </c:forEach>
            </select>
          </div>

          <!-- Contract Period -->

          <div class="genericFormSection">  
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="settings.editOrganization.contractPeriodsTitle"/>
              <jsp:param name="helpLocale" value="settings.editOrganization.contractPeriodsHelp"/>
            </jsp:include>
            
            <div class="genericTableAddRowContainer">
              <span class="genericTableAddRowLinkContainer" onclick="addContractPeriodTableRow(getIxTableById('organizationContractPeriodsTable'));"><fmt:message key="settings.editOrganization.addContractPeriodLink"/></span>
            </div>
    
            <div id="organizationContractPeriodsTableContainer"></div>
          </div>
          
          <!-- Contacts -->
          
          <div class="genericFormSection">  
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="settings.editOrganization.contactPersonsTitle"/>
              <jsp:param name="helpLocale" value="settings.editOrganization.contactPersonsHelp"/>
            </jsp:include>
            
            <div class="genericTableAddRowContainer">
              <span class="genericTableAddRowLinkContainer" onclick="addContactPersonTableRow(getIxTableById('organizationContactPersonsTable'));"><fmt:message key="settings.editOrganization.addContactPersonLink"/></span>
            </div>
    
            <div id="organizationContactPersonsTableContainer"></div>
          </div>
  
          <!-- Billing details -->
    
          <div class="genericFormSection">  
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="settings.editOrganization.billingDetailsTitle"/>
              <jsp:param name="helpLocale" value="settings.editOrganization.billingDetailsHelp"/>
            </jsp:include>
            
            <div id="billing-details-container">
              <div class="billingDetailsRow">
                <label for="billing-details-personName"><fmt:message key="billingDetails.personName"/></label>
                <input id="billing-details-personName" type="text" name="billingDetailsPersonName" value="${organization.billingDetails.personName}"/>
              </div>
              
              <div class="billingDetailsRow">
                <label for="billing-details-companyName"><fmt:message key="billingDetails.companyName"/></label>
                <input id="billing-details-companyName" type="text" name="billingDetailsCompanyName" value="${organization.billingDetails.companyName}"/>
              </div>
                
              <div class="billingDetailsRow">
                <label for="billing-details-streetAddress1"><fmt:message key="billingDetails.streetAddress1"/></label>
                <input id="billing-details-streetAddress1" type="text" name="billingDetailsStreetAddress1" value="${organization.billingDetails.streetAddress1}"/>
              </div>
              
              <div class="billingDetailsRow">
                <label for="billing-details-streetAddress2"><fmt:message key="billingDetails.streetAddress2"/></label>
                <input id="billing-details-streetAddress2" type="text" name="billingDetailsStreetAddress2" value="${organization.billingDetails.streetAddress2}"/>
              </div>
              
              <div class="billingDetailsRow">
                <label for="billing-details-postalCode"><fmt:message key="billingDetails.postalCode"/></label>
                <input id="billing-details-postalCode" type="text" name="billingDetailsPostalCode" value="${organization.billingDetails.postalCode}"/>
              </div>
              
              <div class="billingDetailsRow">
                <label for="billing-details-city"><fmt:message key="billingDetails.city"/></label>
                <input id="billing-details-city" type="text" name="billingDetailsCity" value="${organization.billingDetails.city}"/>
              </div>
              
              <div class="billingDetailsRow">
                <label for="billing-details-region"><fmt:message key="billingDetails.region"/></label>
                <input id="billing-details-region" type="text" name="billingDetailsRegion" value="${organization.billingDetails.region}"/>
              </div>
                
              <div class="billingDetailsRow">
                <label for="billing-details-country"><fmt:message key="billingDetails.country"/></label>
                <input id="billing-details-country" type="text" name="billingDetailsCountry" value="${organization.billingDetails.country}"/>
              </div>
              
              <div class="billingDetailsRow">
                <label for="billing-details-phoneNumber"><fmt:message key="billingDetails.phoneNumber"/></label>
                <input id="billing-details-phoneNumber" type="text" name="billingDetailsPhoneNumber" value="${organization.billingDetails.phoneNumber}"/>
              </div>
              
              <div class="billingDetailsRow">
                <label for="billing-details-emailAddress"><fmt:message key="billingDetails.emailAddress"/></label>
                <input id="billing-details-emailAddress" type="email" name="billingDetailsEmailAddress" value="${organization.billingDetails.emailAddress}"/>
              </div>
                
              <div class="billingDetailsRow">
                <label for="billing-details-companyIdentifier"><fmt:message key="billingDetails.companyIdentifier"/></label>
                <input id="billing-details-companyIdentifier" type="text" name="billingDetailsCompanyIdentifier" value="${organization.billingDetails.companyIdentifier}"/>
              </div>
              
              <div class="billingDetailsRow">
                <label for="billing-details-referenceNumber"><fmt:message key="billingDetails.referenceNumber"/></label>
                <input id="billing-details-referenceNumber" type="text" name="billingDetailsReferenceNumber" value="${organization.billingDetails.referenceNumber}"/>
              </div>
              
              <div class="billingDetailsRow">
                <label for="billing-details-electronicBillingAddress"><fmt:message key="billingDetails.electronicBillingAddress"/></label>
                <input id="billing-details-electronicBillingAddress" type="text" name="billingDetailsElectronicBillingAddress" value="${organization.billingDetails.electronicBillingAddress}" />
              </div>
              
              <div class="billingDetailsRow">
                <label for="billing-details-electronicBillingOperator"><fmt:message key="billingDetails.electronicBillingOperator"/></label>
                <input id="billing-details-electronicBillingOperator" type="text" name="billingDetailsElectronicBillingOperator" value="${organization.billingDetails.electronicBillingOperator}"/>
              </div>
              
              <div class="billingDetailsRow">
                <label for="billing-details-notes"><fmt:message key="billingDetails.notes"/></label>
                <textarea id="billing-details-notes" name="billingDetailsNotes">${organization.billingDetails.notes}</textarea>
              </div>
            </div>
          </div>
        </div>

        <div class="genericFormSubmitSectionOffTab">
          <input type="submit" class="formvalid" value="<fmt:message key="generic.form.saveButton"/>">
        </div>
      </form>
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>