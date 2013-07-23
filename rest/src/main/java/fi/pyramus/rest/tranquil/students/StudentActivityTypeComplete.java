package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.students.StudentActivityType.class, entityType = TranquilModelType.COMPLETE)
public class StudentActivityTypeComplete extends StudentActivityTypeBase {

  public final static String[] properties = {};
}
