package fi.otavanopisto.pyramus.dao.matriculation;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationPersonFlag;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationPersonFlag_;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamEnrollmentFlag;

@Stateless
public class MatriculationPersonFlagDAO extends PyramusEntityDAO<MatriculationPersonFlag> {

  public MatriculationPersonFlag create(Person person, MatriculationExamEnrollmentFlag flag) {
    MatriculationPersonFlag entity = new MatriculationPersonFlag();
    entity.setPerson(person);
    entity.setFlag(flag);
    return persist(entity);
  }
  
  public List<MatriculationPersonFlag> listByPerson(Person person) {
    EntityManager entityManager = getEntityManager(); 
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<MatriculationPersonFlag> criteria = criteriaBuilder.createQuery(MatriculationPersonFlag.class);
    Root<MatriculationPersonFlag> root = criteria.from(MatriculationPersonFlag.class);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(MatriculationPersonFlag_.person), person)
    );

    return entityManager.createQuery(criteria).getResultList();
  }

  @Override
  public void delete(MatriculationPersonFlag e) {
    super.delete(e);
  }
  
}