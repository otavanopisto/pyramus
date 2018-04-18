package fi.otavanopisto.pyramus.rest.security;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.security.impl.UserContextResolver;
import fi.otavanopisto.security.ContextReference;

@RequestScoped
public class RESTCourseStudentUserContextResolverImpl implements UserContextResolver {

  @Inject
  private StudentDAO studentDAO;

  @Override
  public boolean handlesContextReference(ContextReference contextReference) {
    return 
        fi.otavanopisto.pyramus.rest.model.CourseStudent.class.isInstance(contextReference);
  }

  @Override
  public User resolveUser(ContextReference contextReference) {
    if (fi.otavanopisto.pyramus.rest.model.CourseStudent.class.isInstance(contextReference)) {
      fi.otavanopisto.pyramus.rest.model.CourseStudent student = (fi.otavanopisto.pyramus.rest.model.CourseStudent) contextReference;
      Long id = student.getStudentId();
      
      return id != null ? studentDAO.findById(id) : null;
    }
    
    return null;
  }
  
}
