package fi.otavanopisto.pyramus.dao.matriculation;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExam;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamTerm;

@Stateless
public class MatriculationExamDAO extends PyramusEntityDAO<MatriculationExam> {

  public MatriculationExam createOrUpdate(
    Date starts,
    Date ends, 
    Grade signupGrade,
    Integer examYear,
    MatriculationExamTerm examTerm
  ) {
    MatriculationExam exam = get();
    if (exam == null) {
      exam = new MatriculationExam();
      exam.setId(1L);
    }
    exam.setStarts(starts);
    exam.setEnds(ends);
    exam.setSignupGrade(signupGrade);
    exam.setExamYear(examYear);
    exam.setExamTerm(examTerm);
    return persist(exam);
  }

  public MatriculationExam get() {
    List<MatriculationExam> exams = listAll(0, 1);
    if (exams.size() > 0) {
      return exams.get(0);
    } else {
      return null;
    }
  }

}