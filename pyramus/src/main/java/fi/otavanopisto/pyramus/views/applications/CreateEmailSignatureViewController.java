package fi.otavanopisto.pyramus.views.applications;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.AccessDeniedException;
import fi.internetix.smvc.Feature;
import fi.internetix.smvc.LoginRequiredException;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.internetix.smvc.controllers.RequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.users.EmailSignatureDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.users.EmailSignature;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class CreateEmailSignatureViewController extends PyramusViewController {

  public void process(PageRequestContext pageRequestContext) {
    switch (pageRequestContext.getRequest().getMethod()) {
    case "GET":
      doGet(pageRequestContext);
      break;
    case "POST":
      doPost(pageRequestContext);
      break;
    }
  }
  
  private void doGet(PageRequestContext pageRequestContext) {
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StaffMember staffMember = staffMemberDAO.findById(pageRequestContext.getLoggedUserId());
    EmailSignatureDAO emailSignatureDAO = DAOFactory.getInstance().getEmailSignatureDAO();
    EmailSignature emailSignature = emailSignatureDAO.findByUser(staffMember);
    if (emailSignature != null) {
      pageRequestContext.getRequest().setAttribute("signature", emailSignature.getSignature());
    }
    pageRequestContext.setIncludeJSP("/templates/users/createemailsignature.jsp");
  }

  private void doPost(PageRequestContext pageRequestContext) {
    String signature = pageRequestContext.getString("signature");
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StaffMember staffMember = staffMemberDAO.findById(pageRequestContext.getLoggedUserId());
    EmailSignatureDAO emailSignatureDAO = DAOFactory.getInstance().getEmailSignatureDAO();
    EmailSignature emailSignature = emailSignatureDAO.findByUser(staffMember);
    if (StringUtils.isBlank(signature) && emailSignature != null) {
      emailSignatureDAO.delete(emailSignature);
    }
    else if (emailSignature == null) {
      emailSignature = emailSignatureDAO.create(staffMember, signature);
    }
    else {
      emailSignatureDAO.updateSignature(emailSignature, signature);
    }
    pageRequestContext.setRedirectURL(pageRequestContext.getReferer(true));
  }

  @Override
  public void authorize(RequestContext requestContext) throws LoginRequiredException, AccessDeniedException {
    if (!requestContext.hasFeature(Feature.APPLICATION_MANAGEMENT)) {
      throw new AccessDeniedException(requestContext.getRequest().getLocale());
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }

  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "generic.navigation.createEmailSignature");
  }

}
