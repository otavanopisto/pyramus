package fi.otavanopisto.pyramus.rest.controller;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.students.StudentContactLogEntryDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntry;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntryType;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Dependent
@Stateless
public class StudentContactLogEntryController {
  @Inject
  private StudentContactLogEntryDAO contactLogEntryDAO;

  public StudentContactLogEntry createContactLogEntry(Student student, StudentContactLogEntryType type, String text, Date entryDate, String creator, Long creatorId) {
    StudentContactLogEntry contactLogEntry = contactLogEntryDAO.create(student, type, text, entryDate, creator, creatorId);
    return contactLogEntry;
  }
  
  public StudentContactLogEntry findContactLogEntryById(Long id) {
    return contactLogEntryDAO.findById(id);
  }

  public List<StudentContactLogEntry> listContactLogEntriesByStudent(Student student) {
    List<StudentContactLogEntry> contactLogEntries = contactLogEntryDAO.listByStudent(student);
    return contactLogEntries;
  }

  public StudentContactLogEntry updateContactLogEntry(StudentContactLogEntry entry, StudentContactLogEntryType type, String text, Date entryDate) {
    StudentContactLogEntry updated = contactLogEntryDAO.update(entry, type, text, entryDate);
    return updated;
  }

  public void archiveStudentContactLogEntry(StudentContactLogEntry contactLogEntry, User loggedUser) {
    contactLogEntryDAO.archive(contactLogEntry, loggedUser);
  }

  public void deleteStudentContactLogEntry(StudentContactLogEntry contactLogEntry) {
    contactLogEntryDAO.delete(contactLogEntry);
  }
  
}
