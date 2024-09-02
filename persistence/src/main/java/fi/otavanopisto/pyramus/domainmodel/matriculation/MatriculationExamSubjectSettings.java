package fi.otavanopisto.pyramus.domainmodel.matriculation;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import fi.otavanopisto.pyramus.matriculation.MatriculationExamSubject;

@Entity
@Table(uniqueConstraints={
    @UniqueConstraint(columnNames = {"exam_id", "subject"})
})
public class MatriculationExamSubjectSettings {

  public Long getId() {
    return id;
  }
  
  public MatriculationExamSubject getSubject() {
    return subject;
  }

  public void setSubject(MatriculationExamSubject subject) {
    this.subject = subject;
  }

  public Date getExamDate() {
    return examDate;
  }

  public void setExamDate(Date examDate) {
    this.examDate = examDate;
  }

  public MatriculationExam getExam() {
    return exam;
  }

  public void setExam(MatriculationExam exam) {
    this.exam = exam;
  }

  @Id
  @GeneratedValue (strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private MatriculationExam exam;
  
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private MatriculationExamSubject subject;

  @Temporal(TemporalType.TIMESTAMP)
  private Date examDate;
}