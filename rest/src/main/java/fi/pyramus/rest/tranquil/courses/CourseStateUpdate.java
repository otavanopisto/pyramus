package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.courses.CourseState.class, entityType = TranquilModelType.UPDATE)
public class CourseStateUpdate extends CourseStateComplete {

  public final static String[] properties = {};
}
