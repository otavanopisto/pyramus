package fi.otavanopisto.pyramus.dao.base;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.SchoolField;

@Stateless
public class SchoolFieldDAO extends PyramusEntityDAO<SchoolField> {

  public SchoolField create(String name) {
    EntityManager entityManager = getEntityManager();

    SchoolField schoolField = new SchoolField();
    schoolField.setName(name);
    entityManager.persist(schoolField);

    return schoolField;
  }

  public SchoolField update(SchoolField schoolField, String name) {
    EntityManager entityManager = getEntityManager();

    schoolField.setName(name);
    entityManager.persist(schoolField);

    return schoolField;
  }
}
