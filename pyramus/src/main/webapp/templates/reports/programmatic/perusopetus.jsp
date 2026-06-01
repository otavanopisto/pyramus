<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head>
    <title><fmt:message key="reports.viewReport.pageTitle"></fmt:message></title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/table_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/tabs_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/dialog_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/glasspane_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/ajax_support.jsp"></jsp:include>
    <jsp:include page="/templates/generic/validation_support.jsp"></jsp:include>
    
    <script type="text/javascript">
      function submitForm() {
        var glassPane = new IxGlassPane(document.body, { });
        glassPane.show();
        
        // Use {{ variable }} syntax
        var syntax = /(^|.|\r|\n)(\{{\s*([A-Za-z0-9_öåäÖÅÄ]+)\s*}})/;
        var creditRowTemplate = new Template(
            '<td>{{courseName}}</td>' +
            '<td>{{courseCode}}</td>' +
            '<td>{{courseLengthUI}}</td>' +
            '<td>{{creditDateUI}}</td>' +
            '<td>{{gradeName}}</td>' +
            '<td>{{gradingScaleName}}</td>' +
            '<td>{{groupCourseUI}}</td>' +
            '<td>{{studentName}}</td>' +
            '<td>{{studyProgrammeName}}</td>' +
            '<td>{{assessorName}}</td>' +
            '<td>{{schoolName}}</td>' +
            '<td>{{schoolField}}</td>' +
            '<td title="{{previousEvaluationsTooltipUI}}">{{previousEvaluationsUI}}</td>' +
            '<td>{{mismatchingCurriculumUI}}</td>' +
            '<td>{{otherFundingUI}}</td>' +
            '<td>{{evaluatedOutsideStudiesUI}}</td>' +
            '<td>{{koskiFailureUI}}</td>', syntax);

        var summaryRowTemplate = new Template(
            '<th align="left">{{rowName}}</th>' +
            '<td align="right">{{count}}</td>' +
            '<td align="right">{{countHours}}</td>' + 
            '<td align="right">{{countPoints}}</td>', syntax);
        
        const targetgroup = document.querySelector('input[name="targetgroup"]:checked').value;
        const beginDate = getIxDateField('beginDate').getISO8601Date();
        const endDate = getIxDateField('endDate').getISO8601Date();
        
        axios.get("/report/perusopetus", {
            params: {
              linja: targetgroup,
              begin: beginDate,
              end: endDate
            }
          })
          .then(function (response) {
            const acceptedCreditsElement = $('acceptedCredits');
            const rejectedCreditsElement = $('rejectedCredits');
            const summaryElement = $('summary');

            document.querySelectorAll('.perusopetus_preport_tr').forEach(element => element.remove());
            
            const data = response.data;
            
            if (data.acceptedCredits) {
              for (const credit of data.acceptedCredits) {
                const cells = creditRowTemplate.evaluate(prepareCreditRow(credit));
                const tr = new Element("tr", { className: "perusopetus_preport_tr" });
                tr.update(cells);
                acceptedCreditsElement.appendChild(tr);
              }
            }
            
            if (data.rejectedCredits) {
              for (const credit of data.rejectedCredits) {
                const cells = creditRowTemplate.evaluate(prepareCreditRow(credit));
                const tr = new Element("tr", { className: "perusopetus_preport_tr" });
                tr.update(cells);
                rejectedCreditsElement.appendChild(tr);
              }
            }

            if (data.summary) {
              const summaryRows = [
                {
                  rowName: "Hyväksytyt suoritukset",
                  count: data.summary.acceptedCreditCount || 0,
                  countHours: data.summary.acceptedByLengthUnit["h"] || 0,
                  countPoints: data.summary.acceptedByLengthUnit["op"] || 0
                },
                {
                  rowName: "Poistetut suoritukset",
                  count: data.summary.rejecetdCreditCount || 0,
                  countHours: data.summary.rejectedByLengthUnit["h"] || 0,
                  countPoints: data.summary.rejectedByLengthUnit["op"] || 0
                }
              ];

              for (const summaryRow of summaryRows) {
                const cells = summaryRowTemplate.evaluate(summaryRow);
                const tr = new Element("tr", { className: "perusopetus_preport_tr" });
                tr.update(cells);
                summaryElement.appendChild(tr);
              }
            }
            
            glassPane.hide();
            delete glassPane;
          })
          .catch(function (error) {
            console.error(error);
            glassPane.hide();
            delete glassPane;
          });
      }
      
      function prepareCreditRow(creditRow) {
        var dateStr = "";
        if (creditRow.gradeDate) {
          const creditDate = new Date(creditRow.gradeDate);
          if (creditDate instanceof Date && !isNaN(creditDate)) {
            dateStr = getLocale().getDate(creditDate, false);
          }
        }
        
        var previousEvaluationsTooltipUI = "";
        if (creditRow.previousEvaluations && creditRow.previousEvaluations.length) {
          for (const previousEvaluation of creditRow.previousEvaluations) {
            const creditDate = new Date(previousEvaluation.gradeDate);
            const creditDateStr = (creditDate instanceof Date && !isNaN(creditDate)) ? getLocale().getDate(creditDate, false) : "??";

            if (previousEvaluationsTooltipUI != "") {
              previousEvaluationsTooltipUI += "\n";
            }
            
            previousEvaluationsTooltipUI += creditDateStr + " - " + previousEvaluation.gradeName;
          }
        }
        
        var courseLengthUI = "";
        if (creditRow.courseLength) {
          courseLengthUI = creditRow.courseLength.toString();
          if (creditRow.courseLengthSymbol) {
            courseLengthUI += " " + creditRow.courseLengthSymbol;
          }
        }
        
        return Object.assign(creditRow, {
          creditDateUI: dateStr,
          courseLengthUI: courseLengthUI,
          groupCourseUI: creditRow.groupCourse ? "RK" : "",
          previousEvaluationsTooltipUI: previousEvaluationsTooltipUI,
          previousEvaluationsUI: (creditRow.previousEvaluations && creditRow.previousEvaluations.length) ? "Korotus" : "",
          mismatchingCurriculumUI: creditRow.mismatchingCurriculum ? "3" : "",
          otherFundingUI: creditRow.otherFunding ? "4" : "",
          evaluatedOutsideStudiesUI: creditRow.evaluatedOutsideStudies ? "5" : "",
          koskiFailureUI: creditRow.koskiFailure ? "Koski!" : ""
        });
      }
      
      function onLoad(event) {
        var tabControl = new IxProtoTabs($('tabs'));
        
        const reportForm = $('reportForm');
        reportForm.addEventListener('submit', function (event) {
          event.preventDefault();
          submitForm();
        });
      }
    </script>
    
  </head>
  <body onload="onLoad(event);" class="fixedSizedContentContainer">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
  
    <h1 class="genericPageHeader"><fmt:message key="reports.viewReport.pageTitle"/></h1>
  
    <div id="viewReportContainer">
      <div class="genericFormContainer"> 
        <div class="tabLabelsContainer" id="tabs">
          <a class="tabLabel" href="#viewReport">
            <span class="tabLabelLeftTopCorner">
              <span class="tabLabelRightTopCorner">
                <fmt:message key="reports.viewReport.tabLabelViewReport"/>
              </span>
            </span>
          </a>
        </div>
      
        <div id="viewReport" class="tabContent fixedSizedTabContent">
          <div id="viewReportControlsContainer">
          
            <form id="reportForm" ix:jsonform="true">
              <fieldset>
                <legend>Ajanjakso:</legend>
  
                <label for="beginDate">Alkupäivämäärä</label>
                <input id="beginDate" type="text" name="begin" ix:datefieldid="beginDate" class="ixDateField required">
                <label for="endDate">Loppupäivämäärä</label>
                <input id="endDate" type="text" name="end" ix:datefieldid="endDate" class="ixDateField required">
              </fieldset>
            
              <fieldset>
                <legend>Kohderyhmä:</legend>
              
                <div>
                  <input type="radio" id="apalu" name="targetgroup" value="apalu" checked="checked" />
                  <label for="apalu">Alku- ja lukutaitovaihe</label>
                </div>
              
                <div>
                  <input type="radio" id="paanp" name="targetgroup" value="paanp" />
                  <label for="paanp">Päättövaihe ja nettiperuskoulu</label>
                </div>
              </fieldset>
              
              <input type="submit" class="formvalid" value="Lataa raportti" />
            </form>
          </div>
          
          <div id="reportContent">
          
            <h1>Yhteenveto</h1>
            <table id="summary" class="tableWithRowHighlighting">
              <tr style="text-align: left;">
                <th></th>
                <th>Lukumäärä</th>
                <th>Kurssin pituus tunneissa</th>
                <th>Kurssin pituus opintopisteissä</th>
              </tr>
            </table>
            
            <h1>Suoritukset</h1>
            <table id="acceptedCredits" class="tableWithRowHighlighting">
              <tr style="text-align: left;">
                <th>Kurssi</th>
                <th>Koodi</th>
                <th>Pituus</th>
                <th>Arviointipvm</th>
                <th>Arvosana</th>
                <th>Arvosana-asteikko</th>
                <th>Ryhmäkurssi</th>
                <th>Opiskelija</th>
                <th>Koulutusohjelma</th>
                <th>Opettaja</th>
                <th>Oppilaitos</th>
                <th>Oppilaitos k.ala</th>
