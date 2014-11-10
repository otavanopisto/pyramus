package fi.pyramus.views.system;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.reports.ReportContextDAO;
import fi.pyramus.dao.reports.ReportDAO;
import fi.pyramus.dao.users.StaffMemberDAO;
import fi.pyramus.domainmodel.reports.Report;
import fi.pyramus.domainmodel.reports.ReportContext;
import fi.pyramus.domainmodel.reports.ReportContextType;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.framework.PyramusFormViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.util.StringAttributeComparator;

@SuppressWarnings("deprecation")
public class ImportReportViewController extends PyramusFormViewController {

  @Override
  public void processForm(PageRequestContext requestContext) {
    ReportDAO reportDAO = DAOFactory.getInstance().getReportDAO();
    ReportContextDAO reportContextDAO = DAOFactory.getInstance().getReportContextDAO();

    List<Report> reports = reportDAO.listAll();
    Collections.sort(reports, new StringAttributeComparator("getName"));
    
    JSONArray contextTypesJSON = new JSONArray();
    List<String> contextTypes = new ArrayList<String>();
    for (ReportContextType contextType : ReportContextType.values()) {
      contextTypes.add(contextType.toString());
      contextTypesJSON.add(contextType.toString());
    }

    JSONArray reportsJSON = new JSONArray();
    for (Report report : reports) {
      JSONObject rObj = new JSONObject();
      
      List<ReportContext> contexts = reportContextDAO.listByReport(report);
      
      JSONArray rCtxs = new JSONArray();
      for (ReportContext ctx : contexts) {
        rCtxs.add(ctx.getContext().toString());
      }

      rObj.put("id", report.getId().toString());
      rObj.put("name", report.getName());
      rObj.put("contexts", rCtxs);
      
      reportsJSON.add(rObj);
    }
    
    setJsDataVariable(requestContext, "reports", reportsJSON.toString());
    setJsDataVariable(requestContext, "contextTypes", contextTypesJSON.toString());
    
    requestContext.getRequest().setAttribute("reports", reports);
    requestContext.getRequest().setAttribute("contextTypes", contextTypes);
    requestContext.setIncludeJSP("/templates/system/importreport.jsp");
  }

