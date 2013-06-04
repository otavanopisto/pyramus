package fi.pyramus.plugin.openid;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.openid4java.association.AssociationException;
import org.openid4java.consumer.ConsumerException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryException;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.MessageException;
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.AxMessage;
import org.openid4java.message.ax.FetchRequest;
import org.openid4java.message.ax.FetchResponse;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.RequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.users.UserDAO;
import fi.pyramus.dao.users.UserVariableDAO;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.plugin.auth.AuthenticationException;
import fi.pyramus.plugin.auth.ExternalAuthenticationProvider;

public class OpenIDAuthorizationStrategy implements ExternalAuthenticationProvider {

  public OpenIDAuthorizationStrategy() {
//    try {
      consumerManager = new ConsumerManager();
      
//    } catch (ConsumerException e) {
//      throw new SmvcRuntimeException(e);
//    }
  }
  
  public String getName() {
    return "OpenID";
  }
  
  public void performDiscovery(RequestContext requestContext) {
    try {
      HttpSession session = requestContext.getRequest().getSession();

      // perform discovery on the user-supplied identifier
      List<?> discoveries = consumerManager.discover(System.getProperty("authentication.OpenID.identifier"));

      // attempt to associate with the OpenID provider
      // and retrieve one service endpoint for authentication
      DiscoveryInformation discovered = consumerManager.associate(discoveries);
      
      // store the discovery information in the user's session for later use
      session.setAttribute("discovered", discovered);

      // Construct a path back to users/externallogin.page in Pyramus 
      String currentURL = requestContext.getRequest().getRequestURL().toString();
      String pathInfo = requestContext.getRequest().getRequestURI();
      String baseURL = currentURL.substring(0, currentURL.length() - pathInfo.length()); 
      
      StringBuilder returnURL = new StringBuilder(baseURL)
        .append(requestContext.getRequest().getContextPath())
        .append("/users/externallogin.page");
     
      // obtain a AuthRequest message to be sent to the OpenID provider
      AuthRequest authReq = consumerManager.authenticate(discovered, returnURL.toString());
      
      // Attribute Exchange example: fetching the 'email' attribute
      FetchRequest fetch = FetchRequest.createFetchRequest();
      fetch.addAttribute("email",
              // attribute alias
              "http://schema.openid.net/contact/email",   // type URI
              true);                                      // required

      // attach the extension to the authentication request
      authReq.addExtension(fetch);
      
      requestContext.setRedirectURL(authReq.getDestinationUrl(true));
    } catch (DiscoveryException e) {
      throw new SmvcRuntimeException(e);
    } catch (MessageException e) {
      throw new SmvcRuntimeException(e);
    } catch (ConsumerException e) {
      throw new SmvcRuntimeException(e);
    }
  }
  
  @SuppressWarnings("unchecked")
  public User processResponse(RequestContext requestContext) throws AuthenticationException {
    try {
      HttpSession session = requestContext.getRequest().getSession();
      
      // extract the parameters from the authentication response
      // (which comes in as a HTTP request from the OpenID provider)
      ParameterList openidResp = new ParameterList(requestContext.getRequest().getParameterMap());
  
      // retrieve the previously stored discovery information
      DiscoveryInformation discovered = (DiscoveryInformation) session.getAttribute("discovered");
  
      // extract the receiving URL from the HTTP request
      StringBuffer receivingURL = requestContext.getRequest().getRequestURL();
      String queryString = requestContext.getRequest().getQueryString();
      if (queryString != null && queryString.length() > 0) {
        receivingURL.append("?").append(requestContext.getRequest().getQueryString());
      }
  
      // verify the response
      VerificationResult verification = consumerManager.verify(receivingURL.toString(), openidResp, discovered);
      
      // examine the verification result and extract the verified identifier
      Identifier verified = verification.getVerifiedId();

      if (verified != null) {
        AuthSuccess authSuccess = (AuthSuccess) verification.getAuthResponse();
        List<String> emails = null;
        
        if (authSuccess.hasExtension(AxMessage.OPENID_NS_AX)) {
          FetchResponse fetchResp = (FetchResponse) authSuccess.getExtension(AxMessage.OPENID_NS_AX);
          emails = fetchResp.getAttributeValues("email");
        }
        
        UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
        UserVariableDAO userVariableDAO = DAOFactory.getInstance().getUserVariableDAO();
        User user = userDAO.findByExternalIdAndAuthProvider(verified.getIdentifier(), getName());
        if (user == null) {
          user = userDAO.findByEmail(emails.get(0));
          if (user != null) {
            String expectedLoginServer = userVariableDAO.findByUserAndKey(user, "openid.expectedlogin");
            String loginServer = verification.getAuthResponse().getParameterValue("openid.op_endpoint");
            if (!StringUtils.isBlank(expectedLoginServer) && expectedLoginServer.equals(loginServer)) {
              userVariableDAO.setUserVariable(user, "openid.expectedlogin", null);
              userDAO.updateExternalId(user, verified.getIdentifier());
            } else {
              throw new AuthenticationException(AuthenticationException.LOCAL_USER_MISSING);
            }
          } else {
            throw new AuthenticationException(AuthenticationException.LOCAL_USER_MISSING);
          }
        }
        
        return user;
        
      } else {
        return null;
      }
    } catch (MessageException e) {
      throw new SmvcRuntimeException(e);
    } catch (DiscoveryException e) {
      throw new SmvcRuntimeException(e);
    } catch (AssociationException e) {
      throw new SmvcRuntimeException(e);
    }
  }

  private ConsumerManager consumerManager;
}
