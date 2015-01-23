package fi.pyramus.rest.security;

import javax.inject.Inject;

import fi.muikku.security.ContextReference;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.security.impl.UserContextResolver;

public class RESTCourseStudentUserContextResolverImpl implements UserContextResolver {

  @Inject
  private StudentDAO studentDAO;

  @Override
  public boolean handlesContextReference(ContextReference contextReference) {
    return 
        fi.pyramus.rest.model.CourseStudent.class.isInstance(contextReference);
  }

  @Override
  public User resolveUser(ContextReference contextReference) {
    if (fi.pyramus.rest.model.CourseStudent.class.isInstance(contextReference)) {
      fi.pyramus.rest.model.CourseStudent student = (fi.pyramus.rest.model.CourseStudent) contextReference;
      Long id = student.getStudentId();
      
      return id != null ? studentDAO.findById(id) : null;
    }
    
    return null;
  }
  
}
