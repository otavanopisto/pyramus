package fi.pyramus.services.entities.students;

import fi.pyramus.domainmodel.base.Person;
import fi.pyramus.services.entities.EntityFactory;

public class AbstractStudentEntityFactory implements EntityFactory<AbstractStudentEntity> {

  public AbstractStudentEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null)
      return null;
    
    Person abstractStudent = (Person) domainObject; 
    
    String sex = null;
    if (abstractStudent.getSex() != null)
      sex = abstractStudent.getSex().name();
    
    return new AbstractStudentEntity(abstractStudent.getId(), abstractStudent.getBirthday(), abstractStudent.getSocialSecurityNumber(), sex);
  }
}
