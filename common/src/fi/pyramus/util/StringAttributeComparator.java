package fi.pyramus.util;

import java.lang.reflect.Method;
import java.util.Comparator;

public class StringAttributeComparator implements Comparator<Object> {
  
  private final String attributeMethod;
  private final boolean ignoreCase;

  public StringAttributeComparator(String attributeMethod) {
    this(attributeMethod, false);
  }

  public StringAttributeComparator(String attributeMethod, boolean ignoreCase) {
    this.attributeMethod = attributeMethod;
    this.ignoreCase = ignoreCase;
  }
  
  public int compare(Object o1, Object o2) {
    try {
      Object[] params = new Object[] {};
      
      Method method = getMethod(o1, attributeMethod, null);
      String value1 = (String) method.invoke(o1, params);

      method = getMethod(o2, attributeMethod, null);
      String value2 = (String) method.invoke(o2, params);
      
      return value1 == null ? -1 : value2 == null ? 1 : 
        (ignoreCase ? value1.compareToIgnoreCase(value2) : value1.compareTo(value2));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  public static Method getMethod(Object pojo, String methodName, Class<?>[] params) {
    Method method = null;
    
    Class<?> cClass = pojo.getClass();
    while (cClass != null && method == null) {
      try {
        method = cClass.getDeclaredMethod(methodName, params);
      } catch (NoSuchMethodException nsf) {
        cClass = cClass.getSuperclass();
      }
    }
    
    return method;
  }
  
}