package fi.pyramus.json.settings;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.grading.TransferCreditTemplateDAO;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class DeleteTransferCreditTemplateJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    TransferCreditTemplateDAO transferCreditTemplateDAO = DAOFactory.getInstance().getTransferCreditTemplateDAO();
    Long transferCreditTemplateId = jsonRequestContext.getLong("transferCreditTemplateId");
    transferCreditTemplateDAO.delete(transferCreditTemplateDAO.findById(transferCreditTemplateId));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
