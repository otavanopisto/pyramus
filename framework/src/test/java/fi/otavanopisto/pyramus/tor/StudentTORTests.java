package fi.otavanopisto.pyramus.tor;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;

import fi.otavanopisto.pyramus.PyramusConsts;
import fi.otavanopisto.pyramus.tor.curriculum.TORCurriculum;

public class StudentTORTests {

  @Test
  public void testTestTORSetup() {
    StudentTOR tor = createSimpleTestTOR(true, null);

    TORSubject may = tor.findSubject("MAY");
    assertNotNull(may);
    assertNotNull(may.findCourse(1));

    TORSubject maa = tor.findSubject("MAA");
    assertNotNull(maa);
    assertNotNull(maa.findCourse(2));

    TORSubject ke = tor.findSubject("KE");
    assertNotNull(ke);
    assertNotNull(ke.findCourse(1));
    
    TORSubject ge = tor.findSubject("GE");
    assertNotNull(ge);
    assertNotNull(ge.findCourse(1));
    assertNotNull(ge.findCourse(2));
  }
  
  @Test
  public void testSimpleMean() {
    StudentTOR tor = createSimpleTestTOR(true, null);

    TORSubject may = tor.findSubject("MAY");
    assertEquals(7d, may.getArithmeticMeanGrade(), "Arithmetic mean was not calclulated correctly.");
    assertEquals(7d, may.getWeightedMeanGrade(), "Weighted mean was not calculated correctly.");

    TORSubject maa = tor.findSubject("MAA");
    assertEquals(8d, maa.getArithmeticMeanGrade(), "Arithmetic mean was not calclulated correctly.");
    assertEquals(8d, maa.getWeightedMeanGrade(), "Weighted mean was not calculated correctly.");

    TORSubject ke = tor.findSubject("KE");
    assertEquals(6d, ke.getArithmeticMeanGrade(), "Arithmetic mean was not calclulated correctly.");
    assertEquals(6d, ke.getWeightedMeanGrade(), "Weighted mean was not calculated correctly.");

    TORSubject ge = tor.findSubject("GE");
    assertEquals(7d, ge.getArithmeticMeanGrade(), "Arithmetic mean was not calclulated correctly.");
    assertEquals(7.5d, ge.getWeightedMeanGrade(), "Weighted mean was not calculated correctly.");
  }
  
  @Test
  public void testMeanWithCombinedMaths() {
    StudentTOR tor = createTestTORWithCombinedMath(true, null);

    // MAY subject should get removed as it is empty
    TORSubject may = tor.findSubject("MAY");
    assertNull(may);

    TORSubject maa = tor.findSubject("MAA");
    assertEquals(7.5d, maa.getArithmeticMeanGrade(), "Arithmetic mean was not calclulated correctly.");
    assertEquals(7.5d, maa.getWeightedMeanGrade(), "Weighted mean was not calculated correctly.");
  }
  
  @Test
  public void testWithCurriculum() throws Exception {
    TORCurriculum curriculum = StudentTORController.getCurriculum(PyramusConsts.OPS_2018);
    assertNotNull(curriculum, "Couldn't load OPS_2018");

    curriculum = StudentTORController.getCurriculum(PyramusConsts.OPS_2021);
    assertNotNull(curriculum, "Couldn't load OPS_2021");
  }
  
