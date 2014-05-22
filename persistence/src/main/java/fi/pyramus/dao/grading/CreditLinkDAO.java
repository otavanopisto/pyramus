package fi.pyramus.dao.grading;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.grading.Credit;
import fi.pyramus.domainmodel.grading.CreditLink;
import fi.pyramus.domainmodel.grading.CreditLink_;
import fi.pyramus.domainmodel.grading.CreditType;
import fi.pyramus.domainmodel.grading.Credit_;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.users.User;

@Stateless
public class CreditLinkDAO extends PyramusEntityDAO<CreditLink> {

  public CreditLink create(Credit credit, Student student, User creator) {
    EntityManager entityManager = getEntityManager();
    
    CreditLink creditLink = new CreditLink();
    creditLink.setCredit(credit);
    creditLink.setStudent(student);
    creditLink.setCreator(creator);
    creditLink.setCreated(new Date());
    
    entityManager.persist(creditLink);
    
    return creditLink;
  }

  public List<CreditLink> listByStudentAndType(Student student, CreditType creditType) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CreditLink> criteria = criteriaBuilder.createQuery(CreditLink.class);
    Root<CreditLink> root = criteria.from(CreditLink.class);
    Join<CreditLink, Credit> join = root.join(CreditLink_.credit);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(CreditLink_.student), student),
            criteriaBuilder.equal(join.get(Credit_.creditType), creditType),
            criteriaBuilder.equal(root.get(CreditLink_.archived), Boolean.FALSE)
        )
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<CreditLink> listByStudent(Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CreditLink> criteria = criteriaBuilder.createQuery(CreditLink.class);
    Root<CreditLink> root = criteria.from(CreditLink.class);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(CreditLink_.student), student),
            criteriaBuilder.equal(root.get(CreditLink_.archived), Boolean.FALSE)
        )
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public Long countByStudent(Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
    Root<CreditLink> root = criteria.from(CreditLink.class);
    
    criteria.select(criteriaBuilder.count(root));
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(CreditLink_.student), student),
            criteriaBuilder.equal(root.get(CreditLink_.archived), Boolean.FALSE)
        ));
    
    return entityManager.createQuery(criteria).getSingleResult();
  }

  public CreditLink findByStudentAndCredit(Student student, Credit credit) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CreditLink> criteria = criteriaBuilder.createQuery(CreditLink.class);
    Root<CreditLink> root = criteria.from(CreditLink.class);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(CreditLink_.student), student),
            criteriaBuilder.equal(root.get(CreditLink_.credit), credit),
            criteriaBuilder.equal(root.get(CreditLink_.archived), Boolean.FALSE)
        )
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
}
