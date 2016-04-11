package fi.otavanopisto.pyramus.json.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.EducationSubtypeDAO;
import fi.otavanopisto.pyramus.dao.base.EducationTypeDAO;
import fi.otavanopisto.pyramus.dao.base.SubjectDAO;
import fi.otavanopisto.pyramus.dao.modules.ModuleDAO;
import fi.otavanopisto.pyramus.domainmodel.base.EducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.modules.Module;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;

/**
 * The controller responsible of searching modules.
 * 
 * @see fi.otavanopisto.pyramus.views.modules.SearchModulesViewController
 */
public class SearchModulesJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    EducationSubtypeDAO educationSubtypeDAO = DAOFactory.getInstance().getEducationSubtypeDAO();    

    Integer resultsPerPage = NumberUtils.createInteger(requestContext.getRequest().getParameter("maxResults"));
    if (resultsPerPage == null) {
      resultsPerPage = 10;
    }

    Integer page = NumberUtils.createInteger(requestContext.getRequest().getParameter("page"));
    if (page == null) {
      page = 0;
    }

    SearchResult<Module> searchResult;
    
    // Gather the search terms

    if ("advanced".equals(requestContext.getRequest().getParameter("activeTab"))) {
      String name = requestContext.getString("name");
      String tags = requestContext.getString("tags");
      if (!StringUtils.isBlank(tags))
        tags = tags.replace(',', ' ');
        
      String description = requestContext.getString("description");

      Subject subject = null;
      Long subjectId = requestContext.getLong("subject");
      if (subjectId != null)
        subject = subjectDAO.findById(subjectId);
      
      EducationType educationType = null;
      Long educationTypeId = requestContext.getLong("educationType");
      if (educationTypeId != null)
        educationType = educationTypeDAO.findById(educationTypeId);

      EducationSubtype educationSubtype = null;
      Long educationSubtypeId = requestContext.getLong("educationSubtype");
      if (educationSubtypeId != null)
        educationSubtype = educationSubtypeDAO.findById(educationSubtypeId);

      // Search via the DAO object
      searchResult = moduleDAO.searchModules(resultsPerPage, page, null, name, tags, description, null, null, null, subject, educationType, educationSubtype, true);
    }
    else {
      String text = requestContext.getString("text");
  
      // Search via the DAO object
      searchResult = moduleDAO.searchModulesBasic(resultsPerPage, page, text);
    }
    
    List<Map<String, Object>> results = new ArrayList<>();
    List<Module> modules = searchResult.getResults();
    for (Module module : modules) {
      Map<String, Object> moduleInfo = new HashMap<>();
      moduleInfo.put("id", module.getId());
      moduleInfo.put("name", module.getName());
      results.add(moduleInfo);
    }

    String statusMessage;
    Locale locale = requestContext.getRequest().getLocale();
    if (searchResult.getTotalHitCount() > 0) {
      statusMessage = Messages.getInstance().getText(
          locale,
          "modules.searchModules.searchStatus",
          new Object[] { searchResult.getFirstResult() + 1, searchResult.getLastResult() + 1,
              searchResult.getTotalHitCount() });
    }
    else {
      statusMessage = Messages.getInstance().getText(locale, "modules.searchModules.searchStatusNoMatches");
    }

    requestContext.addResponseParameter("results", results);
    requestContext.addResponseParameter("statusMessage", statusMessage);
    requestContext.addResponseParameter("pages", searchResult.getPages());
    requestContext.addResponseParameter("page", searchResult.getPage());
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

}
