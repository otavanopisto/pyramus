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
  
  /**
   * Searches for a TORCurriculumModule with given
   * subjectCode and courseNumber.
   * 
   * @param subjectCode subject's code
   * @param courseNumber course number
   * @return module if found or null otherwise
   */
  public TORCurriculumModule findModule(String subjectCode, int courseNumber) {
    TORCurriculumSubject torSubject = getSubjectByCode(subjectCode);
    return torSubject != null ? torSubject.findModule(courseNumber) : null;
  }
  
  private String name;
  private List<TORCurriculumSubject> subjects;
}
