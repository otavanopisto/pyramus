package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.Koulutus;
import fi.otavanopisto.pyramus.koski.koodisto.PerusopetuksenSuoritusTapa;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTila;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.Koulutusmoduuli;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipiste;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AikuistenPerusopetuksenOppimaaranSuoritus extends AikuistenPerusopetuksenSuoritus {
  
  public AikuistenPerusopetuksenOppimaaranSuoritus(PerusopetuksenSuoritusTapa suoritustapa, 
      Kieli suorituskieli, OrganisaationToimipiste toimipiste, SuorituksenTila tila) {
    super(tila, SuorituksenTyyppi.aikuistenperusopetuksenoppimaara, suorituskieli, toimipiste);
    this.suoritustapa.setValue(suoritustapa);
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
  
  public KoodistoViite<PerusopetuksenSuoritusTapa> getSuoritustapa() {
    return suoritustapa;
  }

  private final Koulutusmoduuli koulutusmoduuli = new Koulutusmoduuli(Koulutus.K201101);
  private final KoodistoViite<PerusopetuksenSuoritusTapa> suoritustapa = new KoodistoViite<>();
  private final Set<AikuistenPerusopetuksenOsasuoritus> osasuoritukset = new HashSet<>();
}
