package fi.otavanopisto.pyramus.koski;

import java.util.HashSet;
import java.util.Set;

import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.grading.Credit;

public class CreditStub {
  
  public CreditStub(String courseCode, Integer courseNumber, String courseName, Subject subject) {
    this.courseCode = courseCode;
    this.courseNumber = courseNumber;
    this.courseName = courseName;
    this.subject = subject;
  }
  
  public String getCourseCode() {
    return courseCode;
  }

  public String getCourseName() {
    return courseName;
  }

  public void addCredit(Credit credit) {
    credits.add(credit);
  }
  
  public Set<Credit> getCredits() {
    return credits;
  }

  public Subject getSubject() {
    return subject;
  }

  public Integer getCourseNumber() {
    return courseNumber;
  }

  public void setCourseNumber(Integer courseNumber) {
    this.courseNumber = courseNumber;
  }

  private final Set<Credit> credits = new HashSet<>();
  private final String courseCode;
  private final String courseName;
  private final Subject subject;
  private Integer courseNumber;
}
