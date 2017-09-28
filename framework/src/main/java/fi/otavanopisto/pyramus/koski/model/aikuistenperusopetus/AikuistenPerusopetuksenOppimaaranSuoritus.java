package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.Koulutus;
import fi.otavanopisto.pyramus.koski.koodisto.PerusopetuksenSuoritusTapa;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTila;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.koodisto.test.KoodistoViite;
import fi.otavanopisto.pyramus.koski.model.Koulutusmoduuli;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipiste;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AikuistenPerusopetuksenOppimaaranSuoritus extends AikuistenPerusopetuksenSuoritus {
  
  public AikuistenPerusopetuksenOppimaaranSuoritus(SuorituksenTila suorituksenTila, 
      PerusopetuksenSuoritusTapa suoritustapa, Kieli suorituskieli, OrganisaationToimipiste toimipiste) {
    this.toimipiste = toimipiste;
    this.suoritustapa.setValue(suoritustapa);
    this.suorituskieli.setValue(suorituskieli);
    this.tila.setValue(suorituksenTila);
  }
  
  public OrganisaationToimipiste getToimipiste() {
    return toimipiste;
  }
  
  public void addOsasuoritus(AikuistenPerusopetuksenOsasuoritus osasuoritus) {
    osasuoritukset.add(osasuoritus);
  }
  
  public Set<AikuistenPerusopetuksenOsasuoritus> getOsasuoritukset() {
    return osasuoritukset;
  }
  
  public Koulutusmoduuli getKoulutusmoduuli() {
    return koulutusmoduuli;
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

  public KoodistoViite<PerusopetuksenSuoritusTapa> getSuoritustapa() {
    return suoritustapa;
  }

  private final Koulutusmoduuli koulutusmoduuli = new Koulutusmoduuli(Koulutus.K201101);
  private final OrganisaationToimipiste toimipiste;
  private final KoodistoViite<SuorituksenTila> tila = new KoodistoViite<>();
  private final KoodistoViite<PerusopetuksenSuoritusTapa> suoritustapa = new KoodistoViite<>();
  private final KoodistoViite<Kieli> suorituskieli = new KoodistoViite<>();
  private final Set<AikuistenPerusopetuksenOsasuoritus> osasuoritukset = new HashSet<>();
  private final KoodistoViite<SuorituksenTyyppi> tyyppi = new KoodistoViite<>(SuorituksenTyyppi.aikuistenperusopetuksenoppimaara);
}
