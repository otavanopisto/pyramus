package fi.pyramus.views.system.setupwizard;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.math.NumberUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.EducationSubtypeDAO;
import fi.pyramus.dao.base.EducationTypeDAO;
import fi.pyramus.dao.base.StudyProgrammeCategoryDAO;
import fi.pyramus.dao.base.StudyProgrammeDAO;
import fi.pyramus.dao.base.SubjectDAO;
import fi.pyramus.domainmodel.base.EducationSubtype;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.StudyProgramme;
import fi.pyramus.domainmodel.base.StudyProgrammeCategory;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.util.JSONArrayExtractor;
import fi.pyramus.util.StringAttributeComparator;

public class StudyProgrammesSetupWizardViewController extends SetupWizardController {
  
  public StudyProgrammesSetupWizardViewController() {
    super("studyprogrammes");
  }
  
  @Override
  public void setup(PageRequestContext pageRequestContext) throws SetupWizardException {
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
    StudyProgrammeCategoryDAO studyProgrammeCategoryDAO = DAOFactory.getInstance().getStudyProgrammeCategoryDAO();
    
    List<StudyProgramme> studyProgrammes = studyProgrammeDAO.listUnarchived();
    JSONArray jsonStudyProgrammes = new JSONArrayExtractor("name", "code", "id").extract(studyProgrammes);
    for (int i=0; i<jsonStudyProgrammes.size(); i++) {
      JSONObject jsonStudyProgrammeCategory = jsonStudyProgrammes.getJSONObject(i);
      if (studyProgrammes.get(i).getCategory() != null) {
        jsonStudyProgrammeCategory.put("categoryId", studyProgrammes.get(i).getCategory().getId());
      }
    }
    
    String jsonCategories = new JSONArrayExtractor("name", "id").extractString(studyProgrammeCategoryDAO.listUnarchived());
    
    this.setJsDataVariable(pageRequestContext, "studyProgrammes", jsonStudyProgrammes.toString());
    this.setJsDataVariable(pageRequestContext, "categories", jsonCategories);
  }
  
  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
    StudyProgrammeCategoryDAO studyProgrammeCategoryDAO = DAOFactory.getInstance().getStudyProgrammeCategoryDAO();

    int rowCount = NumberUtils.createInteger(requestContext.getRequest().getParameter("studyProgrammesTable.rowCount")).intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "studyProgrammesTable." + i;
      Long studyProgrammeId = requestContext.getLong(colPrefix + ".studyProgrammeId");
      String name = requestContext.getString(colPrefix + ".name");
      String code = requestContext.getString(colPrefix + ".code");
      Long categoryId = requestContext.getLong(colPrefix + ".category");
      
      StudyProgrammeCategory category = null;
      
      if (categoryId != null) {
        category = studyProgrammeCategoryDAO.findById(categoryId);
      }
      
      if (studyProgrammeId == -1) {
        studyProgrammeDAO.create(name, category, code); 
      }
    }
  }
}
