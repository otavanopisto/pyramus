package fi.pyramus.views.reports;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Status;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.MagicKeyDAO;
import fi.pyramus.domainmodel.base.MagicKey;
import fi.pyramus.domainmodel.base.MagicKeyScope;
import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;

/**
 * The controller responsible of the List Reports view.
 */
public class ViewReportContentsViewController extends PyramusViewController implements Breadcrumbable {
  
  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    MagicKeyDAO magicKeyDAO = DAOFactory.getInstance().getMagicKeyDAO();
    HttpServletRequest request = pageRequestContext.getRequest();

    Long reportId = pageRequestContext.getLong("reportId");
    String reportsContextPath = System.getProperty("reports.contextPath");
    String outputMethod = "preview"; // output or preview
    
    StringBuilder magicKeyBuilder = new StringBuilder()
      .append(Long.toHexString(reportId))
      .append('-')
      .append(Long.toHexString(System.currentTimeMillis()))
      .append('-')
      .append(Long.toHexString(Thread.currentThread().getId()));
    
    MagicKey magicKey = magicKeyDAO.create(magicKeyBuilder.toString(), MagicKeyScope.REQUEST); 
    String pyramusUrl = request.getRequestURL().toString();
    pyramusUrl = pyramusUrl.substring(0, pyramusUrl.length() - request.getRequestURI().length());
    
    StringBuilder urlBuilder;
    try {
      urlBuilder = new StringBuilder()
        .append(reportsContextPath)
        .append("/")
        .append(outputMethod)
        .append("?magicKey=")
        .append(magicKey.getName())
        .append("&pyramusUrl=")
        .append(URLEncoder.encode(pyramusUrl, "UTF-8"))
        .append("&__report=reports/")
        .append(reportId)
        .append(".rptdesign");
      
      Map<String, String[]> parameterMap = pageRequestContext.getRequest().getParameterMap();
      for (String parameterName : parameterMap.keySet()) {
        if (!reservedParameters.contains(parameterName)) {
          String[] values = parameterMap.get(parameterName);
          for (String value : values) {
            // TODO ISO-8859-1 should be UTF-8, once Birt's parameter dialog form has its accept-charset="UTF-8" set 
            try {
              urlBuilder.append('&').append(parameterName).append('=').append(URLEncoder.encode(value, "ISO-8859-1"));
            }
            catch (UnsupportedEncodingException e) {
              throw new SmvcRuntimeException(e);
            }
          }
        }
      }
      
      pageRequestContext.setIncludeUrl(urlBuilder.toString());
    } catch (UnsupportedEncodingException e) {
      throw new SmvcRuntimeException(Status.STATUS_UNKNOWN, "Unsupported encoding", e);
    }
  }

  /**
   * Returns the roles allowed to access this page. Reports are available for users with
   * {@link Role#USER}, {@link Role#MANAGER} and {@link Role#ADMINISTRATOR} privileges.
   * 
   * @return The roles allowed to access this page
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.USER, UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "reports.viewReport.pageTitle");
  }

  private static Set<String> reservedParameters = new HashSet<String>();
  
  static {
    reservedParameters.add("reportId");
    reservedParameters.add("magicKey");
    reservedParameters.add("__report");
  }

}
