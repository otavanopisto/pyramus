package fi.pyramus.services.entities.base;

import fi.pyramus.domainmodel.base.StudyProgrammeCategory;
import fi.pyramus.services.entities.EntityFactory;
import fi.pyramus.services.entities.EntityFactoryVault;

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
