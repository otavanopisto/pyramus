package fi.otavanopisto.pyramus.domainmodel.worklist;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Indexed;

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

  public WorklistItemState getState() {
    return state;
  }

  public void setState(WorklistItemState state) {
    this.state = state;
  }

  public User getModifier() {
    return modifier;
  }

  public void setModifier(User modifier) {
    this.modifier = modifier;
  }

  public Date getModified() {
    return modified;
  }

  public void setModified(Date modified) {
    this.modified = modified;
  }

  public User getCreator() {
    return creator;
  }

  public void setCreator(User creator) {
    this.creator = creator;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public String getEditableFields() {
    return editableFields;
  }

  public void setEditableFields(String editableFields) {
    this.editableFields = editableFields;
  }

  public String getBillingNumber() {
    return billingNumber;
  }

  public void setBillingNumber(String billingNumber) {
    this.billingNumber = billingNumber;
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
  @Temporal(value=TemporalType.DATE)
  private Date entryDate;

  @NotNull
  @Column(nullable = false)
  @NotEmpty
  private String description;

  @NotNull
  @Column(nullable = false)
  private Double price;

  @NotNull
  @Column(nullable = false)
  private Double factor;
  
  @Column
  private String billingNumber;

  @ManyToOne 
  private CourseAssessment courseAssessment;

  @Column
  private String editableFields;

  @Column
  @Enumerated (EnumType.STRING)
  private WorklistItemState state;

  @NotNull
  @Column(nullable = false)
  @Temporal(value=TemporalType.TIMESTAMP)
  private Date created;

  @ManyToOne 
  private User creator;

  @NotNull
  @Column(nullable = false)
  @Temporal(value=TemporalType.TIMESTAMP)
  private Date modified;

  @ManyToOne 
  private User modifier;
  
  @NotNull
  @Column(nullable = false)
  private Boolean archived = Boolean.FALSE;

}
