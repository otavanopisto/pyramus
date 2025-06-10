package fi.otavanopisto.pyramus.domainmodel.application;

import java.util.Date;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

import fi.otavanopisto.pyramus.domainmodel.base.ArchivableEntity;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Indexed
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

  public Student getStudent() {
    return student;
  }

  public void setStudent(Student student) {
    this.student = student;
  }

  public String getCredentialToken() {
    return credentialToken;
  }

  public void setCredentialToken(String credentialToken) {
    this.credentialToken = credentialToken;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="Application")
  @TableGenerator(name="Application", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @NotNull
  @Column (nullable = false, unique = true)
  @NotEmpty
  @KeywordField
  private String applicationId;

  @NotNull
  @Column (nullable = false)
  @NotEmpty
  @KeywordField
  private String line;

  @NotNull
  @Column (nullable = false)
  @NotEmpty
  @FullTextField
  private String firstName;

  @NotNull
  @Column (nullable = false)
  @NotEmpty
  @FullTextField
  private String lastName;

  @NotNull
  @Column (nullable = false)
  @NotEmpty
  @KeywordField
  private String email;

  @NotNull
  @Column (nullable = false)
  @NotEmpty
  @KeywordField
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
  @Basic(optional = false)
  @FullTextField
  private String formData;
  
  @Column
  @Enumerated (EnumType.STRING)
  @KeywordField
  private ApplicationState state;

  @ManyToOne  
  @JoinColumn(name="handler")
  private StaffMember handler;
  
  @OneToOne
  @JoinColumn(name="student")
  private Student student;

  @KeywordField
  private String credentialToken;

  @NotNull
  @Column (nullable = false)
  @GenericField
  private Boolean applicantEditable;

  @NotNull
  @Column (nullable = false)
  @GenericField
  private Boolean archived;

}
