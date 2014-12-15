package fi.pyramus.util.dataimport.scripting.api;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.domainmodel.base.Person;
import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.domainmodel.users.StaffMember;
import fi.pyramus.domainmodel.users.UserIdentification;
import fi.pyramus.util.dataimport.scripting.InvalidScriptException;

public class UserIdentificationAPI {

  public Long create(Long personId, String authSource, String externalId) throws InvalidScriptException {
    DAOFactory daoFactory = DAOFactory.getInstance();
    Person person = daoFactory.getPersonDAO().findById(personId);
    if (person == null) {
      throw new InvalidScriptException("Person not found");
    }
    UserIdentification userIdentification = daoFactory.getUserIdentificationDAO().create(person, authSource, externalId);
    if (userIdentification == null) {
      return null;
    } else {
      return userIdentification.getId();
    }
  }

}
