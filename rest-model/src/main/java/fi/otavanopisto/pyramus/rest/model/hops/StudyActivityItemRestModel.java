package fi.otavanopisto.pyramus.rest.model.hops;

import java.util.Date;
import java.util.List;

public class StudyActivityItemRestModel {

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public Integer getCourseNumber() {
    return courseNumber;
  }

  public void setCourseNumber(Integer courseNumber) {
    this.courseNumber = courseNumber;
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

  public StudyActivityItemState getState() {
    return state;
  }

  public void setState(StudyActivityItemState state) {
    this.state = state;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Long getCourseId() {
    return courseId;
  }

  public void setCourseId(Long courseId) {
    this.courseId = courseId;
  }

  public String getSubjectName() {
    return subjectName;
  }

  public void setSubjectName(String subjectName) {
    this.subjectName = subjectName;
  }

  public boolean isPassing() {
    return passing;
  }

  public void setPassing(boolean passing) {
    this.passing = passing;
  }

  public Date getGradeDate() {
    return gradeDate;
  }

  public void setGradeDate(Date gradeDate) {
    this.gradeDate = gradeDate;
  }

  public Integer getLength() {
    return length;
  }

  public void setLength(Integer length) {
    this.length = length;
  }

  public String getLengthSymbol() {
    return lengthSymbol;
  }

  public void setLengthSymbol(String lengthSymbol) {
    this.lengthSymbol = lengthSymbol;
  }

  public Mandatority getMandatority() {
    return mandatority;
  }

  public void setMandatority(Mandatority mandatority) {
    this.mandatority = mandatority;
  }

  public List<String> getCurriculums() {
    return curriculums;
  }

  public void setCurriculums(List<String> curriculums) {
    this.curriculums = curriculums;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getEvaluatorName() {
    return evaluatorName;
  }

  public void setEvaluatorName(String evaluatorName) {
    this.evaluatorName = evaluatorName;
  }

  private String subject;
  private String subjectName;
  private Long courseId;
  private Integer courseNumber;
  private String courseName;
  private String grade;
  private boolean passing;
  private Integer length;
  private String lengthSymbol;
  private Mandatority mandatority;
  private StudyActivityItemState state;
  private Date date;
  private Date gradeDate;
  private List<String> curriculums;
  private String text;
  private String evaluatorName;

}
