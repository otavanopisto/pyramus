package fi.otavanopisto.pyramus.domainmodel.reports;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.TableGenerator;

@Entity
public class ReportContext {

  public Long getId() {
    return id;
  }
  
  public Report getReport() {
    return report;
  }

  public void setReport(Report report) {
    this.report = report;
  }

  public ReportContextType getContext() {
    return context;
  }

  public void setContext(ReportContextType context) {
    this.context = context;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="ReportContext")  
  @TableGenerator(name="ReportContext", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @ManyToOne  
  @JoinColumn(name="report")
  private Report report;
  
  @Enumerated (EnumType.STRING)
  @Column (insertable = true, updatable = false, nullable = false)
  private ReportContextType context;
}
