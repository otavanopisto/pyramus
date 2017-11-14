package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import java.util.HashSet;
import java.util.Set;

import fi.otavanopisto.pyramus.koski.koodisto.OpiskeluoikeudenTyyppi;
import fi.otavanopisto.pyramus.koski.model.Opiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.OpiskeluoikeusTila;

public class AikuistenPerusopetuksenOpiskeluoikeus extends Opiskeluoikeus {

  public AikuistenPerusopetuksenOpiskeluoikeus() {
    super(OpiskeluoikeudenTyyppi.aikuistenperusopetus);
  }

  public OpiskeluoikeusTila getTila() {
    return tila;
  }
  
  public void addSuoritus(AikuistenPerusopetuksenSuoritus suoritus) {
    suoritukset.add(suoritus);
  }
  
  public Set<AikuistenPerusopetuksenSuoritus> getSuoritukset() {
    return suoritukset;
  }

  private final OpiskeluoikeusTila tila = new OpiskeluoikeusTila();
  private final Set<AikuistenPerusopetuksenSuoritus> suoritukset = new HashSet<>();
}