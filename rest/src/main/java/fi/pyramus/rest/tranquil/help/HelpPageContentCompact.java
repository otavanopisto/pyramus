package fi.pyramus.rest.tranquil.help;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.help.HelpPageContent.class, entityType = TranquilModelType.COMPACT)
public class HelpPageContentCompact extends HelpPageContentBase {

  public Long getPage_id() {
    return page_id;
  }

  public void setPage_id(Long page_id) {
    this.page_id = page_id;
  }

  public Long getCreator_id() {
    return creator_id;
  }

  public void setCreator_id(Long creator_id) {
    this.creator_id = creator_id;
  }

  public Long getLastModifier_id() {
    return lastModifier_id;
  }

  public void setLastModifier_id(Long lastModifier_id) {
    this.lastModifier_id = lastModifier_id;
  }

  private Long page_id;

  private Long creator_id;

  private Long lastModifier_id;

  public final static String[] properties = {"page","creator","lastModifier"};
}
