package fi.otavanopisto.pyramus.koski.model.apa;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointi;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class APAOppiaineenSuoritus {

  public APAOppiaineenSuoritus() {
  }
  
  public APAOppiaineenSuoritus(APAOppiaineenTunniste koulutusmoduuli) {
    this.koulutusmoduuli = koulutusmoduuli;
  }
  
  public APAOppiaineenTunniste getKoulutusmoduuli() {
    return koulutusmoduuli;
  }
  
  public void setKoulutusmoduuli(APAOppiaineenTunniste koulutusmoduuli) {
    this.koulutusmoduuli = koulutusmoduuli;
  }

  public KoodistoViite<SuorituksenTyyppi> getTyyppi() {
    return tyyppi;
  }
  
  public void addOsasuoritus(APAKurssinSuoritus lukionKurssinSuoritus) {
    osasuoritukset.add(lukionKurssinSuoritus);
  }
  
  public Set<APAKurssinSuoritus> getOsasuoritukset() {
    return osasuoritukset;
  }

  public void addArviointi(KurssinArviointi arviointi) {
    this.arviointi.add(arviointi);
  }
  
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public Set<KurssinArviointi> getArviointi() {
    return arviointi;
  }

  private APAOppiaineenTunniste koulutusmoduuli;
  private final Set<KurssinArviointi> arviointi = new HashSet<>();
  private final KoodistoViite<SuorituksenTyyppi> tyyppi = new KoodistoViite<>(SuorituksenTyyppi.aikuistenperusopetuksenalkuvaiheenoppiaine);
  private final Set<APAKurssinSuoritus> osasuoritukset = new HashSet<>();
}
