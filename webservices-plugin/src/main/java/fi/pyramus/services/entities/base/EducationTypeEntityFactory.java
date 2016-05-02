package fi.pyramus.services.entities.base;

import java.util.List;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.domainmodel.base.EducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.pyramus.services.entities.EntityFactory;
import fi.pyramus.services.entities.EntityFactoryVault;

public class EducationTypeEntityFactory implements EntityFactory<EducationTypeEntity> {

  public EducationTypeEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null)
      return null;
    
    EducationType educationType = (EducationType) domainObject; 
    List<EducationSubtype> educationSubtypes = DAOFactory.getInstance().getEducationSubtypeDAO().listByEducationType(educationType);
    
    EducationSubtypeEntity[] subtypes = (EducationSubtypeEntity[]) EntityFactoryVault.buildFromDomainObjects(educationSubtypes);
    return new EducationTypeEntity(educationType.getId(), educationType.getName(), educationType.getCode(), subtypes,educationType.getArchived());
  }

}
