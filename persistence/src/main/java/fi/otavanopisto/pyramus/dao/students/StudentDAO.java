package fi.otavanopisto.pyramus.dao.students;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStudentDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableKeyDAO;
import fi.otavanopisto.pyramus.domainmodel.base.ArchivableEntity;
import fi.otavanopisto.pyramus.domainmodel.base.ContactInfo;
import fi.otavanopisto.pyramus.domainmodel.base.ContactInfo_;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.Email_;
import fi.otavanopisto.pyramus.domainmodel.base.Language;
import fi.otavanopisto.pyramus.domainmodel.base.Municipality;
import fi.otavanopisto.pyramus.domainmodel.base.Nationality;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.School;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentActivityType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentEducationalLevel;
import fi.otavanopisto.pyramus.domainmodel.students.StudentExaminationType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyEndReason;
import fi.otavanopisto.pyramus.domainmodel.students.Student_;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariable;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariableKey;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariable_;
import fi.otavanopisto.pyramus.events.StudentArchivedEvent;
import fi.otavanopisto.pyramus.events.StudentCreatedEvent;
import fi.otavanopisto.pyramus.events.StudentUpdatedEvent;

@Stateless
public class StudentDAO extends PyramusEntityDAO<Student> {
  
  @Inject
  private Event<StudentCreatedEvent> studentCreatedEvent;
  
  @Inject
  private Event<StudentUpdatedEvent> studentUpdatedEvent;
  
  @Inject
  private Event<StudentArchivedEvent> studentArchivedEvent;
  
  /**
   * Archives a student.
   * 
   * @param student
   *          The student to be archived
   */
  @Override
  public void archive(ArchivableEntity entity, User modifier) {
    super.archive(entity, modifier);
    
    if (entity instanceof Student) {
      Student student = (Student) entity;

      CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
      PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
  
      // Also archive course students of the archived student
      
      List<CourseStudent> courseStudents = courseStudentDAO.listByStudent(student);
      if (!courseStudents.isEmpty()) {
        for (CourseStudent courseStudent : courseStudents) {
          courseStudentDAO.archive(courseStudent);
        }
      }
  
      // This is necessary because Person entity does not really
      // change in this operation but it still needs to be reindexed
  
      personDAO.forceReindex(student.getPerson());
      
      studentArchivedEvent.fire(new StudentArchivedEvent(student.getId()));
    }
  }

  /**
   * Unarchives a student.
   * 
   * @param student
   *          The student to be unarchived
   */
  @Override
  public void unarchive(ArchivableEntity entity, User modifier) {
    super.unarchive(entity, modifier);
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    
    if (entity instanceof Student) {
      // This is necessary because Person entity does not really
      // change in this operation but it still needs to be reindexed

      Student student = (Student) entity;

      personDAO.forceReindex(student.getPerson());
      
      studentUpdatedEvent.fire(new StudentUpdatedEvent(student.getId()));
    }
  }

  public Student create(Person person, String firstName, String lastName, String nickname, String additionalInfo,
      Date studyTimeEnd, StudentActivityType activityType, StudentExaminationType examinationType, StudentEducationalLevel educationalLevel, 
      String education, Nationality nationality, Municipality municipality, Language language, School school, StudyProgramme studyProgramme, 
      Curriculum curriculum, Double previousStudies, Date studyStartDate, Date studyEndDate, StudentStudyEndReason studyEndReason, 
      String studyEndText, Boolean lodging, Boolean archived) {

    EntityManager entityManager = getEntityManager();

    ContactInfo contactInfo = new ContactInfo();
    
    Student student = new Student();
    student.setFirstName(firstName);
    student.setLastName(lastName);
    student.setNickname(nickname);
    student.setAdditionalInfo(additionalInfo);
    student.setStudyTimeEnd(studyTimeEnd);
    student.setActivityType(activityType);
    student.setExaminationType(examinationType);
    student.setEducationalLevel(educationalLevel);
    student.setEducation(education);
    student.setNationality(nationality);
    student.setMunicipality(municipality);
    student.setLanguage(language);
    student.setSchool(school);
    student.setStudyProgramme(studyProgramme);
    student.setCurriculum(curriculum);
    student.setPreviousStudies(previousStudies);
    student.setStudyStartDate(studyStartDate);
    student.setStudyEndDate(studyEndDate);
    student.setStudyEndReason(studyEndReason);
    student.setStudyEndText(studyEndText);
    student.setLodging(lodging);
    student.setContactInfo(contactInfo);
    student.setArchived(archived);

    entityManager.persist(student);

    person.addUser(student);

    entityManager.persist(person);
    
    studentCreatedEvent.fire(new StudentCreatedEvent(student.getId()));

    return student;
  }
  