  /**
   * Simple TOR
   * 
   * MAY
   * - MAY1, 2op, credit 7
   * MAA
   * - MAA2, 2op, credit 8
   * KE
   * - KE1, 2op, credit 6
   * GE (avg 7, w.avg 7.5)
   * - GE1, 1op, credit 6
   * - GE2, 3op, credit 8
   * 
   * @param postProcess
   * @param curriculum
   * @return
   */
  private StudentTOR createSimpleTestTOR(boolean postProcess, TORCurriculum curriculum) {
    StudentTOR studentTOR = new StudentTOR();
    
    /* TORSubject */
    
    TORSubject subjectMAY = new TORSubject(null, "MAY", "MAY", 2L, Boolean.FALSE);
    studentTOR.addSubject(subjectMAY);
    
    TORSubject subjectMAA = new TORSubject(null, "MAA", "MAA", 2L, Boolean.FALSE);
    studentTOR.addSubject(subjectMAA);
    
    TORSubject subjectKE = new TORSubject(null, "KE", "KE", 2L, Boolean.FALSE);
    studentTOR.addSubject(subjectKE);

    TORSubject subjectGE = new TORSubject(null, "GE", "GE", 2L, Boolean.FALSE);
    studentTOR.addSubject(subjectGE);
    
    /* TORCourse */
    
    TORCourse may1 = new TORCourse(subjectMAY, 1, true, 2d, TORCourseLengthUnit.op);
    subjectMAY.addCourse(may1);

    TORCourse maa2 = new TORCourse(subjectMAA, 2, true, 2d, TORCourseLengthUnit.op);
    subjectMAA.addCourse(maa2);

    TORCourse ke1 = new TORCourse(subjectKE, 1, true, 2d, TORCourseLengthUnit.op);
    subjectKE.addCourse(ke1);
    
    TORCourse ge1 = new TORCourse(subjectGE, 1, true, 1d, TORCourseLengthUnit.op);
    subjectGE.addCourse(ge1);
    TORCourse ge2 = new TORCourse(subjectGE, 2, true, 3d, TORCourseLengthUnit.op);
    subjectGE.addCourse(ge2);
    
    /* TORCredit */
    
    TORCredit may1Credit = new TORCredit(null, null, 7d, new Date(), TORCreditType.COURSEASSESSMENT, true);
    may1.addCredit(may1Credit);
    
    TORCredit maa2Credit = new TORCredit(null, null, 8d, new Date(), TORCreditType.COURSEASSESSMENT, true);
    maa2.addCredit(maa2Credit);

    TORCredit ke1Credit = new TORCredit(null, null, 6d, new Date(), TORCreditType.COURSEASSESSMENT, true);
    ke1.addCredit(ke1Credit);
    
    TORCredit ge1Credit = new TORCredit(null, null, 6d, new Date(), TORCreditType.COURSEASSESSMENT, true);
    ge1.addCredit(ge1Credit);
    
    TORCredit ge2Credit = new TORCredit(null, null, 8d, new Date(), TORCreditType.COURSEASSESSMENT, true);
    ge2.addCredit(ge2Credit);
    
    if (postProcess) {
      studentTOR.postProcess(curriculum);
    }
    
    return studentTOR;
  }
  
  /**
   * TOR with combined math
   * 
   * MAA
   * - MAY1, 2op, credit 7
   * - MAA2, 2op, credit 8
   * 
   * @param postProcess
   * @param curriculum
   * @return
   */
  private StudentTOR createTestTORWithCombinedMath(boolean postProcess, TORCurriculum curriculum) {
    StudentTOR studentTOR = new StudentTOR();
    
    /* TORSubject */
    
    TORSubject subjectMAY = new TORSubject(1L, "MAY", "MAY", 2L, Boolean.FALSE);
    studentTOR.addSubject(subjectMAY);
    
    TORSubject subjectMAA = new TORSubject(2L, "MAA", "MAA", 2L, Boolean.FALSE);
    studentTOR.addSubject(subjectMAA);
    
    /* TORCourse */
    
    TORCourse may1 = new TORCourse(subjectMAY, 1, true, 2d, TORCourseLengthUnit.op);
    subjectMAA.addCourse(may1); // Added under MAA

    TORCourse maa2 = new TORCourse(subjectMAA, 2, true, 2d, TORCourseLengthUnit.op);
    subjectMAA.addCourse(maa2);

    /* TORCredit */
    
    TORCredit may1Credit = new TORCredit(null, null, 7d, new Date(), TORCreditType.COURSEASSESSMENT, true);
    may1.addCredit(may1Credit);
    
    TORCredit maa2Credit = new TORCredit(null, null, 8d, new Date(), TORCreditType.COURSEASSESSMENT, true);
    maa2.addCredit(maa2Credit);

    if (postProcess) {
      studentTOR.postProcess(curriculum);
    }
    
    return studentTOR;
  }
  
}
