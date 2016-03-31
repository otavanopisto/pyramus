package fi.otavanopisto.pyramus.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;

@XmlRootElement
public class AcademicTerm {

  public AcademicTerm() {
  }

  public AcademicTerm(Long id, String name, DateTime startDate, DateTime endDate, Boolean archived) {
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
  
  public DateTime getStartDate() {
    return startDate;
  }
  
  public void setStartDate(DateTime startDate) {
    this.startDate = startDate;
  }
  
  public DateTime getEndDate() {
    return endDate;
  }
  
  public void setEndDate(DateTime endDate) {
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
  private DateTime startDate;
  private DateTime endDate;
  private Boolean archived;
}
