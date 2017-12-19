package fi.otavanopisto.pyramus.binary.applications;

import fi.internetix.smvc.controllers.BinaryRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.BinaryRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.json.applications.OnnistuuClient;

public class StaffSignatureDocumentRequestController extends BinaryRequestController {

  public void process(BinaryRequestContext binaryRequestContext) {
    OnnistuuClient oc = OnnistuuClient.getInstance();
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StaffMember staffMember = staffMemberDAO.findById(binaryRequestContext.getLoggedUserId());
    binaryRequestContext.setResponseContent(oc.generateStaffSignatureDocument(binaryRequestContext, "Kerkko Paavali Perämetsä", "nettipk", staffMember), "application/pdf");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER };
  }

}
