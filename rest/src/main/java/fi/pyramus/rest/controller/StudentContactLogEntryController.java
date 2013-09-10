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

@Dependent
@Stateless
public class StudentContactLogEntryController {
  @Inject
  private StudentContactLogEntryDAO contactLogEntryDAO;

  public StudentContactLogEntry createContactLogEntry(Student student, StudentContactLogEntryType type, String text, Date entryDate, String creator) {
    StudentContactLogEntry contactLogEntry = contactLogEntryDAO.create(student, type, text, entryDate, creator);
    return contactLogEntry;
  }

  public List<StudentContactLogEntry> findContactLogEntriesByStudent(Student student) {
    List<StudentContactLogEntry> contactLogEntries = contactLogEntryDAO.listByStudent(student);
    return contactLogEntries;
  }

  public StudentContactLogEntry findContactLogEntryByIdAndStudent(Long id, Student student) {
    List<StudentContactLogEntry> contactLogEntries = contactLogEntryDAO.listByStudent(student);
    for (StudentContactLogEntry contactLogEntry : contactLogEntries) {
      if (contactLogEntry.getId().equals(id)) {
        return contactLogEntry;
      }
    }
    return null;
  }

  public StudentContactLogEntry updateContactLogEntry(StudentContactLogEntry entry, StudentContactLogEntryType type, String text, Date entryDate, String creator) {
    StudentContactLogEntry updated = contactLogEntryDAO.update(entry, type, text, entryDate, creator);
    return updated;
  }
}
