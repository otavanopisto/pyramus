package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.courses.CourseParticipationType.class, entityType = TranquilModelType.UPDATE)
public class CourseParticipationTypeUpdate extends CourseParticipationTypeComplete {

  public final static String[] properties = {};
}
