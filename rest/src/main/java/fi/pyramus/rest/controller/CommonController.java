package fi.pyramus.rest.controller;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.pyramus.dao.base.EducationTypeDAO;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.users.User;

@Dependent
@Stateless
public class CommonController {
  @Inject
  private EducationTypeDAO educationTypeDAO;
  
  public EducationType createEducationType(String name, String code) {
    EducationType educationType = educationTypeDAO.create(name, code);
    return educationType;
  }
  
  public List<EducationType> findEducationTypes() {
    List<EducationType> educationTypes = educationTypeDAO.listAll();
    return educationTypes;
  }
  
  public List<EducationType> findUnarchivedEducationTypes() {
    List<EducationType> educationTypes = educationTypeDAO.listUnarchived();
    return educationTypes;
  }
  
  public EducationType findEducationTypeById(Long id) {
    EducationType educationType = educationTypeDAO.findById(id);
    return educationType;
  }
  
  public EducationType updateEducationType(EducationType educationType, String name, String code) {
    educationTypeDAO.update(educationType, name, code);
    return educationType;
  }
  
  public EducationType archiveEducationType(EducationType educationType, User user) {
    educationTypeDAO.archive(educationType, user);
    return educationType;
  }
  
  public EducationType unarchiveEducationType(EducationType educationType, User user) {
    educationTypeDAO.unarchive(educationType, user);
    return educationType;
  }
}
