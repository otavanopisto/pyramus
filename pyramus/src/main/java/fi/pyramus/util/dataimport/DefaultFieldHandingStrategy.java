package fi.pyramus.util.dataimport;

import java.lang.reflect.Constructor;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.pyramus.framework.PyramusStatusCode;

@SuppressWarnings("rawtypes")
public class DefaultFieldHandingStrategy implements FieldHandlingStrategy {

  private Class entityClass;
  private String fieldName = null;

  public DefaultFieldHandingStrategy(Class entityClass) {
    this.entityClass = entityClass;
  }

  public DefaultFieldHandingStrategy(Class entityClass, String fieldName) {
    this.entityClass = entityClass;
    this.fieldName  = fieldName;
  }
  
  @Override
  public void handleField(String fieldName, Object fieldValue, DataImportContext context) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
    DataImportUtils.setValue(getEntity(entityClass, context), this.fieldName != null ? this.fieldName : fieldName, fieldValue);
  }
  
  @SuppressWarnings("unchecked")
  protected Object getEntity(Class entityClass, DataImportContext context) {
    Object entity = context.getEntity(entityClass);

    if (entity == null) {
      Constructor<?> defaultConstructor;
      try {
        defaultConstructor = entityClass.getDeclaredConstructor(new Class[] {});
        defaultConstructor.setAccessible(true);
        entity = defaultConstructor.newInstance(new Object[] {});
        context.addEntity(entityClass, entity);
      } catch (Exception e) {
        throw new SmvcRuntimeException(PyramusStatusCode.OK, "Couldn't instantiate entityClass");
      }
    }
    
    return entity;
  }
}
