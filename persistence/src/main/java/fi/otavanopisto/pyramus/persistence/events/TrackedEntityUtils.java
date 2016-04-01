package fi.otavanopisto.pyramus.persistence.events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.changelog.TrackedEntityPropertyDAO;
import fi.otavanopisto.pyramus.domainmodel.changelog.TrackedEntityProperty;

public class TrackedEntityUtils {

  public synchronized static boolean isTrackedEntity(String entityName) {
    return getTrackedEntityMap().get(entityName) != null;
  }

  public synchronized static boolean isTrackedProperty(String entityName, String field) {
    Set<String> fields = getTrackedEntityMap().get(entityName);
    return field != null && fields.contains(field);
  }
  
  private synchronized static Map<String, Set<String>> getTrackedEntityMap() {
    if (trackedEntities == null) {
      trackedEntities = new HashMap<String, Set<String>>();
      TrackedEntityPropertyDAO trackedEntityPropertyDAO = DAOFactory.getInstance().getTrackedEntityPropertyDAO();
      List<TrackedEntityProperty> trackedEntityProperties = trackedEntityPropertyDAO.listAll();
      for (TrackedEntityProperty trackedEntityProperty : trackedEntityProperties) {
        String entity = trackedEntityProperty.getEntity();
        String property = trackedEntityProperty.getProperty();
        
        Set<String> properties = trackedEntities.get(entity);
        if (properties == null) {
          properties = new HashSet<String>();
          trackedEntities.put(entity, properties);
        }
        
        properties.add(property);
      }
    }
  
    return trackedEntities;
  }
  
  private static Map<String, Set<String>> trackedEntities = null;

}
