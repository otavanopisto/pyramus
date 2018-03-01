package fi.otavanopisto.pyramus.koski.model.lukio;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipiste;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(using = JsonDeserializer.None.class)
public class LukionOppiaineenOppimaaranSuoritus extends LukionSuoritus {
  
  public LukionOppiaineenOppimaaranSuoritus() {
  }
  
  public LukionOppiaineenOppimaaranSuoritus(LukionOppiaineenTunniste koulutusmoduuli, Kieli suorituskieli, 
      OrganisaationToimipiste toimipiste) {
    super(SuorituksenTyyppi.lukionoppiaineenoppimaara, suorituskieli, toimipiste);
    this.koulutusmoduuli = koulutusmoduuli;
  }
  
  public static LukionOppiaineenOppimaaranSuoritus from(LukionOppiaineenSuoritus oppiaine, Kieli suorituskieli, 
      OrganisaationToimipiste toimipiste) {
    LukionOppiaineenOppimaaranSuoritus suoritus = new LukionOppiaineenOppimaaranSuoritus(
        oppiaine.getKoulutusmoduuli(), suorituskieli, toimipiste);
    suoritus.getArviointi().addAll(oppiaine.getArviointi());
    suoritus.getOsasuoritukset().addAll(oppiaine.getOsasuoritukset());
    return suoritus;
  }
  
  public LukionOppiaineenTunniste getKoulutusmoduuli() {
    return koulutusmoduuli;
  }
  
  public void setKoulutusmoduuli(LukionOppiaineenTunniste koulutusmoduuli) {
    this.koulutusmoduuli = koulutusmoduuli;
  }

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public List<LukionOppiaineenArviointi> getArviointi() {
    return arviointi;
  }
  
  public void addOsasuoritus(LukionKurssinSuoritus osasuoritus) {
    osasuoritukset.add(osasuoritus);
  }
  
  public List<LukionKurssinSuoritus> getOsasuoritukset() {
    return osasuoritukset;
  }
  
  private LukionOppiaineenTunniste koulutusmoduuli;
  private final List<LukionOppiaineenArviointi> arviointi = new ArrayList<>();
  private final List<LukionKurssinSuoritus> osasuoritukset = new ArrayList<>();
}
