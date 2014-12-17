package fi.pyramus.rest.controller;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.pyramus.dao.base.PersonDAO;
import fi.pyramus.domainmodel.base.Person;
import fi.pyramus.domainmodel.students.Sex;
import fi.pyramus.domainmodel.users.User;

@Dependent
@Stateless
public class PersonController {
  
  @Inject
  private PersonDAO personDAO;

  public Person createPerson(Date birthday, String socialSecurityNumber, Sex sex, String basicInfo, Boolean secureInfo) {
    Person person = personDAO.create(birthday, socialSecurityNumber, sex, basicInfo, secureInfo);
    return person;
  }
  
  public List<Person> findPersons() {
    List<Person> persons = personDAO.listAll();
    return persons;
  }
  
  public List<Person> findUnarchivedPersons() {
    List<Person> persons = personDAO.listUnarchived();
    return persons;
  }
  
  public Person findPersonById(Long id) {
    Person person = personDAO.findById(id);
    return person;
  }
  
  public Person updatePerson(Person person, Date birthday, String socialSecurityNumber, Sex sex, String basicInfo, Boolean secureInfo) {
    personDAO.update(person, birthday, socialSecurityNumber, sex, basicInfo, secureInfo);
    return person;
  }

  public Person updatePersonDefaultUser(Person person, User defaultUser) {
    return personDAO.updateDefaultUser(person, defaultUser);
  }

  public void deletePerson(Person person) {
    personDAO.delete(person);
  }
}
