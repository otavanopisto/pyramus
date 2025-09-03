package fi.otavanopisto.pyramus.domainmodel.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import fi.otavanopisto.pyramus.domainmodel.courses.CourseEnrolmentType;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseParticipationType;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseState;

@Entity
public class Defaults {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setInitialCourseState(CourseState initialCourseState) {
    this.initialCourseState = initialCourseState;
  }

  public CourseState getInitialCourseState() {
    return initialCourseState;
  }

  public void setBaseTimeUnit(EducationalTimeUnit baseTimeUnit) {
    this.baseTimeUnit = baseTimeUnit;
  }

  public EducationalTimeUnit getBaseTimeUnit() {
    return baseTimeUnit;
  }
  
  public CourseParticipationType getInitialCourseParticipationType() {
    return initialCourseParticipationType;
  }
  
  public void setInitialCourseParticipationType(CourseParticipationType initialCourseParticipationType) {
    this.initialCourseParticipationType = initialCourseParticipationType;
  }
  
  public CourseEnrolmentType getInitialCourseEnrolmentType() {
    return initialCourseEnrolmentType;
  }
  
  public void setInitialCourseEnrolmentType(CourseEnrolmentType initialCourseEnrolmentType) {
    this.initialCourseEnrolmentType = initialCourseEnrolmentType;
  }

  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  public Organization getOrganization() {
    return organization;
  }

  public void setOrganization(Organization organization) {
    this.organization = organization;
  }

  @Id
  private Long id;

  @ManyToOne 
  @JoinColumn (name = "educationalTimeUnit")
  private EducationalTimeUnit baseTimeUnit;

  @ManyToOne 
  @JoinColumn (name = "courseState")
  private CourseState initialCourseState;
  
  @ManyToOne 
  @JoinColumn (name = "courseParticipationType")
  private CourseParticipationType initialCourseParticipationType;

  @ManyToOne 
  @JoinColumn (name = "courseEnrolmentType")
  private CourseEnrolmentType initialCourseEnrolmentType;
  
  @ManyToOne
  @JoinColumn (name = "organization")
  private Organization organization;
  
  @Version
  @Column(nullable = false)
  private Long version;
}
