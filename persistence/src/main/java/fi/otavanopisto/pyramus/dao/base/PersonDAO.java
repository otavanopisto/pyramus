package fi.otavanopisto.pyramus.dao.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.ContactInfo;
import fi.otavanopisto.pyramus.domainmodel.base.ContactInfo_;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType_;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.Email_;
import fi.otavanopisto.pyramus.domainmodel.base.Language;
import fi.otavanopisto.pyramus.domainmodel.base.Municipality;
import fi.otavanopisto.pyramus.domainmodel.base.Nationality;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.Person_;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.students.Sex;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupStudent;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.domainmodel.users.User_;
import fi.otavanopisto.pyramus.events.PersonCreatedEvent;
import fi.otavanopisto.pyramus.events.PersonUpdatedEvent;
import fi.otavanopisto.pyramus.persistence.search.PersonFilter;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;

@Stateless
public class PersonDAO extends PyramusEntityDAO<Person> {

  @Inject
  private Event<PersonCreatedEvent> personCreatedEvent;
  
  @Inject
  private Event<PersonUpdatedEvent> personUpdatedEvent;
  
  public Person create(Date birthday, String socialSecurityNumber, Sex sex, String basicInfo, Boolean secureInfo) {
    Person person = new Person();

    person.setBirthday(birthday);
    person.setSocialSecurityNumber(socialSecurityNumber);
    person.setSex(sex);
    person.setBasicInfo(basicInfo);
    person.setSecureInfo(secureInfo);

    person = persist(person);
    
    personCreatedEvent.fire(new PersonCreatedEvent(person.getId()));
    
    return person;
  }

  public void update(Person person, Date birthday, String socialSecurityNumber, Sex sex, String basicInfo, Boolean secureInfo) {
    EntityManager entityManager = getEntityManager();
    person.setBirthday(birthday);
    person.setSocialSecurityNumber(socialSecurityNumber);
    person.setSex(sex);
    person.setBasicInfo(basicInfo);
    person.setSecureInfo(secureInfo);
    entityManager.persist(person);
    
    personUpdatedEvent.fire(new PersonUpdatedEvent(person.getId()));
  }
  
