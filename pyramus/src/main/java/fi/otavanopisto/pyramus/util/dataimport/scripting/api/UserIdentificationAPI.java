package fi.otavanopisto.pyramus.util.dataimport.scripting.api;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.users.UserIdentification;
import fi.otavanopisto.pyramus.util.dataimport.scripting.InvalidScriptException;

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
