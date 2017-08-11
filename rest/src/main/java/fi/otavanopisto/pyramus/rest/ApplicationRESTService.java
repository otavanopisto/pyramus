package fi.otavanopisto.pyramus.rest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.dao.base.LanguageDAO;
import fi.otavanopisto.pyramus.dao.base.MunicipalityDAO;
import fi.otavanopisto.pyramus.dao.base.NationalityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Language;
import fi.otavanopisto.pyramus.domainmodel.base.Municipality;
import fi.otavanopisto.pyramus.domainmodel.base.Nationality;
import fi.otavanopisto.pyramus.rest.annotation.Unsecure;
import net.sf.json.JSONObject;

@Path("/application")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateful
@RequestScoped
public class ApplicationRESTService extends AbstractRESTService {
  
  @Context
  private UriInfo uri;

  @Inject
  private MunicipalityDAO municipalityDAO;

  @Inject
  private NationalityDAO nationalityDAO;

  @Inject
  private LanguageDAO languageDAO;
  
  @Path("/createapplication")
  @POST
  @Unsecure
  public Response createApplication(Object object, @HeaderParam("Referer") String referer) {
    if (!isApplicationCall(referer, "application/create.page")) {
      return Response.status(Status.FORBIDDEN).build();
    }
    JSONObject applicationData = JSONObject.fromObject(object);
    //JSONObject basicInfo = JSONObject.fromObject(applicationData.get("basic-info"));
    System.out.println("-----------> " + applicationData.toString());
    return Response.noContent().build();
  }
  
  @Path("/municipalities")
  @GET
  @Unsecure
  public Response listMunicipalities() {
    List<Municipality> municipalities = municipalityDAO.listAll();
    municipalities.sort(new Comparator<Municipality>() {
      public int compare(Municipality o1, Municipality o2) {
        return o1.getName().compareTo(o2.getName());
      }
    });
    
    List<HashMap<String, String>> municipalityList = new ArrayList<HashMap<String, String>>();

    HashMap<String, String> municipalityData = new HashMap<String, String>();
    municipalityData.put("text", "Ei kotikuntaa Suomessa");
    municipalityData.put("value", "none");
    municipalityList.add(municipalityData);
    
    for (Municipality municipality : municipalities) {
      municipalityData = new HashMap<String, String>();
      municipalityData.put("text", municipality.getName());
      municipalityData.put("value", municipality.getId().toString());
      municipalityList.add(municipalityData);
    }
    
    return Response.ok(municipalityList).build();
  }

  @Path("/nationalities")
  @GET
  @Unsecure
  public Response listNationalities() {
    List<Nationality> nationalities = nationalityDAO.listAll();
    nationalities.sort(new Comparator<Nationality>() {
      public int compare(Nationality o1, Nationality o2) {
        return o1.getName().compareTo(o2.getName());
      }
    });
    
    List<HashMap<String, String>> nationalityList = new ArrayList<HashMap<String, String>>();

    for (Nationality nationality : nationalities) {
      HashMap<String, String> nationalityData = new HashMap<String, String>();
      nationalityData.put("text", nationality.getName());
      nationalityData.put("value", nationality.getId().toString());
      nationalityList.add(nationalityData);
    }
    
    return Response.ok(nationalityList).build();
  }

  @Path("/languages")
  @GET
  @Unsecure
  public Response listLanguages() {
    List<Language> languages = languageDAO.listAll();
    languages.sort(new Comparator<Language>() {
      public int compare(Language o1, Language o2) {
        return o1.getName().compareTo(o2.getName());
      }
    });
    
    List<HashMap<String, String>> languageList = new ArrayList<HashMap<String, String>>();

    for (Language language : languages) {
      HashMap<String, String> languageData = new HashMap<String, String>();
      languageData.put("text", language.getName());
      languageData.put("value", language.getId().toString());
      languageList.add(languageData);
    }
    
    return Response.ok(languageList).build();
  }
  
  private boolean isApplicationCall(String referer, String expectedPath) {
    String s = uri.getBaseUri().toString();
    s = s.substring(0, s.length() - 2); // JaxRsActivator path
    s += expectedPath;
    return StringUtils.equals(referer, s);
  }

}
