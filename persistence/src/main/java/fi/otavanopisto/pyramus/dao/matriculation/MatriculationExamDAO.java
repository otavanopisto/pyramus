package fi.otavanopisto.pyramus.dao.matriculation;

import java.util.Date;

import javax.ejb.Stateless;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExam;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamTerm;

@Stateless
public class MatriculationExamDAO extends PyramusEntityDAO<MatriculationExam> {

  public MatriculationExam create(
      Date starts,
      Date ends, 
      Date confirmationDate,
      Integer examYear,
      MatriculationExamTerm examTerm, 
      boolean enrollmentActive
  ) {
    MatriculationExam exam = new MatriculationExam();
    exam.setStarts(starts);
    exam.setEnds(ends);
    exam.setConfirmationDate(confirmationDate);
    exam.setExamYear(examYear);
    exam.setExamTerm(examTerm);
    exam.setEnrollmentActive(enrollmentActive);
    return persist(exam);
  }
  
  
  public MatriculationExam update(
      MatriculationExam exam,
      Date starts,
      Date ends, 
      Date confirmationDate,
      Integer examYear,
      MatriculationExamTerm examTerm,
      boolean enrollmentActive
  ) {
    exam.setStarts(starts);
    exam.setEnds(ends);
    exam.setConfirmationDate(confirmationDate);
    exam.setExamYear(examYear);
    exam.setExamTerm(examTerm);
    exam.setEnrollmentActive(enrollmentActive);
    return persist(exam);
  }

}