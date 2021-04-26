package fi.otavanopisto.pyramus.koski.model.lukio.ops2019;

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
public class LukionOpintojaksonSuoritus2019 implements KurssinSuoritus {

  public LukionOpintojaksonSuoritus2019() {
  }
  
  public LukionOpintojaksonSuoritus2019(LukionOpintojaksonTunniste2019 tunniste, SuorituksenTyyppi suorituksenTyyppi) {
    koulutusmoduuli = tunniste;
    this.tyyppi.setValue(suorituksenTyyppi);
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
  
  public LukionOpintojaksonTunniste2019 getKoulutusmoduuli() {
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

  public void setKoulutusmoduuli(LukionOpintojaksonTunniste2019 koulutusmoduuli) {
    this.koulutusmoduuli = koulutusmoduuli;
  }

  private LukionOpintojaksonTunniste2019 koulutusmoduuli;
  private final List<KurssinArviointi> arviointi = new ArrayList<>();
  private OsaamisenTunnustaminen tunnustettu;
  private KoodistoViite<Kieli> suorituskieli;
  private final KoodistoViite<SuorituksenTyyppi> tyyppi = new KoodistoViite<>();
}
