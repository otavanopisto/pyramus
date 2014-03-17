package fi.pyramus.changelog;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.persistence.metamodel.Attribute;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.SystemDAO;
import fi.pyramus.dao.changelog.ChangeLogEntryDAO;
import fi.pyramus.dao.changelog.ChangeLogEntryEntityDAO;
import fi.pyramus.dao.changelog.ChangeLogEntryEntityPropertyDAO;
import fi.pyramus.dao.changelog.ChangeLogEntryPropertyDAO;
import fi.pyramus.dao.users.UserDAO;
import fi.pyramus.domainmodel.changelog.ChangeLogEntry;
import fi.pyramus.domainmodel.changelog.ChangeLogEntryEntity;
import fi.pyramus.domainmodel.changelog.ChangeLogEntryEntityProperty;
import fi.pyramus.domainmodel.changelog.ChangeLogEntryProperty;
import fi.pyramus.domainmodel.changelog.ChangeLogEntryType;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.persistence.events.EventType;
import fi.pyramus.persistence.events.TrackedEntityUtils;
import fi.pyramus.util.ReflectionApiUtils;

//@MessageDriven(activationConfig = { @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") }, mappedName = "java:/jms/JPAEvents")
public class ChangeLogJPAEventsListener implements MessageListener {

  public void onMessage(Message message) {
    try {
      MapMessage mapMessage = (MapMessage) message;
      
      EventType eventType = EventType.valueOf(mapMessage.getString("eventType"));
      switch (eventType) {
        case Create:
          handleCreate(mapMessage);
        break;
        case Update:
          handleUpdate(mapMessage);
        break;
        case Delete:
          handleDelete(mapMessage);
        break;
      }
    } catch (Exception e) {
      throw new SmvcRuntimeException(e);
    }
  }
  
  private void handleCreate(MapMessage mapMessage) throws JMSException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InvocationTargetException  {
    SystemDAO systemDAO = DAOFactory.getInstance().getSystemDAO();
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    ChangeLogEntryDAO logEntryDAO = DAOFactory.getInstance().getChangeLogEntryDAO();
    ChangeLogEntryEntityDAO entryEntityDAO = DAOFactory.getInstance().getChangeLogEntryEntityDAO();
    ChangeLogEntryEntityPropertyDAO entryEntityPropertyDAO = DAOFactory.getInstance().getChangeLogEntryEntityPropertyDAO();
    ChangeLogEntryPropertyDAO logEntryPropertyDAO = DAOFactory.getInstance().getChangeLogEntryPropertyDAO();
    
    Long loggedUserId = mapMessage.itemExists("loggedUserId") ? mapMessage.getLong("loggedUserId") : null;
    User loggedUser = loggedUserId != null ? userDAO.findById(loggedUserId) : null;
    String entityClassName = mapMessage.getString("entity");
    Object id = mapMessage.getObject("id");
    Date time = new Date(mapMessage.getLong("time"));
    
    Class<?> entityClass = Class.forName(entityClassName);
    Object entity = systemDAO.findEntityById(entityClass, id);
    
    // First we need to check if ChangeLogEntryEntity is already in the database
    ChangeLogEntryEntity changeLogEntryEntity = entryEntityDAO.findByName(entityClassName);
    if (changeLogEntryEntity == null) {
      // if not we need to add it 
      changeLogEntryEntity = entryEntityDAO.create(entityClassName);
    }
    
    // Then we can add the log entry
    ChangeLogEntry entry = logEntryDAO.create(changeLogEntryEntity, ChangeLogEntryType.Create, String.valueOf(id), time, loggedUser);

    // After the entry has been added we can add all initial properties into the entry
    Set<Attribute<?, ?>> attributes = systemDAO.getEntityAttributes(entityClass);
    for (Attribute<?, ?> attribute : attributes) {
      String fieldName = attribute.getName();
      
      if (TrackedEntityUtils.isTrackedProperty(entityClassName, fieldName)) {
        String value = null;
        
        switch (attribute.getPersistentAttributeType()) {
          case BASIC:
            value = String.valueOf(ReflectionApiUtils.getObjectFieldValue(entity, fieldName, true));
          break;
          case ONE_TO_ONE:
          case MANY_TO_ONE:
            Object joinedEntity = ReflectionApiUtils.getObjectFieldValue(entity, fieldName, true);
            if (joinedEntity != null) {
              value = String.valueOf(getEntityId(attribute.getJavaType(), joinedEntity));
            }
          break;
        }
        
        // We need to check if database already contains this entity property
        ChangeLogEntryEntityProperty changeLogEntryEntityProperty = entryEntityPropertyDAO.findByEntityAndName(changeLogEntryEntity, fieldName);
        if (changeLogEntryEntityProperty == null) {
          // if not we add it there
          changeLogEntryEntityProperty = entryEntityPropertyDAO.create(changeLogEntryEntity, fieldName);
        }
        
        // After entity property has been resolved we can add the property itself
        logEntryPropertyDAO.create(entry, changeLogEntryEntityProperty, value);
      }
    }
  }
  
