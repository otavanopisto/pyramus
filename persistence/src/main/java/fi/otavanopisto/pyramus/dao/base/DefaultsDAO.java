package fi.otavanopisto.pyramus.dao.base;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Defaults;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseParticipationType;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseState;

@Stateless
public class DefaultsDAO extends PyramusEntityDAO<Defaults> {

  public boolean isPyramusInitialized() {
//    EntityManager entityManager = getEntityManager();
//    return s.get(Defaults.class, new Long(1)) != null;
    return getDefaults() != null;
  }

  public Defaults getDefaults() {
    return findById(new Long(1));
  }

  public Defaults updateDefaultBaseTimeUnit(EducationalTimeUnit defaultEducationalTimeUnit) {
    EntityManager entityManager = getEntityManager();

    Defaults defaults = getDefaults();
    
    if(defaults == null) {
      defaults = new Defaults();
      defaults.setId(1l);
    }
    
    defaults.setBaseTimeUnit(defaultEducationalTimeUnit);

    entityManager.persist(defaults);

    return defaults;
  }

  public Defaults updateInitialCourseParticipationType(CourseParticipationType initialCourseParticipationType) {
    EntityManager entityManager = getEntityManager();

    Defaults defaults = getDefaults();
    defaults.setInitialCourseParticipationType(initialCourseParticipationType);

    entityManager.persist(defaults);

    return defaults;
  }

  public Defaults updateDefaultInitialCourseState(CourseState initialCourseState) {
    EntityManager entityManager = getEntityManager();

    Defaults defaults = getDefaults();
    defaults.setInitialCourseState(initialCourseState);

    entityManager.persist(defaults);

    return defaults;
  }

}
