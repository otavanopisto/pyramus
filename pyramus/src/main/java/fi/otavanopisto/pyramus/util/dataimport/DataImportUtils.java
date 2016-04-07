package fi.otavanopisto.pyramus.util.dataimport;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.otavanopisto.pyramus.domainmodel.base.VariableType;
import fi.otavanopisto.pyramus.domainmodel.projects.ProjectModuleOptionality;
import fi.otavanopisto.pyramus.domainmodel.students.Sex;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntryType;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.framework.PyramusStatusCode;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.persistence.usertypes.MonetaryAmount;

public class DataImportUtils {

  private DataImportUtils() {
  }

  /**
   * 
   * @param pojo
   * @param methodName
   * @param params
   * @return
   */
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

  /**
   * 
   * @param pojo
   * @param property
   * @param value
   * @throws SecurityException
   * @throws NoSuchFieldException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   */
  public static void setValue(Object pojo, String property, Object value) throws SecurityException, NoSuchFieldException, IllegalArgumentException,  IllegalAccessException {
    Field field = getField(pojo, property);
    Class<?> fieldType = field.getType();

    ValueInterpreter<?> valueInterpreter = DataImportUtils.getValueInterpreter(fieldType);

    if (valueInterpreter != null)
      setFieldValue(pojo, field, valueInterpreter.interpret(value));
    else
      throw new SmvcRuntimeException(PyramusStatusCode.UNDEFINED, "Value interpreter for " + fieldType + " is not implemented yet");
  }

  /**
   * 
   * @param pojo
   * @param property
   * @return
   */
  public static Field getField(Object pojo, String property) {
    Field field = null;
    
    Class<?> cClass = pojo.getClass();
    while (cClass != null && field == null) {
      try {
        field = cClass.getDeclaredField(property);
      } catch (NoSuchFieldException nsf) {
        cClass = cClass.getSuperclass();
      }
    }
    
    return field;
  }
  
  /**
   * 
   * @param pojo
   * @param field
   * @param value
   * @throws SecurityException
   * @throws NoSuchFieldException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   */
  public static void setFieldValue(Object pojo, Field field, Object value) throws SecurityException, NoSuchFieldException, IllegalArgumentException,
      IllegalAccessException {
    field.setAccessible(true);
    field.set(pojo, value);
  }
  
  /**
   * Returns Value Interpreter for given type
   * 
   * @param fieldType
   * @return
   */
  public static ValueInterpreter<?> getValueInterpreter(Class<?> fieldType) {
    return interpreters.get(fieldType);
  }

  
  private static Map<Class<?>, ValueInterpreter<?>> interpreters = new HashMap<Class<?>, ValueInterpreter<?>>();

  static {
    interpreters.put(String.class, new ValueInterpreter<String>() {
      public String interpret(Object o) {
        return (String) o;
      }
    });

    interpreters.put(Long.class, new ValueInterpreter<Long>() {
      public Long interpret(Object o) {
        return NumberUtils.createLong((String) o);
      }
    });

    interpreters.put(Double.class, new ValueInterpreter<Double>() {
      public Double interpret(Object o) {
        return NumberUtils.createDouble((String) o);
      }
    });

    interpreters.put(Boolean.class, new ValueInterpreter<Boolean>() {
      public Boolean interpret(Object o) {
        return "true".equals(o) ? Boolean.TRUE : Boolean.FALSE;
      }
    });

    interpreters.put(Date.class, new ValueInterpreter<Date>() {
      public Date interpret(Object o) {
        if ("NOW".equals(o))
          return new Date(System.currentTimeMillis());
        
        String s = (String) o;
        
        if (s.contains("-")) {
          DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
          try {
            return df.parse(s);
          } catch (ParseException e) {
            e.printStackTrace();
            throw new SmvcRuntimeException(e);
          }
        }

        if (s.contains(".")) {
          DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
          try {
            return df.parse(s);
          } catch (ParseException e) {
            e.printStackTrace();
            throw new SmvcRuntimeException(e);
          }
        }
        
        return new Date(NumberUtils.createLong(s));
      }
    });

    interpreters.put(UserRole.class, new ValueInterpreter<UserRole>() {
      public UserRole interpret(Object o) {
        return UserRole.getRole(NumberUtils.createInteger((String) o));
      }
    });
    
    interpreters.put(Role.class, new ValueInterpreter<Role>() {
      public Role interpret(Object o) {
        return Role.getRole(NumberUtils.createInteger((String) o));
      }
    });
    
    interpreters.put(MonetaryAmount.class, new ValueInterpreter<MonetaryAmount>() {
      public MonetaryAmount interpret(Object o) {
        MonetaryAmount monetaryAmount = new MonetaryAmount();
        monetaryAmount.setAmount(NumberUtils.createDouble((String) o));
        return monetaryAmount;
      }
    });

    interpreters.put(Sex.class, new ValueInterpreter<Sex>() {
      public Sex interpret(Object o) {
        return "male".equalsIgnoreCase((String) o) ? Sex.MALE : Sex.FEMALE;
      }
    });

    interpreters.put(ProjectModuleOptionality.class, new ValueInterpreter<ProjectModuleOptionality>() {
      public ProjectModuleOptionality interpret(Object o) {
        return ProjectModuleOptionality.getOptionality(NumberUtils.createInteger((String) o));
      }
    });
    
    interpreters.put(VariableType.class, new ValueInterpreter<VariableType>() {
      public VariableType interpret(Object o) {
        return VariableType.getType(NumberUtils.createInteger((String) o));
      }
    });

    interpreters.put(StudentContactLogEntryType.class, new ValueInterpreter<StudentContactLogEntryType>() {
      public StudentContactLogEntryType interpret(Object o) {
        return StudentContactLogEntryType.getType(NumberUtils.createInteger((String) o));
      }
    });
    
    interpreters.put(Locale.class, new ValueInterpreter<Locale>() {
      public Locale interpret(Object o) {
        return new Locale((String) o);
      }
    });    

    interpreters.put(Integer.class, new ValueInterpreter<Integer>() {
      public Integer interpret(Object o) {
        return NumberUtils.createInteger((String) o);
      }
    });    
  }
  
}
