package fi.pyramus.rest.tranquil.reports;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.reports.ReportContext.class, entityType = TranquilModelType.COMPACT)
public class ReportContextEntity implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public fi.pyramus.domainmodel.reports.ReportContextType getContext() {
    return context;
  }

  public void setContext(fi.pyramus.domainmodel.reports.ReportContextType context) {
    this.context = context;
  }

  public Long getReport_id() {
    return report_id;
  }

  public void setReport_id(Long report_id) {
    this.report_id = report_id;
  }

  private Long id;

  private fi.pyramus.domainmodel.reports.ReportContextType context;

  private Long report_id;

  public final static String[] properties = {"id","context","report"};
}
