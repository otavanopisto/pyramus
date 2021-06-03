package fi.otavanopisto.pyramus.koski.model.lukio.ops2019;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
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
  
  @JsonProperty("lukionOppimääräSuoritettu")
  public boolean isLukionOppimaaraSuoritettu() {
    return lukionOppimaaraSuoritettu;
  }

  public void setLukionOppimaaraSuoritettu(boolean lukionOppimaaraSuoritettu) {
    this.lukionOppimaaraSuoritettu = lukionOppimaaraSuoritettu;
  }

  public void addOsasuoritus(LukionOppiaineenSuoritus2019 osasuoritus) {
    osasuoritukset.add(osasuoritus);
  }
  
  public Set<LukionOppiaineenSuoritus2019> getOsasuoritukset() {
    return osasuoritukset;
  }

  public LukionOppiaineenOppimaaranKoulutusmoduuli2019 getKoulutusmoduuli() {
    return koulutusmoduuli;
  }

  private final LukionOppiaineenOppimaaranKoulutusmoduuli2019 koulutusmoduuli = new LukionOppiaineenOppimaaranKoulutusmoduuli2019();
  private boolean lukionOppimaaraSuoritettu;
  private final Set<LukionOppiaineenSuoritus2019> osasuoritukset = new HashSet<>();
}
