package fi.pyramus.rest.controller;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.pyramus.dao.base.StudyProgrammeCategoryDAO;
import fi.pyramus.dao.base.StudyProgrammeDAO;
import fi.pyramus.dao.students.StudentStudyEndReasonDAO;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.StudyProgramme;
import fi.pyramus.domainmodel.base.StudyProgrammeCategory;
import fi.pyramus.domainmodel.students.StudentStudyEndReason;

@Dependent
@Stateless
public class StudentSubResourceController {
  
  @Inject
  private StudyProgrammeCategoryDAO studyProgrammeCategoryDAO;
  
  @Inject
  private StudyProgrammeDAO studyProgrammeDAO;
  
  public StudyProgrammeCategory createStudyProgrammeCategory(String name, EducationType educationType) {
    StudyProgrammeCategory studyProgrammeCategory = studyProgrammeCategoryDAO.create(name, educationType);
    return studyProgrammeCategory;
  }
  
  public StudyProgramme createStudyProgramme(String name, StudyProgrammeCategory category, String code) {
    StudyProgramme studyProgramme = studyProgrammeDAO.create(name, category, code);
    return studyProgramme;
  }
  
  public List<StudyProgrammeCategory> findStudyProgrammeCategories() {
    List<StudyProgrammeCategory> studyProgrammeCategories = studyProgrammeCategoryDAO.listAll();
    return studyProgrammeCategories;
  }
  
  public List<StudyProgrammeCategory> findUnarchivedStudyProgrammeCategories() {
    List<StudyProgrammeCategory> studyProgrammeCategories = studyProgrammeCategoryDAO.listUnarchived();
    return studyProgrammeCategories;
  }
  
  public StudyProgrammeCategory findStudyProgrammeCategoryById(Long id) {
    StudyProgrammeCategory studyProgrammeCategory = studyProgrammeCategoryDAO.findById(id);
    return studyProgrammeCategory;
  }
  
  public List<StudyProgramme> findStudyProgrammes() {
    List<StudyProgramme> studyProgrammes = studyProgrammeDAO.listAll();
    return studyProgrammes;
  }

  public List<StudyProgramme> findUnarchivedStudyProgrammes() {
    List<StudyProgramme> studyProgrammes = studyProgrammeDAO.listUnarchived();
    return studyProgrammes;
  }
  
  public StudyProgramme findStudyProgrammeById(Long id) {
    StudyProgramme studyProgramme = studyProgrammeDAO.findById(id);
    return studyProgramme;
  }
  
  public StudyProgrammeCategory updateStudyProgrammeCategory(StudyProgrammeCategory studyProgrammeCategory, String name, EducationType educationType) {
    studyProgrammeCategoryDAO.update(studyProgrammeCategory, name, educationType);
    return studyProgrammeCategory;
  }
  
  public StudyProgramme updateStudyProgramme(StudyProgramme studyProgramme, String name, StudyProgrammeCategory category, String code) {
    studyProgrammeDAO.update(studyProgramme, name, category, code);
    return studyProgramme;
  }
}
