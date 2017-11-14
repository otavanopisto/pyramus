package fi.otavanopisto.pyramus.koski.model.result;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OppijaReturnVal {

  public List<OpiskeluoikeusReturnVal> getOpiskeluoikeudet() {
    return opiskeluoikeudet;
  }

  public void setOpiskeluoikeudet(List<OpiskeluoikeusReturnVal> opiskeluoikeudet) {
    this.opiskeluoikeudet = opiskeluoikeudet;
  }

  @JsonProperty("henkilö")
  public HenkiloOIDReturnVal getHenkilo() {
    return henkilo;
  }

  public void setHenkilo(HenkiloOIDReturnVal henkilo) {
    this.henkilo = henkilo;
  }

  private HenkiloOIDReturnVal henkilo;
  private List<OpiskeluoikeusReturnVal> opiskeluoikeudet = new ArrayList<>();
}
