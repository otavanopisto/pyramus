package fi.pyramus.dao.base;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;

@Stateless
public class EducationalTimeUnitDAO extends PyramusEntityDAO<EducationalTimeUnit> {

  /**
   * Creates a new educational time unit.
   * 
   * @param baseUnits
   *          The number of base units this unit is
   * @param name
   *          The unit name
   * @param symbol TODO
   * @return The created education time unit
   */
  public EducationalTimeUnit create(Double baseUnits, String name, String symbol) {
    EntityManager entityManager = getEntityManager();

    EducationalTimeUnit educationalTimeUnit = new EducationalTimeUnit();
    educationalTimeUnit.setArchived(Boolean.FALSE);
    educationalTimeUnit.setBaseUnits(baseUnits);
    educationalTimeUnit.setName(name);
    educationalTimeUnit.setSymbol(symbol);

    entityManager.persist(educationalTimeUnit);

    return educationalTimeUnit;
  }

  public EducationalTimeUnit update(EducationalTimeUnit educationalTimeUnit, Double baseUnits, String name, String symbol) {
    EntityManager entityManager = getEntityManager();

    educationalTimeUnit.setBaseUnits(baseUnits);
    educationalTimeUnit.setName(name);
    educationalTimeUnit.setSymbol(symbol);

    entityManager.persist(educationalTimeUnit);

    return educationalTimeUnit;
  }

}
