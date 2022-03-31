package fi.otavanopisto.pyramus.koski;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;
import fi.otavanopisto.pyramus.koski.model.lukio.ops2019.PainotettuArvosana;

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
  
  @Test
  public void testWeightedMeanGrade() {
    List<PainotettuArvosana> grades = new ArrayList<>();
    
    grades.add(new PainotettuArvosana(3, ArviointiasteikkoYleissivistava.GRADE_6));
    grades.add(new PainotettuArvosana(3, ArviointiasteikkoYleissivistava.GRADE_6));
    grades.add(new PainotettuArvosana(3, ArviointiasteikkoYleissivistava.GRADE_9));
    grades.add(new PainotettuArvosana(3, ArviointiasteikkoYleissivistava.GRADE_6));
    grades.add(new PainotettuArvosana(3, ArviointiasteikkoYleissivistava.GRADE_6));
    grades.add(new PainotettuArvosana(3, ArviointiasteikkoYleissivistava.GRADE_6));
    
    ArviointiasteikkoYleissivistava meanGrade = ArviointiasteikkoYleissivistava.weightedMeanGrade(grades);
    ArviointiasteikkoYleissivistava expected = ArviointiasteikkoYleissivistava.GRADE_7;
    
    assertTrue(String.format("Expected Mean Grade was %s, expected %s", meanGrade, expected), meanGrade == expected);
  }

  @Test
  public void testWeightedMeanGrade2() {
    List<PainotettuArvosana> grades = new ArrayList<>();
    
    grades.add(new PainotettuArvosana(3, ArviointiasteikkoYleissivistava.GRADE_H));
    grades.add(new PainotettuArvosana(3, ArviointiasteikkoYleissivistava.GRADE_S));
    
    ArviointiasteikkoYleissivistava meanGrade = ArviointiasteikkoYleissivistava.weightedMeanGrade(grades);
    ArviointiasteikkoYleissivistava expected = ArviointiasteikkoYleissivistava.GRADE_S;
    
    assertTrue(String.format("Expected Mean Grade was %s, expected %s", meanGrade, expected), meanGrade == expected);
  }

  @Test
  public void testWeightedMeanGrade3() {
    List<PainotettuArvosana> grades = new ArrayList<>();
    
    grades.add(new PainotettuArvosana(3, ArviointiasteikkoYleissivistava.GRADE_H));
    grades.add(new PainotettuArvosana(3, ArviointiasteikkoYleissivistava.GRADE_S));
    grades.add(new PainotettuArvosana(3, ArviointiasteikkoYleissivistava.GRADE_6));
    
    ArviointiasteikkoYleissivistava meanGrade = ArviointiasteikkoYleissivistava.weightedMeanGrade(grades);
    ArviointiasteikkoYleissivistava expected = ArviointiasteikkoYleissivistava.GRADE_6;
    
    assertTrue(String.format("Expected Mean Grade was %s, expected %s", meanGrade, expected), meanGrade == expected);
  }

  @Test
  public void testWeightedMeanGrade4RoundUp() {
    List<PainotettuArvosana> grades = new ArrayList<>();
    
    grades.add(new PainotettuArvosana(9, ArviointiasteikkoYleissivistava.GRADE_5));
    grades.add(new PainotettuArvosana(1, ArviointiasteikkoYleissivistava.GRADE_10));
    
    ArviointiasteikkoYleissivistava meanGrade = ArviointiasteikkoYleissivistava.weightedMeanGrade(grades);
    ArviointiasteikkoYleissivistava expected = ArviointiasteikkoYleissivistava.GRADE_6;
    
    assertTrue(String.format("Expected Mean Grade was %s, expected %s", meanGrade, expected), meanGrade == expected);
  }

  @Test
  public void testWeightedMeanGrade5RoundDown() {
    List<PainotettuArvosana> grades = new ArrayList<>();
    
    grades.add(new PainotettuArvosana(11, ArviointiasteikkoYleissivistava.GRADE_5));
    grades.add(new PainotettuArvosana(1, ArviointiasteikkoYleissivistava.GRADE_10));
    
    ArviointiasteikkoYleissivistava meanGrade = ArviointiasteikkoYleissivistava.weightedMeanGrade(grades);
    ArviointiasteikkoYleissivistava expected = ArviointiasteikkoYleissivistava.GRADE_5;
    
    assertTrue(String.format("Expected Mean Grade was %s, expected %s", meanGrade, expected), meanGrade == expected);
  }

  @Test
  public void testWeightedMeanGrade6EmptyList() {
    List<PainotettuArvosana> grades = new ArrayList<>();
    
    ArviointiasteikkoYleissivistava meanGrade = ArviointiasteikkoYleissivistava.weightedMeanGrade(grades);
    ArviointiasteikkoYleissivistava expected = null;
    
    assertTrue(String.format("Expected Mean Grade was %s, expected %s", meanGrade, expected), meanGrade == expected);
  }

  
  
}
