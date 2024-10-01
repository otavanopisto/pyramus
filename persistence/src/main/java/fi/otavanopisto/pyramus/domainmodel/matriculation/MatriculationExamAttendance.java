package fi.otavanopisto.pyramus.domainmodel.matriculation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

import org.hibernate.search.annotations.DocumentId;

import fi.otavanopisto.pyramus.domainmodel.grading.ProjectAssessment;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamAttendanceFunding;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamAttendanceStatus;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamGrade;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamSubject;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamTerm;

@Entity
public class MatriculationExamAttendance {

  /**
   * Returns the unique identifier of this object.
   * 
   * @return The unique identifier of this object
   */
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
  public MatriculationExamEnrollment getEnrollment() {
    return enrollment;
  }

  public void setEnrollment(MatriculationExamEnrollment enrollment) {
    this.enrollment = enrollment;
  }

  public MatriculationExamSubject getSubject() {
    return subject;
  }

  public void setSubject(MatriculationExamSubject subject) {
    this.subject = subject;
  }

  public Boolean isMandatory() {
    return mandatory;
  }

  public void setMandatory(Boolean mandatory) {
    this.mandatory = mandatory;
  }

  public Boolean isRetry() {
    return retry;
  }

  public void setRetry(Boolean retry) {
    this.retry = retry;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public MatriculationExamTerm getTerm() {
    return term;
  }

  public void setTerm(MatriculationExamTerm term) {
    this.term = term;
  }

  public MatriculationExamAttendanceStatus getStatus() {
    return status;
  }

  public void setStatus(MatriculationExamAttendanceStatus status) {
    this.status = status;
  }

  public MatriculationExamGrade getGrade() {
    return grade;
  }

  public void setGrade(MatriculationExamGrade grade) {
    this.grade = grade;
  }

  public ProjectAssessment getProjectAssessment() {
    return projectAssessment;
  }

  public void setProjectAssessment(ProjectAssessment projectAssessment) {
    this.projectAssessment = projectAssessment;
  }

  public MatriculationExamAttendanceFunding getFunding() {
    return funding;
  }

  public void setFunding(MatriculationExamAttendanceFunding funding) {
    this.funding = funding;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="MatriculationExamAttendance")  
  @TableGenerator(name="MatriculationExamAttendance", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  @DocumentId 
  private Long id;
  
  @ManyToOne
  private MatriculationExamEnrollment enrollment;
  
  @Column
  @Enumerated(EnumType.STRING)
  private MatriculationExamSubject subject;
  
  @Column
  private Boolean mandatory;
  
  @Column
  private Boolean retry;
  
  @Column
  private Integer year;
  
  @Column
  @Enumerated(EnumType.ORDINAL)
  private MatriculationExamTerm term;
  
  @Column
  @Enumerated(EnumType.STRING)
  private MatriculationExamAttendanceStatus status;

  @Column
  @Enumerated(EnumType.STRING)
  private MatriculationExamAttendanceFunding funding;
  
  @Column
  @Enumerated(EnumType.STRING)
  private MatriculationExamGrade grade;
  
  @ManyToOne
  private ProjectAssessment projectAssessment;
}