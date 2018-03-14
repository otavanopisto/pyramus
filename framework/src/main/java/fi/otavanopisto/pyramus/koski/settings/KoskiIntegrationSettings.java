package fi.otavanopisto.pyramus.koski.settings;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import fi.otavanopisto.pyramus.koski.KoskiStudyProgrammeHandler;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KoskiIntegrationSettings {

  @JsonIgnore
  public KoskiStudyProgrammeHandlerParams getHandlerParams(KoskiStudyProgrammeHandler handler) {
    return handlerParams.get(handler);
  }
  
  public Map<KoskiStudyProgrammeHandler, KoskiStudyProgrammeHandlerParams> getHandlerParams() {
    return handlerParams;
  }

  public void setHandlerParams(Map<KoskiStudyProgrammeHandler, KoskiStudyProgrammeHandlerParams> handlerParams) {
    this.handlerParams = handlerParams;
  }

  public String getAcademyIdentifier() {
    return academyIdentifier;
  }

  public void setAcademyIdentifier(String academyIdentifier) {
    this.academyIdentifier = academyIdentifier;
  }

  public Set<Long> getFilteredGrades() {
    return filteredGrades;
  }

  public void setFilteredGrades(Set<Long> filteredGrades) {
    this.filteredGrades = filteredGrades;
  }

  private String academyIdentifier;
  private Map<KoskiStudyProgrammeHandler, KoskiStudyProgrammeHandlerParams> handlerParams = new HashMap<>();
  private Set<Long> filteredGrades = new HashSet<>();
}
