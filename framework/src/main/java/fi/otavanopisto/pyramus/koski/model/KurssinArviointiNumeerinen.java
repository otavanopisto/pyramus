package fi.otavanopisto.pyramus.koski.model;

import java.util.Date;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class KurssinArviointiNumeerinen extends KurssinArviointi {

  public KurssinArviointiNumeerinen() {
  }
  
  public KurssinArviointiNumeerinen(ArviointiasteikkoYleissivistava arvosana, Date paiva) {
    super(arvosana, paiva);
  }
  
}
