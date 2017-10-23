package fi.otavanopisto.pyramus.koski.koodisto;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import fi.otavanopisto.pyramus.koski.ArviointiasteikkoYleissivistavaComparator;
import fi.otavanopisto.pyramus.koski.KoodistoEnum;

@KoodistoEnum("arviointiasteikkoyleissivistava")
public enum ArviointiasteikkoYleissivistava implements Comparable<ArviointiasteikkoYleissivistava> {
  
  GRADE_4 ("4"),   // hylätty
  GRADE_5 ("5"),   // välttävä
  GRADE_6 ("6"),   // kohtalainen
  GRADE_7 ("7"),   // tyydyttävä
  GRADE_8 ("8"),   // hyvä
  GRADE_9 ("9"),   // kiitettävä
  GRADE_10 ("10"), // erinomainen
  GRADE_H ("H"),   // hylätty
  GRADE_S ("S");    // hyväksytty
  
  public static final ArviointiasteikkoYleissivistava[] NUMERIC = { GRADE_4, GRADE_5, GRADE_6, GRADE_7, GRADE_8, GRADE_9, GRADE_10  };
  public static final ArviointiasteikkoYleissivistava[] LITERAL = { GRADE_H, GRADE_S };
  
  ArviointiasteikkoYleissivistava(String value) {
    this.value = value;
  }

  public static ArviointiasteikkoYleissivistava get(String value) {
    return lookup.get(value);
  }
  
  @Override
  public String toString() {
    return value;
  }
  
  public String getValue() {
    return value;
  }
  
  public static boolean isNumeric(ArviointiasteikkoYleissivistava arvosana) {
    return ArrayUtils.contains(NUMERIC, arvosana);
  }
  
  public static boolean isLiteral(ArviointiasteikkoYleissivistava arvosana) {
    return ArrayUtils.contains(LITERAL, arvosana);
  }
  
  public static ArviointiasteikkoYleissivistava bestGrade(Collection<ArviointiasteikkoYleissivistava> collection) {
    ArviointiasteikkoYleissivistavaComparator comparator = new ArviointiasteikkoYleissivistavaComparator();
    ArviointiasteikkoYleissivistava bestGrade = null;
    
    for (ArviointiasteikkoYleissivistava grade : collection) {
      if (bestGrade == null || comparator.compare(grade, bestGrade) > 0)
        bestGrade = grade;
    }
    
    return bestGrade;
  }
  
  private String value;
  private static Map<String, ArviointiasteikkoYleissivistava> lookup = new HashMap<>();

  static {
    for (ArviointiasteikkoYleissivistava v : values())
      lookup.put(v.getValue(), v);
  }
}
