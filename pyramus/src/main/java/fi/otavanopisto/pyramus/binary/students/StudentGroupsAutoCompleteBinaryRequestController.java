package fi.otavanopisto.pyramus.binary.students;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.queryParser.QueryParser;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.BinaryRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.students.StudentGroupDAO;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.framework.BinaryRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

/** A binary request controller responsible for providing server-side autocomplete
 * for student groups search.
 *
 */
public class StudentGroupsAutoCompleteBinaryRequestController extends BinaryRequestController {

  /** Processes a binary request.
   * The request should contain the following parameters:
   * <dl>
   *   <dt><code>text</code></dt>
   *   <dd>Already typed characters.</dd>
   * </dl>
   * 
   * @param binaryRequestContext The context of the binary request.
   */
  public void process(BinaryRequestContext binaryRequestContext) {
    StudentGroupDAO studentGroupDAO = DAOFactory.getInstance().getStudentGroupDAO();
    String text = binaryRequestContext.getString("text");
    
    
    StringBuilder resultBuilder = new StringBuilder();
    resultBuilder.append("<ul>");

    if (!StringUtils.isBlank(text)) {
      text = QueryParser.escape(StringUtils.trim(text)) + '*';

      List<StudentGroup> studentGroups = studentGroupDAO.searchStudentGroupsBasic(100, 0, text).getResults();

      for (StudentGroup studentGroup : studentGroups) {
        addResult(resultBuilder, studentGroup);
      }
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
  
  private void addResult(StringBuilder resultBuilder, StudentGroup studentGroup) {
    resultBuilder
      .append("<li>")
      .append("<span>")
      .append(StringEscapeUtils.escapeHtml(studentGroup.getName()))
      .append("</span>")
      .append("<input type=\"hidden\" name=\"studentGroupId\" value=\"")
      .append(studentGroup.getId())
      .append("\"/>")
      .append("</li>");
  }
}
