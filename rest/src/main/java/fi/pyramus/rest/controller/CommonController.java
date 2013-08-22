package fi.pyramus.rest.controller;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.pyramus.dao.base.EducationTypeDAO;
import fi.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.pyramus.dao.base.SubjectDAO;
import fi.pyramus.dao.grading.GradingScaleDAO;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.grading.GradingScale;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.persistence.search.SearchResult;

@Dependent
@Stateless
public class CommonController {
  @Inject
  private EducationTypeDAO educationTypeDAO;
  @Inject
  private SubjectDAO subjectDAO;
  @Inject
  private GradingScaleDAO gradingScaleDAO;
  @Inject
  private EducationalTimeUnitDAO educationalTimeUnitDAO;
  
  public EducationType createEducationType(String name, String code) {
    EducationType educationType = educationTypeDAO.create(name, code);
    return educationType;
  }
  
  public Subject createSubject(String code, String name, EducationType educationType) {
    Subject subject = subjectDAO.create(code, name, educationType);
    return subject;
  }
  
  public GradingScale createGradingScale(String name, String description) {
    GradingScale gradingScale = gradingScaleDAO.create(name, description);
    return gradingScale;
  }
  
  public EducationalTimeUnit createEducationalTimeUnit(Double baseUnits, String name) {
    EducationalTimeUnit educationalTimeUnit = educationalTimeUnitDAO.create(baseUnits, name);
    return educationalTimeUnit;
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
  
  public List<GradingScale> findGradingScales() {
    List<GradingScale> gradingScales = gradingScaleDAO.listAll();
    return gradingScales;
  }
  
  public List<GradingScale> findUnarchivedGradingScales() {
    List<GradingScale> gradingScales = gradingScaleDAO.listUnarchived();
    return gradingScales;
  }
  
  public GradingScale findGradingScaleById(Long id) {
    GradingScale gradingScale = gradingScaleDAO.findById(id);
    return gradingScale;
  }
  
  public List<EducationalTimeUnit> findEducationalTimeUnits() {
    List<EducationalTimeUnit> educationalTimeUnits = educationalTimeUnitDAO.listAll();
    return educationalTimeUnits;
  }
  
  public List<EducationalTimeUnit> findUnarchivedEducationalTimeUnits() {
    List<EducationalTimeUnit> educationalTimeUnits = educationalTimeUnitDAO.listUnarchived();
    return educationalTimeUnits;
  }
  
  public EducationalTimeUnit findEducationalTimeUnitById(Long id) {
    EducationalTimeUnit educationalTimeUnit = educationalTimeUnitDAO.findById(id);
    return educationalTimeUnit;
  }
  
  public SearchResult<Subject> searchSubjects(int resultsPerPage, int page, String text) {
    SearchResult<Subject> subjects = subjectDAO.searchSubjectsBasic(resultsPerPage, page, text);
    return subjects;
  }
  
  public EducationType updateEducationType(EducationType educationType, String name, String code) {
    educationTypeDAO.update(educationType, name, code);
    return educationType;
  }
  
  public Subject updateSubject(Subject subject, String code, String name, EducationType educationType) {
    subjectDAO.update(subject, code, name, educationType);
    return subject;
  }
  
  public GradingScale updateGradingScale(GradingScale gradingScale, String name, String description) {
    gradingScaleDAO.update(gradingScale, name, description);
    return gradingScale;
  }
  
  public EducationalTimeUnit updateEducationalTimeUnit(EducationalTimeUnit educationalTimeUnit, Double baseUnits, String name) {
    educationalTimeUnitDAO.update(educationalTimeUnit, baseUnits, name);
    return educationalTimeUnit;
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
  
  public GradingScale archiveGradingScale(GradingScale gradingScale, User user) {
    gradingScaleDAO.archive(gradingScale, user);
    return gradingScale;
  }
  
  public GradingScale unarchiveGradingScale(GradingScale gradingScale, User user) {
    gradingScaleDAO.unarchive(gradingScale, user);
    return gradingScale;
  }
  
  public EducationalTimeUnit archiveEducationalTimeUnit(EducationalTimeUnit educationalTimeUnit, User user) {
    educationalTimeUnitDAO.archive(educationalTimeUnit, user);
    return educationalTimeUnit;
  }
  
  public EducationalTimeUnit unarchiveEducationalTimeUnit(EducationalTimeUnit educationalTimeUnit, User user) {
    educationalTimeUnitDAO.unarchive(educationalTimeUnit, user);
    return educationalTimeUnit;
  }
}
