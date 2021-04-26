package fi.otavanopisto.pyramus.koski.model.lukio.ops2019;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.ModuuliKoodistoLOPS2021;
import fi.otavanopisto.pyramus.koski.model.Laajuus;

/**
 * Käytetään vieraan kielen aineille A, B1, B2, B3, AOM
 */
@JsonDeserialize(using = JsonDeserializer.None.class)
public class LukionOpintojaksonTunnisteVierasKieli2019 extends LukionOpintojaksonTunniste2019 {

  public LukionOpintojaksonTunnisteVierasKieli2019() {
  }
  
  public LukionOpintojaksonTunnisteVierasKieli2019(ModuuliKoodistoLOPS2021 tunniste, Laajuus laajuus, boolean pakollinen) {
    super(laajuus, pakollinen);
    this.tunniste.setValue(tunniste);
  }

  public KoodistoViite<ModuuliKoodistoLOPS2021> getTunniste() {
    return tunniste;
  }

  private final KoodistoViite<ModuuliKoodistoLOPS2021> tunniste = new KoodistoViite<>();
}
