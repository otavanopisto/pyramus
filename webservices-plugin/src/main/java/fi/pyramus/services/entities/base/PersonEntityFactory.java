package fi.pyramus.services.entities.base;

import fi.pyramus.domainmodel.base.Person;
import fi.pyramus.services.entities.EntityFactory;

public class PersonEntityFactory implements EntityFactory<PersonEntity> {

  public PersonEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null)
      return null;
    
    Person person = (Person) domainObject; 
    
    String sex = null;
    if (person.getSex() != null)
      sex = person.getSex().name();
    
    return new PersonEntity(person.getId(), person.getBirthday(), person.getSocialSecurityNumber(), sex);
  }
}
