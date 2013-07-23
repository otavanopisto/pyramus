package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.students.StudentEducationalLevel.class, entityType = TranquilModelType.COMPLETE)
public class StudentEducationalLevelComplete extends StudentEducationalLevelBase {

  public final static String[] properties = {};
}
