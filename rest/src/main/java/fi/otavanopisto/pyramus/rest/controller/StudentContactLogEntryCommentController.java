package fi.otavanopisto.pyramus.rest.controller;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.students.StudentContactLogEntryCommentDAO;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntry;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntryComment;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Dependent
@Stateless
public class StudentContactLogEntryCommentController {
  @Inject
  private StudentContactLogEntryCommentDAO contactLogEntryCommentDAO;

  public StudentContactLogEntryComment createContactLogEntryComment(StudentContactLogEntry entry, String commentText, Date commentDate, String commentCreatorName, Long creatorId) {
    StudentContactLogEntryComment contactLogEntryComment = contactLogEntryCommentDAO.create(entry, commentText, commentDate, commentCreatorName, creatorId);
    return contactLogEntryComment;
  }
  
  public StudentContactLogEntryComment findContactLogEntryCommentById(Long id) {
    return contactLogEntryCommentDAO.findById(id);
  }

  public List<StudentContactLogEntryComment> listContactLogEntryCommentsByEntry(StudentContactLogEntry entry) {
    List<StudentContactLogEntryComment> contactLogEntryComments = contactLogEntryCommentDAO.listByEntry(entry);
    return contactLogEntryComments;
  }

  public StudentContactLogEntryComment updateContactLogEntryComment(StudentContactLogEntryComment comment, String commentText, Date commentDate) {
    StudentContactLogEntryComment updated = contactLogEntryCommentDAO.update(comment, commentText, commentDate);
    return updated;
  }

  public void archiveStudentContactLogEntryComment(StudentContactLogEntryComment comment, User loggedUser) {
    contactLogEntryCommentDAO.archive(comment, loggedUser);
  }

  public void deleteStudentContactLogEntryComment(StudentContactLogEntryComment comment) {
    contactLogEntryCommentDAO.delete(comment);
  }
  
}
