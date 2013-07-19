package fi.pyramus.rest.tranquil.help;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.help.HelpPageContent.class, entityType = TranquilModelType.UPDATE)
public class HelpPageContentUpdate extends HelpPageContentComplete {

  public void setPage(HelpPageCompact page) {
    super.setPage(page);
  }

  public HelpPageCompact getPage() {
    return (HelpPageCompact)super.getPage();
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

  public final static String[] properties = {"page","creator","lastModifier"};
}
