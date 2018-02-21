package fi.otavanopisto.pyramus.koski.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.OpiskeluoikeudenTyyppi;
import fi.otavanopisto.pyramus.koski.model.deserializers.OpiskeluoikeusDeserializer;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = OpiskeluoikeusDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Opiskeluoikeus {

  public Opiskeluoikeus(OpiskeluoikeudenTyyppi tyyppi) {
    this.tyyppi.setValue(tyyppi);
  }
  
  @JsonProperty("alkamispäivä")
  public Date getAlkamispaiva() {
    return alkamispaiva;
  }
  
  public void setAlkamispaiva(Date alkamispaiva) {
    this.alkamispaiva = alkamispaiva;
  }
  
  @JsonProperty("päättymispäivä")
  public Date getPaattymispaiva() {
    return paattymispaiva;
  }
  
  public void setPaattymispaiva(Date paattymispaiva) {
    this.paattymispaiva = paattymispaiva;
  }
  
  public KoodistoViite<OpiskeluoikeudenTyyppi> getTyyppi() {
    return tyyppi;
  }
  
  public String getOid() {
    return oid;
  }

  public void setOid(String oid) {
    this.oid = oid;
  }

  @JsonProperty("lähdejärjestelmänId")
  public LahdeJarjestelmaID getLahdejarjestelmanId() {
    return lahdejarjestelmanId;
  }

  public void setLahdejarjestelmanId(LahdeJarjestelmaID lahdejarjestelmanId) {
    this.lahdejarjestelmanId = lahdejarjestelmanId;
  }

  @JsonProperty("sisältyyOpiskeluoikeuteen")
  public SisaltavaOpiskeluoikeus getSisaltyyOpiskeluoikeuteen() {
    return sisaltyyOpiskeluoikeuteen;
  }

  public void setSisaltyyOpiskeluoikeuteen(SisaltavaOpiskeluoikeus sisaltyyOpiskeluoikeuteen) {
    this.sisaltyyOpiskeluoikeuteen = sisaltyyOpiskeluoikeuteen;
  }

  public Oppilaitos getOppilaitos() {
    return oppilaitos;
  }

  public void setOppilaitos(Oppilaitos oppilaitos) {
    this.oppilaitos = oppilaitos;
  }

  private String oid;
  private Date alkamispaiva;
  private Date paattymispaiva;
  private final KoodistoViite<OpiskeluoikeudenTyyppi> tyyppi = new KoodistoViite<>();
  private LahdeJarjestelmaID lahdejarjestelmanId;
  private SisaltavaOpiskeluoikeus sisaltyyOpiskeluoikeuteen;
  private Oppilaitos oppilaitos;
}
