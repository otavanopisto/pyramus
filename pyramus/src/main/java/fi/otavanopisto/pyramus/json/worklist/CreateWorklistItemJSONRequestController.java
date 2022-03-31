package fi.otavanopisto.pyramus.json.worklist;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.dao.worklist.WorklistItemDAO;
import fi.otavanopisto.pyramus.dao.worklist.WorklistItemTemplateDAO;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItem;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItemTemplate;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class CreateWorklistItemJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    WorklistItemDAO worklistItemDAO = DAOFactory.getInstance().getWorklistItemDAO();
    WorklistItemTemplateDAO worklistItemTemplateDAO = DAOFactory.getInstance().getWorklistItemTemplateDAO();

    Long loggedUserId = requestContext.getLoggedUserId();
    StaffMember loggedUser = staffMemberDAO.findById(loggedUserId);
    
    Long templateId = requestContext.getLong("templateId");
    WorklistItemTemplate template = worklistItemTemplateDAO.findById(templateId);
    
    Long userId = requestContext.getLong("userId");
    StaffMember owner = staffMemberDAO.findById(userId);
    
    WorklistItem worklistItem = worklistItemDAO.create(
        template,
        owner,
        new Date(),
        template.getDescription(),
        template.getPrice(),
        template.getFactor(),
        template.getBillingNumber(),
        null,
        loggedUser);

    Map<String, Object> item = new HashMap<>();
    item.put("id", worklistItem.getId());
    item.put("entryDate", worklistItem.getEntryDate().getTime());
    item.put("description", worklistItem.getDescription());
    item.put("price", worklistItem.getPrice());
    item.put("factor", worklistItem.getFactor());
    item.put("billingNumber", worklistItem.getBillingNumber());
    item.put("state", worklistItem.getState());
    requestContext.addResponseParameter("worklistItem", item);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
