package fi.otavanopisto.pyramus.rest.model.matriculation;

import java.time.LocalDate;
import java.time.LocalDateTime;

import fi.otavanopisto.pyramus.matriculation.MatriculationExamGrade;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamSubject;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamTerm;

public class MatriculationGrade {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
  public Long getPersonId() {
    return personId;
  }
  
  public void setPersonId(Long personId) {
    this.personId = personId;
  }
  
  public MatriculationExamSubject getSubject() {
    return subject;
  }
  
  public void setSubject(MatriculationExamSubject subject) {
    this.subject = subject;
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
  
  public MatriculationExamGrade getGrade() {
    return grade;
  }
  
  public void setGrade(MatriculationExamGrade grade) {
    this.grade = grade;
  }
  
  public LocalDate getGradeDate() {
    return gradeDate;
  }
  
  public void setGradeDate(LocalDate gradeDate) {
    this.gradeDate = gradeDate;
  }
  
  public Long getModifierId() {
    return modifierId;
  }
  
  public void setModifierId(Long modifierId) {
    this.modifierId = modifierId;
  }
  
  public LocalDateTime getLastModified() {
    return lastModified;
  }
  
  public void setLastModified(LocalDateTime lastModified) {
    this.lastModified = lastModified;
  }
  
  private Long id;
  private Long personId;
  private MatriculationExamSubject subject;
  private Integer year;
  private MatriculationExamTerm term;
  private MatriculationExamGrade grade;
  private LocalDate gradeDate;
  private Long modifierId;
  private LocalDateTime lastModified;
}
