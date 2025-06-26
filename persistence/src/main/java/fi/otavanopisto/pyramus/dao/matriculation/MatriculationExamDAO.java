package fi.otavanopisto.pyramus.dao.matriculation;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExam;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExam_;
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

  public List<MatriculationExam> listSorted() {
    EntityManager entityManager = getEntityManager(); 
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<MatriculationExam> criteria = criteriaBuilder.createQuery(MatriculationExam.class);
    Root<MatriculationExam> root = criteria.from(MatriculationExam.class);
    
    criteria.select(root);
    criteria.orderBy(
        criteriaBuilder.desc(root.get(MatriculationExam_.examYear)),
        criteriaBuilder.desc(root.get(MatriculationExam_.examTerm))
    );

    return entityManager.createQuery(criteria).getResultList();
  }
  
}