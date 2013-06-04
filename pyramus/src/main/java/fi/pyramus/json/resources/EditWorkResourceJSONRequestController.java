package fi.pyramus.json.resources;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.TagDAO;
import fi.pyramus.dao.resources.ResourceCategoryDAO;
import fi.pyramus.dao.resources.ResourceDAO;
import fi.pyramus.dao.resources.WorkResourceDAO;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.resources.ResourceCategory;
import fi.pyramus.domainmodel.resources.WorkResource;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.PyramusStatusCode;
import fi.pyramus.framework.UserRole;

public class EditWorkResourceJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {    
    ResourceDAO resourceDAO = DAOFactory.getInstance().getResourceDAO();
    ResourceCategoryDAO resourceCategoryDAO = DAOFactory.getInstance().getResourceCategoryDAO();
    WorkResourceDAO workResourceDAO = DAOFactory.getInstance().getWorkResourceDAO();
    TagDAO tagDAO = DAOFactory.getInstance().getTagDAO();

    String name = jsonRequestContext.getRequest().getParameter("name");
    Long resourceId = NumberUtils.createLong(jsonRequestContext.getRequest().getParameter("resource"));
    Double hourlyCost = NumberUtils.createDouble(jsonRequestContext.getRequest().getParameter("hourlyCost"));
    Double costPerUse = NumberUtils.createDouble(jsonRequestContext.getRequest().getParameter("costPerUse"));
    Long version = NumberUtils.createLong(jsonRequestContext.getRequest().getParameter("version"));
    String tagsText = jsonRequestContext.getString("tags");
    
    Set<Tag> tagEntities = new HashSet<Tag>();
    if (!StringUtils.isBlank(tagsText)) {
      List<String> tags = Arrays.asList(tagsText.split("[\\ ,]"));
      for (String tag : tags) {
        if (!StringUtils.isBlank(tag)) {
          Tag tagEntity = tagDAO.findByText(tag.trim());
          if (tagEntity == null)
            tagEntity = tagDAO.create(tag);
          tagEntities.add(tagEntity);
        }
      }
    }
    
    WorkResource workResource = workResourceDAO.findById(resourceId);
    if (!version.equals(workResource.getVersion())) 
      throw new SmvcRuntimeException(PyramusStatusCode.CONCURRENT_MODIFICATION, Messages.getInstance().getText(jsonRequestContext.getRequest().getLocale(), "generic.errors.concurrentModification"));
    
    ResourceCategory resourceCategory = resourceCategoryDAO.findById(NumberUtils.createLong(jsonRequestContext.getRequest().getParameter("category")));
    
    workResourceDAO.update(workResource, name, resourceCategory, costPerUse, hourlyCost);
    resourceDAO.setResourceTags(workResource, tagEntities);
    
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
