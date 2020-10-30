package fi.otavanopisto.pyramus.koski.model.apa;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.KurssinSuoritus;
import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointi;
import fi.otavanopisto.pyramus.koski.model.OsaamisenTunnustaminen;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class APAKurssinSuoritus implements KurssinSuoritus {

  public APAKurssinSuoritus() {
  }
  
  public APAKurssinSuoritus(APAKurssinTunniste tunniste) {
    koulutusmoduuli = tunniste;
  }
  
  @Override
  public void addArviointi(KurssinArviointi arviointi) {
    this.arviointi.add(arviointi);
  }
  
  @Override
  public List<KurssinArviointi> getArviointi() {
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

  @Override
  public OsaamisenTunnustaminen getTunnustettu() {
    return tunnustettu;
  }

  @Override
  public void setTunnustettu(OsaamisenTunnustaminen tunnustettu) {
    this.tunnustettu = tunnustettu;
  }

  private APAKurssinTunniste koulutusmoduuli;
  private final List<KurssinArviointi> arviointi = new ArrayList<>();
  private KoodistoViite<Kieli> suorituskieli;
  private OsaamisenTunnustaminen tunnustettu;
  private final KoodistoViite<SuorituksenTyyppi> tyyppi = new KoodistoViite<>(SuorituksenTyyppi.aikuistenperusopetuksenalkuvaiheenkurssi);
}
