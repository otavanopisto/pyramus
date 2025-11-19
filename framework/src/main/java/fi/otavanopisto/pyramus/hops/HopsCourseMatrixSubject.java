package fi.otavanopisto.pyramus.hops;

import java.util.List;

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
  
  @JsonIgnore
  public void addModule(HopsCourseMatrixModule module) {
    modules.add(module);
  }

  private String name;
  private String code;
  private List<HopsCourseMatrixModule> modules;

}
