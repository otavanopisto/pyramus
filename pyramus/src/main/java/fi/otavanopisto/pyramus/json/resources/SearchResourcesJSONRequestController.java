package fi.otavanopisto.pyramus.json.resources;

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
import fi.otavanopisto.pyramus.dao.resources.ResourceCategoryDAO;
import fi.otavanopisto.pyramus.dao.resources.ResourceDAO;
import fi.otavanopisto.pyramus.domainmodel.resources.MaterialResource;
import fi.otavanopisto.pyramus.domainmodel.resources.Resource;
import fi.otavanopisto.pyramus.domainmodel.resources.ResourceCategory;
import fi.otavanopisto.pyramus.domainmodel.resources.ResourceType;
import fi.otavanopisto.pyramus.domainmodel.resources.WorkResource;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;

public class SearchResourcesJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    ResourceDAO resourceDAO = DAOFactory.getInstance().getResourceDAO();
    ResourceCategoryDAO resourceCategoryDAO = DAOFactory.getInstance().getResourceCategoryDAO();

    Integer resultsPerPage = NumberUtils.createInteger(requestContext.getRequest().getParameter("maxResults"));
    if (resultsPerPage == null) {
      resultsPerPage = 10;
    }

    Integer page = NumberUtils.createInteger(requestContext.getRequest().getParameter("page"));
    if (page == null) {
      page = 0;
    }
    
    SearchResult<Resource> searchResult;

    if ("advanced".equals(requestContext.getString("activeTab"))) {
      String name = requestContext.getRequest().getParameter("name");
      String tags = requestContext.getRequest().getParameter("tags");

      ResourceType resourceType = null;
      String resourceTypeParam = requestContext.getRequest().getParameter("resourceType");
      if (!StringUtils.isBlank(resourceTypeParam)) {
        resourceType = Enum.valueOf(ResourceType.class, resourceTypeParam);
      }
      
      ResourceCategory resourceCategory = null;
      String resourceCategoryParam = requestContext.getRequest().getParameter("resourceCategory");
      if (!StringUtils.isBlank(resourceCategoryParam)) {
        resourceCategory = resourceCategoryDAO.findById(NumberUtils.createLong(resourceCategoryParam));
      }

      searchResult = resourceDAO.searchResources(resultsPerPage, page, name, tags, resourceType, resourceCategory, true);
    } else {
      String simpleQuery = requestContext.getString("simpleQuery");
      searchResult = resourceDAO.searchResourcesBasic(resultsPerPage, page, simpleQuery);
    }
    

    List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();

    List<Resource> resources = searchResult.getResults();
    for (Resource resource : resources) {
      
      Double unitCost = 0.0;
      if (resource instanceof MaterialResource) {
        unitCost = ((MaterialResource) resource).getUnitCost().getAmount();
      }
      else if (resource instanceof WorkResource) {
        unitCost = ((WorkResource) resource).getCostPerUse().getAmount();
      }
      Double hourlyCost = 0.0;
      if (resource instanceof WorkResource) {
        hourlyCost = ((WorkResource) resource).getHourlyCost().getAmount();
      }
      
      Map<String, Object> resourceInfo = new HashMap<String, Object>();
      resourceInfo.put("id", resource.getId());
      resourceInfo.put("name", resource.getName());
      resourceInfo.put("resourceType", resource.getResourceType());
      resourceInfo.put("resourceCategoryId", resource.getCategory().getId());
      resourceInfo.put("resourceCategoryName", resource.getCategory().getName());
      resourceInfo.put("unitCost", unitCost);
      resourceInfo.put("hourlyCost", hourlyCost);
      results.add(resourceInfo);
    }

    String statusMessage;
    Locale locale = requestContext.getRequest().getLocale();
    if (searchResult.getTotalHitCount() > 0) {
      statusMessage = Messages.getInstance().getText(
          locale,
          "resources.searchResources.searchStatus",
          new Object[] { searchResult.getFirstResult() + 1, searchResult.getLastResult() + 1,
              searchResult.getTotalHitCount() });
    }
    else {
      statusMessage = Messages.getInstance().getText(locale, "resources.searchResources.searchStatusNoMatches");
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
