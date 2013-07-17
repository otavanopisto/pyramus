package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.courses.CourseParticipationType.class, entityType = TranquilModelType.COMPACT)
public class CourseParticipationTypeCompact extends CourseParticipationTypeBase {

  public final static String[] properties = {};
}
