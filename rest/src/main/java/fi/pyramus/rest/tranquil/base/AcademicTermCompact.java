package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.base.AcademicTerm.class, entityType = TranquilModelType.COMPACT)
public class AcademicTermCompact extends AcademicTermBase {

  public final static String[] properties = {};
}
