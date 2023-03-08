package fi.otavanopisto.pyramus.taglib;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.StaffMemberProperties;

public class ApplicationAccessTag extends TagSupport {

  private static final long serialVersionUID = -908100635526804785L;
  
  public int doStartTag() throws JspException {
    HttpSession session = pageContext.getSession();
    Long userId = session == null ? null : (Long) session.getAttribute("loggedUserId");
    if (userId == null) {
      return SKIP_BODY;
    }
    boolean isAdmin = StringUtils.equals(Role.ADMINISTRATOR.name(), (String) session.getAttribute("loggedUserRole"));
    if (isAdmin) {
      return EVAL_BODY_INCLUDE;
    }
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StaffMember staffMember = staffMemberDAO.findById(userId);
    boolean aineopiskelu = "1".equals(staffMember.getProperties().get(StaffMemberProperties.APPLICATIONS_AINEOPISKELU.getKey()));
    boolean nettilukio = "1".equals(staffMember.getProperties().get(StaffMemberProperties.APPLICATIONS_NETTILUKIO.getKey()));
    boolean nettipk = "1".equals(staffMember.getProperties().get(StaffMemberProperties.APPLICATIONS_NETTIPERUSKOULU.getKey()));
    boolean aikuislukio = "1".equals(staffMember.getProperties().get(StaffMemberProperties.APPLICATIONS_AIKUISLUKIO.getKey()));
    boolean mk = "1".equals(staffMember.getProperties().get(StaffMemberProperties.APPLICATIONS_AIKUISTENPERUSOPETUS.getKey()));
    if (StringUtils.isEmpty(line)) {
      return aineopiskelu || nettilukio || nettipk || aikuislukio || mk ? EVAL_BODY_INCLUDE : SKIP_BODY;
    }
    else if (StringUtils.equals(line, "aineopiskelu")) {
      return aineopiskelu ? EVAL_BODY_INCLUDE : SKIP_BODY;
    }
    else if (StringUtils.equals(line, "nettilukio")) {
      return nettilukio ? EVAL_BODY_INCLUDE : SKIP_BODY;
    }
    else if (StringUtils.equals(line, "nettipk")) {
      return nettipk ? EVAL_BODY_INCLUDE : SKIP_BODY;
    }
    else if (StringUtils.equals(line, "aikuislukio")) {
      return aikuislukio ? EVAL_BODY_INCLUDE : SKIP_BODY;
    }
    else if (StringUtils.equals(line, "mk")) {
      return mk ? EVAL_BODY_INCLUDE : SKIP_BODY;
    }
    else {
      return SKIP_BODY;
    }
  }
  
  public void setLine(String line) {
    this.line = line;
  }
  
  private String line;
  
  
}
