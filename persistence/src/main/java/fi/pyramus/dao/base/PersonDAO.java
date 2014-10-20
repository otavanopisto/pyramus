package fi.pyramus.dao.base;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.Person;

@Stateless
public class PersonDAO extends PyramusEntityDAO<Person> {

  public Person create() {
    EntityManager entityManager = getEntityManager();

    Person person = new Person();
    entityManager.persist(person);

    return person;
  }

}
