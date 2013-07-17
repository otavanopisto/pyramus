package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.courses.CourseState.class, entityType = TranquilModelType.COMPACT)
public class CourseStateCompact extends CourseStateBase {

  public final static String[] properties = {};
}
