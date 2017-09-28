package fi.otavanopisto.pyramus.koski.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Oppija {

  @JsonProperty("henkilö")
  public Henkilo getHenkilo() {
    return henkilo;
  }

  public void setHenkilo(Henkilo henkilo) {
    this.henkilo = henkilo;
  }
  
  public void addOpiskeluoikeus(Opiskeluoikeus opiskeluoikeus) {
    opiskeluoikeudet.add(opiskeluoikeus);
  }
  
  public Set<Opiskeluoikeus> getOpiskeluoikeudet() {
    return opiskeluoikeudet;
  }

  private Henkilo henkilo;
  private final Set<Opiskeluoikeus> opiskeluoikeudet = new HashSet<>();
}
