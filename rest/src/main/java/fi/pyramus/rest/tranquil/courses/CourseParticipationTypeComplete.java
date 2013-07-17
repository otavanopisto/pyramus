package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.courses.CourseParticipationType.class, entityType = TranquilModelType.COMPLETE)
public class CourseParticipationTypeComplete extends CourseParticipationTypeBase {

  public final static String[] properties = {};
}
