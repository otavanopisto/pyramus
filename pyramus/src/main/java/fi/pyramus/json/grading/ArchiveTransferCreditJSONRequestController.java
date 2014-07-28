package fi.pyramus.json.grading;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.grading.TransferCreditDAO;
import fi.pyramus.domainmodel.grading.TransferCredit;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

/**
 * The controller responsible of archiving transfer credits.
 */
public class ArchiveTransferCreditJSONRequestController extends JSONRequestController {

  /**
   * Processes the request to archive a transfer credit.
   * The request should contain the either following parameters:
   * <dl>
   *   <dt><code>transferCreditId</code></dt>
   *   <dd>The ID of the transfer credit to archive.</dd>
   * </dl>
   * 
   * 
   * @param jsonRequestContext The JSON request context
   */
  public void process(JSONRequestContext jsonRequestContext) {
    TransferCreditDAO transferCreditDAO = DAOFactory.getInstance().getTransferCreditDAO();
    
    Long transferCreditId = jsonRequestContext.getLong("transferCreditId");
    TransferCredit transferCredit = transferCreditDAO.findById(transferCreditId);
    transferCreditDAO.archive(transferCredit);
    
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  /** Returns the user roles allowed to access this controller.
   * 
   * @return The user roles allowed to access this controller.
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
