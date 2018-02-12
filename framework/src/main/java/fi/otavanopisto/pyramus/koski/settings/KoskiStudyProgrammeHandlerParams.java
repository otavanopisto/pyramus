package fi.otavanopisto.pyramus.koski.settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fi.otavanopisto.pyramus.koski.OpiskelijanOPS;

public class KoskiStudyProgrammeHandlerParams {

  public List<Long> getEducationTypes() {
    return educationTypes;
  }

  public void setEducationTypes(List<Long> educationTypes) {
    this.educationTypes = educationTypes;
  }

  public List<Long> getExcludedEducationTypes() {
    return excludedEducationTypes;
  }

  public void setExcludedEducationTypes(List<Long> excludedEducationTypes) {
    this.excludedEducationTypes = excludedEducationTypes;
  }

  public Map<OpiskelijanOPS, String> getDiaarinumerot() {
    return diaarinumerot;
  }

  public void setDiaarinumerot(Map<OpiskelijanOPS, String> diaarinumerot) {
    this.diaarinumerot = diaarinumerot;
  }

  @JsonIgnore
  public String getDiaariNumero(OpiskelijanOPS ops) {
    return diaarinumerot.get(ops);
  }
  
  private List<Long> educationTypes = new ArrayList<>();
  private List<Long> excludedEducationTypes = new ArrayList<>();
  private Map<OpiskelijanOPS, String> diaarinumerot = new HashMap<>();
}
