package fi.pyramus.rest.controller;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.pyramus.dao.students.StudentContactLogEntryDAO;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.students.StudentContactLogEntry;
import fi.pyramus.domainmodel.students.StudentContactLogEntryType;
import fi.pyramus.domainmodel.users.User;

@Dependent
@Stateless
public class StudentContactLogEntryController {
  @Inject
  private StudentContactLogEntryDAO contactLogEntryDAO;

  public StudentContactLogEntry createContactLogEntry(Student student, StudentContactLogEntryType type, String text, Date entryDate, String creator) {
    StudentContactLogEntry contactLogEntry = contactLogEntryDAO.create(student, type, text, entryDate, creator);
    return contactLogEntry;
  }
  
  public StudentContactLogEntry findContactLogEntryById(Long id) {
    return contactLogEntryDAO.findById(id);
  }

  public List<StudentContactLogEntry> listContactLogEntriesByStudent(Student student) {
    List<StudentContactLogEntry> contactLogEntries = contactLogEntryDAO.listByStudent(student);
    return contactLogEntries;
  }

  public StudentContactLogEntry updateContactLogEntry(StudentContactLogEntry entry, StudentContactLogEntryType type, String text, Date entryDate, String creator) {
    StudentContactLogEntry updated = contactLogEntryDAO.update(entry, type, text, entryDate, creator);
    return updated;
  }

  public void archiveStudentContactLogEntry(StudentContactLogEntry contactLogEntry, User loggedUser) {
    contactLogEntryDAO.archive(contactLogEntry, loggedUser);
  }

  public void deleteStudentContactLogEntry(StudentContactLogEntry contactLogEntry) {
    contactLogEntryDAO.delete(contactLogEntry);
  }
  
}
