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

  public Date getGradeDate() {
    return gradeDate;
  }

  public void setGradeDate(Date gradeDate) {
    this.gradeDate = gradeDate;
  }

  public CourseActivitySubject getSubject() {
    return subject;
  }

  public void setSubject(CourseActivitySubject subject) {
    this.subject = subject;
  }

  public List<CourseActivityCurriculum> getCurriculums() {
    return curriculums;
  }

  public void setCurriculums(List<CourseActivityCurriculum> curriculums) {
    this.curriculums = curriculums;
  }

  private Long courseId;
  private CourseActivitySubject subject;
  private String courseName;
  private List<CourseActivityCurriculum> curriculums;
  private String grade;
  private Boolean passingGrade;
  private Date gradeDate;
  private String text;
  private Date activityDate;
  private CourseActivityState state;

}
