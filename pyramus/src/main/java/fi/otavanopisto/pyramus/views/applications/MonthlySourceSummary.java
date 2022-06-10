package fi.otavanopisto.pyramus.views.applications;

import static fi.otavanopisto.pyramus.applications.ApplicationUtils.getFormValue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Schedule;
import javax.ejb.Stateless;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.applications.ApplicationUtils;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.mailer.Mailer;
import net.sf.json.JSONObject;

@Stateless
public class MonthlySourceSummary {
  
  @Schedule(dayOfMonth = "1", hour = "2", persistent = false)
  public void doMonhtlySummary() {
    Logger logger = Logger.getLogger(MonthlySourceSummary.class.getName());
    logger.info("Running monthly application source summary");
    try {
      
      // Lines and sources
      
      Map<String, Map<String, SummaryItem>> lines = new LinkedHashMap<String, Map<String, SummaryItem>>();
      lines.put("nettilukio", createSourceMap());
      lines.put("nettipk", createSourceMap());
      lines.put("aikuislukio", createSourceMap());
      lines.put("mk", createSourceMap());
      lines.put("aineopiskelu", createSourceMap());
      
      Map<String, Integer> lineApplicationCounts = new LinkedHashMap<String, Integer>();
      lineApplicationCounts.put("nettilukio", 0);
      lineApplicationCounts.put("nettipk", 0);
      lineApplicationCounts.put("aikuislukio", 0);
      lineApplicationCounts.put("mk", 0);
      lineApplicationCounts.put("aineopiskelu", 0);

      // Timeframe

      Calendar c = Calendar.getInstance();
      c.add(Calendar.MONTH, -1);
      c.set(Calendar.DATE, 1);
      c.set(Calendar.HOUR, 0);
      c.set(Calendar.MINUTE, 0);
      c.set(Calendar.SECOND, 0);
      Date beginDate = c.getTime();
      c.set(Calendar.DATE, c.getActualMaximum(Calendar.DAY_OF_MONTH));
      c.set(Calendar.HOUR, 23);
      c.set(Calendar.MINUTE, 59);
      c.set(Calendar.SECOND, 59);
      Date endDate = c.getTime();

      // Applications

      ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
      List<Application> applications = applicationDAO.listByTimeframe(beginDate, endDate);

      // Statistics

      int applicationCount = applications.size();
      for (Application application : applications) {
        String line = application.getLine();
        Integer appCount = lineApplicationCounts.get(line);
        Map<String, SummaryItem> sources = lines.get(line);
        if (appCount == null || sources == null) {
          applicationCount--;
          logger.warning(String.format("Application for unknown line %s", line));
          continue; 
        }
        lineApplicationCounts.put(line, appCount + 1);
        JSONObject formData = JSONObject.fromObject(application.getFormData());
        String source = getFormValue(formData, "field-source");
        String explanation = getFormValue(formData, "field-source-other");
        SummaryItem summaryItem = sources.get(source);
        if (summaryItem != null) {
          summaryItem.count++;
          if (!StringUtils.isEmpty(explanation)) {
            summaryItem.explanations.add(explanation);
          }
        }
      }

      // Mail

      String[] months = new String[] {
        "tammikuussa", "helmikuussa", "maaliskuussa", "huhtikuussa", "toukokuussa", "kesäkuussa", "heinäkuussa", "elokuussa", "syyskuussa", "lokakuussa", "marraskuussa", "joulukuussa"
      };
      String subject = String.format("Hakemukset %s %d", months[c.get(Calendar.MONTH)], c.get(Calendar.YEAR));
      StringBuilder summary = new StringBuilder();
      summary.append("<meta charset=\"UTF-8\"/>");
      summary.append(String.format("<h3>%s</h3><p>Hakemuksia yhteensä: %d</p>", subject, applicationCount));
      Set<String> lineSet = lines.keySet();
      for (String line : lineSet) {
        Map<String, SummaryItem> sources = lines.get(line);
        summary.append(String.format("<hr/><h3>%s (%d)</h3>", ApplicationUtils.applicationLineUiValue(line), lineApplicationCounts.get(line)));
        Set<String> sourceSet = sources.keySet();
        for (String source : sourceSet) {
          SummaryItem summaryItem = sources.get(source);
          if (summaryItem.count > 0) {
            summary.append(String.format("<p>%s: %d</p>", ApplicationUtils.sourceUiValue(source), summaryItem.count));
            if (!CollectionUtils.isEmpty(summaryItem.explanations)) {
              summary.append("<ul>");
              for (String explanation : summaryItem.explanations) {
                summary.append(String.format("<li>%s</li>", explanation));
              }
              summary.append("</ul>");
            }
          }
        }
      }
      
      Mailer.sendMail(Mailer.JNDI_APPLICATION, Mailer.HTML, null, "mediatiimi@otavanopisto.fi", subject, summary.toString());
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, "Failed to send application monthly source summary", e);
    }
  }
  
  private Map<String, SummaryItem> createSourceMap() {
    Map<String, SummaryItem> sources = new LinkedHashMap<String, SummaryItem>();
    sources.put("tuttu", new SummaryItem());
    sources.put("google", new SummaryItem());
    sources.put("facebook", new SummaryItem());
    sources.put("instagram", new SummaryItem());
    sources.put("sanomalehti", new SummaryItem());
    sources.put("tienvarsimainos", new SummaryItem());
    sources.put("valotaulumainos", new SummaryItem());
    sources.put("elokuva", new SummaryItem());
    sources.put("tuttava", new SummaryItem());
    sources.put("opot", new SummaryItem());
    sources.put("messut", new SummaryItem());
    sources.put("te-toimisto", new SummaryItem());
    sources.put("nuorisotyo", new SummaryItem());
    sources.put("muu", new SummaryItem());
    return sources;
  }

  private class SummaryItem {
    public SummaryItem() {
      count = 0;
      explanations = new ArrayList<String>();
    }
    private int count;
    private List<String> explanations; 
  }
  
}
