package fi.otavanopisto.pyramus.koski.model.lukio.aine;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTila;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointi;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipiste;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionKurssinSuoritus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenSuoritus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppiaineenTunniste;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionSuoritus;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LukionOppiaineenOppimaaranSuoritus extends LukionSuoritus {
  
  public LukionOppiaineenOppimaaranSuoritus(Kieli suorituskieli, OrganisaationToimipiste toimipiste, 
      SuorituksenTila tila, LukionOppiaineenSuoritus oppiaine) {
    super(tila, SuorituksenTyyppi.lukionoppiaineenoppimaara, suorituskieli, toimipiste);
    this.oppiaine = oppiaine;
  }
  
  public LukionOppiaineenTunniste getKoulutusmoduuli() {
    return oppiaine.getKoulutusmoduuli();
  }
  
  public Set<KurssinArviointi> getArviointi() {
    return oppiaine.getArviointi();
  }
  
  public Set<LukionKurssinSuoritus> getOsasuoritukset() {
    return oppiaine.getOsasuoritukset();
  }
  
  private LukionOppiaineenSuoritus oppiaine;
}
