package fi.otavanopisto.pyramus.koski.model.lukio.ops2019;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.LukionMuutOpinnot;

/**
 * Käytetään lukiodiplomeille (tunniste = LD), temaattisille opinnoille (TO) ja
 * muille suorituksille (MS).
 */
public class LukionMuidenOpintojenTunniste2019 extends LukionOppiaineenKoulutusmoduuli {

  public LukionMuidenOpintojenTunniste2019(LukionMuutOpinnot tunniste) {
    super();
    this.getTunniste().setValue(tunniste);
  }
  
  public KoodistoViite<LukionMuutOpinnot> getTunniste() {
    return tunniste;
  }

  private final KoodistoViite<LukionMuutOpinnot> tunniste = new KoodistoViite<>();
}
