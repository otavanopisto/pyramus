package fi.pyramus.rest.tranquil.reports;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.reports.ReportContext.class, entityType = TranquilModelType.COMPACT)
public class ReportContextCompact extends ReportContextBase {

  public Long getReport_id() {
    return report_id;
  }

  public void setReport_id(Long report_id) {
    this.report_id = report_id;
  }

  private Long report_id;

  public final static String[] properties = {"report"};
}
