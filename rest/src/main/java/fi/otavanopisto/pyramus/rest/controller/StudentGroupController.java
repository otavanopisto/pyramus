package fi.otavanopisto.pyramus.rest.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.base.TagDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupStudentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupUserDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupStudent;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupUser;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Dependent
@Stateless
public class StudentGroupController {

  @Inject
  private StudentGroupDAO studentGroupDAO;

  @Inject
  private StudentGroupStudentDAO studentGroupStudentDAO;
  
  @Inject
  private StudentGroupUserDAO studentGroupUserDAO;
  
  @Inject
  private TagDAO tagDAO;
  
  public StudentGroup createStudentGroup(String name, String description, Date beginDate, User user) {
    StudentGroup studentGroup = studentGroupDAO.create(name, description, beginDate, user);
    return studentGroup;
  }
  
  public Tag createStudentGroupTag(StudentGroup studentGroup, String text) {
    Tag tag = tagDAO.findByText(text);
    if (tag == null) {
      tag = tagDAO.create(text);
    }
    studentGroup.addTag(tag);
    return tag;
  }
  
  public List<StudentGroup> listStudentGroups() {
    return studentGroupDAO.listAll();
  }

  public List<StudentGroup> listStudentGroups(Integer firstResult, Integer maxResults) {
    return studentGroupDAO.listAll(firstResult, maxResults);
  }
  
  public List<StudentGroup> listUnarchivedStudentGroups() {
    return studentGroupDAO.listUnarchived();
  }

  public List<StudentGroup> listUnarchivedStudentGroups(Integer firstResult, Integer maxResults) {
    return studentGroupDAO.listUnarchived(firstResult, maxResults);
  }
  
  public StudentGroup findStudentGroupById(Long id) {
    return studentGroupDAO.findById(id);
  }
  
  public Set<Tag> findStudentGroupTags(StudentGroup studentGroup) {
    return studentGroup.getTags();
  }
  
  public StudentGroup updateStudentGroup(StudentGroup studentGroup, String name, String description, Date beginDate, User user) {
    return studentGroupDAO.update(studentGroup, name, description, beginDate, user);
  }
  
  public StudentGroup archiveStudentGroup(StudentGroup studentGroup, User user) {
    studentGroupDAO.archive(studentGroup, user);
    return studentGroup;
  }
  
  public StudentGroup unarchiveStudentGroup(StudentGroup studentGroup, User user) {
    studentGroupDAO.unarchive(studentGroup, user);
    return studentGroup;
  }

  public StudentGroup updateStudentGroupTags(StudentGroup studentGroup, List<String> tags) {
    Set<String> newTags = new HashSet<>(tags);
    Set<Tag> studentGroupTags = new HashSet<>(studentGroup.getTags());
    
    for (Tag studentGroupTag : studentGroupTags) {
      if (!newTags.contains(studentGroupTag.getText())) {
        removeStudentGroupTag(studentGroup, studentGroupTag);
      }
        
      newTags.remove(studentGroupTag.getText());
    }
    
    for (String newTag : newTags) {
      createStudentGroupTag(studentGroup, newTag);
    }
    
    return studentGroup;
  }

  public void removeStudentGroupTag(StudentGroup studentGroup, Tag tag) {
    studentGroup.removeTag(tag);
  }

  public void deleteStudentGroup(StudentGroup studentGroup) {
    studentGroupDAO.delete(studentGroup);
  }
  
  /* StudentGroupStudents */

  public StudentGroupStudent findStudentGroupStudentById(Long id) {
    return studentGroupStudentDAO.findById(id);
  }

  public void deleteStudentGroupStudent(StudentGroupStudent studentGroupStudent) {
    studentGroupStudentDAO.delete(studentGroupStudent);
  }

  public StudentGroupStudent createStudentGroupStudent(StudentGroup studentGroup, Student student, User updatingUser) {
    return studentGroupStudentDAO.create(studentGroup, student, updatingUser);
  }

  /* StudentGroupUser */
  
  public StudentGroupUser createStudentGroupStaffMember(StudentGroup studentGroup, StaffMember staffMember, User updatingUser) {
    return studentGroupUserDAO.create(studentGroup, staffMember, updatingUser);
  }

  public StudentGroupUser findStudentGroupUserById(Long id) {
    return studentGroupUserDAO.findById(id);
  }

  public void deleteStudentGroupUser(StudentGroupUser studentGroupUser) {
    studentGroupUserDAO.delete(studentGroupUser);
  }
}
