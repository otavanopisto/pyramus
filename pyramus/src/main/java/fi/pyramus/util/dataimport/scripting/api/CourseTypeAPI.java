package fi.pyramus.util.dataimport.scripting.api;

import java.util.ArrayList;
import java.util.List;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.courses.CourseTypeDAO;
import fi.pyramus.domainmodel.courses.CourseType;

public class CourseTypeAPI {
  
  public CourseTypeAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }
  
  public Long create(String name) {
    return create(name, false);
  }
  
  public Long create(String name, Boolean archived) {
    CourseTypeDAO courseTypeDAO = DAOFactory.getInstance().getCourseTypeDAO();
    return courseTypeDAO.create(name, archived).getId();
  }
  
  public Long[] listIdsByName(String name) {
    CourseTypeDAO courseTypeDAO = DAOFactory.getInstance().getCourseTypeDAO();
    
    List<Long> result = new ArrayList<>();
    
    List<CourseType> courseTypes = courseTypeDAO.listByName(name);
    for (CourseType courseType : courseTypes) {
      result.add(courseType.getId());
    }
    
    return result.toArray(new Long[0]); 
  }

  @SuppressWarnings("unused")
  private Long loggedUserId;
}
