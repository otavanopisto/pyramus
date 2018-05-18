package fi.otavanopisto.pyramus.koski;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;

public class KoskiCommonTests {

  @Test
  public void testMeanGrade() {
    List<ArviointiasteikkoYleissivistava> grades = new ArrayList<>();
    
    grades.add(ArviointiasteikkoYleissivistava.GRADE_6);
    grades.add(ArviointiasteikkoYleissivistava.GRADE_6);
    grades.add(ArviointiasteikkoYleissivistava.GRADE_9);
    grades.add(ArviointiasteikkoYleissivistava.GRADE_6);
    grades.add(ArviointiasteikkoYleissivistava.GRADE_6);
    grades.add(ArviointiasteikkoYleissivistava.GRADE_6);
    
    ArviointiasteikkoYleissivistava meanGrade = ArviointiasteikkoYleissivistava.meanGrade(grades);
    ArviointiasteikkoYleissivistava expected = ArviointiasteikkoYleissivistava.GRADE_7;
    
    assertTrue(String.format("Expected Mean Grade was %s, expected %s", meanGrade, expected), meanGrade == expected);
  }

  @Test
  public void testMeanGrade2() {
    List<ArviointiasteikkoYleissivistava> grades = new ArrayList<>();
    
    grades.add(ArviointiasteikkoYleissivistava.GRADE_S);
    
    ArviointiasteikkoYleissivistava meanGrade = ArviointiasteikkoYleissivistava.meanGrade(grades);
    ArviointiasteikkoYleissivistava expected = ArviointiasteikkoYleissivistava.GRADE_S;
    
    assertTrue(String.format("Expected Mean Grade was %s, expected %s", meanGrade, expected), meanGrade == expected);
  }
  
  @Test
  public void testMeanGrade3() {
    List<ArviointiasteikkoYleissivistava> grades = new ArrayList<>();
    
    grades.add(ArviointiasteikkoYleissivistava.GRADE_5);
    
    ArviointiasteikkoYleissivistava meanGrade = ArviointiasteikkoYleissivistava.meanGrade(grades);
    ArviointiasteikkoYleissivistava expected = ArviointiasteikkoYleissivistava.GRADE_5;
    
    assertTrue(String.format("Expected Mean Grade was %s, expected %s", meanGrade, expected), meanGrade == expected);
  }
  
}
