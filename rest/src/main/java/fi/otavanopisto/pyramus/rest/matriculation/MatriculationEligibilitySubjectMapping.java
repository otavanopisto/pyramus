package fi.otavanopisto.pyramus.rest.matriculation;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model for matriculation eligibility subject.
 * 
 * @author Antti Leppä
 * @author Heikki Kurhinen
 */
public class MatriculationEligibilitySubjectMapping {

  @JsonProperty("comment")
  private String comment;

  @JsonProperty("passing-grades")
  private Integer passingGrades;

  @JsonProperty("education-type")
  private String educationType;

  @JsonProperty("education-subtype")
  private String educationSubtype;

  @JsonProperty("transfer-credit-only-mandatory")
  private Boolean transferCreditOnlyMandatory;
  
  @JsonProperty(value = "included-subjects", required = false)
  private List<String> includedSubjects;
  
  public MatriculationEligibilitySubjectMapping() {
    includedSubjects = new ArrayList<>();
  }
  
  /**
   * Returns comment
   * 
   * @return comment
   */
  public String getComment() {
    return comment;
  }

  /**
   * Sets comment
   * 
   * @param comment comment
   */
  public void setComment(String comment) {
    this.comment = comment;
  }

  /**
   * Returns number of required passing grades
   * 
   * @return number of required passing grades
   */
  public Integer getPassingGrades() {
    return passingGrades;
  }

  /**
   * Sets number of required passing grades
   * 
   * @param passingGrades number of required passing grades
   */
  public void setPassingGrades(Integer passingGrades) {
    this.passingGrades = passingGrades;
  }

  /**
   * Returns required education type
   * 
   * @return required education type
   */
  public String getEducationType() {
    return educationType;
  }

  /**
   * Sets required education type
   * 
   * @param educationType required education type
   */
  public void setEducationType(String educationType) {
    this.educationType = educationType;
  }

  /**
   * Returns required education subtype
   * 
   * @return required education subtype
   */
  public String getEducationSubtype() {
    return educationSubtype;
  }

  /**
   * Sets required education subtype
   * 
   * @param educationSubtype required education subtype
   */
  public void setEducationSubtype(String educationSubtype) {
    this.educationSubtype = educationSubtype;
  }

  /**
   * Returns whether only mandatory transfer credits should be accepted
   * 
   * @return whether only mandatory transfer credits should be accepted
   */
  public Boolean getTransferCreditOnlyMandatory() {
    return transferCreditOnlyMandatory;
  }
  
  /**
   * Sets whether only mandatory transfer credits should be accepted
   * 
   * @param transferCreditOnlyMandatory whether only mandatory transfer credits should be accepted
   */
  public void setTransferCreditOnlyMandatory(Boolean transferCreditOnlyMandatory) {
    this.transferCreditOnlyMandatory = transferCreditOnlyMandatory;
  }

  public void addIncludedSubject(String subject) {
    this.includedSubjects.add(subject);
  }
  
  public List<String> getIncludedSubjects() {
    return includedSubjects;
  }

  public void setIncludedSubjects(List<String> includedSubjects) {
    this.includedSubjects = includedSubjects;
  }
  
}