package fi.otavanopisto.pyramus.rest.model.course;

public class CourseSignupStudyProgramme {

  public CourseSignupStudyProgramme() {
    super();
  }

  public CourseSignupStudyProgramme(Long id, Long courseId, Long studyProgrammeId, String studyProgrammeName) {
    this();
    this.id = id;
    this.courseId = courseId;
    this.studyProgrammeId = studyProgrammeId;
    this.studyProgrammeName = studyProgrammeName;
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

  public Long getStudyProgrammeId() {
    return studyProgrammeId;
  }

  public void setStudyProgrammeId(Long studyProgrammeId) {
    this.studyProgrammeId = studyProgrammeId;
  }

  public String getStudyProgrammeName() {
    return studyProgrammeName;
  }

  public void setStudyProgrammeName(String studyProgrammeName) {
    this.studyProgrammeName = studyProgrammeName;
  }

  private Long id;
  private Long courseId;
  private Long studyProgrammeId;
  private String studyProgrammeName;
}
