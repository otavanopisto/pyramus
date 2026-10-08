package fi.otavanopisto.pyramus.rest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import fi.otavanopisto.pyramus.atomi.AtomiReportType;
import fi.otavanopisto.pyramus.dao.reports.AtomiReportDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.reports.AtomiReport;
import fi.otavanopisto.pyramus.domainmodel.reports.Report;
import fi.otavanopisto.pyramus.domainmodel.reports.ReportFileFormat;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyEndReasonType;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.reports.FTLReportsController;
import fi.otavanopisto.pyramus.reports.FTLReportsController.ReportFormat;
import fi.otavanopisto.pyramus.rest.annotation.AuthScope;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit.Handling;
import fi.otavanopisto.pyramus.rest.controller.PersonController;
import fi.otavanopisto.pyramus.rest.controller.StudentController;
import fi.otavanopisto.pyramus.rest.controller.StudyProgrammeController;
import fi.otavanopisto.pyramus.rest.model.atomi.AtomiViittaus;
import fi.otavanopisto.pyramus.rest.model.atomi.KirjautunutKayttaja;
import fi.otavanopisto.pyramus.rest.model.atomi.Opiskelija;
import fi.otavanopisto.pyramus.rest.model.atomi.OpiskeluoikeudenTila;
import fi.otavanopisto.pyramus.rest.model.atomi.Opiskeluoikeus;
import fi.otavanopisto.pyramus.rest.model.atomi.Rooli;
import fi.otavanopisto.pyramus.security.impl.SessionController;
import fi.otavanopisto.pyramus.util.DateUtils;

@Path("/atomi")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
@AuthScope(AuthScope.ATOMI)
public class AtomiRESTService extends AbstractRESTService {

  private static final int MAX_OPISKELUOIKEUKSIA = 50;
  
  @Inject
  private Logger logger;
  
  @Inject
  private AtomiReportDAO atomiReportDAO;
  
  @Inject
  private PersonController personController;

  @Inject
  private StudentController studentController;

  @Inject
  private StudyProgrammeController studyProgrammeController;

  @Inject
  private SessionController sessionController;

  @Inject
  private FTLReportsController certificateReportsController;
  
  @Path("/kirjautunut")
  @GET
  @RESTPermit (handling = Handling.INLINE)
  public Response haeKirjautunut() {
    User loggedUser = sessionController.getUser();

    if (loggedUser.getPerson() == null) {
      return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }

    String opiskelijatunniste = null;
    Rooli rooli;
    
    if (loggedUser.hasAnyRole(Role.ADMINISTRATOR, Role.MANAGER, Role.STUDY_PROGRAMME_LEADER)) {
      rooli = Rooli.henkilökunta;
    }
    else if (loggedUser.hasRole(Role.STUDENT)) {
      rooli = Rooli.opiskelija;
      opiskelijatunniste = loggedUser.getPersonId().toString();
    }
    else {
      return Response.status(Status.FORBIDDEN).build();
    }

    KirjautunutKayttaja kirjautunutKayttaja = new KirjautunutKayttaja();
    kirjautunutKayttaja.setEtunimet(loggedUser.getFirstName());
    kirjautunutKayttaja.setSukunimi(loggedUser.getLastName());
    kirjautunutKayttaja.setRooli(rooli);
    kirjautunutKayttaja.setOpiskelijatunniste(opiskelijatunniste);
    
    return Response.ok().entity(kirjautunutKayttaja).build();
  }
  
  @Path("/opiskelijat/{TUNNISTE:[0-9]*}")
  @GET
  @RESTPermit (handling = Handling.INLINE)
  public Response haeOpiskelija(@PathParam("TUNNISTE") String opiskelijaTunniste) {
    Long personId = Long.parseLong(opiskelijaTunniste);
    Person person = personController.findPersonById(personId);

    if (person == null || person.getLatestStudent() == null) {
      return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }

    if (!hasPermission(person)) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok().entity(opiskelija(person.getLatestStudent())).build();
  }

  @Path("/opiskelijat/{TUNNISTE:[0-9]*}/opiskeluoikeudet")
  @GET
  @RESTPermit (handling = Handling.INLINE)
  public Response haeOpiskelijanOpiskeluoikeudet(@PathParam("TUNNISTE") String opiskelijaTunniste) {
    Long personId = Long.parseLong(opiskelijaTunniste);
    Person person = personController.findPersonById(personId);

    if (person == null || person.getLatestStudent() == null) {
      return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }

    if (!hasPermission(person)) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    // Listaa kaikki - varmaan mietittävä pitääkö rajata jotenkin
    List<Opiskeluoikeus> opiskeluoikeudet = person.getStudents().stream().map(student -> opiskeluoikeus(student)).toList();
    return Response.ok().entity(opiskeluoikeudet).build();
  }
  