  private void handleDelete(MapMessage mapMessage) throws JMSException  {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    ChangeLogEntryDAO logEntryDAO = DAOFactory.getInstance().getChangeLogEntryDAO();
    ChangeLogEntryEntityDAO entryEntityDAO = DAOFactory.getInstance().getChangeLogEntryEntityDAO();
    
    Long loggedUserId = mapMessage.itemExists("loggedUserId") ? mapMessage.getLong("loggedUserId") : null;
    User loggedUser = loggedUserId != null ? userDAO.findById(loggedUserId) : null;    
    String entityClassName = mapMessage.getString("entity");
    Object id = mapMessage.getObject("id");
    Date time = new Date(mapMessage.getLong("time"));
    
    // First we need to check if ChangeLogEntryEntity is already in the database
    ChangeLogEntryEntity changeLogEntryEntity = entryEntityDAO.findByName(entityClassName);
    if (changeLogEntryEntity == null) {
      // if not we need to add it 
      changeLogEntryEntity = entryEntityDAO.create(entityClassName);
    }
    
    // Then we can add the log entry it self
    logEntryDAO.create(changeLogEntryEntity, ChangeLogEntryType.Delete, String.valueOf(id), time, loggedUser);
  }
  
  private void handleUpdate(MapMessage mapMessage) throws JMSException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InvocationTargetException  {
    SystemDAO systemDAO = DAOFactory.getInstance().getSystemDAO();
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    ChangeLogEntryDAO logEntryDAO = DAOFactory.getInstance().getChangeLogEntryDAO();
    ChangeLogEntryEntityDAO entryEntityDAO = DAOFactory.getInstance().getChangeLogEntryEntityDAO();
    ChangeLogEntryEntityPropertyDAO entryEntityPropertyDAO = DAOFactory.getInstance().getChangeLogEntryEntityPropertyDAO();
    ChangeLogEntryPropertyDAO logEntryPropertyDAO = DAOFactory.getInstance().getChangeLogEntryPropertyDAO();
    
    Long loggedUserId = mapMessage.itemExists("loggedUserId") ? mapMessage.getLong("loggedUserId") : null;
    User loggedUser = loggedUserId != null ? userDAO.findById(loggedUserId) : null;    
    String entityClassName = mapMessage.getString("entity");
    Object id = mapMessage.getObject("id");
    Date time = new Date(mapMessage.getLong("time"));

    Class<?> entityClass = Class.forName(entityClassName);
    Object entity = systemDAO.findEntityById(entityClass, id);
    Map<ChangeLogEntryEntityProperty, String> values = new HashMap<ChangeLogEntryEntityProperty, String>();
    
    // First we need to check if ChangeLogEntryEntity is already in the database
    ChangeLogEntryEntity changeLogEntryEntity = entryEntityDAO.findByName(entityClassName);
    if (changeLogEntryEntity == null) {
      // if not we need to add it 
      changeLogEntryEntity = entryEntityDAO.create(entityClassName);
    }

    // After that we add all changed properties into the values map
    Set<Attribute<?, ?>> attributes = systemDAO.getEntityAttributes(entityClass);
    for (Attribute<?, ?> attribute : attributes) {
      String fieldName = attribute.getName();
      
      if (TrackedEntityUtils.isTrackedProperty(entityClassName, fieldName)) {
     
        ChangeLogEntryEntityProperty changeLogEntryEntityProperty = entryEntityPropertyDAO.findByEntityAndName(changeLogEntryEntity, fieldName);
        if (changeLogEntryEntityProperty == null) {
          changeLogEntryEntityProperty = entryEntityPropertyDAO.create(changeLogEntryEntity, fieldName);
        }
        
        String newValue = null;
        
        switch (attribute.getPersistentAttributeType()) {
          case BASIC:
            newValue = String.valueOf(ReflectionApiUtils.getObjectFieldValue(entity, fieldName, true));
          break;
          case ONE_TO_ONE:
          case MANY_TO_ONE:
            Object joinedEntity = ReflectionApiUtils.getObjectFieldValue(entity, fieldName, true);
            if (joinedEntity != null) {
              newValue = String.valueOf(getEntityId(attribute.getJavaType(), joinedEntity));
            }
          break;
        }
        
        ChangeLogEntryProperty changeLogEntryProperty = logEntryPropertyDAO.findLatestByEntryEntityProperty(changeLogEntryEntityProperty, String.valueOf(id));
        String oldValue = changeLogEntryProperty != null ? changeLogEntryProperty.getValue() : null;

        if (newValue == null ? oldValue != null ? true : false : !newValue.equals(oldValue)) {
          values.put(changeLogEntryEntityProperty, newValue);
        }
      }
    }
    
    // And finally we can iterate values map values into the database
    if (!values.isEmpty()) {
      ChangeLogEntry entry = logEntryDAO.create(changeLogEntryEntity, ChangeLogEntryType.Update, String.valueOf(id), time, loggedUser);
      for (ChangeLogEntryEntityProperty property : values.keySet()) {
        logEntryPropertyDAO.create(entry, property, values.get(property));
      }
    }
    
  } 
  
  private Object getEntityId(Class<?> entityClass, Object entity) {
    SystemDAO systemDAO = DAOFactory.getInstance().getSystemDAO();
    Attribute<?, ?> idAttribute = systemDAO.getEntityIdAttribute(entityClass);
    try {
      return ReflectionApiUtils.getObjectFieldValue(entity, idAttribute.getName(), true);
    } catch (Exception e) {
      return null;
    }
  }
}
