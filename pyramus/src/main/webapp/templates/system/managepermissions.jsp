<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
  <head>
    <title><fmt:message key="system.managepermissions.pageTitle"/></title>

    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>

    <script type="text/javascript">
      function onLoad(event) {
        tabControl = new IxProtoTabs($('tabs'));
      }
    </script>
  </head>
  
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader"><fmt:message key="system.managepermissions.pageTitle"/></h1>
    
    <div class="genericFormContainer"> 
      <form action="managepermissions.page" method="post">
  
        <div class="tabLabelsContainer" id="tabs">
          <a class="tabLabel" href="#permissions">
            <fmt:message key="system.managepermissions.tabLabel"/>
          </a>
        </div>
        
        <div id="permissions" class="tabContent">
          <div>
            Reset
            <select name="roleReset">
              <option></option>
              <c:forEach var="role" items="${roles}">
                <option value="${role}">${role}</option>
              </c:forEach>
            </select>
          </div>
          <table>
            <colgroup>
              <col />
              <c:forEach var="role" items="${roles}" varStatus="vs">
                <c:choose>
                  <c:when test="${vs.index % 2 == 0}">
                    <col style="background-color: #eaf0fe;" />
                  </c:when>
                  <c:otherwise>
                    <col />
                  </c:otherwise>
                </c:choose>
              </c:forEach>
            </colgroup>
            <tr>
              <td></td>
              <c:forEach var="role" items="${roles}">
                <td>${role}</td>
              </c:forEach>
            </tr>
            <c:forEach var="permission" items="${permissions}">
              <tr>
                <td>${permission.name}</td>
                <c:forEach var="role" items="${roles}">
                  <td align="center">
                    <c:set var="key" value="${permission.id}.${role}" />
                    <c:choose>
                      <c:when test="${rolePermissions[key]}">
                        <input name="${key}" type="checkbox" value="1" checked="checked" title="${role}"/>
                      </c:when>
                      <c:otherwise>
                        <input name="${key}" type="checkbox" value="1" title="${role}"/>
                      </c:otherwise>
                    </c:choose>
                  </td>
                </c:forEach>
              </tr>
            </c:forEach>
          </table>
        </div>
  
        <div class="genericFormSubmitSectionOffTab">
          <input type="submit" class="formvalid" value="<fmt:message key="system.managepermissions.saveBtn"/>">
        </div>

      </form>
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>