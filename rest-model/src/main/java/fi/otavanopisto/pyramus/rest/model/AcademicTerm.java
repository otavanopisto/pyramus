package fi.otavanopisto.pyramus.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@XmlRootElement
public class AcademicTerm {

  public AcademicTerm() {
  }

  public AcademicTerm(Long id, String name, OffsetDateTime startDate, OffsetDateTime endDate, Boolean archived) {
    super();
    this.id = id;
    this.name = name;
    this.startDate = startDate;
    this.endDate = endDate;
    this.archived = archived;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public OffsetDateTime getStartDate() {
    return startDate;
  }
  
  public void setStartDate(OffsetDateTime startDate) {
    this.startDate = startDate;
  }
  
  public OffsetDateTime getEndDate() {
    return endDate;
  }
  
  public void setEndDate(OffsetDateTime endDate) {
    this.endDate = endDate;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  private Long id;
  private String name;
  private OffsetDateTime startDate;
  private OffsetDateTime endDate;
  private Boolean archived;
}
