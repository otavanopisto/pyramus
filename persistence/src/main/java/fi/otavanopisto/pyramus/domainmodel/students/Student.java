package fi.otavanopisto.pyramus.domainmodel.students;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;

import fi.otavanopisto.pyramus.domainmodel.base.ArchivableEntity;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.Language;
import fi.otavanopisto.pyramus.domainmodel.base.Municipality;
import fi.otavanopisto.pyramus.domainmodel.base.Nationality;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.School;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Entity
@Indexed
@PrimaryKeyJoinColumn(name="id")
public class Student extends User implements ArchivableEntity {

  /**
   * Returns full name (first name + last name) of this student.
   *  
   * @return Full name
   */
  @Transient 
  @Field (store = Store.NO)
  public String getFullName() {
    return getFirstName() + ' ' + getLastName();
  }
  
  /**
   * Sets additional info for this student.
   * 
   * @param additionalInfo Additional info
   */
  public void setAdditionalInfo(String additionalInfo) {
    this.additionalInfo = additionalInfo;
  }

  /**
   * Returns additional info of this student.
   * 
   * @return Additional info
   */
  public String getAdditionalInfo() {
    return additionalInfo;
  }

  /**
   * Sets the municipality of this student.
   * 
   * @param municipality Municipality
   */
  public void setMunicipality(Municipality municipality) {
    this.municipality = municipality;
  }

  /**
   * Returns the municipality of this student.
   * 
   * @return Municipality
   */
  public Municipality getMunicipality() {
    return municipality;
  }

  /**
   * Sets nationality for this student.
   * 
   * @param nationality Nationality
   */
  public void setNationality(Nationality nationality) {
    this.nationality = nationality;
  }

  /**
   * Returns the nationality of this student.
   * 
   * @return The nationality of this student
   */
  public Nationality getNationality() {
    return nationality;
  }

  /**
   * Sets language for this student.
   * 
   * @param language Language
   */
  public void setLanguage(Language language) {
    this.language = language;
  }

  /**
   * Returns language of this student.
   * 
   * @return Language
   */
  public Language getLanguage() {
    return language;
  }
  
  /**
   * Returns an ending date of students right to study
   * 
   * @return ending date of students right to study
   */
  public Date getStudyTimeEnd() {
    return studyTimeEnd;
  }
  
  /**
   * Sets an ending date of students right to study
   * 
   * @param studyTimeEnd
   */
  public void setStudyTimeEnd(Date studyTimeEnd) {
    this.studyTimeEnd = studyTimeEnd;
  }
  
  public void setSchool(School school) {
    this.school = school;
  }

  public School getSchool() {
    return school;
  }
  
  public StudyProgramme getStudyProgramme() {
    return studyProgramme;
  }
  
  public void setStudyProgramme(StudyProgramme studyProgramme) {
    this.studyProgramme = studyProgramme;
  }

  public void setStudyEndDate(Date studyEndDate) {
	  this.studyEndDate = studyEndDate;
  }

	public Date getStudyEndDate() {
	  return studyEndDate;
  }

	public void setStudyStartDate(Date studyStartDate) {
	  this.studyStartDate = studyStartDate;
  }

	public Date getStudyStartDate() {
	  return studyStartDate;
  }

	public void setPreviousStudies(Double previousStudies) {
	  this.previousStudies = previousStudies;
  }

	public Double getPreviousStudies() {
	  return previousStudies;
  }

	public void setStudyEndReason(StudentStudyEndReason studyEndReason) {
	  this.studyEndReason = studyEndReason;
  }

	public StudentStudyEndReason getStudyEndReason() {
	  return studyEndReason;
  }

	public void setStudyEndText(String studyEndText) {
	  this.studyEndText = studyEndText;
  }

	public String getStudyEndText() {
	  return studyEndText;
  }
	
	@Transient
	public boolean getActive() {
	  return getHasStartedStudies() && !getHasFinishedStudies();
	}

  @Transient
  public boolean getHasStartedStudies() {
    return studyStartDate != null && studyStartDate.before(new Date());
  }

  @Transient
  public boolean getHasFinishedStudies() {
    return studyEndDate != null && studyEndDate.before(new Date());
  }

	public void setActivityType(StudentActivityType activityType) {
    this.activityType = activityType;
  }

  public StudentActivityType getActivityType() {
    return activityType;
  }

  public void setEducationalLevel(StudentEducationalLevel educationalLevel) {
    this.educationalLevel = educationalLevel;
  }

  public StudentEducationalLevel getEducationalLevel() {
    return educationalLevel;
  }

  public void setExaminationType(StudentExaminationType examinationType) {
    this.examinationType = examinationType;
  }

  public StudentExaminationType getExaminationType() {
    return examinationType;
  }

  public String getEducation() {
    return education;
  }
  
  public void setEducation(String education) {
    this.education = education;
  }
  
  public String getNickname() {
    return nickname;
  }
  
  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  @Transient
  @Override
  public Role getRole() {
    return Role.STUDENT;
  }
  
  @Transient
  @Override
  public Organization getOrganization() {
    StudyProgramme studyProgramme2 = getStudyProgramme();
    
    return studyProgramme2 != null ? studyProgramme2.getOrganization() : null;
  }

  public Curriculum getCurriculum() {
    return curriculum;
  }

  public void setCurriculum(Curriculum curriculum) {
    this.curriculum = curriculum;
  }

  private String nickname;
    
  @Lob
  @Basic (fetch = FetchType.LAZY)
  @Column
  private String additionalInfo;
  
  @ManyToOne
  @JoinColumn (name = "nationality")
  private Nationality nationality;

  @ManyToOne
  @JoinColumn (name = "language")
  private Language language;

  @ManyToOne
  @JoinColumn (name = "municipality")
  private Municipality municipality;

  @ManyToOne
  @JoinColumn (name = "school")
  @IndexedEmbedded (depth = 1)
  private School school;
  
  @ManyToOne
  @JoinColumn (name = "activityType")
  private StudentActivityType activityType;

  @ManyToOne
  @JoinColumn (name = "examinationType")
  private StudentExaminationType examinationType;

  @ManyToOne
  @JoinColumn (name = "educationalLevel")
  private StudentEducationalLevel educationalLevel;

  @Temporal (value=TemporalType.DATE)
  private Date studyTimeEnd;
  
  @ManyToOne  
  @JoinColumn(name="studyProgramme")
  private StudyProgramme studyProgramme;

  private Double previousStudies; 
  
  private String education;
  
  @Temporal (value=TemporalType.DATE)
  private Date studyStartDate;
  
  @Temporal (value=TemporalType.DATE)
  @Field (analyze = Analyze.NO)
  @DateBridge (resolution = Resolution.DAY)
  private Date studyEndDate;
  
  @ManyToOne
  @JoinColumn (name = "studyEndReason")
  @IndexedEmbedded
  private StudentStudyEndReason studyEndReason;
  
  @Basic (fetch = FetchType.LAZY)
  private String studyEndText;

  @ManyToOne
  private Curriculum curriculum;
}