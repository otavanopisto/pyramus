package fi.otavanopisto.pyramus.views.system;

import java.util.List;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.clientapplications.ClientApplicationDAO;
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplication;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ClientApplicationsViewController extends PyramusViewController {

  @Override
  public void process(PageRequestContext requestContext) {
    ClientApplicationDAO clientApplicationDAO = DAOFactory.getInstance().getClientApplicationDAO();
    
    List<ClientApplication> clientApplications = clientApplicationDAO.listAll();
    requestContext.getRequest().setAttribute("clientApplications", clientApplications);
    requestContext.setIncludeJSP("/templates/system/clientapplications.jsp");
  }

  @Override
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }

}
