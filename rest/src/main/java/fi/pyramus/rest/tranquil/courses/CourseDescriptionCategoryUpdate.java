package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.courses.CourseDescriptionCategory.class, entityType = TranquilModelType.UPDATE)
public class CourseDescriptionCategoryUpdate extends CourseDescriptionCategoryComplete {

  public final static String[] properties = {};
}
