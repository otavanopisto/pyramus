package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointi;
import fi.otavanopisto.pyramus.koski.model.OsaamisenTunnustaminen;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AikuistenPerusopetuksenKurssinSuoritus {

  public AikuistenPerusopetuksenKurssinSuoritus() {
  }
  
  public AikuistenPerusopetuksenKurssinSuoritus(AikuistenPerusopetuksenKurssinTunniste tunniste) {
    koulutusmoduuli = tunniste;
  }
  
  public void addArviointi(KurssinArviointi arviointi) {
    this.arviointi.add(arviointi);
  }
  
  public List<KurssinArviointi> getArviointi() {
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

  public void setKoulutusmoduuli(AikuistenPerusopetuksenKurssinTunniste koulutusmoduuli) {
    this.koulutusmoduuli = koulutusmoduuli;
  }

  public OsaamisenTunnustaminen getTunnustettu() {
    return tunnustettu;
  }

  public void setTunnustettu(OsaamisenTunnustaminen tunnustettu) {
    this.tunnustettu = tunnustettu;
  }

  private AikuistenPerusopetuksenKurssinTunniste koulutusmoduuli;
  private final List<KurssinArviointi> arviointi = new ArrayList<>();
  private OsaamisenTunnustaminen tunnustettu;
  private KoodistoViite<Kieli> suorituskieli;
  private final KoodistoViite<SuorituksenTyyppi> tyyppi = new KoodistoViite<>(SuorituksenTyyppi.aikuistenperusopetuksenkurssi);
}
