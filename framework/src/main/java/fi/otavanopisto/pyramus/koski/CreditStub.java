package fi.otavanopisto.pyramus.koski;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import fi.otavanopisto.pyramus.domainmodel.base.Subject;

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

  public void addCredit(CreditStubCredit credit) {
    credits.add(credit);

    credits.sort(Comparator.nullsFirst(Comparator.comparing(CreditStubCredit::getDate)));
  }
  
  public List<CreditStubCredit> getCredits() {
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

  private final List<CreditStubCredit> credits = new ArrayList<>();
  private final String courseCode;
  private final String courseName;
  private final Subject subject;
  private Integer courseNumber;
}
