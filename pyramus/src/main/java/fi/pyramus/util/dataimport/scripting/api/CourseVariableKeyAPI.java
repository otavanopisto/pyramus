package fi.pyramus.util.dataimport.scripting.api;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.CourseBaseVariableKeyDAO;
import fi.pyramus.domainmodel.base.CourseBaseVariableKey;
import fi.pyramus.domainmodel.base.VariableType;
import fi.pyramus.util.dataimport.scripting.InvalidScriptException;

public class CourseVariableKeyAPI {
  
  public CourseVariableKeyAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }
  
  public Long create(String key, String name, String type, Boolean userEditable) throws InvalidScriptException {
    VariableType variableType = VariableType.valueOf(type);
    if (variableType == null) {
      throw new InvalidScriptException(String.format("Invalid variable type %s", type));
    }
    
    CourseBaseVariableKeyDAO courseBaseVariableKeyDAO = DAOFactory.getInstance().getCourseBaseVariableKeyDAO();
    CourseBaseVariableKey variableKey = courseBaseVariableKeyDAO.findByVariableKey(key);
    if (variableKey == null) {
      return courseBaseVariableKeyDAO.create(key, name, variableType, userEditable).getId();
    } else {
      throw new InvalidScriptException("Course variable key already exists");
    }
  }
  
  public Long findIdByKey(String key) {
    CourseBaseVariableKeyDAO courseBaseVariableKeyDAO = DAOFactory.getInstance().getCourseBaseVariableKeyDAO();
    CourseBaseVariableKey variableKey = courseBaseVariableKeyDAO.findByVariableKey(key);
    return variableKey != null ? variableKey.getId() : null;
  }
  
  @SuppressWarnings("unused")
  private Long loggedUserId;
}
