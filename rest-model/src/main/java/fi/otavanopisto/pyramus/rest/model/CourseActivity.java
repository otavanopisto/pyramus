package fi.otavanopisto.pyramus.rest.model;

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

  public List<CourseActivityCurriculum> getCurriculums() {
    return curriculums;
  }

  public void setCurriculums(List<CourseActivityCurriculum> curriculums) {
    this.curriculums = curriculums;
  }

  public List<CourseActivitySubject> getSubjects() {
    return subjects;
  }

  public void setSubjects(List<CourseActivitySubject> subjects) {
    this.subjects = subjects;
  }

  public List<CourseActivityAssessment> getAssessments() {
    return assessments;
  }

  public void setAssessments(List<CourseActivityAssessment> assessments) {
    this.assessments = assessments;
  }

  private Long courseId;
  private String courseName;
  private List<CourseActivitySubject> subjects;
  private List<CourseActivityCurriculum> curriculums;
  private List<CourseActivityAssessment> assessments;

}
