package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.courses.CourseEnrolmentType.class, entityType = TranquilModelType.COMPLETE)
public class CourseEnrolmentTypeComplete extends CourseEnrolmentTypeBase {

  public final static String[] properties = {};
}
