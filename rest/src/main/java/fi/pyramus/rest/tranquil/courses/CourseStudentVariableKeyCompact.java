package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.courses.CourseStudentVariableKey.class, entityType = TranquilModelType.COMPACT)
public class CourseStudentVariableKeyCompact extends CourseStudentVariableKeyBase {

  public final static String[] properties = {};
}
