package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.PerusopetuksenSuoritusTapa;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTila;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.Kuvaus;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipiste;
import fi.otavanopisto.pyramus.koski.model.Suoritus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AikuistenPerusopetuksenSuoritus extends Suoritus {

  public AikuistenPerusopetuksenSuoritus(PerusopetuksenSuoritusTapa suoritustapa, SuorituksenTila tila, 
      SuorituksenTyyppi tyyppi, Kieli suorituskieli, OrganisaationToimipiste toimipiste) {
    super(tila, tyyppi, suorituskieli, toimipiste);
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