<!--                 <th>S.K-hyvluk(1)</th> -->
                <th>Mahd. Kor(2)</th>
                <th>Eri OPS(3)</th>
                <th>Muu rah.(4)</th>
                <th>Arviointi pvm (5)</th>
                <th>Koski(6)</th>
              </tr>
            </table>

            <h1>Poistetut suoritukset</h1>
            <table id="rejectedCredits" class="tableWithRowHighlighting">
              <tr style="text-align: left;">
                <th>Kurssi</th>
                <th>Koodi</th>
                <th>Pituus</th>
                <th>Arviointipvm</th>
                <th>Arvosana</th>
                <th>Arvosana-asteikko</th>
                <th>Ryhmäkurssi</th>
                <th>Opiskelija</th>
                <th>Koulutusohjelma</th>
                <th>Opettaja</th>
                <th>Oppilaitos</th>
                <th>Oppilaitos k.ala</th>
<!--                 <th>S.K-hyvluk(1)</th> -->
                <th>Korotus (2)</th>
                <th>Eri OPS(3)</th>
                <th>Muu rah.(4)</th>
                <th>Arviointi pvm (5)</th>
                <th>Koski(6)</th>
              </tr>
            </table>
          </div>
          
          <p>
<!--             (1) Sarakkeessa 1 jos opiskelijan välilehdellä (/opiskeluoikeudella) on samasta kurssista (aine + kurssinro) hyväksiluku tai siirretty hyväksiluku. Tällaiset tulkitaan Kosken toimesta korotuksiksi ja ne eivät oikeuta valtionosuuksiin.<br/> -->
            (2) Sarakkeessa 2 jos suorituksesta on opiskelijalla samalla opiskeluoikeudella useampia suorituksia. Kyseessä saattaa tällöin olla korotettu suoritus, joka voi vaikuttaa rahoitukseen.<br/>
            (3) Sarakkeessa 3 jos kurssille on merkitty opetussunnitelm(i)a ja opiskelijan OPS ei ole yksikään kurssin opetussuunnitelmista.<br/>
            (4) Sarakkeessa 4 jos opiskelijan tiedoissa rahoitustieto on merkitty "Rahoitettu muuta kautta"<br/>
            (5) Sarakkeessa 5, jos kurssi arvioitu opiskeluajan ulkopuolella<br/>
            (6) Sarakkeessa Koski! jos opiskelijan (henkilö) logitiedoissa ei ole tietoa onnistuneesta viennistä Koskeen. Tämä indikoi sitä, että opiskelija on joko Koski-päivitysjonossa tai tietojen viennissä on ollut virhe.<br/>
          </p>
        </div>
      </div>
    </div>
    
    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>