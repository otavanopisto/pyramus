package fi.otavanopisto.pyramus.hops;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HopsCourseMatrixSubject {

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

  public List<HopsCourseMatrixModule> getModules() {
    return modules;
  }

  public void setModules(List<HopsCourseMatrixModule> modules) {
    this.modules = modules;
  }
  
  public boolean isHiddenFromHops() {
    return hiddenFromHops;
  }

  public void setHiddenFromHops(boolean hiddenFromHops) {
    this.hiddenFromHops = hiddenFromHops;
  }

  public boolean isChoiceSubject() {
    return choiceSubject;
  }

  public void setChoiceSubject(boolean choiceSubject) {
    this.choiceSubject = choiceSubject;
  }
  
  @JsonIgnore
  public Set<Integer> listHiddenCourseNumbers() {
    return modules.stream().filter(m -> m.isHiddenFromHops()).map(m -> m.getCourseNumber()).collect(Collectors.toSet());
  }

  @JsonIgnore
  public void addModule(HopsCourseMatrixModule module) {
    modules.add(module);
  }
  
  @JsonIgnore
  public void removeModuleByCourseNumber(int courseNumber) {
    modules.removeIf(m -> m.getCourseNumber() == courseNumber);
  }

  private String name;
  private String code;
  private List<HopsCourseMatrixModule> modules;
  private boolean choiceSubject;
  private boolean hiddenFromHops;

}
