package fi.pyramus.rest.tranquil.help;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.help.HelpPageContent.class, entityType = TranquilModelType.COMPLETE)
public class HelpPageContentComplete extends HelpPageContentBase {

  public TranquilModelEntity getPage() {
    return page;
  }

  public void setPage(TranquilModelEntity page) {
    this.page = page;
  }

  public TranquilModelEntity getCreator() {
    return creator;
  }

  public void setCreator(TranquilModelEntity creator) {
    this.creator = creator;
  }

  public TranquilModelEntity getLastModifier() {
    return lastModifier;
  }

  public void setLastModifier(TranquilModelEntity lastModifier) {
    this.lastModifier = lastModifier;
  }

  private TranquilModelEntity page;

  private TranquilModelEntity creator;

  private TranquilModelEntity lastModifier;

  public final static String[] properties = {"page","creator","lastModifier"};
}
