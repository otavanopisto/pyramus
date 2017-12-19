package fi.otavanopisto.pyramus.json.applications;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.xhtmlrenderer.pdf.ITextRenderer;

import fi.internetix.smvc.controllers.RequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.system.SettingDAO;
import fi.otavanopisto.pyramus.dao.system.SettingKeyDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.system.Setting;
import fi.otavanopisto.pyramus.domainmodel.system.SettingKey;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;

public class OnnistuuClient {
  
  public static OnnistuuClient getInstance() {
    return INSTANCE;
  }
  
  public void test() {
    /*
    try {
      String body = "";
     
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] bodyBytes = md.digest(body.getBytes());
      bodyBytes = Base64.getEncoder().encode(bodyBytes);
      
      String contentMd5 = new String(bodyBytes);
      String dateString = dateToRFC2822(new Date());

      String join = "GET\n" + contentMd5 + "\n" + APPLICATION_JSON + "\n" + dateString + "\n/api/v1/document/____DOCID";

      String key = getSecretKey();;
      SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.getDecoder().decode(key.getBytes()), HMAC_SHA512);
      Mac mac = Mac.getInstance(HMAC_SHA512);
      mac.init(secretKeySpec);
      String authEnd = new String(Base64.getEncoder().encode(mac.doFinal(join.getBytes())));
      
      StringBuilder sb = new StringBuilder();
      sb.append("Onnistuu ");
      sb.append(getClientIdentifier());
      sb.append(":");
      sb.append(authEnd);

      Client client = ClientBuilder.newClient();
      WebTarget target = client.target("https://www.onnistuu.fi/api/v1/document/____DOCID");
      Builder request = target
        .request(MediaType.APPLICATION_JSON_TYPE)
        .header("Content-MD5", new String(bodyBytes))
        .header("Date", dateString)
        .header("Authorization", sb);
      
      Response response = request.get();
      System.out.println(response.readEntity(String.class));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    */
  }
  
  private String getClientIdentifier() {
    SettingKeyDAO settingKeyDAO = DAOFactory.getInstance().getSettingKeyDAO();
    SettingKey settingKey = settingKeyDAO.findByName("applications.visma.clientIdentifier");
    if (settingKey != null) {
      SettingDAO settingDAO = DAOFactory.getInstance().getSettingDAO();
      Setting setting = settingDAO.findByKey(settingKey);
      if (setting != null) {
        return setting.getValue();
      }
    }
    return null;
  }

  private String getSecretKey() {
    SettingKeyDAO settingKeyDAO = DAOFactory.getInstance().getSettingKeyDAO();
    SettingKey settingKey = settingKeyDAO.findByName("applications.visma.secretKey");
    if (settingKey != null) {
      SettingDAO settingDAO = DAOFactory.getInstance().getSettingDAO();
      Setting setting = settingDAO.findByKey(settingKey);
      if (setting != null) {
        return setting.getValue();
      }
    }
    return null;
  }
  
  private Long getPrimarySignerId() {
    SettingKeyDAO settingKeyDAO = DAOFactory.getInstance().getSettingKeyDAO();
    SettingKey settingKey = settingKeyDAO.findByName("applications.defaultSignerId");
    if (settingKey != null) {
      SettingDAO settingDAO = DAOFactory.getInstance().getSettingDAO();
      Setting setting = settingDAO.findByKey(settingKey);
      if (setting != null) {
        return NumberUtils.toLong(setting.getValue());
      }
    }
    return null;
  }
  
  private String dateToRFC2822(Date date) {
    return new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH).format(date);
  }
  
  public byte[] generateStaffSignatureDocument(RequestContext requestContext, String applicant, String line, StaffMember signer) {
    try {
      HttpServletRequest httpRequest = requestContext.getRequest();
      StringBuilder baseUrl = new StringBuilder();
      baseUrl.append(httpRequest.getScheme());
      baseUrl.append("://");
      baseUrl.append(httpRequest.getServerName());
      baseUrl.append(":");
      baseUrl.append(httpRequest.getServerPort());
      
      // Staff signed document skeleton
      
      String document = IOUtils.toString(requestContext.getServletContext().getResourceAsStream(
          "/templates/applications/document-staff-signed.html"), "UTF-8");
      
      // Replace document date
      
      document = StringUtils.replace(document, "[DOCUMENT-DATE]", new SimpleDateFormat("d.M.yyyy").format(new Date()));
      
      // Replace applicant name
      
      document = StringUtils.replace(document, "[DOCUMENT-APPLICANT]", applicant);
      
      // Replace line specific welcome text
      
      String welcomeText = IOUtils.toString(requestContext.getServletContext().getResourceAsStream(
          String.format("/templates/applications/document-acceptance-%s.html", line)), "UTF-8");
      document = StringUtils.replace(document, "[DOCUMENT-TEXT]", welcomeText);
      
      // Replace primary and (optional) secondary signers
      
      Long primarySignerId = getPrimarySignerId();
      if (primarySignerId == null) {
        primarySignerId = signer.getId();
      }
      if (primarySignerId.equals(signer.getId())) {
        document = StringUtils.replace(document, "[DOCUMENT-PRIMARY-SIGNER]", getSignature(signer));
        document = StringUtils.replace(document, "[DOCUMENT-SECONDARY-SIGNER]", "");
      }
      else {
        StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
        StaffMember primarySigner = staffMemberDAO.findById(primarySignerId);
        document = StringUtils.replace(document, "[DOCUMENT-PRIMARY-SIGNER]", getSignature(primarySigner));
        document = StringUtils.replace(document, "[DOCUMENT-SECONDARY-SIGNER]", "<p>Puolesta</p>" + getSignature(signer));
      }
      
      // Convert to PDF
      
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      ITextRenderer renderer = new ITextRenderer();
      renderer.setDocumentFromString(document, baseUrl.toString());
      renderer.layout();
      renderer.createPDF(out);
      return out.toByteArray();
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, "Unable to create staff document", e);
    }
    return null;
  }
  
  private String getSignature(StaffMember staffMember) {
    StringBuffer sb = new StringBuffer();
    sb.append(String.format("<p>%s</p>", staffMember.getFullName()));
    if (!StringUtils.isBlank(staffMember.getTitle())) {
      sb.append(String.format("<p>%s</p>", StringUtils.capitalize(staffMember.getTitle())));
    }
    sb.append("<p>Otavan Opisto</p>");
    return sb.toString();
  }

  private static final OnnistuuClient INSTANCE = new OnnistuuClient();
  private static final String APPLICATION_JSON = "application/json";
  private static final String HMAC_SHA512 = "HmacSHA512";
  private static final Logger logger = Logger.getLogger(OnnistuuClient.class.getName());

}
