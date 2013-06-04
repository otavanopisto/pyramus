package fi.pyramus.services.entities.base;

import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.services.entities.EntityFactory;

public class EducationalTimeUnitEntityFactory implements EntityFactory<EducationalTimeUnitEntity> {

  public EducationalTimeUnitEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null)
      return null;
    
    EducationalTimeUnit educationalTimeUnit = (EducationalTimeUnit) domainObject; 
    return new EducationalTimeUnitEntity(educationalTimeUnit.getId(), educationalTimeUnit.getBaseUnits(), educationalTimeUnit.getName(), educationalTimeUnit.getArchived());
  }

}
