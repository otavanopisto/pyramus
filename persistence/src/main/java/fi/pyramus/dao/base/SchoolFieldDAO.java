package fi.pyramus.dao.base;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.SchoolField;

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
