package fi.otavanopisto.pyramus.views.applications;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.application.ApplicationSignaturesDAO;
import fi.otavanopisto.pyramus.dao.base.LanguageDAO;
import fi.otavanopisto.pyramus.dao.base.MunicipalityDAO;
import fi.otavanopisto.pyramus.dao.base.NationalityDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationSignatures;
import fi.otavanopisto.pyramus.domainmodel.base.Language;
import fi.otavanopisto.pyramus.domainmodel.base.Municipality;
import fi.otavanopisto.pyramus.domainmodel.base.Nationality;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ViewApplicationViewController extends PyramusViewController {

  private static final Logger logger = Logger.getLogger(EditApplicationViewController.class.getName());

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }
  
  public void process(PageRequestContext pageRequestContext) {
    try {

      StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
      StaffMember staffMember = staffMemberDAO.findById(pageRequestContext.getLoggedUserId());

      Long applicationId = NumberUtils.createLong(pageRequestContext.getRequest().getParameter("application"));
      if (applicationId == null) {
        pageRequestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
      ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
      Application application = applicationDAO.findById(applicationId);
      if (application == null) {
        pageRequestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
      ApplicationSignaturesDAO applicationSignaturesDAO = DAOFactory.getInstance().getApplicationSignaturesDAO();
      ApplicationSignatures signatures = applicationSignaturesDAO.findByApplication(application);
      
      JSONObject formData = JSONObject.fromObject(application.getFormData());

      Map<String, Map<String, String>> sections = new LinkedHashMap<>();
      
      // Perustiedot
      
      Map<String, String> fields = new LinkedHashMap<>();
      sections.put("Perustiedot", fields);
      
      fields.put("Linja", ApplicationUtils.applicationLineUiValue(getFormValue(formData, "field-line")));
      fields.put("Nimi", String.format("%s, %s", getFormValue(formData, "field-last-name"), getFormValue(formData, "field-first-names")));
      if (StringUtils.isNotBlank(getFormValue(formData, "field-nickname"))) {
        fields.put("Kutsumanimi", getFormValue(formData, "field-nickname"));
      }
      fields.put("Syntymäaika", getFormValue(formData, "field-birthday"));
      if (StringUtils.isNotBlank(getFormValue(formData, "field-ssn-end"))) {
        fields.put("Henkilötunnuksen loppuosa", StringUtils.upperCase(getFormValue(formData, "field-ssn-end"))); 
      }
      fields.put("Sukupuoli", genderUiValue(getFormValue(formData, "field-sex")));
      fields.put("Osoite", String.format("%s\n%s %s\n%s",
          getFormValue(formData, "field-street-address"),
          getFormValue(formData, "field-zip-code"),
          getFormValue(formData, "field-city"),
          getFormValue(formData, "field-country")));
      fields.put("Kotikunta", municipalityUiValue(getFormValue(formData, "field-municipality")));
      fields.put("Kansallisuus", nationalityUiValue(getFormValue(formData, "field-nationality")));
      fields.put("Äidinkieli", languageUiValue(getFormValue(formData, "field-language")));
      fields.put("Puhelinnumero", getFormValue(formData, "field-phone"));
      fields.put("Sähköposti", getFormValue(formData, "field-email"));

      // Alaikäisen hakemustiedot
      
      if (StringUtils.isNotBlank(getFormValue(formData, "field-underage-first-name"))) {
        fields = new LinkedHashMap<>();
        sections.put("Alaikäisen hakemustiedot", fields);
        if (StringUtils.isNotBlank(getFormValue(formData, "field-underage-grounds"))) {
          fields.put("Hakemusperusteet", getFormValue(formData, "field-underage-grounds"));
        }
        fields.put("Huoltajan yhteystiedot", String.format("%s %s\n%s\n%s %s\n%s\n%s\n%s",
          getFormValue(formData, "field-underage-first-name"),
          getFormValue(formData, "field-underage-last-name"),
          getFormValue(formData, "field-underage-street-address"),
          getFormValue(formData, "field-underage-zip-code"),
          getFormValue(formData, "field-underage-city"),
          getFormValue(formData, "field-underage-country"),
          "Puh: " + getFormValue(formData, "field-underage-phone"),
          "Sähköposti: " + getFormValue(formData, "field-underage-email")));
      }

      // Hakemiseen vaadittavat lisätiedot
      
      fields = new LinkedHashMap<>();
      sections.put("Hakemiseen vaadittavat lisätiedot", fields);
      
      if (StringUtils.isNotBlank(getFormValue(formData, "field-previous-studies"))) {
        fields.put("Aiemmat opinnot", getFormValue(formData, "field-previous-studies"));
      }
      fields.put("Opiskelee toisessa oppilaitoksessa", simpleBooleanUiValue(getFormValue(formData, "field-other-school")));
      if (StringUtils.isNotBlank(getFormValue(formData, "field-other-school-name"))) {
        fields.put("Oppilaitos", getFormValue(formData, "field-other-school-name"));
      }
      if (StringUtils.isNotBlank(getFormValue(formData, "field-goals"))) {
        fields.put("Opiskelutavoitteet", goalsUiValue(getFormValue(formData, "field-goals")));
      }
      if (StringUtils.isNotBlank(getFormValue(formData, "field-foreign-student"))) {
        fields.put("Ulkomainen vaihto-opiskelija", simpleBooleanUiValue(getFormValue(formData, "field-foreign-student")));
      }
      if (StringUtils.isNotBlank(getFormValue(formData, "field-previous-foreign-studies"))) {
        fields.put("Aiemmat opinnot kotimaassa ja Suomessa", getFormValue(formData, "field-previous-foreign-studies"));
      }
      if (StringUtils.isNotBlank(getFormValue(formData, "field-job"))) {
        if ("muu".equals(getFormValue(formData, "field-job"))) {
          fields.put("Asema", getFormValue(formData, "field-job-other"));
        }
        else {
          fields.put("Asema", jobUiValue(getFormValue(formData, "field-job")));
        }
      }
      if (StringUtils.isNotBlank(getFormValue(formData, "field-foreign-line"))) {
        fields.put("Opintojen tyyppi", foreignLineUiValue(getFormValue(formData, "field-foreign-line")));
      }
      if (StringUtils.isNotBlank(getFormValue(formData, "field-residence-permit"))) {
        fields.put("Oleskelulupa Suomeen", simpleBooleanUiValue(getFormValue(formData, "field-residence-permit")));
      }
      if (StringUtils.isNotBlank(getFormValue(formData, "field-info"))) {
        fields.put("Vapaamuotoinen esittely", getFormValue(formData, "field-info"));
      }
      if (StringUtils.isNotBlank(getFormValue(formData, "field-lodging"))) {
        fields.put("Tarvitsee asunnon kampukselta", "Kyllä");
      }
      
      // Hakulähde
      
      StringBuffer sb = new StringBuffer();
      if (formData.has("field-source")) {
        String source = getFormValue(formData, "field-source");
        if (!StringUtils.startsWith(source, "[")) {
          source = String.format("[\"%s\"]", source);
        }
        JSONArray sourcesArray = JSONArray.fromObject(source);
        for (int i = 0; i < sourcesArray.size(); i++) {
          if (sb.length() > 0) {
            sb.append("\n");
          }
          sb.append(sourceUiValue(sourcesArray.getString(i)));
        }
      }
      if (StringUtils.isNotBlank(getFormValue(formData, "field-source-other"))) {
        if (sb.length() > 0) {
          sb.append("\n");
        }
        sb.append(getFormValue(formData, "field-source-other"));
      }
      if (sb.length() > 0) {
        fields.put("Mistä sai tiedon koulutuksesta", sb.toString());
      }
      
      // Hakemuksen tilatiedot
      
      pageRequestContext.getRequest().setAttribute("infoState", application.getState());
      pageRequestContext.getRequest().setAttribute("infoStateUi", ApplicationUtils.applicationStateUiValue(application.getState()));
      pageRequestContext.getRequest().setAttribute("infoApplicantEditable", application.getApplicantEditable());
      if (application.getHandler() != null) {
        pageRequestContext.getRequest().setAttribute("infoHandler", application.getHandler().getFullName());
        pageRequestContext.getRequest().setAttribute("infoHandlerId", application.getHandler().getId());
      }
      pageRequestContext.getRequest().setAttribute("infoCreated", application.getCreated());
      pageRequestContext.getRequest().setAttribute("infoLastModified", ApplicationUtils.getLatest(
          application.getLastModified(),
          application.getApplicantLastModified(),
          application.getCreated()));
      pageRequestContext.getRequest().setAttribute("infoSignatures", signatures);
      pageRequestContext.getRequest().setAttribute("infoSsn", staffMember == null ? null : staffMember.getPerson().getSocialSecurityNumber());
      
      pageRequestContext.getRequest().setAttribute("mode", "view");
      pageRequestContext.getRequest().setAttribute("applicationEntityId", application.getId());      
      pageRequestContext.getRequest().setAttribute("applicationId", application.getApplicationId());      
      pageRequestContext.getRequest().setAttribute("applicationLine", application.getLine());      
      pageRequestContext.getRequest().setAttribute("sections", sections);      
      
      pageRequestContext.setIncludeJSP("/templates/applications/management-view-application.jsp");
    }
    catch (IOException e) {
      logger.log(Level.SEVERE, "Unable to serve error response", e);
      return;
    }
  }
  
  private String getFormValue(JSONObject object, String key) {
    return object.has(key) ? object.getString(key) : null;
  }
  
  private String municipalityUiValue(String value) {
    if (StringUtils.isBlank(value)) {
      return null;
    }
    else if (value.equals("none")) {
      return "Ei kotikuntaa Suomessa";
    }
    Long municipalityId = Long.valueOf(value);
    MunicipalityDAO municipalityDAO = DAOFactory.getInstance().getMunicipalityDAO();
    Municipality municipality = municipalityDAO.findById(municipalityId);
    return municipality == null ? null : municipality.getName();
  }

  private String nationalityUiValue(String value) {
    if (StringUtils.isBlank(value)) {
      return null;
    }
    Long nationalityId = Long.valueOf(value);
    NationalityDAO nationalityDAO = DAOFactory.getInstance().getNationalityDAO();
    Nationality nationality = nationalityDAO.findById(nationalityId);
    return nationality == null ? null : nationality.getName();
  }

  private String languageUiValue(String value) {
    if (StringUtils.isBlank(value)) {
      return null;
    }
    Long languageId = Long.valueOf(value);
    LanguageDAO languageDAO = DAOFactory.getInstance().getLanguageDAO();
    Language language = languageDAO.findById(languageId);
    return language == null ? null : language.getName();
  }
  
  private String genderUiValue(String value) {
    switch (value) {
    case "mies":
      return "Mies";
    case "nainen":
      return "Nainen";
    case "muu":
      return "Muu";
    default:
      return null;
    }
  }

  private String sourceUiValue(String value) {
    switch (value) {
    case "tuttu":
      return "Ennestään tuttu";
    case "google":
      return "Google";
    case "facebook":
      return "Facebook";
    case "instagram":
      return "Instagram";
    case "sanomalehti":
      return "Sanomalehti";
    case "tienvarsimainos":
      return "Tienvarsimainos";
    case "valotaulumainos":
      return "Valotaulumainos";
    case "elokuva":
      return "Elokuva- tai TV-mainos";
    case "radio":
      return "Radio";
    case "tuttava":
      return "Kuulin kaverilta, tuttavalta, tms.";
    case "te-toimisto":
      return "TE-toimisto";
    case "messut":
      return "Messut";
    case "nuorisotyo":
      return "Nuorisotyö";
    case "opot":
      return "Opot";
    case "muu":
      return "Muu";
    default:
      return null;
    }
  }
  
  
  private String foreignLineUiValue(String value) {
    switch (value) {
    case "apa":
      return "Aikuisten perusopetuksen alkuvaiheen koulutus";
    case "pk":
      return "Monikulttuurinen peruskoululinja (Aikuisten perusopetuksen päättövaihe)";
    case "luva":
      return "LUVA eli lukioon valmentava koulutus maahanmuuttajille";
    default:
      return null;
    }
  }
  
  private String jobUiValue(String value) {
    switch (value) {
    case "tyollinen":
      return "Työllinen";
    case "tyoton":
      return "Työtön";
    case "opiskelija":
      return "Opiskelija";
    case "elakelainen":
      return "Eläkeläinen";
    case "muu":
      return "Muu";
    default:
      return null;
    }
  }

  private String simpleBooleanUiValue(String value) {
    switch (value) {
    case "kylla":
      return "Kyllä";
    case "ei":
    case "en":
      return "Ei";
    default:
      return null;
    }
  }

  private String goalsUiValue(String value) {
    switch (value) {
    case "lukio":
      return "Lukion päättötodistus";
    case "yo":
      return "YO-tutkinto";
    case "molemmat":
      return "Lukion päättötodistus ja YO-tutkinto";
    default:
      return null;
    }
  }

}
