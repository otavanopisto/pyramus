package fi.pyramus.reports.viewer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.reports.ReportDAO;
import fi.pyramus.domainmodel.reports.Report;

public class PrepareReportFilter implements Filter {

  public void destroy() {
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    prepareReport(request.getParameter("__report"));
    chain.doFilter(request, response);
  }

  public void init(FilterConfig filterConfig) throws ServletException {
    rootPath = filterConfig.getServletContext().getRealPath("/");
  }

  private File getReportsFolder() {
    File reportsFolder = new File(rootPath + "reports/");
    if (!reportsFolder.exists())
      reportsFolder.mkdirs();

    return reportsFolder;
  }

  private void prepareReport(String reportName) throws IOException {
    synchronized (syncObject) {
      ReportDAO reportDAO = DAOFactory.getInstance().getReportDAO();
      if (StringUtils.startsWith(reportName, "reports/") && StringUtils.endsWith(reportName, ".rptdesign")) {
        Long reportId = NumberUtils.createLong(reportName.substring(8, reportName.length() - 10));
        String reportFileName = String.valueOf(reportId) + ".rptdesign";

        Report report = reportDAO.findById(reportId);
        File reportFile = new File(getReportsFolder(), reportFileName);

        if (reportFile.exists()) {
          Date fileLastModified = new Date(reportFile.lastModified());
          if (fileLastModified.before(report.getLastModified())) {
            FileOutputStream fileOutputStream = new FileOutputStream(reportFile);
            fileOutputStream.write(report.getData().getBytes("UTF-8"));
            fileOutputStream.flush();
            fileOutputStream.close();
          }
        } else {
          if (reportFile.createNewFile()) {
            FileOutputStream fileOutputStream = new FileOutputStream(reportFile);
            fileOutputStream.write(report.getData().getBytes("UTF-8"));
            fileOutputStream.flush();
            fileOutputStream.close();
          }
        }
      }
    }
  }

  private Object syncObject = new Object();
  private String rootPath;
}
