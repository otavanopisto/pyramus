package fi.otavanopisto.pyramus.dao.students;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntry;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntryType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntry_;

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
  public StudentContactLogEntry create(Student student, StudentContactLogEntryType type, String text, Date entryDate, String creator) {
    EntityManager entityManager = getEntityManager(); 

    StudentContactLogEntry entry = new StudentContactLogEntry();
    entry.setStudent(student);
    entry.setCreatorName(creator);
    entry.setEntryDate(entryDate);
    entry.setText(text);
    entry.setType(type);

    entityManager.persist(entry);
    return entry;
  }

  public StudentContactLogEntry update(StudentContactLogEntry entry, StudentContactLogEntryType type, 
      String text, Date entryDate, String creator) {
    EntityManager entityManager = getEntityManager(); 

    entry.setType(type);
    entry.setText(text);
    entry.setEntryDate(entryDate);
    entry.setCreatorName(creator);

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
  
}
