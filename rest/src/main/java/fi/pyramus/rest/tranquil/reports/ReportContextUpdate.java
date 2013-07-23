package fi.pyramus.rest.tranquil.reports;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.reports.ReportContext.class, entityType = TranquilModelType.UPDATE)
public class ReportContextUpdate extends ReportContextComplete {

  public void setReport(ReportCompact report) {
    super.setReport(report);
  }

  public ReportCompact getReport() {
    return (ReportCompact)super.getReport();
  }

  public final static String[] properties = {"report"};
}
