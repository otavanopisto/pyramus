package fi.otavanopisto.pyramus.rest.controller;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.base.StudyProgrammeDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgrammeCategory;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Stateless
@Dependent
public class StudyProgrammeController {
  
  @Inject
  private StudyProgrammeDAO studyProgrammeDAO;
  
  public StudyProgramme createStudyProgramme(Organization organization, String name, String code, StudyProgrammeCategory category) {
    StudyProgramme studyProgramme = studyProgrammeDAO.create(organization, name, category, code);
    return studyProgramme;
  }
  
  public StudyProgramme findStudyProgrammeById(Long id) {
    StudyProgramme studyProgramme = studyProgrammeDAO.findById(id);
    return studyProgramme;
  }
  
  public List<StudyProgramme> listStudyProgrammes() {
    List<StudyProgramme> studyProgrammes = studyProgrammeDAO.listAll();
    return studyProgrammes;
  }

  public List<StudyProgramme> listUnarchivedStudyProgrammes() {
    List<StudyProgramme> studyProgrammes = studyProgrammeDAO.listUnarchived();
    return studyProgrammes;
  }

  public List<StudyProgramme> listStudyProgrammesByOrganization(Organization organization) {
    List<StudyProgramme> studyProgrammes = studyProgrammeDAO.listByOrganization(organization, null);
    return studyProgrammes;
  }

  public List<StudyProgramme> listUnarchivedStudyProgrammesByOrganization(Organization organization) {
    List<StudyProgramme> studyProgrammes = studyProgrammeDAO.listByOrganization(organization, false);
    return studyProgrammes;
  }
  
  public StudyProgramme updateStudyProgramme(StudyProgramme studyProgramme, Organization organization, String name, String code, StudyProgrammeCategory category) {
    return studyProgrammeDAO.update(studyProgramme, organization, name, category, code);
  }

  public StudyProgramme archiveStudyProgramme(StudyProgramme studyProgramme, User user) {
    studyProgrammeDAO.archive(studyProgramme, user);
    return studyProgramme;
  }

  public void deleteStudyProgramme(StudyProgramme studyProgramme) {
    studyProgrammeDAO.delete(studyProgramme);
  }

}
