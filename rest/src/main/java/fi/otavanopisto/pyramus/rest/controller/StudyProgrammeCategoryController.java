package fi.otavanopisto.pyramus.rest.controller;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.base.StudyProgrammeCategoryDAO;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgrammeCategory;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Stateless
@Dependent
public class StudyProgrammeCategoryController {
  
  @Inject
  private StudyProgrammeCategoryDAO studyProgrammeCategoryDAO;
  
  public StudyProgrammeCategory createStudyProgrammeCategory(String name, EducationType educationType) {
    StudyProgrammeCategory activityType = studyProgrammeCategoryDAO.create(name, educationType);
    return activityType;
  }
  
  public StudyProgrammeCategory findStudyProgrammeCategoryById(Long id) {
    StudyProgrammeCategory activityType = studyProgrammeCategoryDAO.findById(id);
    return activityType;
  }
  
  public List<StudyProgrammeCategory> listStudyProgrammeCategories() {
    List<StudyProgrammeCategory> activityTypes = studyProgrammeCategoryDAO.listAll();
    return activityTypes;
  }

  public List<StudyProgrammeCategory> listUnarchivedStudyProgrammeCategories() {
    List<StudyProgrammeCategory> activityTypes = studyProgrammeCategoryDAO.listUnarchived();
    return activityTypes;
  }
  
  public StudyProgrammeCategory updateStudyProgrammeCategory(StudyProgrammeCategory studyProgrammeCategory, String name, EducationType educationType) {
    return studyProgrammeCategoryDAO.update(studyProgrammeCategory, name, educationType);
  }

  public StudyProgrammeCategory archiveStudyProgrammeCategory(StudyProgrammeCategory studyProgrammeCategory, User user) {
    studyProgrammeCategoryDAO.archive(studyProgrammeCategory, user);
    return studyProgrammeCategory;
  }

  public void deleteStudyProgrammeCategory(StudyProgrammeCategory studyProgrammeCategory) {
    studyProgrammeCategoryDAO.delete(studyProgrammeCategory);
  }

}
