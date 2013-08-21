package fi.pyramus.rest.controller;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.pyramus.dao.base.EducationTypeDAO;
import fi.pyramus.dao.base.SubjectDAO;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.users.User;

@Dependent
@Stateless
public class CommonController {
  @Inject
  private EducationTypeDAO educationTypeDAO;
  @Inject
  private SubjectDAO subjectDAO;
  
  public EducationType createEducationType(String name, String code) {
    EducationType educationType = educationTypeDAO.create(name, code);
    return educationType;
  }
  
  public Subject createSubject(String code, String name, EducationType educationType) {
    Subject subject = subjectDAO.create(code, name, educationType);
    return subject;
  }
  
  public List<EducationType> findEducationTypes() {
    List<EducationType> educationTypes = educationTypeDAO.listAll();
    return educationTypes;
  }
  
  public List<EducationType> findUnarchivedEducationTypes() {
    List<EducationType> educationTypes = educationTypeDAO.listUnarchived();
    return educationTypes;
  }
  
  public EducationType findEducationTypeById(Long id) {
    EducationType educationType = educationTypeDAO.findById(id);
    return educationType;
  }
  
  public List<Subject> findSubjects() {
    List<Subject> subjects = subjectDAO.listAll();
    return subjects;
  }
  
  public List<Subject> findUnarchivedSubjects() {
    List<Subject> subjects = subjectDAO.listUnarchived();
    return subjects;
  }
  
  public List<Subject> findSubjectsByEducationType(EducationType educationType) {
    List<Subject> subjects = subjectDAO.listByEducationType(educationType);
    return subjects;
  }
  
  public Subject findSubjectById(Long id) {
    Subject subject = subjectDAO.findById(id);
    return subject;
  }
  
  public EducationType updateEducationType(EducationType educationType, String name, String code) {
    educationTypeDAO.update(educationType, name, code);
    return educationType;
  }
  
  public Subject updateSubject(Subject subject, String code, String name, EducationType educationType) {
    subjectDAO.update(subject, code, name, educationType);
    return subject;
  }
  
  public EducationType archiveEducationType(EducationType educationType, User user) {
    educationTypeDAO.archive(educationType, user);
    return educationType;
  }
  
  public EducationType unarchiveEducationType(EducationType educationType, User user) {
    educationTypeDAO.unarchive(educationType, user);
    return educationType;
  }
  
  public Subject archiveSubject(Subject subject, User user) {
    subjectDAO.archive(subject, user);
    return subject;
  }
  
  public Subject unarchiveSubject(Subject subject, User user) {
    subjectDAO.unarchive(subject, user);
    return subject;
  }
}
