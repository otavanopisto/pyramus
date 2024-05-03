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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.tor.curriculum.TORCurriculum;
import fi.otavanopisto.pyramus.tor.curriculum.TORCurriculumModule;
import fi.otavanopisto.pyramus.tor.curriculum.TORCurriculumSubject;

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
  
  public Boolean getCompleted() {
    return completed;
  }

  public Integer getMandatoryCourseCompletedCount() {
    return mandatoryCourseCompletedCount;
  }

  public Integer getMandatoryCourseCount() {
    return mandatoryCourseCount;
  }

  protected void postProcess(TORCurriculum curriculum, TORProblems problems) {
    Collections.sort(courses, Comparator.comparing(TORCourse::getCourseNumber, Comparator.nullsLast(Integer::compareTo)));
    courses.forEach(course -> course.postProcess());
    
    calculateArithmeticMeanGrade();
    calculateWeightedMeanGrade(problems);
    processCurriculum(curriculum, problems);
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
  
  /**
   * Figures out if the subject is 'completed', as in if all the
   * courses marked as mandatory in the curriculum are successfully
   * completed.
   * 
   * @param curriculum
   * @param problems
   */
  private void processCurriculum(TORCurriculum curriculum, TORProblems problems) {
    if (curriculum != null && StringUtils.isNotBlank(getCode())) {
      TORCurriculumSubject curriculumSubject = curriculum.getSubjectByCode(getCode());
      if (curriculumSubject != null && CollectionUtils.isNotEmpty(curriculumSubject.getModules())) {
        // Collect the course numbers of mandatory courses from curriculum
        
        Set<Integer> mandatoryCourseNumbers = curriculumSubject.getModules().stream()
          .filter(TORCurriculumModule::isMandatory)
          .map(TORCurriculumModule::getCourseNumber)
          .collect(Collectors.toSet());
        
        // If the curriculum has mandatory courses for the subject we can fill in the extra fields.
        
        if (CollectionUtils.isNotEmpty(mandatoryCourseNumbers)) {
          int numCompleted = 0;
          for (Integer mandatoryCourseNumber : mandatoryCourseNumbers) {
            TORCourse torCourse = findCourse(mandatoryCourseNumber);
            if (torCourse != null && torCourse.isPassed()) {
              numCompleted++;
            }
          }
          
          this.mandatoryCourseCount = mandatoryCourseNumbers.size();
          this.mandatoryCourseCompletedCount = numCompleted;
          this.completed = mandatoryCourseCount == mandatoryCourseCompletedCount;
        }
      }
    }
  }

  private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
    Set<Object> seen = ConcurrentHashMap.newKeySet();
    return t -> seen.add(keyExtractor.apply(t));
  }
  
  private Double arithmeticMeanGrade;
  private Double weightedMeanGrade;
  private Grade meanGrade;
  private List<TORCourse> courses = new ArrayList<>();
  
  private Boolean completed;
  private Integer mandatoryCourseCompletedCount;
  private Integer mandatoryCourseCount;
}
