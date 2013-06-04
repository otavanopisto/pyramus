package fi.pyramus.json.tags;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.TagDAO;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class GetAllTagsJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    TagDAO tagDAO = DAOFactory.getInstance().getTagDAO();
    
    Set<String> tagTexts = new HashSet<String>();
    
    List<Tag> tags = tagDAO.listAll();
    for (Tag tag : tags) {
      tagTexts.add(tag.getText());
    }
    
    requestContext.addResponseParameter("tags", tagTexts);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }
}

