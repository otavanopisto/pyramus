package fi.pyramus.rest.tranquil.drafts;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.drafts.FormDraft.class, entityType = TranquilModelType.COMPLETE)
public class FormDraftComplete extends FormDraftBase {

  public TranquilModelEntity getCreator() {
    return creator;
  }

  public void setCreator(TranquilModelEntity creator) {
    this.creator = creator;
  }

  private TranquilModelEntity creator;

  public final static String[] properties = {"creator"};
}
