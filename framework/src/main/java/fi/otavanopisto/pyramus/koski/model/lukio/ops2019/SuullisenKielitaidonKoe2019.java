package fi.otavanopisto.pyramus.koski.model.lukio.ops2019;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoKehittyvanKielitaidonTasot;
import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.Kielivalikoima;
import fi.otavanopisto.pyramus.koski.model.Kuvaus;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SuullisenKielitaidonKoe2019 {

  public SuullisenKielitaidonKoe2019() {
  }
  
  public SuullisenKielitaidonKoe2019(Kielivalikoima kieli, ArviointiasteikkoYleissivistava arvosana, 
      ArviointiasteikkoKehittyvanKielitaidonTasot taitotaso, LocalDate paiva) {
    this.kieli.setValue(kieli);
    this.arvosana.setValue(arvosana);
    this.taitotaso.setValue(taitotaso);
    this.paiva = paiva;
  }
  
  public KoodistoViite<Kielivalikoima> getKieli() {
    return kieli;
  }
  public void setKieli(KoodistoViite<Kielivalikoima> kieli) {
    this.kieli = kieli;
  }

  public KoodistoViite<ArviointiasteikkoYleissivistava> getArvosana() {
    return arvosana;
  }

  public void setArvosana(KoodistoViite<ArviointiasteikkoYleissivistava> arvosana) {
    this.arvosana = arvosana;
  }

  public KoodistoViite<ArviointiasteikkoKehittyvanKielitaidonTasot> getTaitotaso() {
    return taitotaso;
  }

  public void setTaitotaso(KoodistoViite<ArviointiasteikkoKehittyvanKielitaidonTasot> taitotaso) {
    this.taitotaso = taitotaso;
  }

  public Kuvaus getKuvaus() {
    return kuvaus;
  }

  public void setKuvaus(Kuvaus kuvaus) {
    this.kuvaus = kuvaus;
  }

  @JsonProperty("päivä")
  public LocalDate getPaiva() {
    return paiva;
  }

  public void setPaiva(LocalDate paiva) {
    this.paiva = paiva;
  }

  private KoodistoViite<Kielivalikoima> kieli = new KoodistoViite<>();
  private KoodistoViite<ArviointiasteikkoYleissivistava> arvosana = new KoodistoViite<>();
  private KoodistoViite<ArviointiasteikkoKehittyvanKielitaidonTasot> taitotaso = new KoodistoViite<>();
  private Kuvaus kuvaus;
  private LocalDate paiva;
}
