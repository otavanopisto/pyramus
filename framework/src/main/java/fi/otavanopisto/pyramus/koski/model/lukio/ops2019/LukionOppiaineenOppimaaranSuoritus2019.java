package fi.otavanopisto.pyramus.koski.model.lukio.ops2019;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.LukionOppimaara;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipiste;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class LukionOppiaineenOppimaaranSuoritus2019 extends LukionSuoritus2019 {

  public LukionOppiaineenOppimaaranSuoritus2019() {
  }
  
  public LukionOppiaineenOppimaaranSuoritus2019(LukionOppimaara oppimaara, Kieli suorituskieli, OrganisaationToimipiste toimipiste, String perusteenDiaarinumero) {
    super(SuorituksenTyyppi.lukionaineopinnot, oppimaara, suorituskieli, toimipiste);
    this.koulutusmoduuli.setPerusteenDiaarinumero(perusteenDiaarinumero);
  }
  
  public void addOsasuoritus(LukionOppiaineenSuoritus2019 osasuoritus) {
    osasuoritukset.add(osasuoritus);
  }
  
  public List<LukionOppiaineenSuoritus2019> getOsasuoritukset() {
    return osasuoritukset;
  }

  public LukionOppiaineenOppimaaranKoulutusmoduuli2019 getKoulutusmoduuli() {
    return koulutusmoduuli;
  }

  private final LukionOppiaineenOppimaaranKoulutusmoduuli2019 koulutusmoduuli = new LukionOppiaineenOppimaaranKoulutusmoduuli2019();
  private final List<LukionOppiaineenSuoritus2019> osasuoritukset = new ArrayList<>();
}
