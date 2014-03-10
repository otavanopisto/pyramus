package fi.pyramus.views.system.setupwizard;

import java.util.Collections;
import java.util.List;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.EducationTypeDAO;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.util.JSONArrayExtractor;
import fi.pyramus.util.StringAttributeComparator;

public class EducationTypesSetupWizardHandler implements SetupWizardHandler {

  @Override
  public String getPhaseName() {
    return "educationtypes";
  }

  @Override
  public String populate(PageRequestContext requestContext) {
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    

    List<EducationType> educationTypes = educationTypeDAO.listUnarchived();
    Collections.sort(educationTypes, new StringAttributeComparator("getName"));
    
    String jsonEducationTypes = new JSONArrayExtractor("name", "code", "id").extractString(educationTypes);

    return jsonEducationTypes;
  }
}
