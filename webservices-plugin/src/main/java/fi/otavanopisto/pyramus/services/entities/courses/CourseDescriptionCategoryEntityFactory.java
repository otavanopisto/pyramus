package fi.otavanopisto.pyramus.services.entities.courses;

import fi.otavanopisto.pyramus.domainmodel.courses.CourseDescriptionCategory;
import fi.otavanopisto.pyramus.services.entities.EntityFactory;

public class CourseDescriptionCategoryEntityFactory implements EntityFactory<CourseDescriptionCategoryEntity> {

  public CourseDescriptionCategoryEntity buildFromDomainObject(Object domainObject) {
    if (domainObject == null)
      return null;

    CourseDescriptionCategory category = (CourseDescriptionCategory) domainObject;

    return new CourseDescriptionCategoryEntity(category.getId(), category.getName());
  }
}
