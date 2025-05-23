package fi.otavanopisto.pyramus.json.applications;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.system.SettingDAO;
import fi.otavanopisto.pyramus.dao.system.SettingKeyDAO;
import fi.otavanopisto.pyramus.domainmodel.system.Setting;
import fi.otavanopisto.pyramus.domainmodel.system.SettingKey;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class OnnistuuClient {

  public static OnnistuuClient getInstance() {
    return INSTANCE;
  }

  public String createDocument(String name) throws OnnistuuClientException {

    // Payload

    JSONObject document = new JSONObject();
    document.put("name", name);
    JSONObject payload = new JSONObject();
    payload.put("document", document);
    String json = payload.toString();

    // Call

    Entity<String> entity = Entity.entity(json, MediaType.APPLICATION_JSON);
    String contentMd5 = getMd5(json);
    Response response = doPost(
        "/api/v1/document/",
        contentMd5,
        MediaType.APPLICATION_JSON,
        entity);

    // Validation

    if (response.getStatus() == 201) {
      String location = response.getHeaderString("Location");
      String documentId = StringUtils.substringAfterLast(location, "/");
      if (StringUtils.length(documentId) != 36) { // should be uuid
        logger.severe(String.format("Invalid Onnistuu document uuid %s parsed from location %s", documentId, location));
        throw new OnnistuuClientException("Virheellinen dokumenttitunniste");
      }
      return documentId;
    }
    else {
      int status = response.getStatus();
      String reason = response.getStatusInfo().getReasonPhrase();
      logger.severe(String.format("Onnistuu response %d: %s", status, reason));
      throw new OnnistuuClientException(String.format("Dokumentin luonti epäonnistui (%d: %s)", status, reason));
    }
  }
  
  public byte[] getDocument(String documentId) throws OnnistuuClientException {
    Response response = doGet(String.format("/api/v1/document/%s/files/0", documentId));
    if (response.getStatus() == 200) {
      InputStream is = response.readEntity(InputStream.class);
      try {
        return IOUtils.toByteArray(is);
      }
      catch (IOException e) {
        throw new OnnistuuClientException(String.format("Dokumentin lataus epäonnistui (%s)", e.getMessage()));
      }
    }
    throw new OnnistuuClientException("Dokumentin lataus epäonnistui (404)");
  }
  
  public String getSignatureUrl(String invitationId, String returnUrl, String ssn, String authService) throws OnnistuuClientException {

    // Payload

    JSONObject payload = new JSONObject();
    payload.put("returnUrl", returnUrl);
    payload.put("identifier", ssn);
    payload.put("authService", authService);
    String json = payload.toString();
    
    // #1174: Since the document might already have been signed (timeout while waiting for Visma Sign API to confirm it),
    // check the invitation status before trying to sign, which would fail due to an existing signature...
    
    if (isSigned(invitationId)) {
      return returnUrl;
    }
    
    // Call

    Entity<String> entity = Entity.entity(json, MediaType.APPLICATION_JSON);
    String contentMd5 = getMd5(json);
    Response response = doPost(
        String.format("/api/v1/invitation/%s/signature", invitationId),
        contentMd5,
        MediaType.APPLICATION_JSON,
        entity);
    
    // Validation
    
    if (response.getStatus() == 201) {
      return response.getHeaderString("Location");
    }
    else {
      int status = response.getStatus();
      String reason = response.getStatusInfo().getReasonPhrase();
      logger.severe(String.format("Onnistuu response %d: %s", status, reason));
      throw new OnnistuuClientException(String.format("Dokumentin allekirjoittaminen epäonnistui (%d: %s)", status, reason));
    }
  }

  public void addPdf(String document, byte[] pdf) throws OnnistuuClientException {
    Entity<byte[]> entity = Entity.entity(pdf, "application/pdf");
    String contentMd5 = getMd5(pdf);
    Response response = doPost(
        String.format("/api/v1/document/%s/files", document),
        contentMd5,
        "application/pdf",
        entity);
    if (response.getStatus() != 201) {
      int status = response.getStatus();
      String reason = response.getStatusInfo().getReasonPhrase();
      logger.severe(String.format("Onnistuu response %d: %s", status, reason));
      throw new OnnistuuClientException(
          String.format("PDF-dokumentin lähettäminen epäonnistui (%d: %s)", status, reason));
    }
  }

  public OnnistuuClient.Invitation createInvitation(String document, String email) throws OnnistuuClientException {
    
    // Payload
    
    JSONArray payload = new JSONArray();
    JSONObject invitation = new JSONObject();
    invitation.put("email",  email);
    JSONObject messages = new JSONObject();
    messages.put("send_invitation_email", false);
    messages.put("send_invitation_sms", false);
    messages.put("send_invitee_all_collected_email", false);
    messages.put("send_inviter_one_collected_emails", false);
    invitation.put("messages", messages);
    payload.add(invitation);
    String json = payload.toString();

    // Call
    
    Entity<String> entity = Entity.entity(json, MediaType.APPLICATION_JSON);
    String contentMd5 = getMd5(json);
    Response response = doPost(
        String.format("/api/v1/document/%s/invitations", document),
        contentMd5,
        MediaType.APPLICATION_JSON,
        entity);
    
    // Response
    
    if (response.getStatus() == 201) {
      String jsonData = response.readEntity(String.class);
      JSONArray jsonArray = JSONArray.fromObject(jsonData);
      JSONObject createdInvitation = jsonArray.getJSONObject(0);
      String uuid = createdInvitation.getString("uuid");
      String passphrase = createdInvitation.getString("passphrase");
      return new OnnistuuClient.Invitation(uuid, passphrase);
    }
    else {
      int status = response.getStatus();
      String reason = response.getStatusInfo().getReasonPhrase();
      logger.severe(String.format("Onnistuu response %d: %s", status, reason));
      throw new OnnistuuClientException(
          String.format("Dokumenttikutsun luonti epäonnistui (%d: %s)", status, reason));
    }
  }
  
  public boolean isSigned(String invitationId) throws OnnistuuClientException {
    Response response = doGet(String.format("/api/v1/invitation/%s", invitationId));
    if (response.getStatus() == 200) {
      String jsonData = response.readEntity(String.class);
      JSONObject jsonObject = JSONObject.fromObject(jsonData);
      return StringUtils.equals(jsonObject.getString("status"), "signed");
    }
    return false;
  }

  public JSONObject listSignatureSources() throws OnnistuuClientException {
    Response response = doGet("/api/v1/auth/methods");
    String jsonData = response.readEntity(String.class);
    return response.getStatus() == 200 ? JSONObject.fromObject(jsonData) : null;
  }

  private Response doGet(String path) throws OnnistuuClientException {
    String contentMd5 = getMd5("");
    String date = dateToRFC2822(new Date());
    String auth = getAuthorizationHeader("GET", contentMd5, MediaType.APPLICATION_JSON, date, path);
    Client client = ClientBuilder.newClient();
    WebTarget target = client.target(String.format("https://sign.visma.net%s", path));
    Builder request = target.request()
        .header("Content-Type", MediaType.APPLICATION_JSON)
        .header("Content-MD5", contentMd5)
        .header("Date", date)
        .header("Authorization", auth);
    return request.get();
  }

  @SuppressWarnings("rawtypes")
  private Response doPost(String path, String contentMd5, String contentType, Entity entity)
      throws OnnistuuClientException {
    String date = dateToRFC2822(new Date());
    String auth = getAuthorizationHeader("POST", contentMd5, contentType, date, path);
    Client client = ClientBuilder.newClient();
    WebTarget target = client.target(String.format("https://sign.visma.net%s", path));
    Builder request = target.request()
        .header("Content-Type", contentType)
        .header("Content-MD5", contentMd5)
        .header("Date", date)
        .header("Authorization", auth);
    return request.post(entity);
  }

  private String getMd5(String s) throws OnnistuuClientException {
    try {
      if (s == null) {
        s = "";
      }
      return getMd5(s.getBytes("UTF-8"));
    }
    catch (Exception e) {
      throw new OnnistuuClientException(e.getMessage(), e);
    }
  }

  private String getMd5(byte[] bytes) throws OnnistuuClientException {
    try {
      return new String(Base64.getEncoder().encode(MessageDigest.getInstance("MD5").digest(bytes)));
    }
    catch (Exception e) {
      throw new OnnistuuClientException(e.getMessage(), e);
    }
  }

  private String getAuthorizationHeader(String method, String contentMd5, String contentType, String date, String path)
      throws OnnistuuClientException {
    String clientIdentifier = getClientIdentifier();
    if (clientIdentifier == null) {
      throw new OnnistuuClientException(String.format("Asetus %s puuttuu", SETTINGKEY_CLIENTIDENTIFIER));
    }
    String secretKey = getSecretKey();
    if (secretKey == null) {
      throw new OnnistuuClientException(String.format("Asetus %s puuttuu", SETTINGKEY_SECRETKEY));
    }
    String requestParts = String.format("%s\n%s\n%s\n%s\n%s", method, contentMd5, contentType, date, path);
    SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.getDecoder().decode(secretKey.getBytes()), HMAC_SHA512);
    try {
      Mac mac = Mac.getInstance(HMAC_SHA512);
      mac.init(secretKeySpec);
      String authPostfix = Base64.getEncoder().encodeToString(mac.doFinal(requestParts.getBytes("UTF-8")));
      StringBuilder sb = new StringBuilder();
      sb.append("Onnistuu ");
      sb.append(clientIdentifier);
      sb.append(":");
      sb.append(authPostfix);
      return sb.toString();
    }
    catch (Exception e) {
      throw new OnnistuuClientException(e.getMessage(), e);
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

  private String dateToRFC2822(Date date) {
    return new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH).format(date);
  }

  private static final OnnistuuClient INSTANCE = new OnnistuuClient();
  private static final String SETTINGKEY_CLIENTIDENTIFIER = "applications.onnistuuClientIdentifier";
  private static final String SETTINGKEY_SECRETKEY = "applications.onnistuuSecretKey";
  private static final String HMAC_SHA512 = "HmacSHA512";
  private static final Logger logger = Logger.getLogger(OnnistuuClient.class.getName());

  public class Invitation {
    public Invitation(String uuid, String passphrase) {
      this.uuid = uuid;
      this.passphrase = passphrase;
    }

    public String getUuid() {
      return uuid;
    }

    public String getPassphrase() {
      return passphrase;
    }

    String uuid;
    String passphrase;
  }

}
