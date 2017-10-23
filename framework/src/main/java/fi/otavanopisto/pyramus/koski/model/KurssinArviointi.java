package fi.otavanopisto.pyramus.koski.model;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;

public abstract class KurssinArviointi {

  public KurssinArviointi(ArviointiasteikkoYleissivistava arvosana) {
    this.arvosana.setValue(arvosana);
  }
  
  public KoodistoViite<ArviointiasteikkoYleissivistava> getArvosana() {
    return arvosana;
  }
  
  private final KoodistoViite<ArviointiasteikkoYleissivistava> arvosana = new KoodistoViite<>();
}
