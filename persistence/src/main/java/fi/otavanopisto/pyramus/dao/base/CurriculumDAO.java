package fi.otavanopisto.pyramus.dao.base;

import javax.ejb.Stateless;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;

@Stateless
public class CurriculumDAO extends PyramusEntityDAO<Curriculum> {

  public Curriculum create(String name) {
    Curriculum curriculum = new Curriculum();
    curriculum.setName(name);
    curriculum.setArchived(false);
    return persist(curriculum);
  }

  public Curriculum updateName(Curriculum curriculum, String name) {
    curriculum.setName(name);
    return persist(curriculum);
  }

  public void archive(Curriculum curriculum) {
    curriculum.setArchived(true);
    persist(curriculum);
  }
  
}
