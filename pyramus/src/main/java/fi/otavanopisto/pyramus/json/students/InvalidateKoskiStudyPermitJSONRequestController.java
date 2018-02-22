package fi.otavanopisto.pyramus.json.students;

import javax.enterprise.inject.spi.CDI;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.koski.KoskiClient;

public class InvalidateKoskiStudyPermitJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    
    Long personId = requestContext.getLong("personId");
    Person person = personDAO.findById(personId);
    String oid = requestContext.getString("oid");
    

    KoskiClient client = CDI.current().select(KoskiClient.class).get();
    
    try {
      client.invalidateStudyOid(person, oid);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
