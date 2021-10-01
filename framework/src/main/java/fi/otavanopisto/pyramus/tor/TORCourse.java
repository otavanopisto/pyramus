package fi.otavanopisto.pyramus.tor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

public class TORCourse {

  public TORCourse() {
  }

  public TORCourse(Subject subject, Integer courseNumber, Double courseLength, TORCourseLengthUnit lengthUnit) {
    this.subject = subject;
    this.courseNumber = courseNumber;
    this.courseLength = courseLength;
    this.lengthUnit = lengthUnit;
  }
  
  public Subject getSubject() {
    return subject;
  }

  public void setSubject(Subject subject) {
    this.subject = subject;
  }

  public Integer getCourseNumber() {
    return courseNumber;
  }

  public void setCourseNumber(Integer courseNumber) {
    this.courseNumber = courseNumber;
  }

  public void addCredit(TORCredit credit) {
    this.credits.add(credit);
  }
  
  public List<TORCredit> getCredits() {
    return credits;
  }

  public void setCredits(List<TORCredit> credits) {
    this.credits = credits;
  }

  public boolean isPassed() {
    return credits.stream().anyMatch(credit -> Boolean.TRUE.equals(credit.getPassingGrade()));
  }
  
  public TORCredit getLatestCredit() {
    this.postProcess();
    return CollectionUtils.isNotEmpty(credits) ? credits.get(0) : null;
  }
  
  public Double getCourseLength() {
    return courseLength;
  }

  public void setCourseLength(Double courseLength) {
    this.courseLength = courseLength;
  }

  public TORCourseLengthUnit getLengthUnit() {
    return lengthUnit;
  }

  public void setLengthUnit(TORCourseLengthUnit lengthUnit) {
    this.lengthUnit = lengthUnit;
  }

  protected void postProcess() {
    Collections.sort(credits, Collections.reverseOrder());
  }

  private Integer courseNumber;
  private Subject subject;
  private Double courseLength;
  private TORCourseLengthUnit lengthUnit;
  private List<TORCredit> credits = new ArrayList<>();
}
