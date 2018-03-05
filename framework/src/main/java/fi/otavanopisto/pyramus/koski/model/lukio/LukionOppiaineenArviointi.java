package fi.otavanopisto.pyramus.koski.model.lukio;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LukionOppiaineenArviointi {

  public LukionOppiaineenArviointi() {
  }
  
  public LukionOppiaineenArviointi(ArviointiasteikkoYleissivistava arvosana, Date paiva) {
    this.arvosana.setValue(arvosana);
    this.paiva = paiva;
  }
  
  public KoodistoViite<ArviointiasteikkoYleissivistava> getArvosana() {
    return arvosana;
  }
  
  @JsonProperty("päivä")
  public Date getPaiva() {
    return paiva;
  }
  
  public void setPaiva(Date paiva) {
    this.paiva = paiva;
  }

  private Date paiva;
  private final KoodistoViite<ArviointiasteikkoYleissivistava> arvosana = new KoodistoViite<>();
}
