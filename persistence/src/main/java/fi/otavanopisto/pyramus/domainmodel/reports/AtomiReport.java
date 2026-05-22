package fi.otavanopisto.pyramus.domainmodel.reports;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import fi.otavanopisto.pyramus.atomi.AtomiReportType;

@Entity
public class AtomiReport {

  public Long getId() {
    return id;
  }

  public AtomiReportType getType() {
    return type;
  }

  public void setType(AtomiReportType type) {
    this.type = type;
  }

  public Report getReport() {
    return report;
  }

  public void setReport(Report report) {
    this.report = report;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)  
  private Long id;

  @Enumerated (EnumType.STRING)
  @Column (unique = true, updatable = false, nullable = false)
  private AtomiReportType type;

  @ManyToOne
  private Report report;
}
