package fi.otavanopisto.pyramus.koski.model.apa;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.PerusopetuksenSuoritusTapa;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTila;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipiste;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenSuoritus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class APASuoritus extends AikuistenPerusopetuksenSuoritus {
  
  public APASuoritus(PerusopetuksenSuoritusTapa suoritustapa, 
      Kieli suorituskieli, OrganisaationToimipiste toimipiste, SuorituksenTila tila) {
    super(suoritustapa, tila, SuorituksenTyyppi.aikuistenperusopetuksenoppimaaranalkuvaihe, suorituskieli, toimipiste);
  }
  
  public void addOsasuoritus(APAOppiaineenSuoritus osasuoritus) {
    osasuoritukset.add(osasuoritus);
  }
  
  public Set<APAOppiaineenSuoritus> getOsasuoritukset() {
    return osasuoritukset;
  }
  
  public APAKoulutusmoduuli getKoulutusmoduuli() {
    return koulutusmoduuli;
  }
  
  private final APAKoulutusmoduuli koulutusmoduuli = new APAKoulutusmoduuli(SuorituksenTyyppi.aikuistenperusopetuksenoppimaaranalkuvaihe);
  private final Set<APAOppiaineenSuoritus> osasuoritukset = new HashSet<>();
}
