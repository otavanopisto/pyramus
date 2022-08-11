package fi.otavanopisto.pyramus.dao.students;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntry;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntryType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntry_;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;

@Stateless
public class StudentContactLogEntryDAO extends PyramusEntityDAO<StudentContactLogEntry> {

  /**
   * Creates new contact log entry for a student.
   * 
   * @param student
   *          Student
   * @param type
   *          Type for the new entry
   * @param text
   *          Text for then new entry
   * @param entryDate
   *          Entry date for the new entry
   * @param creator
   *          Creator for the new entry
   * @return The new entry
   */
  public StudentContactLogEntry create(Student student, StudentContactLogEntryType type, String text, Date entryDate, String creatorName, StaffMember creator) {
    EntityManager entityManager = getEntityManager(); 

    StudentContactLogEntry entry = new StudentContactLogEntry();
    entry.setStudent(student);
    entry.setCreatorName(creatorName);
    entry.setEntryDate(entryDate);
    entry.setText(text);
    entry.setType(type);
    entry.setCreator(creator);

    entityManager.persist(entry);
    return entry;
  }

  public StudentContactLogEntry update(StudentContactLogEntry entry, StudentContactLogEntryType type, 
      String text, Date entryDate) {
    EntityManager entityManager = getEntityManager(); 

    entry.setType(type);
    entry.setText(text);
    entry.setEntryDate(entryDate);

    entityManager.persist(entry);
    return entry;
  }
  
  /**
   * Lists all contact log entries for given student.
   * 
   * @param student
   *          Student to list contact entries for.
   * @return List with StudentContactLogEntry items belonging to the student.
   */
  public List<StudentContactLogEntry> listByStudent(Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentContactLogEntry> criteria = criteriaBuilder.createQuery(StudentContactLogEntry.class);
    Root<StudentContactLogEntry> root = criteria.from(StudentContactLogEntry.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(StudentContactLogEntry_.archived), Boolean.FALSE),
            criteriaBuilder.equal(root.get(StudentContactLogEntry_.student), student)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  /**
   * Lists all contact log entries for given student.
   * 
   * @param student
   *          Student to list contact entries for.
   * @return List with StudentContactLogEntry items belonging to the student.
   */
  public SearchResult<StudentContactLogEntry> listByStudent(Student student, int resultsPerPage, int page) {
    int firstResult = page * resultsPerPage;

    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentContactLogEntry> criteria = criteriaBuilder.createQuery(StudentContactLogEntry.class);
    Root<StudentContactLogEntry> root = criteria.from(StudentContactLogEntry.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(StudentContactLogEntry_.archived), Boolean.FALSE),
            criteriaBuilder.equal(root.get(StudentContactLogEntry_.student), student)
        ));
    
    criteria.orderBy(criteriaBuilder.desc(root.get(StudentContactLogEntry_.entryDate)));

    TypedQuery<StudentContactLogEntry> query = entityManager.createQuery(criteria);
    
    int hits = query.getResultList().size();

    query.setFirstResult(firstResult);
    query.setMaxResults(resultsPerPage);
    
    int pages = hits / resultsPerPage;
    if (hits % resultsPerPage > 0)
      pages++;

    int lastResult = Math.min(firstResult + resultsPerPage, hits) - 1;

    return new SearchResult<>(page, pages, hits, firstResult, lastResult, query.getResultList());
  }
  
  public SearchResult<StudentContactLogEntry> listByStudentAndCreator(Student student, StaffMember staffMember, int resultsPerPage, int page) {
    int firstResult = page * resultsPerPage;

    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentContactLogEntry> criteria = criteriaBuilder.createQuery(StudentContactLogEntry.class);
    Root<StudentContactLogEntry> root = criteria.from(StudentContactLogEntry.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(StudentContactLogEntry_.archived), Boolean.FALSE),
            criteriaBuilder.equal(root.get(StudentContactLogEntry_.student), student),
            criteriaBuilder.equal(root.get(StudentContactLogEntry_.creator), staffMember)
        ));
    
    criteria.orderBy(criteriaBuilder.desc(root.get(StudentContactLogEntry_.entryDate)));

    TypedQuery<StudentContactLogEntry> query = entityManager.createQuery(criteria);
    int hits = query.getResultList().size();
    
    query.setFirstResult(firstResult);
    query.setMaxResults(resultsPerPage);
    
    int pages = hits / resultsPerPage;
    if (hits % resultsPerPage > 0)
      pages++;

    int lastResult = Math.min(firstResult + resultsPerPage, hits) - 1;

    return new SearchResult<>(page, pages, hits, firstResult, lastResult, query.getResultList());
  }
  
}
