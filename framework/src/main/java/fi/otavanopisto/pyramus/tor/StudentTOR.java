package fi.otavanopisto.pyramus.tor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.tor.curriculum.TORCurriculum;

public class StudentTOR {

  public void addSubject(TORSubject subject) {
    this.subjects.add(subject);
  }

  public TORSubject findSubject(Long subjectId) {
    return subjects.stream().filter(subject -> Objects.equals(subject.getId(), subjectId)).findFirst().orElse(null);
  }
  
  public TORSubject findSubject(String subjectCode) {
    return subjects.stream().filter(subject -> StringUtils.equals(subject.getCode(), subjectCode)).findFirst().orElse(null);
  }
  
  public List<TORSubject> getSubjects() {
    return subjects;
  }

  public void setSubjects(List<TORSubject> subjects) {
    this.subjects = subjects;
  }
  
  public TORProblems getProblems() {
    return problems;
  }

  public double getTotalCourseLengths(TORCourseLengthUnit lengthUnit, Boolean mandatory) {
    Stream<TORCourse> stream = subjects.stream()
      .flatMap(torSubject -> torSubject.getCourses().stream())
      .filter(torCourse -> torCourse.isPassed())
      .filter(torCourse -> torCourse.getLengthUnit() == lengthUnit);

    if (mandatory != null) {
      stream = stream
        .filter(torCourse -> torCourse.isMandatory() == mandatory);
    }
    
    return stream
      .mapToDouble(torCourse -> torCourse.getCourseLength())
      .filter(Objects::nonNull)
      .sum();
  }
  
  protected void postProcess(TORCurriculum curriculum) {
    Collections.sort(subjects, Comparator.comparing(TORSubject::getName));
    subjects.forEach(subject -> subject.postProcess(curriculum, problems));
  }
  
  private final TORProblems problems = new TORProblems();
  private List<TORSubject> subjects = new ArrayList<>();
}
