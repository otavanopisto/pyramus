package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import fi.otavanopisto.pyramus.koski.model.Majoitusjakso;
import fi.otavanopisto.pyramus.koski.model.lukio.Maksuttomuus;
import fi.otavanopisto.pyramus.koski.model.lukio.OikeuttaMaksuttomuuteenPidennetty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AikuistenPerusopetuksenOpiskeluoikeudenLisatiedot {

  public AikuistenPerusopetuksenOpiskeluoikeudenLisatiedot() {
  }

  public void addSisaoppilaitosmainenMajoitus(Majoitusjakso jakso) {
    sisaoppilaitosmainenMajoitus.add(jakso);
  }
  
  @JsonProperty("sisäoppilaitosmainenMajoitus")
  public List<Majoitusjakso> getSisaoppilaitosmainenMajoitus() {
    return sisaoppilaitosmainenMajoitus;
  }

  public List<Majoitusjakso> getUlkomaanjaksot() {
    return ulkomaanjaksot;
  }

  public Majoitusjakso getMajoitusetu() {
    return majoitusetu;
  }

  public void setMajoitusetu(Majoitusjakso majoitusetu) {
    this.majoitusetu = majoitusetu;
  }

  public void addOikeuttaMaksuttomuuteenPidennetty(OikeuttaMaksuttomuuteenPidennetty oikeuttaMaksuttomuuteenPidennetty) {
    this.oikeuttaMaksuttomuuteenPidennetty.add(oikeuttaMaksuttomuuteenPidennetty);
  }

  public List<OikeuttaMaksuttomuuteenPidennetty> getOikeuttaMaksuttomuuteenPidennetty() {
    return oikeuttaMaksuttomuuteenPidennetty;
  }

  public void setOikeuttaMaksuttomuuteenPidennetty(List<OikeuttaMaksuttomuuteenPidennetty> oikeuttaMaksuttomuuteenPidennetty) {
    this.oikeuttaMaksuttomuuteenPidennetty = oikeuttaMaksuttomuuteenPidennetty;
  }

  public void addMaksuttomuus(Maksuttomuus maksuttomuus) {
    this.maksuttomuus.add(maksuttomuus);
  }

  public List<Maksuttomuus> getMaksuttomuus() {
    return maksuttomuus;
  }

  public void setMaksuttomuus(List<Maksuttomuus> maksuttomuus) {
    this.maksuttomuus = maksuttomuus;
  }

  private final List<Majoitusjakso> ulkomaanjaksot = new ArrayList<>();
  private Majoitusjakso majoitusetu;
  private final List<Majoitusjakso> sisaoppilaitosmainenMajoitus = new ArrayList<>();
  private List<Maksuttomuus> maksuttomuus = new ArrayList<>();
  private List<OikeuttaMaksuttomuuteenPidennetty> oikeuttaMaksuttomuuteenPidennetty = new ArrayList<>();
}