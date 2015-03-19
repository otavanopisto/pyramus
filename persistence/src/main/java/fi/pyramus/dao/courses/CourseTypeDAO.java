package fi.pyramus.dao.courses;

import javax.ejb.Stateless;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.courses.CourseType;

@Stateless
public class CourseTypeDAO extends PyramusEntityDAO<CourseType> {

  public CourseType create(String name) {
    return create(name, Boolean.FALSE);
  }
  
  public CourseType create(String name, Boolean archived) {
    CourseType courseState = new CourseType();
    courseState.setName(name);
    courseState.setArchived(archived);
    
    return persist(courseState);
  }

  public CourseType updateName(CourseType courseType, String name) {
    courseType.setName(name);
    return persist(courseType);
  }

}
