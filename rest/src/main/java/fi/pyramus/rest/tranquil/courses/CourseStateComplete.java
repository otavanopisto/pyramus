package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.courses.CourseState.class, entityType = TranquilModelType.COMPLETE)
public class CourseStateComplete extends CourseStateBase {

  public final static String[] properties = {};
}
