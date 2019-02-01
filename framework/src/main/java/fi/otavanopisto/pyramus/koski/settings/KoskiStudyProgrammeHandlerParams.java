package fi.otavanopisto.pyramus.koski.settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import fi.otavanopisto.pyramus.koski.OpiskelijanOPS;

@JsonIgnoreProperties(ignoreUnknown = true)
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
  
  public String getToimipisteOID() {
    return toimipisteOID;
  }

  public void setToimipisteOID(String toimipisteOID) {
    this.toimipisteOID = toimipisteOID;
  }

  private List<Long> educationTypes = new ArrayList<>();
  private List<Long> excludedEducationTypes = new ArrayList<>();
  private Map<OpiskelijanOPS, String> diaarinumerot = new HashMap<>();
  private String toimipisteOID;
}
