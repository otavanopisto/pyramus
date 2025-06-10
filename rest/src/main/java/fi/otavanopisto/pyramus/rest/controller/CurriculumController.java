package fi.otavanopisto.pyramus.rest.controller;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

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
  
  public List<Curriculum> listCurriculums() {
    List<Curriculum> curriculums = curriculumDAO.listAll();
    return curriculums;
  }
  
  public List<Curriculum> listUnarchivedCurriculums() {
    List<Curriculum> curriculums = curriculumDAO.listUnarchived();
    return curriculums;
  }

  public Curriculum updateCurriculum(Curriculum curriculum, String name) {
    return curriculumDAO.updateName(curriculum, name);
  }
  
  public Curriculum archiveCurriculum(Curriculum curriculum, User user) {
    curriculumDAO.archive(curriculum, user);
    return curriculum;
  }

  public void deleteCurriculum(Curriculum curriculum) {
    curriculumDAO.delete(curriculum);
  }
  
}
