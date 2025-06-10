package fi.otavanopisto.pyramus.rest.controller;

import java.util.Date;
import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.koski.KoskiPersonLogDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.students.Sex;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Dependent
@Stateless
public class PersonController {
  
  @Inject
  private PersonDAO personDAO;

  @Inject
  private KoskiPersonLogDAO koskiPersonLogDAO;

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
    return personDAO.findById(id);
  }
  
  public Person findBySsn(String ssn) {
    return personDAO.findBySSN(ssn);
  }
  
  public Person findUniquePersonByEmail(String email) {
    return personDAO.findByUniqueEmail(email);
  }
  
  public Person updatePerson(Person person, Date birthday, String socialSecurityNumber, Sex sex, String basicInfo, Boolean secureInfo) {
    personDAO.update(person, birthday, socialSecurityNumber, sex, basicInfo, secureInfo);
    return person;
  }

  public Person updatePersonDefaultUser(Person person, User defaultUser) {
    return personDAO.updateDefaultUser(person, defaultUser);
  }

  public void deletePerson(Person person) {
    koskiPersonLogDAO.deleteByPerson(person);
    personDAO.delete(person);
  }
}
