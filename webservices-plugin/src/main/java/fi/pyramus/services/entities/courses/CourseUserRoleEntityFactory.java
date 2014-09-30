package fi.pyramus.services.entities.courses;

import fi.pyramus.domainmodel.courses.CourseUserRole;
import fi.pyramus.services.entities.EntityFactory;
import fi.pyramus.services.entities.courses.CourseUserRoleEntity;

public class CourseUserRoleEntityFactory implements EntityFactory<CourseUserRoleEntity> {

  public CourseUserRoleEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null) {
      return null;
    }
    
    // Temporary fix that maps role enum to old role system
    
    CourseUserRole courseUserRole = (CourseUserRole) domainObject;
    switch (courseUserRole) {
      case TEACHER:
        return new CourseUserRoleEntity(1l, "Opettaja");   
      case TUTOR:
        return new CourseUserRoleEntity(2l, "Tutor");   
      default:
        return new CourseUserRoleEntity(3l, "Vastuuhenkilö");   
    }
    
  }

}
