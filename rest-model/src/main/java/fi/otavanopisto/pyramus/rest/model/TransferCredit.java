package fi.otavanopisto.pyramus.rest.model;

import java.time.OffsetDateTime;

public class TransferCredit {
  
  public TransferCredit() {
  }

  public TransferCredit(Long id, Long studentId, OffsetDateTime date, Long gradeId, Long gradingScaleId, String verbalAssessment,
      Long assessorId, Boolean archived, String courseName, Integer courseNumber, Double length, Long lengthUnitId,
      Long schoolId, Long subjectId, CourseOptionality optionality, Long curriculumId, Boolean offCurriculum) {
    super();
    this.id = id;
    this.studentId = studentId;
    this.date = date;
    this.gradeId = gradeId;
    this.gradingScaleId = gradingScaleId;
    this.verbalAssessment = verbalAssessment;
    this.assessorId = assessorId;
    this.archived = archived;
    this.courseName = courseName;
    this.courseNumber = courseNumber;
    this.length = length;
    this.lengthUnitId = lengthUnitId;
    this.schoolId = schoolId;
    this.subjectId = subjectId;
    this.optionality = optionality;
    this.curriculumId = curriculumId;
    this.offCurriculum = offCurriculum;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getStudentId() {
    return studentId;
  }

  public void setStudentId(Long studentId) {
    this.studentId = studentId;
  }

  public OffsetDateTime getDate() {
    return date;
  }

  public void setDate(OffsetDateTime date) {
    this.date = date;
  }

  public Long getGradeId() {
    return gradeId;
  }

  public void setGradeId(Long gradeId) {
    this.gradeId = gradeId;
  }
  
  public Long getGradingScaleId() {
    return gradingScaleId;
  }
  
  public void setGradingScaleId(Long gradingScaleId) {
    this.gradingScaleId = gradingScaleId;
  }

  public String getVerbalAssessment() {
    return verbalAssessment;
  }

  public void setVerbalAssessment(String verbalAssessment) {
    this.verbalAssessment = verbalAssessment;
  }

  public Long getAssessorId() {
    return assessorId;
  }
  
  public void setAssessorId(Long assessorId) {
    this.assessorId = assessorId;
  }
  
  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public String getCourseName() {
    return courseName;
  }

  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }

  public Integer getCourseNumber() {
    return courseNumber;
  }

  public void setCourseNumber(Integer courseNumber) {
    this.courseNumber = courseNumber;
  }

  public Double getLength() {
    return length;
  }

  public void setLength(Double length) {
    this.length = length;
  }

  public Long getLengthUnitId() {
    return lengthUnitId;
  }

  public void setLengthUnitId(Long lengthUnitId) {
    this.lengthUnitId = lengthUnitId;
  }

  public Long getSchoolId() {
    return schoolId;
  }

  public void setSchoolId(Long schoolId) {
    this.schoolId = schoolId;
  }

  public Long getSubjectId() {
    return subjectId;
  }

  public void setSubjectId(Long subjectId) {
    this.subjectId = subjectId;
  }

  public CourseOptionality getOptionality() {
    return optionality;
  }

  public void setOptionality(CourseOptionality optionality) {
    this.optionality = optionality;
  }

  public Long getCurriculumId() {
    return curriculumId;
  }

  public void setCurriculumId(Long curriculumId) {
    this.curriculumId = curriculumId;
  }

  public Boolean getOffCurriculum() {
    return offCurriculum;
  }

  public void setOffCurriculum(Boolean offCurriculum) {
    this.offCurriculum = offCurriculum;
  }

  private Long id;
  private Long studentId;
  private OffsetDateTime date;
  private Long gradeId;
  private Long gradingScaleId;
  private String verbalAssessment;
  private Long assessorId;
  private Boolean archived;
  private String courseName;
  private Integer courseNumber;
  private Double length;
  private Long lengthUnitId;
  private Long schoolId;
  private Long subjectId;
  private CourseOptionality optionality;
  private Long curriculumId;
  private Boolean offCurriculum;
}
