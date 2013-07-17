package fi.pyramus.rest.tranquil.reports;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.reports.ReportCategory.class, entityType = TranquilModelType.COMPLETE)
public class ReportCategoryComplete extends ReportCategoryBase {

  public final static String[] properties = {};
}
