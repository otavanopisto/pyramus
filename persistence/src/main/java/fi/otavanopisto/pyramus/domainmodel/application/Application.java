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
import org.hibernate.search.annotations.FullTextFilterDef;
import org.hibernate.search.annotations.FullTextFilterDefs;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.constraints.NotEmpty;

import fi.otavanopisto.pyramus.domainmodel.base.ArchivableEntity;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.persistence.search.filters.ArchivedEntityFilterFactory;

@Entity
@Indexed
@FullTextFilterDefs (
  @FullTextFilterDef (
    name="ArchivedApplication",
    impl=ArchivedEntityFilterFactory.class
  )
)
public class Application implements ArchivableEntity {
  
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

  public String getLine() {
    return line;
  }

  public void setLine(String line) {
    this.line = line;
  }

  public Boolean getApplicantEditable() {
    return applicantEditable;
  }

  public void setApplicantEditable(Boolean applicantEditable) {
    this.applicantEditable = applicantEditable;
  }

  public StaffMember getHandler() {
    return handler;
  }

  public void setHandler(StaffMember handler) {
    this.handler = handler;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="Application")
  @TableGenerator(name="Application", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @NotNull
  @Column (nullable = false, unique = true)
  @NotEmpty
  @Field
  private String applicationId;

  @NotNull
  @Column (nullable = false)
  @NotEmpty
  @Field
  private String line;

  @NotNull
  @Column (nullable = false)
  @NotEmpty
  @Field
  private String firstName;

  @NotNull
  @Column (nullable = false)
  @NotEmpty
  @Field
  private String lastName;

  @NotNull
  @Column (nullable = false)
  @NotEmpty
  @Field
  private String email;

  @NotNull
  @Column (nullable = false)
  @NotEmpty
  @Field
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
  @Field
  private String formData;
  
  @Column
  @Enumerated (EnumType.STRING)
  @Field
  private ApplicationState state;

  @ManyToOne  
  @JoinColumn(name="handler")
  private StaffMember handler;

  @NotNull
  @Column (nullable = false)
  @Field
  private Boolean applicantEditable;

  @NotNull
  @Column (nullable = false)
  @Field
  private Boolean archived;

}
