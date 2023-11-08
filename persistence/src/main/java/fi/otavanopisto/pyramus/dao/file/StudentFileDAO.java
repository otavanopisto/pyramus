package fi.otavanopisto.pyramus.dao.file;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.TSB;
import fi.otavanopisto.pyramus.domainmodel.file.FileType;
import fi.otavanopisto.pyramus.domainmodel.file.StudentFile;
import fi.otavanopisto.pyramus.domainmodel.file.StudentFile_;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Stateless
public class StudentFileDAO extends PyramusEntityDAO<StudentFile> {

  public StudentFile create(Student student, String name, String fileName, String fileId, FileType fileType, String contentType, byte[] data, User creator) {
    EntityManager em = getEntityManager();
    Date created = new Date();
    
    StudentFile studentFile = new StudentFile();

    studentFile.setStudent(student);
    studentFile.setName(name);
    studentFile.setFileName(fileName);
    studentFile.setFileId(fileId);
    studentFile.setFileType(fileType);

    studentFile.setContentType(contentType);
    studentFile.setData(data);

    studentFile.setCreated(created);
    studentFile.setCreator(creator);
    studentFile.setLastModified(created);
    studentFile.setLastModifier(creator);
    
    em.persist(studentFile);

    return studentFile;
  }
  
  public StudentFile findByFileId(String fileId) {
    EntityManager entityManager = getEntityManager(); 
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentFile> criteria = criteriaBuilder.createQuery(StudentFile.class);
    Root<StudentFile> root = criteria.from(StudentFile.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(StudentFile_.fileId), fileId)
    );
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public List<Long> listIdsByLargerAndLimit(Long id, int count) {
    EntityManager entityManager = getEntityManager();
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
    Root<StudentFile> root = criteria.from(StudentFile.class);
    criteria.select(root.get(StudentFile_.id));
    criteria.where(
      criteriaBuilder.greaterThan(root.get(StudentFile_.id), id)
    );
    criteria.orderBy(criteriaBuilder.asc(root.get(StudentFile_.id)));
    TypedQuery<Long> query = entityManager.createQuery(criteria);
    query.setMaxResults(count);
    return query.getResultList();
  }
  
  public StudentFile updateBasicInfo(StudentFile studentFile, String name, String fileName, FileType fileType, User modifier) {
    EntityManager em = getEntityManager();
    
    studentFile.setName(name);
    studentFile.setFileName(fileName);
    studentFile.setFileType(fileType);

    studentFile.setLastModified(new Date());
    studentFile.setLastModifier(modifier);
    
    em.persist(studentFile);

    return studentFile;
  }
  
  public StudentFile updateData(StudentFile studentFile, String contentType, String fileId, byte[] data, User modifier) {
    EntityManager em = getEntityManager();
    
    studentFile.setContentType(contentType);
    studentFile.setFileId(fileId);
    studentFile.setData(data);
    studentFile.setLastModified(new Date());
    studentFile.setLastModifier(modifier);
    
    em.persist(studentFile);

    return studentFile;
  }
  
  /**
   * Don't use this directly, use PyramusFileUtils.moveFileForNewStudent
   */
  public StudentFile updateStudent(StudentFile studentFile, Student student, User modifier) {
    if (studentFile.getStudent() == null || student == null || !studentFile.getStudent().getPerson().getId().equals(student.getPerson().getId())) {
      throw new IllegalArgumentException("Cannot move files between different persons.");
    }
    
    EntityManager em = getEntityManager();

    studentFile.setStudent(student);
    studentFile.setLastModified(new Date());
    studentFile.setLastModifier(modifier);
  
    em.persist(studentFile);

    return studentFile;
  }
  
  public List<StudentFile> listByStudent(Student student) {
    return listByStudent(student, TSB.FALSE);
  }

  public List<StudentFile> listByStudent(Student student, TSB archived) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentFile> criteria = criteriaBuilder.createQuery(StudentFile.class);
    Root<StudentFile> root = criteria.from(StudentFile.class);
    criteria.select(root);
    
    if (archived.isBoolean()) {
      criteria.where(
          criteriaBuilder.and(
              criteriaBuilder.equal(root.get(StudentFile_.archived), archived.booleanValue()),
              criteriaBuilder.equal(root.get(StudentFile_.student), student)));
    } else {
      criteria.where(criteriaBuilder.equal(root.get(StudentFile_.student), student));
    }
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<Student> listPastStudentsWithFiles(Date studentStudyEndThreshold, int maxResults) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Student> criteria = criteriaBuilder.createQuery(Student.class);
    Root<StudentFile> root = criteria.from(StudentFile.class);
    Join<StudentFile, Student> student = root.join(StudentFile_.student);

    criteria.select(student).distinct(true);
    criteria.where(getPastStudentPredicate(criteriaBuilder, student, studentStudyEndThreshold));
    
    return entityManager.createQuery(criteria).setMaxResults(maxResults).getResultList();
  }
  
}
