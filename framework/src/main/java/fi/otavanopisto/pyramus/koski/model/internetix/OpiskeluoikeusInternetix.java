package fi.otavanopisto.pyramus.koski.model.internetix;

import fi.otavanopisto.pyramus.koski.model.Opiskeluoikeus;

public class OpiskeluoikeusInternetix {

  public OpiskeluoikeusInternetix(Opiskeluoikeus opiskeluoikeus, boolean eiSuorituksia) {
    this.opiskeluoikeus = opiskeluoikeus;
    this.eiSuorituksia = eiSuorituksia;
  }
  
  public Opiskeluoikeus getOpiskeluoikeus() {
    return opiskeluoikeus;
  }

  public boolean isEiSuorituksia() {
    return eiSuorituksia;
  }

  private final Opiskeluoikeus opiskeluoikeus;
  private final boolean eiSuorituksia;
}
