package fi.otavanopisto.pyramus.rest.security;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.security.impl.UserContextResolver;
import fi.otavanopisto.security.ContextReference;

@RequestScoped
public class RESTUserEntityContextResolverImpl implements UserContextResolver {

  @Inject
  private StudentDAO studentDAO;

  @Inject
  private StaffMemberDAO staffMemberDAO;
  
  @Override
  public boolean handlesContextReference(ContextReference contextReference) {
    return 
        fi.otavanopisto.pyramus.rest.model.Student.class.isInstance(contextReference) ||
        fi.otavanopisto.pyramus.rest.model.StaffMember.class.isInstance(contextReference);
  }

  @Override
  public User resolveUser(ContextReference contextReference) {
    if (fi.otavanopisto.pyramus.rest.model.Student.class.isInstance(contextReference)) {
      fi.otavanopisto.pyramus.rest.model.Student student = (fi.otavanopisto.pyramus.rest.model.Student) contextReference;
      Long id = student.getId();
      
      return id != null ? studentDAO.findById(id) : null;
    }
    
    if (fi.otavanopisto.pyramus.rest.model.StaffMember.class.isInstance(contextReference)) {
      fi.otavanopisto.pyramus.rest.model.StaffMember staffMember = (fi.otavanopisto.pyramus.rest.model.StaffMember) contextReference;
      Long id = staffMember.getId();
      
      return id != null ? staffMemberDAO.findById(id) : null;
    }
    
    return null;
  }
  
}
