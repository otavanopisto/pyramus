package fi.pyramus.views.system.setupwizard;

import java.util.List;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.EducationSubtypeDAO;
import fi.pyramus.dao.base.EducationTypeDAO;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.util.JSONArrayExtractor;

public class EducationSubtypesSetupWizardViewController extends SetupWizardController {
  
  public EducationSubtypesSetupWizardViewController() {
    super("educationsubtypes");
  }
  
  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException {
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    
    List<EducationType> educationTypes = educationTypeDAO.listUnarchived();
    this.setJsDataVariable(requestContext, "educationTypes", new JSONArrayExtractor("name", "id").extractString(educationTypes));
  }
  
  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
    EducationSubtypeDAO educationSubtypeDAO = DAOFactory.getInstance().getEducationSubtypeDAO();
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    

    int rowCount = requestContext.getInteger("educationSubtypesTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "educationSubtypesTable." + i;
      String name = requestContext.getString(colPrefix + ".name");
      String code = requestContext.getString(colPrefix + ".code");
      Long educationTypeId = requestContext.getLong(colPrefix + ".educationTypeId");

      EducationType educationType = null;
      if (educationTypeId != null) {
        educationType = educationTypeDAO.findById(educationTypeId);
      }
      
      educationSubtypeDAO.create(educationType, name, code);
    }
    
  }

  @Override
  public boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException {
    EducationSubtypeDAO educationSubtypeDAO = DAOFactory.getInstance().getEducationSubtypeDAO();
    return !educationSubtypeDAO.listUnarchived().isEmpty();
  }
}
