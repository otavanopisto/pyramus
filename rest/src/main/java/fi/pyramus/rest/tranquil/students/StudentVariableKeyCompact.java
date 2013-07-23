package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.students.StudentVariableKey.class, entityType = TranquilModelType.COMPACT)
public class StudentVariableKeyCompact extends StudentVariableKeyBase {

  public final static String[] properties = {};
}
