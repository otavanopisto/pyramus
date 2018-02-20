package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.koodisto.OpiskeluoikeudenTyyppi;
import fi.otavanopisto.pyramus.koski.model.Opiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.OpiskeluoikeusTila;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class AikuistenPerusopetuksenOpiskeluoikeus extends Opiskeluoikeus {

  public AikuistenPerusopetuksenOpiskeluoikeus() {
    super(OpiskeluoikeudenTyyppi.aikuistenperusopetus);
  }

  public OpiskeluoikeusTila getTila() {
    return tila;
  }
  
  public void addSuoritus(AikuistenPerusopetuksenSuoritus suoritus) {
    suoritukset.add(suoritus);
  }
  
  public Set<AikuistenPerusopetuksenSuoritus> getSuoritukset() {
    return suoritukset;
  }

  @JsonProperty("lisätiedot")
  public AikuistenPerusopetuksenOpiskeluoikeudenLisatiedot getLisatiedot() {
    return lisatiedot;
  }

  public void setLisatiedot(AikuistenPerusopetuksenOpiskeluoikeudenLisatiedot lisatiedot) {
    this.lisatiedot = lisatiedot;
  }

  private final OpiskeluoikeusTila tila = new OpiskeluoikeusTila();
  private final Set<AikuistenPerusopetuksenSuoritus> suoritukset = new HashSet<>();
  private AikuistenPerusopetuksenOpiskeluoikeudenLisatiedot lisatiedot;
}