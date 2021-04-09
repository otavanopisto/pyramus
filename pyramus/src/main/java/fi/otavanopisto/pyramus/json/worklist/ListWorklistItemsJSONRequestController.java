package fi.otavanopisto.pyramus.json.worklist;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.dao.worklist.WorklistItemDAO;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItem;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ListWorklistItemsJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    WorklistItemDAO worklistItemDAO = DAOFactory.getInstance().getWorklistItemDAO();

    Long staffMemberId = requestContext.getLong("staffMemberId");
    StaffMember staffMember = staffMemberDAO.findById(staffMemberId);
    Date beginDate = requestContext.getDate("beginDate");
    Date endDate = requestContext.getDate("endDate");
    
    List<WorklistItem> worklistItems = worklistItemDAO.listByOwnerAndTimeframeAndArchived(staffMember, beginDate, endDate, Boolean.FALSE);
    worklistItems.sort(Comparator.comparing(WorklistItem::getEntryDate));

    List<Map<String, Object>> items = new ArrayList<>();
    for (WorklistItem worklistItem : worklistItems) {
      Map<String, Object> item = new HashMap<>();
      item.put("id",  worklistItem.getId());
      item.put("entryDate",  worklistItem.getEntryDate().getTime());
      item.put("description",  worklistItem.getDescription());
      item.put("price", worklistItem.getPrice());
      item.put("factor", worklistItem.getFactor());
      item.put("billingNumber", worklistItem.getBillingNumber());
      if (worklistItem.getCourseAssessment() != null) {
        item.put("hasAssessment", true);
      }
      items.add(item);
    }
    
    requestContext.addResponseParameter("worklistItems", items);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
