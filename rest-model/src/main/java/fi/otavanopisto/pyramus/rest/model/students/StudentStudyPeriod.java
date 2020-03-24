package fi.otavanopisto.pyramus.rest.model.students;

import java.time.LocalDate;

public class StudentStudyPeriod {

  public StudentStudyPeriod() {
  }

  public StudentStudyPeriod(Long id, Long studentId, StudentStudyPeriodType type, LocalDate begin, LocalDate end) {
    this.id = id;
    this.studentId = studentId;
    this.type = type;
    this.begin = begin;
    this.end = end;
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

  public StudentStudyPeriodType getType() {
    return type;
  }

  public void setType(StudentStudyPeriodType type) {
    this.type = type;
  }

  public LocalDate getBegin() {
    return begin;
  }

  public void setBegin(LocalDate begin) {
    this.begin = begin;
  }

  public LocalDate getEnd() {
    return end;
  }

  public void setEnd(LocalDate end) {
    this.end = end;
  }

  private Long id;
  private Long studentId;
  private StudentStudyPeriodType type;
  private LocalDate begin;
  private LocalDate end;
}