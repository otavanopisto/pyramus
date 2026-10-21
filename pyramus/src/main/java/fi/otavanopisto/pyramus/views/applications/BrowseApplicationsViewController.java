package fi.otavanopisto.pyramus.views.applications;

import java.util.Comparator;
import java.util.List;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.SchoolDAO;
import fi.otavanopisto.pyramus.domainmodel.base.School;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class BrowseApplicationsViewController extends PyramusViewController {
  
  public void process(PageRequestContext pageRequestContext) {
    SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();
    List<School> contractSchools = schoolDAO.listByVariable("contractSchool", "1");
    contractSchools.sort(Comparator.comparing(School::getName));
    pageRequestContext.getRequest().setAttribute("contractSchools", contractSchools);
    pageRequestContext.setIncludeJSP("/templates/applications/management-browse-applications.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
