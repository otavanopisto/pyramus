package fi.otavanopisto.pyramus.views.matriculation;

import java.util.Calendar;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class YTLJSONReportViewController extends PyramusViewController {
  
  public void process(PageRequestContext pageRequestContext) {
    pageRequestContext.getRequest().setAttribute("currentYear", Calendar.getInstance().get(Calendar.YEAR));
    pageRequestContext.setIncludeJSP("/templates/matriculation/ytl-json.jsp");
  }
  
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
