package fi.otavanopisto.pyramus.koski.koodisto;

import java.util.HashMap;
import java.util.Map;

import fi.otavanopisto.pyramus.koski.KoodistoEnum;

@KoodistoEnum("opintojenlaajuusyksikko")
public enum OpintojenLaajuusYksikko {

  ov ("1"),
  op ("2"),
  vuosiviikkotuntia ("3"),
  kurssia ("4"),
  tuntia ("5"),
  osp ("6"),
  vuotta ("7");
  
  OpintojenLaajuusYksikko(String value) {
    this.value = value;
  }
  
  @Override
  public String toString() {
    return value;
  }
  
  public String getValue() {
    return value;
  }
  
  public static OpintojenLaajuusYksikko get(String value) {
    return lookup.get(value);
  }
  
  public static OpintojenLaajuusYksikko reverseLookup(String value) {
    return get(value);
  }
  
  private String value;
  private static Map<String, OpintojenLaajuusYksikko> lookup = new HashMap<>();

  static {
    for (OpintojenLaajuusYksikko v : values()) {
      lookup.put(v.getValue(), v);
    }
  }
}
