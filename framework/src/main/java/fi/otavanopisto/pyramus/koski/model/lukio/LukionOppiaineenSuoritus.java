package fi.otavanopisto.pyramus.koski.model.lukio;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

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
  
  public List<LukionKurssinSuoritus> getOsasuoritukset() {
    return osasuoritukset;
  }

  public void addArviointi(KurssinArviointi arviointi) {
    this.arviointi.add(arviointi);
  }
  
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public List<KurssinArviointi> getArviointi() {
    return arviointi;
  }

  private final LukionOppiaineenTunniste koulutusmoduuli;
  private final List<KurssinArviointi> arviointi = new ArrayList<>();
  private final KoodistoViite<SuorituksenTyyppi> tyyppi = new KoodistoViite<>(SuorituksenTyyppi.lukionoppiaine);
  private final List<LukionKurssinSuoritus> osasuoritukset = new ArrayList<>();
}
