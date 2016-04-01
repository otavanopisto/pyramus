package fi.otavanopisto.pyramus.plugin.testauth;

import fi.internetix.smvc.controllers.RequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.plugin.auth.AuthenticationException;
import fi.otavanopisto.pyramus.plugin.auth.ExternalAuthenticationProvider;
import fi.otavanopisto.pyramus.plugin.auth.LocalUserMissingException;

public class TestAuthorizationStrategy implements ExternalAuthenticationProvider {
  
  public String getName() {
    return "TestAuth";
  }
  
  public void performDiscovery(RequestContext requestContext) {
  }
  
  public StaffMember processResponse(RequestContext requestContext) throws AuthenticationException {
    StaffMemberDAO staffDAO = DAOFactory.getInstance().getStaffMemberDAO();
    Long userid = requestContext.getLong("testuserid");
    if(userid != null){
      StaffMember user = staffDAO.findById(userid);
      if(user != null){
        return user;
      } else {
        throw new LocalUserMissingException("test.auth@example.com");
      }
    }else{
      return null;
    }
    
  }

  @Override
  public String logout(RequestContext requestContext) {
    return null;
  }
  
  
}
