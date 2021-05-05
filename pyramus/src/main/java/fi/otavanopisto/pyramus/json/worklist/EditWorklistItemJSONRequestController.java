package fi.otavanopisto.pyramus.json.worklist;

import java.util.Date;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.dao.worklist.WorklistItemDAO;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItem;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItemState;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class EditWorklistItemJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    WorklistItemDAO worklistItemDAO = DAOFactory.getInstance().getWorklistItemDAO();

    Long itemId = requestContext.getLong("itemId");
    Date entryDate = requestContext.getDate("entryDate");
    String description = requestContext.getRequest().getParameter("description");
    Double price = requestContext.getDouble("price");
    Double factor = requestContext.getDouble("factor");
    String billingNumber = requestContext.getString("billingNumber"); 
    WorklistItemState state = WorklistItemState.valueOf(requestContext.getString("state"));
    Long loggedUserId = requestContext.getLoggedUserId();
    StaffMember loggedUser = staffMemberDAO.findById(loggedUserId);
    
    WorklistItem worklistItem = worklistItemDAO.findById(itemId);
    worklistItemDAO.update(worklistItem, entryDate, description, price, factor, billingNumber, state, loggedUser);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
