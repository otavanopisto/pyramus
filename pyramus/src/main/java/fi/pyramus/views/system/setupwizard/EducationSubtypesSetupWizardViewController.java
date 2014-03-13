package fi.pyramus.views.system.setupwizard;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.EducationSubtypeDAO;
import fi.pyramus.dao.base.EducationTypeDAO;
import fi.pyramus.dao.base.SubjectDAO;
import fi.pyramus.domainmodel.base.EducationSubtype;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.util.JSONArrayExtractor;
import fi.pyramus.util.StringAttributeComparator;

public class EducationSubtypesSetupWizardViewController extends SetupWizardController {
  
  public EducationSubtypesSetupWizardViewController() {
    super("educationsubtypes");
  }
  
  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException {
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    
    List<EducationType> educationTypes = educationTypeDAO.listUnarchived();
    Collections.sort(educationTypes, new StringAttributeComparator("getName"));
    
    JSONArray jsonEducationTypes = new JSONArrayExtractor("name", "id").extract(educationTypes);
    for (int i=0; i<educationTypes.size(); i++) {
      List<EducationSubtype> subtypes = educationTypes.get(i).getUnarchivedSubtypes();
      JSONArray jsonSubtypes = new JSONArrayExtractor("id", "name", "code").extract(subtypes);
      jsonEducationTypes.getJSONObject(i).put("subtypes", jsonSubtypes);
    }

    this.setJsDataVariable(requestContext, "educationTypes", jsonEducationTypes.toString());
  }
  
  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
    EducationSubtypeDAO educationSubtypeDAO = DAOFactory.getInstance().getEducationSubtypeDAO();
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    


    int rowCount = requestContext.getInteger("educationSubtypesTable.rowCount");
    Set<Long> removedEducationSubtypeIds = new HashSet<Long>();
    for (EducationSubtype educationSubtype : educationSubtypeDAO.listAll()) {
      removedEducationSubtypeIds.add(educationSubtype.getId());
    }
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "educationSubtypesTable." + i;
      String name = requestContext.getString(colPrefix + ".name");
      String code = requestContext.getString(colPrefix + ".code");
      Long educationTypeId = requestContext.getLong(colPrefix + ".educationTypeId");
      Long educationSubtypeId = requestContext.getLong(colPrefix + ".educationSubtypeId");
      
      EducationSubtype educationSubtype = educationSubtypeDAO.findById(educationSubtypeId);
      EducationType educationType = null;
      if (educationTypeId != null) {
          educationType = educationTypeDAO.findById(educationTypeId);
      }
      if (educationSubtype != null) {
        educationSubtypeDAO.update(educationSubtype, name, code);
        removedEducationSubtypeIds.remove(educationSubtype.getId());
      } else {
        educationSubtypeDAO.create(educationType, name, code);
      }
    }
    for (Long removedEducationSubtypeId : removedEducationSubtypeIds) {
      educationSubtypeDAO.archive(educationSubtypeDAO.findById(removedEducationSubtypeId));
    }
  }

  @Override
  public boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException {
    // TODO Auto-generated method stub
    return false;
  }
}
