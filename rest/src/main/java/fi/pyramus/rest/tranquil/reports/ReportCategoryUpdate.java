package fi.pyramus.rest.tranquil.reports;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.reports.ReportCategory.class, entityType = TranquilModelType.UPDATE)
public class ReportCategoryUpdate extends ReportCategoryComplete {

  public final static String[] properties = {};
}
