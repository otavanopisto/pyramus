package fi.otavanopisto.pyramus.koski.model.lukio.ops2019;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.ModuuliKoodistoLOPS2021;
import fi.otavanopisto.pyramus.koski.model.Laajuus;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class LukionOpintojaksonTunnisteMuuModuuli2019 extends LukionOpintojaksonTunniste2019 {

  public LukionOpintojaksonTunnisteMuuModuuli2019() {
  }
  
  public LukionOpintojaksonTunnisteMuuModuuli2019(ModuuliKoodistoLOPS2021 tunniste, 
      Laajuus laajuus, boolean pakollinen) {
    super(laajuus, pakollinen);
    this.tunniste.setValue(tunniste);
  }

  public KoodistoViite<ModuuliKoodistoLOPS2021> getTunniste() {
    return tunniste;
  }

  private final KoodistoViite<ModuuliKoodistoLOPS2021> tunniste = new KoodistoViite<>();
}
