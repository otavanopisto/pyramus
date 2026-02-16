package fi.otavanopisto.pyramus.domainmodel.students;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import fi.otavanopisto.pyramus.domainmodel.grading.SpokenLanguageExamSkillLevel;

@Table(
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student", "skillType"})
    }
)

@Entity
public class StudentLanguageSkillLevel {

  public Long getId() {
    return id;
  }

  public Student getStudent() {
    return student;
  }

  public void setStudent(Student student) {
    this.student = student;
  }

  public LanguageSkillType getSkillType() {
    return skillType;
  }

  public void setSkillType(LanguageSkillType skillType) {
    this.skillType = skillType;
  }

  public SpokenLanguageExamSkillLevel getSkillLevel() {
    return skillLevel;
  }

  public void setSkillLevel(SpokenLanguageExamSkillLevel skillLevel) {
    this.skillLevel = skillLevel;
  }

  public Date getGradingDate() {
    return gradingDate;
  }

  public void setGradingDate(Date gradingDate) {
    this.gradingDate = gradingDate;
  }

  @Id 
  @GeneratedValue (strategy = GenerationType.IDENTITY) 
  private Long id;
  
  @ManyToOne (optional = false)
  @JoinColumn (name = "student")
  private Student student;
  
  @NotNull
  @Column(nullable = false)
  @Enumerated (EnumType.STRING)
  private LanguageSkillType skillType;
  
  @NotNull
  @Column(nullable = false)
  @Enumerated (EnumType.STRING)
  private SpokenLanguageExamSkillLevel skillLevel;
  
  @Temporal(value = TemporalType.DATE)
  private Date gradingDate;
}
