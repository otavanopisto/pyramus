package fi.otavanopisto.pyramus.views.students.tor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import fi.otavanopisto.pyramus.rest.model.Subject;

public class TORCourse {

  public TORCourse() {
  }

  public TORCourse(Subject subject, Integer courseNumber) {
    this.subject = subject;
    this.courseNumber = courseNumber;
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

  public void sort() {
    Collections.sort(credits, Comparator.comparing(TORCredit::getDate, Comparator.nullsLast(Date::compareTo)).reversed());
  }

  public boolean isPassed() {
    return credits.stream().anyMatch(credit -> Boolean.TRUE.equals(credit.getPassingGrade()));
  }
  
  public TORCredit getLatestCredit() {
    this.sort();
    return CollectionUtils.isNotEmpty(credits) ? credits.get(0) : null;
  }
  
  private Integer courseNumber;
  private Subject subject;
  private List<TORCredit> credits = new ArrayList<>();
}
