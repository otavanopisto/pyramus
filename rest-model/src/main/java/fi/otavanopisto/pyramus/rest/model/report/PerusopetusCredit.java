package fi.otavanopisto.pyramus.rest.model.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PerusopetusCredit {

  public String getCourseName() {
    return courseName;
  }

  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }
  
  public String getCourseCode() {
    return courseCode;
  }

  public void setCourseCode(String courseCode) {
    this.courseCode = courseCode;
  }

  public String getGradeName() {
    return gradeName;
  }

  public void setGradeName(String gradeName) {
    this.gradeName = gradeName;
  }

  public String getGradingScaleName() {
    return gradingScaleName;
  }

  public void setGradingScaleName(String gradingScaleName) {
    this.gradingScaleName = gradingScaleName;
  }

  public boolean isGroupCourse() {
    return groupCourse;
  }

  public void setGroupCourse(boolean groupCourse) {
    this.groupCourse = groupCourse;
  }

  public Long getStudentId() {
    return studentId;
  }

  public void setStudentId(Long studentId) {
    this.studentId = studentId;
  }

  public Long getPersonId() {
    return personId;
  }

  public void setPersonId(Long personId) {
    this.personId = personId;
  }

  public String getStudentName() {
    return studentName;
  }

  public void setStudentName(String studentName) {
    this.studentName = studentName;
  }

  public String getStudyProgrammeName() {
    return studyProgrammeName;
  }

  public void setStudyProgrammeName(String studyProgrammeName) {
    this.studyProgrammeName = studyProgrammeName;
  }

  public String getAssessorName() {
    return assessorName;
  }

  public void setAssessorName(String assessorName) {
    this.assessorName = assessorName;
  }

  public String getSchoolName() {
    return schoolName;
  }

  public void setSchoolName(String schoolName) {
    this.schoolName = schoolName;
  }

  public String getSchoolField() {
    return schoolField;
  }

  public void setSchoolField(String schoolField) {
    this.schoolField = schoolField;
  }

  public Date getGradeDate() {
    return gradeDate;
  }

  public void setGradeDate(Date gradeDate) {
    this.gradeDate = gradeDate;
  }

  public boolean isMismatchingCurriculum() {
    return mismatchingCurriculum;
  }

  public void setMismatchingCurriculum(boolean mismatchingCurriculum) {
    this.mismatchingCurriculum = mismatchingCurriculum;
  }

  public boolean isOtherFunding() {
    return otherFunding;
  }

  public void setOtherFunding(boolean otherFunding) {
    this.otherFunding = otherFunding;
  }

  public boolean isEvaluatedOutsideStudies() {
    return evaluatedOutsideStudies;
  }

  public void setEvaluatedOutsideStudies(boolean evaluatedOutsideStudies) {
    this.evaluatedOutsideStudies = evaluatedOutsideStudies;
  }

  public boolean isKoskiFailure() {
    return koskiFailure;
  }

  public void setKoskiFailure(boolean koskiFailure) {
    this.koskiFailure = koskiFailure;
  }

  public PerusopetusCreditState getState() {
    return state;
  }

  public void setState(PerusopetusCreditState state) {
    this.state = state;
  }

  public void addPreviousEvaluation(PerusopetusCredit previousEvaluation) {
    previousEvaluations.add(previousEvaluation);
  }
  
  public List<PerusopetusCredit> getPreviousEvaluations() {
    return previousEvaluations;
  }

  public void setPreviousEvaluations(List<PerusopetusCredit> previousEvaluations) {
    this.previousEvaluations = previousEvaluations;
  }

  public Double getCourseLength() {
    return courseLength;
  }

  public void setCourseLength(Double courseLength) {
    this.courseLength = courseLength;
  }

  public String getCourseLengthSymbol() {
    return courseLengthSymbol;
  }

  public void setCourseLengthSymbol(String courseLengthSymbol) {
    this.courseLengthSymbol = courseLengthSymbol;
  }

  public Long getCourseId() {
    return courseId;
  }

  public void setCourseId(Long courseId) {
    this.courseId = courseId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  private Long courseId;
  private String courseName;
  private String courseCode;
  private Double courseLength;
  private String courseLengthSymbol;
  private String gradeName;
  private Date gradeDate;
  private String gradingScaleName;
  private boolean groupCourse;
  private Long studentId;
  private Long personId;
  private String studentName;
  private String studyProgrammeName;
  private String assessorName;
  private String schoolName;
  private String schoolField;
  private String type;
  
  private PerusopetusCreditState state;
  private boolean mismatchingCurriculum;
  private boolean otherFunding;
  private boolean evaluatedOutsideStudies;
  private boolean koskiFailure;
  
  private List<PerusopetusCredit> previousEvaluations = new ArrayList<>();
}
