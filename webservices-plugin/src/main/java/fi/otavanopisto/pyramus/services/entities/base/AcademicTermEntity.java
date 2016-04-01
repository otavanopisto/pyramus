package fi.otavanopisto.pyramus.services.entities.base;

import java.util.Date;

public class AcademicTermEntity {

  public AcademicTermEntity() {
  }
  
  public AcademicTermEntity(Long id, String name, Date startDate, Date endDate, Boolean archived) {
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

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
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
  private Date startDate;
  private Date endDate;
  private Boolean archived;
}
