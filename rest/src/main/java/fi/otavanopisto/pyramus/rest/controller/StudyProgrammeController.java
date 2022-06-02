package fi.otavanopisto.pyramus.rest.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.base.StudyProgrammeDAO;
import fi.otavanopisto.pyramus.domainmodel.Archived;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgrammeCategory;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.UserUtils;

@Stateless
@Dependent
public class StudyProgrammeController {
  
  @Inject
  private StudyProgrammeDAO studyProgrammeDAO;
  
  public StudyProgramme createStudyProgramme(Organization organization, String name, String code, String officialEducationType, StudyProgrammeCategory category, boolean hasEvaluationFees) {
    StudyProgramme studyProgramme = studyProgrammeDAO.create(organization, name, category, code, officialEducationType, hasEvaluationFees);
    return studyProgramme;
  }
  
  public StudyProgramme findStudyProgrammeById(Long id) {
    StudyProgramme studyProgramme = studyProgrammeDAO.findById(id);
    return studyProgramme;
  }
  
  public List<StudyProgramme> listStudyProgrammes(Archived archived) {
    switch (archived) {
      case UNARCHIVED:
        return listUnarchivedStudyProgrammes();
      case ARCHIVED:
        return listArchivedStudyProgrammes();
      case BOTH:
        return listStudyProgrammes();
    }
    
    throw new RuntimeException("unknown archived parameter");
  }

  public List<StudyProgramme> listStudyProgrammes() {
    List<StudyProgramme> studyProgrammes = studyProgrammeDAO.listAll();
    return studyProgrammes;
  }

  public List<StudyProgramme> listUnarchivedStudyProgrammes() {
    List<StudyProgramme> studyProgrammes = studyProgrammeDAO.listUnarchived();
    return studyProgrammes;
  }

  public List<StudyProgramme> listArchivedStudyProgrammes() {
    List<StudyProgramme> studyProgrammes = studyProgrammeDAO.listArchived();
    return studyProgrammes;
  }
  
  public List<StudyProgramme> listStudyProgrammesByOrganization(Organization organization, Archived archived) {
    List<StudyProgramme> studyProgrammes = studyProgrammeDAO.listByOrganization(organization, archived);
    return studyProgrammes;
  }

  public StudyProgramme updateStudyProgramme(StudyProgramme studyProgramme, Organization organization, String name, String code, String officialEducationType, StudyProgrammeCategory category, boolean hasEvaluationFees) {
    return studyProgrammeDAO.update(studyProgramme, organization, name, category, code, officialEducationType, hasEvaluationFees);
  }

  public StudyProgramme archiveStudyProgramme(StudyProgramme studyProgramme, User user) {
    studyProgrammeDAO.archive(studyProgramme, user);
    return studyProgramme;
  }

  public void deleteStudyProgramme(StudyProgramme studyProgramme) {
    studyProgrammeDAO.delete(studyProgramme);
  }

  public List<StudyProgramme> listAccessibleStudyProgrammes(User user, Archived archived) {
    if (user != null) {
      if (UserUtils.canAccessAllOrganizations(user)) {
        return listStudyProgrammes(archived);
      } else {
        if (user.getOrganization() != null) {
          return listStudyProgrammesByOrganization(user.getOrganization(), archived);
        }
      }
    }
    
    // No matching case found - return empty list
    return new ArrayList<>();
  }

}
