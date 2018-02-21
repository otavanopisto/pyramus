package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.PerusopetuksenSuoritusTapa;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.KurssinArviointi;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipiste;

@JsonDeserialize(using = JsonDeserializer.None.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PerusopetuksenOppiaineenOppimaaranSuoritus extends AikuistenPerusopetuksenSuoritus {
  
  public PerusopetuksenOppiaineenOppimaaranSuoritus() {
  }
  
  public PerusopetuksenOppiaineenOppimaaranSuoritus(AikuistenPerusopetuksenOppiaineenTunniste koulutusmoduuli,
      PerusopetuksenSuoritusTapa suoritustapa, Kieli suorituskieli, OrganisaationToimipiste toimipiste) {
    super(suoritustapa, SuorituksenTyyppi.perusopetuksenoppiaineenoppimaara, suorituskieli, toimipiste);
    this.koulutusmoduuli = koulutusmoduuli;
  }
  
  public static PerusopetuksenOppiaineenOppimaaranSuoritus from(AikuistenPerusopetuksenOppiaineenSuoritus oppiaine, 
      PerusopetuksenSuoritusTapa suoritusTapa, Kieli kieli, OrganisaationToimipiste toimipiste) {
    PerusopetuksenOppiaineenOppimaaranSuoritus suoritus = new PerusopetuksenOppiaineenOppimaaranSuoritus(
        oppiaine.getKoulutusmoduuli(), suoritusTapa, kieli, toimipiste);
    suoritus.getArviointi().addAll(oppiaine.getArviointi());
    suoritus.getOsasuoritukset().addAll(oppiaine.getOsasuoritukset());
    return suoritus;
  }
  
  public void addOsasuoritus(AikuistenPerusopetuksenKurssinSuoritus lukionKurssinSuoritus) {
    osasuoritukset.add(lukionKurssinSuoritus);
  }
  
  public List<AikuistenPerusopetuksenKurssinSuoritus> getOsasuoritukset() {
    return osasuoritukset;
  }
  
  public AikuistenPerusopetuksenOppiaineenTunniste getKoulutusmoduuli() {
    return koulutusmoduuli;
  }
 
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public List<KurssinArviointi> getArviointi() {
    return arviointi;
  }
  
  public void setKoulutusmoduuli(AikuistenPerusopetuksenOppiaineenTunniste koulutusmoduuli) {
    this.koulutusmoduuli = koulutusmoduuli;
  }

  private AikuistenPerusopetuksenOppiaineenTunniste koulutusmoduuli;
  private final List<KurssinArviointi> arviointi = new ArrayList<>();
  private final List<AikuistenPerusopetuksenKurssinSuoritus> osasuoritukset = new ArrayList<>();
}
