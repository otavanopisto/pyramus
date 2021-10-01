package fi.otavanopisto.pyramus.tor;

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

  public long getPassedCoursesCount() {
    return courses.stream().filter(TORCourse::isPassed).count();
  }
  
  public Double getArithmeticMeanGrade() {
    return arithmeticMeanGrade;
  }
  
  public Double getWeightedMeanGrade() {
    return weightedMeanGrade;
  }
  
  public String getComputedMeanGrade() {
    Double meanGrade = getWeightedMeanGrade();
    if (meanGrade != null) {
      return String.format("%.2f", meanGrade);
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
  
  protected void postProcess(TORProblems problems) {
    Collections.sort(courses, Comparator.comparing(TORCourse::getCourseNumber, Comparator.nullsLast(Integer::compareTo)));
    courses.forEach(course -> course.postProcess());
    
    calculateArithmeticMeanGrade();
    calculateWeightedMeanGrade(problems);
  }
  
  private void calculateArithmeticMeanGrade() {
    Double avg = courses.stream()
      .filter(torCourse -> torCourse.getLatestCredit() != null)
      .map(TORCourse::getLatestCredit)
      .filter(torCredit -> torCredit.getNumericGrade() != null)
      .mapToDouble(TORCredit::getNumericGrade)
      .average().orElse(Double.NaN);
    
    this.arithmeticMeanGrade = avg != null && !Double.isNaN(avg) ? avg : null;
  }

  private void calculateWeightedMeanGrade(TORProblems problems) {
    Set<TORCourseLengthUnit> courseLengthUnits = courses.stream()
      .filter(torCourse -> (torCourse.getLatestCredit() != null && torCourse.getLatestCredit().getNumericGrade() != null))
      .map(TORCourse::getLengthUnit)
      .collect(Collectors.toSet());

    /**
     * If there's one length unit, we're good. But there might be 0
     * if all credits have literate (non numeric) grades.
     */
    if (courseLengthUnits.size() > 1) {
      problems.add(new TORProblem(TORProblemType.INCOMPATIBLE_LENGTHUNITS, this.getCode()));
    }
    
    double totalLength = 0d;
    double totalWeightedGrade = 0d;
    
    for (TORCourse course : courses) {
      if (course.getLatestCredit() != null && course.getLatestCredit().getNumericGrade() != null) {
        totalLength += course.getCourseLength();
        totalWeightedGrade += course.getLatestCredit().getNumericGrade() * course.getCourseLength();
      }
    }
    
    this.weightedMeanGrade = totalLength > 0 ? totalWeightedGrade / totalLength : null;
  }
  
  private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
    Set<Object> seen = ConcurrentHashMap.newKeySet();
    return t -> seen.add(keyExtractor.apply(t));
  }
  
  private Double arithmeticMeanGrade;
  private Double weightedMeanGrade;
  private Grade meanGrade;
  private List<TORCourse> courses = new ArrayList<>();
}
