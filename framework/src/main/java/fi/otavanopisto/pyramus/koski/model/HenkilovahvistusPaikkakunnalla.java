package fi.otavanopisto.pyramus.koski.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.Kunta;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HenkilovahvistusPaikkakunnalla {
  
  public HenkilovahvistusPaikkakunnalla() {
  }
  
  public HenkilovahvistusPaikkakunnalla(Date paiva, Kunta paikkakunta, Organisaatio myontajaOrganisaatio) {
    this.paiva = paiva;
    this.paikkakunta.setValue(paikkakunta);
    this.myontajaOrganisaatio = myontajaOrganisaatio;
  }

  @JsonProperty("päivä")
  public Date getPaiva() {
    return paiva;
  }

  public KoodistoViite<Kunta> getPaikkakunta() {
    return paikkakunta;
  }

  @JsonProperty("myöntäjäOrganisaatio")
  public Organisaatio getMyontajaOrganisaatio() {
    return myontajaOrganisaatio;
  }

  public void addMyontajaHenkilo(OrganisaatioHenkilo henkilo) {
    myontajaHenkilot.add(henkilo);
  }
  
  @JsonProperty("myöntäjäHenkilöt")
  public List<OrganisaatioHenkilo> getMyontajaHenkilot() {
    return myontajaHenkilot;
  }

  public void setPaiva(Date paiva) {
    this.paiva = paiva;
  }

  public void setMyontajaOrganisaatio(Organisaatio myontajaOrganisaatio) {
    this.myontajaOrganisaatio = myontajaOrganisaatio;
  }

  private Date paiva;
  private final KoodistoViite<Kunta> paikkakunta = new KoodistoViite<>();
  private Organisaatio myontajaOrganisaatio;
  private final List<OrganisaatioHenkilo> myontajaHenkilot = new ArrayList<>();
}
