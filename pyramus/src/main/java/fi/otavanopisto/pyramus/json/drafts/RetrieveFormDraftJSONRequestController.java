package fi.otavanopisto.pyramus.json.drafts;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.drafts.DraftDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.drafts.FormDraft;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class RetrieveFormDraftJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    DraftDAO draftDAO = DAOFactory.getInstance().getDraftDAO();

    String url = requestContext.getRequest().getHeader("Referer");
    User loggedUser = userDAO.findById(requestContext.getLoggedUserId());
    
    FormDraft formDraft = draftDAO.findByUserAndURL(loggedUser, url);
    if (formDraft == null) {
      requestContext.addResponseParameter("draftDeleted", true);
    } else {
      requestContext.addResponseParameter("draftDeleted", false);
      requestContext.addResponseParameter("url", formDraft.getUrl());
      requestContext.addResponseParameter("draftData", formDraft.getData());
      requestContext.addResponseParameter("draftCreated", formDraft.getCreated());
      requestContext.addResponseParameter("draftModified", formDraft.getModified());
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.GUEST, UserRole.USER, UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }
}

