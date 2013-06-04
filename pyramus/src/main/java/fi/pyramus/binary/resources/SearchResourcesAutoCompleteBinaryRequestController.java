package fi.pyramus.binary.resources;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.BinaryRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.resources.ResourceCategoryDAO;
import fi.pyramus.dao.resources.ResourceDAO;
import fi.pyramus.domainmodel.resources.Resource;
import fi.pyramus.domainmodel.resources.ResourceCategory;
import fi.pyramus.framework.BinaryRequestController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.persistence.search.SearchResult;
import fi.pyramus.util.StringAttributeComparator;

/** A binary request controller responsible for providing server-side autocomplete
 * for resources search.
 *
 */
public class SearchResourcesAutoCompleteBinaryRequestController extends BinaryRequestController {

  /** Processes a binary request.
   * The request should contain the following parameters:
   * <dl>
   *   <dt><code>query</code></dt>
   *   <dd>Already typed characters.</dd>
   * </dl>
   * 
   * @param binaryRequestContext The context of the binary request.
   */
  public void process(BinaryRequestContext binaryRequestContext) {
    ResourceDAO resourceDAO = DAOFactory.getInstance().getResourceDAO();
    ResourceCategoryDAO resourceCategoryDAO = DAOFactory.getInstance().getResourceCategoryDAO();
    
    String query = binaryRequestContext.getString("query");
    
    StringBuilder resultBuilder = new StringBuilder();
    
    resultBuilder.append("<ul>");
    
    List<ResourceCategory> resourceCategories = resourceCategoryDAO.listUnarchived();
    Collections.sort(resourceCategories, new StringAttributeComparator("getName"));
    
    for (ResourceCategory resourceCategory : resourceCategories) {
      SearchResult<Resource> searchResult = resourceDAO.searchResources(5, 0, query + '*', null, null, resourceCategory, true);
      if (searchResult.getTotalHitCount() > 0) {
        addResourceCategory(resultBuilder, resourceCategory);
        
        for (Resource resource : searchResult.getResults()) {
          addResource(resultBuilder, resource);
        }
      }
    }
    
    resultBuilder.append("</ul>");
    
    try {
      binaryRequestContext.setResponseContent(resultBuilder.toString().getBytes("UTF-8"), "text/html;charset=UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new SmvcRuntimeException(e);
    }
  }
  
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.USER, UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }
  
  private void addResourceCategory(StringBuilder resultBuilder, ResourceCategory resourceCategory) {
    resultBuilder
      .append("<li class=\"autocompleteGroupTitle\">")
      .append("<span>") 
      .append(StringEscapeUtils.escapeHtml(resourceCategory.getName()))
      .append("</span>")
      .append("<input type=\"hidden\" name=\"categoryId\" value=\"")
      .append(resourceCategory.getId())
      .append("\"/>")
      .append("</li>");
  }
  
  private void addResource(StringBuilder resultBuilder, Resource resource) {
    resultBuilder
      .append("<li>")
      .append("<span>")
      .append(StringEscapeUtils.escapeHtml(resource.getName()))
      .append("</span>")
      .append("<input type=\"hidden\" name=\"resourceId\" value=\"")
      .append(resource.getId())
      .append("\"/>")
      .append("<input type=\"hidden\" name=\"resourceType\" value=\"")
      .append(resource.getResourceType())
      .append("\"/>")
      .append("</li>");
  }
}
