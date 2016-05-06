package fi.otavanopisto.pyramus.json.users;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.users.UserDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class SetDefaultUserJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();

    Person person = personDAO.findById(requestContext.getLong("personId"));
    User user = userDAO.findById(requestContext.getLong("userId"));
    
    if (user.getPerson().getId().equals(person.getId()))
      personDAO.updateDefaultUser(person, user);
    
    requestContext.setRedirectURL(requestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
