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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import fi.otavanopisto.pyramus.domainmodel.projects.Project;

@Entity
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

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }

  @Id
  @GeneratedValue (strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  @Enumerated(EnumType.STRING)
  private MatriculationExamSubject subject;

  @ManyToOne
  private Project project;
  
  @Temporal(TemporalType.TIMESTAMP)
  private Date examDate;
}