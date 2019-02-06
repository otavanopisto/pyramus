package fi.otavanopisto.pyramus.views.students.tor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.rest.model.Grade;
import fi.otavanopisto.pyramus.rest.model.Subject;

public class TORSubject extends Subject {

  public TORSubject() {
  }
  
  public TORSubject(Long id, String code, String name, Long educationTypeId, Boolean archived) {
    super(id, code, name, educationTypeId, archived);
  }
  
  public static TORSubject from(Subject subject) {
    return  new TORSubject(subject.getId(), subject.getCode(), subject.getName(), 
        subject.getEducationTypeId(), subject.getArchived());
  }

  public void addCourse(TORCourse course) {
    this.courses.add(course);
  }

  public TORCourse findCourse(Integer courseNumber) {
    return this.courses.stream().filter(course -> Objects.equals(course.getCourseNumber(), courseNumber)).findFirst().orElse(null);
  }
  
  public List<TORCourse> getCourses() {
    return courses;
  }

  public void setCourses(List<TORCourse> courses) {
    this.courses = courses;
  }

  public void sort() {
    Collections.sort(courses, Comparator.comparing(TORCourse::getCourseNumber, Comparator.nullsLast(Integer::compareTo)));
    courses.forEach(course -> course.sort());
  }
  
  public long getPassedCoursesCount() {
    return courses.stream().filter(TORCourse::isPassed).count();
  }
  
  public Double getArithmeticMeanGrade() {
    Double avg = courses.stream()
      .filter(torCourse -> torCourse.getLatestCredit() != null)
      .map(TORCourse::getLatestCredit)
      .filter(torCredit -> torCredit.getNumericGrade() != null)
      .mapToDouble(TORCredit::getNumericGrade)
      .average().orElse(Double.NaN);
    
    return avg != null && !Double.isNaN(avg) ? avg : null;
  }

  public String getComputedMeanGrade() {
    Double arithmeticMeanGrade = getArithmeticMeanGrade();
    if (arithmeticMeanGrade != null) {
      return String.format("%.2f", arithmeticMeanGrade);
    }

    // Credits may be passing but non-numeric so try to return them
    String literalPassedCredit = courses.stream()
      .filter(torCourse -> torCourse.getLatestCredit() != null)
      .map(TORCourse::getLatestCredit)
      .filter(torCredit -> Boolean.TRUE.equals(torCredit.getPassingGrade()))
      .filter(distinctByKey(TORCredit::getGradeId))
      .map(TORCredit::getGradeName)
      .collect(Collectors.joining("/"));
    
    return StringUtils.isNotBlank(literalPassedCredit) ? literalPassedCredit : null;
  }
  
  public Grade getMeanGrade() {
    return meanGrade;
  }
  
  public void setMeanGrade(Grade meanGrade) {
    this.meanGrade = meanGrade;
  }
  
  private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
    Set<Object> seen = ConcurrentHashMap.newKeySet();
    return t -> seen.add(keyExtractor.apply(t));
  }
  
  private Grade meanGrade;
  private List<TORCourse> courses = new ArrayList<>();
}
