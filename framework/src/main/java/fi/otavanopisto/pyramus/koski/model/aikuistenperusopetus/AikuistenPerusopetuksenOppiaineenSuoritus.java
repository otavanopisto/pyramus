package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import java.util.ArrayList;
import java.util.List;

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
  
  public List<AikuistenPerusopetuksenKurssinSuoritus> getOsasuoritukset() {
    return osasuoritukset;
  }

  public void addArviointi(KurssinArviointi arviointi) {
    this.arviointi.add(arviointi);
  }
  
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public List<KurssinArviointi> getArviointi() {
    return arviointi;
  }

  private final AikuistenPerusopetuksenOppiaineenTunniste koulutusmoduuli;
  private final List<KurssinArviointi> arviointi = new ArrayList<>();
  private final KoodistoViite<SuorituksenTyyppi> tyyppi = new KoodistoViite<>(SuorituksenTyyppi.aikuistenperusopetuksenoppiaine);
  private final List<AikuistenPerusopetuksenKurssinSuoritus> osasuoritukset = new ArrayList<>();
}
