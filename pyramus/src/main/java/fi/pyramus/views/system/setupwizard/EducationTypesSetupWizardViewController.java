package fi.pyramus.views.system.setupwizard;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.EducationTypeDAO;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.util.JSONArrayExtractor;
import fi.pyramus.util.StringAttributeComparator;

public class EducationTypesSetupWizardViewController extends SetupWizardController {
  
  public EducationTypesSetupWizardViewController() {
    super("educationtypes");
  }
  
  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException {
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    
    List<EducationType> educationTypes = educationTypeDAO.listUnarchived();
    Collections.sort(educationTypes, new StringAttributeComparator("getName"));
    String jsonEducationTypes = new JSONArrayExtractor("name", "code", "id").extractString(educationTypes);

    this.setJsDataVariable(requestContext, "educationtypes", jsonEducationTypes);
  }
  
  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    

    int rowCount = requestContext.getInteger("educationTypesTable.rowCount");
    Set<Long> removedEducationTypeIds = new HashSet<Long>();
    for (EducationType educationType : educationTypeDAO.listAll()) {
      removedEducationTypeIds.add(educationType.getId());
    }
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "educationTypesTable." + i;
      String name = requestContext.getString(colPrefix + ".name");
      String code = requestContext.getString(colPrefix + ".code");
      long educationTypeId = Long.valueOf(
          requestContext.getString(colPrefix + ".educationTypeId"));
      
      EducationType educationType = educationTypeDAO.findById(educationTypeId);
      if (educationType != null) {
        educationTypeDAO.update(educationType, name, code);
        removedEducationTypeIds.remove(educationType.getId());
      } else {
        educationTypeDAO.create(name, code);
      }
    }
    for (Long removedEducationTypeId : removedEducationTypeIds) {
      educationTypeDAO.archive(educationTypeDAO.findById(removedEducationTypeId));
    }
  }
}
