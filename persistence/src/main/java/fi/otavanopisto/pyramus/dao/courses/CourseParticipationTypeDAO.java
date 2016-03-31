package fi.otavanopisto.pyramus.dao.courses;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseParticipationType;
import fi.pyramus.domainmodel.courses.CourseParticipationType_;

@Stateless
public class CourseParticipationTypeDAO extends PyramusEntityDAO<CourseParticipationType> {
  
  public CourseParticipationType create(String name) {
    Integer indexColumn = findMaxIndexColumn();
    
    if (indexColumn == null)
      indexColumn = new Integer(0);
    else
      indexColumn++;

    EntityManager entityManager = getEntityManager();
    CourseParticipationType courseParticipationType = new CourseParticipationType();
    courseParticipationType.setName(name);
    courseParticipationType.setIndexColumn(indexColumn);

    entityManager.persist(courseParticipationType);

    return courseParticipationType;
  }

  public CourseParticipationType update(CourseParticipationType courseParticipationType, String name) {
    EntityManager entityManager = getEntityManager();

    courseParticipationType.setName(name);

    entityManager.persist(courseParticipationType);

    return courseParticipationType;
  }

  public Integer findMaxIndexColumn() {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Integer> criteria = criteriaBuilder.createQuery(Integer.class);
    Root<CourseParticipationType> root = criteria.from(CourseParticipationType.class);
    criteria.select(criteriaBuilder.max(root.get(CourseParticipationType_.indexColumn)));
    
    return entityManager.createQuery(criteria).getSingleResult();
  }
}
