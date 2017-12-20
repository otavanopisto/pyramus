package fi.otavanopisto.pyramus.json.applications;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
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
import net.sf.json.JSONObject;

public class OnnistuuClient {
  
  public static OnnistuuClient getInstance() {
    return INSTANCE;
  }
  
  public String createDocument(String name) {
    
    // Payload
    
    JSONObject document = new JSONObject();
    document.put("name",  name);
    JSONObject payload = new JSONObject();
    payload.put("document",  document);
    String json = payload.toString(); 
    
    // Call
    
    Entity<String> entity = Entity.entity(json, MediaType.APPLICATION_JSON);
    String contentMd5 = getMd5(json);
    Response response = doPost("/api/v1/document/", contentMd5, MediaType.APPLICATION_JSON, entity);
    
    // Validation
    
    if (response.getStatus() == 201) {
      String location = StringUtils.substringAfterLast(response.getHeaderString("Location"), "/");
      if (StringUtils.length(location) != 36) { // should be uuid
        // ERROR!
      }
      return location;
    }
    else {
      // ERROR!
    }
    // TODO RuntimeException
    return null;
  }
  
  public void addPdf(String document, byte[] pdf) {
    Entity<byte[]> entity = Entity.entity(pdf, "application/pdf");
    String contentMd5 = getMd5(pdf);
    Response response = doPost(String.format("/api/v1/document/%s/files", document), contentMd5, "application/pdf", entity);
    if (response.getStatus() == 201) {
      
    }
    // ERROR
    // TODO RuntimeException
    
    System.out.println("Response status code: " + response.getStatus());
    if (response.getStatusInfo() != null) {
      System.out.println("Response status reason: " + response.getStatusInfo().getReasonPhrase());
    }
    MultivaluedMap<String, Object> headers = response.getHeaders();
    System.out.println(headers.size() + " headers");
    Set<String> keys = headers.keySet();
    for (String key : keys) {
      System.out.println(key + " = " + headers.get(key));
    }
  }
  
  private Response doGet(String path) {
    String contentMd5 = getMd5("");
    String date = dateToRFC2822(new Date());
    String auth = getAuthorizationHeader("GET", contentMd5, MediaType.APPLICATION_JSON, date, path);
    Client client = ClientBuilder.newClient();
    WebTarget target = client.target(String.format("https://www.onnistuu.fi%s", path));
    Builder request = target
      .request()
      .header("Content-Type", MediaType.APPLICATION_JSON)
      .header("Content-MD5", contentMd5)
      .header("Date", date)
      .header("Authorization", auth);
    return request.get();
  }

  private Response doPost(String path, String contentMd5, String contentType, Entity entity) {
    String date = dateToRFC2822(new Date());
    String auth = getAuthorizationHeader("POST", contentMd5, contentType, date, path);
    Client client = ClientBuilder.newClient();
    WebTarget target = client.target(String.format("https://www.onnistuu.fi%s", path));
    
    System.out.println("POST URI: " + target.getUri().toString());
    System.out.println("POST CONTENT-TYPE: " + contentType);
    System.out.println("POST CONTENT-MD5: " + contentMd5);
    System.out.println("POST DATE: " + date);
    System.out.println("POST AUTH: " + auth);
    
    Builder request = target
      .request()
      .header("Content-Type", contentType)
      .header("Content-MD5", contentMd5)
      .header("Date", date)
      .header("Authorization", auth);
    return request.post(entity);
  }
  
  private String getMd5(String s) {
    if (s == null) {
      s = "";
    }
    return getMd5(s.getBytes());
  }

  private String getMd5(byte[] bytes) {
    try {
      return new String(Base64.getEncoder().encode(MessageDigest.getInstance("MD5").digest(bytes)));
    }
    catch (Exception e) {
      throw new IllegalStateException("MD5 not available");
    }
  }
  
  private String getAuthorizationHeader(String method, String contentMd5, String contentType, String date, String path) {
    String clientIdentifier = getClientIdentifier();
    if (clientIdentifier == null) {
      throw new IllegalStateException(String.format("%s not set", SETTINGKEY_CLIENTIDENTIFIER));
    }
    String secretKey = getSecretKey();
    if (secretKey == null) {
      throw new IllegalStateException(String.format("%s not set", SETTINGKEY_SECRETKEY));
    }
    String requestParts = String.format("%s\n%s\n%s\n%s\n%s", method, contentMd5, contentType, date, path);
    SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.getDecoder().decode(secretKey.getBytes()), HMAC_SHA512);
    try {
      Mac mac = Mac.getInstance(HMAC_SHA512);
      mac.init(secretKeySpec);
      String authPostfix = new String(Base64.getEncoder().encode(mac.doFinal(requestParts.getBytes())));
      StringBuilder sb = new StringBuilder();
      sb.append("Onnistuu ");
      sb.append(clientIdentifier);
      sb.append(":");
      sb.append(authPostfix);
      return sb.toString();
    }
    catch (Exception e) {
      throw new IllegalStateException(String.format("%s not available", HMAC_SHA512));
    }
  }

  private String getClientIdentifier() {
    SettingKeyDAO settingKeyDAO = DAOFactory.getInstance().getSettingKeyDAO();
    SettingKey settingKey = settingKeyDAO.findByName(SETTINGKEY_CLIENTIDENTIFIER);
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
    SettingKey settingKey = settingKeyDAO.findByName(SETTINGKEY_SECRETKEY);
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
    SettingKey settingKey = settingKeyDAO.findByName(SETTINGKEY_SIGNERID);
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
  private static final String SETTINGKEY_CLIENTIDENTIFIER = "applications.onnistuuClientIdentifier";
  private static final String SETTINGKEY_SECRETKEY = "applications.onnistuuSecretKey";
  private static final String SETTINGKEY_SIGNERID = "applications.defaultSignerId";
  private static final String HMAC_SHA512 = "HmacSHA512";
  private static final Logger logger = Logger.getLogger(OnnistuuClient.class.getName());

}
