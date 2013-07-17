package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.courses.CourseUserRole.class, entityType = TranquilModelType.COMPACT)
public class CourseUserRoleCompact extends CourseUserRoleBase {

  public final static String[] properties = {};
}
