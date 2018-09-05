package fi.otavanopisto.pyramus.views.matriculation;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class BrowseMatriculationExamsViewController extends PyramusViewController {
  
  public void process(PageRequestContext pageRequestContext) {
    pageRequestContext.setIncludeJSP("/templates/matriculation/management-browse-enrollments.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