  @Path("/opiskeluoikeudet")
  @GET
  @RESTPermit (handling = Handling.INLINE)
  public Response haeOpiskeluoikeuksia(
      @QueryParam("hetu") String hetu,
      @QueryParam("sukunimi") String sukunimi,
      @QueryParam("koulutusohjelma") String koulutusohjelmaTunniste,
      @QueryParam("valmalku") String valmistumisPvmAlkuStr,
      @QueryParam("valmloppu") String valmistumisPvmLoppuStr,
      @QueryParam("opiskeluoikeudet") String opiskeluoikeudetCDT
      ) {
    User loggedUser = sessionController.getUser();

    // Rajattu nyt näin. Jos tätä tulee tarvetta muuttaa, niin on varmaan 
    // katsottava opiskeluoikeuskohtaisesti, mitä saa nähdä.
    
    if (!loggedUser.hasAnyRole(Role.ADMINISTRATOR, Role.MANAGER, Role.STUDY_PROGRAMME_LEADER)) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    Collection<Organization> organizations = Arrays.asList(loggedUser.getOrganization());
    StudyProgramme studyProgramme = null;
    Set<Long> studentIds = null;
    Date graduationDateStart = null;
    Date graduationDateEnd = null;
    int firstResult = 0;
    int maxResults = MAX_OPISKELUOIKEUKSIA;

    if (StringUtils.isNotBlank(koulutusohjelmaTunniste)) {
      if (NumberUtils.isCreatable(koulutusohjelmaTunniste)) {
        Long studyProgrammeId = Long.parseLong(koulutusohjelmaTunniste);
        studyProgramme = studyProgrammeController.findStudyProgrammeById(studyProgrammeId);
        if (studyProgramme == null) {
          return Response.status(Status.BAD_REQUEST).build();
        }
      }
      else {
        return Response.status(Status.BAD_REQUEST).build();
      }
    }
    
    if (StringUtils.isNotBlank(opiskeluoikeudetCDT)) {
      try {
        studentIds = Arrays.stream(opiskeluoikeudetCDT.split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toSet());
        
        if (studentIds.size() > MAX_OPISKELUOIKEUKSIA) {
          return Response.status(Status.BAD_REQUEST).build();
        }
      } catch (Exception e) {
        return Response.status(Status.BAD_REQUEST).build();
      }
    }
    
    if (StringUtils.isNotBlank(valmistumisPvmAlkuStr)) {
      try {
        LocalDate valmistumisPvmAlku = LocalDate.parse(valmistumisPvmAlkuStr);
        graduationDateStart = DateUtils.toDate(valmistumisPvmAlku);
      } catch (Exception e) {
        return Response.status(Status.BAD_REQUEST).build();
      }
    }

    if (StringUtils.isNotBlank(valmistumisPvmLoppuStr)) {
      try {
        LocalDate valmistumisPvmLoppu = LocalDate.parse(valmistumisPvmLoppuStr);
        graduationDateEnd = DateUtils.toDate(valmistumisPvmLoppu);
      } catch (Exception e) {
        return Response.status(Status.BAD_REQUEST).build();
      }
    }

    List<Student> results = studentController.atomiSearch(organizations, hetu, sukunimi, studyProgramme, studentIds, graduationDateStart, graduationDateEnd, firstResult, maxResults);
    List<Opiskeluoikeus> opiskeluoikeudet = results.stream().map(student -> opiskeluoikeus(student)).toList();
    return Response.ok().entity(opiskeluoikeudet).build();
  }
  
  @Path("/opiskeluoikeudet/{TUNNISTE:[0-9]*}")
  @GET
  @RESTPermit (handling = Handling.INLINE)
  public Response haeOpiskeluoikeusTunnisteella(@PathParam("TUNNISTE") String opiskeluoikeudenTunniste) {
    Long studentId = Long.parseLong(opiskeluoikeudenTunniste);
    Student student = studentController.findStudentById(studentId);

    if (student == null || student.getPerson() == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!hasPermission(student.getPerson())) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok().entity(opiskeluoikeus(student)).build();
  }

  @Path("/opiskeluoikeudet/{TUNNISTE:[0-9]*}/dokumentit/{REPORTTYPE}")
  @GET
  @RESTPermit (handling = Handling.INLINE)
  public Response haeDokumentti(@PathParam("TUNNISTE") String opiskeluoikeudenTunniste, @PathParam("REPORTTYPE") AtomiReportType reportType,
      @QueryParam("format") @DefaultValue("pdf") String formatArg) {
    
    try {
      Long studentId = Long.parseLong(opiskeluoikeudenTunniste);
      Student student = studentController.findStudentById(studentId);
      AtomiReport atomiReport = atomiReportDAO.findByType(reportType);
      Report report = atomiReport != null ? atomiReport.getReport() : null;
  
      if (student == null || student.getPerson() == null || report == null || report.getFormat() != ReportFileFormat.FTL) {
        return Response.status(Status.NOT_FOUND).build();
      }
  
      if (!hasPermission(student.getPerson())) {
        return Response.status(Status.NOT_FOUND).build();
      }
  
      formatArg = StringUtils.upperCase(formatArg);
      ReportFormat format = EnumUtils.getEnum(ReportFormat.class, formatArg);
      
      if (format == null) {
        return Response.status(Status.BAD_REQUEST).build();
      }
    
      return Response.ok(certificateReportsController.renderFTLStudentReport(report, student, format)).type(format.getContentType()).build();
    } catch (Exception e) {
      logger.log(Level.SEVERE, "Atomi report generation failed", e);
      
      return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }
  }

