package fi.otavanopisto.pyramus.util.dataimport;

import java.util.Set;

import javax.validation.ConstraintViolation;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.SystemDAO;
import fi.otavanopisto.pyramus.framework.PyramusStatusCode;

@SuppressWarnings("rawtypes")
public class DefaultEntityHandlingStrategy implements EntityHandlingStrategy {

  private EntityImportStrategy entityStrategy;

  public DefaultEntityHandlingStrategy(Class entityClass, EntityImportStrategy entityStrategy) {
    this.entityClass = entityClass;
    this.entityStrategy = entityStrategy;
  }
  
  @Override
  public void handleValue(String fieldName, Object value, DataImportContext context) 
      throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
    FieldHandlingStrategy fieldHandler = DataImportStrategyProvider.instance().getFieldHandler(entityStrategy, fieldName); 
    
    if (fieldHandler == null)
      throw new NoSuchFieldException("Entity handler cannot find required field handler: " + fieldName);
    
    fieldHandler.handleField(fieldName, value, context);
  }
  
  private Class entityClass;

  public void initializeContext(DataImportContext context) {
  }
  
  protected void bindEntities(DataImportContext context) {
  }
  
  @Override
  public void saveEntities(DataImportContext context) {
    bindEntities(context);
    
    Object[] entities = context.getEntities();
    SystemDAO systemDAO = DAOFactory.getInstance().getSystemDAO();
    
    for (int i = 0; i < entities.length; i++) {
      Object entity = entities[i];
      
      Set<ConstraintViolation<Object>> constraintViolations = systemDAO.validateEntity(entity);

      if (constraintViolations.size() == 0) {
        systemDAO.persistEntity(entity);
      } else {
        String message = "";
        for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
          message += constraintViolation.getMessage() + '\n';
        }
        
        throw new SmvcRuntimeException(PyramusStatusCode.VALIDATION_FAILURE, "Validation failure: " + message);
      }
    }
  }

  @Override
  public Class getMainEntityClass() {
    return entityClass;
  }
}
