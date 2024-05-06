package fi.otavanopisto.pyramus.tor.curriculum;

import java.util.List;

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

  private String name;
  private String code;
  private List<TORCurriculumModule> modules;
}
