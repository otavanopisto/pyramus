package fi.pyramus.views.system.setupwizard;

import java.util.Collections;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.EducationTypeDAO;
import fi.pyramus.dao.base.StudyProgrammeCategoryDAO;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.StudyProgrammeCategory;
import fi.pyramus.util.JSONArrayExtractor;
import fi.pyramus.util.StringAttributeComparator;

public class StudyProgrammeCategoriesSetupWizardViewController extends SetupWizardController {

  public StudyProgrammeCategoriesSetupWizardViewController() {
    super("studyprogrammecategories");
  }

  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException {
    StudyProgrammeCategoryDAO studyProgrammeCategoryDAO = DAOFactory.getInstance().getStudyProgrammeCategoryDAO();
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    

    List<EducationType> educationTypes = educationTypeDAO.listUnarchived();
    Collections.sort(educationTypes, new StringAttributeComparator("getName"));
    
    List<StudyProgrammeCategory> studyProgrammeCategories = studyProgrammeCategoryDAO.listUnarchived();
    JSONArray jsonStudyProgrammeCategories = new JSONArrayExtractor("name", "id").extract(studyProgrammeCategories);
    for (int i=0; i<jsonStudyProgrammeCategories.size(); i++) {
      JSONObject jsonStudyProgrammeCategory = jsonStudyProgrammeCategories.getJSONObject(i);
      if (studyProgrammeCategories.get(i).getEducationType() != null) {
        jsonStudyProgrammeCategory.put("educationTypeId", studyProgrammeCategories.get(i).getEducationType().getId());
      }
    }
    String jsonEducationTypes = new JSONArrayExtractor("name", "id").extractString(educationTypes);
    
    this.setJsDataVariable(requestContext, "studyProgrammeCategories", jsonStudyProgrammeCategories.toString());
    this.setJsDataVariable(requestContext, "educationTypes", jsonEducationTypes);

  }

  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    
    StudyProgrammeCategoryDAO studyProgrammeCategoryDAO = DAOFactory.getInstance().getStudyProgrammeCategoryDAO();

    int rowCount = NumberUtils.createInteger(requestContext.getRequest().getParameter("studyProgrammeCategoriesTable.rowCount")).intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "studyProgrammeCategoriesTable." + i;
      String name = requestContext.getString(colPrefix + ".name");
      Long educationTypeId = requestContext.getLong(colPrefix + ".educationTypeId");
      EducationType educationType = null;
      if (educationTypeId != null){
        educationType = educationTypeDAO.findById(educationTypeId);
      }
      
      studyProgrammeCategoryDAO.create(name, educationType); 

    }
  }

  @Override
  public boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException {
    StudyProgrammeCategoryDAO studyProgrammeCategoryDAO = DAOFactory.getInstance().getStudyProgrammeCategoryDAO();
    return !studyProgrammeCategoryDAO.listUnarchived().isEmpty();
  }

}
