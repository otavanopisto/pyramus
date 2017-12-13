package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import fi.otavanopisto.pyramus.koski.model.Majoitusjakso;

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

  public List<Majoitusjakso> getUlkomailla() {
    return ulkomailla;
  }

  public Boolean getVuosiluokkiinSitoutumatonOpetus() {
    return vuosiluokkiinSitoutumatonOpetus;
  }

  public void setVuosiluokkiinSitoutumatonOpetus(Boolean vuosiluokkiinSitoutumatonOpetus) {
    this.vuosiluokkiinSitoutumatonOpetus = vuosiluokkiinSitoutumatonOpetus;
  }

  public Boolean getVammainen() {
    return vammainen;
  }

  public void setVammainen(Boolean vammainen) {
    this.vammainen = vammainen;
  }

  public Boolean getVaikeastiVammainen() {
    return vaikeastiVammainen;
  }

  public void setVaikeastiVammainen(Boolean vaikeastiVammainen) {
    this.vaikeastiVammainen = vaikeastiVammainen;
  }

  public Majoitusjakso getMajoitusetu() {
    return majoitusetu;
  }

  public void setMajoitusetu(Majoitusjakso majoitusetu) {
    this.majoitusetu = majoitusetu;
  }

  public Majoitusjakso getOikeusMaksuttomaanAsuntolapaikkaan() {
    return oikeusMaksuttomaanAsuntolapaikkaan;
  }

  public void setOikeusMaksuttomaanAsuntolapaikkaan(Majoitusjakso oikeusMaksuttomaanAsuntolapaikkaan) {
    this.oikeusMaksuttomaanAsuntolapaikkaan = oikeusMaksuttomaanAsuntolapaikkaan;
  }

  private Boolean vuosiluokkiinSitoutumatonOpetus;
  private Boolean vammainen;
  private Boolean vaikeastiVammainen;
  private final List<Majoitusjakso> ulkomailla = new ArrayList<>();
  private Majoitusjakso majoitusetu;
  private Majoitusjakso oikeusMaksuttomaanAsuntolapaikkaan;
  private final List<Majoitusjakso> sisaoppilaitosmainenMajoitus = new ArrayList<>();
}