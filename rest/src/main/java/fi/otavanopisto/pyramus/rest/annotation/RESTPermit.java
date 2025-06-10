package fi.otavanopisto.pyramus.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.enterprise.util.Nonbinding;
import jakarta.interceptor.InterceptorBinding;

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
  
  enum Style {
    OR,
    AND
  }
  
  /**
   * How method permit checking is done. Declaration checks are done automatically
   * by SecurityFilter, Inline checks are performed by inline code.  
   */
  Handling handling() default Handling.DECLARATION;

  enum Handling {
    DECLARATION,
    INLINE
  }
}
