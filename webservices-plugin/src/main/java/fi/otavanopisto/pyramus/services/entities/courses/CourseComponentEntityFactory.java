package fi.otavanopisto.pyramus.services.entities.courses;

import fi.otavanopisto.pyramus.domainmodel.courses.CourseComponent;
import fi.otavanopisto.pyramus.services.entities.EntityFactory;

public class CourseComponentEntityFactory implements EntityFactory<CourseComponentEntity> {

  public CourseComponentEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null)
      return null;
    
    CourseComponent courseComponent = (CourseComponent) domainObject;
    
    Double componentLengthUnits = null;
    Long componentLengthUnitId = null;
    if (courseComponent.getLength() != null) {
      componentLengthUnits = courseComponent.getLength().getUnits();
      if (courseComponent.getLength().getUnit() != null)
        componentLengthUnitId = courseComponent.getLength().getUnit().getId();
    }
    
    return new CourseComponentEntity(courseComponent.getId(), courseComponent.getName(), courseComponent.getDescription(), componentLengthUnits, componentLengthUnitId);
  }

}
