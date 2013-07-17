package fi.pyramus.rest.tranquil.reports;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.reports.ReportContext.class, entityType = TranquilModelType.BASE)
public class ReportContextBase implements fi.tranquil.TranquilModelEntity {

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

  private Long id;

  private fi.pyramus.domainmodel.reports.ReportContextType context;

  public final static String[] properties = {"id","context"};
}
