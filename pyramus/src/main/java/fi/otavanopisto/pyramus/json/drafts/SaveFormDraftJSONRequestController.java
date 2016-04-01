package fi.otavanopisto.pyramus.json.drafts;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.drafts.DraftDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.drafts.FormDraft;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class SaveFormDraftJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    DraftDAO draftDAO = DAOFactory.getInstance().getDraftDAO();

    String url = requestContext.getRequest().getHeader("Referer");
    String draftData = requestContext.getString("draftData");
    
    if (draftData != null) {
      User loggedUser = userDAO.findById(requestContext.getLoggedUserId());
      
      FormDraft formDraft = draftDAO.findByUserAndURL(loggedUser, url);
      if (formDraft == null)
        formDraft = draftDAO.create(loggedUser, url, draftData);
      else
        draftDAO.update(formDraft, draftData);
      
      requestContext.addResponseParameter("url", formDraft.getUrl());
      requestContext.addResponseParameter("draftCreated", formDraft.getCreated());
      requestContext.addResponseParameter("draftModified", formDraft.getModified());
    } 
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.GUEST, UserRole.USER, UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
