package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.students.StudentVariableKey.class, entityType = TranquilModelType.UPDATE)
public class StudentVariableKeyUpdate extends StudentVariableKeyComplete {

  public final static String[] properties = {};
}
