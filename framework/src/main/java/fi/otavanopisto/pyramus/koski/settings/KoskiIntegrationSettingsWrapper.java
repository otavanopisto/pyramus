package fi.otavanopisto.pyramus.koski.settings;

public class KoskiIntegrationSettingsWrapper {

  public KoskiIntegrationSettings getKoski() {
    return koski;
  }

  public void setKoski(KoskiIntegrationSettings koski) {
    this.koski = koski;
  }

  private KoskiIntegrationSettings koski = new KoskiIntegrationSettings();
}
