package fi.otavanopisto.pyramus.koski.model;

import java.util.Date;

import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;

public class KurssinArviointiNumeerinen extends KurssinArviointi {

  public KurssinArviointiNumeerinen(ArviointiasteikkoYleissivistava arvosana, Date paiva) {
    super(arvosana, paiva);
  }
  
}
