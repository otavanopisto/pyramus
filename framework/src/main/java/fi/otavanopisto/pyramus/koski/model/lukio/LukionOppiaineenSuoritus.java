package fi.otavanopisto.pyramus.koski.model.lukio;

import java.util.HashSet;
import java.util.Set;

import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTila;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.koodisto.test.KoodistoViite;

public class LukionOppiaineenSuoritus extends LukionOsasuoritus {

  public LukionOppiaineenSuoritus(LukionOppiaineenTunniste koulutusmoduuli, 
      SuorituksenTila tila) {
    this.koulutusmoduuli = koulutusmoduuli;
    this.tila.setValue(tila);
  }
  
  public LukionOppiaineenTunniste getKoulutusmoduuli() {
    return koulutusmoduuli;
  }
  
  public KoodistoViite<SuorituksenTila> getTila() {
    return tila;
  }
  
  public KoodistoViite<SuorituksenTyyppi> getTyyppi() {
    return tyyppi;
  }
  
  public void addOsasuoritus(LukionKurssinSuoritus lukionKurssinSuoritus) {
    osasuoritukset.add(lukionKurssinSuoritus);
  }
  
  public Set<LukionKurssinSuoritus> getOsasuoritukset() {
    return osasuoritukset;
  }

  private final LukionOppiaineenTunniste koulutusmoduuli;
  private final KoodistoViite<SuorituksenTila> tila = new KoodistoViite<>();
  private final KoodistoViite<SuorituksenTyyppi> tyyppi = new KoodistoViite<>(SuorituksenTyyppi.lukionoppiaine);
  private final Set<LukionKurssinSuoritus> osasuoritukset = new HashSet<>();
}
