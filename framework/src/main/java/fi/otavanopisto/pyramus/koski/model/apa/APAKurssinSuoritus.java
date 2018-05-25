package fi.otavanopisto.pyramus.koski.model.apa;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointi;
import fi.otavanopisto.pyramus.koski.model.OsaamisenTunnustaminen;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class APAKurssinSuoritus {

  public APAKurssinSuoritus() {
  }
  
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

  public void setKoulutusmoduuli(APAKurssinTunniste koulutusmoduuli) {
    this.koulutusmoduuli = koulutusmoduuli;
  }

  public KoodistoViite<Kieli> getSuorituskieli() {
    return suorituskieli;
  }
  
  public void setSuorituskieli(KoodistoViite<Kieli> suorituskieli) {
    this.suorituskieli = suorituskieli;
  }

  public OsaamisenTunnustaminen getTunnustettu() {
    return tunnustettu;
  }

  public void setTunnustettu(OsaamisenTunnustaminen tunnustettu) {
    this.tunnustettu = tunnustettu;
  }

  private APAKurssinTunniste koulutusmoduuli;
  private final Set<KurssinArviointi> arviointi = new HashSet<>();
  private KoodistoViite<Kieli> suorituskieli;
  private OsaamisenTunnustaminen tunnustettu;
  private final KoodistoViite<SuorituksenTyyppi> tyyppi = new KoodistoViite<>(SuorituksenTyyppi.aikuistenperusopetuksenalkuvaiheenkurssi);
}
