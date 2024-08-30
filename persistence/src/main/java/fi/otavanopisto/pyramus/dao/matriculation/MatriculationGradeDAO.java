package fi.otavanopisto.pyramus.dao.matriculation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationGrade;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationGrade_;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamGrade;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamSubject;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamTerm;

@Stateless
public class MatriculationGradeDAO extends PyramusEntityDAO<MatriculationGrade> {

  public MatriculationGrade create(Person person, MatriculationExamSubject subject, Integer year, 
      MatriculationExamTerm term, MatriculationExamGrade grade, LocalDate gradeDate, StaffMember modifier) {
    MatriculationGrade entity = new MatriculationGrade();

    entity.setPerson(person);
    entity.setSubject(subject);
    entity.setYear(year);
    entity.setTerm(term);
    entity.setGrade(grade);
    entity.setGradeDate(gradeDate);
    entity.setModifier(modifier);
    entity.setLastModified(LocalDateTime.now());
    
    return persist(entity);
  }
  
  public MatriculationGrade findBy(Person person, int year, MatriculationExamTerm term, MatriculationExamSubject subject) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<MatriculationGrade> criteria = criteriaBuilder.createQuery(MatriculationGrade.class);
    Root<MatriculationGrade> root = criteria.from(MatriculationGrade.class);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(MatriculationGrade_.person), person),
            criteriaBuilder.equal(root.get(MatriculationGrade_.year), year),
            criteriaBuilder.equal(root.get(MatriculationGrade_.term), term),
            criteriaBuilder.equal(root.get(MatriculationGrade_.subject), subject)
        )
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  public List<MatriculationGrade> listBy(Person person) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<MatriculationGrade> criteria = criteriaBuilder.createQuery(MatriculationGrade.class);
    Root<MatriculationGrade> root = criteria.from(MatriculationGrade.class);
    
    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(MatriculationGrade_.person), person)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<MatriculationGrade> listBy(Person person, int year, MatriculationExamTerm term) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<MatriculationGrade> criteria = criteriaBuilder.createQuery(MatriculationGrade.class);
    Root<MatriculationGrade> root = criteria.from(MatriculationGrade.class);
    
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(MatriculationGrade_.person), person),
        criteriaBuilder.equal(root.get(MatriculationGrade_.year), year),
        criteriaBuilder.equal(root.get(MatriculationGrade_.term), term)
      )
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public MatriculationGrade update(MatriculationGrade matriculationGrade, MatriculationExamGrade grade, LocalDate gradeDate,
      StaffMember loggedUser) {
    
    matriculationGrade.setGrade(grade);
    matriculationGrade.setGradeDate(gradeDate);
    matriculationGrade.setModifier(loggedUser);
    matriculationGrade.setLastModified(LocalDateTime.now());

    return persist(matriculationGrade);
  }
  
}