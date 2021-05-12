package fi.otavanopisto.pyramus.koski.model.lukio.ops2019;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.Koulutus;
import fi.otavanopisto.pyramus.koski.koodisto.LukionOppimaara;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.Koulutusmoduuli;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipiste;

@JsonDeserialize(using = JsonDeserializer.None.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LukionOppimaaranSuoritus2019 extends LukionSuoritus2019 {
  
  public LukionOppimaaranSuoritus2019() {
  }
  
  public LukionOppimaaranSuoritus2019(LukionOppimaara oppimaara, Kieli suorituskieli, 
      OrganisaationToimipiste toimipiste) {
    super(SuorituksenTyyppi.lukionoppimaara, oppimaara, suorituskieli, toimipiste);
  }
  
  public void addOsasuoritus(LukionOsasuoritus2019 osasuoritus) {
    osasuoritukset.add(osasuoritus);
  }
  
  public Set<LukionOsasuoritus2019> getOsasuoritukset() {
    return osasuoritukset;
  }
  
  public Koulutusmoduuli getKoulutusmoduuli() {
    return koulutusmoduuli;
  }

  private final Koulutusmoduuli koulutusmoduuli = new Koulutusmoduuli(Koulutus.K309902);
  private final Set<LukionOsasuoritus2019> osasuoritukset = new HashSet<>();
}
