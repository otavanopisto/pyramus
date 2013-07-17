package fi.pyramus.rest.tranquil.drafts;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.drafts.FormDraft.class, entityType = TranquilModelType.COMPACT)
public class FormDraftCompact extends FormDraftBase {

  public Long getCreator_id() {
    return creator_id;
  }

  public void setCreator_id(Long creator_id) {
    this.creator_id = creator_id;
  }

  private Long creator_id;

  public final static String[] properties = {"creator"};
}
