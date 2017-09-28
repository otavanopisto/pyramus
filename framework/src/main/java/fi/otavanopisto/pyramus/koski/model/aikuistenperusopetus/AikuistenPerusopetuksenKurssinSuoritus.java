package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTila;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.koodisto.test.KoodistoViite;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointi;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AikuistenPerusopetuksenKurssinSuoritus {

  public AikuistenPerusopetuksenKurssinSuoritus(AikuistenPerusopetuksenKurssinTunniste tunniste, SuorituksenTila tila) {
    koulutusmoduuli = tunniste;
    this.tila.setValue(tila);
  }
  
  public KoodistoViite<SuorituksenTila> getTila() {
    return tila;
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
  
  public AikuistenPerusopetuksenKurssinTunniste getKoulutusmoduuli() {
    return koulutusmoduuli;
  }

  public KoodistoViite<Kieli> getSuorituskieli() {
    return suorituskieli;
  }
  
  public void setSuorituskieli(KoodistoViite<Kieli> suorituskieli) {
    this.suorituskieli = suorituskieli;
  }

  private final AikuistenPerusopetuksenKurssinTunniste koulutusmoduuli;
  private final KoodistoViite<SuorituksenTila> tila = new KoodistoViite<>();
  private final Set<KurssinArviointi> arviointi = new HashSet<>();
  private KoodistoViite<Kieli> suorituskieli;
  private final KoodistoViite<SuorituksenTyyppi> tyyppi = new KoodistoViite<>(SuorituksenTyyppi.aikuistenperusopetuksenkurssi);
}
