package fi.otavanopisto.pyramus.tor.curriculum;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TORCurriculumSubject {

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public List<TORCurriculumModule> getModules() {
    return modules;
  }

  public void setModules(List<TORCurriculumModule> modules) {
    this.modules = modules;
  }

  public List<String> getIncludedSubjects() {
    return includedSubjects;
  }

  public void setIncludedSubjects(List<String> includedSubjects) {
    this.includedSubjects = includedSubjects;
  }

  /**
   * Returns sum of module lengths for modules that are marked as mandatory.
   * 
   * @return sum of module lengths for modules that are marked as mandatory
   */
  public int getMandatoryModuleLengthSum() {
    if (this.modules == null) {
      return 0;
    }
    
    // TODO Curriculum has no indication what unit type the length is and also it is in integer format
    //      while it probably should be a decimal number, as that's what the course credit points are.
    return this.modules.stream()
        .filter(TORCurriculumModule::isMandatory)
        .mapToInt(TORCurriculumModule::getLength)
        .sum();
  }

  /**
   * Returns sum of module lengths for modules that are marked as mandatory.
   * Includes the lengths of mandatory modules from included subjects.
   * 
   * @return
   */
  public int getMandatoryModuleLengthSumWithIncludedModules(TORCurriculum torCurriculum) {
    int sum = getMandatoryModuleLengthSum();
    
    if (CollectionUtils.isNotEmpty(this.includedSubjects)) {
      for (String includedSubjectCode : this.includedSubjects) { 
        TORCurriculumSubject torCurriculumSubject = torCurriculum.getSubjectByCode(includedSubjectCode);
        if (torCurriculumSubject != null) {
          sum += torCurriculumSubject.getMandatoryModuleLengthSum();
        }
      }
    }
    
    return sum;
  }
  
  public Double getMatriculationRequiredStudies() {
    return matriculationRequiredStudies;
  }

  public void setMatriculationRequiredStudies(Double matriculationRequiredStudies) {
    this.matriculationRequiredStudies = matriculationRequiredStudies;
  }

  private String name;
  private String code;
  private Double matriculationRequiredStudies;
  private List<TORCurriculumModule> modules;
  @JsonProperty(value = "included-subjects", required = false)
  private List<String> includedSubjects;
}
