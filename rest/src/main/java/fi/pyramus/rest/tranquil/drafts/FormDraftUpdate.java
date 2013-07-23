package fi.pyramus.rest.tranquil.drafts;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.drafts.FormDraft.class, entityType = TranquilModelType.UPDATE)
public class FormDraftUpdate extends FormDraftComplete {

  public void setCreator(UserCompact creator) {
    super.setCreator(creator);
  }

  public UserCompact getCreator() {
    return (UserCompact)super.getCreator();
  }

  public final static String[] properties = {"creator"};
}
