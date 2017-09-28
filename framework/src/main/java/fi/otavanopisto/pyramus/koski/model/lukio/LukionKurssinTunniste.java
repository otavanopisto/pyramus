package fi.otavanopisto.pyramus.koski.model.lukio;

import fi.otavanopisto.pyramus.koski.koodisto.LukionKurssinTyyppi;
import fi.otavanopisto.pyramus.koski.koodisto.test.KoodistoViite;
import fi.otavanopisto.pyramus.koski.model.Laajuus;

public abstract class LukionKurssinTunniste {

  public LukionKurssinTunniste(LukionKurssinTyyppi kurssinTyyppi) {
    this.kurssinTyyppi.setValue(kurssinTyyppi);
  }
  
  public Laajuus getLaajuus() {
    return laajuus;
  }
  
  public void setLaajuus(Laajuus laajuus) {
    this.laajuus = laajuus;
  }

  public KoodistoViite<LukionKurssinTyyppi> getKurssinTyyppi() {
    return kurssinTyyppi;
  }
  
  private Laajuus laajuus;
  private final KoodistoViite<LukionKurssinTyyppi> kurssinTyyppi = new KoodistoViite<>();
}
