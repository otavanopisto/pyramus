package fi.otavanopisto.pyramus.rest.controller;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.base.StudyProgrammeDAO;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgrammeCategory;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Stateless
@Dependent
public class StudyProgrammeController {
  
  @Inject
  private StudyProgrammeDAO studyProgrammeDAO;
  
  public StudyProgramme createStudyProgramme(String name, String code, StudyProgrammeCategory category) {
    StudyProgramme activityType = studyProgrammeDAO.create(name, category, code);
    return activityType;
  }
  
  public StudyProgramme findStudyProgrammeById(Long id) {
    StudyProgramme activityType = studyProgrammeDAO.findById(id);
    return activityType;
  }
  
  public List<StudyProgramme> listStudyProgrammes() {
    List<StudyProgramme> activityTypes = studyProgrammeDAO.listAll();
    return activityTypes;
  }

  public List<StudyProgramme> listUnarchivedStudyProgrammes() {
    List<StudyProgramme> activityTypes = studyProgrammeDAO.listUnarchived();
    return activityTypes;
  }
  
  public StudyProgramme updateStudyProgramme(StudyProgramme studyProgramme, String name, String code, StudyProgrammeCategory category) {
    return studyProgrammeDAO.update(studyProgramme, name, category, code);
  }

  public StudyProgramme archiveStudyProgramme(StudyProgramme studyProgramme, User user) {
    studyProgrammeDAO.archive(studyProgramme, user);
    return studyProgramme;
  }

  public void deleteStudyProgramme(StudyProgramme studyProgramme) {
    studyProgrammeDAO.delete(studyProgramme);
  }

}
