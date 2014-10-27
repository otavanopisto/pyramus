package fi.pyramus.dao.base;

import javax.ejb.Stateless;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.Person;

@Stateless
public class PersonDAO extends PyramusEntityDAO<Person> {

  public Person create() {
    Person person = new Person();

    return persist(person);
  }

}
