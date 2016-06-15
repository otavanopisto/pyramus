package fi.otavanopisto.pyramus.rest.controller;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.base.CurriculumDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Dependent
@Stateless
public class CurriculumController {

  @Inject
  private CurriculumDAO curriculumDAO;
  
  public Curriculum createCurriculum(String name) {
    Curriculum curriculum = curriculumDAO.create(name);
    return curriculum;
  }

  public Curriculum findCurriculumById(Long id) {
    Curriculum curriculum = curriculumDAO.findById(id);
    return curriculum;
  }
  
  public List<Curriculum> listNationalities() {
    List<Curriculum> nationalities = curriculumDAO.listAll();
    return nationalities;
  }
  
  public List<Curriculum> listUnarchivedNationalities() {
    List<Curriculum> nationalities = curriculumDAO.listUnarchived();
    return nationalities;
  }

  public Curriculum updateCurriculum(Curriculum curriculum, String name) {
    return curriculumDAO.updateName(curriculum, name);
  }
  
  public Curriculum archiveCurriculum(Curriculum curriculum, User user) {
    curriculumDAO.archive(curriculum, user);
    return curriculum;
  }
  
}
