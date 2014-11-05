package fi.pyramus.views.users;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ResponseType;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.clientapplications.ClientApplicationAuthorizationCodeDAO;
import fi.pyramus.dao.clientapplications.ClientApplicationDAO;
import fi.pyramus.dao.users.StaffMemberDAO;
import fi.pyramus.domainmodel.clientapplications.ClientApplication;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.framework.PyramusFormViewController;
import fi.pyramus.framework.UserRole;

public class AuthorizeClientApplicationViewController extends PyramusFormViewController {

  @Override
  public void processForm(PageRequestContext requestContext) {

    HttpServletRequest request = requestContext.getRequest();
    OAuthAuthzRequest oauthRequest = null;
    OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

    ClientApplicationDAO clientApplicationDAO = DAOFactory.getInstance().getClientApplicationDAO();

    try {
      oauthRequest = new OAuthAuthzRequest(request);

      ClientApplication clientApplication = clientApplicationDAO.findByClientId(oauthRequest.getClientId());
      
      if (clientApplication != null) {
        request.getSession().setAttribute("clientAppId", oauthRequest.getClientId());
        String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);

        if (responseType.equals(ResponseType.CODE.toString())) {
          final String authorizationCode = oauthIssuerImpl.authorizationCode();
          request.getSession().setAttribute("pendingAuthCode", authorizationCode);
        }

        String redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);

        request.getSession().setAttribute("pendingOauthRedirectUrl", redirectURI);
        request.setAttribute("clientAppName", clientApplication.getClientName());

      } else {
        requestContext.setIncludeJSP("/templates/generic/errorpage.jsp");
        throw new SmvcRuntimeException(HttpServletResponse.SC_FORBIDDEN, "Client application not found");
      }
    } catch (OAuthProblemException | OAuthSystemException e) {
      throw new SmvcRuntimeException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
    }

    requestContext.setIncludeJSP("/templates/users/authorizeclientapp.jsp"); // TODO: show auth page only if everything is ok
  }

  @Override
  public void processSend(PageRequestContext requestContext) {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    ClientApplicationDAO clientApplicationDAO = DAOFactory.getInstance().getClientApplicationDAO();
    ClientApplicationAuthorizationCodeDAO clientApplicationAuthorizationCodeDAO = DAOFactory.getInstance().getClientApplicationAuthorizationCodeDAO();

    HttpServletRequest request = requestContext.getRequest();
    HttpSession session = request.getSession();
    Boolean authorized = "Authorize".equals(request.getParameter("authorize"));

    if (authorized) {
      Long userId = (Long) session.getAttribute("loggedUserId");
      String authorizationCode = (String) session.getAttribute("pendingAuthCode");
      String redirectURI = (String) session.getAttribute("pendingOauthRedirectUrl");
      ClientApplication clientApplication = clientApplicationDAO.findByClientId((String) session.getAttribute("clientAppId"));

      if (userId != null && authorizationCode != null && redirectURI != null && clientApplication != null) {
        try {
          OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND);
          builder.setCode(authorizationCode);
          final OAuthResponse response = builder.location(redirectURI).buildQueryMessage();
          User user = userDAO.findById(userId);
          clientApplicationAuthorizationCodeDAO.create(user, clientApplication, authorizationCode, redirectURI);
          requestContext.setRedirectURL(response.getLocationUri());
        } catch (OAuthSystemException e) {
          requestContext.setIncludeJSP("/templates/generic/errorpage.jsp");
          throw new SmvcRuntimeException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
      } else {
        requestContext.setIncludeJSP("/templates/generic/errorpage.jsp");
        throw new SmvcRuntimeException(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameters");
      }

    }
  }

  @Override
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.USER, UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