  public Person findByUniqueEmail(String email){
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Person> criteria = criteriaBuilder.createQuery(Person.class);
    Root<Person> root = criteria.from(Person.class);
    ListJoin<Person, User> userJoin = root.join(Person_.users);
    Join<User, ContactInfo> contactInfoJoin = userJoin.join(User_.contactInfo);
    ListJoin<ContactInfo, Email> emailJoin = contactInfoJoin.join(ContactInfo_.emails);
    Join<Email, ContactType> contactTypeJoin = emailJoin.join(Email_.contactType);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(emailJoin.get(Email_.address), email),
            criteriaBuilder.equal(userJoin.get(User_.archived), Boolean.FALSE),
            criteriaBuilder.equal(contactTypeJoin.get(ContactType_.nonUnique), Boolean.FALSE)
        )
    ).distinct(true);
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public Person updateDefaultUser(Person person, User defaultUser){
    person.setDefaultUser(defaultUser);
    person = persist(person);
    
    personUpdatedEvent.fire(new PersonUpdatedEvent(person.getId()));

    return person;
  }
  
  /**
   * Returns an abstract student with the given social security number.
   * 
   * @param ssn
   *          The social security number
   * 
   * @return An abstract student with the given social security number
   */
  public Person findBySSN(String ssn) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Person> criteria = criteriaBuilder.createQuery(Person.class);
    Root<Person> root = criteria.from(Person.class);
    criteria.select(root);
    criteria.where(criteriaBuilder.equal(root.get(Person_.socialSecurityNumber), ssn));
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public List<Person> listBySSNUppercase(String ssn) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Person> criteria = criteriaBuilder.createQuery(Person.class);
    Root<Person> root = criteria.from(Person.class);
    criteria.select(root);
    criteria.where(criteriaBuilder.equal(criteriaBuilder.upper(root.get(Person_.socialSecurityNumber)), ssn.toUpperCase()));
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public SearchResult<Person> searchPersonsBasic(int resultsPerPage, int page, String queryText) {
    return searchPersonsBasic(resultsPerPage, page, queryText, PersonFilter.ACTIVE_STUDENTS);
  }

  public SearchResult<Person> searchPersonsBasic(int resultsPerPage, int page, String queryText, PersonFilter studentFilter) {
    return searchPersonsBasic(resultsPerPage, page, queryText, studentFilter, null, null);
  }
  
  /**
   * Changes @ -signs to ? wildcard for string. 
   * 
   * Without the wildcard users cannot be found with exact match of email. This is because
   * we construct the query by hand and lucene tokenizes the email and creates a BooleanQuery
   * for the email fields (which apparently doesn't work).
   * 
   * Wildcard changes the search so that instead of BooleanQuery you get WildcardQuery which
   * seems to work with the untokenized email fields
   * 
   * @param queryText
   * @return
   */
  private String lucenizeEmailSearchTerm(String queryText) {
    return queryText.replace('@', '?');
  }
  
  @SuppressWarnings("unchecked")
  public SearchResult<Person> searchPersonsBasic(int resultsPerPage, int page, String queryText, PersonFilter studentFilter, StudyProgramme studyProgramme, StudentGroup studentGroup) {
    int firstResult = page * resultsPerPage;

    StringBuilder queryBuilder = new StringBuilder();
    switch (studentFilter) {
      case ALL:

        // Search should find past students as well, so an abstract student is considered
        // a match if it contains at least one non-archived student, no matter whether
        // their study end date has been set or not
        
        if (!StringUtils.isBlank(queryText)) {
          queryBuilder.append("+(");
          addTokenizedSearchCriteria(queryBuilder, false, queryText, "activeFirstNames", "inactiveFirstNames", "staffMemberFirstNames");
          addTokenizedSearchCriteria(queryBuilder, false, queryText, "activeNicknames", "inactiveNicknames");
          addTokenizedSearchCriteria(queryBuilder, false, queryText, "staffMemberTitles");
          addTokenizedSearchCriteria(queryBuilder, false, queryText, "activeLastNames", "inactiveLastNames", "staffMemberLastNames");
          addTokenizedSearchCriteria(queryBuilder, false, lucenizeEmailSearchTerm(queryText), "activeEmails", "inactiveEmails", "staffMemberEmails");
          addTokenizedSearchCriteria(queryBuilder, false, queryText, "activeTags", "inactiveTags", "staffMemberTags");
          
          queryBuilder.append(")");
        }
        
        if (studyProgramme != null)
          addTokenizedSearchCriteria(queryBuilder, "inactiveStudyProgrammeIds", "activeStudyProgrammeIds", studyProgramme.getId().toString(), true);

        queryBuilder.append("+(");
        addTokenizedSearchCriteria(queryBuilder, "active", "true", false, 0f);
        addTokenizedSearchCriteria(queryBuilder, "inactive", "true", false, 0f);
        addTokenizedSearchCriteria(queryBuilder, "staff", "true", false, 0f);
        queryBuilder.append(")");
        
        // Other search terms
      break;
      case INACTIVE_STUDENTS:

        // Search should only find past students, so an abstract student is considered
        // a match if it only contains non-archived students who have their study end date
        // set and that date is in the past
        
        // Other search terms
        
        if (!StringUtils.isBlank(queryText)) {
          queryBuilder.append("+(");
          addTokenizedSearchCriteria(queryBuilder, "inactiveFirstNames", queryText, false);
          addTokenizedSearchCriteria(queryBuilder, "inactiveNicknames", queryText, false);
          addTokenizedSearchCriteria(queryBuilder, "inactiveLastNames", queryText, false);
          addTokenizedSearchCriteria(queryBuilder, "inactiveEmails", lucenizeEmailSearchTerm(queryText), false);
          addTokenizedSearchCriteria(queryBuilder, "inactiveTags", queryText, false);
          
          queryBuilder.append(")");
        }

        if (studyProgramme != null)
          addTokenizedSearchCriteria(queryBuilder, "inactiveStudyProgrammeIds", studyProgramme.getId().toString(), true);

        addTokenizedSearchCriteria(queryBuilder, "active", "false", true);
        addTokenizedSearchCriteria(queryBuilder, "inactive", "true", true, 0f);
      break;
      case ACTIVE_STUDENTS:
        
        // Search should skip past students, so an abstract student is considered a match
        // if it contains at least one non-archived student who hasn't got his study end
        // date set or the date is in the future

        if (!StringUtils.isBlank(queryText)) {
          queryBuilder.append("+(");
          addTokenizedSearchCriteria(queryBuilder, "activeFirstNames", queryText, false);
          addTokenizedSearchCriteria(queryBuilder, "activeNicknames", queryText, false);
          addTokenizedSearchCriteria(queryBuilder, "activeLastNames", queryText, false);
          addTokenizedSearchCriteria(queryBuilder, "activeEmails", lucenizeEmailSearchTerm(queryText), false);
          addTokenizedSearchCriteria(queryBuilder, "activeTags", queryText, false);
          
          queryBuilder.append(")");
        }

        if (studyProgramme != null)
          addTokenizedSearchCriteria(queryBuilder, "activeStudyProgrammeIds", studyProgramme.getId().toString(), true);

        addTokenizedSearchCriteria(queryBuilder, "active", "true", true, 0f);
      break;
      case STAFFMEMBERS:
        if (!StringUtils.isBlank(queryText)) {
          queryBuilder.append("+(");

          addTokenizedSearchCriteria(queryBuilder, "staffMemberFirstNames", queryText, false);
          addTokenizedSearchCriteria(queryBuilder, "staffMemberLastNames", queryText, false);
          addTokenizedSearchCriteria(queryBuilder, "staffMemberTitles", queryText, false);
          addTokenizedSearchCriteria(queryBuilder, "staffMemberEmails", lucenizeEmailSearchTerm(queryText), false);
          addTokenizedSearchCriteria(queryBuilder, "staffMemberTags", queryText, false);
          
          queryBuilder.append(")");

          addTokenizedSearchCriteria(queryBuilder, "staff", "true", false, 0f);
        }
      break;
    }
    
    List<Long> studentIds = null;
    
    if (studentGroup != null) {
      studentIds = new ArrayList<>();
      
      switch (studentFilter) {
        case ALL:
          for (StudentGroupStudent sgs : studentGroup.getStudents()) {
            studentIds.add(sgs.getStudent().getId());
          }
        break;
        case INACTIVE_STUDENTS:
          for (StudentGroupStudent sgs : studentGroup.getStudents()) {
            if (!sgs.getStudent().getActive())
              studentIds.add(sgs.getStudent().getId());
          }
        break;
        case ACTIVE_STUDENTS:
          for (StudentGroupStudent sgs : studentGroup.getStudents()) {
            if (sgs.getStudent().getActive())
              studentIds.add(sgs.getStudent().getId());
          }
        break;
        case STAFFMEMBERS:
        break;
      }
    }

    EntityManager entityManager = getEntityManager();
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
    
    try {
      String queryString = queryBuilder.toString();
      
      Query luceneQuery;
      
      QueryParser parser = new QueryParser("", new StandardAnalyzer());
      luceneQuery = parser.parse(queryString);

      FullTextQuery query = (FullTextQuery) fullTextEntityManager
          .createFullTextQuery(luceneQuery, Person.class)
          .setSort(
              new Sort(new SortField[] { 
                  SortField.FIELD_SCORE, 
                  new SortField("lastNameSortable", SortField.Type.STRING),
                  new SortField("firstNameSortable", SortField.Type.STRING) })).setFirstResult(firstResult).setMaxResults(resultsPerPage);

      if (studentGroup != null)
        query.enableFullTextFilter("StudentIdFilter").setParameter("studentIds", studentIds);
      
      int hits = query.getResultSize();
      int pages = hits / resultsPerPage;
      if (hits % resultsPerPage > 0) {
        pages++;
      }

      int lastResult = Math.min(firstResult + resultsPerPage, hits) - 1;

      return new SearchResult<>(page, pages, hits, firstResult, lastResult, query.getResultList());
    } catch (ParseException e) {
      throw new PersistenceException(e);
    }
  }

  @SuppressWarnings("unchecked")
  public SearchResult<Person> searchPersons(int resultsPerPage, int page, String firstName, String lastName, String nickname, String tags, 
      String education, String email, Sex sex, String ssn, String addressCity, String addressCountry, String addressPostalCode, String addressStreetAddress,
      String phone, Boolean lodging, StudyProgramme studyProgramme, Language language, Nationality nationality, Municipality municipality,
      String title, PersonFilter personFilter) {

    int firstResult = page * resultsPerPage;

    StringBuilder queryBuilder = new StringBuilder();

    if (sex != null)
      addTokenizedSearchCriteria(queryBuilder, "sex", sex.toString(), true);
    if (!StringUtils.isBlank(ssn))
      addTokenizedSearchCriteria(queryBuilder, "socialSecurityNumber", ssn, true);

    switch (personFilter) {
      case ALL:

        // Search should find past students as well, so an abstract student is considered
        // a match if it contains at least one non-archived student, no matter whether
        // their study end date has been set or not
        
//        queryBuilder.append("+(");
//        addTokenizedSearchCriteria(queryBuilder, "active", "true", false);
//        addTokenizedSearchCriteria(queryBuilder, "inactive", "true", false);
//        queryBuilder.append(")");
        
        // Other search terms
        
        if (!StringUtils.isBlank(firstName))
          addTokenizedSearchCriteria(queryBuilder, true, firstName, "inactiveFirstNames", "activeFirstNames", "staffMemberFirstNames");
        if (!StringUtils.isBlank(lastName))
          addTokenizedSearchCriteria(queryBuilder, true, lastName, "inactiveLastNames", "activeLastNames", "staffMemberLastNames");
        if (!StringUtils.isBlank(title))
          addTokenizedSearchCriteria(queryBuilder, true, title, "staffMemberTitles");
        if (!StringUtils.isBlank(nickname))
          addTokenizedSearchCriteria(queryBuilder, true, nickname, "inactiveNicknames", "activeNicknames");
        if (!StringUtils.isBlank(tags))
          addTokenizedSearchCriteria(queryBuilder, true, tags, "inactiveTags", "activeTags", "staffMemberTags");
        if (!StringUtils.isBlank(education))
          addTokenizedSearchCriteria(queryBuilder, true, education, "inactiveEducations", "activeEducations");
        if (!StringUtils.isBlank(email))
          addTokenizedSearchCriteria(queryBuilder, true, lucenizeEmailSearchTerm(email), "inactiveEmails", "activeEmails", "staffMemberEmails");
        if (!StringUtils.isBlank(addressCity))
          addTokenizedSearchCriteria(queryBuilder, true, addressCity, "inactiveCities", "activeCities", "staffMemberCities");
        if (!StringUtils.isBlank(addressCountry))
          addTokenizedSearchCriteria(queryBuilder, true, addressCountry, "inactiveCountries", "activeCountries", "staffMemberCountries");
        if (!StringUtils.isBlank(addressPostalCode))
          addTokenizedSearchCriteria(queryBuilder, true, addressPostalCode, "inactivePostalCodes", "activePostalCodes", "staffMemberPostalCodes");
        if (!StringUtils.isBlank(addressStreetAddress))
          addTokenizedSearchCriteria(queryBuilder, true, addressStreetAddress, "inactiveStreetAddresses", "activeStreetAddresses", "staffMemberStreetAddresses");
        if (!StringUtils.isBlank(phone))
          addTokenizedSearchCriteria(queryBuilder, true, phone, "inactivePhones", "activePhones", "staffMemberPhones");
        if (studyProgramme != null)
          addTokenizedSearchCriteria(queryBuilder, "inactiveStudyProgrammeIds", "activeStudyProgrammeIds", studyProgramme.getId().toString(), true);
        if (nationality != null)
          addTokenizedSearchCriteria(queryBuilder, "inactiveNationalityIds", "activeNationalityIds", nationality.getId().toString(), true);
        if (municipality != null)
          addTokenizedSearchCriteria(queryBuilder, "inactiveMunicipalityIds", "activeMunicipalityIds", municipality.getId().toString(), true);
        if (language != null)
          addTokenizedSearchCriteria(queryBuilder, "inactiveLanguageIds", "activeLanguageIds", language.getId().toString(), true);
        if (lodging != null) {
          addTokenizedSearchCriteria(queryBuilder, "inactiveLodgings", "activeLodgings", lodging.toString(), true);
        }

      break;
      case INACTIVE_STUDENTS:

        // Search should only find past students, so an abstract student is considered
        // a match if it only contains non-archived students who have their study end date
        // set and that date is in the past
        
        addTokenizedSearchCriteria(queryBuilder, "active", "false", true);
        addTokenizedSearchCriteria(queryBuilder, "inactive", "true", true);
        
        // Other search terms
        
        if (!StringUtils.isBlank(firstName))
          addTokenizedSearchCriteria(queryBuilder, "inactiveFirstNames", firstName, true);
        if (!StringUtils.isBlank(lastName))
          addTokenizedSearchCriteria(queryBuilder, "inactiveLastNames", lastName, true);
        if (!StringUtils.isBlank(nickname))
          addTokenizedSearchCriteria(queryBuilder, "inactiveNicknames", nickname, true);
        if (!StringUtils.isBlank(tags))
          addTokenizedSearchCriteria(queryBuilder, "inactiveTags", tags, true);
        if (!StringUtils.isBlank(education))
          addTokenizedSearchCriteria(queryBuilder, "inactiveEducations", education, true);
        if (!StringUtils.isBlank(email))
          addTokenizedSearchCriteria(queryBuilder, "inactiveEmails", lucenizeEmailSearchTerm(email), true);
        if (!StringUtils.isBlank(addressCity))
          addTokenizedSearchCriteria(queryBuilder, "inactiveCities", addressCity, true);
        if (!StringUtils.isBlank(addressCountry))
          addTokenizedSearchCriteria(queryBuilder, "inactiveCountries", addressCountry, true);
        if (!StringUtils.isBlank(addressPostalCode))
          addTokenizedSearchCriteria(queryBuilder, "inactivePostalCodes", addressPostalCode, true);
        if (!StringUtils.isBlank(addressStreetAddress))
          addTokenizedSearchCriteria(queryBuilder, "inactiveStreetAddresses", addressStreetAddress, true);
        if (!StringUtils.isBlank(phone))
          addTokenizedSearchCriteria(queryBuilder, "inactivePhones", phone, true);
        if (studyProgramme != null)
          addTokenizedSearchCriteria(queryBuilder, "inactiveStudyProgrammeIds", studyProgramme.getId().toString(), true);
        if (nationality != null)
          addTokenizedSearchCriteria(queryBuilder, "inactiveNationalityIds", nationality.getId().toString(), true);
        if (municipality != null)
          addTokenizedSearchCriteria(queryBuilder, "inactiveMunicipalityIds", municipality.getId().toString(), true);
        if (language != null)
          addTokenizedSearchCriteria(queryBuilder, "inactiveLanguageIds", language.getId().toString(), true);
        if (lodging != null) {
          addTokenizedSearchCriteria(queryBuilder, "inactiveLodgings", lodging.toString(), true);
        }
      break;
      case ACTIVE_STUDENTS:
        
        // Search should skip past students, so an abstract student is considered a match
        // if it contains at least one non-archived student who hasn't got his study end
        // date set or the date is in the future

        addTokenizedSearchCriteria(queryBuilder, "active", "true", true);
        
        // Other search terms
        
        if (!StringUtils.isBlank(firstName))
          addTokenizedSearchCriteria(queryBuilder, "activeFirstNames", firstName, true);
        if (!StringUtils.isBlank(lastName))
          addTokenizedSearchCriteria(queryBuilder, "activeLastNames", lastName, true);
        if (!StringUtils.isBlank(nickname))
          addTokenizedSearchCriteria(queryBuilder, "activeNicknames", nickname, true);
        if (!StringUtils.isBlank(education))
          addTokenizedSearchCriteria(queryBuilder, "activeEducations", education, true);
        if (!StringUtils.isBlank(tags))
          addTokenizedSearchCriteria(queryBuilder, "activeTags", tags, true);
        if (!StringUtils.isBlank(email))
          addTokenizedSearchCriteria(queryBuilder, "activeEmails", lucenizeEmailSearchTerm(email), true);
        if (!StringUtils.isBlank(addressCity))
          addTokenizedSearchCriteria(queryBuilder, "activeCities", addressCity, true);
        if (!StringUtils.isBlank(addressCountry))
          addTokenizedSearchCriteria(queryBuilder, "activeCountries", addressCountry, true);
        if (!StringUtils.isBlank(addressPostalCode))
          addTokenizedSearchCriteria(queryBuilder, "activePostalCodes", addressPostalCode, true);
        if (!StringUtils.isBlank(addressStreetAddress))
          addTokenizedSearchCriteria(queryBuilder, "activeStreetAddresses", addressStreetAddress, true);
        if (!StringUtils.isBlank(phone))
          addTokenizedSearchCriteria(queryBuilder, "activePhones", phone, true);
        if (studyProgramme != null)
          addTokenizedSearchCriteria(queryBuilder, "activeStudyProgrammeIds", studyProgramme.getId().toString(), true);
        if (nationality != null)
          addTokenizedSearchCriteria(queryBuilder, "activeNationalityIds", nationality.getId().toString(), true);
        if (municipality != null)
          addTokenizedSearchCriteria(queryBuilder, "activeMunicipalityIds", municipality.getId().toString(), true);
        if (language != null)
          addTokenizedSearchCriteria(queryBuilder, "activeLanguageIds", language.getId().toString(), true);
        if (lodging != null) {
          addTokenizedSearchCriteria(queryBuilder, "activeLodgings", lodging.toString(), true);
        }
      break;
      
      case STAFFMEMBERS:
        if (!StringUtils.isBlank(firstName))
          addTokenizedSearchCriteria(queryBuilder, true, firstName, "staffMemberFirstNames");
        if (!StringUtils.isBlank(lastName))
          addTokenizedSearchCriteria(queryBuilder, true, lastName, "staffMemberLastNames");
        if (!StringUtils.isBlank(title))
          addTokenizedSearchCriteria(queryBuilder, true, title, "staffMemberTitles");
        if (!StringUtils.isBlank(tags))
          addTokenizedSearchCriteria(queryBuilder, true, tags, "staffMemberTags");
        if (!StringUtils.isBlank(email))
          addTokenizedSearchCriteria(queryBuilder, true, lucenizeEmailSearchTerm(email), "staffMemberEmails");
        if (!StringUtils.isBlank(addressCity))
          addTokenizedSearchCriteria(queryBuilder, true, addressCity, "staffMemberCities");
        if (!StringUtils.isBlank(addressCountry))
          addTokenizedSearchCriteria(queryBuilder, true, addressCountry, "staffMemberCountries");
        if (!StringUtils.isBlank(addressPostalCode))
          addTokenizedSearchCriteria(queryBuilder, true, addressPostalCode, "staffMemberPostalCodes");
        if (!StringUtils.isBlank(addressStreetAddress))
          addTokenizedSearchCriteria(queryBuilder, true, addressStreetAddress, "staffMemberStreetAddresses");
        if (!StringUtils.isBlank(phone))
          addTokenizedSearchCriteria(queryBuilder, true, phone, "staffMemberPhones");
      break;
    }

    EntityManager entityManager = getEntityManager();
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

    try {
      String queryString = queryBuilder.toString();
      Query luceneQuery;
      QueryParser parser = new QueryParser("", new StandardAnalyzer());
      if (StringUtils.isBlank(queryString)) {
        luceneQuery = new MatchAllDocsQuery();
      } else {
        luceneQuery = parser.parse(queryString);
      }

      FullTextQuery query = (FullTextQuery) fullTextEntityManager
          .createFullTextQuery(luceneQuery, Person.class)
          .setFirstResult(firstResult);
      
      query.setSort(new Sort(
          new SortField[] { SortField.FIELD_SCORE, new SortField("lastNameSortable", SortField.Type.STRING),
              new SortField("firstNameSortable", SortField.Type.STRING) })).setMaxResults(resultsPerPage);

      int hits = query.getResultSize();
      int pages = hits / resultsPerPage;
      if (hits % resultsPerPage > 0) {
        pages++;
      }

      int lastResult = Math.min(firstResult + resultsPerPage, hits) - 1;

      return new SearchResult<>(page, pages, hits, firstResult, lastResult, query.getResultList());
    } catch (ParseException e) {
      throw new PersistenceException(e);
    }
  }
  
}
