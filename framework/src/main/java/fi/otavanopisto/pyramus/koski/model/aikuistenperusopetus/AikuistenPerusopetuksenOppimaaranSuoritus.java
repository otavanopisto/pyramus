package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.Koulutus;
import fi.otavanopisto.pyramus.koski.koodisto.PerusopetuksenSuoritusTapa;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.Koulutusmoduuli;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipiste;

@JsonDeserialize(using = JsonDeserializer.None.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AikuistenPerusopetuksenOppimaaranSuoritus extends AikuistenPerusopetuksenSuoritus {
  
  public AikuistenPerusopetuksenOppimaaranSuoritus() {
  }
  
  public AikuistenPerusopetuksenOppimaaranSuoritus(PerusopetuksenSuoritusTapa suoritustapa, 
      Kieli suorituskieli, OrganisaationToimipiste toimipiste) {
    super(suoritustapa, SuorituksenTyyppi.aikuistenperusopetuksenoppimaara, suorituskieli, toimipiste);
  }
  
  public void addOsasuoritus(AikuistenPerusopetuksenOsasuoritus osasuoritus) {
    osasuoritukset.add(osasuoritus);
  }
  
  public Set<AikuistenPerusopetuksenOsasuoritus> getOsasuoritukset() {
    return osasuoritukset;
  }
  
  public Koulutusmoduuli getKoulutusmoduuli() {
    return koulutusmoduuli;
  }
  
  private final Koulutusmoduuli koulutusmoduuli = new Koulutusmoduuli(Koulutus.K201101);
  private final Set<AikuistenPerusopetuksenOsasuoritus> osasuoritukset = new HashSet<>();
}
