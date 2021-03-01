package fi.otavanopisto.pyramus.domainmodel.worklist;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.constraints.NotEmpty;

import fi.otavanopisto.pyramus.domainmodel.base.ArchivableEntity;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Entity
@Indexed
public class WorklistItem implements ArchivableEntity {

  public Long getId() {
    return id;
  }

  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public Double getFactor() {
    return factor;
  }

  public void setFactor(Double factor) {
    this.factor = factor;
  }

  public CourseAssessment getCourseAssessment() {
    return courseAssessment;
  }

  public void setCourseAssessment(CourseAssessment courseAssessment) {
    this.courseAssessment = courseAssessment;
  }

  public Date getEntryDate() {
    return entryDate;
  }

  public void setEntryDate(Date entryDate) {
    this.entryDate = entryDate;
  }

  public WorklistItemTemplate getTemplate() {
    return template;
  }

  public void setTemplate(WorklistItemTemplate template) {
    this.template = template;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne 
  private WorklistItemTemplate template;

  @ManyToOne 
  private User owner;

  @NotNull
  @Column(nullable = false)
  @Temporal(value=TemporalType.TIMESTAMP)
  private Date entryDate;

  @NotNull
  @Column(nullable = false)
  @NotEmpty
  private String description;

  @Column
  private Double price;

  @Column
  private Double factor;

  @ManyToOne 
  private CourseAssessment courseAssessment;
  
  @NotNull
  @Column(nullable = false)
  private Boolean archived = Boolean.FALSE;

}
