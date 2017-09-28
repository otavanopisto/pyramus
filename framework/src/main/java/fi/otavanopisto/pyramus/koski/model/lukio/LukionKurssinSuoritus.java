package fi.otavanopisto.pyramus.koski.model.lukio;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTila;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointi;
import fi.otavanopisto.pyramus.koski.model.OsaamisenTunnustaminen;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LukionKurssinSuoritus {

  public LukionKurssinSuoritus(LukionKurssinTunniste tunniste, SuorituksenTila tila) {
    koulutusmoduuli = tunniste;
    this.tila.setValue(tila);
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
  
  public LukionKurssinTunniste getKoulutusmoduuli() {
    return koulutusmoduuli;
  }

  public OsaamisenTunnustaminen getTunnustettu() {
    return tunnustettu;
  }

  public void setTunnustettu(OsaamisenTunnustaminen tunnustettu) {
    this.tunnustettu = tunnustettu;
  }

  public KoodistoViite<Kieli> getSuorituskieli() {
    return suorituskieli;
  }
  
  public void setSuorituskieli(KoodistoViite<Kieli> suorituskieli) {
    this.suorituskieli = suorituskieli;
  }

  private final LukionKurssinTunniste koulutusmoduuli;
  private final KoodistoViite<SuorituksenTila> tila = new KoodistoViite<>();
  private final Set<KurssinArviointi> arviointi = new HashSet<>();
  private OsaamisenTunnustaminen tunnustettu;
  private KoodistoViite<Kieli> suorituskieli;
  private final KoodistoViite<SuorituksenTyyppi> tyyppi = new KoodistoViite<>(SuorituksenTyyppi.lukionkurssi);
  private boolean suoritettuLukiodiplomina;
  private boolean suoritettuSuullisenaKielikokeena;
}
