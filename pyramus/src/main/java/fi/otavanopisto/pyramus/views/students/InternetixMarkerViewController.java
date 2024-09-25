package fi.otavanopisto.pyramus.views.oo;

import java.util.Date;

import fi.internetix.smvc.controllers.PageController;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

/**
 * The controller responsible of the Logged User Info view of the application.
 * 
 */
public class InternetixMarkerViewController extends PyramusViewController implements PageController {
  
  /**
   * Processes the page request.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    Date startDate = pageRequestContext.getDate("startDate");
    startDate = startDate != null ? startDate : new Date();
    Date endDate = pageRequestContext.getDate("endDate");
    endDate = endDate != null ? endDate : new Date();
    
    pageRequestContext.getRequest().setAttribute("startDate", startDate.getTime());
    pageRequestContext.getRequest().setAttribute("endDate", endDate.getTime());
    
    pageRequestContext.setIncludeFtl("/plugin/oo/studentmarker.ftl");
  }

  @Override
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR, UserRole.STUDY_PROGRAMME_LEADER };
  }
  
}
