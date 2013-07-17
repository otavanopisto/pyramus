package fi.pyramus.rest.tranquil.reports;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.reports.Report.class, entityType = TranquilModelType.COMPLETE)
public class ReportComplete extends ReportBase {

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

  public TranquilModelEntity getCategory() {
    return category;
  }

  public void setCategory(TranquilModelEntity category) {
    this.category = category;
  }

  private TranquilModelEntity creator;

  private TranquilModelEntity lastModifier;

  private TranquilModelEntity category;

  public final static String[] properties = {"creator","lastModifier","category"};
}
