package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.students.StudentExaminationType.class, entityType = TranquilModelType.UPDATE)
public class StudentExaminationTypeUpdate extends StudentExaminationTypeComplete {

  public final static String[] properties = {};
}
