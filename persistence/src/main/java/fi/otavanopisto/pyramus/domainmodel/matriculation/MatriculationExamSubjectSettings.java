package fi.otavanopisto.pyramus.domainmodel.matriculation;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;

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