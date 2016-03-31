package fi.otavanopisto.pyramus.services.entities.courses;

import fi.otavanopisto.pyramus.domainmodel.courses.CourseDescription;
import fi.otavanopisto.pyramus.services.entities.EntityFactory;
import fi.otavanopisto.pyramus.services.entities.EntityFactoryVault;

public class CourseDescriptionEntityFactory implements EntityFactory<CourseDescriptionEntity> {

  public CourseDescriptionEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null)
      return null;

    CourseDescription desc = (CourseDescription) domainObject;

    CourseDescriptionCategoryEntity category = EntityFactoryVault.buildFromDomainObject(desc.getCategory());
    
    return new CourseDescriptionEntity(desc.getId(), desc.getCourseBase().getId(), category, desc.getDescription());
  }
}
