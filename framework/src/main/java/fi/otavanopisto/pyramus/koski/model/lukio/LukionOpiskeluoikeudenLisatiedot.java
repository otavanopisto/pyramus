package fi.otavanopisto.pyramus.koski.model.lukio;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import fi.otavanopisto.pyramus.koski.model.Kuvaus;
import fi.otavanopisto.pyramus.koski.model.Majoitusjakso;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LukionOpiskeluoikeudenLisatiedot {

  public LukionOpiskeluoikeudenLisatiedot() {
  }
  
  public LukionOpiskeluoikeudenLisatiedot(boolean pidennettyPaattymispaiva, boolean ulkomainenVaihtoopiskelija, boolean yksityisopiskelija, boolean oikeusMaksuttomaanAsuntolapaikkaan) {
    this.pidennettyPaattymispaiva = pidennettyPaattymispaiva;
    this.ulkomainenVaihtoopiskelija = ulkomainenVaihtoopiskelija;
    this.yksityisopiskelija = yksityisopiskelija;
    this.oikeusMaksuttomaanAsuntolapaikkaan = oikeusMaksuttomaanAsuntolapaikkaan;
  }
  
  public Kuvaus getAlle18vuotiaanAikuistenLukiokoulutuksenAloittamisenSyy() {
    return alle18vuotiaanAikuistenLukiokoulutuksenAloittamisenSyy;
  }
  
  public void setAlle18vuotiaanAikuistenLukiokoulutuksenAloittamisenSyy(
      Kuvaus alle18vuotiaanAikuistenLukiokoulutuksenAloittamisenSyy) {
    this.alle18vuotiaanAikuistenLukiokoulutuksenAloittamisenSyy = alle18vuotiaanAikuistenLukiokoulutuksenAloittamisenSyy;
  }
  
  @JsonProperty("pidennettyPäättymispäivä")
  public boolean isPidennettyPaattymispaiva() {
    return pidennettyPaattymispaiva;
  }
  
  public boolean isUlkomainenVaihtoopiskelija() {
    return ulkomainenVaihtoopiskelija;
  }
  
  @Deprecated
  public boolean isYksityisopiskelija() {
    return yksityisopiskelija;
  }
  
  public boolean isOikeusMaksuttomaanAsuntolapaikkaan() {
    return oikeusMaksuttomaanAsuntolapaikkaan;
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

  @Deprecated
  public void setYksityisopiskelija(boolean yksityisopiskelija) {
    this.yksityisopiskelija = yksityisopiskelija;
  }

  public void setOikeusMaksuttomaanAsuntolapaikkaan(boolean oikeusMaksuttomaanAsuntolapaikkaan) {
    this.oikeusMaksuttomaanAsuntolapaikkaan = oikeusMaksuttomaanAsuntolapaikkaan;
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
  private Kuvaus alle18vuotiaanAikuistenLukiokoulutuksenAloittamisenSyy;
  @Deprecated private boolean yksityisopiskelija;
  private boolean oikeusMaksuttomaanAsuntolapaikkaan;
  private List<Majoitusjakso> sisaoppilaitosmainenMajoitus = new ArrayList<>();
  private List<Maksuttomuus> maksuttomuus = new ArrayList<>();
  private List<OikeuttaMaksuttomuuteenPidennetty> oikeuttaMaksuttomuuteenPidennetty = new ArrayList<>();
}