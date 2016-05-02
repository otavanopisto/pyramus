package fi.pyramus.services.entities.base;

import fi.otavanopisto.pyramus.domainmodel.base.AcademicTerm;
import fi.pyramus.services.entities.EntityFactory;

public class AcademicTermEntityFactory implements EntityFactory<AcademicTermEntity> {

  public AcademicTermEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null)
      return null;
    
    AcademicTerm academicTerm = (AcademicTerm) domainObject; 
    return new AcademicTermEntity(academicTerm.getId(), academicTerm.getName(), academicTerm.getStartDate(), academicTerm.getEndDate(), academicTerm.getArchived());
  }
  
}
