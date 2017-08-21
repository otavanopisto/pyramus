package fi.otavanopisto.pyramus.domainmodel.application;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.NotEmpty;

import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Entity
public class Application {
  
  public Long getId() {
    return id;
  }

  public String getApplicationId() {
    return applicationId;
  }

  public void setApplicationId(String applicationId) {
    this.applicationId = applicationId;
  }

  public String getReferenceCode() {
    return referenceCode;
  }

  public void setReferenceCode(String referenceCode) {
    this.referenceCode = referenceCode;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public Date getLastModified() {
    return lastModified;
  }

  public void setLastModified(Date lastModified) {
    this.lastModified = lastModified;
  }

  public User getLastModifier() {
    return lastModifier;
  }

  public void setLastModifier(User lastModifier) {
    this.lastModifier = lastModifier;
  }

  public Date getApplicantLastModified() {
    return applicantLastModified;
  }

  public void setApplicantLastModified(Date applicantLastModified) {
    this.applicantLastModified = applicantLastModified;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public StudyProgramme getStudyProgramme() {
    return studyProgramme;
  }

  public void setStudyProgramme(StudyProgramme studyProgramme) {
    this.studyProgramme = studyProgramme;
  }

  public ApplicationState getState() {
    return state;
  }

  public void setState(ApplicationState state) {
    this.state = state;
  }

  public String getFormData() {
    return formData;
  }

  public void setFormData(String formData) {
    this.formData = formData;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="Application")
  @TableGenerator(name="Application", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @NotNull
  @Column (nullable = false, unique = true)
  @NotEmpty
  private String applicationId;

  @ManyToOne  
  @JoinColumn(name="studyProgramme")
  private StudyProgramme studyProgramme;

  @NotNull
  @Column (nullable = false)
  @NotEmpty
  private String firstName;

  @NotNull
  @Column (nullable = false)
  @NotEmpty
  private String lastName;

  @NotNull
  @Column (nullable = false)
  @NotEmpty
  private String email;

  @NotNull
  @Column (nullable = false)
  @NotEmpty
  private String referenceCode;

  @NotNull
  @Column (updatable=false, nullable=false)
  @Temporal (value=TemporalType.TIMESTAMP)
  private Date created;

  @ManyToOne  
  @JoinColumn(name="lastModifier")
  private User lastModifier;

  @NotNull
  @Column (nullable=false)
  @Temporal (value=TemporalType.TIMESTAMP)
  private Date lastModified;

  @NotNull
  @Column (nullable=false)
  @Temporal (value=TemporalType.TIMESTAMP)
  private Date applicantLastModified;
  
  @Lob
  @NotNull
  @Column (nullable=false)
  private String formData;
  
  @Column
  @Enumerated (EnumType.STRING)
  @Field(store = Store.NO)
  private ApplicationState state;

  @NotNull
  @Column (nullable = false)
  @Field
  private Boolean archived;

}
