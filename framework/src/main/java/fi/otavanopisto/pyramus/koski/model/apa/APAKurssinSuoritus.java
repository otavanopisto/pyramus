package fi.otavanopisto.pyramus.koski.model.apa;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointi;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class APAKurssinSuoritus {

  public APAKurssinSuoritus(APAKurssinTunniste tunniste) {
    koulutusmoduuli = tunniste;
  }
  
  public void addArviointi(KurssinArviointi arviointi) {
    this.arviointi.add(arviointi);
  }
  
  public Set<KurssinArviointi> getArviointi() {
    return arviointi;
  }
  
  public KoodistoViite<SuorituksenTyyppi> getTyyppi() {
    return tyyppi;
  }
  
  public APAKurssinTunniste getKoulutusmoduuli() {
    return koulutusmoduuli;
  }

  public KoodistoViite<Kieli> getSuorituskieli() {
    return suorituskieli;
  }
  
  public void setSuorituskieli(KoodistoViite<Kieli> suorituskieli) {
    this.suorituskieli = suorituskieli;
  }

  private final APAKurssinTunniste koulutusmoduuli;
  private final Set<KurssinArviointi> arviointi = new HashSet<>();
  private KoodistoViite<Kieli> suorituskieli;
  private final KoodistoViite<SuorituksenTyyppi> tyyppi = new KoodistoViite<>(SuorituksenTyyppi.aikuistenperusopetuksenalkuvaiheenkurssi);
}