  public void update(Student student, String firstName, String lastName, String nickname, String additionalInfo,
      Date studyTimeEnd, StudentActivityType activityType, StudentExaminationType examinationType, StudentEducationalLevel educationalLevel, 
      String education, Nationality nationality, Municipality municipality, Language language, School school, StudyProgramme studyProgramme, 
      Curriculum curriculum, Double previousStudies, Date studyStartDate, Date studyEndDate, StudentStudyEndReason studyEndReason, 
      String studyEndText, Boolean lodging) {
    EntityManager entityManager = getEntityManager();

    student.setFirstName(firstName);
    student.setLastName(lastName);
    student.setNickname(nickname);
    student.setAdditionalInfo(additionalInfo);
    student.setStudyTimeEnd(studyTimeEnd);
    student.setActivityType(activityType);
    student.setExaminationType(examinationType);
    student.setEducationalLevel(educationalLevel);
    student.setEducation(education);
    student.setNationality(nationality);
    student.setMunicipality(municipality);
    student.setLanguage(language);
    student.setSchool(school);
    student.setStudyProgramme(studyProgramme);
    student.setPreviousStudies(previousStudies);
    student.setStudyStartDate(studyStartDate);
    student.setStudyEndDate(studyEndDate);
    student.setStudyEndReason(studyEndReason);
    student.setStudyEndText(studyEndText);
    student.setLodging(lodging);
    student.setCurriculum(curriculum);

    entityManager.persist(student);
    
    studentUpdatedEvent.fire(new StudentUpdatedEvent(student.getId()));
  }

  public void updateStudentMunicipality(Student student, Municipality municipality) {
    EntityManager entityManager = getEntityManager();

    student.setMunicipality(municipality);

    entityManager.persist(student);

    studentUpdatedEvent.fire(new StudentUpdatedEvent(student.getId()));
  }

  public void updateSchool(Student student, School school) {
    EntityManager entityManager = getEntityManager();
    student.setSchool(school);
    entityManager.persist(student);
    studentUpdatedEvent.fire(new StudentUpdatedEvent(student.getId()));
  }

  public void updateCurriculum(Student student, Curriculum curriculum) {
    EntityManager entityManager = getEntityManager();
    student.setCurriculum(curriculum);
    entityManager.persist(student);
    studentUpdatedEvent.fire(new StudentUpdatedEvent(student.getId()));
  }

  public Student setStudentTags(Student student, Set<Tag> tags) {
    EntityManager entityManager = getEntityManager();
    
    student.setTags(tags);
    
    entityManager.persist(student);

    studentUpdatedEvent.fire(new StudentUpdatedEvent(student.getId()));
    
    return student;
  }

  public void endStudentStudies(Student student, Date endDate, StudentStudyEndReason endReason, String endReasonText) {
    EntityManager entityManager = getEntityManager();
    student.setStudyEndDate(endDate);
    student.setStudyEndReason(endReason);
    student.setStudyEndText(endReasonText);
    entityManager.persist(student);

    studentUpdatedEvent.fire(new StudentUpdatedEvent(student.getId()));
  }
  
  public Long countByStudyEndReason(StudentStudyEndReason studyEndReason) {
    EntityManager entityManager = getEntityManager();
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
    Root<Student> root = criteria.from(Student.class);
    criteria.select(criteriaBuilder.count(root));
    criteria.where(
        criteriaBuilder.equal(root.get(Student_.studyEndReason), studyEndReason)
    );
    
    return entityManager.createQuery(criteria).getSingleResult();
  }

