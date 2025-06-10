package fi.otavanopisto.pyramus.persistence.events;

import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.MapMessage;
import jakarta.jms.Message;
import jakarta.jms.MessageProducer;
import jakarta.jms.Queue;
import jakarta.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PreRemove;
import jakarta.persistence.metamodel.Attribute;
import jakarta.servlet.http.HttpSession;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.SystemDAO;
import fi.otavanopisto.pyramus.util.ReflectionApiUtils;
import fi.otavanopisto.pyramus.util.ThreadSessionContainer;

public class EntityListener {
  
  @PostPersist
  void onPostPersist(Object entity) {
    String entityName = entity.getClass().getName();
    if (TrackedEntityUtils.isTrackedEntity(entityName)) {
      try {
        Object id = getEntityId(entity);
        if (id != null) {
          Session session = createSession();
          
          MapMessage message = session.createMapMessage();
          message.setLong("time", System.currentTimeMillis());
          message.setString("entity", entity.getClass().getName());
          message.setString("eventType", EventType.Create.name());
          message.setObject("id", id);
          
          Long loggedUserId = getLoggedUserId();
          if (loggedUserId != null)
            message.setLong("loggedUserId", loggedUserId);

          sendMessage(session, message);
        }
      } catch (JMSException e) {
        throw new EventException(e);
      } catch (NamingException e) {
        throw new EventException(e);
      }
    }
  }

   @PostUpdate
  void onPostUpdate(Object entity) {
    String entityName = entity.getClass().getName();
   
    if (TrackedEntityUtils.isTrackedEntity(entityName)) {
      try {
        Object id = getEntityId(entity);
        if (id != null) {
          Session session = createSession();
  
          MapMessage message = session.createMapMessage();
          message.setLong("time", System.currentTimeMillis());
          message.setString("entity", entity.getClass().getName());
          message.setString("eventType", EventType.Update.name());
          message.setObject("id", id);
          
          Long loggedUserId = getLoggedUserId();
          if (loggedUserId != null)
            message.setLong("loggedUserId", loggedUserId);

          sendMessage(session, message);
        }
      } catch (JMSException e) {
        throw new EventException(e);
      } catch (NamingException e) {
        throw new EventException(e);
      }
    }
  }

   @PreRemove
  void onPreRemove(Object entity) {
    String entityName = entity.getClass().getName();
    
    if (TrackedEntityUtils.isTrackedEntity(entityName)) {
      try {
        Session session = createSession();
        
        MapMessage message = session.createMapMessage();
        message.setLong("time", System.currentTimeMillis());
        message.setString("entity", entityName);
        message.setString("eventType", EventType.Delete.name());
        message.setObject("id", getEntityId(entity));
        
        Long loggedUserId = getLoggedUserId();
        if (loggedUserId != null)
          message.setLong("loggedUserId", loggedUserId);

        sendMessage(session, message);
      } catch (JMSException e) {
        throw new EventException(e);
      } catch (NamingException e) {
        throw new EventException(e);
      }
    }
  }
  
  private Object getEntityId(Object entity) {
    return getEntityId(entity.getClass(), entity);
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

  private void sendMessage(Session session, Message message) throws NamingException, JMSException {
    Queue queue = getQueue();
    MessageProducer producer = session.createProducer(queue);
    producer.send(message);
  }

  private Session createSession() throws JMSException, NamingException {
    ConnectionFactory factory = getConnectionFactory();
    Connection connection = factory.createConnection();
    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    return session;
  }

  private ConnectionFactory getConnectionFactory() throws NamingException {
    return (ConnectionFactory) new InitialContext().lookup("java:/jms/JPAEventsFactory");
  }

  private Queue getQueue() throws NamingException {
    return (Queue) new InitialContext().lookup("java:/jms/JPAEvents");
  }
  
  private Long getLoggedUserId() {
    HttpSession httpSession = ThreadSessionContainer.getSession();
    if (httpSession != null) {
      return (Long) httpSession.getAttribute("loggedUserId");
    }
    
    return null;
  }

}