package fi.pyramus.services.entities.base;

import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.services.entities.EntityFactory;
import fi.pyramus.services.entities.EntityFactoryVault;

public class EducationTypeEntityFactory implements EntityFactory<EducationTypeEntity> {

  public EducationTypeEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null)
      return null;
    
    EducationType educationType = (EducationType) domainObject; 
    EducationSubtypeEntity[] subtypes = (EducationSubtypeEntity[]) EntityFactoryVault.buildFromDomainObjects(educationType.getSubtypes());
    return new EducationTypeEntity(educationType.getId(), educationType.getName(), educationType.getCode(), subtypes,educationType.getArchived());
  }

}
