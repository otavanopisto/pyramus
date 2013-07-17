package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.ContactType.class, entityType = TranquilModelType.COMPLETE)
public class ContactTypeComplete extends ContactTypeBase {

  public final static String[] properties = {};
}
