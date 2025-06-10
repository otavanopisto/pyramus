package fi.otavanopisto.pyramus.rest.controller;

import java.util.Date;
import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

import fi.otavanopisto.pyramus.dao.students.StudentContactLogEntryCommentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentContactLogEntryDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntry;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntryComment;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntryType;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;

@Dependent
@Stateless
public class StudentContactLogEntryController {
  @Inject
  private StudentContactLogEntryDAO contactLogEntryDAO;
  
  @Inject
  private StudentContactLogEntryCommentDAO contactLogEntryCommentDAO;

  public StudentContactLogEntry createContactLogEntry(Student student, StudentContactLogEntryType type, String text, Date entryDate, String creatorName, StaffMember creator) {
    return contactLogEntryDAO.create(student, type, text, entryDate, creatorName, creator);
  }
  
  public StudentContactLogEntry findContactLogEntryById(Long id) {
    return contactLogEntryDAO.findById(id);
  }

  public SearchResult<StudentContactLogEntry> listContactLogEntriesByStudent(Student student, Integer firstResult, Integer maxResults) {
    return contactLogEntryDAO.listByStudent(student, firstResult, maxResults);
  }
  
  public SearchResult<StudentContactLogEntry> listContactLogEntriesByStudentAndCreator(Student student, StaffMember creator,Integer firstResult, Integer maxResults) {
    return contactLogEntryDAO.listByStudentAndCreator(student, creator, firstResult, maxResults);
  }

  public StudentContactLogEntry updateContactLogEntry(StudentContactLogEntry entry, StudentContactLogEntryType type, String text, Date entryDate) {
    return contactLogEntryDAO.update(entry, type, text, entryDate);
  }

  public void archiveStudentContactLogEntry(StudentContactLogEntry contactLogEntry, User loggedUser) {
    
    List<StudentContactLogEntryComment> comments = contactLogEntryCommentDAO.listByEntry(contactLogEntry);
    
    for (StudentContactLogEntryComment comment : comments) {
      contactLogEntryCommentDAO.archive(comment, loggedUser);
    }
    contactLogEntryDAO.archive(contactLogEntry, loggedUser);
  }

  public void deleteStudentContactLogEntry(StudentContactLogEntry contactLogEntry) {
    
    List<StudentContactLogEntryComment> comments = contactLogEntryCommentDAO.listByEntry(contactLogEntry);
    
    for (StudentContactLogEntryComment comment : comments) {
      contactLogEntryCommentDAO.delete(comment);
    }
    contactLogEntryDAO.delete(contactLogEntry);
  }
  
}
