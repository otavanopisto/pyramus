package fi.pyramus.services.entities.base;

import fi.pyramus.domainmodel.base.EducationSubtype;
import fi.pyramus.services.entities.EntityFactory;

public class EducationSubtypeEntityFactory implements EntityFactory<EducationSubtypeEntity> {

  public EducationSubtypeEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null)
      return null;
    
    EducationSubtype educationSubtype = (EducationSubtype) domainObject;
    return new EducationSubtypeEntity(educationSubtype.getId(), educationSubtype.getName(), educationSubtype.getCode(), educationSubtype.getEducationType().getId(), educationSubtype.getArchived());
  }

}
