package fi.pyramus.dao.students;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.students.StudentContactLogEntryType;
import fi.pyramus.domainmodel.students.StudentGroup;
import fi.pyramus.domainmodel.students.StudentGroupContactLogEntry;
import fi.pyramus.domainmodel.students.StudentGroupContactLogEntry_;

@Stateless
public class StudentGroupContactLogEntryDAO extends PyramusEntityDAO<StudentGroupContactLogEntry> {

  /**
   * Creates new contact log entry for a student group.
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
  public StudentGroupContactLogEntry create(StudentGroup studentGroup, StudentContactLogEntryType type, String text, Date entryDate, String creator) {
    StudentGroupContactLogEntry entry = new StudentGroupContactLogEntry();
    entry.setStudentGroup(studentGroup);
    entry.setCreatorName(creator);
    entry.setEntryDate(entryDate);
    entry.setText(text);
    entry.setType(type);
    entry.setArchived(false);

    return persist(entry);
  }

  public StudentGroupContactLogEntry update(StudentGroupContactLogEntry entry, StudentContactLogEntryType type, 
      String text, Date entryDate, String creator) {
    entry.setType(type);
    entry.setText(text);
    entry.setEntryDate(entryDate);
    entry.setCreatorName(creator);

    return persist(entry);
  }
  
  /**
   * Lists all contact log entries for given student.
   * 
   * @param student
   *          Student to list contact entries for.
   * @return List with StudentContactLogEntry items belonging to the student.
   */
  public List<StudentGroupContactLogEntry> listByStudentGroup(StudentGroup studentGroup) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentGroupContactLogEntry> criteria = criteriaBuilder.createQuery(StudentGroupContactLogEntry.class);
    Root<StudentGroupContactLogEntry> root = criteria.from(StudentGroupContactLogEntry.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(StudentGroupContactLogEntry_.archived), Boolean.FALSE),
            criteriaBuilder.equal(root.get(StudentGroupContactLogEntry_.studentGroup), studentGroup)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
}
