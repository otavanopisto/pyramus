package fi.otavanopisto.pyramus.koski.model.lukio;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import fi.otavanopisto.pyramus.koski.model.Kuvaus;
import fi.otavanopisto.pyramus.koski.model.Majoitusjakso;

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

  public void setYksityisopiskelija(boolean yksityisopiskelija) {
    this.yksityisopiskelija = yksityisopiskelija;
  }

  public void setOikeusMaksuttomaanAsuntolapaikkaan(boolean oikeusMaksuttomaanAsuntolapaikkaan) {
    this.oikeusMaksuttomaanAsuntolapaikkaan = oikeusMaksuttomaanAsuntolapaikkaan;
  }

  public void setSisaoppilaitosmainenMajoitus(List<Majoitusjakso> sisaoppilaitosmainenMajoitus) {
    this.sisaoppilaitosmainenMajoitus = sisaoppilaitosmainenMajoitus;
  }

  private boolean pidennettyPaattymispaiva;
  private boolean ulkomainenVaihtoopiskelija;
  private Kuvaus alle18vuotiaanAikuistenLukiokoulutuksenAloittamisenSyy;
  private boolean yksityisopiskelija;
  private boolean oikeusMaksuttomaanAsuntolapaikkaan;
  private List<Majoitusjakso> sisaoppilaitosmainenMajoitus = new ArrayList<>();
}