package fi.pyramus.util.dataimport.scripting.api;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.domainmodel.base.Person;
import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.domainmodel.users.StaffMember;
import fi.pyramus.util.dataimport.scripting.InvalidScriptException;

public class StaffMemberAPI {
  
  public Long create(String firstName, String lastName, String externalId,
                     String authProvider, String role, Long personId)
                         throws InvalidScriptException {
    DAOFactory daoFactory = DAOFactory.getInstance();
    Person person = daoFactory.getPersonDAO().findById(personId);
    if (person == null) {
      throw new InvalidScriptException("Person not found");
    }
    StaffMember staffMember = 
        daoFactory.getStaffMemberDAO()
                  .create(firstName,
                          lastName,
                          externalId,
                          authProvider,
                          Role.valueOf(role),
                          person);
    if (staffMember == null) {
      return null;
    } else {
      return staffMember.getId();
    }
  }

}
