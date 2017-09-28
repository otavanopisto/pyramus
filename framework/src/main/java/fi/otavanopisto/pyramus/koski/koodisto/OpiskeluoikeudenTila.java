package fi.otavanopisto.pyramus.koski.koodisto;

import fi.otavanopisto.pyramus.koski.KoodistoEnum;

@KoodistoEnum("koskiopiskeluoikeudentila")
public enum OpiskeluoikeudenTila {

  lasna,
  eronnut,
  erotettu,
  katsotaaneronneeksi,
  peruutettu,
  valiaikaisestikeskeytynyt,
  valmistunut;
  
  public static final OpiskeluoikeudenTila[] QUIT_STATES = { eronnut, erotettu, katsotaaneronneeksi, peruutettu };
  public static final OpiskeluoikeudenTila[] GRADUATED_STATES = { valmistunut };
}
