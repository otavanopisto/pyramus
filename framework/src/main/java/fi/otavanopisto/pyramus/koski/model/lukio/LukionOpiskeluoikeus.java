package fi.otavanopisto.pyramus.koski.model.lukio;

import java.util.HashSet;
import java.util.Set;

import fi.otavanopisto.pyramus.koski.koodisto.OpiskeluoikeudenTyyppi;
import fi.otavanopisto.pyramus.koski.model.OpiskeluoikeusTila;
import fi.otavanopisto.pyramus.koski.model.Opiskeluoikeus;

public class LukionOpiskeluoikeus extends Opiskeluoikeus {

  public LukionOpiskeluoikeus() {
    super(OpiskeluoikeudenTyyppi.lukiokoulutus);
  }

  public OpiskeluoikeusTila getTila() {
    return tila;
  }

  public void addSuoritus(LukionSuoritus suoritus) {
    suoritukset.add(suoritus);
  }
  
  public Set<LukionSuoritus> getSuoritukset() {
    return suoritukset;
  }

  private final OpiskeluoikeusTila tila = new OpiskeluoikeusTila();
  private final Set<LukionSuoritus> suoritukset = new HashSet<>();
}
