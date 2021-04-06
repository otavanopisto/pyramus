package fi.otavanopisto.pyramus.json.worklist;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.worklist.WorklistItemDAO;
import fi.otavanopisto.pyramus.domainmodel.worklist.WorklistItem;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ArchiveWorklistItemJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    WorklistItemDAO worklistItemDAO = DAOFactory.getInstance().getWorklistItemDAO();
    Long itemId = NumberUtils.createLong(requestContext.getRequest().getParameter("itemId"));
    WorklistItem worklistItem = worklistItemDAO.findById(itemId);
    worklistItemDAO.archive(worklistItem);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }
  
}
