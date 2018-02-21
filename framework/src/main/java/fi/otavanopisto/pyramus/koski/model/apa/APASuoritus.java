package fi.otavanopisto.pyramus.koski.model.apa;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.PerusopetuksenSuoritusTapa;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipiste;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenSuoritus;

@JsonDeserialize(using = JsonDeserializer.None.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APASuoritus extends AikuistenPerusopetuksenSuoritus {
  
  public APASuoritus() {
  }
  
  public APASuoritus(PerusopetuksenSuoritusTapa suoritustapa, 
      Kieli suorituskieli, OrganisaationToimipiste toimipiste) {
    super(suoritustapa, SuorituksenTyyppi.aikuistenperusopetuksenoppimaaranalkuvaihe, suorituskieli, toimipiste);
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
