package fi.otavanopisto.pyramus.koski.model.lukio;

import java.util.HashSet;
import java.util.Set;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointi;

public class LukionOppiaineenSuoritus extends LukionOsasuoritus {

  public LukionOppiaineenSuoritus(LukionOppiaineenTunniste koulutusmoduuli) {
    this.koulutusmoduuli = koulutusmoduuli;
  }
  
  public LukionOppiaineenTunniste getKoulutusmoduuli() {
    return koulutusmoduuli;
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

  public void addArviointi(KurssinArviointi arviointi) {
    this.arviointi.add(arviointi);
  }
  
  public Set<KurssinArviointi> getArviointi() {
    return arviointi;
  }

  private final LukionOppiaineenTunniste koulutusmoduuli;
  private final Set<KurssinArviointi> arviointi = new HashSet<>();
  private final KoodistoViite<SuorituksenTyyppi> tyyppi = new KoodistoViite<>(SuorituksenTyyppi.lukionoppiaine);
  private final Set<LukionKurssinSuoritus> osasuoritukset = new HashSet<>();
}
