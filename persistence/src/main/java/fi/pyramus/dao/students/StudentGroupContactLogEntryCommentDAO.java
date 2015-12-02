package fi.pyramus.dao.students;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.students.StudentGroupContactLogEntry;
import fi.pyramus.domainmodel.students.StudentGroupContactLogEntryComment;
import fi.pyramus.domainmodel.students.StudentGroupContactLogEntryComment_;

@Stateless
public class StudentGroupContactLogEntryCommentDAO extends PyramusEntityDAO<StudentGroupContactLogEntryComment> {

  public StudentGroupContactLogEntryComment create(
      StudentGroupContactLogEntry entry, String commentText, Date commentDate,
      String commentCreatorName) {
    StudentGroupContactLogEntryComment comment = new StudentGroupContactLogEntryComment();
    comment.setEntry(entry);
    comment.setCreatorName(commentCreatorName);
    comment.setCommentDate(commentDate);
    comment.setText(commentText);
    comment.setArchived(false);

    return persist(comment);
  }
  
  public StudentGroupContactLogEntryComment update(StudentGroupContactLogEntryComment comment, 
      String commentText, Date commentDate, String commentCreatorName) {
    comment.setText(commentText);
    comment.setCommentDate(commentDate);
    comment.setCreatorName(commentCreatorName);
    return persist(comment);
  }
  
  public List<StudentGroupContactLogEntryComment> listByEntry(StudentGroupContactLogEntry entry) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentGroupContactLogEntryComment> criteria = criteriaBuilder.createQuery(StudentGroupContactLogEntryComment.class);
    Root<StudentGroupContactLogEntryComment> root = criteria.from(StudentGroupContactLogEntryComment.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(StudentGroupContactLogEntryComment_.archived), Boolean.FALSE),
            criteriaBuilder.equal(root.get(StudentGroupContactLogEntryComment_.entry), entry)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
}
