package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointi;

public class AikuistenPerusopetuksenOppiaineenSuoritus extends AikuistenPerusopetuksenOsasuoritus {

  public AikuistenPerusopetuksenOppiaineenSuoritus(AikuistenPerusopetuksenOppiaineenTunniste koulutusmoduuli) {
    this.koulutusmoduuli = koulutusmoduuli;
  }
  
  public AikuistenPerusopetuksenOppiaineenTunniste getKoulutusmoduuli() {
    return koulutusmoduuli;
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

  public void addArviointi(KurssinArviointi arviointi) {
    this.arviointi.add(arviointi);
  }
  
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public Set<KurssinArviointi> getArviointi() {
    return arviointi;
  }

  private final AikuistenPerusopetuksenOppiaineenTunniste koulutusmoduuli;
  private final Set<KurssinArviointi> arviointi = new HashSet<>();
  private final KoodistoViite<SuorituksenTyyppi> tyyppi = new KoodistoViite<>(SuorituksenTyyppi.aikuistenperusopetuksenoppiaine);
  private final Set<AikuistenPerusopetuksenKurssinSuoritus> osasuoritukset = new HashSet<>();
}
