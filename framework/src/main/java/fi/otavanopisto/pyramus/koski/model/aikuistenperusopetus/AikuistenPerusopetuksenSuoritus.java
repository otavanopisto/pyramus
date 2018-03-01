package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.PerusopetuksenSuoritusTapa;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.Kuvaus;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipiste;
import fi.otavanopisto.pyramus.koski.model.Suoritus;
import fi.otavanopisto.pyramus.koski.model.deserializers.SuoritusDeserializer;

@JsonDeserialize(using = SuoritusDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AikuistenPerusopetuksenSuoritus extends Suoritus {

  public AikuistenPerusopetuksenSuoritus() {
  }
  
  public AikuistenPerusopetuksenSuoritus(PerusopetuksenSuoritusTapa suoritustapa, 
      SuorituksenTyyppi tyyppi, Kieli suorituskieli, OrganisaationToimipiste toimipiste) {
    super(tyyppi, suorituskieli, toimipiste);
    this.suoritustapa.setValue(suoritustapa);
  }

  public KoodistoViite<PerusopetuksenSuoritusTapa> getSuoritustapa() {
    return suoritustapa;
  }

  @JsonProperty("todistuksellaNäkyvätLisätiedot")
  public Kuvaus getTodistuksellaNakyvatLisatiedot() {
    return todistuksellaNakyvatLisatiedot;
  }

  public void setTodistuksellaNakyvatLisatiedot(Kuvaus todistuksellaNakyvatLisatiedot) {
    this.todistuksellaNakyvatLisatiedot = todistuksellaNakyvatLisatiedot;
  }

  private final KoodistoViite<PerusopetuksenSuoritusTapa> suoritustapa = new KoodistoViite<>();
  private Kuvaus todistuksellaNakyvatLisatiedot;
}
