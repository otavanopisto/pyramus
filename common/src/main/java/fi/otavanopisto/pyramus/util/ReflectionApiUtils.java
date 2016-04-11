package fi.otavanopisto.pyramus.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;

public class ReflectionApiUtils {

  private ReflectionApiUtils() {
  }

  /**
   * Returns object field value using Java reflection API
   * 
   * @param object object
   * @param name name of the field in question
   * @param preferGetter specifies that method tries primarily to get value from getter method instead of getting value directly from field
   * @return field's value
   * @throws IllegalArgumentException IllegalArgumentException
   * @throws IllegalAccessException IllegalAccessException
   * @throws InvocationTargetException InvocationTargetException
   */
  public static Object getObjectFieldValue(Object object, String name, boolean preferGetter) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
    Method getterMethod = preferGetter ? getMethod(object.getClass(), "get" + StringUtils.capitalize(name)) : null;
    
    if (getterMethod != null) {
      return getterMethod.invoke(object);
    } else {
      Field field = getField(object.getClass(), name);
      if (field != null) {
        field.setAccessible(true);
        return field.get(object);
      } else {
        return null;
      }
    }
  }

  public static Method getMethod(Class<?> entityClass, String name) {
    try {
      return entityClass.getDeclaredMethod(name);
    } catch (SecurityException e) {
      return null;
    } catch (NoSuchMethodException e) {
      Class<?> superClass = entityClass.getSuperclass();
      if (superClass != null && !Object.class.equals(superClass))
        return getMethod(superClass, name);
    }

    return null;
  }

  public static Field getField(Class<?> entityClass, String name) {
    try {
      return entityClass.getDeclaredField(name);
    } catch (SecurityException e) {
      return null;
    } catch (NoSuchFieldException e) {
      Class<?> superClass = entityClass.getSuperclass();
      if (superClass != null && !Object.class.equals(superClass))
        return getField(superClass, name);
    }

    return null;
  }
  
  public static boolean isInheritedFrom(Class<?> clazz, Class<?> from) {
    if (clazz.equals(from))
      return true;
    
    Class<?> superclass = clazz.getSuperclass();
    if (superclass != null)
      return isInheritedFrom(superclass, from);
    
    return false;
  }
}
