package fi.otavanopisto.pyramus.hops;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// curriculum_2018.json or curriculum_2021.json without name and lengthUnitSymbol
@JsonIgnoreProperties(ignoreUnknown = true)
public class HopsCourseMatrix {

  public HopsCourseMatrix() {
    subjects = new ArrayList<>();
    problems = new HashSet<>();
  }
  
  public List<HopsCourseMatrixSubject> getSubjects() {
    return subjects;
  }

  public void setSubjects(List<HopsCourseMatrixSubject> subjects) {
    this.subjects = subjects;
  }

  public Set<HopsCourseMatrixProblem> getProblems() {
    return problems;
  }

  public void setProblems(Set<HopsCourseMatrixProblem> problems) {
    this.problems = problems;
  }
  
  @JsonIgnore
  public void addProblem(HopsCourseMatrixProblem problem) {
    problems.add(problem);
  }
  
  @JsonIgnore
  public Set<String> listSubjectCodes() {
    return subjects.stream().map(s -> s.getCode()).collect(Collectors.toSet());
  }
  
  @JsonIgnore
  public Set<Integer> listCourseNumbers(String subjectCode) {
    HopsCourseMatrixSubject subject = subjects.stream().filter(s -> StringUtils.equals(subjectCode, s.getCode())).findFirst().orElse(null);
    return subject == null ? Collections.emptySet() : subject.getModules().stream().map(m -> m.getCourseNumber()).collect(Collectors.toSet());
  }

  @JsonIgnore
  public void removeSubject(String subjectCode) {
    HopsCourseMatrixSubject subject = subjects.stream().filter(s -> StringUtils.equals(subjectCode, s.getCode())).findFirst().orElse(null);
    if (subject != null) {
      subjects.remove(subject);
    }
  }

  public HopsCourseMatrixType getType() {
    return type;
  }

  public void setType(HopsCourseMatrixType type) {
    this.type = type;
  }

  private HopsCourseMatrixType type;
  private List<HopsCourseMatrixSubject> subjects;
  private Set<HopsCourseMatrixProblem> problems;

}
