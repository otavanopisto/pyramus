<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    <title>YO-ilmoittautumiset, YTL JSON</title>

    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/scriptaculous_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>

    <style>
      .ktable {
        display: table;
      }
      .krow {
        display: table-row;
      }
      .kcell {
        display: table-cell;
        padding: 4px;
      }
      .ktablehead {
        font-weight: bold;
      }
    </style>
    
    <script type="text/javascript">
      function onLoad(event) {
        var tabControl = new IxProtoTabs($('tabs'));
      };

      function showJSONClick() {
        var form = $('ytlJSONForm');

        // Use {{ variable }} syntax
        var syntax = /(^|.|\r|\n)(\{{\s*([A-Za-z0-9_öåäÖÅÄ]+)\s*}})/;
        var kokelasTemplate = new Template(
            '<div class="krow {{extraClass}}">' + 
              '<div class="kcell">{{hetu}}</div>' +
              '<div class="kcell">{{oppijanumero}}</div>' +
              '<div class="kcell">{{etunimet}}</div>' +
              '<div class="kcell">{{sukunimi}}</div>' +
              '<div class="kcell">{{koulutustyyppi}}</div>' +
              '<div class="kcell">{{tutkintotyyppi}}</div>' +
              '<div class="kcell">{{uudelleenaloittaja}}</div>' +
              '<div class="kcell">{{kokelasnumero}}</div>' +
              '<div class="kcell">{{äidinkielenKoe}}</div>' +
              '<div class="kcell">{{pakollisetKokeet}}</div>' +
              '<div class="kcell">{{ylimääräisetKokeet}}</div>' +
              '<div class="kcell">{{kurssit}}</div>' +
            '</div>', syntax);
        var kokelasTemplateHeaders = {
            'extraClass': "ktablehead",
            'hetu': "Hetu",
            'oppijanumero': "Oppijanumero",
            'etunimet': "Etunimet",
            'sukunimi': "Sukunimi",
            'koulutustyyppi': "Koulutustyyppi",
            'tutkintotyyppi': "Tutkintotyyppi",
            'uudelleenaloittaja': "Uudelleenaloittaja",
            'kokelasnumero': "Kokelasnumero",
            "äidinkielenKoe": "ÄidinkielenKoe",
            "pakollisetKokeet": "Pakolliset kokeet",
            "ylimääräisetKokeet": "Ylimääräiset kokeet",
            "kurssit": "Suoritetut kurssit"
        };

        var kurssitTemplate = new Template('{{aine}}{{oppimäärä}}({{kursseja}})', syntax);

        new Ajax.Request('/ytl/report.binary', {
          method: 'post',
          parameters: {
            schoolId: form.schoolId.value,
            examId: form.examId.value
          },
          onSuccess: function(response) {
            var report = response.responseJSON;
            var container = $('showReportTableContainer');

            container.update();
            container.insert(kokelasTemplate.evaluate(kokelasTemplateHeaders));
            
            if (report && report.kokelaat) {
              report.kokelaat.each(function (kokelas) {
                var kurssitContainer = [];

                if (kokelas.suoritetutKurssit) {
                  kokelas.suoritetutKurssit.each(function (kurssit) {
                    kurssitContainer.push(kurssitTemplate.evaluate(kurssit));
                  });
                }
                
                container.insert(kokelasTemplate.evaluate(Object.extend({kurssit: kurssitContainer}, kokelas)));
              });
            }
          }
        });
      }
    </script>
    
  </head> 
  <body onload="onLoad(event);">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
    
    <h1 class="genericPageHeader">YTL JSON -raportti</h1>
    
    <div class="genericFormContainer">
      <div class="tabLabelsContainer" id="tabs">
        <a class="tabLabel" href="#settings">YTL JSON</a>
      </div>
      <div id="settings" class="tabContent">
        <form method="get" action="${pageContext.request.contextPath}/ytl/report.binary" id="ytlJSONForm">
          <div class="genericFormSection">  
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleLocale" value="matriculation.ytljson.schoolId"/>
            </jsp:include>                                           
            <input type="number" name="schoolId"/>
          </div>

          <div class="genericFormSection">
            <jsp:include page="/templates/generic/fragments/formtitle.jsp">
              <jsp:param name="titleText" value="Ilmoittautumiskierros"/>
            </jsp:include>
            <select id="examId" name="examId">
              <option value=""></option>
              <c:forEach var="exam" items="${exams}">
                <c:choose>
                  <c:when test="${exam.examTerm == 'SPRING'}">
                    <c:set var="examTermText"><fmt:message key="terms.seasons.spring" /></c:set>
                  </c:when>
                  <c:when test="${exam.examTerm == 'AUTUMN'}">
                    <c:set var="examTermText"><fmt:message key="terms.seasons.autumn" /></c:set>
                  </c:when>
                  <c:otherwise>
                    <c:set var="examTermText"></c:set>
                  </c:otherwise>
                </c:choose>
                <option value="${exam.id}">${exam.examYear} ${examTermText}</option>
              </c:forEach>
            </select>
          </div>

          <div class="genericFormSubmitSection">
            <input type="submit" value="Tallenna">
            <input type="button" value="Näytä" onclick="showJSONClick();">
          </div>
        </form>

        <div class="genericFormSection">  
          <div id="showReportTableContainer" class="ktable"></div>
        </div>
      </div>
    </div>
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>