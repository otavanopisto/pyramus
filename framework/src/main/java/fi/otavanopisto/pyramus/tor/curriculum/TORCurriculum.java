package fi.otavanopisto.pyramus.tor.curriculum;

import java.util.List;
import java.util.Objects;

/**
 * Model for JSON Curriculums used for StudentTOR
 */
public class TORCurriculum {

  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }

  public List<TORCurriculumSubject> getSubjects() {
    return subjects;
  }

  public void setSubjects(List<TORCurriculumSubject> subjects) {
    this.subjects = subjects;
  }

  public TORCurriculumSubject getSubjectByCode(String code) {
    return subjects != null
      ? subjects.stream().filter(subject -> Objects.equals(subject.getCode(), code)).findFirst().orElse(null)
      : null;
  }
  
  private String name;
  private List<TORCurriculumSubject> subjects;
}