  @Override
  public void processSend(PageRequestContext requestContext) {
    ReportDAO reportDAO = DAOFactory.getInstance().getReportDAO();
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    
    Long existingReportId = requestContext.getLong("report");
    String name = requestContext.getString("name");
    FileItem file = requestContext.getFile("file");

    try {
      ByteArrayOutputStream dataStream = null;
      if (file.getSize() > 0) {
        DocumentBuilder db = documentBuilderFactory.newDocumentBuilder();
        Document reportDocument = db.parse(new InputSource(new InputStreamReader(file.getInputStream(), "UTF-8")));
        NodeList hibernateDataSources = XPathAPI.selectNodeList(reportDocument.getDocumentElement(),
            "data-sources/oda-data-source[@extensionID='org.jboss.tools.birt.oda']");
        for (int i = 0, l = hibernateDataSources.getLength(); i < l; i++) {
          Element dataSourceElement = (Element) hibernateDataSources.item(i);
          Node configurationProperyNode = XPathAPI.selectSingleNode(dataSourceElement, "property[@name='configuration']");
          if (configurationProperyNode != null)
            dataSourceElement.removeChild(configurationProperyNode);
  
          Element jndiNameElement = (Element) XPathAPI.selectSingleNode(dataSourceElement, "property[@name='jndiName']");
          if (jndiNameElement == null) {
            jndiNameElement = reportDocument.createElement("property");
            jndiNameElement.setAttribute("name", "jndiName");
            dataSourceElement.appendChild(jndiNameElement);
          }
  
          jndiNameElement.setTextContent("pyramusSessionFactory");
        }
  
        reportDocument.getDocumentElement().getAttributeNode("version").setTextContent("3.2.20");
  
        NodeList jdbcDataSources = XPathAPI.selectNodeList(reportDocument.getDocumentElement(),
            "data-sources/oda-data-source[@extensionID='org.eclipse.birt.report.data.oda.jdbc']");
        for (int i = 0, l = jdbcDataSources.getLength(); i < l; i++) {
          Element dataSourceElement = (Element) jdbcDataSources.item(i);
  
          Node removeNode = XPathAPI.selectSingleNode(dataSourceElement, "property[@name='odaUser']");
          if (removeNode != null)
            dataSourceElement.removeChild(removeNode);
          removeNode = XPathAPI.selectSingleNode(dataSourceElement, "property[@name='odaURL']");
          if (removeNode != null)
            dataSourceElement.removeChild(removeNode);
          removeNode = XPathAPI.selectSingleNode(dataSourceElement, "encrypted-property[@name='odaPassword']");
          if (removeNode != null)
            dataSourceElement.removeChild(removeNode);
          removeNode = XPathAPI.selectSingleNode(dataSourceElement, "property[@name='odaDriverClass']");
          if (removeNode != null)
            dataSourceElement.removeChild(removeNode);
          removeNode = XPathAPI.selectSingleNode(dataSourceElement, "list-property[@name='privateDriverProperties']");
          if (removeNode != null)
            dataSourceElement.removeChild(removeNode);
          
          Element jndiNameElement = (Element) XPathAPI.selectSingleNode(dataSourceElement, "property[@name='odaJndiName']");
          if (jndiNameElement == null) {
            jndiNameElement = reportDocument.createElement("property");
            jndiNameElement.setAttribute("name", "odaJndiName");
            dataSourceElement.appendChild(jndiNameElement);
          }
          
          jndiNameElement.setTextContent("jdbc/pyramus");
        }
  
        dataStream = new ByteArrayOutputStream();
  
        XMLSerializer xmlSerializer = new XMLSerializer(dataStream, new OutputFormat(reportDocument));
        xmlSerializer.serialize(reportDocument);
      }
      
      User loggedUser = userDAO.findById(requestContext.getLoggedUserId());
      
      if (existingReportId != null) {
        Report report = reportDAO.findById(existingReportId);
        if (name == null && dataStream == null) {
          reportDAO.delete(report);
          requestContext.setRedirectURL(requestContext.getReferer(true));
        }
        else {
          if (!StringUtils.isBlank(name)) {
            reportDAO.updateName(report, name, loggedUser);
          }
          if (dataStream != null) {
            reportDAO.updateData(report, dataStream.toString("UTF-8"), loggedUser);
          }
          handleContexts(requestContext, report);
          requestContext.setRedirectURL(requestContext.getRequest().getContextPath()
              + "/reports/viewreport.page?&reportId=" + report.getId());
        }
      }
      else {
        Report report = reportDAO.create(name, dataStream.toString("UTF-8"), loggedUser);
        handleContexts(requestContext, report);
        requestContext.setRedirectURL(requestContext.getRequest().getContextPath()
            + "/reports/viewreport.page?&reportId=" + report.getId());
      }
    }
    catch (IOException e) {
      throw new SmvcRuntimeException(e);
    }
    catch (ParserConfigurationException e) {
      throw new SmvcRuntimeException(e);
    }
    catch (SAXException e) {
      throw new SmvcRuntimeException(e);
    }
    catch (TransformerException e) {
      throw new SmvcRuntimeException(e);
    }
  }

  private void handleContexts(PageRequestContext requestContext, Report report) {
    ReportContextDAO reportContextDAO = DAOFactory.getInstance().getReportContextDAO();
    for (ReportContextType contextType : ReportContextType.values()) {
      ReportContext context = reportContextDAO.findByReportAndContextType(report, contextType);
      
      boolean selected = requestContext.getBoolean("context." + contextType.toString());
      
      if ((selected) && (context == null))
        reportContextDAO.create(report, contextType);
      else if ((!selected) && (context != null))
        reportContextDAO.delete(context);
    }
  }
  
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }

  private DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
}