package fi.otavanopisto.pyramus.koski.settings;

import fi.otavanopisto.pyramus.koski.koodisto.OpiskeluoikeudenTila;

public class StudyEndReasonMapping {

  public OpiskeluoikeudenTila getOpiskeluoikeudenTila() {
    return opiskeluoikeudenTila;
  }
  
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

  private OpiskeluoikeudenTila opiskeluoikeudenTila;
  private boolean sisällytäVahvistaja = false;
  private boolean laskeAinekeskiarvot = false;
}
