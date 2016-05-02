package fi.pyramus.services.entities.base;

import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.pyramus.services.entities.EntityFactory;
import fi.pyramus.services.entities.EntityFactoryVault;

public class SubjectEntityFactory implements EntityFactory<SubjectEntity> {

  public SubjectEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null)
      return null;
    
    Subject subject = (Subject) domainObject; 
    
    EducationTypeEntity educationTypeEntity = EntityFactoryVault
      .buildFromDomainObject(subject.getEducationType());
    
    return new SubjectEntity(subject.getId(), subject.getCode(), subject.getName(), educationTypeEntity, subject.getArchived());
  }


}
