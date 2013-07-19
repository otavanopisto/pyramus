package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.CourseBaseVariableKey.class, entityType = TranquilModelType.UPDATE)
public class CourseBaseVariableKeyUpdate extends CourseBaseVariableKeyComplete {

  public final static String[] properties = {};
}
