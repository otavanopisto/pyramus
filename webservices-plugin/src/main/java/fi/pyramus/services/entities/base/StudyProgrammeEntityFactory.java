package fi.pyramus.services.entities.base;

import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.pyramus.services.entities.EntityFactory;
import fi.pyramus.services.entities.EntityFactoryVault;

public class StudyProgrammeEntityFactory implements EntityFactory<StudyProgrammeEntity> {

  public StudyProgrammeEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null) {
      return null;
    }
    StudyProgramme studyProgramme = (StudyProgramme) domainObject;

    StudyProgrammeCategoryEntity categoryEntity = EntityFactoryVault
        .buildFromDomainObject(studyProgramme.getCategory());

    return new StudyProgrammeEntity(studyProgramme.getId(), studyProgramme.getName(), studyProgramme.getCode(),
        categoryEntity, studyProgramme.getArchived());
  }

}
