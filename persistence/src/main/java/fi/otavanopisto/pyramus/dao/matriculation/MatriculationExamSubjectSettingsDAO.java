package fi.otavanopisto.pyramus.dao.matriculation;

import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamSubject;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamSubjectSettings;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamSubjectSettings_;
import fi.otavanopisto.pyramus.domainmodel.projects.Project;

@Stateless
public class MatriculationExamSubjectSettingsDAO extends PyramusEntityDAO<MatriculationExamSubjectSettings> {

  public MatriculationExamSubjectSettings create(MatriculationExamSubject subject, Project project, Date examDate) {
    MatriculationExamSubjectSettings subset = new MatriculationExamSubjectSettings();
    subset.setSubject(subject);
    subset.setProject(project);
    subset.setExamDate(examDate);
    return persist(subset);
  }

  public MatriculationExamSubjectSettings findBy(MatriculationExamSubject subject) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<MatriculationExamSubjectSettings> criteria = criteriaBuilder.createQuery(MatriculationExamSubjectSettings.class);
    Root<MatriculationExamSubjectSettings> root = criteria.from(MatriculationExamSubjectSettings.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(MatriculationExamSubjectSettings_.subject), subject)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public MatriculationExamSubjectSettings update(MatriculationExamSubjectSettings subjectSettings, Project project, Date examDate) {
    subjectSettings.setProject(project);
    subjectSettings.setExamDate(examDate);
    return persist(subjectSettings);
  }

}