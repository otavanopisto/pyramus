package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.PerusopetuksenSuoritusTapa;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTila;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointi;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipiste;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PerusopetuksenOppiaineenOppimaaranSuoritus extends AikuistenPerusopetuksenSuoritus {
  
  public PerusopetuksenOppiaineenOppimaaranSuoritus(PerusopetuksenSuoritusTapa suoritustapa, 
      Kieli suorituskieli, OrganisaationToimipiste toimipiste, SuorituksenTila tila,
      AikuistenPerusopetuksenOppiaineenSuoritus oppiaine) {
    super(suoritustapa, tila, SuorituksenTyyppi.perusopetuksenoppiaineenoppimaara, suorituskieli, toimipiste);
    this.oppiaine = oppiaine;
  }
  
  public List<AikuistenPerusopetuksenKurssinSuoritus> getOsasuoritukset() {
    return oppiaine.getOsasuoritukset();
  }
  
  public AikuistenPerusopetuksenOppiaineenTunniste getKoulutusmoduuli() {
    return oppiaine.getKoulutusmoduuli();
  }
 
  public List<KurssinArviointi> getArviointi() {
    return oppiaine.getArviointi();
  }
  
  private AikuistenPerusopetuksenOppiaineenSuoritus oppiaine;
}
