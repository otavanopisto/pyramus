package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.Language.class, entityType = TranquilModelType.UPDATE)
public class LanguageUpdate extends LanguageComplete {

  public final static String[] properties = {};
}
