package fi.otavanopisto.pyramus.util.dataimport.scripting.api;

import java.util.Date;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.students.Sex;

public class PersonAPI {

  public PersonAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }

  public Long create(Date birthday, String socialSecurityNumber, String sex, String basicInfo, boolean secureInfo) {
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    Sex sexEntity;

    if ("m".equals(sex)) {
      sexEntity = Sex.MALE;
    } else if ("f".equals(sex)) {
      sexEntity = Sex.FEMALE;
    } else {
      throw new IllegalArgumentException("sex must be \"m\" or \"f\"");
    }

    Person person = personDAO.create(birthday, socialSecurityNumber, sexEntity, basicInfo, secureInfo);

    return person.getId();
  }
  
  public Long findIdBySocialSecurityNumber(String socialSecurityNumber) {
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    Person person = personDAO.findBySSN(socialSecurityNumber);
    return person != null ? person.getId() : null;
  }
  
  public Long findIdByEmail(String email) {
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();

    // Trim the email address
    email = email != null ? email.trim() : null;

    Person person = personDAO.findByUniqueEmail(email);
    if (person != null) {
      return person.getId();
    }
    
    return null;
  }

  @SuppressWarnings("unused")
  private Long loggedUserId;
}
