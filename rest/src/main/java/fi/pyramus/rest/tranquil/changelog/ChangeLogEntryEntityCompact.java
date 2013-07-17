package fi.pyramus.rest.tranquil.changelog;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.changelog.ChangeLogEntryEntity.class, entityType = TranquilModelType.COMPACT)
public class ChangeLogEntryEntityCompact extends ChangeLogEntryEntityBase {

  public final static String[] properties = {};
}
