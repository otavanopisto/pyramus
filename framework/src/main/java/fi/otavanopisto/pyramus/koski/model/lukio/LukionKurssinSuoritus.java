package fi.otavanopisto.pyramus.koski.model.lukio;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.KurssinSuoritus;
import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointi;
import fi.otavanopisto.pyramus.koski.model.OsaamisenTunnustaminen;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LukionKurssinSuoritus implements KurssinSuoritus {

  public LukionKurssinSuoritus() {
  }
  
  public LukionKurssinSuoritus(LukionKurssinTunniste tunniste) {
    koulutusmoduuli = tunniste;
  }
  
  public boolean isSuoritettuLukiodiplomina() {
    return suoritettuLukiodiplomina;
  }
  
  public void setSuoritettuLukiodiplomina(boolean suoritettuLukiodiplomina) {
    this.suoritettuLukiodiplomina = suoritettuLukiodiplomina;
  }
  
  public boolean isSuoritettuSuullisenaKielikokeena() {
    return suoritettuSuullisenaKielikokeena;
  }
  
  public void setSuoritettuSuullisenaKielikokeena(boolean suoritettuSuullisenaKielikokeena) {
    this.suoritettuSuullisenaKielikokeena = suoritettuSuullisenaKielikokeena;
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
  
  public LukionKurssinTunniste getKoulutusmoduuli() {
    return koulutusmoduuli;
  }

  @Override
  public OsaamisenTunnustaminen getTunnustettu() {
    return tunnustettu;
  }

  @Override
  public void setTunnustettu(OsaamisenTunnustaminen tunnustettu) {
    this.tunnustettu = tunnustettu;
  }

  public KoodistoViite<Kieli> getSuorituskieli() {
    return suorituskieli;
  }
  
  public void setSuorituskieli(KoodistoViite<Kieli> suorituskieli) {
    this.suorituskieli = suorituskieli;
  }

  public void setKoulutusmoduuli(LukionKurssinTunniste koulutusmoduuli) {
    this.koulutusmoduuli = koulutusmoduuli;
  }

  private LukionKurssinTunniste koulutusmoduuli;
  private final List<KurssinArviointi> arviointi = new ArrayList<>();
  private OsaamisenTunnustaminen tunnustettu;
  private KoodistoViite<Kieli> suorituskieli;
  private final KoodistoViite<SuorituksenTyyppi> tyyppi = new KoodistoViite<>(SuorituksenTyyppi.lukionkurssi);
  private boolean suoritettuLukiodiplomina;
  private boolean suoritettuSuullisenaKielikokeena;
}
