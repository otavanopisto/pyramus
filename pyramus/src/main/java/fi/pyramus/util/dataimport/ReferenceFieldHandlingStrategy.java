package fi.pyramus.util.dataimport;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.framework.PyramusStatusCode;

@SuppressWarnings("rawtypes")
public class ReferenceFieldHandlingStrategy implements FieldHandlingStrategy {

  private final Class entityClass;
  private final String fieldName;
  private final Class referencedClass;

  /**
   * 
   * @param entityClass Entity class to save data into
   * @param fieldName Field name in entityClass entity to save data into
   * @param referencedClass Class of the object that fieldName in entity is referring to
   */
  public ReferenceFieldHandlingStrategy(Class entityClass, String fieldName, Class referencedClass) {
    this.entityClass = entityClass;
    this.referencedClass = referencedClass;
    this.fieldName = fieldName;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void handleField(String fieldName, Object fieldValue, DataImportContext context) throws SecurityException, IllegalArgumentException,
      NoSuchFieldException, IllegalAccessException {
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

    Long id = Long.valueOf((String) fieldValue);
    fieldValue = DAOFactory.getInstance().getSystemDAO().findEntityById(referencedClass, id);
    
    Field field = DataImportUtils.getField(entity, this.fieldName != null ? this.fieldName : fieldName);
    DataImportUtils.setFieldValue(entity, field, fieldValue);
  }

}
