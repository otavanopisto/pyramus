package fi.otavanopisto.pyramus.koski.model.lukio;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import fi.otavanopisto.pyramus.koski.model.Majoitusjakso;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LukionOpiskeluoikeudenLisatiedot {

  public LukionOpiskeluoikeudenLisatiedot() {
  }
  
  public LukionOpiskeluoikeudenLisatiedot(boolean pidennettyPaattymispaiva, boolean ulkomainenVaihtoopiskelija) {
    this.pidennettyPaattymispaiva = pidennettyPaattymispaiva;
    this.ulkomainenVaihtoopiskelija = ulkomainenVaihtoopiskelija;
  }
  
  @JsonProperty("pidennettyPäättymispäivä")
  public boolean isPidennettyPaattymispaiva() {
    return pidennettyPaattymispaiva;
  }
  
  public boolean isUlkomainenVaihtoopiskelija() {
    return ulkomainenVaihtoopiskelija;
  }
  
  public void addSisaoppilaitosmainenMajoitus(Majoitusjakso jakso) {
    sisaoppilaitosmainenMajoitus.add(jakso);
  }
  
  @JsonProperty("sisäoppilaitosmainenMajoitus")
  public List<Majoitusjakso> getSisaoppilaitosmainenMajoitus() {
    return sisaoppilaitosmainenMajoitus;
  }

  public void setPidennettyPaattymispaiva(boolean pidennettyPaattymispaiva) {
    this.pidennettyPaattymispaiva = pidennettyPaattymispaiva;
  }

  public void setUlkomainenVaihtoopiskelija(boolean ulkomainenVaihtoopiskelija) {
    this.ulkomainenVaihtoopiskelija = ulkomainenVaihtoopiskelija;
  }

  public void setSisaoppilaitosmainenMajoitus(List<Majoitusjakso> sisaoppilaitosmainenMajoitus) {
    this.sisaoppilaitosmainenMajoitus = sisaoppilaitosmainenMajoitus;
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

  private boolean pidennettyPaattymispaiva;
  private boolean ulkomainenVaihtoopiskelija;
  private List<Majoitusjakso> sisaoppilaitosmainenMajoitus = new ArrayList<>();
  private List<Maksuttomuus> maksuttomuus = new ArrayList<>();
  private List<OikeuttaMaksuttomuuteenPidennetty> oikeuttaMaksuttomuuteenPidennetty = new ArrayList<>();
}