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
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import fi.otavanopisto.pyramus.domainmodel.base.ArchivableEntity;

@Entity
@Indexed
public class MatriculationExamAttendance implements ArchivableEntity {

  /**
   * Returns the unique identifier of this object.
   * 
   * @return The unique identifier of this object
   */
  public Long getId() {
    return id;
  }

  @Override
  public Boolean getArchived() {
    return archived;
  }

  @Override
  public void setArchived(Boolean archived) {
    this.archived = archived;
  }
  
  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
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

  public Boolean isRepeat() {
    return repeat;
  }

  public void setRepeat(Boolean repeat) {
    this.repeat = repeat;
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
  
  @Column(name = "repeat_")
  private Boolean repeat;
  
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
  private MatriculationExamGrade grade;
  
  @NotNull
  @Column(nullable = false)
  @Field
  private Boolean archived = Boolean.FALSE;

  @Version
  @Column(nullable = false)
  private Long version;
}