package fi.otavanopisto.pyramus.dao.courses;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseDescriptionCategory;

@Stateless
public class CourseDescriptionCategoryDAO extends PyramusEntityDAO<CourseDescriptionCategory> {
  public CourseDescriptionCategory create(String name) {
    EntityManager entityManager = getEntityManager();
    
    CourseDescriptionCategory category = new CourseDescriptionCategory();
    category.setName(name);
    category.setArchived(Boolean.FALSE);

    entityManager.persist(category);

    return category;
  }

  public CourseDescriptionCategory update(CourseDescriptionCategory category, String name) {
    EntityManager entityManager = getEntityManager();
    
    category.setName(name);

    entityManager.persist(category);

    return category;
  }
  
}
