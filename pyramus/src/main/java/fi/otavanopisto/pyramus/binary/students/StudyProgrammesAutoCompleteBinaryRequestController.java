package fi.otavanopisto.pyramus.binary.students;

import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.queryparser.classic.QueryParser;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.BinaryRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.StudyProgrammeDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.Archived;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.BinaryRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.framework.UserUtils;

public class StudyProgrammesAutoCompleteBinaryRequestController extends BinaryRequestController {

  public void process(BinaryRequestContext binaryRequestContext) {
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    
    StringBuilder resultBuilder = new StringBuilder();
    resultBuilder.append("<ul>");

    if (!StringUtils.isBlank(binaryRequestContext.getString("text"))) {
      final String text = QueryParser.escape(StringUtils.lowerCase(StringUtils.trim(binaryRequestContext.getString("text"))));

      StaffMember loggedUser = staffMemberDAO.findById(binaryRequestContext.getLoggedUserId());
      
      List<StudyProgramme> studyProgrammes = UserUtils.canAccessAllOrganizations(loggedUser) ? 
          studyProgrammeDAO.listUnarchived() : studyProgrammeDAO.listByOrganization(loggedUser.getOrganization(), Archived.UNARCHIVED);
      
      // Filter studyProgramme list, sort it and convert to return type
      
      studyProgrammes.stream()
        .filter(studyProgramme -> {
          String name = StringUtils.lowerCase(studyProgramme.getName());
          String code = StringUtils.lowerCase(studyProgramme.getCode());
          
          return (name != null && name.contains(text)) || (code != null && code.contains(text));
        })
        .sorted(Comparator.comparing(StudyProgramme::getName))
        .forEach(studyProgramme -> addResult(resultBuilder, studyProgramme));
    }
    
    resultBuilder.append("</ul>");

    try {
      binaryRequestContext.setResponseContent(resultBuilder.toString().getBytes("UTF-8"), "text/html;charset=UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new SmvcRuntimeException(e);
    }
  }
  
  /** Returns the user roles allowed to access this controller.
   * 
   * @return The user roles allowed to access this controller.
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.USER, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }
  
  private void addResult(StringBuilder resultBuilder, StudyProgramme studyProgramme) {
    String studyProgrammeName = StringEscapeUtils.escapeHtml(studyProgramme.getName());
    if (studyProgramme.getOrganization() != null) {
      studyProgrammeName = studyProgrammeName + " (" + StringEscapeUtils.escapeHtml(studyProgramme.getOrganization().getName()) + ")";
    }
    
    resultBuilder
      .append("<li>")
      .append("<span>")
      .append(studyProgrammeName)
      .append("</span>")
      .append("<input type=\"hidden\" name=\"studyProgrammeId\" value=\"")
      .append(studyProgramme.getId())
      .append("\"/>")
      .append(String.format("<input type=\"hidden\" name=\"studyProgrammeName\" value=\"%s\"/>", studyProgrammeName))
      .append("</li>");
  }
}
