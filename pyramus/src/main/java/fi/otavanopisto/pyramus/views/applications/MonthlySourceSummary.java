package fi.otavanopisto.pyramus.views.applications;

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
import net.sf.json.JSONObject;

@Stateless
public class MonthlySourceSummary {
  
  @Schedule(dayOfMonth = "1", hour = "2", persistent = false)
  public void doMonhtlySummary() {
    try {
      // Sources

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
      summary.append(String.format("<p>%s</p>", subject));
      summary.append(String.format("<p>Hakemuksia yhteensä: %d</p>", applicationCount));
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

      //TODO Enable
      //Mailer.sendMail(Mailer.JNDI_APPLICATION, Mailer.HTML, null, "pasi.kukkonen@otavanopisto.fi", subject, summary.toString());
    }
    catch (Exception e) {
      Logger logger = Logger.getLogger(MonthlySourceSummary.class.getName());
      logger.log(Level.SEVERE, "Failed to send application monhtly source summary", e);
    }
  }

  private String getFormValue(JSONObject object, String key) {
    return object.has(key) ? object.getString(key) : null;
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
