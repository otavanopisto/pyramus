package fi.pyramus.rest.tranquil.help;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.help.HelpItemTitle.class, entityType = TranquilModelType.COMPLETE)
public class HelpItemTitleComplete extends HelpItemTitleBase {

  public TranquilModelEntity getItem() {
    return item;
  }

  public void setItem(TranquilModelEntity item) {
    this.item = item;
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

  private TranquilModelEntity item;

  private TranquilModelEntity creator;

  private TranquilModelEntity lastModifier;

  public final static String[] properties = {"item","creator","lastModifier"};
}
