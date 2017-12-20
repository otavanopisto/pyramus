package fi.otavanopisto.pyramus.koski.koodisto;

import fi.otavanopisto.pyramus.koski.KoodistoEnum;

@KoodistoEnum("koskiopiskeluoikeudentila")
public enum OpiskeluoikeudenTila {

  eronnut,
  katsotaaneronneeksi,
  lasna,
  loma,
  mitatoity,
  peruutettu,
  valiaikaisestikeskeytynyt,
  valmistunut;
  
  public static final OpiskeluoikeudenTila[] QUIT_STATES = { eronnut, katsotaaneronneeksi, peruutettu };
  public static final OpiskeluoikeudenTila[] GRADUATED_STATES = { valmistunut };
}
