package fi.otavanopisto.pyramus.views.students.tor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

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

  protected void postProcess() {
    Collections.sort(subjects, Comparator.comparing(TORSubject::getName));
    subjects.forEach(subject -> subject.postProcess(problems));
  }
  
  private final TORProblems problems = new TORProblems();
  private List<TORSubject> subjects = new ArrayList<>();
}
