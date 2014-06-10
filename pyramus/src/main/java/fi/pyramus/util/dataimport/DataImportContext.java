package fi.pyramus.util.dataimport;

import java.util.HashMap;
import java.util.Map;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.users.UserDAO;
import fi.pyramus.domainmodel.users.User;

@SuppressWarnings("rawtypes")
public class DataImportContext {

  public DataImportContext(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }
  
  public Object getEntity(Class c) {
    return entities.get(c);
  }

  public void setEntity(Object entity) {
    entities.put(entity.getClass(), entity);
  }

  public Object[] getEntities() {
    return entities.values().toArray();
  }
  
  public void addEntity(Class c, Object entity) {
    entities.put(c, entity);
  }

  public void setFields(String[] firstLine) {
    this.fields = firstLine;
  }

  public String[] getFirstLine() {
    return this.fields;
  }
  
  public boolean hasField(String fieldName) {
    if (this.fields != null) {
      for (int i = 0; i < this.fields.length; i++) {
        if (fieldName.equals(this.fields[i]))
          return true;
      }
    }
    
    return false;
  }

  public void setFieldValues(String[] values) {
    this.values = values;
  }
  
  public String[] getFieldValues() {
    return this.values;
  }
  
  public String getFieldValue(String fieldName) {
    if ((this.fields != null) && (this.values != null) && (this.fields.length == this.values.length)) {
      for (int i = 0; i < this.fields.length; i++) {
        if (fieldName.equals(this.fields[i]))
          return this.values[i];
      }
    }
    
    return null;
  }

  public User getLoggedUser() {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    this.loggedUser = userDAO.findById(this.loggedUserId); 
    return loggedUser;
  }

  private Map<Class, Object> entities = new HashMap<Class, Object>();
  private String[] fields;
  private String[] values;
  private User loggedUser;
  private Long loggedUserId;
}
