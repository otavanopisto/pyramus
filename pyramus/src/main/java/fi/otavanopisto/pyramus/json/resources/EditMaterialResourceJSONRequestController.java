package fi.otavanopisto.pyramus.json.resources;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.TagDAO;
import fi.otavanopisto.pyramus.dao.resources.MaterialResourceDAO;
import fi.otavanopisto.pyramus.dao.resources.ResourceCategoryDAO;
import fi.otavanopisto.pyramus.dao.resources.ResourceDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.resources.MaterialResource;
import fi.otavanopisto.pyramus.domainmodel.resources.ResourceCategory;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.PyramusStatusCode;
import fi.otavanopisto.pyramus.framework.UserRole;

public class EditMaterialResourceJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {    
    ResourceDAO resourceDAO = DAOFactory.getInstance().getResourceDAO();
    ResourceCategoryDAO resourceCategoryDAO = DAOFactory.getInstance().getResourceCategoryDAO();
    MaterialResourceDAO materialResourceDAO = DAOFactory.getInstance().getMaterialResourceDAO();
    TagDAO tagDAO = DAOFactory.getInstance().getTagDAO();

    String name = jsonRequestContext.getRequest().getParameter("name");
    Long resourceId = NumberUtils.createLong(jsonRequestContext.getRequest().getParameter("resource"));
    Double unitCost = NumberUtils.createDouble(jsonRequestContext.getRequest().getParameter("unitCost"));
    Long version = NumberUtils.createLong(jsonRequestContext.getRequest().getParameter("version"));
    String tagsText = jsonRequestContext.getString("tags");
    
    Set<Tag> tagEntities = new HashSet<>();
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
    
    MaterialResource materialResource = materialResourceDAO.findById(resourceId);
    if (!version.equals(materialResource.getVersion())) 
      throw new SmvcRuntimeException(PyramusStatusCode.CONCURRENT_MODIFICATION, Messages.getInstance().getText(jsonRequestContext.getRequest().getLocale(), "generic.errors.concurrentModification"));
    
    ResourceCategory resourceCategory = resourceCategoryDAO.findById(NumberUtils.createLong(jsonRequestContext.getRequest().getParameter("category")));
    
    materialResourceDAO.update(materialResource, name, resourceCategory, unitCost);
    resourceDAO.setResourceTags(materialResource, tagEntities);
    
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
