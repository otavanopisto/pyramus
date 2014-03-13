package fi.pyramus.views.system.setupwizard;

import java.util.List;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.EducationTypeDAO;
import fi.pyramus.dao.base.SubjectDAO;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.util.JSONArrayExtractor;

public class SubjectsSetupWizardViewController extends SetupWizardController {
  
  public SubjectsSetupWizardViewController() {
    super("subjects");
  }
  
  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException {
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    
    List<EducationType> educationTypes = educationTypeDAO.listUnarchived();
    this.setJsDataVariable(requestContext, "educationTypes", new JSONArrayExtractor("name", "id").extractString(educationTypes));
  }
  
  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    

    int rowCount = requestContext.getInteger("subjectsTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "subjectsTable." + i;
      String name = requestContext.getString(colPrefix + ".name");
      String code = requestContext.getString(colPrefix + ".code");
      Long educationTypeId = requestContext.getLong(colPrefix + ".educationTypeId");
      
      EducationType educationType = null;
      if (educationTypeId != null) {
        educationType = educationTypeDAO.findById(educationTypeId);
      }

      subjectDAO.create(code, name, educationType);
    }
  }

  @Override
  public boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException {
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    return !subjectDAO.listUnarchived().isEmpty();
  }
}
