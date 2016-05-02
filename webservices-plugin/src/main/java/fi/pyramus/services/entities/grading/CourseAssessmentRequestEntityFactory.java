package fi.pyramus.services.entities.grading;

import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessmentRequest;
import fi.pyramus.services.entities.EntityFactory;

public class CourseAssessmentRequestEntityFactory implements EntityFactory<CourseAssessmentRequestEntity> {

  public CourseAssessmentRequestEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null) {
      return null;
    }
    
    CourseAssessmentRequest courseAssessmentRequest = (CourseAssessmentRequest) domainObject;
    
    return new CourseAssessmentRequestEntity(
        courseAssessmentRequest.getId(),
        courseAssessmentRequest.getCourseStudent().getId(),
        courseAssessmentRequest.getCreated(),
        courseAssessmentRequest.getRequestText());
  }

}
