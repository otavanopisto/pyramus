package fi.otavanopisto.pyramus.koski.koodisto;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import fi.otavanopisto.pyramus.koski.ArviointiasteikkoYleissivistavaComparator;
import fi.otavanopisto.pyramus.koski.KoodistoEnum;
import fi.otavanopisto.pyramus.koski.model.lukio.ops2019.PainotettuArvosana;

@KoodistoEnum("arviointiasteikkoyleissivistava")
public enum ArviointiasteikkoYleissivistava implements 
  Comparable<ArviointiasteikkoYleissivistava> {
  
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
  
  public static ArviointiasteikkoYleissivistava reverseLookup(String value) {
    return get(value);
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
      if (bestGrade == null || comparator.compare(grade, bestGrade) > 0) {
        bestGrade = grade;
      }
    }
    
    return bestGrade;
  }

  public static ArviointiasteikkoYleissivistava weightedMeanGrade(List<PainotettuArvosana> grades) {
    if (grades.stream().noneMatch(grade -> ArviointiasteikkoYleissivistava.isNumeric(grade.getArvosana()))) {
      if (grades.stream().anyMatch(grade -> ArviointiasteikkoYleissivistava.isLiteral(grade.getArvosana()))) {
        // Literal grade S/H
        
        if (grades.stream().anyMatch(grade -> ArviointiasteikkoYleissivistava.GRADE_S == grade.getArvosana())) {
          return ArviointiasteikkoYleissivistava.GRADE_S;
        } else {
          return ArviointiasteikkoYleissivistava.GRADE_H;
        }
      } else {
        return null;
      }
    } else {
      // Numeric grade

      int totalWeight = 0;
      int gradeSum = 0;
      
      for (PainotettuArvosana grade : grades) {
        if (ArviointiasteikkoYleissivistava.isNumeric(grade.getArvosana())) {
          gradeSum += Integer.valueOf(grade.getArvosana().toString()) * grade.getKurssinLaajuus();
          totalWeight += grade.getKurssinLaajuus();
        }
      }

      if (totalWeight > 0) {
        return ArviointiasteikkoYleissivistava.get(String.valueOf(Math.round((double) gradeSum / totalWeight)));
      } else {
        return null;
      }
    }
  }

  public static ArviointiasteikkoYleissivistava meanGrade(List<ArviointiasteikkoYleissivistava> grades) {
    if (grades.stream().anyMatch(grade -> ArviointiasteikkoYleissivistava.isNumeric(grade))) {
      // Numeric grade
      
      int gradeSum = 0;
      int gradeCount = 0;
      
      for (ArviointiasteikkoYleissivistava grade : grades) {
        if (ArviointiasteikkoYleissivistava.isNumeric(grade)) {
          gradeSum += Integer.valueOf(grade.toString());
          gradeCount++;
        }
      }

      if (gradeCount > 0) {
        return ArviointiasteikkoYleissivistava.get(String.valueOf(Math.round((double) gradeSum / gradeCount)));
      } else {
        return null;
      }
    } else {
      if (grades.stream().anyMatch(grade -> ArviointiasteikkoYleissivistava.isLiteral(grade))) {
        // Literal grade S/H
        
        if (grades.stream().anyMatch(grade -> ArviointiasteikkoYleissivistava.GRADE_S == grade)) {
          return ArviointiasteikkoYleissivistava.GRADE_S;
        } else {
          return ArviointiasteikkoYleissivistava.GRADE_H;
        }
      } else {
        return null;
      }
    }
  }

  private String value;
  private static Map<String, ArviointiasteikkoYleissivistava> lookup = new HashMap<>();

  static {
    for (ArviointiasteikkoYleissivistava v : values())
      lookup.put(v.getValue(), v);
  }
}
