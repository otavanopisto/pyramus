package fi.otavanopisto.pyramus.dao.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.search.backend.lucene.LuceneExtension;
import org.hibernate.search.backend.lucene.search.query.LuceneSearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;

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
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
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
import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.ListJoin;
import jakarta.persistence.criteria.Root;

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
    
    auditUpdate(person.getId(), null, person, Person_.socialSecurityNumber, socialSecurityNumber, false);
    auditUpdate(person.getId(), null, person, Person_.secureInfo, secureInfo, true);
    
    person.setBirthday(birthday);
    person.setSocialSecurityNumber(socialSecurityNumber);
    person.setSex(sex);
    person.setBasicInfo(basicInfo);
    person.setSecureInfo(secureInfo);
    entityManager.persist(person);
    
    personUpdatedEvent.fire(new PersonUpdatedEvent(person.getId()));
  }
  
  public void updateSocialSecurityNumber(Person person, String socialSecurityNumber) {
    EntityManager entityManager = getEntityManager();

    auditUpdate(person.getId(), null, person, Person_.socialSecurityNumber, socialSecurityNumber, false);
    
    person.setSocialSecurityNumber(socialSecurityNumber);
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

  public SearchResult<Person> searchPersonsBasic(int resultsPerPage, int page, String queryText, PersonFilter studentFilter, Set<Organization> organizations, Set<StudyProgramme> staffStudyProgrammes) {
    return searchPersonsBasic(resultsPerPage, page, queryText, studentFilter, null, null, organizations, staffStudyProgrammes);
  }
  
  public SearchResult<Person> searchPersonsBasic(int resultsPerPage, int page, String queryText, PersonFilter personFilter, StudyProgramme studyProgramme, StudentGroup studentGroup, Set<Organization> organizations, Set<StudyProgramme> staffStudyProgrammes) {
    
    // #1416: Basic security checks that always lead to no results
    
    if (CollectionUtils.isEmpty(organizations) || CollectionUtils.isEmpty(staffStudyProgrammes)) {
      return new SearchResult<>(0, 0, 0, 0, 0, new ArrayList<>()); 
    }

    int firstResult = page * resultsPerPage;

    Set<Long> organizationIds = organizations.stream().map(Organization::getId).collect(Collectors.toSet());
    Set<Long> staffStudyProgrammeIds = staffStudyProgrammes.stream().map(StudyProgramme::getId).collect(Collectors.toSet());

    final List<Long> studentIds = new ArrayList<>();
    
    if (studentGroup != null) {
      switch (personFilter) {
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
    SearchSession session = Search.session(entityManager);
    
    final String[] studentQueryFields = { 
        "students.firstName", 
        "students.lastName",
        "students.nickname",
        "students.contactInfo.emails.address",
        "students.tags.text",
        "students.person.koskiPersonOID",
        "students.koskiStudentOIDs"
    };
    
    final String[] staffMemberQueryFields = { 
        "staffMember.firstName", 
        "staffMember.lastName",
        "staffMember.title",
        "staffMember.contactInfo.emails.address",
        "staffMember.tags.text"
    };
    
    LuceneSearchResult<Person> fetch = session
        .search(Person.class)
        .extension(LuceneExtension.get())
        .where(f -> f.bool().with(b -> {
          b.must(f.or().with(studentORStaffPredicate -> {
            if (EnumSet.of(PersonFilter.ALL, PersonFilter.ACTIVE_STUDENTS, PersonFilter.INACTIVE_STUDENTS).contains(personFilter)) {
              studentORStaffPredicate.add(f.nested("students").with(n -> {
                if (StringUtils.isNotBlank(queryText)) {
                  n.add(f.match().fields(studentQueryFields).matching(queryText));
                }
  
                if (EnumSet.of(PersonFilter.ACTIVE_STUDENTS, PersonFilter.INACTIVE_STUDENTS).contains(personFilter)) {
                  n.add(f.match().field("students.active").matching(Boolean.valueOf(personFilter == PersonFilter.ACTIVE_STUDENTS)));
                }
                
                // If studentGroup is set, restrict to the members of the group
                if (studentGroup != null) {
                  n.add(or(f, "students.id", studentIds));
                }
  
                if (studyProgramme != null) {
                  n.add(f.match().field("students.studyProgramme.id").matching(studyProgramme.getId()));
                }
  
                n.add(or(f, "students.organization.id", organizationIds));
                // #1416 Limit results to study programmes of staff member
                n.add(or(f, "students.studyProgramme.id", staffStudyProgrammeIds));
                n.add(f.match().field("students.archived").matching(Boolean.FALSE));
              }));
            }
            
            if (EnumSet.of(PersonFilter.ALL, PersonFilter.STAFFMEMBERS).contains(personFilter)) {
              studentORStaffPredicate.add(f.nested("staffMember").with(n -> {
                if (StringUtils.isNotBlank(queryText)) {
                  n.add(f.match().fields(staffMemberQueryFields).matching(queryText));
                }
  
                n.add(or(f, "staffMember.organization.id", organizationIds));
                n.add(f.match().field("staffMember.archived").matching(Boolean.FALSE));
              }));
            }            
          })); // Student OR Staff Predicate
        })) // where
        .sort(f -> 
            f.field("active").desc()
            .then().field("lastNameSortable")
            .then().field("firstNameSortable"))
        .fetch(firstResult, resultsPerPage);
    return searchResults(fetch, page, firstResult, resultsPerPage);
  }

  public SearchResult<Person> searchPersons(int resultsPerPage, int page, String firstName, String lastName, String nickname, String tags, 
      String education, String email, Sex sex, String ssn, String addressCity, String addressCountry, String addressPostalCode, String addressStreetAddress,
      String phone, StudyProgramme studyProgramme, Language language, Nationality nationality, Municipality municipality,
      String title, PersonFilter personFilter, Set<Organization> organizations, Set<StudyProgramme> staffStudyProgrammes) {

    // #1416: Basic security checks that always lead to no results
    
    if (CollectionUtils.isEmpty(organizations) || CollectionUtils.isEmpty(staffStudyProgrammes)) {
      return new SearchResult<>(0, 0, 0, 0, 0, new ArrayList<>()); 
    }

    int firstResult = page * resultsPerPage;

    Set<Long> organizationIds = organizations.stream().map(Organization::getId).collect(Collectors.toSet());
    Set<Long> staffStudyProgrammeIds = staffStudyProgrammes.stream().map(StudyProgramme::getId).collect(Collectors.toSet());

    EntityManager entityManager = getEntityManager();
    SearchSession session = Search.session(entityManager);

    LuceneSearchResult<Person> fetch = session
        .search(Person.class)
        .extension(LuceneExtension.get())
        .where(f -> f.bool().with(b -> {
          b.must(f.or().with(or -> {
            if (EnumSet.of(PersonFilter.ALL, PersonFilter.ACTIVE_STUDENTS, PersonFilter.INACTIVE_STUDENTS).contains(personFilter)) {
              // Nested fields under Student
              or.add(f.nested("students").with(n -> {
                if (!StringUtils.isBlank(firstName)) {
                  n.add(f.match().field("students.firstName").matching(firstName));
                }
                if (!StringUtils.isBlank(lastName)) {
                  n.add(f.match().field("students.lastName").matching(lastName));
                }
                if (!StringUtils.isBlank(nickname)) {
                  n.add(f.match().field("students.nickname").matching(nickname));
                }
                if (!StringUtils.isBlank(tags)) {
                  n.add(f.match().field("students.tags.text").matching(tags));
                }
                if (!StringUtils.isBlank(education)) {
                  n.add(f.match().field("students.education").matching(education));
                }
                if (!StringUtils.isBlank(email)) {
                  n.add(f.match().field("students.contactInfo.emails.address").matching(email));
                }
                if (!StringUtils.isBlank(addressCity)) {
                  n.add(f.match().field("students.contactInfo.addresses.city").matching(addressCity));
                }
                if (!StringUtils.isBlank(addressCountry)) {
                  n.add(f.match().field("students.contactInfo.addresses.country").matching(addressCountry));
                }
                if (!StringUtils.isBlank(addressPostalCode)) {
                  n.add(f.match().field("students.contactInfo.addresses.postalCode").matching(addressPostalCode));
                }
                if (!StringUtils.isBlank(addressStreetAddress)) {
                  n.add(f.match().field("students.contactInfo.addresses.streetAddress").matching(addressStreetAddress));
                }
                if (!StringUtils.isBlank(phone)) {
                  n.add(f.match().field("students.contactInfo.phoneNumbers.number").matching(phone));
                }
                if (nationality != null) {
                  n.add(f.match().field("students.nationality.id").matching(nationality.getId()));
                }
                if (municipality != null) {
                  n.add(f.match().field("students.municipality.id").matching(municipality.getId()));
                }
                if (language != null) {
                  n.add(f.match().field("students.language.id").matching(language.getId()));
                }
                if (sex != null) {
                  b.must(f.match().field("students.person.sex").matching(sex));
                }
                if (!StringUtils.isBlank(ssn)) {
                  b.must(f.match().field("students.person.socialSecurityNumber").matching(ssn));
                }

                // TODO replace with date range
                
                if (EnumSet.of(PersonFilter.ACTIVE_STUDENTS, PersonFilter.INACTIVE_STUDENTS).contains(personFilter)) {
                  n.add(f.match().field("students.active").matching(Boolean.valueOf(personFilter == PersonFilter.ACTIVE_STUDENTS)));
                }
                
                if (studyProgramme != null) {
                  n.add(f.match().field("students.studyProgramme.id").matching(studyProgramme.getId()));
                }
  
                n.add(or(f, "students.organization.id", organizationIds));
                // #1416 Limit results to study programmes of staff member
                n.add(or(f, "students.studyProgramme.id", staffStudyProgrammeIds));
                n.add(f.match().field("students.archived").matching(Boolean.FALSE));
              }));
            }
            
            if (EnumSet.of(PersonFilter.ALL, PersonFilter.STAFFMEMBERS).contains(personFilter)) {
              // Nested fields under StaffMember
              or.add(f.nested("staffMember").with(n -> {
                if (!StringUtils.isBlank(firstName)) {
                  n.add(f.match().field("staffMember.firstName").matching(firstName));
                }
                if (!StringUtils.isBlank(lastName)) {
                  n.add(f.match().field("staffMember.lastName").matching(lastName));
                }
                if (!StringUtils.isBlank(title)) {
                  n.add(f.match().field("staffMember.title").matching(title));
                }
                if (!StringUtils.isBlank(tags)) {
                  n.add(f.match().field("staffMember.tags.text").matching(tags));
                }
                if (!StringUtils.isBlank(email)) {
                  n.add(f.match().field("staffMember.contactInfo.emails.address").matching(email));
                }
                if (!StringUtils.isBlank(addressCity)) {
                  n.add(f.match().field("staffMember.contactInfo.addresses.city").matching(addressCity));
                }
                if (!StringUtils.isBlank(addressCountry)) {
                  n.add(f.match().field("staffMember.contactInfo.addresses.country").matching(addressCountry));
                }
                if (!StringUtils.isBlank(addressPostalCode)) {
                  n.add(f.match().field("staffMember.contactInfo.addresses.postalCode").matching(addressPostalCode));
                }
                if (!StringUtils.isBlank(addressStreetAddress)) {
                  n.add(f.match().field("staffMember.contactInfo.addresses.streetAddress").matching(addressStreetAddress));
                }
                if (!StringUtils.isBlank(phone)) {
                  n.add(f.match().field("staffMember.contactInfo.phoneNumbers.number").matching(phone));
                }
                if (sex != null) {
                  b.must(f.match().field("staffMember.person.sex").matching(sex));
                }
                if (!StringUtils.isBlank(ssn)) {
                  b.must(f.match().field("staffMember.person.socialSecurityNumber").matching(ssn));
                }
                
                n.add(or(f, "staffMember.organization.id", organizationIds));
                n.add(f.match().field("staffMember.archived").matching(Boolean.FALSE));
              }));
            } // StaffMember Predicate
          })); // OR for Student/StaffMember
        })) // where
        .sort(f -> 
            f.field("lastNameSortable")
            .then().field("firstNameSortable"))
        .fetch(firstResult, resultsPerPage);
    return searchResults(fetch, page, firstResult, resultsPerPage);
  }

}
