package fi.otavanopisto.pyramus.rest.controller;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

import fi.otavanopisto.pyramus.dao.base.MunicipalityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Municipality;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Stateless
@Dependent
public class MunicipalityController {

  @Inject
  private MunicipalityDAO municipalityDAO;
  
  public Municipality createMunicipality(String name, String code) {
    Municipality municipality = municipalityDAO.create(name, code);
    return municipality;
  }
  
  public List<Municipality> listMunicipalities() {
    List<Municipality> municipalities = municipalityDAO.listAll();
    return municipalities;
  }
  
  public List<Municipality> listUnarchivedMunicipalities() {
    List<Municipality> municipalities = municipalityDAO.listUnarchived();
    return municipalities;
  }
  
  public Municipality findMunicipalityById(Long id) {
    Municipality municipality = municipalityDAO.findById(id);
    return municipality;
  }
  
  public Municipality updateMunicipality(Municipality municipality, String name, String code) {
    Municipality updated = municipalityDAO.update(municipality, name, code);
    return updated;
  }

  public Municipality archiveMunicipality(Municipality municipality, User user) {
    municipalityDAO.archive(municipality, user);
    return municipality;
  }

  public void deleteMunicipality(Municipality municipality) {
    municipalityDAO.delete(municipality);
  }

}
