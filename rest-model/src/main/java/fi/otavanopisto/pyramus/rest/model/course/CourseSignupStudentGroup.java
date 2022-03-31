package fi.otavanopisto.pyramus.rest.model.course;

import fi.otavanopisto.pyramus.rest.model.OrganizationBasicInfo;

public class CourseSignupStudentGroup {

  public CourseSignupStudentGroup() {
    super();
  }

  public CourseSignupStudentGroup(Long id, Long courseId, Long studentGroupId, String studentGroupName, OrganizationBasicInfo organization) {
    this();
    this.id = id;
    this.courseId = courseId;
    this.studentGroupId = studentGroupId;
    this.studentGroupName = studentGroupName;
    this.organization = organization;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCourseId() {
    return courseId;
  }

  public void setCourseId(Long courseId) {
    this.courseId = courseId;
  }

  public Long getStudentGroupId() {
    return studentGroupId;
  }

  public void setStudentGroupId(Long studentGroupId) {
    this.studentGroupId = studentGroupId;
  }

  public String getStudentGroupName() {
    return studentGroupName;
  }

  public void setStudentGroupName(String studentGroupName) {
    this.studentGroupName = studentGroupName;
  }

  public OrganizationBasicInfo getOrganization() {
    return organization;
  }

  public void setOrganization(OrganizationBasicInfo organization) {
    this.organization = organization;
  }

  private Long id;
  private Long courseId;
  private Long studentGroupId;
  private String studentGroupName;
  private OrganizationBasicInfo organization;
}
