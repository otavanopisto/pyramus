package fi.pyramus.services.entities.grading;

import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.pyramus.services.entities.EntityFactory;
import fi.pyramus.services.entities.EntityFactoryVault;

public class GradeEntityFactory implements EntityFactory<GradeEntity> {

  public GradeEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null) {
      return null;
    }
    Grade grade = (Grade) domainObject;
    GradingScaleEntity gradingScale = EntityFactoryVault.buildFromDomainObject(grade.getGradingScale());
    return new GradeEntity(grade.getId(), grade.getName(), grade.getDescription(), gradingScale, grade.getPassingGrade(), grade
        .getArchived(), grade.getQualification(), grade.getGPA());
  }

}
