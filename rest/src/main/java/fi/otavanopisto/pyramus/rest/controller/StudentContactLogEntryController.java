package fi.otavanopisto.pyramus.rest.controller;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.students.StudentContactLogEntryCommentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentContactLogEntryDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntry;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntryComment;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntryType;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Dependent
@Stateless
public class StudentContactLogEntryController {
  @Inject
  private StudentContactLogEntryDAO contactLogEntryDAO;
  
  @Inject
  private StudentContactLogEntryCommentDAO contactLogEntryCommentDAO;

  public StudentContactLogEntry createContactLogEntry(Student student, StudentContactLogEntryType type, String text, Date entryDate, String creatorName, StaffMember creator) {
    StudentContactLogEntry contactLogEntry = contactLogEntryDAO.create(student, type, text, entryDate, creatorName, creator);
    return contactLogEntry;
  }
  
  public StudentContactLogEntry findContactLogEntryById(Long id) {
    return contactLogEntryDAO.findById(id);
  }

  public List<StudentContactLogEntry> listContactLogEntriesByStudent(Student student) {
    List<StudentContactLogEntry> contactLogEntries = contactLogEntryDAO.listByStudent(student);
    return contactLogEntries;
  }
  
  public List<StudentContactLogEntry> listContactLogEntriesByStudentAndCreator(Student student, StaffMember creator) {
    List<StudentContactLogEntry> contactLogEntries = contactLogEntryDAO.listByStudentAndCreator(student, creator);
    return contactLogEntries;
  }

  public StudentContactLogEntry updateContactLogEntry(StudentContactLogEntry entry, StudentContactLogEntryType type, String text, Date entryDate) {
    StudentContactLogEntry updated = contactLogEntryDAO.update(entry, type, text, entryDate);
    return updated;
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
