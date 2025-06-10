package fi.otavanopisto.pyramus.domainmodel.application;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class ApplicationSignatures {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Application getApplication() {
    return application;
  }

  public void setApplication(Application application) {
    this.application = application;
  }

  public String getStaffDocumentId() {
    return staffDocumentId;
  }

  public void setStaffDocumentId(String staffDocumentId) {
    this.staffDocumentId = staffDocumentId;
  }

  public ApplicationSignatureState getStaffDocumentState() {
    return staffDocumentState;
  }

  public void setStaffDocumentState(ApplicationSignatureState staffDocumentState) {
    this.staffDocumentState = staffDocumentState;
  }

  public Date getStaffDocumentModified() {
    return staffDocumentModified;
  }

  public void setStaffDocumentModified(Date staffDocumentModified) {
    this.staffDocumentModified = staffDocumentModified;
  }

  public String getApplicantDocumentId() {
    return applicantDocumentId;
  }

  public void setApplicantDocumentId(String applicantDocumentId) {
    this.applicantDocumentId = applicantDocumentId;
  }

  public ApplicationSignatureState getApplicantDocumentState() {
    return applicantDocumentState;
  }

  public void setApplicantDocumentState(ApplicationSignatureState applicantDocumentState) {
    this.applicantDocumentState = applicantDocumentState;
  }

  public String getApplicantInvitationId() {
    return applicantInvitationId;
  }

  public void setApplicantInvitationId(String applicantInvitationId) {
    this.applicantInvitationId = applicantInvitationId;
  }

  public String getApplicantInvitationToken() {
    return applicantInvitationToken;
  }

  public void setApplicantInvitationToken(String applicantInvitationToken) {
    this.applicantInvitationToken = applicantInvitationToken;
  }

  public Date getApplicantDocumentModified() {
    return applicantDocumentModified;
  }

  public void setApplicantDocumentModified(Date applicantDocumentModified) {
    this.applicantDocumentModified = applicantDocumentModified;
  }

  public String getStaffInvitationId() {
    return staffInvitationId;
  }

  public void setStaffInvitationId(String staffInvitationId) {
    this.staffInvitationId = staffInvitationId;
  }

  public String getStaffInvitationToken() {
    return staffInvitationToken;
  }

  public void setStaffInvitationToken(String staffInvitationToken) {
    this.staffInvitationToken = staffInvitationToken;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="ApplicationSignatures")
  @TableGenerator(name="ApplicationSignatures", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @OneToOne  
  @JoinColumn(name="application")
  private Application application;

  private String staffDocumentId;

  private String staffInvitationId;

  private String staffInvitationToken;
  
  @Enumerated (EnumType.STRING)
  private ApplicationSignatureState staffDocumentState;

  @Temporal (value=TemporalType.TIMESTAMP)
  private Date staffDocumentModified;
  
  private String applicantDocumentId;

  @Enumerated (EnumType.STRING)
  private ApplicationSignatureState applicantDocumentState;

  private String applicantInvitationId;

  private String applicantInvitationToken;

  @Temporal (value=TemporalType.TIMESTAMP)
  private Date applicantDocumentModified;

}