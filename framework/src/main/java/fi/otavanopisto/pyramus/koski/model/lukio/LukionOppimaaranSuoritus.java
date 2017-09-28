package fi.otavanopisto.pyramus.koski.model.lukio;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.Koulutus;
import fi.otavanopisto.pyramus.koski.koodisto.LukionOppimaara;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTila;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.koodisto.test.KoodistoViite;
import fi.otavanopisto.pyramus.koski.model.Koulutusmoduuli;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipiste;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LukionOppimaaranSuoritus extends LukionSuoritus {
  
  public LukionOppimaaranSuoritus(SuorituksenTila suorituksenTila, 
      LukionOppimaara oppimaara, Kieli suorituskieli, OrganisaationToimipiste toimipiste) {
    this.toimipiste = toimipiste;
    this.oppimaara.setValue(oppimaara);
    this.suorituskieli.setValue(suorituskieli);
    this.tila.setValue(suorituksenTila);
  }
  
  public OrganisaationToimipiste getToimipiste() {
    return toimipiste;
  }
  
  public void setToimipiste(OrganisaationToimipiste toimipiste) {
    this.toimipiste = toimipiste;
  }

  public void addOsasuoritus(LukionOsasuoritus osasuoritus) {
    osasuoritukset.add(osasuoritus);
  }
  
  public Set<LukionOsasuoritus> getOsasuoritukset() {
    return osasuoritukset;
  }
  
  public Koulutusmoduuli getKoulutusmoduuli() {
    return koulutusmoduuli;
  }
  
  @JsonProperty("oppimäärä")
  public KoodistoViite<LukionOppimaara> getOppimaara() {
    return oppimaara;
  }
  
  public KoodistoViite<Kieli> getSuorituskieli() {
    return suorituskieli;
  }
  
  public KoodistoViite<SuorituksenTila> getTila() {
    return tila;
  }
  
  public KoodistoViite<SuorituksenTyyppi> getTyyppi() {
    return tyyppi;
  }

  private final Koulutusmoduuli koulutusmoduuli = new Koulutusmoduuli(Koulutus.K309902);
  private final KoodistoViite<LukionOppimaara> oppimaara = new KoodistoViite<>();
  private OrganisaationToimipiste toimipiste;
  private final KoodistoViite<Kieli> suorituskieli = new KoodistoViite<>();
  private final Set<LukionOsasuoritus> osasuoritukset = new HashSet<>();
  private final KoodistoViite<SuorituksenTila> tila = new KoodistoViite<>();
  private final KoodistoViite<SuorituksenTyyppi> tyyppi = new KoodistoViite<>(SuorituksenTyyppi.lukionoppimaara);
}
