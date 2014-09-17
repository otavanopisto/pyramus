package fi.pyramus.views.system;

import java.util.List;
import java.util.UUID;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.clientapplications.ClientApplicationDAO;
import fi.pyramus.domainmodel.clientapplications.ClientApplication;
import fi.pyramus.framework.PyramusFormViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.util.OauthClientSecretGenerator;

public class ClientApplicationsViewController extends PyramusFormViewController {

  @Override
  public void processForm(PageRequestContext requestContext) {
    ClientApplicationDAO clientApplicationDAO = DAOFactory.getInstance().getClientApplicationDAO();
    List<ClientApplication> clientApplications = clientApplicationDAO.listAll();
    requestContext.getRequest().setAttribute("clientApplications", clientApplications);
    requestContext.setIncludeJSP("/templates/system/clientapplications.jsp");
  }

  @Override
  public void processSend(PageRequestContext requestContext) {
    ClientApplicationDAO clientApplicationDAO = DAOFactory.getInstance().getClientApplicationDAO();

    Long clientApplicationsRowCount = requestContext.getLong("clientApplicationsTable.rowCount");
    for (int i = 0; i < clientApplicationsRowCount; i++) {
      String colPrefix = "clientApplicationsTable." + i;

      Long id = requestContext.getLong(colPrefix + ".id");
      Boolean remove = "1".equals(requestContext.getString(colPrefix + ".remove"));
      Boolean regenerateSecret = "1".equals(requestContext.getString(colPrefix + ".regenerateSecret"));
      String clientName = requestContext.getString(colPrefix + ".appName");
      String clientId = requestContext.getString(colPrefix + ".appId");
      String clientSecret = requestContext.getString(colPrefix + ".appSecret");

      if (id == null) {
        clientId = UUID.randomUUID().toString();
        clientSecret = new OauthClientSecretGenerator(80).nextString();
        clientApplicationDAO.create(clientName, clientId, clientSecret);
      } else {
        ClientApplication clientApplication = clientApplicationDAO.findById(id);

        if (remove) {
          clientApplicationDAO.delete(clientApplication);
        }else{
          if (regenerateSecret) {
            clientSecret = new OauthClientSecretGenerator(80).nextString();
            clientApplicationDAO.updateClientSecret(clientApplication, clientSecret);
          }

          clientApplicationDAO.updateName(clientApplication, clientName);
        }
      }
    }

    processForm(requestContext);

  }

  @Override
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }
}
