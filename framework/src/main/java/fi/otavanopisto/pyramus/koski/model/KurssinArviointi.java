package fi.otavanopisto.pyramus.koski.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;
import fi.otavanopisto.pyramus.koski.model.deserializers.KurssinArviointiDeserializer;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = KurssinArviointiDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class KurssinArviointi {

  public KurssinArviointi() {
  }
  
  public KurssinArviointi(ArviointiasteikkoYleissivistava arvosana, Date paiva) {
    this.arvosana.setValue(arvosana);
    this.paiva = paiva;
  }
  
  public KoodistoViite<ArviointiasteikkoYleissivistava> getArvosana() {
    return arvosana;
  }
  
  public void setArvosana(KoodistoViite<ArviointiasteikkoYleissivistava> arvosana) {
    this.arvosana = arvosana;
  }

  @JsonProperty("päivä")
  public Date getPaiva() {
    return paiva;
  }
  
  public void setPaiva(Date paiva) {
    this.paiva = paiva;
  }

  private Date paiva;
  private KoodistoViite<ArviointiasteikkoYleissivistava> arvosana = new KoodistoViite<>();
}
