package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.Municipality.class, entityType = TranquilModelType.UPDATE)
public class MunicipalityUpdate extends MunicipalityComplete {

  public final static String[] properties = {};
}