  private boolean hasPermission(Person studentPerson) {
    if (!sessionController.isLoggedIn()) {
      return false;
    }
    
    if (sessionController.getUser().hasAnyRole(Role.ADMINISTRATOR, Role.MANAGER, Role.STUDY_PROGRAMME_LEADER)) {
      return true;
    }
    
    if (Objects.equals(sessionController.getUser().getPersonId(), studentPerson.getId())) {
      return true;
    }
    
    return false;
  }

  /**
   * Palauttaa opiskelijan kuvauksen Studentille.
   * 
   * @param student
   * @return
   */
  private Opiskelija opiskelija(Student student) {
    Opiskelija opiskelija = new Opiskelija();
    opiskelija.setTunniste(student.getPersonId().toString());
    opiskelija.setKäyttäjätunnus(student.getPersonId().toString());
    opiskelija.setOpiskelijanumero(student.getPersonId().toString());
    opiskelija.setSyntymäaika(DateUtils.toLocalDate(student.getPerson().getBirthday()));
    opiskelija.setEtunimet(student.getFirstName());
    opiskelija.setSukunimi(student.getLastName());
    opiskelija.setEmail(student.getPrimaryEmail() != null ? student.getPrimaryEmail().getAddress() : null);
    return opiskelija;
  }

  /**
   * Palauttaa opiskeluoikeuden kuvauksen Studentille.
   * 
   * @param student
   * @return
   */
  private Opiskeluoikeus opiskeluoikeus(Student student) {
    Opiskeluoikeus opiskeluoikeus = new Opiskeluoikeus();
    
    OpiskeluoikeudenTila tila = OpiskeluoikeudenTila.läsnä;

    if (student.getStudyEndDate() != null) {
      // Jos päättymispäivä on merkitty, veikataan että opiskelija on eronnut ja jos 
      // päättymissyyn tyyppi indikoi opintojen valmistumista niin tilaksi valmistunut
      tila = OpiskeluoikeudenTila.eronnut;
      
      if (student.getStudyEndReason() != null && student.getStudyEndReason().getType() == StudentStudyEndReasonType.GRADUATED) {
        tila = OpiskeluoikeudenTila.valmistunut;
      }
    }

    switch (tila) {
      case läsnä:
        // Arvioitu opinto-oikeuden päättymispäivämäärä jos opinnot on kesken
        if (student.getStudyTimeEnd() != null) {
          opiskeluoikeus.setPäättymispäivämäärä(DateUtils.toLocalDate(student.getStudyTimeEnd()));
        }
      break;
      
      case eronnut:
        opiskeluoikeus.setEropäivämäärä(DateUtils.toLocalDate(student.getStudyEndDate()));
      break;

      case valmistunut:
        opiskeluoikeus.setValmistumispäivämäärä(DateUtils.toLocalDate(student.getStudyEndDate()));
      break;
    }

    Opiskelija opiskelija = opiskelija(student);
    Set<AtomiReportType> dokumentit = getAvailableReportTypes();

    opiskeluoikeus.setTunniste(student.getId().toString());
    opiskeluoikeus.setAloituspäivämäärä(DateUtils.toLocalDate(student.getStudyStartDate()));
    opiskeluoikeus.setTila(tila);
    opiskeluoikeus.setOpiskelija(opiskelija);
    opiskeluoikeus.setSaatavillaOlevatDokumentit(dokumentit);

    if (student.getStudyProgramme() != null) {
      AtomiViittaus koulutusohjelma = new AtomiViittaus();
      koulutusohjelma.setTunniste(student.getStudyProgramme().getId().toString());
      koulutusohjelma.setNimi(student.getStudyProgramme().getName());
      opiskeluoikeus.setKoulutusohjelma(koulutusohjelma);
    }
    
    return opiskeluoikeus;
  }
  
  /**
   * Palauttaa saatavilla olevat dokumenttityypit.
   * 
   * @return
   */
  private Set<AtomiReportType> getAvailableReportTypes() {
    List<AtomiReport> atomiReports = atomiReportDAO.listAll();
    Set<AtomiReportType> types = new HashSet<>();
    for (AtomiReport report : atomiReports) {
      if (report.getReport() != null) {
        types.add(report.getType());
      }
    }
    return types;
  }

}