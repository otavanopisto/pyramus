package fi.otavanopisto.pyramus.koski;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Person;

@Stateless
public class KoskiUpdater {

  @Inject
  private KoskiClient koskiClient;
  
  @Inject
  private PersonDAO personDAO;
  
  @Asynchronous
  public void updatePerson(Long personId) {
    Person person = personDAO.findById(personId);
    try {
      koskiClient.updatePerson(person);
    } catch (KoskiException e) {
    }
  }
  
}
