package fi.otavanopisto.pyramus.koski.model;

import java.util.ArrayList;
import java.util.List;

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
  
  public List<Opiskeluoikeus> getOpiskeluoikeudet() {
    return opiskeluoikeudet;
  }

  private Henkilo henkilo;
  private final List<Opiskeluoikeus> opiskeluoikeudet = new ArrayList<>();
}
