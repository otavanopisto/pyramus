package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.base.Language.class, entityType = TranquilModelType.COMPACT)
public class LanguageCompact extends LanguageBase {

  public final static String[] properties = {};
}
