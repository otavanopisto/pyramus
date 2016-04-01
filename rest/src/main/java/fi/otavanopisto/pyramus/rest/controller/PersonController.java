package fi.otavanopisto.pyramus.rest.controller;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.students.Sex;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Dependent
@Stateless
public class PersonController {
  
  @Inject
  private PersonDAO personDAO;

  public Person createPerson(Date birthday, String socialSecurityNumber, Sex sex, String basicInfo, Boolean secureInfo) {
    Person person = personDAO.create(birthday, socialSecurityNumber, sex, basicInfo, secureInfo);
    return person;
  }
  
  public List<Person> listPersons() {
    return listPersons(null, null);
  }
  
  public List<Person> listPersons(Integer firstResult, Integer maxResults) {
    return personDAO.listAll(firstResult, maxResults);
  }
  
  public List<Person> listUnarchivedPersons() {
    return listUnarchivedPersons(null, null);
  }
  
  public List<Person> listUnarchivedPersons(Integer firstResult, Integer maxResults) {
    return personDAO.listUnarchived(firstResult, maxResults);
  }
  
  public Person findPersonById(Long id) {
    Person person = personDAO.findById(id);
    return person;
  }
  
  public Person findUniquePersonByEmail(String email) {
    Person person = personDAO.findByUniqueEmail(email);
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
