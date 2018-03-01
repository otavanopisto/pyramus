package fi.otavanopisto.pyramus.koski.koodisto;

import java.util.HashMap;
import java.util.Map;

import fi.otavanopisto.pyramus.koski.KoodistoEnum;

@KoodistoEnum("koulutus")
public enum Koulutus {
  /* Incomplete list */

  K201101 ("201101"),
  K309902 ("309902");
  
  Koulutus(String value) {
    this.value = value;
  }
  
  @Override
  public String toString() {
    return value;
  }
  
  public String getValue() {
    return value;
  }
  
  public static Koulutus reverseLookup(String value) {
    try {
      return lookup.get(value);
    } catch (Exception ex) {
    }
    return null;
  }
  
  private String value;
  private static Map<String, Koulutus> lookup = new HashMap<>();

  static {
    for (Koulutus v : values()) {
      lookup.put(v.getValue(), v);
    }
  }
  
}
