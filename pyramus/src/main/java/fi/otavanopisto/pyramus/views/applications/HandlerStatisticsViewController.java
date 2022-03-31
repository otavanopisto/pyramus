package fi.otavanopisto.pyramus.views.applications;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationState;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class HandlerStatisticsViewController extends PyramusViewController {
  
  public void process(PageRequestContext pageRequestContext) {
    Long staffMemberId = pageRequestContext.getLong("staffMemberId");
    
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
    
    StaffMember staffMember = staffMemberDAO.findById(staffMemberId);
    List<ApplicationState> states = Stream.of(
        ApplicationState.PROCESSING,
        ApplicationState.WAITING_STAFF_SIGNATURE,
        ApplicationState.STAFF_SIGNED,
        ApplicationState.APPROVED_BY_SCHOOL,
        ApplicationState.APPROVED_BY_APPLICANT).collect(Collectors.toList());
    List<Application> applications = applicationDAO.listByHandlerAndStatesAndArchived(staffMember, states, Boolean.FALSE);
    
    int processing = 0;
    int waitingStaffSignature = 0;
    int staffSigned = 0;
    int approvedBySchool = 0;
    int approvedByApplicant = 0;
    
    for (Application application : applications) {
      switch (application.getState()) {
        case PROCESSING:
          processing++;
          break;
        case WAITING_STAFF_SIGNATURE:
          waitingStaffSignature++;
          break;
        case STAFF_SIGNED:
          staffSigned++;
          break;
        case APPROVED_BY_SCHOOL:
          approvedBySchool++;
          break;
        case APPROVED_BY_APPLICANT:
          approvedByApplicant++;
          break;
        default:
          break;
      }
    }
    pageRequestContext.getRequest().setAttribute("processing", processing);
    pageRequestContext.getRequest().setAttribute("waitingStaffSignature", waitingStaffSignature);
    pageRequestContext.getRequest().setAttribute("staffSigned", staffSigned);
    pageRequestContext.getRequest().setAttribute("approvedBySchool", approvedBySchool);
    pageRequestContext.getRequest().setAttribute("approvedByApplicant", approvedByApplicant);
    pageRequestContext.setIncludeJSP("/templates/applications/handlerstatistics.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
