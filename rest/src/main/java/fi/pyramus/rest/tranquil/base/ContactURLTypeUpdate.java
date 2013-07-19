package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.ContactURLType.class, entityType = TranquilModelType.UPDATE)
public class ContactURLTypeUpdate extends ContactURLTypeComplete {

  public final static String[] properties = {};
}
