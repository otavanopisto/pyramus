package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import java.util.HashSet;
import java.util.Set;

import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTila;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.koodisto.test.KoodistoViite;

public class AikuistenPerusopetuksenOppiaineenSuoritus extends AikuistenPerusopetuksenOsasuoritus {

  public AikuistenPerusopetuksenOppiaineenSuoritus(AikuistenPerusopetuksenOppiaineenTunniste koulutusmoduuli, 
      SuorituksenTila tila) {
    this.koulutusmoduuli = koulutusmoduuli;
    this.tila.setValue(tila);
  }
  
  public AikuistenPerusopetuksenOppiaineenTunniste getKoulutusmoduuli() {
    return koulutusmoduuli;
  }
  
  public KoodistoViite<SuorituksenTila> getTila() {
    return tila;
  }
  
  public KoodistoViite<SuorituksenTyyppi> getTyyppi() {
    return tyyppi;
  }
  
  public void addOsasuoritus(AikuistenPerusopetuksenKurssinSuoritus lukionKurssinSuoritus) {
    osasuoritukset.add(lukionKurssinSuoritus);
  }
  
  public Set<AikuistenPerusopetuksenKurssinSuoritus> getOsasuoritukset() {
    return osasuoritukset;
  }

  private final AikuistenPerusopetuksenOppiaineenTunniste koulutusmoduuli;
  private final KoodistoViite<SuorituksenTila> tila = new KoodistoViite<>();
  private final KoodistoViite<SuorituksenTyyppi> tyyppi = new KoodistoViite<>(SuorituksenTyyppi.aikuistenperusopetuksenoppiaine);
  private final Set<AikuistenPerusopetuksenKurssinSuoritus> osasuoritukset = new HashSet<>();
}
