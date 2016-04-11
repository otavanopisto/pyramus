package fi.otavanopisto.pyramus.binary.reports;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.BinaryRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.MagicKeyDAO;
import fi.otavanopisto.pyramus.dao.reports.ReportDAO;
import fi.otavanopisto.pyramus.domainmodel.base.MagicKey;
import fi.otavanopisto.pyramus.domainmodel.base.MagicKeyScope;
import fi.otavanopisto.pyramus.domainmodel.reports.Report;
import fi.otavanopisto.pyramus.framework.BinaryRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.ReportUtils;

/** A binary request controller responsible for serving reports
 * in various binary file formats.
 *
 */
public class DownloadReportBinaryRequestController extends BinaryRequestController {

  /** Processes a binary request for a report in binary format.
   * The request should contain the following parameters:
   * <dl>
   *   <dt><code>reportId</code></dt>
   *   <dd>The ID of the report to download.</dd>
   *   <dt><code>format</code></dt>
   *   <dd>The file format of the report. Possible values:
   *     <ul>
   *       <li><code>html</code></li>
   *       <li><code>pdf</code></li>
   *       <li><code>rtf</code></li>
   *       <li><code>doc</code></li>
   *       <li><code>xml</code></li>
   *     </ul>
   *   </dd>
   * </dl>
   * 
   * @param binaryRequestContext The context of the binary request.
   */
  public void process(BinaryRequestContext binaryRequestContext) {
    ReportDAO reportDAO = DAOFactory.getInstance().getReportDAO();
    MagicKeyDAO magicKeyDAO = DAOFactory.getInstance().getMagicKeyDAO();

    Long reportId = binaryRequestContext.getLong("reportId");
    String formatParameter = binaryRequestContext.getString("format");
    ReportOutputFormat outputFormat = Enum.valueOf(ReportOutputFormat.class, formatParameter);
    
    StringBuilder magicKeyBuilder = new StringBuilder()
      .append(Long.toHexString(reportId))
      .append('-')
      .append(Long.toHexString(System.currentTimeMillis()))
      .append('-')
      .append(Long.toHexString(Thread.currentThread().getId()));
  
    MagicKey magicKey = magicKeyDAO.create(magicKeyBuilder.toString(), MagicKeyScope.REQUEST); 
    Report report = reportDAO.findById(reportId);
    
    String reportName = report.getName().toLowerCase().replaceAll("[^a-z0-9\\.]", "_");
    
    String localeAdd = "";
    Locale locale = binaryRequestContext.getRequest().getLocale();
    if (locale != null) {
      localeAdd = "&__locale=";
      localeAdd += locale.getLanguage();

      if (!StringUtils.isEmpty(locale.getCountry())) {
        localeAdd += "_";
        localeAdd += locale.getCountry();
      }
    }
    
    StringBuilder urlBuilder = new StringBuilder()
      .append(ReportUtils.getReportsUrl(binaryRequestContext.getRequest()))
      .append("/preview")
      .append("?magicKey=")
      .append(magicKey.getName())
      .append(localeAdd)
      .append("&__report=reports/")
      .append(reportId)
      .append(".rptdesign")
      .append("&__format=")
      .append(outputFormat.name());
    
    Map<String, String[]> parameterMap = binaryRequestContext.getRequest().getParameterMap();
    for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
      if (!reservedParameters.contains(entry.getKey())) {
        String[] values = entry.getValue();
        for (String value : values) {
          // TODO ISO-8859-1 should be UTF-8, once Birt's parameter dialog form has its accept-charset="UTF-8" set 
          try {
            urlBuilder.append('&').append(entry.getKey()).append('=').append(URLEncoder.encode(value, "ISO-8859-1"));
          }
          catch (UnsupportedEncodingException e) {
            throw new SmvcRuntimeException(e);
          }
        }
      }
    }
    
    binaryRequestContext.setFileName(reportName + '.' + outputFormat.getExt());
    binaryRequestContext.setContentUrl(urlBuilder.toString());
  }
  
  /** Returns the user roles allowed to access this controller.
   * 
   * @return The user roles allowed to access this controller.
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.USER, UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }
  
  private enum ReportOutputFormat {
    HTML ("html"),
    PDF  ("pdf"),
    RTF  ("rtf"),
    DOC  ("doc"),
    XLS  ("xml");
    
    ReportOutputFormat (String ext) {
      this.ext = ext;
    }
    
    public String getExt() {
      return ext;
    }
    
    private String ext;
  }
  

  private static Set<String> reservedParameters = new HashSet<>();
  
  static {
    reservedParameters.add("reportId");
    reservedParameters.add("magicKey");
    reservedParameters.add("format");
    reservedParameters.add("__format");
    reservedParameters.add("__report");
  }
  
}
