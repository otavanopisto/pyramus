package fi.pyramus.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;

import fi.muikku.security.AuthorizationException;

/**
 * Marks method as permission restricted method. Defines Permissions that allow access to method.
 * 
 * @author antti.viljakainen
 */
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RESTPermit {
  /**
   * Permissions to check authorization against. 
   * 
   * @return permissions
   */
  @Nonbinding 
  String[] value() default {};
  
  /**
   * Style of check, default OR, optionally AND (all defined Permissions must apply)
   */
  Style style() default Style.OR;
  
  /**
   * How to handle permission exception, default is just silent ignore of method call
   */
  Handle handle() default Handle.SILENT;
  
  Class<? extends Exception> exceptionClass() default AuthorizationException.class;
  
  enum Style {
    OR,
    AND
  }
  
  enum Handle {
    SILENT,
    EXCEPTION
  }
}