  public List<Student> listActiveStudents() {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Student> criteria = criteriaBuilder.createQuery(Student.class);
    Root<Student> root = criteria.from(Student.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(Student_.archived), Boolean.FALSE),
            criteriaBuilder.or(
                criteriaBuilder.isNull(root.get(Student_.studyEndDate)),
                criteriaBuilder.greaterThan(root.get(Student_.studyEndDate), new Date())
            )
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<Student> listByStudyProgramme(StudyProgramme studyProgramme) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Student> criteria = criteriaBuilder.createQuery(Student.class);
    Root<Student> root = criteria.from(Student.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(Student_.archived), Boolean.FALSE),
            criteriaBuilder.equal(root.get(Student_.studyProgramme), studyProgramme)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<Student> listActiveStudentsByStudyProgramme(StudyProgramme studyProgramme) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Student> criteria = criteriaBuilder.createQuery(Student.class);
    Root<Student> root = criteria.from(Student.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(Student_.archived), Boolean.FALSE),
            criteriaBuilder.equal(root.get(Student_.studyProgramme), studyProgramme),
            criteriaBuilder.or(
                criteriaBuilder.isNull(root.get(Student_.studyEndDate)),
                criteriaBuilder.greaterThan(root.get(Student_.studyEndDate), new Date())
            )
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  public List<Student> listByPerson(Person person) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Student> criteria = criteriaBuilder.createQuery(Student.class);
    Root<Student> root = criteria.from(Student.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(Student_.archived), Boolean.FALSE),
            criteriaBuilder.equal(root.get(Student_.person), person)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<Student> listActiveStudentsByPerson(Person person) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Student> criteria = criteriaBuilder.createQuery(Student.class);
    Root<Student> root = criteria.from(Student.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(Student_.archived), Boolean.FALSE),
            criteriaBuilder.equal(root.get(Student_.person), person),
            criteriaBuilder.or(
                criteriaBuilder.isNull(root.get(Student_.studyEndDate)),
                criteriaBuilder.greaterThan(root.get(Student_.studyEndDate), new Date())
            )
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<Student> listByUserVariable(String key, String value) {
    UserVariableKeyDAO variableKeyDAO = DAOFactory.getInstance().getUserVariableKeyDAO();
    UserVariableKey UserVariableKey = variableKeyDAO.findByVariableKey(key);
    
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Student> criteria = criteriaBuilder.createQuery(Student.class);
    Root<UserVariable> variable = criteria.from(UserVariable.class);
    Root<Student> student = criteria.from(Student.class);

    criteria.select(student);
    criteria.where(
      criteriaBuilder.and(
          criteriaBuilder.equal(student, variable.get(UserVariable_.user)),
          criteriaBuilder.equal(student.get(Student_.archived), Boolean.FALSE),
          criteriaBuilder.equal(variable.get(UserVariable_.key), UserVariableKey),
          criteriaBuilder.equal(variable.get(UserVariable_.value), value)
      )
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<Student> listByEmail(String email) {
    return listByEmail(email, null, null);
  }
  
  public List<Student> listByEmail(String email, Integer firstResult, Integer maxResults) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Student> criteria = criteriaBuilder.createQuery(Student.class);
    Root<Student> root = criteria.from(Student.class);
    Join<Student, ContactInfo> contactInfoJoin = root.join(Student_.contactInfo);
    ListJoin<ContactInfo, Email> emailJoin = contactInfoJoin.join(ContactInfo_.emails);
    
    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(emailJoin.get(Email_.address), email)
    );
    
    TypedQuery<Student> query = entityManager.createQuery(criteria);
    
    if (firstResult != null) {
      query.setFirstResult(firstResult);
    }
   
    if (maxResults != null) {
      query.setMaxResults(maxResults);
    }
  
    return query.getResultList();
  }

  public List<Student> listByEmailAndArchived(String email, Boolean archived) {
    return listByEmailAndArchived(email, archived, null, null);
  }
  
  public List<Student> listByEmailAndArchived(String email, Boolean archived, Integer firstResult, Integer maxResults) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Student> criteria = criteriaBuilder.createQuery(Student.class);
    Root<Student> root = criteria.from(Student.class);
    Join<Student, ContactInfo> contactInfoJoin = root.join(Student_.contactInfo);
    ListJoin<ContactInfo, Email> emailJoin = contactInfoJoin.join(ContactInfo_.emails);
    
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(Student_.archived), archived),
        criteriaBuilder.equal(emailJoin.get(Email_.address), email)
      )
    );
  
    TypedQuery<Student> query = entityManager.createQuery(criteria);
    
    if (firstResult != null) {
      query.setFirstResult(firstResult);
    }
   
    if (maxResults != null) {
      query.setMaxResults(maxResults);
    }
  
    return query.getResultList();
  }
  
  public Student updatePerson(Student student, Person person) {
    Person oldPerson = student.getPerson();
    if (oldPerson != null) {
      oldPerson.removeUser(student);
      getEntityManager().persist(oldPerson);
    }
    
    if (person != null) {
      person.addUser(student);
      getEntityManager().persist(person);
    }

    persist(student);

    studentUpdatedEvent.fire(new StudentUpdatedEvent(student.getId()));

    return student;
  }

  public Student removeTag(Student student, Tag tag) {
    student.removeTag(tag);
    persist(student);

    studentUpdatedEvent.fire(new StudentUpdatedEvent(student.getId()));

    return student;
  }

  public Student addTag(Student student, Tag tag) {
    student.addTag(tag);
    persist(student);

    studentUpdatedEvent.fire(new StudentUpdatedEvent(student.getId()));

    return student;
  }
  
  @Override
  public void delete(Student e) {
    Person person = e.getPerson();
    
    person.removeUser(e);
    getEntityManager().persist(person);
    
    super.delete(e);
  }
}
