package fi.pyramus.rest.tranquil.help;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.help.HelpItemTitle.class, entityType = TranquilModelType.UPDATE)
public class HelpItemTitleUpdate extends HelpItemTitleComplete {

  public void setItem(HelpItemCompact item) {
    super.setItem(item);
  }

  public HelpItemCompact getItem() {
    return (HelpItemCompact)super.getItem();
  }

  public void setCreator(UserCompact creator) {
    super.setCreator(creator);
  }

  public UserCompact getCreator() {
    return (UserCompact)super.getCreator();
  }

  public void setLastModifier(UserCompact lastModifier) {
    super.setLastModifier(lastModifier);
  }

  public UserCompact getLastModifier() {
    return (UserCompact)super.getLastModifier();
  }

  public final static String[] properties = {"item","creator","lastModifier"};
}
