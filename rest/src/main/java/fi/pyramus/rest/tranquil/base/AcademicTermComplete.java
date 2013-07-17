package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.AcademicTerm.class, entityType = TranquilModelType.COMPLETE)
public class AcademicTermComplete extends AcademicTermBase {

  public final static String[] properties = {};
}
