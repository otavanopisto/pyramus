package fi.pyramus.rest.tranquil.reports;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.reports.ReportContext.class, entityType = TranquilModelType.COMPLETE)
public class ReportContextComplete extends ReportContextBase {

  public TranquilModelEntity getReport() {
    return report;
  }

  public void setReport(TranquilModelEntity report) {
    this.report = report;
  }

  private TranquilModelEntity report;

  public final static String[] properties = {"report"};
}
