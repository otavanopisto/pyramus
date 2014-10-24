package fi.pyramus.plugin.testauth;

import fi.internetix.smvc.controllers.RequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.users.StaffMemberDAO;
import fi.pyramus.domainmodel.users.StaffMember;
import fi.pyramus.plugin.auth.AuthenticationException;
import fi.pyramus.plugin.auth.ExternalAuthenticationProvider;

public class TestAuthorizationStrategy implements ExternalAuthenticationProvider {
  
  public String getName() {
    return "TestAuth";
  }
  
  public void performDiscovery(RequestContext requestContext) {
  }
  
  public StaffMember processResponse(RequestContext requestContext) throws AuthenticationException {
    StaffMemberDAO staffDAO = DAOFactory.getInstance().getStaffDAO();
    Long userid = requestContext.getLong("testuserid");
    if(userid != null){
      StaffMember user = staffDAO.findById(userid);
      if(user != null){
        return user;
      }else{
        throw new AuthenticationException(AuthenticationException.LOCAL_USER_MISSING);
      }
    }else{
      return null;
    }
    
  }
}
