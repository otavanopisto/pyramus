package fi.otavanopisto.pyramus.domainmodel.grading;

import java.util.Set;

import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalLength;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;

/**
 * Abstraction layer for Credits that are Student's
 * completion marks of a Course or Course Module.
 * 
 * Abstracts the set of properties there are common
 * for these types of Credits such that those types
 * can be handled with common methods.
 */
public abstract class CourseCredit extends Credit {

  /**
   * Returns the Course name.
   * @return
   */
  public abstract String getCourseName();
  
  /**
   * Returns the Subject of the Credit.
   * @return
   */
  public abstract Subject getSubject();
  
  /**
   * Returns the Course Number of the Credit.
   * @return
   */
  public abstract Integer getCourseNumber();

  /**
   * Returns the Course Length of the Credit.
   * @return
   */
  public abstract EducationalLength getCourseLength();
  
  /**
   * Returns curriculum(s) this credit applies to. 
   * Returns empty set if there is no curriculums,
   * which should be interpreted as the credit applying
   * to any and all curriculums.
   * @return
   */
  public abstract Set<Curriculum> getCurriculums();
}
