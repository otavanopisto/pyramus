package fi.otavanopisto.pyramus.views.settings;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Inheritance;
import jakarta.persistence.Version;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.SystemDAO;
import fi.otavanopisto.pyramus.dao.changelog.TrackedEntityPropertyDAO;
import fi.otavanopisto.pyramus.domainmodel.changelog.ChangeLogEntry;
import fi.otavanopisto.pyramus.domainmodel.changelog.ChangeLogEntryEntity;
import fi.otavanopisto.pyramus.domainmodel.changelog.ChangeLogEntryEntityProperty;
import fi.otavanopisto.pyramus.domainmodel.changelog.ChangeLogEntryProperty;
import fi.otavanopisto.pyramus.domainmodel.changelog.TrackedEntityProperty;
import fi.otavanopisto.pyramus.framework.PyramusFormViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.JSONArrayExtractor;

/**
 * The controller responsible of the system settings view of the application.
 */
public class ManageChangeLogViewController extends PyramusFormViewController {

  @Override
  public void processForm(PageRequestContext requestContext) {
    SystemDAO systemDAO = DAOFactory.getInstance().getSystemDAO();
    TrackedEntityPropertyDAO trackedEntityPropertyDAO = DAOFactory.getInstance().getTrackedEntityPropertyDAO();

    List<ManageChangeLogViewEntityBean> entityBeans = new ArrayList<>();
    List<EntityType<?>> entities = new ArrayList<>(systemDAO.getEntities());
    for (EntityType<?> entity : entities) {
      if (!isAbstractSuperclass(entity) && !isChangeLogEntity(entity)) {

        List<ManageChangeLogViewEntityPropertyBean> properties = new ArrayList<>();

        String entityName = entity.getJavaType().getCanonicalName();
        Class<?> entityClass = entity.getJavaType();
        SingularAttribute<?, ?> idAttribute = systemDAO.getEntityIdAttribute(entityClass);

        Set<Attribute<?, ?>> attributes = systemDAO.getEntityAttributes(entityClass);
        for (Attribute<?, ?> attribute : attributes) {
          switch (attribute.getPersistentAttributeType()) {
          case BASIC:
          case ONE_TO_ONE:
          case MANY_TO_ONE:
            if (!attribute.equals(idAttribute) && !this.isVersion(entityClass, attribute)) {
              String propertyName = attribute.getName();
              TrackedEntityProperty trackedEntityProperty = trackedEntityPropertyDAO.findByEntityAndProperty(entityName, propertyName);
              ManageChangeLogViewEntityPropertyBean propertyBean = new ManageChangeLogViewEntityPropertyBean(propertyName,
                  StringUtils.capitalize(propertyName), trackedEntityProperty != null);
              properties.add(propertyBean);
            }
            break;
          default:
            break;
          }
        }

        Collections.sort(properties, new Comparator<ManageChangeLogViewEntityPropertyBean>() {
          @Override
          public int compare(ManageChangeLogViewEntityPropertyBean o1, ManageChangeLogViewEntityPropertyBean o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
          }
        });

        ManageChangeLogViewEntityBean entityBean = new ManageChangeLogViewEntityBean(entityClass.getName(), entityClass.getSimpleName(), properties);
        entityBeans.add(entityBean);
      }
    }

    Collections.sort(entityBeans, new Comparator<ManageChangeLogViewEntityBean>() {
      @Override
      public int compare(ManageChangeLogViewEntityBean o1, ManageChangeLogViewEntityBean o2) {
        return o1.getName().compareToIgnoreCase(o2.getName());
      }
    });
    
    JSONArray jsonEntities = new JSONArray();
    for (ManageChangeLogViewEntityBean entityBean : entityBeans) {
      JSONObject jsonEntity = new JSONObject();
      jsonEntity.put("name", entityBean.getName());
      jsonEntity.put("displayName", entityBean.getDisplayName());
      jsonEntity.put("properties",
          new JSONArrayExtractor("name", "displayName", "track").extract(entityBean.getProperties()));
      jsonEntities.add(jsonEntity);
    }
    
    this.setJsDataVariable(requestContext, "entities", jsonEntities.toString());

    requestContext.setIncludeJSP("/templates/settings/managechangelog.jsp");
  }

  @Override
  public void processSend(PageRequestContext requestContext) {
    TrackedEntityPropertyDAO trackedEntityPropertyDAO = DAOFactory.getInstance().getTrackedEntityPropertyDAO();

    int rowCount = requestContext.getInteger("settingsTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "settingsTable." + i;
      boolean track = "1".equals(requestContext.getString(colPrefix + ".track"));
      String entity = requestContext.getString(colPrefix + ".entity");
      String property = requestContext.getString(colPrefix + ".property");

      if (!StringUtils.isBlank(entity) && !StringUtils.isBlank(property)) {
        TrackedEntityProperty trackedEntityProperty = trackedEntityPropertyDAO.findByEntityAndProperty(entity, property);
        if (track == false && trackedEntityProperty != null)
          trackedEntityPropertyDAO.delete(trackedEntityProperty);
        else if (track == true && trackedEntityProperty == null)
          trackedEntityPropertyDAO.create(entity, property);
      }
    }

    requestContext.setRedirectURL(requestContext.getRequest().getContextPath() + "/settings/managechangelog.page");
  }

  /**
   * Returns the roles allowed to access this page.
   * 
   * @return The roles allowed to access this page
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

  private boolean isChangeLogEntity(EntityType<?> entity) {
    Class<?> entityClass = entity.getJavaType();
    if (entityClass.equals(ChangeLogEntry.class) || entityClass.equals(ChangeLogEntryEntity.class) || entityClass.equals(ChangeLogEntryEntityProperty.class)
        || entityClass.equals(ChangeLogEntryProperty.class) || entityClass.equals(TrackedEntityProperty.class))
      return true;
    return false;
  }

  private boolean isAbstractSuperclass(EntityType<?> entity) {
    Class<?> entityClass = entity.getJavaType();

    if (Modifier.isAbstract(entityClass.getModifiers()))
      return true;

    if (entityClass.isAnnotationPresent(Inheritance.class))
      return true;

    return false;
  }

  private boolean isVersion(Class<?> entityClass, Attribute<?, ?> attribute) {
    try {
      Field field = getField(entityClass, attribute.getName());
      if (field != null) {
        if (field.isAnnotationPresent(Version.class))
          return true;
      }
    } catch (SecurityException e) {
    }

    return false;
  }

  private Field getField(Class<?> clazz, String name) {
    try {
      return clazz.getDeclaredField(name);
    } catch (SecurityException e) {
      return null;
    } catch (NoSuchFieldException e) {
      Class<?> superClass = clazz.getSuperclass();
      if (superClass != null && !Object.class.equals(superClass))
        return getField(superClass, name);
    }

    return null;
  }
}
