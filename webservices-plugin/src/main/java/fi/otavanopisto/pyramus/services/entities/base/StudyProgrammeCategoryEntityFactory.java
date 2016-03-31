package fi.otavanopisto.pyramus.services.entities.base;

import fi.otavanopisto.pyramus.domainmodel.base.StudyProgrammeCategory;
import fi.otavanopisto.pyramus.services.entities.EntityFactory;
import fi.otavanopisto.pyramus.services.entities.EntityFactoryVault;

public class StudyProgrammeCategoryEntityFactory implements EntityFactory<StudyProgrammeCategoryEntity> {

  public StudyProgrammeCategoryEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null) {
      return null;
    }

    StudyProgrammeCategory studyProgrammeCategory = (StudyProgrammeCategory) domainObject;
    
    EducationTypeEntity educationTypeEntity = EntityFactoryVault
      .buildFromDomainObject(studyProgrammeCategory.getEducationType());
    
    return new StudyProgrammeCategoryEntity(studyProgrammeCategory.getId(), studyProgrammeCategory.getName(),
        studyProgrammeCategory.getArchived(), educationTypeEntity);
  }

}
