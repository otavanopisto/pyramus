package fi.otavanopisto.pyramus.koski.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.OpiskeluoikeudenTyyppi;

@JsonInclude(JsonInclude.Include.NON_NULL)
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

  private String oid;
  private Date alkamispaiva;
  private Date paattymispaiva;
  private final KoodistoViite<OpiskeluoikeudenTyyppi> tyyppi = new KoodistoViite<>();
}
