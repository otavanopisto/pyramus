package fi.otavanopisto.pyramus.domainmodel.grading;

import java.time.LocalDateTime;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;

@Entity
public class SpokenLanguageExam {

  public Long getId() {
    return id;
  }
  
  public Credit getCredit() {
    return credit;
  }

  public void setCredit(Credit credit) {
    this.credit = credit;
  }

  public Grade getGrade() {
    return grade;
  }

  public void setGrade(Grade grade) {
    this.grade = grade;
  }

  public SpokenLanguageExamSkillLevel getSkillLevel() {
    return skillLevel;
  }

  public void setSkillLevel(SpokenLanguageExamSkillLevel skillLevel) {
    this.skillLevel = skillLevel;
  }

  public String getVerbalAssessment() {
    return verbalAssessment;
  }

  public void setVerbalAssessment(String verbalAssessment) {
    this.verbalAssessment = verbalAssessment;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public StaffMember getAssessor() {
    return assessor;
  }

  public void setAssessor(StaffMember assessor) {
    this.assessor = assessor;
  }

  @Id 
  @GeneratedValue(strategy = GenerationType.IDENTITY)  
  private Long id;

  @OneToOne
  @JoinColumn(name = "credit")
  private Credit credit;

  @ManyToOne
  @JoinColumn(name = "grade")
  private Grade grade;
  
  @Column (nullable = false)
  @Enumerated (EnumType.STRING)
  private SpokenLanguageExamSkillLevel skillLevel;

  @Lob
  @Basic (fetch = FetchType.LAZY)
  private String verbalAssessment;
  
  @Column(nullable = false)
  private LocalDateTime timestamp;
  
  @ManyToOne  
  private StaffMember assessor;
}