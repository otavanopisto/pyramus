package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.koodisto.OpiskeluoikeudenTyyppi;
import fi.otavanopisto.pyramus.koski.model.Opiskeluoikeus;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class AikuistenPerusopetuksenOpiskeluoikeus extends Opiskeluoikeus {

  public AikuistenPerusopetuksenOpiskeluoikeus() {
    super(OpiskeluoikeudenTyyppi.aikuistenperusopetus);
  }

  public void addSuoritus(AikuistenPerusopetuksenSuoritus suoritus) {
    suoritukset.add(suoritus);
  }
  
  public List<AikuistenPerusopetuksenSuoritus> getSuoritukset() {
    return suoritukset;
  }

  @JsonProperty("lisätiedot")
  public AikuistenPerusopetuksenOpiskeluoikeudenLisatiedot getLisatiedot() {
    return lisatiedot;
  }

  public void setLisatiedot(AikuistenPerusopetuksenOpiskeluoikeudenLisatiedot lisatiedot) {
    this.lisatiedot = lisatiedot;
  }

  private final List<AikuistenPerusopetuksenSuoritus> suoritukset = new ArrayList<>();
  private AikuistenPerusopetuksenOpiskeluoikeudenLisatiedot lisatiedot;
}