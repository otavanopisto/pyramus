package fi.otavanopisto.pyramus.dao.students;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.TSB;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntry;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntryComment;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntryComment_;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;

@Stateless
public class StudentContactLogEntryCommentDAO extends PyramusEntityDAO<StudentContactLogEntryComment> {

  public StudentContactLogEntryComment create(
      StudentContactLogEntry entry, String commentText, Date commentDate,
      String commentCreatorName, StaffMember commentCreator) {
    EntityManager entityManager = getEntityManager(); 

    StudentContactLogEntryComment comment = new StudentContactLogEntryComment();
    comment.setEntry(entry);
    comment.setCreatorName(commentCreatorName);
    comment.setCommentDate(commentDate);
    comment.setText(commentText);
    comment.setCreator(commentCreator);

    entityManager.persist(comment);
    return comment;
  }
  
  public StudentContactLogEntryComment update(StudentContactLogEntryComment comment, 
      String commentText, Date commentDate) {
    EntityManager entityManager = getEntityManager(); 
    comment.setText(commentText);
    comment.setCommentDate(commentDate);
    entityManager.persist(comment);
    return comment;
  }
  
  public List<StudentContactLogEntryComment> listByEntry(StudentContactLogEntry entry) {
    return listByEntry(entry, TSB.FALSE);
  }

  public List<StudentContactLogEntryComment> listByEntry(StudentContactLogEntry entry, TSB archived) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentContactLogEntryComment> criteria = criteriaBuilder.createQuery(StudentContactLogEntryComment.class);
    Root<StudentContactLogEntryComment> root = criteria.from(StudentContactLogEntryComment.class);
    criteria.select(root);
    
    if (archived.isBoolean()) {
      criteria.where(
          criteriaBuilder.and(
              criteriaBuilder.equal(root.get(StudentContactLogEntryComment_.archived), Boolean.FALSE),
              criteriaBuilder.equal(root.get(StudentContactLogEntryComment_.entry), entry)
          ));
    } else {
      criteria.where(criteriaBuilder.equal(root.get(StudentContactLogEntryComment_.entry), entry));
    }
    
    return entityManager.createQuery(criteria).getResultList();
  }

}
