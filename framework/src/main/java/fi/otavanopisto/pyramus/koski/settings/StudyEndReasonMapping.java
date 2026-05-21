package fi.otavanopisto.pyramus.koski.settings;

import fi.otavanopisto.pyramus.koski.koodisto.OpiskeluoikeudenTila;

public class StudyEndReasonMapping {

  /**
   * To be replaced by StudentStudyEndReason.type
   * 
   * @deprecated
   * @return
   */
  @Deprecated(forRemoval = true)
  public OpiskeluoikeudenTila getOpiskeluoikeudenTila() {
    return opiskeluoikeudenTila;
  }
  
  /**
   * To be replaced by StudentStudyEndReason.type
   * 
   * @deprecated
   */
  @Deprecated(forRemoval = true)
  public void setOpiskeluoikeudenTila(OpiskeluoikeudenTila opiskeluoikeudenTila) {
    this.opiskeluoikeudenTila = opiskeluoikeudenTila;
  }
  
  public boolean getSisällytäVahvistaja() {
    return sisällytäVahvistaja;
  }
  
  public void setSisällytäVahvistaja(boolean sisällytäVahvistaja) {
    this.sisällytäVahvistaja = sisällytäVahvistaja;
  }

  public boolean getLaskeAinekeskiarvot() {
    return laskeAinekeskiarvot;
  }

  public void setLaskeAinekeskiarvot(boolean laskeAinekeskiarvot) {
    this.laskeAinekeskiarvot = laskeAinekeskiarvot;
  }

  public boolean isLukionOppimääräSuoritettu() {
    return lukionOppimääräSuoritettu;
  }

  public void setLukionOppimääräSuoritettu(boolean lukionOppimääräSuoritettu) {
    this.lukionOppimääräSuoritettu = lukionOppimääräSuoritettu;
  }

  private OpiskeluoikeudenTila opiskeluoikeudenTila;
  private boolean sisällytäVahvistaja = false;
  private boolean laskeAinekeskiarvot = false;
  private boolean lukionOppimääräSuoritettu = false;
}
