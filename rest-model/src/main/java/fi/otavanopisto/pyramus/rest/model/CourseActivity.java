package fi.otavanopisto.pyramus.rest.model;

import java.util.Date;
import java.util.List;

public class CourseActivity {

  public Long getCourseId() {
    return courseId;
  }

  public void setCourseId(Long courseId) {
    this.courseId = courseId;
  }

  public String getCourseName() {
    return courseName;
  }

  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }

  public List<Long> getCurriculumIds() {
    return curriculumIds;
  }

  public void setCurriculumId(List<Long> curriculumIds) {
    this.curriculumIds = curriculumIds;
  }

  public String getGrade() {
    return grade;
  }

  public void setGrade(String grade) {
    this.grade = grade;
  }

  public Boolean getPassingGrade() {
    return passingGrade;
  }

  public void setPassingGrade(Boolean passingGrade) {
    this.passingGrade = passingGrade;
  }

  public Date getActivityDate() {
    return activityDate;
  }

  public void setActivityDate(Date activityDate) {
    this.activityDate = activityDate;
  }

  public CourseActivityState getState() {
    return state;
  }

  public void setState(CourseActivityState state) {
    this.state = state;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  private Long courseId;
  private String courseName;
  private List<Long> curriculumIds;
  private String grade;
  private Boolean passingGrade;
  private String text;
  private Date activityDate;
  private CourseActivityState state;

}
