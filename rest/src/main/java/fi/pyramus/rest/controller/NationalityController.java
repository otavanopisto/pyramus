package fi.pyramus.rest.controller;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.pyramus.dao.base.NationalityDAO;
import fi.pyramus.domainmodel.base.Nationality;
import fi.pyramus.domainmodel.users.User;

@Dependent
@Stateless
public class NationalityController {

  @Inject
  private NationalityDAO nationalityDAO;
  
  public Nationality createNationality(String name, String code) {
    Nationality nationality = nationalityDAO.create(name, code);
    return nationality;
  }

  public Nationality findNationalityById(Long id) {
    Nationality nationality = nationalityDAO.findById(id);
    return nationality;
  }
  
  public List<Nationality> listNationalities() {
    List<Nationality> nationalities = nationalityDAO.listAll();
    return nationalities;
  }
  
  public List<Nationality> listUnarchivedNationalities() {
    List<Nationality> nationalities = nationalityDAO.listUnarchived();
    return nationalities;
  }

  public Nationality updateNationality(Nationality nationality, String name, String code) {
    return nationalityDAO.updateCode(nationalityDAO.updateName(nationality, name), code);
  }
  
  public Nationality archiveNationality(Nationality nationality, User user) {
    nationalityDAO.archive(nationality, user);
    return nationality;
  }
  
  public void deleteNationality(Nationality nationality) {
    nationalityDAO.delete(nationality);
  }
}
