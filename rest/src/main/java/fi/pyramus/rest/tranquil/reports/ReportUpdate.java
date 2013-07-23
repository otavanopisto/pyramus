package fi.pyramus.rest.tranquil.reports;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.reports.Report.class, entityType = TranquilModelType.UPDATE)
public class ReportUpdate extends ReportComplete {

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

  public void setCategory(ReportCategoryCompact category) {
    super.setCategory(category);
  }

  public ReportCategoryCompact getCategory() {
    return (ReportCategoryCompact)super.getCategory();
  }

  public final static String[] properties = {"creator","lastModifier","category"};
}
