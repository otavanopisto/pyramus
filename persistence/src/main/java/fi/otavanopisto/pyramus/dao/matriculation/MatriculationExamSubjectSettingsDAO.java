package fi.otavanopisto.pyramus.dao.matriculation;

import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExam;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamSubjectSettings;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamSubjectSettings_;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamSubject;

@Stateless
public class MatriculationExamSubjectSettingsDAO extends PyramusEntityDAO<MatriculationExamSubjectSettings> {

  public MatriculationExamSubjectSettings create(MatriculationExam exam, MatriculationExamSubject subject, Date examDate) {
    MatriculationExamSubjectSettings subset = new MatriculationExamSubjectSettings();
    subset.setExam(exam);
    subset.setSubject(subject);
    subset.setExamDate(examDate);
    return persist(subset);
  }

  public MatriculationExamSubjectSettings findBy(MatriculationExam exam, MatriculationExamSubject subject) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<MatriculationExamSubjectSettings> criteria = criteriaBuilder.createQuery(MatriculationExamSubjectSettings.class);
    Root<MatriculationExamSubjectSettings> root = criteria.from(MatriculationExamSubjectSettings.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(MatriculationExamSubjectSettings_.exam), exam),
            criteriaBuilder.equal(root.get(MatriculationExamSubjectSettings_.subject), subject)
        )
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public MatriculationExamSubjectSettings update(MatriculationExamSubjectSettings subjectSettings, Date examDate) {
    subjectSettings.setExamDate(examDate);
    return persist(subjectSettings);
  }

  public Date findMaxExamDate(MatriculationExam exam) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Date> criteria = criteriaBuilder.createQuery(Date.class);
    Root<MatriculationExamSubjectSettings> root = criteria.from(MatriculationExamSubjectSettings.class);
    
    criteria.select(criteriaBuilder.greatest(root.get(MatriculationExamSubjectSettings_.examDate)));
    criteria.where(
        criteriaBuilder.equal(root.get(MatriculationExamSubjectSettings_.exam), exam)
    );
    
    return entityManager.createQuery(criteria).getSingleResult();
  }

}