package fi.pyramus.binary.users;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.queryParser.QueryParser;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.BinaryRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.users.UserDAO;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.framework.BinaryRequestController;
import fi.pyramus.framework.UserRole;

/** A binary request controller responsible for providing server-side autocomplete
 * for users search.
 *
 */
public class UsersAutoCompleteBinaryRequestController extends BinaryRequestController {

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
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();

    String text = binaryRequestContext.getString("text");

    StringBuilder resultBuilder = new StringBuilder();
    resultBuilder.append("<ul>");

    if (!StringUtils.isBlank(text)) {
      text = QueryParser.escape(StringUtils.trim(text)) + '*';
      
      List<User> users = userDAO.searchUsersBasic(100, 0, text).getResults();
      
      for (User user : users) {
        addUser(resultBuilder, user);
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
    return new UserRole[] { UserRole.USER, UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }
  
  private void addUser(StringBuilder resultBuilder, User user) {
    resultBuilder
      .append("<li>")
      .append("<span>")
      .append(StringEscapeUtils.escapeHtml(user.getFullName()))
      .append("</span>")
      .append("<input type=\"hidden\" name=\"userId\" value=\"")
      .append(user.getId())
      .append("\"/>")
      .append("</li>");
  }
}
