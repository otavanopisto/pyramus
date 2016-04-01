package fi.otavanopisto.pyramus.services.entities.grading;

import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.services.entities.EntityFactory;
import fi.otavanopisto.pyramus.services.entities.EntityFactoryVault;
import fi.otavanopisto.pyramus.services.entities.users.UserEntity;

public class CourseAssessmentEntityFactory implements EntityFactory<CourseAssessmentEntity> {

  public CourseAssessmentEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null) {
      return null;
    }
    
    CourseAssessment courseAssessment = (CourseAssessment) domainObject;
    GradeEntity grade = EntityFactoryVault.buildFromDomainObject(courseAssessment.getGrade());
    UserEntity assessingUser = EntityFactoryVault.buildFromDomainObject(courseAssessment.getAssessor());

    return new CourseAssessmentEntity(courseAssessment.getId(), courseAssessment.getStudent().getId(), courseAssessment.getDate(), 
        grade, courseAssessment.getVerbalAssessment(), assessingUser, courseAssessment.getArchived(),
        courseAssessment.getCourseStudent().getCourse().getId(), courseAssessment.getCourseStudent().getId());
  }

}
