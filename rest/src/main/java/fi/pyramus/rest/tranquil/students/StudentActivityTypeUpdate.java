package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.students.StudentActivityType.class, entityType = TranquilModelType.UPDATE)
public class StudentActivityTypeUpdate extends StudentActivityTypeComplete {

  public final static String[] properties = {};
}
