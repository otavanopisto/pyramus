package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.AcademicTerm.class, entityType = TranquilModelType.UPDATE)
public class AcademicTermUpdate extends AcademicTermComplete {

  public final static String[] properties = {};
}
