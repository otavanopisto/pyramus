package fi.pyramus.rest.security;

import javax.inject.Inject;

import fi.muikku.security.ContextReference;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.dao.users.StaffMemberDAO;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.security.impl.UserContextResolver;

public class RESTUserEntityContextResolverImpl implements UserContextResolver {

  @Inject
  private StudentDAO studentDAO;

  @Inject
  private StaffMemberDAO staffMemberDAO;
  
  @Override
  public boolean handlesContextReference(ContextReference contextReference) {
    return 
        fi.pyramus.rest.model.Student.class.isInstance(contextReference) ||
        fi.pyramus.rest.model.StaffMember.class.isInstance(contextReference);
  }

  @Override
  public User resolveUser(ContextReference contextReference) {
    if (fi.pyramus.rest.model.Student.class.isInstance(contextReference)) {
      fi.pyramus.rest.model.Student student = (fi.pyramus.rest.model.Student) contextReference;
      Long id = student.getId();
      
      return id != null ? studentDAO.findById(id) : null;
    }
    
    if (fi.pyramus.rest.model.StaffMember.class.isInstance(contextReference)) {
      fi.pyramus.rest.model.StaffMember staffMember = (fi.pyramus.rest.model.StaffMember) contextReference;
      Long id = staffMember.getId();
      
      return id != null ? staffMemberDAO.findById(id) : null;
    }
    
    return null;
  }
  
}
