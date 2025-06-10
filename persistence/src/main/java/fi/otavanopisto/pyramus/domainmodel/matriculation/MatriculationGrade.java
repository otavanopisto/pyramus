package fi.otavanopisto.pyramus.domainmodel.matriculation;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamGrade;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamSubject;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamTerm;

@Entity
@Table(uniqueConstraints = {
    // One grade for a person from a subject at a single year & term time
    @UniqueConstraint(columnNames = {"person_id", "subject", "year", "term"})
})
public class MatriculationGrade {

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

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  public StaffMember getModifier() {
    return modifier;
  }

  public void setModifier(StaffMember modifier) {
    this.modifier = modifier;
  }

  public LocalDateTime getLastModified() {
    return lastModified;
  }

  public void setLastModified(LocalDateTime lastModified) {
    this.lastModified = lastModified;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)  
  private Long id;
  
  @ManyToOne
  private Person person;
  
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private MatriculationExamSubject subject;
  
  @Column(nullable = false)
  private Integer year;
  
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private MatriculationExamTerm term;
  
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private MatriculationExamGrade grade;
  
  @Column(nullable = false)
  private LocalDate gradeDate;

  @ManyToOne
  private StaffMember modifier;

  @Column(nullable = false)
  private LocalDateTime lastModified;
}