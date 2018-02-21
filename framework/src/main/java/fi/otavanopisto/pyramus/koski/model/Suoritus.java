package fi.otavanopisto.pyramus.koski.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.deserializers.SuoritusDeserializer;

@JsonDeserialize(using = SuoritusDeserializer.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Suoritus {

  public Suoritus() {
  }
  
  public Suoritus(SuorituksenTyyppi tyyppi, Kieli suorituskieli, OrganisaationToimipiste toimipiste) {
    this.toimipiste = toimipiste;
    this.suorituskieli.setValue(suorituskieli);
    this.tyyppi.setValue(tyyppi);
  }
  
  public HenkilovahvistusPaikkakunnalla getVahvistus() {
    return vahvistus;
  }

  public void setVahvistus(HenkilovahvistusPaikkakunnalla vahvistus) {
    this.vahvistus = vahvistus;
  }

  public OrganisaationToimipiste getToimipiste() {
    return toimipiste;
  }

  public void setToimipiste(OrganisaationToimipiste toimipiste) {
    this.toimipiste = toimipiste;
  }

  public KoodistoViite<Kieli> getSuorituskieli() {
    return suorituskieli;
  }

  public KoodistoViite<SuorituksenTyyppi> getTyyppi() {
    return tyyppi;
  }

  @JsonProperty("todistuksellaNäkyvätLisätiedot")
  public Kuvaus getTodistuksellaNakyvatLisatiedot() {
    return todistuksellaNakyvatLisatiedot;
  }

  public void setTodistuksellaNakyvatLisatiedot(Kuvaus todistuksellaNakyvatLisatiedot) {
    this.todistuksellaNakyvatLisatiedot = todistuksellaNakyvatLisatiedot;
  }

  private Kuvaus todistuksellaNakyvatLisatiedot;
  private OrganisaationToimipiste toimipiste;
  private HenkilovahvistusPaikkakunnalla vahvistus;
  private final KoodistoViite<Kieli> suorituskieli = new KoodistoViite<>();
  private final KoodistoViite<SuorituksenTyyppi> tyyppi = new KoodistoViite<>();
}
